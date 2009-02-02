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
	/**
	 * The actual geometry
	 *
	 * @var Geometry
	 */
	private $geometry = NULL;
	
	/**
	 * One of growth; utilised (static); utilised (mobile); current, manufacture
	 *
	 * @var String
	 */
	private $type = NULL;
	
	/**
	 * Precision of the location information in metres
	 *
	 * @var Integer
	 */
	private $precision = NULL;
	
	/**
	 * Additional information about the location 
	 *
	 * @var String
	 */
	private $comment = NULL;	
	
	
	
	function __construct()
	{
	}
	
    /***********/
    /* SETTERS */
    /***********/   

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
		$this->setType($type);
		$this->setPrecision($precision);
		$this->setComment($comment);	
	}
	
	/**
	 * Set the geometry field using a GML string
	 *
	 * @todo Implement!  Hopefully someone has already written a library to do this otherwise we'll have to parse the GML ourselves.
	 * @param String $gml
	 */
	function setGeometryFromGML($gml)
	{
	}
	
	/**
	 * Set the geometry using latitude and longitude.  This function assumes that the lat and long are standard WGS84 coordinates.
	 *
	 * @param Float $lat
	 * @param Float $long
	 */
	function setPointGeometryFromLatLong($lat, $long)
	{
		// Make sure the parameters are numbers to stop sql injection
		$lat = (float) $lat;
		$long = (float) $long;
		$srid = 4326; // Standard WGS84 SRID is assumed for this funciton	
		
		$sql = "select setsrid(makepoint(".sprintf("%1.8f",$long).", ".sprintf("%1.8f",$lat)."), $srid) as thevalue";
		$this->geometry = $this->runSQLCalculation($sql);		
	}
	
	
	/**
	 * Set the type of location the location field 
	 *
	 * @param unknown_type $value
	 * @return unknown
	 */
	function setType($value)
	{
		$this->type = addslashes($value);
		return true;
	}
	
	function setPrecision($value)
	{	
		$this->precision = (int) $value;
		return true;
	}
	
	function setComment($value)
	{
		$this->comment = addslashes($value);
		return true;
	}

    /***********/
    /* GETTERS */
    /***********/   

	function asXML()
	{
        $xml = "<tridas:location>";
       	$xml.= "<tridas:locationGeometry>";
       	$xml.= $this->asGML();
       	$xml.= "</tridas:locationGeometry>";
       	if($this->getLocationType()!=NULL) $xml .= "<tridas:locationType>".$this->getLocationType()."</tridas:locationType>";
        if($this->getLocationPrecision()!=NULL) $xml .= "<tridas:locationPrecision>".$this->getLocationPrecision()."</tridas:locationPrecision>";
        if($this->getLocationComment()!=NULL) $xml .= "<tridas:locationComment>".$this->getLocationComment()."</tridas:locationComment>";      	
       	$xml.= "</tridas:location>";
        return $xml;
	}
	
	function asGML()
	{
		$sql = "select asgml('".$this->geometry."') as thevalue";
		return $this->runSQLCalculation($sql);
	}
	
	function asKML()
	{
		$sql = "select askml('".$this->geometry."') as thevalue";
		return $this->runSQLCalculation($sql);
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
	
	function getLocationGeometry()
	{
		return $this->geometry;
	}
	
	function getLocationType()
	{
		return $this->type;
	}
	
	function getLocationPrecision()
	{	
		return $this->precision;
	}
	
	function getLocationComment()
	{
		return $this->comment;
	}	

	private function runSQLCalculation($sql)
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
			return false;
		}			
	}
	
}
?>
