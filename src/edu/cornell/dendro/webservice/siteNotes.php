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

require_once("inc/siteNote.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new siteNoteRequest($myMetaHeader);

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
            if((gettype($myRequest->isstandard)!="boolean") && ($myRequest->isstandard!=NULL)) $myMetaHeader->setMessage("901", "Invalid parameter - 'isstandard' must be a boolean.");
            if(!(gettype($myRequest->id)=="integer") && !($myRequest->id)) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
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
            if(!(gettype($myRequest->id)=="integer") && !(isset($myRequest->id))) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
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
            if($myRequest->isstandard == NULL) $myRequest->isstandard = FALSE;
            if($theNote == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'note' field is required.");
            if(!(gettype($myRequest->isstandard)=="boolean")) $myMetaHeader->setMessage("901", "Invalid parameter - 'isstandard' must be a boolean.");
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
            if($myRequest->id == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if($myRequest->siteid == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'siteid' field is required.");
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
            if($myRequest->id == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
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
    // Create siteNote object 
    $mySiteNote = new siteNote();
    $parentTagBegin = $mySiteNote->getParentTagBegin();
    $parentTagEnd = $mySiteNote->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete' || $myRequest->mode=='assign' || $myRequest->mode=='unassign') 
    {
        $success = $mySiteNote->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {	
        if(!($myAuth->isAdmin()) && ($myRequest->mode=='update'))
        {
            $myMetaHeader->setMessage("103", "Permission denied");
        }
        else
        {
            if (isset($theNote)) $mySiteNote->setNote($theNote);
            if (isset($myRequest->isstandard)) $mySiteNote->setIsStandard($myRequest->isstandard);

            // Write to object to database
            $success = $mySiteNote->writeToDB();
            if($success)
            {
                $xmldata=$mySiteNote->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
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
                $sql="select * from tlkpsitenote order by sitenoteid";
            }
            else
            {
                $sql="select * from tlkpsitenote where sitenoteid=".$myRequest->id." order by sitenoteid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $mySiteNote = new siteNote();
                    $success = $mySiteNote->setParamsFromDB($row['sitenoteid']);

                    if($success)
                    {
                        $xmldata.=$mySiteNote->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setMessage($mySiteNote->getLastErrorCode, $mySiteNote->getLastErrorMessage);
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
    
    if($myRequest->mode=='assign')
    {
        if($myAuth->sitePermission($myRequest->siteid, "update"))   
        {
            $mySiteNote->assignToSite($myRequest->siteid);     
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on siteid=."$myRequest->siteid);
        }

    }
    
    if($myRequest->mode=='unassign')
    {
        if($myAuth->sitePermission($myRequest->siteid, "update"))   
        {
            $mySiteNote->unassignToSite($myRequest->siteid);     
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on siteid=."$myRequest->siteid);
        }

    }
}


// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata, $parentTagBegin, $parentTagEnd);
