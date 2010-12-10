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
        global $firebug;
	
        // Get main attributes
    	$searchParamsTag = $this->xmlRequestDom->getElementsByTagName("searchParams")->item(0); 	
		$this->returnObject = $searchParamsTag->getAttribute("returnObject");
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
									'objectlocation' =>						array('tbl' => 'vwtblobject', 'field' => 'locationgeometry'),
									'countOfChildSeriesOfObject' =>			array('tbl' => 'vwtblobject', 'field' => 'countofchildvmeasurements'),
									'anyparentobjectid' =>					array('tbl' => 'vwtblobject', 'field' => 'anyparentobjectid'),
									'anyparentobjectcode' =>				array('tbl' => 'vwtblobject', 'field' => 'anyparentobjectcode'),
		
									'boxid' => 								array('tbl' => 'vwtblbox', 'field' => 'boxid'),
		
		
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
									'elementcode' =>						array('tbl' => 'vwtblelement', 'field' => 'code'),
		
									'sampleid' => 							array('tbl' => 'vwtblsample', 'field' => 'sampleid'),
									'sampledbid' => 						array('tbl' => 'vwtblsample', 'field' => 'sampleid'),
									'samplingdate' => 						array('tbl' => 'vwtblsample', 'field' => 'samplingdate'),
									'samplefile' => 						array('tbl' => 'vwtblsample', 'field' => 'file'),
									'sampleposition' => 					array('tbl' => 'vwtblsample', 'field' => 'position'),
									'samplestate' => 						array('tbl' => 'vwtblsample', 'field' => 'state'),
									'samplehasknots' => 					array('tbl' => 'vwtblsample', 'field' => 'knots'),
									'sampledescription' => 					array('tbl' => 'vwtblsample', 'field' => 'description'),
									'samplecreated' => 						array('tbl' => 'vwtblsample', 'field' => 'createtimestamp'),
									'samplelastmodified' => 				array('tbl' => 'vwtblsample', 'field' => 'lastmodifiedtimstamp'),
									'samplingdatecertainty' => 				array('tbl' => 'vwtblsample', 'field' => 'datecertainty'),
									'sampleboxid' =>						array('tbl' => 'vwtblsample', 'field' => 'boxid'),
									'samplecode' =>							array('tbl' => 'vwtblsample', 'field' => 'code'),
		

                                    'radiusid' =>                           		array('tbl' => 'vwtblradius', 'field' => 'radiusid'),
									//'radiusdbid' 
									'radiuspith'=>									array('tbl' => 'vwtblradius', 'field' => 'radiuspith'),
									'radiussapwood'=>								array('tbl' => 'vwtblradius', 'field' => 'sapwood'),
									'radiusheartwood'=>								array('tbl' => 'vwtblradius', 'field' => 'heartwood'),
									'radiusbark'=>									array('tbl' => 'vwtblradius', 'field' => 'barkpresent'),
									'radiusnumbersapwoodrings'=>					array('tbl' => 'vwtblradius', 'field' => 'numberofsapwoodrings'),
									'radiuslastringunderbark'=>						array('tbl' => 'vwtblradius', 'field' => 'lastringunderbark'),
									'radiusmissingheartwoodringstopith'=>			array('tbl' => 'vwtblradius', 'field' => 'missingheartwoodringstopith'),
									'radiusmissingheartwoodringstopithfoundation'=>	array('tbl' => 'vwtblradius', 'field' => 'missingheartwoodringstopithfoundation'),
									'radiusmissingsapwoodringstobark'=>				array('tbl' => 'vwtblradius', 'field' => 'missingsapwoodringstobark'),
									'radiusmissingsapwoodringstobarkfoundation'=>	array('tbl' => 'vwtblradius', 'field' => 'missingsapwoodringstobarkfoundation'),
									'radiuscreated'=>								array('tbl' => 'vwtblradius', 'field' => 'radiuscreated'),
									'radiuslastmodified'=>							array('tbl' => 'vwtblradius', 'field' => 'radiuslastmodified'),
									'radiusazimuth'=>								array('tbl' => 'vwtblradius', 'field' => 'azimuth'),
									'radiustitle'=>									array('tbl' => 'vwtblradius', 'field' => 'radiuscode'),
									'radiuscode' =>									array('tbl' => 'vwtblradius', 'field' => 'radiuscode'),	
		
									'seriesid' =>                           array('tbl' => 'vwcomprehensivevm', 'field' => 'vmeasurementid'),
									'seriesdbid' =>                           array('tbl' => 'vwcomprehensivevm', 'field' => 'vmeasurementid'),
		
									//'seriesmeasuringmethod' =>				array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesvariable'=>						array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesunit'=>							array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriespower' =>						array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									'seriesmeasuringdate' =>				array('tbl' => 'vwcomprehensivevm', 'field' => 'birthdate'),
									//'seriesanalyst' =>						array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesdendrochronologist' =>			array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									'seriescomments' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'comments'),

									'seriesfirstyear' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'startyear'),
									//'seriessproutyear' =>					array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesdeathyear' =>					array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									'seriesprovenance' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'provenance'),
									'seriestype' =>							array('tbl' => 'vwcomprehensivevm', 'field' => 'opname'),
									//'seriesstandardizingmethod' =>			array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesauthor' =>						array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									'seriesobjective' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'objective'),
									'seriesversion' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'version'),
									'seriesderivationdate' =>				array('tbl' => 'vwcomprehensivevm', 'field' => 'birthdate'),
									'serieslastmodified' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'lastmodifiedtimestamp'),
									'seriesoperatorparameter' =>			array('tbl' => 'vwcomprehensivevm', 'field' => 'vmeasurementopparameter'),
									'seriesisreconciled' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'isreconciled'),
									'seriesdatingtype' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'datingtype'),
									//'seriesdatingerrorpositive' =>			array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesdatingerrornegative' =>			array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									//'seriesvaluecount' =>					array('tbl' => 'vwcomprehensivevm', 'field' => ''),
									'seriescount' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'directchildcount'),
									'seriescode' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'code')
		
								
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
	
			$firebug->log($param->getAttribute("name"), "param name");
			
			// Use translation array to get the correct table and field names
			if(isset($translationArray[$param->getAttribute("name")]))
			{
				$fieldname = $translationArray[$param->getAttribute("name")]['field'];
				$tablename = $translationArray[$param->getAttribute("name")]['tbl'];
				$operator = $param->getAttribute("operator");
				$value = $param->getAttribute("value");
				$temparr = array('table'=>$tablename, 'field'=>$fieldname, 'operator'=>$operator, 'value'=>$value);
	
				// Add params to a parametersArray
				array_push($this->paramsArray,  $temparr);
			}
			else
			{
				trigger_error("104"."Unknown parameter ".$param->getAttribute("name")." specified.", E_USER_ERROR);
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
    	if($authTag!=NULL)
    	{
	        if($authTag->getAttribute("username")!=NULL)   $this->username   = $authTag->getAttribute("username");
	        if($authTag->getAttribute("password")!=NULL)   $this->password   = $authTag->getAttribute("password");
	        if($authTag->getAttribute("cnonce")!=NULL)     $this->cnonce     = $authTag->getAttribute("cnonce");
	        if($authTag->getAttribute("snonce")!=NULL)     $this->snonce     = $authTag->getAttribute("snonce");
	        if($authTag->getAttribute("hash")!=NULL)       $this->hash       = $authTag->getAttribute("hash");
	        if($authTag->getAttribute("seq")!=NULL)        $this->seq        = $authTag->getAttribute("seq");
    	}
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
    var $mergeWithID = NULL;

    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
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
        $this->mergeWithID = $mergeWithID;
            		
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
		   	case "description":			$this->setDescription($child->nodeValue); break;
		   	case "creator":				$this->setCreator($child->nodeValue); break;
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "owner":				$this->setOwner($child->nodeValue); break;
		   	case "file":				$this->addFile($child->nodeValue); break;
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "comments":			$this->setComments($child->nodeValue); break;
		   	case "createdTimestamp":	break;
		   	case "lastModifiedTimestamp": break;

		    case "type": 				
		   		if($child->hasAttribute("normalStd"))
		   		{
		   			if($child->getAttribute("normalStd")=="Corina")
		   			{
		   				$this->setType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
		   			}
		   			else
		   			{
		   				trigger_error("901"."Webservice only supports Corina vocabularies for element type", E_USER_ERROR);
		   				break;
		   			}
		   		}
				trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
		   	
		   	case "location": 
				$locationTags = $child->childNodes;
				foreach($locationTags as $tag)
				{	
		  	 		if($tag->nodeType != XML_ELEMENT_NODE) continue;  
		  	 		
		  	 		switch($tag->tagName)
		  	 		{
		  	 			case "locationGeometry": $this->location->setGeometryFromGML($this->xmlRequestDom->saveXML($tag)); break;
		  	 			case "locationComment" : $this->location->setComment($tag->nodeValue); break;
		  	 			case "locationPrecision" : $this->location->setPrecision($tag->nodeValue); break;
		  	 			case "locationType":		$this->location->setType(NULL, $tag->nodeValue); break;  	
		  	 			case "address":		
		  	 				$addressTags = $tag->childNodes;  
		  	 				foreach($addressTags as $addtag)
							{	
					  	 		if($addtag->nodeType != XML_ELEMENT_NODE) continue;  
					  	 		
					  	 		switch($addtag->tagName)
					  	 		{	
					  	 			case "addressLine1":		$this->location->setAddressLine1($addtag->nodeValue); break;	  	 				
					  	 			case "addressLine2":		$this->location->setAddressLine2($addtag->nodeValue); break;	
					  	 			case "cityOrTown":			$this->location->setCityOrTown($addtag->nodeValue); break;	
					  	 			case "stateProvinceRegion":	$this->location->setStateProvinceRegion($addtag->nodeValue); break;  	 				
					  	 			case "postalCode":			$this->location->setPostalCode($addtag->nodeValue); break;
					  	 			case "country":				$this->location->setCountry($addtag->nodeValue); break;
					  	 			default:
									trigger_error("901"."Unknown tag ".$addtag->tagName." in address section of the 'element' entity. This tag is being ignored", E_USER_NOTICE);		  	 			
					  	 		}
							}
					  	 	break;  		  	 			 
		  	 			default:
						trigger_error("901"."Unknown tag ".$tag->tagName." in location section of the 'object' entity. This tag is being ignored", E_USER_NOTICE);
		  	 				
		  	 		}	  	 		
				}
				break;
		   	
		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{	   			
		   			case "corina.objectLabCode" : $this->setCode($value); break;
		   			case "corina.countOfChildSeries" : break;
		   			case "corina.mapLink" :		break;
		   			
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
	var $mergeWithID = NULL;
	
    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
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
        $this->mergeWithID = $mergeWithID;
             		
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
	
	function setParamsFromXMLRequest()
	{
		global $corinaNS;
        global $tridasNS;
        global $taxonomicAuthorityEdition;
        global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
               
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	

		   switch ($child->tagName)
		   {
		   	case "title":				$this->setTitle($child->nodeValue); break;		   	
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;		   		
		   	case "createdTimestamp":	  break;
		   	case "lastModifiedTimestamp": break;	
		   	case "comments":			$this->setComments($child->nodeValue); break;			   	
		   	case "type": 				
		   		if($child->hasAttribute("normalStd"))
		   		{
		   			if($child->getAttribute("normalStd")=="Corina")
		   			{
		   				$this->setType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
		   			}
		   			else
		   			{
		   				trigger_error("901"."Webservice only supports Corina vocabularies for element type", E_USER_ERROR);
		   				break;
		   			}
		   		}
				trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
		   		
		   		
		   	case "description":			$this->setDescription($child->nodeValue); break;
		   	case "file": 				$this->addFile($child->nodeValue); break;
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
		   	case "shape": $this->setShape(null, $child->getAttribute("normalTridas")); break; 
		   	case "dimensions":
		   		
		   		$unitTag = $child->getElementsByTagName("unit")->item(0);
		   		$this->setDimensionUnits($unitTag->getAttribute("normalTridas"));
		   		//$dimensionTags = $unitTag->childNodes;
		   		$dimensionTags = $child->childNodes;
		   		
		   		foreach($dimensionTags as $dimension)
		   		{
		   			if($dimension->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($dimension->tagName)
		   			{
		   				case "units":
		   					//@todo implement
		   					break;
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
		   	case "authenticity":	 	$this->setAuthenticity($child->nodeValue); break;   		   	
		   	case "processing": 			$this->setProcessing($child->nodeValue); break;	   	
		   	case "marks": 				$this->setMarks($child->nodeValue); break;
		   	case "altitude":			$this->setAltitude($child->nodeValue); break;

		    case "location": 
				$locationTags = $child->childNodes;
				foreach($locationTags as $tag)
				{	
		  	 		if($tag->nodeType != XML_ELEMENT_NODE) continue;  
		  	 		
		  	 		switch($tag->tagName)
		  	 		{
		  	 			case "locationGeometry": $this->location->setGeometryFromGML($this->xmlRequestDom->saveXML($tag)); break;
		  	 			case "locationComment" : $this->location->setComment($tag->nodeValue); break;
		  	 			case "locationPrecision" : $this->location->setPrecision($tag->nodeValue); break;
		  	 			case "locationType":		$this->location->setType(NULL, $tag->nodeValue); break;  	 
		  	 			case "address":		
		  	 				$addressTags = $tag->childNodes;  
		  	 				foreach($addressTags as $addtag)
							{	
					  	 		if($addtag->nodeType != XML_ELEMENT_NODE) continue;  
					  	 		
					  	 		switch($addtag->tagName)
					  	 		{	
					  	 			case "addressLine1":		$this->location->setAddressLine1($addtag->nodeValue); break;	  	 				
					  	 			case "addressLine2":		$this->location->setAddressLine2($addtag->nodeValue); break;	
					  	 			case "cityOrTown":			$this->location->setCityOrTown($addtag->nodeValue); break;	
					  	 			case "stateProvinceRegion":	$this->location->setStateProvinceRegion($addtag->nodeValue); break;  	 				
					  	 			case "postalCode":			$this->location->setPostalCode($addtag->nodeValue); break;
					  	 			case "country":				$this->location->setCountry($addtag->nodeValue); break;
					  	 			default:
									trigger_error("901"."Unknown tag ".$addtag->tagName." in address section of the 'element' entity. This tag is being ignored", E_USER_NOTICE);		  	 			
					  	 		}
							}
					  	 	break;  
		  	 			default:
						trigger_error("901"."Unknown tag ".$tag->tagName." in location section of the 'element' entity. This tag is being ignored", E_USER_NOTICE);
		  	 				
		  	 		}	  	 		
				}
				break;
 	
		   	case "slope":
		   		$slopeTags = $child->childNodes;
		   		
		   		foreach($slopeTags as $slopeTag)
		   		{
		   			if($slopeTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($slopeTag->tagName)
		   			{
		   				case "angle":
		   					$this->setSlopeAngle($slopeTag->nodeValue);
		   					break;
		   				case "azimuth":
		   					$this->setSlopeAzimuth($slopeTag->nodeValue);
		   					break;
		   			}
		   		}
		   		break;
		   	case "soil":
		   		$soilTags = $child->childNodes;
		   		
		   		foreach($soilTags as $soilTag)
		   		{
		   			if($soilTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($soilTag->tagName)
		   			{
		   				case "depth":
		   					$this->setSoilDepth($soilTag->nodeValue);
		   					break;
		   				case "description":
		   					$this->setSoilDescription($soilTag->nodeValue);
		   					break;
		   			}
		   		}
		   		break;	
		   	case "bedrock":
		   		$bedrockTags = $child->childNodes;
		   		
		   		foreach($bedrockTags as $bedrockTag)
		   		{
		   			if($bedrockTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($bedrockTag->tagName)
		   			{
		   				case "description":
		   					$this->setBedrockDescription($bedrockTag->nodeValue);
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
		   			case "name":
		   				$this->setName($value);
		   				break;
		   			
		   			case "corina.boxID":	
		   				$this->setBoxID($value);
		   				break;
		   				
		   			// Ignore autogenerated fields
		   			case "corina.kingdom":	break;
		   			case "corina.phylum":	break;
		   			case "corina.class":	break;
		   			case "corina.order":	break;
		   			case "corina.family":	break;
		   			case "corina.genus":	break;
		   			case "corina.species":	break;		   			
		   			//default:
		   			//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'element' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   		}
		   		break;
	
		   	case "sample":
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
	var $mergeWithID = NULL;
	       
    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
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
        $this->mergeWithID = $mergeWithID;
                
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
		   	case "createdTimestamp":	  break;
		   	case "lastModifiedTimestamp": break;	
		   	case "comments":			$this->setComments($child->nodeValue); break;
		   	case "type": 				
		   		if($child->hasAttribute("normalStd"))
		   		{
		   			if($child->getAttribute("normalStd")=="Corina")
		   			{
		   				$this->setType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
		   			}
		   			else
		   			{
		   				trigger_error("901"."Webservice only supports Corina vocabularies for sample type", E_USER_ERROR);
		   				break;
		   			}
		   		}
				trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
		   	case "description":			$this->setDescription($child->nodeValue); break;	
		   	case "file":				$this->addFile($child->nodeValue); break;		   	
		   	case "samplingDate":		$this->setSamplingDate($child->nodeValue); break;
		   	case "position":			$this->setPosition($child->nodeValue); break;
		   	case "state":				$this->setState($child->nodeValue); break;
		   	case "knots":				$this->setKnots(dbHelper::formatBool($child->nodeValue)); break;
		   		

		   	case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{	  
		   			case "corina.boxID":
		   				$this->setBoxID($value);
		   				break; 			
		   			default:
		   			//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'sample' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
	
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
	var $mergeWithID = NULL;
	
    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
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
        $this->mergeWithID = $mergeWithID;
        
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
		global $corinaNS;
        global $tridasNS;
        global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
   			case "createdTimestamp": 	  break;
   			case "lastModifiedTimestamp": break;
		   	case "comments":			$this->setComments($child->nodeValue); break;			
		   	case "woodCompleteness":
		   		$woodCompletenessTags = $child->childNodes;
		   		foreach($woodCompletenessTags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($tag->tagName)
		   			{
		   				case "nrOfUnmeasuredInnerRings":
		   					$this->setNrOfUnmeasuredInnerRings($tag->nodeValue);
		   					$firebug->log($tag->nodeValue, "setting unmeasured inner rings");
		   					break;
		   					
		   				case "nrOfUnmeasuredOuterRings":
		   					$this->setNrOfUnmeasuredOuterRings($tag->nodeValue);
		   					break;		   					
		   				
		   				case "pith":
		   					$this->setPith(NULL, $tag->getAttribute("presence"));
		   					break;
		   				case "heartwood":	
					   		$this->setHeartwood(null, $tag->getAttribute("presence"));
					   		
					   		$heartwoodtags = $tag->childNodes;
					   		
					   		foreach($heartwoodtags as $heartwoodtag)
					   		{
					   			if($heartwoodtag->nodeType != XML_ELEMENT_NODE) continue;
					   			switch($heartwoodtag->tagName)
					   			{
					   				case "missingHeartwoodRingsToPith":				$this->setMissingHeartwoodRingsToPith($heartwoodtag->nodeValue); break;
					   				case "missingHeartwoodRingsToPithFoundation":	$this->setMissingHeartwoodRingsToPithFoundation($heartwoodtag->nodeValue); break;	
					   				default:
					   					trigger_error("901"."Unknown tag &lt;".$heartwoodtag->tagName."&gt; in 'radius.woodCompleteness.heartwood' entity of the XML request. Tag is being ignored", E_USER_NOTICE);	
					   			}
					   		}
					   		break;		   					
		   				case "sapwood":
		   					$this->setSapwood(NULL, $tag->getAttribute("presence"));
					   		$sapwoodtags = $tag->childNodes;
					   		foreach($sapwoodtags as $sapwoodtag)
					   		{
					   			if($sapwoodtag->nodeType != XML_ELEMENT_NODE) continue;
					   			
					   			switch ($sapwoodtag->tagName)
					   			{
					   				
					   				case "nrOfSapwoodRings": 						$this->setNumberOfSapwoodRings($sapwoodtag->nodeValue); break;
					   				case "lastRingUnderBark": 						
					   					if($sapwoodtag->nodeValue=="")
					   					{
					   						$this->setLastRingUnderBark("", $sapwoodtag->getAttribute("presence"));
					   					}
					   					else
					   					{
					   						$this->setLastRingUnderBark($sapwoodtag->nodeValue, $sapwoodtag->getAttribute("presence"));
					   					}
					   					break;
					   				case "missingSapwoodRingsToBark":				$this->setMissingSapwoodRingsToBark($sapwoodtag->nodeValue); break;
					   				case "missingSapwoodRingsToBarkFoundation": 	$this->setMissingSapwoodRingsToBarkFoundation($sapwoodtag->nodeValue); break;
					   				default:
					   					trigger_error("901"."Unknown tag &lt;".$sapwoodtag->tagName."&gt; in 'radius.woodCompleteness.sapwood' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
					   			}
					   		}		   			
							break;
		   				case "bark":
		   					$firebug->log($tag->getAttribute("presence"), "bark tag: ");
		   					$this->setBarkPresent($tag->getAttribute("presence"));
		   					break;					   		
				
		   				default:
		   					trigger_error("901"."Unknown tag &lt;".$tag->tagName."&gt; in 'radius.woodCompleteness' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   			}
		   		}		   			
				break;
		   	case "azimuth":				$this->setAzimuth($child->nodeValue); break;
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
	var $mergeWithID = NULL;
	
    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
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
        $this->mergeWithID = $mergeWithID;
        
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
		global $corinaNS;
        global $tridasNS;
        global $firebug;

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
   			case "createdTimestamp": 		break;
   			case "lastModifiedTimestamp": 	break;			   	
		   	case "comments":			$this->setComments($child->nodeValue); break;
   			case "measuringDate":		$this->setMeasuringDate($child->nodeValue); break;
   			case "derivationDate":		$this->setDerivationDate($child->nodeValue); break;
 		   	case "type":				$this->setVMeasurementOp(null, $child->nodeValue); break;	  				   	
   			case "linkSeries":
   				$seriesTags = $child->childNodes;
   				foreach($seriesTags as $series)
   				{
   					if($series->nodeType != XML_ELEMENT_NODE) continue;
   					if($series->nodeName=='series')
   					{
   						if($series->nodeType != XML_ELEMENT_NODE) continue;
   						$idTags = $series->childNodes;
   						foreach($idTags as $idTag)
		   				{
		   					if($idTag->nodeType != XML_ELEMENT_NODE) continue;
		   					if($idTag->nodeName=='identifier')
		   					{
		   						array_push($this->referencesArray, $idTag->nodeValue);  		
		   					}
		   					else
		   					{
		   						trigger_error("901"."Only identifier tags are currently supported in Corina.", E_USER_NOTICE);
		   					}
		   				}
   					}
   				}
   				break;	
		   	case "objective":			$this->setObjective($child->nodeValue); break;
   			case "standardizingMethod":	$this->setStandardizingMethod(null, $child->nodeValue); break;
		
		   	// Ignore user name fields as we use genericField ID's for these instead
		   	case "analyst":				break;
		   	case "dendrochronologist":	break;
		   	case "author":				break;
		   	case "measuringMethod":		$this->setMeasuringMethod(NULL, $child->getAttribute("normalTridas")); break;
		   	case "version":				$this->setVersion($child->nodeValue); break;
		   	case "interpretation":
		   		$interpTags = $child->childNodes;
		   		foreach($interpTags as $interpTag)
		   		{
		   			if($interpTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch($interpTag->nodeName)
		   			{

		   				case "datingReference":
		   					$datingRefTags = $interpTag->childNodes;
					   		foreach($datingRefTags as $datingRefTag)
		   					{	
		   						if($datingRefTag->nodeType != XML_ELEMENT_NODE) continue;		 
		   			   			switch($datingRefTag->nodeName)
		   						{		
		   							
						   			case "linkSeries":
						   				$seriesTags = $datingRefTag->childNodes;
						   				foreach($seriesTags as $series)
						   				{
						   					if($series->nodeType != XML_ELEMENT_NODE) continue;
					   					if($series->nodeName=='identifier')
						   					{						   
						   						$this->setMasterVMeasurementID($series->nodeValue);  		
						   					}
						   				}
						   				break;
		   						}
		   					}
		   					
			   			case "dating":
			   				$this->dating->setDatingType(null, $interpTag->getAttribute("type"));
			   				break;
		   					
			   			case "firstYear": 
			   				// Special case.  If the series is 'direct' then user may be redating in place
			   				// Kludgey and gross.
			   				if($this->getVMeasurementOp()=="Direct") $this->setFirstYear($interpTag->nodeValue);
			   				break;
			   			case "pithYear": break;
			   			case "deathYear":  break;
		   				default:
		   					trigger_error("901"."Unknown tag &lt;".$interpTag->tagName."&gt;. Tag is being ignored", E_USER_NOTICE);
		   					
		   					break;
		   			}
		   		}
		   		break;
			case "interpretationUnsolved":	break;
			case "location": break;
			
		    case "genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "corina.analystID":
		   				$this->analyst->setParamsFromDB($value);
		   				break;
		   			
		   			case "corina.dendrochronologistID":
		   				$this->dendrochronologist->setParamsFromDB($value);
		   				break;

		   			case "corina.authorID":
		   				$this->setAuthor($value);
		   				break;
		   				
		   			case "corina.justification":
		   				$this->setJustification($value);
		   				break;
		   			
		   			case "corina.crossdateConfidenceLevel":
		   				$this->setConfidenceLevel($value);
		   				break;
		   				
		   			case "corina.isReconciled":		   				
		   				$this->setIsReconciled($value);
		   				break;
		   				
		   			case "corina.newStartYear":
	   					$this->setNewStartYear($value);
	   					break;	
		   				
		   			case "corina.newEndYear":
		   				$this->setNewEndYear($value);	   					
		   			  				
		   			case "corina.directChildCount":		break;
		   			case "corina.mapLink":				break;
		   			case "corina.isPublished":			break;
		   			case "corina.readingCount":			break;		   			
		   				
		   		}
		   		break;
		   			   		
		   	case "values":
		   		
		   		
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
			   				if($valuechild->tagName == 'remark')
			   				{
			   					// Create a notes object to hold note
			   					$currReadingNote = new readingNote();
			   					
			   					if($valuechild->hasAttribute("normalTridas"))
			   					{
			   						// TRiDaS controlled remark
			   						$currReadingNote->setControlledVoc(null, "TRiDaS");
			   						$currReadingNote->setNote($valuechild->getAttribute("normalTridas"));	   						
			   					}
			   					else
			   					{
				   					// Corina or free text remark
				   					$currReadingNote->setID($valuechild->getAttribute("normalId"));
				   					$currReadingNote->setControlledVoc(null, $valuechild->getAttribute("normalStd"));
				   					
				   					if($valuechild->hasAttribute("normal"))
				   					{
				   						$currReadingNote->setNote($valuechild->getAttribute("normal"));
				   					}
				   					else
				   					{  		
				   						$currReadingNote->setNote($valuechild->nodeValue);			
				   						
				   					}
			   					}
			   					
			   					// Notes fields common to all types of notes
			   					$currReadingNote->setInheritedCount($valuechild->getAttribute("inheritedCount"));
			   					
			   					// Add notes in a notes array
			   					array_push($myNotesArray, $currReadingNote);
			   				}
			   				
			   			}

			   			// Mush all the reading and notes into the ReadingArray
						$this->readingsArray[$i] = array('value' => unit::unitsConverter($value, $this->getUnits(), "db-default"), 
	                                                     'wjinc' => NULL, 
	                                                     'wjdec' => NULL, 
	                                                     'count' => NULL,
	                                                     'notesArray' => $myNotesArray);
		   			}
		   			else if($tag->tagName=='variable')
		   			{		   				
		   				if($tag->getAttribute("normalTridas")!=NULL)
		   				{
		   					$this->setVariable(NULL, $tag->getAttribute("normalTridas"));
		   				}
		   				else
		   				{
		   					trigger_error("104"."Only TRiDaS normalised variables are supported by this webservice", E_USER_ERROR);
		   				}
		   			}
		   			else if($tag->tagName=='unit')
		   			{
		   				if($tag->getAttribute("normalTridas")!=NULL)
		   				{
		   					$this->setUnits(NULL, $tag->getAttribute("normalTridas"));
		   				}
		   				else
		   				{
		   					trigger_error("104"."Only TRiDaS normalised units are supported by this webservice", E_USER_ERROR);
		   				}		   				
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
    
class boxParameters extends boxEntity implements IParams
{
	var $xmlRequestDom = NULL;
	var $mergeWithID = NULL;
	
    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
    {
    	parent::__construct();    	
    	
    	$this->mergeWithID = $mergeWithID;
    	
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='box')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
     		            		
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
		   	case "title":				$this->setTitle($child->nodeValue); break;
		   	case "trackingLocation":	$this->setTrackingLocation($child->nodeValue); break;
		   	case "curationLocation":	$this->setCurationLocation($child->nodeValue); break;
		   	case "comments":			$this->setComments($child->nodeValue); break;
		   	case "createdTimestamp":	break;
		   	case "lastModifiedTimestamp": break;
		   	case "sampleCount"			: break;
		   	case "sample":				break;	   				   		
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'box' entity of the XML request", E_USER_NOTICE);
		   }
        }   
    }	
}
    
    
    
class securityUserParameters extends securityUserEntity implements IParams
{
	var $xmlRequestDom = NULL;
    
    function __construct($xmlrequest, $parentID=NULL)
    {
    	global $firebug;
    	parent::__construct();    	
    	
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='box')
        {
            $this->xmlRequestDom = $xmlrequest;
        }
        else
        {
    		$this->xmlRequestDom = new DomDocument();   		
    		$this->xmlRequestDom->loadXML($xmlrequest);
        }
     		            		
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
    	global $firebug;
		global $corinaNS;
        global $tridasNS;

        $children = $this->xmlRequestDom->documentElement->childNodes;
     	
        foreach($children as $child)
        {
			
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   $firebug->log($child->hasAttribute("id"), "has id?");
		   
		   if ($child->tagName=='securityUser')
		   {
		   		$firebug->log($child->getAttribute("isActive"), "child");
		   		if($child->hasAttribute("id")) $this->setID($child->getAttribute("id"), null);
		   		if($child->hasAttribute("username")) $this->setUsername($child->getAttribute("username"));
		   		if($child->hasAttribute("firstName")) $this->setFirstname($child->getAttribute("firstName"));
		   		if($child->hasAttribute("lastName")) $this->setLastname($child->getAttribute("lastName"));
		   		if($child->hasAttribute("isActive")) $this->setIsActive(dbhelper::formatBool($child->getAttribute("isActive"),"php"));
		   		if($child->hasAttribute("hashOfPassword")) $this->setPassword($child->getAttribute("hashOfPassword"), "hash");
		  		 $members = $child->childNodes;

			   // Set membership of groups but only if user is an admin
			 	global $myAuth;
			 	if($myAuth->isAdmin()==TRUE)
			 	{
			 		$firebug->log("I have admin rights");
				   foreach($members as $member)
				   {
				   		if($member->nodeType != XML_ELEMENT_NODE) continue;
				   		if($member->tagName=='memberOf')
				   		{
				   			$groups = $member->childNodes;
				   			foreach($groups as $group)
				   			{
				   				if($group->nodeType != XML_ELEMENT_NODE) continue;
				   				if($group->tagName=='securityGroup')
				   				{		
				   					if($group->hasAttribute("id"))
				   					{
				   						$this->groupArray[]= $group->getAttribute("id");
				   					} 
				   				}
				   			}
				   		}
				   }
		 	}
		   
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
