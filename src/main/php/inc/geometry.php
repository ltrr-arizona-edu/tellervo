<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

/**
 * Wrapper for the PostGIS Geometry datatype
 *
 */
class geometry
{
	function __construct()
	{
	}	

	/**
	 * The actual geometry
	 *
	 * @var Geometry
	 */
	protected $geometry = NULL;
			
    /***********/
    /* SETTERS */
    /***********/   


	
	/**
	 * Set the geometry field using a GML string.  Currently this isn't very sophisticated as it will only accept gml:point data
	 * At some point this should use the OGR library.
	 *
	 * @param String $gml
	 * @return Boolean
	 */
	function setGeometryFromGML($gml)
	{

	global $firebug;
	global $gmlNS;
	global $tridasNS;
				
	// Wrap GML tags in root elements
	$start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tellervo xmlns=\"$gmlNS\" xmlns:tridas=\"$tridasNS\">\n<featureMember>\n";
	$end =  "</featureMember></tellervo>"; 
        $doc = new DomDocument;


        $doc->loadXML($start.$gml.$end);
	$firebug->log($start.$gml.$end, "GML");

        // Handle validation errors ourselves
        libxml_use_internal_errors(true);		
		
        // Do various checks for unsupported GML data
        $tags = $doc->getElementsByTagName("locationGeometry")->item(0)->childNodes;
        foreach($tags as $tag)
        {
		   if($tag->nodeType != XML_ELEMENT_NODE) continue;   
			
		   if(strtolower($tag->tagName)!="gml:point")
		   {
		   		trigger_error("104"."This webservice does not accept gml:".$tag->tagName.". Please resubmit your request with either no GML data or with gml:point data.", E_USER_ERROR);
		   		return false;
		   }
		   
		   if($tag->hasAttribute("srsName"))
		   {
		   		if(($tag->getAttribute("srsName")!="EPSG:4326") && ($tag->getAttribute("srsName")!="urn:ogc:def:crs:EPSG::4326"))
		   		{
		   			trigger_error("104"."This webservice currently only supports GML data supplied in the EPSG:4326 coordinate system", E_USER_ERROR);
		   			return false;
		   		}
		   }
		   
		   // Extract coordinates from point GML
		   $coords = explode(" ", $doc->getElementsByTagName("gml:pos")->item(0)->nodeValue, 2);
		     
		   
		   // Calculate geometry value and store
		   if($tag->hasAttribute("srsName") && $tag->getAttribute("srsName") && $tag->getAttribute("srsName")!="urn:ogc:def:crs:EPSG::4326")
		   { 
		        // When reading geom with full urn  for srsName we need to switch the coordinates
		   	$this->setPointGeometryFromLatLong($coords[1], $coords[0]);	
		   }
		   else
		   {
		   	$this->setPointGeometryFromLatLong($coords[0], $coords[1]);	
		   }
		   return true;	   
        }
  
	}
	
	/**
	 * Set the geometry using latitude and longitude.  Default coordinate system is 4326
	 *
	 * @param Float $lat
	 * @param Float $long
	 * @param Integer $srid
	 */
	function setPointGeometryFromLatLong($lat, $long, $srid=4326)
	{
		// Make sure the parameters are numbers to stop sql injection
		$lat = (float) $lat;
		$long = (float) $long;	
		
		$sql = "select st_setsrid(st_makepoint(".sprintf("%1.8f",pg_escape_string($dbconn, $long)).", ".sprintf("%1.8f",pg_escape_string($dbconn, $lat))."), ".pg_escape_string($dbconn, $srid).") as thevalue";
	
		$this->geometry = $this->runSQLCalculation($sql);	
	}		
	
	/**
	 * Set the actual geometry field
	 *
	 * @param String $geometry
	 */
	function setGeometry($geometry)
	{
		$this->geometry = $geometry;
	}
		

    /***********/
    /* GETTERS */
    /***********/   
	
	/**
	 * Get this location geometry as GML
	 *
	 * @param Integer $gmlversion either 2 or 3 defaults to 3
	 * @return String
	 */
	function asGML($gmlversion=3)
	{
		// Override to v3 if not 2 or 3
		if ($gmlversion!=2 || $gmlversion!=3) $gmlversion=3;
		
		// PG v8.x 
		//$sql = "select st_asgml(".pg_escape_string($dbconn, $gmlversion).", '".pg_escape_string($dbconn, $this->geometry)."') as thevalue";
		
		// PG v9.1 
		// the final parameter of 17 in st_asgml flips the coordinates to lat/lon as they should be for EPSG:4326.  
		$sql = "select st_asgml(".pg_escape_string($dbconn, $gmlversion).", '".pg_escape_string($dbconn, $this->geometry)."'::geometry, 15, 17) as thevalue";
				
		return $this->runSQLCalculation($sql);
	}
	
	/**
	 * Get the location geometry as KML
	 *
	 * @param Integer $kmlversion
	 * @return String
	 */
	function asKML($kmlversion=2, $returnDataType="POINT")
	{
		global $firebug;
		global $dbconn;
		$sql = "select geometrytype('".pg_escape_string($dbconn, $this->geometry)."') as thevalue";
		$actualDataType = $this->runSQLCalculation($sql);
		$kml = "";
		
		$firebug->log($actualDataType, "GML datatype");
		
		switch($returnDataType)
		{
			case "POINT":
				// Need to return point data
				switch($actualDataType)
				{
					case "POINT":
						// Actual datatype is point so simple
						
						// PG v8.x
						//$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", '".pg_escape_string($dbconn, $this->geometry)."') as thevalue";
						
						// PG v9.1
						// TODO Check whether st_askml needs final parameter switching to 17 like st_asgml does
						$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", '".pg_escape_string($dbconn, $this->geometry)."'::geometry, 15, 1) as thevalue";
						
						$kml .= $this->runSQLCalculation($sql);	
						$firebug->log($kml, "Output KML for point:");
						return $kml;
					case "POLYGON":
						// Data type is polygon so return centroid
						
						// PG v8.x 
						//$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", st_centroid(st_expand('".pg_escape_string($dbconn, $this->geometry)."'::geometry, 0.1))) as thevalue";

						// PG v9.1 
						$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", st_centroid(st_expand('".pg_escape_string($dbconn, $this->geometry)."'::geometry, 0.1)), 15, 1) as thevalue";
												
						$kml .= $this->runSQLCalculation($sql);	
						$firebug->log($kml, "Output KML for centroid of polygon:");
						return $kml;						
				}
			case "POLYGON":
				// Need to return polygon
				switch($actualDataType)
				{
					case "POINT":
						// Actual data type is point so can't return what was requested
						return null;
					case "POLYGON":
						// PG v8.x 
						//$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", '".pg_escape_string($dbconn, $this->geometry)."') as thevalue";
						
						// PG v9.1 
						$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", '".pg_escape_string($dbconn, $this->geometry)."'::geometry, 15, 1) as thevalue";
												
						$kml .= $this->runSQLCalculation($sql);	
						$firebug->log($kml, "Output KML for polygon:");
						return $kml;						
				}
		}
		
		return null;
	}
	
	function asKMLValue($kmlversion=2, $value)
	{
		global $firebug;
        global $dbconn;
        
		// Actual datatype is point so simple
		$value = $value/20;
		$sql = "select st_askml(".pg_escape_string($dbconn, $kmlversion).", ST_Buffer('".pg_escape_string($dbconn, $this->geometry)."'::geometry, ".$value.")) as thevalue";
		$kml .= $this->runSQLCalculation($sql);				
		$firebug->log($kml, "Output KML for point value:");
		return $kml;

		
		
		return null;
	}
	
	function getX()
	{
		$sql = "select st_x(centroid('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getY()
	{	
		$sql = "select st_y(centroid('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getXMin()
	{
		$sql = "select st_xmin(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);				
	}
	
	function getYMin()
	{
		$sql = "select st_ymin(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}

	function getXMax()
	{
		$sql = "select st_xmax(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getYMax()
	{
		$sql = "select st_ymax(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getGeometry()
	{
		return $this->geometry;
	}
	
	function runSQLCalculation($sql)
	{
		global $dbconn;
        $dbconnstatus = pg_connection_status($dbconn);
		if ($dbconnstatus ===PGSQL_CONNECTION_OK)
		{
		    pg_send_query($dbconn, $sql);
		    $result = pg_get_result($dbconn); 
            $row = pg_fetch_array($result);		
            
            // Unfortunately the PostGIS GML function includes the srsDimension
            // attribute that is not present in GML-SF.  Until PostGIS supports
            // this we'll   
            $value = $row['thevalue'];
            $value = str_replace("srsDimension=\"2\"", "", $value);
       
            return $value;
		}
		else
		{
			trigger_error("No value returned in SQL query");
			return false;
		}			
	}
	
}

class location extends geometry
{
	/**
	 * Additional information about the location 
	 *
	 * @var String
	 */
	protected $comment = NULL;	
	protected $addressline1 = NULL;
	protected $addressline2 = NULL;
	protected $cityortown = NULL;
	protected $stateprovinceregion = NULL;
	protected $postalcode = NULL;
	protected $country = NULL;
	
	
	
	function __construct()
	{
		$this->type = new locationType();
	}
		
    /***********/
    /* SETTERS */
    /***********/   
	
	function setComment($value)
	{
		$this->comment = $value;
		return true;
	}	
	
	function setAddressLine1($value)
	{
		$this->addressline1 = $value;
		return true;
	}	

	function setAddressLine2($value)
	{
		$this->addressline2 = $value;
		return true;
	}		
	
	function setCityOrTown($value)
	{
		$this->cityortown = $value;
		return true;
	}

	function setStateProvinceRegion($value)
	{
		$this->stateprovinceregion = $value;
		return true;
	}	
	
	function setPostalCode($value)
	{
		$this->postalcode = $value;
		return true;
	}
	
	function setCountry($value)
	{
		$this->country = $value;
		return true;
	}	
	
	/**
	 * One of growth; utilised (static); utilised (mobile); current, manufacture
	 *
	 * @var locationType
	 */
	private $type = NULL;
	
	/**
	 * Precision of the location information in metres
	 *
	 * @var Integer
	 */
	private $precision = NULL;	
	
	/**
	 * Set the geometry field using a native PostGIS geometry representation
	 *
	 * @param String $geometry
	 * @param String $type
	 * @param String $precision
	 * @param String $comment
	 */
	function setGeometry($geometry, $type=null, $precision=null)
	{
		$this->geometry = $geometry;
		global $firebug;

		$firebug->log($type, "Location type:");
		if($type!=NULL) $this->type->setLocationType(NULL, $type);
		$this->setPrecision($precision);	
	}
		
	/**
	 * Set the type of location the location field 
	 *
	 * @param unknown_type $value
	 * @return unknown
	 */
	function setType($value)
	{
		global $firebug;
		$firebug->log($value, "setType called on location type object with value");
		return $this->type->setLocationType(NULL, $value);
	}
	
	function setPrecision($value)
	{	
		$this->precision = (int) $value;
		return true;
	}
	

    /***********/
    /* GETTERS */
    /***********/  	
	
	/**
	 * Get the comment associated with this geometry
	 *
	 * @return String
	 */
	function getComment()
	{
		return $this->comment;
	}
	
	/**
	 * Get the first line of the address
	 * 
	 * @return String
	 */
	function getAddressLine1()
	{
		return $this->addressline1;
	}
	
	/**
	 * Get the second line of the address
	 * 
	 * @return String
	 */	
	function getAddressLine2()
	{
		return $this->addressline2;
	}
	
	/**
	 * Get the city or town for the address
	 * 
	 * @return String
	 */	
	function getCityOrTown()
	{
		return $this->cityortown;
		
	}
	
	/**
	 * Get the state, province or region for the address
	 * 
	 * @return String
	 */	
	function getStateProvinceRegion()
	{
		return $this->stateprovinceregion;
	}
	
	/**
	 * Get the postal code for the address
	 * 
	 * @return String
	 */	
	function getPostalCode()
	{
		return $this->postalcode;
	}
	
	/**
	 * Get the country
	 * 
	 * @return String
	 */	
	function getCountry()
	{
		return $this->country;
	}
	
	/**
	 * Does this have address information
	 * 
	 * @return Boolean
	 */
	function hasAddress()
	{
	global $firebug;
		if ($this->getAddressLine1()!=NULL ||
			$this->getAddressLine2()!=NULL ||
			$this->getCityOrTown()!=NULL ||
			$this->getStateProvinceRegion()!=NULL ||
			$this->getPostalCode()!=NULL ||
			$this->getCountry()!=NULL)
			{
				$firebug->log("has address");
				return true;
			}
		else
		{
				$firebug->log("does not have address");
			return false;
		}
	}
	
	function asXML()
	{
        $xml = "<tridas:location>\n";
	if($this->geometry!=null)
	{
       		$xml.= "<tridas:locationGeometry>\n";
       		$xml.= $this->asGML();
	       	$xml.= "\n</tridas:locationGeometry>\n";
	}
       	if($this->getType()!=NULL) $xml .= "<tridas:locationType>".dbHelper::escapeXMLChars($this->getType())."</tridas:locationType>\n";
        if($this->getPrecision()!=NULL) $xml .= "<tridas:locationPrecision>".$this->getPrecision()."</tridas:locationPrecision>\n";
        if($this->getComment()!=NULL) $xml .= "<tridas:locationComment>".dbHelper::escapeXMLChars($this->getComment())."</tridas:locationComment>\n"; 
        
        if($this->hasAddress())
        {
        	$xml.="<tridas:address>\n";
        	if($this->getAddressLine1()!=NULL) $xml .= "<tridas:addressLine1>".dbHelper::escapeXMLChars($this->getAddressLine1())."</tridas:addressLine1>\n"; 
        	if($this->getAddressLine2()!=NULL) $xml .= "<tridas:addressLine2>".dbHelper::escapeXMLChars($this->getAddressLine2())."</tridas:addressLine2>\n"; 
        	if($this->getCityOrTown()!=NULL)   $xml .= "<tridas:cityOrTown>".dbHelper::escapeXMLChars($this->getCityOrTown())."</tridas:cityOrTown>\n";
        	if($this->getStateProvinceRegion()!=NULL)   $xml .= "<tridas:stateProvinceRegion>".dbHelper::escapeXMLChars($this->getStateProvinceRegion())."</tridas:stateProvinceRegion>\n";
        	if($this->getPostalCode()!=NULL)   $xml .= "<tridas:postalCode>".dbHelper::escapeXMLChars($this->getPostalCode())."</tridas:postalCode>\n";
        	if($this->getCountry()!=NULL)   $xml .= "<tridas:country>".dbHelper::escapeXMLChars($this->getCountry())."</tridas:country>\n";
        	$xml.="</tridas:address>\n";
        	
        }
        
       	$xml.= "</tridas:location>\n";
        return $xml;
	}	
	
	function getType()
	{
		return $this->type->getValue();
	}
	
	function getTypeID()
	{
		return $this->type->getID();
	}
	
	function getPrecision()
	{	
		return $this->precision;
	}
	
	function getLocationGeometry()
	{
		return $this->getGeometry();
	}	
}

class extent extends geometry
{

    /***********/
    /* GETTERS */
    /***********/  	
	
	function asXML()
	{
        $xml = "<tridas:extent>\n";
       	$xml.= "<tridas:extentGeometry>\n";
       	$xml.= $this->asGML();
       	$xml.= "\n</tridas:extentGeometry>\n";
        if($this->getComment()!=NULL) $xml .= "<tridas:extentComment>".$this->getComment()."</tridas:extentComment>\n";      	
       	$xml.= "</tridas:extent>\n";
        return $xml;
	}
}
?>
