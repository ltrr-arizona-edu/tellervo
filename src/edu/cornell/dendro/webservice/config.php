<?php
//*******************************************************************
//// PHP Corina Middleware 
//// License: GPL
//// Author: Peter Brewer
//// E-Mail: p.brewer@cornell.edu
////
//// Requirements : PHP >= 5.0
////*******************************************************************

// Only report errors
//error_reporting(E_ERROR);

$debugFlag = false;
$wsversion = "0.1.".exec(svnversion);
$rngSchema = "/var/www/webservice/schemas/corina.rng";

$domain = "dendro.cornell.edu";
$wikiManualFolder = "corina-manual";


?>
