<?php

include('inc/odkauth.php');
include('inc/odkhelper.php');
$odkauth = new ODKAuth();

if($odkauth->doAuthentication()===TRUE)
{

}
else
{
	die();
}

// ok, valid username & password

header('HTTP/1.1 200 OK');
header('X-OpenRosa-Version: 1.0');
header('Date: '.date("r"));
header('Content-Type: text/xml');
header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past


echo "<?xml version='1.0' encoding='UTF-8' ?>
<xforms xmlns=\"http://openrosa.org/xforms/xformsList\">\n";

getFormList($odkauth->getUsername());

function getFormList($theUsername)
{
    global $dbconn;
    global $firebug;
    global $domain;
    global $securehttp;

    $sql = "select * from tblodkdefinition where ownerid = (SELECT securityuserid from tblsecurityuser where username='".pg_escape_string($theUsername)."' and isactive=true)  OR ispublic = true";
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
		echo "  <xform>\n";
		echo "    <formID>";
		echo $row['odkdefinitionid'];
		echo "</formID>\n";
		echo "    <name>".$row['name']."</name>\n";
		echo "    <hash>md5:".md5($row['definition'])."</hash>\n";
		
		$folder = substr($_SERVER['REQUEST_URI'], 0, -13);

		if($securehttp===TRUE)
		{
			echo "    <downloadUrl>https://".$_SERVER['HTTP_HOST'].$folder."/odk/forms?id=".$row['odkdefinitionid']."</downloadUrl>\n";
		}
		else
		{
			echo "    <downloadUrl>http://".$_SERVER['HTTP_HOST'].$folder."/odk/forms?id=".$row['odkdefinitionid']."</downloadUrl>\n";
		}
		echo "  </xform>\n";
	}

    }



}

echo"</xforms>";



