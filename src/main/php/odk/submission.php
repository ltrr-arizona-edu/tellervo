<?php
file_put_contents('/tmp/headers.txt', "\n\n Submission.php called \n", FILE_APPEND);
include('inc/odkauth.php');
include('inc/odkhelper.php');
require_once('../inc/dbhelper.php');
$odkauth = new ODKAuth();
$mediaStoreFolder = "/usr/share/tellervo-server/mediastore/";

function endsWith($haystack, $needle) {
    // search forward starting from end minus needle length characters
    return $needle === "" || (($temp = strlen($haystack) - strlen($needle)) >= 0 && strpos($haystack, $needle, $temp) !== FALSE);
}


if($odkauth->doAuthentication()===TRUE)
{

}
else
{
	die();
}

$deviceid = $_GET['deviceID'] or printError("Device ID is missing", "400 Device id is missing");
$filearray = array();

$resp=201;

// Create media folder for user if it doesn't already exist
if (!file_exists($mediaStoreFolder."/".$odkauth->getUserID())) {
    mkdir($mediaStoreFolder."/".$odkauth->getUserID(), 0777, true);
}



if( $_SERVER['REQUEST_METHOD']==="HEAD") $resp=204;
elseif( $_SERVER['REQUEST_METHOD']==="POST")
{
	$tmpname = $_FILES['xml_submission_file']['tmp_name'];
	$name = $_FILES['xml_submission_file']['name'];
	//file_put_contents('/tmp/headers.txt', "Temp name : ".$tmpname."\n", FILE_APPEND);
	//file_put_contents('/tmp/headers.txt', "Name : ".$name."\n", FILE_APPEND);
	libxml_use_internal_errors(true);
	//if(!$file_exists($tmpname))  printError("XML file not found");
	$xml=simplexml_load_file($tmpname);
	if($xml === false) {
		$errormsgs= "";
		foreach(libxml_get_errors() as $error){
			$errormsgs.="\t".$error->message;
		}
		file_put_contents('/tmp/headers.txt', "XML parse errors: ".$errormsgs."\n", FILE_APPEND);
		printError("Unable to parse ODK XML file.\n".$errormsgs, "400 XML error");
	}
	$instanceName = $xml->meta->instanceName or printError("Unable to parse ODK XML file", "400 XML parse failed");

  	file_put_contents('/tmp/headers.txt', "\n\nInstance name: ".$instanceName."\n", FILE_APPEND);
  	file_put_contents('/tmp/headers.txt', "\n".$xml->asXML()."\n", FILE_APPEND);
	
	
	foreach( $_FILES as $file)
	{
		if(endsWith($file['name'], "xml")) continue;

		switch($file['error'])
		{
			case UPLOAD_ERR_OK:
				// Upload fine
				break;

			case UPLOAD_ERR_INI_SIZE:
		                printError("The uploaded file exceeds the upload_max_filesize directive in php.ini", "413 Media file too large");
				break; 
			case UPLOAD_ERR_FORM_SIZE:
				$message = "The uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the HTML form";
		                printError($message, "413 Media file too large");
				break; 
			case UPLOAD_ERR_PARTIAL:
				$message = "The uploaded file was only partially uploaded";
		                printError($message, "400 Media file upload failure");
				break; 
			case UPLOAD_ERR_NO_FILE:
				$message = "No file was uploaded";
		                printError($message, "400 Media file upload failure");
				break; 
			case UPLOAD_ERR_NO_TMP_DIR:
				$message = "Missing a temporary folder";
		                printError($message, "500".$message);
				break; 
			case UPLOAD_ERR_CANT_WRITE:
				$message = "Failed to write file to disk";
		                printError($message, "500".$message);
				break; 
			case UPLOAD_ERR_EXTENSION:
				$message = "File upload stopped by extension";
		                printError($message, "500".$message);
				break; 
			default:
				$message = "Unknown upload error";
		                printError($message, "500".$message);
				break;
				
		}

		

		$arraycontents = print_r($file, true);
  		file_put_contents('/tmp/headers.txt', $arraycontents."\n", FILE_APPEND);

			
  		//file_put_contents('/tmp/headers.txt', "Copying file: ".$file['name']." to temp folder\n", FILE_APPEND);
		//move_uploaded_file( $file['tmp_name'], $mediastorefolder.$file['name']) or printError("Failed to copy media file", "400 Bad request");
		$currentname = $file['tmp_name'];
		$storedname = $mediaStoreFolder.$odkauth->getUserID()."/".$file['name']; 
		file_put_contents('/tmp/headers.txt', "Current filename : ".$currentname."\n", FILE_APPEND);
		file_put_contents('/tmp/headers.txt', "Stored filename : ".$storedname."\n", FILE_APPEND);

		// Fail if media file already exists
		/*if (file_exists($storedname))
		{
			printError("Failed to copy media file", "500 Media file already exists");
		}*/
		move_uploaded_file($currentname, $storedname) or printError("Failed to copy media file", "500 Media file copy fail");
		$filearray[] = $storedname;

	}

	writeInstanceToDB($instanceName, $xml->asXML(), $filearray);
}

function writeInstanceToDB($instanceName, $instance, $fileArray)
{
    global $firebug;
    global $dbconn;
    global $deviceid;
    global $odkauth;

    $sql = "INSERT INTO tblodkinstance (deviceid, ownerid, name, instance, files) values (";

    $sql.=dbHelper::tellervo_pg_escape_string($deviceid).", ";
    $sql.=dbHelper::tellervo_pg_escape_string($odkauth->getUserID()).", ";
    $sql.=dbHelper::tellervo_pg_escape_string($instanceName).", ";
    $sql.=dbHelper::tellervo_pg_escape_string($instance).", ";
    $sql.=dbHelper::phpArrayToPGStrArray($fileArray); 
    $sql.=")";
  
   file_put_contents('/tmp/headers.txt', "SQL : ".$sql."\n", FILE_APPEND);

    if ($sql)
    {
       // Run SQL 
       pg_send_query($dbconn, $sql);
       $result = pg_get_result($dbconn);
       if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE)) printError(pg_result_error($result), "400 Bad request");

    }



}



header( "X-OpenRosa-Version: 1.0");
header( "X-OpenRosa-Accept-Content-Length: 2000000");
header( "Date: ".date('r'), false, $resp);

?>
<OpenRosaResponse xmlns="http://openrosa.org/http/response">
        <message nature="submit_success">Thanks</message>
</OpenRosaResponse> 

