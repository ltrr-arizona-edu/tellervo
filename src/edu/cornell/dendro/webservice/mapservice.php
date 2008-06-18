<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

require_once("inc/dbsetup.php");
require_once("inc/dbhelper.php");
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/output.php");

require_once("inc/site.php");
require_once("inc/subSite.php");
require_once("inc/tree.php");
require_once("inc/specimen.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/authenticate.php");


$xmldata = NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();

// Get GET parameters
$reqObject = $_GET['object'];
$reqID = $_GET['id'];

// Extract the type of request from XML doc
$myMetaHeader->setRequestType('read');


// Check authentication and request login if necessary
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}
else
{
    $myMetaHeader->requestLogin($myAuth->nonce());
}

switch($reqObject)
{
case "site":
    $myObject = new site();
    break;
case "tree":
    $myObject = new tree();
    break;
case "measurement":
    $myObject = new measurement();
    break;
default :
    echo "unknown object type";
    die();
}

        
// Before doing anything else check the request parameters are valid
if($myMetaHeader->status != "Error")
{
    /*if($success)
    {
        trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), $defaultErrType);
        continue;
    }*/
}

// Do permissions check
if($myAuth->getPermission("read", $reqObject, $reqID)===FALSE)
{
    // Standard error message
    trigger_error("103"."Permission to read ".$reqObject." id $reqID was denied.");
}

// Populated object with data
$success = $myObject->setParamsFromDB($reqID);
$success2 = $myObject->setChildParamsFromDB();
if(!($success && $success2))
{
    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_NOTICE);
}

// Get XML representation of data
if($myMetaHeader->status != "Error")
{
    $xmldata.= $myObject->asXML();
}
        
//writeOutput($myMetaHeader, $xmldata);
writeGMapOutput(createOutput($myMetaHeader, $xmldata), $myRequest);
