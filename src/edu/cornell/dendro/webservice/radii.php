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
            if(!(gettype($myRequest->id)=="integer") && !($myRequest->id==NULL)) trigger_error("901", "Invalid parameter - 'id' field must be an integer.", E_USER_ERROR);
            if(!($myRequest->id>0) && !($myRequest->id==NULL)) trigger_error("901", "Invalid parameter - 'id' field must be a valid positive integer.", E_USER_ERROR);
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
            if(($myRequest->specimenid==NULL) && ($myRequest->name==NULL)) trigger_error("902"."Missing parameters - you haven't specified any parameters to update.", E_USER_ERROR);
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
            if($myRequest->name == NULL) trigger_error("902"."Missing parameter - 'name' field is required.", E_USER_ERROR);
            if($myRequest->specimenid == NULL) trigger_error("902"."Missing parameter - 'specimenid' field is required.", E_USER_ERROR);
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
            trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage(), E_USER_ERROR);
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->name)) $myRadius->setName($myRequest->name);
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
               trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage(), E_USER_ERROR);
            }
        }  
        else
        {
            if($myRequest->mode=='update')
            {
                trigger_error("103"."Permission denied on radiusid ".$myRequest->id, E_USER_ERROR);
            }
            else
            {
                trigger_error("103"."Permission denied on specimenid ".$myRequest->specimenid, E_USER_ERROR);
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
                trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage(), E_USER_ERROR);
            }
        }
        else
        {
            trigger_error("103"."Permission denied on radiusid ".$myRequest->id, E_USER_ERROR);
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
                // Check user has permission to read radius
                if($myAuth->radiusPermission($myRequest->id, "read"))
                {
                    $myRadius = new radius();
                    $success = $myRadius->setParamsFromDB($myRequest->id);
                    $success2 = $myRadius->setChildParamsFromDB();

                    if($success && $success2)
                    {
                        $xmldata.= $myRadius->asXML();
                    }
                    else
                    {
                        trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage(), E_USER_ERROR);
                    }
                }
                else
                {
                    trigger_error("103"."Permission denied on radiusid ".$myRequest->id, E_USER_WARNING);
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
                            trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage(), E_USER_ERROR);
                        }
                    }
                    else
                    {
                        trigger_error("103"."Permission denied on radiusid ".$row['radiusid'], E_USER_WARNING);
                    }
                }
                $xmldata.= $parentTagEnd;
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





