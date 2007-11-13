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
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new siteRequest($myMetaHeader);

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}

//$serverHashCurrent = hash('md5', "webuser:c0af77cf8294ff93a5cdb2963ca9f038:0123456789ABCDE:AA");
//echo $serverHashCurrent;

// **************
// GET PARAMETERS
// **************

$theMode = strtolower(addslashes($_POST['mode']));

if(isset($_POST['username'])) $theUsername = addslashes($_POST['username']);
if(isset($_POST['password'])) $thePassword = addslashes($_POST['password']);
if(isset($_POST['hash'])) $theClientHash = addslashes($_POST['hash']);
if(isset($_POST['nonce'])) $theClientNonce = addslashes($_POST['nonce']);

if($debugFlag)
{
    $myMetaHeader->setMessage("Username", $_POST['username'], "Warning");
    $myMetaHeader->setMessage("Password", $_POST['password'], "Warning");
    $myMetaHeader->setMessage("Hash", $_POST['hash'], "Warning");
    $myMetaHeader->setMessage("Prehash", $_POST['prehash'], "Warning");
    $myMetaHeader->setMessage("nonce", $_POST['nonce'], "Warning");
}


// ****************
// CHECK PARAMETERS 
// ****************

// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "plainlogin":
        $myMetaHeader->setRequestType("login");
        if($theUsername == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
        if($thePassword == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'password' field is required.");
        break;
    
    case "securelogin":
        $myMetaHeader->setRequestType("login");
        if($theUsername == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
        if($theClientHash == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'hash' field is required.");
        if($theClientNonce == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'nonce' field is required.");
        break;

    case "logout":
        $myMetaHeader->setRequestType("logout");
        break;

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
    // Update parameters in object if updating or creating an object 
    if($theMode=='plainlogin')
    {
        $myAuth->login($theUsername, $thePassword);
        if($myAuth->isLoggedIn())
        {
            // Log in worked
        }
        else
        {
            // Log in failed
            $myMetaHeader->setMessage(101, "Authentication failed using plain login");
        }
    }
    
    // Update parameters in object if updating or creating an object 
    if($theMode=='securelogin')
    {
        $myAuth->loginWithNonce($theUsername, $theClientHash, $theClientNonce);
        
        if($myAuth->isLoggedIn())
        {
            // Log in worked
        }
        else
        {
            // Log in failed
            $myMetaHeader->setMessage(101, "Authentication failed using secure login");
        }
    }

    // Destroy session
    if($theMode=='logout')
    {
        $myAuth->logout();
    }
}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader);
