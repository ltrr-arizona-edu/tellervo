<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

require_once("dbhelper.php");
$myMetaHeader = new meta();


class meta
{
  var $securityUserID = NULL;
  var $username = NULL;
  var $firstname = NULL;
  var $lastname = NULL;
  var $wsversion = NULL;
  var $clientversion = NULL;
  var $requestdate = NULL;
  var $startTimestamp = NULL;
  var $requesturl = NULL;
  var $requesttype = "Read";
  var $status = "OK";
  var $messages = array();
  var $nonce = NULL;
  var $seq = NULL;
  var $timing = array();
  	

  function meta($theRequestType="")
  {
    global $wsversion;
    $this->startTimestamp = microtime(true);
    $this->requestdate= date('c');
    $this->requesturl= dbHelper::escapeXMLChars($_SERVER['REQUEST_URI']);
    $this->clientversion= dbHelper::escapeXMLChars($_SERVER['HTTP_USER_AGENT']);
    if($theRequestType)  $this->requesttype= $theRequestType;
    $this->wsversion = $wsversion;
  }
  function setRequestType($theRequestType)
  {
    $this->requesttype= $theRequestType;
  }

  function setUser($theUsername, $theFirstname, $theLastname, $theSecurityUserID)
  {
    // Setter for user details
    $this->username = $theUsername;
    $this->firstname = $theFirstname;
    $this->lastname = $theLastname;
    $this->securityUserID = $theSecurityUserID;
  }
  
  function setMessage($theCode, $theMessage, $theStatus="Error")
  {
    // Setter for error and warning messages

    $message = array($theCode => $theMessage);
    array_push($this->messages, $message);
        
    // Set corresponding status 
    if($this->status=="Error" || $theStatus=="Error") 
    {
        $this->status="Error";
    }
    elseif($this->status=="Warning" || $theStatus=="Warning" )
    {
        $this->status="Warning";
    }
    elseif($this->status=="Notice" || $theStatus=="Notice")
    {
    	$this->startTimestamp="Notice";
    }
    else
    {
        $this->status="OK";
    }
  }

  function setTiming($theLabel)
  {
  	global $debugFlag;
  	global $timingFlag;
  	global $firebug;
  	
  	if(( $debugFlag===TRUE) && ($timingFlag===TRUE))
  	{
    	//$message = array($theLabel => round(((microtime(true)*1000)-($this->startTimestamp*1000)), 0));
    	//array_push($this->timing, $message);
    	
  		$firebug->log(round(((microtime(true)*1000)-($this->startTimestamp*1000)), 0), $theLabel);
  	}
  }
  
  function requestLogin($nonce, $seq, $messageType="Error")
  {
      $this->seq=$seq;
      $this->nonce= $nonce;
      if(!($messageType=="OK"))
      {
          $this->setMessage("102", "You must login to run this query.", $messageType);
      }
  }

  function getObjectName()
  {
      // Trim off all after the ?
      $string = explode("?", $this->requesturl);
      // Remove the first '/'
      $string = substr($string[0], 1);
      // Remove the .php
      $string = substr($string, 0, -4);
      // Uppercase the first letter
      $string = ucfirst($string);
      return $string;
  }

  function getIsLoginRequired()
  {
     foreach($this->messages as $message)
     {
        if (isset($message[102]))
        {
           return True;
        }
     }
     return False;
  }

  /**
   * Search the complete HTTP_USER_AGENT for identifiers of known client programs and return the associated version number.  
   * If no known clients are identifier then returns false
   *
   * @return Mixed
   */
  function getClientVersion()
  {
  	global $tellervoClientIdentifiers;
  	global $firebug;
  	
  	foreach($tellervoClientIdentifiers as $app)
  	{ 	
  		
	  	if (strstr($this->clientversion, $app['name']))
	  	{
	  		$wholeClientString = strstr($this->clientversion, $app['name']);
	  		//$firebug->log($wholeClientString, "Client");
	  			  		
	  		$lengthOfName = strlen($app['name'])+1;
	  		//$firebug->log($lengthOfName, "LenOfName");
	  		
	  		$versionWithTrailing = substr($wholeClientString, $lengthOfName);
	  		//$firebug->log($versionWithTrailing, "VersionWithTrailing");
	  		
	  		$endMarkerPos = strpos($versionWithTrailing, " ");
	  		if($endMarkerPos==null) $endMarkerPos = strlen($versionWithTrailing);
	  		//$firebug->log($endMarkerPos, "Endpos");
	  		 		
	  		$appVersion = substr($versionWithTrailing, 0, $endMarkerPos);
	  		//$firebug->log($appVersion, "AppVersion");
	  		

	  		return $appVersion;
	  		break;
	  	}	
  	}	
  	return false;
  }
  
  /**
   * Is the version of the client being used valid?
   *
   * @return Boolean
   */
  function isClientVersionValid()
  {
  	global $tellervoClientIdentifiers;
	global $onlyAllowKnownClients;
	global $firebug;
	
  	foreach($tellervoClientIdentifiers as $app)
  	{ 			
	  	if (strstr($this->clientversion, $app['name']))
	  	{
	  		// Client recognised so check version
	  		
	  		$clientVersionArray      = explode(".", $this->getClientVersion());
	  		$minRequiredVersionArray = explode(".", $this->getMinRequiredClientVersion());
	  		
	  		for ($i=0; $i<sizeof($clientVersionArray) && $i<sizeof($minRequiredVersionArray); $i++)
	  		{
	  			if($clientVersionArray[$i]==$minRequiredVersionArray[$i])
	  			{
	  				$firebug->log("not sure if client version is ok yet...");
	  				continue;
	  			}
	  			else if($clientVersionArray[$i]<$minRequiredVersionArray[$i])
	  			{
	  				$firebug->log("Client too old");
	  				$firebug->log($clientVersionArray, "Client Version");
	  				$firebug->log($minRequiredVersionArray, "Required Version");
	  				return false;
	  			}
	  			else if ($clientVersionArray[$i]>$minRequiredVersionArray[$i])
	  			{
	  				$firebug->log("Client ok");
	  				$firebug->log($clientVersionArray, "Client Version");
	  				$firebug->log($minRequiredVersionArray, "Required Version");
	  				return true;
	  			}

	  		}
	  			  		
	  		// Version matches required version
	  		return true;
	  	}	
  	}

  	// Client not recognised so return either false or null depending on if strict client checking is enabled
  	if($onlyAllowKnownClients===TRUE)
  	{ 
  		return false;
  	}
  	else
  	{
  		return null;
  	}
  }
  
  
  function getClientName()
  {
  	global $tellervoClientIdentifiers;	
  	
  	foreach($tellervoClientIdentifiers as $app)
  	{ 	
	  	if (strstr($this->clientversion, $app['name']))
	  	{
	  		return $app['name'];
	  	}	
  	}	
  	return false;
  }
  
  function getMinRequiredClientVersion()
  {
  	global $tellervoClientIdentifiers;	
  	
  	foreach($tellervoClientIdentifiers as $app)
  	{ 	
	  	if (strstr($this->clientversion, $app['name']))
	  	{
	  		return $app['minVersion'];
	  	}	
  	}	
  	return false;
  }
  
    
  function asXML()
  {
    // Get class as XML 
    $xml="<header>\n";
    if (!($this->username==NULL))
    {
        $xml.="<securityUser id=\"".$this->securityUserID."\" username=\"".$this->username."\" firstName=\"".$this->firstname."\" lastName=\"".$this->lastname."\" />\n";
    }
    $xml.="<webserviceVersion>".$this->wsversion."</webserviceVersion>\n";
    $xml.="<clientVersion>".$this->clientversion."</clientVersion>\n";
    $xml.="<requestDate>".$this->requestdate."</requestDate>\n";
    $xml.="<queryTime unit=\"seconds\">".round((microtime(true)-$this->startTimestamp), 2)."</queryTime>\n";
    $xml.="<requestUrl>".$this->requesturl."</requestUrl>\n";
    $xml.="<requestType>".strtolower($this->requesttype)."</requestType>\n";
    $xml.="<status>".$this->status."</status>\n";

    // Remove duplicate messages
    $this->messages = array_unique($this->messages);

    foreach($this->messages as $item)
    {
        foreach($item as $code => $message)
        {
          $xml.="<message code=\"".$code."\">".dbhelper::escapeXMLChars($message)."</message>\n";
        }
    }

    foreach($this->timing as $item)
    {
        foreach($item as $code => $message)
        {
          $xml.="<timing label=\"".$code."\">".$message."</timing>\n";
        }
    }

    if ($this->nonce)
    {
        $xml.="<nonce seq=\"".$this->seq."\">".$this->nonce."</nonce>\n";
    }

    $xml.="</header>\n";
    return $xml;
  }
} 
?>
