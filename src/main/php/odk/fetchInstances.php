<?php

include('inc/odkhelper.php');
require_once('../inc/dbhelper.php');
require_once('../inc/output.php');
require_once('../inc/meta.php');
require_once("../inc/auth.php");
require_once('../inc/FirePHPCore/FirePHP.class.php');

$myAuth         = new auth();
$myMetaHeader   = new meta();
$firebug = FirePHP::getInstance(true);
$mediaStoreFolder = "/usr/share/tellervo-server/mediastore/";
		
try{
require_once("../config.php");
require_once("../inc/dbsetup.php");
} catch (Exception $e)
{
	trigger_error('704'.'Tellervo server configuration file missing.  Contact your systems administrator');
}

try{
	require_once("../systemconfig.php");
} catch (Exception $e)
{
	trigger_error('704'.'System configuration file missing.  Server administrator needs to run tellervo-server --reconfigure', E_USER_ERROR);
}

if($myAuth->isLoggedIn===TRUE)
{

}
else
{
        // Not logged in
        $seq = $myAuth->sequence();
        $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq, 'Error');
        writeOutput($myMetaHeader, " ");
        die();
}

//file_put_contents('/tmp/headers.txt', "Getting form instances...\n", FILE_APPEND);

$zipfile = createZipFile();

getFile($zipfile);

function createZipFile()
{
    global $dbconn;
    global $firebug;
    global $domain;
    global $securehttp;
    global $myAuth;
    global $mediaStoreFolder;

	$zip = new ZipArchive();
	$temp = tempnam("/tmp", "odk-").".zip";
//	$temp = "/tmp/test.zip";

	$firebug->log($temp, "Temp name");
	if($zip->open($temp, ZipArchive::CREATE)!==TRUE){
		printError("Cannot create zip file");
	}
	
	


    $sql = "SELECT *, array_to_string(files, '><'::text) AS filearr FROM tblodkinstance WHERE ownerid = (SELECT securityuserid from tblsecurityuser where securityuserid='".pg_escape_string($myAuth->getID())."' and isactive=true)";
    
    $firebug->log($sql);

    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        if (pg_num_rows($result)==0)
        {
            echo "";
	    $firebug->log("no forms");
	    return;
        }
	

	while ($row = pg_fetch_array($result))   
	{
		$firebug->log($row['instance'], "Code");
	
		$zip->addFromString($row['odkinstanceid'].".xml", $row['instance']);	
                //file_put_contents('/tmp/headers.txt', "File field from db ".$row['filearr']."\n", FILE_APPEND);
		$fa = dbHelper::pgStrArrayToPHPArray($row['filearr']);
		//$arrcontents = print_r($fa, true);

                //file_put_contents('/tmp/headers.txt', "File array ".$fa."\n", FILE_APPEND);
                //file_put_contents('/tmp/headers.txt', "File array contents ".$arrcontents."\n", FILE_APPEND);


		foreach($fa as $mediafile)
		{
                	//file_put_contents('/tmp/headers.txt', "File ".$fileref."\n", FILE_APPEND);
			//$mediafile = substr($fileref, 1, -1);
			
			if(file_exists($mediafile))
			{
				$firebug->log($mediafile, "Media file");
                		//file_put_contents('/tmp/headers.txt', "File exists\n", FILE_APPEND);
				$zip->addFile($mediafile, basename($mediafile));
			}
			else
			{
				$firebug->log($mediafile, "Media file DOES NOT EXIST!");
                		//file_put_contents('/tmp/headers.txt', "File DOES NOT EXIST\n", FILE_APPEND);
			}
		}
	}

    }
   
    $zip->close();

    $firebug->log($temp, "Zip file name");
    return $temp;

}



function getFile($zipfile)
{
    global $dbconn;
    global $firebug;
    global $myAuth;
    global $mediaStoreFolder;


    $firebug->log($zipfile, "Filename");

    if(file_exists($zipfile))
    {
	    $filesize = filesize($zipfile);
        header('Content-Description: File Transfer');
        header('Content-Disposition: attachment; filename='.basename($zipfile));
        header('Content-Transfer-Encoding: binary');
        header('Expires: 0');
        header('Content-Type: application/zip, application/octet-stream');
        header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
        header('Pragma: public');
        header('Content-Length: '.$filesize);
        header("Content-Range: 0-".($filesize-1)."/".$filesize);
	    ob_clean();
	    flush();
	    readfile($zipfile);
        unlink($zipfile);
	    exit;
    }
    else
    {
	printError("File not found", "404 Zip file not found");
    }

}


?>


