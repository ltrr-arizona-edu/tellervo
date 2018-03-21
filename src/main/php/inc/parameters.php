<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This file contains the interface and classes that contain the 
 * parameters requested by the user.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
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
    	global $tellervoNS;
        global $tridasNS;
        global $firebug;


        
        // Get main attributes
    	$searchParamsTag = $this->xmlRequestDom->getElementsByTagName("searchParams")->item(0); 	
		$this->returnObject = $searchParamsTag->getAttribute("returnObject");
		$this->limit = (int) $searchParamsTag->getAttribute("limit");
		$this->skip = (int) $searchParamsTag->getAttribute("skip");
		$this->includeChildren = (bool) $searchParamsTag->getAttribute("includeChildren");
		
		// Get individual params
		
		$allTags = $this->xmlRequestDom->getElementsByTagName("all");
		
		foreach ($allTags as $param)
		{
			if($param->nodeType != XML_ELEMENT_NODE) continue; 
			
			$firebug->log("Found 'all' records tag");

			$this->allData=TRUE; 
			break;
		}
		
		
		$paramsTags = $this->xmlRequestDom->getElementsByTagName("param");	
		
				// Create an array for translating the search parameters names into Tellervo database table and field names
		$translationArray = array (	
									'projectid' =>							array('tbl' => 'vwtblproject', 'field' => 'projectid'),
									'projecttitle' =>						array('tbl' => 'vwtblproject', 'field' => 'title'),
									'projectcreated' =>						array('tbl' => 'vwtblproject', 'field' => 'createdtimestamp'),
									'projectmodified' =>					array('tbl' => 'vwtblproject', 'field' => 'lastmodifiedtimestamp'),
				
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
									'objectlatitude' => 				array('tbl' => 'vwtblobject', 'field' => 'latitude'),
									'objectlongitude' => 				array('tbl' => 'vwtblobject', 'field' => 'longitude'),
									'objecttype' =>							array('tbl' => 'vwtblobject', 'field' => 'type'),	
									'parentobjectid' =>						array('tbl' => 'vwtblobject', 'field' => 'parentobjectid'),
									'objectcode' =>							array('tbl' => 'vwtblobject', 'field' => 'code'),
									'objectlocation' =>						array('tbl' => 'vwtblobject', 'field' => 'locationgeometry'),
									'countOfChildSeriesOfObject' =>			array('tbl' => 'vwtblobject', 'field' => 'countofchildvmeasurements'),
									'anyparentobjectid' =>					array('tbl' => 'vwtblobject', 'field' => 'anyparentobjectid'),
									'anyparentobjectcode' =>				array('tbl' => 'vwtblobject', 'field' => 'anyparentobjectcode'),
									'topobjectid' =>					array('tbl' => 'vwtblobject', 'field' => 'topobjectid'),
									'topobjectcode' =>				array('tbl' => 'vwtblobject', 'field' => 'topobjectcode'),
									'subobjectid' =>					array('tbl' => 'vwtblobject', 'field' => 'subobjectid'),
									'subobjectcode' =>				array('tbl' => 'vwtblobject', 'field' => 'subobjectcode'),

		
									'boxid' => 								array('tbl' => 'vwtblbox', 'field' => 'boxid'),
		
		
									'elementid' => 							array ('tbl' => 'vwtblelement', 	'field'  => 'elementid'),
									//'elementdbid' => 						array('tbl' => 'vwtblelement', 'field' => 'elementid'),
									'elementoriginaltaxonname' => 			array('tbl' => 'vwtblelement', 'field' => 'taxonlabel'),
									//'elementphylumname' => array('tbl' => 'vwtblelement', 'field' => ''),
									//'elementclassname' => array('tbl' => 'vwtblelement', 'field' => ''),
									'elementordername' => array('tbl' => 'vwtblelement', 'field' => 'taxonorder'),
									'elementfamilyname' => array('tbl' => 'vwtblelement', 'field' => 'taxonfamily'),
									'elementgenusname' => array('tbl' => 'vwtblelement', 'field' => 'taxongenus'),
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
									'elementlatitude' => 				array('tbl' => 'vwtblelement', 'field' => 'latitude'),
									'elementlongitude' => 				array('tbl' => 'vwtblelement', 'field' => 'longitude'),
									
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
									'samplestatus' =>							array('tbl' => 'vwtblsample', 'field' => 'samplestatus'),
		

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
									'seriesanalyst' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'measuredby'),
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
									'seriesvaluecount' =>					array('tbl' => 'vwcomprehensivevm', 'field' => 'readingcount'),
									'seriescount' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'directchildcount'),
									'seriescode' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'code'),
									'dependentseriesid' =>						array('tbl' => 'vwcomprehensivevm', 'field' => 'dependentseriesid'),
		
									
									'loanid' =>						array('tbl' => 'vwtblloan', 'field' => 'loanid'),
									'loanduedate' =>						array('tbl' => 'vwtblloan', 'field' => 'duedate'),
									'loanissuedate' =>						array('tbl' => 'vwtblloan', 'field' => 'issuedate'),
									'loanreturndate' =>						array('tbl' => 'vwtblloan', 'field' => 'returndate'),
									'loanfirstname' =>						array('tbl' => 'vwtblloan', 'field' => 'firstname'),
									'loanlastname' =>						array('tbl' => 'vwtblloan', 'field' => 'lastname'),
									'loanorganisation' =>						array('tbl' => 'vwtblloan', 'field' => 'organisation'),
									'loannotes' =>						array('tbl' => 'vwtblloan', 'field' => 'notes'),
									
									'tagtext' =>						array('tbl' => 'tbltag', 'field' => 'tag'),
									'tagid' =>						array('tbl' => 'tbltag', 'field' => 'tagid')
								  );
		
		// Loop through each param tag
		foreach ($paramsTags as $param)
		{


	
			//$firebug->log($param->getAttribute("name"), "param name");
			
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

class statisticsParameters implements IParams
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


class loanParameters extends loanEntity implements IParams
{
 	var $xmlRequestDom = NULL;
	var $mergeWithID = NULL;
	
    function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
    {
    	global $firebug;
    	
    	
    	$firebug->log($xmlrequest, "XML request received by loanParameters");
    	
    	parent::__construct("loan");    	

    	$this->xmlRequestDom = new DomDocument();
    	$this->xmlRequestDom->loadXML($xmlrequest);
        
     		            		
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
		global $tellervoNS;
        global $tridasNS;
		global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        $this->entityIdArray = array();
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
        	$firebug->log($child->tagName, "XML tag name");
        	$firebug->log($child->nodeValue, "XML tag value");
        	
		   switch ($child->tagName)
		   {
		   		case "tridas:identifier": 	$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   		case "firstname":			$this->setFirstName($child->nodeValue); break;
		   		case "lastname":			$this->setLastName($child->nodeValue); break;
		   		case "organisation":		$this->setOrganisation($child->nodeValue); break;
		   		case "notes":				$this->setNotes($child->nodeValue); break;
		   		case "duedate":				$this->setDueDate($child->nodeValue); break;
		   		case "returndate":			$this->setReturnDate($child->nodeValue); break;
		   		case "issuedate":			break;
		   		case "tridas:sample":	
		   				$firebug->log("Found a sample in a loan... looping through sample tags...");			   		
		   				$sampleTags = $child->childNodes;
		   				foreach($sampleTags as $tag)
		   				{
		   					if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   					switch ($tag->tagName)
		   					{
		   						case "tridas:identifier" : 
		   							$firebug->log($tag->nodeValue, "Found sample id");
		   							array_push($this->entityIdArray, $tag->nodeValue );
		   					}
		   				}
		   				break;
		   		case "tridas:file" :		
		   				if($child->hasAttribute("xlink:href"))
						{
							$this->addFile($child->getAttribute("xlink:href"));
						}
						break;
		   		
		   		default : 		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'loan' entity of the XML request", E_USER_NOTICE);
		   			return;
		   }
        }
        
    }
	
}


class curationParameters extends curationEntity implements IParams
{
	var $xmlRequestDom = NULL;
	var $mergeWithID = NULL;

	function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
	{
		global $firebug;
		$firebug->log($xmlrequest, "XML request received by curationParameters");
		 
		parent::__construct();

		$this->xmlRequestDom = new DomDocument();
		$this->xmlRequestDom->loadXML($xmlrequest);

		// Extract parameters from the XML request
		$this->setParamsFromXMLRequest();
	}

	function setParamsFromXMLRequest()
	{
		global $tellervoNS;
		global $tridasNS;
		global $firebug;

		$children = $this->xmlRequestDom->documentElement->childNodes;

		foreach($children as $child)
		{
			if($child->nodeType != XML_ELEMENT_NODE) continue;
			 
			$firebug->log($child->tagName, "XML tag name");
			$firebug->log($child->nodeValue, "XML tag value");
			 
			switch ($child->tagName)
			{
				case "tridas:identifier": 		$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
				case "status":				$this->setCurationStatus(NULL, $child->nodeValue); break;
				case "tridas:securityUser":		break;
				case "curationtimestamp":	break;
				case "notes":				$this->setNotes($child->nodeValue); break;
				case "tridas:sample":
					$firebug->log("Found a sample in a loan... looping through sample tags...");
					$sampleTags = $child->childNodes;
					foreach($sampleTags as $tag)
					{
						if($tag->nodeType != XML_ELEMENT_NODE) continue;
						switch ($tag->tagName)
						{
							case "tridas:identifier" :
								$firebug->log($tag->nodeValue, "Found sample id");
								array_push($this->entityIdArray, $tag->nodeValue );
						}
					}
					break;
			}
		}

	}

}


class permissionParameters extends permissionEntity implements IParams
{
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
    	global $tellervoNS;
        global $tridasNS;
	global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   $firebug->log($child->tagName, "permissions tag name");
		   switch ($child->tagName)
		   {
		   	case "permissionToCreate": 	$this->setCanCreate($child->nodeValue); break;
		   	case "permissionToRead": 	$this->setCanRead($child->nodeValue); break;
		   	case "permissionToUpdate": 	$this->setCanUpdate($child->nodeValue); break;
		   	case "permissionToDelete": 	$this->setCanDelete($child->nodeValue); break;
		   	case "permissionDenied": 	$this->setPermDenied($child->nodeValue); break;
		   	
		   	case "entity":
		   		$entity = array("type" => $child->getAttribute("type"), "id" => $child->getAttribute("id"));
		   		array_push($this->entityArray, $entity);		   		
		   		break;
		   		
		   	case "securityGroup":
		   		array_push($this->securityGroupArray, $child->getAttribute("id"));	
		   		break;
		   		
		   	case "securityUser":
		   		array_push($this->securityUserArray, $child->getAttribute("id"));	
		   		break;
		   		
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'permissions' entity of the XML request", E_USER_NOTICE);
		   		return;
		   		
		   }
        }
    }
}

class odkFormDefinitionParameters extends odkFormDefinitionEntity implements IParams
{
 
	var $xmlRequestDom = NULL;
	var $mergeWithID = NULL;

	function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
	{
		global $firebug;
		//$firebug->log($xmlrequest, "XML request received by odkFormDefinitionParameters");
		 
		parent::__construct();

		$this->xmlRequestDom = new DomDocument();
		$this->xmlRequestDom->loadXML($xmlrequest);

		// Extract parameters from the XML request
		$this->setParamsFromXMLRequest();
	}

	function innerXML($node) 
	{ 
		$doc = $node->ownerDocument; 
		$frag = $doc->createDocumentFragment(); 
		foreach ($node->childNodes as $child) 
		{ 
			$frag->appendChild($child->cloneNode(TRUE)); 
		} 
		return $doc->saveXML($frag); 
	}

	function setParamsFromXMLRequest()
	{
		
		global $tellervoNS;
		global $tridasNS;
		global $firebug;

		
		
		$this->setName($this->xmlRequestDom->documentElement->getAttribute("name"));
		$this->setOwnerID($this->xmlRequestDom->documentElement->getAttribute("ownerid"));
		$this->setID($this->xmlRequestDom->documentElement->getAttribute("id"));
		$this->setIsPublic($this->xmlRequestDom->documentElement->getAttribute("ispublic"));
		$this->setDefinition($this->innerXML($this->xmlRequestDom->documentElement));



	}

}

class tagParameters extends tagEntity implements IParams
{
 
	var $xmlRequestDom = NULL;
	var $mergeWithID = NULL;

	function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
	{
		global $firebug;
		$firebug->log($xmlrequest, "XML request received by tagParameters");
		 
		parent::__construct();

		$this->xmlRequestDom = new DomDocument();
		$this->xmlRequestDom->loadXML($xmlrequest);

		// Extract parameters from the XML request
		$this->setParamsFromXMLRequest();
	}

	function setParamsFromXMLRequest()
	{
		
		global $tellervoNS;
		global $tridasNS;
		global $firebug;

		
		
		$this->setTagText($this->xmlRequestDom->documentElement->getAttribute("value"));
		$this->setOwnerID($this->xmlRequestDom->documentElement->getAttribute("ownerid"));
		$this->setID($this->xmlRequestDom->documentElement->getAttribute("id"));
		

		 $children = $this->xmlRequestDom->documentElement->childNodes;
	
		foreach($children as $child)
		{
			if($child->nodeType != XML_ELEMENT_NODE) continue;
		 
			switch ($child->tagName)
			{
			  case "assignedTo":
			  $entitynodes = $child->childNodes;
			    foreach($entitynodes as $entity)
			    {
			        if($entity->nodeType != XML_ELEMENT_NODE) continue;
			    	array_push($this->entityIdArray, $entity->getAttribute("id"));  		
			    }
			    break;
			    
			}
		}

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

class projectParameters extends projectEntity implements IParams
{
	var $xmlRequestDom = NULL;
	var $hasChild   = FALSE;
	var $parentID   = NULL;
	var $mergeWithID = NULL;

	function __construct($xmlrequest, $parentID=NULL, $mergeWithID=NULL)
	{
		parent::__construct();

		// Load the xmlrequest into a local DOM variable
		if (gettype($xmlrequest)=='project')
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
		global $tellervoNS;
		global $tridasNS;
		global $firebug;


		$children = $this->xmlRequestDom->documentElement->childNodes;

		foreach($children as $child)
		{
			if($child->nodeType != XML_ELEMENT_NODE) continue;


			switch ($child->tagName)
			{
				case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
				case "tridas:description":			$this->setDescription($child->nodeValue); break;
				case "tridas:title":				$this->setTitle($child->nodeValue); break;
				case "tridas:title":				$this->setTitle($child->nodeValue); break;
				case "tridas:comments":			$this->setComments($child->nodeValue); break;
				case "tridas:commissioner":		$this->setCommissioner($child->nodeValue); break;
				case "tridas:period": 			$this->setPeriod($child->nodeValue); break;
				case "tridas:investigator":		$this->setInvestigator($child->nodeValue); break;
				
				
				case "tridas:createdTimestamp":	break;
				case "tridas:lastModifiedTimestamp": break;

				case "tridas:requestDate": 			$this->setRequestDate($child->nodeValue); break;;
				case "tridas:reference": 			break;
				case "tridas:research": 			break;
				case "tridas:laboratory": 			break;
				
				
				case "tridas:file":
					if($child->hasAttribute("xlink:href"))
					{
						$this->addFile($child->getAttribute("xlink:href"));
					}
					else
					{
						trigger_error("901"."Error getting href", E_USER_ERROR);
					}
					break;

				case "tridas:category":
					if($child->hasAttribute("normalStd"))
					{
						if($child->getAttribute("normalStd")=="Tellervo")
						{
							$this->setCategory($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
						}
						else
						{
							trigger_error("901"."Webservice only supports Tellervo vocabularies for project category", E_USER_ERROR);
							break;
						}
					}
					trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
					
				case "tridas:type":
					if($child->hasAttribute("normalStd"))
					{
						if($child->getAttribute("normalStd")=="Tellervo")
						{
							$this->addType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
						}
						else
						{
							trigger_error("901"."Webservice only supports Tellervo vocabularies for project type", E_USER_ERROR);
							break;
						}
					}
					trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;


				case "tridas:genericField":
					$type = $child->getAttribute("type");
					$name = $child->getAttribute("name");
					$value = $child->nodeValue;
					switch($name)
					{
						default:
							if(substr($name, 0, strlen("userDefinedField"))==="userDefinedField")
							{
								$fieldname = substr($name, strlen("userDefinedField."));
								try{
									$this->setUserDefinedFieldByName($value, $fieldname);
									break;
								} catch (Exception $e)
								{
									 
									trigger_error("901".$e->getMessage(), E_USER_NOTICE);
								}
							}
							else
							{
								//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'object' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
							}
							 
					}
					break;
										 
				default:
					trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'project' entity of the XML request", E_USER_NOTICE);
			}
		}
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
     		
	
	$this->setParentObjectID( $parentID);
        $this->parentID=$parentID;
        $this->mergeWithID = $mergeWithID;
            		
        // Extract parameters from the XML request
        $this->setParamsFromXMLRequest();
    }
    
    function setParamsFromXMLRequest()
    {
	global $tellervoNS;
        global $tridasNS;
	global $firebug;
	

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        		

		   switch ($child->tagName)
		   {
		   	case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "tridas:description":			$this->setDescription($child->nodeValue); break;
		   	case "tridas:creator":				$this->setCreator($child->nodeValue); break;
		   	case "tridas:title":				$this->setTitle($child->nodeValue); break;
		   	case "tridas:owner":				$this->setOwner($child->nodeValue); break;
		   	case "tridas:title":				$this->setTitle($child->nodeValue); break;
		   	case "tridas:comments":			$this->setComments($child->nodeValue); break;
		   	case "tridas:createdTimestamp":	break;
		   	case "tridas:lastModifiedTimestamp": break;

		        case "tridas:file":				
				if($child->hasAttribute("xlink:href"))
				{
					$this->addFile($child->getAttribute("xlink:href"));
				}
				else
				{
					trigger_error("901"."Error getting href", E_USER_ERROR);
				}
				break;

    		    	case "tridas:type": 				
		   		if($child->hasAttribute("normalStd"))
		   		{
		   			if($child->getAttribute("normalStd")=="Tellervo")
		   			{
		   				$this->setType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
		   			}
		   			else
		   			{
		   				trigger_error("901"."Webservice only supports Tellervo vocabularies for element type", E_USER_ERROR);
		   				break;
		   			}
		   		}
				trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
		   	
		   	case "tridas:location": 
				$locationTags = $child->childNodes;
				foreach($locationTags as $tag)
				{	
		  	 		if($tag->nodeType != XML_ELEMENT_NODE) continue;  
		  	 		
		  	 		switch($tag->tagName)
		  	 		{
		  	 			case "tridas:locationGeometry": $this->location->setGeometryFromGML($this->xmlRequestDom->saveXML($tag)); break;
		  	 			case "tridas:locationComment" : $this->location->setComment($tag->nodeValue); $firebug->log($tag->nodeValue, "Location comment"); break;
		  	 			case "tridas:locationPrecision" : $this->location->setPrecision($tag->nodeValue); break;
		  	 			case "tridas:locationType":		$this->location->setType($tag->nodeValue); break;  	
		  	 			case "tridas:address":		
		  	 				$addressTags = $tag->childNodes;  
		  	 				foreach($addressTags as $addtag)
							{	
					  	 		if($addtag->nodeType != XML_ELEMENT_NODE) continue;  
					  	 		
					  	 		switch($addtag->tagName)
					  	 		{	
					  	 			case "tridas:addressLine1":		$this->location->setAddressLine1($addtag->nodeValue); break;	  	 				
					  	 			case "tridas:addressLine2":		$this->location->setAddressLine2($addtag->nodeValue); break;	
					  	 			case "tridas:cityOrTown":			$this->location->setCityOrTown($addtag->nodeValue); break;	
					  	 			case "tridas:stateProvinceRegion":	$this->location->setStateProvinceRegion($addtag->nodeValue); break;  	 				
					  	 			case "tridas:postalCode":			$this->location->setPostalCode($addtag->nodeValue); break;
					  	 			case "tridas:country":				$this->location->setCountry($addtag->nodeValue); break;
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
		   	
		   	case "tridas:genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{	   			
		   			case "tellervo.objectLabCode" : $this->setCode($value); break;
		   			case "tellervo.vegetationType" : $this->setVegetationType($value); break;
		   			case "tellervo.countOfChildSeries" : break;
		   			case "tellervo.mapLink" :		break;
		   			case "tellervo.object.projectid" : $this->setProjectID($value); break;
		   			
		   			default:
		   				if(substr($name, 0, strlen("userDefinedField"))==="userDefinedField")
		   				{
		   					$fieldname = substr($name, strlen("userDefinedField."));
		   					try{
		   						$this->setUserDefinedFieldByName($value, $fieldname);
		   						break;
		   					} catch (Exception $e)
		   					{
		   				
		   						trigger_error("901".$e->getMessage(), E_USER_NOTICE);
		   					}
		   				}
		   				else
		   				{
		   					//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'object' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   				}
		   				
		   		}
		   		break;
			
		   	case "tridas:coverage":			
		   		$coverageTags = $child->childNodes;
		   		foreach($covereageTags as $tag)
		   		{
		   			switch($tag->tagName)
		   			{
		   				case "tridas:coverageTemporal":			$coverageTemporal = $tag->nodeValue; break;
		   				case "tridas:coverageTemporalFoundation": 	$coverageTemporalFoundation = $tag->nodeValue; break;

		   			}
		   			
		   			if ( (isset($coverageTemporal)) && (isset($coverageTemporalFoundation)) ) $this->setCoverageTemporal($coverageTemporal, $coverageTemporalFoundation);
		
		   		}
		   		break;
		   	
		   	
		   		
		   	case "tridas:object":
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
		global $tellervoNS;
        global $tridasNS;
        global $taxonomicAuthorityEdition;
        global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
               
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	

		   switch ($child->tagName)
		   {
		   	case "tridas:title":				$this->setTitle($child->nodeValue); break;		   	
		   	case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;		   		
		   	case "tridas:createdTimestamp":	  break;
		   	case "tridas:lastModifiedTimestamp": break;	
		   	case "tridas:comments":			$this->setComments($child->nodeValue); break;			   	
		   	case "tridas:type": 				
		   		if($child->hasAttribute("normalStd"))
		   		{
		   			if($child->getAttribute("normalStd")=="Tellervo")
		   			{
		   				$this->setType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
		   			}
		   			else
		   			{
		   				trigger_error("901"."Webservice only supports Tellervo vocabularies for element type", E_USER_ERROR);
		   				break;
		   			}
		   		}
				trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
		   		
		   		
		   	case "tridas:description":			$this->setDescription($child->nodeValue); break;
	        case "tridas:file":				
			if($child->hasAttribute("xlink:href"))
			{
				$this->addFile($child->getAttribute("xlink:href"));
			}
			break;
		   	case "tridas:taxon":
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
					trigger_error("901"."The Tellervo web service only supports taxonomic data that conforms to the '$taxonomicAuthorityEdition'.  Please normalise your data and try again.", E_USER_ERROR);
		   		}
		   		break; 
		   	case "tridas:shape": $this->setShape(null, $child->getAttribute("normalTridas")); break; 
		   	case "tridas:dimensions":
		   		
		   		$unitTag = $child->getElementsByTagName("unit")->item(0);
		   		$this->setDimensionUnits($unitTag->getAttribute("normalID"), $unitTag->getAttribute("normalTridas"));
		   		//$dimensionTags = $unitTag->childNodes;
		   		$dimensionTags = $child->childNodes;
		   		
		   		foreach($dimensionTags as $dimension)
		   		{
		   			if($dimension->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($dimension->tagName)
		   			{
		   				case "tridas:units":
		   					//@todo implement
		   					break;
		   				case "tridas:diameter":
		   					$this->setDiameter($dimension->nodeValue);
		   					break;
		   				case "tridas:height":
		   					$this->setHeight($dimension->nodeValue);
		   					break;
		   				case "tridas:width":
		   					
		   					$this->setWidth($dimension->nodeValue);
		   					break;
		   				case "tridas:depth":
		   					$this->setDepth($dimension->nodeValue);
		   					break;
		   			}
		   		}
		   		break;
		   	case "tridas:authenticity":	 	$this->setAuthenticity($child->nodeValue); break;   		   	
		   	case "tridas:processing": 			$this->setProcessing($child->nodeValue); break;	   	
		   	case "tridas:marks": 				$this->setMarks($child->nodeValue); break;
		   	case "tridas:altitude":			$this->setAltitude($child->nodeValue); break;
		   	case "tridas:location": 
				$locationTags = $child->childNodes;
				foreach($locationTags as $tag)
				{	
		  	 		if($tag->nodeType != XML_ELEMENT_NODE) continue;  
		  	 		
		  	 		switch($tag->tagName)
		  	 		{
		  	 			case "tridas:locationGeometry": $this->location->setGeometryFromGML($this->xmlRequestDom->saveXML($tag)); break;
		  	 			case "tridas:locationComment" : $this->location->setComment($tag->nodeValue); $firebug->log($tag->nodeValue, "Location comment"); break;
		  	 			case "tridas:locationPrecision" : $this->location->setPrecision($tag->nodeValue); break;
		  	 			case "tridas:locationType":		$this->location->setType($tag->nodeValue); break;  	
		  	 			case "tridas:address":		
		  	 				$addressTags = $tag->childNodes;  
		  	 				foreach($addressTags as $addtag)
							{	
					  	 		if($addtag->nodeType != XML_ELEMENT_NODE) continue;  
					  	 		
					  	 		switch($addtag->tagName)
					  	 		{	
					  	 			case "tridas:addressLine1":		$this->location->setAddressLine1($addtag->nodeValue); break;	  	 				
					  	 			case "tridas:addressLine2":		$this->location->setAddressLine2($addtag->nodeValue); break;	
					  	 			case "tridas:cityOrTown":			$this->location->setCityOrTown($addtag->nodeValue); break;	
					  	 			case "tridas:stateProvinceRegion":	$this->location->setStateProvinceRegion($addtag->nodeValue); break;  	 				
					  	 			case "tridas:postalCode":			$this->location->setPostalCode($addtag->nodeValue); break;
					  	 			case "tridas:country":				$this->location->setCountry($addtag->nodeValue); break;
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
		   		
		   	case "tridas:slope":
		   		$slopeTags = $child->childNodes;
		   		
		   		foreach($slopeTags as $slopeTag)
		   		{
		   			if($slopeTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($slopeTag->tagName)
		   			{
		   				case "tridas:angle":
		   					$this->setSlopeAngle($slopeTag->nodeValue);
		   					break;
		   				case "tridas:azimuth":
		   					$this->setSlopeAzimuth($slopeTag->nodeValue);
		   					break;
		   			}
		   		}
		   		break;
		   	case "tridas:soil":
		   		$soilTags = $child->childNodes;
		   		
		   		foreach($soilTags as $soilTag)
		   		{
		   			if($soilTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($soilTag->tagName)
		   			{
		   				case "tridas:depth":
		   					$this->setSoilDepth($soilTag->nodeValue);
		   					break;
		   				case "tridas:description":
		   					$this->setSoilDescription($soilTag->nodeValue);
		   					break;
		   			}
		   		}
		   		break;	
		   	case "tridas:bedrock":
		   		$bedrockTags = $child->childNodes;
		   		
		   		foreach($bedrockTags as $bedrockTag)
		   		{
		   			if($bedrockTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($bedrockTag->tagName)
		   			{
		   				case "tridas:description":
		   					$this->setBedrockDescription($bedrockTag->nodeValue);
		   					break;
		   			}
		   		}
		   		break;		   		   			   			   		

	   	
		   	
		   	case "tridas:genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "name":
		   				$this->setName($value);
		   				break;
		   			
		   			case "tellervo.boxID":	
		   				$this->setBoxID($value);
		   				break;
		   				
		   			// Ignore autogenerated fields
		   			case "tellervo.kingdom":	break;
		   			case "tellervo.phylum":	break;
		   			case "tellervo.class":	break;
		   			case "tellervo.order":	break;
		   			case "tellervo.family":	break;
		   			case "tellervo.genus":	break;
		   			case "tellervo.species":	break;		
		   			default:
		   				 
		   				if(substr($name, 0, strlen("userDefinedField"))==="userDefinedField")
		   				{
		   					$fieldname = substr($name, strlen("userDefinedField."));
		   					try{
		   						$this->setUserDefinedFieldByName($value, $fieldname);
		   						break;
		   					} catch (Exception $e)
		   					{
		   							
		   						trigger_error("901".$e->getMessage(), E_USER_NOTICE);
		   					}
		   				}
		   				else
		   				{
		   					//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'sample' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   				}
		   			
		   			
		   			//default:
		   			//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'element' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   		}
		   		break;
	
		   	case "tridas:sample":
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
		global $tellervoNS;
        global $tridasNS;
        global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "tridas:title":				$this->setTitle($child->nodeValue); break;
		   	case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "tridas:createdTimestamp":	  break;
		   	case "tridas:lastModifiedTimestamp": break;	
		   	case "tridas:comments":			$this->setComments($child->nodeValue); break;
		   	case "tridas:type": 				
		   		if($child->hasAttribute("normalStd"))
		   		{
		   			if($child->getAttribute("normalStd")=="Tellervo")
		   			{
		   				$this->setType($child->getAttribute("normalId"), $child->getAttribute("normal")); break;
		   			}
		   			else
		   			{
		   				trigger_error("901"."Webservice only supports Tellervo vocabularies for sample type", E_USER_ERROR);
		   				break;
		   			}
		   		}
				trigger_error("902"."The requested element type is unsupported", E_USER_ERROR); break;
		   	case "tridas:description":			$this->setDescription($child->nodeValue); break;	
	        case "tridas:file":				
			if($child->hasAttribute("xlink:href"))
			{
				$this->addFile($child->getAttribute("xlink:href"));
			}
			break;	   	
		   	case "tridas:samplingDate":		$this->setSamplingDate($child->nodeValue); break;
		   	case "tridas:position":			$this->setPosition($child->nodeValue); break;
		   	case "tridas:state":				$this->setState($child->nodeValue); break;
		   	case "tridas:knots":				$this->setKnots(dbHelper::formatBool($child->nodeValue)); break;
		   		

		   	case "tridas:genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   	
		   		
		   		$firebug->log($name, "GenericFieldName");
		   		$firebug->log($value, "GenericFieldValue");	
		   		switch($name)
		   		{	  
		   			case "tellervo.boxID":
		   				$firebug->log($value, "Setting boxid");
		   				$this->setBoxID($value);
		   				break; 			
		   			case "tellervo.externalID":
		   				$this->setExternalID($value);
		   				break; 			
		   			case "tellervo.sampleStatus":
		   				$this->setSampleStatus(null, $value);
		   				break; 		
		   			case "tellervo.samplingDatePrecision":
		   				$this->setSamplingDatePrecision($value);
		   				break;		   				
		   			default:
		   				
		   				if(substr($name, 0, strlen("userDefinedField"))==="userDefinedField")
		   				{
		   					$fieldname = substr($name, strlen("userDefinedField."));
		   					try{
		   						$this->setUserDefinedFieldByName($value, $fieldname);
		   						break;
		   					} catch (Exception $e)
		   					{
		   						
		   						trigger_error("901".$e->getMessage(), E_USER_NOTICE);
		   					}
		   				}
		   				else
		   				{
		   					//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'sample' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   				}
	
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
		global $tellervoNS;
        global $tridasNS;
        global $firebug;

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "tridas:title":				$this->setTitle($child->nodeValue); break;
		   	case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
   			case "tridas:createdTimestamp": 	  break;
   			case "tridas:lastModifiedTimestamp": break;
		   	case "tridas:comments":			$this->setComments($child->nodeValue); break;			
		   	case "tridas:woodCompleteness":
		   		$woodCompletenessTags = $child->childNodes;
		   		foreach($woodCompletenessTags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch ($tag->tagName)
		   			{
		   				case "tridas:nrOfUnmeasuredInnerRings":
		   					$this->setNrOfUnmeasuredInnerRings($tag->nodeValue);
		   					$firebug->log($tag->nodeValue, "setting unmeasured inner rings");
		   					break;
		   					
		   				case "tridas:nrOfUnmeasuredOuterRings":
		   					$this->setNrOfUnmeasuredOuterRings($tag->nodeValue);
		   					break;		   					
		   				
		   				case "tridas:pith":
		   					$this->setPith(NULL, $tag->getAttribute("presence"));
		   					break;
		   				case "tridas:heartwood":	
					   		$this->setHeartwood(null, $tag->getAttribute("presence"));
					   		
					   		$heartwoodtags = $tag->childNodes;
					   		
					   		foreach($heartwoodtags as $heartwoodtag)
					   		{
					   			if($heartwoodtag->nodeType != XML_ELEMENT_NODE) continue;
					   			switch($heartwoodtag->tagName)
					   			{
					   				case "tridas:missingHeartwoodRingsToPith":				$this->setMissingHeartwoodRingsToPith($heartwoodtag->nodeValue); break;
					   				case "tridas:missingHeartwoodRingsToPithFoundation":	$this->setMissingHeartwoodRingsToPithFoundation($heartwoodtag->nodeValue); break;	
					   				default:
					   					trigger_error("901"."Unknown tag &lt;".$heartwoodtag->tagName."&gt; in 'radius.woodCompleteness.heartwood' entity of the XML request. Tag is being ignored", E_USER_NOTICE);	
					   			}
					   		}
					   		break;		   					
		   				case "tridas:sapwood":
		   					$this->setSapwood(NULL, $tag->getAttribute("presence"));
					   		$sapwoodtags = $tag->childNodes;
					   		foreach($sapwoodtags as $sapwoodtag)
					   		{
					   			if($sapwoodtag->nodeType != XML_ELEMENT_NODE) continue;
					   			
					   			switch ($sapwoodtag->tagName)
					   			{
					   				
					   				case "tridas:nrOfSapwoodRings": 						$this->setNumberOfSapwoodRings($sapwoodtag->nodeValue); break;
					   				case "tridas:lastRingUnderBark": 						
					   					if($sapwoodtag->nodeValue=="")
					   					{
					   						$this->setLastRingUnderBark("", $sapwoodtag->getAttribute("presence"));
					   					}
					   					else
					   					{
					   						$this->setLastRingUnderBark($sapwoodtag->nodeValue, $sapwoodtag->getAttribute("presence"));
					   					}
					   					break;
					   				case "tridas:missingSapwoodRingsToBark":				$this->setMissingSapwoodRingsToBark($sapwoodtag->nodeValue); break;
					   				case "tridas:missingSapwoodRingsToBarkFoundation": 	$this->setMissingSapwoodRingsToBarkFoundation($sapwoodtag->nodeValue); break;
					   				default:
					   					trigger_error("901"."Unknown tag &lt;".$sapwoodtag->tagName."&gt; in 'radius.woodCompleteness.sapwood' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
					   			}
					   		}		   			
							break;
		   				case "tridas:bark":
		   					$firebug->log($tag->getAttribute("presence"), "bark tag: ");
		   					$this->setBarkPresent($tag->getAttribute("presence"));
		   					break;					   		
				
		   				default:
		   					trigger_error("901"."Unknown tag &lt;".$tag->tagName."&gt; in 'radius.woodCompleteness' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   			}
		   		}		   			
				break;
		   	case "tridas:azimuth":				$this->setAzimuth($child->nodeValue); break;
		   	case "tridas:genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{	   			
		   			default:
		   			if(substr($name, 0, strlen("userDefinedField"))==="userDefinedField")
		   				{
		   					$fieldname = substr($name, strlen("userDefinedField."));
		   					try{
		   						$this->setUserDefinedFieldByName($value, $fieldname);
		   						break;
		   					} catch (Exception $e)
		   					{
		   				
		   						trigger_error("901".$e->getMessage(), E_USER_NOTICE);
		   					}
		   				}
		   				else
		   				{
		   					//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'radius' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
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
        
        global $firebug;
        
        //$firebug->log($this, "State of measurementParameters following construction");
    }
    
    function setParamsFromXMLRequest()
    {
		global $tellervoNS;
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
		   	case "tridas:title":				$this->setTitle($child->nodeValue);
		   	case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
   			case "tridas:createdTimestamp": 		break;
   			case "tridas:lastModifiedTimestamp": 	break;			   	
		   	case "tridas:comments":			$this->setComments($child->nodeValue); break;
   			case "tridas:measuringDate":		$this->setMeasuringDate($child->nodeValue); break;
   			case "tridas:derivationDate":		$this->setDerivationDate($child->nodeValue); break;
 		   	case "tridas:type":				$this->setVMeasurementOp(null, $child->nodeValue); break;	  				   	
   			case "tridas:linkSeries":
   				$seriesTags = $child->childNodes;
   				foreach($seriesTags as $series)
   				{
   					if($series->nodeType != XML_ELEMENT_NODE) continue;
   					if($series->nodeName=='tridas:series')
   					{
   						if($series->nodeType != XML_ELEMENT_NODE) continue;
   						$idTags = $series->childNodes;
   						foreach($idTags as $idTag)
		   				{
		   					if($idTag->nodeType != XML_ELEMENT_NODE) continue;
		   					if($idTag->nodeName=='tridas:identifier')
		   					{
		   						array_push($this->referencesArray, $idTag->nodeValue);  		
		   					}
		   					else
		   					{
		   						trigger_error("901"."Only identifier tags are currently supported in Tellervo.", E_USER_NOTICE);
		   					}
		   				}
   					}
   				}
   				break;	
		   	case "tridas:objective":			$this->setObjective($child->nodeValue); break;
   			case "tridas:standardizingMethod":	$this->setStandardizingMethod(null, $child->nodeValue); break;
		
		   	// Ignore user name fields as we use genericField ID's for these instead
		   	case "tridas:analyst":				break;
		   	case "tridas:dendrochronologist":	break;
		   	case "tridas:author":				break;
		   	case "tridas:measuringMethod":		$this->setMeasuringMethod(NULL, $child->getAttribute("normalTridas")); break;
		   	case "tridas:version":				$this->setVersion($child->nodeValue); break;
		   	case "tridas:interpretation":
		   		$interpTags = $child->childNodes;
		   		foreach($interpTags as $interpTag)
		   		{
		   			if($interpTag->nodeType != XML_ELEMENT_NODE) continue;
		   			switch($interpTag->nodeName)
		   			{

		   				case "tridas:datingReference":
		   					$datingRefTags = $interpTag->childNodes;
					   		foreach($datingRefTags as $datingRefTag)
		   					{	
		   						if($datingRefTag->nodeType != XML_ELEMENT_NODE) continue;		 
		   			   			switch($datingRefTag->nodeName)
		   						{		
		   							
						   			case "tridas:linkSeries":
						   				$seriesTags = $datingRefTag->childNodes;
						   				foreach($seriesTags as $series)
						   				{
						   					if($series->nodeType != XML_ELEMENT_NODE) continue;
					   					if($series->nodeName=='tridas:identifier')
						   					{						   
						   						$this->setMasterVMeasurementID($series->nodeValue);  		
						   					}
						   				}
						   				break;
		   						}
		   					}
		   					
			   			case "tridas:dating":
							$firebug->log($interpTag->getAttribute("type"), "dating type");
			   				$this->dating->setDatingType(null, $interpTag->getAttribute("type"));
			   				break;
		   					
			   			case "tridas:firstYear": 
			   				// Special case.  If the series is 'direct' then user may be redating in place
			   				// Kludgey and gross.
			   				if($this->getVMeasurementOp()=="Direct" || $this->getVMeasurementOp()==false)
							{
								$yearwithsuff = $interpTag->nodeValue.$interpTag->getAttribute("suffix");
								$firebug->log(dateHelper::getSignedYearFromYearWithSuffix($yearwithsuff, "astronomical"), "First Year in parameters");
								$this->setFirstYear(dateHelper::getSignedYearFromYearWithSuffix($yearwithsuff, "astronomical"));
							}
			   				break;
			   			case "tridas:pithYear": break;
			   			case "tridas:deathYear":  break;
		   				default:
		   					trigger_error("901"."Unknown tag &lt;".$interpTag->tagName."&gt;. Tag is being ignored", E_USER_NOTICE);
		   					
		   					break;
		   			}
		   		}
		   		break;
			case "tridas:interpretationUnsolved":	break;
			case "tridas:location": break;
			
		    case "tridas:genericField":
		   		$type = $child->getAttribute("type");
		   		$name = $child->getAttribute("name");
		   		$value = $child->nodeValue;		   		
		   		switch($name)
		   		{
		   			case "tellervo.analystID":
		   				$this->analyst->setParamsFromDB($value);
		   				break;
		   			
		   			case "tellervo.dendrochronologistID":
		   				$this->dendrochronologist->setParamsFromDB($value);
		   				break;

		   			case "tellervo.authorID":
		   				$this->setAuthor($value);
		   				break;
		   				
		   			case "tellervo.justification":
		   				$this->setJustification($value);
		   				break;
		   			
		   			case "tellervo.crossdateConfidenceLevel":
		   				$this->setConfidenceLevel($value);
		   				break;
		   				
		   			case "tellervo.isReconciled":		   				
		   				$this->setIsReconciled($value);
		   				break;
		   				
		   			case "tellervo.newStartYear":
	   					$this->setNewStartYear($value);
	   					break;	
		   				
		   			case "tellervo.newEndYear":
		   				$this->setNewEndYear($value);	   					
		   			  				
		   			case "tellervo.directChildCount":		break;
		   			case "tellervo.mapLink":				break;
		   			case "tellervo.isPublished":			break;
		   			case "tellervo.readingCount":			break;		   			
		   			default:
		   				if(substr($name, 0, strlen("userDefinedField"))==="userDefinedField")
		   				{
		   					$fieldname = substr($name, strlen("userDefinedField."));
		   					try{
		   						$this->setUserDefinedFieldByName($value, $fieldname);
		   						break;
		   					} catch (Exception $e)
		   					{
		   						 
		   						trigger_error("901".$e->getMessage(), E_USER_NOTICE);
		   					}
		   				}
		   				else
		   				{
		   					//trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'object' entity of the XML request. Tag is being ignored", E_USER_NOTICE);
		   				}
		   				 
		   		}
		   		break;

			case "tridas:woodCompleteness":
				// Ignore for now, as we expect this in radius instead.  
				// TODO!
				break;
		   			   		
		   	case "tridas:values":
		   		
		   		// In a <values> group tag
		   		
		   		$valuetags = $child->childNodes;
		   		$i = 0;
		   		
		   		foreach($valuetags as $tag)
		   		{
		   			if($tag->nodeType != XML_ELEMENT_NODE) continue;
		   			
		   			if($tag->tagName=='tridas:variable')
		   			{		   				
		   				if($tag->getAttribute("normalTridas")!=NULL)
		   				{
		   					$firebug->log($tag->getAttribute("normalTridas"), "Setting measurement variable");
		   					$this->setVariable(NULL, $tag->getAttribute("normalTridas"));
		   					$firebug->log($this->getVariable(), "Variable now set to ...");
		   					continue;
		   				}
		   				else
		   				{
		   					trigger_error("104"."Only TRiDaS normalised variables are supported by this webservice", E_USER_ERROR);
		   				}
		   			}
		   			else if($tag->tagName=='tridas:unit')
		   			{
		   				if($tag->getAttribute("normalTridas")!=NULL)
		   				{
		   					$this->setUnits(NULL, $tag->getAttribute("normalTridas"));
		   					continue;
		   				}
		   				else
		   				{
		   					trigger_error("104"."Only TRiDaS normalised units are supported by this webservice", E_USER_ERROR);
		   				}		   				
		   			}
		   			else if($tag->tagName == 'tridas:value') 
		   			{	
		   				// Tag is a <value> tag so process
		   				
		   				// Check that we know what variable we're dealing with
		   				if($this->getVariable()==null || $this->getVariable()=='')
		   				{
		   					trigger_error("104"."Values 'variable' is missing", E_USER_ERROR);		   		
		   				}
		   				
		   				// get the value fields
			   			$value = $tag->getAttribute("value");  

						$firebug->log($value, "Ring width value");
			   			
			   			
			   			// If this is the ring width variable, see if it has child nodes (notes)
			   			if($this->getVariable()=='ring width')
			   			{
				   			$valuechildren = $tag->childNodes;
			   				$myNotesArray = array();			  			
				   			foreach($valuechildren as $valuechild)
				   			{			   				
				   				if($valuechild->nodeType != XML_ELEMENT_NODE) continue;
				   				if($valuechild->tagName == 'tridas:remark')
				   				{

									$firebug->log($valuechild, "Remark tag");
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
					   					// Tellervo or free text remark
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
			   			}

			   			// Mush all the reading and notes into the ReadingArray
			   			
			   			// Start by grabbing the readings Array if it exists or creating
			   			// an empty one if it does not
			   			if(count($this->readingsArray)>$i)
			   			{
			   				// readingsArray already exists
			   				$readingArrEntry = $this->readingsArray[$i];
			   			}
			   			else
			   			{
			   				// no entry yet for this year in readingsArray
			   				// so create a new empty one
			   				$readingArrEntry = array('value' => NULL, 
		                                                     'wjinc' => NULL, 
		                                                     'wjdec' => NULL, 
			   												 'ewwidth' => NULL,
			   							   					 'lwwidth' => NULL,
		                                                     'count' => NULL,
		                                                     'notesArray' => NULL);
			   				$this->readingsArray[]= $readingArrEntry;
			   			}
	
			   			//$firebug->log($this->getVariable(), "Variable being used");
			   			//$firebug->log($value, "Width value");
			   			
			   			
			   			if($this->getVariable()=="ring width")
			   			{
			   				//$firebug->log(unit::unitsConverter($value, $this->getUnits(), "db-default"), "Setting new ring width value");
			   				$readingArrEntry['value'] = unit::unitsConverter($value, $this->getUnits(), "db-default");
			   				$readingArrEntry['notesArray'] = $myNotesArray;	
			   				//$firebug->log($readingArrEntry, "new readingArrEntry is now...");	   				
			   			}
			   			else if ($this->getVariable()=="earlywood width")
			   			{
			   				$readingArrEntry['ewwidth'] = unit::unitsConverter($value, $this->getUnits(), "db-default");
			   			}
		   				else if ($this->getVariable()=="latewood width")
			   			{
			   				$readingArrEntry['lwwidth'] = unit::unitsConverter($value, $this->getUnits(), "db-default");
			   			}	
			   			else
			   			{
			   				$firebug->log("Unknown measurement variable"); 
			   				die();
			   				
			   			}		   			
			   			
			   			// Add our new or edited entry to the readingsArray
			   			//$firebug->log($i, "Inserting details into readingArray at index..."); 
			   			//$firebug->log($readingArrEntry, "ReadingArray that is being inserted...");
			   			$this->readingsArray[$i] = $readingArrEntry;
			   			
		   			}
		   			else
		   			{
		   				// Tag not a value tag so skip
		   				continue;
		   			}
		   			
		   			// Increment readingArray counter
					$i++;
					
		   		}	
		   		
		   		// Reset variable to 'ring width'
		   		$this->setVariable(NULL, 'ring width');   
		   		//$firebug->log($this->readingsArray, "Readings array in parameters.php");			
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
		global $tellervoNS;
        global $tridasNS;
	

        $children = $this->xmlRequestDom->documentElement->childNodes;
        
        foreach($children as $child)
        {
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   switch ($child->tagName)
		   {
		   	case "tridas:identifier": 			$this->setID($child->nodeValue, $child->getAttribute("domain")); break;
		   	case "tridas:title":				$this->setTitle($child->nodeValue); break;
		   	case "trackingLocation":	$this->setTrackingLocation($child->nodeValue); break;
		   	case "curationLocation":	$this->setCurationLocation($child->nodeValue); break;
		   	case "tridas:comments":			$this->setComments($child->nodeValue); break;
		   	case "tridas:createdTimestamp"     : break;
		   	case "tridas:lastModifiedTimestamp": break;
		   	case "sampleCount"			: break;
		   	case "tridas:sample"			: break;	   				   		
		   	default:
		   		trigger_error("901"."Unknown tag &lt;".$child->tagName."&gt; in 'box' entity of the XML request", E_USER_NOTICE);
		   }
        }   
    }	
}
    

class securityGroupParameters extends securityGroupEntity implements IParams
{
	var $xmlRequestDom = NULL;
    
    function __construct($xmlrequest, $parentID=NULL)
    {
    	global $firebug;
    	parent::__construct();    	
    	
    	// Load the xmlrequest into a local DOM variable
        if (gettype($xmlrequest)=='root')
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
		global $tellervoNS;
        global $tridasNS;

        $children = $this->xmlRequestDom->documentElement->childNodes;
     	
        foreach($children as $child)
        {
			
		   if($child->nodeType != XML_ELEMENT_NODE) continue;        	
        	
		   $firebug->log($child->hasAttribute("id"), "has id?");
		   
		   if ($child->tagName=='securityGroup')
		   {
		   		$firebug->log($child->getAttribute("isActive"), "child");
		   		if($child->hasAttribute("id")) $this->setID($child->getAttribute("id"), null);
		   		if($child->hasAttribute("name")) $this->setName($child->getAttribute("name"));
		   		if($child->hasAttribute("description")) $this->setDescription($child->getAttribute("description"));
		   		if($child->hasAttribute("isActive")) $this->setIsActive(dbhelper::formatBool($child->getAttribute("isActive"),"php"));
				if($child->hasAttribute("userMembers")){
					$groups = explode(" ", trim($child->getAttribute("userMembers")));
					$this->userMembers  = $groups;
				}

		   		if($child->hasAttribute("groupMembers")){
					$groups = explode(" ", trim($child->getAttribute("groupMembers")));
					$this->groupMembers  = $groups;
				}
		   
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
        if (gettype($xmlrequest)=='root')
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
	global $tellervoNS;
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
		   		if($child->hasAttribute("odkPassword")) $this->setODKPassword($child->getAttribute("odkPassword"));
				if($child->hasAttribute("memberOf")){
					$groups = explode(" ", trim($child->getAttribute("memberOf")));
					$this->groupArray  = $groups;
				}

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
    

?>
