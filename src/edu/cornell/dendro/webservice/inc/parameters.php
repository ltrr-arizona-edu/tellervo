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
        if(isset($this->xmlrequest->precision))             $this->precision            = (double)   $this->xmlrequest->precision;
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
    var $readingsType           = NULL;
    var $readingsUnits          = NULL;
    var $readingsArray          = array();
    var $referencesArray        = array();
    var $measurementNoteArray   = array();
    var $masterVMeasurementID   = NULL;
    var $certaintyLevel         = NULL;
    var $justification          = NULL;
    var $newStartYear           = NULL;


    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->radiusID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }

    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                                          $this->id                    = (int)                $this->xmlrequest['id'];
        if(isset($this->xmlrequest->metadata->measuredBy['id']))                    $this->measuredByID          = (int)                $this->xmlrequest->metadata->measuredBy['id'];
        if(isset($this->xmlrequest->metadata->owner['id']))                         $this->ownerUserID           = (int)                $this->xmlrequest->metadata->owner['id'];
        //if(isset($this->xmlrequest->metadata->datingTypeID))                      $this->datingTypeID          = addslashes(          $this->xmlrequest->metadata->datingTypeID);
        if(isset($this->xmlrequest->metadata->dating['startYear']))                 $this->startYear             = (int)                $this->xmlrequest->metadata->dating['startYear'];
        if(isset($this->xmlrequest->metadata->dating['type']))                      $this->datingType            = addslashes(          $this->xmlrequest->metadata->dating['type']);
        if(isset($this->xmlrequest->metadata->dating['positiveError']))             $this->datingErrorPositive   = (int)                $this->xmlrequest->metadata->dating['positiveError'];
        if(isset($this->xmlrequest->metadata->dating['negativeError']))             $this->datingErrorNegative   = (int)                $this->xmlrequest->metadata->dating['negativeError'];
        if(isset($this->xmlrequest->metadata->name))                                $this->name                  = addslashes(          $this->xmlrequest->metadata->name);
        if(isset($this->xmlrequest->metadata->description))                         $this->description           = addslashes(          $this->xmlrequest->metadata->description);
        if(isset($this->xmlrequest->metadata->isLegacyCleaned))                     $this->isLegacyCleaned       = fromStringtoPHPBool( $this->xmlrequest->metadata->isLegacyCleaned);
        if(isset($this->xmlrequest->metadata->isReconciled))                        $this->isReconciled          = fromStringtoPHPBool( $this->xmlrequest->metadata->isReconciled);
        if(isset($this->xmlrequest->metadata->isPublished))                         $this->isPublished           = fromStringtoPHPBool( $this->xmlrequest->metadata->isPublished);
        if(isset($this->xmlrequest->readings['type']))                              $this->readingsType          = addslashes(          $this->xmlrequest->readings['type']);
        if(isset($this->xmlrequest->readings['units']))                             $this->readingsUnits         = (int)                $this->xmlrequest->readings['units'];
        if(isset($this->xmlrequest->metadata->operation))                           $this->vmeasurementOp        = addslashes(          $this->xmlrequest->metadata->operation);
        if(isset($this->xmlrequest->metadata->operation['parameter']))              $this->vmeasurementOpParam   = addslashes(          $this->xmlrequest->metadata->operation['parameter']);
        if(isset($this->xmlrequest->metadata->crossdate->basedOn->measurement['id']))       
                                                                                    $this->masterVMeasurementID  = (int)                $this->xmlrequest->metadata->crossdate->basedOn->measurement['id'];
        if(isset($this->xmlrequest->metadata->crossdate->startYear))                $this->startYear             = (int)                $this->xmlrequest->metadata->crossdate->startYear;
        if(isset($this->xmlrequest->metadata->crossdate->certaintyLevel))           $this->certaintyLevel        = (int)                $this->xmlrequest->metadata->crossdate->certaintyLevel;
        if(isset($this->xmlrequest->metadata->crossdate->justification))            $this->justification         = addslashes(          $this->xmlrequest->metadata->crossdate->justification);
        if(isset($this->xmlrequest->metadata->crossdate->startYear))                $this->newStartYear          = (int)                $this->xmlrequest->metadata->crossdate->startYear;

        
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

class taxonParameters extends parameters
{
    // TO DO!!!
    var $id         = NULL;

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
    var $snonce       = NULL;
    var $cnonce       = NULL;
    var $seq          = NULL;
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
        if(isset($this->xmlrequest[0]['cnonce']))                    $this->cnonce                      = addslashes($this->xmlrequest[0]['cnonce']);
        if(isset($this->xmlrequest[0]['snonce']))                    $this->snonce                      = addslashes($this->xmlrequest[0]['snonce']);
        if(isset($this->xmlrequest[0]['hash']))                      $this->hash                        = addslashes($this->xmlrequest[0]['hash']);
        if(isset($this->xmlrequest[0]['seq']))                       $this->seq                         = addslashes($this->xmlrequest[0]['seq']);
    }
}

class securityUserParameters extends parameters
{
    var $id            = NULL;
    var $username      = NULL;
    var $firstName     = NULL;
    var $lastName      = NULL;
    var $hashPassword  = NULL;
    var $isActive      = NULL;
    var $groupArray    = array();

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))              $this->id            = (int) $this->xmlrequest['id'];
        if(isset($this->xmlrequest['isActive']))        $this->isActive      = fromStringToPHPBool($this->xmlrequest['isActive']);
        if(isset($this->xmlrequest['username']))        $this->username      = addslashes(trim($this->xmlrequest['username']));
        if(isset($this->xmlrequest['firstName']))       $this->firstName     = addslashes(trim($this->xmlrequest['firstName']));
        if(isset($this->xmlrequest['lastName']))        $this->lastName      = addslashes(trim($this->xmlrequest['lastName']));
        if(isset($this->xmlrequest['plainPassword']))   $this->hashPassword  = hash('md5', addslashes(trim($this->xmlrequest['plainPassword'])));
        if(isset($this->xmlrequest['hashPassword']))    $this->hashPassword  = addslashes(trim($this->xmlrequest['hashPassword']));
        
        $groupArray = $this->xmlrequest->xpath('//memberOf');
        if (isset($this->xmlrequest->memberOf[0]))
        {
            foreach($this->xmlrequest->memberOf[0] as $item)
            {
                array_push($this->groupArray, (int) $item['id']);
            }
        }
        else
        {
            $this->groupArray = array('empty');
        }
    }

}

class securityGroupParameters extends parameters
{
    var $id            = NULL;
    var $name          = NULL;
    var $description   = NULL;
    var $isActive      = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))              $this->id            = (int) $this->xmlrequest['id'];
        if(isset($this->xmlrequest['isActive']))        $this->isActive      = fromStringToPHPBool($this->xmlrequest['isActive']);
        if(isset($this->xmlrequest['name']))            $this->name          = addslashes(trim($this->xmlrequest['name']));
        if(isset($this->xmlrequest['description']))     $this->description   = addslashes(trim($this->xmlrequest['description']));
    }

}

class searchParameters extends parameters
{
    var $returnObject            = NULL;
    var $limit                   = NULL;
    var $skip                    = NULL;
    var $allData                 = FALSE;
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
        if(isset($this->xmlrequest->all))      $this->allData       = TRUE;
                        
        foreach($this->xmlrequest->xpath('//param') as $param)
        {
            // Site Parameters
            if    ( ($param['name'] == 'siteid') || 
                    ($param['name'] == 'sitename') || 
                    ($param['name'] == 'sitecode') || 
                    ($param['name'] == 'sitecreated') || 
                    ($param['name'] == 'sitexcentroid') || 
                    ($param['name'] == 'siteycentroid') || 
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
                    ($param['name'] == 'originaltaxonname') || 
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
                array_push($this->measurementParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
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
