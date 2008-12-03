<?php

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

function writeOutput($metaHeader, $xmldata="", $parentTagBegin="", $parentTagEnd="")
{
    header('Content-Type: application/xhtml+xml; charset=utf-8');
    echo createOutput($metaHeader, $xmldata, $parentTagBegin, $parentTagEnd);
}


function createOutput($metaHeader, $xmldata="", $parentTagBegin="", $parentTagEnd="")
{
    global $domain;
    $outputStr ="";
    $outputStr.="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    if ($metaHeader->status =="Error")
    {
        $outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"https://".$domain."css/corina.css\"?>";
        $outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"https://".$domain."css/docbook/driver.css\"?>";
    }
    $outputStr.= "<corina>\n";
    $outputStr.= $metaHeader->asXML();
    
    if($metaHeader->status !="Error")
    {
        $outputStr.= "<content>\n";
        $outputStr.= $parentTagBegin."\n";
        $outputStr.= $xmldata;
        $outputStr.= $parentTagEnd."\n";
        $outputStr.= "</content>\n";
    }
    else
    {
        $outputStr.= "<help>\n";
        if($metaHeader->getIsLoginRequired())
        {
            // WS Request failed because the user isn't authenticated. SHow authentication docs
            $outputStr.= getHelpDocbook('Authentication');
        }
        else
        {
            // WS Request failed for another reason so show this objects docs
            $outputStr.= getHelpDocbook('Introduction');
        }
        $outputStr.= "</help>\n";
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


function writeGMapOutput($xmldata, $requestParams)
{
    global $gMapAPIKey;
    include('inc/mapFunctions.php');
    
    // Set map parameters
    $mapWidth = 300;
    $mapHeight = 300;
    $mapType = "G_PHYSICAL_MAP";
    
    if(isset($requestParams->mapwidth))         $mapWidth=$requestParams->mapwidth;
    if(isset($requestParams->mapheight))        $mapHeight=$requestParams->mapheight;
    if(isset($requestParams->maptype))          
    {
        switch ($requestParams->maptype)
        {
        case "Terrain":
                $mapType="G_PHYSICAL_MAP";
                break;
        case "Satellite":
                $mapType="G_SATELLITE_MAP";
                break;
        case "Map":
                $mapType="G_NORMAL_MAP";
                break;
        case "Hybrid":
                $mapType="G_HYBRID_MAP";
                break;
        }
    }

?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<title>Corina Map</title>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<?echo $gMapAPIKey;?>" type="text/javascript"></script>
<script type="text/javascript">
//<![CDATA[
function load() {
  if (GBrowserIsCompatible()) {
    var map = new GMap2(document.getElementById("map"));
    var bounds = new GLatLngBounds;
<?php
echo "    bounds.extend(new GLatLng(".gMapExtentFromXML($xmldata, "minLat").",".gMapExtentFromXML($xmldata, "minLong")."));\n";
echo "    bounds.extend(new GLatLng(".gMapExtentFromXML($xmldata, "maxLat").",".gMapExtentFromXML($xmldata, "maxLong")."));\n";
?> 
    map.setCenter(bounds.getCenter(), map.getBoundsZoomLevel(bounds));
    map.addControl(new GLargeMapControl());
    map.addControl(new GMenuMapTypeControl());
    map.addControl(new GOverviewMapControl());
    map.addMapType(G_PHYSICAL_MAP);
    map.setMapType(<?php echo $mapType;?>);
<?    
    include('inc/gMapStyles.php');
echo gMapDataFromXML($xmldata);
?>
}}
//]]>
</script>
</head>
  <body onload="load()" onunload="GUnload()">
  <div id="map" style="margin-left:0px;margin-bottom:0px;height:100%;background:#f90"></div>
  </body>
</html>
<?

}

?>
