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
require_once('inc/note.php');

class measurement extends measurementEntity implements IDBAccessor
{
    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        // Constructor for this class.
        $this->setVMeasurementOp(5);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDB($theID, $format="standard")
    {
        // Set the current objects parameters from the database
        global $dbconn;
        
        $this->setID($theID);

        // the uberquery - one query to rule them all?
        $sql = "SELECT vm.*, mc.*, su.username, op.name as opname, dt.datingtype, ".
		"m.radiusid, m.isreconciled, m.islegacycleaned, m.datingtypeid, m.datingerrorpositive, m.datingerrornegative, m.measuredbyid, ".
		"x(centroid(vmextent)), y(centroid(vmextent)), xmin(vmextent), xmax(vmextent), ymin(vmextent), ymax(vmextent) ".
		"FROM tblvmeasurement vm ".
		"INNER JOIN tlkpvmeasurementop op ON vm.vmeasurementopid=op.vmeasurementopid ".
		"LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid ".
		"LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid=mc.vmeasurementid ".
		"LEFT JOIN tlkpdatingtype dt ON m.datingtypeid=dt.datingtypeid ".
		"LEFT JOIN tblsecurityuser su ON vm.owneruserid=su.securityuserid ".
		"WHERE vm.vmeasurementid=".$this->getID();
	// the query that makes the server make the measurement
        $sql2 = "select * from cpgdb.getvmeasurementresult('".$this->getID()."')";
        
	// the old query - here for posterity
        //$sql2 = "select tblvmeasurement.* from tblvmeasurement where vmeasurementid=".$this->vmeasurementID;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // Kludge city - the dbconn occassionally has results left on it from somewhere not sure
            // where so retrieve results before sending SQL otherwise you'll get a messy notice
            $result = pg_get_result($dbconn); 
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn); 

            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No match for measurement id=".$this->getID().". SQL was $sql");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                 
                //$this->vmeasurementResultID = $row['vmeasurementresultid'];
                //$this->vmeasurementOpID = $row['vmeasurementopid'];
                //$this->vmeasurementOpParam = $row['vmeasurementparamid'];
               
                $this->setIsReconciled(fromPGtoPHPBool($row['isreconciled']));
                $this->setStartYear($row['startyear']);
                $this->setIsLegacyCleaned(fromPGtoPHPBool($row['islegacycleaned']));
                $this->setDatingErrorPositive($row['datingerrorpositive']);
                $this->setDatingErrorNegative($row['datingerrornegative']);
                $this->setCreatedTimestamp($row['createdtimestamp']);
                $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
                //$this->setMeasuredByID($row['measuredbyid']);

                // not this way... 
		/*
 	               $this->setDatingTypeID($row['datingtypeid']);
			$this->setMeasurementID();
	                $this->setVMeasurementOp($row2['vmeasurementopid']);
	                $this->setOwnerUserID($row2['owneruserid']);
		*/
				$this->setDatingType($row['datingtype']);
				$this->setMeasurementID($row['measurementid']);
				$this->vmeasurementOpID = $row['vmeasurementopid'];
				$this->setVMeasurementOp($row['opname']);
				$this->setOwnerUserID($row['owneruserid']);
		        $this->setMeasuredByID($row['measuredbyid']);
				$this->owner = $row['username'];

				// all the metacache stuff...
                $this->setReadingCount($row['readingcount']);
                $this->setMeasurementCount($row['measurementcount']);
                $this->setSummaryInfo($row['objectcode'], $row['objectcount'], $row['commontaxonname'], $row['taxoncount'], $row['prefix']);



                if($this->vmeasurementOp=='Index')
                {
                    // indexid for a sum
                    $this->setVMeasurementOpParam($this->getIndexNameFromParamID($row['vmeasurementopparameter']));
                }

                if($this->vmeasurementOp=='Crossdate')
                {
                    $this->setCrossdateParamsFromDB();
                }

                if (($format=="standard") || ($format=="comprehensive") || ($format=="summary") )
                {
                    $this->setReferencesFromDB();
                }

		// Deal with readings if we actually need them...
		if($format=="standard" || $format=="comprehensive") {
                    pg_send_query($dbconn, $sql2);
                    $result2 = pg_get_result($dbconn);
                    $row2 = pg_fetch_array($result2);

                    $this->vmeasurementResultID = $row2['vmeasurementresultid'];
                    $success = $this->setReadingsFromDB();
                    return $success;
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

    /**
     * @todo 
     *
     */
    function setParentsFromDB()
    {
    	global $dbconn;
    	$sql = "SELECT radiusid FROM tblmeasurement WHERE measurementid=?";
    	/*
    	        // Set up parent details
          	   	$this->parentEntityArray = array();
           		$myRadius = new radius();
           		$success = $myRadius->setParamsFromDB($row['radiusid']);
	            if($success===FALSE)
	            {
	            	trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage());
	            }  
               	array_push($this->parentEntityArray,$myRadius);
                
                $this->setCode($row['code']);
                $this->setComments($row['comments']);
*/
    }
    
    
    private function setCrossdateParamsFromDB()
    {
        global $dbconn;
        $sql = "select * from tblcrossdate where vmeasurementid=".$this->getID();
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $this->setMasterVMeasurementID($row['mastervmeasurementid']);
                $this->setFirstYear($row['startyear']);
                $this->setJustification($row['justification']);
                $this->setCertaintyLevel($row['confidence']);
            }
        }

    }

    private function setReadingsFromDB()
    {
        // Add all readings data to the object
        global $dbconn;

        // Empty the reading array in case we have data already in there
        unset($this->readingsArray);
        $this->readingsArray = array();

        $sql  = "select * from cpgdb.getvmeasurementreadingresult('".$this->vmeasurementResultID."') order by relyear asc";
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
                    $this->readingsArray[$row['relyear']] = array('reading' => $row['reading'], 
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
                $noteSQL = "SELECT tlkpreadingnote.*, tblreadingreadingnote.readingid FROM tlkpreadingnote, tblreadingreadingnote WHERE tblreadingreadingnote.readingid = ".$row['readingid'];
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
        //echo "setReferencesFromDB called";
        // Add any vmeasurements that the current measurement has been made from
        global $dbconn;
        
        
        $sql  = "select * from cpgdb.findvmparents('".$this->getID()."', 'false') where recursionlevel=0";
echo $sql;
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


    function setParamsFromParamsClass($paramsClass, $auth)
    {
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if ($paramsClass->radiusID!=NULL)              $this->setRadiusID($paramsClass->radiusID);
        if (isset($paramsClass->isReconciled))         $this->setIsReconciled($paramsClass->isReconciled);
        if (isset($paramsClass->startYear))            $this->setStartYear($paramsClass->startYear);
        if (isset($paramsClass->isLegacyCleaned))      $this->setIsLegacyCleaned($paramsClass->isLegacyCleaned);
        //if (isset($paramsClass->datingTypeID))        $this->setDatingTypeID($paramsClass->datingTypeID);
        if (isset($paramsClass->datingType))           $this->setDatingType($paramsClass->datingType);
        if (isset($paramsClass->datingErrorPositive))  $this->setDatingErrorPositive($paramsClass->datingErrorPositive);
        if (isset($paramsClass->datingErrorNegative))  $this->setDatingErrorNegative($paramsClass->datingErrorNegative);
        if (isset($paramsClass->name))                 $this->setName($paramsClass->name);
        if (isset($paramsClass->description))          $this->setDescription($paramsClass->description);
        if (isset($paramsClass->isPublished))          $this->setIsPublished($paramsClass->isPublished);
        if (isset($paramsClass->vmeasurementOp))       $this->setVMeasurementOp($paramsClass->vmeasurementOp);
        if (isset($paramsClass->vmeasurementOpParam))  $this->setVMeasurementOpParam($paramsClass->vmeasurementOpParam);
        if (sizeof($paramsClass->referencesArray)>0)   $this->setReferencesArray($paramsClass->referencesArray);
        if (isset($paramsClass->masterVMeasurementID)) $this->setMasterVMeasurementID($paramsClass->masterVMeasurementID);
        if (isset($paramsClass->justification))        $this->setJustification($paramsClass->justification);
        if (isset($paramsClass->certaintyLevel))       $this->setCertaintyLevel($paramsClass->certaintyLevel);
        if (isset($paramsClass->newStartYear))         $this->setNewStartYear($paramsClass->newStartYear);
        if (isset($paramsClass->readingsUnits))        $this->setUnits($paramsClass->readingsUnits);
        if (sizeof($paramsClass->readingsArray)>0)     $this->setReadingsArray($paramsClass->readingsArray);

        //echo "BEFORE\n";
        //print_r($this->readingsArray);
        
        // Convert readings provided by user to microns
        if (sizeof($paramsClass->readingsArray)>0) 
        {
            foreach($this->readingsArray as $key => $value)
            {
                $convValue = $this->unitsHandler($value['reading'], $this->readingsUnits, 'db-default');
                $this->readingsArray[$key]['reading'] = $convValue;
            } 
        }
        //echo "AFTER\n";
        //print_r($this->readingsArray);
        
        // Set Owner and Measurer IDs if specified otherwise use current user details
        if (isset($paramsClass->ownerUserID))
        {
            $this->setOwnerUserID($paramsClass->ownerUserID);
        }
        else
        {
            $this->setOwnerUserID($auth->getID());
        }
        if (isset($paramsClass->measuredByID))
        {
            $this->setMeasuredByID($paramsClass->measuredByID);
        }
        else
        {
            $this->setMeasuredByID($auth->getID());
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
                        if(!is_numeric($reading['reading'])) 
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
                /**if( ($paramsObj->name==NULL) ) 
                {   
                    $this->setErrorMessage("902","Missing parameter - a new measurement requires the name parameter.");
                    return false;
                }*/
                if(($paramsObj->readingsArray) && ($paramsObj->getFirstYear()== NULL) && ($paramsObj->datingTypeID==1))
                {
                    $this->setErrorMessage("902","Missing parameter - a new absolute direct measurement must include a startYear.");
                    return false;
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
                if(($paramsObj->referencesArray) && ($paramsObj->radiusID)) 
                {
                    $this->setErrorMessage("902","Invalid parameter - a new measurement based on other measurements cannot include a radiusID.");
                    return false;
                }
                if((sizeof($paramsObj->referencesArray)>0) && ($paramsObj->vmeasurementOp==NULL)) 
                {
                    $this->setErrorMessage("902","Missing parameter - a new measurement based on other measurements must include an operation.");
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
        // Add the id's of the current objects direct children from the database

        global $dbconn;

        $sql  = "select vmeasurementnoteid from tblvmeasurementvmeasurementnote where vmeasurementid=".$this->vmeasurementID;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->vmeasurementNoteArray, $row['vmeasurementnoteid']);
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

    function asXML($format='standard', $parts="full")
    {

        // Only direct measurements can have comprehensive format so overide if necessary
        if( ($format=='comprehensive') && ($this->vmeasurementOp!='Direct'))
        {
            $format = 'standard';
        }

        switch($format)
        {
        case "comprehensive":
            require_once('site.php');
            require_once('subSite.php');
            require_once('tree.php');
            require_once('specimen.php');
            require_once('radius.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid, tbltree.treeid, tblspecimen.specimenid, tblradius.radiusid, tblmeasurement.measurementid, tblvmeasurement.vmeasurementid 
                FROM tblsubsite 
                INNER JOIN tbltree ON tblsubsite.subsiteid=tbltree.subsiteid
                INNER JOIN tblspecimen ON tbltree.treeid=tblspecimen.treeid
                INNER JOIN tblradius ON tblspecimen.specimenid = tblradius.specimenid
                INNER JOIN tblmeasurement ON tblradius.radiusid = tblmeasurement.radiusid
                INNER JOIN tblvmeasurement ON tblmeasurement.measurementid=tblvmeasurement.measurementid
                where tblvmeasurement.vmeasurementid='".$this->vmeasurementID."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for measurement id=".$this->vmeasurementID);
                    return FALSE;
                }
                else
                {
                    $row = pg_fetch_array($result);

                    $mySite = new site();
                    $mySubSite = new subSite();
                    $myTree = new tree();
                    $mySpecimen = new specimen();
                    $myRadius = new radius();

                    $success = $mySite->setParamsFromDB($row['siteid']);
                    if($success===FALSE)
                    {
                        trigger_error($mySite->getLastErrorCode().$mySite->getLastErrorMessage());
                    }
                    
                    $success = $mySubSite->setParamsFromDB($row['subsiteid']);
                    if($success===FALSE)
                    {
                        trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage());
                    }
                    
                    $success = $myTree->setParamsFromDB($row['treeid']);
                    if($success===FALSE)
                    {
                        trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage());
                    }
                    
                    $success = $mySpecimen->setParamsFromDB($row['specimenid']);
                    if($success===FALSE)
                    {
                        trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage());
                    }
                    
                    $success = $myRadius->setParamsFromDB($row['radiusid']);
                    if($success===FALSE)
                    {
                        trigger_error($myRadius->getLastErrorCode().$myRadius->getLastErrorMessage());
                    }

                    $xml = $mySite->asXML("summary", "beginning");
                    $xml.= $mySubSite->asXML("summary", "beginning");
                    $xml.= $myTree->asXML("summary", "beginning");
                    $xml.= $mySpecimen->asXML("summary", "beginning");
                    $xml.= $myRadius->asXML("summary", "beginning");
                    $xml.= $this->_asXML("standard", "all");
                    $xml.= $myRadius->asXML("summary", "end");
                    $xml.= $mySpecimen->asXML("summary", "end");
                    $xml.= $myTree->asXML("summary", "end");
                    $xml.= $mySubSite->asXML("summary", "end");
                    $xml.= $mySite->asXML("summary", "end");
                }
            }
            return $xml;
        
        case "standard":
            return $this->_asXML($format, $parts);

        case "summary":
            return $this->_asXML($format, $parts);

        case "minimal":
            return $this->_asXML($format, $parts);

        
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary' or 'comprehensive'");
            return false;
        }
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
            $xml.= "<tridas:measurementSeries>";
            $xml.= "<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>";
            //$xml.= getResourceLinkTag("measurement", $this->vmeasurementID);

            // Include map reference tag if appropriate
            //if( (isset($this->centroidLat)) && (isset($this->centroidLong)) )
            //{
            //    $xml.= getResourceLinkTag("measurement", $this->vmeasurementID, "map");
            //}

            // Include permissions details if requested            
            $xml .= $this->getPermissionsXML();
            
            //if($format!="minimal") $xml.= "<metadata>\n";
            if($this->getCode()!=NULL)                  $xml.= "<tridas:genericField type=\"code\">".dbHelper::escapeXMLChars($this->getCode())."</tridas:genericField>\n";


            // Only output the remainder of the data if we're not using the 'minimal' format
            if ($format!="minimal")
            {
            	if($this->getAnalyst()!=NULL)				$xml.= "<tridas:analyst>".$this->getAnalyst()."</tridas:analyst>\n";
         		if($this->getDendrochronologist()!=NULL)	$xml.= "<tridas:dendrochronologist>".$this->getDendrochronologist()."</trodas:dendrochronologist>\n";
         		if($this->getMeasuringMethod()!=NULL)		$xml.= "<tridas:measuringMethod>".$this->getMeasuringMethod()."</tridas:measuringMethod>\n";
         		if($this->getMeasuringDate()!=NULL) 		$xml.= "<tridas:measuringDate>".$this->getMeasuringDate()."</tridas:measuringDate>\n";
         		if($this->getVariable()!=NULL)				$xml.= "<tridas:variable>".$this->getVariable()."</tridas:variable>";
         		if($this->getUnits()!=NULL)
         		{
         													$xml.= "<tridas:units factor=\"".$this->getUnitsPower()."\" system=\"SI\">\n";
         													$xml.= "<tridas:unit>".$this->getUnits()."</tridas:unit>\n";
         													$xml.= "</tridas:units>\n";
         		}
         		
         		if($this->getComments()!=NULL)				$xml.= "<tridas:comments>".$this->getComments()."</tridas:comments>\n";
         		if($this->getUsage()!=NULL)					$xml.= "<tridas:usage>".$this->getUsage()."</tridas:usage>\n";
         		if($this->getUsageComments()!=NULL)			$xml.= "<tridas:usageComments>".$this->getUsageComments()."</tridas:usageComments>\n";
            	
            	
                if($this->getIsReconciled()!=NULL)    		$xml.= "<tridas:genericField type=\"isReconciled\">".dbHelper::fromPHPtoStringBool($this->isReconciled)."</tridas:genericField>\n";

                											$xml.="<tridas:interpretation>\n";
                if($this->getFirstYear()!=NULL)				$xml.="<tridas:firstYear>".$this->getFirstYear()."</tridas:firstYear>\n";
                if($this->getSproutYear()!=NULL)			$xml.="<tridas:sproutYear>".$this->getSproutYear()."</tridas:sproutYear>\n";
                if($this->getDeathYear()!=NULL)				$xml.="<tridas:deathYear>".$this->getDeathYear()."</tridas:deathYear>\n";
                if($this->getProvenance()!=NULL)			$xml.="<tridas:provenance>".$this->getProvenance()."</tridas:provenance>\n";
                											$xml.="</tridas:interpretation>\n";
                
                								
                
                
                $xml.="<dating ";
                if(isset($this->startYear))             $xml.= "startYear=\"".$this->startYear."\" ";
                if(isset($this->readingCount))          $xml.= "count=\"".$this->readingCount."\" ";
                if(isset($this->datingType))            $xml.= "type=\"".$this->datingType."\" ";
                if(isset($this->datingErrorPositive))   $xml.= "positiveError=\"".$this->datingErrorPositive."\" ";
                if(isset($this->datingErrorNegative))   $xml.= "negativeError=\"".$this->datingErrorNegative."\" ";
                $xml.="/>";

                if(isset($this->isLegacyCleaned))       $xml.= "<isLegacyCleaned>".fromPHPtoStringBool($this->isLegacyCleaned)."</isLegacyCleaned>\n";
                if(isset($this->measuredByID))          $xml.= "<measuredBy id=\"".$this->measuredByID."\">".dbHelper::escapeXMLChars($this->measuredBy)."</measuredBy>\n";
                if(isset($this->ownerUserID))           $xml.= "<owner id=\"".$this->ownerUserID."\">".dbHelper::escapeXMLChars($this->owner)."</owner>\n";
                if(isset($this->description))           $xml.= "<description>".dbHelper::escapeXMLChars($this->description)."</description>\n";
                if(isset($this->isPublished))           $xml.= "<isPublished>".fromPHPtoStringBool($this->isPublished)."</isPublished>\n";
                if(isset($this->vmeasurementOp))
                {
                    // Include operation details if applicable
                    if(isset($this->vmeasurementOpParam))
                    {
                        $xml.= "<operation parameter=\"".$this->getIndexNameFromParamID($this->vmeasurementOpParam)."\">".strtolower($this->vmeasurementOp)."</operation>\n";
                    }
                    else
                    {
                        $xml.= "<operation>".strtolower($this->vmeasurementOp)."</operation>\n";
                    }
                }
                
                if($this->vmeasurementOp=="Crossdate")
                {
                    // Include crossdate info if applicable
                    $xml.= "<crossdate>\n";
                    $xml.= "<basedOn><measurement id=\"".$this->masterVMeasurementID."\"/></basedOn>\n";
                    $xml.= "<startYear>".$this->startYear."</startYear>\n";
                    $xml.= "<certaintyLevel>".$this->certaintyLevel."</certaintyLevel>\n";
                    $xml.= "<justification>".escapeXMLChars($this->justification)."</justification>\n";
                    $xml.= "</crossdate>\n";
                }
                
                if(isset($this->createdTimeStamp))      $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                if(isset($this->lastModifiedTimeStamp)) $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";
                if( (isset($this->minLat)) && (isset($this->minLong)) && (isset($this->maxLat)) && (isset($this->maxLong)))
                {
                    $xml.= "<extent minLat=\"".$this->minLat."\" maxLat=\"".$this->maxLat."\" minLong=\"".$this->minLong."\" maxLong=\"".$this->maxLong."\" centroidLat=\"".$this->centroidLat."\" centroidLong=\"".$this->centroidLong."\" />";
                }

		// show summary information in standard and summary modes
		if($format=="summary" || $format=="standard") {
                    // Return special summary section
                    $xml.="<summary>";
                    $xml.="<labPrefix>".dbHelper::escapeXMLChars($this->labPrefix)."</labPrefix>\n";
                    $xml.="<fullLabCode>".dbHelper::escapeXMLChars($this->fullLabCode)."</fullLabCode>\n";
                    $xml.="<taxon count=\"".$this->summaryTaxonCount."\" commonAncestor=\"".$this->summaryTaxonName."\"/>\n";
                    $xml.="<site count=\"".$this->summarySiteCount."\" ";
                    if($this->summarySiteCount=1) $xml.="siteCode=\"".$this->summarySiteCode."\"/>\n";
                    if($this->measurementCount!=NULL) $xml.="<measurement count=\"".$this->measurementCount."\"/>";
                    $xml.="</summary>";
		}

                // Using 'summary' format so just give minimal XML for all references and nothing else
                if($format=="summary")
                {
                    $xml.= "</tridas:measurementSeries>\n";
                    return $xml;
                }

                // Standard or Comprehensive format so give the whole lot
                else
                {
                    // Include all readings 
                    if ($this->readingsArray)
                    {
                        // Initially set yearvalue to 1001 default
                        if ($this->datingType=='Relative')
                        {
                            $yearvalue = 1001;
                        }
                        else
                        {
                            if($this->startYear==NULL)
                            {
                                $this->setErrorMessage(667, "Start year missing from absolute or absolute with error measurement.  You shouldn't have been able to get this far!");
                                return false;    
                            }
                            else
                            {
                                $yearvalue = $this->startYear;
                            }
                        }


                        $xml.="<tridas:values>\n";
                        foreach($this->readingsArray as $key => $value)
                        {
                            // Calculate absolute year where possible
                            if ($this->datingType!='Relative')
                            {
                                if($yearvalue==0)
                                {
                                    // If year value is 0 increment to 1 as there is no such year as 0bc/ad
                                    $yearvalue = 1;
                                }
                            }

                            $xml.="<tridas:value index=\"nr".$yearvalue."\" ";
                            //if (!($value['wjinc'] === NULL && $value['wjdec'] === NULL))
                            //{
                            //    $xml.="weiserjahre=\"".$value['wjinc']."/".$value['wjdec']."\" ";
                            //}
                            //$xml .="count=\"".$value['count']."\" value=\"".$this->unitsHandler($value['reading'], "db-default", "ws-default")."\">";
                         	$xml.="value=\"".$this->unitsHandler($value['reading'], "db-default", "ws-default")."\">";

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
                    }
                }    
            }
            $xml.= "</tridas:measurementSeries>\n";
            return $xml;
        }
        else
        {
            // Errors so returning false
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
        
        // Check for required parameters
        if($this->name == NULL) 
        {
            //$this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            //return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {

            // Check DB connection is OK before continueing
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // 
                // New record
                //
                if(($this->vmeasurementID == NULL))
                {
                    if($this->vmeasurementOp=='Direct')
                    {
                        // New direct measurement so create tblmeasurement record first
                        $sql = "insert into tblmeasurement  (  ";
                            if($this->radiusID)              $sql.= "radiusid, "; 
                            if($this->isReconciled!=NULL)    $sql.= "isreconciled, "; 
                            if($this->startYear)             $sql.= "startyear, "; 
                            if($this->isLegacyCleaned!=NULL) $sql.= "islegacycleaned, "; 
                            if($this->measuredByID)          $sql.= "measuredbyid, "; 
                            if($this->datingErrorPositive)   $sql.= "datingerrorpositive, "; 
                            if($this->datingErrorNegative)   $sql.= "datingerrornegative, "; 
                            if($this->datingTypeID)          $sql.= "datingtypeid, "; 
                        // Trim off trailing space and comma
                        $sql = substr($sql, 0, -2);
                        $sql.=") values (";
                            if($this->radiusID)               $sql.= "'".$this->radiusID."', ";
                            if($this->isReconciled!=NULL)     $sql.= "'".fromPHPtoPGBool($this->isReconciled)."', ";
                            if($this->startYear)              $sql.=     $this->startYear.", ";
                            if($this->isLegacyCleaned!=NULL)  $sql.= "'".fromPHPtoPGBool($this->isLegacyCleaned)."', ";
                            if($this->measuredByID)           $sql.= "'".$this->measuredByID."', ";
                            if($this->datingErrorPositive)    $sql.= "'".$this->datingErrorPositive."', ";
                            if($this->datingErrorNegative)    $sql.= "'".$this->datingErrorNegative."', ";
                            if($this->datingTypeID)           $sql.= "'".$this->datingTypeID."', ";
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
                                $this->measurementID=$row['measurementid'];   
                            }
                        }    
                        
                        // Insert new readings
                        $relyear = 0;
                        foreach($this->readingsArray as $key => $value)
                        {
                            // First loop through the readingsArray and create insert statement for tblreading table
                            $insertSQL = "insert into tblreading (measurementid, relyear, reading) values (".$this->measurementID.", ".$relyear.", ".$value['reading'].")";
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
                                        $insertSQL = "INSERT INTO tblreadingreadingnote (readingid, readingnoteid) value(".$thisReadingID.", ".$noteKey.")";
                                    
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


                    /*
                        -- CreateNewVMeasurement(VMeasurementOp, VMeasurementOpParameter, OwnerUserID, Name,
                        --                       Description, MeasurementID, Constituents)
                        -- VMeasurementOp - Varchar - From tlkpVMeasurementOp
                        -- VMeasurementOpParameter - Integer - Must be specified for REDATE or INDEX; otherwise NULL
                        -- Name - Varchar - Must be specified
                        -- Description - Varchar - May be NULL
                        -- MeasurementID - Integer - For direct only; the measurement derived from.
                        -- Constituents - Array of VMeasurementID - Must be NULL for DIRECT type, an array of one value for any type
                        --                other than SUM and DIRECT, and an array of one or more values for SUM
                        -- RETURNS: A new VMeasurementID
                    */

                    $sql = "select * from cpgdb.createnewvmeasurement(";
                    $sql.= "'".$this->vmeasurementOp."', ";
                    if($this->vmeasurementOpParam)      { $sql.= "'".$this->vmeasurementOpParam."', "; } else { $sql.= "NULL, "; }
                    $sql.= $this->ownerUserID.", ";
                    $sql.= "'".$this->name."', ";
                    if($this->description)              { $sql.= "'".$this->description."', ";         } else { $sql.= "NULL, "; }
                    if($this->vmeasurementOp=='Direct') { $sql.= $this->measurementID.", ";            } else { $sql.= "NULL, "; }
                    if($this->vmeasurementOp!='Direct') 
                    { 
                        $sql.= "ARRAY[";
                        foreach($this->referencesArray as $value)
                        {
                            $sql.= $value.", ";
                        }
                        $sql = substr($sql, 0, -2)."])";            
                    } 
                    else 
                    { 
                        $sql.= "NULL) "; 
                    }

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
                        $sql = "select cpgdb.finishCrossdate(".$localVMID.", ".$this->newStartYear.", ".$this->masterVMeasurementID.", '".$this->justification."', ".$this->certaintyLevel.")";
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

                    // First update the tblvmeasurement table
                    $updateSQL2 = NULL;
                    $updateSQL = "update tblvmeasurement set ";
                    $insertSQL="";
                    if($this->name)               $updateSQL.= "name = '".$this->name."', ";
                    if($this->description)        $updateSQL.= "description = '".$this->description."', ";
                    if(isset($this->isPublished)) $updateSQL.= "ispublished='".fromPHPtoPGBool($this->isPublished)."' ,";
                    if($this->ownerUserID)        $updateSQL.= "owneruserid = ".$this->ownerUserID.", ";
                    $updateSQL = substr($updateSQL, 0 , -2);
                    $updateSQL.= " where vmeasurementid=".$this->vmeasurementID."; ";

                    // Then update references or readings depending on whether the measurement is direct or not
                    if ($this->vmeasurementOp!=="Direct")
                    {
                        // Update references to other vmeasurements
                        $deleteSQL = "delete from tblvmeasurementgroup where vmeasurementid=".$this->vmeasurementID."; ";
                        $relyear = 0;
                        foreach($this->referencesArray as $key => $value)
                        {
                            $insertSQL .= "insert into tblvmeasurementgroup (vmeasurementid, membervmeasurementid) values (".$this->vmeasurementID.", ".$value."); ";
                            $relyear++;
                        }
                    }
                    elseif($this->vmeasurementOp=="Direct")
                    {
                        // Update the tblmeasurement table
                        $updateSQL2.= "update tblmeasurement set ";
                        if(isset($this->radiusID))            $updateSQL2.= "radiusid = ".$this->radiusID.", ";
                        if(isset($this->isReconciled))        $updateSQL2.= "isreconciled='".fromPHPtoPGBool($this->isReconciled)."', ";
                        if(isset($this->startYear))           $updateSQL2.= "startyear = ".$this->startYear.", ";
                        if(isset($this->isLegacyCleaned))     $updateSQL2.= "islegacycleaned='".fromPHPtoPGBool($this->isLegacyCleaned)."', ";
                        if(isset($this->measuredByID))        $updateSQL2.= "measuredbyid = ".$this->measuredByID.", ";
                        if(isset($this->datingTypeID))        $updateSQL2.= "datingtypeid = ".$this->datingTypeID.", ";
                        if(isset($this->datingerrorpositive)) $updateSQL2.= "datingerrorpositive = ".$this->datingerrorpositive.", ";
                        if(isset($this->datingerrornegative)) $updateSQL2.= "datingerrornegative = ".$this->datingerrornegative.", ";
                        $updateSQL2 = substr($updateSQL2, 0 , -2);
                        $updateSQL2.= " where measurementid=".$this->measurementID."; ";

                        // Update readings
                        $deleteSQL = "delete from tblreading where measurementid=".$this->measurementID."; ";
                        $relyear = 0;
                        foreach($this->readingsArray as $key => $value)
                        {
                            $insertSQL .= "insert into tblreading (measurementid, relyear, reading) values (".$this->measurementID.", ".$relyear.", ".$value['reading']."); ";
                            $relyear++;
                        }
                    }
                    
                    // Perform query using transactions so that if anything goes wrong we can roll back
                    $transaction = array("begin;", $deleteSQL, $insertSQL, $updateSQL, $updateSQL2);

                    foreach($transaction as $stmt)
                    {
                        $result = pg_query($dbconn, $stmt);
                        //$result = pg_get_result($dbconn);
                        if($result===FALSE)
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
        if($this->vmeasurementID == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'id' field is required.");
            return FALSE;
        }
        
        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $sql = "select * from cpgdb.findvmchildren('".$this->vmeasurementID."', FALSE)";
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
                    $this->setParamsFromDB($this->vmeasurementID);

                    if($this->vmeasurementOp=="Direct")
                    {
                        // This is a direct measurement so we can delete the tblmeasurement entry and everything else should cascade delete
                        $deleteSQL = "delete from tblmeasurement where measurementid=".$this->measurementID.";"; 
                    }
                    else
                    {
                        // This is a derived measurement so we just delete the tblvmeasurement record and let everything else cascade delete
                        $deleteSQL = "delete from tblvmeasurement where vmeasurementid=".$this->vmeasurementID.";"; 
                    }

                    // Perform deletes using transactions
                    $transaction = "begin;".$deleteSQL;

                    pg_send_query($dbconn, $transaction);
                    $result = pg_get_result($dbconn);
                    $status = pg_transaction_status($dbconn);
                    if($status === PGSQL_TRANSACTION_INERROR)
                    {
                        // All gone badly so throw error and rollback
                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $transaction");
                        pg_send_query($dbconn, "rollback;");
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
                        pg_send_query($dbconn, "commit;");
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
