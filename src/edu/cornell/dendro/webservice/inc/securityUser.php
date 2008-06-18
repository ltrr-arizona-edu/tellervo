<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('inc/specimen.php');
require_once('inc/taxon.php');

class securityUser 
{
    var $id = NULL;
    var $username = NULL;
    var $firstname = NULL;
    var $lastname = NULL;
    var $isActive = TRUE;

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
        // Set the current objects precision 
        $this->username=$theUsername;
    }
    
    function setFirstname($theFirstname)
    {
        // Set the current objects precision 
        $this->firstname=$theFirstname;
    }
    
    function setLastname($theLastname)
    {
        // Set the current objects precision 
        $this->lastname=$theLastname;
    }
    
    function setPassword($thePassword)
    {
        // Set the current objects precision 
        $this->password=hash('md5', $thePassword);
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
                $this->username = $row['username'];
                $this->firstname = $row['firstname'];
                $this->lastname = $row['lastname'];
                $this->password = $row['password'];
                $this->isActive = $row['isactive'];
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
        return TRUE;
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

    function asXML($mode="all")
    {
        $xml = NULL;
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($mode=="all") || ($mode=="begin"))
            {
                // Only return XML when there are no errors.
                $xml.= "<user ";
                $xml.= "id=\"".$this->id."\" ";
                $xml.= "username=\"".escapeXMLChars($this->username)."\" ";
                $xml.= "firstname=\"".escapeXMLChars($this->firstname)."\" ";
                $xml.= "lastname=\"".escapeXMLChars($this->lastname)."\" ";
                $xml.= "isActive=\"".fromPGtoStringBool($this->isActive)."\" ";
                $xml.= ">";
            }

            if(($mode=="all") || ($mode=="end"))
            {
                // End XML tag
                $xml.= "</user>\n";
            }

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

        // Check for required parameters
        if($this->username == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'username' field is required.");
            return FALSE;
        }
        
        if($this->firstname == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'firstname' field is required.");
            return FALSE;
        }
        
        if($this->lastname == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'lastname' field is required.");
            return FALSE;
        }
        
        if($this->password == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'password' field is required.");
            return FALSE;
        }

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
                    $sql = "insert into tblsecurityuser (username, password, firstname, lastname, isactive) values (";
                    $sql.= "'".$this->username."', ";
                    $sql.= "'".$this->password."', ";
                    $sql.= "'".$this->firstname."', ";
                    $sql.= "'".$this->lastname."', ";
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
                    $sql.= "firstname = '".$this->firstname."', ";
                    $sql.= "lastname = '".$this->lastname."', ";
                    $sql.= "isactive = '".fromPHPtoPGBool($this->isActive)."'";
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
                        $this->id=$row['securityuserid'];   
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
