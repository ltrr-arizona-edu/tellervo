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
class box extends boxEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "boxes";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
        $this->setTitle($row['title']);
        $this->setComments($row['comments']);
		$this->setCurationLocation($row['curationlocation']);
		$this->setTrackingLocation($row['trackinglocation']);
		$this->setSampleCount($row['samplecount']);
		$this->setCreatedTimestamp($row['createdtimestamp']);
		$this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
		$this->setID($row['boxid']);

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
        global $dbconn;
        global $firebug;
        
        if($theID==NULL) {
		$firebug->log("Box is null");
		return FALSE;
	}
        
        $this->setID($theID);
        $sql = "SELECT * from vwtblbox WHERE boxid='".pg_escape_string($this->getID())."'";
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
                trigger_error("903"."No records match the specified box id", E_USER_ERROR);
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
    
    function setParamsFromDBFromName($name)
    {
        global $dbconn;
        global $firebug;
        
        if($name==NULL) return FALSE;
        
        $sql = "SELECT * from vwtblbox WHERE title='".pg_escape_string($name)."'";
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
                trigger_error("903"."No records match the specified id", E_USER_ERROR);
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
        // Add the id's of the current objects direct children from the database
        // RadiusRadiusNotes

        global $dbconn;
        global $firebug;
        
        
        $sql  = "select sampleid from tblsample where boxid='".pg_escape_string($this->getID())."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {

            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
            	
                // Get all samples for this box
                array_push($this->sampleArray, $row['sampleid']);
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
     * @param boxParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {    	
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        $this->setTitle($paramsClass->getTitle());
        $this->setID($paramsClass->getID());
        $this->setComments($paramsClass->getComments());
        $this->setCurationLocation($paramsClass->getCurationLocation());
        $this->setTrackingLocation($paramsClass->getTrackingLocation());		
		
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
            require_once('sample.php');
            global $dbconn;
	        global $tellervoNS;
	        global $tridasNS;
	        global $gmlNS;
	        
	        // We need to return the comprehensive XML for this element i.e. including all it's ancestral 
	        // object entities.
	        
	        // Make sure the parent entities are set
	        if($this->setParentsFromDB()===FALSE)
	        {
				return FALSE;     
	        }

            // Grab the XML representation of the immediate parent using the 'comprehensive'
            // attribute so that we get all the object ancestors formatted correctly                   
            $xml = new DomDocument();   
    		$xml->loadXML("<root xmlns=\"$tellervoNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->parentEntityArray[0]->asXML('comprehensive')."</root>");                   

    		// We need to locate the leaf tridas:sample (one with no child sample)
    		// because we need to insert our sample xml here
	        $xpath = new DOMXPath($xml);
	       	$xpath->registerNamespace('cor', $tellervoNS);
	       	$xpath->registerNamespace('tridas', $tridasNS);		    		
    		$nodelist = $xpath->query("//tridas:sample[* and not(descendant::tridas:sample)]");

    		// Create a temporary DOM document to store our element XML
    		$tempdom = new DomDocument();
			$tempdom->loadXML("<root xmlns=\"$tellervoNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->asXML()."</root>");
   		
			// Import and append the radius XML node into the main XML DomDocument
			$node = $tempdom->getElementsByTagName("radius")->item(0);
			$node = $xml->importNode($node, true);
			$nodelist->item(0)->appendChild($node);
            // Return an XML string representation of the entire shebang
            return $xml->saveXML($xml->getElementsByTagName("object")->item(0));
            
        case "standard":
            return $this->_asXML($format, $parts);
        case "summary":
            return $this->_asXML($format, $parts);
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
        global $firebug;
		$xml = NULL;
		
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            // Only return XML when there are no errors.
            if( ($parts=="all") || ($parts=="beginning"))
            {
                $xml.= "<box>\n";
                $xml.= $this->getIdentifierXML();          
                if($this->getComments()!=NULL) 					$xml.= "<tridas:comments>".$this->getComments()."</tridas:comments>\n";
                
                if($this->getTrackingLocation()!=NULL)			$xml.= "<trackingLocation>".$this->getTrackingLocation()."</trackingLocation>\n";
                if($this->getCurationLocation()!=NULL)			$xml.= "<curationLocation>".$this->getCurationLocation()."</curationLocation>\n";
				if($this->getSampleCount()!=NULL)				$xml.= "<sampleCount>".$this->getSampleCount()."</sampleCount>\n";
                
                 // Include samples if present
                if (($this->sampleArray) && ($format!="minimal"))
                {
                    foreach($this->sampleArray as $value)
                    {
                        $mysample = new sample();
                        $success = $mysample->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$mysample->asXML("summary", "all");
                        }
                        else
                        {
                            $myMetaHeader->setErrorMessage($mysample->getLastErrorCode, $mysample->getLastErrorMessage);
                        }
                    }
                }          
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</box>\n";
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
        // Write the current object to the database

        global $dbconn;
        global $domain;
        global $firebug;
        $sql = NULL;
        $sql2 = NULL;
        
        // Check for required parameters
        if($crudMode!="create" && $crudMode!="update")
        {
	    $this->setErrorMessage("667", "Invalid mode specified in writeToDB().  Only create and update are supported");
	    return FALSE;
        }
        
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
                    
                    // Generate a new UUID pkey
                	$this->setID(uuid::getUUID(), $domain);                 	
                	
                    $sql = "insert into tblbox ( ";
                        if($this->getTitle()!=NULL)                   				$sql.="title, ";
                    	if($this->getID()!=NULL) 									$sql.="boxid, "; 
                    	if($this->getComments()!=NULL)								$sql.="comments, ";
						if($this->getCurationLocation()!=NULL)						$sql.="curationlocation, ";
						if($this->getTrackingLocation()!=NULL)						$sql.="trackinglocation, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if($this->getTitle()!=NULL)                   				$sql.="'".pg_escape_string($this->getTitle())."', ";
                        if($this->getID()!=NULL)                   					$sql.="'".pg_escape_string($this->getID())."', ";
                        if($this->getComments()!=NULL)                   			$sql.="'".pg_escape_string($this->getComments())."', ";
						if($this->getCurationLocation()!=NULL)						$sql.="'".pg_escape_string($this->getCurationLocation())."', ";
						if($this->getTrackingLocation()!=NULL)						$sql.="'".pg_escape_string($this->getTrackingLocation())."', ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblbox where boxid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql.="UPDATE tblbox SET ";
                        $sql.="title='".pg_escape_string($this->getCode())."', ";
                        $sql.="comments='".pg_escape_string($this->getComments())."', ";
						$sql.="curationlocation='".pg_escape_string($this->getCurationLocation())."', ";
						$sql.="trackinglocation='".pg_escape_string($this->getTrackingLocation())."', ";
						
                    $sql = substr($sql, 0, -2);
                    $sql.= " WHERE boxid='".pg_escape_string($this->getID())."'";
                    $firebug->log($sql, "Box update SQL");
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
                        $this->setCreatedTimestamp($row['createdtimestamp']);   
                        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);   
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

                $sql = "DELETE FROM tblbox WHERE boxid='".pg_escape_string($this->getID())."'";

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete or reassign all associated samples before deleting this box.");
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
