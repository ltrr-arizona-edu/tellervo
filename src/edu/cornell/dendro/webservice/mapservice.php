<?php
//*******************************************************************
////// PHP Corina Middleware
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

require_once("inc/object.php");
require_once("inc/element.php");
require_once("inc/sample.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/authenticate.php");


$xmldata = NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();

// Get GET parameters
$reqEntity = $_GET['entity'];
$reqID = $_GET['id'];
$format = $_GET['format'];
$code = $_GET['code'];

// Do basic error checking and set defaults
if($format==NULL) $format = "gmap";

// If both id and code are given use id  
if($reqID && $code) $code = NULL;


$myMetaHeader->setRequestType('read');


// Check authentication and request login if necessary
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}
else
{
    $seq = $myAuth->sequence();
    $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq);
    echo "Not logged in";
    die();
}

$xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<kml xmlns=\"http://earth.google.com/kml/2.1\">\n<Document>\n<name>Corina Map</name>\n";
$xmldata .= "<Style id=\"corinaDefault\">
              <IconStyle>
               <scale>1</scale>
               <Icon>
                <href>http://dendro.cornell.edu/temp/pin.png</href>
               </Icon>
               <hotSpot x=\"0.5\" y=\"0.5\" xunits=\"fraction\" yunits=\"fraction\"/>
              </IconStyle>
            <LabelStyle>
              <color>FFFFFFFF</color>
            <scale>1</scale>

            </LabelStyle>
            <LineStyle>
              <color>ff030385</color>
            
            <width>1.5</width>
            </LineStyle>
            <PolyStyle>
              <color>7d030385</color>
            </PolyStyle>            
           </Style>";

if(($reqID=="all") || ($reqID==NULL) || ($reqID==""))
{

		global $dbconn;
		$deniedRecArray = array();
        // Do SQL Query
        switch ($reqEntity)
        {
        	case "object": 		$fullSQL = "select * from vwtblobject where locationgeometry is not null"; break;
        	case "series":		$fullSQL = "select * from vwcomprehensivevm where extentgeometry is not null"; break;
        	default:	"unsupported entity type."; die();
        	
        }
        
                    
    	$result = pg_query($dbconn, $fullSQL);

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
                $xmldata.=$myReturnObject->asKML($format);
            }
            else
            {
                echo $myReturnObject->getLastErrorCode(), $myReturnObject->getLastErrorMessage();
                die();
            }
        }
	
	
}
else
{
	switch($reqEntity)
	{
	    case "object": 				$myEntity = new object(); break;      
	    case "element": 			$myEntity = new element(); break;   
	    case "measurementSeries" :	$myEntity = new measurement(); break;
	    case "derivedSeries" :  	$myEntity = new measurement(); break;	    
	    case "series":				$myEntity = new measurement(); break;
	
	    default:
	    	echo "Unknown entity $reqEntity";
	    	die();
	}
	
	$myEntity->__construct();
	
	if($myAuth->getPermission('read', $reqEntity, $reqID)===FALSE)
	{
		echo "Permission denied";
		die();
	}
	
	
	$success = $myEntity->setParamsFromDB($reqID);
	if(!$success)
	{
		echo $myEntity->getLastErrorCode().$myEntity->getLastErrorMessage();
	}
		
	$xmldata .= $myEntity->asKML();
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
        

