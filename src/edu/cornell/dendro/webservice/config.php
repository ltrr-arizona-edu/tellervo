<?php
//*******************************************************************
//// PHP Corina Middleware 
//// License: GPL
//// Author: Peter Brewer
//// E-Mail: p.brewer@cornell.edu
////
//// Requirements : PHP >= 5.0
////*******************************************************************

//************************
// USER EDITABLE VARIABLES
//************************

// Whether to display debug messages
$debugFlag = false; 

// Version number to be displayed in output headers
$wsversion = "0.1.".exec(svnversion); 

// Path to RelaxNG schema
$rngSchema = "/var/www/webservice/schemas/corina.rng"; 

// Timezone of the server this software is running on
$serverTimezone = "America/New_York";

// Domain name of the server this software is running on
$domain = "dendro.cornell.edu";

// Folder name where the wiki documentation is stored
$wikiManualFolder = "corina-manual";

// Google maps API Key for server
$gMapAPIKey = "ABQIAAAAs0rCgSUzwBX9znK1mNUjuxQ2hntmN47BkGQDV0OcxMEAjHeRKhS83HleB6295GV1FrzHI7MycgtWZg";

//************************




date_default_timezone_set($serverTimezone); // Default time zone 

?>
