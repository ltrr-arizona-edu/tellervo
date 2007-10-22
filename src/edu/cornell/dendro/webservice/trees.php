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
require_once("inc/tree.php");
require_once("inc/specimen.php");
require_once("inc/auth.php");

$myAuth = new auth();

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theID = (int) $_GET['id'];
if(isset($_GET['label'])) $theName = addslashes($_GET['label']);

// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        break;
    
    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isLoggedIn())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(($theName == NULL) && ($theCode==NULL)) $myMetaHeader->setMessage("902", "Missing parameter - either 'name' or 'code' fields (or both) must be specified.");
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
            if($theName == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
            if($theCode == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'code' field is required.");
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
else
{
    $myMetaHeader->setUser("Guest", "Guest", "Guest");
}

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create tree object 
    $myTree = new tree();
    $parentTagBegin = $myTree->getParentTagBegin();
    $parentTagEnd = $myTree->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $myTree->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (isset($theName)) $myTree->setName($theName);
        if (isset($theCode)) $myTree->setCode($theCode);

        // Write to object to database
        $success = $myTree->writeToDB();
        if($success)
        {
            $xmldata=$myTree->asXML();
        }
        else
        {
            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        // Write to Database
        $success = $myTree->deleteFromDB();
        if($success)
        {
            $xmldata=$myTree->asXML();
        }
        else
        {
            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
        }
    }

    if($theMode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if(!$theID==NULL)
            {
                $sql="select * from tbltree where treeid=$theID order by treeid";
            }
            else
            {
                $sql="select * from tbltree order by treeid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $myTree = new tree();
                    $success = $myTree->setParamsFromDB($row['treeid']);
                    $success2 = $myTree->setChildParamsFromDB();

                    if($success && $success2)
                    {
                        if($myAuth->treeReadPermission($myTree->getID()))
                        {
                            $xmldata.=$myTree->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage("103", "Permission denied on treeid ".$myTree->getID(), "Warning");
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
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
