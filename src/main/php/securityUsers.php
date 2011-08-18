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
require_once("inc/securityUser.php");
require_once("inc/auth.php");

$myAuth = new auth();
// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theID = (int) $_GET['id'];
if(isset($_GET['username'])) $theUsername = addslashes($_GET['username']);
if(isset($_GET['password'])) $thePassword = addslashes($_GET['password']);
if(isset($_GET['firstname'])) $theFirstname = addslashes($_GET['firstname']);
if(isset($_GET['lastname'])) $theLastname = addslashes($_GET['lastname']);
$theIsActive= fromStringToPHPBool($_GET['isactive']);


// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isAdmin())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(($theUsername==NULL) && ($thePassword==NULL) && ($theFirstname==NULL) && ($theLastname==NULL) ) $myMetaHeader->setMessage("902", "Missing parameters - you haven't specified any parameters to update.");
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
            if($theUsername == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
            if($thePassword == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'password' field is required.");
            if($theFirstname == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'firstname' field is required.");
            if($theLastname == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'lastname' field is required.");
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
    // Create securityuser object 
    $mySecurityUser = new securityUser();
    $parentTagBegin = $mySecurityUser->getParentTagBegin();
    $parentTagEnd = $mySecurityUser->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $mySecurityUser->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySecurityUser->getLastErrorCode(), $mySecurityUser->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (!($theUsername)==NULL) $mySecurityUser->setUsername($theUsername);
        if (!($theFirstname)==NULL) $mySecurityUser->setFirstname($theFirstname);
        if (!($theLastname)==NULL) $mySecurityUser->setLastname($theLastname);
        if (!($thePassword)==NULL) $mySecurityUser->setPassword($thePassword);
        if (isset($theIsActive)) $mySecurityUser->setIsActive($theIsActive);
        
        $success = $mySecurityUser->writeToDB();
        if($success)
        {
            $xmldata=$mySecurityUser->asXML();
        }
        else
        {
           $myMetaHeader->setMessage($mySecurityUser->getLastErrorCode(), $mySecurityUser->getLastErrorMessage());
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        $success = $mySecurityUser->deleteFromDB();
        if($success)
        {
            $xmldata=$mySecurityUser->asXML();
        }
        else
        {
            $myMetaHeader->setMessage($mySecurityUser->getLastErrorCode(), $mySecurityUser->getLastErrorMessage());
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
