<?php
$bibfile = "/home/aps03pwb/labpublications.bib";
include_once($basepath."phpinc/OSBiB/LOADSTYLE.php");
include_once($basepath."phpinc/OSBiB/format/basicoutput.php");
$styles = LOADSTYLE::loadDir("/var/www/website/phpinc/OSBiB/styles/bibliography");
$styleKeys = array_keys($styles);
$useStyle = $styleKeys[3];
$dendroBib= new DendroBib($useStyle);
$dendroBib->execute();

?>
