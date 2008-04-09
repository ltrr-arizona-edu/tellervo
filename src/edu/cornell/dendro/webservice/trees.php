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
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/tree.php");
require_once("inc/subSite.php");
require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
            if(($myRequest->taxonid==NULL) && ($myRequest->name==NULL) && ($myRequest->subsiteid==NULL) && ($myRequest->latitude==NULL) && ($myRequest->longitude==NULL) && ($myRequest->precision==NULL))                 trigger_error("902"."Missing parameters - you haven't specified any parameters to update.", E_USER_ERROR);
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
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
            if($myRequest->name == NULL) trigger_error("902"."Missing parameter - 'name' field is required.", E_USER_ERROR);
            if($myRequest->taxonid == NULL) trigger_error("902"."Missing parameter - 'taxonid' field is required.", E_USER_ERROR);
            if($myRequest->subsiteid == NULL) trigger_error("902"."Missing parameter - 'subsiteid' field is required.", E_USER_ERROR);
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "failed":
        $myMetaHeader->setRequestType("help");

    default:
        $myMetaHeader->setRequestType("help");
        // Output the resulting XML
        writeHelpOutput($myMetaHeader);
        die;
}

// *************
// PERFORM QUERY
// *************

$xmldata ="";
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
            trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage(), E_USER_ERROR);
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->name)) $myTree->setName($myRequest->name);
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
               trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage(), E_USER_ERROR);
            }
        }  
        else
        {
            trigger_error("103"."Permission denied on treeid $myRequest->id", E_USER_ERROR);
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
                trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage(), E_USER_ERROR);
            }
        }
        else
        {
            trigger_error("103"."Permission denied on treeid $myRequest->id", E_USER_ERROR);
        }
    }

    if($myRequest->mode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if($myRequest->id!=NULL)
            {
                    // Check user has permission to read tree
                    if($myAuth->treePermission($myRequest->id, "read"))
                    {
                        $myTree = new tree();
                        $success = $myTree->setParamsFromDB($myRequest->id);
                        $success2 = $myTree->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.= $myTree->asXML();
                        }
                        else
                        {
                            trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage(), E_USER_ERROR);
                        }
                    }
                    else
                    {
                        trigger_error("103"."Permission denied on treeid ".$myRequest->id, E_USER_WARNING);
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
                            trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage(), E_USER_WARNING);
                        }
                    }
                    else
                    {
                        trigger_error("103"."Permission denied on treeid ".$row['treeid'], E_USER_WARNING);
                    }
                }
                $xmldata.= $parentTagEnd."\n";
            }
        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database", E_USER_ERROR);
        }
    }
}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata);
