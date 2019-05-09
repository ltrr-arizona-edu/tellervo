<?php
// This prints file full path and name
//	echo "This file full path and file name is '" . __FILE__ . "'.\n";

	// This prints file full path, without file name
//	echo "This file full path is '" . __DIR__ . "'.\n";


	require_once ('../config.php');
	global $baseFolder; 
	$baseFolder = "/var/www/tellervo-servers/dev/";

	require_once($baseFolder.'/inc/FirePHPCore/FirePHP.class.php');
	$firebug = FirePHP::getInstance(true);
	try{
	require_once($baseFolder.'/config.php');
	require_once($baseFolder."/inc/dbsetup.php");
	} catch (Exception $e)
	{
		trigger_error('704'.'Tellervo server configuration file missing.  Contact your systems administrator');
	}

	try{
		require_once("$baseFolder/systemconfig.php");
	} catch (Exception $e)
	{
		trigger_error('704'.'System configuration file missing.  Server administrator needs to run tellervo-server --reconfigure', E_USER_ERROR);
	}

class ODKAuth 
{
	var $usrname = "";
	var $usrid = "";

	function getDBPwd($theUsername)
	{
	    global $dbconn;
	    global $firebug;
	    $sql = "select * from tblsecurityuser where username='".pg_escape_string($theUsername)."' and isactive=true";

	    $dbconnstatus = pg_connection_status($dbconn);
	    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	    {
		$result = pg_query($dbconn, $sql);
		if (pg_num_rows($result)==0)
		{
		    $this->printAuthReq();
		}
		$row=pg_fetch_array($result);

		$this->usrid = $row['securityuserid'];
		$this->usrname = $row['username'];

		if (isset($row['odkpassword']) && $row['odkpassword']!=null && $row['odkpassword']!='')
		{
		     
		    return $row['odkpassword'];
		}
		else 
		{
		    $this->printAuthReq();
		}
	    }
		
	   

	}


	// equiv to rand, mt_rand
	// returns int in *closed* interval [$min,$max]
	/*function devurandom_rand($min = 0, $max = 0x7FFFFFFF) {

	    global $firebug;

	    $diff = $max - $min;
	    if ($diff < 0 || $diff > 0x7FFFFFFF) {
		throw new RuntimeException("Bad range");
	    }
	    $bytes = mcrypt_create_iv(4, MCRYPT_DEV_URANDOM);
	    if ($bytes === false || strlen($bytes) != 4) {
		throw new RuntimeException("Unable to get 4 bytes");
	    }
	    $ary = unpack("Nint", $bytes);
	    $val = $ary['int'] & 0x7FFFFFFF;   // 32-bit safe
	    $fp = (float) $val / 2147483647.0; // convert to [0,1]
	    return round($fp * $diff) + $min;
	}*/


	function printAuthReq()
	{
	   global $domain;
	   global $bits;
	   global $firebug;

	   $rand = rand();

	   $firebug->log("auth req");

	    header('HTTP/1.1 401 Unauthorized');
	    header('WWW-Authenticate: Digest realm="'.$domain.
		   '",qop="auth",nonce="A'.$rand.'",opaque="'.md5($domain).'"');

	    header('Content-Type: text/html');

	    echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"
	 \"http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd\">
	<HTML>
	  <HEAD>
	    <TITLE>Error</TITLE>
	    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=ISO-8859-1\">
	  </HEAD>
	  <BODY><H1>401 Unauthorized.</H1></BODY>
	</HTML>
	";
	    die();
	   

	}


	// function to parse the http auth header
	function http_digest_parse($txt)
	{
	    global $firebug;
	    
	    $firebug->log($txt, "headers text");

	    // protect against missing data
	    $needed_parts = array('nonce'=>1, 'nc'=>1, 'cnonce'=>1, 'qop'=>1, 'username'=>1, 'uri'=>1, 'response'=>1);
	    $data = array();
	    $keys = implode('|', array_keys($needed_parts));

	    preg_match_all('@(' . $keys . ')=(?:([\'"])([^\2]+?)\2|([^\s,]+))@', $txt, $matches, PREG_SET_ORDER);

	    foreach ($matches as $m) {
		$data[$m[1]] = $m[3] ? $m[3] : $m[4];
		unset($needed_parts[$m[1]]);
	    }

	    $firebug->log($data, "headers");

            $this->username = $data['username'];
	    //return $needed_parts ? false : $data;
	    return  $data;
	}


	function doAuthentication()
	{
		global $firebug;
		global $domain;
		$bits=8;

		if (empty($_SERVER['PHP_AUTH_DIGEST'])) {
			$this->printAuthReq();
		}

		if (!($data = $this->http_digest_parse($_SERVER['PHP_AUTH_DIGEST']))){
			$this->printAuthReq();
		}

		$dbpwd = $this->getDBPwd($data['username']);
		$firebug->log($dbpwd, "dbpwd");


		// generate the valid response

		//$A = $data['username'] . ':' . $domain . ':' . $users[$data['username']];
		$A = $data['username'] . ':' . $domain . ':' . $dbpwd;
		$A1 = md5($A);
		$A2 = md5($_SERVER['REQUEST_METHOD'].':'.$data['uri']);

		$valid_responsea = $A1.':'.$data['nonce'].':'.$data['nc'].':'.$data['cnonce'].':'.$data['qop'].':'.$A2;
		$valid_response = md5($valid_responsea);


		$firebug->log($data['response'], "response from client");

		$firebug->log($valid_response, "response from server");
		if ($data['response'] != $valid_response)
		{
			$this->printAuthReq();
		}
		// ok, valid username & password
		return true;
	}

        function getUsername()
        {
		return $this->usrname;
        }

	function getUserID()
	{
		return $this->usrid;
	}
}
