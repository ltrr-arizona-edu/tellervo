<?php

function gMapDataFromXML($xmlstring)
{
    $returnString = "// Creates a marker at the given point 
                    function createMarker(point, popup) 
                    {
                        var marker = new GMarker(point, iconred);
                        GEvent.addListener(marker, \"click\", function() {
                        marker.openInfoWindowHtml(popup);
                        });
                        return marker;
                    }";

    $xmldata = simplexml_load_string($xmlstring);

    if ($xmldata->xpath('//tree'))
    {
        foreach($xmldata->xpath('//tree') as $tree)
        {
           if ((isset($tree->latitude)) && (isset($tree->longitude)))
           {
               $returnString.= "\nvar point = new GLatLng(".$tree->latitude.", ".$tree->longitude.");\n";
               $htmlString = "<b>".$tree->validatedTaxon."</b><br>";
               $returnString.= "map.addOverlay(createMarker(point,'$htmlString'));\n\n";
           }
        }
    }
    
    if ($xmldata->xpath('//site'))
    {
        foreach($xmldata->xpath('//site') as $site)
        {
           if ((isset($site->extent[minLat])) && (isset($site->extent[maxLat])) && (isset($site->extent[minLong])) && (isset($site->extent[maxLong])))
           {
                $returnString .= "\nvar polygon = new GPolygon([";
                $returnString .= "new GLatLng(".$site->extent[minLat].",".$site->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[maxLat].",".$site->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[maxLat].",".$site->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[minLat].",".$site->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$site->extent[minLat].",".$site->extent['minLong'].")";
                $returnString .= "], \"#ff0000\", 1, 1, \"#FF0000\", 0.3);\n";
                $returnString .= "map.addOverlay(polygon);\n\n"; 
           }
        }
    }
    
    if ($xmldata->xpath('/content/measurement'))
    {
        foreach($xmldata->xpath('/content/measurement') as $measurement)
        {
           if ((isset($measurement->extent[minLat])) && (isset($measurement->extent[maxLat])) && (isset($measurement->extent[minLong])) && (isset($measurement->extent[maxLong])))
           {
                $returnString .= "\nvar polygon = new GPolygon([";
                $returnString .= "new GLatLng(".$measurement->extent[minLat].",".$measurement->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$measurement->extent[maxLat].",".$measurement->extent['minLong']."), ";
                $returnString .= "new GLatLng(".$measurement->extent[maxLat].",".$measurement->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$measurement->extent[minLat].",".$measurement->extent['maxLong']."), ";
                $returnString .= "new GLatLng(".$measurement->extent[minLat].",".$measurement->extent['minLong'].")";
                $returnString .= "], \"#ff0000\", 1, 1, \"#FF0000\", 0.3);\n";
                $returnString .= "map.addOverlay(polygon);\n\n"; 
           }
        }
    }
    
    return $returnString;

}

function gMapExtentFromXML($xmlstring, $type)
{
    $minLat = 10000;
    $minLong = 10000;
    $maxLat = -10000;
    $maxLong = -10000;
    
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
    
    if ($xmldata->xpath('/content/measurement'))
    {
        foreach($xmldata->xpath('/content/measurement') as $measurement)
        {
           if ((isset($measurement->extent[minLat])) && (isset($measurement->extent[maxLat])) && (isset($measurement->extent[minLong])) && (isset($measurement->extent[maxLong])))
           {
               if ( $measurement->extent[maxLat]  > $maxLat ) $maxLat  = (float) $measurement->extent[maxLat];
               if ( $measurement->extent[maxLong] > $maxLong) $maxLong = (float) $measurement->extent[maxLong];
               if ( $measurement->extent[minLat]  < $minLat ) $minLat  = (float) $measurement->extent[minLat];
               if ( $measurement->extent[minLong] < $minLong) $minLong = (float) $measurement->extent[minLong];
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
