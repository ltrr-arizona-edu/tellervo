<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

class parameters 
{
    var $xmlrequest                 = NULL;
    var $format                     = NULL;
    var $simplexml                  = NULL;
    var $metaHeader                 = NULL;
    var $auth                       = NULL;
    var $hasChild                   = FALSE;


    function __construct($metaHeader, $auth, $xmlrequest)
    {
        $this->metaHeader = $metaHeader;
        $this->auth = $auth;
        $this->xmlrequest = $xmlrequest;
        $this->getXMLParams();
    }

    function getXMLParams()
    {
        echo "This function should be overloaded";
    }


}


class siteParameters extends parameters
{
    var $id             = NULL;
    var $name           = NULL;
    var $code           = NULL;
    var $siteNoteArray = array();
        
    function __construct($metaHeader, $auth, $xmlrequest, $parentID)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        $mySite = $this->xmlrequest->xpath('//site');
        if(isset($mySite[0]['id']))                  $this->id                       = (int) $mySite[0]['id'];
        if(isset($mySite[0]->name))                  $this->name                     = addslashes($mySite[0]->name);
        if(isset($mySite[0]->code))                  $this->code                     = addslashes($mySite[0]->code);
        if(isset($this->xmlrequest->subSite))        $this->hasChild                 = True;

        $siteNotes = $this->xmlrequest->xpath('//siteNotes');
        if (isset($siteNotes[0]->siteNote[0]))
        {
            foreach($siteNotes[0] as $item)
            {
                array_push($this->siteNoteArray, $item['id']);
            }
        }
        else
        {
            $this->siteNoteArray = array('empty');
        }
    }
}

class subSiteParameters extends parameters
{
    var $id         = NULL;
    var $name       = NULL;
    var $siteID     = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->siteID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))      $this->id           = (int) $this->xmlrequest['id'];
        if(isset($this->xmlrequest->name))      $this->name         = addslashes($this->xmlrequest->name);
        if(isset($this->xmlrequest->tree))      $this->hasChild     = True;
    }

}

class treeParameters extends parameters
{
    var $id                 = NULL;
    var $name               = NULL;
    var $taxonID            = NULL;
    var $originalTaxonName  = NULL;
    var $latitude           = NULL;
    var $longitude          = NULL;
    var $precision          = NULL;
    var $subSiteID          = NULL;
    var $isLiveTree         = NULL;
    var $treeNoteArray      = array();

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->subSiteID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id                   = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest->name))                  $this->name                 = addslashes($this->xmlrequest->name);
        if(isset($this->xmlrequest->originalTaxonName))     $this->originalTaxonName    = addslashes($this->xmlrequest->originalTaxonName);
        if(isset($this->xmlrequest->validatedTaxon['id']))  $this->taxonID              = (int)      $this->xmlrequest->validatedTaxon['id'];
        if(isset($this->xmlrequest->latitude))              $this->latitude             = (double)   $this->xmlrequest->latitude;
        if(isset($this->xmlrequest->longitude))             $this->longitude            = (double)   $this->xmlrequest->longitude;
        if(isset($this->xmlrequest->precision))             $this->precision            = (int)      $this->xmlrequest->precision;
        if(isset($this->xmlrequest->isLiveTree))            $this->isLiveTree           = fromStringtoPHPBool($this->xmlrequest->isLiveTree);
        if(isset($this->xmlrequest->specimen))              $this->hasChild             = True;
        
        $treeNotes = $this->xmlrequest->xpath('//treeNotes');
        if (isset($treeNotes[0]->treeNote[0]))
        {
            foreach($treeNotes[0] as $item)
            {
                array_push($this->treeNoteArray, $item['id']);
            }
        }
        else
        {
            $this->treeNoteArray = array('empty');
        }
    }
}

class specimenParameters extends parameters
{
    var $id                            = NULL;
    var $treeID                        = NULL;
    var $name                          = NULL;
    var $dateCollected                 = NULL;
    var $specimenType                  = NULL;
    var $terminalRing                  = NULL;
    var $sapwoodCount                  = NULL;
    var $specimenQuality               = NULL;
    var $specimenContinuity            = NULL;
    var $pith                          = NULL;
    var $unmeasuredPre                 = NULL;
    var $unmeasuredPost                = NULL;
    var $isTerminalRingVerified        = NULL;
    var $isSapwoodCountVerified        = NULL;
    var $isSpecimenQualityVerified     = NULL;
    var $isSpecimenContinuityVerified  = NULL;
    var $isPithVerified                = NULL;
    var $isUnmeasuredPreVerified       = NULL;
    var $isUnmeasuredPostVerified      = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->treeID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                          $this->id                               = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest->name))                          $this->name                             = addslashes($this->xmlrequest->name);
        if(isset($this->xmlrequest->dateCollected))                 $this->dateCollected                    = addslashes($this->xmlrequest->dateCollected);
        if(isset($this->xmlrequest->specimenType))                  $this->specimenType                     = addslashes($this->xmlrequest->specimenType);
        if(isset($this->xmlrequest->terminalRing))                  $this->terminalRing                     = addslashes($this->xmlrequest->terminalRing);
        if(isset($this->xmlrequest->sapwoodCount))                  $this->sapwoodCount                     = (int)      $this->xmlrequest->sapwoodCount;
        if(isset($this->xmlrequest->specimenQuality))               $this->specimenQuality                  = addslashes($this->xmlrequest->specimenQuality);
        if(isset($this->xmlrequest->specimenContinuity))            $this->specimenContinuity               = addslashes($this->xmlrequest->specimenContinuity);
        if(isset($this->xmlrequest->pith))                          $this->pith                             = addslashes($this->xmlrequest->pith);
        if(isset($this->xmlrequest->unmeasuredPre))                 $this->unmeasuredPre                    = (int)      $this->xmlrequest->unmeasuredPre;
        if(isset($this->xmlrequest->unmeasuredPost))                $this->unmeasuredPost                   = (int)      $this->xmlrequest->unmeasuredPost;
        if(isset($this->xmlrequest->isTerminalRingVerified))        $this->isTerminalRingVerified           = fromStringtoPHPBool(   $this->xmlrequest->isTerminalRingVerified);
        if(isset($this->xmlrequest->isSapwoodCountVerified))        $this->isSapwoodCountVerified           = fromStringtoPHPBool(   $this->xmlrequest->isSapwoodCountVerified);
        if(isset($this->xmlrequest->isSpecimenQualityVerified))     $this->isSpecimenQualityVerified        = fromStringtoPHPBool(   $this->xmlrequest->isSpecimenQualityVerified);
        if(isset($this->xmlrequest->isSpecimenContinuityVerified))  $this->isSpecimenContinuityVerified     = fromStringtoPHPBool(   $this->xmlrequest->isSpecimenContinuityVerified);
        if(isset($this->xmlrequest->isPithVerified))                $this->isPithVerified                   = fromStringtoPHPBool(   $this->xmlrequest->isPithVerified);
        if(isset($this->xmlrequest->isUnmeasuredPreVerified))       $this->isUnmeasuredPreVerified          = fromStringtoPHPBool(   $this->xmlrequest->isUnmeasuredPreVerified);
        if(isset($this->xmlrequest->isUnmeasuredPostVerified))      $this->isUnmeasuredPostVerified         = fromStringtoPHPBool(   $this->xmlrequest->isUnmeasuredPostVerified);
        if(isset($this->xmlrequest->radius))                        $this->hasChild                         = True;
    }
}

class radiusParameters extends parameters
{
    var $id         = NULL;
    var $name       = NULL;
    var $specimenID = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->specimenID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id           = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest->name))                  $this->name         = addslashes($this->xmlrequest->name);
        if(isset($this->xmlrequest->measurement))           $this->hasChild     = True;
    }
}



class measurementParameters extends parameters
{
    var $id                     = NULL;
    var $radiusID               = NULL;
    var $startYear              = NULL;
    var $measuredByID           = NULL;  
    var $ownerUserID            = NULL;
    var $datingTypeID           = NULL;
    var $datingType             = NULL;
    var $datingErrorPositive    = NULL;
    var $datingErrorNegative    = NULL;
    var $name                   = NULL;
    var $description            = NULL;  
    var $isLegacyCleaned        = NULL;
    var $isReconciled           = NULL;
    var $isPublished            = NULL;
    var $vmeasurementOp         = NULL;
    var $vmeasurementOpParam    = NULL;
    var $readingType            = NULL;
    var $readingUnits           = NULL;
    var $readingsArray          = array();
    var $referencesArray        = array();
    var $measurementNoteArray   = array();


    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->radiusID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }

    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                       $this->id                    = (int)                $this->xmlrequest['id'];
        if(isset($this->xmlrequest->startYear))                  $this->startYear             = (int)                $this->xmlrequest->startYear;
        if(isset($this->xmlrequest->measuredByID))               $this->measuredByID          = (int)                $this->xmlrequest->measuredByID;
        if(isset($this->xmlrequest->ownerUserID))                $this->ownerUserID           = (int)                $this->xmlrequest->ownerUserID;
        if(isset($this->xmlrequest->datingTypeID))               $this->datingTypeID          = addslashes(          $this->xmlrequest->datingTypeID);
        if(isset($this->xmlrequest->datingType))                 $this->datingType            = addslashes(          $this->xmlrequest->datingType);
        if(isset($this->xmlrequest->datingErrorPositive))        $this->datingErrorPositive   = (int)                $this->xmlrequest->datingErrorPositive;
        if(isset($this->xmlrequest->datingErrorNegative))        $this->datingErrorNegative   = (int)                $this->xmlrequest->datingErrorNegative;
        if(isset($this->xmlrequest->name))                       $this->name                  = addslashes(          $this->xmlrequest->name);
        if(isset($this->xmlrequest->description))                $this->description           = addslashes(          $this->xmlrequest->description);
        if(isset($this->xmlrequest->isLegacyCleaned))            $this->isLegacyCleaned       = fromStringtoPHPBool( $this->xmlrequest->isLegacyCleaned);
        if(isset($this->xmlrequest->isReconciled))               $this->isReconciled          = fromStringtoPHPBool( $this->xmlrequest->isReconciled);
        if(isset($this->xmlrequest->isPublished))                $this->isPublished           = fromStringtoPHPBool( $this->xmlrequest->isPublished);
        if(isset($this->xmlrequest->references['operation']))    $this->vmeasurementOp        = addslashes(          $this->xmlrequest->references['operation']);
        if(isset($this->xmlrequest->references['param']))        $this->vmeasurementOpParam   = addslashes(          $this->xmlrequest->references['param']);
        if(isset($this->xmlrequest->readings['type']))           $this->readingType           = addslashes(          $this->xmlrequest->readings['type']);
        if(isset($this->xmlrequest->readings['units']))          $this->readingUnits          = addslashes(          $this->xmlrequest->readings['units']);
        
        foreach($this->xmlrequest->xpath('//references/measurement') as $refmeasurement)
        {
            if($refmeasurement['id']) array_push($this->referencesArray, $refmeasurement['id']);
        }
        
        $theYear =-1;
        foreach($this->xmlrequest->xpath('//readings/reading') as $reading)
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

            $theValue = (int) $reading['value'];
            $this->readingsArray[$theYear] = array('reading' => $theValue, 'wjinc' => NULL, 'wjdec' => NULL, 'count' => 1, 'notesArray' => array());
                
            if(isset($reading->readingNote))
            {
                foreach($reading->readingNote as $readingNote)
                {
                    array_push($this->readingsArray[$theYear], (int) $readingNote['id']); 
                }

            }

        }
        
        $measurementNotes = $this->xmlrequest->xpath('//measurementNotes');
        if (isset($measurementNotes[0]->measurementNote[0]))
        {
            foreach($measurmentNotes[0] as $item)
            {
                array_push($this->measurementNoteArray, $item['id']);
            }
        }
        else
        {
            $this->measurementNoteArray = array('empty');
        }
    }
}

class siteNoteParameters extends parameters
{
    var $id         = NULL;
    var $note       = NULL;
    var $isStandard = FALSE;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id           = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest['isStandard']))          $this->isStandard   = fromStringToPHPBool($this->xmlrequest['isStandard']);
        if(isset($this->xmlrequest))                        $this->note         = addslashes(trim($this->xmlrequest));
    }
}

class treeNoteParameters extends parameters
{
    var $id         = null;
    var $note       = null;
    var $isStandard = false;

    function __construct($metaheader, $auth, $xmlrequest, $parentid=null)
    {
        parent::__construct($metaheader, $auth, $xmlrequest);
    }
    
    function getxmlparams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id           = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest['isStandard']))          $this->isStandard   = fromStringToPHPBool($this->xmlrequest['isStandard']);
        if(isset($this->xmlrequest))                        $this->note         = addslashes(trim($this->xmlrequest));
    }
}

class vmeasurementNoteParameters extends parameters
{
    var $id         = NULL;
    var $note       = NULL;
    var $isStandard = FALSE;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id           = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest['isStandard']))          $this->isStandard   = fromStringToPHPBool($this->xmlrequest['isStandard']);
        if(isset($this->xmlrequest))                        $this->note         = addslashes(trim($this->xmlrequest));
    }
}

class readingNoteParameters extends parameters
{
    var $id         = NULL;
    var $note       = NULL;
    var $isStandard = FALSE;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id           = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest['isStandard']))          $this->isStandard   = fromStringToPHPBool($this->xmlrequest['isStandard']);
        if(isset($this->xmlrequest))                        $this->note         = addslashes(trim($this->xmlrequest));
    }
}

class dictionariesParameters extends parameters
{   
    var $id = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }

    function getXMLParams()
    {
    }
}

class authenticationParameters extends parameters
{
    var $username     = NULL;
    var $nonce        = NULL;
    var $hash         = NULL;
    var $password     = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->radiusID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }

    function getXMLParams()
    {
        if(isset($this->xmlrequest[0]['username']))                  $this->username                    = addslashes($this->xmlrequest[0]['username']);
        if(isset($this->xmlrequest[0]['password']))                  $this->password                    = addslashes($this->xmlrequest[0]['password']);
        if(isset($this->xmlrequest[0]['nonce']))                     $this->nonce                       = addslashes($this->xmlrequest[0]['nonce']);
        if(isset($this->xmlrequest[0]['hash']))                      $this->hash                        = addslashes($this->xmlrequest[0]['hash']);
    }
}

class searchParameters extends parameters
{
    var $returnObject            = NULL;
    var $limit                   = NULL;
    var $skip                    = NULL;
    var $siteParamsArray         = array();
    var $subSiteParamsArray      = array();
    var $treeParamsArray         = array();
    var $specimenParamsArray     = array();
    var $radiusParamsArray       = array();
    var $measurementParamsArray  = array();
    var $vmeasurementParamsArray  = array();
    var $vmeasurementResultParamsArray  = array();
    var $vmeasurementMetaCacheParamsArray  = array();


    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }

    function getXMLParams()
    {
        if($this->xmlrequest['returnObject'])  $this->returnObject  = addslashes ( $this->xmlrequest['returnObject']);
        if($this->xmlrequest['limit'])         $this->limit         = (int) $this->xmlrequest['limit'];
        if($this->xmlrequest['skip'])          $this->skip          = (int) $this->xmlrequest['skip'];
                        
        foreach($this->xmlrequest->xpath('//param') as $param)
        {
            // Site Parameters
            if    ( ($param['name'] == 'siteid') || 
                    ($param['name'] == 'sitename') || 
                    ($param['name'] == 'sitecode') || 
                    ($param['name'] == 'sitecreated') || 
                    ($param['name'] == 'sitexcentroid') || 
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
                array_push($this->subSiteParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
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
                array_push($this->radiusParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }

            // Measurement Parameters
            elseif( ($param['name'] == 'measurementname') || 
                    ($param['name'] == 'measurementoperator') || 
                    ($param['name'] == 'measurementdescription') || 
                    ($param['name'] == 'measurementispublished') || 
                    ($param['name'] == 'measurementowneruserid') || 
                    ($param['name'] == 'measurementcreated') || 
                    ($param['name'] == 'operatorparameter') || 
                    ($param['name'] == 'measurementlastmodified'))
            {
                array_push($this->vmeasurementParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }
            elseif( ($param['name'] == 'measurementid') ) 
            {
                array_push($this->vmeasurementParamsArray, array ('name' => addslashes('vmeasurementid'), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }
            elseif( ($param['name'] == 'measurementisreconciled') || 
                    ($param['name'] == 'datingtype') || 
                    ($param['name'] == 'datingerrornegative') || 
                    ($param['name'] == 'datingerrorpositive'))
            {
                array_push($this->vmeasurementResultParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }
            elseif( ($param['name'] == 'startyear') || 
                    ($param['name'] == 'readingcount') || 
                    ($param['name'] == 'measurementcount') || 
                    ($param['name'] == 'measurementymin') || 
                    ($param['name'] == 'measurementymax') || 
                    ($param['name'] == 'measurementxmin') || 
                    ($param['name'] == 'measurementxmax') || 
                    ($param['name'] == 'measurementxcentroid') || 
                    ($param['name'] == 'measurementycentroid'))
            {
                array_push($this->vmeasurementMetaCacheParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }

        }
    }
}
?>
