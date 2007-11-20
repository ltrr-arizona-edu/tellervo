<?php
include_once('post.php');

function directoryToArray($directory, $recursive) 
{
    $array_items = array();
    if ($handle = opendir($directory)) 
    {
        while (false !== ($file = readdir($handle))) 
        {
            if ($file != "." && $file != "..") 
            {
                if (is_dir($directory. "/" . $file)) 
                {
                    if($recursive) 
                    {
                        $array_items = array_merge($array_items, directoryToArray($directory. "/" . $file, $recursive));
                    }
                    //$file = $directory . "/" . $file;
                    //$array_items[] = preg_replace("/\/\//si", "/", $file);
                } 
                else 
                {
                    if ($file=='SITE.XML')
                    {
                        $file = $directory . "/" . $file;
                        $array_items[] = preg_replace("/\/\//si", "/", $file);
                    }
                
                }
            }
        }
        closedir($handle);
    }
    return $array_items;
}


$files = directoryToArray("/dendrodata/DATA/", true);

echo '<ul>';

$sitesarray = array();

login("peter", "codatanl");


foreach ($files as $file) 
{
    $handle = fopen($file, "r");
    $contents = fread($handle, filesize($file));
    fclose($handle);


    $xml = new SimpleXMLElement(iconv("windows-1251", "UTF-8", $contents));
    $code = $xml->identity->code;

    $locfile = substr($file, 0, -3)."LOC";
    $lines = @file($locfile);
    $lat = substr($lines[0],5);
    $long = substr($lines[1],6);

    echo "<li>".$code .": ".substr($lines[0],5).", ".substr($lines[1],6)."</li>";
    $xmlrequest="<corina><request type=\"create\"><site name=\"".$code."\" code=\"".$code."\" latitude=\"".$lat."\" longitude=\"".$long."\" /></request></corina>";
    echo "<li>".postData($xmlrequest, "sites.php");
    echo "</li>";

}

echo '</ul>';
?>
