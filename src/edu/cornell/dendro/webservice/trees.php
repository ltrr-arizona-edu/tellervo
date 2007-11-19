<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
header('Content-Type: application/xhtml+xml; charset=utf-8');

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/tree.php");
require_once("inc/subSite.php");
require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new treeRequest($myMetaHeader, $myAuth);

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}

// **************
// GET PARAMETERS
// **************

if(isset($_POST['xmlrequest']))
{
    // Extract parameters from XML request POST
    $myRequest->getXMLParams();
}
else
{
    // Extract parameters from get request and ensure no SQL has been injected
    $myRequest->getGetParams();
}

// ****************
// CHECK PARAMETERS 
// ****************
switch($myRequest->mode)
{
    case "read":
        $myMetaHeader->setRequestType("read");
        if($myAuth->isLoggedIn())
        {
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }
    
    case "update":
        $myMetaHeader->setRequestType("update");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->id == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(($myRequest->taxonid==NULL) && ($myRequest->label==NULL) && ($myRequest->subsiteid==NULL) && ($myRequest->latitude==NULL) && ($myRequest->longitude==NULL) && ($myRequest->precision==NULL))                 $myMetaHeader->setMessage("902", "Missing parameters - you haven't specified any parameters to update.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "delete":
        $myMetaHeader->setRequestType("delete");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->id == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "create":
        $myMetaHeader->setRequestType("create");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->label == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'label' field is required.");
            if($myRequest->taxonid == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'taxonid' field is required.");
            if($myRequest->subsiteid == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'subsiteid' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "failed":
        $myMetaHeader->setRequestType("help");
        break;

    default:
        $myMetaHeader->setRequestType("help");
        // Output the resulting XML
        $xmldata ="Details of how to use this web service will be added here later!";
        writeHelpOutput($myMetaHeader,$xmldata);
        die;
}

// *************
// PERFORM QUERY
// *************

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create tree object 
    $myTree = new tree();
    $parentTagBegin = $myTree->getParentTagBegin();
    $parentTagEnd = $myTree->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $myTree->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->label)) $myTree->setLabel($myRequest->label);
        if (!($myRequest->latitude)==NULL) $myTree->setLatitude($myRequest->latitude);
        if (!($myRequest->longitude)==NULL) $myTree->setLongitude($myRequest->longitude);
        if (!($myRequest->precision)==NULL) $myTree->setPrecision($myRequest->precision);
        if (!($myRequest->taxonid)==NULL) $myTree->setTaxonID($myRequest->taxonid);
        if (!($myRequest->subsiteid)==NULL) $myTree->setSubSiteID($myRequest->subsiteid);
        
        if( (($myRequest->mode=='update') && ($myAuth->treePermission($myRequest->id, "update")))  || 
            (($myRequest->mode=='create') && ($myAuth->treePermission($myRequest->id, "create")))    )
        {
            // Check user has permission to update / create tree before writing object to database
            $success = $myTree->writeToDB();
            if($success)
            {
                $xmldata=$myTree->asXML();
            }
            else
            {
               $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
            }
        }  
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on treeid $myRequest->id");
        }
    }

    // Delete record from db if requested
    if($myRequest->mode=='delete')
    {
        if($myAuth->treePermission($myRequest->id, "delete"))
        {
            // Check user has permission to delete record before performing statement
            $success = $myTree->deleteFromDB();
            if($success)
            {
                $xmldata=$myTree->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on treeid $myRequest->id");
        }
    }

    if($myRequest->mode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if(!$myRequest->id==NULL)
            {
                $sql="select tbltree.*, tblsubsite.subsiteid, tblsubsite.siteid from tbltree, tblsubsite  where treeid=$myRequest->id and tbltree.subsiteid=tblsubsite.subsiteid order by tbltree.treeid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read tree
                    if($myAuth->treePermission($row['treeid'], "read"))
                    {
                        $myTree = new tree();
                        $success = $myTree->setParamsFromDB($row['treeid']);
                        $success2 = $myTree->setChildParamsFromDB();
                        $mySubSite = new subSite();
                        $success3 = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $mySite = new site();
                        $success4 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 && success4 )
                        {
                            $xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML("begin");
                            $xmldata.= $myTree->asXML();
                            $xmldata.= $mySubSite->asXML("end");
                            $xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on treeid ".$row['treeid'], "Warning");
                    }
                }
            }
            else
            {
                $xmldata.= $parentTagBegin."\n";
                $sql="select * from tbltree order by treeid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read tree
                    if($myAuth->treePermission($row['treeid'], "read"))
                    {
                        $myTree = new tree();
                        $success = $myTree->setParamsFromDB($row['treeid']);
                        $success2 = $myTree->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.=$myTree->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on treeid ".$row['treeid'], "Warning");
                    }
                }
                $xmldata.= $parentTagEnd."\n";
            }
        }
        else
        {
            // Connection bad
            $myMetaHeader->setMessage("001", "Error connecting to database");
        }
    }
}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata);
