<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@ltrr.arizona.edu
 * Requirements : PHP >= 5.2
 *
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

try{
	require_once("config.php");
} catch (Exception $e)
{
	trigger_error('704'.'Tellervo server configuration file missing.  Contact your systems administrator');
}

try{
	require_once("systemconfig.php");
} catch (Exception $e)
{
	trigger_error('704'.'System configuration file missing.  Server administrator needs to run tellervo-server --reconfigure', E_USER_ERROR);
}
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/output.php");


$myAuth  = new auth();



if(!$myAuth->isLoggedIn)
{
	echo "Please log in first";
	die();
}

if(!$myAuth->isAdmin())
{
	echo "Only accessible to administrators";
	die();
}


$sql = "SELECT * from tlkptaxon offset 5 limit 5";

$dbconnstatus = pg_connection_status ( $dbconn );
if ($dbconnstatus === PGSQL_CONNECTION_OK) {
	pg_send_query ( $dbconn, $sql );
	$result = pg_get_result ( $dbconn );
	if (pg_num_rows ( $result ) == 0) {
		// No records match the id specified
		echo "No records match the specified id. $sql";
		die();
	} else {
		while ($row = pg_fetch_array($result))
		{
		     $oldtaxon = new oldtaxon();
		     $oldtaxon->populateFromRow($row);
		     
		     echo $oldtaxon->label." converts to: \n";
		     
		     $newtaxon = new newtaxon();
		     
		     echo "Original rank = ".$oldtaxon->taxonrankid."\n";
		     
		     if($oldtaxon->taxonrankid<9)
		     {
		     	$newtaxon->populateFromCOLWebservice($oldtaxon->label);
		     }
		     else if($oldtaxon->taxonrankid==9)
		     {
		     	echo "Parsing species label: $oldtaxon->label \n";
		     	$parts = explode(" ", $oldtaxon->label);
		     	if(count($parts)>=3){
		     		 
		     		$newtaxon->populateFromCOLWebservice($parts[0]." ".$parts[1]);
		     	}
		     	else
		     	{
		     		echo "Unexpected format to label "+$oldtaxon->label;
		     		die();
		     	}
		     }
		     else 
		     {
		     	echo "Parsing infraspecies label: $oldtaxon->label \n";
		     	$parts = explode(" ", $oldtaxon->label);
		     	if(count($parts)>=3){
		     		
		     		$newtaxon->populateFromCOLWebservice($parts[0]." ".$parts[1]. " ". $parts[3]);
		     	}
		     	else
		     	{
		     		echo "Unexpected format to label "+$oldtaxon->label;
		     		die();
		     	}
		     }
		     
		    print_r($newtaxon);
		} 

		
	}
} else {
	// Connection bad
	echo "Error connecting to database";
	die();
	
}


class oldtaxon {
	
	var $taxonid;
	var $colid;
	var $colparentid;
	var $taxonrankid;
	var $label;
	var $parenttaxonid;
	
	function populateFromRow($row)
	{
		
		$this->taxonid = $row['taxonid'];
		$this->colid = $row['colid'];
		$this->colparentid = $row['colparentid'];
		$this->taxonrankid = $row['taxonrankid'];
		$this->label = $row['label'];
		$this->parenttaxonid = $row['parenttaxonid'];
		
	}
	
}




class newtaxon {

	var $colid;
	var $colparentid;
	var $taxonrankid;
	var $label;

	function populateFromRow($row)
	{
		$this->colid = $row['colid'];
		$this->colparentid = $row['colparentid'];
		$this->taxonrankid = $row['taxonrankid'];
		$this->label = $row['label'];
	}
	
	function populateFromCOLWebservice($name)
	{
		// Get cURL resource
		$curl = curl_init();
		// Set some options - we are passing in a useragent too here
		curl_setopt_array($curl, array(
				CURLOPT_RETURNTRANSFER => 1,
				CURLOPT_URL => 'http://www.catalogueoflife.org/annual-checklist/2017/webservice?format=xml&name='.$name,
				CURLOPT_USERAGENT => 'Tellervo'
		));
		// Send the request & save response to $resp
		$response = curl_exec($curl);
		// Close request to clear up some resources
		curl_close($curl);
		

		$xml = simplexml_load_string($response);
		
		//print_r($xml);
		
		if($xml->result[0]->name_status=='accepted name')
		{
			if( ($xml->result[0]->rank=='Kingdom') || 
					($xml->result[0]->rank=='Phyllum') ||
					($xml->result[0]->rank=='Division') ||
					($xml->result[0]->rank=='Class') ||
					($xml->result[0]->rank=='Order') ||
					($xml->result[0]->rank=='Family') ||
					($xml->result[0]->rank=='Genus') )
			{
				$this->colid = (string) $xml->result[0]->id;
				$this->taxonrankid = $this->convertRankToRankID($xml->result[0]->rank);
				$this->label = (string) $xml->result[0]->name;
				
				
			}
			else if ($xml->result[0]->rank=="Species")
			{
				$this->colid = (string) $xml->result[0]->id;
				$this->label = (string) $xml->result[0]->name ." ". (string) $xml->result[0]->author;
				$this->taxonrankid = $this->convertRankToRankID($xml->result[0]->rank);
				
			}
			
			
			
		}
		else
		{
			// Synonym so grab accepted info
			$this->colid = $xml->result[0]->accepted_name->id;
			$this->label = $xml->result[0]->accepted_name->id;
		}
	}
	
	function convertRankToRankID($rank)
	{
		if($rank == 'Kingdom') return 1;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Phylum') return 3;
		if($rank == 'Division') return 4;
		if($rank == 'Class') return 5;
		if($rank == 'Order') return 6;
		if($rank == 'Family') return 7;
		if($rank == 'Genus') return 8;
		if($rank == 'Species') return 9;
		/*if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;
		if($rank == 'Subkingdom') return 2;*/
		
			
		/*1;"kingdom"
		2;"subkingdom"
		3;"phylum"
		4;"division"
		5;"class"
		6;"order"
		7;"family"
		8;"genus"
		9;"species"
		10;"subsp."
		11;"race"
		12;"var."
		13;"subvariety"
		14;"form"
		15;"subform"
		16;"subgenus"
		17;"section"
		18;"subsection"
		;""*/
		
	}
	
	function populateFromCOLWebserviceID($id)
	{
		// Get cURL resource
		$curl = curl_init();
		// Set some options - we are passing in a useragent too here
		curl_setopt_array($curl, array(
				CURLOPT_RETURNTRANSFER => 1,
				CURLOPT_URL => 'http://www.catalogueoflife.org/annual-checklist/2017/webservice?id='.$id.'&format=xml&response=full',
				CURLOPT_USERAGENT => 'Tellervo'
		));
		// Send the request & save response to $resp
		$response = curl_exec($curl);
		// Close request to clear up some resources
		curl_close($curl);
		
		
		$xml = simplexml_load_string($response);
		
		$this->colid = $xml->result[0]->id;
	}

}





?>
