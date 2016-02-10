<?php


//include('inc/odkauth.php');
include('inc/odkhelper.php');
require_once('../inc/dbhelper.php');

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


require_once("../inc/auth.php");
        require_once('../inc/FirePHPCore/FirePHP.class.php');
        $firebug = FirePHP::getInstance(true);



//$odkauth = new ODKAuth();


$myAuth         = new auth();


$mediaStoreFolder = "/usr/share/tellervo-server/mediastore/";




if($myAuth->isLoggedIn===TRUE)
{

}
else
{
	$firebug->log("Not logged in");
	printError("Not logged in");
}

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
	
	


    $sql = "SELECT * FROM tblodkinstance WHERE ownerid = (SELECT securityuserid from tblsecurityuser where securityuserid='".pg_escape_string($myAuth->getID())."' and isactive=true)";
    
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
		$fa = dbHelper::pgStrArrayToPHPArray($row['files']);
		foreach($fa as $fileref)
		{
			$mediafile = substr($fileref, 1, -1);
			
			if(file_exists($mediafile))
			{
				$firebug->log($mediafile, "Media file");
				$zip->addFile($mediafile, basename($mediafile));
			}
			else
			{
				$firebug->log($mediafile, "Media file DOES NOT EXIST!");
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
	    header('Content-Description: File Transfer');
	    header('Content-Disposition: attachment; filename='.basename($zipfile));
	    header('Content-Transfer-Encoding: binary');
	    header('Expires: 0');
	    header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
	    header('Pragma: public');
	    header('Content-Length: ' . filesize($zipfile));
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


