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
 * @package CorinaWS
 * *******************************************************************
 */
require_once('inc/dbEntity.php');
require_once('inc/dbhelper.php');

/**
 * Interface for parameters objects
 *
 */
interface IParams
{
	function __construct($xmlrequest);
    function setParamsFromXMLRequest();
}


class searchParameters implements IParams
{
    var $xmlRequestDom 			 			= NULL;	
    var $returnObject            			= NULL;
    var $limit                   			= NULL;
    var $skip                    			= NULL;
    var $allData                 			= FALSE;
    var $includeChildren					= FALSE;
    
    var $paramsArray						= array();
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
		$this->includeChildren = (bool) $searchParamsTag->getAttribute("includeChildren");
		
		// Get individual params
		$paramsTags = $this->xmlRequestDom->getElementsByTagName("param");	
		
				// Create an array for translating the search parameters names into Corina database table and field names
		$translationArray = array (		
									'objectid' => 							array('tbl' => 'vwtblobject', 'field' => 'objectid'),
									//'objectdbid' => 						array('tbl' => 'vwtblobject', 'field' => 'objectid'),                        
									'objecttitle' => 						array('tbl' => 'vwtblobject', 'field' => 'title'),
									'objectcreated' => 						array('tbl' => 'vwtblobject', 'field' => 'createdtimestamp'),
									'objectlastmodified' => 				array('tbl' => 'vwtblobject', 'field' => 'lastmodifiedtimestamp'),
									'objectdescription' => 					array('tbl' => 'vwtblobject', 'field' => 'description'),
									'objectcreator' => 						array('tbl' => 'vwtblobject', 'field' => 'creator'),
									'objectowner' => 						array('tbl' => 'vwtblobject', 'field' => 'owner'),
									'objectfile' => 						array('tbl' => 'vwtblobject', 'field' => 'file'),
									'objectcoverageTemporal' => 			array('tbl' => 'vwtblobject', 'field' => 'coveragetemporal'),
									'objectcoverageTemporalFoundation' => 	array('tbl' => 'vwtblobject', 'field' => 'coveragetemporalfoundation'),
									'objectlocationtype' => 				array('tbl' => 'vwtblobject', 'field' => 'locationtype'),
									'objectlocationprecision' => 			array('tbl' => 'vwtblobject', 'field' => 'locationprecision'),
									'objectlocationcomment' => 				array('tbl' => 'vwtblobject', 'field' => 'locationcomment'),
									'objecttype' =>							array('tbl' => 'vwtblobject', 'field' => 'type'),	
									'parentobjectid' =>						array('tbl' => 'vwtblobject', 'field' => 'parentobjectid'),
									'objectcode' =>							array('tbl' => 'vwtblobject', 'field' => 'code'),
		
									'elementid' => 							array ('tbl' => 'vwtblelement', 	'field'  => 'elementid'),
									//'elementdbid' => 						array('tbl' => 'vwtblelement', 'field' => 'elementid'),
									'elementoriginaltaxonname' => 			array('tbl' => 'vwtblelement', 'field' => 'originaltaxonname'),
									//'elementphylumname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementclassname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementordername' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementfamilyname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementgenusname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementspeciesname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementinfraspeciesname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementinfraspeciestype' => array('tbl' => 'vwtblelement', 'field' => ''),
									'elementauthenticity' => 				array('tbl' => 'vwtblelement', 'field' => 'elementauthenticity'),
									'elementshape' => 						array('tbl' => 'vwtblelement', 'field' => 'elementshape'),
									'elementheight' => 						array('tbl' => 'vwtblelement', 'field' => 'height'),
									'elementwidth' => 						array('tbl' => 'vwtblelement', 'field' => 'width'),
									'elementdepth' => 						array('tbl' => 'vwtblelement', 'field' => 'depth'),
									'elementdiameter' => 					array('tbl' => 'vwtblelement', 'field' => 'diameter'),
									'elementdimensionunits' => 				array('tbl' => 'vwtblelement', 'field' => 'units'),
									//'elementdimensionunitspower' => array('tbl' => 'vwtblelement', 'field' => ''),
									'elementtype' => 						array('tbl' => 'vwtblelement', 'field' => 'elementtype'),
									'elementfile' => 						array('tbl' => 'vwtblelement', 'field' => 'file'),
									'elementlocationtype' => 				array('tbl' => 'vwtblelement', 'field' => 'locationtype'),
									'elementlocationprecision' => 			array('tbl' => 'vwtblelement', 'field' => 'locationprecision'),
									'elementlocationcomment' => 			array('tbl' => 'vwtblelement', 'field' => 'locationcomment'),
									'elementprocessing' => 					array('tbl' => 'vwtblelement', 'field' => 'processing'),
									'elementmarks' => 						array('tbl' => 'vwtblelement', 'field' => 'marks'),
									'elementdescription' => 				array('tbl' => 'vwtblelement', 'field' => 'description'),
									'elementcreated' => 					array('tbl' => 'vwtblelement', 'field' => 'createdtimestamp'),
									'elementlastmodified' =>	 			array('tbl' => 'vwtblelement', 'field' => 'lastmodifiedtimestamp'),

									'sampleid' => 							array('tbl' => 'vwtblsample', 'field' => 'sampleid'),
									//'sampledbid' => 						array('tbl' => 'vwtblsample', 'field' => 'sampleid'),
									'samplingdate' => 						array('tbl' => 'vwtblsample', 'field' => 'samplingdate'),
									'samplefile' => 						array('tbl' => 'vwtblsample', 'field' => 'file'),
									'sampleposition' => 					array('tbl' => 'vwtblsample', 'field' => 'position'),
									'samplestate' => 						array('tbl' => 'vwtblsample', 'field' => 'state'),
									'samplehasknots' => 					array('tbl' => 'vwtblsample', 'field' => 'knots'),
									'sampledescription' => 					array('tbl' => 'vwtblsample', 'field' => 'description'),
									'samplecreated' => 						array('tbl' => 'vwtblsample', 'field' => 'createtimestamp'),
									'samplelastmodified' => 				array('tbl' => 'vwtblsample', 'field' => 'lastmodifiedtimstamp'),
									'samplingdatecertainty' => 				array('tbl' => 'vwtblsample', 'field' => 'datecertainty'),
									
								  );
		
		// Loop through each param tag
		foreach ($paramsTags as $param)
		{
			if($param->nodeType != XML_ELEMENT_NODE) continue; 
			
			// If the <all> tag is found set allData to true and finish
			if($param->tagName=='all') 
			{
				$this->allData=TRUE; 
				break;
			}
	
			// Use translation array to get the correct table and field names
			if(isset($translationArray[$param->getAttribute("name")]))
			{
				$fieldname = $translationArray[$param->getAttribute("name")]['field'];
				$tablename = $translationArray[$param->getAttribute("name")]['tbl'];
				$operator = addslashes($param->getAttribute("operator"));
				$value = addslashes($param->getAttribute("value"));
				$temparr = array('table'=>$tablename, 'field'=>$fieldname, 'operator'=>$operator, 'value'=>$value);
	
				// Add params to a parametersArray
				array_push($this->paramsArray,  $temparr);
			}
			else
			{
				trigger_error("104"."Unknown parameter type of $paramType specified.", E_USER_ERROR);
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
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "createdTimestamp":	break;
		   	case "lastModifiedTimestamp": break;

		   	case "location": 
				$locationTags = $child->childNodes;
				foreach($locationTags as $tag)
				{
		  	 		if($tag->nodeType != XML_ELEMENT_NODE) continue;  
		  	 		$this->location->setGeometryFromGML($this->xmlRequestDom->saveXML($tag)); break;
				}
		   	
		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{	   			
		   			case "labCode" : $this->setCode($value); break;
		   			
		   			default:
		   			trigger_error("901"."Unknown attribute type $name in the &lt;".$child->tagName."&gt; tag of the 'object'. This tag is being ignored", E_USER_NOTICE);
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
		   	case "type": 				$this->setType($child->nodeValue); break;
		   	case "file": 				$this->setFile($child->nodeValue); break;	   	
		   	case "processing": 			$this->setProcessing($child->nodeValue); break;	   	
		   	case "marks": 				$this->setMarks($child->nodeValue); break;	   	
		   	case "description":			$this->setDescription($child->nodeValue); break;		   		
		   	case "locationType": 		$this->location->setType($child->nodeValue); break;		   	
		   	case "locationPrecision": 	$this->location->setPrecision($child->nodeValue); break;		   	
		   	case "locationComment": 	$this->location->setComment($child->nodeValue); break;
		   	case "locationGeometry": 	$this->location->setGeometryFromGML($this->xmlRequestDom->saveXML($child)); break;
		   	case "createdTimestamp":	  break;
		   	case "lastModifiedTimestamp": break;		   	
		   	
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

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "type":				$this->setType($child->nodeValue); break;
		   	case "samplingDate":		$this->setSamplingDate($child->nodeValue); break;
		   	case "file":				$this->setFile($child->nodeValue); break;
		   	case "position":			$this->setPosition($child->nodeValue); break;
		   	case "state":				$this->setState($child->nodeValue); break;
		   	case "knots":				$this->setKnots(formatBool($child->nodeValue)); break;
		   	case "description":			$this->setDescription($child->nodeValue); break;	
		   	case "createdTimestamp":	  break;
		   	case "lastModifiedTimestamp": break;			   		

		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{	   			
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

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "pith":				$this->setPithPresent(dbHelper::formatBool($child->getAttribute("presence"))); break;
		   	case "bark":				$this->setBarkPresent(dbHelper::formatBool($child->getAttribute("presence"))); break;
		   	case "azimuth":				$this->setAzimuth($child->nodeValue); break;
   			case "createdTimestamp": 	  break;
   			case "lastModifiedTimestamp": break;
		   	
   			/*
		    case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{		   			
		   			default:
		   				trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'radius' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   		}
		   		break;
			*/
		   	
		   	case "sapwood":
		   		$this->setSapwood(NULL, $child->getAttribute("presence"));
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
		   		$this->setHeartwood(null, $child->getAttribute("presence"));
		   		
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

        if($this->xmlRequestDom->documentElement->tagName=='measurementSeries')
        {
        	$this->setVMeasurementOp(5, 'Direct');
        }  

        
        $children = $this->xmlRequestDom->documentElement->childNodes;   
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "title":				$this->setTitle($child->nodeValue);
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	// Ignore user name fields as we use genericField ID's for these instead
		   	case "analyst":				break;
		   	case "dendrochronologist":	break;
		   	case "author":				break;
		   	case "measuringMethod":		$this->setMeasurementMethod(NULL, $child->nodeValue); break;
		   	case "comments":			$this->setComments($child->nodeValue); break;
		   	case "usage":				$this->setUsage($child->nodeValue); break;
		   	case "usageComments":		$this->setUsageComments($child->nodeValue); break;
		   	case "type":				$this->setVMeasurementOp(null, $child->nodeValue); break;			
   			case "standardizingMethod":	$this->setStandardizingMethod(null, $child->nodeValue); break;
   			case "createdTimeStamp": 		break;
   			case "lastModifiedTimeStamp": 	break;		
   			   	
   			case "linkSeries":
   				$seriesTags = $child->childNodes;
   				foreach($seriesTags as $series)
   				{
   					if($series->nodeType != XML_ELEMENT_NODE) continue;
   					if($series->nodeName=='identifier')
   					{
   						array_push($this->referencesArray, $series->nodeValue);  		
   					}
   				}
   				break;	
   		
		   	case "units":
		   		$unitsName = $child->getAttribute("name");
		   		$unitsDescription = $child->getAttribute("description");
		   		$unitsFactor = $child->getAttribute("factor");
		   		$unitsSystem = $child->getAttribute("system");
		   		$unitTags = $child->childNodes;
		   		foreach($unitTags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			if($tag->tagName=='unit')
		   			{
		   				$unitPower = $tag->getAttribute("power");
		   				$unitDescription = $tag->getAttribute("description");
		   				$unit = $tag->nodeValue;
		   			}
		   		}		

		   		$this->setUnits(NULL, $unitsName);
		   		$this->units->setPower($unitPower);
				break;		   		
			
		   	case "interpretation":
		   		$interpTags = $child->childNodes;
		   		foreach($interpTags as $interpTag)
		   		{
		   			if($interpTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch($interpTag->nodeName)
		   			{
		   				case "firstYear":
		   					$this->setFirstYear($interpTag->nodeValue);
		   					break;
		   					
		   				//<tridas:calendar><tridas:linkseries>
		   				case "usedSeries":
		   					$this->setMasterVMeasurementID($interpTag->nodeValue);
		   					break;
		   					
		   				default:
		   					break;
		   			}
		   		}
		   		break;
				
		    case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "analystID":
		   				$this->analyst->setParamsFromDB($value);
		   				break;
		   			
		   			case "dendrochronologistID":
		   				$this->dendrochronologist->setParamsFromDB($value);
		   				break;

		   			case "authorID":
		   				$this->setAuthor($value);
		   				break;
		   				
		   			case "isLegacyCleaned":
		   				$this->setIsLegacyCleaned($value);
		   				break;
		   			
		   			case "isPublished":
		   				break;
		   			
		   			case "code":
		   				$this->setCode($value);
		   				break;
		   				
		   			case "crossdateJustification":
		   				$this->setJustification($value);
		   				break;
		   			
		   			case "crossdateConfidenceLevel":
		   				$this->setConfidenceLevel($value);
		   				break;

		   			
		   			default:
		   				trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; with name attribute '".$name."' in the series entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
		   		}
		   		break;
		   			   		
		   	case "values":
		   		
		   		$this->setVariable(NULL, $child->getAttribute("type"));
		   		$valuetags = $child->childNodes;
		   		$i = 0;
		   		
		   		foreach($valuetags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			if($tag->tagName == 'value') 
		   			{	
		   				// Tag is a <value> tag so process
		   				
		   				// get the index and value fields
			   			$index = $tag->getAttribute("index");
			   			$value = $tag->getAttribute("value");  

			   			// See if it has child nodes (notes or subvalues)
			   			$valuechildren = $tag->childNodes;
		   				$myNotesArray = array();			   			
			   			foreach($valuechildren as $valuechild)
			   			{
			   				if($valuechild->nodeType != XML_ELEMENT_NODE) continue;
			   				if($valuechild->tagName == 'note')
			   				{
			   					// Add notes in a notes array
			   					array_push($myNotesArray, $valuechild->nodeValue);
			   				}
			   				
			   			}

			   			// Mush all the reading and notes into the ReadingArray
						$this->readingsArray[$i] = array('value' => $value, 
	                                                     'wjinc' => NULL, 
	                                                     'wjdec' => NULL, 
	                                                     'count' => NULL,
	                                                     'notesArray' => $myNotesArray);
		   			}
		   			else
		   			{
		   				// Tag not a value tag so skip
		   				continue;
		   			}
		   			
		   			// Increment readingArray counter
					$i++;
					
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
/*

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
