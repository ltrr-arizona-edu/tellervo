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
    var $xmlrequest                 = NULL;
    var $format                     = NULL;
    var $mapwidth                   = NULL;
    var $mapheight                  = NULL;
    var $maptype                    = NULL;
    var $simplexml                  = NULL;
    var $metaHeader                 = NULL;
    var $auth                       = NULL;

    var $mode                       = "failed";
    var $id                         = NULL;
    var $code                       = NULL;
    var $name                       = NULL;
    var $label                      = NULL;

    var $taxonid                    = NULL;
    var $parentid                   = NULL;
    var $colid                      = NULL;
    var $colparentid                = NULL;
    var $subsiteid                  = NULL;
    var $siteid                     = NULL;
    var $treeid                     = NULL;
    var $readingid                  = NULL;
    var $specimenid                 = NULL;
    var $measurementid              = NULL;
    var $radiusid                   = NULL;
    var $vmeasurementopid           = NULL;
    var $owneruserid                = NULL;
    var $readingsArray              = array();
    var $referencesArray            = array();

    var $vmeasurementop             = NULL;
    var $readingtype                = NULL;
    var $readingunits               = NULL;
    var $description                = NULL;
    var $startyearon                = NULL;
    var $measuredby                 = NULL;
    var $datingtype                 = NULL;
    var $datingerrorpositive        = NULL;
    var $datingerrornegative        = NULL;
    var $latitude                   = NULL;
    var $longitude                  = NULL;
    var $precision                  = NULL;
    var $islivetree                 = NULL;
    //var $collectedday               = NULL;
    //var $collectedmonth             = NULL;
    //var $collectedyear              = NULL;
    var $datecollected              = NULL;
    var $specimentype               = NULL;
    var $terminalring               = NULL;
    var $isterminalringverified     = NULL;
    var $sapwoodcount               = NULL;
    var $issapwoodcountverified     = NULL;
    var $specimenquality            = NULL;
    var $isspecimenqualityverified  = NULL;
    var $specimencontinuity         = NULL;
    var $isspecimencontinuityverified  = NULL;
    var $pith                       = NULL;
    var $ispithverified             = NULL;
    var $unmeasuredpre              = NULL;
    var $unmeasuredpost             = NULL;
    var $isunmeasuredpreverified    = NULL;
    var $isunmeasuredpostverified   = NULL;
    var $isstandard                 = NULL;
    var $isreconciled               = NULL;
    var $islegacycleaned            = NULL;
    var $ispublished                = NULL;
    var $sitecode                   = NULL;
    var $note                       = NULL;
    var $taxonrank                  = NULL;

    var $password                   = NULL;
    var $username                   = NULL;
    var $hash                       = NULL;
    var $nonce                      = NULL;

    var $returnObject               = NULL;
    var $siteParamsArray            = array();
    var $subsiteParamsArray         = array();
    var $treeParamsArray            = array();
    var $specimenParamsArray        = array();
    var $radiusParamsArray          = array();
    var $measurementParamsArray     = array();
    var $limit                      = NULL;
    var $skip                       = NULL;

    function request($metaHeader, $auth)
    {
        $this->metaHeader = $metaHeader;
        $this->auth = $auth;
        if(isset($_POST['xmlrequest'])) $this->xmlrequest = stripslashes($_POST['xmlrequest']);
        $this->getParams();
    }

    function readXML()
    {
        global $rngSchema;
        $origErrorLevel = error_reporting(E_ERROR);
        // Extract parameters from XML post
        
        if(isset($this->xmlrequest))
        {
            $xmlstring = $this->xmlrequest;
            $doc = new DomDocument;
            $doc->loadXML($xmlstring);
            //$success = @$doc->relaxNGValidate($rngSchema));
            if($doc->relaxNGValidate($rngSchema))
            {
                $this->simplexml = simplexml_load_string($xmlstring);
                if($this->simplexml)
                {
                    $this->mode= strtolower($this->simplexml->request['type']);
                    error_reporting($origErrorLevel);
                    return true;
                }
                else
                {
                    $this->setXMLErrors($myMetaHeader);
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
        else
        {
            $this->metaHeader->setMessage("905", "No XML supplied.");
        }
    }
    

    function getGetMapRequest()
    {
        if(isset($this->simplexml->request['format']))           $this->format       = addslashes($this->simplexml->request['format']);
        if(isset($this->simplexml->request->mapParams->width))   $this->mapwidth     = (int) $this->simplexml->request->mapParams->width;
        if(isset($this->simplexml->request->mapParams->height))  $this->mapheight    = (int) $this->simplexml->request->mapParams->height;
        if(isset($this->simplexml->request->mapParams->type))    $this->maptype      = addslashes($this->simplexml->request->mapParams->type);
    }

    function logRequest()
    {
        global $dbconn;
        global $wsversion;

        if ($this->xmlrequest)
        {
            $request = $this->xmlrequest;
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

    function setXMLErrors()
    { 
        $message = "XML errors";
        $this->metaHeader->setMessage("905", $message);

    }
    
    function getParams()
    {

        if(isset($_POST['xmlrequest']))
        {
            // Extract parameters from XML request POST
            $this->getXMLParams();
        }
        else
        {
            // Extract parameters from get request and ensure no SQL has been injected
            $this->getGetParams();
        }

    }


    function getXMLParams()
    {
        $this->logRequest();
        $this->readXML();
    }

    
    function getGetParams()
    {
        // Extract Parameters from GET requests
        if(isset($_GET['mode']))   $this->mode   = strtolower(addslashes($_GET['mode']));
        if(isset($_GET['format'])) $this->format = strtolower(addslashes($_GET['format']));
        if(isset($_GET['mapwidth'])) $this->mapwidth = (int) $_GET['mapwidth'];
        if(isset($_GET['mapheight'])) $this->mapheight = (int) $_GET['mapheight'];
        
        // Loggin specific 
        if(isset($_POST['mode'])) $this->mode = addslashes($_POST['mode']);

        // ID values
        if(isset($_GET['id']))              $this->id               = (int) $_GET['id'];
        if(isset($_GET['colid']))           $this->colid            = (int) $_GET['colid'];
       
       
       
        // Disabling GET requests for anything other than read
        /* 
                if(isset($_GET['taxonid']))         $this->taxonid          = (int) $_GET['taxonid'];
                if(isset($_GET['subsiteid']))       $this->subsiteid        = (int) $_GET['subsiteid'];
                if(isset($_GET['siteid']))          $this->siteid           = (int) $_GET['siteid'];
                if(isset($_GET['treeid']))          $this->treeid           = (int) $_GET['treeid'];
                if(isset($_GET['readingid']))       $this->readingid        = (int) $_GET['readingid'];
                if(isset($_GET['specimenid']))      $this->specimenid       = (int) $_GET['specimenid'];
                if(isset($_GET['measurementid']))   $this->measurementid    = (int) $_GET['measurementid'];
                if(isset($_GET['radiusid']))        $this->radiusid         = (int) $_GET['radiusid'];

                // Main data items
                if(isset($_GET['code']))                        $this->code                         = addslashes($_GET['code']);
                if(isset($_GET['name']))                        $this->name                         = addslashes($_GET['name']);
                if(isset($_GET['label']))                       $this->label                        = addslashes($_GET['label']);
                if(isset($_GET['latitude']))                    $this->latitude                     = (double) $_GET['latitude'];
                if(isset($_GET['longitude']))                   $this->longitude                    = (double) $_GET['longitude'];
                if(isset($_GET['precision']))                   $this->precision                    = (int) $_GET['precision'];
                if(isset($_GET['collectedday']))                $this->collectedday                 = (int) $_GET['collectedday'];
                if(isset($_GET['collectedmonth']))              $this->collectedmonth               = (int) $_GET['collectedmonth'];
                if(isset($_GET['collectedyear']))               $this->collectedyear                = (int) $_GET['collectedyear'];
                if(isset($_GET['specimentype']))                $this->specimentype                 = addslashes($_GET['specimentype']);
                if(isset($_GET['terminalring']))                $this->terminalring                 = addslashes($_GET['terminalring']);
                if(isset($_GET['sapwoodcount']))                $this->sapwoodcount                 = (int) $_GET['sapwoodcount'];
                if(isset($_GET['specimenquality']))             $this->specimenquality              = addslashes($_GET['specimenquality']);
                if(isset($_GET['specimencontinuity']))          $this->specimencontinuity           = addslashes($_GET['specimencontinuity']);
                if(isset($_GET['pith']))                        $this->pith                         = addslashes($_GET['pith']);
                if(isset($_GET['unmeasuredpre']))               $this->unmeasuredpre                = (int) $_GET['unmeasuredpre'];
                if(isset($_GET['unmeasuredpost']))              $this->unmeasuredpost               = (int) $_GET['unmeasuredpost'];
                if(isset($_GET['note']))                        $this->note                         = addslashes($_GET['note']);
                if(isset($_GET['description']))                 $this->description                  = addslashes($_GET['description']);
                if(isset($_GET['startyear']))                   $this->startyear                    = (int) $_GET['startyear'];
                if(isset($_GET['datingerrorpositive']))         $this->datingerrorpositive          = (int) $_GET['datingerrorpostive'];
                if(isset($_GET['datingerrornegative']))         $this->datingerrornegative          = (int) $_GET['datingerrornegative'];
                if(isset($_GET['measuredbyid']))                $this->measuredbyid                 = (int) $_GET['measuredbyid'];
                if(isset($_GET['owneruserid']))                 $this->owneruserid                  = (int) $_GET['owneruserid'];
                if(isset($_GET['datingtypeid']))                $this->datingtypeid                 = (int) $_GET['datingtypeid'];
                if(isset($_GET['issapwoodcountverified']))      $this->issapwoodcountverified       = fromStringtoPHPBool($_GET['issapwoodcountverified']);
                if(isset($_GET['isspecimenqualityverified']))   $this->isspecimenqualityverified    = fromStringtoPHPBool($_GET['isspecimenqualityverified']);
                if(isset($_GET['ispithverified']))              $this->ispithverified               = fromStringtoPHPBool($_GET['ispithverified']);
                if(isset($_GET['isunmeasuredpreverified']))     $this->isunmeasurementpreverified   = fromStringtoPHPBool($_GET['isunmeasuredpreverified']);
                if(isset($_GET['isunmeasuredpostverified']))    $this->isunmeasurementpostverified  = fromStringtoPHPBool($_GET['isunmeasuredpostverified']);
                if(isset($_GET['isstandard']))                  $this->isstandard                   = fromStringtoPHPBool($_GET['isstandard']);
                if(isset($_GET['isterminalringverified']))      $this->isterminalringverified       = fromStringtoPHPBool($_GET['isterminalringverified']);
                if(isset($_GET['isreconciled']))                $this->isreconciled                 = fromStringtoPHPBool($_GET['isreconciled']);
                if(isset($_GET['ispublished']))                 $this->ispublished                  = fromStringtoPHPBool($_GET['ispublished']);
                if(isset($_GET['islegacycleaned']))             $this->islegacycleaned              = fromStringtoPHPBool($_GET['islegacycleaned']);
                // Retrieve arrays
                if(isset($_GET['references'])) $this->referencesArray = explode(",", $_GET['references']);
                if(isset($_GET['readings'])) 
                {
                    $readings = explode(",", $_GET['readings']);
                    foreach($readings as $reading)
                    {
                        array_push($this->readingsArray, array('reading' => $reading, 'wkinc' => NULL , 'wjdec' => NULL, 'count' => '1'));
                    }
                }
            */

        // Login specific (note POST not GET)
        if(isset($_POST['username']))                   $this->username                     = addslashes($_POST['username']);
        if(isset($_POST['password']))                   $this->password                     = addslashes($_POST['password']);
        if(isset($_POST['hash']))                       $this->hash                         = addslashes($_POST['hash']);
        if(isset($_POST['nonce']))                      $this->nonce                        = addslashes($_POST['nonce']);

        // Search parameters
        //if(isset($_GET['returnobject'])                 $this->returnobject                 = addslashes($_GET['returnobject']);

        //Log request
        $this->logRequest();
    }
    
}

// ***********
// SUB CLASSES
// ***********
class siteRequest extends request
{
    function siteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {   
            $this->getGetMapRequest();
            foreach($this->simplexml->xpath('request//site[1]') as $site)
            {
                if($site['id'])         $this->id = (int) $site['id'];
                if($site->code)         $this->code = addslashes($site->code);
                if($site->name)         $this->name = addslashes($site->name);
                if($site->latitude)     $this->latitude = (float) $site->latitude;
                if($site->longitude)    $this->longitude = (float) $site->longitude;
            }
        }
    }
}

class treeRequest extends request
{

    function treeRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            
            $this->getGetMapRequest();

            foreach($this->simplexml->xpath('request//tree[1]') as $tree)
            {
                if($tree['id'])                 $this->id           = (int)         $tree['id'];
                if($tree->name)                 $this->name         = addslashes(   $tree->name);
                if($tree->validatedTaxon['id']) $this->taxonid      = (int)         $tree->validatedTaxon['id'];
                if($tree->latitude)             $this->latitude     = (double)      $tree->latitude;
                if($tree->longitude)            $this->longitude    = (double)      $tree->longitude;
                if($tree->precision)            $this->precision    = (int)         $tree->precision;
            }

            foreach($this->simplexml->xpath('request//subSite[1]') as $subsite)
            {
                if($subsite['id'])         $this->subsiteid    = (int)         $subsite['id'];
            }
        }
    }
}

class subSiteRequest extends request
{

    function subSiteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//subSite[1]') as $subsite)
            {
                if($subsite['id'])            $this->id           = (int)         $subsite['id'];
                if($subsite->name)            $this->name         = addslashes(   $subsite->name);
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

    function specimenRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach ($this->simplexml->xpath('request//specimen[1]') as $specimen)
            {
                if($specimen['id'])                           $this->id                               = (int)                  $specimen['id'];
                if($specimen->name)                           $this->label                            = addslashes(            $specimen->label);
                if($specimen->dateCollected)                  $this->datecollected                    = addslashes(            $specimen->dateCollected);
                if($specimen->specimenType)                   $this->specimentype                     = addslashes(            $specimen->specimenType);
                if($specimen->terminalRing)                   $this->terminalring                     = addslashes(            $specimen->terminalRing);
                if($specimen->sapwoodCount)                   $this->sapwoodcount                     = (int)                  $specimen->sapwoodCount;
                if($specimen->specimenQuality)                $this->specimenquality                  = addslashes(            $specimen->specimenQuality);
                if($specimen->specimenContinuity)             $this->specimencontinuity               = addslashes(            $specimen->specimenContinuity);
                if($specimen->pith)                           $this->pith                             = addslashes(            $specimen->pith);
                if($specimen->unmeasuredPre)                  $this->unmeasuredpre                    = (int)                  $specimen->unmeasuredPre;
                if($specimen->unmeasuredPost)                 $this->unmeasuredpost                   = (int)                  $specimen->unmeasuredPost;
                if($specimen->isTerminalRingVerified)         $this->isterminalringverified           = fromStringtoPHPBool(   $specimen->isTerminalRingVerified);
                if($specimen->isSapwoodCountVerified)         $this->issapwoodcountverified           = fromStringtoPHPBool(   $specimen->isSapwoodCountVerified);
                if($specimen->isSpecimenQualityVerified)      $this->isspecimenqualityverified        = fromStringtoPHPBool(   $specimen->isSpecimenQualityVerified);
                if($specimen->isSpecimenContinuityVerified)   $this->isspecimencontinuityverified     = fromStringtoPHPBool(   $specimen->isSpecimenContinuityVerified);
                if($specimen->isPithVerified)                 $this->ispithverified                   = fromStringtoPHPBool(   $specimen->isPithVerified);
                if($specimen->isUnmeasuredPreVerified)        $this->isunmeasuredpreverified          = fromStringtoPHPBool(   $specimen->isUnmeasuredPreVerified);
                if($specimen->isUnmeasuredPostVerified)       $this->isunmeasuredpostverified         = fromStringtoPHPBool(   $specimen->isUnmeasuredPostVerified);
            }

            foreach ($this->simplexml->xpath('request/tree[1]') as $tree)
            {
                if($tree['id'])                            $this->treeid                       = (int)         $tree['id'];
            }
        }
    }
}

class radiusRequest extends request
{

    function radiusRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//radius[1]') as $radius)
            {
                if($radius['id'])            $this->id           = (int)         $radius['id'];
                if($radius->name)            $this->name         = addslashes(   $radius->name);
            }

            foreach($this->simplexml->xpath('request//specimen[1]') as $specimen)
            {
                if($specimen['id'])          $this->specimenid       = (int)     $specimen['id'];
            }
        }
    }
}

class siteNoteRequest extends request
{

    function siteNoteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//siteNote[1]') as $siteNote)
            {
                if(isset($siteNote['id']))            $this->id           = (int)                 $siteNote['id'];
                if(isset($siteNote))                  $this->note         = addslashes(           $siteNote);
                if(isset($siteNote['isStandard']))    $this->isstandard   = fromStringtoPHPBool(  $siteNote['isStandard']);
            }
            
            foreach($this->simplexml->xpath('request//site[1]') as $site)
            {
                if($site['id'])               $this->siteid       = (int)         $site['id'];
            }
        }
    }
}

class treeNoteRequest extends request
{
    var $id         = NULL;
    var $treeid     = NULL;
    var $note       = NULL;
    var $isstandard = NULL;

    function treeNoteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//treeNote') as $treeNote)
            {
                if($treeNote['id'])            $this->id           = (int)                $treeNote['id'];
                if($treeNote)                  $this->note         = addslashes(          $treeNote);
                if($treeNote['isStandard'])    $this->isstandard   = fromStringtoPHPBool( $treeNote['isStandard']);
            }

            foreach($this->simplexml->xpath('request//tree[1]') as $tree)
            {
                if($tree['id'])               $this->treeid       = (int)         $tree['id'];
            }
        }
    }
}

class readingNoteRequest extends request
{
    var $id         = NULL;
    var $readingid  = NULL;
    var $note       = NULL;
    var $isstandard = NULL;

    function readingNoteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request') as $request)
            {
                if($request->readingNote['id'])            $this->id           = (int)         $request->readingNote['id'];
                if($request->readingNote)                  $this->note         = addslashes(   $request->readingNote);
                if($request->readingNote['isStandard'])    $this->isstandard   = fromStringtoPHPBool($request->readingNote['isStandard']);
            }

            foreach($this->simplexml->xpath('request//reading[1]') as $reading)
            {
                if($reading['id'])               $this->readingid       = (int)         $reading['id'];
            }
        }
    }
}

class vmeasurementNoteRequest extends request
{
    var $id             = NULL;
    var $vmeasurementid = NULL;
    var $note           = NULL;
    var $isstandard     = NULL;

    function vmeasurementNoteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//measurementNote[1]') as $measurementNote)
            {
                if(isset($measurementNote['id']))            $this->id           = (int)                 $measurementNote['id'];
                if(isset($measurementNote))                  $this->note         = addslashes(           $measurementNote);
                if(isset($measurementNote['isStandard']))    $this->isstandard   = fromStringtoPHPBool(  $measurementNote['isStandard']);
            }
            
            foreach($this->simplexml->xpath('request//measurement[1]') as $measurement)
            {
                if($measurement['id'])               $this->measurementid       = (int)         $measurement['id'];
            }
        }
    }
}

class authenticateRequest extends request
{
    var $password   = NULL;
    var $username   = NULL;
    var $hash       = NULL;
    var $nonce      = NULL;

    function authenticateRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('//request') as $request)
            {
                if($request->authenticate['username'])            $this->username         = addslashes(   $request->authenticate['username']);
                if($request->authenticate['password'])            $this->password         = addslashes(   $request->authenticate['password']);
                if($request->authenticate['hash'])                $this->hash             = addslashes(   $request->authenticate['hash']);
                if($request->authenticate['nonce'])               $this->nonce            = addslashes(   $request->authenticate['nonce']);
            }
        }
    }
}

class measurementRequest extends request
{
    var $password   = NULL;
    var $username   = NULL;
    var $hash       = NULL;
    var $nonce      = NULL;

    function authenticateRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('//request/measurement') as $measurement)
            {
                if(isset($measurement['id']))                       $this->id                    = (int)                $measurement['id'];
                if(isset($measurement->radiusID))                   $this->radiusid              = (int)                $measurement->radiusID;
                if(isset($measurement->startYear))                  $this->startyear             = (int)                $measurement->startYear;
                if(isset($measurement->measuredByID))               $this->measuredbyid          = (int)                $measurement->measuredByID;
                if(isset($measurement->ownerUserID))                $this->owneruserid           = (int)                $measurement->ownerUserID;
                if(isset($measurement->datingTypeID))               $this->datingtypeid          = addslashes(          $measurement->datingTypeID);
                if(isset($measurement->datingErrorPositive))        $this->datingerrorpositive   = (int)                $measurement->datingErrorPositive;
                if(isset($measurement->datingErrorNegative))        $this->datingerrornegative   = (int)                $measurement->datingErrorNegative;
                if(isset($measurement->name))                       $this->name                  = addslashes(          $measurement->name);
                if(isset($measurement->description))                $this->description           = addslashes(          $measurement->description);
                if(isset($measurement->isLegacyCleaned))            $this->islegacycleaned       = fromStringtoPHPBool( $measurement->isLegacyCleaned);
                if(isset($measurement->isReconciled))               $this->isreconciled          = fromStringtoPHPBool( $measurement->isReconciled);
                if(isset($measurement->isPublished))                $this->ispublished           = fromStringtoPHPBool( $measurement->isPublished);
                if(isset($measurement->references['operation']))    $this->vmeasurementop        = addslashes(          $measurement->references['operation']);
                //if(isset($measurement->references['operationID']))  $this->vmeasurementopid      = (int)                $measurement->references['operationID'];
                if(isset($measurement->readings['type']))           $this->readingtype           = addslashes(          $measurement->readings['type']);
                if(isset($measurement->readings['units']))          $this->readingunits          = addslashes(          $measurement->readings['units']);
            }

            foreach($this->simplexml->xpath('//request/measurement/references/measurement') as $refmeasurement)
            {
                if($refmeasurement['id']) array_push($this->referencesArray, $refmeasurement['id']);
            }
            
            $theYear =-1;
            foreach($this->simplexml->xpath('//request/measurement/readings/reading') as $reading)
            {
                if ($reading['year']!=NULL) 
                {
                    // If the XML includes a year attribute use it
                    $theYear = (int) $reading['year'];
                }
                else
                {
                    // Otherwise use relative years - base 0
                    $theYear++; 
                }

                $theValue = (int) $reading;
                $this->readingsArray[$theYear] = array('reading' => $theValue, 'wjinc' => NULL, 'wjdec' => NULL, 'count' => 1, 'notesArray' => array());
                    
                if(isset($reading->readingNote))
                {
                    foreach($reading->readingNote as $readingNote)
                    {
                        array_push($this->readingsArray[$theYear][notesArray], (int) $readingNote['id']); 
                    }

                }

            }
        }
    }
}

class searchRequest extends request
{
    function authenticateRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            if($this->simplexml->request->searchParams['returnObject'])  $this->returnObject  = addslashes ( $this->simplexml->request->searchParams['returnObject']);
            if($this->simplexml->request->searchParams['limit'])         $this->limit         = (int) $this->simplexml->request->searchParams['limit'];
            if($this->simplexml->request->searchParams['skip'])          $this->skip          = (int) $this->simplexml->request->searchParams['skip'];
                        
            foreach($this->simplexml->xpath('//request/searchParams/param') as $param)
            {
                // Site Parameters
                if    ( ($param['name'] == 'siteid') || 
                        ($param['name'] == 'sitename') || 
                        ($param['name'] == 'sitecode') || 
                        ($param['name'] == 'sitecreated') || 
                        ($param['name'] == 'sitelastmodified') )
                {
                    array_push($this->siteParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
                }

                // Subsite Parameters
                elseif( ($param['name'] == 'subsiteid') || 
                        ($param['name'] == 'subsitename') || 
                        ($param['name'] == 'subsitecreated') || 
                        ($param['name'] == 'subsitelastmodified'))
                {
                    array_push($this->subsiteParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }

                // Tree Parameters
                elseif( ($param['name'] == 'treeid') || 
                        ($param['name'] == 'treename') || 
                        ($param['name'] == 'treecreated') || 
                        ($param['name'] == 'treelastmodified') || 
                        ($param['name'] == 'precision') || 
                        ($param['name'] == 'islivetree') || 
                        ($param['name'] == 'latitude') || 
                        ($param['name'] == 'longitude'))
                {
                    array_push($this->treeParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }  

                // Specimen parameters
                elseif( ($param['name'] == 'specimenid') || 
                        ($param['name'] == 'specimenname') || 
                        ($param['name'] == 'datecollected') || 
                        ($param['name'] == 'specimencreated') || 
                        ($param['name'] == 'specimenlastmodified') || 
                        ($param['name'] == 'specimentypeid') || 
                        ($param['name'] == 'specimentype') || 
                        ($param['name'] == 'isterminalringverified') || 
                        ($param['name'] == 'sapwoodcount') || 
                        ($param['name'] == 'issapwoodcountverified') || 
                        ($param['name'] == 'isspecimenqualityverified') || 
                        ($param['name'] == 'ispithverified') || 
                        ($param['name'] == 'unmeaspre') || 
                        ($param['name'] == 'unmeaspost') || 
                        ($param['name'] == 'isunmeaspreverified') || 
                        ($param['name'] == 'isunmeaspostverified') || 
                        ($param['name'] == 'terminalringid') || 
                        ($param['name'] == 'terminalring') || 
                        ($param['name'] == 'specimenqualityid') || 
                        ($param['name'] == 'specimenquality') || 
                        ($param['name'] == 'specimencontinuityid') || 
                        ($param['name'] == 'specimencontinuity') || 
                        ($param['name'] == 'pithid') || 
                        ($param['name'] == 'pith') || 
                        ($param['name'] == 'isspecimencontinuityverified'))
                {
                    array_push($this->specimenParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }

                // Radius parameters
                elseif( ($param['name'] == 'radiusid') || 
                        ($param['name'] == 'radiusname') || 
                        ($param['name'] == 'radiuscreated') || 
                        ($param['name'] == 'radiuslastmodified'))
                {
                    array_push($this->radiusParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }

                // Measurement Parameters
                elseif( ($param['name'] == 'measurementname') || 
                        ($param['name'] == 'measurementcreated') || 
                        ($param['name'] == 'measurementlastmodified'))
                {
                    array_push($this->measurementParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }
                
                elseif( ($param['name'] == 'measurementid') ) 
                {
                    array_push($this->measurementParamsArray, array ('name' => addslashes('vmeasurementid'), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }
            }
        }
    }
}


class importColRequest extends request
{

    function authenticateRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('//request') as $request)
            {
            }
        }
    }
}

class taxonRequest extends request
{

    function authenticateRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('//request/taxon') as $taxon)
            {
                if($taxon['id'])        $this->id           = (int) $taxon['id'];
                if($taxon['parentID'])  $this->parentid     = (int) $taxon['parentID'];
                if($taxon['colID'])     $this->colid        = (int) $taxon['colID'];
                if($taxon['taxonRank']) $this->taxonrank    = addslashes($taxon['taxonRank']);
                if($taxon)              $this->label        = addslashes($taxon);
            }
        }
    }
}


?>
