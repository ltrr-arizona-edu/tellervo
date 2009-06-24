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
require_once('inc/note.php');

/**
 * Class for interacting with a measurementEntity.  This contains the logic of how to read and write data from the database as well as error checking etc.
 *
 */
class measurement extends measurementEntity implements IDBAccessor
{
	/***************/
	/* CONSTRUCTOR */
	/***************/

	function __construct()
	{
		// Constructor for this class.
		//$this->setVMeasurementOp(5);
		parent::__construct();
	}

	/***********/
	/* SETTERS */
	/***********/

	function setParamsFromDBRow($row, $format="standard")
	{
		global $debugFlag;
		global $myMetaHeader;
		if ($debugFlag===TRUE) $myMetaHeader->setTiming("Setting measurement parameters from DB result");
		$this->setID($row['vmeasurementid']);
		$this->setAnalyst($row['measuredbyid']);
		$this->setAuthor($row['owneruserid']);
		//$this->setCertaintyLevel();
		$this->setTitle($row['code']);
		$this->setComments($row['comments']);
		$this->setDatingType($row['datingtypeid'],$row['datingtype']);
		$this->dating->setDatingErrors($row['datingerrorpositive'], $row['datingerrornegative']);
		//$this->setDendrochronologist();
		$this->setFirstYear($row['startyear']);
		$this->setIsPublished(dbHelper::formatBool($row['ispublished']));
		$this->setJustification($row['justification']);
		$this->setConfidenceLevel($row['confidence']);
		$this->setMasterVMeasurementID($row['mastervmeasurementid']);
		$this->setMeasurementCount($row['measurementcount']);
		$this->setMeasurementID($row['measurementid']);
		$this->setMeasurementMethod($row['measuringmethodid'], NULL);
		$this->setMeasuringUnits($row['unitid'], NULL, $row['power']);
		$this->setVariable($row['measurementvariableid'], NULL);
		//$this->setNewStartYear();
		$this->setObjective($row['objective']);
		//$this->setOwnerUserID();
		$this->setProvenance($row['provenance']);
		$this->setRadiusID($row['radiusid']);
		$this->setReadingCount($row['readingcount']);
		//$this->setSignificanceLevel();
		$this->setVMeasurementOp($row['vmeasurementopid'], $row['opname']);
		$this->setStandardizingMethod($row['vmeasurementopparameter'], null);
		$this->setCreatedTimestamp($row['createdtimestamp']);
		$this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
		$this->setExtent($row['extentgeometry']);
		//$this->setExtentComment($row['extentcomment']);
		$this->setUnits($row['unitid'], NULL);
		//$this->setSummaryInfo($row['objectcode'], $row['objectcount'], $row['commontaxonname'], $row['taxoncount'], $row['prefix']);
		if($this->vmeasurementOp=='Index') $this->setVMeasurementOpParam($row['vmeasurementopparameter']);
		if($this->vmeasurementOp=='Crossdate') $this->setCrossdateParamsFromDB();
		$this->setSummaryElementTitle($row['elementcode']);
		$this->setSummarySampleTitle($row['samplecode']);
		$this->setSummaryRadiusTitle($row['radiuscode']);
		$this->setSummaryTaxonName($row['commontaxonname']);
		$this->setSummaryTaxonCount($row['taxoncount']);
		$this->setMeasurementCount($row['measurementcount']);
		$this->setBirthDate($row['birthdate']);

		// Only load summary fields if this is a summary...
		if($format=='summary') $this->setSummaryObjectArray($row['objectid']);

		// Deal with readings if we actually need them...
		if($format!="summary") $this->setReadingsFromDB();

		// Set References
		$this->setReferencesFromDB();

		if ($debugFlag===TRUE)
		$myMetaHeader->setTiming("Completed setting measurement parameters from DB result");

		return true;
	}

	function setParamsFromDB($theID, $format="standard")
	{
		global $dbconn;
		global $myMetaHeader;
		global $debugFlag;
		 
		 
		// Set the current objects parameters from the database

		$this->setID($theID);

		// the uberquery - one query to rule them all?
		$sql = "SELECT * FROM vwcomprehensivevm WHERE vmeasurementid='".pg_escape_string($this->getID())."'";
		 
		$dbconnstatus = pg_connection_status($dbconn);
		if ($dbconnstatus ===PGSQL_CONNECTION_OK)
		{

			if ($debugFlag===TRUE) $myMetaHeader->setTiming("Starting setParamsFromDB SQL query");
			pg_send_query($dbconn, $sql);
			$result = pg_get_result($dbconn);
			if ($debugFlag===TRUE) $myMetaHeader->setTiming("Completed setParamsFromDB SQL query");

			if(pg_num_rows($result)==0)
			{
				// No records match the id specified
				$this->setErrorMessage("903", "No match for measurement id=".$this->getID());
				return FALSE;
			}
			else
			{
				// Set parameters from db
				$row = pg_fetch_array($result);
				$this->setParamsFromDBRow($row, $format);
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
	 * @todo
	 *
	 */
	function setParentsFromDB()
	{
		require_once('radius.php');
		global $dbconn;
		global $corinaNS;
		global $tridasNS;
		global $gmlNS;

		if($this->radiusID == NULL)
		{
			$this->setErrorMessage("903", "There are no radii associated with measurement id=".$this->getID());
			return FALSE;
		}

		// Empty array before populating it
		$this->parentEntityArray = array();

		// see if we've cached it already
		if(($myRadius = dbEntity::getCachedEntity("radius", $this->radiusID)) != NULL)
		{
			array_push($this->parentEntityArray, $myRadius);
			return;
		}
		 
		$myRadius = new radius();
		$success = $myRadius->setParamsFromDB($this->radiusID);
		if($success===FALSE)
		trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage());

		// Add to the array of parents
		array_push($this->parentEntityArray,$myRadius);
	}



	private function setReadingsFromDB()
	{
		// Add all readings data to the object
		global $dbconn;
		global $myMetaHeader;
		global $debugFlag;

		// Empty the reading array in case we have data already in there
		unset($this->readingsArray);
		$this->readingsArray = array();


		// Call getVMeasurementResult().
		if ($debugFlag===TRUE) $myMetaHeader->setTiming("Running cpgdb.getVMeasurementResult()");
		$sql2 = "SELECT * FROM cpgdb.getvmeasurementresult('".pg_escape_string($this->getID())."')";
		pg_send_query($dbconn, $sql2);
		$result2 = pg_get_result($dbconn);
		$row2 = pg_fetch_array($result2);
		$this->vmeasurementResultID = $row2['vmeasurementresultid'];

		// Call getVMeasurementReadingResult()
		$sql  = "SELECT * FROM cpgdb.getvmeasurementreadingresult('".pg_escape_string($this->getVMeasurementResultID())."') ".
        		"ORDER BY relyear ASC";
		$dbconnstatus = pg_connection_status($dbconn);
		if ($dbconnstatus ===PGSQL_CONNECTION_OK)
		{
			$result = pg_query($dbconn, $sql);
			$relYearCheck = 0;
			while ($row = pg_fetch_array($result))
			{
				if ($relYearCheck==$row['relyear'])
				{
					// Get all reading values to array
					$this->readingsArray[$row['relyear']] = array('value' => $row['reading'],
                                                                  'wjinc' => $row['wjinc'], 
                                                                  'wjdec' => $row['wjdec'], 
                                                                  'count' => $row['count'],
                                                                  'notesArray' => array()
					);
					$relYearCheck++;
				}
				else
				{
					// Something screwy going on with relyears in the vmeasurementResult
					$this->setErrorMessage("701", "The relative years dating in the database has gone screwy. Please tell someone!");
					return FALSE;

				}

			}

			// If this is a direct measurement then add any notes as a subarray
			if($row['readingid'])
			{
				$noteSQL = "SELECT tlkpreadingnote.*, tblreadingreadingnote.readingid FROM tlkpreadingnote, tblreadingreadingnote WHERE tblreadingreadingnote.readingid = ".pg_escape_string($row['readingid']);
				$noteResult = pg_query($dbconn, $noteSQL);
				while($noteRow = pg_fetch_array($noteResult))
				{
					// Get all reading values to array
					array_push($this->readingsArray[$row['relyear']][notesArray], $noteRow['readingnoteid']);
				}
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

	private function setReferencesFromDB()
	{
		// Add any vmeasurements that the current measurement has been made from
		global $dbconn;


		$sql  = "select * from cpgdb.findvmparents('".pg_escape_string($this->getID())."', 'false') where recursionlevel=0";

		$dbconnstatus = pg_connection_status($dbconn);
		if ($dbconnstatus ===PGSQL_CONNECTION_OK)
		{
			$this->referencesArray = array();
			$result = pg_query($dbconn, $sql);
			while ($row = pg_fetch_array($result))
			{
				// Get add all reading values to array
				array_push($this->referencesArray, $row['vmeasurementid']);
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
	 * Set attributes of this class using a parametersClass
	 *
	 * @param measurementParameters $paramsClass
	 * @param auth $auth
	 * @return Boolean
	 */
	function setParamsFromParamsClass($paramsClass, $auth)
	{
		// Alters the parameter values based upon values supplied by the user and passed as a parameters class
		if ($paramsClass->getIsReconciled()!=NULL)         	$this->setIsReconciled($paramsClass->getIsReconciled());
		if ($paramsClass->getFirstYear()!=NULL)            	$this->setFirstYear($paramsClass->getFirstYear());
		if (isset($paramsClass->dating))
		{
			$this->setDatingType($paramsClass->dating->getID(), $paramsClass->dating->getValue());
			$this->dating->setDatingErrors($paramsClass->dating->getDatingErrorPositive(), $paramsClass->dating->getDatingErrorNegative());
		}
		if ($paramsClass->getCode()!=NULL)					$this->setCode($paramsClass->getCode());
		if ($paramsClass->getComments()!=NULL)				$this->setComments($paramsClass->getComments());
		if (isset($paramsClass->vmeasurementOp))
		{
			$this->setVMeasurementOp($paramsClass->vmeasurementOp->getID(), $paramsClass->vmeasurementOp->getValue());
			$this->vmeasurementOp->setStandardizingMethod($paramsClass->vmeasurementOp->getParamID(), $paramsClass->vmeasurementOp->getStandardizingMethod());
		}
		if($paramsClass->getUnits()!=NULL)					$this->setUnits(NULL, $paramsClass->getUnits());


		if (sizeof($paramsClass->referencesArray)>0)   $this->setReferencesArray($paramsClass->referencesArray);
		//if (isset($paramsClass->masterVMeasurementID)) $this->setMasterVMeasurementID($paramsClass->masterVMeasurementID);
		//if (isset($paramsClass->justification))        $this->setJustification($paramsClass->justification);
		//if (isset($paramsClass->certaintyLevel))       $this->setCertaintyLevel($paramsClass->certaintyLevel);
		//if (isset($paramsClass->newStartYear))         $this->setNewStartYear($paramsClass->newStartYear);
		//if (isset($paramsClass->readingsUnits))        $this->setUnits($paramsClass->readingsUnits);
		if (sizeof($paramsClass->readingsArray)>0)     $this->setReadingsArray($paramsClass->readingsArray);
		 
		 


		if ($paramsClass->parentID!=NULL)
		{
			$parentObj = new radius();
			$parentObj->setParamsFromDB($paramsClass->parentID);
			array_push($this->parentEntityArray, $parentObj);
		}
		 

		//echo "BEFORE\n";
		//print_r($this->readingsArray);

		// Convert readings provided by user to microns
		if (sizeof($paramsClass->readingsArray)>0)
		{
			foreach($this->readingsArray as $key => $value)
			{
				if (isset($this->units))
				{
					$power = $this->units->getPower();
				}
				else
				{
					$power = -5;
				}
				$convValue = $this->unitsHandler($value['value'], $power, 'db-default');
				$this->readingsArray[$key]['value'] = $convValue;
			}
		}
		//echo "AFTER\n";
		//print_r($this->readingsArray);

		// Set Owner and Measurer IDs if specified otherwise use current user details
		if (isset($paramsClass->author))
		{
			$this->setAuthor($paramsClass->author->getID());
		}
		else
		{
			$this->setAuthor($auth->getID());
		}
		if (isset($paramsClass->analyst))
		{
			$this->setAnalyst($paramsClass->analyst->getID());
		}
		else
		{
			$this->setAnalyst($auth->getID());
		}

		return true;
	}


	/**
	 * Validate the parameters passed from a measurementParameters class
	 *
	 * @param measurementParameters $paramsObj
	 * @param String $crudMode
	 * @return Boolean
	 */
	function validateRequestParams($paramsObj, $crudMode)
	{
		// Check parameters based on crudMode
		switch($crudMode)
		{
			case "read":
				if($paramsObj->getID()==NULL)
				{
					$this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a measurement.");
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
					$this->setErrorMessage("902","Missing parameter - 'id' field is required when updating measurement.");
					return false;
				}
				if(($paramsObj->readingsArray) && (count($paramsObj->readingsArray)< 10))
				{
					$this->setErrorMessage("902","Invalid parameter - You have only supplied ".count($paramsObj->readingsArray)." readings.  Minimum number required is 10.");
					return false;
				}
				if($paramsObj->readingsArray)
				{
					foreach ($paramsObj->readingsArray as $reading)
					{
						if(!is_numeric($reading['value']))
						{
							$this->setErrorMessage("902","Invalid parameter - All your readings must be numbers.");
							return false;
						}
					}
				}
				if($paramsObj->referencesArray)
				{
					foreach ($paramsObj->referencesArray as $reference)
					{
						if(!is_numeric( (int) $reference[0]))
						{
							$this->setErrorMessage("902","Invalid parameter - All your reference ID's must be numbers.");
							break;
						}
					}
				}

				// Only allow update on a measurement which is not used by other vm's downstream
				global $dbconn;
				$sql = "select cpgdb.findvmchildren(".$paramsObj->getID().", false)";
				$dbconnstatus = pg_connection_status($dbconn);
				if ($dbconnstatus ===PGSQL_CONNECTION_OK)
				{
					pg_send_query($dbconn, $sql);
					$result = pg_get_result($dbconn);
					if(pg_num_rows($result)>0)
					{
						// No records match the label specified
						$this->setErrorMessage("902", "The measurement that you have specified cannot be updated as it is referred to by other measurements (sums, indexes etc).");
						return FALSE;
					}
				}
				else
				{
					// Connection bad
					$this->setErrorMessage("001", "Error connecting to database");
					return FALSE;
				}


				return true;



			case "delete":
				if($paramsObj->getID() == NULL)
				{
					$this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a measurement.");
					return false;
				}
				return true;

			case "create":
				if(($paramsObj->referencesArray == NULL) && ($paramsObj->readingsArray == NULL))
				{
					$this->setErrorMessage("902","Missing parameter - you must specify either references or readings when creating a new measurement.");
					return false;
				}
				if(($paramsObj->readingsArray) && ($paramsObj->parentID== NULL))
				{
					$this->setErrorMessage("902","Missing parameter - a new direct measurement must include a radiusID.");
					return false;
				}
				if( ($paramsObj->getTitle()==NULL) )
				{
					$this->setErrorMessage("902","Missing parameter - a new measurement requires the title parameter.");
					return false;
				}
				if(($paramsObj->readingsArray) && ($paramsObj->getFirstYear()== NULL) && (isset($paramsObj->dating)) )
				{
					if ($paramsObj->dating->getID()==1)
					{
						$this->setErrorMessage("902","Missing parameter - a new absolute direct measurement must include a startYear.");
						return false;
					}
				}
				/*if(($paramsObj->readingsArray) && ($paramsObj->datingTypeID==NULL))
				 {
				 $this->setErrorMessage("902","Missing parameter - a new direct measurement must include a datingTypeID.");
				 return false;
				 }*/
				if(($paramsObj->readingsArray) && (count($paramsObj->readingsArray)< 10))
				{
					$this->setErrorMessage("902","Invalid parameter - You have only supplied ".count($paramsObj->readingsArray)." readings.  Minimum number required is 10.", E_USER_ERROR);
					return false;
				}
				if(($paramsObj->referencesArray) && ($paramsObj->parentID))
				{
					$this->setErrorMessage("902","Invalid parameter - a new measurement based on other measurements cannot include a radiusID.");
					return false;
				}
				if((sizeof($paramsObj->referencesArray)>0) && ($paramsObj->vmeasurementOp==NULL))
				{
					$this->setErrorMessage("902","Missing parameter - a new measurement based on other measurements must include an operation.");
					return false;
				}
				if((sizeof($paramsObj->referencesArray)<2) && ($paramsObj->getType()=='Sum') )
				{
					$this->setErrorMessage("902","You need to supply two or more measurements if you want to create a sum", E_USER_ERROR);
					return false;
				}
				if( (!(isset($paramsObj->referencesArray))) && (isset($paramsObj->vmeasurementOp)) )
				{
					$this->setErrorMessage("902","Missing parameter - you have included an operation which suggests you are creating a new measurement based on others. However, you have not specified any references to other measurements.");
					return false;
				}
				if( ($paramsObj->vmeasurementOp=='Crossdate') && (!(isset($paramsObj->startYear))) )
				{
					$this->setErrorMessage("902","Missing parameter - a startYear is required when doing a crossdate.");
					return false;
				}
				if( ($paramsObj->vmeasurementOp=='Crossdate') && (!(isset($paramsObj->masterVMeasurementID))) )
				{
					$this->setErrorMessage("902","Missing parameter - a basedOnMeasurementID is required when doing a crossdate.");
					return false;
				}
				if( ($paramsObj->vmeasurementOp=='Crossdate') && (!(isset($paramsObj->certaintyLevel))) )
				{
					$this->setErrorMessage("902","Missing parameter - a certaintyLevel is required when doing a crossdate.");
					return false;
				}
				if( ($paramsObj->vmeasurementOp=='Crossdate') && (!(isset($paramsObj->newStartYear))) )
				{
					$this->setErrorMessage("902","Missing parameter - a new startYear  is required when doing a crossdate.");
					return false;
				}
				if( ($paramsObj->vmeasurementOp=='Crossdate') && (!(isset($paramsObj->justification))) )
				{
					$this->setErrorMessage("902","Missing parameter - a justification is required when doing a crossdate.");
					return false;
				}
				 
				return true;

			default:
				$this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
				return false;
		}
	}


	function setChildParamsFromDB()
	{


		return TRUE;
	}

	/***********/
	/*ACCESSORS*/
	/***********/


	function asTimelineXML()
	{
		// Only return XML when there are no errors.
		$xml = "<event ";
		$xml.= "isDuration='true' ";
		$xml.= "start='".$this->startYear."' ";
		$xml.= "end='".$this->getEndYear()."' ";
		$xml.= "title='".$this->name."' ";
		$xml.= ">".$this->name."</event>\n";

		return $xml;

	}

	function recursiveSetParentsFromDB()
	{
		// load radius
		$this->setParentsFromDB();
		// load sample
		$this->parentEntityArray[0]->setParentsFromDB();
		// load element
		$this->parentEntityArray[0]->parentEntityArray[0]->setParentsFromDB();
		// load objects
		$this->parentEntityArray[0]->parentEntityArray[0]->parentEntityArray[0]->setParentsFromDB();
	}

	function loadDerivationTree(&$direct, &$derived, &$all, $format='standard', $depth=1)
	{
		$myformat = ($format=='standard')?'summary':$format;

		// if we're on the first level, or we're comprehensive, traverse looking for children
		if($depth == 1 || $myformat!='summary')
		{
			foreach($this->referencesArray as $ref)
			{
				if(array_key_exists($ref, $all))
				continue; // duplicate!

				$refMeasurement = new measurement();
				$refMeasurement->setParamsFromDB($ref, $myformat);

				$all[$ref] = $refMeasurement;

				if($refMeasurement->getTridasSeriesType()=='measurementSeries') {
					// store this measurement in the direct array
					array_push($direct, $refMeasurement);

					// load parents
					$refMeasurement->recursiveSetParentsFromDB();
				}
				else
				{
					// add any 'derived' children
					$refMeasurement->loadDerivationTree($direct, $derived, $all, $myformat, $depth+1);
				}
			}
		}

		// add myself to the appropriate list
		if($this->getTridasSeriesType()=='measurementSeries')
		{
			// first level measurement? load myself!
			if($depth == 1)
			$this->recursiveSetParentsFromDB();
			array_push($direct, $this);
		}
		else if($this->getTridasSeriesType()=='derivedSeries')
		array_push($derived, $this);
	}

	function buildDerivationTrees(&$direct, &$objectToElementMap, &$elementTree, &$objectTree, &$all)
	{
		foreach($direct as $d) {
			$myradius = $d->parentEntityArray[0];
			$mysample = $myradius->parentEntityArray[0];
			$myelement = $mysample->parentEntityArray[0];
			$myobjects = $myelement->parentEntityArray;
			$lastobject = $myobjects[count($myobjects) - 1];

			// get some IDs...
			$rid = $myradius->getID();
			$sid = $mysample->getID();
			$eid = $myelement->getID();
			$did = $d->getID();
			$loid = $lastobject->getID();

			// now, we have a map from element -> sample -> radius -> measurementSeries
			$elementTree[$eid][$sid][$rid][$did] = 1;
			// and last object id to radius map...
			$objectToElementMap[$loid][$eid] = 1;

			// make a map from type,id -> class
			$all['measurement'][$did] = $d;
			$all['radius'][$rid] = $myradius;
			$all['sample'][$sid] = $mysample;
			$all['element'][$eid] = $myelement;

			// now, make a tree $objectTree[objid1][objid2][objid3]...
			$lastArray = &$objectTree;
			foreach($myobjects as $obj)
			{
				$objid = $obj->getID();

				// add it to the map (like before)
				$all['object'][$objid] = $obj;

				// make a tree of objects
				if(!array_key_exists($objid, $lastArray))
				$lastArray[$objid] = array();
				$lastArray = &$lastArray[$objid];
			}
		}
	}

	function textAsNode($text, $dom)
	{
		global $corinaNS;
		global $tridasNS;
		global $gmlNS;

		$domhead = "<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">";
		$domfoot = "</root>";

		$tmpdom = new DomDocument();
		if($tmpdom->loadXML($domhead.$text.$domfoot) === FALSE) {
			trigger_error("Malformed XML in textAsNode: Internal Error.\n".$text);
		}
		return $dom->importNode($tmpdom->documentElement->childNodes->item(0), true);
	}

	function outputElementDerivationTree($elementID, &$dom, $curNode, &$elementTree, &$all, $format)
	{
		$myElement = $all['element'][$elementID];
		$myElementNode = $this->textAsNode($myElement->asXML(), $dom);
		$curNode->appendChild($myElementNode);

		foreach($elementTree[$elementID] as $sampleID => $radii)
		{
			$mySample = $all['sample'][$sampleID];
			$mySampleNode = $this->textAsNode($mySample->asXML(), $dom);
			$myElementNode->appendChild($mySampleNode);

			foreach($radii as $radiusID => $measurements)
			{
				$myRadius = $all['radius'][$radiusID];
				$myRadiusNode = $this->textAsNode($myRadius->asXML(), $dom);
				$mySampleNode->appendChild($myRadiusNode);

				foreach($measurements as $measurementID => $dummy)
				{
					$myMeasurement = $all['measurement'][$measurementID];
					$myFormat = ($myMeasurement == $this) ? $format : (($format=='standard') ? 'summary' : $format);

					$myMeasurementNode = $this->textAsNode($myMeasurement->_asXML($myFormat, "full"), $dom);
					$myRadiusNode->appendChild($myMeasurementNode);
				}
			}
		}
	}

	// this has to be recursive and strange, because we can have an unknown depth tree of objects
	function outputDerivationTree(&$dom, $curNode, &$objectTree, &$elementTree, &$objectToElementMap, &$all, $format)
	{
		foreach($objectTree as $objid => $subObjectTree)
		{
			$obj = $all['object'][$objid];

			$node = $this->textAsNode($obj->asXML(), $dom);
			$curNode->appendChild($node);

			// do subtrees first
			if(count($subObjectTree) > 0)
			$this->outputDerivationTree($dom, $node, $subObjectTree, $elementTree, $objectToElementMap, $all, $format);

			// ok, done with any subtrees. Do I have any elements in this object?
			if(array_key_exists($objid, $objectToElementMap))
			{
				foreach($objectToElementMap[$objid] as $elementID => $dummy)
				$this->outputElementDerivationTree($elementID, $dom, $node, $elementTree, $all, $format);
			}
		}
	}

	function fullSeriesAsXML($format='standard')
	{
		global $corinaNS;
		global $tridasNS;
		global $gmlNS;

		// first off, load everything
		$direct = Array();
		$derived = Array();
		$all = Array();
		$this->loadDerivationTree($direct, $derived, $all, $format);

		// then, sort everything
		$objectToElementMap = array();
		$all = array();
		$elementTree = array();
		$objectTree = array();
		$this->buildDerivationTrees($direct, $objectToElementMap, $elementTree, $objectTree, $all);

		$dom = new DomDocument();
		$dom->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\"></root>");
		$this->outputDerivationTree($dom, $dom->documentElement, $objectTree, $elementTree, $objectToElementMap, $all, $format);

		// now, just print out the derivedSeries in order. $this is going to be last.
		foreach($derived as $d)
		{
			$myformat = ($d == $this) ? $format : (($format=='standard') ? 'summary' : $format);
			$dom->documentElement->appendChild($this->textAsNode($d->_asXML($myformat, "full"), $dom));
		}

		$txml = "";
		foreach($dom->documentElement->childNodes as $child)
		$txml .= $dom->saveXML($child);

		return $txml;
	}

	function asXML($format='standard', $parts="full")
	{
		 
		// Only direct measurements can have comprehensive format so overide if necessary
		/*if( ($format=='comprehensive') && ($this->vmeasurementOp!='Direct'))
		{
		$format = 'standard';
		}*/

		switch($format)
		{
			case "comprehensive":
			case "standard":
				if($this->getTridasSeriesType()=='measurementSeries')
				{
					return $this->fullSeriesAsXML($format);
				}
				elseif($this->getTridasSeriesType()=='derivedSeries')
				{
					return $this->fullSeriesAsXML($format);
				}
				else
				{
					echo "error";
					die();
				}

			case "summary":
				return $this->_asXML($format, $parts);
			case "minimal":
				return $this->_asXML($format, $parts);
			default:
				$this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary', 'minimal' or 'comprehensive'");
				return false;
		}
	}

	public function asKML()
	{
		$kml = "<Placemark><name>".$this->getSummaryObjectCode()."-".$this->getSummaryElementTitle()."-".$this->getSummarySampleTitle()."-".$this->getSummaryRadiusTitle()."-".$this->getTitle()."</name><description><![CDATA[<br><b>Type</b>: ".$this->getType()."<br><b>Description</b>: ]]></description>";
		$kml .= "<styleUrl>#corinaDefault</styleUrl>";
		$kml .= $this->extent->asKML();
		$kml .= "</Placemark>";
		return $kml;
	}

	private function _asXML($format, $parts, $recurseLevel=2)
	{

		 
		// Return a string containing the current object in XML format

		// $recurseLevel = the number of levels of references tags you would like
		//      in your XML output.
		//      Default = 2 - which means the current measurement and its immediate parents
		// $format = the type of XML output
		//      standard = all XML including notes and readings
		//      summary = only metadata

		global $domain;
		$xml = "";

		// Check whether we are at the requested level of recursion or not
		if($recurseLevel==-1)
		{
			return;
		}
		else
		{
			// Decrement recurse level
			$recurseLevel=$recurseLevel-1;
		}

		// Proceed if there are no errors already
		if ($this->getLastErrorCode()==NULL)
		{
			// Only return XML when there are no errors.
			if($this->getTridasSeriesType()=='measurementSeries')
			{
				return $this->getMeasurementSeriesXML($format, $parts, $recurseLevel);
			}
			else
			{
				return $this->getDerivedSeriesXML($format, $parts, $recurseLevel);
			}
			 

		}
		else
		{
			// Errors so returning false
			return FALSE;
		}
	}

	/**
	 * Get the Corina specific (genericField) XML tags that summarise the higher levels of the TRiDaS hierarchy
	 *
	 * @return String
	 */
	private function getSummaryXMLTags()
	{
		$tags = "";
		$i = 1;
		foreach($this->summaryObjectArray as $object)
		{
			$tags .= "<tridas:genericField name=\"corina.objectTitle.$i\" type=\"xs:string\">".dbHelper::escapeXMLChars($object->getTitle())."</tridas:genericField>\n";
			$tags .= "<tridas:genericField name=\"corina.objectCode.$i\" type=\"xs:string\">".dbHelper::escapeXMLChars($object->getCode())."</tridas:genericField>\n";
			$i++;
		}
		$tags.= "<tridas:genericField name=\"corina.elementTitle\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getSummaryElementTitle())."</tridas:genericField>\n";
		$tags.= "<tridas:genericField name=\"corina.sampleTitle\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getSummarySampleTitle())."</tridas:genericField>\n";
		$tags.= "<tridas:genericField name=\"corina.radiusTitle\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getSummaryRadiusTitle())."</tridas:genericField>\n";
		$tags.= "<tridas:genericField name=\"corina.seriesCount\" type=\"xs:integer\">".dbHelper::escapeXMLChars($this->getMeasurementCount())."</tridas:genericField>\n";
		$tags.= "<tridas:genericField name=\"corina.summaryTaxonName\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getSummaryTaxonName())."</tridas:genericField>\n";
		$tags.= "<tridas:genericField name=\"corina.summaryTaxonCount\" type=\"xs:integer\">".dbHelper::escapeXMLChars($this->getSummaryTaxonCount())."</tridas:genericField>\n";
		return $tags;
	}

	private function getDerivedSeriesXML($format, $parts, $recurseLevel=2)
	{
		global $domain;
		$xml = "";

		$xml.= "<tridas:".$this->getTridasSeriesType()." id=\"".$this->getXMLRefID()."\">";
		$xml.= $this->getIdentifierXML();
		
		if($this->getComments()!=NULL)	$xml.= "<tridas:comments>".dbHelper::escapeXMLChars($this->getComments())."</tridas:comments>\n";
		if($this->getBirthDate()!=NULL)	$xml.= "<tridas:derivationDate>".dbHelper::escapeXMLChars($this->getBirthDate())."</tridas:derivationDate>\n";	

		if(isset($this->vmeasurementOp)) 			$xml.= "<tridas:type>".$this->vmeasurementOp->getValue()."</tridas:type>\n";
		if($this->getObjective()!=NULL)				$xml.= "<tridas:objective>".addslashes($this->getObjective())."</tridas:objective>\n";
		if(isset($this->referencesArray))
		{											$xml.= "<tridas:linkSeries>\n";
		foreach($this->referencesArray as $ref)
		{
			$xml.= "<tridas:identifier domain=\"$domain\">".$ref."</tridas:identifier>\n";
		}
		$xml.= "</tridas:linkSeries>\n";
		}

		if($this->getStandardizingMethod()!=NULL)	$xml.= "<tridas:standardizingMethod>".addslashes($this->getStandardizingMethod())."</tridas:standardizingMethod>\n";
		if($this->getAuthor()!=NULL)				$xml.= "<tridas:author>".addslashes($this->getAuthor())."</tridas:author>\n";
		if($this->getVersion()!=NULL)				$xml.= "<tridas:version>".addslashes($this->getVersion())."</tridas:version>\n";


		if($this->getComments()!=NULL)				$xml.= "<tridas:comments>".addslashes($this->getComments())."</tridas:comments>\n";
		$xml.= "<tridas:interpretation>\n";
		if($this->getMasterVMeasurementID()!=NULL)	$xml.= "<tridas:datingReference>\n<tridas:linkSeries>\n<tridas:identifier domain=\"$domain\">".$this->getMasterVMeasurementID()."</tridas:identifier>\n</tridas:linkSeries>\n</tridas:datingReference>\n";

		//if($this->getFirstYear()!=NULL)				$xml.= "<tridas:firstYear suffix=\"".dateHelper::getGregorianSuffixFromSignedYear($this->getFirstYear())."\">".dateHelper::getGregorianYearNumberFromSignedYear($this->getFirstYear())."</tridas:firstYear>\n";
		if($this->getSproutYear()!=NULL)			$xml.= "<tridas:sproutYear>".$this->getSproutYear()."</tridas:sproutYear>\n";
		if($this->getDeathYear()!=NULL)				$xml.= "<tridas:deathYear>".$this->getDeathYear()."</tridas:deathYear>\n";
		if($this->getProvenance()!=NULL)			$xml.= "<tridas:provenance>".addslashes($this->getProvenance())."</tridas:provenance>\n";
		$xml.= "</tridas:interpretation>\n";


		if($this->hasExtent()!=NULL)				$xml.= $this->getExtentAsXML();
		if($this->getJustification()!=NULL)			$xml.= "<tridas:genericField name=\"crossdateJustification\" type=\"xs:string\">".$this->getJustification()."</tridas:genericField>\n";
		if($this->getConfidenceLevel()!=NULL)		$xml.= "<tridas:genericField name=\"crossdateConfidenceLevel\" type=\"xs:string\">".$this->getConfidenceLevel()."</tridas:genericField>\n";
		if(isset($this->vmeasurementOpParam))       $xml.= "<tridas:genericField name=\"operationParameter\" type=\"xs:string\">".$this->getIndexNameFromParamID($this->vmeasurementOpParam)."</tridas:genericField>\n";
		if($this->getAuthor()!=NULL)				$xml.= "<tridas:genericField name=\"authorID\" type=\"xs:integer\">".$this->author->getID()."</tridas:genericField>\n";
	    											$xml.= "<tridas:genericField name=\"corina.isReconciled\" type=\"xs:boolean\">".dbHelper::formatBool($this->getIsReconciled(), 'english')."</tridas:genericField>\n";
		
		$xml .= $this->getPermissionsXML();
		if($this->getReadingCount()!=NULL)			$xml.= "<tridas:genericField name=\"corina.readingCount\" type=\"xs:integer\">".$this->getReadingCount()."</tridas:genericField>\n";




		// Using 'summary' format so just give minimal XML for all references and nothing else
		if($format=="summary")
		{
			$xml.= $this->getSummaryXMLTags()."\n";
			$xml.= "</tridas:".$this->getTridasSeriesType().">";
			return $xml;
		}

		// Standard or Comprehensive format so give the whole lot
		else 
		{
			$xml.=$this->getValuesXML();
			$xml.=$this->getValuesXML(true);
			$xml.= "</tridas:".$this->getTridasSeriesType().">";
			return $xml;
		}

	}

	private function getMeasurementSeriesXML($format, $parts, $recurseLevel=2)
	{

		$xml = "<tridas:".$this->getTridasSeriesType()." id=\"".$this->getXMLRefID()."\">";
		$xml.= $this->getIdentifierXML();


		// Only output the remainder of the data if we're not using the 'minimal' format
		if ($format!="minimal")
		{
			if($this->getComments()!=NULL)				$xml.= "<tridas:comments>".$this->getComments()."</tridas:comments>\n";
			if($this->getBirthDate()!=NULL)				$xml.= "<tridas:measuringDate>".$this->getMeasuringDate()."</tridas:measuringDate>\n";
			if($this->analyst->getFormattedName()!=NULL) $xml.= "<tridas:analyst>".$this->analyst->getFormattedName()."</tridas:analyst>\n";
			if($this->dendrochronologist->getFormattedName()!=NULL) $xml.= "<tridas:dendrochronologist>".$this->dendrochronologist->getFormattedName()."</tridas:dendrochronologist>\n";
			if($this->getMeasuringMethod()!=NULL)		$xml.= "<tridas:measuringMethod normalTridas=\"".$this->measuringMethod->getValue()."\"/>\n";

			$xml.="<tridas:interpretation>\n";
			if($this->getFirstYear()!=NULL)				$xml.="<tridas:firstYear suffix=\"".dateHelper::getGregorianSuffixFromSignedYear($this->getFirstYear())."\">".$this->getFirstYear()."</tridas:firstYear>\n";
			if($this->getSproutYear()!=NULL)			$xml.="<tridas:sproutYear suffix=\"".dateHelper::getGregorianSuffixFromSignedYear($this->getSproutYear())."\">".$this->getSproutYear()."</tridas:sproutYear>\n";
			if($this->getDeathYear()!=NULL)				$xml.="<tridas:deathYear suffix=\"".dateHelper::getGregorianSuffixFromSignedYear($this->getDeathYear())."\">".$this->getDeathYear()."</tridas:deathYear>\n";
			if($this->getProvenance()!=NULL)			$xml.="<tridas:provenance>".$this->getProvenance()."</tridas:provenance>\n";
			$xml.="</tridas:interpretation>\n";

			// Include permissions details if requested
			$xml .= $this->getPermissionsXML();
			if($this->getIsReconciled()!=NULL)    		$xml.= "<tridas:genericField type=\"corina.isReconciled\" type=\"xs:boolean\">".dbHelper::fromPHPtoStringBool($this->isReconciled)."</tridas:genericField>\n";
			if(isset($this->isPublished))           	$xml.= "<tridas:genericField name=\"corina.isPublished\" type=\"xs:boolean\">".dbHelper::formatBool($this->isPublished, "english")."</tridas:genericField>\n";
			if(isset($this->analyst))					$xml.= "<tridas:genericField name=\"corina.analystID\" type=\"xs:integer\">".$this->analyst->getID()."</tridas:genericField>\n";
			if($this->dendrochronologist->getID()!=NULL) $xml.= "<tridas:genericField name=\"corina.dendrochronologistID\" type=\"xs:integer\">".$this->dendrochronologist->getID()."</tridas:genericField>\n";
			if($this->getReadingCount()!=NULL)			$xml.= "<tridas:genericField name=\"corina.readingCount\" type=\"xs:integer\">".$this->getReadingCount()."</tridas:genericField>\n";
														$xml.= "<tridas:genericField name=\"corina.isReconciled\" type=\"xs:boolean\">".dbHelper::formatBool($this->getIsReconciled(), 'english')."</tridas:genericField>\n";
			
			// show summary information in standard and summary modes
			/*if($format=="summary" || $format=="standard") {
			// Return special summary section
			$xml.="<summary>";
			$xml.="<labPrefix>".dbHelper::escapeXMLChars($this->labPrefix)."</labPrefix>\n";
			$xml.="<fullLabCode>".dbHelper::escapeXMLChars($this->fullLabCode)."</fullLabCode>\n";
			$xml.="<taxon count=\"".$this->summaryTaxonCount."\" commonAncestor=\"".$this->summaryTaxonName."\"/>\n";
			$xml.="<site count=\"".$this->summarySiteCount."\" ";
			if($this->summarySiteCount=1) $xml.="siteCode=\"".$this->summarySiteCode."\"/>\n";
			if($this->measurementCount!=NULL) $xml.="<measurement count=\"".$this->measurementCount."\"/>";
			$xml.="</summary>";
			}*/

			// Using 'summary' format so just give minimal XML for all references and nothing else
			if($format=="summary")
			{
				$xml.= $this->getSummaryXMLTags()."\n";
				$xml.= "</tridas:".$this->getTridasSeriesType().">";
				return $xml;
			}

			// Standard or Comprehensive format so give the whole lot
			else
			{
				$xml.=$this->getValuesXML();
				$xml.= "</tridas:".$this->getTridasSeriesType().">";
				return $xml;
			}
		}

	}


	private function getValuesXML($wj=false)
	{
		if (!$this->readingsArray)
		{
			return false;
		}
		 
		if($wj===TRUE && $this->getVMeasurementOp()=='Index')
		{
			return false;
		}
		 
		 
		// Initially set yearvalue to 1001 default
		if ($this->dating->getValue()=='Relative')
		{
			$yearvalue = 1001;
		}
		else
		{
			if($this->getFirstYear()==NULL)
			{
				$this->setErrorMessage(667, "First year missing from absolute or absolute with error measurement.  You shouldn't have been able to get this far!");
				return false;
			}
			else
			{
				$yearvalue = $this->getFirstYear();
			}
		}

		$xml = "<tridas:values>\n";
		 
		if($this->getTridasSeriesType()=='measurementSeries')
		{
			global $wsDefaultUnits;
			$xml .="<tridas:variable normalTridas=\"".$this->getVariable()."\"/>\n";
			if($this->getUnits()!=NULL)
			{
				$xml.="<tridas:unit normalTridas=\"$wsDefaultUnits\" />\n";
			}
			else
			{
				$xml.="<tridas:unitless/>\n";
			}
		}
		else
		{
			if($wj===TRUE)
			{
				$xml .="<tridas:variable normalStd=\"Corina\" normal=\"Weiserjahre\"/>\n";
			}
			else
			{
				$xml .="<tridas:variable normalTridas=\"".$this->getVariable()."\"/>\n";
			}
			$xml.="<tridas:unit/>\n";
		}

		//print_r($this->readingsArray);
		foreach($this->readingsArray as $key => $value)
		{
			// Calculate absolute year where possible
			if ($this->dating->getValue()!='Relative')
			{
				if($yearvalue==0)
				{
					// If year value is 0 increment to 1 as there is no such year as 0bc/ad
					$yearvalue = 1;
				}
			}

			// Add year index
			$xml.="<tridas:value index=\"nr".$yearvalue."\" ";
			 
			// Add actual value
			if($wj===TRUE)
			{
				$xml.= "value=\"".$value['wjinc']."/".$value['wjdec']."\" ";
			}
			else
			{
				$xml.= "value=\"".$this->unitsHandler($value['value'], "db-default", "ws-default")."\" ";
			}
			 
			// Add count if appropriate
			if(($value['count']!=NULL) && ($wj===FALSE))
			{
				$xml.= "count=\"".$value['count']."\"";
			}
			 
			$xml .=">";

			// Add any notes that are in the notesArray subarray
			if(count($value['notesArray']) > 0)
			{
				foreach($value['notesArray'] as $notevalue)
				{
					$myReadingNote = new readingNote;
					$success = $myReadingNote->setParamsFromDB($notevalue);

					if($success)
					{
						$xml.=$myReadingNote->asXML();
					}
					else
					{
						$this->setErrorMessage($myReadingNote->getLastErrorCode(), $myReadingNote->getLastErrorMessage());
					}
				}
			}

			$xml.="</tridas:value>\n";

			// Increment yearvalue for next loop
			$yearvalue++;

		}
		$xml.="</tridas:values>\n";

		return $xml;
	}

	/**
	 * Creates the sql for doing a cpgdb.createnewvmeasurement()
	 *
	 * @return UUID
	 */
	private function getCreateNewVMeasurementSQL()
	{
		/*
		 -- VMeasurementOp          - Varchar - From tlkpVMeasurementOp
		 -- VMeasurementOpParameter - Integer - Must be specified for REDATE or INDEX; otherwise NULL
		 -- Name                    - Varchar - Must be specified
		 -- Description             - Varchar - May be NULL
		 -- MeasurementID           - Integer - For direct only; the measurement derived from.
		 -- Constituents            - Array   - Array of VMeasurementID - Must be NULL for DIRECT type, an array of one value for any type
		 --                                     other than SUM and DIRECT, and an array of one or more values for SUM
		 -- Objective               - Varchar -
		 -- Version                 - Varchar -
		 -- Birthdate				- Date    - maps to measuringDate and derivationDate
		 -- RETURNS: A new VMeasurementID
		 */

		$sql = "select * from cpgdb.createnewvmeasurement(";

		// Operation
		$sql.= "'".pg_escape_string($this->getVMeasurementOp())."', ";

		// Operation parameters
		if($this->getStandardizingMethod()!=NULL)
		{
			$sql.= "'".pg_escape_string($this->vmeasurementOp->getStandardizingMethodID())."', ";
		}
		else
		{
			$sql.= "NULL, ";
		}

		// Author
		if($this->author->getID()!=NULL)
		{
			$sql.= pg_escape_string($this->author->getID()).", ";
		}
		else
		{
			$sql.= pg_escape_string($myAuth->getID()).", ";
		}

		// Code
		$sql.= "'".pg_escape_string($this->getCode())."', ";

		// Comments
		if($this->getComments()!=NULL)
		{
			$sql.= "'".pg_escape_string($this->getComments())."', ";
		}
		else
		{
			$sql.= "NULL, ";
		}

		// Base measurement
		if($this->getVMeasurementOp()=='Direct')
		{
			$sql.= pg_escape_string($this->getMeasurementID()).", ";
		}
		else
		{
			$sql.= "NULL, ";
		}

		// Constituents
		if($this->getVMeasurementOp()!='Direct')
		{
			$sql.= "ARRAY[";
			foreach($this->referencesArray as $value)
			{
				$sql.= pg_escape_string($value).", ";
			}
			$sql = substr($sql, 0, -2)."], ";
		}
		else
		{
			$sql.= "NULL, ";
		}

		// Objective
		if($this->getObjective()!=NULL)
		{
			$sql.= "'".pg_escape_string($this->getObjective())."', ";
		}
		else
		{
			$sql.="NULL, ";
		}

		// Version
		if($this->getVersion()!=NULL)
		{
			$sql.= "'".pg_escape_string($this->getVersion())."', ";
		}
		else
		{
			$sql.= "NULL, ";
		}

		// Birth (measuring/derivation) Date
		if($this->getMeasuringDate()!=NULL)
		{
			$sql.= "'".pg_escape_string($this->getBirthDate())."')";
		}
		else
		{
			$sql.= "NULL)";
		}
		
		
		return $sql;
		 
	}



	/***********/
	/*FUNCTIONS*/
	/***********/

	/**
	 * Writes this object to the database
	 *
	 * @return Boolean
	 */
	function writeToDB()
	{
		// Write the current object to the database

		/**
		 * ORDER OF PLAY
		 * *************
		 * New direct measurements:
		 * 1) Insert tblmeasurement row
		 * 2) Insert multiple tblreading rows
		 * 3) Insert tblreadingreadingnote rows
		 * 4) Create new vmeasurement with cpgdb.createnewvmeasurement()
		 *
		 * New derived measurement:
		 * 1) Use cpgdb.createnewvmeasurement()
		 * 2) Use cpgdb.finishcrossdate() if it's a crossdate
		 *
		 * Edit existing measurement:
		 * 1) Delete then reinsert relevant tblvmeasurementgroup entries if derived
		 * 2) Update tblmeasurement row
		 * 3) Delete then reinsert tblreading entries if direct
		 * 4) Update tblvmeasurement row
		 *
		 * **************
		 */
		 

		global $dbconn;
		global $myAuth;

		
		//Only attempt to run SQL if there are no errors so far
		if($this->getLastErrorCode() == NULL)
		{

			// Check DB connection is OK before continueing
			$dbconnstatus = pg_connection_status($dbconn);
			if ($dbconnstatus ===PGSQL_CONNECTION_OK)
			{
				//
				// New record
				//
				if(($this->getID() == NULL))
				{
					if($this->getVMeasurementOp()=='Direct')
					{
						// New direct measurement so create tblmeasurement record first
						$sql = "insert into tblmeasurement  (  ";
						if(isset($this->parentEntityArray[0]))              $sql.= "radiusid, ";
						if($this->getIsReconciled()!=NULL)    				$sql.= "isreconciled, ";
						if($this->getFirstYear())				            $sql.= "startyear, ";
						if(isset($this->analyst))					        $sql.= "measuredbyid, ";
						if($this->dating->getID()!=NULL)
						{
							$sql.= "datingtypeid, ";
							if($this->dating->getDatingErrorNegative()!=NULL)   $sql.= "datingerrornegative, ";
							if($this->dating->getDatingErrorPositive()!=NULL)   $sql.= "datingerrorpositive, ";
						}
						if(isset($this->variable))							$sql.= "measurementvariableid, ";
						if(isset($this->units))
						{
							$sql.= "unitid, ";
							if($this->units->getPower()!=NULL)					$sql.= "power, ";
						}
						if($this->getProvenance()!=NULL)					$sql.= "provenance, ";
						if(isset($this->measuringMethod))					$sql.= "measuringmethodid, ";
						if($this->dendrochronologist->getID()!=NULL)				$sql.= "supervisedbyid, ";
						// Trim off trailing space and comma
						$sql = substr($sql, 0, -2);
						$sql.=") values (";
						if(isset($this->parentEntityArray[0]))              $sql.= "'".pg_escape_string($this->parentEntityArray[0]->getID())."', ";
						if($this->getIsReconciled()!=NULL)    				$sql.= "'".dbHelper::formatBool($this->getIsReconciled(), 'english')."', ";
						if($this->getFirstYear())				            $sql.= "'".pg_escape_string($this->getFirstYear())."', ";
						if(isset($this->analyst))					        $sql.= "'".pg_escape_string($this->analyst->getID())."', ";
						if($this->dating->getID()!=NULL)
						{
							$sql.= "'".pg_escape_string($this->dating->getID())."', ";
							if($this->dating->getDatingErrorNegative()!=NULL)   $sql.= "'".pg_escape_string($this->dating->getDatingErrorNegative())."', ";
							if($this->dating->getDatingErrorPositive()!=NULL)   $sql.= "'".pg_escape_string($this->dating->getDatingErrorPositive())."', ";
						}
						if(isset($this->variable))							$sql.= "'".$this->variable->getID()."', ";
						if(isset($this->units))
						{
							$sql.= "'".pg_escape_string($this->units->getID())."', ";
							if($this->units->getPower()!=NULL)					$sql.= "'".pg_escape_string($this->units->getPower())."', ";
						}
						if($this->getProvenance()!=NULL)					$sql.= "'".pg_escape_string($this->getProvenance())."', ";
						if(isset($this->measuringMethod))					$sql.= "'".pg_escape_string($this->measuringMethod->getID())."', ";
						if($this->dendrochronologist->getID()!=NULL)		$sql.= "'".pg_escape_string($this->dendrochronologist->getID())."', ";
						// Trim off trailing space and comma
						$sql = substr($sql, 0, -2);
						$sql.=")";

						// Run SQL
						pg_send_query($dbconn, $sql);
						$result = pg_get_result($dbconn);
						if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
						{
							$this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
							return FALSE;
						}
						else
						{
							// Insert successful so retrieve automated field values
							$sql2 = "select * from tblmeasurement where measurementid=currval('tblmeasurement_measurementid_seq')";
							$result = pg_query($dbconn, $sql2);
							while ($row = pg_fetch_array($result))
							{
								 
								$this->setMeasurementID($row['measurementid']);
							}
						}

						// Insert new readings
						$relyear = 0;
						foreach($this->readingsArray as $key => $value)
						{
							// First loop through the readingsArray and create insert statement for tblreading table
							$insertSQL = "insert into tblreading (measurementid, relyear, reading) values (".pg_escape_string($this->measurementID).", ".pg_escape_string($relyear).", ".pg_escape_string($value['value']).")";
							$relyear++;

							// Do tblreading inserts
							pg_send_query($dbconn, $insertSQL);
							$result = pg_get_result($dbconn);
							if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
							{
								// Insert failed
								$this->setErrorMessage("002", pg_result_error($result)."--- SQL was $insertSQL");
								return FALSE;
							}
							else
							{
								// Insert successful
								if(count($value['notesArray']) > 0)
								{
									// There are notes associated with this reading.  Before insert new notes we first need the pkey of the newly inserted record
									$sql3 = "SELECT * from tblreading where readingid=currval('tblreading_readingid_seq')";
									$result3 = pg_query($dbconn, $sql3);
									while ($row3 = pg_fetch_array($result3))
									{
										$thisReadingID = $row3['readingid'];
									}

									foreach($value['notesArray'] as $noteKey )
									{
										// Looping through notes and creating SQL insert statements
										$insertSQL = "INSERT INTO tblreadingreadingnote (readingid, readingnoteid) value(".pg_escape_string($thisReadingID).", ".pg_escape_string($noteKey).")";

										// Do tblreadingreadingnote inserts
										pg_send_query($dbconn, $insertSQL);
										$result4 = pg_get_result($dbconn);
										if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
										{
											// Insert failed
											$this->setErrorMessage("002", pg_result_error($result4)."--- SQL was $insertSQL");
											return FALSE;
										}
									}
								}
							}
						}
						// End of Readings insert


					}

					// Create new vmeasurement
					$sql = $this->getCreateNewVMeasurementSQL();


					// Run SQL
					pg_send_query($dbconn, $sql);
					$result = pg_get_result($dbconn);
					if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
					{
						$this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
						return FALSE;
					}
					else
					{
						// Successful so retrieve the automated fields for this new vmeasurement
						while ($row = pg_fetch_array($result))
						{
							$localVMID = $row['createnewvmeasurement'];
							$this->setParamsFromDB($row['createnewvmeasurement']);
						}
					}

					// This extra SQL query is needed to finish off a crossdate
					if($this->vmeasurementOp=='Crossdate')
					{
						$sql = "select cpgdb.finishCrossdate(".pg_escape_string($localVMID).", ".
						pg_escape_string($this->newStartYear).", ".
						pg_escape_string($this->masterVMeasurementID).", '".
						pg_escape_string($this->justification)."', ".
						pg_escape_string($this->certaintyLevel).")";
						pg_send_query($dbconn, $sql);
						$result = pg_get_result($dbconn);
						if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
						{
							$this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
							return FALSE;
						}
					}

					// Successful so retrieve the automated fields for this new vmeasurement
					$this->setParamsFromDB($localVMID);


				}
				else
				{
					// Editing an exisiting record
					$insertSQL	= NULL;
					$updateSQL2 = NULL;
					$deleteSQL = NULL;

					// Update references or readings depending on whether the measurement is direct or not

					if ($this->vmeasurementOp->getValue()!=="Direct")
					{
						// Update references to other vmeasurements
						$deleteSQL = "DELETE FROM tblvmeasurementgroup WHERE vmeasurementid=".pg_escape_string($this->vmeasurementID)."; ";
						$relyear = 0;
						foreach($this->referencesArray as $key => $value)
						{
							$insertSQL .= "INSERT INTO tblvmeasurementgroup (vmeasurementid, membervmeasurementid) VALUES (".pg_escape_string($this->getID()).", ".pg_escape_string($value)."); ";
							$relyear++;
						}
					}
					elseif($this->vmeasurementOp->getValue()=="Direct")
					{
						// Update the tblmeasurement table
						$updateSQL2.= "UPDATE tblmeasurement SET ";
						if(isset($this->parentEntityArray[0]))            	$updateSQL2.= "radiusid = ".pg_escape_string($this->parentEntityArray[0]->getID()).", ";
						if($this->getIsReconciled()!=NULL)        					$updateSQL2.= "isreconciled='".dbHelper::formatBool($this->getIsReconciled(),'pg')."', ";
						if($this->getFirstYear()!=NULL)           					$updateSQL2.= "startyear = ".pg_escape_string($this->getFirstYear()).", ";
						if($this->analyst->getID()!=NULL)	        				$updateSQL2.= "measuredbyid = ".pg_escape_string($this->analyst->getID()).", ";
						if($this->dating->getID()!=NULL)        					$updateSQL2.= "datingtypeid = ".pg_escape_string($this->dating->getID()).", ";
						if($this->dating->getDatingErrorPositive()!=NULL) 			$updateSQL2.= "datingerrorpositive = ".pg_escape_string($this->dating->getDatingErrorPositive()).", ";
						if($this->dating->getDatingErrorNegative()!=NULL) 			$updateSQL2.= "datingerrornegative = ".pg_escape_string($this->dating->getDatingErrorNegative()).", ";
						$updateSQL2 = substr($updateSQL2, 0 , -2);
						$updateSQL2.= " WHERE measurementid=".pg_escape_string($this->getMeasurementID())."; ";

						// Update readings
						$deleteSQL = "DELETE FROM tblreading WHERE measurementid=".pg_escape_string($this->getMeasurementID())."; ";
						$relyear = 0;
						foreach($this->readingsArray as $key => $value)
						{
							$insertSQL .= "INSERT INTO tblreading (measurementid, relyear, reading) VALUES (".pg_escape_string($this->getMeasurementID()).", ".pg_escape_string($relyear).", ".pg_escape_string($value['value'])."); ";
							$relyear++;
						}
					}

					// Update the tblvmeasurement table
					$updateSQL = "UPDATE tblvmeasurement SET ";

					if($this->getVMeasurementOp()!=NULL)		$updateSQL.= "vmeasurementopid ='".pg_escape_string($this->vmeasurementOp->getID())."', ";
					if($this->vmeasurementOp->getParamID()!=NULL)	$updateSQL.= "vmeasurementopparameter ='".pg_escape_string($this->vmeasurementOp->getParamID())."', ";
					if($this->getCode()!=NULL)               	$updateSQL.= "code = '".pg_escape_string($this->getCode())."', ";
					if($this->getComments()!=NULL)        		$updateSQL.= "comments = '".pg_escape_string($this->getComments())."', ";
					if($this->author->getID()!=NULL)			$updateSQL.= "owneruserid = '".pg_escape_string($this->author->getID())."', ";
					if($this->objective!=NULL)					$updateSQL.= "objective= '".pg_escape_string($this->objective)."', ";
					if($this->version!=NULL)					$updateSQL.= "version= '".pg_escape_string($this->version)."', ";
					$updateSQL = substr($updateSQL, 0 , -2);
					$updateSQL.= " WHERE vmeasurementid=".$this->getID()."; ";


					// Perform query using transactions so that if anything goes wrong we can roll back
					$transaction = array("begin;", $deleteSQL, $insertSQL, $updateSQL2, $updateSQL );
					
					foreach($transaction as $stmt)
					{
						if ($stmt==NULL) continue;
						$result = pg_query($dbconn, $stmt);
						//$result = pg_get_result($dbconn);
						if(!$result)
						{
							// All gone badly so throw error and rollback
							$this->setErrorMessage("002", pg_last_error()."--- SQL was $stmt");
							pg_query($dbconn, "rollback;");
							return FALSE;
						}

					}

					// All gone well so commit transaction to db
					$result = pg_query($dbconn, "commit;");
				}
			}
			else
			{
				// Connection bad
				$this->seterrormessage("001", "error connecting to database");
				return false;
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
				$sql = "SELECT * FROM cpgdb.findvmchildren('".$this->getID()."', FALSE)";
				pg_send_query($dbconn, $sql);
				$result = pg_get_result($dbconn);

				// Check whether there are any vmeasurements that rely upon this one
				if(pg_num_rows($result)>0)
				{
					$this->setErrorMessage("903", "There are existing measurements that rely upon this measurement.  You must delete all child measurements before deleting the parent.");
					return FALSE;
				}
				else
				{
					// Retrieve data for record about to be deleted
					$this->setParamsFromDB($this->getID());

					if($this->vmeasurementOp->getValue()=="Direct")
					{
						// This is a direct measurement so we can delete the tblmeasurement entry and everything else should cascade delete
						$deleteSQL = "DELETE FROM tblmeasurement WHERE measurementid=".pg_escape_string($this->getMeasurementID()).";";
					}
					else
					{
						// This is a derived measurement so we just delete the tblvmeasurement record and let everything else cascade delete
						$deleteSQL = "DELETE FROM tblvmeasurement WHERE vmeasurementid=".pg_escape_string($this->getID()).";";
					}

					// Perform deletes using transactions
					$transaction = "BEGIN;".$deleteSQL;

					pg_send_query($dbconn, $transaction);
					$result = pg_get_result($dbconn);
					$status = pg_transaction_status($dbconn);
					if($status === PGSQL_TRANSACTION_INERROR)
					{
						// All gone badly so throw error and rollback
						$this->setErrorMessage("002", pg_result_error($result)."--- SQL was $transaction");
						pg_send_query($dbconn, "ROLLBACK;");
						return FALSE;
					}
					else
					{
						while($result==TRUE)
						{
							$result = pg_get_result($dbconn);
							//echo $result;
						}
						// All gone well so commit transaction to db
						pg_send_query($dbconn, "COMMIT;");
						return TRUE;
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
