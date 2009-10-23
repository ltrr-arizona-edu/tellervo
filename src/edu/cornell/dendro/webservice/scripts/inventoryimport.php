<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
 * *******************************************************************
 */


/**
 * This is an import script for moving a simple Access db containing 
 * an inventory of the Cornell collection into the Corina databaes.  It is
 * unlikely to be of any use to anyone else!
 */


ob_start();
set_time_limit(0);

require_once("config.php");
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
?><HTML><?php 

$importTable = "inventory";
global $firebug;
global $dbconn;

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

$limit = 999999;
$offset = 0;
$doBoxes = TRUE;
$firstRun = TRUE;
$doThird = TRUE;




// On first run create tables to store failed imports for later consideration
if($firstRun)
{
	$droptblsql = "drop table elementimporterror; drop table sampleimporterror;";
	$tblsql = "CREATE TABLE elementimporterror
	(
	  id integer,
	  boxname character varying(50),
	  regioncode character varying(5),
	  elementstartnum integer,
	  elementendnum integer,
	  samplecode character varying(1),
	  taxa character varying(50),
	  sitecode character varying(50),
	  missing character varying(255),
	  notes text,
	  sampletype character varying(50),
	  currentelement character varying,
	  error character varying,
	  fixed boolean default false
	)
	WITH (OIDS=FALSE);
	ALTER TABLE elementimporterror OWNER TO webuser;
	
	CREATE TABLE sampleimporterror
	(
	  id integer,
	  boxname character varying(50),
	  regioncode character varying(5),
	  elementstartnum integer,
	  elementendnum integer,
	  samplecode character varying(1),
	  taxa character varying(50),
	  sitecode character varying(50),
	  missing character varying(255),
	  notes text,
	  sampletype character varying(50),
	  currentelement character varying,
	  error character varying,
	  fixed boolean default false
	)
	WITH (OIDS=FALSE);
	ALTER TABLE sampleimporterror OWNER TO webuser;
	
	CREATE TABLE sampleimportfixerror
	(
	  id integer,
	  boxname character varying(50),
	  regioncode character varying(5),
	  elementstartnum integer,
	  elementendnum integer,
	  samplecode character varying(1),
	  taxa character varying(50),
	  sitecode character varying(50),
	  missing character varying(255),
	  notes text,
	  sampletype character varying(50),
	  currentelement character varying,
	  error character varying,
	  fixed boolean default false
	)
	WITH (OIDS=FALSE);
	ALTER TABLE sampleimportfixerror OWNER TO webuser;"
	;
	
	pg_query($dbconn, $droptblsql);
	$oldres = pg_get_result($dbconn);
	pg_query($dbconn, $tblsql);
	$oldres = pg_get_result($dbconn);
	pg_query($dbconn, $tblsql);
	$oldres = pg_get_result($dbconn);
}

// Create boxes
if($doBoxes)
{
	$sql = "SELECT distinct(boxname) from $importTable";
	$dbconnstatus = pg_connection_status($dbconn);
	if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	{
		$result = pg_query($dbconn, $sql);
        
  		if(pg_num_rows($result)==0)
		{
			// No records match the id specified
			if($debugFlag) $firebug->log("No match", "Error");
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
$sql = "SELECT * from $importTable order by id asc limit $limit offset $offset";
if($debugFlag) $firebug->log($sql, "Starting first loop"); 
	$dbconnstatus = pg_connection_status($dbconn);
	if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	{
		$oldresult = pg_get_result($dbconn);
		$result = pg_query($dbconn, $sql);
        
		/**
		 *   INVENTORY ROW LOOP BEGINS
		 */
		while ($row = pg_fetch_array($result))
		{	
			$elemEnd = $row['elementendnum'];
			$elemStart = $row['elementstartnum'];
			
			if(($elemEnd-$elemStart)<0)
			{
				if($debugFlag) $firebug->log($row, "Problem with element numbers"); 
				continue;
			}
			
			$objSQL = "select objectid from tblobject where code='".$row['sitecode']."'";
			
			$oldresult = pg_get_result($dbconn);
			
			pg_send_query($dbconn, $objSQL);
       		$objresult = pg_get_result($dbconn);
           	if(pg_num_rows($result)<1)
           	{
           		if($debugFlag) $firebug->log($objSQL, "no matches for ");
           		continue;
           	}

            
			/**
			 * OBJECT LOOP BEGINS
			 */
            while ($objrow = pg_fetch_array($objresult))
            {
				$theObject = new object();
				$theObject->setParamsFromDB($objrow['objectid']);
			
				//echo "Process data for site ".$theObject->getCode()."<br>";
				/**
				 * ELEMENT LOOP 
				 */
				for($thiselemnum = $row['elementstartnum']; $thiselemnum<=$elemEnd; $thiselemnum=$thiselemnum+1)
				{

					//echo $row['sitecode']."-".$thiselemnum."-".$row['samplecode']."<br>";
					
					$thisElement = new element();					
					// Set parent object
					array_push($thisElement->parentEntityArray,$theObject);				
					// Set title 
					$thisElement->setTitle($thiselemnum);
					// Set Type to unknown
					$thisElement->setType(null, "Unknown");				
					// Set taxon to Plantae
					$thisElement->taxon->setParamsFromDB('1');		
					// Write element to db
					$success = $thisElement->writeToDB();
					
					if ($success!=TRUE)
					{
						$insertsql = "insert into elementimporterror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error)
								values(".
								"'".$row['id']."', ".
						        "'".$row['boxname']."', ".
						 		"'".$row['regioncode']."', ".
						        "'".$row['elementstartnum']."', ".
						        "'".$row['elementendnum']."', ".
						        "'".$row['samplecode']."', ".
        						"'".$row['taxa']."', ".
						        "'".$row['sitecode']."', ".
						        "'".$row['missing']."', ".
						        "'".$row['notes']."', ".
						        "'".$row['sampletype']."', ".
								"'".$thiselemnum."', ".
								"'".pg_escape_string($thisElement->getLastErrorMessage())."' ".
								")";
						pg_send_query($dbconn, $insertsql);	
						if($debugFlag) $firebug->log($thisElement->getID(), "Failed to write this element to db");
						$elementFailureCount++;	
					}		
					else
					{
						$elementSuccessCount++;
					}
					
				}
				
				
			// End object loop
			}	
			$progressCount++;
			if($debugFlag===FALSE) 
			{
				$bar->increase();
			}
			else
			{
				printSummaryTable();
			}
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

	$sql = "SELECT * from $importTable order by id asc limit $limit offset $offset";
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
			$elemEnd = $row['elementendnum'];
			$elemStart = $row['elementstartnum'];
			
			if(($elemEnd-$elemStart)<0)
			{
				if($debugFlag) $firebug->log($row, "Problem with element numbers"); 
				continue;
			}
			
           				
				/**
				 * ELEMENT LOOP 
				 */
				for($thiselemnum = $row['elementstartnum']; $thiselemnum<=$elemEnd; $thiselemnum=$thiselemnum+1)
				{

					$oldres = pg_get_result($dbconn);
					$elemSQL = "select tblelement.elementid, tblobject.objectid 
					from tblelement, tblobject where tblelement.code='".$thiselemnum."' 
					and tblobject.code='".$row['sitecode']."' and tblelement.objectid=tblobject.objectid";		

					pg_send_query($dbconn, $elemSQL);
		       		$elemresult = pg_get_result($dbconn);
		           	if(pg_num_rows($result)<1)
		           	{
		           		if($debugFlag) $firebug->log($elemSQL, "no matches for ");
		           		logSampleError($row, $thiselemnum, $error='no matches for this element in db');
		           		continue;
		           	}
		
		   
		            while ($elemrow = pg_fetch_array($elemresult))
		            {
						$theElement = new element();
						$theElement->setParamsFromDB($elemrow['elementid']);				
		            }
					
					$thisSample = new sample();
					array_push($thisSample->parentEntityArray,$theElement);
					$thisSample->setTitle($row['samplecode']);
					// Set Type to unknown
					$thisSample->setType(null, "Unknown");
					$foundBox = $thisSample->setBoxFromName(strtoupper($row['boxname']));
					if($foundBox===FALSE) 
					{
						if($debugFlag) $firebug->log(strtoupper($row['boxname']), "Can't find box:");
					}
					else
					{

					}
					$thisSample->setBoxComments($row['notes']);
					

	
					$success3 = $thisSample->writeToDB();
					
					if ($success3!=TRUE)
					{
						if($debugFlag) $firebug->log($thisSample->getID(), "Failed to write this sample to db");
						logSampleError($row, $thiselemnum, $thisSample->getLastErrorMessage());
						$sampleFailureCount++;	
					}		
					else
					{
						$sampleSuccessCount++;
					}
					
							
				// End element loop
				}
			
				$progressCount++;
				
				if($debugFlag===FALSE) 
				{
					$bar->increase();
				}
				else
				{
					printSummaryTable();
				}

				
		// End inventory row loop	
		}

	}
	else
	{
		// Connection bad
		if($debugFlag) $firebug->log("Error connecting to database", "Error");
	}
	
	
	//echo "<BR><b>Attempting to fix sample errors...<br><br>";
	
// THIRD TIME AROUND! 
// Now we try and update the existing samples with their new box numbers


if($doThird)
{
	$sql = "SELECT * from sampleimporterror where fixed='f' order by id asc";
	$dbconnstatus = pg_connection_status($dbconn);
	if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	{
		pg_get_result($dbconn);
		pg_get_result($dbconn);
		pg_get_result($dbconn);
				
		$result = pg_query($dbconn, $sql);
        
		//echo "Number of failed samples found= ".pg_num_rows($result)."<br>";
		if($debugFlag===FALSE) $bar2->initialize( pg_num_rows($result)); //print the empty bar
		
		if($debugFlag===FALSE) ob_flush();
		if($debugFlag===FALSE) flush();
		
		/**
		 *   ERROR TABLE LOOP BEGINS
		 */
		while ($row = pg_fetch_array($result))
		{	
			$i++;
			$sampleSQL = "select tblsample.sampleid, tblelement.elementid, tblobject.objectid 
						from tblsample 
						LEFT JOIN tblelement ON tblsample.elementid=tblelement.elementid
						LEFT JOIN tblobject ON tblelement.objectid=tblobject.objectid
						WHERE tblsample.code='".$row['samplecode']."'
						AND tblelement.code='".$row['currentelement']."'
						AND tblobject.code='".$row['sitecode']."'";	

			pg_send_query($dbconn, $sampleSQL);
       		$sampleresult = pg_get_result($dbconn);
           	if(pg_num_rows($sampleresult)<1)
           	{
           		if($debugFlag) $firebug->log($sampleSQL, "no matches for ");
           		logSampleError($row, $row['currentelement'], $thisSample->getLastErrorMessage(), TRUE);	
           		continue;
           	}
			else
			{
				//echo "$i number of samples found = ".pg_num_rows($sampleresult)."<br>";
			}
   
            while ($samplerow = pg_fetch_array($sampleresult))
            {
				$thisSample = new sample();
				$thisSample->setParamsFromDB($samplerow['sampleid']);
				$thisSample->setBoxFromName(strtoupper($row['boxname']));		
				$thisSample->setBoxComments($row['notes']);	
            
				
				$success4 = $thisSample->writeToDB();
				if($success4!=TRUE)
				{
					$sampleFixFailureCount++;
					if($debugFlag) $firebug->log($thisSample->getID(), "Failed to update this sample to db");
					logSampleError($row, $row['currentelement'], $thisSample->getLastErrorMessage(), TRUE);					
				}
				else
				{
					$sampleFixSuccessCount++;
					//$sampleFailureCount--;
					$fixedSQL = "update sampleimporterror set fixed='t' where id='".$row['id']."' 
					and currentelement='".$row['currentelement']."' 
					and samplecode='".$row['samplecode']."'";
					pg_query($dbconn, $fixedSQL);
					$oldres = pg_get_result($dbconn);
				}						
            }
					
		// End import error table row loop
		if($debugFlag===FALSE) $bar2->increase();	
		}

	}
	else
	{
		// Connection bad
		if($debugFlag) $firebug->log("Error connecting to database", "Error");
	}
	
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
	echo "<tr><td><b>Samples</b> </td><td>$sampleSuccessCount </td><td> $sampleFailureCount </td></tr>";
	echo "<tr><td><b>Samples Fixed</b> </td><td>$sampleFixSuccessCount </td><td> $sampleFixFailureCount </td></tr></table><br></div>";
	
	if($debugFlag===FALSE)
	{	
		ob_flush();
		flush();
		ob_clean();
	}

}


function logSampleError($row, $thiselemnum, $error=' ', $update=FALSE)
{

	global $dbconn;
	global $debugFlag;
	global $firebug;
	
	$insertsql = "insert into ";
	
	if($update)
	{
		$insertsql .= "sampleimportfixerror ";
	}
	else
	{
		$insertsql .= "sampleimporterror ";
	}
	
	$insertsql .= "(id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error)
				values(".
				"'".$row['id']."', ".
		        "'".$row['boxname']."', ".
		 		"'".$row['regioncode']."', ".
		        "'".$row['elementstartnum']."', ".
		        "'".$row['elementendnum']."', ".
		        "'".$row['samplecode']."', ".
    						"'".$row['taxa']."', ".
		        "'".$row['sitecode']."', ".
		        "'".$row['missing']."', ".
		        "'".$row['notes']."', ".
		        "'".$row['sampletype']."', ".
				"'".$thiselemnum."', ".
				"'".pg_escape_string($error)."' ".				
				")";
	
	pg_send_query($dbconn, $insertsql);	
	if($debugFlag) $firebug->log($row['sitecode']."-".$thiselemnum."-".$row['samplecode'], "Failed to write this sample to db");
	
	
}
?>
</HTML>