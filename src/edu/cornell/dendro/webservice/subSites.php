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
require_once("inc/errors.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/subSite.php");
require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
            if($myRequest->name == NULL) trigger_error("902"."Missing parameter - 'name' field is required.", E_USER_ERROR);
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
            if($myRequest->name == NULL)   trigger_error("902"."Missing parameter - 'name' field is required.", E_USER_ERROR);
            if($myRequest->siteid == NULL) trigger_error("902"."Missing parameter - 'siteid' field is required.", E_USER_ERROR);
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
            trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage(), E_USER_ERROR);
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
                trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage(), E_USER_ERROR);
            }
        }
        else
        {
            if($myRequest->id)
            {
                trigger_error("103"."Permission denied on subsiteid ".$myRequest->id, E_USER_ERROR);
            }
            else
            {
                trigger_error("103"."Permission denied on siteid ".$myRequest->siteid, E_USER_ERROR);
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
                trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage(), E_USER_ERROR);
            }
        }
        else
        {
            trigger_error("103"."Permission denied on subsiteid $myRequest->id", E_USER_ERROR);
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
                            //$xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML();
                            //$xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage(), E_USER_ERROR);
                        }
                    }
                    else
                    {
                        trigger_error("103"."Permission denied on subsiteid ".$row['subsiteid'], E_USER_WARNING);
                    }
                }
            }
            else
            {
                $xmldata = $parentTagBegin."\n";
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
                            trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage(), E_USER_ERROR);
                        }
                    }
                    else
                    {
                        trigger_error("103"."Permission denied on subsiteid ".$row['subsiteid'], E_USER_WARNING);
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


