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
$myMetaHeader = new meta("Read");
$myMetaHeader->setUser("Guest", "", "");

// Extract parameters from request and ensure no SQL has been injected
$theID = (int) $_GET['id'];

// Check data types of parameters
if(!(gettype($theID)=="integer") && !($theID==NULL))
{
    $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be an integer.");
}
elseif(!($theID>0) && !($theID==NULL))
{
    $myMetaHeader->setMessage("901", "Invalid parameter - 'id' field must be a valid positive integer.");
}

// **********************
// Build Data XML section
// **********************

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        
        // Build SQL depending on parameters
        if($theID==NULL)
        {
           $sql="select * from tlkpsitenote order by sitenoteid";
        }
        else
        {
           $sql="select * from tlkpsitenote where sitenoteid=$theID order by sitenoteid";
        }

        if($sql)
        {
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySiteNote = new siteNote();
                $success = $mySiteNote->setParamsFromDB($row['sitenoteid']);

                if($success)
                {
                    $xmldata.=$mySiteNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySiteNote->getLastErrorCode, $mySiteNote->getLastErrorMessage);
                }
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


// Output the resulting XML
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<data>\n";
echo "<siteNoteDictionary>\n";
echo $xmldata;
echo "</siteNoteDictionary>\n";
echo "</data>\n";
echo "</corina>";
?>
