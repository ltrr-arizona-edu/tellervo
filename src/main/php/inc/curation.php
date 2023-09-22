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
class curation extends curationEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "curation";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
    	$this->setID($row['curationid']);
        $this->setCreatedTimestamp($row['createdtimestamp']);
     	$this->setNotes($row['notes']);
     	$this->curator->setParamsFromDB($row['curatorid']);
     	$this->setCurationStatus($row['curationstatusid'], $row['curationstatus']);
			
     	$this->sample = new sample();
     	$this->sample->setParamsFromDB($row['sampleid']);
     	
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
        $sql = "SELECT * FROM vwtblcuration WHERE curationid='".pg_escape_string($dbconn, $this->getID())."'";
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
    	
    	$this->sample = new sample();
    	$this->sample->setParamsFromDB($paramsClass->entityIdArray[0]);
    	
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
                $xml.="<curation>\n";
				$xml.="<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>\n";
				$xml.="<status>".$this->getCurationStatus()."</status>\n";
				$xml.=$this->curator->asXML();
				$xml.="<curationtimestamp>".$this->getCreatedTimestamp()."</curationtimestamp>\n";
                $xml.="<notes>".dbhelper::escapeXMLChars($this->getNotes())."</notes>\n";
                $xml.=$this->sample->asXML();
                
                if($this->loan!=null)
                {
                	$xml.=$this->loan->asXML();
                }
               
                
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</curation>\n";
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
			
    	$curationsql = "INSERT INTO tblcuration (curationstatusid, curatorid, sampleid, notes) values (";
    	$curationsql .= $this->getCurationStatus(true).", '".$myMetaHeader->securityUserID."', '".$this->sample->getID()."', ".dbHelper::tellervo_pg_escape_string($dbconn, $this->getNotes()).") RETURNING curationid";
    	
    	$firebug->log($curationsql, "Curation SQL");
    	$query = pg_query($dbconn, $curationsql);
    	
    	$row = pg_fetch_row($query); 
    	$new_id = $row['0'];

    	$sql2 = "SELECT * from tblcuration where curationid=$new_id";
    	
    	// Retrieve automated field values when a new record has been inserted
    	if ($sql2)
    	{
    		// Run SQL
    		$result = pg_query($dbconn, $sql2);
    		while ($row = pg_fetch_array($result))
    		{
    			$this->setCreatedTimestamp($row['createdtimestamp']);
    			$this->setID($row['curationid']);
    			$this->curation = new curation();
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
