<?php

function gMapDataFromXML($xmlstring)
{
    $returnString = "
// Creates a marker at the given point 
function createMarker(point, popup) 
{
    var marker = new GMarker(point, iconred);
    GEvent.addListener(marker, \"click\", function() {
    marker.openInfoWindowHtml(popup);
});
return marker;
}\n";

    $xmldata = simplexml_load_string($xmlstring);

    $returnString.="\n// Add the actual data\n";
    if ($xmldata->xpath('//tree'))
    {
        foreach($xmldata->xpath('//tree') as $tree)
        {
           if ((isset($tree->latitude)) && (isset($tree->longitude)))
           {
               $returnString.= "var point = new GLatLng(".$tree->latitude.", ".$tree->longitude.");\n";
               $htmlString = "<b>".$tree->validatedTaxon."</b><br>";
               $returnString.= "map.addOverlay(createMarker(point,'$htmlString'));\n";
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
                $htmlString = "<b>".$site->name."</b><br>";
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
