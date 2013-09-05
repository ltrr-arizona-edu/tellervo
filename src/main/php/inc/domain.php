<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('inc/dbEntity.php');

/**
 * Class for interacting with a domainEntity.  This contains the logic of how to read and write data from the database as well as error checking etc.
 *
 */
class domain extends dbEntity implements IDBAccessor
{	  

	protected $domainid;
	protected $domain;
	protected $prefix;
	
    /***************/
    /* CONSTRUCTOR */
    /***************/


    
    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
       $this->domainid = $row['domainid'];
       $this->domain = $row['domain'];
       $this->prefix = $row['prefix'];

       return true;
    }

    
    function setDomain($domain)
    {
    	$this->domain = $domain;
    }
    
    function getDomain()
    {
    	return $this->domain;
    }
    
    function setPrefix($prefix)
    {
    	$this->prefix = $prefix;
    }
    
    function getPrefix()
    {
    	return $this->prefix;
    }

    
    
    /**
     * Set the current objects parameters from the database
     *
     * @param Integer $theID
     * @return Boolean
     */
    function setParamsFromDB($theID)
    {
        global $dbconn;
        global $firebug;

        $firebug->log("setParamsFromDB() called in domain.php");
        
        if($theID==NULL) return FALSE;
        
        $this->setID($theID);
        $sql = "SELECT * from tlkpdomain WHERE domainid='".pg_escape_string($this->getID())."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
            	$firebug->log(__LINE__, "line no");
            	$firebug->log($sql, "sql is");
                // No records match the id specified
                trigger_error("903"."No records match the specified domain id", E_USER_ERROR);
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->setParamsFromDBRow($row);
            }
        }
        else
        {
            // Connection bad
            trigger_Error("001"."Error connecting to database", E_USER_ERROR);
            return FALSE;
        }

        $this->cacheSelf();
        return TRUE;
    }
    

    function setParentsFromDB()
    {

    }      
    
    function setChildParamsFromDB()
    {
       
    }
    
    /**
     * Set parameters based on those in a parameters class
     *
     * @param boxParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {    	
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        $this->setDomain($paramsClass->getDomain());
        $this->setID($paramsClass->getID());	
        $this->setPrefix($paramsClass->getPrefix());
        
        return true;  
    }

    /**
     * Validate parameters from a parameters class
     *
     * @param radiusParameters $paramsObj
     * @param String $crudMode
     * @return unknown
     */
    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode    	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID()==NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required when reading a radius.", E_USER_ERROR);
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->getID()==NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->getID() == NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
                return true;

            case "create":
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
            switch($format)
        {
        case "comprehensive":
        case "standard":
        case "summary":
        case "minimal":
        	global $dbconn;
        	global $tellervoNS;
        	global $tridasNS;
        	global $gmlNS;
        	 
        	return $this->_asXML($format, $parts);
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary', 'minimal' or 'comprehensive'");
            return false;
        }
    }


    private function _asXML($format, $parts)
    {

        global $domain;
        global $firebug;
		$xml = NULL;
		
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
    		$xml = "<domain ";
    		
    		if(isset($this->prefix))
    		{
    			$xml.=" prefix=\"".$this->prefix."\" ";
		
    		}
    		$xml.=">".$this->domain."</domain>\n";
        	
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    /***********/
    /*FUNCTIONS*/
    /***********/

    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        global $domain;
        global $firebug;
        $sql = NULL;
        $sql2 = NULL;
        
        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {

               // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->getID() == NULL)
                {
                    // New record
                    
                          	
                	
                    $sql = "INERT INTO tlkpdomain ( ";
                        if($this->getDomain()!=NULL)                   				$sql.="domain, ";
                    	if($this->getID()!=NULL) 									$sql.="domainid, "; 
                    	if($this->getPrefix()!=NULL)								$sql.="prefix, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if($this->getTitle()!=NULL)                   				$sql.="'".pg_escape_string($this->getDomain())."', ";
                        if($this->getID()!=NULL)                   					$sql.="'".pg_escape_string($this->getID())."', ";
                        if($this->getComments()!=NULL)                   			$sql.="'".pg_escape_string($this->getPrefix())."', ";
                   // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tlkpdomain where domainid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql.="UPDATE tlkpdomain SET ";
                        $sql.="domain='".pg_escape_string($this->getDomain())."', ";
                        $sql.="prefix='".pg_escape_string($this->getPrefix())."', ";
						
                    $sql = substr($sql, 0, -2);
                    $sql.= " WHERE domainid='".pg_escape_string($this->getID())."'";
                    $firebug->log($sql, "Domain update SQL");
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
        global $firebug;

        // Check for required parameters
        if($this->getID() == NULL) 
        {
            trigger_error("902". "Missing parameter - 'id' field is required.", E_USER_ERROR);
            return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {

                $sql = "DELETE FROM tlkpdomain WHERE domainid='".pg_escape_string($this->getID())."'";

                $firebug->log($sql, "delete sql");
                
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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete or reassign all associated records before deleting this domain.");
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

    function mergeRecords($newParentID)
    {
    	return false;
    }
    
// End of Class
} 
?>
