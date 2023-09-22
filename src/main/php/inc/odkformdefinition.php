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
 * Class for interacting with a radiusEntity.  This contains the logic of how to read and write data from the database as well as error checking etc.
 *
 */
class odkFormDefinition extends odkFormDefinitionEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "odkFormDefinition";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
    	$this->setID($row['odkdefinitionid']);
     	$this->setName($row['name']);
     	$this->setOwnerID($row['ownerid']);
     	$this->setDefinition($row['definition']);
     	$this->setIsPublic($row['ispublic']);
     	
     	
        return true;
    }

    /**
     * Set the current objects parameters from the database
     *
     * @param Integer $theID
     * @return Boolean
     */
    function setParamsFromDB($theID)
    {
	global $firebug;
        global $dbconn;
        
        $this->setID($theID);
        $sql = "SELECT * FROM tblodkdefinition WHERE odkdefinitionid='".pg_escape_string($dbconn, $this->getID())."'";
		$firebug->log($sql, "odkformdefinition sql");
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified form id");
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
            $this->setErrorMessage("001", "Error connecting to database");
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
	global $firebug;
        global $dbconn;
        return TRUE;
    }
    
    /**
     * Set parameters based on those in a parameters class
     *
     * @param tagParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {    	
    	global $firebug;
    	
    	
    	$this->setID($paramsClass->getID());
     	$this->setName($paramsClass->getName());
     	$this->setDefinition($paramsClass->getDefinition());
	$this->setOwnerID($paramsClass->getOwnerID());
	$this->setIsPublic($paramsClass->getIsPublic());


    	$firebug->log($paramsClass, "params class");
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
	global $firebug;
	
	$firebug->log("validateRequestParams called in odkformdefinition.php");
    
        // Check parameters based on crudMode    	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID()==NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
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
                if($paramsObj->getID() ==NULL) 
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
            return $this->_asXML($format, $parts);
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary', 'minimal' or 'comprehensive'");
            return false;
        }
    }


    private function _asXML($format, $parts)
    {

        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()===NULL)
        {
            // Only return XML when there are no errors.
            if( ($parts=="all") || ($parts=="beginning"))
            {
                $xml.="<odkFormDefinition ";
				$xml.="id=\"".$this->getID()."\" ";
				$xml.="name=\"".$this->getName()."\" ";
				$xml.="ispublic=\"".dbHelper::formatBool($this->getIsPublic(), "english")."\" >\n";
			$xml.=$this->getDefinition();
				
                $xml.="</odkFormDefinition>\n";
                             
                
            }

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

    function writeToDB($crudMode="create")
    {
    	global $dbconn;
    	global $firebug;
    	global $domain;
	global $myMetaHeader;
	global $myAuth;
	
	if($crudMode=="update" && $this->getID()==NULL)
        {
	    $this->setErrorMessage("902", "Missing parameter - 'id' field is required when editing");
            return FALSE;
        }

	if($this->getOwnerID()==NULL) $this->setOwnerID($myAuth->getID());
	
	if( $crudMode=="create" )
	{
	 
	   if($this->getID()==NULL) $this->setID(uuid::getUUID(), $domain);  
	
	   $sql = "INSERT INTO tblodkdefinition (odkdefinitionid, name, ownerid, ispublic, definition) values (";
	   $sql.= dbHelper::tellervo_pg_escape_string($dbconn, $this->getID()). ", ";	
	   $sql.= dbHelper::tellervo_pg_escape_string($dbconn, $this->getName()). ", ";	
           $sql.= dbHelper::tellervo_pg_escape_string($dbconn, $this->getOwnerID()).  ", ";     
           $sql.= dbHelper::formatBool($this->getIsPublic(), 'pg').  ", ";     
	   $sql.= dbHelper::tellervo_pg_escape_string($dbconn, $this->getDefinition()).  ", "; 
	   $sql = substr($sql, 0, -2);
           $sql.=")";
           
           $firebug->log($sql, "SQL");
           
           
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
			default:
			    $this->setErrorMessage("002", "Error code: ".$PHPErrorCode."  -  ".pg_result_error($result)."--- SQL was $sql");
			    return FALSE;
		    }
		   
		}
	    }
	    
          	
	}
	else if ($crudMode=="update")
	{
	   $sql = "UPDATE tblodkdefinition SET ";
	   $sql.= "ownerid=".dbHelper::tellervo_pg_escape_string($dbconn, $this->getOwnerID()).", ";
	   $sql.= "name=".dbHelper::tellervo_pg_escape_string($dbconn, $this->getName()).", ";
	   $sql.= "definition=".dbHelper::tellervo_pg_escape_string($dbconn, $this->getDefinition()).", ";
	   $sql.= "WHERE odkdefinitionid=". dbHelper::tellervo_pg_escape_string($dbconn, $this->getID());

           
           $firebug->log($sql, "SQL");
           
           
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
		   
		    default:
			    // Any other error
			    $this->setErrorMessage("002", "Error code: ".$PHPErrorCode."  -  ".pg_result_error($result)."--- SQL was $sql");
			    return FALSE;
		    }
		   
		}
	    }
	    
	    $this->assignToSeries();
	    
	}
	else
	{
	    $this->setErrorMessage("667", "Invalid mode specified in writeToDB().  Only create and update are supported");
	    return FALSE;
	
	}
			

	
	return TRUE;
    }
    
    function deleteFromDB()
    {
    
        global $dbconn;
    	global $firebug;
    	global $auth;

	if($this->getID()=='all')
	{
		$sql = "DELETE FROM tblodkdefinition WHERE ownerid='".$auth->getUserID()."'";
	}
	else
	{
		$sql = "DELETE FROM tblodkdefinition WHERE odkdefinitionid='".$this->getID()."' AND ownerid='".$auth->getUserID()."'";
	}
	      	
	      	$firebug->log($sql, "odk form delete SQL");
	      	
	      	// Run SQL 
		pg_send_query($dbconn, $sql);
		$result = pg_get_result($dbconn);
		if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
		{
		    $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
		    switch($PHPErrorCode)
		    {
		     		    
		    default:
			    // Any other error
			    $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
		    }
		    return FALSE;
		}
		
	return TRUE;
    }

    function mergeRecords($mergeWithID)
    {

    }
    
// End of Class
} 
?>
