<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
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
		
	/**
	 * Additional information about the location 
	 *
	 * @var String
	 */
	protected $comment = NULL;	

	
    /***********/
    /* SETTERS */
    /***********/   

	function setComment($value)
	{
		$this->comment = addslashes($value);
		return true;
	}	
	

	/**
	 * Set the geometry field using a GML string.  Currently this isn't very sophisticated as it will only accept gml:point data
	 * At some point this should use the OGR library.
	 *
	 * @param String $gml
	 * @return Boolean
	 */
	function setGeometryFromGML($gml)
	{

		global $gmlNS;
				
		// Wrap GML tags in root elements
		$start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".
				 "<corina xmlns=\"$gmlNS\">".
    			 "<featureMember>\n";
    	$end =  "</featureMember></corina>"; 
        $doc = new DomDocument;
        $doc->loadXML($start.$gml.$end);

        // Handle validation errors ourselves
        libxml_use_internal_errors(true);		
		
        // Do various checks for unsupported GML data
        $tags = $doc->getElementsByTagName("locationGeometry")->item(0)->childNodes;
        foreach($tags as $tag)
        {
		   if($tag->nodeType != XML_ELEMENT_NODE) continue;   
			
		   if(strtolower($tag->tagName)!="point")
		   {
		   		trigger_error("104"."This webservice does not accept gml:".$tag->tagName.". Please resubmit your request with either no GML data or with gml:point data.", E_USER_ERROR);
		   		return false;
		   }
		   
		   if($tag->hasAttribute("srsName"))
		   {
		   		if($tag->getAttribute("srsName")!="EPSG:4326")
		   		{
		   			trigger_error("104"."This webservice currently only supports GML data supplied in the EPSG:4326 coordinate system", E_USER_ERROR);
		   			return false;
		   		}
		   }
		   
		   // Extract coordinates from point GML
		   $coords = explode(" ", $doc->getElementsByTagName("pos")->item(0)->nodeValue, 2);
		     
		   
		   // Calculate geometry value and store
		   $this->setPointGeometryFromLatLong($coords[0], $coords[1]);	
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
		
		$sql = "select setsrid(makepoint(".sprintf("%1.8f",pg_escape_string($long)).", ".sprintf("%1.8f",pg_escape_string($lat))."), ".pg_escape_string($srid).") as thevalue";
	
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
	 * Get the comment associated with this geometry
	 *
	 * @return String
	 */
	function getComment()
	{
		return $this->comment;
	}
	
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
		
		$sql = "select st_asgml(".pg_escape_string($gmlversion).", '".pg_escape_string($this->geometry)."') as thevalue";		
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
		$sql = "select geometrytype('".pg_escape_string($this->geometry)."') as thevalue";
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
						$sql = "select st_askml(".pg_escape_string($kmlversion).", '".pg_escape_string($this->geometry)."') as thevalue";
						$kml .= $this->runSQLCalculation($sql);	
						$firebug->log($kml, "Output KML for point:");
						return $kml;
					case "POLYGON":
						// Data type is polygon so return centroid
						$sql = "select st_askml(".pg_escape_string($kmlversion).", st_centroid(st_expand('".pg_escape_string($this->geometry)."'::geometry, 0.1))) as thevalue"; 
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
						$sql = "select st_askml(".pg_escape_string($kmlversion).", '".pg_escape_string($this->geometry)."') as thevalue";
						$kml .= $this->runSQLCalculation($sql);	
						$firebug->log($kml, "Output KML for polygon:");
						return $kml;						
				}
		}
		
		return null;
	}
	
	function getX()
	{
		$sql = "select x(centroid('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getY()
	{	
		$sql = "select y(centroid('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getXMin()
	{
		$sql = "select xmin(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);				
	}
	
	function getYMin()
	{
		$sql = "select ymin(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}

	function getXMax()
	{
		$sql = "select xmax(getbbox('".$this->geometry."')) as thevalue";
		return $this->runSQLCalculation($sql);			
	}
	
	function getYMax()
	{
		$sql = "select ymax(getbbox('".$this->geometry."')) as thevalue";
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
            return $row['thevalue'];
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
	
	function __construct()
	{
		$this->type = new locationType();
	}
		
    /***********/
    /* SETTERS */
    /***********/   
	
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
	function setGeometry($geometry, $type=null, $precision=null, $comment=null)
	{
		$this->geometry = $geometry;
		if($type!=NULL) $this->setType(NULL, $type);
		$this->setPrecision($precision);
		$this->setComment($comment);	
	}
		
	/**
	 * Set the type of location the location field 
	 *
	 * @param unknown_type $value
	 * @return unknown
	 */
	function setType($id=NULL, $value=NULL)
	{
		return $this->type->setLocationType($id, $value);
	}
	
	function setPrecision($value)
	{	
		$this->precision = (int) $value;
		return true;
	}
	

    /***********/
    /* GETTERS */
    /***********/  	
	
	function asXML()
	{
        $xml = "<tridas:location>\n";
       	$xml.= "<tridas:locationGeometry>\n";
       	$xml.= $this->asGML();
       	$xml.= "\n</tridas:locationGeometry>\n";
       	if($this->getType()!=NULL) $xml .= "<tridas:locationType>".$this->getType()."</tridas:locationType>\n";
        if($this->getPrecision()!=NULL) $xml .= "<tridas:locationPrecision>".$this->getPrecision()."</tridas:locationPrecision>\n";
        if($this->getComment()!=NULL) $xml .= "<tridas:locationComment>".$this->getComment()."</tridas:locationComment>\n";      	
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
