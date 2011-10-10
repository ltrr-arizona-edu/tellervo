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
require_once('dbhelper.php');

class securityGroup extends securityGroupEntity implements IDBAccessor
{


    var $parentXMLTag = "securityGroup"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function securityGroup()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/
    


    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database
	
        global $dbconn;
        global $firebug;
        
        $firebug->log($theID);
        
        $this->id=$theID;
        $sql = "select * from tblsecuritygroup where securitygroupid=$theID";
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
                $this->id = $row['securitygroupid'];
                $this->name = $row['name'];
                $this->description = $row['description'];
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
        global $dbconn;
        
        $this->userMembersArray = array();
        
        $sql = "SELECT securityuserid from tblsecurityusermembership where securitygroupid='".$this->id."' order by securityuserid asc";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            // Set parameters from db
            while ($row = pg_fetch_array($result))
            {
                array_push($this->userMembersArray, $row['securityuserid']);
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }
        
        
        
        $this->groupMembersArray = array();
        
        $sql = "SELECT childsecuritygroupid from tblsecuritygroupmembership where parentsecuritygroupid='".$this->id."' order by childsecuritygroupid asc";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            // Set parameters from db
            while ($row = pg_fetch_array($result))
            {
                array_push($this->groupMembersArray, $row['childsecuritygroupid']);
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
        if (isset($paramsClass->name))         $this->setName($paramsClass->name);
        if (isset($paramsClass->description))  $this->setDescription($paramsClass->description);
        if (isset($paramsClass->isActive))     $this->setIsActive($paramsClass->isActive);
        if (isset($paramsClass->userMembers))     $this->userMembers = $paramsClass->userMembers;
        if (isset($paramsClass->groupMembers))     $this->groupMembers = $paramsClass->groupMembers;
        
        return true;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer when reading groups.");
                    return false;
                }
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a groups.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a group.");
                    return false;
                }
                if(($paramsObj->name == NULL) && ($paramsObj->description==NULL) && ($paramsObj->isActive===NULL)) 
                {
                    $this->setErrorMessage("902","Missing parameter(s) - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a group.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->name == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a group.");
                    return false;
                }
                if($paramsObj->description == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'description' field is required when creating a group.");
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

    function asXML($mode="comprehensive")
    {
    	global $firebug;
        $xml = NULL;

        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml.= "<securityGroup ";
            $xml.= "id=\"".$this->id."\" ";
            $xml.= "name=\"".$this->name."\" ";
            $xml.= "description=\"".$this->description."\" ";
            $xml.= "isActive=\"".dbHelper::fromPGtoStringBool($this->isActive)."\" ";
        

            if($mode=="comprehensive") 
            {
                 // Add userMembers if there are any
            	if(count($this->userMembersArray)>0)
            	{
	            	$xml.=" userMembers=\"";
	            	
	            	foreach($this->userMembersArray as $id)
	            	{
	            		$xml.= $id ." ";
	            	}
	            	
	            	// Trim off trailing space
	            	$xml = substr($xml, 0, -1);
	            	
	            	$xml.="\"";
            	}
            	
                // Add groupMembers if there are any
            	if(count($this->groupMembersArray)>0)
            	{
	            	$xml.=" groupMembers=\"";
	            	
	            	foreach($this->groupMembersArray as $id)
	            	{
	            		$xml.= $id ." ";
	            	}
	            	
	            	// Trim off trailing space
	            	$xml = substr($xml, 0, -1);
	            	
	            	$xml.="\"";
            	}
            }

            // End XML tag
            $xml.= "/>\n";

            return $xml;
        }
        else
        {
        	$firebug->log($this->lastErrorMessage, "error in asXML");
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
        global $firebug;
        $sql = NULL;
        $sql2 = NULL;

        // Check for required parameters
        if($this->name == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            return FALSE;
        }
        
        if($this->description == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'description' field is required.");
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
                    $sql = "insert into tblsecuritygroup (name, description, isactive) values (";
                    $sql.= "'".$this->getName()."', ";
                    $sql.= "'".$this->getDescription()."', ";
                    $sql.= "'".dbhelper::formatBool($this->getIsActive(),"pg")."'";
                    $sql.= " )";
                    $sql2 = "select * from tblsecuritygroup where securitygroupid=currval('tblsecuritygroup_securitygroupid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblsecuritygroup set ";
                    $sql.= "name = '".$this->getName()."', ";
                    $sql.= "description = '".$this->getDescription()."', ";
                    $sql.= "isactive = '".dbhelper::formatBool($this->getIsActive(),"pg")."'";
                    
                    $sql.= " where securitygroupid='".$this->id."'";
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
                        $this->id=$row['securitygroupid'];   
                    }
                }
                
                
                // Set or unset members for this group                 
                //if(count($this->userMembers)>0)
                //{
                    $sql = "delete from tblsecurityusermembership where securitygroupid=".$this->id;
                    $result = pg_query($dbconn, $sql);

                    foreach($this->userMembers as $item)
                    {
                        $sql = "insert into tblsecurityusermembership (securitygroupid, securityuserid) values ('$this->id', '$item')";
                        $firebug->log($sql, "Membership SQL"); 
                        $result = pg_query($dbconn, $sql);
                    }
                //} 
                
                // Set or unset groups for this group                 
                if(count($this->groupMembers)>0)
                {
                    $sql = "delete from tblsecuritygroupmembership where parentsecuritygroupid=".$this->id;
                    $result = pg_query($dbconn, $sql);

                    foreach($this->groupMembers as $item)
                    {
                    	if($item!="")
                    	{
	                        $sql = "insert into tblsecuritygroupmembership (parentsecuritygroupid, childsecuritygroupid) values ('$this->id', '$item')";
	                        $firebug->log($sql, "membership SQL"); 
	                        $result = pg_query($dbconn, $sql);
                    	}
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

                $sql = "delete from tblsecuritygroup where securitygroupid=".$this->id;

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
    
    function mergeRecords($newParentID)
    {
    	return false;
    }

// End of Class
} 
?>
