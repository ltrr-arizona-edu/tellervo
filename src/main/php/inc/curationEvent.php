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
require_once('inc/box.php');
require_once('inc/sample.php');

/**
 * Class for interacting with a curationEvent.  This contains the logic of how to read and write data from the database as well as error checking etc.
 *
 */
class curationEvent extends curationEventEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "curationEvent";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
    	$this->setID($row['curationeventid']);
        $this->setCreatedTimestamp($row['createdtimestamp']);
     	$this->setNotes($row['notes']);
     	$this->curator->setParamsFromDB($row['curatorid']);
     	$this->setCurationStatus($row['curationstatusid'], $row['curationstatus']);
			
     	if($row['sampleid']!=null)
     	{
     		$this->sample = new sample();
     		$this->sample->setParamsFromDB($row['sampleid']);
     	}
     	
     	if($row['boxid']!=null)
     	{
     		$this->box = new box();
     		$this->box->setParamsFromDB($row['boxid']);
     	}
     	
     	if($row['loanid']!=null)
     	{
     		$this->loan = new loan();
     		$this->loan->setParamsFromDB($row['loanid']);
     	}
     	
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
        $sql = "SELECT * FROM vwtblcurationevent WHERE curationeventid='".pg_escape_string($this->getID())."'";
		$firebug->log($sql, "curation sql");
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
    	
    	$this->setCurationStatus(NULL, $paramsClass->getCurationStatus());
		// securityuser
		// createdtimestamp
    	$this->setNotes($paramsClass->getNotes());    	

	

	file_put_contents("/tmp/debug.txt", "Params class:\n".print_r($paramsClass, true), FILE_APPEND | LOCK_EX);
    	
    	if(isset($paramsClass->sample))
    	{
    		$this->sample = new sample();
    		$this->sample->setParamsFromDB($paramsClass->sample->getID());
    	}
    	
    	if(isset($paramsClass->box))
    	{
    		$this->box = new box();
    		$this->box->setParamsFromDB($paramsClass->box->getID());
    	}
    	
    	$firebug->log($paramsClass, "params class");
	 
		return true;  
    }

    /**
     * Validate parameters from a parameters class
     *
     * @param radiusParameters $paramsObj
     * @param String $crudMode
     * @return boolean
     */
    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode    	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID()===NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required when reading a curation record.", E_USER_ERROR);
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
				if($paramsObj->getCurationStatus()===NULL)
				{
					trigger_error("902"."Missing parameter - 'status' field is required.", E_USER_ERROR);
						
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
                $xml.="<curationEvent>\n";
				$xml.="<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>\n";
				$xml.="<status>".$this->getCurationStatus()."</status>\n";
				$xml.=$this->curator->asXML();
				$xml.="<curationtimestamp>".$this->getCreatedTimestamp()."</curationtimestamp>\n";
                $xml.="<notes>".dbhelper::escapeXMLChars($this->getNotes())."</notes>\n";
                
		if(isset($this->sample) && $this->sample!=null)
		{
			$xml.=$this->sample->asXML();
		}
		else if(isset($this->box) && $this->box!=null)
		{
			$xml.=$this->box->asXML();
		}
		
		if($this->loan!=null)
		{
			$xml.=$this->loan->asXML();
		}
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</curationEvent>\n";
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
		global $myMetaHeader;
			
			
        // Check for required parameters
        if($crudMode!="create" && $crudMode!="update")
        {
	    $this->setErrorMessage("667", "Invalid mode specified in writeToDB().  Only create and update are supported");
	    return FALSE;
        }			
			
        $sampleID = NULL;
        $boxID = NULL;
        
        if(isset($this->sample))
        {
        	$sampleID = $this->sample->getID();
        }
        if(isset($this->box))
        {
        	$boxID = $this->box->getID();
        }
        
    	$curationsql = "INSERT INTO tblcurationevent (curationstatusid, curatorid, sampleid, boxid, notes) values (";
    	$curationsql .= $this->getCurationStatus(true).", '".$myMetaHeader->securityUserID."', ".dbHelper::tellervo_pg_escape_string($sampleID).", ".dbHelper::tellervo_pg_escape_string($boxID).", ".dbHelper::tellervo_pg_escape_string($this->getNotes()).") RETURNING curationeventid";
    	
	file_put_contents("/tmp/debug.txt", $curationsql."\n", FILE_APPEND | LOCK_EX);

    	$firebug->log($curationsql, "Curation SQL");
    	$query = pg_query($dbconn, $curationsql);
    	
    	$row = pg_fetch_row($query); 
    	$new_id = $row['0'];

    	$sql2 = "SELECT * from tblcurationevent where curationeventid=$new_id";
    	
    	// Retrieve automated field values when a new record has been inserted
    	if ($sql2)
    	{
    		// Run SQL
    		$result = pg_query($dbconn, $sql2);
    		while ($row = pg_fetch_array($result))
    		{
    			$this->setCreatedTimestamp($row['createdtimestamp']);
    			$this->setID($row['curationeventid']);
    			$this->curation = new curationEvent();
     			$this->curator->setParamsFromDB($row['curatorid']);
    		}
    	}
    	
        return TRUE;
    }

    function deleteFromDB()
    {

    }

    function mergeRecords($mergeWithID)
    {

    }
    
// End of Class
} 
?>
