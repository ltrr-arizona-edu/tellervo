<?php
set_time_limit(0);

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


/**
 * This is an import script for moving a simple Access db containing 
 * Cornell field collection info into the Tellervo databaes.  It is
 * unlikely to be of any use to anyone else!
 */



require_once("config.php");
if($debugFlag===FALSE) ob_start();
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/output.php");
require_once("inc/object.php");
require_once("inc/element.php");
require_once("inc/sample.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/box.php");
require_once("inc/authenticate.php");
require_once("inc/dictionaries.php");
require_once("inc/search.php");
require_once("inc/ProgressBar.php");

$elementImportTable = "elementout";
$sampleImportTable = "sampleout";
global $firebug;
global $dbconn;
global $debugFlag;



if($debugFlag===FALSE) $bar = new ProgressBar("Import in progress...", FALSE, 0, 800, 100);
if($debugFlag===FALSE) $bar2 = new ProgressBar("Assigning existing samples to boxes", FALSE, 0, 800, 100);



$elementSuccessCount = 0;
$sampleSuccessCount = 0;
$sampleFixSuccessCount = 0;
$boxSuccessCount = 0;
$elementFailureCount = 0;
$sampleFailureCount = 0;
$sampleFixFailureCount = 0;
$boxFailureCount = 0;

$progressCount=0;

$limit = 10000;
$offset = 0;
$doBoxes = TRUE;
$firstRun = TRUE;





// On first run create tables to store failed imports for later consideration
if($firstRun)
{
	$droptblsql = "drop table elementimporterror2; drop table sampleimporterror2;";
	$tblsql = "CREATE TABLE elementimporterror2
	(
	  id integer,
	  sitecode character varying(50),
	  elementcode character varying(50),
	  error character varying,
	  fixed boolean default false
	)
	WITH (OIDS=FALSE);
	ALTER TABLE elementimporterror2 OWNER TO webuser;
	
	CREATE TABLE sampleimporterror2
	(
	  id integer,
	  sitecode character varying(50),
	  samplecode character varying(50),
	  elementcode character varying(50),
	  error character varying,
	  fixed boolean default false
	)
	WITH (OIDS=FALSE);
	ALTER TABLE sampleimporterror2 OWNER TO webuser"
	;
	
	pg_query($dbconn, $droptblsql);
	$oldres = pg_get_result($dbconn);
	pg_query($dbconn, $tblsql);
	$oldres = pg_get_result($dbconn);
}

// Create boxes
if($doBoxes)
{
	$sql = "SELECT distinct(boxname) from $sampleImportTable where boxname is not null";
	$dbconnstatus = pg_connection_status($dbconn);
	if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	{
		$result = pg_query($dbconn, $sql);
        
  		if(pg_num_rows($result)==0)
		{
			// No records match the id specified
			$firebug->log("No match", "Error");
			return FALSE;
		}
		else
		{
			// Set parameters from db
			while ($row = pg_fetch_array($result))
			{	
			
				$newBox = new box();
				
				$newBox->setTitle(strtoupper($row['boxname']));
				
				$bxsuccess = $newBox->writeToDB();
				if ($bxsuccess===FALSE)
				{
					$boxFailureCount++;
				}
				else
				{
					$boxSuccessCount++;
				}
			
			}
		}

	}
	else
	{
		// Connection bad
		echo "Db connection failed";
		die();
	}
}     

	
if($debugFlag===FALSE) $bar->initialize( $limit*2); //print the empty bar

// First Time around we do import the elements
$sql = "SELECT * from $elementImportTable order by id asc limit $limit offset $offset";
	$dbconnstatus = pg_connection_status($dbconn);
	if ($dbconnstatus===PGSQL_CONNECTION_OK)
	{
		$oldresult = pg_get_result($dbconn);
		$result = pg_query($dbconn, $sql);
        
		/**
		 *   INVENTORY ROW LOOP BEGINS
		 */
		while ($row = pg_fetch_array($result))
		{						
			$objSQL = "select objectid from tblobject where code='".$row['sitecode']."'";
			
			$oldresult = pg_get_result($dbconn);
			
			pg_send_query($dbconn, $objSQL);
       		$objresult = pg_get_result($dbconn);
           	if(pg_num_rows($result)<1)
           	{
           		if($debugFlag) $firebug->log($objSQL, "no matches for ");
           		
				$insertsql = "insert into elementimporterror2 (id, sitecode, elementcode, error)
						values(".
						"'".$row['id']."', ".
				        "'".$row['sitecode']."', ".
				        "'".$row['elementcode']."', ".
						"'No object for this element in the database' ".
						")";
				pg_send_query($dbconn, $insertsql);	
				if($debugFlag) $firebug->log($thisElement->getID(), "Failed to write this element to db");
				$elementFailureCount++;	           		
           		continue;
           	}

            
			/**
			 * OBJECT LOOP BEGINS
			 */
            while ($objrow = pg_fetch_array($objresult))
            {
				$theObject = new tobject();
				$theObject->setParamsFromDB($objrow['objectid']);
				
					
				$thisElement = new element();					
				// Set parent object
				array_push($thisElement->parentEntityArray,$theObject);	

				//$firebug->log($theObject->getCode()."-".$thisElement->getTitle(), "Doing element");
				
				// Set title 
				$thisElement->setTitle($row['elementcode']);
				// Set Type to unknown
				$thisElement->setType(null, "Unknown");				
				// Set taxon
				$thisElement->taxon->setParamsFromCoL($row['taxonid']);		
				$thisElement->setDescription($row['description']);
				if($row['altitude']>0) $thisElement->setAltitude($row['altitude']);
				if($row['slopeangle']>0) $thisElement->setSlopeAngle($row['slopeangle']);
				if($row['slopeazimuth']>0) $thisElement->setSlopeAzimuth($row['slopeazimuth']);
				$thisElement->setSoilDescription($row['soildescription']);
				if($row['soildepth']>0) $thisElement->setSoilDepth($row['soildepth']);
				$thisElement->setBedrockDescription($row['bedrock']);
				
				
				// Write element to db
				$success = $thisElement->writeToDB();
				
				if ($success!=TRUE)
				{
					$insertsql = "insert into elementimporterror2 (id, sitecode, elementcode, error)
							values(".
							"'".$row['id']."', ".
					        "'".$theObject->getCode()."', ".
					        "'".$row['elementcode']."', ".
							"'".addslashes($thisElement->getLastErrorMessage())."' ".
							")";
					pg_send_query($dbconn, $insertsql);	
					if($debugFlag) $firebug->log($thisElement->getID(), "Failed to write this element to db");
					$elementFailureCount++;	
				}		
				else
				{
					$elementSuccessCount++;
				}

			// End object loop
			}	
			if($debugFlag) $progressCount++;
			if($debugFlag) printSummaryTable();
			if($debugFlag===FALSE) $bar->increase();
			
		// End inventory row loop	
		}

	}
	else
	{
		// Connection bad
		if($debugFlag) $firebug->log("Error connecting to database", "Error");
	}


	
// SECOND TIME AROUND! 
// Now we do all the samples

	$sql = "SELECT * from $sampleImportTable order by id asc limit $limit offset $offset";

	
	$dbconnstatus = pg_connection_status($dbconn);
	if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	{
		pg_get_result($dbconn);
				pg_get_result($dbconn);
		
						pg_get_result($dbconn);
				
		$result = pg_query($dbconn, $sql);
        
		/**
		 *   INVENTORY ROW LOOP BEGINS
		 */
		while ($row = pg_fetch_array($result))
		{	
           				
			$oldres = pg_get_result($dbconn);
			$elemSQL = "select tblelement.elementid, tblobject.objectid 
			from tblelement, tblobject where tblelement.code='".$row['elementcode']."' 
			and tblobject.code='".$row['sitecode']."' and tblelement.objectid=tblobject.objectid";		
			
			pg_send_query($dbconn, $elemSQL);
       		$elemresult = pg_get_result($dbconn);
           	if(pg_num_rows($result)<1)
           	{
           		if($debugFlag) $firebug->log($elemSQL, "no matches for ");
           		continue;
           	}

   			$theElement = new element();			
           	$thisSample = new sample();
           	$success3 = false;
           	
            while ($elemrow = pg_fetch_array($elemresult))
            {

				$theElement->setParamsFromDB($elemrow['elementid']);				
            
			

				array_push($thisSample->parentEntityArray,$theElement);
				$thisSample->setTitle($row['samplecode']);
				// Set Type to unknown
				$thisSample->setType(null, $row['type']);
				
				if($row['boxname']!=null) 
				{
					$foundBox = $thisSample->setBoxFromName(strtoupper($row['boxname']));
					if($foundBox===FALSE) 
					{
						if($debugFlag) $firebug->log(strtoupper($row['boxname']), "Can't find box:");
					}
				}
				
				$thisSample->setSamplingDate($row['samplingdate']);
				if($row['sampleheight']>0) $thisSample->setPosition($row['sampleheight']."cm above ground level");
				$success3 = $thisSample->writeToDB();
			
            }
			
			
			
			if ($success3!=TRUE)
			{
				if($debugFlag) $firebug->log($elemSQL, "SQL used to get existing element record");
				$insertsql = "insert into sampleimporterror2 (id, sitecode, samplecode, elementcode, error)
						values(".
						"'".$row['id']."', ".
						"'".$row['sitecode']."', ".
				        "'".$row['samplecode']."', ".
				        "'".$row['elementcode']."', ".
						"'".addslashes($thisSample->getLastErrorMessage())."' ".
						")";
				pg_send_query($dbconn, $insertsql);	
				if($debugFlag) $firebug->log($thisSample->getID(), "Failed to write this sample to db");
				$sampleFailureCount++;	
			}		
			else
			{
				$sampleSuccessCount++;
			}
			
			
			// Create radius records for each
			/*$thisRadius = new radius();
			array_push($thisRadius->parentEntityArray,$thisSample);
			if($row['azimuth']!=null) $thisRadius->setAzimuth($row['azimuth']);
			$thisRadius->setTitle("A");
			$success5 = $thisRadius->writeToDB();
			
			if($success5!=TRUE)
			{
				if($debugFlag) $firebug->log($thisRadius->getLastErrorMessage(), "failed to create radius");
			}*/
	
		$progressCount++;
		
		if($debugFlag===FALSE) $bar->increase();
		if($debugFlag) printSummaryTable();

				
		// End inventory row loop	
		}

	} 
	else
	{
		// Connection bad
		if($debugFlag) $firebug->log("Error connecting to database", "Error");
	}
	


printSummaryTable();	
	
function printSummaryTable()
{	
	global $elementSuccessCount;
	global $sampleSuccessCount;
	global $sampleFixSuccessCount;
	global $boxSuccessCount;
	global $elementFailureCount;
	global $sampleFailureCount;
	global $sampleFixFailureCount;
	global $boxFailureCount;
	global $progressCount;
	global $limit;
	global $debugFlag;
	
	echo "</div></div><div><br><br><br><br><h1>Summary of import</h1><table border=\"1\"><tr><td></td><td>           Success</td><td>Failure</td></tr>";
	echo "<tr><td><b>Boxes</b></td><td> $boxSuccessCount </td><td> $boxFailureCount </td></tr>";
	echo "<tr><td><b>Elements</b></td><td> $elementSuccessCount </td><td> $elementFailureCount </td></tr>";
	echo "<tr><td><b>Samples</b> </td><td>$sampleSuccessCount </td><td> $sampleFailureCount </td></tr></table><br></div>";
	
	if($debugFlag===FALSE) 	ob_flush();
	if($debugFlag===FALSE) 	flush();
	if($debugFlag===FALSE) 	ob_clean();
}
?>
</HTML>