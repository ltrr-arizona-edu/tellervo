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
		global $dbconn;
		$sql = "select asgml('".$this->geometry."')";
        $dbconnstatus = pg_connection_status($dbconn);
		if ($dbconnstatus ===PGSQL_CONNECTION_OK)
		{
		    pg_send_query($dbconn, $sql);
		    $result = pg_get_result($dbconn); 
                    $row = pg_fetch_array($result);		
                    return $row['asgml'];

		}
		else
		{
			return false;
		}

	}
	
	function asKML()
	{	
	}
	
	function getX()
	{	
	}
	
	function getY()
	{	
	}
	
	function getXMin()
	{
	}
	
	function getYMin()
	{
	}

	function getXMax()
	{
	}
	
	function getYMax()
	{
	}	
}
?>
