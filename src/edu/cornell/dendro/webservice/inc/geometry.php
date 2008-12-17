<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
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
	 * Additional information and the location 
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
	function setGeometry($geometry)
	{
		$this->geometry = $geometry;	
	}
	
	function setGeometryFromGML($gml)
	{
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
