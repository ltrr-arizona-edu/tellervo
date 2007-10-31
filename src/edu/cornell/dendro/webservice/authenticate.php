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

//$serverHashCurrent = hash('md5', "webuser:c0af77cf8294ff93a5cdb2963ca9f038:0123456789ABCDE:AA");
echo $serverHashCurrent;

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theClientHash = addslashes($_GET['hash']);
$theClientNonce = addslashes($_GET['nonce']);
if(isset($_GET['username'])) $theUsername = addslashes($_GET['username']);
if(isset($_GET['password'])) $thePassword = addslashes($_GET['password']);

// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "login":
        $myMetaHeader = new meta("login");
        if($theUsername == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
        if($thePassword == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'password' field is required.");
        break;
    
    case "login2":
        $myMetaHeader = new meta("login");
        if($theUsername == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
        if($theClientHash == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'hash' field is required.");
        if($theClientNonce == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'nonce' field is required.");
        break;

    case "logout":
        $myMetaHeader = new meta("logout");
        break;

    default:
        $myMetaHeader = new meta("help");
        // Output the resulting XML
        echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        echo "<corina>\n";
        echo $myMetaHeader->asXML();
        echo "<help> Details of how to use this web service will be added here later! </help>";
        echo "</corina>\n";
        die;
}

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Creat Authentication object
    $myAuth = new auth();
    
    // Set user details
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());

    // Update parameters in object if updating or creating an object 
    if($theMode=='login')
    {
        $myAuth->login($theUsername, $thePassword);
        
        if($myAuth->isLoggedIn())
        {
            // Log in worked
        }
        else
        {
            // Log in failed
            $myMetaHeader->setMessage(101, "Authentication failed");
        }
    }
    
    // Update parameters in object if updating or creating an object 
    if($theMode=='login2')
    {
        $myAuth->login2($theUsername, $theClientHash, $theClientNonce);
        
        if($myAuth->isLoggedIn())
        {
            // Log in worked
        }
        else
        {
            // Log in failed
            $myMetaHeader->setMessage(101, "Authentication failed");
        }
    }

    // Delete record from db if requested
    if($theMode=='logout')
    {
        $myAuth->logout();
    }
}



// Output the resulting XML
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<content>\n";
echo $xml;
echo "</content>\n";
echo "</corina>";
