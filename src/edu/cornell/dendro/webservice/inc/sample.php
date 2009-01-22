<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('inc/element.php');
require_once('inc/radius.php');
require_once('inc/sampleType.php');
require_once('inc/dbEntity.php');

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
    
    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        global $domain;    

        $this->setID($theID);
        $sql = "select * from tblsample where sampleid='$theID'";
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
                $this->setName($row['name']);
                $this->setID($row['sampleid'], $domain);
                $this->setSamplingDate($row['samplingdate']);
		$this->setCreatedTimestamp($row['createdtimestamp']);
                $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
                $this->setPosition($row['position']);
                $this->setState($row['state']);
                $this->setKnots($row['knots']);
                $this->setDescription($row['description']);
                
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

    function setParentsFromDB()
    {
        require_once('element.php');
        global $dbconn;
        global $corinaNS;
        global $tridasNS;
        global $gmlNS;
                
        // First find the immediate object entity parent
           $sql = "SELECT elementid from tblsample where sampleid=".$this->getID();
           $dbconnstatus = pg_connection_status($dbconn);
           if ($dbconnstatus ===PGSQL_CONNECTION_OK)
           {
               pg_send_query($dbconn, $sql);
               $result = pg_get_result($dbconn); 

               if(pg_num_rows($result)==0)
               {
                   // No records match the id specified
                   $this->setErrorMessage("903", "There are no elements associated with sample id=".$this->getID());
                   return FALSE;
               }
               else
               {
				   // Empty array before populating it
               	   $this->parentEntityArray = array();
               	   
               	   // Loop through all the parents
                   while($row = pg_fetch_array($result))
                   {
                   		$myElement = new element();
            			$success = $myElement->setParamsFromDB($row['elementid']);
	                   	if($success===FALSE)
	                   	{
	                   	    trigger_error($myElement->getLastErrorCode().$myElement->getLastErrorMessage());
	                   	}  

	                   	// Add to the array of parents
	                   	array_push($this->parentEntityArray,$myElement);
                   }                   
               }
           }	
    }    
    
    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database
        // samplesampleNotes

        global $dbconn;

        $sql  = "select radiusid from tblradius where sampleid='".$this->getID()."'";
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
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if($paramsClass->getID()!=NULL)         	$this->setID($paramsClass->getID());                                          
        if($paramsClass->getName()!=NULL)       	$this->setName($paramsClass->getName());                      
        if($paramsClass->getSamplingDate())     	$this->setSamplingDate($paramsClass->getSamplingDate());            
        if($paramsClass->getType()!=NULL)       	$this->setType($paramsClass->getType());         
        if($paramsClass->getFile()!=NULL)			$this->setFile($paramsClass->getFile());     
        if($paramsClass->getPosition()!=NULL)		$this->setPosition($paramsClass->getPosition());
        if($paramsClass->getState()!=NULL)			$this->setState($paramsClass->getState());
        if($paramsClass->getKnots()!=NULL)			$this->setKnots($paramsClass->getKnots());
        if($paramsClass->getDescription()!=NULL) 	$this->setDescription($paramsClass->getDescription());

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
    		$sql = "select elementid from tblsample where sampleid =".$this->getID();
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
                if($paramsObj->getID()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a sample.");
                    return false;
                }
                if(!($paramsObj->getID()>0) && !($paramsObj->getID()==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->getID() == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                if(($paramsObj->getName() ==NULL) 
                    && ($paramsObj->getSamplingDate()==NULL) 
                    && ($paramsObj->getType()==NULL) 
                    && ($paramsObj->getFile()==NULL)
                    && ($paramsObj->getPosition()==NULL)
                    && ($paramsObj->getState()==NULL)
                    && ($paramsObj->getKnots()==NULL)
                    && ($paramsObj->getDescription()==NULL)
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->getID() == NULL) 
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
                    if($paramsObj->getName() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a sample.");
                        return false;
                    }
                    if($paramsObj->getParentID() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - parentID field is required when creating a sample.");
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
	        global $corinaNS;
	        global $tridasNS;
	        global $gmlNS;
	        
	        // We need to return the comprehensive XML for this element i.e. including all it's ancestral 
	        // object entities.
	        
	        // Make sure the parent entities are set
	        $this->setParentsFromDB();	        
	        
            // Grab the XML representation of the immediate parent using the 'comprehensive'
            // attribute so that we get all the object ancestors formatted correctly                   
            $xml = new DomDocument();   
    		$xml->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->parentEntityArray[0]->asXML('comprehensive')."</root>");                   

    		// We need to locate the leaf tridas:element (one with no child tridas:objects)
    		// because we need to insert our element xml here
	        $xpath = new DOMXPath($xml);
	       	$xpath->registerNamespace('cor', $corinaNS);
	       	$xpath->registerNamespace('tridas', $tridasNS);		    		
    		$nodelist = $xpath->query("//tridas:element[* and not(descendant::tridas:element)]");
    		
    		// Create a temporary DOM document to store our element XML
    		$tempdom = new DomDocument();
			$tempdom->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->asXML()."</root>");
   		
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
                $xml.= "<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>";
                      
                // Include permissions details if requested            
                $xml .= $this->getPermissionsXML();
                
                if($this->getName()!=NULL)                        $xml.= "<tridas:genericField name=\"name\">".escapeXMLChars($this->getName())."</tridas:genericField>\n";
                
                if($format!="minimal")
                {
                    if($this->getSamplingDate()!=NULL)           $xml.= "<tridas:samplingDate\">".$this->getSamplingDate()."</samplingDate>\n";
                    if($this->getType()!=NULL)                   $xml.= "<tridas:type>".$this->getType()."</tridas:type>\n";
                    if($this->getCreatedTimeStamp()!=NULL)       $xml.= "<tridas:genericField name=\"createdTimeStamp\">".$this->getCreatedTimestamp()."</tridas:genericField>\n";
                    if($this->getLastModifiedTimestamp()!=NULL)  $xml.= "<tridas:genericField name=\"lastModifiedTimeStamp\">".$this->getLastModifiedTimestamp()."</tridas:genericField>\n";
                    if($this->getPosition()!=NULL)				 $xml.= "<tridas:position>".$this->getPosition()."</tridas:position>";
                    if($this->getState()!=NULL)					 $xml.= "<tridas:state>".$this->getState()."</tridas:state>";
                    if($this->getKnots()!=NULL)					 $xml.= "<tridas:knots>".$this->getKnots('english')."</tridas:knots>";
                    if($this->getDescription()!=NULL)			 $xml.= "<tridas:description>".$this->getDescription()."</tridas:description";
                }
            }

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
                    $sql = "insert into tblsample ( ";
                        if($this->getName()!=NULL)                   	$sql.="name, ";
                        if(isset($this->parentEntityArray[0]))		 	$sql.="elementid, ";
                        if($this->getSamplingDate()!=NULL)           	$sql.="samplingdate, ";
                        if($this->getType()!=NULL)                   	$sql.="type, ";
                        if($this->getFile()!=NULL)					 	$sql.="file, ";
                        if($this->getPosition()!=NULL)					$sql.="position, ";
                        if($this->getState()!=NULL)						$sql.="state, ";
                        if($this->getKnots()!=NULL)						$sql.="knots, ";
                        if($this->getDescription()!=NULL)				$sql.="description, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if($this->getName()!=NULL)                   	$sql.="'".$this->getName()             	."', ";
                        if(isset($this->parentEntityArray[0]))      	$sql.="'".$this->parentEntityArray[0]->getID() 	."', ";
                        if($this->getSamplingDate()!=NULL)           	$sql.="'".$this->getSamplingDate()     	."', ";
                        if($this->getType()!=NULL)                   	$sql.="'".$this->getType()             	."', ";
                        if($this->getFile()!=NULL)					 	$sql.="'".$this->getFile()  			."', ";
                        if($this->getPosition()!=NULL)					$sql.="'".$this->getPosition()			."', ";
                        if($this->getState()!=NULL)						$sql.="'".$this->getState()				."', ";
                        if($this->getKnots()!=NULL)						$sql.="'".$this->getKnots("pg")			."', ";
                        if($this->getDescription()!=NULL)				$sql.="'".$this->getDescription()		."', ";                        
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblsample where sampleid=currval('tblsample_sampleid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql.="update tblsample set ";
                        if($this->getName()!=NULL)             	$sql.="name='"           	.$this->getName()          						."', ";
                        if(isset($this->parentEntityArray[0])) 	$sql.="elementid='"      	.$this->parentEntityArray[0]->getID() 	."', ";
                        if($this->getSamplingDate()!=NULL)     	$sql.="samplingdate='"   	.$this->getSamplingDate()  						."', ";
                        if($this->getType()!=NULL)          	$sql.="type='"     			.$this->getType()          						."', ";
                        if($this->getFile()!=NULL)				$sql.="file='"	 			.$this->getFile()  								."', ";
                        if($this->getPosition()!=NULL)			$sql.="position='"			.$this->getPosition()							."', ";
                        if($this->getState()!=NULL)				$sql.="state='"				.$this->getState()								."', ";
                        if($this->getKnots()!=NULL)				$sql.="knots='"				.$this->getKnots("pg")			."', ";
                        if($this->getDescription()!=NULL)		$sql.="description='"		.$this->getDescription()		."', ";                             
                    $sql = substr($sql, 0, -2);
                    $sql.= " where sampleid='".$this->getID()."'";
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
                        $this->setID($row['sampleid']);   
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

                $sql = "delete from tblsample where sampleid='".$this->getID()."'";

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

// End of Class
} 
?>
