<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

class meta
{
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
  var $timing = array();

  function meta($theRequestType="")
  {
    global $wsversion;
    $this->startTimestamp = microtime(true);
    $this->requestdate= date(DATE_ISO8601);
    $this->requesturl= htmlentities($_SERVER['REQUEST_URI']);
    $this->clientversion= htmlentities($_SERVER['HTTP_USER_AGENT']);
    if($theRequestType)  $this->requesttype= $theRequestType;
    $this->wsversion = $wsversion;
  }
  function setRequestType($theRequestType)
  {
    $this->requesttype= $theRequestType;
  }

  function setUser($theUsername, $theFirstname, $theLastname)
  {
    // Setter for user details
    $this->username = $theUsername;
    $this->firstname = $theFirstname;
    $this->lastname = $theLastname;
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
    elseif($this->status=="Warning" || $theStatus=="Warning")
    {
        $this->status="Warning";
    }
    else
    {
        $this->status="OK";
    }
  }

  function setTiming($theLabel)
  {
    // Setter for error and warning messages

    $message = array($theLabel => round(((microtime(true)*1000)-($this->startTimestamp*1000)), 0));
    array_push($this->timing, $message);
  }

  function requestLogin($nonce, $messageType="Error")
  {
      $this->nonce= $nonce;
      if(!($messageType=="OK"))
      {
          $this->setMessage("102", "You must login to run this query.", $messageType);
      }
  }

  function asXML()
  {
      // Get class as XML 
    $xml="<header>\n";
    if (!($this->username==NULL))
    {
        $xml.="<user username=\"".$this->username."\" firstname=\"".$this->firstname."\" lastname=\"".$this->lastname."\" />\n";
    }
    $xml.="<wsversion>".$this->wsversion."</wsversion>\n";
    $xml.="<clientversion>".$this->clientversion."</clientversion>\n";
    $xml.="<requestdate>".$this->requestdate."</requestdate>\n";
    $xml.="<queryTime unit=\"seconds\">".round((microtime(true)-$this->startTimestamp), 2)."</queryTime>\n";
    $xml.="<requesturl>".$this->requesturl."</requesturl>\n";
    $xml.="<requesttype>".$this->requesttype."</requesttype>\n";
    $xml.="<status>".$this->status."</status>\n";

    foreach($this->messages as $item)
    {
        foreach($item as $code => $message)
        {
          $xml.="<message code=\"".$code."\">".$message."</message>\n";
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
        $xml.="<nonce>".$this->nonce."</nonce>\n";
    }

    $xml.="</header>\n";
    return $xml;
  }
} 
?>
