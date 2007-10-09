<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

require_once("../config.php");
require_once("../inc/dbsetup.php");
require_once("../inc/meta.php");
require_once("../inc/siteNote.php");

header('Content-Type: application/xhtml+xml; charset=utf-8');

// Set up Meta Class
$myMetaHeader = new meta("Create");
$myMetaHeader->setUser("Guest", "", "");

// Extract parameters from request and ensure no SQL has been injected
$theNote = addslashes($_GET['note']);
$theIsStandard = fromStringToPHPBool($_GET['isstandard']);

// Set parameter default values
if($theIsStandard == NULL) $theIsStandard = FALSE;


// Check required parameters
if($theNote == NULL)
{
    $myMetaHeader->setMessage("902", "Missing parameter - 'note' field is required.");
}

// Check data types of parameters
if(!(gettype($theIsStandard)=="boolean"))
{
    $myMetaHeader->setMessage("901", "Invalid parameter - 'isstandard' must be a boolean.");
}


// **********************
// Build Data XML section
// **********************
$xmldata ="<data>\n";

//Only attempt to write to DB if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create siteNote object
    $mySiteNote = new siteNote();

    // Set parameter values
    if ($theNote) $mySiteNote->setNote($theNote);
    if ($theIsStandard) $mySiteNote->setIsStandard($theIsStandard);

    // Write to Database
    $success = $mySiteNote->writeToDB();

    if($success)
    {
        $xmldata.=$mySiteNote->asXML();
    }
    else
    {
        $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
    }    
}

$xmldata.="</data>\n";


// Output the resulting XML
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo $xmldata;
echo "</corina>";
?>
