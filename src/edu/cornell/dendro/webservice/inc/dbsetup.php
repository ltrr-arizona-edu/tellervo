<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This file contains the interface and classes that store data 
 * representing the various data entities in the data model.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package DatabaseIO
 * *******************************************************************
 */

// Access user and password from a file not in svn or web accessible!
global $cdbCredentialsFile;
global $dbName;
$contents = str_replace("\n", "=", file_get_contents($cdbCredentialsFile));
$myarray = explode("=", $contents, 5);
$username = $myarray[1];
$password = $myarray[3];

//Set up database connection
$conn_string = "host=dendro.cornell.edu port=5432 dbname=$dbName user=".$username." password=".$password;
$dbconn = pg_connect ($conn_string);

//Date format
$sql = "set datestyle to 'ISO'";
pg_query($dbconn, $sql);

?>
