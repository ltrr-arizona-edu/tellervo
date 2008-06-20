<?php
//*******************************************************************
////// PHP Corina Middleware
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('dbhelper.php');

class radius 
{
    var $id = NULL;
    var $name = NULL;
    var $specimenID = NULL;
    
    var $measurementArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
    
    var $includePermissions = FALSE;
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;


    var $parentXMLTag = "radii"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function radius()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/

    function setName($theName)
    {
        // Set the current objects note.
        $this->name=$theName;
    }
    
    function setSpecimenID($theSpecimenID)
    {
        // Set the current objects note.
        $this->specimenID=$theSpecimenID;
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
        $sql = "select * from tblradius where radiusid=$theID";
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
                $this->name = $row['name'];
                $this->id = $row['radiusid'];
                $this->specimenID = $row['specimenid'];
                $this->createdTimeStamp = $row['createdtimestamp'];
                $this->lastModifiedTimeStamp = $row['lastmodifiedtimestamp'];
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
        // Add the id's of the current objects direct children from the database
        // RadiusRadiusNotes

        global $dbconn;

        $sql  = "select measurementid from tblmeasurement where radiusid=".$this->id;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {

            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->measurementArray, $row['measurementid']);
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
        if (isset($paramsClass->name))       $this->name       = $paramsClass->name;
        if (isset($paramsClass->specimenID)) $this->specimenID = $paramsClass->specimenID;

        return true;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a radius.");
                    return false;
                }
                if( (gettype($paramsObj->id)!="integer") && ($paramsObj->id!=NULL) ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer.  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                if(($paramsObj->specimenID==NULL) 
                    && ($paramsObj->name==NULL)
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->id == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'radiusid' field is required when creating a measurement.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a radius.");
                        return false;
                    }
                    if($paramsObj->specimenID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'specimenid' field is required when creating a radius.");
                        return false;
                    }
                }
                return true;

            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }

    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, 'radius', $this->id)";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            $row = pg_fetch_array($result);
            
            $this->canCreate = fromPGtoPHPBool($row['cancreate']);
            $this->canUpdate = fromPGtoPHPBool($row['canupdate']);
            $this->canDelete = fromPGtoPHPBool($row['candelete']);
            $this->includePermissions = TRUE;
    
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

    function asXML($mode="all", $includePermissions=False)
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if($style=="full")
            {
                // Only return XML when there are no errors.
                $xml.= "<radius ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("radius", $this->id)."\n ";
                
                // Include permissions details if requested
                if($this->includePermissions===TRUE) 
                {
                    $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->canCreate)."\" ";
                    $xml.= "canUpdate=\"".fromPHPtoStringBool($this->canUpdate)."\" ";
                    $xml.= "canDelete=\"".fromPHPtoStringBool($this->canDelete)."\" />\n";
                } 
                
                $xml.= "<name>".escapeXMLChars($this->name)."</name>\n";
                $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";
                $xml.= "</radius>\n";
            }
            elseif($style=="brief")
            {
                $xml.= "<radius ";
                $xml.= "id=\"".$this->id."\" ";
                $xml.= "url=\"http://$domain/radius/".$this->id."\" />\n ";

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
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblradius")."'>";
        return $xml;
    }

    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
    }

    function getID()
    {
        return $this->id;
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
        $sql = NULL;
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
                    $sql = "insert into tblradius (name, specimenid) values ('".$this->name."', '".$this->specimenID."')";
                    $sql2 = "select * from tblradius where radiusid=currval('tblradius_radiusid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblradius set name='".$this->name."', specimenid='".$this->specimenID."' where radiusid=".$this->id;
                    $sql = "update tblradius set ";
                    if (isset($this->name)) $sql.="name='".$this->name."', ";
                    if (isset($this->specimenid)) $sql.="specimenid='".$this->specimenID.", ";
                    $sql = substr($sql, 0, -2);
                    $sql.= " where radiusid=".$this->id;
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
                        $this->id=$row['radiusid'];   
                        $this->createdTimeStamp=$row['createdtimestamp'];   
                        $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
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

                $sql = "delete from tblradius where radiusid=".$this->id;

                // Run SQL command
                if ($sql)
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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated measurements before deleting this radius.");
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

        // Return true as write to DB went ok.
        return TRUE;
    }

// End of Class
} 
?>
