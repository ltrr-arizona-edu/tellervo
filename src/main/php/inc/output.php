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

function getHelpDocbook($page)
{
	//    header('Content-Type: application/xhtml+xml; charset=utf-8');
	/*global $domain;
	global $wikiManualBaseUrl;

	$filename = $wikiManualBaseUrl."/WebserviceDocs-".$page."?action=format&mimetype=xml/docbook";
	$file = file_get_contents($filename);

	// Remove XML header line
	$xml = substr($file, 21);

	// Remove para tags from lists cos it stuffs things up
	$xml = str_replace("<listitem><para>", "<listitem>", $xml);
	$xml = str_replace("</para></listitem>", "</listitem>", $xml);
	*/

	$xml = "<para></para>";

	// Return XML
	return $xml;
}

/**
 * Enter description here...
 *
 * @param meta $metaHeader
 * @param unknown_type $xmldata
 * @param unknown_type $parentTagBegin
 * @param unknown_type $parentTagEnd
 */
function writeOutput($metaHeader, $xmldata="", $parentTagBegin="", $parentTagEnd="")
{
	global $debugFlag;
	$theOutput =  createOutput($metaHeader, $xmldata, $parentTagBegin, $parentTagEnd);

	// If debug flag is on then validate created XML
	if($debugFlag)
	{
		global $tellervoXSD;
		$origErrorLevel = error_reporting(E_ERROR);
		$doc = new DomDocument;
		$doc->loadXML($theOutput);
		libxml_use_internal_errors(true);

		if($doc->schemaValidate($tellervoXSD))
		{
			header('Content-Type: application/xhtml+xml; charset=utf-8');
			echo $theOutput;
		}
		else
		{
			header('Content-Type: application/xhtml+xml; charset=utf-8');
			$therrs = libxml_get_errors();
			foreach($therrs as $err)
			{
				trigger_error("703".$err->message);
			}
			error_reporting($origErrorLevel);
			echo $theOutput;
			return false;
		}
	}
	else
	{
		header('Content-Type: application/xhtml+xml; charset=utf-8');
		echo $theOutput;
	}

}

/**
 * Enter description here...
 *
 * @param meta $metaHeader
 * @param String $xmldata
 * @param String $parentTagBegin
 * @param String $parentTagEnd
 * @return String
 */
function createOutput($metaHeader, $xmldata="", $parentTagBegin="", $parentTagEnd="")
{
	global $domain;
	global $securehttp;
	global $tellervoNS;
	global $tridasNS;
	global $gmlNS;
	global $xlinkNS;

	$outputStr ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	if ($metaHeader->status =="Error")
	{
		if($securehttp===TRUE)
		{
			$http = "https://";
		}
		else
		{
			$http = "http://";
		}
		$outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"$http".$domain."css/tellervo.css\"?>\n";
		$outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"$http".$domain."css/docbook/driver.css\"?>\n";
	}

	// Set root XML tag
	$outputStr.= "\n<tellervo xmlns=\"$tellervoNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\" xmlns:xlink=\"$xlinkNS\">\n";



	$outputStr.= $metaHeader->asXML();

	if($metaHeader->status !="Error")
	{
		// There is proper content so display
		$outputStr.= "<content>\n";
		$outputStr.= $parentTagBegin."\n";
		$outputStr.= $xmldata;
		$outputStr.= $parentTagEnd."\n";
		$outputStr.= "</content>\n";
	}
	else
	{
		// There was an error so try and be helpful

		if($metaHeader->getClientName()=='Tellervo WSI')
		{
			// Don't waste time showing docs as client is Tellervo and docs won't be read
		}
		elseif($metaHeader->getIsLoginRequired())
		{
			$outputStr.= "<help>\n";
			// WS Request failed because the user isn't authenticated. Show authentication docs
			$outputStr.= getHelpDocbook('Authentication');
			$outputStr.= "</help>\n";
		}
		else
		{
			$outputStr.= "<help>\n";
			// WS Request failed for another reason so show this objects docs
			$outputStr.= getHelpDocbook('Introduction');
			$outputStr.= "</help>\n";
		}

	}

	$outputStr.= "</tellervo>";
	return $outputStr;
}


function writeHelpOutput($metaHeader)
{
	/*    header('Content-Type: application/xhtml+xml; charset=utf-8');
	 echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	 echo "<?xml-stylesheet type=\"text/css\" href=\"css/tellervo.css\"?>";
	 echo "<?xml-stylesheet type=\"text/css\" href=\"css/docbook/driver.css\"?>";
	 echo "<tellervo>\n";
	 echo $metaHeader->asXML();
	 echo "<help>\n";
	 echo getHelpDocbook($metaHeader->getObjectName());
	 echo "</help>\n";
	 echo "</tellervo>";
	 */
	writeIntroOutput($metaHeader);
}

function writeWelcomeOutput($metaHeader)
{
	global $labname; 
	header('Content-Type: application/xhtml+xml; charset=utf-8');
	echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	echo "<?xml-stylesheet type=\"text/css\" href=\"css/tellervo.css\"?>";
	echo "<?xml-stylesheet type=\"text/css\" href=\"css/docbook/driver.css\"?>";
	echo "<tellervo>\n";
	echo $metaHeader->asXML();
	echo "<help>\n";
	echo "<chapter>Welcome to the $labname Tellervo Webservice</chapter>\n";
	echo "<para>The webservice appears to be configured correctly and is ready to be used.</para><para>To access the webservice you should use the Tellervo desktop application, point in at this URL and log in with the username and password provided to you by the $labname systems administrator.</para>\n<para>If you have not yet installed Tellervo desktop you can download the application from <ulink url=\"http://www.tellervo.org/download\">http://www.tellervo.org/download</ulink></para>\n";
	echo "</help>\n";
	echo "</tellervo>";
}


function writeIntroOutput($metaHeader)
{
	header('Content-Type: application/xhtml+xml; charset=utf-8');
	echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	echo "<?xml-stylesheet type=\"text/css\" href=\"css/tellervo.css\"?>";
	echo "<?xml-stylesheet type=\"text/css\" href=\"css/docbook/driver.css\"?>";
	echo "<tellervo>\n";
	echo $metaHeader->asXML();
	echo "<help>\n";
	echo getHelpDocbook("Introduction");
	echo "</help>\n";
	echo "</tellervo>";
}


function writeKMLOutput($xmldata)
{

	header('Content-Type: application/xhtml+xml; charset=utf-8');
	$xml.= "<kml xmlns=\"http://earth.google.com/kml/2.2\"> ";
	$xml.= "<Document>";
	/*    $xml.= "<name>Tellervo Sites</name>";
	 $xml.= "<open>1</open>";
	 $xml.= "<description>Sites where dendrochronology samples have been collected by the Cornell Tree Ring Laboratory and stored in Tellervo</description>";

	 $xml.= "<Style id=\"redLineRedPoly\"> <LineStyle> <color>ff0000ff</color></LineStyle> <PolyStyle> <color>ff0000ff</color> </PolyStyle> </Style>";

	 $xml.= "<Folder>";
	 $xml.= "<name>Sites</name>";
	 $xml.= "<visibility>1</visibility>";
	 $xml.= "<description>Sites where dendrochronology samples have been collected by the Cornell Tree Ring Laboratory and stored in Tellervo</description>";
	 /
	 $xml.= "<Placemark>";
	 $xml.= "<name>Site</name>";
	 $xml.= "<visibility>1</visibility>";
	 $xml.= "<styleURL>#redLineRedPoly</styleURL>";
	 */
	$xml.= $xmldata;
	// $xml.= "</Placemark>";
	$xml.= "</Document>";
	$xml.= "</kml>";

	$myFile = "/var/www/website/out.kml";
	$fh = fopen($myFile, 'w');
	fwrite($fh, $xml);
	fclose($fh);

	echo $xml;

}

?>
