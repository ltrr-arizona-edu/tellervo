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
    var $simplexml                  = NULL;
    var $metaHeader                 = NULL;
    var $auth                       = NULL;

    var $mode                       = "failed";
    var $id                         = NULL;
    var $code                       = NULL;
    var $name                       = NULL;
    var $label                      = NULL;

    var $taxonid                    = NULL;
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
    var $isstandard                 = NULL;
    var $isreconciled               = NULL;
    var $islegacycleaned            = NULL;
    var $ispublished                = NULL;
    var $sitecode                   = NULL;
    var $note                       = NULL;
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
        $this->xmlrequest = stripslashes($_POST['xmlrequest']);
    }

    function readXML()
    {
        global $rngSchema;
        $origErrorLevel = error_reporting(E_ERROR);
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
            $sql = "insert into tblrequestlog (request, ipaddr, wsversion) values ('".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion')";
        }
        else
        {
            $sql = "insert into tblrequestlog (securityuserid, request, ipaddr, wsversion) values ('".$this->auth->getID()."', '".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion')";
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

    function getXMLParams()
    {
        $this->logRequest();
        $this->readXML();
    }
    
    function getGetParams()
    {
        // Extract Parameters from GET requests
        $this->mode   = strtolower(addslashes($_GET['mode']));
        $this->format = strtolower(addslashes($_GET['format']));
        
        // Loggin specific 
        if(isset($_POST['mode'])) $this->mode = addslashes($_POST['mode']);

        // ID values
        if(isset($_GET['id'])) $this->id = (int) $_GET['id'];
        if(isset($_GET['taxonid'])) $this->taxonid = (int) $_GET['taxonid'];
        if(isset($_GET['subsiteid'])) $this->subsiteid = (int) $_GET['subsiteid'];
        if(isset($_GET['siteid'])) $this->siteid = (int) $_GET['siteid'];
        if(isset($_GET['treeid'])) $this->treeid = (int) $_GET['treeid'];
        if(isset($_GET['readingid'])) $this->readingid = (int) $_GET['readingid'];
        if(isset($_GET['specimenid'])) $this->specimenid = (int) $_GET['specimenid'];
        if(isset($_GET['measurementid'])) $this->measurementid = (int) $_GET['measurementid'];
        if(isset($_GET['radiusid'])) $this->radiusid = (int) $_GET['radiusid'];

        // Main data items
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
        if(isset($_GET['isstandard'])) $this->isstandard = (bool) $_GET['isstandard'];
        if(isset($_GET['note'])) $this->note = addslashes($_GET['note']);
        if(isset($_GET['description'])) $this->description = addslashes($_GET['description']);
        if(isset($_GET['isreconciled'])) $this->isreconciled = (bool) $_GET['isreconciled'];
        if(isset($_GET['ispublished'])) $this->ispublished = (bool) $_GET['ispublished'];
        if(isset($_GET['islegacycleaned'])) $this->islegacycleaned = (bool) $_GET['islegacycleaned'];
        if(isset($_GET['startyear'])) $this->startyear = (int) $_GET['startyear'];
        if(isset($_GET['datingerrorpositive'])) $this->datingerrorpositive = (int) $_GET['datingerrorpostive'];
        if(isset($_GET['datingerrornegative'])) $this->datingerrornegative = (int) $_GET['datingerrornegative'];
        if(isset($_GET['measuredbyid'])) $this->measuredbyid = (int) $_GET['measuredbyid'];
        if(isset($_GET['owneruserid'])) $this->owneruserid = (int) $_GET['owneruserid'];
        if(isset($_GET['datingtypeid'])) $this->datingtypeid = (int) $_GET['datingtypeid'];

        // Login specific (note POST not GET)
        if(isset($_POST['username'])) $this->username = addslashes($_POST['username']);
        if(isset($_POST['password'])) $this->password = addslashes($_POST['password']);
        if(isset($_POST['hash']))     $this->hash = addslashes($_POST['hash']);
        if(isset($_POST['nonce'])) $this->nonce = addslashes($_POST['nonce']);

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
        


        $this->logRequest();
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
    var $latitude   = NULL;
    var $longitude  = NULL;

    function siteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {   
            foreach($this->simplexml->xpath('request//site[1]') as $site)
            {
                if($site['id'])   $this->id = (int) $site['id'];
                if($site['code']) $this->code = addslashes($site['code']);
                if($site['name']) $this->name = addslashes($site['name']);
                if($site['latitude'])   $this->latitude = (float) $site['latitude'];
                if($site['longitude'])   $this->longitude = (float) $site['longitude'];
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

    function treeRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
        if($this->readXML())
        {
            foreach($this->simplexml->xpath('request//tree[1]') as $tree)
            {
                if($tree['id'])            $this->id           = (int)         $tree['id'];
                if($tree['label'])         $this->label        = addslashes(   $tree['label']);
                if($tree['taxonID'])       $this->taxonid      = (int)         $tree['taxonID'];
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

    function subSiteRequest($metaHeader, $auth)
    {
        parent::request($metaHeader, $auth);
    }

    function getXMLParams()
    {
        $this->logRequest();
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

class radiusRequest extends request
{
    var $id         = NULL;
    var $specimenid = NULL;
    var $label      = NULL;

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
                if($radius['label'])         $this->label        = addslashes(   $radius['label']);
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
    var $id         = NULL;
    var $siteid     = NULL;
    var $note       = NULL;
    var $isstandard = NULL;

    function siteNoteRequest($metaHeader, $auth)
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
                if($request->siteNote['id'])            $this->id           = (int)         $request->siteNote['id'];
                if($request->siteNote)                  $this->note         = addslashes(   $request->siteNote);
                if($request->siteNote['isStandard'])    $this->isstandard   = (bool)        $request->siteNote['isStandard'];
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
            foreach($this->simplexml->xpath('request') as $request)
            {
                if($request->treeNote['id'])            $this->id           = (int)         $request->treeNote['id'];
                if($request->treeNote)                  $this->note         = addslashes(   $request->treeNote);
                if($request->treeNote['isStandard'])    $this->isstandard   = (bool)        $request->treeNote['isStandard'];
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
                if($request->readingNote['isStandard'])    $this->isstandard   = (bool)        $request->readingNote['isStandard'];
            }

            foreach($this->simplexml->xpath('request//reading[1]') as $reading)
            {
                if($reading['id'])               $this->readingid       = (int)         $reading['id'];
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
            foreach($this->simplexml->xpath('//request') as $request)
            {
                if($request->measurement['id'])                   $this->id                    = (int)         $request->measurement['id'];
                if($request->measurement['radiusID'])             $this->radiusid              = (int)         $request->measurement['radiusID'];
                if($request->measurement['isReconciled'])         $this->isreconciled          = (bool)        $request->measurement['isReconciled'];
                if($request->measurement['startYear'])            $this->startyear             = (int)         $request->measurement['startYear'];
                if($request->measurement['isLegacyCleaned'])      $this->islegacycleaned       = (bool)        $request->measurement['isLegacyCleaned'];
                if($request->measurement['measuredByID'])         $this->measuredby            = (int)         $request->measurement['measuredByID'];
                if($request->measurement['ownerUserID'])          $this->owneruserid           = (int)         $request->measurement['ownerUserID'];
                if($request->measurement['datingTypeID'])         $this->datingtypeid          = addslashes(   $request->measurement['datingTypeID']);
                if($request->measurement['datingErrorPositive'])  $this->datingerrorpositive   = (int)         $request->measurement['datingErrorPositive'];
                if($request->measurement['datingErrorNegative'])  $this->datingerrornegative   = (int)         $request->measurement['datingErrorNegative'];
                if($request->measurement['name'])                 $this->name                  = addslashes(   $request->measurement['name']);
                if($request->measurement['description'])          $this->description           = addslashes(   $request->measurement['description']);
                if($request->measurement['isPublished'])          $this->ispublished           = (bool)        $request->measurement['isPublished'];
            }
            
            foreach($this->simplexml->xpath('//request/measurement/references') as $references)
            {
                if($references['operationID']) $this->vmeasurementopid = (int) $references['operationID'];
            }

            foreach($this->simplexml->xpath('//request/measurement/references/measurement') as $refmeasurement)
            {
                if($refmeasurement['id']) array_push($this->referencesArray, $refmeasurement['id']);
            }
            
            $theYear =-1;
            foreach($this->simplexml->xpath('//request/measurement/readings/value') as $value)
            {
                if ($value['year']!=NULL) 
                {
                    $theYear = (int) $value['year'];
                }
                else
                {
                    $theYear++; 
                }

                $theValue = (int) $value;
                $this->readingsArray[$theYear] = array('reading' => $theValue, 'wjinc' => NULL, 'wjdec' => NULL, 'count' => 1);
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
                    array_push($this->siteParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
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
                elseif( ($param['name'] == 'measurementid') || 
                        ($param['name'] == 'measurementname') || 
                        ($param['name'] == 'measurementcreated') || 
                        ($param['name'] == 'measurementlastmodified'))
                {
                    array_push($this->measurementParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
                }
            }
        }
    }
}


?>
