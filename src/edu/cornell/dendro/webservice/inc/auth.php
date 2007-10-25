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
    global $dbconn;

    $sql = "select * from tblsecurityuser where username='$theUsername'";
    
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
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
            session_start();
            $_SESSION['initiated'] = TRUE;
            $_SESSION["securityuserid"] = $row['securityuserid'];
            $_SESSION['firstname'] = $row['firstname'];
            $_SESSION['lastname'] = $row['lastname'];
            $_SESSION['username'] = $row['username'];
            $this->loggedIn = TRUE;
        }
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

  function getID()
  {
    return $this->securityuserid;
  }

  function vmeasurementPermission($theVMeasurementID, $theType)
  {
        global $dbconn;
        
        switch($theType)
        {
        case "create":
            return true;
            break;

        case "read":
            $thePermissionID=2;
            break;

        case "update":
            $thePermissionID=4;
            break;

        case "delete":
            $thePermissionID=5;
            break;
        } 
        
        // Check user is logged in first
        if ($this->isLoggedIn())
        {
            $sql = "select * from securitygroupvmeasurementmaster($thePermissionID, ".$this->securityuserid.") where objectid=$theVMeasurementID";
            
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    return false;
  }
  
  function sitePermission($theSiteID, $theType)
  {
        global $dbconn;
        
        switch($theType)
        {
        case "create":
            return true;
            break;

        case "read":
            $thePermissionID=2;
            break;

        case "update":
            $thePermissionID=4;
            break;

        case "delete":
            $thePermissionID=5;
            break;
        } 
        
        // Check user is logged in first
        if ($this->isLoggedIn())
        {
            $sql = "select * from securitygroupsitemaster($thePermissionID, ".$this->securityuserid.") where objectid=$theSiteID";
            
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    return false;
  }

  function treePermission($theTreeID, $theType)
  {
        global $dbconn;

        switch($theType)
        {
        case "create":
            return true;
            break;

        case "read":
            $thePermissionID=2;
            break;

        case "update":
            $thePermissionID=4;
            break;

        case "delete":
            $thePermissionID=5;
            break;
        } 
        
        // Check user is logged in first
        if ($this->isLoggedIn())
        {
            $sql = "select * from securitygrouptreemaster($thePermissionID, ".$this->securityuserid.") where objectid=$theTreeID";
            
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    return false;
  }

  function subSitePermission($theSubSiteID, $theType)
  {

        global $dbconn;
        $sql = "select siteid from tblsubsite where subsiteid=$theSubSiteID";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                return false;
            }
            else
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    return $this->sitePermission($row['siteid'], $theType);
                }
            }
        }
        return false;
  }

  function specimenPermission($theSpecimenID, $theType)
  {
        global $dbconn;
        $sql = "select treeid from tblspecimen where specimenid=$theSpecimenID";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                return false;
            }
            else
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    return $this->treePermission($row['treeid'], $theType);
                }
            }
        }
        return false;
  }
  
  function radiusPermission($theRadiusID, $theType)
  {
        global $dbconn;
        $sql = "select tblradius.radiusid, tblspecimen.treeid from tblradius, tblspecimen where tblradius.radiusid=$theRadiusID and tblradius.specimenid=tblspecimen.specimenid";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                return false;
            }
            else
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    return $this->treePermission($row['treeid'], $theType);
                }
            }
        }
        return false;
  }

  function hashPassword($password)
  {
    mt_srand((double)microtime()*1000000);
    $salt = mhash_keygen_s2k(MHASH_SHA1, $password, substr(pack('h*', md5(mt_rand())), 0, 8), 4);
    $hash = "{SSHA}".base64_encode(mhash(MHASH_SHA1, $password.$salt).$salt);
    return $hash;
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


// End of class
} 
?>
