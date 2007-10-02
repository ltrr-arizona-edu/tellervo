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
  var $wsversion = 0.1;
  var $clientversion = NULL;
  var $requestdate = NULL;
  var $requesturl = NULL;
  var $requesttype = "Read";
  var $status = "OK";
  var $messages = array();

  function meta($theRequestType="Read")
  {
    $this->requestdate= date(DATE_ISO8601);
    $this->requesturl= htmlentities($_SERVER['REQUEST_URI']);
    $this->clientversion= htmlentities($_SERVER['HTTP_USER_AGENT']);
    $this->requesttype= $theRequestType;
  }

  function setUser($theUsername, $theFirstname, $theLastname)
  {
    // Setter for user details
    $this->username = $theUsername;
    $this->firstname = $theFirstname;
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

  function asXML()
  {
    // Get class as XML 
    $xml ="<meta>\n";
    $xml.="<user username=\"".$this->username."\" firstname=\"".$this->firstname."\" lastname=\"".$lastname."\" />\n";
    $xml.="<wsversion>".$this->wsversion."</wsversion>\n";
    $xml.="<clientversion>".$this->clientversion."</clientversion>\n";
    $xml.="<requestdate>".$this->requestdate."</requestdate>\n";
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
    $xml.="</meta>\n";
    return $xml;
  }
} 
?>
