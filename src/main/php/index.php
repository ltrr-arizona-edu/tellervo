<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

try{
require_once("config.php");
} catch (Exception $e)
{
	trigger_error('704'.'Tellervo server configuration file missing.  Contact your systems administrator');
}

try{
	require_once("systemconfig.php");
} catch (Exception $e)
{
	trigger_error('704'.'System configuration file missing.  Server administrator needs to run tellervo-server --reconfigure', E_USER_ERROR);
}
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
require_once("inc/loan.php");
require_once("inc/curation.php");
require_once("inc/box.php");
require_once("inc/permission.php");
require_once("inc/authenticate.php");
require_once("inc/dictionaries.php");
require_once("inc/search.php");
require_once("inc/tag.php");
require_once("inc/statistics.php");
require_once("inc/odkformdefinition.php");


$xmldata 		= NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();


if ($debugFlag===TRUE) $myMetaHeader->setTiming("Beginning request");

// Create request object from supplied XML document
if(isset($_POST['xmlrequest']))
{
    if($_POST['xmlrequest']!=NULL) 
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
}
else
{
	if($myMetaHeader->getClientName()=='Tellervo WSI')
	{
		trigger_error('902'.'This webservice expects a POST variable called xmlrequest inside which should be a string representation of your XML request document.  Please see the documentation for detailed information on how to interact with this webservice.', E_USER_ERROR);
	    $myMetaHeader->setRequestType("help");
	    writeHelpOutput($myMetaHeader);
	    die();
	}
	else
	{
		writeWelcomeOutput($myMetaHeader);
		die();
	}
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
            case "measurementParameters": 		$myObject = new measurement(); break;
            //case "readingNoteParameters": 		$myObject = new readingNote(); break;
            case "authenticationParameters": 		$myObject = new authenticate(); break;
            case "searchParameters": 			$myObject = new search(); break;
            case "dictionariesParameters": 		$myObject = new dictionaries(); break;
            case "securityUserParameters": 		$myObject = new securityUser(); break;
            case "securityGroupParameters":		$myObject = new securityGroup(); break;
            case "boxParameters":				$myObject = new box(); break;
            case "permissionParameters":		$myObject = new permission(); break;
            case "loanParameters":				$myObject = new loan(); break;
            case "curationParameters":			$myObject = new curation(); break;
            case "tagParameters":			$myObject = new tag(); break;
            case "statisticsParameters":			$myObject = new statistics(); break;
            case "odkFormDefinitionParameters":			$myObject = new odkFormDefinition(); break;

            default:
            	trigger_error("104"."The parameter object '".get_class($paramObj)."'  is unsupported", E_USER_ERROR);
            	echo "Object type not supported";
            	die();
        }

        $myObject->__construct();
        
        // Get the name of the object (minus the Parameters bit)
        $objectType = substr(get_class($paramObj), 0, -10);

        //$firebug->log($myObject, "My object");
        
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
        //  DO PERMISSION CHECK FOR CREATE, READ, UPDATE, DELETE, ASSIGN and UNASSIGN REQUESTS
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
                    $objectType="object";
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
                    $myID = $paramObj->getID();
                    break;
                case "securityGroup":
                    $myID = $paramObj->getID();
                    break;
                case "box":
                	$myID = null;
                	break;
                case "permission":
                	$myID = null;
                	break;
                case "loan":
                	$myID = null;
                	break;
                case "curation":
                	$myID = null;
                	break;
                case "tag":
                	$myID = null;
                	$objectType="tag";
                	break;                	
                case "odkFormDefinition":
                	$myID = null;
                	$objectType="odkFormDefinition";
                	break;                	
                default:
                	$firebug->log($objectType, "Unknown object type");
             
                  
            }
        }
        elseif( ($myRequest->getCrudMode()=='merge') || ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete'))
        {
            $myID = $paramObj->getID();
        }
       

	if( ($myRequest->getCrudMode()=='assign') || ($myRequest->getCrudMode()=='unassign'))
	{
	    if($objectType!="tag")
	    {
	      trigger_error("901"."The assign and unassign request types are only applicable to the tag entity", $defaultErrType);
	    }	
	}
       
       
        if( ($myRequest->getCrudMode()=='create') || ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete') )
        {
        	
            // Do permissions check
	    $firebug->log("Beginning permissions check...");

	    $hasPermission = $myAuth->getPermission($myRequest->getCrudMode(), $objectType, $myID, $paramObj);
	    	    
            if($hasPermission!=TRUE)
            {
                // Permission denied
                if (($originalObjectType=='object') && ($objectType=='default'))
                {
                    // Permission determined from database default
                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." a ".$originalObjectType." was denied. ".$myAuth->authFailReason, $defaultErrType);
                    break;
                }
                
                /**
                 * TEMPORARY BODGE FIX
                 * 
                 * Need to handle inheritance of permissions for derived series.
                 */
                if(($myRequest->getCrudMode()=='create') && $originalObjectType=='measurement')
				{
					if( ($paramObj->getType()=="Sum") || ($paramObj->getType()=="Index") || ($paramObj->getType()=="Redate") || ($paramObj->getType()=="Crossdate") || ($paramObj->getType()=="Truncate"))
					{
						// For now give everyone permission to *create* a derived series
					}
					else
					{
						// Permission determined from parent object so the error message should be set accordingly
	                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." a ".$originalObjectType." associated with ".$objectType." ".$myID." was denied. ".$myAuth->authFailReason, $defaultErrType);
	                    break;
					}
				}
			
                else if ($originalObjectType!=$objectType)
                {
                    // Permission determined from parent object so the error message should be set accordingly
                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." a ".$originalObjectType." associated with ".$objectType." ".$myID." was denied. ".$myAuth->authFailReason, $defaultErrType);
                    break;

                }
                else
                {
                    // Standard error message
                    trigger_error("103"."Permission to ".$myRequest->getCrudMode()." ".$objectType." was denied. ".$myAuth->authFailReason, $defaultErrType);
                    break;
                }
            }
            if ($debugFlag===TRUE) $myMetaHeader->setTiming("Permissions check completed");
           	$firebug->log("Permissions check completed");	
        }
        
        // Merges should only be done by admins
	if( $myRequest->getCrudMode()=='merge')
	{
		if(!$myAuth->isAdmin())
		{
			trigger_error("103"."Merges can only be done by administrators", $defaultErrType);
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
                    // Override these so that the WS with continue as if its a 'read' request and will show the
                    // user their credentials
                    $myObject = new securityUser(); 
                    $paramObj = new securityUserParameters("<request></request>");
                   	$paramObj->setID($myAuth->getID());
                                        
                }
                else
                {
                	$myAuth->logout();
					$seq = $myAuth->sequence();
				    $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq, 'OK');
		    
                    //trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
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
                    
                    // Override these so that the WS with continue as if its a 'read' request and will show the
                    // user their credentials
                    $myObject = new securityUser(); 
                    $paramObj = new securityUserParameters("<request></request>");
                   	$paramObj->setID($myAuth->getID());
                }
                else
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }
        }

        // ********************
        // Populate class with data stored in db 
        // ********************
                        	
        if( ($myRequest->getCrudMode()=='read') || ($myRequest->getCrudMode()=='update') || ($myRequest->getCrudMode()=='delete') ||
        	($myRequest->getCrudMode()=='plainlogin')  || ($myRequest->getCrudMode()=='securelogin') || ($myRequest->getCrudMode()=='merge') || 
        	($myRequest->getCrudMode()=='assign') || ($myRequest->getCrudMode()=='unassign'))
        {
            if($myMetaHeader->status != "Error")
            {   
		if($objectType=='permission')
		{
			// Special case for permissions as we need more than a basic id for looking up
			$success = $myObject->setParamsFromParamsClass($paramObj, $myAuth);
			if(!$success)
			{
			    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
			}
		}
		else
		{
			if ($debugFlag===TRUE) $myMetaHeader->setTiming("Calling setParamsFromDB on ".get_class($myObject));
			$success = $myObject->setParamsFromDB($paramObj->getID());	
			if ($debugFlag===TRUE) $myMetaHeader->setTiming("Completed setParamsFromDB on ".get_class($myObject));
			if ($debugFlag===TRUE) $myMetaHeader->setTiming("Calling setChildParamsFromDB on ".get_class($myObject));             	                 
			$success2 = $myObject->setChildParamsFromDB();
			if ($debugFlag===TRUE) $myMetaHeader->setTiming("Completed setChildParamsFromDB on ".get_class($myObject));                 
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
        }

        // ********************
        // Set parameters to values requested by user and save them to the db
        // ********************
       
        if( ($myRequest->getCrudMode()=='create') || ($myRequest->getCrudMode()=='update'))
        {
            // Update parameters in object
            if($myMetaHeader->status != "Error")
            {
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Setting parameters to new requested values");             	
                $success = $myObject->setParamsFromParamsClass($paramObj, $myAuth);
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }

            // Write object to db
            if($myMetaHeader->status != "Error")
            {
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Writing ".get_class($myObject)." to database");              	
                $success = $myObject->writeToDB($myRequest->getCrudMode());
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
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Deleting ".get_class($myObject)." from database");  
                $success = $myObject->deleteFromDB();
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }
        }

        
        
        // ********************
        // Assing/Unassign tag records from DB
        // ********************
        if( ($myRequest->getCrudMode()=='assign') || ($myRequest->getCrudMode()=='unassign'))
        {
            // Update parameters in object
            if($myMetaHeader->status != "Error")
            {
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Setting parameters to new requested values");             	
                $success = $myObject->setParamsFromParamsClass($paramObj, $myAuth);
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }

            // Write object to db
            if($myMetaHeader->status != "Error")
            {
            
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Writing ".get_class($myObject)." to database");              	
            	
            	if($myRequest->getCrudMode()=='assign')
            	{
			$success = $myObject->assignToSeries();
                }
                
                            	
            	if($myRequest->getCrudMode()=='unassign')
            	{
			$success = $myObject->unassignFromSeries();
                }
                
                if(!$success)
                {
                    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage(), E_USER_ERROR);
                }
            }
            
      	$firebug->log("Finished assign/unassign records");

        }
        
        // ********************
        // Do a search for records
        // ********************
       
        if($myRequest->getCrudMode()=='search')
        {
            if($myMetaHeader->status != "Error")
            {
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Beginning search...");              	
                $success = $myObject->doSearch($paramObj, $myAuth, $myRequest->includePermissions, $myRequest->getFormat());
            	if ($debugFlag===TRUE) $myMetaHeader->setTiming("Completed search");                       
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
            $xmldata.= $myObject->xmlDebugOutput();
        }

        // ********************
        // Merge records
        // ******************** 
        if($myRequest->getCrudMode()=='merge')
        {
            if($myMetaHeader->status != "Error")
            {
            	$myObject->mergeRecords($paramObj->mergeWithID);
       
            }
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
            if ($debugFlag===TRUE) $myMetaHeader->setTiming("Outputting XML...");

            if( ($myRequest->getCrudMode()=='delete') )
            {
                    // No return XML required for delete requests
            }
            else
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
            if ($debugFlag===TRUE) $myMetaHeader->setTiming("XML output complete");
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
