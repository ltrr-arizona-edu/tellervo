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
    header('Content-Type: application/xhtml+xml; charset=utf-8');
    echo createOutput($metaHeader, $xmldata, $parentTagBegin, $parentTagEnd);
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
    global $corinaNS;
    global $tridasNS;
    global $gmlNS;

    $outputStr ="";
    $outputStr.="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    if ($metaHeader->status =="Error")
    {
        $outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"https://".$domain."css/corina.css\"?>\n";
        $outputStr.= "<?xml-stylesheet type=\"text/css\" href=\"https://".$domain."css/docbook/driver.css\"?>\n";
    }
    
   	// Set root XML tag
   	// If client is unsupported play safe and don't use namespaces
    if ( (isset($metaHeader->messages[0][107])) || (isset($metaHeader->messages[0][108])) )
    {
        $outputStr.= "\n<corina>\n";
    }
    else
    {
    	$outputStr.= "\n<corina xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">\n";
    }
    
    
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


function writeGMapOutput($xmldata)
{
	global $gMapAPIKey;
    include('inc/mapFunctions.php');
	echo "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" 
    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">
	<html xmlns=\"http://www.w3.org/1999/xhtml\">
  	<head>
    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>
    <title>Corina Map</title>
    
   <!-- Make the document body take up the full screen -->
  <style type=\"text/css\">
    v\:* {behavior:url(#default#VML);}
    html, body {width: 100%; height: 100%}
    body {margin-top: 0px; margin-right: 0px; margin-left: 0px; margin-bottom: 0px}
  </style>
     
    <script src=\"http://maps.google.com/maps?file=api&amp;v=2&amp;key=$gMapAPIKey\"
      type=\"text/javascript\"></script>
    <script type=\"text/javascript\"> 
    
   
    var map;
    var geoXml; 

    function initialize() {
      if (GBrowserIsCompatible()) {
        geoXml = new GGeoXml(\"$xmldata\", finishedLoadingXml);
	    map = new GMap2(document.getElementById(\"map_canvas\")); 
	    // Set initial view outside of scope so that the user doesn't get a jerky experience
	    map.setCenter(new GLatLng(-200,-200), 5, G_HYBRID_MAP); 
        map.addControl(new GLargeMapControl());
	    map.addControl(new GMenuMapTypeControl());
	    map.addControl(new GOverviewMapControl());            
    	map.addOverlay(geoXml);  

      // Monitor the window resize event and let the map know when it occurs
      if (window.attachEvent) { 
        window.attachEvent(\"onresize\", function() {this.map.onResize()} );
      } else {
        window.addEventListener(\"resize\", function() {this.map.onResize()} , false);
      }  	
		
 
      }

      // Callback function which is run when XML is loaded
      function finishedLoadingXml()
      {   	
      	geoXml.gotoDefaultViewport(map); 
      	map.setZoom(9);
	  }
	  
	  
    } 
   </script>
  </head>

  <body onload=\"initialize()\" onunload=\"GUnload()\">
    <div id=\"map_canvas\" style=\"margin-left:0px;margin-bottom:0px; width: 99.6%; height: 99.8%; float:left; border: 1px solid black;\">aaa</div>
    <div id=\"link\" style=\"position: absolute; top: 10px; left: 120px; color: white\"><a href=\"$xmldata\" style=\"font-family: verdana, arial, helvetica, sans-serif; font-size : 10px; 
     \">Download data</a></div>
  </body>
</html>
	";


}


function oldwriteGMapOutput($xmldata, $requestParams)
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
