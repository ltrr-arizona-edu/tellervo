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
require_once('inc/element.php');
require_once('inc/radius.php');
require_once('inc/dbEntity.php');
require_once('inc/box.php');

class sample extends sampleEntity implements IDBAccessor
{
    var $radiusArray = array();
    
    /***************/
    /* CONSTRUCTOR */
    /***************/

    public function __construct()
    {
        $groupXMLTag = "samples";
    	parent::__construct($groupXMLTag);
    }

    public function __destruct()
    {

    }
    
    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
        global $domain;

        $this->setTitle($row['title']);
        $this->setID($row['sampleid'], $domain);
        $this->setCreatedTimestamp($row['createdtimestamp']);
        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
        $this->setComments($row['comments']);
        $this->setType($row['sampletypeid'], $row['sampletype']);        
        $this->setDescription($row['description']);
        $this->setFilesFromStrArray($row['file']);
        $this->setSamplingDate($row['samplingdate']);
        $this->setPosition($row['position']);
        $this->setState($row['state']);
        $this->setKnots($row['knots']);
		$this->setExternalID($row['externalid']);
		$this->setElementID($row['elementid']);
		$this->setCode($row['code']);
		$this->setBoxID($row['boxid']);
		$this->setCurationStatus($row['curationstatus']);
		$this->setSummaryObjectCode($row['objectcode']);
		$this->setSummaryElementCode($row['elementcode']);
		
        return true;
    }
    
    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        global $domain;    
	global $firebug;
	$firebug->log($theID, "Setting sample params for ID");

        $this->setID($theID);
        $sql = "SELECT * FROM vwtblsample WHERE sampleid='".$theID."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified sample id");
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
        require_once('element.php');

	if($this->elementID == NULL)
	{   
		// No records match the id specified
		$this->setErrorMessage("903", "There are no elements associated with sample id=".$this->getID());
		return FALSE;
	}
	// Empty array before populating it
	$this->parentEntityArray = array();

    	// see if we've cached it already
    	if(($myElement = dbEntity::getCachedEntity("element", $this->elementID)) != NULL)
    	{
    	   array_push($this->parentEntityArray, $myElement);
    	   return;
    	}

	$myElement = new element();
	$success = $myElement->setParamsFromDB($this->elementID);
	if($success===FALSE)
	{
		trigger_error($myElement->getLastErrorCode().$myElement->getLastErrorMessage());
	}
	
	// Add to the array of parents
	array_push($this->parentEntityArray,$myElement);
    }    
    
    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database
        // samplesampleNotes

        global $dbconn;

        $sql  = "SELECT radiusid FROM tblradius WHERE sampleid='".pg_escape_string($this->getID())."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
			
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->radiusArray, $row['radiusid']);
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
     * Set the parameters of this class based upon the Parameters Class that has been passed
     *
     * @param sampleParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {		
	global $firebug;

	$firebug->log($paramsClass, "Params class in setParamsFromParamsClass");

        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        $this->setTitle($paramsClass->getTitle());                                             	
    	$this->setID($paramsClass->getID());                                          
        $this->setComments($paramsClass->getComments());   
        $this->setType($paramsClass->getType(true), $paramsClass->getType());         
        $this->setDescription($paramsClass->getDescription());
        $this->setFiles($paramsClass->getFiles());     
        $this->setSamplingDate($paramsClass->getSamplingDate());            
        $this->setPosition($paramsClass->getPosition());
        $this->setState($paramsClass->getState());
        $this->setExternalID($paramsClass->getExternalID());

        $this->setKnots($paramsClass->getKnots());        
    	$this->setCode($paramsClass->getCode());        
    	$this->setBoxID($paramsClass->getBoxID());              
     
	if ($paramsClass->parentID!=NULL)
        {
        	$parentObj = new element();
        	$parentObj->setParamsFromDB($paramsClass->parentID);
        	array_push($this->parentEntityArray, $parentObj);
        }																				 
		return true;               
   
    }
    
    /**
     * Set the details of this samples parent.  If the sample is in the database already it will look up based on it's ID, otherwise you must pass the
     * ID of the parent manually
     *
     * @param Integer $parentID
     * @return Boolean
     */
    function setParentEntity($parentID=NULL)
    {
    	global $dbconn; 	
    	
    	// Find out the parent ID if it's not specified
    	if($parentID==NULL)
    	{
    		$sql = "SELECT elementid FROM tblsample WHERE sampleid ='".pg_escape_string($this->getID()."'");
	    	$dbconnstatus = pg_connection_status($dbconn);
	        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	        {
	            pg_send_query($dbconn, $sql);
	            $result = pg_get_result($dbconn);
	            if(pg_num_rows($result)==0)
	            {
	                // No records match the id specified
	                echo $sql;
	                $this->setErrorMessage("903", "No records match the specified id");
	                return FALSE;
	            }
	            else
	            {   

	            	$parentID = $row['elementid'];
			
	            }
        	}
    	}
    	
    	// Actually do the setting
        $this->parentEntity = new element;
  		$this->parentEntity->setParamsFromDB($parentID);  
    }    

    
    /*************/
    /* FUNCTIONS */
    /*************/    
    
    /**
     * Validate the parameters passed based on the CRUD mode
     *
     * @param sampleParameters $paramsObj
     * @param String $crudMode one of create, read, update or delete.
     * @return unknown
     */
    function validateRequestParams($paramsObj, $crudMode)
    {   	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID()===NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a sample.");
                    return false;
                }
                if(!(uuid::isUUID($paramsObj->getID())) && !($paramsObj->getID()===NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a UUID.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->getID() === NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                if(($paramsObj->getCode() ===NULL) 
                    && ($paramsObj->getSamplingDate()===NULL) 
                    && ($paramsObj->getType()===NULL) 
                    && ($paramsObj->getFiles()===NULL)
                    && ($paramsObj->getPosition()===NULL)
                    && ($paramsObj->getState()===NULL)
                    && ($paramsObj->getKnots()===NULL)
                    && ($paramsObj->getDescription()===NULL)
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->getID() === NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->getID() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'sampleid' field is required when creating a radius.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->getCode() === NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'code' field is required when creating a sample.");
                        return false;
                    }
                    if($paramsObj->getParentID() === NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - parentID field is required when creating a sample.");
                        return false;
                    }
                }
                return true;

            case "merge":
                if($paramsObj->getID() === NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required when merging.", E_USER_ERROR);
                    return false;
                }
                if($paramsObj->mergeWithID === NULL)
                {
                	trigger_error("902"."Missing parameter - 'mergeWithID' field is required when merging.", E_USER_ERROR);
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

	/**
	 * Get the XML representation of this class
	 * 
	 * @param String $format one of standard, comprehensive, summary, or minimal. Defaults to 'standard'
	 * @param String $parts one of all, beginning or end. Defaults to 'all'
	 * @return String
	 */
    function asXML($format='standard', $parts='all')
    {  	
   	
        switch($format)
        {
        case "comprehensive":
            require_once('element.php');
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

    		// We need to locate the leaf tridas:element (one with no child tridas:objects)
    		// because we need to insert our element xml here
	        $xpath = new DOMXPath($xml);
	       	$xpath->registerNamespace('cor', $tellervoNS);
	       	$xpath->registerNamespace('tridas', $tridasNS);		    		
    		$nodelist = $xpath->query("//tridas:element[* and not(descendant::tridas:element)]");
    		
    		// Create a temporary DOM document to store our element XML
    		$tempdom = new DomDocument();
			$tempdom->loadXML("<root xmlns=\"$tellervoNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->asXML()."</root>");
   		
			// Import and append the sample XML node into the main XML DomDocument
			$node = $tempdom->getElementsByTagName("sample")->item(0);
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

	/**
	 * Internal function for getting the XML representation of this class.
	 * You should almost certainly be used asXML() instead.
	 *
	 * @param String $format one of standard, comprehensive, summary, or minimal. Defaults to 'standard'
	 * @param String $parts one of all, beginning or end. Defaults to 'all'
	 * @return String
	 */
    private function _asXML($format, $parts)
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            // Only return XML when there are no errors.
    
            if( ($parts=="all") || ($parts=="beginning"))
            {
                $xml.= "<tridas:sample>\n";
                $xml.=  $this->getIdentifierXML();
                $xml.= "<tridas:comments>".dbhelper::escapeXMLChars($this->getComments())."</tridas:comments>\n";
              	$xml.= "<tridas:type normal=\"".dbhelper::escapeXMLChars($this->getType())."\" normalId=\"".$this->getType(TRUE)."\" normalStd=\"Tellervo\" />\n";                     
             
                if($this->getDescription()!==NULL)			 $xml.= "<tridas:description>".dbhelper::escapeXMLChars($this->getDescription())."</tridas:description>\n";
            	if($this->getFiles()!==NULL)					 $xml.= $this->getFileXML();
            	if($this->getSamplingDate()!==NULL)           $xml.= "<tridas:samplingDate>".dbhelper::escapeXMLChars($this->getSamplingDate())."</tridas:samplingDate>\n";
                if($this->getPosition()!==NULL)				 $xml.= "<tridas:position>".dbhelper::escapeXMLChars($this->getPosition())."</tridas:position>\n";
                if($this->getState()!==NULL)					 $xml.= "<tridas:state>".dbhelper::escapeXMLChars($this->getState())."</tridas:state>\n";
                if($this->getKnots()!==NULL)					 $xml.= "<tridas:knots>".dbhelper::formatBool($this->getKnots(), "english")."</tridas:knots>\n";
      
            }

            if($this->getBoxID()!=NULL)							$xml.="<tridas:genericField name=\"tellervo.boxID\" type=\"xs:string\">".$this->getBoxID()."</tridas:genericField>\n";
            
            if ($this->getBoxID()!=NULL )//&& $format=="summary")
            {
            	$thisbox = new Box();
            	$thisbox->setParamsFromDB($this->getBoxID());
            	$xml.="<tridas:genericField name=\"tellervo.boxCode\" type=\"xs:string\">".$thisbox->getTitle()."</tridas:genericField>\n";
            	$xml.="<tridas:genericField name=\"tellervo.boxCurationLocation\" type=\"xs:string\">".$thisbox->getCurationLocation()."</tridas:genericField>\n";
            	$xml.="<tridas:genericField name=\"tellervo.boxTrackingLocation\" type=\"xs:string\">".$thisbox->getTrackingLocation()."</tridas:genericField>\n";
            }
            
            $xml.="<tridas:genericField name=\"tellervo.curationStatus\" type=\"xs:string\">".$this->getCurationStatus()."</tridas:genericField>\n";
            
            if ($format=="summary")
            {
            	$xml.="<tridas:genericField name=\"tellervo.objectLabCode\" type=\"xs:string\">".$this->getSummaryObjectCode()."</tridas:genericField>\n";           
            	$xml.="<tridas:genericField name=\"tellervo.elementLabCode\" type=\"xs:string\">".$this->getSummaryElementCode()."</tridas:genericField>\n";           
            }
            

		$xml.="<tridas:genericField name=\"tellervo.externalID\" type=\"xs:string\">".dbhelper::escapeXMLChars($this->getExternalID())."</tridas:genericField>\n";
            
            // Include permissions details if requested            
            $xml .= $this->getPermissionsXML();            
            
            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</tridas:sample>\n";
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

    
    /**
     * Write this class representation to the database  
     *
     * @return Boolean
     */
    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        global $domain;
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
                    
                    // Generate a new UUID pkey
                	$this->setID(uuid::getUUID(), $domain);       
                	
                    $sql = "INSERT INTO tblsample ( ";
                        $sql.="code, ";
                    	$sql.="sampleid, ";
                        $sql.="comments, ";
                        $sql.="typeid, ";
                       	$sql.="description, ";
                       	$sql.="file, ";
                        $sql.="samplingdate, ";
                       	$sql.="position, ";
                       	$sql.="state, ";
                       	$sql.="knots, ";
                       	$sql.="boxid, ";
                       	$sql.="externalid, ";
                        if(isset($this->parentEntityArray[0]))	$sql.="elementid, ";                        
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") VALUES (";
                        $sql.=dbHelper::tellervo_pg_escape_string($this->getTitle()).", ";
	               	$sql.=dbHelper::tellervo_pg_escape_string($this->getID()).", "; 
                       	$sql.=dbHelper::tellervo_pg_escape_string($this->getComments()).", ";
                        $sql.=dbHelper::tellervo_pg_escape_string($this->getType(true)).", ";
                       	$sql.=dbHelper::tellervo_pg_escape_string($this->getDescription()).", ";                        
                       	$sql.=dbHelper::phpArrayToPGStrArray($this->getFiles()).", ";
	                $sql.=dbHelper::tellervo_pg_escape_string($this->getSamplingDate()).", ";
                        $sql.=dbHelper::tellervo_pg_escape_string($this->getPosition()).", ";
                        $sql.=dbHelper::tellervo_pg_escape_string($this->getState()).", ";
                        $sql.=dbHelper::formatBool($this->getKnots(),"pg").", ";
                        $sql.=dbHelper::tellervo_pg_escape_string($this->getBoxID()).", ";
                        $sql.=dbHelper::tellervo_pg_escape_string($this->getExternalID()).", ";
                        if(isset($this->parentEntityArray[0])) $sql.=dbHelper::tellervo_pg_escape_string($this->parentEntityArray[0]->getID()).", ";                        
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "SELECT * FROM tblsample WHERE sampleid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql.="update tblsample set ";
                        $sql.="code="          .dbHelper::tellervo_pg_escape_string($this->getTitle()).", ";
                        $sql.="comments="      .dbHelper::tellervo_pg_escape_string($this->getComments()).", ";
                        $sql.="typeid="     	.dbHelper::tellervo_pg_escape_string($this->getType(true)).", ";
                        $sql.="description="	.dbHelper::tellervo_pg_escape_string($this->getDescription()).", ";                             
                       	$sql.="file="	 	.dbHelper::phpArrayToPGStrArray($this->getFiles()).", ";                                 
                        $sql.="samplingdate=" 	.dbHelper::tellervo_pg_escape_string($this->getSamplingDate()).", ";
                       	$sql.="position="	.dbHelper::tellervo_pg_escape_string($this->getPosition()).", ";
                        $sql.="state="		.dbHelper::tellervo_pg_escape_string($this->getState()).", ";
                        $sql.="knots="		.dbHelper::formatBool($this->getKnots(),"pg").", ";
                        $sql.="boxid="		.dbHelper::tellervo_pg_escape_string($this->getBoxID()).", ";
                        $sql.="externalid="	.dbHelper::tellervo_pg_escape_string($this->getExternalID()).", ";
                        if(isset($this->parentEntityArray[0])) 	$sql.="elementid='"	.pg_escape_string($this->parentEntityArray[0]->getID()) 	.", ";
                        
                    $sql = substr($sql, 0, -2);
                    $sql.= " WHERE sampleid=".dbHelper::tellervo_pg_escape_string($this->getID());
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
/*                        case 23514:
                                // Foreign key violation
                                $this->setErrorMessage("909", "Check constraint violation.  Sapwood count must be a positive integer.");
                                break;*/
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

    
    /**
     * Delete this class representation from the database
     *
     * @return Boolean
     */
    function deleteFromDB()
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

                $sql = "DELETE FROM tblsample WHERE sampleid='".pg_escape_string($this->getID())."'";

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated radii before deleting this sample.");
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

    function mergeRecords($mergeWithID)
    {
    	global $firebug;
		global $dbconn;
		
		$goodID = $mergeWithID;
		$badID  = $this->getID();
	
		//Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
        	$sql = "select * from cpgdb.mergesamples('$goodID', '$badID')";
        	$firebug->log($sql, "SQL");
	       	$dbconnstatus = pg_connection_status($dbconn);
	        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
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
                else
                {
                	$firebug->log("Merge successful");
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
        
        $this->setParamsFromDB($goodID);
        return TRUE;
	}
    
    
// End of Class
} 
?>
