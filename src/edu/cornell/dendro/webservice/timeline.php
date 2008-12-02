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
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/measurement.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new measurementRequest($myMetaHeader, $myAuth);

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}

// *************
// PERFORM QUERY
// *************

$xmldata ="";
//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create measurement object 
    $myMeasurement = new measurement();
    $xmldata = "<data>";
    
    $sql = "select vmeasurementid from tblvmeasurement where vmeasurementid < 100";
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            $myMeasurement = new measurement();
            $success = $myMeasurement->setParamsFromDB($row['vmeasurementid']);
            if($success)
            {
                $xmldata.=$myMeasurement->asTimelineXML();
            }
            else
            {
                trigger_error($myMeasurement->getLastErrorCode().$myMeasurement->getLastErrorMessage());
            }
        }
    }
    $xmldata .= "</data>";
}

// ***********
// OUTPUT DATA
// ***********
//writeOutput($myMetaHeader, $xmldata);
echo $xmldata;
