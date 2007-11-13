<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

class request
{
    var $xmlrequest = NULL;
    var $simplexml = NULL;
    var $metaHeader =NULL;

    var $mode = "failed";
    var $id = NULL;
    var $code = NULL;
    var $name = NULL;
    var $label = NULL;
    var $taxonid = NULL;
    var $subsiteid = NULL;
    var $latitude = NULL;
    var $longitude = NULL;
    var $precision = NULL;

    function request($metaHeader)
    {
        $this->metaHeader = $metaHeader;
        $this->xmlrequest = stripslashes($_POST['xmlrequest']);
    }

    function readXML()
    {
        global $rngSchema;

        // Extract parameters from XML post
        $xmlstring = $this->xmlrequest;
        $doc = new DomDocument;
        $doc->loadXML($xmlstring);
        if($doc->relaxNGValidate($rngSchema))
        {

            $this->simplexml = simplexml_load_string($xmlstring);
            if($this->simplexml)
            {
                $this->mode= strtolower($this->simplexml->request['type']);
                return true;
            }
            else
            {
                $this->setXMLErrors($myMetaHeader);
                return false;
            }
        }
        else
        {
            $this->metaHeader->setMessage("905", "XML does not validate against schema. ");
            return false;
        }
    }

    function setXMLErrors()
    { 
        $message = "XML errors";
        $this->metaHeader->setMessage("905", $message);

    }

    function getGetParams()
    {
        // Extract Parameters from GET requests

        $this->mode = strtolower(addslashes($_GET['mode']));
        if(isset($_GET['id'])) $this->id = (int) $_GET['id'];
        if(isset($_GET['code'])) $this->code = addslashes($_GET['code']);
        if(isset($_GET['name'])) $this->name = addslashes($_GET['name']);
        if(isset($_GET['label'])) $this->label = addslashes($_GET['label']);
        if(isset($_GET['taxonid'])) $this->taxonid = (int) $_GET['label'];
        if(isset($_GET['subsiteid'])) $this->subsiteid = (int) $_GET['subsite'];
        if(isset($_GET['latitude'])) $this->latitude = (double) $_GET['latitude'];
        if(isset($_GET['longitude'])) $this->longitude = (double) $_GET['longitude'];
        if(isset($_GET['precision'])) $this->precision = (int) $_GET['precision'];
    }

}

// ***********
// SUB CLASSES
// ***********
class siteRequest extends request
{
    var $id         = NULL;
    var $code       = NULL;
    var $name       = NULL;

    function siteRequest($metaHeader)
    {
        parent::request($metaHeader);
    }

    function getXMLParams()
    {
        if($this->readXML())
        {
            if($this->simplexml->xpath('request//site/@id'))            $this->id           = (int)         $this->simplexml->xpath('request//site/@id'         );
            if($this->simplexml->xpath('request//site/@code'))          $this->code         = addslashes(   $this->simplexml->xpath('request//site/@code')      );
            if($this->simplexml->xpath('request//site/@name'))          $this->name         = addslashes(   $this->simplexml->xpath('request//site/@name')      );
        }
    }
}

class treeRequest extends request
{
    var $id         = NULL;
    var $label      = NULL;
    var $taxonid    = NULL;
    var $subsiteid  = NULL;
    var $latitude   = NULL;
    var $longitude  = NULL;
    var $precision  = NULL;

    function treeRequest($metaHeader)
    {
        parent::request($metaHeader);
    }

    function getXMLParams()
    {
        if($this->readXML())
        {
            if($this->simplexml->xpath('request//tree/@id'))            {$this->id           = (int)         $this->simplexml->xpath('request//tree/@id'         );}
            if($this->simplexml->xpath('request//tree/@label'))         {$this->label        = addslashes(   $this->simplexml->xpath('request//tree/@label')     );}
            if($this->simplexml->xpath('request//tree/@taxonID'))       {$this->taxonid      = (int)         $this->simplexml->xpath('/request//tree/@taxonid'   );}
            if($this->simplexml->xpath('request//subsite/@id'))         {$this->subsiteid    = (int)         $this->simplexml->xpath('/request//subsite/@id'     );}
            if($this->simplexml->xpath('request//tree/@latitude'))      {$this->latitude     = (double)      $this->simplexml->xpath('/request//tree/@latitude'  );}
            if($this->simplexml->xpath('request//tree/@longitude'))     {$this->longitude    = (double)      $this->simplexml->xpath('/request//tree/@longitude' );}
            if($this->simplexml->xpath('request//tree/@precision'))     {$this->precision    = (int)         $this->simplexml->xpath('/request//tree/@precision' );}
        }
    }
}

?>
