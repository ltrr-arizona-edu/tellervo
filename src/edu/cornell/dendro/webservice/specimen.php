<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/meta.php");

header('Content-Type: application/xhtml+xml; charset=utf-8');


$myMetaHeader = new meta();
$myMetaHeader->setUser("Guest", "", "");


echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<data>\n";

echo "</data>\n";
echo "</corina>\n";
?>
