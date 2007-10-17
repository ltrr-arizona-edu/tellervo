<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

class auth 
{
  var $securityuserid = NULL;
  var $firstname = NULL;
  var $lastname = NULL;
  var $username = NULL;
  var $loggedIn = FALSE;


  function auth()
  {
    session_start();  
    if(isset($_SESSION['initiated']))
    {
        // Already logged in
        $this->securityuserid = $_SESSION['securityuserid'];
        $this->firstname = $_SESSION['firstname'];
        $this->lastname = $_SESSION['lastname'];
        $this->username = $_SESSION['username'];
        $this->loggedIn = TRUE;
    }
    else
    {
        // Need to log in 
        $this->loggedIn = FALSE;
    }
  }

  function login($theUsername, $thePassword)
  {
    $sql = "select * from tblsecurityuser where username='$theUsername' and password='$thePassword'";

    // Authenticate against the database
    if (1==1)
    {
        // Login passed
        session_start();
        $_SESSION['initiated'] = TRUE;
        $_SESSION["securityuserid"] = 1;
        $_SESSION['firstname'] = "George";
        $_SESSION['lastname'] = "Bush";
        $_SESSION['username'] = "gb1";
        $this->loggedIn = TRUE;
    }
    else
    {
        // Login failed
        $this->loggedIn = FALSE;
    }

    return $this->loggedIn;
  }

  function logout()
  {
    $this->loggedIn = FALSE;
    $_SESSION = array();
    if(isset($_COOKIE[session_name()]))
    {
        setcookie(session_name(), '', time()-42000, '/');
    }
    $session_destroy;
  }

  function isLoggedIn()
  {
    return $this->loggedIn;
  }

  function getFirstname()
  {
    return $this->firstname;
  }
  
  function getLastname()
  {
    return $this->lastname;
  }
  
  function getUsername()
  {
    return $this->username;
  }



  function accessSite($theId)
  {
    if($this->loggedIn)
    {
        return true; 
    }
  }

  function accessTree($theId)
  {
    if($this->loggedIn)
    {
        return true; 
    }
  }

  function accessVMeasurement($theId)
  {
    if($this->loggedIn)
    {
        return true; 
    }
  }


} 
?>
