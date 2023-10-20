<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

require_once('FirePHPCore/fb.php');

// Set up FireBug debugging
$firebug = new FB();
//$firebug->setEnabled($debugFlag);
//if($debugFlag===TRUE) ob_start();


// Remove standard PHP error reporting
if($debugFlag===TRUE)
{
    error_reporting(0);
}

// Set up our own error handling
$old_error_handler = set_error_handler("userErrorHandler");

function userErrorHandler($errno, $errmsg, $filename, $linenum) 
{
    global $myMetaHeader;
    global $firebug;
    $wrapwidth = 90;

    // Associative array for looking up friendly error names
    $errortype      = array(
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
    // Array of errors triggered by the Tellervo WS
    $usererrors     = array( 
                        E_USER_ERROR, 
                        E_USER_WARNING, 
                        E_USER_NOTICE
                    );
    // Array of internal PHP errors
    $phperrors      = array(
                        E_ERROR, 
                        E_PARSE, 
                        E_CORE_ERROR, 
                        E_COMPILE_ERROR
                    );
    // Array of internal PHP warnings
    $phpwarnings    = array(
                        E_WARNING, 
                        E_CORE_WARNING
                    );
    // Associative array that translates Tellervo error codes into PHP error types
    $tellervoErrCodes = array(
                        001 => E_USER_ERROR,
                        002 => E_USER_ERROR,
                        101 => E_USER_ERROR,
                        102 => E_USER_ERROR,
                        103 => E_USER_ERROR,
                        104 => E_USER_ERROR,
                        666 => E_USER_ERROR,
                        667 => E_USER_ERROR,
                        701 => E_USER_ERROR,
                        901 => E_USER_ERROR,
                        902 => E_USER_ERROR,
                        903 => E_USER_NOTICE,
                        904 => E_USER_ERROR,
                        905 => E_USER_ERROR,
                        906 => E_USER_ERROR,
                        907 => E_USER_ERROR,
                        908 => E_USER_ERROR,
                        909 => E_USER_ERROR,
                    );
    

    // Generic PHP errors
    if (in_array($errno, $phperrors))
    {
    	$message = "PHP ".$errortype[$errno]." - ".$errmsg.". See line $linenum in file $filename";
        $myMetaHeader->setMessage($errno, wordwrap($message, $wrapwidth), "Warning");
        $firebug->log($message, "PHP warning code $errno");
    }
    elseif (in_array($errno, $phpwarnings))
    {
    	$message = "PHP ".$errortype[$errno]." - ".$errmsg.". See line $linenum in file $filename ";
        $myMetaHeader->setMessage($errno, wordwrap($message, $wrapwidth), "Warning");
        $firebug->log($message, "PHP warning code $errno");
    }

    // Tellervo specific errors
    elseif ($errno == E_USER_ERROR)
    {
        $errno = (int) substr($errmsg, 0, 3);
        $errmsg = substr($errmsg, 3);
        $myMetaHeader->setMessage($errno, wordwrap($errmsg, $wrapwidth), "Error");
        $firebug->log($errmsg, "Error $errno");
    }
    elseif ($errno == E_USER_WARNING)
    {
        $errno = (int) substr($errmsg, 0, 3);
        $errmsg = substr($errmsg, 3);
        $myMetaHeader->setMessage($errno, wordwrap($errmsg, $wrapwidth), "Warning");
        $firebug->log($errmsg, "Warning $errno");
    }
    elseif ($errno == E_USER_NOTICE)
    {
        $errno = (int) substr($errmsg, 0, 3);
        $errmsg = substr($errmsg, 3);
        $myMetaHeader->setMessage($errno, wordwrap($errmsg, $wrapwidth), "Notice");
        $firebug->log($errmsg, "Notice $errno");
    }
    
    // Other unusual PHP errors
    else 
    {
        $message = $errmsg.". \n See line $linenum \n in file $filename";
        try{
            $etype = "Misc error";
            if(array_key_exists($errno, $errortype)){
                $etype = $errortype[$errno];
            }
            $message = "PHP ".$etype." - ".$errmsg.". \n See line $linenum \n in file $filename";
        }
        catch (Exception $ex)
        {
            
        }
    	
        $myMetaHeader->setMessage("99".$errno, wordwrap($message, $wrapwidth), "Warning");
        $firebug->log($message, "PHP warning code $errno");
    }
    
}

?>
