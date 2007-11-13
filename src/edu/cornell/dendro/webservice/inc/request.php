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
        if(isset($_GET['taxonid'])) $this->taxonid = (int) $_GET['label'];
        if(isset($_GET['subsiteid'])) $this->subsiteid = (int) $_GET['subsite'];
        if(isset($_GET['siteid'])) $this->siteid = (int) $_GET['site'];
        if(isset($_GET['treeid'])) $this->treeid = (int) $_GET['tree'];
        if(isset($_GET['code'])) $this->code = addslashes($_GET['code']);
        if(isset($_GET['name'])) $this->name = addslashes($_GET['name']);
        if(isset($_GET['label'])) $this->label = addslashes($_GET['label']);
        if(isset($_GET['latitude'])) $this->latitude = (double) $_GET['latitude'];
        if(isset($_GET['longitude'])) $this->longitude = (double) $_GET['longitude'];
        if(isset($_GET['precision'])) $this->precision = (int) $_GET['precision'];
        if(isset($_GET['collectedday'])) $this->collectedday = (int) $_GET['collectedday'];
        if(isset($_GET['collectedmonth'])) $this->collectedmonth = (int) $_GET['collectedmonth'];
        if(isset($_GET['collectedyear'])) $this->collectedyear = (int) $_GET['collectedyear'];
        if(isset($_GET['specimentype'])) $this->specimentype = addslashes($_GET['specimentype']);
        if(isset($_GET['terminalring'])) $this->terminalring = addslashes($_GET['terminalring']);
        if(isset($_GET['isterminalringverified'])) $this->isterminalringverified = (bool) $_GET['isterminalringverified'];
        if(isset($_GET['sapwoodcount'])) $this->sapwoodcount = (int) $_GET['sapwoodcount'];
        if(isset($_GET['issapwoodcountverified'])) $this->issapwoodcountverified = (bool) $_GET['issapwoodcountverified'];
        if(isset($_GET['specimenquality'])) $this->specimenquality = addslashes($_GET['specimenquality']);
        if(isset($_GET['isspecimenqualityverified'])) $this->isspecimenqualityverified = (bool) $_GET['isspecimenqualityverified'];
        if(isset($_GET['specimencontinuity'])) $this->specimencontinuity = addslashes($_GET['specimencontinuity']);
        if(isset($_GET['pith'])) $this->pith = addslashes($_GET['pith']);
        if(isset($_GET['ispithverified'])) $this->ispithverified = (bool) $_GET['ispithverified'];
        if(isset($_GET['unmeasuredpre'])) $this->unmeasuredpre = (int) $_GET['unmeasuredpre'];
        if(isset($_GET['unmeasuredpost'])) $this->unmeasuredpost = (int) $_GET['unmeasuredpost'];
        if(isset($_GET['isunmeasuredpreverified'])) $this->isunmeasurementpreverified = (bool) $_GET['isunmeasuredpreverified'];
        if(isset($_GET['isunmeasuredpostverified'])) $this->isunmeasurementpostverified = (bool) $_GET['isunmeasuredpostverified'];
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
            foreach($this->simplexml->xpath('request//site[1]') as $site)
            {
                if($site['id'])   $this->id = (int) $site['id'];
                if($site['code']) $this->code = addslashes($site['code']);
                if($site['name']) $this->name = addslashes($site['name']);
            }
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
            foreach($this->simplexml->xpath('request//tree[1]') as $tree)
            {
                if($tree['id'])            $this->id           = (int)         $tree['id'];
                if($tree['label'])         $this->label        = addslashes(   $tree['label']);
                if($tree['taxonID'])       $this->taxonid      = (int)         $tree['taxonid'];
                if($tree['latitude'])      $this->latitude     = (double)      $tree['latitude'];
                if($tree['longitude'])     $this->longitude    = (double)      $tree['longitude'];
                if($tree['precision'])     $this->precision    = (int)         $tree['precision'];
            }

            foreach($this->simplexml->xpath('request//subsite[1]') as $subsite)
            {
                if($subsite['id'])         $this->subsiteid    = (int)         $subsite['id'];
            }
        }
    }
}

class subSiteRequest extends request
{
    var $id         = NULL;
    var $siteid     = NULL;
    var $name       = NULL;

    function subSiteRequest($metaHeader)
    {
        parent::request($metaHeader);
    }

    function getXMLParams()
    {
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//subsite[1]') as $subsite)
            {
                if($subsite['id'])            $this->id           = (int)         $subsite['id'];
                if($subsite['name'])          $this->name         = addslashes(   $subsite['name']);
            }

            foreach($this->simplexml->xpath('request//site[1]') as $site)
            {
                if($site['id'])               $this->siteid       = (int)         $site['id'];
            }
        }
    }
}

class specimenRequest extends request
{
    var $id                         = NULL;
    var $label                      = NULL;
    var $treeid                     = NULL;
    var $collectedday               = NULL;
    var $collectedmonth             = NULL;
    var $collectedyear              = NULL;
    var $specimentype               = NULL;
    var $terminalring               = NULL;
    var $isterminalringverified     = NULL;
    var $sapwoodcount               = NULL;
    var $issapwoodcountverified     = NULL;
    var $specimenquality            = NULL;
    var $isspecimenqualityverified  = NULL;
    var $specimencontinuity         = NULL;
    var $pith                       = NULL;
    var $ispithverified             = NULL;
    var $unmeasuredpre              = NULL;
    var $unmeasuredpost             = NULL;
    var $isunmeasuredpreverified    = NULL;
    var $isunmeasuredpostverified   = NULL;

    function specimenRequest($metaHeader)
    {
        parent::request($metaHeader);
    }

    function getXMLParams()
    {
        if($this->readXML())
        {
            foreach ($this->simplexml->xpath('request//specimen[1]') as $specimen)
            {
                if($specimen['id'])                        $this->id                           = (int)         $specimen['id'];
                if($specimen['label'])                     $this->label                        = addslashes(   $specimen['label']);
                if($specimen['dateCollected'])             $this->collectedday                 = (int)         date('j', strtotime($specimen['treeid']));
                if($specimen['dateCollected'])             $this->collectedmonth               = (int)         date('n', strtotime($specimen['treeid']));
                if($specimen['dateCollected'])             $this->collectedyear                = (int)         date('Y', strtotime($specimen['treeid']));
                if($specimen['specimenType'])              $this->specimentype                 = addslashes(   $specimen['specimenType']);
                if($specimen['terminalRing'])              $this->terminalring                 = addslashes(   $specimen['terminalRing']);
                if($specimen['isTerminalRingVerified'])    $this->isterminalringverified       = (bool)        $specimen['isTerminalRingVerified'];
                if($specimen['sapwoodCount'])              $this->sapwoodcount                 = (int)         $specimen['sapwoodCount'];
                if($specimen['isSapwoodCountVerified'])    $this->issapwoodcountverified       = (bool)        $specimen['isSapwoodCountVerified'];
                if($specimen['specimenQuality'])           $this->specimenquality              = addslashes(   $specimen['specimenQuality']);
                if($specimen['isSpecimenQualityVerified']) $this->isspecimenqualityverified    = (bool)        $specimen['isSpecimenQualityVerified'];
                if($specimen['specimenContinuity'])        $this->specimenContinuity           = addslashes(   $specimen['specimenContinuity']);
                if($specimen['pith'])                      $this->pith                         = addslashes(   $specimen['pith']);
                if($specimen['isPithVerified'])            $this->ispithverified               = (bool)        $specimen['isPithVerified'];
                if($specimen['unmeasuredPre'])             $this->unmeasuredPre                = (int)         $specimen['unmeasurePre'];
                if($specimen['isUnmeasuredPreVerified'])   $this->isunmeasuredPreVerified      = (bool)        $specimen['isUnmeasuredPreVerified'];
                if($specimen['unmeasuredPost'])            $this->unmeasuredPost               = (int)         $specimen['unmeasurePost'];
                if($specimen['isUnmeasuredPostVerified'])  $this->isunmeasuredPostVerified     = (bool)        $specimen['isUnmeasuredPostVerified'];
            }

            foreach ($this->simplexml->xpath('request/tree[1]') as $tree)
            {
                if($tree['id'])                            $this->treeid                       = (int)         $tree['id'];
            }
        }
    }
}
?>
