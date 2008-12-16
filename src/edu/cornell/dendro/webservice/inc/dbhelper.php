<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

/**
 * Converts PG ISO 8601 date into a slightly different form of ISO 8601 date so that it vaidates in XML schema.
 * Adds T between date and time
 * Adds :00 to timezone.  Super fudgy as will bomb if a non hourly timezone is used e.g. +13:45 - Chatham Is.
 *
 * @param Date $theDate
 * @return String
 */
function dateFudge($theDate)
{
    return str_replace(" ", "T", $theDate).":00";
}

/**
 * Get the timestamp when the specified table was last updated
 *
 * @param String $tablename
 * @return Timestamp
 */
function getLastUpdateDate($tablename)
{
    global $dbconn;
    $sql = "select max(lastmodifiedtimestamp) as max from $tablename";
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
 * Converts from most English, PHP or PG representations of a boolean into a PHP, English or PG boolean.
 * Default output format is PHP
 *
 * @param String $value
 * @param String $format
 */
function magicBool($value, $format='php')
{
	// Turn value into an internal boolean
    if(($value===TRUE) || (strtolower($value) = 't') || (strtolower($value) = 'true') || ($value = 1) )
    {   
        $outputvalue = TRUE;
    }
    else
    {
        $outputvalue = FALSE;
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
    		if($outputvalue===TRUE) return 'True';
    		return 'False';
    	default:
    		return false;
    }
	
}

/**
 * Translates PostgreSQL boolean to a PHP boolean
 *
 * @param PG Bool $theValue
 * @return PHP Bool
 */
function fromPGtoPHPBool($theValue)
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
 * @param PHP Bool $theValue
 * @return PG Bool
 */
function fromPHPtoPGBool($theValue)
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
 * @param String $theValue
 * @return Bool
 */
function fromStringtoPHPBool($theValue)
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
 * @param PG Bool $theValue
 * @return String
 */
function fromPGtoStringBool($theValue)
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
 * @param Bool $theValue
 * @return String
 */
function fromPHPtoStringBool($theValue)
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
function xmlSpecialCharReplace($xmlrequest)
{
    // Seriously kludge fix to swap out special chars
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
function getResourceLinkTag($object, $id, $format="xml")
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

/**
 * Replace all special XML characters in a string
 *
 * @param String $theString
 * @return String
 */
function escapeXMLChars($theString)
{
    $theString = str_replace('&', '&amp;',  $theString);
    $theString = str_replace("'", '&apos;', $theString);
    $theString = str_replace('"', '&quot;', $theString);
    $theString = str_replace('<', '&lt;',   $theString);
    $theString = str_replace('>', '&gt;',   $theString);
    return $theString;
}
?>
