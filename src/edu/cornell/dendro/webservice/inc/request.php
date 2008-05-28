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
    protected $format                     = NULL;

    function __construct($metaHeader, $auth, $xmlrequest)
    {
        $this->metaHeader = $metaHeader;
        $this->auth = $auth;
        $this->logRequest($xmlrequest);
        $this->validateXML(stripslashes($xmlrequest));
    }

    function __destruct()
    {
        
    }

    function validateXML($xmlrequest)
    {
        global $rngSchema;
        $origErrorLevel = error_reporting(E_ERROR);

        $xmlrequest = xmlSpecialCharReplace($xmlrequest);

        // Extract parameters from XML
        $doc = new DomDocument;
        $doc->loadXML($xmlrequest);


        if($doc->relaxNGValidate($rngSchema))
        {
            $this->xmlrequest = simplexml_load_string($xmlrequest);
            if($this->xmlrequest)
            {
                $this->crudMode = strtolower($this->xmlrequest->request['type']);
                $this->format = strtolower($this->xmlrequest->request['format']);
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
            $myErrorObj = error_get_last();
            $myError = explode(":", $myErrorObj['message']);
            $this->metaHeader->setMessage("905", "XML does not validate against schema. ".end($myError).".");
            error_reporting($origErrorLevel);
            return false;
        }
    }

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
            $sql = "insert into tblrequestlog (request, ipaddr, wsversion, page) values ('".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion', '".$_SERVER['SCRIPT_NAME']."')";
        }
        else
        {
            $sql = "insert into tblrequestlog (securityuserid, request, ipaddr, wsversion, page) values ('".$this->auth->getID()."', '".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion', '".$_SERVER['SCRIPT_NAME']."')";
        }

        pg_send_query($dbconn, $sql);
        $result = pg_get_result($dbconn);
        if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
        {
            echo pg_result_error($result)."--- SQL was $sql";
        }

    }
    

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
            
            if($this->xmlrequest->xpath('//tree'))
            {
                foreach ($this->xmlrequest->xpath('//tree') as $item)
                {
                    $parentID = NULL;
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->subSite['id']))
                    {
                        $parentID = $parent[0]->subSite['id'];
                    }
                    $myParamObj = new treeParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//specimen'))
            {
                foreach ($this->xmlrequest->xpath('//specimen') as $item)
                {
                    $parentID = NULL;
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->tree['id']))
                    {
                        $parentID = $parent[0]->tree['id'];
                    }
                    $myParamObj = new specimenParameters($this->metaHeader, $this->auth, $item, $parentID);
                    array_push($this->paramObjectsArray, $myParamObj);
                }
            }
            
            if($this->xmlrequest->xpath('//radius'))
            {
                foreach ($this->xmlrequest->xpath('//radius') as $item)
                {
                    $parentID = "";
                    $parent = $item->xpath('../..');
                    if(isset($parent[0]->specimen['id']))
                    {
                        $parentID = $parent[0]->specimen['id'];
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
            
            if($this->xmlrequest->xpath('//request/treeNote'))
            {
                foreach ($this->xmlrequest->xpath('//request/treeNote') as $item)
                {
                    $parentID = "";
                    $myParamObj = new treeNoteParameters($this->metaHeader, $this->auth, $item, $parentID);
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
        }
        elseif ( ($this->crudMode=='plainlogin') || ($this->crudMode=='securelogin') ) 
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


    // GETTERS
    //

    function getFormat()
    {
        return $this->format;
    }
    
    function getCrudMode()
    {
        return $this->crudMode;
    }
    
    function getParamObjectsArray()
    {
        return $this->paramObjectsArray;
    }


}

?>
