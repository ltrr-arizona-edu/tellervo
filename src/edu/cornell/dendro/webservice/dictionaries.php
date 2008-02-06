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

require_once("inc/dbsetup.php");
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/errors.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

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

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new request($myMetaHeader, $myAuth);

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}

// **************
// GET PARAMETERS
// **************
if(isset($_POST['xmlrequest']))
{
    // Extract parameters from XML request POST
    $myRequest->getXMLParams();
}
else
{
    // Extract parameters from get request and ensure no SQL has been injected
    $myRequest->getGetParams();
}

// ****************
// CHECK PARAMETERS 
// ****************

switch($myRequest->mode)
{
    case "read":
        $myMetaHeader->setRequestType("read");
        if($myAuth->isLoggedIn())
        {
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }
    
    case "failed":
        $myMetaHeader->setRequestType("help");

    default:
        $myMetaHeader->setRequestType("help");
        // Output the resulting XML
        $xmldata ="Details of how to use this web service will be added here later!";
        writeHelpOutput($myMetaHeader,$xmldata);
        die;
}


// *************
// PERFORM QUERY
// *************
$xmldata = "";
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
            $xmldata.=$myDummySpecimenType->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySpecimenType = new specimenType();
                $success = $mySpecimenType->setParamsFromDB($row['specimentypeid']);

                if($success)
                {
                    $xmldata.=$mySpecimenType->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummySpecimenType->getParentTagEnd();
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
            $xmldata.=$myDummyTerminalRing->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myTerminalRing = new terminalRing();
                $success = $myTerminalRing->setParamsFromDB($row['terminalringid']);

                if($success)
                {
                    $xmldata.=$myTerminalRing->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myTerminalRing->getLastErrorCode(), $myTerminalRing->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummyTerminalRing->getParentTagEnd();
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
            $xmldata.=$myDummySpecimenQuality->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySpecimenQuality = new specimenQuality();
                $success = $mySpecimenQuality->setParamsFromDB($row['specimenqualityid']);

                if($success)
                {
                    $xmldata.=$mySpecimenQuality->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySpecimenQuality->getLastErrorCode(), $mySpecimenQuality->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummySpecimenQuality->getParentTagEnd();
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
            $xmldata.=$myDummyPith->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myPith = new pith();
                $success = $myPith->setParamsFromDB($row['pithid']);

                if($success)
                {
                    $xmldata.=$myPith->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myPith->getLastErrorCode(), $myPith->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummyPith->getParentTagEnd();
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
            $xmldata.=$myDummySpecimenContinuity->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySpecimenContinuity = new specimenContinuity();
                $success = $mySpecimenContinuity->setParamsFromDB($row['specimencontinuityid']);

                if($success)
                {
                    $xmldata.=$mySpecimenContinuity->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySpecimenContinuity->getLastErrorCode(), $mySpecimenContinuity->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummySpecimenContinuity->getParentTagEnd();
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
            $xmldata.=$myDummySiteNote->getParentTagBegin();
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
                    $myMetaHeader->setMessage($mySiteNote->getLastErrorCode(), $mySiteNote->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummySiteNote->getParentTagEnd();
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
            $xmldata.=$myDummyTreeNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myTreeNote = new treeNote();
                $success = $myTreeNote->setParamsFromDB($row['treenoteid']);

                if($success)
                {
                    $xmldata.=$myTreeNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myTreeNote->getLastErrorCode(), $myTreeNote->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummyTreeNote->getParentTagEnd();
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
            $xmldata.=$myDummyVMeasurementNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myVMeasurementNote = new vmeasurementNote();
                $success = $myVMeasurementNote->setParamsFromDB($row['vmeasurementnoteid']);

                if($success)
                {
                    $xmldata.=$myVMeasurementNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myVMeasurementNote->getLastErrorCode(), $myVMeasurementNote->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummyVMeasurementNote->getParentTagEnd();
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
            $xmldata.=$myDummyReadingNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $myReadingNote = new readingNote();
                $success = $myReadingNote->setParamsFromDB($row['readingnoteid']);

                if($success)
                {
                    $xmldata.=$myReadingNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($myReadingNote->getLastErrorCode(), $myReadingNote->getLastErrorMessage());
                }
            }
            $xmldata.=$myDummyReadingNote->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
}


// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata);
