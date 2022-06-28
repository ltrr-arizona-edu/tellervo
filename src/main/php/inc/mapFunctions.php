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

function incGMapScript($script)
{

    $jsf_drawCircle = "function drawCircle(center, radius, nodes, liColor, liWidth, liOpa, fillColor, fillOpa)
    {
        // Calculate km/degree
        var latConv = center.distanceFrom(new GLatLng(center.lat()+0.1, center.lng()))/100;
        var lngConv = center.distanceFrom(new GLatLng(center.lat(), center.lng()+0.1))/100;
        
        //Loop 
        var points = [];
        var step = parseInt(360/nodes)||10;
        for(var i=0; i<=360; i+=step)
        {
            var pint = new GLatLng(center.lat() + (radius/latConv * Math.cos(i * Math.PI/180)), center.lng() + 
                (radius/lngConv * Math.sin(i * Math.PI/180)));
            points.push(pint);
            bounds.extend(pint); //this is for fit function
        }
        fillColor = fillColor||liColor||\"#0055ff\";
        liWidth = liWidth||2;
        var poly = new GPolygon(points,liColor,liWidth,liOpa,fillColor,fillOpa);
        map.addOverlay(poly);
    }\n\n";

    $jsf_createMarker = "function createMarker(point, popup) 
    {
        var marker = new GMarker(point, iconred);
        GEvent.addListener(marker, \"click\", function() {
        marker.openInfoWindowHtml(popup);
    });
    return marker;
    }\n\n";

    switch($script)
    {
    case "drawCircle":
        return $jsf_drawCircle;
    case "createMarker":
        return $jsf_createMarker;
    default:
        return false;
    }

}

//*******************************************


function gMapDataFromXML($xmlstring)
{
    
    // Load the XML data into a simple xml string
    $xmldata = simplexml_load_string($xmlstring);
    
    // Creates a marker at the given point 
    $returnString .= incGMapScript("createMarker");

    // Include drawCircle function for showing precision
    $returnString .= incGMapScript("drawCircle");

    // Include the actual data for GMap
    $returnString.="\n// Add the actual data\n";
    if ($xmldata->xpath('//tree'))
    {
        foreach($xmldata->xpath('//tree') as $tree)
        {
           if ((isset($tree->latitude)) && (isset($tree->longitude)))
           {
               $returnString.= "var point = new GLatLng(".$tree->latitude.", ".$tree->longitude.");\n";
               $htmlString = "<b>".$tree->parentSummary->fullLabCode."</b><br><br><font size=-1>".$tree->validatedTaxon."</font>";
               $returnString.= "map.addOverlay(createMarker(point,'$htmlString'));\n";
               if($tree->precision>0)
               {
                   $precision = $tree->precision/1000;
                   $returnString.= "drawCircle(point, $precision, 40, 'black', 1, 0.2, 'black', 0.15);\n";
               }
           }
        }
    }
    
    
    if ($xmldata->xpath('//site'))
    {
        foreach($xmldata->xpath('//site') as $site)
        {
           if ((isset($site->extent[minLat])) && (isset($site->extent[maxLat])) && (isset($site->extent[minLong])) && (isset($site->extent[maxLong])))
           {
                $returnString .= "var polygon = new GPolygon([";
                $returnString .= "new GLatLng(".$site->extent[minLat].",".$site->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[maxLat].",".$site->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[maxLat].",".$site->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[minLat].",".$site->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[minLat].",".$site->extent['minLong'].")";
                $returnString .= "], \"#ff0000\", 1, 1, \"#FF0000\", 0.3);\n";
                $returnString .= "map.addOverlay(polygon);\n"; 
                $returnString .= "var point = new GLatLng(".$site->extent['centroidLat'].", ".$site->extent['centroidLong'].");\n";
                $htmlString = "<b>".escapeXMLChars($site->name)." (".$site->code.")</b><br>";
                $returnString.= "map.addOverlay(createMarker(point,'$htmlString'));\n";
           }
        }
    }
    
    if ($xmldata->xpath('content/measurement'))
    {
        foreach($xmldata->xpath('content/measurement') as $measurement)
        {
           if ((isset($measurement->metadata->extent[minLat])) && (isset($measurement->metadata->extent[maxLat])) && (isset($measurement->metadata->extent[minLong])) && (isset($measurement->metadata->extent[maxLong])))
           {
                $returnString .= "var polygon = new GPolygon([";
                $returnString .= "new GLatLng(".$measurement->metadata->extent[minLat].",".$measurement->metadata->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$measurement->metadata->extent[maxLat].",".$measurement->metadata->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$measurement->metadata->extent[maxLat].",".$measurement->metadata->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$measurement->metadata->extent[minLat].",".$measurement->metadata->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$measurement->metadata->extent[minLat].",".$measurement->metadata->extent['minLong'].")";
                $returnString .= "], \"#ff0000\", 1, 1, \"#FF0000\", 0.3);\n";
                $returnString .= "map.addOverlay(polygon);\n"; 
                $returnString .= "var point = new GLatLng(".$measurement->metadata->extent['centroidLat'].", ".$measurement->metadata->extent['centroidLong'].");\n";
                $htmlString = "<b>".$measurement->metadata->name."</b><br>";
                $returnString.= "map.addOverlay(createMarker(point,'$htmlString'));\n";
           }
        }
    }
    
    return $returnString;

}

function gMapExtentFromXML($xmlstring, $type)
{
    $minLat = 90;
    $minLong = 180;
    $maxLat = -90;
    $maxLong = -180;
    
    $xmldata = simplexml_load_string($xmlstring);
    
    if ($xmldata->xpath('//tree'))
    {
        foreach($xmldata->xpath('//tree') as $tree)
        {
           if ((isset($tree->latitude)) && (isset($tree->longitude)))
           {
               if ( $tree->latitude  > $maxLat ) $maxLat  = (float) $tree->latitude;
               if ( $tree->longitude > $maxLong) $maxLong = (float) $tree->longitude;
               if ( $tree->latitude  < $minLat ) $minLat  = (float) $tree->latitude;
               if ( $tree->longitude < $minLong) $minLong = (float) $tree->longitude;
           }
        }
    }
    
    if ($xmldata->xpath('//site'))
    {
        foreach($xmldata->xpath('//site') as $site)
        {
           if ((isset($site->extent[minLat])) && (isset($site->extent[maxLat])) && (isset($site->extent[minLong])) && (isset($site->extent[maxLong])))
           {
               if ( $site->extent[maxLat]  > $maxLat ) $maxLat  = (float) $site->extent[maxLat];
               if ( $site->extent[maxLong] > $maxLong) $maxLong = (float) $site->extent[maxLong];
               if ( $site->extent[minLat]  < $minLat ) $minLat  = (float) $site->extent[minLat];
               if ( $site->extent[minLong] < $minLong) $minLong = (float) $site->extent[minLong];
           }

        }
    }
    
    if ($xmldata->xpath('//measurement'))
    {
        foreach($xmldata->xpath('//measurement') as $measurement)
        {
           if ((isset($measurement->metadata->extent[minLat])) && (isset($measurement->metadata->extent[maxLat])) && (isset($measurement->metadata->extent[minLong])) && (isset($measurement->metadata->extent[maxLong])))
           {
               if ( $measurement->metadata->extent[maxLat]  > $maxLat ) $maxLat  = (float) $measurement->metadata->extent[maxLat];
               if ( $measurement->metadata->extent[maxLong] > $maxLong) $maxLong = (float) $measurement->metadata->extent[maxLong];
               if ( $measurement->metadata->extent[minLat]  < $minLat ) $minLat  = (float) $measurement->metadata->extent[minLat];
               if ( $measurement->metadata->extent[minLong] < $minLong) $minLong = (float) $measurement->metadata->extent[minLong];
           }

        }
    }

    switch ($type)
    {
        case "minLat":
            return $minLat;
        case "minLong":
            return $minLong;
        case "maxLat":
            return $maxLat;
        case "maxLong":
            return $maxLong;
        default:
            return false;
    }


}

    

?>
