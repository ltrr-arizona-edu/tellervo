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
require_once("inc/securityGroup.php");
require_once("inc/auth.php");

$myAuth = new auth();
// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
if(isset($_GET['id'])) $theID = (int) $_GET['id'];
if(isset($_GET['name'])) $theName = addslashes($_GET['name']);
if(isset($_GET['description'])) $theDescription = addslashes($_GET['description']);
$theIsActive= fromStringToPHPBool($_GET['isactive']);


// Create new meta object and check required input parameters and data types
switch($theMode)
{

    case "read":
        $myMetaHeader = new meta("read");
        if($myAuth->isAdmin())
        {
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must have administrative privileges to run this query");
            break;
        }


    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isAdmin())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(($theName==NULL) && ($theDescription==NULL) && ($theIsActive==NULL) ) $myMetaHeader->setMessage("902", "Missing parameters - you haven't specified any parameters to update.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must have administrative privileges to run this query");
            break;
        }

    case "delete":
        $myMetaHeader = new meta("delete");
        if($myAuth->isAdmin())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must have administrative privileges to run this query");
            break;
        }


    case "create":
        $myMetaHeader = new meta("create");
        if($myAuth->isAdmin())
        {
            if($theName == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
            if($theDescription == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'description' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must have administrative privileges to run this query");
            break;
        }

    default:
        $myMetaHeader = new meta("help");
        $myMetaHeader->setUser("Guest", "", "", "");
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
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create securitygroup object 
    $mySecurityGroup = new securityGroup();
    $parentTagBegin = $mySecurityGroup->getParentTagBegin();
    $parentTagEnd = $mySecurityGroup->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $mySecurityGroup->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySecurityGroup->getLastErrorCode(), $mySecurityGroup->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (!($theName)==NULL) $mySecurityGroup->setName($theName);
        if (!($theDescription)==NULL) $mySecurityGroup->setDescription($theDescription);
        if (isset($theIsActive)) $mySecurityGroup->setIsActive($theIsActive);
        
        $success = $mySecurityGroup->writeToDB();
        if($success)
        {
            $xmldata=$mySecurityGroup->asXML();
        }
        else
        {
           $myMetaHeader->setMessage($mySecurityGroup->getLastErrorCode(), $mySecurityGroup->getLastErrorMessage());
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        $success = $mySecurityGroup->deleteFromDB();
        if($success)
        {
            $xmldata=$mySecurityGroup->asXML();
        }
        else
        {
            $myMetaHeader->setMessage($mySecurityGroup->getLastErrorCode(), $mySecurityGroup->getLastErrorMessage());
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
                $sql = "select securitygroupid from tblsecuritygroup ";
            }
            else
            {
                $sql = "select securitygroupid from tblsecuritygroup where securitygroupid=$theID";
            }
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                    $mySecurityGroup = new securityGroup();
                    $success = $mySecurityGroup->setParamsFromDB($row['securitygroupid']);
                    if($success)
                    {
                        $xmldata.=$mySecurityGroup->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
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
echo $xmldata;
echo "</data>\n";
echo "</corina>";
