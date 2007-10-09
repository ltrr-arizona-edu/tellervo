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
$myMetaHeader = new meta("Update");
$myMetaHeader->setUser("Guest", "", "");

// Extract parameters from request and ensure no SQL has been injected
$theID = (int) $_GET['id'];
$theNote = addslashes($_GET['note']);
$theIsStandard = fromStringToPHPBool($_GET['isstandard']);


// Check for required parameters
if($theNote == NULL)
{
    $myMetaHeader->setMessage("902", "Missing parameter - 'note' field is required.");
}
if($theID == NULL)
{
    $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
}

// Check data types of parameters
if((gettype($theIsStandard)!="boolean") && ($theIsStandard!=NULL))
{
    $myMetaHeader->setMessage("901", "Invalid parameter - 'isstandard' must be a boolean.");
}
if(!(gettype($theID)=="integer") && !($theID))
{
    $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
}

// **********************
// Build Data XML section
// **********************
$xmldata ="<data>\n";

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create siteNote object and set existing parameter values
    $mySiteNote = new siteNote();
    $mySiteNote->setParamsFromDB($theID);

    // Update parameters as requested
    if ($theNote) $mySiteNote->setNote($theNote);
    if ($theIsStandard) $mySiteNote->setIsStandard($theIsStandard);
    
    // Write to database and return as XML
    $mySiteNote->writeToDB();
    $xmldata.=$mySiteNote->asXML();

}    
$xmldata.="</data>\n";

// Output the resulting XML
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo $xmldata;
echo "</corina>";
?>
