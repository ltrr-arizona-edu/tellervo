<?php


include('inc/odkauth.php');
include('inc/odkhelper.php');
require_once('../inc/dbhelper.php');

$odkauth = new ODKAuth();
$mediaStoreFolder = "/usr/share/tellervo-server/mediastore/";




if($odkauth->doAuthentication()===TRUE)
{

}
else
{
	die();
}






if(!isset($_GET['fileid']) || empty($_GET['fileid']))
{
    printError("File ID missing from request", "400 Bad Request");
    die();
}


getFile($_GET['fileid']);

function getFile($id)
{
    global $dbconn;
    global $firebug;
    global $odkauth;
    global $mediaStoreFolder;


    $file = $mediaStoreFolder.$odkauth->getUserID()."/".$id;
    $firebug->log($file, "Filename");

    if(file_exists($file))
    {
	    header('Content-Description: File Transfer');
	    header('Content-Disposition: attachment; filename='.basename($file));
	    header('Content-Transfer-Encoding: binary');
	    header('Expires: 0');
	    header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
	    header('Pragma: public');
	    header('Content-Length: ' . filesize($file));
	    ob_clean();
	    flush();
	    readfile($file);
	    exit;
    }
    else
    {
	printError("File not found", "404 Media file not found");
    }

}


?>


