#!/usr/bin/php
<?php 
if (!isset($argc))
{
	echo "This file should be called from the command line only";
	die();
	
}

// Help text
if ($argc != 2 || in_array($argv[1], array('--help', '-help', '-h', '-?'))) {
	?>
		
This is a command line PHP script for extracting site location data from a Tellervo v1.1 site database file.

  Usage:
  <?php echo $argv[0]; ?> <infile>

  <infile> if the Tellervo sitedb file that you want to process
  
  With the --help, -help, -h,
  or -? options, you can get this help.

<?php
	die();
}

// Put args into nice variables
$dbName = "gps";
$table = $argv[1];

// Postgres credentials file location
$cdbCredentialsFile = "/home/aps03pwb/.tellervo_server_credentials";


$contents = str_replace("\n", "=", file_get_contents($cdbCredentialsFile));
$myarray = explode("=", $contents, 5);
$username = $myarray[1];
$password = $myarray[3];
$firstRun = true;

//Set up database connection
$conn_string = "host=dendro.cornell.edu port=5432 dbname=$dbName user=".$username." password=".$password;
$conn_string2 = "host=dendro.cornell.edu port=5432 dbname=tellervo_dev user=".$username." password=".$password;
$dbconn = pg_connect ($conn_string);
$dbconn2 = pg_connect ($conn_string2);



//Date format
$sql = "set datestyle to 'ISO'";
pg_query($dbconn, $sql);


// On first run create tables to store failed imports for later consideration
if($firstRun)
{
	$droptblsql = "drop table gpsimporterror;";
	$tblsql = "CREATE TABLE gpsimporterror
	(
	  id integer,
	  sitename character varying(50),
	  elementname character varying(5)
	)
	WITH (OIDS=FALSE);
	ALTER TABLE gpsimporterror OWNER TO webuser;";

	
}

pg_query($dbconn, $droptblsql);
$oldres = pg_get_result($dbconn);
pg_query($dbconn, $tblsql);
$oldres = pg_get_result($dbconn);


$sql = "SELECT name, geom as the_geom from $table where import='t' order by name asc";

          
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_query($dbconn, $sql);
  
            while ($row = pg_fetch_array($result))
            {
            	
            	$objcode = getSiteFromString($row['name']);
            	$elemcode = getElementFromString($row['name']);
            	
            	$objsql = "select * from tblobject where code='$objcode'";
            	$elemsql = "select * from tblelement where code='$elemcode'";
            	
            	
            	$objresult = pg_query($dbconn2, $objsql);
            	$objrows= pg_num_rows($objresult);
            	
            	$elemresult = pg_query($dbconn2, $elemsql);
            	$elemrows = pg_num_rows($elemresult);
            	
            	if($objrows<1) echo "The object '$objcode' for '".$row['name']."' was not found\n"; continue;
            	if($elemrows<1) echo "$objcode-$elemcode not found\n"; continue;        	
            	
            
            	//echo "String = ".$row['name']. ", Site = ".getSiteFromString($row['name']).", Element = ".getElementFromString($row['name']).",  $count \n";
            	$sql= "update tblelement set locationgeometry='".$row['the_geom']."' where elementid=("
            	."select elementid from tblelement, tblobject " 
            	."where tblelement.objectid=tblobject.objectid and " 
            	."tblobject.code='".getSiteFromString($row['name'])."' and "
            	."tblelement.code='".getElementFromString($row['name'])."');\n";
            	
            	
                // Run SQL 
                pg_send_query($dbconn2, $sql);
                $result2 = pg_get_result($dbconn2);
                if(pg_result_error_field($result2, PGSQL_DIAG_SQLSTATE))
                {
                    $PHPErrorCode = pg_result_error_field($result2, PGSQL_DIAG_SQLSTATE);
       				echo "problems: ". pg_result_error($result2);

                }
            	else
            	{
   
            		echo "insert ok \n";
            	}
            	
            	
            	
            }
        }
        else
        {
			echo "Error connecting to DB ";
			die();
        }



function getSiteFromString($string)
{
      $pattern = "/^[a-zA-Z]{4}/";
      if (preg_match($pattern,$string)) 
      {
      	return substr($string, 0, 4);
      }
      
      $pattern = "/^[a-zA-Z]{3}/";
      if (preg_match($pattern,$string)) 
      {
      	return substr($string, 0, 3);
      }
      else
      {
      	return "error"; 
      }
     
	
}

function getElementFromString($string)
{
      
      return substr($string, strlen(getSiteFromString($string)), 6);	

      
}
?>

