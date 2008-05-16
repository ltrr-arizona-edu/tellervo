<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************


function dateFudge($theDate)
{
    // Converts PG ISO 8601 date into a slightly different form of ISO 8601 date so that it vaidates in XML schema.
    // Adds T between date and time
    // Adds :00 to timezone.  Super fudgy as will bomb if a non hourly timezone is used e.g. +13:45 - Chatham Is.

    return str_replace(" ", "T", $theDate).":00";


}

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

function fromPHPtoStringBool($theValue)
{
    return fromPGtoStringBool($theValue);
}

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

?>
