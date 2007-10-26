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
require_once("inc/vMeasurementNote.php");
require_once("inc/auth.php");

$myAuth = new auth();

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theID = (int) $_GET['id'];
$theVMeasurementID = (int) $_GET['vmeasurementid'];
if(isset($_GET['note'])) $theNote = addslashes($_GET['note']);
$theIsStandard = fromStringToPHPBool($_GET['isstandard']);

// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        if($myAuth->isLoggedIn())
        {
            if(!(gettype($theID)=="integer") && !($theID==NULL)) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
            if(!($theID>0) && !($theID==NULL)) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be a valid positive integer.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }
    
    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isLoggedIn())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if((gettype($theIsStandard)!="boolean") && ($theIsStandard!=NULL)) $myMetaHeader->setMessage("901", "Invalid parameter - 'isstandard' must be a boolean.");
            if(!(gettype($theID)=="integer") && !($theID)) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }

    case "delete":
        $myMetaHeader = new meta("delete");
        if($myAuth->isLoggedIn())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(!(gettype($theID)=="integer") && !(isset($theID))) $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }

    case "create":
        $myMetaHeader = new meta("create");
        if($myAuth->isLoggedIn())
        {
            // Set default value if not specified
            if($theIsStandard == NULL) $theIsStandard = FALSE;
            if($theNote == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'note' field is required.");
            if(!(gettype($theIsStandard)=="boolean")) $myMetaHeader->setMessage("901", "Invalid parameter - 'isstandard' must be a boolean.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }
    
    case "assign":
        $myMetaHeader = new meta("assign");
        if($myAuth->isLoggedIn())
        {
            // Set default value if not specified
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if($theVMeasurementID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'vmeasurementid' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }
    
    case "unassign":
        $myMetaHeader = new meta("unassign");
        if($myAuth->isLoggedIn())
        {
            // Set default value if not specified
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if($theVMeasurementID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'vmeasurementid' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }

    default:
        $myMetaHeader = new meta("help");
        $myMetaHeader->setUser("Guest", "", "");
        // Output the resulting XML
        echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        echo "<corina>\n";
        echo $myMetaHeader->asXML();
        echo "<help> Details of how to use this web service will be added here later! </help>";
        echo "</corina>\n";
        die;
}

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create vMeasurementNote object 
    $mySiteNote = new vMeasurementNote();
    $parentTagBegin = $mySiteNote->getParentTagBegin();
    $parentTagEnd = $mySiteNote->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete' || $theMode=='assign' || $theMode=='unassign') 
    {
        $success = $mySiteNote->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {	
        if(!($myAuth->isAdmin()) && ($theMode=='update'))
        {
            $myMetaHeader->setMessage("103", "Permission denied");
        }
        else
        {
            if (isset($theNote)) $mySiteNote->setNote($theNote);
            if (isset($theIsStandard)) $mySiteNote->setIsStandard($theIsStandard);

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

    if($theMode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if($theID==NULL)
            {
                $sql="select * from tlkpvmeasurementnote order by vmeasurementnoteid";
            }
            else
            {
                $sql="select * from tlkpvmeasurementnote where vmeasurementnoteid=$theID order by vmeasurementnoteid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $mySiteNote = new vMeasurementNote();
                    $success = $mySiteNote->setParamsFromDB($row['vmeasurementnoteid']);

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
    
    if($theMode=='assign')
    {
        if($myAuth->vMeasurementPermission($theVMeasurementID, "update"))   
        {
            $mySiteNote->assignToVMeasurement($theVMeasurementID);     
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on vmeasurementid=$theVMeasurementID");
        }

    }
    
    if($theMode=='unassign')
    {
        if($myAuth->vMeasurementPermission($theVNeasurementID, "update"))   
        {
            $mySiteNote->unassignToVMeasurement($theVMeasurementID);     
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on vmeasurementid=$theVMeasurementID");
        }

    }
}

// Output the resulting XML
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<data>\n";
echo $parentTagBegin."\n";
echo $xmldata;
echo $parentTagEnd."\n";
echo "</data>\n";
echo "</corina>";
