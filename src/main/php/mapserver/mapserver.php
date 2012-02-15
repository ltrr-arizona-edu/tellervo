<?php
//*******************************************************************
////// PHP Tellervo Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

// This script is a wrapper to the Map server CGI.  It enables PHP to authenticate requests.
// Map server really wasn't ever meant to deal with authentication so a lot of what follows
// is quite kludgy and inelegant.

header('Content-Type: application/xhtml+xml; charset=utf-8');

// Include files
include('/var/www/webservice/inc/dbsetup.php');

// Parameters
$expiryTime = "1 hour"; // In PG interval format
$tmpFolder = "/tmp";
$mapFileTemplate = "/var/www/webservice/mapserver/mapfiles/template.map";



// Compile all the GET parameters into a request string
$requestParameters="";
foreach($_GET as $requestParam => $requestVariable)
{
    $requestParameters.="&".$requestParam."=".$requestVariable;
}

// Check for IP address that have been authenticated in the last hour
$sql = "select securityuserid from tbliptracking where ipaddr='".$_SERVER['REMOTE_ADDR']."' and timestamp>(now()- interval '$expiryTime')";
pg_send_query($dbconn, $sql);
$result = pg_get_result($dbconn);
if(pg_num_rows($result)==0)
{
    // No records match the id specified
    echo "<error>Invalid IP address</error>";
}
else
{
    // Get user ID from IP address
    $row = pg_fetch_array($result);
    $userid = $row['securityuserid'];

    // Create a random file name and db table names 
    $tmpMapFileName = $tmpFolder."/mps".mt_rand().".map";
    $tmpSiteTableName = "site".substr($tmpMapFileName, 8, -4);
    $tmpTreeTableName = "tree".substr($tmpMapFileName, 8, -4);
    $tmpMeasurementTableName = "measurement".substr($tmpMapFileName, 8, -4);

    // Load template map file and change parameters for this specific user
    $templateMapFile = file_get_contents($mapFileTemplate);
    $templateMapFile = str_replace("%%SECURITYUSERID%%", $userid, $templateMapFile);
    $templateMapFile = str_replace("%%SITETEMPTABLE%%", $tmpSiteTableName, $templateMapFile);
    $templateMapFile = str_replace("%%TREETEMPTABLE%%", $tmpTreeTableName, $templateMapFile);
    $templateMapFile = str_replace("%%MEASUREMENTTEMPTABLE%%", $tmpMeasurementTableName, $templateMapFile);
    $templateMapFile = str_replace("%%CONNSTRING%%", $conn_string, $templateMapFile);

    // Create temporary map file based on template
    $tmpMapFileHandle = fopen($tmpMapFileName, "w");
    fwrite($tmpMapFileHandle, $templateMapFile);
    fclose($tmpMapFileHandle);
    
    // Create temporary table of data
    $sql = "SELECT * INTO TABLE ".$tmpSiteTableName." FROM cpgdb.mpssite($userid)"; 
    pg_send_query($dbconn, $sql);
    $result = pg_get_result($dbconn);
    $sql = "SELECT * INTO TABLE ".$tmpMeasurementTableName." FROM cpgdb.mpsvmeasurementmetacache($userid)"; 
    pg_send_query($dbconn, $sql);
    $result = pg_get_result($dbconn);
    
    if($result)
    {
        // Building request URL, collect XML and return to user
        $requestURL="http://dendro.cornell.edu/cgi-bin/mapserv?map=".$tmpMapFileName.$requestParameters;
        $xml = simplexml_load_file($requestURL);
        echo $xml->asXML();
    }
    else
    {
        echo "<error>Something went wrong when extracting your data</error>";
    }

    // Delete temporaqry map file
    unlink($tmpMapFileName);

    // Drop temporary db tables;
    $sql = "drop table $tmpSiteTableName; drop table $tmpMeasurementTableName";
    pg_send_query($dbconn, $sql);
}


?>

