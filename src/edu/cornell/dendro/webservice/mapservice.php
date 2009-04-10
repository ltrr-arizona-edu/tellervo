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

if($format==NULL) $format = "kml";

// Extract the type of request from XML doc
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

$xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<kml xmlns=\"http://earth.google.com/kml/2.0\">\n<Document>\n";
$xmldata .= "<Style id=\"corinaDefault\">
              <IconStyle>
               <scale>1</scale>
               <Icon>
                <href>http://dendro.cornell.edu/temp/pin.png</href>
               </Icon>
               <hotSpot x=\"0.5\" y=\"0.5\" xunits=\"fraction\" yunits=\"fraction\"/>
              </IconStyle>
            <LabelStyle>
              <scale>1</scale>
              <color>FFFFFFFF</color>
            </LabelStyle>
            <LineStyle>
              <width>1.5</width>
              <color>ff030385</color>
            </LineStyle>
            <PolyStyle>
              <color>7d030385</color>
            </PolyStyle>            
           </Style>";
$xmldata.= "<name>Corina Map Data</name>\n";

if($reqID=="all")
{
	echo "not supported yet";
	die();
	
}
else
{
	switch($reqEntity)
	{
	    case "object": 				$myEntity = new object(); break;      
	    case "element": 			$myEntity = new element(); break;   
	    case "measurementSeries" :	$myEntity = new measurement(); break;
	    case "derivedSeries" :  	$myEntity = new measurement(); break;	    
	     
	
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

switch($format)
{
	case "kml": echo $xmldata; die();
	
	case "gmap": 
		global $tempFolder;
		global $tempFolderURL;
		
		

		$tmpfname = tempnam($tempFolder, "CMS-Temp-").".kml";
		try
		{
			$handle = fopen($tmpfname, "w");
			fwrite($handle, $xmldata);
			fclose($handle);			
		}
		catch (Exception $exception)
		{
			echo $exception->getMessage();
			
		}

		
		writeGMapOutput($tempFolderURL.basename($tmpfname));
				
}
        

