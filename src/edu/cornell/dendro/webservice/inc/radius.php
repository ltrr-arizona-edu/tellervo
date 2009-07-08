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

require_once('dbhelper.php');
require_once('inc/dbEntity.php');

/**
 * Class for interacting with a radiusEntity.  This contains the logic of how to read and write data from the database as well as error checking etc.
 *
 */
class radius extends radiusEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "radii";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
        $this->setTitle($row['radiuscode']);
    	$this->setID($row['radiusid']);
        $this->setCreatedTimestamp($row['radiuscreated']);
        $this->setLastModifiedTimestamp($row['radiuslastmodified']);
        $this->setComments($row['comments']);
        $this->setPith($row['pithid'], $row['pith']);
        $this->setHeartwood($row['heartwoodid'], $row['heartwood']);
        $this->setMissingHeartwoodRingsToPith($row['missingheartwoodringstopith']);
        $this->setMissingHeartwoodRingsToPithFoundation($row['missingheartwoodringstopithfoundation']);
        $this->setSapwood($row['sapwoodid'], $row['sapwood']);
        $this->setNumberOfSapwoodRings($row['numberofsapwoodrings']);
        $this->setLastRingUnderBark($row['lastringunderbark']);
        $this->setMissingSapwoodRingsToBark($row['missingsapwoodringstobark']);
        $this->setMissingSapwoodRingsToBarkFoundation($row['missingsapwoodringstobarkfoundation']);          
        $this->setBarkPresent(dbHelper::formatBool($row['barkpresent']));     
        $this->setAzimuth($row['azimuth']);
        $this->setSampleID($row['sampleid']);
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
        
        $this->setID($theID);
        $sql = "SELECT * FROM vwtblradius WHERE radiusid='".pg_escape_string($this->getID())."'";
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
        require_once('sample.php');
		if($this->sampleID == NULL)
		{   
			// No records match the id specified
			$this->setErrorMessage("903", "There are no samples associated with radius id=".$this->getID());
			return FALSE;
		}
	
		// Empty array before populating it
		$this->parentEntityArray = array();


		// see if we've cached it already
        if(($mySample = dbEntity::getCachedEntity("sample", $this->sampleID)) != NULL)
        {
        	array_push($this->parentEntityArray, $mySample);
            return;
        }
        

		$mySample = new sample();
		$success = $mySample->setParamsFromDB($this->sampleID);
		if($success===FALSE)
		{
			trigger_error($mySample->getLastErrorCode().$mySample->getLastErrorMessage());
		}
	
		// Add to the array of parents
		array_push($this->parentEntityArray,$mySample);

    }      
    
    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database
        // RadiusRadiusNotes

        global $dbconn;

        $sql  = "select measurementid from tblmeasurement where radiusid='".pg_escape_string($this->getID())."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {

            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->measurementArray, $row['measurementid']);
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
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if ($paramsClass->getTitle()!=NULL)       							$this->setTitle($paramsClass->getTitle());
        if ($paramsClass->getID()!=NULL)		 							$this->setID($paramsClass->getID());
		if ($paramsClass->getComments()!=NULL)								$this->setComments($paramsClass->getComments());
        if ($paramsClass->getPithPresent()!=NULL)							$this->setPithPresent($paramsClass->getPithPresent());
        if ($paramsClass->getHeartwood()!=NULL)								$this->setHeartwood($paramsClass->getHeartwood(true), $paramsClass->getHeartwood(false));
        if ($paramsClass->getMissingHeartwoodRingsToPith()!=NULL)			$this->setMissingHeartwoodRingsToPith($paramsClass->getMissingHeartwoodRingsToPith());
        if ($paramsClass->getMissingHeartwoodRingsToPithFoundation()!=NULL)	$this->setMissingHeartwoodRingsToPithFoundation($paramsClass->getMissingHeartwoodRingsToPithFoundation());
        if ($paramsClass->getSapwood()!=NULL)								$this->setSapwood($paramsClass->getSapwood(true), $paramsClass->getSapwood(false));
        if ($paramsClass->getBarkPresent()!=NULL)							$this->setBarkPresent($paramsClass->getBarkPresent());
        if ($paramsClass->getNumberOfSapwoodRings()!=NULL)					$this->setNumberOfSapwoodRings($paramsClass->getNumberOfSapwoodRings());
        if ($paramsClass->getLastRingUnderBark()!=NULL)						$this->setLastRingUnderBark($paramsClass->getLastRingUnderBark());
        if ($paramsClass->getMissingSapwoodRingsToBark()!=NULL)				$this->setMissingSapwoodRingsToBark($paramsClass->getMissingSapwoodRingsToBark());
        if ($paramsClass->getMissingSapwoodRingsToBarkFoundation()!=NULL)	$this->setMissingSapwoodRingsToBarkFoundation($paramsClass->getMissingSapwoodRingsToBarkFoundation());
        if ($paramsClass->getAzimuth()!=NULL)								$this->setAzimuth($paramsClass->getAzimuth());
        if ($paramsClass->parentID!=NULL)
        {
        	$parentObj = new element();
        	$parentObj->setParamsFromDB($paramsClass->parentID);
        	array_push($this->parentEntityArray, $parentObj);
        }																				 
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
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a radius.");
                    return false;
                }
                if(!($paramsObj->getID()>0) && !($paramsObj->getID()==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->getID()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                if(($paramsObj->getSampleID()==NULL) 
                    && ($paramsObj->getCode()==NULL)
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
                        $this->setErrorMessage("902","Missing parameter - 'radiusid' field is required when creating a measurement.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->getCode() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'code' field is required when creating a radius.");
                        return false;
                    }
                    if($paramsObj->parentID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'sampleid' field is required when creating a radius.");
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
            require_once('sample.php');
            global $dbconn;
	        global $corinaNS;
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
    		$xml->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->parentEntityArray[0]->asXML('comprehensive')."</root>");                   

    		// We need to locate the leaf tridas:sample (one with no child sample)
    		// because we need to insert our sample xml here
	        $xpath = new DOMXPath($xml);
	       	$xpath->registerNamespace('cor', $corinaNS);
	       	$xpath->registerNamespace('tridas', $tridasNS);		    		
    		$nodelist = $xpath->query("//tridas:sample[* and not(descendant::tridas:sample)]");

    		// Create a temporary DOM document to store our element XML
    		$tempdom = new DomDocument();
			$tempdom->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->asXML()."</root>");
   		
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
        $xml ="";
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            // Only return XML when there are no errors.
            if( ($parts=="all") || ($parts=="beginning"))
            {
                $xml.= "<tridas:radius>\n";
                $xml.= $this->getIdentifierXML();          
                if($this->getComments()!=NULL) 										$xml.= "<tridas:comments>".$this->getComments()."</tridas:comments>\n";
                if($format!="minimal")
                {
                    $xml.= "<tridas:woodCompleteness>\n";
                    if($this->getPith()!=NULL)											$xml.= "<tridas:pith presence=\"".$this->getPith()."\"></tridas:pith>\n";
                    if( ($this->getHeartwood()!=NULL)  )    
                    {
                        																$xml.= "<tridas:heartwood presence=\"".$this->getHeartwood()."\">";
						if($this->getMissingHeartwoodRingsToPith()!=NULL)				$xml.= "<tridas:missingHeartwoodRingsToPith>".$this->getMissingHeartwoodRingsToPith()."</tridas:missingHeartwoodRingsToPith>\n";
						if($this->getMissingHeartwoodRingsToPithFoundation()!=NULL)		$xml.= "<tridas:missingHeartwoodRingsToPithFoundation>".$this->getMissingHeartwoodRingsToPithFoundation()."</tridas:missingHeartwoodRingsToPithFoundation>\n";																				
																						$xml.= "</tridas:heartwood>\n";
					}

					if($this->getSapwood()!=NULL)
					{
																						$xml.= "<tridas:sapwood presence=\"".$this->getSapwood()."\">";
	                    if($this->getNumberOfSapwoodRings()!=NULL)						$xml.= "<tridas:nrOfSapwoodRings>".$this->getNumberOfSapwoodRings()."</tridas:nrOfSapwoodRings>\n";
	                    if($this->getLastRingUnderBark()!=NULL)							$xml.= "<tridas:lastRingUnderBark>".$this->getLastRingUnderBark()."</tridas:lastRingUnderBark>\n";
	                    if($this->getMissingSapwoodRingsToBark()!=NULL)					$xml.= "<tridas:missingSapwoodRingsToBark>".$this->getMissingSapwoodRingsToBark()."</tridas:missingSapwoodRingsToBark>\n";
	                    if($this->getMissingSapwoodRingsToBarkFoundation()!=NULL)		$xml.= "<tridas:missingSapwoodRingsToBarkFoundation>".$this->getMissingSapwoodRingsToBarkFoundation()."</tridas:missingSapwoodRingsToBarkFoundation>\n";	
	                    																$xml.= "</tridas:sapwood>\n";
					}
            		$xml.= "<tridas:bark presence=\"".dbHelper::formatBool($this->getBarkPresent(), "presentabsent")."\"/>\n";
                    $xml.= "</tridas:woodCompleteness>\n";
					if($this->getAzimuth()!=NULL)									$xml.= "<tridas:azimuth>".$this->getAzimuth()."</tridas:azimuth>\n";
                }
                // Include permissions details if requested            
                $xml .= $this->getPermissionsXML();                
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</tridas:radius>\n";
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
                    
                    // Generate a new UUID pkey
                	$this->setID(uuid::getUUID(), $domain);                 	
                	
                    $sql = "insert into tblradius ( ";
                        if($this->getTitle()!=NULL)                   				$sql.="code, ";
                    	if($this->getID()!=NULL) 									$sql.="radiusid, "; 
                    	if($this->getComments()!=NULL)								$sql.="comments, ";
                        if($this->getPith()!=NULL)									$sql.="pithid, ";
                        if($this->getHeartwood()!=NULL)								$sql.="heartwoodid, ";
                        if($this->getMissingHeartwoodRingsToPith()!=NULL)			$sql.="missingheartwoodringstopith, ";
                        if($this->getMissingHeartwoodRingsToPithFoundation()!=NULL)	$sql.="missingheartwoodringstopithfoundation, ";
                        if($this->getSapwood()!=NULL)								$sql.="sapwoodid, ";     
                        if($this->getNumberOfSapwoodRings()!=NULL)					$sql.="numberofsapwoodrings, ";                                             
                        if($this->getLastRingUnderBark()!=NULL)						$sql.="lastringunderbark, ";
                        if($this->getMissingSapwoodRingsToBark()!=NULL)				$sql.="missingsapwoodringstobark, ";
                        if($this->getMissingSapwoodRingsToBarkFoundation()!=NULL)	$sql.="missingsapwoodringstobarkfoundation, ";
                        if($this->getBarkPresent()!=NULL)							$sql.="barkpresent, ";
                        if($this->getAzimuth()!=NULL)								$sql.="azimuth, ";                      
                        if(isset($this->parentEntityArray[0]))		 				$sql.="sampleid, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if($this->getTitle()!=NULL)                   				$sql.="'".pg_escape_string($this->getTitle())."', ";
                        if($this->getID()!=NULL)                   					$sql.="'".pg_escape_string($this->getID())."', ";
                        if($this->getComments()!=NULL)                   			$sql.="'".pg_escape_string($this->getComments())."', ";
                        if($this->getPith()!=NULL)									$sql.="'".pg_escape_string($this->getPith(true))."', ";
                        if($this->getHeartwood()!=NULL)								$sql.="'".pg_escape_string($this->getHeartwood(true))."', ";
                        if($this->getMissingHeartwoodRingsToPith()!=NULL)			$sql.="'".pg_escape_string($this->getMissingHeartwoodRingsToPith())."', ";
                        if($this->getMissingHeartwoodRingsToPithFoundation()!=NULL)	$sql.="'".pg_escape_string($this->getMissingHeartwoodRingsToPithFoundation())."', ";
                        if($this->getSapwood()!=NULL)								$sql.="'".pg_escape_string($this->getSapwood(true))."', ";                     
                        if($this->getNumberOfSapwoodRings()!=NULL)					$sql.="'".pg_escape_string($this->getNumberOfSapwoodRings())."', ";
                        if($this->getLastRingUnderBark()!=NULL)						$sql.="'".pg_escape_string($this->getLastRingUnderBark())."', ";
                        if($this->getMissingSapwoodRingsToBark()!=NULL)				$sql.="'".pg_escape_string($this->getMissingSapwoodRingsToBark())."', ";
                        if($this->getMissingSapwoodRingsToBarkFoundation()!=NULL)	$sql.="'".pg_escape_string($this->getMissingSapwoodRingsToBarkFoundation())."', ";
                        if($this->getBarkPresent()!=NULL)							$sql.="'".formatBool($this->getBarkPresent(),"pg"). "', ";
                        if($this->getAzimuth()!=NULL)								$sql.="'".pg_escape_string($this->getAzimuth())."', ";
                        if(isset($this->parentEntityArray[0]))      				$sql.="'".pg_escape_string($this->parentEntityArray[0]->getID())."', ";                        
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblradius where radiusid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql.="UPDATE tblradius SET ";
                        if($this->getTitle()!=NULL)                   				$sql.="code='".pg_escape_string($this->getCode())."', ";
                        if($this->getComments()!=NULL)                   			$sql.="comments='".pg_escape_string($this->getComments())."', ";
                        if($this->getPith()!=NULL)									$sql.="pithid='".pg_escape_string($this->getPith(true))."', ";    
                        if($this->getHeartwood()!=NULL)								$sql.="heartwoodid='".pg_escape_string($this->getHeartwood(true))."', ";
                        if($this->getMissingHeartwoodRingsToPith()!=NULL)			$sql.="missingheartwoodringstopith='".pg_escape_string($this->getMissingHeartwoodRingsToPith())."', ";
                        if($this->getMissingHeartwoodRingsToPithFoundation()!=NULL)	$sql.="missingheartwoodringstopithfoundation='".pg_escape_string($this->getMissingHeartwoodRingsToPithFoundation())."', ";
                        if($this->getSapwood()!=NULL)								$sql.="sapwood='".pg_escape_string($this->getSapwood(true))."', ";        
                        if($this->getNumberOfSapwoodRings()!=NULL)					$sql.="numberofsapwoodrings='".pg_escape_string($this->getNumberOfSapwoodRings())."', ";
                        if($this->getLastRingUnderBark()!=NULL)						$sql.="lastringunderbark='".pg_escape_string($this->getLastRingUnderBark())."', ";
                        if($this->getMissingSapwoodRingsToBark()!=NULL)				$sql.="missingsapwoodringstobark='".pg_escape_string($this->getMissingSapwoodRingsToBark())."', ";
                        if($this->getMissingSapwoodRingsToBarkFoundation()!=NULL)	$sql.="missingsapwoodringstobarkfoundation='".pg_escape_string($this->getMissingSapwoodRingsToBarkFoundation())."', ";
                        if($this->getBarkPresent()!=NULL)							$sql.="barpresent='".formatBool($this->getBarkPresent(),"pg")."', ";
                        if($this->getAzimuth()!=NULL)								$sql.="azimuth='".pg_escape_string($this->getAzimuth())."', ";                        
                        if(isset($this->parentEntityArray[0]))      				$sql.="sampleid='".pg_escape_string($this->parentEntityArray[0]->getID())."', ";             
                    $sql = substr($sql, 0, -2);
                    $sql.= " WHERE radiusid='".pg_escape_string($this->getID())."'";
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

                $sql = "DELETE FROM tblradius WHERE radiusid=".pg_escape_string($this->getID());

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated measurements before deleting this radius.");
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
