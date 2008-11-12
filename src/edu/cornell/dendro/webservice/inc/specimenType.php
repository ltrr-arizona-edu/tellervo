<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('dbhelper.php');

class specimenType 
{
    var $id = NULL;
    var $label = NULL;
    var $parentXMLTag = "specimenTypeDictionary"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function specimenType()
    {
        // Constructor for this class.
        $this->isStandard = FALSE;
    }

    /***********/
    /* SETTERS */
    /***********/

    function setLabel($theLabel)
    {
        // Set the current objects note.
        $this->label=$theLabel;
    }

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function setParamsFromLabel($theLabel)
    {
        global $dbconn;
        $sql = "select * from tlkpspecimentype where label='$theLabel'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the label specified
                $this->setErrorMessage("903", "No records match the specified label");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->setParamsFromDB($row['specimentypeid']);
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

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $sql = "select * from tlkpspecimentype where specimentypeid='$theID'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match") ;
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->label = $row['label'];
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


    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML()
    {
        $xml="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml.= "<specimenType>".$this->label."</specimenType>\n";
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getID()
    {
        return $this->id;
    }

    function getLabel()
    {
        return $this->label;
    }

    static function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<specimenTypeDictionary>";
        return $xml;
    }

    static function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</specimenTypeDictionary>";
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


    /*
     *
     * FUNCTIONS DISABLED FOR THIS CLASS
     *
     *



    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;

        // Check for required parameters
        if($this->note == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'note' field is required.");
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
                    $sql = "insert into tlkpspecimentype (label) values ('".$this->label."')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tlkpspecimentype set label='".$this->label."' where specimentypeid=".$this->id;
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

                $sql = "delete from tlkpspecimentype where specimentypeid=".$this->id;

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
 */
// End of Class
} 
?>
