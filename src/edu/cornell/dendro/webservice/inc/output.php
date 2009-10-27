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
    global $corinaNS;
    global $tridasNS;
    global $gmlNS;
    global $xlinkNS;

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
    	$outputStr.= "\n<corina xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\" xmlns:xlink=\"$xlinkNS\">\n";
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


function writeOpenLayerOutput($xmldata)
{
	global $gMapAPIKey;
    include('inc/mapFunctions.php');
    echo "<html xmlns=\"http://www.w3.org/1999/xhtml\">
    <head>
        <title>Corina Map Service</title>        
       <link rel=\"stylesheet\" href=\"css/openLayersStyle.css\" type=\"text/css\" />
       <link rel=\"stylesheet\" href=\"css/google.css\" type=\"text/css\" />
       
        <style type=\"text/css\">
            body {
                margin: 0;
            }
            #map {
                width: 100%;
                height: 100%;
            }

            #text {
                position: absolute;
                bottom: 1em;
                left: 1em;
                width: 512px;
            }
        </style>
        
        <style type=\"text/css\">
            #controls
            {
                width: 512px;
            }
        </style>
        
        
        <script src=\"jscript/OpenLayers.js\"></script>
        <script src='http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6.1'></script>
        <script src=\"http://api.maps.yahoo.com/ajaxymap?v=3.0&appid=euzuro-openlayers\"></script>
        <script src='http://maps.google.com/maps?file=api&amp;v=3&amp;key=$gMapAPIKey'></script>
        <!-- <script src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAjpkAC9ePGem0lIq5XcMiuhT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTS6gjckBmeABOGXIUiOiZObZESPg'></script>-->
 
        <script type=\"text/javascript\">
    var map, drawControls, selectControl, selectedFeature;
        function onPopupClose(evt) {
            selectControl.unselect(selectedFeature);
        }
        function onFeatureSelect(feature) {
            selectedFeature = feature;
            popup = new OpenLayers.Popup.FramedCloud(\"chicken\", 
                                     feature.geometry.getBounds().getCenterLonLat(),
                                     new OpenLayers.Size(400,300),
                                     \"<div><h2>\"+feature.attributes.name+\"</h2>\"+feature.attributes.description+\"</div>\",
                                     null, true, onPopupClose);
            feature.popup = popup;
            map.addPopup(popup);
        }
        function onFeatureUnselect(feature) {
            map.removePopup(feature.popup);
            feature.popup.destroy();
            feature.popup = null;
        }
    
    
            function init(){
           
            var options = {
                projection: new OpenLayers.Projection(\"EPSG:900913\"),
                displayProjection: new OpenLayers.Projection(\"EPSG:4326\"),
                units: \"m\",
                numZoomLevels: 18,
                maxResolution: 156543.0339,
                maxExtent: new OpenLayers.Bounds(-20037508, -20037508,
                                                 20037508, 20037508.34)
            };
            
            
              map = new OpenLayers.Map('map', options);

            // create Google Mercator layers
            var gmap = new OpenLayers.Layer.Google(
                \"Google - Map\",
                {'sphericalMercator': true}
            );
            var gsat = new OpenLayers.Layer.Google(
                \"Google - Satellite\",
                {type: G_SATELLITE_MAP, 'sphericalMercator': true, numZoomLevels: 22}
            );
            var ghyb = new OpenLayers.Layer.Google(
                \"Google - Hybrid\",
                {type: G_HYBRID_MAP, 'sphericalMercator': true}
            );
            var gphy = new OpenLayers.Layer.Google(
                \"Google - Physical\",
                {type: G_PHYSICAL_MAP, 'sphericalMercator': true}
            );

            // create Virtual Earth layers
            var veroad = new OpenLayers.Layer.VirtualEarth(
                \"Bing - Map\",
                {'type': VEMapStyle.Road, 'sphericalMercator': true}
            );
            var veaer = new OpenLayers.Layer.VirtualEarth(
                \"Bing - Satellite\",
                {'type': VEMapStyle.Aerial, 'sphericalMercator': true}
            );
            var vehyb = new OpenLayers.Layer.VirtualEarth(
                \"Bing - Hybrid\",
                {'type': VEMapStyle.Hybrid, 'sphericalMercator': true}
            );

            // create Yahoo layer
            var yahoo = new OpenLayers.Layer.Yahoo(
                \"Yahoo - Map\",
                {'sphericalMercator': true}
            );
            var yahoosat = new OpenLayers.Layer.Yahoo(
                \"Yahoo - Satellite\",
                {'type': YAHOO_MAP_SAT, 'sphericalMercator': true}
            );
            var yahoohyb = new OpenLayers.Layer.Yahoo(
                \"Yahoo - Hybrid\",
                {'type': YAHOO_MAP_HYB, 'sphericalMercator': true}
            );

            var olwms = new OpenLayers.Layer.WMS( \"OpenLayers WMS\",
                \"http://labs.metacarta.com/wms/vmap0\", {layers: 'basic'} );
            
            var mapnik = new OpenLayers.Layer.TMS(
                \"OpenStreetMap\",
                \"http://tile.openstreetmap.org/\",
            {
                type: 'png', getURL: osm_getTileURL,
                    displayOutsideMaxExtent: true,
                    attribution: '<a href=\"http://www.openstreetmap.org/\">OpenStreetMap</a>'
            }
            );          

            var kmldata = new OpenLayers.Layer.GML(\"Dendro Data\", \"$xmldata\", 
            {
                'displayInLayerSwitcher' : false,
                projection: new OpenLayers.Projection(\"EPSG:4326\"),
                displayProjection: new OpenLayers.Projection(\"EPSG:4326\"),
                format: OpenLayers.Format.KML, 
                formatOptions: {
                  extractStyles: true, 
                  extractAttributes: true,
                  maxDepth: 2
                  }

                }
            );

 
            

             map.addLayers([gmap, gsat, ghyb, gphy, veroad, veaer, vehyb,
                           yahoo, yahoosat, yahoohyb, olwms, mapnik, kmldata]);
            
            map.addControl(new OpenLayers.Control.LayerSwitcher());


  			map.zoomToMaxExtent();
  			
  			map.setBaseLayer(gphy);
  			
             selectControl = new OpenLayers.Control.SelectFeature(kmldata,
                {onSelect: onFeatureSelect, onUnselect: onFeatureUnselect});
                
                            map.addControl(selectControl);
            selectControl.activate(); 
  			

			}
function osm_getTileURL(bounds) {
    var res = this.map.getResolution();
    var x = Math.round((bounds.left - this.maxExtent.left) / (res * this.tileSize.w));
    var y = Math.round((this.maxExtent.top - bounds.top) / (res * this.tileSize.h));
    var z = this.map.getZoom();
    var limit = Math.pow(2, z);

    if (y < 0 || y >= limit) {
        return OpenLayers.Util.getImagesLocation() + \"404.png\";
} else {
    x = ((x % limit) + limit) % limit;
    return this.url + z + \"/\" + x + \"/\" + y + \".\" + this.type;
}
}

        </script>
    </head>
    <body onload=\"init()\">
        <div id=\"map\" >
                <div id=\"link\" style=\"padding: 2px 2px 2px 2px; position: absolute; top: 140px; left: 13px; color: white; z-index: 99999\"><a href=\"$xmldata\" style=\"font-family: verdana, arial, helvetica, sans-serif; font-size : 10px; 
     \"><img src=\"images/googleearth22x22.png\" border=0></a></div>
        
        </div>

    </body>
</html>";
    
    
    
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
	    map.setCenter(new GLatLng(-200,-200), 5, G_PHYSICAL_MAP); 
        map.addControl(new GLargeMapControl());
	    map.addControl(new GMenuMapTypeControl());
	    map.addControl(new GOverviewMapControl());    
	    map.addMapType(G_PHYSICAL_MAP) ;         
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
      	map.setZoom(5);
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
