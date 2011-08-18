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
require_once("inc/meta.php");
require_once("inc/output.php");

$myMetaHeader = new meta("help");
writeIntroOutput($myMetaHeader);


?>
