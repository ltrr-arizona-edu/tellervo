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

require_once("inc/measurement.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new measurementRequest($myMetaHeader, $myAuth);

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
        $myMetaHeader = new meta("read");
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
        $myMetaHeader = new meta("update");
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

    case "delete":
        $myMetaHeader = new meta("delete");
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
        $myMetaHeader = new meta("create");
        if($myAuth->isLoggedIn())
        {
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
    // Create measurement object 
    $myMeasurement = new measurement();
    $parentTagBegin = $myMeasurement->getParentTagBegin();
    $parentTagEnd = $myMeasurement->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $myMeasurement->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($myMeasurement->getLastErrorCode(), $myMeasurement->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->radiusid))            $myMeasurement->setRadiusID($myRequest->radiusid);
        if (isset($myRequest->isreconciled))        $myMeasurement->setIsReconciled($myRequest->isreconciled);
        if (isset($myRequest->startyear))           $myMeasurement->setStartYear($myRequest->startyear);
        if (isset($myRequest->islegacycleaned))     $myMeasurement->setIsLegacyCleaned($myRequest->islegacycleaned);
        if (isset($myRequest->measuredbyid))        $myMeasurement->setMeasuredByID($myRequest->measuredbyid);
        if (isset($myRequest->datingtypeid))        $myMeasurement->setDatingTypeID($myRequest->datingtypeid);
        if (isset($myRequest->datingerrorpositive)) $myMeasurement->setDatingErrorPositive($myRequest->datingerrorpositive);
        if (isset($myRequest->datingerrornegative)) $myMeasurement->setDatingErrorNegative($myRequest->datingerrornegative);
        if (isset($myRequest->name))                $myMeasurement->setName($myRequest->name);
        if (isset($myRequest->description))         $myMeasurement->setDescription($myRequest->description);
        if (isset($myRequest->ispublished))         $myMeasurement->setIsPublished($myRequest->ispublished);
        if (isset($myRequest->measurementopid))     $myMeasurement->setMeasurementOpID($myRequest->measurementopid);
        if (isset($myRequest->ownerid))             $myMeasurement->setOwnerID($myRequest->ownerid);
        if (isset($myRequest->readingsArray))       $myMeasurement->setReadingsArray($myRequest->readingsArray);

        if( (($myRequest->mode=='update') && ($myAuth->vmeasurementPermission($myRequest->id, "update")))  || 
            (($myRequest->mode=='create') && ($myAuth->vmeasurementPermission($myRequest->id, "create")))    )
        {
            // Write object to database
            $success = $myMeasurement->writeToDB();
            if($success)
            {
                $xmldata=$myMeasurement->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($myMeasurement->getLastErrorCode(), $myMeasurement->getLastErrorMessage());
            }
        }  
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on vmeasurementid ".$myRequest->id);
        }
    }

    // Delete record from db if requested
    if($myRequest->mode=='delete')
    {
        if($myAuth->vmeasurementPermission($myRequest->id, "delete"))
        {
            // Write to Database
            $success = $myMeasurement->deleteFromDB();
            if($success)
            {
                $xmldata=$myMeasurement->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($myMeasurement->getLastErrorCode(), $myMeasurement->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on vmeasurementid ".$myRequest->id);
        }
    }

    if($myRequest->mode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            
                // Check user has permission to read tree
                if($myAuth->vmeasurementPermission($myRequest->id, "read"))
                {
                    $myMeasurement = new measurement();
                    $success = $myMeasurement->setParamsFromDB($myRequest->id);
                    if($success)
                    {
                        $xmldata.=$myMeasurement->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setMessage($myMeasurement->getLastErrorCode(), $myMeasurement->getLastErrorMessage());
                    }
                }
                else
                {
                    $myMetaHeader->setMessage("103", "Permission denied on measurementid ".$myRequest->id, "Warning");
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
