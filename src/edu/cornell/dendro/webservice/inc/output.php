<?php

function writeOutput($metaHeader, $xmldata="", $parentTagBegin="", $parentTagEnd="")
{
    echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    echo "<corina>\n";
    echo $metaHeader->asXML();
    echo "<content>\n";
    echo $parentTagBegin."\n";
    echo $xmldata;
    echo $parentTagEnd."\n";
    echo "</content>\n";
    echo "</corina>";
}


function writeHelpOutput($metaHeader, $xmldata)
{
    echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    echo "<corina>\n";
    echo $metaHeader->asXML();
    echo "<help>\n";
    echo $xmldata."\n";
    echo "</help>\n";
    echo "</corina>";
}

function writeKMLOutput($xmldata)
{

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
