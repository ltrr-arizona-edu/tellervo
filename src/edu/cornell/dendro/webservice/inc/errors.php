<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

// Remove standard PHP error reporting
//error_reporting(0);

// Set up our own error handling
$old_error_handler = set_error_handler("userErrorHandler");

function userErrorHandler($errno, $errmsg, $filename, $linenum, $vars) 
{
    global $myMetaHeader;

    $errortype = array(
                    E_ERROR              => 'Error',
                    E_WARNING            => 'Warning',
                    E_PARSE              => 'Parsing Error',
                    E_NOTICE             => 'Notice',
                    E_CORE_ERROR         => 'Core Error',
                    E_CORE_WARNING       => 'Core Warning',
                    E_COMPILE_ERROR      => 'Compile Error',
                    E_COMPILE_WARNING    => 'Compile Warning',
                    E_USER_ERROR         => 'User Error',
                    E_USER_WARNING       => 'User Warning',
                    E_USER_NOTICE        => 'User Notice',
                    E_STRICT             => 'Runtime Notice',
                    E_RECOVERABLE_ERROR  => 'Catchable Fatal Error'
                );

    $phperrors = array(E_ERROR, E_PARSE, E_CORE_ERROR, E_COMPILE_ERROR);
    $phpwarnings = array(E_WARNING, E_CORE_WARNING);
    $usererrors = array(E_USER_ERROR, E_USER_NOTICE);
    $userwarnings = array(E_USER_WARNING);

    // Generic PHP errors
    if (in_array($errno, $phperrors))
    {
        $myMetaHeader->setMessage($errno, "PHP ".$errortype[$errno]." - ".$errmsg.". See line $linenum in file $filename", "Warning");
    }
    elseif (in_array($errno, $phpwarnings))
    {
        $myMetaHeader->setMessage($errno, "PHP ".$errortype[$errno]." - ".$errmsg.". See line $linenum in file $filename ", "Warning");
    }

    // Corina specific errors
    elseif (in_array($errno, $usererrors))
    {
        $errno = (int) substr($errmsg, 0, 3);
        $errmsg = substr($errmsg, 3);
        $myMetaHeader->setMessage($errno, $errmsg, "Error");
    }
    elseif (in_array($errno, $userwarnings))
    {
        $errno = (int) substr($errmsg, 0, 3);
        $errmsg = substr($errmsg, 3);
        $myMetaHeader->setMessage($errno, $errmsg, "Warning");
    }
    
    // Other unusual PHP errors
    else 
    {
        $myMetaHeader->setMessage("P".$errno, "PHP ".$errortype[$errno]." - ".$errmsg.". \n See line $linenum \n in file $filename", "Warning");
    }
    
}

?>
