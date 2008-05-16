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
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/output.php");

require_once("inc/site.php");
require_once("inc/subSite.php");
require_once("inc/tree.php");
require_once("inc/specimen.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/authenticate.php");
require_once("inc/dictionaries.php");
require_once("inc/search.php");


$xmldata = NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();



// Create request object from supplied XML document
if($_POST['xmlrequest'])
{
    $myRequest  = new request($myMetaHeader, $myAuth, $_POST['xmlrequest']);
}
else
{
    trigger_error('902'.'No XML request file given');
    $myMetaHeader->setRequestType("help");
    writeHelpOutput($metaHeader);
    die();
}


// Extract the type of request from XML doc
$myMetaHeader->setRequestType($myRequest->getCrudMode());


// Check authentication and request login if necessary
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}
elseif( ($myRequest->getCrudMode()!="plainlogin") && ($myRequest->getCrudMode()!="securelogin"))
{
    // User is not logged in and is either requesting a nonce or isn't trying to log in at all
    // so request them to log in first
    $myMetaHeader->requestLogin($myAuth->nonce());
}


if($myRequest->getCrudMode()== "Help")
{
    // Output the resulting XML
    writeHelpOutput($myMetaHeader);
    die;
}
// If there have been no errors so far go ahead and process the request
elseif($myMetaHeader->status != "Error")
{
    // create parameter objects from sections of the request XML document
    $myRequest->createParamObjects();

    // Process each Param object in turn during monster foreach loop!
    foreach ($myRequest->getParamObjectsArray() as $paramObj)
    {
        // Create classes to hold data in, based on type of sections in xml request 
        switch(get_class($paramObj))
        {
            case "siteParameters":
                $myObject = new site();
                break;
            case "subSiteParameters":
                $myObject = new subSite();
                break;
            case "treeParameters":
                $myObject = new tree();
                break;
            case "specimenParameters":
                $myObject = new specimen();
                break;
            case "radiusParameters":
                $myObject = new radius();
                break;
            case "measurementParameters":
                $myObject = new measurement();
                break;
            case "siteNoteParameters":
                $myObject = new siteNote();
                break;
            case "treeNoteParameters":
                $myObject = new treeNote();
                break;
            case "vmeasurementNoteParameters":
                $myObject = new vmeasurementNote();
                break;
            case "readingNoteParameters":
                $myObject = new readingNote();
                break;
            case "authenticationParameters":
                $myObject = new authenticate();
                break;
            case "searchParameters":
                $myObject = new search();
                break;
            case "dictionariesParameters":
                $myObject = new dictionaries();
                break;
            default:
                trigger_error("104"."The parameter object '".get_class($paramObj)."'  is unsupported");
        }
        
        // Get the name of the object (minus the Parameters bit)
        $objectType = substr(get_class($paramObj), 0, -10);
        
        // Before doing anything else check the request parameters are valid
        if($myMetaHeader->status != "Error")
        {
            $success = $myObject->validateRequestParams($paramObj, $myRequest->getCrudMode());
            if(!$success)
            {
                trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
            }
        }


        // If doing a create and the current object has a child then skip because
        // the focus of the create request is the child
        if (($myRequest->getCrudMode()=='create') && ($paramObj->hasChild===TRUE) )
        {
            continue;
        }

        // ********************
        //  DO PERMISSION CHECK FOR CREATE, READ, UPDATE OR DELETE REQUESTS
        //  search request permissions are handled by inc/search.php
        //  other requests do not need to be verified
        // ********************

        if ($myRequest->getCrudMode()=='create')
        {
            // For create requests we need the type and id of the parent to check permissions
            switch ($objectType)
            {
                case "site":
                    $myID = NULL;
                    $objectType="default";
                    break;
                case "subSite":
                    $myID = $paramObj->siteID;
                    $objectType="site";
                    break;
                case "tree":
                    $myID = $paramObj->subSiteID;
                    $objectType="subSite";
                    break;
                case "specimen":
                    $myID = $paramObj->treeID;
                    $objectType="tree";
                    break;
                case "radius":
                    $myID = $paramObj->specimenID;
                    $objectType="specimen";
                    break;
                case "measurement":
                    $myID = $paramObj->radiusID;
                    $objectType="radius";
                    break;

                // These objects don't have parents                
                case "siteNote":
                    $myID = $paramObj->id;
                    break;
                case "treeNote":
                    $myID = $paramObj->id;
                    break;
                case "vmeasurementNote":
                    $myID = $paramObj->id;
                    break;
                case "readingNote":
                    $myID = $paramObj->id;
                    break;
            }
        }
        elseif( ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete'))
        {
            $myID = $paramObj->id;
        }

        if( ($myRequest->getCrudMode()=='create') || ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete'))
        {
            // Do permissions check
            if($myAuth->getPermission($myRequest->getCrudMode(), $objectType, $myID)===FALSE)
            {
                // Permission denied
                trigger_error("103"."Permission to ".$myRequest->getCrudMode()." ".$objectType."id $myID was denied.");
                break;
            }
        }


        // ********************
        // DOING SECURE LOGIN
        // ********************

        if($myRequest->getCrudMode()=='securelogin')
        {
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->doSecureAuthentication($paramObj, $myAuth);
                if($success)
                {
                    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
                }
                else
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
                }
            }
        }
        
        // ********************
        // DOING PLAIN LOGIN
        // ********************
        
        if($myRequest->getCrudMode()=='plainlogin') 
        {
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->doPlainAuthentication($paramObj, $myAuth);
                if($success)
                {
                    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
                }
                else
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
                }
            }
        }

        // ********************
        // Populate class with data stored in db 
        // ********************
       
        if( ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete') )
        {
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->setParamsFromDB($paramObj->id);
                $success2 = $myObject->setChildParamsFromDB();
                if(!($success && $success2))
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
                }
            }
        }

        // ********************
        // Set parameters to values requested by user and save them to the db
        // ********************
       
        if( ($myRequest->getCrudMode()=='create') || ($myRequest->getCrudMode()=='update'))
        {
            // Update parameters in object
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->setParamsFromParamsClass($paramObj, $myAuth);
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
                }
            }
            
            // Write object to db
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->writeToDB();
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
                }
            }
        }

        // ********************
        // Delete record from DB
        // ********************
       
        if( ($myRequest->getCrudMode()=='delete') )
        {
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->deleteFromDB();
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
                }
            }
        }

        // ********************
        // Do a search for records
        // ********************
       
        if($myRequest->getCrudMode()=='search')
        {
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->doSearch($paramObj, $myAuth);
                if(!$success)
                {
                    if ($myObject->getLastErrorCode()=='103')
                    {
                        // Permission denied so just raise a warning not an error
                        trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_WARNING);
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

        // ********************
        // Get XML representation of data
        // ********************
        if($myMetaHeader->status != "Error")
        {
            $xmldata.= $myObject->asXML();
        }
        

    // END Monster Foreach loop    
    }

// END Monster IF
}

// ***********
// OUTPUT DATA
// ***********
switch ($myRequest->getFormat())
{
    case "kml":
        writeKMLOutput($xmldata);
        break;
    case "data":
        writeOutput($myMetaHeader, $xmldata);
        break;
    case "map":
        writeGMapOutput(createOutput($myMetaHeader, $xmldata), $myRequest);
        break;
    default:
        writeOutput($myMetaHeader, $xmldata);
}
