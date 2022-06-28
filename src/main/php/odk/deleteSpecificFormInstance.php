<?php
$mediaStoreFolder = "/usr/share/tellervo-server/mediastore/";
require_once('../systemconfig.php');
require_once('../config.php');
require_once("../inc/errors.php");
require_once("../inc/auth.php");
require_once("../inc/meta.php");

try{
	require_once("../config.php");
	require_once("../inc/dbsetup.php");
} catch (Exception $e)
{
	$meta->setMessage("704", 'Tellervo server configuration file missing.  Contact your systems administrator');
}

try{
	require_once("../systemconfig.php");
} catch (Exception $e)
{
	$meta->setMessage("704", 'System configuration file missing.  Server administrator needs to run tellervo-server --reconfigure', E_USER_ERROR);
}


require_once("../inc/output.php");

//$odkauth = new ODKAuth();
$myAuth         = new auth();
$meta		= new meta();


if($myAuth->isLoggedIn===TRUE)
{
	$firebug->log("Logged in");
}
else
{
        // Not logged in
        $seq = $myAuth->sequence();
        $meta->requestLogin($myAuth->nonce($seq), $seq, 'Error');
        writeOutput($myMetaHeader, " ");
        die();
}

// ok, valid username & password


deleteFormList();
    writeOutput($meta);

function deleteFormList()
{
    global $dbconn;
    global $firebug;
    global $domain;
    global $securehttp;
    global $myAuth;
    global $meta;
    global $mediaStoreFolder;

    $sql = "DELETE FROM tblodkinstance WHERE ownerid IN (SELECT securityuserid from tblsecurityuser where securityuserid='".pg_escape_string($myAuth->getID())."' and isactive=TRUE)";
	$firebug->log($sql);

    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        pg_send_query($dbconn, $sql);
	$res = pg_get_result($dbconn);
	$err = pg_result_error($res);
	if(!$err=="")
	{
		$firebug->log("Error in sql ");
		$firebug->log($err);
		$meta->setMessage("000", $err);
		$xmldata = "<hello/>";
    		writeOutput($meta, $xmldata);
		die();
	}
	
	$media = $mediaStoreFolder.$myAuth->getID();
	$firebug->log("Deleting ".$media);
	rrmdir($media);
	
    }
    else
    {
	$meta->setMessage("000", "DB connection error");
	$xmldata = "<hello/>";
 	writeOutput($meta, $xmldata);
	die();

    }


}

 function rrmdir($dir) { 
   if (is_dir($dir)) { 
     $objects = scandir($dir); 
     foreach ($objects as $object) { 
       if ($object != "." && $object != "..") { 
         if (is_dir($dir."/".$object))
           rrmdir($dir."/".$object);
         else
           unlink($dir."/".$object); 
       } 
     }
     rmdir($dir); 
   } 
 }



