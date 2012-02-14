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

function getHelpDocbook($page)
{
	//    header('Content-Type: application/xhtml+xml; charset=utf-8');
	global $domain;
	global $wikiManualBaseUrl;

	$filename = $wikiManualBaseUrl."/WebserviceDocs-".$page."?action=format&mimetype=xml/docbook";
	$file = file_get_contents($filename);

	// Remove XML header line
	$xml = substr($file, 21);

	// Remove para tags from lists cos it stuffs things up
	$xml = str_replace("<listitem><para>", "<listitem>", $xml);
	$xml = str_replace("</para></listitem>", "</listitem>", $xml);

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
		global $corinaXSD;
		$origErrorLevel = error_reporting(E_ERROR);
		$doc = new DomDocument;
		$doc->loadXML($theOutput);
		libxml_use_internal_errors(true);

		if($doc->schemaValidate($corinaXSD))
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
	global $corinaNS;
	global $tridasNS;
	global $gmlNS;
	global $xlinkNS;

	$outputStr ="";
	$outputStr.="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
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
		$outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"$http".$domain."css/corina.css\"?>\n";
		$outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"$http".$domain."css/docbook/driver.css\"?>\n";
	}

	// Set root XML tag
	$outputStr.= "\n<corina xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\" xmlns:xlink=\"$xlinkNS\">\n";



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

		if($metaHeader->getClientName()=='Corina WSI')
		{
			// Don't waste time showing docs as client is Corina and docs won't be read
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

	$outputStr.= "</corina>";
	return $outputStr;
}


function writeHelpOutput($metaHeader)
{
	/*    header('Content-Type: application/xhtml+xml; charset=utf-8');
	 echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	 echo "<?xml-stylesheet type=\"text/css\" href=\"css/corina.css\"?>";
	 echo "<?xml-stylesheet type=\"text/css\" href=\"css/docbook/driver.css\"?>";
	 echo "<corina>\n";
	 echo $metaHeader->asXML();
	 echo "<help>\n";
	 echo getHelpDocbook($metaHeader->getObjectName());
	 echo "</help>\n";
	 echo "</corina>";
	 */
	writeIntroOutput($metaHeader);
}

function writeIntroOutput($metaHeader)
{
	header('Content-Type: application/xhtml+xml; charset=utf-8');
	echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	echo "<?xml-stylesheet type=\"text/css\" href=\"css/corina.css\"?>";
	echo "<?xml-stylesheet type=\"text/css\" href=\"css/docbook/driver.css\"?>";
	echo "<corina>\n";
	echo $metaHeader->asXML();
	echo "<help>\n";
	echo getHelpDocbook("Introduction");
	echo "</help>\n";
	echo "</corina>";
}


function writeKMLOutput($xmldata)
{

	header('Content-Type: application/xhtml+xml; charset=utf-8');
	$xml.= "<kml xmlns=\"http://earth.google.com/kml/2.2\"> ";
	$xml.= "<Document>";
	/*    $xml.= "<name>Corina Sites</name>";
	 $xml.= "<open>1</open>";
	 $xml.= "<description>Sites where dendrochronology samples have been collected by the Cornell Tree Ring Laboratory and stored in Corina</description>";

	 $xml.= "<Style id=\"redLineRedPoly\"> <LineStyle> <color>ff0000ff</color></LineStyle> <PolyStyle> <color>ff0000ff</color> </PolyStyle> </Style>";

	 $xml.= "<Folder>";
	 $xml.= "<name>Sites</name>";
	 $xml.= "<visibility>1</visibility>";
	 $xml.= "<description>Sites where dendrochronology samples have been collected by the Cornell Tree Ring Laboratory and stored in Corina</description>";
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
