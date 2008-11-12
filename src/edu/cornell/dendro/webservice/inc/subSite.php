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
require_once('inc/element.php');

class subSite 
{
    var $id = NULL;
    var $name = NULL;
    var $siteID = NULL;
    var $childArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
    
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;
    var $includePermissions = FALSE;


    var $parentXMLTag = "subSites"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function subSite()
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
    
    function setSiteID($theID)
    {
        // Set the current objects note.
        $this->siteID=$theID;
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

        if($theID==NULL)
        {
            $this->setErrorMessage("903", "No subsite id specified");
            return FALSE;
        }
        
        $this->id=$theID;
        $sql = "select * from tblsubsite where subsiteid=$theID";
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
                $this->siteID = $row['siteid'];
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
        // Siteelements
        
        if($this->id==NULL)
        {
            $this->setErrorMessage("903", "No subsite id specified");
            return FALSE;
        }

        global $dbconn;
        
        $sql = "select elementid from tblelement where subsiteid=".$this->id;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                array_push($this->childArray, $row['elementid']);
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
        if (isset($paramsClass->name))   $this->name   = $paramsClass->name;
        if (isset($paramsClass->siteID)) $this->siteID = $paramsClass->siteID;

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
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a subsite.");
                    return false;
                }
                if( (gettype($paramsObj->id)!="integer") && ($paramsObj->id!=NULL) ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer when reading a sub site.  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer when reading a sub site.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a sub site.");
                    return false;
                }
                if(($paramsObj->siteID==NULL) && ($paramsObj->name==NULL) && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update for this sub site.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a sub site.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->id == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'subsiteid' field is required when creating a element.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a sub site.");
                        return false;
                    }
                    if($paramsObj->siteID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'siteid' field is required when creating a sub site.");
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

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, 'subSite', $this->id)";
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
    
    function asXML($format='standard', $parts='all')
    {
        switch($format)
        {
        case "comprehensive":
            require_once('site.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid FROM tblsubsite where tblsubsite.subsiteid='".$this->id."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for subsite id=".$this->id);
                    echo "$sql";
                    return FALSE;
                }
                else
                {
                    $row = pg_fetch_array($result);

                    $mySite = new site();

                    $success = $mySite->setParamsFromDB($row['siteid']);
                    if($success===FALSE)
                    {
                        trigger_error($mySite->getLastErrorCode().$mySite->getLastErrorMessage());
                        echo "here";
                    }

                    $xml = $mySite->asXML("summary", "beginning");
                    $xml.= $this->_asXML("standard", "all");
                    $xml.= $mySite->asXML("summary", "end");

                }
            }
            return $xml;
        
        case "standard":
            return $this->_asXML($format, $parts);
        case "summary":
            return $this->_asXML($format, $parts);
        case "minimal":
            return $this->_asXML($format, $parts);
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary' or 'comprehensive'");
            return false;
        }
    }

    private function _asXML($format, $parts)
    {
        global $domain;
        $xml="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($parts=="all") || ($parts=="beginning"))
            {
                // Only return XML when there are no errors.
                $xml = "<subSite ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("subSite", $this->id)."\n";
                
                // Include permissions details if requested
                if($this->includePermissions===TRUE) 
                {
                    $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->canCreate)."\" ";
                    $xml.= "canUpdate=\"".fromPHPtoStringBool($this->canUpdate)."\" ";
                    $xml.= "canDelete=\"".fromPHPtoStringBool($this->canDelete)."\" />\n";
                } 

                $xml.= "<name>".escapeXMLChars($this->name)."</name>\n";

                if($format!="minimal")
                {

                    $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                    $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>";

                    if($format!="summary")
                    { 
                        // Include subSite notes if present
                        if ($this->childArray)
                        {
                            $xml.="<references>\n";
                            foreach($this->childArray as $value)
                            {
                                $myTree = new element();
                                $success = $myTree->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$myTree->asXML("minimal", "all");
                                }
                                else
                                {
                                    $myMetaHeader->setErrorMessage($myTree->getLastErrorCode, $myTree->getLastErrorMessage);
                                }
                            }
                            $xml.="</references>\n";
                        }
                    }
                }
            }

            if(($parts=="all") || ($parts=="end"))
            {
                // End XML tag
                $xml.= "</subSite>\n";
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
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblsubsite")."'>";
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
        $sql  = NULL;
        $sql2 = NULL;
        
        // Check for required parameters
        if($this->name == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            return FALSE;
        }
        if($this->siteID == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'siteid' field is required.");
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
                    $sql = "insert into tblsubsite (name, siteid) values ('".$this->name."', '".$this->siteID."')";
                    $sql2 = "select * from tblsubsite where subsiteid=currval('tblsubsite_subsiteid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblsubsite set name='".$this->name."' where subsiteid=".$this->id;
                }

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
                        case 23505:
                                // Foreign key violation
                                $this->setErrorMessage("908", "Unique constraint violation.  A sub site with this name already exists.");
                                break;
                        default:
                                // Any other error
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        }
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
                        $this->id=$row['subsiteid'];   
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

                $sql = "delete from tblsubsite where subsiteid=".$this->id;

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all elements from a sub site before deleting the sub site itself.");
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
