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




class note 
{
    var $id                 = NULL;
    var $note               = NULL;
    var $isStandard         = FALSE; 
    var $parentXMLTag       = NULL; 
    var $lastErrorMessage   = NULL;
    var $lastErrorCode      = NULL;
    var $parentID           = NULL;
    
    var $objectName         = NULL;
    var $parentName         = NULL;
    var $tableName          = NULL;
    var $joinTableName      = NULL;
    var $parentField        = NULL;
    var $objectField        = NULL;
    var $xmlTag             = NULL;

    var $includePermissions = FALSE;
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct($type)
    {
        $this->objectName = $type."Note";
        $this->parentName = $type;
        $this->joinTableName = "tbl".$type.strtolower($this->objectName);
        $this->parentField = strtolower($type)."id";
        $this->objectField = strtolower($this->objectName)."id";
        $this->tableName = "tlkp".strtolower($this->objectName);
        $this->parentXMLTag = $this->objectName."Dictionary";
        
        if($type=='vmeasurement') 
        {
            $this->xmlTag = 'measurementNote';
        }
        else
        {
            $this->xmlTag = $this->objectName; 
        }
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
        
        $this->setID($theID);
        $sql = "select * from ".$this->tableName." where ".$this->objectField."=$theID";
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

    function setChildParamsFromDB()
    {
        return true;
    }

    function setParamsFromParamsClass($paramsClass)
    {
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if (isset($paramsClass->id))            $this->id           = $paramsClass->id;
        if (isset($paramsClass->note))          $this->note         = $paramsClass->note;
        if (isset($paramsClass->isStandard))    $this->isStandard   = $paramsClass->isStandard;
        //if (isset($paramsClass->$parentField))  $this->$parentField = $paramsClass->$parentField;
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
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer when reading ".$this->objectName.".  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer when reading a ".$this->objectName.".");
                    return false;
                }
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a ".$this->objectName.".");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a ".$this->objectName.".");
                    return false;
                }
                if( (gettype($paramsObj->id)!="integer")  ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer when updating a ".$this->objectName.".  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if((gettype($paramsObj->isStandard)!="boolean") && ($paramsObj->isStandard!=NULL)) 
                {
                    $this->setErrorMessage("901", "Invalid parameter - 'isstandard' must be a boolean when updating a ".$this->objectName.".");
                    return false;
                }
                return true;

            case "delete":
                if(!isset($paramsObj->id)) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a ".$this->objectName.".");
                    return false;
                }
                return true;

            case "create":
                if( ($paramsObj->note==NULL) || ($paramsObj->note=='') ) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'note' field is required when creating a ".$this->objectName.".");
                    return false;
                }
                if(!(gettype($paramsObj->isStandard)=="boolean"))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'isstandard' must be a boolean when creating a ".$this->objectName.".");
                    return false;
                }
                return true;

            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }
    


    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML($format='standard', $parts='all')
    {
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml = "<".$this->xmlTag." id=\"".$this->id."\" isStandard=\"".fromPGtoStringBool($this->isStandard)."\">".escapeXMLChars($this->note);
            
            if($this->includePermissions===TRUE) 
            {
                $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->canCreate)."\" ";
                $xml.= "canUpdate=\"".fromPHPtoStringBool($this->canUpdate)."\" ";
                $xml.= "canDelete=\"".fromPHPtoStringBool($this->canDelete)."\" />\n";
            } 

            $xml.="</".$this->xmlTag.">\n";
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.isadmin(".$securityUserID.") where isadmin=true";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
        
            if(pg_num_rows($result)>0)
            { 
                $this->canCreate = true;
                $this->canUpdate = true;
                $this->canDelete = true;
                $this->includePermissions = TRUE;
            }
            else
            {
                $this->canCreate = true;
                $this->canUpdate = false;
                $this->canDelete = false;
                $this->includePermissions = TRUE;
            }
    
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        //$xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tlkpsitenote")."'>";
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
                    $sql = "insert into ".$this->tableName." (note, isstandard) values ('".$this->note."', '".fromPHPtoPGBool($this->isStandard)."')";
                    $sql2 = "select ".$this->objectField." as id from ".$this->tableName." where ".$this->objectField."=currval('".$this->tableName."_".$this->objectName."id_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update ".$this->tableName." set note='".$this->note."', isstandard='".fromPHPtoPGBool($this->isStandard)."' where ".$this->objectField."=".$this->id;
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
                        $this->id=$row['id'];   
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

                $sql = "delete from ".$this->tableName." where ".$this->objectField."=".$this->id;

                // Run SQL command
                if (isset($sql))
                {
                    // Run SQL 
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
                        switch($PHPErrorCode)
                        {
                        case 23503:
                                // Foreign key violation
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all uses of this note before deleting the note itself.");
                                break;
                        default:
                                // Any other error
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        }
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
        else
        {
            $this->setErrorMessage("002", "Already errors so not deleting");
        }

        // Return true as write to DB went ok.
        return TRUE;
    }
    
    function assignToParent($theParentID)
    {
        global $dbconn;
        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // First check this combination exists
                $sql = "select * from ".$this->joinTableName." where ".$this->parentField."=$theParentID and ".$this->objectField."=".$this->id;
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)>0)
                {
                    $this->setErrorMessage("906", "This ".$this->objectName." is already assigned to this record.");
                    return FALSE;
                }
                else
                {
                    // Add note
                    $sql = "insert into ".$this->joinTableName." (".$this->parentField.", ".$this->objectField.") values ($theParentID, ".$this->id.")";
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
    
    function unassignToParent($theParentID)
    {
        global $dbconn;
        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // First check this combination exists
                $sql = "select * from ".$this->joinTableName." where ".$this->parentField."=$theParentID and ".$this->objectField."=".$this->id;
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_num_rows($result)==0)
                {
                    $this->setErrorMessage("906", "This ".$this->objectName." is not assigned to this ".$this->parentName.".");
                    return FALSE;
                }
                else
                {
                    $sql = "delete from ".$this->joinTableName." where ".$this->parentField."=$theParentID and ".$this->objectField."=".$this->id;
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


class siteNote extends note
{
    function __construct()
    {
        parent::__construct('site'); 
    }

}

class treeNote extends note
{
    function __construct()
    {
        parent::__construct('tree'); 
    }

}

class vmeasurementNote extends note
{
    function __construct()
    {
        parent::__construct('vmeasurement'); 
    }

}

class readingNote extends note
{
    function __construct()
    {
        parent::__construct('reading'); 
    }

}
?>
