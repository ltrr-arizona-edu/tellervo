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

require_once("inc/subSite.php");
require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new subSiteRequest($myMetaHeader, $myAuth);

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
            if($myRequest->name == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
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
            if($myRequest->name == NULL)   $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
            if($myRequest->siteid == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'siteid' field is required.");
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
    // Create subSite object 
    $mySubSite = new subSite();
    $parentTagBegin = $mySubSite->getParentTagBegin();
    $parentTagEnd = $mySubSite->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $mySubSite->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->name))    $mySubSite->setName($myRequest->name);
        if (isset($myRequest->siteid))  $mySubSite->setSiteID($myRequest->siteid);

        if( (($myRequest->mode=='update') && ($myAuth->subSitePermission($myRequest->id, "update")))  || 
            (($myRequest->mode=='create') && ($myAuth->sitePermission($myRequest->siteid, "create")))    )
        {
            // Check user has permission to update / create subsite before writing object to database
            $success = $mySubSite->writeToDB();
            if($success)
            {
                $xmldata=$mySubSite->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
            }
        }
        else
        {
            if($myRequest->id)
            {
                $myMetaHeader->setMessage("103", "Permission denied on subsiteid $myRequest->id");
            }
            else
            {
                $myMetaHeader->setMessage("103", "Permission denied on siteid $myRequest->siteid");
            }
        }
    }

    // Delete record from db if requested
    if($myRequest->mode=='delete')
    {
        if($myAuth->subSitePermission($myRequest->id, "delete"))
        {
            // Check the user has permission to delete record before performing statement
            $success = $mySubSite->deleteFromDB();
            if($success)
            {
                $xmldata=$mySubSite->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on subsiteid $myRequest->id");
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
                $sql="select tblsubsite.subsiteid, tblsubsite.siteid from tblsubsite  where subsiteid=$myRequest->id order by tblsubsite.subsiteid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read subSite
                    if($myAuth->subSitePermission($row['subsiteid'], "read"))
                    {
                        $mySubSite = new subSite();
                        $success = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success2 = $mySubSite->setChildParamsFromDB();
                        $mySite = new site();
                        $success3 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 )
                        {
                            $xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML();
                            $xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on subsiteid ".$row['subsiteid'], "Warning");
                    }
                }
            }
            else
            {
                $xmldata.= $parentTagBegin."\n";
                $sql="select * from tblsubsite order by subsiteid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read subSite
                    if($myAuth->subSitePermission($row['subsiteid'], "read"))
                    {
                        $mySubSite = new subSite();
                        $success = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success2 = $mySubSite->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.=$mySubSite->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on subsiteid ".$row['subsiteid'], "Warning");
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
writeOutput($myMetaHeader, $xmldata, $parentTagBegin, $parentTagEnd);


