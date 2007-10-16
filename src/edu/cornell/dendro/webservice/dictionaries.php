<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
header('Content-Type: application/xhtml+xml; charset=utf-8');

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/meta.php");
require_once("inc/dictionaries.php");
require_once("inc/siteNote.php");
require_once("inc/treeNote.php");
require_once("inc/readingNote.php");
require_once("inc/vMeasurementNote.php");
require_once("inc/specimenType.php");
require_once("inc/terminalRing.php");
require_once("inc/specimenQuality.php");
require_once("inc/specimenContinuity.php");
require_once("inc/pith.php");

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));

switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        break;
    
    default:
        $myMetaHeader = new meta("help");
        $myMetaHeader->setUser("Guest", "", "");
        // Output the resulting XML
        echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        echo "<corina>\n";
        echo $myMetaHeader->asXML();
        echo "<help> Details of how to use this web service will be added here later! In the mean time try access https:/dendro.cornell.edu/dictionaries.php?mode=read </help>";
        echo "</corina>\n";
        die;
}

$myMetaHeader->setUser("Guest", "", "");

$myXML="";

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{

    global $dbconn;
    
    // Specimen Type 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpspecimentype order by specimentypeid";

        if($sql)
        {
            $myDummySpecimenType = new specimenType();
            $myXML.=$myDummySpecimenType->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySpecimenType = new specimenType();
                $success = $mySpecimenType->setParamsFromDB($row['specimentypeid']);

                if($success)
                {
                    $myXML.=$mySpecimenType->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
                }
            }
            $myXML.=$myDummySpecimenType->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    // Terminal Ring 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpterminalring order by terminalringid";

        if($sql)
        {
            $myDummyTerminalRing = new terminalRing();
            $myXML.=$myDummyTerminalRing->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myTerminalRing = new terminalRing();
                $success = $myTerminalRing->setParamsFromDB($row['terminalringid']);

                if($success)
                {
                    $myXML.=$myTerminalRing->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myTerminalRing->getLastErrorCode(), $myTerminalRing->getLastErrorMessage());
                }
            }
            $myXML.=$myDummyTerminalRing->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    
    // Specimen Quality 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpspecimenquality order by specimenqualityid";

        if($sql)
        {
            $myDummySpecimenQuality = new specimenQuality();
            $myXML.=$myDummySpecimenQuality->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySpecimenQuality = new specimenQuality();
                $success = $mySpecimenQuality->setParamsFromDB($row['specimenqualityid']);

                if($success)
                {
                    $myXML.=$mySpecimenQuality->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySpecimenQuality->getLastErrorCode(), $mySpecimenQuality->getLastErrorMessage());
                }
            }
            $myXML.=$myDummySpecimenQuality->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }

    // Pith 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkppith order by pithid";

        if($sql)
        {
            $myDummyPith = new pith();
            $myXML.=$myDummyPith->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myPith = new pith();
                $success = $myPith->setParamsFromDB($row['pithid']);

                if($success)
                {
                    $myXML.=$myPith->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myPith->getLastErrorCode(), $myPith->getLastErrorMessage());
                }
            }
            $myXML.=$myDummyPith->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    // Specimen Continuity 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpspecimencontinuity order by specimencontinuityid";

        if($sql)
        {
            $myDummySpecimenContinuity = new specimenContinuity();
            $myXML.=$myDummySpecimenContinuity->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySpecimenContinuity = new specimenContinuity();
                $success = $mySpecimenContinuity->setParamsFromDB($row['specimencontinuityid']);

                if($success)
                {
                    $myXML.=$mySpecimenContinuity->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySpecimenContinuity->getLastErrorCode(), $mySpecimenContinuity->getLastErrorMessage());
                }
            }
            $myXML.=$myDummySpecimenContinuity->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    // Site note 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpsitenote where isstandard=true order by sitenoteid";

        if($sql)
        {
            $myDummySiteNote = new siteNote();
            $myXML.=$myDummySiteNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySiteNote = new siteNote();
                $success = $mySiteNote->setParamsFromDB($row['sitenoteid']);

                if($success)
                {
                    $myXML.=$mySiteNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
                }
            }
            $myXML.=$myDummySiteNote->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    // Tree note 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkptreenote where isstandard=true order by treenoteid";

        if($sql)
        {
            $myDummyTreeNote = new treeNote();
            $myXML.=$myDummyTreeNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myTreeNote = new treeNote();
                $success = $myTreeNote->setParamsFromDB($row['treenoteid']);

                if($success)
                {
                    $myXML.=$myTreeNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myTreeNote->getLastErrorCode(), $myTreeNote->getLastErrorMessage());
                }
            }
            $myXML.=$myDummyTreeNote->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    // VMeasurement note 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpvmeasurementnote where isstandard=true order by vmeasurementnoteid";

        if($sql)
        {
            $myDummyVMeasurementNote = new vmeasurementNote();
            $myXML.=$myDummyVMeasurementNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myVMeasurementNote = new vmeasurementNote();
                $success = $myVMeasurementNote->setParamsFromDB($row['vmeasurementnoteid']);

                if($success)
                {
                    $myXML.=$myVMeasurementNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myVMeasurementNote->getLastErrorCode(), $myVMeasurementNote->getLastErrorMessage());
                }
            }
            $myXML.=$myDummyVMeasurementNote->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    
    // Reading note 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpreadingnote where isstandard=true order by readingnoteid";

        if($sql)
        {
            $myDummyReadingNote = new readingNote();
            $myXML.=$myDummyReadingNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myReadingNote = new readingNote();
                $success = $myReadingNote->setParamsFromDB($row['readingnoteid']);

                if($success)
                {
                    $myXML.=$myReadingNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myReadingNote->getLastErrorCode(), $myReadingNote->getLastErrorMessage());
                }
            }
            $myXML.=$myDummyReadingNote->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
}

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<data>\n";
echo $myXML;
echo "</data>\n";
echo "</corina>\n";
?>
