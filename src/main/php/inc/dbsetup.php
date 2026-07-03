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

$credentials = array();
foreach(file($cdbCredentialsFile, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES) as $line)
{
  $parts = explode("=", $line, 2);
  if(count($parts)==2)
  {
    $credentials[$parts[0]] = $parts[1];
  }
}
$username = isset($credentials["username"]) ? $credentials["username"] : "";
$password = isset($credentials["password"]) ? $credentials["password"] : "";

//Set up database connection
$conn_string = "host=$hostname port=$pgport dbname=$dbName user=".$username." password=".$password;
$dbconn = pg_connect ($conn_string);
if($dbconn===FALSE)
{
  die("Unable to connect to PostgreSQL database using configured credentials.\n");
}

//Date format
$sql = "set datestyle to 'ISO'";
pg_query($dbconn, $sql);

?>
