#!/usr/bin/php
<?php

/**
 * Check this script is being run from the command line
 */
if (!isset($argc))
{
	writeStdErr("This file should be called from the command line only.");
	die();
}

if (!function_exists('curl_init')) {
	writeStdErr("This script requires the php5-curl library");
	die();
}

$wsurl = "http://www.catalogueoflife.org/col/webservice?";
$outfile = "/tmp/out.sql";

$fp = fopen($outfile, 'w');

doTopDownSearch("Cedrus");

/*$toptaxon = "Tsuga";
$childarr = array();

$result = getAllChildrenForTaxon($toptaxon);
echo "Result = ";
print_r($childarr);
*/


fclose($fp);
die();

function doTopDownSearch($strtoptaxon)
{
	
	$toptaxon = searchByName($strtoptaxon);
	recursiveBuildInserts($toptaxon['results'][0]['id']);
	
}

function recursiveBuildInserts($id, $parentid='')
{
	echo "Recursing ID=$id and Parent=$parentid\n";
	$searchres = searchById($id);
	
	foreach($searchres['results'] as $res)
	{
		writeInsert($res, $parentid);
		$parentid = $id;
		
		if($res['child_taxa']!=null)
		{
			foreach($res['child_taxa'] as $child)
			{
				recursiveBuildInserts($child['id'], $parentid);
			}
		}
	}
	
}

function writeInsert($taxon, $parentid='')
{
	global $fp;
	
	$name = '';
	

	$name = str_replace("</i>", "", str_replace("<i>", "", $taxon['name_html']));


	
	
	fwrite($fp, "INSERT INTO tlkptaxon taxonid, colid, colparentid, taxonrankid, label, parenttaxonid ".
		"VALUES ('".$taxon['id']."', '".$taxon['id']."', '$parentid', '".getRankId($taxon['rank'])."', '".$name."', '$parentid')\n");
		
}


function getAllChildrenForTaxon($strtoptaxon)
{
	$toptaxon = searchByName($strtoptaxon);
	$idarr = array();
	array_push($idarr, $toptaxon['results'][0]['id']);
	return getRecursiveChildIds($idarr);
}

function getRecursiveChildIds($arrOfIds, $runningTotal=null, $level=0)
{	
	global $childarr;
	$level++;
	
	echo "Recursing to level $level\n";
	
	if($runningTotal==null)
	{
		$runningTotal = array();
	}
	
	foreach($arrOfIds as $id)
	{		
		array_push($runningTotal, $id);
		array_push($childarr, $id);
		
		// Search for it and see if it has children itself
		$thisresult = searchById($id);
		if(count($thisresult['results'][0]['child_taxa'])>0)
		{
			$newChildIds = getChildIds($thisresult['results'][0]['child_taxa'], $thisresult['results'][0]['id']);
			array_merge($runningTotal, $newChildIds);
			getRecursiveChildIds($newChildIds, $runningTotal);
			
		}
		else
		{
			echo "No children found for $id\n";
		}
		
	}
	
	return $runningTotal;
}

function getChildIds($taxa, $parentid)
{	
	global $fp;
	
	$idarr = array();
	if($taxa==null) return $idarr;

    foreach($taxa as $taxon)
    {   	  	
		//echo "Getting ID for ".$taxon['name']."  (".$taxon['id'].")\n";    
		fwrite($fp, "INSERT INTO tlkptaxon taxonid, colid, colparentid, taxonrankid, label, parenttaxonid ".
		"VALUES ('".$taxon['id']."', '".$taxon['id']."', '$parentid', '".getRankId($taxon['rank'])."', '".$taxon['name']."', '$parentid')\n");
		
    	array_push($idarr, $taxon['id']);
    	
    }
    
    return $idarr;
}

function getRankID($rank)
{
	switch(strtolower($rank))
	{
		case "kingdom":
			return "1";
		case "subkingdom":
			return "2";
		case "phylum":
			return "3";
		case "division":
			return "4";
		case "class":
			return "5";
		case "order":
			return "6";
		case "family":
			return "7";
		case "genus":
			return "8";
		case "species":
			return "9";
		case "subsp":
			return "10";
		case "race":
			return "11"; 
		case "variety":
			return 12;	
		case "subvariety":
			return 13;
		case "form":
			return 14;
		case "subform":
			return 15;		
		case "infraspecies":
			return 10; 
	}
	
	return "";
}

function searchByName($name)
{
	global $wsurl;	
    $url =$wsurl."name=".$name."&format=php&response=full";
    $data = search($url);
    


   
    return $data;
}


function searchById($id)
{
	global $wsurl;
    $url =$wsurl."id=$id&format=php&response=full";
    return search($url);
}

function search($url)
{

   // initialize a new curl resource
   $ch = curl_init(); 
   // set the url to fetch
   curl_setopt($ch, CURLOPT_URL, $url);

   // don't give me the headers just the content
   curl_setopt($ch, CURLOPT_HEADER, 0);

   // return the value instead of printing the response to browser
   curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

   // use a user agent to mimic a browser
   curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.5) Gecko/20041107 Firefox/1.0');

   $contents = curl_exec($ch);

   // remember to always close the session and free all resources
   curl_close($ch); 
   
   return unserialize($contents);
}
?>