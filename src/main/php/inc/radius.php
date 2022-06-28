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
require_once ('dbhelper.php');
require_once ('inc/dbEntity.php');

/**
 * Class for interacting with a radiusEntity.
 * This contains the logic of how to read and write data from the database as well as error checking etc.
 */
class radius extends radiusEntity implements IDBAccessor {
	
	/**
	 * ************
	 */
	/* CONSTRUCTOR */
	/**
	 * ************
	 */
	function __construct() {
		$groupXMLTag = "radii";
		parent::__construct ( $groupXMLTag );
	}
	
	/**
	 * ********
	 */
	/* SETTERS */
	/**
	 * ********
	 */
	function setParamsFromDBRow($row, $format = "standard") {
		$this->setTitle ( $row ['radiuscode'] );
		$this->setID ( $row ['radiusid'] );
		$this->setCreatedTimestamp ( $row ['radiuscreated'] );
		$this->setLastModifiedTimestamp ( $row ['radiuslastmodified'] );
		$this->setComments ( $row ['comments'] );
		$this->setPith ( $row ['pithid'], $row ['pith'] );
		$this->setHeartwood ( $row ['heartwoodid'], $row ['heartwood'] );
		$this->setMissingHeartwoodRingsToPith ( $row ['missingheartwoodringstopith'] );
		$this->setMissingHeartwoodRingsToPithFoundation ( $row ['missingheartwoodringstopithfoundation'] );
		$this->setSapwood ( $row ['sapwoodid'], $row ['sapwood'] );
		$this->setNumberOfSapwoodRings ( $row ['numberofsapwoodrings'] );
		$this->setLastRingUnderBark ( $row ['lastringunderbark'], $row ['lastringunderbarkpresent'] );
		$this->setMissingSapwoodRingsToBark ( $row ['missingsapwoodringstobark'] );
		$this->setMissingSapwoodRingsToBarkFoundation ( $row ['missingsapwoodringstobarkfoundation'] );
		$this->setNrOfUnmeasuredInnerRings ( $row ['nrofunmeasuredinnerrings'] );
		$this->setNrOfUnmeasuredOuterRings ( $row ['nrofunmeasuredouterrings'] );
		$this->setBarkPresent ( dbHelper::formatBool ( $row ['barkpresent'] ) );
		$this->setAzimuth ( $row ['azimuth'] );
		$this->setSampleID ( $row ['sampleid'] );
		$this->setUserDefinedFieldAndValueArrayByEntityID ( $this->getID () );
		
		return true;
	}
	
	/**
	 * Set the current objects parameters from the database
	 *
	 * @param Integer $theID        	
	 * @return Boolean
	 */
	function setParamsFromDB($theID) {
		global $firebug;
		global $dbconn;
		
		$this->setID ( $theID );
		$sql = "SELECT * FROM vwtblradius WHERE radiusid='" . pg_escape_string ( $this->getID () ) . "'";
		$firebug->log ( $sql, "radius sql" );
		$dbconnstatus = pg_connection_status ( $dbconn );
		if ($dbconnstatus === PGSQL_CONNECTION_OK) {
			pg_send_query ( $dbconn, $sql );
			$result = pg_get_result ( $dbconn );
			if (pg_num_rows ( $result ) == 0) {
				// No records match the id specified
				$this->setErrorMessage ( "903", "No records match the specified id" );
				return FALSE;
			} else {
				// Set parameters from db
				$row = pg_fetch_array ( $result );
				$this->setParamsFromDBRow ( $row );
			}
		} else {
			// Connection bad
			$this->setErrorMessage ( "001", "Error connecting to database" );
			return FALSE;
		}
		
		$this->cacheSelf ();
		return TRUE;
	}
	function setParentsFromDB() {
		global $firebug;
		require_once ('sample.php');
		if ($this->getSampleID () == NULL) {
			// No records match the id specified
			$this->setErrorMessage ( "903", "There are no samples associated with radius id=" . $this->getID () );
			return FALSE;
		}
		
		// Empty array before populating it
		$this->parentEntityArray = array ();
		
		// see if we've cached it already
		if (($mySample = dbEntity::getCachedEntity ( "sample", $this->getSampleID () )) != NULL) {
			array_push ( $this->parentEntityArray, $mySample );
			return;
		}
		
		$mySample = new sample ();
		$success = $mySample->setParamsFromDB ( $this->getSampleID () );
		if ($success === FALSE) {
			trigger_error ( $mySample->getLastErrorCode () . $mySample->getLastErrorMessage () );
		}
		
		// Add to the array of parents
		array_push ( $this->parentEntityArray, $mySample );
	}
	function setChildParamsFromDB() {
		// Add the id's of the current objects direct children from the database
		// RadiusRadiusNotes
		global $dbconn;
		
		$sql = "select measurementid from tblmeasurement where radiusid='" . pg_escape_string ( $this->getID () ) . "'";
		$dbconnstatus = pg_connection_status ( $dbconn );
		if ($dbconnstatus === PGSQL_CONNECTION_OK) {
			
			$result = pg_query ( $dbconn, $sql );
			while ( $row = pg_fetch_array ( $result ) ) {
				// Get all tree note id's for this tree and store
				array_push ( $this->measurementArray, $row ['measurementid'] );
			}
		} else {
			// Connection bad
			$this->setErrorMessage ( "001", "Error connecting to database" );
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
	function setParamsFromParamsClass($paramsClass) {
		global $firebug;
		
		$firebug->log ( $paramsClass, "params class" );
		
		// Alters the parameter values based upon values supplied by the user and passed as a parameters class
		$this->setTitle ( $paramsClass->getTitle () );
		$this->setID ( $paramsClass->getID () );
		$this->setComments ( $paramsClass->getComments () );
		$this->setPith ( null, $paramsClass->getPith () );
		$this->setHeartwood ( $paramsClass->getHeartwood ( true ), $paramsClass->getHeartwood ( false ) );
		$this->setMissingHeartwoodRingsToPith ( $paramsClass->getMissingHeartwoodRingsToPith () );
		$this->setMissingHeartwoodRingsToPithFoundation ( $paramsClass->getMissingHeartwoodRingsToPithFoundation () );
		$this->setSapwood ( $paramsClass->getSapwood ( true ), $paramsClass->getSapwood ( false ) );
		$this->setBarkPresent ( dbHelper::formatBool ( $paramsClass->getBarkPresent (), 'php' ) );
		$this->setNumberOfSapwoodRings ( $paramsClass->getNumberOfSapwoodRings () );
		$this->setLastRingUnderBark ( $paramsClass->getLastRingUnderBark (), $paramsClass->getLastRingUnderBarkPresence () );
		$this->setMissingSapwoodRingsToBark ( $paramsClass->getMissingSapwoodRingsToBark () );
		$this->setMissingSapwoodRingsToBarkFoundation ( $paramsClass->getMissingSapwoodRingsToBarkFoundation () );
		$this->setNrOfUnmeasuredInnerRings ( $paramsClass->getNrOfUnmeasuredInnerRings () );
		$this->setNrOfUnmeasuredOuterRings ( $paramsClass->getNrOfUnmeasuredOuterRings () );
		$this->setAzimuth ( $paramsClass->getAzimuth () );
		$this->setUserDefinedFieldAndValueArray ( $paramsClass->getUserDefinedFieldAndValueArray () );
		
		if ($paramsClass->parentID != NULL) {
			$parentObj = new sample ();
			$parentObj->setParamsFromDB ( $paramsClass->parentID );
			array_push ( $this->parentEntityArray, $parentObj );
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
	function validateRequestParams($paramsObj, $crudMode) {
		// Check parameters based on crudMode
		switch ($crudMode) {
			case "read" :
				if ($paramsObj->getID () === NULL) {
					trigger_error ( "902" . "Missing parameter - 'id' field is required when reading a radius.", E_USER_ERROR );
					return false;
				}
				if (! ($paramsObj->getID () > 0) && ! ($paramsObj->getID () === NULL)) {
					$this->setErrorMessage ( "901", "Invalid parameter - 'id' field must be a valid positive integer.", E_USER_ERROR );
					return false;
				}
				return true;
			
			case "update" :
				if ($paramsObj->getID () === NULL) {
					trigger_error ( "902" . "Missing parameter - 'id' field is required.", E_USER_ERROR );
					return false;
				}
				if (($paramsObj->getSampleID () === NULL) && ($paramsObj->getCode () === NULL) && ($paramsObj->hasChild != True)) {
					trigger_error ( "902" . "Missing parameters - you haven't specified any parameters to update.", E_USER_ERROR );
					return false;
				}
				return true;
			
			case "delete" :
				if ($paramsObj->getID () === NULL) {
					trigger_error ( "902" . "Missing parameter - 'id' field is required.", E_USER_ERROR );
					return false;
				}
				return true;
			
			case "create" :
				if ($paramsObj->hasChild === TRUE) {
					if ($paramsObj->getID () === NULL) {
						trigger_error ( "902" . "Missing parameter - 'radiusid' field is required when creating a measurement.", E_USER_ERROR );
						return false;
					}
				} else {
					if ($paramsObj->getCode () === NULL) {
						trigger_error ( "902" . "Missing parameter - 'code' field is required when creating a radius.", E_USER_ERROR );
						return false;
					}
					if ($paramsObj->parentID === NULL) {
						trigger_error ( "902" . "Missing parameter - 'sampleid' field is required when creating a radius.", E_USER_ERROR );
						return false;
					}
					if ($paramsObj->pith->getValue () === NULL) {
						trigger_error ( "902" . "Missing parameter - 'pith' field is required when creating a radius.", E_USER_ERROR );
					}
					if ($paramsObj->heartwood->getValue () === NULL) {
						trigger_error ( "902" . "Missing parameter - 'heartwood' field is required when creating a radius.", E_USER_ERROR );
					}
					if ($paramsObj->sapwood->getValue () === NULL) {
						trigger_error ( "902" . "Missing parameter - 'sapwood' field is required when creating a radius.", E_USER_ERROR );
					}
				}
				return true;
			
			default :
				$this->setErrorMessage ( "667", "Program bug - invalid crudMode specified when validating request" );
				return false;
		}
	}
	
	/**
	 * ********
	 */
	/* ACCESSORS */
	/**
	 * ********
	 */
	function asXML($format = 'standard', $parts = 'all') {
		switch ($format) {
			case "comprehensive" :
				require_once ('sample.php');
				global $dbconn;
				global $tellervoNS;
				global $tridasNS;
				global $gmlNS;
				global $xlinkNS;
				
				// We need to return the comprehensive XML for this element i.e. including all it's ancestral
				// object entities.
				
				// Make sure the parent entities are set
				if ($this->setParentsFromDB () === FALSE) {
					return FALSE;
				}
				
				// Grab the XML representation of the immediate parent using the 'comprehensive'
				// attribute so that we get all the object ancestors formatted correctly
				$xml = new DomDocument ();
				$xml->loadXML ( "<root xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\"  xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">" . $this->parentEntityArray [0]->asXML ( 'comprehensive' ) . "</root>" );
				
				// We need to locate the leaf tridas:sample (one with no child sample)
				// because we need to insert our sample xml here
				$xpath = new DOMXPath ( $xml );
				$xpath->registerNamespace ( 'cor', $tellervoNS );
				$xpath->registerNamespace ( 'tridas', $tridasNS );
				$nodelist = $xpath->query ( "//tridas:sample[* and not(descendant::tridas:sample)]" );
				
				// Create a temporary DOM document to store our element XML
				$tempdom = new DomDocument ();
				$tempdom->loadXML ( "<root xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\"  xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">" . $this->asXML () . "</root>" );
				
				// Import and append the radius XML node into the main XML DomDocument
				$node = $tempdom->getElementsByTagName ( "radius" )->item ( 0 );
				$node = $xml->importNode ( $node, true );
				$nodelist->item ( 0 )->appendChild ( $node );
				// Return an XML string representation of the entire shebang
				return $xml->saveXML ( $xml->getElementsByTagName ( "object" )->item ( 0 ) );
			
			case "standard" :
				return $this->_asXML ( $format, $parts );
			case "summary" :
				return $this->_asXML ( $format, $parts );
			case "minimal" :
				return $this->_asXML ( $format, $parts );
			default :
				$this->setErrorMessage ( "901", "Unknown format. Must be one of 'standard', 'summary', 'minimal' or 'comprehensive'" );
				return false;
		}
	}
	private function _asXML($format, $parts) {
		global $domain;
		$xml = "";
		// Return a string containing the current object in XML format
		if ($this->getLastErrorCode () === NULL) {
			// Only return XML when there are no errors.
			if (($parts == "all") || ($parts == "beginning")) {
				$xml .= "<tridas:radius>\n";
				$xml .= $this->getIdentifierXML ();
				if ($this->getComments () !== NULL)
					$xml .= "<tridas:comments>" . dbhelper::escapeXMLChars ( $this->getComments () ) . "</tridas:comments>\n";
				
				$xml .= "<tridas:woodCompleteness>\n";
				
				if ($this->getNrOfUnmeasuredInnerRings () !== NULL)
					$xml .= "<tridas:nrOfUnmeasuredInnerRings>" . dbhelper::escapeXMLChars ( $this->getNrOfUnmeasuredInnerRings () ) . "</tridas:nrOfUnmeasuredInnerRings>\n";
				if ($this->getNrOfUnmeasuredOuterRings () !== NULL)
					$xml .= "<tridas:nrOfUnmeasuredOuterRings>" . dbhelper::escapeXMLChars ( $this->getNrOfUnmeasuredOuterRings () ) . "</tridas:nrOfUnmeasuredOuterRings>\n";
				
				if ($this->getPith () !== NULL)
					$xml .= "<tridas:pith presence=\"" . dbhelper::escapeXMLChars ( $this->getPith () ) . "\"></tridas:pith>\n";
				if (($this->getHeartwood () !== NULL)) {
					$xml .= "<tridas:heartwood presence=\"" . dbhelper::escapeXMLChars ( $this->getHeartwood () ) . "\">";
					if ($this->getMissingHeartwoodRingsToPith () !== NULL)
						$xml .= "<tridas:missingHeartwoodRingsToPith>" . dbhelper::escapeXMLChars ( $this->getMissingHeartwoodRingsToPith () ) . "</tridas:missingHeartwoodRingsToPith>\n";
					if ($this->getMissingHeartwoodRingsToPithFoundation () !== NULL)
						$xml .= "<tridas:missingHeartwoodRingsToPithFoundation>" . dbhelper::escapeXMLChars ( $this->getMissingHeartwoodRingsToPithFoundation () ) . "</tridas:missingHeartwoodRingsToPithFoundation>\n";
					$xml .= "</tridas:heartwood>\n";
				}
				
				if ($this->getSapwood () !== NULL) {
					$xml .= "<tridas:sapwood presence=\"" . dbhelper::escapeXMLChars ( $this->getSapwood () ) . "\">";
					if ($this->getNumberOfSapwoodRings () !== NULL)
						$xml .= "<tridas:nrOfSapwoodRings>" . dbhelper::escapeXMLChars ( $this->getNumberOfSapwoodRings () ) . "</tridas:nrOfSapwoodRings>\n";
					if (($this->getLastRingUnderBark () !== NULL) || (dbHelper::formatBool ( $this->getLastRingUnderBarkPresence (), 'presentabsent' ) !== NULL))
						$xml .= "<tridas:lastRingUnderBark presence=\"" . dbHelper::formatBool ( $this->getLastRingUnderBarkPresence (), "presentabsent" ) . "\">" . dbhelper::escapeXMLChars ( $this->getLastRingUnderBark () ) . "</tridas:lastRingUnderBark>\n";
					if ($this->getMissingSapwoodRingsToBark () !== NULL)
						$xml .= "<tridas:missingSapwoodRingsToBark>" . dbhelper::escapeXMLChars ( $this->getMissingSapwoodRingsToBark () ) . "</tridas:missingSapwoodRingsToBark>\n";
					if ($this->getMissingSapwoodRingsToBarkFoundation () !== NULL)
						$xml .= "<tridas:missingSapwoodRingsToBarkFoundation>" . dbhelper::escapeXMLChars ( $this->getMissingSapwoodRingsToBarkFoundation () ) . "</tridas:missingSapwoodRingsToBarkFoundation>\n";
					$xml .= "</tridas:sapwood>\n";
				}
				$xml .= "<tridas:bark presence=\"" . dbHelper::formatBool ( $this->getBarkPresent (), "presentabsent" ) . "\"/>\n";
				$xml .= "</tridas:woodCompleteness>\n";
				if ($this->getAzimuth () !== NULL)
					$xml .= "<tridas:azimuth>" . dbhelper::escapeXMLChars ( $this->getAzimuth () ) . "</tridas:azimuth>\n";
				
				if ($this->getUserDefinedFieldAndValueArray () != null && count ( $this->getUserDefinedFieldAndValueArray () > 0 )) {
					foreach ( $this->getUserDefinedFieldAndValueArray () as $field ) {
						$xml .= $field->getAsTridasXML ();
					}
				}
					
					// Include permissions details if requested
				$xml .= $this->getPermissionsXML ();
			}
			
			if (($parts == "all") || ($parts == "end")) {
				$xml .= "</tridas:radius>\n";
			}
			return $xml;
		} else {
			return FALSE;
		}
	}
	
	/**
	 * ********
	 */
	/* FUNCTIONS */
	/**
	 * ********
	 */
	function writeToDB($crudMode = "create") {
		// Write the current object to the database
		global $dbconn;
		global $domain;
		global $firebug;
		$sql = NULL;
		$sql2 = NULL;
		
		// Check for required parameters
		if ($crudMode != "create" && $crudMode != "update") {
			$this->setErrorMessage ( "667", "Invalid mode specified in writeToDB().  Only create and update are supported" );
			return FALSE;
		}
		
		// Only attempt to run SQL if there are no errors so far
		if ($this->getLastErrorCode () === NULL) {
			$dbconnstatus = pg_connection_status ( $dbconn );
			if ($dbconnstatus === PGSQL_CONNECTION_OK) {
				
				// If ID has not been set then we assume that we are writing a new record to the DB. Otherwise updating.
				if ($this->getID () === NULL) {
					// New record
					
					// Generate a new UUID pkey
					$this->setID ( uuid::getUUID (), $domain );
					
					$sql = "insert into tblradius ( ";
					$sql .= "code, ";
					$sql .= "radiusid, ";
					$sql .= "comments, ";
					$sql .= "pithid, ";
					$sql .= "heartwoodid, ";
					$sql .= "missingheartwoodringstopith, ";
					$sql .= "missingheartwoodringstopithfoundation, ";
					$sql .= "sapwoodid, ";
					$sql .= "numberofsapwoodrings, ";
					// $sql.="lastringunderbark, ";
					$sql .= "lastringunderbarkpresent, ";
					$sql .= "missingsapwoodringstobark, ";
					$sql .= "missingsapwoodringstobarkfoundation, ";
					$sql .= "nrofunmeasuredinnerrings, ";
					$sql .= "nrofunmeasuredouterrings, ";
					$sql .= "barkpresent, ";
					$sql .= "azimuth, ";
					if (isset ( $this->parentEntityArray [0] ))
						$sql .= "sampleid, ";
						// Trim off trailing space and comma
					$sql = substr ( $sql, 0, - 2 );
					$sql .= ") values (";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getTitle () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getID () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getComments () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getPith ( true ) ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getHeartwood ( true ) ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getMissingHeartwoodRingsToPith () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getMissingHeartwoodRingsToPithFoundation () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getSapwood ( true ) ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getNumberOfSapwoodRings () ) . ", ";
					// $sql.=pg_escape_string($this->getLastRingUnderBark(true)).", ";
					$sql .= dbHelper::formatBool ( $this->getLastRingUnderBarkPresence (), "pg" ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getMissingSapwoodRingsToBark () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getMissingSapwoodRingsToBarkFoundation () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getNrOfUnmeasuredInnerRings () ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getNrOfUnmeasuredOuterRings () ) . ", ";
					$sql .= dbHelper::formatBool ( $this->getBarkPresent (), "pg" ) . ", ";
					$sql .= dbHelper::tellervo_pg_escape_string ( $this->getAzimuth () ) . ", ";
					if (isset ( $this->parentEntityArray [0] ))
						$sql .= dbHelper::tellervo_pg_escape_string ( $this->parentEntityArray [0]->getID () ) . ", ";
						// Trim off trailing space and comma
					$sql = substr ( $sql, 0, - 2 );
					$sql .= ")";
					$sql2 = "select * from tblradius where radiusid='" . $this->getID () . "'";
				} else {
					// Updating DB
					$sql .= "UPDATE tblradius SET ";
					$sql .= "code=" . dbHelper::tellervo_pg_escape_string ( $this->getCode () ) . ", ";
					$sql .= "comments=" . dbHelper::tellervo_pg_escape_string ( $this->getComments () ) . ", ";
					$sql .= "pithid=" . dbHelper::tellervo_pg_escape_string ( $this->getPith ( true ) ) . ", ";
					$sql .= "heartwoodid=" . dbHelper::tellervo_pg_escape_string ( $this->getHeartwood ( true ) ) . ", ";
					$sql .= "missingheartwoodringstopith=" . dbHelper::tellervo_pg_escape_string ( $this->getMissingHeartwoodRingsToPith () ) . ", ";
					$sql .= "missingheartwoodringstopithfoundation=" . dbHelper::tellervo_pg_escape_string ( $this->getMissingHeartwoodRingsToPithFoundation () ) . ", ";
					$sql .= "sapwoodid=" . dbHelper::tellervo_pg_escape_string ( $this->getSapwood ( true ) ) . ", ";
					$sql .= "numberofsapwoodrings=" . dbHelper::tellervo_pg_escape_string ( $this->getNumberOfSapwoodRings () ) . ", ";
					// $sql.="lastringunderbark=".dbHelper::tellervo_pg_escape_string($this->getLastRingUnderBark()).", ";
					$sql .= "lastringunderbarkpresent=" . dbHelper::formatBool ( $this->getLastRingUnderBarkPresence (), "pg" ) . ", ";
					$sql .= "missingsapwoodringstobark=" . dbHelper::tellervo_pg_escape_string ( $this->getMissingSapwoodRingsToBark () ) . ", ";
					$sql .= "missingsapwoodringstobarkfoundation=" . dbHelper::tellervo_pg_escape_string ( $this->getMissingSapwoodRingsToBarkFoundation () ) . ", ";
					$sql .= "nrofunmeasuredinnerrings=" . dbHelper::tellervo_pg_escape_string ( $this->getNrOfUnmeasuredInnerRings () ) . ", ";
					$sql .= "nrofunmeasuredouterrings=" . dbHelper::tellervo_pg_escape_string ( $this->getNrOfUnmeasuredOuterRings () ) . ", ";
					$sql .= "barkpresent=" . dbHelper::formatBool ( $this->getBarkPresent (), "pg" ) . ", ";
					$sql .= "azimuth=" . pg_escape_string ( $this->getAzimuth () ) . ", ";
					if (isset ( $this->parentEntityArray [0] ))
						$sql .= "sampleid=" . dbHelper::tellervo_pg_escape_string ( $this->parentEntityArray [0]->getID () ) . ", ";
					$sql = substr ( $sql, 0, - 2 );
					$sql .= " WHERE radiusid='" . pg_escape_string ( $this->getID () ) . "'";
				}
				
				$firebug->log ( $sql, "Write sql" );
				// Run SQL command
				if ($sql) {
					// Run SQL
					pg_send_query ( $dbconn, $sql );
					$result = pg_get_result ( $dbconn );
					if (pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE )) {
						$this->setErrorMessage ( "002", pg_result_error ( $result ) . "--- SQL was $sql" );
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
				if (is_countable($this->userDefinedFieldAndValueArray) && count ( $this->userDefinedFieldAndValueArray ) > 0) {
						
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
	function deleteFromDB() {
		// Delete the record in the database matching the current object's ID
		global $dbconn;
		
		// Check for required parameters
		if ($this->getID () === NULL) {
			trigger_error ( "902" . "Missing parameter - 'id' field is required.", E_USER_ERROR );
			return FALSE;
		}
		
		// Only attempt to run SQL if there are no errors so far
		if ($this->getLastErrorCode () === NULL) {
			$dbconnstatus = pg_connection_status ( $dbconn );
			if ($dbconnstatus === PGSQL_CONNECTION_OK) {
				
				$sql = "DELETE FROM tblradius WHERE radiusid='" . pg_escape_string ( $this->getID () ) . "'";
				
				// Run SQL command
				if ($sql) {
					// Run SQL
					pg_send_query ( $dbconn, $sql );
					$result = pg_get_result ( $dbconn );
					if (pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE )) {
						$PHPErrorCode = pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE );
						switch ($PHPErrorCode) {
							case 23503 :
								// Foreign key violation
								$this->setErrorMessage ( "907", "Foreign key violation.  You must delete all associated measurements before deleting this radius." );
								break;
							default :
								// Any other error
								$this->setErrorMessage ( "002", pg_result_error ( $result ) . "--- SQL was $sql" );
						}
						return FALSE;
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
	function mergeRecords($mergeWithID) {
		global $firebug;
		global $dbconn;
		
		$goodID = $mergeWithID;
		$badID = $this->getID ();
		
		// Only attempt to run SQL if there are no errors so far
		if ($this->getLastErrorCode () === NULL) {
			$sql = "select * from cpgdb.mergeradii('$goodID', '$badID')";
			$firebug->log ( $sql, "SQL" );
			$dbconnstatus = pg_connection_status ( $dbconn );
			if ($dbconnstatus === PGSQL_CONNECTION_OK) {
				// Run SQL
				pg_send_query ( $dbconn, $sql );
				$result = pg_get_result ( $dbconn );
				if (pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE )) {
					$PHPErrorCode = pg_result_error_field ( $result, PGSQL_DIAG_SQLSTATE );
					switch ($PHPErrorCode) {
						default :
							// Any other error
							$this->setErrorMessage ( "002", pg_result_error ( $result ) . "--- SQL was $sql" );
					}
					return FALSE;
				} else {
					$firebug->log ( "Merge successful" );
				}
			} else {
				// Connection bad
				$this->setErrorMessage ( "001", "Error connecting to database" );
				return FALSE;
			}
		}
		
		// Return true as write to DB went ok.
		
		$this->setParamsFromDB ( $goodID );
		return TRUE;
	}
	
	// End of Class
}
?>
