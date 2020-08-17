<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */
require_once ('dbhelper.php');
class project extends projectEntity implements IDBAccessor {
	
	/**
	 * ************
	 */
	/* CONSTRUCTOR */
	/**
	 * ************
	 */
	public function __construct() {
		$groupXMLTag = "project";
		parent::__construct ( $groupXMLTag );
	}
	
	public function __destruct() {
	}
	
	/**
	 * ********
	 */
	/* SETTERS */
	/**
	 * ********
	 */
	function setParamsFromDBRow($row, $format = "standard") {
		global $debugFlag;
		global $myMetaHeader;
		if ($debugFlag === TRUE)
			$myMetaHeader->setTiming ( "Setting project parameters for projectid " . $row ['projectid'] );
			
			// Set parameters from db
		$this->setTitle ( $row ['title'] );
		$this->setID ( $row ['projectid'], $row ['domain'] );
		$this->setCreatedTimestamp ( $row ['createdtimestamp'] );
		$this->setLastModifiedTimestamp ( $row ['lastmodifiedtimestamp'] );
		$this->setComments ( $row ['comments'] );
		$this->setTypesFromStrArray ( $row ['types'] );
		$this->setDescription ( $row ['description'] );
		$this->setFilesFromStrArray ( $row ['file'] );
		$this->setInvestigator($row['investigator']);
		$this->setPeriod($row['period']);
		$this->setCommissioner($row['commissioner']);
		$this->setUserDefinedFieldAndValueArrayByEntityID ( $this->getID () );
		$this->setCategory($row['projectcategoryid'], $row['projectcategory']);
		$this->setRequestDate($row['requestdate']);
		
		return true;
	}
	
	/**
	 * Set this object's parameters from the database
	 *
	 * @param Integer $theID        	
	 * @param String $idType        	
	 */
	function setParamsFromDB($theID, $idType = 'db') {
		global $dbconn;
		
		switch (strtolower ( $idType )) {
			case 'db' :
				$sql = "SELECT * FROM vwtblproject WHERE projectid='" . $theID . "'";
				break;
			
			case 'lab' :
				$sql = "SELECT * FROM vwtblproject WHERE code='" . $theID . "'";
				break;
			
			default :
				trigger_error ( '667' . 'Unknown database id type.', E_USER_ERROR );
				die ();
		}
		
		$dbconnstatus = pg_connection_status ( $dbconn );
		if ($dbconnstatus === PGSQL_CONNECTION_OK) {
			pg_send_query ( $dbconn, $sql );
			$result = pg_get_result ( $dbconn );
			if (pg_num_rows ( $result ) == 0) {
				// No records match the id specified
				trigger_error ( "903" . "No records match the specified id. $sql", E_USER_ERROR );
				return FALSE;
			} else {
				// Set parameters from db
				$row = pg_fetch_array ( $result );
				$this->setParamsFromDBRow ( $row );
			}
		} else {
			// Connection bad
			trigger_error ( "001" . "Error connecting to database", E_USER_ERROR );
			return FALSE;
		}
		
		$this->cacheSelf ();
		return TRUE;
	}
	
	/**
	 * Add the id's of the current project's direct children from the database
	 *
	 * @return Boolean
	 */
	function setChildParamsFromDB($cascade = false) {
		global $dbconn;
		
		$sql = "select objectid from vwtblobject where projectid='" . $this->getID () . "'";
		$dbconnstatus = pg_connection_status ( $dbconn );
		if ($dbconnstatus === PGSQL_CONNECTION_OK) {
			$result = pg_query ( $dbconn, $sql );
			
			if (pg_num_rows ( $result ) > 0) {
				// Object has 'object' descendants
				while ( $row = pg_fetch_array ( $result ) ) {
					$entity = new tobject ();
					$entity->setParamsFromDB ( $row ['objectid'] );
					$entity->setChildParamsFromDB ( true );
					// print_r($entity);
					array_push ( $this->childrenEntityArray, $entity );
				}
			}
		} else {
			// Connection bad
			$this->setErrorMessage ( "001", "Error connecting to database" );
			return FALSE;
		}
		
		return TRUE;
	}
	
	/**
	 * Set the current project's parameters from a paramsClass project
	 *
	 * @param projectParameters $paramsClass        	
	 * @return Boolean
	 */
	function setParamsFromParamsClass($paramsClass) {
		global $firebug;
		$firebug->log ( $paramsClass, "params class" );
		
		$this->setTitle ( $paramsClass->getTitle () );
		$this->setComments ( $paramsClass->getComments () );
		$this->setTypes ( $paramsClass->getTypes () );
		$this->setDescription ( $paramsClass->getDescription () );
		$this->setFiles ( $paramsClass->getFiles () );
		$this->setInvestigator($paramsClass->getInvestigator());
		$this->setCommissioner($paramsClass->getCommissioner());
		$this->setPeriod($paramsClass->getPeriod());
		$this->setCategory($paramsClass->getCategory(true), $paramsClass->getCategory());
		$this->setUserDefinedFieldAndValueArray ( $paramsClass->getUserDefinedFieldAndValueArray () );
		$this->setRequestDate($paramsClass->getRequestDate());
		
		return true;
	}
	
	/**
	 * ********
	 */
	/* GETTERS */
	/**
	 * ********
	 */
	
	/**
	 * Get an XML representation of this project
	 *
	 * @param String $format        	
	 * @param String $parts        	
	 * @return Boolean
	 */
	function asXML($format = 'standard', $parts = 'all') {
		global $myRequest;
		
		// Override request for comprehensive if doing a delete
		if ($myRequest->getCrudMode () == 'delete')
			$format = 'standard';
		
		switch ($format) {
			case "comprehensive" :
				require_once ('object.php');
				global $dbconn;
				global $tellervoNS;
				global $tridasNS;
				global $xlinkNS;
				global $gmlNS;
				
				// Create a DOM Document to hold the XML as it's produced
				$xml = new DomDocument ();
				$xml->loadXML ( "<object xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\"></object>" );
				// $xml->formatOutput = true;
				
				$myParentObjectArray = Array ();
				array_push ( $myParentObjectArray, $this );
				
				$sql = "SELECT * from cpgdb.findobjectancestors('" . $this->getID () . "', false)";
				$dbconnstatus = pg_connection_status ( $dbconn );
				if ($dbconnstatus === PGSQL_CONNECTION_OK) {
					$result = pg_query ( $dbconn, $sql );
					
					while ( $row = pg_fetch_array ( $result ) ) {
						$myParentObject = new tobject ();
						$myParentObject->setParamsFromDB ( $row ['objectid'] );
						array_push ( $myParentObjectArray, $myParentObject );
					}
					
					$myParentObjectArray = array_reverse ( $myParentObjectArray );
					
					$i = 0;
					foreach ( $myParentObjectArray as $obj ) {
						$dom = new DomDocument ();
						$dom->loadXML ( "<root xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">" . $obj->asXML () . "</root>" );
						$dom->formatOutput = true;
						
						$objnode = $dom->getElementsByTagName ( "object" )->item ( 0 );
						$objnode = $xml->importNode ( $objnode, true );
						$xml->getElementsByTagName ( "object" )->item ( $i )->appendChild ( $objnode );
						$i ++;
					}
					
					return $xml->saveXML ( $xml->getElementsByTagName ( "object" )->item ( 1 ) );
				} else {
					// Connection bad
					$this->setErrorMessage ( "001", "Error connecting to database" );
					return FALSE;
				}
			
			case "standard" :
				return $this->_asXML ( $format, $parts );
			
			case "summary" :
				return $this->_asXML ( $format, $parts );
			
			case "minimal" :
				return $this->_asXML ( $format, $parts );
			
			default :
				$this->setErrorMessage ( "901", "Unknown format. Must be one of 'standard', 'summary' or 'comprehensive'" );
				return false;
		}
	}
	
	
	private function _asXML($format = 'standard', $parts = 'all') {
		global $firebug;
		global $labname;
		global $labacronym;
		$xml = NULL;
		
		// Return a string containing the current object in XML format
		if ($this->getLastErrorCode () == NULL) {
			if (($parts == "all") || ($parts == "beginning")) {
				$xml .= "<tridas:project>\n";
				$xml .= $this->getIdentifierXML ();
				if ($this->getComments () != NULL)
					$xml .= "<tridas:comments>" . dbhelper::escapeXMLChars ( $this->getComments () ) . "</tridas:comments>\n";
				
					
				if(($this->getTypes()!=NULL) && (count($this->getTypes())>0))
		       	{
		        	foreach($this->getTypes() as $projectType)	
		        	{
		     
						$xml .= "<tridas:type normal=\"" . $projectType->getValue() . "\" normalId=\"" . $projectType->getID() . "\" normalStd=\"Tellervo\" />\n";
		        	}
		       	}
		       	else
		       	{
		       		$xml .= "<tridas:type normal=\"\" normalId=\"\" normalStd=\"\" />\n";
		       		 
		       	}

				if ($this->getDescription () !== NULL)
					$xml .= "<tridas:description>" . dbHelper::escapeXMLChars ( $this->getDescription () ) . "</tridas:description>";
				$xml .= $this->getFileXML ();
				
				// Laboratory
				$xml.= "<tridas:laboratory>
						          <tridas:name acronym=\"{$labacronym}\">{$labname}</tridas:name>
            					<tridas:address>
            					</tridas:address>
        					</tridas:laboratory>";

				// Category
				$xml .= "<tridas:category normal=\"" . dbhelper::escapeXMLChars ( $this->getCategory () ) . "\" normalId=\"" . $this->getCategory ( TRUE ) . "\" normalStd=\"Tellervo\" />\n";
								
	
				$xml .= "<tridas:investigator>".dbHelper::escapeXMLChars($this->getInvestigator())."</tridas:investigator>";

				$xml .= "<tridas:period>".dbHelper::escapeXMLChars($this->getPeriod())."</tridas:period>";

				// Request date
				if($this->getRequestDate()!=null )
				{
					$xml .= "<tridas:requestDate>".dbHelper::escapeXMLChars($this->getRequestDate())."</tridas:requestDate>";
				}
		
				$xml .= "<tridas:commissioner>".dbHelper::escapeXMLChars($this->getCommissioner())."</tridas:commissioner>";
				
				// Reference
				// Research

				
				// Generic fields
				if ($this->getUserDefinedFieldAndValueArray () != null && count ( $this->getUserDefinedFieldAndValueArray () > 0 )) {
					foreach ( $this->getUserDefinedFieldAndValueArray () as $field ) {
						$xml .= $field->getAsTridasXML ();
					}
				}
			}
						
			if (($parts == "all") || ($parts == "end")) {
				// End XML tag
				$xml .= "</tridas:project>\n";
			}
			
			return $xml;
		} else {
			return FALSE;
		}
	}
	
	/**
	 * **********
	 */
	/* FUNCTIONS */
	/**
	 * **********
	 */
	
	/**
	 * Write the current object to the database
	 *
	 * @return Boolean
	 */
	function writeToDB($crudMode = "create") {
		// Write the current object to the database
		global $dbconn;
		global $domain;
		global $firebug;
		$sql = "";
		$sql2 = "";
		
		// Check for required parameters
		if ($crudMode != "create" && $crudMode != "update") {
			$this->setErrorMessage ( "667", "Invalid mode specified in writeToDB().  Only create and update are supported" );
			return FALSE;
		}
		
		if ($this->getTitle () == NULL) {
			$this->setErrorMessage ( "902", "Missing parameter - 'title' field is required." );
			return FALSE;
		}
		
		if ($crudMode == "update" && $this->getID () == NULL) {
			$this->setErrorMessage ( "902", "Missing parameter - 'id' field is required when editing" );
			return FALSE;
		}
		
		// Only attempt to run SQL if there are no errors so far
		if ($this->getLastErrorCode () == NULL) {
			$dbconnstatus = pg_connection_status ( $dbconn );
			if ($dbconnstatus === PGSQL_CONNECTION_OK) {
				// If ID has not been set then we assume that we are writing a new record to the DB. Otherwise updating.
				if ($crudMode == "create") {
					// New Record
					if ($this->getID () == NULL)
						$this->setID ( uuid::getUUID (), $domain );
						
						// New Record
					
					$sql = "insert into tblproject ( ";
					
					$sql .= "title, ";
					$sql .= "projectid, ";
					$sql .= "comments, ";
					$sql .= "projecttypes, ";
					$sql .= "description, ";
					$sql .= "file, ";
					$sql .= "requestdate, ";
					$sql .= "investigator, ";
					$sql .= "period, ";
					$sql .= "commissioner, ";
					$sql .= "projectcategoryid, ";
					$sql = substr ( $sql, 0, - 2 );
					$sql .= ") values (";
					
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getTitle () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getID () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getComments () ) . ", ";
					$sql .= dbHelper::phpArrayToPGStrArray ( $this->getTypes(true) ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getDescription () ) . ", ";
					$sql .= dbHelper::phpArrayToPGStrArray ( $this->getFiles () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getRequestDate() ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getInvestigator () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getPeriod () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getCommissioner () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getCategory( true ) ) . ", ";
					$sql = substr ( $sql, 0, - 2 );
					$sql .= ")";
					$sql2 = "select * from tblproject where projectid='" . $this->getID () . "'";
				} else {
					// Updating DB
					$sql = "update tblproject set ";
					$sql .= "title=" . dbHelper::tellervo_pg_escape_string ( $this->getTitle () ) . ", ";
					$sql .= "comments=" . dbHelper::tellervo_pg_escape_string ( $this->getComments () ) . ", ";
					//$sql .= "projecttypeid=" . dbHelper::tellervo_pg_escape_string ( $this->getType ( true ) ) . ", ";
					$sql .= "description=" . dbHelper::tellervo_pg_escape_string ( $this->getDescription () ) . ", ";
					$sql .= "file=" . dbHelper::phpArrayToPGStrArray ( $this->getFiles () ) . ", ";
					$sql .= "requestdate=" . dbHelper::tellervo_pg_escape_string ( $this->getRequestDate () ) . ", ";
					$sql .= "investigator=" . dbHelper::tellervo_pg_escape_string ( $this->getInvestigator () ) . ", ";
					$sql .= "period=" . dbHelper::tellervo_pg_escape_string ( $this->getPeriod () ) . ", ";
					$sql .= "commissioner=" . dbHelper::tellervo_pg_escape_string ( $this->getCommissioner () ) . ", ";
					$sql .= "projectcategoryid=" . dbHelper::tellervo_pg_escape_string ( $this->getCategory ( true ) ) . ", ";
						
					$sql = substr ( $sql, 0, - 2 );
					$sql .= " where projectid='" . $this->getID () . "'";
				}
				// echo $sql;
				$firebug->log ( $sql, "write sql" );
				
				// Run SQL command
				if ($sql) {
					// Run SQL
					pg_send_query ( $dbconn, $sql );
					$result = pg_get_result ( $dbconn );
					if (pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE )) {
						$PHPErrorCode = pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE );
						switch ($PHPErrorCode) {
							
							default :
								// Any other error
								/*
								 * $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE), "PGSQL_DIAG_SQLSTATE"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_HINT), "PGSQL_DIAG_MESSAGE_HINT"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_PRIMARY), "PGSQL_DIAG_MESSAGE_PRIMARY"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_DETAIL), "PGSQL_DIAG_MESSAGE_DETAIL"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SEVERITY), "PGSQL_DIAG_SEVERITY"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_STATEMENT_POSITION), "PGSQL_DIAG_STATEMENT_POSITION"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_INTERNAL_POSITION), "PGSQL_DIAG_INTERNAL_POSITION"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_INTERNAL_QUERY), "PGSQL_DIAG_INTERNAL_QUERY"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_CONTEXT), "PGSQL_DIAG_CONTEXT"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SOURCE_FILE), "PGSQL_DIAG_SOURCE_FILE"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SOURCE_LINE), "PGSQL_DIAG_SOURCE_LINE"); $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SOURCE_FUNCTION), "PGSQL_DIAG_SOURCE_FUNCTION");
								 */
								$this->setErrorMessage ( "002", pg_result_error ( $result ) . "--- SQL was $sql" );
						}
						return FALSE;
					}
				}
				// Retrieve automated field values when a new record has been inserted
				if ($sql2) {
					// Run SQL
					$result = pg_query ( $dbconn, $sql2 );
					while ( $row = pg_fetch_array ( $result ) ) {
						$this->setCreatedTimestamp ( $row ['createdtimestamp'] );
						$this->setLastModifiedTimestamp ( $row ['lastmodifiedtimestamp'] );
					}
				}
				
				// Write user defined fields to database
				if (count ( $this->userDefinedFieldAndValueArray ) > 0) {
						
					foreach ( $this->userDefinedFieldAndValueArray as $field ) {
						try {
							$result = $field->writeToDB ( $this->getID () );
							if ($result === false) {
								$this->setErrorMessage ( "002", pg_result_error ( $result ) . " Error writing user defined fields to disk" );
							}
						} catch ( Exception $e ) {
							$this->setErrorMessage ( "002", "Error writing user defined fields to disk. " . $e->getMessage () );
						}
					}
				}
				
			} else {
				// Connection bad
				$this->setErrorMessage ( "001", "Error connecting to database" );
				return FALSE;
			}
		}
		
		// Return true as write to DB went ok.
		return TRUE;
	}
	
	/**
	 * Delete this object from the database
	 *
	 * @return Boolean
	 */
	function deleteFromDB() {
		// Delete the record in the database matching the current object's ID
		global $dbconn;
		
		// Check for required parameters
		if ($this->getID () == NULL) {
			$this->setErrorMessage ( "902", "Missing parameter - 'id' field is required." );
			return FALSE;
		}
		
		// Only attempt to run SQL if there are no errors so far
		if ($this->getLastErrorCode () == NULL) {
			$dbconnstatus = pg_connection_status ( $dbconn );
			if ($dbconnstatus === PGSQL_CONNECTION_OK) {
				
				$sql = "DELETE FROM tblproject WHERE projectid='" . $this->getID () . "'";
				// Run SQL
				pg_send_query ( $dbconn, $sql );
				$result = pg_get_result ( $dbconn );
				if (pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE )) {
					$PHPErrorCode = pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE );
					switch ($PHPErrorCode) {
						case 23503 :
							// Foreign key violation
							$this->setErrorMessage ( "907", "Foreign key violation.  You must delete all entities associated with an project before deleting the project itself." );
							break;
						default :
							// Any other error
							$this->setErrorMessage ( "002", pg_result_error ( $result ) . "--- SQL was $sql" );
					}
					return FALSE;
				}
			} else {
				// Connection bad
				$this->setErrorMessage ( "001", "Error connecting to database" );
				return FALSE;
			}
		}
		
		// Return true as write to DB went ok.
		return TRUE;
	}
	
	/**
	 * Check that the parameters within a defined parameters class are valid
	 *
	 * @param projectParameters $paramsClass        	
	 * @param String $crudMode        	
	 * @return Boolean
	 */
	function validateRequestParams($paramsClass, $crudMode) {
		switch ($crudMode) {
			case "read" :
				if ($paramsClass->getID () == NULL) {
					$this->setErrorMessage ( "902", "Missing parameter - 'id' field is required when reading a element." );
					return false;
				}
				return true;
			
			case "update" :
				if ($paramsClass->getID () == NULL) {
					$this->setErrorMessage ( "902", "Missing parameter - 'id' field is required when updating a element." );
					return false;
				}
				if (($paramsClass->getCode () == NULL) && ($paramsClass->getCreator () == NULL) && ($paramsClass->getDescription () === NULL) && ($paramsClass->getType () == NULL) && ($paramsClass->getFiles () == NULL) && ($paramsClass->getOwner () == NULL) && ($paramsClass->getTemporalCoverage () == NULL) && ($paramsClass->getTemporalCoverageFoundation () == NULL) && ($paramsClass->getTitle () == NULL)) {
					$this->setErrorMessage ( "902", "Missing parameters - you haven't specified any parameters to update." );
					return false;
				}
				return true;
			
			case "delete" :
				if ($paramsClass->getID () == NULL) {
					$this->setErrorMessage ( "902", "Missing parameter - 'id' field is required when deleting a element." );
					return false;
				}
				return true;
			
			case "create" :
				if ($paramsClass->hasChild === TRUE) {
					if ($paramsClass->getID () == NULL) {
						$this->setErrorMessage ( "902", "Missing parameter - 'projectid' field is required when creating an element." );
						return false;
					}
				} else {
					if ($paramsClass->getTitle () == NULL) {
						$this->setErrorMessage ( "902", "Missing parameter - 'title' field is required when creating an object." );
						return false;
					}
				}
				return true;
						
			default :
				$this->setErrorMessage ( "667", "Program bug - invalid crudMode specified when validating request" );
				return false;
		}
	}
	
	function mergeRecords($mergeWithID)
	{
		$this->setErrorMessage ( "902", "Merging projects is not supported" );
		return false;
	}
	
}
?>
