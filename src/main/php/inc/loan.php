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
        // RadiusRadiusNotes

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
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->getID() ===NULL) 
                    {
                        trigger_error("902"."Missing parameter - 'loanid' field is required when creating a loan.", E_USER_ERROR);
                        return false;
                    }
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
                
                $xml.= "<notes>".dbhelper::escapeXMLChars($this->getNotes())."</notes>\n";
                
                foreach ($this->entityIdArray as $entityid)
                {
                	//$xml.="<entity type=\"sample\" id=\"".$entityid."\"/>\n";
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

    function writeToDB()
    {
  
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
