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
class loan extends loanEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "loan";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
    	$this->setID($row['loanid']);
        $this->setCreatedTimestamp($row['issuedate']);
     	$this->setFirstName($row['firstname']);
     	$this->setLastName($row['lastname']);
     	$this->setOrganisation($row['organisation']);
     	$this->setNotes($row['notes']);
		$this->setDueDate($row['duedate']);
		$this->setFilesFromStrArray($row['files']);
		$this->setReturnDate($row['returndate']);
		
		$this->setChildParamsFromDB();
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
        $sql = "SELECT * FROM vwtblloan WHERE loanid='".pg_escape_string($this->getID())."'";
		$firebug->log($sql, "loan sql");
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
        // Add the id's of the current objects direct children from the database

        global $dbconn;
        global $firebug;
                
        $sql  = "select sampleid from tblcuration where loanid='".pg_escape_string($this->getID())."'";
        
        $firebug->log($sql, "Setting samples associated with a loan");
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {

            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                array_push($this->entityIdArray, $row['sampleid']);
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
    
    /**
     * Set parameters based on those in a parameters class
     *
     * @param radiusParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {    	
    	global $firebug;
    	
    	$this->setFirstName($paramsClass->getFirstName());
    	$this->setLastName($paramsClass->getLastName());
    	$this->setOrganisation($paramsClass->getOrganisation());
    	$this->setDueDate($paramsClass->getDueDate());
    	$this->setReturnDate($paramsClass->getReturnDate());
    	
    	$this->setFiles($paramsClass->getFiles());
    	$this->entityIdArray = $paramsClass->entityIdArray;
    	
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
        // Check parameters based on crudMode    	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID()===NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required when reading a loan.", E_USER_ERROR);
                    return false;
                }
                
                return true;
         
            case "update":
                if($paramsObj->getID()===NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
                
                return true;

            case "delete":
                if($paramsObj->getID() ===NULL) 
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
                $xml.= "<loan>\n";
				$xml.="<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>\n";
				$xml.="<issuedate>".$this->getCreatedTimestamp()."</issuedate>\n";
				if($this->getDueDate()!=null)
				{
					$xml.="<duedate>".$this->getDueDate()."</duedate>";
				}
                if($this->getReturnDate()!=null)
                {
                	$xml.="<returndate>".$this->getReturnDate()."</returndate>\n";
                }

                $xml.= "<firstname>".dbhelper::escapeXMLChars($this->getFirstName())."</firstname>\n";
                $xml.= "<lastname>".dbhelper::escapeXMLChars($this->getLastName())."</lastname>\n";
                $xml.= "<organisation>".dbhelper::escapeXMLChars($this->getOrganisation())."</organisation>\n";
                $xml.= $this->getFileXML();
                
                if($this->getNotes()!=null)
                {
                	$xml.= "<notes>".dbhelper::escapeXMLChars($this->getNotes())."</notes>\n";
                }
                
                foreach ($this->entityIdArray as $entityid)
                {
                	$sample = new sample();
                	$sample->setParamsFromDB($entityid);	            	
                	$xml.= $sample->asXML();
                }
                
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</loan>\n";
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
    	global $domain;
    	global $firebug;
    	global $myMetaHeader;
    	$sql = NULL;
    	$sql2 = NULL;
    	$newRecord = false;
    	
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
    				$newRecord = true;
    				// Generate a new UUID pkey
    				$this->setID(uuid::getUUID(), $domain);
    				 
    				$sql = "INSERT INTO tblloan ( ";
    				$sql.="loanid, ";
    				$sql.="firstname, ";
    				$sql.="lastname, ";
    				$sql.="organisation, ";
    				$sql.="duedate, ";
    				$sql.="files, ";
    				$sql.="notes, ";
    				$sql.="returndate, ";
    				$sql = substr($sql, 0, -2);
    				$sql.=") VALUES (";
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getID()).", ";
    				
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getFirstName()).", ";
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getLastName()).", ";
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getOrganisation()).", ";
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getDueDate()).", ";
    				$sql.=dbHelper::phpArrayToPGStrArray($this->getFiles()).", ";
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getNotes()).", ";
    				$sql.=dbHelper::tellervo_pg_escape_string($this->getReturnDate()).", ";
    				
    				// Trim off trailing space and comma
    				$sql = substr($sql, 0, -2);
    				$sql.=")";
    				$sql2 = "SELECT * FROM tblloan WHERE loanid='".$this->getID()."'";
    			}
    			else
    			{
    				// Updating DB
    				$sql.="UPDATE tblloan SET ";
    				$sql.="loanid="         .dbHelper::tellervo_pg_escape_string($this->getID()).", ";
    				$sql.="firstname="      .dbHelper::tellervo_pg_escape_string($this->getFirstName()).", ";
    				$sql.="lastname="     	.dbHelper::tellervo_pg_escape_string($this->getLastName()).", ";
    				$sql.="organisation="	.dbHelper::tellervo_pg_escape_string($this->getOrganisation()).", ";
    				$sql.="duedate="		.dbHelper::tellervo_pg_escape_string($this->getDueDate()).", ";
    				$sql.="files="	 		.dbHelper::phpArrayToPGStrArray($this->getFiles()).", ";
    				$sql.="notes="			.dbHelper::tellervo_pg_escape_string($this->getNotes()).", ";
    				$sql.="returndate="		.dbHelper::tellervo_pg_escape_string($this->getReturnDate()).", ";
    				
    				$sql = substr($sql, 0, -2);
    				$sql.= " WHERE loanid=".dbHelper::tellervo_pg_escape_string($this->getID());
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
    					$this->setCreatedTimestamp($row['issuedate']);
    				}
    			}
    			
    			
    			if($newRecord)
    			{    				
    				// Add tblcuration records when creating a new loan.  Note that tblcuration records cannot be edited    				
    				$firebug->log("Writing new loan curation events");
	    			$transaction = array("BEGIN;");
					// Now add tblcuration records for each sample
	    			foreach ($this->entityIdArray as $entityid)
	    			{
	    				$curationsql = "INSERT INTO tblcuration (curationstatusid, curatorid, sampleid, loanid) values (";
	    				$curationsql .= "2, ".$myMetaHeader->securityUserID.", '".$entityid."', '".$this->getID()."')";
	    				
	    				array_push($transaction, $curationsql);
	    			}

    			}
    			else
    			{
    				// Editing an existing record.  
    				// If returnDate is set, then set the curation status of any samples marked as 'on loan' to 'archived'
    				
    				$firebug->log("Editing existing record...");
    				
    				$transaction = array("BEGIN;");
    				$getSampleIDSQL = "SELECT * from tblcuration where loanid='".$this->getID()."'";


    				pg_send_query($dbconn, $getSampleIDSQL);
    				$result = pg_get_result($dbconn);
    				if(pg_num_rows($result)==0)
    				{
    					// No records match
    					$this->setErrorMessage("903", "No records match when trying to find curation events for loan ".$this->getID());
    					return FALSE;
    				}
    				else
    				{
   						while ($row = pg_fetch_array($result))
   						{
   							if($row['curationstatusid']==2 || $row['curationstatusid']==3)
    						{
    							$curationsql = "INSERT INTO tblcuration (curationstatusid, curatorid, sampleid) values (";
	    						$curationsql .= "1, ".$myMetaHeader->securityUserID.", '".$row['sampleid']."')";
	    			
	    						array_push($transaction, $curationsql);
    						}
    					}
   					}    					
    			}
    			

    			$firebug->log($transaction, "Curation event transactions");
    			
    			foreach($transaction as $stmt)
    			{
    				$firebug->log("Processing curation event transaction...");
    				if ($stmt==NULL) continue;
    			
    				$firebug->log($stmt, "Transaction statement");
    				pg_send_query($dbconn, $stmt);
    				while ($result = pg_get_result($dbconn))
    				{
    					if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
    					{
    						trigger_error("002".pg_result_error($result)."--- SQL was $stmt", E_USER_ERROR);
    			
    						pg_query($dbconn, "rollback;");
    							
    						$this->internalDeleteFromDB();
    						return FALSE;
    					}
    				}
    			}
    			
    			// All gone well so commit transaction to db
    			$result = pg_query($dbconn, "commit;");
    			
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

    
    function internalDeleteFromDB()
    {
    	// Delete the record in the database matching the current object's ID
    	
    	global $dbconn;
    	
    	// Check for required parameters
    	if($this->getID() == NULL)
    	{
    		$this->setErrorMessage("902", "Missing parameter - 'id' field is required.");
    		return FALSE;
    	}
    	
    	//Only attempt to run SQL if there are no errors so far
    	if($this->getLastErrorCode() == NULL)
    	{
    		$dbconnstatus = pg_connection_status($dbconn);
    		if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    		{
    	
    			$sql = "DELETE FROM tblloan WHERE loanid='".pg_escape_string($this->getID())."'";
    	
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
    							$this->setErrorMessage("907", "Foreign key violation.  You must delete all associated records before deleting this loan.");
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
    
    
    function deleteFromDB()
    {
    

        // Return true as write to DB went ok.
        return TRUE;
    }

    function mergeRecords($mergeWithID)
    {

	
    }
    
// End of Class
} 
?>
