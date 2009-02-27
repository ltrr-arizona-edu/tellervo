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
 * Class containing static functions for performing various database formatting functions 
 *
 */
class dbHelper
{
	
	/**
	 * Translates a value into a db pkey for one of the standard lookup tables
	 *
	 * @param String $entityname
	 * @param String $value
	 * @return Integer
	 */
	public static function getKeyFromValue($entityname, $value)
	{
		global $dbconn;
		$sql = "select tlkp".pgsql_escape_string($entityname).".".pgsql_escape_string($entityname)."id as key from tlkp".pgsql_escape_string($entityname)." where ".pgsql_escape_string($entityname)."='".pgsql_escape_string($value)."'";
		
		$dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
            	trigger_error("667"."No record found when looking up key for ".$value." in table tlkp$entityname", E_USER_NOTICE);
                return NULL;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
				return $row['key'];
            }

        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database", E_USER_ERROR);
            return FALSE;
        }

        return TRUE;		
	}
	
	/**
	 * Replace all special XML characters in a string
	 *
	 * @param String $theString
	 * @return String
	 */
	public static function escapeXMLChars($theString)
	{
	    $theString = str_replace('&', '&amp;',  $theString);
	    $theString = str_replace("'", '&apos;', $theString);
	    $theString = str_replace('"', '&quot;', $theString);
	    $theString = str_replace('<', '&lt;',   $theString);
	    $theString = str_replace('>', '&gt;',   $theString);
	    return $theString;
	}
	
	/**
	 * Get the timestamp when the specified table was last updated
	 *
	 * @param String $tablename
	 * @return Timestamp
	 */
	public static function getLastUpdateDate($tablename)
	{
	    global $dbconn;
	    $sql = "select max(lastmodifiedtimestamp) as max from ".pgsql_escape_string($tablename);
	    $dbconnstatus = pg_connection_status($dbconn);        
	    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	    {
	        $result = pg_query($dbconn, $sql);
	        $row = pg_fetch_array($result);
	        {
	            return dateFudge($row['max']);
	        }
	    }
	    else
	    {
	        return false; 
	    }
	}
	
	/**
	 * Converts from most English, PHP or PG representations of a boolean into a PHP, English, Presence/Absence or PG boolean.
	 * Default output format is PHP
	 *
	 * @param String $value
	 * @param String $format
	 * @return Boolean or 'error'
	 */
	
	public static function formatBool($value, $format='php')
	{
		// Turn value into an internal boolean
	    if(($value===TRUE) || (strtolower($value) == 't') || (strtolower($value) == 'true') || ($value === 1) || ($value == '1') || (strtolower($value) == 'present'))
	    {   
	        $outputvalue = TRUE;
	    }
	    elseif(($value===FALSE) || (strtolower($value) == 'f') || (strtolower($value) == 'false') || ($value === 0) || ($value == '0') || (strtolower($value) == 'absent') )
	    {
	        $outputvalue = FALSE;
	    }	
	    elseif($value==NULL)
	    {
	    	return NULL;
	    }
	    else
	    {
			trigger_error("667"."Unable to interpret a boolean from the value provided to formatBool().  Value given was '$value'", E_USER_ERROR);    	
	    	return 'error';
	    }
	    
	    // Output depending on format
	    switch(strtolower($format))
	    {
	    	case "php":
	    		return $outputvalue;
	    	case "pg":
	    		if($outputvalue===TRUE) return 't';
	    		return 'f';
	    	case "english":
	    		if($outputvalue===TRUE) return 'true';
	    		return 'false';
	    	case "presentabsent":
	    		if($outputvalue===TRUE) return 'present';
	    		return 'absent';
	    	default:
	    		trigger_error("667"."Invalid format provided in formatBool().  Should be one of php, pg or english but got $format instead", E_USER_ERROR);
	    		return false;
	    }
		
	}
	
	/**
	 * Translates PostgreSQL boolean to a PHP boolean
	 *
	 * @deprecated use dbHelper::formatBool()
	 * @param PG Bool $theValue
	 * @return PHP Bool
	 */
	public static function fromPGtoPHPBool($theValue)
	{
	    // Translates PG Bool into PHP Bool.
	    // Returns NULL if value is neither
	
	    if($theValue=='t')
	    {   
	        return TRUE;
	    }
	    elseif($theValue=='f')
	    {
	        return FALSE;
	    }
	    else
	    {
	        return NULL;
	    }
	}
	
	/**
	 * Translates a PHP boolean to a PostgreSQL boolean.
	 *
	 * @deprecated use dbHelper::formatBool() 
	 * @param PHP Bool $theValue
	 * @return PG Bool
	 */
	public static function fromPHPtoPGBool($theValue)
	{
	    // Translates PHP Bool into PG Bool
	    // Returns NULL if value is neither
	
	
	    if($theValue===TRUE)
	    {   
	        $value = "t";
	    }
	    elseif($theValue===FALSE)
	    {
	        $value = "f";
	    }
	    else
	    {
	        $value = NULL;
	    }
	
	    return $value;
	}
	
	/**
	 * Translate any English value for true and false into a boolean
	 *
	 * @deprecated use dbHelper::formatBool() 
	 * @param String $theValue
	 * @return Bool
	 */
	public static function fromStringtoPHPBool($theValue)
	{
	    if($theValue=="t" || $theValue=="true" || $theValue=="TRUE" || $theValue=="True" || $theValue=="1"|| $theValue === TRUE)
	    {
	        return TRUE;
	    }
	    elseif($theValue=="f" || $theValue=="false" || $theValue=="FALSE" || $theValue=="False" || $theValue=="0" || $theValue=== FALSE)
	    {
	        return FALSE;
	    }
	    elseif($theValue==NULL)
	    {
	        return NULL;
	    }
	    else
	    {
	        return "Invalid";
	    }
	}
	
	/**
	 * Translate a PostgreSQL Bool to an English string
	 *
	 * @deprecated use dbHelper::formatBool() 
	 * @param PG Bool $theValue
	 * @return String
	 */
	public static function fromPGtoStringBool($theValue)
	{
	    if($theValue=="t")
	    {
	        return "true";
	    }
	    else
	    {
	        return "false";
	    }
	}
	
	/**
	 * Translate a PHP Bool into an English string
	 *
	 * @deprecated use dbHelper::formatBool() 
	 * @param Bool $theValue
	 * @return String
	 */
	public static function fromPHPtoStringBool($theValue)
	{
	    return fromPGtoStringBool($theValue);
	}
	
	/**
	 * Replace special XML characters in a string.
	 * Nasty kludge, use escapeXMLChars() instead
	 * 
	 * @deprecated Use escapeXMLChars
	 * @param String $xmlrequest
	 * @return String
	 */
	public static function xmlSpecialCharReplace($xmlrequest)
	{
	    // Seriously kludgey fix to swap out special chars
	    $findStr = "operator=\">\"";
	    $replaceStr = "operator=\"&gt;\"";
	    $xmlrequest = str_replace($findStr, $replaceStr, $xmlrequest);
	    $findStr = "operator=\"<\"";
	    $replaceStr = "operator=\"&lt;\"";
	    $xmlrequest = str_replace($findStr, $replaceStr, $xmlrequest);
	    return $xmlrequest;
	
	}
	
	/**
	 * Get the resource link XML tag
	 *
	 * @param String $object - type of database object
	 * @param String $id - object identifier
	 * @param String $format - either map or xml.  Defaults to xml.
	 * @return String
	 */
	public static function getResourceLinkTag($object, $id, $format="xml")
	{
	    global $domain;
	    global $wspage;
	    global $mspage;
	
	    if ($format=="map")
	    {
	        return "<link type=\"map/html\" url=\"".escapeXMLChars("https://$domain"."/$mspage?object=$object&id=$id")."\" />\n";
	    }
	    else
	    { 
	        return "<link type=\"corina/xml\" object=\"$object\" id=\"$id\" url=\"https://$domain"."/$wspage\" />\n";
	    }
	
	}
	
	
	public static function pgDateTimeToCompliantISO($datetime)
	{
		return date('c', strtotime($datetime));
	}
	
}

?>
