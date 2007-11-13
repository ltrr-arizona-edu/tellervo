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

// Only report errors
//error_reporting(E_ERROR);

require_once("inc/dbsetup.php");
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new siteRequest($myMetaHeader);

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
            if(($myRequest->name == NULL) && ($myRequest->code==NULL)) $myMetaHeader->setMessage("902", "Missing parameter - either 'name' or 'code' fields (or both) must be specified.");
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
            if($myRequest->name == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
            if($myRequest->code == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'code' field is required.");
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
    // Create site object 
    $mySite = new site();
    $parentTagBegin = $mySite->getParentTagBegin();
    $parentTagEnd = $mySite->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $mySite->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySite->getLastErrorCode(), $mySite->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->name)) $mySite->setName($myRequest->name);
        if (isset($myRequest->code)) $mySite->setCode($myRequest->code);

        if( (($myRequest->mode=='update') && ($myAuth->sitePermission($myRequest->id, "update")))  || 
            (($myRequest->mode=='create') && ($myAuth->sitePermission($myRequest->id, "create")))    )
        {
            // Write to object to database
            $success = $mySite->writeToDB();
            if($success)
            {
                $xmldata=$mySite->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySite->getLastErrorCode(), $mySite->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on siteid". $myRequest->id);
        }
    }

    // Delete record from db if requested
    if($myRequest->mode=='delete')
    {
        if($myAuth->sitePermission($myRequest->id, "delete"))
        {
            // Write to Database
            $success = $mySite->deleteFromDB();
            if($success)
            {
                $xmldata=$mySite->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySite->getLastErrorCode(), $mySite->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on siteid". $myRequest->id);
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
                $sql="select * from tblsite where siteid=".$myRequest->id." order by siteid";
            }
            elseif((isset($myRequest->name)) && (isset($myRequest->code)))
            {
                echo "2";
                $sql="select * from tblsite where name='$myRequest->name' and code='$myRequest->code' order by siteid";
            }
            elseif(isset($myRequest->name))
            {
                $sql="select * from tblsite where name ilike '%$myRequest->name%' order by siteid";
            }
            elseif(isset($myRequest->code))
            {
                $sql="select * from tblsite where code ilike '%$myRequest->code%' order by siteid";
            }
            else
            {
                $sql="select tblsite.* from (tblsite INNER JOIN securitygroupsitemaster(2,".$myAuth->getID().") on tblsite.siteid = securitygroupsitemaster.objectid) order by tblsite.siteid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                $myMetaHeader->setTiming("Start main SQL request");
                while ($row = pg_fetch_array($result))
                {
                    $myMetaHeader->setTiming("Found row match now check permission");
                    // Check user has permission to read tree
                    if($myAuth->sitePermission($row['siteid'], "read"))
                    {
                        $myMetaHeader->setTiming("Permission ok");
                        $mySite = new site();
                        $success = $mySite->setParamsFromDB($row['siteid']);
                        $myMetaHeader->setTiming("Params set from DB");
                        $success2 = $mySite->setChildParamsFromDB();
                        $myMetaHeader->setTiming("Child object params set from DB");

                        if($success && $success2)
                        {
                            $myMetaHeader->setTiming("Start XML build");
                            $xmldata.=$mySite->asXML();
                            $myMetaHeader->setTiming("End XML build");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySite->getLastErrorCode, $mySite->getLastErrorMessage);
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on site id ".$row['siteid'], "Warning");
                    }
                }
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
writeOutput($myMetaHeader, $xmldata, $parentTagBegin, $parentTagEnd);


