<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This file contains the interface and classes that contain the 
 * parameters requested by the user.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */
require_once('inc/dbEntity.php');
require_once('inc/dbhelper.php');

interface IParams
{
    function setParamsFromXMLRequest();
}


class searchParameters implements IParams
{
    var $xmlRequestDom 			 			= NULL;	
    var $returnObject            			= NULL;
    var $limit                   			= NULL;
    var $skip                    			= NULL;
    var $allData                 			= FALSE;
    
    var $objectParamsArray		 			= array();
    var $elementParamsArray					= array();
    var $sampleParamsArray					= array();
    var $radiusParamsArray					= array();
    var $measurementParamsArray  			= array();
    var $vmeasurementParamsArray  			= array();
    var $vmeasurementResultParamsArray  	= array();
    var $vmeasurementMetaCacheParamsArray  	= array();
    var $derivedCacheParamsArray			= array();


    function __construct($xmlrequest)
    {
        // Load the xmlrequest into a DOMDocument if it isn't already
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }	
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
        
        $this->setParamsFromXMLRequest();
    }

    function setParamsFromXMLRequest()
    {
    	global $corinaNS;
        global $tridasNS;
	
        // Get main attributes
    	$searchParamsTag = $this->xmlRequestDom->getElementsByTagName("searchParams")->item(0); 	
		$this->returnObject = addslashes($searchParamsTag->getAttribute("returnObject"));
		$this->limit = (int) $searchParamsTag->getAttribute("limit");
		$this->skip = (int) $searchParamsTag->getAttribute("skip");
		
		// Get individual params
		$paramsTags = $this->xmlRequestDom->getElementsByTagName("param");	
		
		foreach ($paramsTags as $param)
		{
			if($param->nodeType != XML_ELEMENT_NODE) continue; 
			
			// If the <all> tag is found set allData to true and finish
			if($param->tagName=='all') 
			{
				$this->allData=TRUE; 
				break;
			}
			
			// Detect table name from parameter name
			$paramType = substr(strtolower($param->getAttribute("name")), 0, 6);
			if($paramType=='elemen') $paramType = 'element';						
			    
			// Create a parameter array
			$paramArray = array ('name' => addslashes($param->getAttribute("name")), 
								 'operator' => addslashes($param->getAttribute("operator")),
								 'value' => addslashes($param->getAttribute("value")));
			
			// Add parameter array to the relevant *ParamsArray
			switch ($paramType)
			{
				case "object":	array_push($this->objectParamsArray, $paramArray);	break;
				
				case "element":	array_push($this->elementParamsArray, $paramArray ); break;

				case "sample":	array_push($this->sampleParamsArray, $paramArray );	break;
									
				case "radius":	array_push($this->radiusParamsArray, $paramArray );	break;
									
				default:		trigger_error("104"."Unknown parameter type of $paramType specified.", E_USER_ERROR);
			}	
		}		

	}
}

class dictionariesParameters implements IParams
{   
    protected $xmlRequestDom = NULL;

    function __construct($xmlrequest)
    {

    }

    function setParamsFromXMLRequest()
    {
    	return null;
    }
    
    function getID()
    {
    	return null;
    }
}


class authenticationParameters implements IParams
{
    protected $username      = NULL;
    protected $snonce        = NULL;
    protected $cnonce        = NULL;
    protected $seq           = NULL;
    protected $hash          = NULL;
    protected $password      = NULL;
    protected $xmlRequestDom = NULL;
    var 	  $hasChild 	 = NULL;
    var       $parentID 	 = NULL;

    

    function __construct($xmlrequest)
    {
        // Load the xmlrequest into a DOMDocument if it isn't already
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
        
        $this->setParamsFromXMLRequest();
    }

    function setParamsFromXMLRequest()
    {
    	$authTag = $this->xmlRequestDom->getElementsByTagName("authenticate")->item(0);
        if($authTag->getAttribute("username")!=NULL)   $this->username   = addslashes($authTag->getAttribute("username"));
        if($authTag->getAttribute("password")!=NULL)   $this->password   = addslashes($authTag->getAttribute("password"));
        if($authTag->getAttribute("cnonce")!=NULL)     $this->cnonce     = addslashes($authTag->getAttribute("cnonce"));
        if($authTag->getAttribute("snonce")!=NULL)     $this->snonce     = addslashes($authTag->getAttribute("snonce"));
        if($authTag->getAttribute("hash")!=NULL)       $this->hash       = addslashes($authTag->getAttribute("hash"));
        if($authTag->getAttribute("seq")!=NULL)        $this->seq        = addslashes($authTag->getAttribute("seq"));
    }
    
    function getUsername()
    {
    	return $this->username;
    }
    
    function getSNonce()
    {
    	return $this->snonce;
    }
    
    function getCNonce()
    {
    	return $this->cnonce;
    }
    
    function getSeq()
    {
    	return $this->seq;
    }
    
    function getHash()
    {
    	return $this->hash;
    }
    
    function getPassword()
    {
    	return $this->password;
    }
    
}

class objectParameters extends objectEntity implements IParams
{
	var $xmlRequestDom = NULL;
    var $hasChild   = FALSE;
    var $parentID   = NULL;

    function __construct($xmlrequest, $parentID=NULL)
    {
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
     		
        $this->parentID=$parentID;
            		
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
		global $corinaNS;
        global $tridasNS;
	

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "type":				$this->setType($child->nodeValue); break;
		   	case "description":			$this->setDescription($child->nodeValue); break;
		   	case "creator":				$this->setCreator($child->nodeValue); break;
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "owner":				$this->setOwner($child->nodeValue); break;
		   	case "file":				$this->setFile($child->nodeValue); break;
		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "code":
		   				$this->setCode($value);
		   				break;
		   			// Ignore autogenerated fields
		   			case "createdTimeStamp": break;
		   			case "lastModifiedTimeStamp": break;
		   			
		   			default:
		   			trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'object' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
		   		}
		   		break;
			
		   	case "coverage":			
		   		$coverageTags = $child->childNodes;
		   		foreach($covereageTags as $tag)
		   		{
		   			switch($tag->tagName)
		   			{
		   				case "coverageTemporal":			$coverageTemporal = $tag->nodeValue; break;
		   				case "coverageTemporalFoundation": 	$coverageTemporalFoundation = $tag->nodeValue; break;

		   			}
		   			
		   			if ( (isset($coverageTemporal)) && (isset($coverageTemporalFoundation)) ) $this->setCoverageTemporal($coverageTemporal, $coverageTemporalFoundation);
		
		   		}
		   	
		   	case "location":
		   		// @todo 
		   		break;
		   		
		   	case "object":
		   		trigger_error("901"."Nested objects not supported in XML request.  Use parentID instead to show relationships", E_USER_ERROR);
		   		break;
		   		
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'object' entity of the XML request", E_USER_NOTICE);
		   }
        }
        
        

        
        
        
    }	
}

class elementParameters extends elementEntity implements IParams
{   
	var $xmlRequestDom = NULL;
    var $hasChild   = FALSE;
    var $parentID   = NULL;

    function __construct($xmlrequest, $parentID=NULL)
    {
    	parent::__construct();
    	
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
    		
        $this->parentID=$parentID;
             		
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
	
	function setParamsFromXMLRequest()
	{
		global $corinaNS;
        global $tridasNS;
        global $taxonomicAuthorityEdition;

        $children = $this->xmlRequestDom->documentElement->childNodes;
               
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	

		   switch ($child->tagName)
		   {
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;		   		
		   	case "authenticity":	 	$this->setAuthenticity($child->nodeValue); break;   	
		   	case "shape": 				$this->setShape($child->nodeValue); break; 	  		
		   	case "function": 			$this->setType($child->nodeValue); break;
		   	case "file": 				$this->setFile($child->nodeValue); break;	   	
		   	case "processing": 			$this->setProcessing($child->nodeValue); break;	   	
		   	case "marks": 				$this->setMarks($child->nodeValue); break;	   	
		   	case "description":			$this->setDescription($child->nodeValue); break;		   		
		   	case "locationType": 		$this->geometry->setType($child->nodeValue); break;		   	
		   	case "locationPrecision": 	$this->geometry->setPrecision($child->nodeValue); break;		   	
		   	case "locationComment": 	$this->geometry->setComment($child->nodeValue); break;
		   	case "locationGeometry": 	$this->geometry->setGeometryFromGML($this->xmlRequestDom->saveXML($child)); break;
		   	
		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "name":
		   				$this->setName($value);
		   				break;
		   				
		   			// Ignore autogenerated fields
		   			case "kingdom":	break;
		   			case "phylum":	break;
		   			case "class":	break;
		   			case "order":	break;
		   			case "family":	break;
		   			case "genus":	break;
		   			case "species":	break;
		   			case "createdTimeStamp": break;
		   			case "lastModifiedTimeStamp": break;
		   			
		   			default:
		   			trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'element' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
		   		}
		   		break;
		    
		   	case "taxon":
		   		/* @todo Decisions need to be made as to how taxonomy and dictionaries will be handled
		   		 * */

		   		if($child->getAttribute("normalStd")==$taxonomicAuthorityEdition)
		   		{
		   			$this->taxon->setCoLID($child->getAttribute("normalId"));
		   			$this->taxon->setLabel($child->getAttribute("normal"));
		   			$this->taxon->setOriginalTaxon($child->nodeValue);
		   		}
		   		else
		   		{
					trigger_error("901"."The Corina web service only supports taxonomic data that conforms to the '$taxonomicAuthorityEdition'.  Please normalise your data and try again.", E_USER_ERROR);
		   		}
		   		break; 
		   	
		   	case "dimensions":
		   		$unitTag = $child->getElementsByTagName("unit")->item(0);
		   		$this->setDimensionUnits($unitTag->nodeValue.$child->getAttribute("power"));
		   		$dimensionTags = $unitTag->childNodes;
		   		
		   		foreach($dimensionTags as $dimension)
		   		{
		   			if($dimension->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($dimension->tagName)
		   			{
		   				case "diameter":
		   					$this->setDiameter($dimension->nodeValue);
		   					break;
		   				case "height":
		   					$this->setHeight($dimension->nodeValue);
		   					break;
		   				case "width":
		   					$this->setWidth($dimension->nodeValue);
		   					break;
		   				case "depth":
		   					$this->setDepth($dimension->nodeValue);
		   					break;
		   			}
		   		}
		   		break;
		   		
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'element' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   
		   }
        } 	


	}
	
	/**
	 * Get the ID of this element's parent 'object entity'
	 *
	 * @return Integer
	 */
    function getParentID()
    {
    	return $this->parentID;
    }	
	
}

class sampleParameters extends sampleEntity implements IParams
{   
	var $xmlRequestDom = NULL;
    var $hasChild   = FALSE;
    var $parentID   = NULL;
       
    function __construct($xmlrequest, $parentID=NULL)
    {
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
    		
        $this->parentID=$parentID;
                
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
	
	
    function setParamsFromXMLRequest()
    {
		global $corinaNS;
        global $tridasNS;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "type":				$this->setType($child->nodeValue); break;
		   	case "samplingDate":		$this->setSamplingDate($child->nodeValue); break;
		   	case "file":				$this->setFile($child->nodeValue); break;
		   	case "position":			$this->setPosition($child->nodeValue); break;
		   	case "state":				$this->setState($child->nodeValue); break;
		   	case "knots":				$this->setKnots(formatBool($child->nodeValue)); break;
		   	case "description":			$this->setDescription($child->nodeValue); break;		

		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "name":
		   				$this->setName($value);
		   				break;
		   			// Ignore autogenerated fields
		   			case "createdTimeStamp": break;
		   			case "lastModifiedTimeStamp": break;
		   			
		   			default:
		   			trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'sample' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
		   		}
		   		break;
		   				   	
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'sample' entity of the XML request", E_USER_NOTICE);
		   }
        }
    }

    /**
     * Get the Id of this sample's parent 'element' entity
     *
     * @return Integer 
     */
    function getParentID()
    {
    	return $this->parentID;
    }
}

class radiusParameters extends radiusEntity implements IParams
{   
	var $xmlRequestDom = NULL;
    var $hasChild   = FALSE;
    var $parentID   = NULL;

    function __construct($xmlrequest, $parentID=NULL)
    {
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
    		
        $this->parentID=$parentID;
        
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
		global $corinaNS;
        global $tridasNS;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "pith":				$this->setPithPresent(formatBool($child->getAttribute("presence"))); break;
		   	case "bark":				$this->setBarkPresent(formatBool($child->getAttribute("presence"))); break;

		    case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "name":
		   				$this->setName($value);
		   				break;
		   				
		   			// Ignore autogenerated fields
		   			case "createdTimeStamp": break;
		   			case "lastModifiedTimeStamp": break;
		   			
		   			default:
		   				trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'radius' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
		   		}
		   		break;
		   	
		   	case "sapwood":
		   		$this->setSapwood($child->getAttribute("presence"));
		   		$sapwoodtags = $child->childNodes;
		   		
		   		foreach($sapwoodtags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($tag->tagName)
		   			{
		   				case "nrOfSapwoodRings": 						$this->setNumberOfSapwoodRings($tag->nodeValue); break;
		   				case "lastRingUnderBark": 						$this->setLastRingUnderBark($tag->nodeValue); break;
		   				case "missingSapwoodRingsToBark":				$this->setMissingSapwoodRingsToBark($tag->nodeValue); break;
		   				case "missingSapwoodRingsToBarkFoundation": 	$this->setMissingSapwoodRingsToBarkFoundation($tag->nodeValue); break;
		   				default:
		   					trigger_error("901"."Unknown tag &lt;".$tag->tagName."&gt; in 'radius.sapwood' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   			}
		   		}		   			
				break;
		   		
		   	case "heartwood":
		   		$this->setHeartwoodPresent($child->getAttribute("presence"));
		   		
		   		$heartwoodtags = $child->childNodes;
		   		
		   		foreach($heartwoodtags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch($tag->tagName)
		   			{
		   				case "missingHeartwoodRingsToPith":				$this->setMissingHeartwoodRingsToPith($tag->nodeValue); break;
		   				case "missingHeartwoodRingsToPithFoundation":	$this->setMissingHeartwoodRingsToPithFoundation($tag->nodeValue); break;	
		   			}
		   		}
		   		break;
		   	
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'radius' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   }
        }
    }	
}
        

class measurementParameters extends measurementEntity implements IParams
{   
	var $xmlRequestDom = NULL;
    var $hasChild   = FALSE;
    var $parentID   = NULL;

    function __construct($xmlrequest, $parentID=NULL)
    {
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='object')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
    		
        $this->parentID=$parentID;
        
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
		global $corinaNS;
        global $tridasNS;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;


		    case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "name":
		   				$this->setName($value);
		   				break;
		   				
		   			// Ignore autogenerated fields
		   			case "createdTimeStamp": break;
		   			case "lastModifiedTimeStamp": break;
		   			
		   			default:
		   				trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'series' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
		   		}
		   		break;
		   		   	
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'series' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   }
        }
    }	
}





/**
 * Old stuff needs refactoring
 *
 */


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
        echo "This function must be overloaded";
        die();
    }
    
}
/*
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
        if(isset($this->xmlrequest->sample))                $this->hasChild             = True;
        
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
*/


?>
