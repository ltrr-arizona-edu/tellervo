<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('dbhelper.php');
require_once('inc/note.php');

class series 
{
    var $seriesID = NULL;
    var $vseriesID = NULL;
    var $vseriesResultID = NULL;
    var $vseriesOpID = 5;
    var $vseriesOpParam = NULL;
    var $vseriesOp = "Direct";
    var $vseriesUnits = NULL;
    var $indexType = NULL;
    var $radiusID = NULL;
    var $isReconciled = FALSE;
    var $startYear = NULL;
    var $isLegacyCleaned = NULL;
    var $datingTypeID = 3;
    var $datingType = "Relative";
    var $datingErrorPositive = NULL;
    var $datingErrorNegative = NULL;
    var $isPublished = NULL;
    var $measuredByID = NULL;
    var $measuredBy = NULL;
    var $ownerUserID = NULL;
    var $owner = NULL;
    var $valuesArray = array();
    var $referencesArray = array();
    var $vseriesNoteArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
    var $name = NULL;
    var $valueCount = NULL;
    var $seriesCount = NULL;
    var $description = NULL;
    var $centroidLat = NULL;
    var $centroidLong = NULL;
    var $minLat = NULL;
    var $maxLat = NULL;
    var $minLong = NULL;
    var $maxLong = NULL;

    var $summarySiteCode = NULL;
    var $summarySiteCount = NULL;
    var $summaryTaxonName = NULL;
    var $summaryTaxonCount = NULL;
    var $fullLabCode = NULL;
    
    var $includePermissions = FALSE;
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;

    var $parentXMLTag = "series"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        // Constructor for this class.
        $this->vseriesOpID = 5;
    }

    /***********/
    /* SETTERS */
    /***********/

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function setParamsFromDB($theID, $format="standard")
    {
        // Set the current objects parameters from the database
        global $dbconn;
        
        $this->vseriesID=$theID;
        $sql = "select * from cpgdb.getvseriesresult('$theID')";
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
                $this->setErrorMessage("903", "No match for series id=".$theID);
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->name = $row['name'];
                $this->description = $row['description'];
                $this->vseriesID = $row['vseriesid'];
                $this->vseriesResultID = $row['vseriesresultid'];
                //$this->vseriesOpID = $row['vseriesopid'];
                //$this->vseriesOpParam = $row['vseriesparamid'];
                $this->radiusID = $row['radiusid'];
                $this->isReconciled = fromPGtoPHPBool($row['isreconciled']);
                $this->startYear = $row['startyear'];
                $this->isLegacyCleaned = fromPGtoPHPBool($row['islegacycleaned']);
                $this->datingErrorPositive = $row['datingerrorpositive'];
                $this->datingErrorNegative = $row['datingerrornegative'];
                $this->createdTimeStamp = $row['createdtimestamp'];
                $this->lastModifiedTimeStamp = $row['lastmodifiedtimestamp'];
                $this->setDatingTypeID($row['datingtypeid']);
                //$this->setMeasuredByID($row['measuredbyid']);

                // Get more parameters directly from tblvseries
                $sql2 = "select tblvseries.* from tblvseries where vseriesid=".$this->vseriesID;
                pg_send_query($dbconn, $sql2);
                $result2 = pg_get_result($dbconn);
                $row2 = pg_fetch_array($result2);
                $this->setMeasurementID();
                $this->setVMeasurementOp($row2['vseriesopid']);
                $this->setOwnerUserID($row2['owneruserid']);

                if($this->vseriesOp=='Index')
                {
                    // indexid for a sum
                    $this->setVMeasurementOpParam($this->getIndexNameFromParamID($row2['vseriesopparameter']));
                }
                else
                {
                    // Year for a redate
                    $this->setVMeasurementOpParam($row2['vseriesopparameter']);
                }

                if (($format=="standard") || ($format=="comprehensive") || ($format=="summary") )
                {
                    $sql = "select tblvseriesmetacache.*, x(centroid(vmextent)), y(centroid(vmextent)), xmin(vmextent), xmax(vmextent), ymin(vmextent), ymax(vmextent) from tblvseriesmetacache where vseriesid=".$this->vseriesID;
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    $row = pg_fetch_array($result);
                    $this->minLat = $row['ymin'];
                    $this->maxLat = $row['ymax'];
                    $this->minLong = $row['xmin'];
                    $this->maxLong = $row['xmax'];
                    $this->centroidLat = $row['y'];
                    $this->centroidLong = $row['x'];
                    $this->valueCount = $row['valuecount'];
                    $this->seriesCount = $row['seriescount'];
                    $this->summarySiteCode = $row['sitecode'];
                    $this->summarySiteCount = $row['sitecount'];
                    $this->summaryTaxonName = $row['commontaxonname'];
                    $this->summaryTaxonCount = $row['taxoncount'];
                    $this->fullLabCode = $row['label'];

                    $this->setReadingsFromDB();
                    $this->setReferencesFromDB();
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

    private function setReadingsFromDB()
    {
        // Add all values data to the object
        global $dbconn;

        $sql  = "select * from cpgdb.getvseriesvalueresult('".$this->vseriesResultID."') order by relyear asc";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all value values to array 
                $this->valuesArray[$row['relyear']] = array('value' => $row['value'], 
                                                              'wjinc' => $row['wjinc'], 
                                                              'wjdec' => $row['wjdec'], 
                                                              'count' => $row['count'],
                                                              'notesArray' => array()
                                                             );
            }

            // If this is a direct series then add any notes as a subarray
            if($row['valueid'])
            {
                $noteSQL = "SELECT tlkpvaluenote.*, tblvaluevaluenote.valueid FROM tlkpvaluenote, tblvaluevaluenote WHERE tblvaluevaluenote.valueid = ".$row['valueid'];
                $noteResult = pg_query($dbconn, $noteSQL);
                while($noteRow = pg_fetch_array($noteResult))
                {
                    // Get all value values to array 
                    array_push($this->valuesArray[$row['relyear']][notesArray], $noteRow['valuenoteid']); 
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
        // Add any vseriess that the current series has been made from
        global $dbconn;
        
        $sql  = "select * from cpgdb.findvmparents('".$this->vseriesID."', 'false') where recursionlevel=0";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $this->referencesArray = array();
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get add all value values to array 
                array_push($this->referencesArray, $row['vseriesid']);
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
        if ($paramsClass->radiusID!=NULL)             $this->setRadiusID($paramsClass->radiusID);
        if (isset($paramsClass->isReconciled))        $this->setIsReconciled($paramsClass->isReconciled);
        if (isset($paramsClass->startYear))           $this->setStartYear($paramsClass->startYear);
        if (isset($paramsClass->isLegacyCleaned))     $this->setIsLegacyCleaned($paramsClass->isLegacyCleaned);
        //if (isset($paramsClass->datingTypeID))        $this->setDatingTypeID($paramsClass->datingTypeID);
        if (isset($paramsClass->datingType))          $this->setDatingType($paramsClass->datingType);
        if (isset($paramsClass->datingErrorPositive)) $this->setDatingErrorPositive($paramsClass->datingErrorPositive);
        if (isset($paramsClass->datingErrorNegative)) $this->setDatingErrorNegative($paramsClass->datingErrorNegative);
        if (isset($paramsClass->name))                $this->setName($paramsClass->name);
        if (isset($paramsClass->description))         $this->setDescription($paramsClass->description);
        if (isset($paramsClass->isPublished))         $this->setIsPublished($paramsClass->isPublished);
        if (isset($paramsClass->vseriesOp))      $this->setVMeasurementOp($paramsClass->vseriesOp);
        if (isset($paramsClass->vseriesOpParam)) $this->setVMeasurementOpParam($paramsClass->vseriesOpParam);
        if (sizeof($paramsClass->valuesArray)>0)    $this->setReadingsArray($paramsClass->valuesArray);
        if (sizeof($paramsClass->referencesArray)>0)  $this->setReferencesArray($paramsClass->referencesArray);
        
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


    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a series.");
                    return false;
                }
                if( (gettype($paramsObj->id)!="integer") && ($paramsObj->id!=NULL) ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer.  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating series.");
                    return false;
                }
                if(($paramsObj->valuesArray) && (count($paramsObj->valuesArray)< 10)) 
                {
                    $this->setErrorMessage("902","Invalid parameter - You have only supplied ".count($paramsObj->valuesArray)." values.  Minimum number required is 10.");
                    return false;
                }
                if($paramsObj->valuesArray)
                {
                    foreach ($paramsObj->valuesArray as $value)
                    {
                        if(!is_numeric($value['value'])) 
                        {
                            $this->setErrorMessage("902","Invalid parameter - All your values must be numbers.");
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
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a series.");
                    return false;
                }
                return true;

            case "create":
                if(($paramsObj->referencesArray == NULL) && ($paramsObj->valuesArray == NULL)) 
                {
                    $this->setErrorMessage("902","Missing parameter - you must specify either references or values when creating a new series.");
                    return false;
                }
                if(($paramsObj->valuesArray) && ($paramsObj->radiusID== NULL))
                {
                    $this->setErrorMessage("902","Missing parameter - a new direct series must include a radiusID.");
                    return false;
                }
                if( ($paramsObj->name==NULL) ) 
                {   
                    $this->setErrorMessage("902","Missing parameter - a new series requires the name parameter.");
                    return false;
                }
                if(($paramsObj->valuesArray) && ($paramsObj->startYear== NULL) && ($paramsObj->datingTypeID==1))
                {
                    $this->setErrorMessage("902","Missing parameter - a new absolute direct series must include a startYear.");
                    return false;
                }
                /*if(($paramsObj->valuesArray) && ($paramsObj->datingTypeID==NULL))
                {
                    $this->setErrorMessage("902","Missing parameter - a new direct series must include a datingTypeID.");
                    return false;
                }*/
                if(($paramsObj->valuesArray) && (count($paramsObj->valuesArray)< 10)) 
                {
                    $this->setErrorMessage("902","Invalid parameter - You have only supplied ".count($paramsObj->valuesArray)." values.  Minimum number required is 10.", E_USER_ERROR);
                    return false;
                }
                if(($paramsObj->referencesArray) && ($paramsObj->radiusID)) 
                {
                    $this->setErrorMessage("902","Invalid parameter - a new series based on other seriess cannot include a radiusID.");
                    return false;
                }
                if((sizeof($paramsObj->referencesArray)>0) && ($paramsObj->vseriesOp==NULL)) 
                {
                    $this->setErrorMessage("902","Missing parameter - a new series based on other seriess must include an operation.");
                    return false;
                }
                if( (!(isset($paramsObj->referencesArray))) && (isset($paramsObj->vseriesOp)) )
                {
                    $this->setErrorMessage("902","Missing parameter - you have included an operation which suggests you are creating a new series based on others. However, you have not specified any references to other seriess.");
                    return false;
                }
                   
                return true;

            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }

    function setOwnerUserID($theOwnerUserID)
    {
        if($theOwnerUserID)
        {
            //Only run if valid parameter has been provided
            global $dbconn;

            $this->ownerUserID = $theOwnerUserID;
            
            $sql  = "select username from tblsecurityuser where securityuserid=".$this->ownerUserID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->owner = $row['username'];
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
        return FALSE;
    }

    function setVMeasurementOp($theVMeasurementOp)
    {    
        if(is_numeric($theVMeasurementOp))
        {
            // ID provided
            global $dbconn;

            $this->vseriesOpID = $theVMeasurementOp;
            
            $sql  = "select name from tlkpvseriesop where vseriesopid=".$this->vseriesOpID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->vseriesOp = $row['name'];
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
        elseif(is_string($theVMeasurementOp))
        {
            global $dbconn;

            $this->vseriesOp = ucfirst(strtolower($theVMeasurementOp));
            
            $sql  = "select vseriesopid from tlkpvseriesop where name ilike'".$theVMeasurementOp."'";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->vseriesOpID = $row['vseriesopid'];
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
        else
        {
            return FALSE;
        }
    }

    function setVMeasurementOpParam($theParam)
    {
        global $dbconn;
        if(is_numeric($theParam))
        {
            // Param is numeric - probably a year for use in redating
            $this->vseriesOpParam = $theParam;
            return true;
        }
        elseif(is_string($theParam))
        {
            // Param is string - probably index type
            $sql  = "select indexid from tlkpindextype where indexname ilike '$theParam'";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->vseriesOpParam = $row['indexid'];
                }
            }
            else
            {
                // Connection bad
                $this->setErrorMessage("001", "Error connecting to database for index type lookup");
                return FALSE;
            }
            
        } 
        elseif($theParam==NULL)
        {
            return TRUE;
        }
        else
        {
            // Shouldn't be here!!
            $this->setErrorMessage("667", "Program bug - series operation parameter not recognised");
            return FALSE;
        }
    }
    
    function setMeasurementID()
    {
        if($this->vseriesID)
        {
            //Only run if valid parameter has been provided
            global $dbconn;
            
            $sql  = "select seriesid from tblvseries where vseriesid=".$this->vseriesID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->seriesID = $row['seriesid'];
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
        return FALSE;
    }
    
    function setReadingsArray($theReadingsArray)
    {
        $this->valuesArray = $theReadingsArray;
    }
    
    function setReferencesArray($theReferencesArray)
    {
        $this->referencesArray = array();
        $this->referencesArray = $theReferencesArray;
    }
    
    function setRadiusID($theRadiusID)
    {
        $this->radiusID = $theRadiusID;
    }

    function setIsReconciled($isReconciled)
    {
        $this->isReconciled = fromStringtoPHPBool($isReconciled);
    }

    function setStartYear($theStartYear)
    {
        $this->startYear = $theStartYear;
    }

    function setIsLegacyCleaned($isLegacyCleaned)
    {
        $this->isLegacyCleaned = fromStringtoPHPBool($isLegacyCleaned);
    }
    
    function setMeasuredByID($theMeasuredByID)
    {
        if($theMeasuredByID)
        {
            global $dbconn;

            $this->measuredByID = $theMeasuredByID;
            
            $sql  = "select username from tblsecurityuser where securityuserid=".$this->measuredByID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->measuredBy = $row['username'];
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
        return FALSE;
    }
    
    function setDatingType($theDatingType)
    {
        if ($theDatingType)
        {
            global $dbconn;

            $this->datingType = $theDatingType;
            
            $sql  = "select datingtypeid from tlkpdatingtype where label='".ucfirst(strtolower($this->datingType))."'";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->datingTypeID = $row['datingtypeid'];
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
       return FALSE;
    }

    function setDatingTypeID($theDatingTypeID)
    {
        if ($theDatingTypeID)
        {
            global $dbconn;

            $this->datingTypeID = $theDatingTypeID;
            
            $sql  = "select label from tlkpdatingtype where datingtypeid=".$this->datingTypeID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->datingType = $row['label'];
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
        return FALSE;
     }


    function setDatingErrorPositive($theDatingErrorPositive)
    {
        $this->datingErrorPositive = $theDatingErrorPositive;
    }
    
    function setDatingErrorNegative($theDatingErrorNegative)
    {
        $this->datingErrorNegative = $theDatingErrorNegative;
    }

    function setName($theName)
    {
        $this->name = $theName;
    }
    
    function setDescription($theDescription)
    {
        $this->description = $theDescription;
    }

    function setIsPublished($isPublished)
    {
        $this->isPublished = fromStringtoPHPBool($isPublished);
    }

    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database

        global $dbconn;

        $sql  = "select vseriesnoteid from tblvseriesvseriesnote where vseriesid=".$this->vseriesID;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all element note id's for this element and store 
                array_push($this->vseriesNoteArray, $row['vseriesnoteid']);
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

    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, 'series', $this->vseriesID)";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            $row = pg_fetch_array($result);
            
            $this->canCreate = fromPGtoPHPBool($row['cancreate']);
            $this->canUpdate = fromPGtoPHPBool($row['canupdate']);
            $this->canDelete = fromPGtoPHPBool($row['candelete']);
            $this->includePermissions = TRUE;
    
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

    function getEndYear()
    {
        $length = count($this->valuesArray);
        return $this->startYear + $length;
    }

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

        // Only direct seriess can have comprehensive format so overide if necessary
        if( ($format=='comprehensive') && ($this->vseriesOp!='Direct'))
        {
            $format = 'standard';
        }

        switch($format)
        {
        case "comprehensive":
            require_once('site.php');
            require_once('subSite.php');
            require_once('element.php');
            require_once('sample.php');
            require_once('radius.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid, tblelement.elementid, tblsample.sampleid, tblradius.radiusid, tblseries.seriesid, tblvseries.vseriesid 
                FROM tblsubsite 
                INNER JOIN tblelement ON tblsubsite.subsiteid=tblelement.subsiteid
                INNER JOIN tblsample ON tblelement.elementid=tblsample.elementid
                INNER JOIN tblradius ON tblsample.sampleid = tblradius.sampleid
                INNER JOIN tblseries ON tblradius.radiusid = tblseries.radiusid
                INNER JOIN tblvseries ON tblseries.seriesid=tblvseries.seriesid
                where tblvseries.vseriesid='".$this->vseriesID."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for series id=".$this->vseriesID);
                    return FALSE;
                }
                else
                {
                    $row = pg_fetch_array($result);

                    $mySite = new site();
                    $mySubSite = new subSite();
                    $myTree = new element();
                    $mySpecimen = new sample();
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
                    
                    $success = $myTree->setParamsFromDB($row['elementid']);
                    if($success===FALSE)
                    {
                        trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage());
                    }
                    
                    $success = $mySpecimen->setParamsFromDB($row['sampleid']);
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
        //      Default = 2 - which means the current series and its immediate parents
        // $format = the type of XML output
        //      standard = all XML including notes and values
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
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml.= "<series ";
            $xml.= "id=\"".$this->vseriesID."\">";
            $xml.= getResourceLinkTag("series", $this->vseriesID);

            // Include map reference tag if appropriate
            if( (isset($this->centroidLat)) && (isset($this->centroidLong)) )
            {
                $xml.= getResourceLinkTag("series", $this->vseriesID, "map");
            }

            // Include permissions details if requested
            if($this->includePermissions===TRUE) 
            {
                $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->canCreate)."\" ";
                $xml.= "canUpdate=\"".fromPHPtoStringBool($this->canUpdate)."\" ";
                $xml.= "canDelete=\"".fromPHPtoStringBool($this->canDelete)."\" />\n";
            } 
            
            if($format!="minimal") $xml.= "<metadata>\n";
            if(isset($this->name))                  $xml.= "<name>".escapeXMLChars($this->name)."</name>\n";


            // Only output the remainder of the data if we're not using the 'minimal' format
            if ($format!="minimal")
            {
                if(isset($this->isReconciled))          $xml.= "<isReconciled>".fromPHPtoStringBool($this->isReconciled)."</isReconciled>\n";

                $xml.="<dating ";
                if(isset($this->startYear))             $xml.= "startYear=\"".$this->startYear."\" ";
                if(isset($this->valueCount))          $xml.= "count=\"".$this->valueCount."\" ";
                if(isset($this->datingType))            $xml.= "type=\"".$this->datingType."\" ";
                if(isset($this->datingErrorPositive))   $xml.= "positiveError=\"".$this->datingErrorPositive."\" ";
                if(isset($this->datingErrorNegative))   $xml.= "negativeError=\"".$this->datingErrorNegative."\" ";
                $xml.="/>";

                if(isset($this->isLegacyCleaned))       $xml.= "<isLegacyCleaned>".fromPHPtoStringBool($this->isLegacyCleaned)."</isLegacyCleaned>\n";
                if(isset($this->measuredByID))          $xml.= "<measuredBy id=\"".$this->measuredByID."\">".escapeXMLChars($this->measuredBy)."</measuredBy>\n";
                if(isset($this->ownerUserID))           $xml.= "<owner id=\"".$this->ownerUserID."\">".escapeXMLChars($this->owner)."</owner>\n";
                if(isset($this->description))           $xml.= "<description>".escapeXMLChars($this->description)."</description>\n";
                if(isset($this->isPublished))           $xml.= "<isPublished>".fromPHPtoStringBool($this->isPublished)."</isPublished>\n";
                if(isset($this->vseriesOp))
                {
                    if(isset($this->vseriesOpParam))
                    {
                                                        $xml.= "<operation parameter=\"".$this->getIndexNameFromParamID($this->vseriesOpParam)."\">".strtolower($this->vseriesOp)."</operation>\n";
                    }
                    else
                    {
                                                        $xml.= "<operation>".strtolower($this->vseriesOp)."</operation>\n";
                    }
                }
                if(isset($this->createdTimeStamp))      $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                if(isset($this->lastModifiedTimeStamp)) $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";
                if( (isset($this->minLat)) && (isset($this->minLong)) && (isset($this->maxLat)) && (isset($this->maxLong)))
                {
                    $xml.= "<extent minLat=\"".$this->minLat."\" maxLat=\"".$this->maxLat."\" minLong=\"".$this->minLong."\" maxLong=\"".$this->maxLong."\" centroidLat=\"".$this->centroidLat."\" centroidLong=\"".$this->centroidLong."\" />";
                }

                // Using 'summary' format so just give minimal XML for all references and nothing else
                if($format=="summary")
                {
                    // Return special summary section
                    $xml.="<parentSummary>";
                    $xml.="<fullLabCode>".$this->fullLabCode."</fullLabCode>\n";
                    $xml.="<taxon count=\"".$this->summaryTaxonCount."\" commonAncestor=\"".$this->summaryTaxonName."\"/>\n";
                    $xml.="<site count=\"".$this->summarySiteCount."\" ";
                    if($this->summarySiteCount=1) $xml.="siteCode=\"".$this->summarySiteCode."\"/>\n";
                    if($this->seriesCount!=NULL) $xml.="<series count=\"".$this->seriesCount."\"/>";
                    $xml.="</parentSummary>";

                    // Only look up references if recursion level is greater than 0
                    if($recurseLevel>0)
                    {
                        if(sizeof($this->referencesArray)>0)
                        {
                            $xml.="<references>";
                            foreach($this->referencesArray as $value)
                            {
                                $myReference = new series();
                                $success = $myReference->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$myReference->asXML("minimal", "all");
                                }
                                else
                                {
                                    $this->setErrorMessage($myReference->getLastErrorCode, $myReference->getLastErrorMessage);
                                }
                            }
                            $xml.="</references>";
                        }
                    }

                    $xml.="</metadata>\n";
                    $xml.= "</series>\n";
                    return $xml;
                }

                // Standard or Comprehensive format so give the whole lot
                else
                {
                
                    // Include site notes if present
                    if ($this->vseriesNoteArray)
                    {
                        $xml.= "<siteNotes>\n";
                        foreach($this->vseriesNoteArray as $value)
                        {
                            $myVMeasurementNote = new vseriesNote();
                            $success = $myVMeasurementNote->setParamsFromDB($value);

                            if($success)
                            {
                                $xml.=$myVMeasurementNote->asXML();
                            }
                            else
                            {
                                $this->setErrorMessage($myVMeasurementNote->getLastErrorCode, $myVMeasurementNote->getLastErrorMessage);
                            }
                        }
                        $xml.= "</siteNotes>\n";
                    }
                    
                    // Include all refences to other vseriess recursing if requested 
                    if ($this->referencesArray)
                    {
                        // Only look up references if recursion level is greater than 0
                        if($recurseLevel>0)
                        {
                            $xml.="<references>";
                            foreach($this->referencesArray as $value)
                            {
                                $myReference = new series();
                                $success = $myReference->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$myReference->asXML($recurseLevel, "summary");
                                }
                                else
                                {
                                    $this->setErrorMessage($myReference->getLastErrorCode, $myReference->getLastErrorMessage);
                                }
                            }
                            $xml.="</references>\n";
                        }
                    }
                    else
                    {
                        $xml.="<references>\n";
                        $xml.= getResourceLinkTag("radius", $this->radiusID);
                        $xml.="</references>\n";
                    }
                    
                    $xml.= "</metadata>\n";

                    // Include all values 
                    if ($this->valuesArray)
                    {
                        $xml.="<values type=\"annual\" units=\"-6\">\n";
                        foreach($this->valuesArray as $key => $value)
                        {
                            // Calculate absolute year where possible
                            if ($this->startYear)
                            {
                                if($this->startYear+$key  >= 0)
                                {
                                    // Add 1 to year to cope with BC/AD transition issue (no 0bc/ad)
                                    $yearvalue = $key;
                                }
                                else
                                {
                                    // Date is BC so fudge not required
                                    $yearvalue = $key - 1;
                                }
                            }
                            else
                            {
                                // Years are relative
                                $yearvalue = $key;
                            }

                            $xml.="<value year=\"".$yearvalue."\" ";
                            if ($value['wjinc'] || $value['wjdec'])
                            {
                                $xml.="weiserjahre=\"".$value['wjinc']."/".$value['wjdec']."\" ";
                            }
                            $xml .="count=\"".$value['count']."\" value=\"".$value['value']."\">";

                            // Add any notes that are in the notesArray subarray
                            if(count($value['notesArray']) > 0)
                            {
                                foreach($value['notesArray'] as $notevalue)
                                {
                                    $myReadingNote = new valueNote;
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

                            $xml.="</value>\n";

                        }
                        $xml.="</values>\n";
                    }
                }    
            }
            $xml.= "</series>\n";
            return $xml;
        }
        else
        {
            // Errors so returning false
            return FALSE;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblseries")."'>";
        return $xml;
    }

    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
    }

    function getID()
    {
        return $this->vseriesID;
    }
    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }

    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
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
                if(($this->vseriesID == NULL))
                {
                    if($this->vseriesOp=='Direct')
                    {
                        // New direct series so create tblseries record first
                        $sql = "insert into tblseries  (  ";
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
                            $sql2 = "select * from tblseries where seriesid=currval('tblseries_seriesid_seq')";
                            $result = pg_query($dbconn, $sql2);
                            while ($row = pg_fetch_array($result))
                            {
                                $this->seriesID=$row['seriesid'];   
                            }
                        }    
                        
                        // Insert new values
                        foreach($this->valuesArray as $key => $value)
                        {
                            // First loop through the valuesArray and create insert statement for tblvalue table
                            $insertSQL = "insert into tblvalue (seriesid, relyear, value) values (".$this->seriesID.", ".$key.", ".$value['value'].")";
                            
                            // Do tblvalue inserts
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
                                    // There are notes associated with this value.  Before insert new notes we first need the pkey of the newly inserted record
                                    $sql3 = "SELECT * from tblvalue where valueid=currval('tblvalue_valueid_seq')";
                                    $result3 = pg_query($dbconn, $sql3);
                                    while ($row3 = pg_fetch_array($result3))
                                    {
                                        $thisReadingID = $row3['valueid'];
                                    }

                                    foreach($value['notesArray'] as $noteKey )
                                    {
                                        // Looping through notes and creating SQL insert statements
                                        $insertSQL = "INSERT INTO tblvaluevaluenote (valueid, valuenoteid) value(".$thisReadingID.", ".$noteKey.")";
                                    
                                        // Do tblvaluevaluenote inserts
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
                        -- MeasurementID - Integer - For direct only; the series derived from.
                        -- Constituents - Array of VMeasurementID - Must be NULL for DIRECT type, an array of one value for any type
                        --                other than SUM and DIRECT, and an array of one or more values for SUM
                        -- RETURNS: A new VMeasurementID
                    */

                    $sql = "select * from cpgdb.createnewvseries(";
                    $sql.= "'".$this->vseriesOp."', ";
                    if($this->vseriesOpParam)      { $sql.= "'".$this->vseriesOpParam."', "; } else { $sql.= "NULL, "; }
                    $sql.= $this->ownerUserID.", ";
                    $sql.= "'".$this->name."', ";
                    if($this->description)              { $sql.= "'".$this->description."', ";         } else { $sql.= "NULL, "; }
                    if($this->vseriesOp=='Direct') { $sql.= $this->seriesID.", ";            } else { $sql.= "NULL, "; }
                    if($this->vseriesOp!='Direct') 
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
                        // Successful to retrieve the automated fields for this new vseries
                        while ($row = pg_fetch_array($result))
                        {
                            $this->setParamsFromDB($row['createnewvseries']);
                        }
                    }
                }
                else
                {
                    // Editing an exisiting record

                    // First update the tblvseries table
                    $updateSQL2 = NULL;
                    $updateSQL = "update tblvseries set ";
                    $insertSQL="";
                    if($this->name)               $updateSQL.= "name = '".$this->name."', ";
                    if($this->description)        $updateSQL.= "description = '".$this->description."', ";
                    if(isset($this->isPublished)) $updateSQL.= "ispublished='".fromPHPtoPGBool($this->isPublished)."' ,";
                    if($this->ownerUserID)        $updateSQL.= "owneruserid = ".$this->ownerUserID.", ";
                    $updateSQL = substr($updateSQL, 0 , -2);
                    $updateSQL.= " where vseriesid=".$this->vseriesID."; ";

                    // Then update references or values depending on whether the series is direct or not
                    if ($this->vseriesOp!=="Direct")
                    {
                        // Update references to other vseriess
                        $deleteSQL = "delete from tblvseriesgroup where vseriesid=".$this->vseriesID."; ";
                        $relyear = 0;
                        foreach($this->referencesArray as $key => $value)
                        {
                            $insertSQL .= "insert into tblvseriesgroup (vseriesid, membervseriesid) values (".$this->vseriesID.", ".$value."); ";
                            $relyear++;
                        }
                    }
                    elseif($this->vseriesOp=="Direct")
                    {
                        // Update the tblseries table
                        $updateSQL2.= "update tblseries set ";
                        if(isset($this->radiusID))            $updateSQL2.= "radiusid = ".$this->radiusID.", ";
                        if(isset($this->isReconciled))        $updateSQL2.= "isreconciled='".fromPHPtoPGBool($this->isReconciled)."', ";
                        if(isset($this->startYear))           $updateSQL2.= "startyear = ".$this->startYear.", ";
                        if(isset($this->isLegacyCleaned))     $updateSQL2.= "islegacycleaned='".fromPHPtoPGBool($this->isLegacyCleaned)."', ";
                        if(isset($this->measuredByID))        $updateSQL2.= "measuredbyid = ".$this->measuredByID.", ";
                        if(isset($this->datingTypeID))        $updateSQL2.= "datingtypeid = ".$this->datingTypeID.", ";
                        if(isset($this->datingerrorpositive)) $updateSQL2.= "datingerrorpositive = ".$this->datingerrorpositive.", ";
                        if(isset($this->datingerrornegative)) $updateSQL2.= "datingerrornegative = ".$this->datingerrornegative.", ";
                        $updateSQL2 = substr($updateSQL2, 0 , -2);
                        $updateSQL2.= " where seriesid=".$this->seriesID."; ";

                        // Update values
                        $deleteSQL = "delete from tblvalue where seriesid=".$this->seriesID."; ";
                        $relyear = 0;
                        foreach($this->valuesArray as $key => $value)
                        {
                            $insertSQL .= "insert into tblvalue (seriesid, relyear, value) values (".$this->seriesID.", ".$relyear.", ".$value['value']."); ";
                            $relyear++;
                        }
                    }
                    
                    // Perform query using transactions so that if anything goes wrong we can roll back
                    $transaction = array("begin;", $updateSQL, $updateSQL2, $deleteSQL, $insertSQL);

                    foreach($transaction as $stmt)
                    {
                        pg_send_query($dbconn, $stmt);
                        $result = pg_get_result($dbconn);
                        if(pg_result_status($result, PGSQL_STATUS_LONG)!=PGSQL_COMMAND_OK)
                        {
                            // All gone badly so throw error and rollback
                            $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $stmt");
                            pg_send_query($dbconn, "rollback;");
                            return FALSE;
                        }
                    }

                    // All gone well so commit transaction to db
                    do {    
                        $result = pg_get_result($dbconn);
                    } while ($result!=FALSE); 
                    pg_send_query($dbconn, "commit;");
                    $result = pg_get_result($dbconn);
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
        if($this->vseriesID == NULL) 
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
                $sql = "select * from cpgdb.findvmchildren('".$this->vseriesID."', FALSE)";
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);

                // Check whether there are any vseriess that rely upon this one
                if(pg_num_rows($result)>0)
                {
                    $this->setErrorMessage("903", "There are existing seriess that rely upon this series.  You must delete all child seriess before deleting the parent.");
                    return FALSE;
                }
                else
                {
                    // Retrieve data for record about to be deleted
                    $this->setParamsFromDB($this->vseriesID);

                    if($this->vseriesOp=="Direct")
                    {
                        // This is a direct series so we can delete the tblseries entry and everything else should cascade delete
                        $deleteSQL = "delete from tblseries where seriesid=".$this->seriesID.";"; 
                    }
                    else
                    {
                        // This is a derived series so we just delete the tblvseries record and let everything else cascade delete
                        $deleteSQL = "delete from tblvseries where vseriesid=".$this->vseriesID.";"; 
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

    function getIndexNameFromParamID($paramid)
    {
        global $dbconn;

        $sql  = "select indexname from tlkpindextype where indexid='".$paramid."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                return $row['indexname'];
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }
    }


// End of Class
} 

?>
