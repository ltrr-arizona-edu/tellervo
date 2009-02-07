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
  var $firstname = "Unknown";
  var $lastname = "Unknown";
  var $username = "Guest";
  var $dbPasswordHash = NULL;
  var $isLoggedIn = FALSE;
  var $isAdmin = FALSE;
  var $authFailReason = NULL;
  var $lastErrorCode = NULL;
  var $lastErrorMessage = NULL;

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
        $this->isLoggedIn = TRUE;
        $this->isAdmin = $this->isAdmin();
        $this->logIP();
    }
    else
    {
        // Need to log in 
        $this->isLoggedIn = FALSE;
    }
  }

  function loginWithNonce($theUsername, $theClientHash, $theClientNonce, $theServerNonce, $theSequence)
  {
    global $dbconn;

    // Before we do anything - check that the server nonce the client is sending has not expired
    if($this->isValidServerNonce($theServerNonce,$theSequence)===FALSE) 
    {
        $this->isLoggedIn = FALSE;
        $this->setLastErrorMessage("105","Invalid server nonce supplied.");
        return false;
    }

    
    $sql = "select * from tblsecurityuser where username='$theUsername' and isactive=true";
 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            $this->dbPasswordHash = $row['password'];
            $this->username = $row['username'];
        }
    }

    // Authenticate against the database
    if ($this->checkClientHash($this->dbPasswordHash, $theClientNonce, $theSequence, $theClientHash))
    {
        // Login passed
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            $_SESSION['initiated'] = TRUE;
            $_SESSION["securityuserid"] = $row['securityuserid'];
            $this->securityuserid = $row['securityuserid'];
            $_SESSION['firstname'] = $row['firstname'];
            $this->firstname = $row['firstname'];
            $_SESSION['lastname'] = $row['lastname'];
            $this->lastname = $row['lastname'];
            $_SESSION['username'] = $row['username'];
            $this->username = $row['username'];
            
            $this->isLoggedIn = TRUE;
            $this->logIP();
            $this->logRequest("loginSecure");
            $this->isAdmin = $this->isAdmin();
        }
    }
    else
    {
        // Login failed
        $this->isLoggedIn = FALSE;
        $this->logRequest("loginFailure");
        $this->setLastErrorMessage("101", "Authentication failed using secure login. Please check and try again.");
    }

    return $this->isLoggedIn;
  }

  function login($theUsername, $thePassword)
  {
    global $dbconn;

    $sql = "select * from tblsecurityuser where username='$theUsername' and isactive=true";
    
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        if (pg_num_rows($result)==0)
        {
            $this->setLastErrorMessage("106", "Login failed - user unknown");
            return false;
        }
        else
        {
            $row=pg_fetch_array($result);
            $dbpassword = $row['password'];
        }
    }

    // Authenticate against the database
    if (hash('md5', $thePassword)==$dbpassword)
    {
        // Login passed
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            $_SESSION['initiated'] = TRUE;
            $_SESSION["securityuserid"] = $row['securityuserid'];
            $this->securityuserid = $row['securityuserid'];
            $_SESSION['firstname'] = $row['firstname'];
            $this->firstname = $row['firstname'];
            $_SESSION['lastname'] = $row['lastname'];
            $this->lastname = $row['lastname'];
            $_SESSION['username'] = $row['username'];
            $this->username = $row['username'];


            $this->logIP();
            $this->isLoggedIn = TRUE;
            $this->logRequest("loginPlain");
        }
    }
    else
    {
        // Login failed
        $this->isLoggedIn = FALSE;
        $this->logRequest("loginFailure");
        $this->setLastErrorMessage("101", "Authentication failed using plain login");
    }

    return $this->isLoggedIn;
  }

  function logout()
  {
    $this->isLoggedIn = FALSE;
    $_SESSION = array();
    if(isset($_COOKIE[session_name()]))
    {
        setcookie(session_name(), '', time()-42000, '/');
    }
    $this->logRequest("logout");
    $this->logIP('logout');
    $session_destroy;
  }
  
  function logIP($method="login")
  {
      global $dbconn;

      // Delete exisitng log entry for this IP 
      $sql = "delete from tbliptracking where ipaddr='".$_SERVER['REMOTE_ADDR']."'; ";
          
      if($method=="login")
      {
          // Add entry to IP tracking table
          $sql.= "insert into tbliptracking (ipaddr, securityuserid) values ('".$_SERVER['REMOTE_ADDR']."', ".$this->getID().")";
      }

      pg_send_query($dbconn, $sql);

      // Get 'delete' result then 'insert' result, ignoring delete result as we don't need to know
      $result = pg_get_result($dbconn);
      $result = pg_get_result($dbconn);
      if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
      {
          return false;
      }
      else
      {
          return true;
      }
  }

  function logRequest($type)
  {
    global $dbconn;
    global $wsversion;

    if ($type=="loginPlain")
    {
        $request = "Logged in using plain text";
    }
    elseif ($type=="loginSecure")
    {
        $request = "Logged in with security";
    }
    elseif ($type=="logout")
    {
        $request = "Logged out";
    }
    elseif ($type=="loginFailure")
    {
        $request = "Failed login attempt for username: '".$this->username."'";
    }
    elseif ($type=="invalidSNonce")
    {
        $request = "Server nonce supplied was invalid";
    }
    else
    {
        $request = "Unknown authentication request";
    }

    if ($this->getID()==NULL) 
    {
        $sql = "insert into tblrequestlog (request, ipaddr, wsversion) values ('".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion')";
    }
    else
    {
        $sql = "insert into tblrequestlog (securityuserid, request, ipaddr, wsversion) values ('".$this->getID()."', '".addslashes($request)."', '".$_SERVER['REMOTE_ADDR']."', '$wsversion')";
    }

    pg_send_query($dbconn, $sql);
    $result = pg_get_result($dbconn);
    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
    {
        echo pg_result_error($result)."--- SQL was $sql";
    }


  }

  function isLoggedIn()
  {
    return $this->isLoggedIn;
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

  function getID()
  {
    return $this->securityuserid;
  }

  function getPermission($thePermissionType, $theObjectType, $theObjectID)
  {
        // $theObjectType should be one of site,tree, vmeasurement, default

        global $dbconn;

        // Always allow access to dictionaries
        if ( ($theObjectType=='dictionaries') || ($theObjectType=='authentication') )
        {
            return true;
        }

        // If Object is measurement change it to vmeasurement so that db understands!
        if ($theObjectType=='measurement')
        {
            $theObjectType='vmeasurement';
        }

        // Convert PG NULL to a string 'NULL' for SQL insert
        if($theObjectID===NULL)
        {
            $theObjectID = "NULL";
        }

        // If user is not logged in deny!
        if ($this->isLoggedIn!=TRUE)
        {
            $this->authFailReason = "Not logged in";
            return false;
        }

        // If user is admin give them the world
        if ($this->isAdmin)
        {
            return true;
        }

        // For security functions user *must* be admin
        if (($theObjectType=='securityUser') || ($theObjectType=='securityGroup'))
        {
            $this->authFailReason = "Only admin users can use security functions";
            return false;
        }

        // For objects that don't have direct security, we need to move up the object chain to check perms
        // We do this by running a sql statement to get details of the next parent which can be searched for perms
        switch($theObjectType)
        {
        case "radius":
            $sql = "select tblradius.radiusid, tblspecimen.treeid as parentid from tblradius, tblspecimen where tblradius.radiusid=$theObjectID and tblradius.specimenid=tblspecimen.specimenid";
            $theObjectType="tree";
            break;
        case "specimen":
            $sql = "select tblspecimen.specimenid, tblspecimen.treeid as parentid from tblspecimen where tblspecimen.specimenid=$theObjectID ";
            $theObjectType="tree";
            break;
        case "subSite":
            $sql = "select tblsubsite.subsiteid, tblsubsite.siteid as parentid from tblsubsite where tblsubsite.subsiteid=$theObjectID ";
            $theObjectType="site";
            break;
        default:
            $sql = NULL;
        }

        if($sql)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)==0)
                {
                    // No records match the id specifiedn
                    $this->authFailReason = "Attempt to determine permissions failed because the parent of this object doesn't exist.  SQL used was $sql";
                    return false;
                }
                else
                {
                    $result = pg_query($dbconn, $sql);
                    while ($row = pg_fetch_array($result))
                    {
                        $theObjectID = $row['parentid'];
                    }
                }
            }
        }

        // Dp the actual perms lookup
        $sql = "select * from cpgdb.getuserpermissionset($this->securityuserid, '$theObjectType', $theObjectID)";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            $row = pg_fetch_array($result);
            
            if($row['denied']===TRUE)
            {
                // Check whether 'denied' over rules.
                if ($theObjectType=='default')
                {
                    $this->authFailReason = "Your default database permissions are: 
                        denied=".$row['denied']." 
                        create=".$row['cancreate']."  
                        read=".$row['canread']." 
                        update=".$row['canupdate']." 
                        delete=".$row['candelete'];
                }
                else
                {
                    $this->authFailReason = "Your permissions for ".$theObjectType." id ".$theObjectID." are: 
                        denied=".$row['denied']." 
                        create=".$row['cancreate']." 
                        read=".$row['canread']." 
                        update=".$row['canupdate']." 
                        delete=".$row['candelete'];
                }
                return False;
            }

            switch ($thePermissionType)
            {
            case "create":
                if($row['cancreate']=='t') return true;
            case "read":
                if($row['canread']=='t') return true;
            case "update":
                if($row['canupdate']=='t') return true;
            case "delete":
                if($row['candelete']=='t') return true;
            default:
                // Incorrect permission type specified returning false
                if ($theObjectType=='default')
                {
                    $this->authFailReason = "Your default database permissions are: denied=".$row['denied']." create=".$row['cancreate']."  read=".$row['canread']." update=".$row['canupdate']." delete=".$row['candelete'];
                }
                else
                {
                    $this->authFailReason = "Your permissions for ".$theObjectType." id ".$theObjectID." are: denied=".$row['denied']." create=".$row['cancreate']." read=".$row['canread']." update=".$row['canupdate']." delete=".$row['candelete'];
                }
                return False;
            }
        }
  }

  function isAdmin()
  {
        global $dbconn;
        $sql = "select * from cpgdb.isadmin(".$this->securityuserid.") where isadmin=true";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match so not in admin group
                return false;
            }
            else
            {
                // Result returned so must be in admin group
                return true;
            }
        }
        return false;
  }

  function setPassword($username, $password)
  {
    global $dbconn;

    $sql = "update tblsecurityuser set password='".hash('md5', $password)."' where username='$username'";
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
    }
  }

  function sequence() 
  {
     return mt_rand(1, 1048576);
  }

  function nonce($seq, $timeseed="current")
  {
    if ($timeseed=="current")
    {
        // hash of current date
       return hash('md5', date("l F j H:i T Y") . ":" . $seq);
    }
    else
    {
        // hash of  date 1 minute ago
       return hash('md5', date("l F j H:i T Y", time()-60) . ":" . $seq);
    }
  }

  function checkClientHash($dbPasswordHash, $cnonce, $seq, $clientHash)
  {
    $serverHashCurrent = hash('md5', $this->username.":".$dbPasswordHash.":".$this->nonce($seq, "current").":".$cnonce);
    $serverHashLast = hash('md5', $this->username.":".$dbPasswordHash.":".$this->nonce($seq, "last").":".$cnonce);

    //echo $this->username.":".$dbPasswordHash.":".$this->nonce($seq, "current").":".$cnonce."<br/>";
    //echo $serverHashCurrent."<br/>";
    //echo $clientHash."<br/>";

    //echo $this->username.":".$dbPasswordHash.":".$this->nonce($seq, "last").":".$cnonce."<br/>";
    //echo $serverHashLast."<br/>";

    if (($serverHashCurrent==$clientHash) || ($serverHashLast==$clientHash))
    {
        return true;
    }
    else
    {
        return false;
    }

  }

  function isValidServerNonce($snonce, $seq)
  {
      if( ($snonce==$this->nonce($seq, 'current')) || ($snonce==$this->nonce($seq, 'previous'))  )
      {
          return true;
      }
      else
      {
          return false;
      }
        
  }

  function setLastErrorMessage($theCode, $theMessage)
  {
        $this->lastErrorMessage = $theMessage;
        $this->lastErrorCode = $theCode;
  }

  function getLastErrorCode()
  {
        return $this->lastErrorCode;
  }
  
  function getLastErrorMessage()
  {
        return $this->lastErrorMessage;
  }


// End of class
} 
?>