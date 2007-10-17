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
require_once("inc/site.php");
require_once("inc/auth.php");

$myAuth = new auth();

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theID = (int) $_GET['id'];
if(isset($_GET['code'])) $theCode = addslashes($_GET['code']);
if(isset($_GET['name'])) $theName = addslashes($_GET['name']);

// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        //if(!($theName==NULL) && (strlen($theName)<3)) $myMetaHeader->setMessage("904", "Parameter too short - 'name' field must contain three or more characters."); 
        //if(!($theCode==NULL) && (strlen($theName)<2)) $myMetaHeader->setMessage("904", "Parameter too short - 'name' field must contain two or more characters."); 
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
        if($myAuth->isLoggedIn())
        {
            $myMetaHeader = new meta("delete");
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }


    case "create":
        if($myAuth->isLoggedIn())
        {
            $myMetaHeader = new meta("create");
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
    // Create site object 
    $mySite = new site();
    $parentTagBegin = $mySite->getParentTagBegin();
    $parentTagEnd = $mySite->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $mySite->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySite->getLastErrorCode(), $mySite->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (isset($theName)) $mySite->setName($theName);
        if (isset($theCode)) $mySite->setCode($theCode);

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

    // Delete record from db if requested
    if($theMode=='delete')
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

    if($theMode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if(!$theID==NULL)
            {
                $sql="select * from tblsite where siteid=$theID order by siteid";
            }
            elseif((isset($theName)) && (isset($theCode)))
            {
                echo "2";
                $sql="select * from tblsite where name='$theName' and code='$theCode' order by siteid";
            }
            elseif(isset($theName))
            {
                $sql="select * from tblsite where name ilike '%$theName%' order by siteid";
            }
            elseif(isset($theCode))
            {
                $sql="select * from tblsite where code ilike '%$theCode%' order by siteid";
            }
            else
            {
                $sql="select * from tblsite order by siteid";
            }

            if($sql)
            {
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $mySite = new site();
                    $success = $mySite->setParamsFromDB($row['siteid']);
                    $success2 = $mySite->setChildParamsFromDB();

                    if($success && $success2)
                    {
                        $xmldata.=$mySite->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setMessage($mySite->getLastErrorCode, $mySite->getLastErrorMessage);
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
