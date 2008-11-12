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
        if(isset($this->xmlrequest->element))      $this->hasChild     = True;
    }

}

class elementParameters extends parameters
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
    var $elementNoteArray      = array();

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
        if(isset($this->xmlrequest->sample))              $this->hasChild             = True;
        
        $elementNotes = $this->xmlrequest->xpath('//elementNotes');
        if (isset($elementNotes[0]->elementNote[0]))
        {
            foreach($elementNotes[0] as $item)
            {
                array_push($this->elementNoteArray, $item['id']);
            }
        }
        else
        {
            $this->elementNoteArray = array('empty');
        }
    }
}

class sampleParameters extends parameters
{
    var $id                            = NULL;
    var $elementID                        = NULL;
    var $name                          = NULL;
    var $dateCollected                 = NULL;
    var $sampleType                  = NULL;
    var $terminalRing                  = NULL;
    var $sapwoodCount                  = NULL;
    var $sampleQuality               = NULL;
    var $sampleContinuity            = NULL;
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
        $this->elementID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                          $this->id                               = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest->name))                          $this->name                             = addslashes($this->xmlrequest->name);
        if(isset($this->xmlrequest->dateCollected))                 $this->dateCollected                    = addslashes($this->xmlrequest->dateCollected);
        if(isset($this->xmlrequest->sampleType))                  $this->sampleType                     = addslashes($this->xmlrequest->sampleType);
        if(isset($this->xmlrequest->terminalRing))                  $this->terminalRing                     = addslashes($this->xmlrequest->terminalRing);
        if(isset($this->xmlrequest->sapwoodCount))                  $this->sapwoodCount                     = (int)      $this->xmlrequest->sapwoodCount;
        if(isset($this->xmlrequest->sampleQuality))               $this->sampleQuality                  = addslashes($this->xmlrequest->sampleQuality);
        if(isset($this->xmlrequest->sampleContinuity))            $this->sampleContinuity               = addslashes($this->xmlrequest->sampleContinuity);
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
    var $sampleID = NULL;

    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->sampleID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }
    
    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                  $this->id           = (int)      $this->xmlrequest['id'];
        if(isset($this->xmlrequest->name))                  $this->name         = addslashes($this->xmlrequest->name);
        if(isset($this->xmlrequest->series))           $this->hasChild     = True;
    }
}



class seriesParameters extends parameters
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
    var $vseriesOp         = NULL;
    var $vseriesOpParam    = NULL;
    var $valueType            = NULL;
    var $valueUnits           = NULL;
    var $valuesArray          = array();
    var $referencesArray        = array();
    var $seriesNoteArray   = array();


    function __construct($metaHeader, $auth, $xmlrequest, $parentID=NULL)
    {
        $this->radiusID = $parentID;
        parent::__construct($metaHeader, $auth, $xmlrequest);
    }

    function getXMLParams()
    {
        if(isset($this->xmlrequest['id']))                                 $this->id                    = (int)                $this->xmlrequest['id'];
        if(isset($this->xmlrequest->metadata->measuredBy['id']))           $this->measuredByID          = (int)                $this->xmlrequest->metadata->measuredBy['id'];
        if(isset($this->xmlrequest->metadata->owner['id']))                $this->ownerUserID           = (int)                $this->xmlrequest->metadata->owner['id'];
        //if(isset($this->xmlrequest->metadata->datingTypeID))               $this->datingTypeID          = addslashes(          $this->xmlrequest->metadata->datingTypeID);
        if(isset($this->xmlrequest->metadata->dating['startYear']))        $this->startYear             = (int)                $this->xmlrequest->metadata->dating['startYear'];
        if(isset($this->xmlrequest->metadata->dating['type']))             $this->datingType            = addslashes(          $this->xmlrequest->metadata->dating['type']);
        if(isset($this->xmlrequest->metadata->dating['positiveError']))    $this->datingErrorPositive   = (int)                $this->xmlrequest->metadata->dating['positiveError'];
        if(isset($this->xmlrequest->metadata->dating['negativeError']))    $this->datingErrorNegative   = (int)                $this->xmlrequest->metadata->dating['negativeError'];
        if(isset($this->xmlrequest->metadata->name))                       $this->name                  = addslashes(          $this->xmlrequest->metadata->name);
        if(isset($this->xmlrequest->metadata->description))                $this->description           = addslashes(          $this->xmlrequest->metadata->description);
        if(isset($this->xmlrequest->metadata->isLegacyCleaned))            $this->isLegacyCleaned       = fromStringtoPHPBool( $this->xmlrequest->metadata->isLegacyCleaned);
        if(isset($this->xmlrequest->metadata->isReconciled))               $this->isReconciled          = fromStringtoPHPBool( $this->xmlrequest->metadata->isReconciled);
        if(isset($this->xmlrequest->metadata->isPublished))                $this->isPublished           = fromStringtoPHPBool( $this->xmlrequest->metadata->isPublished);
        if(isset($this->xmlrequest->metadata->values['type']))           $this->valueType           = addslashes(          $this->xmlrequest->metadata->values['type']);
        if(isset($this->xmlrequest->metadata->values['units']))          $this->valueUnits          = addslashes(          $this->xmlrequest->metadata->values['units']);
        if(isset($this->xmlrequest->metadata->operation))                  $this->vseriesOp        = addslashes(          $this->xmlrequest->metadata->operation);
        if(isset($this->xmlrequest->metadata->operation['parameter']))     $this->vseriesOpParam   = addslashes(          $this->xmlrequest->metadata->operation['parameter']);
        
        foreach($this->xmlrequest->xpath('//references/series') as $refseries)
        {
            if($refseries['id']) array_push($this->referencesArray, $refseries['id']);
        }
        
        $theYear =-1;
        foreach($this->xmlrequest->xpath('//values/value') as $value)
        {
            if ($value['year']!=NULL) 
            {
                // If the XML includes a year attribute use it
                $theYear = (int) $value['year'];
            }
            else
            {
                // Otherwise use relative years - base 0
                $theYear++; 
            }

            $theValue = (int) $value['value'];
            $this->valuesArray[$theYear] = array('value' => $theValue, 'wjinc' => NULL, 'wjdec' => NULL, 'count' => 1, 'notesArray' => array());
                
            if(isset($value->valueNote))
            {
                foreach($value->valueNote as $valueNote)
                {
                    array_push($this->valuesArray[$theYear], (int) $valueNote['id']); 
                }

            }

        }
        
        $seriesNotes = $this->xmlrequest->xpath('//seriesNotes');
        if (isset($seriesNotes[0]->seriesNote[0]))
        {
            foreach($measurmentNotes[0] as $item)
            {
                array_push($this->seriesNoteArray, $item['id']);
            }
        }
        else
        {
            $this->seriesNoteArray = array('empty');
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

class elementNoteParameters extends parameters
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

class vseriesNoteParameters extends parameters
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

class valueNoteParameters extends parameters
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
    var $elementParamsArray         = array();
    var $sampleParamsArray     = array();
    var $radiusParamsArray       = array();
    var $seriesParamsArray  = array();
    var $vseriesParamsArray  = array();
    var $vseriesResultParamsArray  = array();
    var $vseriesMetaCacheParamsArray  = array();


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
            elseif( ($param['name'] == 'elementid') || 
                    ($param['name'] == 'elementname') || 
                    ($param['name'] == 'originaltaxonname') || 
                    ($param['name'] == 'elementcreated') || 
                    ($param['name'] == 'elementlastmodified') || 
                    ($param['name'] == 'precision') || 
                    ($param['name'] == 'isliveelement') || 
                    ($param['name'] == 'latitude') || 
                    ($param['name'] == 'longitude'))
            {
                array_push($this->elementParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
            }  

            // Specimen parameters
            elseif( ($param['name'] == 'sampleid') || 
                    ($param['name'] == 'samplename') || 
                    ($param['name'] == 'datecollected') || 
                    ($param['name'] == 'samplecreated') || 
                    ($param['name'] == 'samplelastmodified') || 
                    ($param['name'] == 'sampletypeid') || 
                    ($param['name'] == 'sampletype') || 
                    ($param['name'] == 'isterminalringverified') || 
                    ($param['name'] == 'sapwoodcount') || 
                    ($param['name'] == 'issapwoodcountverified') || 
                    ($param['name'] == 'issamplequalityverified') || 
                    ($param['name'] == 'ispithverified') || 
                    ($param['name'] == 'unmeaspre') || 
                    ($param['name'] == 'unmeaspost') || 
                    ($param['name'] == 'isunmeaspreverified') || 
                    ($param['name'] == 'isunmeaspostverified') || 
                    ($param['name'] == 'terminalringid') || 
                    ($param['name'] == 'terminalring') || 
                    ($param['name'] == 'samplequalityid') || 
                    ($param['name'] == 'samplequality') || 
                    ($param['name'] == 'samplecontinuityid') || 
                    ($param['name'] == 'samplecontinuity') || 
                    ($param['name'] == 'pithid') || 
                    ($param['name'] == 'pith') || 
                    ($param['name'] == 'issamplecontinuityverified'))
            {
                array_push($this->sampleParamsArray, array ('name' => addslashes($param['name']), 'operator' => $param['operator'], 'value' => addslashes($param['value'])));
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
            elseif( ($param['name'] == 'seriesname') || 
                    ($param['name'] == 'seriesoperator') || 
                    ($param['name'] == 'seriesdescription') || 
                    ($param['name'] == 'seriesispublished') || 
                    ($param['name'] == 'seriesowneruserid') || 
                    ($param['name'] == 'seriescreated') || 
                    ($param['name'] == 'operatorparameter') || 
                    ($param['name'] == 'serieslastmodified'))
            {
                array_push($this->vseriesParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }
            elseif( ($param['name'] == 'seriesid') ) 
            {
                array_push($this->vseriesParamsArray, array ('name' => addslashes('vseriesid'), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }
            elseif( ($param['name'] == 'seriesisreconciled') || 
                    ($param['name'] == 'datingtype') || 
                    ($param['name'] == 'datingerrornegative') || 
                    ($param['name'] == 'datingerrorpositive'))
            {
                array_push($this->vseriesResultParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }
            elseif( ($param['name'] == 'startyear') || 
                    ($param['name'] == 'valuecount') || 
                    ($param['name'] == 'seriescount') || 
                    ($param['name'] == 'seriesymin') || 
                    ($param['name'] == 'seriesymax') || 
                    ($param['name'] == 'seriesxmin') || 
                    ($param['name'] == 'seriesxmax') || 
                    ($param['name'] == 'seriesxcentroid') || 
                    ($param['name'] == 'seriesycentroid'))
            {
                array_push($this->vseriesMetaCacheParamsArray, array ('name' => addslashes($param['name']), 'operator' => addslashes($param['operator']), 'value' => addslashes($param['value'])));
            }

        }
    }
}
?>
