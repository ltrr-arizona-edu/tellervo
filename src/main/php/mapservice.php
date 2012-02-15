<?php
//*******************************************************************
////// PHP Tellervo Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/dbhelper.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/search.php");
require_once("inc/errors.php");
require_once("inc/output.php");
require_once("inc/olStyles.php");

require_once("inc/object.php");
require_once("inc/element.php");
require_once("inc/sample.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/authenticate.php");

$xmldata = NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();

// Intercept login requests
if($_POST['requesturl'])
{
	global $domain;
	
	$myAuth->login($_POST['user'], $_POST['password']);
	
	$redirect = "Location: https://dendro.cornell.edu".$_POST['requesturl'];
	header( $redirect ) ;
}

// Get GET parameters
$reqEntity = $_GET['entity'];
$reqID = explode(",", $_GET['id']);
$format = $_GET['format'];
$code = explode(",", $_GET['code']);
$values = explode(",", $_GET['values']);

$firebug->log($reqID, "IDs requested");
$firebug->log($code, "Codes requested");


// Do basic error checking and set defaults
if($format==NULL) $format = "gmap";
if($reqID[0]=='') $reqID = null;
if($code[0]=='') $code = null;
if(count($values)>0)
{
	if(count($values)!=count($reqID)) {
		$firebug->log("Different number of values to ids"); die(); 
	}
}


$firebug->log($reqID, "IDs requested");
$firebug->log($code, "Codes requested");
$firebug->log($values, "Values for each point");

if($reqEntity==NULL) $reqEntity = 'object';



$myMetaHeader->setRequestType('read');


// Check authentication and request login if necessary
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}
else
{
	echo "<html>
	<link rel=\"stylesheet\" href=\"css/weblogin.css\" type=\"text/css\" />
	<link rel=\"stylesheet\" href=\"css/openLayersStyle.css\" type=\"text/css\" />
	<div id=\"weblogin\"><h1>Tellervo login:</h1>
	<br/>
	<form method=\"POST\">
	<table>
	<tr><th>Username: </td><td><input type=\"text\" size=\"25\" name=\"user\"></td></tr>
	<tr><th>Password: </td><td><input type=\"password\" size=\"25\" name=\"password\"></td></tr>
	<tr><td></td><td><input type=\"submit\" value=\"Login\"/></td></tr>
	</table>
	<input type=\"hidden\" name=\"requesturl\" value=\"".$_SERVER['REQUEST_URI']."\"/>
	</form>
	</div>";
	
    $seq = $myAuth->sequence();
    $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq);
    die();
}

$xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<kml xmlns=\"http://earth.google.com/kml/2.1\">\n<Document>\n<name>Tellervo Map</name>\n";
$xmldata .= getTScoreStyle();

$fullSQL = null;

if((count($reqID)==0) && (count($code)==0))
{
		$firebug->log("Doing all objects");
		global $dbconn;
		$deniedRecArray = array();
        // Do SQL Query
        switch ($reqEntity)
        {
        	case "object": 		$fullSQL = "select * from vwtblobject where locationgeometry is not null order by code asc"; break;
        	case "element": 	$fullSQL = "select * from vwtblelement where locationgeometry is not null order by code asc"; break;
        	case "sample": 	    $fullSQL = "select * from vwtblsample where locationgeometry is not null order by code asc"; break;   	
        	case "series":		$fullSQL = "select * from vwcomprehensivevm where extentgeometry is not null order by objectcode asc"; break;
        	default:	"unsupported entity type."; die();
        	
        }
}

else if (count($reqID)==1)
{
	$firebug->log("Doing single object from ID");

	global $dbconn;
	$deniedRecArray = array();
    // Do SQL Query
    switch ($reqEntity)
    {
    	case "object": 		$fullSQL = "select * from vwtblobject where locationgeometry is not null and objectid='".$reqID[0]."' order by code asc"; break;
    	case "element":     $fullSQL = "select * from vwtblelement where locationgeometry is not null and elementid='".$reqID[0]."' order by code asc"; break;
    	case "sample":      $fullSQL = "select * from vwtblsample where locationgeometry is not null and sampleid='".$reqID[0]."' order by code asc"; break;
    	
    	case "series":		$fullSQL = "select * from vwcomprehensivevm where extentgeometry is not null and vmeasurementid='".$reqID[0]."' order by objectcode asc"; break;
    	default:	"unsupported entity type."; die();
    	
    }
}

else if (count($reqID)>1)
{
	$firebug->log("Doing multiple entities from id list");
	global $dbconn;
	$deniedRecArray = array();
    // Do SQL Query
    switch ($reqEntity)
    {
    	case "object": 		
    		$fullSQL = "select * from vwtblobject where locationgeometry is not null and (";
    		foreach ($reqID as $id)
    		{
    			$fullSQL.=" objectid='".trim($id)."' or ";
    		}
    		// Trim off last 'or'
    		$fullSQL = substr($fullSQL, 0, -4);
    		$fullSQL .= ") order by code asc"; 
    		$firebug->log($fullSQL, "SQL");
    		break;  
    	case "element": 		
    		$fullSQL = "select * from vwtblelement where locationgeometry is not null and (";
    		foreach ($reqID as $id)
    		{
    			$fullSQL.=" elementid='".trim($id)."' or ";
    		}
    		// Trim off last 'or'
    		$fullSQL = substr($fullSQL, 0, -4);
    		$fullSQL .= ") order by code asc"; 
    		$firebug->log($fullSQL, "SQL");
    		break;    		
    	case "series": 		
    		$fullSQL = "select * from vwcomprehensivevm where locationgeometry is not null and (";
    		foreach ($reqID as $id)
    		{
    			$fullSQL.=" vmeasurementid='".trim($id)."' or ";
    		}
    		// Trim off last 'or'
    		$fullSQL = substr($fullSQL, 0, -4);
    		$fullSQL .= ") order by objectcode asc"; 
    		$firebug->log($fullSQL, "SQL");
    		break; 
    	default:	"unsupported entity type."; die();
    	
    }
}

else if (count($code)==1)
{
	$firebug->log("Doing single object from code");
	global $dbconn;
	$deniedRecArray = array();
    // Do SQL Query
    switch ($reqEntity)
    {
    	case "object": 		$fullSQL = "select * from vwtblobject where locationgeometry is not null and code = '".$code[0]."' order by code asc"; break;
    	case "series":		$fullSQL = "select * from vwcomprehensivevm where extentgeometry is not null and code = '".$code[0]."' order by objectcode asc"; break;
    	default:	"unsupported entity type."; die();
    	
    }
	
}

else if (count($code)>1)
{
	$firebug->log("Doing multiple objects from code list");
	global $dbconn;
	$deniedRecArray = array();
    // Do SQL Query
    switch ($reqEntity)
    {
    	case "object": 		
    		$fullSQL = "select * from vwtblobject where locationgeometry is not null and (";
    		foreach ($code as $thecode)
    		{
    			$fullSQL.=" code='".trim($thecode)."' or ";
    		}
    		// Trim off last 'or'
    		$fullSQL = substr($fullSQL, 0, -4);
    		$fullSQL .= ") order by code asc"; 
    		$firebug->log($fullSQL, "SQL");
    		break;  		
    	default:	"unsupported entity type."; die();
    	
    }
}


// DO SEARCH
   $result = pg_query($dbconn, $fullSQL);

   $i = 0;
   while ($row = pg_fetch_array($result))
   {
                	
       // Check user has permission to read then create a new object
       if($reqEntity=="object") 
       {
           $myReturnObject = new object();
           $hasPermission = $myAuth->getPermission("read", "object", $row['objectid']);
       }
       elseif($reqEntity=="element")
       {
           $myReturnObject = new element();
           $hasPermission = $myAuth->getPermission("read", "element", $row['elementid']);
       }
       elseif($reqEntity=="sample") 
       {
           $myReturnObject = new sample();
           $hasPermission = $myAuth->getPermission("read", "sample", $row['sampleid']);
       }
       elseif($reqEntity=="radius") 
       {
           $myReturnObject = new radius();
           $hasPermission = $myAuth->getPermission("read", "radius", $row['radiusid']);
       }
       elseif($reqEntity=="series")
       {
           $myReturnObject = new measurement();
           $hasPermission = $myAuth->getPermission("read", "measurement", $row['vmeasurementid']);
       }
       else
       {
           echo "Invalid return object ".$reqEntity." specified.";
       }

       if($hasPermission===FALSE)
       {
           array_push($deniedRecArray, $row['id']); 
           continue;
       }

       // Set parameters on new object and return XML
       $success = $myReturnObject->setParamsFromDBRow($row);

       if($success)
       {     	 
       	   if($values[$i]!=NULL)
       	   {
       	   		// Values associated 
       	   		$firebug->log("Point with value");
           		$xmldata.=$myReturnObject->asKMLWithValue($values[$i]);
       	   }
       	   else
       	   {
       	   		// No values just points
       	   		$firebug->log("Point without value");
       	   		$xmldata.=$myReturnObject->asKML();
       	   }
       }
       else
       {
           echo $myReturnObject->getLastErrorCode(), $myReturnObject->getLastErrorMessage();
           die();
       }
       
       $i++;
    }






$xmldata .= "\n</Document>\n";
$xmldata .= "</kml>";

global $firebug;

switch($format)
{
	case "kml": 
		header('Content-Type: application/xhtml+xml; charset=utf-8');
		echo $xmldata; 
		die();
	
	case "gmap": 
		global $tempFolder;
		global $tempFolderURL;
		
		

		$tmpfname = tempnam($tempFolder, "CMS-Temp-").".kml";
		$firebug->log($tmpfname, "Temp filename");
		try
		{
			$handle = fopen($tmpfname, "w");
			fwrite($handle, $xmldata);
			fclose($handle);			
		}
		catch (Exception $exception)
		{
			$firebug->log($exception->getMessage(), "KML file write exception");
			
		}

		
		writeOpenLayerOutput($tempFolderURL.basename($tmpfname));
				
}
        

