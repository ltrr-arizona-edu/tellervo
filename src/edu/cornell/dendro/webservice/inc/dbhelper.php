<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************


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
