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

class treeNote 
{
    var $id = NULL;
    var $note = NULL;
    var $isStandard = NULL; 
    var $parentXMLTag = "treeNoteDictionary"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function treeNote()
    {
        // Constructor for this class.
        $this->isStandard = FALSE;
    }

    /***********/
    /* SETTERS */
    /***********/

    function setID($theID)
    {
        // Set the current objects ID.
        $this->id=$theID;
    }

    function setNote($theNote)
    {
        // Set the current objects note.
        $this->note=$theNote;
    }

    function setIsStandard($theFlag)
    {
        // Set the current objects isStandard flag. This denotes whether the record should be regarded as standard for use in combo boxes etc.
        $this->isStandard=$theFlag;
    }

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $this->id=$theID;
        $sql = "select * from tlkptreenote where treenoteid=$theID";
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
                $this->note = $row['note'];
                $this->isStandard = fromPGtoPHPBool($row['isstandard']);
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
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml= "<treeNote id=\"".$this->id."\" isStandard=\"".fromPGtoStringBool($this->isStandard)."\">".$this->note."</treeNote>\n";
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
                    $sql = "insert into tlkptreenote (note, isstandard) values ('".$this->note."', '".fromPHPtoPGBool($this->isStandard)."')";
                    $sql2 = "select * from tlkptreenote where treenoteid=currval('tlkptreenote_treenoteid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tlkptreenote set note='".$this->note."', isstandard='".fromPHPtoPGBool($this->isStandard)."' where treenoteid=".$this->id;
                }

                // Run SQL command
                if (isset($sql))
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
                if (isset($sql2))
                {
                    // Run SQL
                    $result = pg_query($dbconn, $sql2);
                    while ($row = pg_fetch_array($result))
                    {
                        $this->id=$row['treenoteid'];   
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

                $sql = "delete from tlkptreenote where treenoteid=".$this->id;

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
    
    function assignToTree($theTreeID)
    {
        global $dbconn;
        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // First check this combination exisits
                $sql = "select * from tbltreetreenote where treeid=$theTreeID and treenoteid=".$this->id;
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)>0)
                {
                    $this->setErrorMessage("906", "This tree note is already assigned to this tree.");
                    return FALSE;
                }
                else
                {
                    // Add note
                    $sql = "insert into tbltreetreenote (treeid, treenoteid) values ($theTreeID, ".$this->id.")";
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
        return TRUE;
    }
    
    function unassignToTree($theTreeID)
    {
        global $dbconn;
        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // First check this combination exists
                $sql = "select * from tbltreetreenote where treeid=$theTreeID and treenoteid=".$this->id;
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)==0)
                {
                    $this->setErrorMessage("906", "This tree note is not assigned to this tree.");
                    return FALSE;
                }
                else
                {
                    $sql = "delete from tbltreetreenote where treeid=$theTreeID and treenoteid=".$this->id;
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
        return TRUE;
    }

// End of Class
} 
?>
