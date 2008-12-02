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
$myRequest      = new authenticateRequest($myMetaHeader, $myAuth);

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}

//$serverHashCurrent = hash('md5', "webuser:c0af77cf8294ff93a5cdb2963ca9f038:0123456789ABCDE:AA");
//echo $serverHashCurrent;

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
    case "plainlogin":
        $myMetaHeader->setRequestType("login");
        if($myRequest->username == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
        if($myRequest->password == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'password' field is required.");
        break;
    
    case "securelogin":
        $myMetaHeader->setRequestType("login");
        if($myRequest->username == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'username' field is required.");
        if($myRequest->hash == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'hash' field is required.");
        if($myRequest->nonce == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'nonce' field is required.");
        break;

    case "logout":
        $myMetaHeader->setRequestType("logout");
        break;
        
    case "nonce":
        $myMetaHeader->requestLogin($myAuth->nonce(), "OK");
        break;

    case "failed":
        $myMetaHeader->setRequestType("help");

    default:
        $myMetaHeader->setRequestType("help");
        // Output the resulting XML
        $xmlstring = getHelpDocbook('Authenticate');
        writeHelpOutput($myMetaHeader,$out);
        die;
}

// *************
// PERFORM QUERY
// *************

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='plainlogin')
    {
        $myAuth->login($myRequest->username, $myRequest->password);
        if($myAuth->isLoggedIn())
        {
            // Log in worked
            $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
        }
        else
        {
            // Log in failed
            $myMetaHeader->setMessage(101, "Authentication failed using plain login");
        }
    }
    
    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='securelogin')
    {
        $myAuth->loginWithNonce($myRequest->username, $myRequest->hash, $myRequest->nonce);
        
        if($myAuth->isLoggedIn())
        {
            // Log in worked
            $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
        }
        else
        {
            // Log in failed
            $myMetaHeader->setMessage(101, "Authentication failed using secure login");
        }
    }

    // Destroy session
    if($myRequest->mode=='logout')
    {
        $myAuth->logout();
    }
}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader);
