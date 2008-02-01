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

require_once("inc/dbsetup.php");
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/radius.php");
require_once("inc/subSite.php");
require_once("inc/site.php");
require_once("inc/specimen.php");
require_once("inc/tree.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new radiusRequest($myMetaHeader, $myAuth);

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
            if(!(gettype($myRequest->id)=="integer") && !($myRequest->id==NULL)) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
            if(!($myRequest->id>0) && !($myRequest->id==NULL)) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be a valid positive integer.");
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
            if(($myRequest->specimenid==NULL) && ($myRequest->label==NULL)) $myMetaHeader->setMessage("902", "Missing parameters - you haven't specified any parameters to update.");
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
            $myMetaHeader->requestLogin($myAuth->nonce());
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
            if($myRequest->specimenid == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'specimenid' field is required.");
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
    // Create radius object 
    $myRadius = new radius();
    $parentTagBegin = $myRadius->getParentTagBegin();
    $parentTagEnd = $myRadius->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $myRadius->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->label)) $myRadius->setLabel($myRequest->label);
        if (!($myRequest->specimenid)==NULL) $myRadius->setSpecimenID($myRequest->specimenid);
        
        if( (($myRequest->mode=='update') && ($myAuth->radiusPermission($myRequest->id, "update")))  || 
            (($myRequest->mode=='create') && ($myAuth->specimenPermission($myRequest->specimenid, "create")))    )
        {
            // Check user has permission to update / create radius before writing object to database
            $success = $myRadius->writeToDB();
            if($success)
            {
                $xmldata=$myRadius->asXML();
            }
            else
            {
               $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
            }
        }  
        else
        {
            if($myRequest->mode=='update')
            {
                $myMetaHeader->setMessage("103", "Permission denied on radiusid ".$myRequest->id);
            }
            else
            {
                $myMetaHeader->setMessage("103", "Permission denied on specimenid ".$myRequest->specimenid);
            }
        }
    }

    // Delete record from db if requested
    if($myRequest->mode=='delete')
    {
        if($myAuth->radiusPermission($myRequest->id, "delete"))
        {
            // Check user has permission to delete record before performing statement
            $success = $myRadius->deleteFromDB();
            if($success)
            {
                $xmldata=$myRadius->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on radiusid ".$myRequest->id);
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
                $sql="select tblradius.*, tblspecimen.specimenid, tbltree.treeid, tblsubsite.subsiteid, tblsubsite.siteid from tblradius, tblspecimen, tbltree, tblsubsite where radiusid=".$myRequest->id."  
                    and tblradius.specimenid=tblspecimen.specimenid and tblspecimen.treeid=tbltree.treeid and tbltree.subsiteid=tblsubsite.subsiteid order by tblradius.radiusid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read radius
                    if($myAuth->radiusPermission($row['radiusid'], "read"))
                    {
                        $myRadius = new radius();
                        $mySpecimen = new specimen();
                        $myTree = new tree();
                        $mySubSite = new subSite();
                        $mySite = new site();
                        $success = $myRadius->setParamsFromDB($row['radiusid']);
                        $success2 = $myRadius->setChildParamsFromDB();
                        $success3 = $mySpecimen->setParamsFromDB($row['specimenid']);
                        $success4 = $myTree->setParamsFromDB($row['treeid']);
                        $success5 = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success6 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 && $success4 && $success5 && $success6 )
                        {
                            $xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML("begin");
                            $xmldata.= $myTree->asXML("begin");
                            $xmldata.= $mySpecimen->asXML("begin");
                            $xmldata.= $myRadius->asXML();
                            $xmldata.= $mySpecimen->asXML("end");
                            $xmldata.= $myTree->asXML("end");
                            $xmldata.= $mySubSite->asXML("end");
                            $xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on radiusid ".$row['radiusid'], "Warning");
                    }
                }
            }
            else
            {
                $sql="select * from tblradius order by radiusid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                $xmldata.= $parentTagBegin;
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read radius
                    if($myAuth->radiusPermission($row['radiusid'], "read"))
                    {
                        $myRadius = new radius();
                        $success = $myRadius->setParamsFromDB($row['radiusid']);
                        $success2 = $myRadius->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.=$myRadius->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on radiusid ".$row['radiusid'], "Warning");
                    }
                }
                $xmldata.= $parentTagEnd;
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





