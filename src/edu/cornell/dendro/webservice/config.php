<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This class contains the logic for extracting the meaning from the
 * user request and for storing these requests in parameter classes.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
 * *******************************************************************
 */

//************************
// USER EDITABLE VARIABLES
//************************

// Database name
$dbName = corina_dev;

// Domain name of the server this software is running on
$domain = "dendro.cornell.edu/dev/";

// Whether to display debug messages
$debugFlag = FALSE; 

// Version number to be displayed in output headers
$wsversion = "0.2.".exec(svnversion); 

// Path to RelaxNG schema
$rngSchema = "/var/www/corina-webservice/dev/schemas/corina.rng"; 

// Path to Corina XSD
$corinaXSD = "/var/www/corina-webservice/dev/schemas/corina.xsd";

// Web accessible temporary folder
$tempFolder = "/var/www/website/corinaMSTemp/";

// Base URL of web accessible temporary folder
$tempFolderURL = "http://dendro.cornell.edu/corinaMSTemp/";

// Corina Namespace URL
$corinaNS = "http://dendro.cornell.edu/schema/corina/1.0";

// TRiDaS Namespace URL
$tridasNS = "http://www.tridas.org/1.1";

// TRiDaS Namespace URL
$gmlNS = "http://www.opengis.net/gml";

// The name and edition of the taxonomic authority used for taxonomy 
$taxonomicAuthorityEdition = "Catalogue of Life Annual Checklist 2008";

// Timezone of the server this software is running on
$serverTimezone = "America/New_York";

// Folder name where the wiki documentation is stored
$wikiManualBaseUrl = "http://dendro.cornell.edu/corina-manual";

// Page name where main webservice is run from
$wspage = "webservice.php";

// Page name where mapping service is run from
$mspage = "mapservice.php";

// Google maps API Key for server
$gMapAPIKey = "ABQIAAAAs0rCgSUzwBX9znK1mNUjuxQ2hntmN47BkGQDV0OcxMEAjHeRKhS83HleB6295GV1FrzHI7MycgtWZg";

// Postgres credentials file location
$cdbCredentialsFile = "/home/aps03pwb/.corina_server_credentials";

// Array of known clients and the minimum version that can be used when accessing this webservice
$corinaClientIdentifiers = array( 
								array("name" => "Corina WSI", 	"minVersion" => "2.03"), 
								array("name" => "Firefox", 		"minVersion" => "1.0"),  
								array("name" => "Safari",		"minVersion" => "1.0"),
								array("name" => "Gecko", 		"minVersion" => "1.0")
								);
								
// Only accept known clients								
$onlyAllowKnownClients = TRUE;


//************************




date_default_timezone_set($serverTimezone); // Default time zone 

?>
