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

?>