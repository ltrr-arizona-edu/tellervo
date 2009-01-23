<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */

require_once("config.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/output.php");
require_once("inc/object.php");
require_once("inc/element.php");
require_once("inc/sample.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/authenticate.php");
require_once("inc/dictionaries.php");
require_once("inc/search.php");


$xmldata 		= NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();


// Create request object from supplied XML document
if($_POST['xmlrequest'])
{
    $myRequest  = new request($_POST['xmlrequest']);
}
else
{
    trigger_error('902'.'No XML request file given', E_USER_ERROR);
    $myMetaHeader->setRequestType("help");
    writeHelpOutput($myMetaHeader);
    die();
}

// If there have been no errors so far go ahead and process the request
if($myMetaHeader->status != "Error")
{
    // create parameter objects from sections of the request XML document
    $myRequest->createParamObjects();

    // If there is more than one parameter objects set the default err message to 'notice'
    // so that the remaining objects can be processed
    /*if( ($myRequest->getCrudMode()=='search') && ($myRequest->countParamObjects()>1) )
    {
        $defaultErrType = E_USER_NOTICE;
    }
    else
    {
        $defaultErrType = E_USER_ERROR;
    }*/

    $defaultErrType = E_USER_ERROR;

    // Process each Param object in turn during monster foreach loop!
    foreach ($myRequest->getParamObjectsArray() as $paramObj)
    {
        // Create classes to hold data in, based on type of sections in xml request 
        switch(get_class($paramObj))
        {
            case "elementParameters": 			$myObject = new element(); break;
            case "sampleParameters":  			$myObject = new sample(); break;
            case "radiusParameters": 			$myObject = new radius(); break;
            case "objectParameters": 			$myObject = new object(); break;            
            //case "measurementParameters": 		$myObject = new measurement(); break;
            //case "siteNoteParameters": 			$myObject = new siteNote(); break;
            //case "elementNoteParameters": 		$myObject = new elementNote(); break;
            //case "vmeasurementNoteParameters": 	$myObject = new vmeasurementNote(); break;
            //case "readingNoteParameters": 		$myObject = new readingNote(); break;
            case "authenticationParameters": 	$myObject = new authenticate(); break;
            case "searchParameters": 			$myObject = new search(); break;
            //case "dictionariesParameters": 		$myObject = new dictionaries(); break;
            //case "securityUserParameters": 		$myObject = new securityUser(); break;
            //case "securityGroupParameters":		$myObject = new securityGroup(); break;
            default:
            	trigger_error("104"."The parameter object '".get_class($paramObj)."'  is unsupported", E_USER_ERROR);
        }

        // Get the name of the object (minus the Parameters bit)
        $objectType = substr(get_class($paramObj), 0, -10);
         
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

        // ********************
        //  DO PERMISSION CHECK FOR CREATE, READ, UPDATE OR DELETE REQUESTS
        //  search request permissions are handled by inc/search.php
        //  other requests do not need to be verified
        // ********************

        // We may need to alter the object type, so store the original object type first
        $originalObjectType = $objectType;
    
        if ($myRequest->getCrudMode()=='create')
        {
            // For create requests we need the type and id of the parent to check permissions
            switch ($objectType)
            {
                case "element":
                    $myID = $paramObj->parentID;
                    $objectType="subSite";
                    break;
                case "sample":
                    $myID = $paramObj->parentID;
                    $objectType="element";
                    break;
                case "radius":
                    $myID = $paramObj->parentID;
                    $objectType="sample";
                    break;
                case "measurement":
                    $myID = $paramObj->parentID;
                    $objectType="radius";
                    break;

                // These objects don't have parents     
                case "object":
                	$myID = $paramObj->getID();
                	break;           
                case "siteNote":
                    $myID = $paramObj->id;
                    break;
                case "elementNote":
                    $myID = $paramObj->id;
                    break;
                case "vmeasurementNote":
                    $myID = $paramObj->id;
                    break;
                case "readingNote":
                    $myID = $paramObj->id;
                    break;
                case "securityUser":
                    $myID = $paramObj->id;
                    break;
                case "securityGroup":
                    $myID = $paramObj->id;
                    break;
            }
        }
        elseif( ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete'))
        {
            $myID = $paramObj->getID();
        }

        if( ($myRequest->getCrudMode()=='create') || ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete'))
        {
            // Do permissions check
            if($myAuth->getPermission($myRequest->getCrudMode(), $objectType, $myID)===FALSE)
            {
                // Permission denied
                if (($originalObjectType=='site') && ($objectType=='default'))
                {
                    // Permission determined from database default
                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." a ".$originalObjectType." was denied. ".$myAuth->authFailReason, $defaultErrType);
                    break;
                }
                if ($originalObjectType!=$objectType)
                {
                    // Permission determined from parent object so the error message should be set accordingly
                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." a ".$originalObjectType." associated with ".$objectType." ".$myID." was denied. ".$myAuth->authFailReason, $defaultErrType);
                    break;

                }
                else
                {
                    // Standard error message
                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." ".$objectType." id $myID was denied. ".$myAuth->authFailReason, $defaultErrType);
                    break;
                }
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
                    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
                }
                else
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
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
                    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
                }
                else
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }
        }

        // ********************
        // GET NONCE
        // ********************
        
        if($myRequest->getCrudMode()=='nonce') 
        {
            $myObject->setNonce($paramObj, $myAuth);

        }

        // ********************
        // Populate class with data stored in db 
        // ********************
                        	
        if( ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete') )
        {
            if($myMetaHeader->status != "Error")
            {            	
                $success = $myObject->setParamsFromDB($paramObj->getID());
                $success2 = $myObject->setChildParamsFromDB();
                if(!($success && $success2))
                {
                    if ($myObject->getLastErrorCode()==701)
                    {
                        trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                    }
                    else
                    {
                        trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_NOTICE);
                    }
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
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }

            // Write object to db
            if($myMetaHeader->status != "Error")
            {
                $success = $myObject->writeToDB();
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
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
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
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
                $success = $myObject->doSearch($paramObj, $myAuth, $myRequest->includePermissions, $myRequest->getFormat());
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
                        trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                    }
                }
            }
            $xmldata.="<sql>".htmlSpecialChars($myObject->sqlcommand)."</sql>";
        }


        // ********************
        // Include permissions in output when requested 
        // ********************
        if($myRequest->includePermissions===TRUE)
        {
            $myObject->getPermissions($myAuth->getID());
        }


        // ********************
        // Get XML representation of data
        // ********************
        if($myMetaHeader->status != "Error")
        {
            if($myRequest->getFormat()!=NULL)
            {
                $xmldata.= $myObject->asXML($myRequest->getFormat());
            }
            else
            {
                $xmldata.= $myObject->asXML();
            }
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
