<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

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
            return $row['max'];
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
    if($theValue=="t" || $theValue=="true" || $theValue=="TRUE" || $theValue=="True")
    {
        return TRUE;
    }
    elseif($theValue=="f" || $theValue=="false" || $theValue=="FALSE" || $theValue=="False")
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


?>
