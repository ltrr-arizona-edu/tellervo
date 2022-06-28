<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

// Access user and password from a file not in svn or web accessible!
global $cdbCredentialsFile;
global $dbName;
global $hostname;
global $pgport;

$contents = str_replace("\n", "=", file_get_contents($cdbCredentialsFile));
$myarray = explode("=", $contents, 5);
$username = $myarray[1];
$password = $myarray[3];

//Set up database connection
$conn_string = "dbname=$dbName user=".$username." password=".$password;
//$conn_string = "host=$hostname port=$pgport dbname=$dbName user=".$username." password=".$password;
$dbconn = pg_connect ($conn_string);

//Date format
$sql = "set datestyle to 'ISO'";
pg_query($dbconn, $sql);

?>
