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
if(($theIsStandard == NULL) && ($theIsStandard!=FALSE))
{
    $myMetaHeader->setMessage("902", "Missing parameter - 'isstandard' field is required.");
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
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql = "update tlkpsitenote set note='$theNote', isstandard='".fromPHPtoPGBool($theIsStandard)."' where sitenoteid=$theID";

        if ($sql)
        {
            // Run SQL 
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
            {
                $myMetaHeader->setMessage("002", pg_result_error($result)."--- SQL was $sql");
            }
        }
        else
        {
            // No SQL to run
        }
    }
    else
    {
      // Connection bad
      $myMetaHeader->setMessage("001", "Error connecting to database");
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
