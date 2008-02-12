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

require_once("inc/readingNote.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new readingNoteRequest($myMetaHeader, $myAuth);

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
    // Create readingNote object 
    $myReadingNote = new readingNote();
    $parentTagBegin = $myReadingNote->getParentTagBegin();
    $parentTagEnd = $myReadingNote->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete' || $myRequest->mode=='assign' || $myRequest->mode=='unassign') 
    {
        $success = $myReadingNote->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            trigger_error($myReadingNote->getLastErrorCode().$myReadingNote->getLastErrorMessage());
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
            if (isset($myRequest->note))        $myReadingNote->setNote($myRequest->note);
            if (isset($myRequest->isstandard))  $myReadingNote->setIsStandard($myRequest->isstandard);

            // Write to object to database
            $success = $myReadingNote->writeToDB();
            if($success)
            {
                $xmldata=$myReadingNote->asXML();
            }
            else
            {
                trigger_error($myReadingNote->getLastErrorCode().$myReadingNote->getLastErrorMessage());
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
                $sql="select * from tlkpreadingnote order by readingnoteid";
            }
            else
            {
                $sql="select * from tlkpreadingnote where readingnoteid=".$myRequest->id." order by readingnoteid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $myReadingNote = new readingNote();
                    $success = $myReadingNote->setParamsFromDB($row['readingnoteid']);

                    if($success)
                    {
                        $xmldata.=$myReadingNote->asXML();
                    }
                    else
                    {
                        trigger_error($myReadingNote->getLastErrorCode().$myReadingNote->getLastErrorMessage());
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
        if($myAuth->treePermission($myRequest->readingnoteid, "update"))   
        {
            $success = $myReadingNote->assignToTree($myRequest->readingnoteid);     
            if($success)
            {
                $xmldata=$myReadingNote->asXML();
            }
            else
            {
                trigger_error($myReadingNote->getLastErrorCode().$myReadingNote->getLastErrorMessage());
            }
        }
        else
        {
            trigger_error("103"."Permission denied on readingnoteid=".$myRequest->readingnoteid);
        }
    }
    
    if($myRequest->mode=='unassign')
    {
        if($myAuth->treePermission($myRequest->readingnoteid, "update"))   
        {
            $success = $myReadingNote->unassignToTree($myRequest->readingnoteid);     
            if($success)
            {
                $xmldata=$myReadingNote->asXML();
            }
            else
            {
                trigger_error($myReadingNote->getLastErrorCode().$myReadingNote->getLastErrorMessage());
            }
        }
        else
        {
            trigger_error("103"."Permission denied on readingnoteid=".$myRequest->readingnoteid);
        }
    }

    if($myRequest->mode=='delete')
    {
        
        if($myAuth->isAdmin()) 
        {
            // Write to Database
            $success = $myReadingNote->deleteFromDB();
            if($success)
            {
                $xmldata=$myReadingNote->asXML();
            }
            else
            {
                trigger_error($myReadingNote->getLastErrorCode().$myReadingNote->getLastErrorMessage());
            }
        }
        else
        {
            trigger_error("103"."Permission denied on readingnoteid ".$myRequest->id);
        }

    }

}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata, $parentTagBegin, $parentTagEnd);
