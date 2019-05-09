<?php


include('odkauth.php');
include('odkhelper.php');
$odkauth = new ODKAuth();



if($odkauth->doAuthentication()===TRUE)
{

}
else
{
	die();
}






if(!isset($_GET['id']) || empty($_GET['id']))
{
    printError("Form ID missing from request", "400 Bad Request");
    die();
}
$firebug->log($_GET['id']);


getForm($_GET['id']);

function getForm($id)
{
    global $dbconn;
    global $firebug;
    global $odkauth;
    $sql = "select * from tblodkdefinition where odkdefinitionid = '".pg_escape_string($id)."' AND (ispublic = true OR ownerid='".$odkauth->getUserID()."')";
	$firebug->log($sql);

    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        if (pg_num_rows($result)==0)
        {
	    $firebug->log("no forms");
	    printError("Form with specified ID does not exist or you do not have permission to access it.");
	    die();
        }


	header('HTTP/1.1 200 OK');
	header('X-OpenRosa-Version: 1.0');
	header('Date: '.date("r"));
	header('Content-Type: text/xml');
	header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
	header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past


	echo "<?xml version='1.0' encoding='UTF-8' ?>";
	

	while ($row = pg_fetch_array($result))   
	{
		echo $row['definition'];
	}

    }
}


?>


