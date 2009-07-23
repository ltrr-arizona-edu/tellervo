<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
 * *******************************************************************
 */
require_once('inc/sample.php');
require_once('inc/taxon.php');
require_once('inc/securityGroup.php');

class securityUser 
{
    var $id = NULL;
    var $username = NULL;
    var $firstName = NULL;
    var $lastName = NULL;
    var $password = NULL;
    var $isActive = TRUE;
    var $groupArray = array();

    var $parentXMLTag = "users"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function securityUser()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/
    
    function setUsername($theUsername)
    {
        $this->username=$theUsername;
    }
    
    function setFirstname($theFirstname)
    {
        $this->firstName=$theFirstname;
    }
    
    function setLastname($theLastname)
    {
        $this->lastName=$theLastname;
    }
    
    function setPassword($thePassword, $format="plain")
    {
        switch($format)
        {
        case "plain":
            // password supplied is plain tetxt so hash first
            $this->password=hash('md5', $thePassword);
            break;
        case "hash":
            // password is already hashed so just store
            $this->password=$thePassword;
            break;
        default:
            return false;
            break;
        }

        return true;
    }
    
    function setIsActive($theIsActive)
    {
        // Set the current objects precision 
        $this->isActive=$theIsActive;
    }

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        if ($theID==NULL) return false;
        
        $this->id=$theID;
        $sql = "select * from tblsecurityuser where securityuserid=$theID";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified id");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->id = $row['securityuserid'];
                $this->setUsername($row['username']);
                $this->setFirstName($row['firstname']);
                $this->setLastName($row['lastname']);
                //$this->setPassword($row['password'], "hash");
                $this->setIsActive(dbHelper::formatBool($row['isactive']));
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        return TRUE;
    }  
    
    

    function setChildParamsFromDB()
    {
        global $dbconn;
        
        $sql = "SELECT DISTINCT SecurityGroupID FROM cpgdb.GetGroupMembership(".$this->id.") ORDER BY SecurityGroupID ASC";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            // Set parameters from db
            while ($row = pg_fetch_array($result))
            {
                array_push($this->groupArray, $row['securitygroupid']);
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        return TRUE;
    }
    
    function setParamsFromParamsClass($paramsClass)
    {
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if (isset($paramsClass->id))           $this->id   = $paramsClass->id;
        if (isset($paramsClass->username))     $this->setUsername($paramsClass->username);
        if (isset($paramsClass->firstName))    $this->setFirstName($paramsClass->firstName);
        if (isset($paramsClass->lastName))     $this->setLastName($paramsClass->lastName);
        if (isset($paramsClass->hashPassword)) $this->setPassword($paramsClass->hashPassword, "hash");
        if (isset($paramsClass->isActive))     $this->setIsActive($paramsClass->isActive);
        
        if (isset($paramsClass->groupArray) && $paramsClass->groupArray[0]!='empty')
        {
            // Remove any existing groups,  ready to be replaced with what user has specified
            unset($this->groupArray);
            $this->groupArray = array();

            foreach($paramsClass->groupArray as $item)
            {
                array_push($this->groupArray, $item);
            }
        }   
        return true;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if( (gettype($paramsObj->id)!="integer") && ($paramsObj->id!=NULL) ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer when reading users.  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer when reading users.");
                    return false;
                }
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a users.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a user.");
                    return false;
                }
                if(($paramsObj->username == NULL) && ($paramsObj->firstName==NULL) && ($paramsObj->lastName==NULL) && ($paramsObj->isActive==NULL) && ($paramsObj->hashPassword==NULL) ) 
                {
                    $this->setErrorMessage("902","Missing parameter(s) - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a user.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->username == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'username' field is required when creating a user.");
                    return false;
                }
                if($paramsObj->firstName == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'firstName' field is required when creating a user.");
                    return false;
                }
                if($paramsObj->lastName == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'lastName' field is required when creating a user.");
                    return false;
                }
                if($paramsObj->hashPassword == NULL)  
                {
                    $this->setErrorMessage("902","Missing parameter - either 'plainPassword' or 'hashPassword' is required when creating a user.");
                    return false;
                }
                return true;

            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }
    
    
    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }


    /***********/
    /*ACCESSORS*/
    /***********/

    function getID()
    {
    	return $this->id;
    	
    }
    
    function getFormattedName()
    {
    	if (($this->firstName!=NULL) && ($this->lastName!=NULL))
    	{
    		return $this->firstName." ".$this->lastName;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    function asXML()
    {
        $xml = NULL;
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml.= "<user ";
            $xml.= "id=\"".$this->id."\" ";
            $xml.= "username=\"".dbHelper::escapeXMLChars($this->username)."\" ";
            $xml.= "firstName=\"".dbHelper::escapeXMLChars($this->firstName)."\" ";
            $xml.= "lastName=\"".dbHelper::escapeXMLChars($this->lastName)."\" ";
            $xml.= "isActive=\"".dbHelper::formatBool($this->isActive, 'english')."\" ";
            $xml.= ">";

            if (isset($this->groupArray))
            {
                $xml.= "<memberOf>";
                foreach($this->groupArray as $groupID)
                {
                    $group = new securityGroup();
                    $success = $group->setParamsFromDB($groupID);

                    if($success)
                    {
                        $xml.=$group->asXML();
                    }
                    else
                    {
                        $this->setErrorMessage($group->getLastErrorCode(), $group->getLastErrorMessage());
                    }

                }
                $xml.= "</memberOf>";

            }
            
            $xml.= "</user>\n";
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag.">";
        return $xml;
    }

    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
    }

    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }

    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
    }

    /***********/
    /*FUNCTIONS*/
    /***********/

    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        $sql  = NULL;
        $sql2 = NULL;

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->id == NULL)
                {
                    // New record
                    $sql = "insert into tblsecurityuser (username, password, firstName, lastName, isactive) values (";
                    $sql.= "'".$this->username."', ";
                    $sql.= "'".$this->password."', ";
                    $sql.= "'".$this->firstName."', ";
                    $sql.= "'".$this->lastName."', ";
                    $sql.= "'".fromPHPtoPGBool($this->isActive)."'";
                    $sql.= " )";
                    $sql2 = "select * from tblsecurityuser where securityuserid=currval('tblsecurityuser_securityuserid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblsecurityuser set ";
                    $sql.= "username = '".$this->username."', ";
                    $sql.= "password = '".$this->password."', ";
                    $sql.= "firstName = '".$this->firstName."', ";
                    $sql.= "lastName = '".$this->lastName."', ";
                    $sql.= "isactive = '".fromPHPtoPGBool($this->isActive)."'";
                    $sql.= "where securityuserid = '".$this->id."'";
                }

                // Run SQL command
                if ($sql)
                {
                    // Run SQL 
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        return FALSE;
                    }
                }
                // Retrieve automated field values when a new record has been inserted
                if ($sql2)
                {
                    // Run SQL
                    $result = pg_query($dbconn, $sql2);
                    while ($row = pg_fetch_array($result))
                    {
                        $this->setParamasFromDB($row['securityuserid']);   
                    }
                }
                
                // Set or unset member groups for this user
                if(isset($this->groupArray))
                {
                    $sql = "delete from tblsecurityusermembership where securityuserid=".$this->id;
                    $result = pg_query($dbconn, $sql);

                    foreach($this->groupArray as $item)
                    {
                        $sql = "insert into tblsecurityusermembership (securityuserid, securitygroupid) values ('$this->id', '$item')"; 
                        $result = pg_query($dbconn, $sql);
                    }
                } 
            }
            else
            {
                // Connection bad
                $this->setErrorMessage("001", "Error connecting to database");
                return FALSE;
            }
        }

        // Return true as write to DB went ok.
        return TRUE;
    }

    function deleteFromDB()
    {
        // Delete the record in the database matching the current object's ID

        global $dbconn;

        // Check for required parameters
        if($this->id == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'id' field is required.");
            return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {

                $sql = "delete from tblsecurityuser where securityuserid=".$this->id;

                // Run SQL command
                if ($sql)
                {
                    // Run SQL 
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        return FALSE;
                    }
                }
            }
            else
            {
                // Connection bad
                $this->setErrorMessage("001", "Error connecting to database");
                return FALSE;
            }
        }

        // Return true as write to DB went ok.
        return TRUE;
    }

// End of Class
} 
?>
