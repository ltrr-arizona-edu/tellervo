<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/dbhelper.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/search.php");
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
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}
else
{
    $seq = $myAuth->sequence();
    $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq);
    echo "Not logged in";
    die();
}

// Validate request and compile XML
if( ($reqObject=='site') || ($reqObject=='tree') )
{
    if($reqID)
    {
        $xmlrequest = "<corina><request type=\"search\" format=\"summary\"><searchParams returnObject=\"$reqObject\"><param name=\"".$reqObject."id"."\" operator=\"=\" value=\"".$reqID."\" /></searchParams></request></corina>";
    }
    else
    {
        $xmlrequest = "<corina><request type=\"search\" format=\"summary\"><searchParams returnObject=\"$reqObject\"><all/></searchParams></request></corina>";
    }
}
elseif($reqObject=='measurement')
{
    $xmlrequest = "<corina><request type=\"search\" format=\"summary\"><searchParams returnObject=\"$reqObject\"><param name=\"".$reqObject."id"."\" operator=\"=\" value=\"".$reqID."\" /></searchParams></request></corina>";
    //echo $xmlrequest;
}
else
{
    echo "unknown object type";
    die();
}

$myRequest = new request($myMetaHeader, $myAuth, $xmlrequest);
$myRequest->createParamObjects();

foreach ($myRequest->getParamObjectsArray() as $paramObj)
{
    $myObject = new search();
    
    // Before doing anything else check the request parameters are valid
    if($myMetaHeader->status != "Error")
    {
        $success = $myObject->validateRequestParams($paramObj, $myRequest->getCrudMode());
        if(!$success)
        {

            trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), $defaultErrType);
            continue;
        }
    }
   
   
    if($myMetaHeader->status != "Error")
    {
        $success = $myObject->doSearch($paramObj, $myAuth, 'false', $myRequest->getFormat());
        if(!$success)
        {
            if ($myObject->getLastErrorCode()=='103')
            {
                // Permission denied so just raise a notice not an error
                trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_NOTICE);
            }
            else
            {
                // Full blown error
                trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
            }
        }
    }
    $xmldata.="<sql>".htmlSpecialChars($myObject->sqlcommand)."</sql>";
}



// Get XML representation of data
if($myMetaHeader->status != "Error")
{
    $xmldata.= $myObject->asXML();
}
        
//writeOutput($myMetaHeader, $xmldata);
writeGMapOutput(createOutput($myMetaHeader, $xmldata), $myRequest);
