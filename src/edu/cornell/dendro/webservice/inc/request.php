<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This class contains the logic for extracting the meaning from the
 * user request and for storing these requests in parameter classes.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
 * *******************************************************************
 */
require_once("inc/parameters.php");
require_once("inc/dbhelper.php");
require_once("inc/xmlhelper.php");
require_once("inc/output.php");

/**
 * Class to handle the requests from a user.  Contains the logic for parsing XML requests and or creating parameter objects.
 *
 */
class request
{ 
    protected $xmlrequest                 = NULL;
    protected $xmlRequestDom			  = NULL;
    protected $paramObjectsArray          = array(); 
    protected $crudMode                   = NULL;
    protected $format                     = 'standard';
  
    var $includePermissions               = FALSE;

    
    /*************/
    /*CONSTRUCTOR*/
    /*************/
    
    function __construct($xmlrequest)
    {
        $this->logRequest($xmlrequest);
        $this->validateXML(stripslashes($xmlrequest));
    }

    /************/
    /*DESTRUCTOR*/
    /************/    
    
    function __destruct()
    { 
    }
  
    
    private function setCrudMode($crudMode)
    {
    	global $myMetaHeader;
    	global $myAuth;   	
    	
    	// If using a known client then check that the version is compatible
   		if($myMetaHeader->isClientVersionValid()===FALSE)
   		{
   			if($myMetaHeader->getClientName()===FALSE)
   			{
   				trigger_error("107"."The client that you are using is not recognised/supported by this webservice.", E_USER_ERROR);
   			}
   			else
   			{
   				trigger_error("107"."The version of ".$myMetaHeader->getClientName()." that you are using (v".$myMetaHeader->getClientVersion().") to connect to the Corina webservice is no longer supported.\n"  
   						     ."Please download an updated version (>".$myMetaHeader->getMinRequiredClientVersion().") and try again.", E_USER_ERROR);
   			}	
   		}
    	
    	$this->crudMode = strtolower($crudMode);
    	$myMetaHeader->setRequestType($this->getCrudMode());
    	
    	
		// Check authentication and request login if necessary
		if($myAuth->isLoggedIn())
		{
		    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
		}
		elseif( ($this->getCrudMode()=="nonce"))
		{
		
		}
		elseif( ($this->getCrudMode()!="plainlogin") && ($this->getCrudMode()!="securelogin"))
		{
		    // User is not logged in and is either requesting a nonce or isn't trying to log in at all
		    // so request them to log in first
		    $seq = $myAuth->sequence();
		    $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq);
		}
		
		if($this->getCrudMode()=='logout')
		{
		    $myAuth->logout();
		    writeHelpOutput($myMetaHeader);
		    die;
		}
		
		if($this->getCrudMode()== "help")
		{
		    // Output the resulting XML
		    writeHelpOutput($myMetaHeader);
		    die;
		}
		    	    	
    }
    
    /***********/
    /*FUNCTIONS*/
    /***********/   
   
    /**
     * Validate the request XML against the schema
     *
     * @param String $xmlrequest
     * @return unknown
     */
    function validateXML($xmlrequest)
    {
        global $corinaXSD;
        global $corinaNS;
        global $tridasNS;
        

        $origErrorLevel = error_reporting(E_ERROR);
        $xmlrequest = dbHelper::xmlSpecialCharReplace($xmlrequest);
        $doc = new DomDocument;
        $doc->loadXML($xmlrequest);
        
        // Handle validation errors ourselves
        libxml_use_internal_errors(true);

        // Do the validation
        if($doc->schemaValidate($corinaXSD))
        {      
        	$this->xmlrequest = $xmlrequest;
            $this->xmlRequestDom = $doc;
            
            // Extract basic request information
            $requesttag = $doc->getElementsByTagName("request")->item(0);
            $this->setCrudMode($requesttag->getAttribute("type"));          
            
    
            if($requesttag->getAttribute("format")!=NULL) $this->format = strtolower($requesttag->getAttribute("format"));
            $this->includePermissions = dbHelper::formatBool($requesttag->getAttribute("includePermissions"));
            
            // Override format to standard if doing a 'delete'
            if ($this->getCrudMode()=='delete') $this->format = 'standard';
            
            
            error_reporting($origErrorLevel);
            return true;
        }
        else
        {
        	trigger_error("905"."The XML request does not validate against the schema. ".$this->xml_validation_errors($xmlrequest), E_USER_ERROR);
            error_reporting($origErrorLevel);
            return false;
        }
 
    }

    /**
     * Log this request in the database request log
     *
     * @param unknown_type $xmlrequest
     */
    function logRequest($xmlrequest)
    {
        global $dbconn;
        global $wsversion;
        global $myAuth;

        if ($xmlrequest)
        {
            $request = $xmlrequest;
        }
        else
        {
            $request = $_SERVER['REQUEST_URI'];
        }

        if($myAuth->getID()==NULL)
        {
            $sql = "insert into tblrequestlog (request, ipaddr, wsversion, page, client) values ('".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion', '".$_SERVER['SCRIPT_NAME']."', '".addslashes($_SERVER['HTTP_USER_AGENT'])."')";
        }
        else
        {
            $sql = "insert into tblrequestlog (securityuserid, request, ipaddr, wsversion, page, client) values ('".$myAuth->getID()."', '".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion', '".$_SERVER['SCRIPT_NAME']."', '".addslashes($_SERVER['HTTP_USER_AGENT'])."')";
        }

        pg_send_query($dbconn, $sql);
        $result = pg_get_result($dbconn);
        if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
        {
            echo pg_result_error($result)."--- SQL was $sql";
        }

    }
    
    /**
     * Parse the XML and create a parameters class from this request
     *
     * @return unknown
     */
    function createParamObjects()
    {
        global $domain;
        global $corinaNS;
        global $tridasNS;

        $xpath = new DOMXPath($this->xmlRequestDom);
       	$xpath->registerNamespace('cor', $corinaNS);
       	$xpath->registerNamespace('tridas', $tridasNS);
        
        
    	//******
    	//SEARCH
    	//******
        if($this->crudMode=='search')
        {
        	$fragment = $xpath->query("//cor:request[(descendant::cor:searchParams)]");
	       	
        	foreach ($fragment as $item)
        	{	
        		if($item->nodeType != XML_ELEMENT_NODE) continue;  
        		
	            $parentID = NULL;
	            $myParamObj = new searchParameters($this->xmlRequestDom->saveXML($item), $parentID);
	            array_push($this->paramObjectsArray, $myParamObj);
        	}
        }
        
    	//***************
    	//READ and DELETE
    	//***************        
        elseif ( ($this->crudMode=='read') || ($this->crudMode=='delete'))
        {
        	if($this->xmlRequestDom->getElementsByTagName("entity")->item(0)!=NULL)
        	{
	            foreach ($this->xmlRequestDom->getElementsByTagName("entity") as $item)
	            {
	                $parentID = NULL;
	                switch(strtolower($item->getAttribute('type')))
	                {
	                	case 'object':    
	                		$newxml = "<tridas:object><identifier domain=\"$domain\">".$item->getAttribute('id')."</identifier></tridas:object>";
	                		$myParamObj = new objectParameters($newxml, $parentID);
                            break;	                		

                        case 'element':    
	                		$newxml = "<tridas:element><identifier domain=\"$domain\">".$item->getAttribute('id')."</identifier></tridas:element>";
	                		$myParamObj = new elementParameters($newxml, $parentID);
                            break;	                		
                            
	                	case 'sample':
	                		$newxml = "<tridas:sample><identifier domain=\"$domain\">".$item->getAttribute('id')."</identifier></tridas:sample>";
	                		$myParamObj = new sampleParameters($newxml, $parentID);
                            break;

	                	case 'radius':
	                		$newxml = "<tridas:radius><identifier domain=\"$domain\">".$item->getAttribute('id')."</identifier></tridas:radius>";
	                		$myParamObj = new radiusParameters($newxml, $parentID);
                            break;
                            
	                	case 'measurementseries':

	                		$newxml = "<tridas:measurementSeries><identifier domain=\"$domain\">".$item->getAttribute('id')."</identifier></tridas:measurementSeries>";
	                		$myParamObj = new measurementParameters($newxml, $parentID);
                            break;
                            
	                	case 'derivedseries':
	                		$newxml = "<tridas:derivedSeries><identifier domain=\"$domain\">".$item->getAttribute('id')."</identifier></tridas:derivedSeries>";
	                		$myParamObj = new measurementParameters($newxml, $parentID);
                            break;	 
                                                        
	                	default:
	                		trigger_error("901"."Unknown entity type specified", E_USER_ERROR);
	                }
	                
	                array_push($this->paramObjectsArray, $myParamObj);
	            }
        	}
        	elseif($this->xmlRequestDom->getElementsByTagName("dictionaries")->item(0)!=NULL)
        	{
        		$myParamObj = new dictionariesParameters(null, null);
        		array_push($this->paramObjectsArray, $myParamObj);
        	}
            else
            {
                trigger_error("905"."Invalid XML request - read or delete requests require entity tags", E_USER_ERROR);
            }
        
        }
        elseif ( ($this->crudMode=='create') || ($this->crudMode=='update') || ($this->crudMode=='assign') || ($this->crudMode=='unassign') )
        {

            // Extract ID of Parent entity if supplied
            $requestTag = $this->xmlRequestDom->getElementsByTagName("request")->item(0);
            if($requestTag!=NULL) $parentID = $requestTag->getAttribute("parentEntityID");
			
        	$fragment = $xpath->query("/cor:corina/cor:request/*");


            foreach ($fragment as $item)
            {
            	switch($item->tagName)
            	{
            		case "tridas:element":
            			$myParamObj = new elementParameters($this->xmlRequestDom->saveXML($item), $parentID);
            			break;
            		case "tridas:sample":
            			$myParamObj = new sampleParameters($this->xmlRequestDom->saveXML($item), $parentID);
            			break;
            		case "tridas:object":
            			$myParamObj = new objectParameters($this->xmlRequestDom->saveXML($item), $parentID);
            			break;
            		case "tridas:radius":
            			$myParamObj = new radiusParameters($this->xmlRequestDom->saveXML($item), $parentID);
            			break;        
            		case "tridas:measurementSeries":
            			$myParamObj = new measurementParameters($this->xmlRequestDom->saveXML($item), $parentID);
            			break;
            		case "tridas:derivedSeries":
            			$myParamObj = new measurementParameters($this->xmlRequestDom->saveXML($item), $parentID);
            			break;    			
            		default:
            			trigger_error("901"."Unknown entity tag &lt;".$item->tagName."&gt; when trying to ".$this->crudMode." a record", E_USER_ERROR);
            	}
            	
                if(isset($myParamObj)) array_push($this->paramObjectsArray, $myParamObj);

            }



/*
            if($this->xmlrequest->xpath('//dictionaries'))
            {
                $parentID = NULL;
                $item = $this->xmlrequest->xpath('//request/searchParams');
                $myParamObj = new dictionariesParameters($this->metaHeader, $this->auth, $item, $parentID);
                array_push($this->paramObjectsArray, $myParamObj);
            }
        */
            /*
            if($this->xmlrequest->xpath('//subSite'))
            {
                foreach ($this->xmlrequest->xpath('//subSite') as $item)
                {
                    $parentID = NULL;
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->site['id']))
                    {
                        $parentID = (int) $parent[0]->site['id'][0];
                    }
                    $myParamObj = new subSiteParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//element'))
            {
                foreach ($this->xmlrequest->xpath('//element') as $item)
                {
                    $parentID = NULL;
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->subSite['id']))
                    {
                        $parentID = $parent[0]->subSite['id'];
                    }
                    $myParamObj = new elementParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->request->children('http://www.tridas.org/1.1'));
            {
                foreach ($this->xmlrequest->request->children('http://www.tridas.org/1.1') as $item)
                {
                    $myParamObj = new sampleParameters($item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//radius'))
            {
                foreach ($this->xmlrequest->xpath('//radius') as $item)
                {
                    $parentID = "";
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->sample['id']))
                    {
                        $parentID = $parent[0]->sample['id'];
                    }
                    $myParamObj = new radiusParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//measurement'))
            {
                foreach ($this->xmlrequest->xpath('//measurement') as $item)
                {
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->radius['id']))
                    {
                        $parentID = $parent[0]->radius['id'];
                        $myParamObj = new measurementParameters($this->metaHeader, $this->auth, $item, $parentID);
                        array_push($this->paramObjectsArray, $myParamObj);
                    }
                    elseif(isset($parent[0]->references))
                    {
                        // this measurement is a reference within a derived measurement so ignore
                    }
                    elseif(isset($parent[0]->basedOn))
                    {
                        // this measurement is a reference within a derived measurement so ignore
                    }
                    else
                    {
                        $parentID = "";
                        $myParamObj = new measurementParameters($this->metaHeader, $this->auth, $item, $parentID);
                        array_push($this->paramObjectsArray, $myParamObj);
                    }
                }
            }
            
            if($this->xmlrequest->xpath('//request/siteNote'))
            {
                foreach ($this->xmlrequest->xpath('//request/siteNote') as $item)
                {
                    $parentID = "";
                    $myParamObj = new siteNoteParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//request/elementNote'))
            {
                foreach ($this->xmlrequest->xpath('//request/elementNote') as $item)
                {
                    $parentID = "";
                    $myParamObj = new elementNoteParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//request/measurementNote'))
            {
                foreach ($this->xmlrequest->xpath('//request/measurementNote') as $item)
                {
                    $parentID = "";
                    $myParamObj = new vmeasurementNoteParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//request/readingNote'))
            {
                foreach ($this->xmlrequest->xpath('//request/readingNote') as $item)
                {
                    $parentID = "";
                    $myParamObj = new readingNoteParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//request/taxon'))
            {
                foreach ($this->xmlrequest->xpath('//request/taxon') as $item)
                {
                    $parentID = "";
                    $myParamObj = new taxonParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//request/user'))
            {
                foreach ($this->xmlrequest->xpath('//request/user') as $item)
                {
                    $parentID = "";
                    $myParamObj = new securityUserParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//request/securityGroup'))
            {
                foreach ($this->xmlrequest->xpath('//request/securityGroup') as $item)
                {
                    $parentID = "";
                    $myParamObj = new securityGroupParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }*/
         

        }
        elseif ( ($this->crudMode=='plainlogin') || ($this->crudMode=='securelogin') || ($this->crudMode=='nonce') ) 
        { 	
        	$authTag = $this->xmlRequestDom->getElementsByTagName("authenticate")->item(0);
       		$myParamObj = new authenticationParameters($this->xmlRequestDom->saveXML($authTag));
       		array_push($this->paramObjectsArray, $myParamObj);  
        }
        
        else
        {
            trigger_error("667"."Program bug - unable to determine request mode but shouldn't have got this far!", E_USER_ERROR);
        }


        // Check that at least one parameters object has been extracted from the xml request
        if(sizeof($this->paramObjectsArray)>0)
        {
            return true;
        }
        else
        {
            trigger_error("905"."Invalid XML request - unable to extract a meaningful request from your XML document.", E_USER_ERROR);
            return false;
        }

    }


    /*********/
    /*GETTERS*/
    /*********/

    /**
     * Get this format for this request.  Normally 'standard'
     *
     * @return unknown
     */
    function getFormat()
    {
        return $this->format;
    }

    /**
     * Returns the number of parameter objects of a specified type.
     *
     * @return unknown
     */
    function countParamObjects()
    {
        return count($this->paramObjectsArray);
    }
    
    /**
     * Get the 'crud' mode for this request (create, read, update or delete)
     *
     * @return unknown
     */
    function getCrudMode()
    {
        return $this->crudMode;
    }
    
    /**
     * Get this request in the form of a parameters class
     *
     * @return unknown
     */
    function getParamObjectsArray()
    {
        return $this->paramObjectsArray;
    }


    private function libxml_display_error($error, $xmlRequest=NULL)
    {

        $message ="";
        
        // Include the actual error message
        $message .= trim($error->message);

        // If $xmlRequest has been specified then try and grab the dodgy line
        if (isset($xmlRequest))
        { 
            $codeArray = explode('<br />', nl2br($xmlRequest));
            $problemCode = $codeArray[$error->line-1];

            $message .= " This error is on line $error->line";

            // Include the column number if available 
            if($error->column > 0)
            {
                $message .= ", column $error->column";
            }
            $message .= " and is shown below: <sourceCode>".dbHelper::escapeXMLChars($problemCode)."</sourceCode> ";
        }
        else
        {
            $message .= " There is an error on line $error->line. ";
        }


        // Return the string
        return $message;
    }
    
    private function xml_validation_errors($xmlRequest=NULL) {
        $message ="";
        $errors = libxml_get_errors();
        foreach ($errors as $error) {
            $message .= $this->libxml_display_error($error, $xmlRequest);
        }
        libxml_clear_errors();
        return $message;
    }


}


?>
