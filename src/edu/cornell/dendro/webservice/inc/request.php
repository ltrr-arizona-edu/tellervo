<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once("inc/parameters.php");
require_once("inc/dbhelper.php");


class request
{ 
    protected $xmlrequest                 = NULL;
    protected $auth                       = NULL;
    protected $metaHeader                 = NULL;
    protected $paramObjectsArray          = array(); 
    protected $crudMode                   = NULL;
    protected $format                     = 'standard';
  
    var $includePermissions               = FALSE;

    
    /*************/
    /*CONSTRUCTOR*/
    /*************/
    
    function __construct($metaHeader, $auth, $xmlrequest)
    {
        $this->metaHeader = $metaHeader;
        $this->auth = $auth;
        $this->logRequest($xmlrequest);
        $this->validateXML(stripslashes($xmlrequest));
    }

    /************/
    /*DESTRUCTOR*/
    /************/    
    
    function __destruct()
    { 
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
        $origErrorLevel = error_reporting(E_ERROR);

        $xmlrequest = xmlSpecialCharReplace($xmlrequest);

        // *** WARNING ***
        // Shameful kludge for making sure a lack of namespace in the XML request doesn't screw things up
        // This really needs fixing because it's soooo embarassing!
        $doc2 = new DomDocument;
        $doc2->loadXML($xmlrequest);
        if(!($doc2->documentElement->hasAttribute('xmlns')))
        {
            $doc2->documentElement->setAttributeNode(new DOMAttr('xmlns', 'http://dendro.cornell.edu/schema/corina/1.0'));
        }
        $doc = new DomDocument;
        $doc->loadXML($doc2->saveXML());
        // ****************

        // Handle validation errors ourselves
        libxml_use_internal_errors(true);

        // Do the validation
        if($doc->schemaValidate($corinaXSD))
        {
            $this->xmlrequest = simplexml_load_string($xmlrequest);
            if($this->xmlrequest)
            {
                $this->crudMode = strtolower($this->xmlrequest->request['type']);
                if(isset($this->xmlrequest->request['format'])) $this->format=strtolower($this->xmlrequest->request['format']);
                if(isset($this->xmlrequest->request['includePermissions'])) $this->includePermissions = fromStringtoPHPBool($this->xmlrequest->request['includePermissions']);
                error_reporting($origErrorLevel);
                return true;
            }
            else
            {
                $this->metaHeader->setMessage("905", "Unknown XML Errors");
                error_reporting($origErrorLevel);
                return false;
            }
        }
        else
        {
            $this->metaHeader->setMessage("905", "The XML request does not validate against the schema ".$this->xml_validation_errors($xmlrequest));
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

        if ($xmlrequest)
        {
            $request = $xmlrequest;
        }
        else
        {
            $request = $_SERVER['REQUEST_URI'];
        }

        if($this->auth->getID()==NULL)
        {
            $sql = "insert into tblrequestlog (request, ipaddr, wsversion, page, client) values ('".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion', '".$_SERVER['SCRIPT_NAME']."', '".addslashes($_SERVER['HTTP_USER_AGENT'])."')";
        }
        else
        {
            $sql = "insert into tblrequestlog (securityuserid, request, ipaddr, wsversion, page, client) values ('".$this->auth->getID()."', '".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion', '".$_SERVER['SCRIPT_NAME']."', '".addslashes($_SERVER['HTTP_USER_AGENT'])."')";
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
        if($this->crudMode=='search')
        {
            if($this->xmlrequest->xpath('//request/searchParams'))
            {
                foreach ($this->xmlrequest->xpath('//request/searchParams') as $item)
                {
                    $parentID = NULL;
                    $myParamObj = new searchParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            else
            {
                $this->metaHeader->setMessage("905", "Invalid XML request - search parameters have not been set for your search request");

            }   
        }
        elseif ( ($this->crudMode=='create') || ($this->crudMode=='read') || ($this->crudMode=='update') || ($this->crudMode=='delete') || ($this->crudMode=='assign') || ($this->crudMode=='unassign') )
        {
            if($this->xmlrequest->xpath('//dictionaries'))
            {
                $parentID = NULL;
                $item = $this->xmlrequest->xpath('//request/searchParams');
                $myParamObj = new dictionariesParameters($this->metaHeader, $this->auth, $item, $parentID);
                array_push($this->paramObjectsArray, $myParamObj);
            }
        
            if($this->xmlrequest->xpath('//site'))
            {
                foreach ($this->xmlrequest->xpath('//site') as $item)
                {
                    $parentID = NULL;
                    $myParamObj = new siteParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
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
            
            if($this->xmlrequest->xpath('//sample'))
            {
                foreach ($this->xmlrequest->xpath('//sample') as $item)
                {
                    $parentID = NULL;
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->element['id']))
                    {
                        $parentID = $parent[0]->element['id'];
                    }
                    $myParamObj = new sampleParameters($this->metaHeader, $this->auth, $item, $parentID);
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
            }
        }
        elseif ( ($this->crudMode=='plainlogin') || ($this->crudMode=='securelogin') || ($this->crudMode=='nonce') ) 
        {
            $parentID = NULL;
            $item = $this->xmlrequest->xpath('//authenticate');
            $myParamObj = new authenticationParameters($this->metaHeader, $this->auth, $item, $parentID);
            array_push($this->paramObjectsArray, $myParamObj);
        }
        else
        {
            $this->metaHeader->setMessage("667", "Program bug - unable to determine request mode but shouldn't have got this far!");
        }


        // Check that at least one parameters object has been extracted from the xml request
        if(sizeof($this->paramObjectsArray)>0)
        {
            return true;
        }
        else
        {
            $this->metaHeader->setMessage("905", "Invalid XML request - unable to extract a meaningful request from your XML document.");
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

        // If $xmlRequest has been specified then try and grab the dodgy line
        if (isset($xmlRequest))
        { 
            $codeArray = explode('<br />', nl2br($xmlRequest));
            $problemCode = $codeArray[$error->line-1];

            $message .= " because there is an error on line $error->line";

            // Include the column number if available 
            if($error->column > 0)
            {
                $message .= ", column $error->column";
            }
            $message .= ": <sourceCode>".escapeXMLChars($problemCode)."</sourceCode> ";
        }
        else
        {
            $message .= " because there is an error on line $error->line. ";
        }

        // Include the actual error message
        $message .= trim($error->message);

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
