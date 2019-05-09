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

if(!isset($_GET['id']) || empty($_GET['id']))
{
    printError("Form ID missing from request", "400 Bad Request");
    die();
}



// ok, valid username & password


deleteForm($_GET['id']);
echo "Success";

function deleteForm($id)
{
    global $dbconn;
    global $firebug;
    global $odkauth;


    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {

	$res = pg_query_params($dbconn,"DELETE FROM tblodkdefinition WHERE odkdefinitionid=$1 AND ownerid IN (SELECT securityuserid FROM tblsecurityuser WHERE securityuserid=$2 AND isactive=TRUE)", array($id, $odkauth->getUserID()) );
	$err = pg_result_error($res);
	if(!$err=="")
	{
		printError("Failed");
		die();
	}

	if(pg_num_rows($res)==1)
	{

	}
	else
	{
		printError("User=".$odkauth->getUserID().", form definition is ".$id.", Number of rows is ".pg_num_rows($res));
		die();

	}
	
    }
    else
    {
		printError("DB connection error");
		die();
    }


}




