<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

/**
 * This class handles all the authentication requirements for the webservice.  It is not to be confused with authenticate.php which is used to direct 
 * authentication requests from the user.
 *
 */

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

  /**
   * Constructor for the Auth class
   *
   */
  function __construct()
  {
     global $firebug;
     global $timeout;
    session_start(); 

        if($timeout==null || $timeout==0)
	{
		$timeout=1800;
	}
 
	if (isset($_SESSION['LAST_ACTIVITY']) && (time() - $_SESSION['LAST_ACTIVITY'] > $timeout)) {
	    $firebug->log("Session has expired");
	    session_destroy();   // destroy session data in storage
	    session_unset();     // unset $_SESSION variable for the runtime
		$this->isLoggedIn = FALSE;
	}
	else
	{
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
	$_SESSION['LAST_ACTIVITY'] = time(); // update last activity time stamp



  }

  /**
   * Login using a nonce
   *
   * @param String $theUsername
   * @param String $theClientHash
   * @param String $theClientNonce
   * @param String $theServerNonce
   * @param String $theSequence
   * @return unknown
   */
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

    
    $sql = "select * from tblsecurityuser where username='".pg_escape_string($theUsername)."' and isactive=true";
 
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

  
  /**
   * Login using a plain username and pasword
   *
   * @param String $theUsername
   * @param String $thePassword
   * @return unknown
   */
  public function login($theUsername, $thePassword)
  {
    global $dbconn;

    $sql = "select * from tblsecurityuser where username='".pg_escape_string($theUsername)."' and isactive=true";
    
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

  /**
   * Logout the current user
   *
   */
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
  
  /**
   * Log the user's IP address for security auditing
   *
   * @param String $method defaults to 'login'
   * @return unknown
   */
  function logIP($method="login")
  {
      global $dbconn;

      // Delete exisitng log entry for this IP 
      $sql = "delete from tbliptracking where ipaddr='".$_SERVER['REMOTE_ADDR']."'; ";
          
      if($method=="login")
      {
          // Add entry to IP tracking table
          $sql.= "insert into tbliptracking (ipaddr, securityuserid) values ('".$_SERVER['REMOTE_ADDR']."', '".$this->getID()."')";
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

  /**
   * Log request for security auditing purposes
   *
   * @param String $requestType  
   */
  function logRequest($requestType)
  {
    global $dbconn;
    global $wsversion;

    if ($requestType=="loginPlain")
    {
        $request = "Logged in using plain text";
    }
    elseif ($requestType=="loginSecure")
    {
        $request = "Logged in with security";
    }
    elseif ($requestType=="logout")
    {
        $request = "Logged out";
    }
    elseif ($requestType=="loginFailure")
    {
        $request = "Failed login attempt for username: '".$this->username."'";
    }
    elseif ($requestType=="invalidSNonce")
    {
        $request = "Server nonce supplied was invalid";
    }
    else
    {
        $request = "Unknown authentication request";
    }

    if ($this->getID()==NULL)
    {
        $sql = "insert into tblrequestlog (request, ipaddr, wsversion) values ('".pg_escape_string($request)."', '".pg_escape_string($_SERVER['REMOTE_ADDR'])."', '".pg_escape_string($wsversion)."')";
    }
    else
    {
        $sql = "insert into tblrequestlog (securityuserid, request, ipaddr, wsversion) values ('".$this->getID()."', '".pg_escape_string($request)."', '".pg_escape_string($_SERVER['REMOTE_ADDR'])."', '".pg_escape_string($wsversion)."')";
    }

    pg_send_query($dbconn, $sql);
    $result = pg_get_result($dbconn);
    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
    {
        echo pg_result_error($result)."--- SQL was $sql";
    }
  }


   /*********/
   /*GETTERS*/
   /*********/
  
  /**
   * Is the user logged in?
   *
   * @return Boolean
   */
  public function isLoggedIn()
  {
    return $this->isLoggedIn;
  }

  /**
   * Get the first name of the current user
   *
   * @return String
   */
  public function getFirstname()
  {
    return $this->firstname;
  }
  
  /**
   * Get the last name of the current user
   *
   * @return String
   */
  public function getLastname()
  {
    return $this->lastname;
  }
  
  /**
   * Get the username of the current user
   *
   * @return String
   */
  public function getUsername()
  {
    return $this->username;
  }

  /**
   * Get the user ID of the current user
   *
   * @return Integer
   */
  public function getID()
  {
    return $this->securityuserid;
  }

  /**
   * Determine if the current user has permission do stuff to a vmeasurement
   * 
   * @todo NEEDS WORK!!!
   * 
   * @param $thePermissionType
   * @param $parentSeries
   * @return unknown_type
   */
  public function getVMPermission($thePermissionType, $parentSeriesArray)
  {
  	if($thePermissionType=="create")
  	{
  		return true;
  	}
  	else
  	{
  		return false;
  	}
  	
  	
  }
  
  /**
   * Check the permission setting for the current user for a particular entity id
   *
   * @param unknown_type $thePermissionType
   * @param unknown_type $theObjectType
   * @param unknown_type $theObjectID
   * @return unknown
   */
  public function getPermission($thePermissionType, $theObjectType, $theObjectID, $paramObj=null)
  {
        global $dbconn;
		global $firebug;

	
        // If user is not logged in deny!
        if ($this->isLoggedIn!=TRUE)
        {
            $this->authFailReason = "Not logged in";
            return false;
        }
		

        // If user is admin give them the world
        if ($this->isAdmin)
        {
            return TRUE;
        }
  	
		
        // Always allow access to dictionaries and authentication requests
        if ( ($theObjectType=='dictionaries') || ($theObjectType=='authentication') )
        {
            return true;
        }
        
        // Always DENY access to permission as only admin can do this
        if( $theObjectType=='permission')
        {
        	return false;
        }

        // If Object is measurement change it to vmeasurement so that db understands!
        if ($theObjectType=='measurement')
        {
            $theObjectType='vmeasurement';
        }

        // Convert PG NULL to a string 'NULL' for SQL insert
        if($theObjectID===NULL)
        {
            //$theObjectID = "NULL";
	    $theObjectID = "aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa";
        }

        if($theObjectType=='project')
        {
        	// Everyone has read access to projects
        	if($thePermissionType=='read')
        	{
        		return true;
        	}
        	
        	// ...but only admins can edit projects
        	return false;
        }    
        
        if (($theObjectType=='securityUser') || ($theObjectType=='securityGroup'))
        {
        	if(($thePermissionType=='update' || $thePermissionType=='read') 
        	    && $theObjectID==$this->getID()
        	    && $theObjectType=='securityUser')
        	{
        		// User is trying to read or update their own details
        		return true;        		
        	}
        	else
        	{
        		// For other security functions user *must* be admin
            	$this->authFailReason = "Only admin users can use security functions.";
            	return false;
        	}
        }
        
        // For tags, permission to CRUD is based on:
        // 1) only admins can do global tags
        // 2) only owners can do their own
        if ($theObjectType=='tag')
        {
        
          	$firebug->log($theObjectType, "getPermission object type");
  	$firebug->log($thePermissionType, "getPermission permission type");

        
	    if( ($thePermissionType=='read') || ($thePermissionType=='update') || ($thePermissionType=='delete') || ($thePermissionType=='assign') || ($thePermissionType=='unassign'))
	    {
		$sql = "SELECT ownerid from tbltag where tagid='$theObjectID'";
		$firebug->log($sql, "Tag perms SQL");
	        if($sql)
		{
		    $dbconnstatus = pg_connection_status($dbconn);
		    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
		    {
			pg_send_query($dbconn, $sql);
			$result = pg_get_result($dbconn);
			if(pg_num_rows($result)==0)
			{
			    // No records match the id specified
			    $this->authFailReason = "Attempt to determine permissions failed because the the tag doesn't exist.";
			    return false;
			}
			else
			{
			    $result = pg_query($dbconn, $sql);
			    while ($row = pg_fetch_array($result))
			    {
				if($row['ownerid']==$this->getID())
				{
				    return true;
				}
				else
				{
				    $this->authFailReason = "You can only ".$thePermissionType." your own personal tags";
				    return false;
				}
			    }
			}
			return true;
		    }
		}
		$this->authFailReason = "SQL permission check failed";
		return false;
	    }
	    else if ($thePermissionType=='create')
	    {
		if($paramObj->getOwnerID()==null)
		{
		     $this->authFailReason = "Only admin users can create shared tags";
		     return false;
		}
		else
		{
		    return true;
		}
	    
	    }
	    else
	    {
		$this->authFailReason = "Unhandled permission type";

		return false;
	    }
	    
        
        }

        // For objects that don't have direct security, we need to move up the object chain to check perms
        // We do this by running a sql statement to get details of the next parent which can be searched for perms
        switch($theObjectType)
        {
        case "radius":
            $sql = "select tblradius.radiusid, tblsample.elementid as parentid from tblradius, tblsample where tblradius.radiusid='$theObjectID' and tblradius.sampleid=tblsample.sampleid";
            $theObjectType="element";
            break;
        case "sample":
            $sql = "select tblsample.sampleid, tblsample.elementid as parentid from tblsample where tblsample.sampleid='$theObjectID' ";
            $theObjectType="element";
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

        // Do the actual perms lookup
        $sql = "select * from cpgdb.getuserpermissionset('$this->securityuserid'::uuid, '$theObjectType', '$theObjectID'::uuid)";
        $firebug->log($sql, "Get permissions sql");
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            $row = pg_fetch_array($result);
           
  	    $firebug->log("Permissions are: denied=".$row['denied']." create=".$row['cancreate']."  read=".$row['canread']." update=".$row['canupdate']." delete=".$row['candelete']);
            
            if($row['denied']=='t')
            {
                // Check whether 'denied' over rules.
                if ($theObjectType=='default')
                {
                    $this->authFailReason = "Your default database permissions are set to 'deny'";

                }
                else
                {
                    $this->authFailReason = "Your permissions for ".$theObjectType." id ".$theObjectID." are set to 'deny'"; 
                }
                return FALSE;
            }

	    $firebug->log($thePermissionType, "Permission type");
            switch ($thePermissionType)
            {
            case "create":
                if($row['cancreate']=='t') return true;
		break;
            case "read":
                if($row['canread']=='t') return TRUE;
		break;
            case "update":
                if($row['canupdate']=='t') return TRUE;
		break;
            case "delete":
                if($row['candelete']=='t') return TRUE;
		break;
	    default:
		break;


		$firebug->log("permissions not granted");
                // Incorrect permission type specified returning false
                if ($theObjectType=='default')
                {
                    $this->authFailReason = "Your default database permissions are: denied=".$row['denied']." create=".$row['cancreate']."  read=".$row['canread']." update=".$row['canupdate']." delete=".$row['candelete'];
                }
                else
                {
                    $this->authFailReason = "Your permissions for ".$theObjectType." id ".$theObjectID." are: denied=".$row['denied']." create=".$row['cancreate']." read=".$row['canread']." update=".$row['canupdate']." delete=".$row['candelete'];
                }
                return FALSE;
            }
        }
  }

  /**
   * Does the current user have admin priviledges?
   *
   * @return boolean
   */
  public function isAdmin()
  {
	global $firebug;
        global $dbconn;
        $sql = "select * from cpgdb.isadmin('".$this->securityuserid."'::uuid) where isadmin=true";
	$firebug->log($sql, "Checking isAdmin SQL");
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

  /**
   * Get the sequence number
   *
   * @return Integer
   */
  public function sequence() 
  {
     return mt_rand(1, 1048576);
  }

  /**
   * Get the nonce for a particular sequence for now or 1 minute ago
   *
   * @param Integer $seq
   * @param String $timeseed defaults to 'current'
   * @return String
   */
  public function nonce($seq, $timeseed="current")
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

  /**
   * Check the client hash is correct
   *
   * @param String $dbPasswordHash
   * @param String $cnonce
   * @param Integer $seq
   * @param String $clientHash
   * @return Boolean
   */
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

  /**
   * Check whether server nonce is valid with particular sequence
   *
   * @param String $snonce
   * @param Integer $seq
   * @return Boolean
   */
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
	
  /**
   * Set error message
   *
   * @param Integer $theCode
   * @param String $theMessage
   */
  function setLastErrorMessage($theCode, $theMessage)
  {
        $this->lastErrorMessage = $theMessage;
        $this->lastErrorCode = $theCode;
        trigger_error($theCode.$theMessage, E_USER_ERROR);
  }

  /**
   * Get the error code for the last error
   *
   * @return Integer
   */
  function getLastErrorCode()
  {
        return $this->lastErrorCode;
  }
  
  /**
   * Get the error message for the last error
   *
   * @return String
   */
  function getLastErrorMessage()
  {
        return $this->lastErrorMessage;
  }

   /*********/
   /*GETTERS*/
   /*********/

  /**
   * Set the password for this user
   *
   * @param String $password
   */
  protected function setPassword($password)
  {
    global $dbconn;

    $sql = "update tblsecurityuser set password='".hash('md5', pg_escape_string($password))."' where username='".pg_escape_string($this->username)."'";
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
    }
  }  
  
// End of class
} 
?>
<?php
