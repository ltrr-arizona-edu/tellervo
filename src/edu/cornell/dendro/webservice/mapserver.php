<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

// This script is a wrapper to the Map server CGI.  It enables PHP to authenticate requests.

header('Content-Type: application/xhtml+xml; charset=utf-8');

// Compile all the GET parameters into a request string
$requestParameters="";
foreach($_GET as $requestParam => $requestVariable)
{
    $requestParameters.="&".$requestParam."=".$requestVariable;
}

// Do authentication checks
include('/var/www/webservice/inc/dbsetup.php');

// Check for IP address that have been authenticated in the last hour
$sql = "select * from tbliptracking where ipaddr='".$_SERVER['REMOTE_ADDR']."' and timestamp>(now()- interval '1 hour')";
pg_send_query($dbconn, $sql);
$result = pg_get_result($dbconn);
if(pg_num_rows($result)==0)
{
    // No records match the id specified
    echo "<error>Invalid IP address</error>";
}
else
{
    // Building request URL, collect XML and return to user
    $requestURL="http://dendro.cornell.edu/cgi-bin/mapserv?map=/var/www/phpmapserver/mapfiles/mymap.map".$requestParameters;
    $xml = simplexml_load_file($requestURL);
    echo $xml->asXML();
}



?>

