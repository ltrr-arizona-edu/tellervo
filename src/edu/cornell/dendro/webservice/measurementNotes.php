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

require_once("inc/vmeasurementNote.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new vmeasurementNoteRequest($myMetaHeader, $myAuth);

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

// Create new meta object and check required input parameters and data types
switch($myRequest->mode)
{
    case "read":
        $myMetaHeader->setRequestType("read");
        if($myAuth->isLoggedIn())
        {
            if(!(gettype($myRequest->id)=="integer") && !($myRequest->id==NULL)) trigger_error("901"."Invalid parameter - 'id' field must be an integer.");
            if(!($myRequest->id>0) && !($myRequest->id==NULL)) trigger_error("901"."Invalid parameter - 'id' field must be a valid positive integer.");
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.");
            if((gettype($myRequest->isstandard)!="boolean") && ($myRequest->isstandard!=NULL)) trigger_error("901"."Invalid parameter - 'isstandard' must be a boolean.");
            if(!(gettype($myRequest->id)=="integer") && !($myRequest->id)) trigger_error("901"."Invalid parameter - 'id' field must be an integer.");
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.");
            if(!(gettype($myRequest->id)=="integer") && !(isset($myRequest->id))) trigger_error("901"."Invalid parameter - 'id' field must be an integer.");
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
            // Set default value if not specified
            if($myRequest->isstandard == NULL)                  $myRequest->isstandard = FALSE;
            if($myRequest->note == NULL)                        trigger_error("902"."Missing parameter - 'note' field is required.");
            if(!(gettype($myRequest->isstandard)=="boolean"))   trigger_error("901"."Invalid parameter - 'isstandard' must be a boolean.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }
    
    case "assign":
        $myMetaHeader->setRequestType("assign");
        if($myAuth->isLoggedIn())
        {
            // Set default value if not specified
            if($myRequest->id == NULL)                          trigger_error("902"."Missing parameter - 'id' field is required.");
            if($myRequest->measurementid == NULL)               trigger_error("902"."Missing parameter - 'measurementid' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }
    
    case "unassign":
        $myMetaHeader->setRequestType("unassign");
        if($myAuth->isLoggedIn())
        {
            // Set default value if not specified
            if($myRequest->id == NULL)                          trigger_error("902"."Missing parameter - 'id' field is required.");
            if($myRequest->measurementid == NULL)               trigger_error("902"."Missing parameter - 'measurementid' field is required.");
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

$xmldata = "";
//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create vmeasurementNote object 
    $myMeasurementNote = new vmeasurementNote();
    $parentTagBegin = $myMeasurementNote->getParentTagBegin();
    $parentTagEnd = $myMeasurementNote->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete' || $myRequest->mode=='assign' || $myRequest->mode=='unassign') 
    {
        $success = $myMeasurementNote->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            trigger_error($myMeasurementNote->getLastErrorCode().$myMeasurementNote->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {	
        if(!($myAuth->isAdmin()) && ($myRequest->mode=='update'))
        {
            trigger_error("103"."Permission denied");
        }
        else
        {
            if (isset($myRequest->note))        $myMeasurementNote->setNote($myRequest->note);
            if (isset($myRequest->isstandard))  $myMeasurementNote->setIsStandard($myRequest->isstandard);

            // Write to object to database
            $success = $myMeasurementNote->writeToDB();
            if($success)
            {
                $xmldata=$myMeasurementNote->asXML();
            }
            else
            {
                trigger_error($myMeasurementNote->getLastErrorCode().$myMeasurementNote->getLastErrorMessage());
            }
        }
    }

    if($myRequest->mode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if($myRequest->id==NULL)
            {
                $sql="select * from tlkpvmeasurementnote order by vmeasurementnoteid";
            }
            else
            {
                $sql="select * from tlkpvmeasurementnote where vmeasurementnoteid=".$myRequest->id." order by vmeasurementnoteid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $myMeasurementNote = new vmeasurementNote();
                    $success = $myMeasurementNote->setParamsFromDB($row['vmeasurementnoteid']);

                    if($success)
                    {
                        $xmldata.=$myMeasurementNote->asXML();
                    }
                    else
                    {
                        trigger_error($myMeasurementNote->getLastErrorCode().$myMeasurementNote->getLastErrorMessage());
                    }
                }
            }
        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database");
        }
    }
    
    
    if($myRequest->mode=='assign')
    {
        if($myAuth->vmeasurementPermission($myRequest->measurementid, "update"))   
        {
            $success = $myMeasurementNote->assignToVMeasurement($myRequest->measurementid);     
            if($success)
            {
                $xmldata=$myMeasurementNote->asXML();
            }
            else
            {
                trigger_error($myMeasurementNote->getLastErrorCode().$myMeasurementNote->getLastErrorMessage());
            }
        }
        else
        {
            trigger_error("103"."Permission denied on measurementid=".$myRequest->measurementid);
        }
    }
    
    if($myRequest->mode=='unassign')
    {
        if($myAuth->vmeasurementPermission($myRequest->measurementid, "update"))   
        {
            $success = $myMeasurementNote->unassignToVMeasurement($myRequest->measurementid);     
            if($success)
            {
                $xmldata=$myMeasurementNote->asXML();
            }
            else
            {
                trigger_error($myMeasurementNote->getLastErrorCode().$myMeasurementNote->getLastErrorMessage());
            }
        }
        else
        {
            trigger_error("103"."Permission denied on measurementnoteid=".$myRequest->vmeasurementnoteid);
        }
    }

    if($myRequest->mode=='delete')
    {
        
        if($myAuth->isAdmin()) 
        {
            // Write to Database
            $success = $myMeasurementNote->deleteFromDB();
            if($success)
            {
                $xmldata=$myMeasurementNote->asXML();
            }
            else
            {
                trigger_error($myMeasurementNote->getLastErrorCode().$myMeasurementNote->getLastErrorMessage());
            }
        }
        else
        {
            trigger_error("103"."Permission denied on measurementnoteid ".$myRequest->id);
        }

    }

}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata, $parentTagBegin, $parentTagEnd);
