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

class measurement 
{
    var $id = NULL;
    var $measurementID = NULL;
    var $vmeasurementID = NULL;
    var $vmeasurementResultID = NULL;
    var $vmeasurementOpID = 5;
    var $vmeasurementOp = NULL;
    var $radiusID = NULL;
    var $isReconciled = FALSE;
    var $startYear = NULL;
    var $isLegacyCleaned = NULL;
    var $datingTypeID = NULL;
    var $datingType = NULL;
    var $datingErrorPositive = NULL;
    var $datingErrorNegative = NULL;
    var $measuredByID = NULL;
    var $measuredBy = NULL;
    var $ownerUserID = NULL;
    var $owner = NULL;
    var $readingsArray = array();
    var $referencesArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
    var $name = NULL;
    var $description = NULL;

    var $parentXMLTag = "measurement"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function measurement()
    {
        // Constructor for this class.
        $this->vmeasurementOpID = 5;
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

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $this->vmeasurementID=$theID;
        $sql = "select * from cpgdb.getvmeasurementresult('$theID')";
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
                $this->name = $row['name'];
                $this->description = $row['description'];
                $this->vmeasurementID = $row['vmeasurementid'];
                $this->vmeasurementResultID = $row['vmeasurementresultid'];
                $this->radiusID = $row['radiusid'];
                $this->isReconciled = $row['isreconciled'];
                $this->startYear = $row['startyear'];
                $this->isLegacyCleaned = $row['islegacycleaned'];
                $this->datingErrorPositive = $row['datingerrorpositive'];
                $this->datingErrorNegative = $row['datingerrornegative'];
                $this->createdTimeStamp = $row['createdtimestamp'];
                $this->lastModifiedTimeStamp = $row['lastmodifiedtimestamp'];
                
                $this->setMeasurementID($row['measurementid']);
                $this->setVMeasurementOpID($row['vmeasurementopid']);
                $this->setDatingTypeID($row['datingtypeid']);
                $this->setOwnerUserID($row['owneruserid']);
                $this->setMeasuredByID($row['measuredbyid']);

                $this->setReadingsFromDB();
                $this->setReferencesFromDB();
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

    function setReadingsFromDB()
    {
        // Add all readings data to the object
        global $dbconn;

        $sql  = "select * from cpgdb.getvmeasurementreadingresult('".$this->vmeasurementResultID."') order by relyear asc";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get add all reading values to array 
                $this->readingsArray[$row['relyear']] = array('reading' => $row['reading'], 
                                                              'wjinc' => $row['wjinc'], 
                                                              'wjdec' => $row['wjdec'], 
                                                              'count' => $row['count']
                                                             );
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

    function setReferencesFromDB()
    {
        // Add any vmeasurements that the current measurement has been made from
        global $dbconn;

        $sql  = "select * from cpgdb.findvmparents('".$this->vmeasurementID."')";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
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

    function setVMeasurementOpID($theVMeasurementOpID)
    {
        if($theVMeasurementOpID)
        {
            //Only run if valid parameter has been provided
            global $dbconn;

            $this->vmeasurementOpID = $theVMeasurementOpID;
            
            $sql  = "select name from tlkpvmeasurementop where vmeasurementopid=".$this->vmeasurementOpID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->vmeasurementOp = $row['name'];
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
    
    function setMeasurementID()
    {
        if($this->vmeasurementID)
        {
            //Only run if valid parameter has been provided
            global $dbconn;
            
            $sql  = "select measurementid from tblvmeasurementid where vmeasurementid=".$this->vmeasurementID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->measurementID = $row['measurementid'];
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
        $this->readingsArray = $theReadingsArray;
    }
    
    function setRadiusID($theRadiusID)
    {
        $this->radiusID = $theRadiusID;
    }

    function setIsReconciled($isReconciled)
    {
        $this->isReconciled = $isReconciled;
    }

    function setStartYear($theStartYear)
    {
        $this->startYear = $theStartYear;
    }

    function setIsLegacyCleaned($isLegacyCleaned)
    {
        $this->isLegacyCleaned = $isLegacyCleaned;
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
        $this->datingErrorPositive = $theDatingPositive;
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
        $this->isPublished = $isPublished;
    }


    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML($mode="all", $isRecursive=false)
    {
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($mode=="all") || ($mode=="begin"))
            {
                // Only return XML when there are no errors.
                $xml.= "<measurement ";
                $xml.= "id=\"".$this->vmeasurementID."\" ";
                $xml.= "radiusID=\"".$this->radiusID."\" ";
                $xml.= "isReconciled=\"".fromPHPtoStringBool($this->isReconciled)."\" ";
                $xml.= "startYear=\"".$this->startYear."\" ";
                $xml.= "isLegacyCleaned=\"".fromPHPtoStringBool($this->isLegacyCleaned)."\" ";
                $xml.= "measuredByID=\"".$this->measuredByID."\" ";
                $xml.= "measuredBy=\"".$this->measuredBy."\" ";
                $xml.= "ownerUserID=\"".$this->ownerUserID."\" ";
                $xml.= "owner=\"".$this->owner."\" ";
                $xml.= "datingTypeID=\"".$this->datingTypeID."\" ";
                $xml.= "datingType=\"".$this->datingType."\" ";
                $xml.= "datingErrorPositive=\"".$this->datingErrorPositive."\" ";
                $xml.= "datingErrorNegative=\"".$this->datingErrorNegative."\" ";
                $xml.= "name=\"".$this->name."\" ";
                $xml.= "description=\"".$this->description."\" ";
                $xml.= "isPublished=\"".fromPHPtoStringBool($this->isPublished)."\" ";
                $xml.= "createdTimeStamp=\"".$this->createdTimeStamp."\" ";
                $xml.= "lastModifiedTimeStamp=\"".$this->lastModifiedTimeStamp."\" ";
                $xml.= ">";

                // Include all readings 
                if ($this->readingsArray)
                {
                    $xml.="<readings>";
                    foreach($this->readingsArray as $key => $value)
                    {
                        // Calculate absolute year where possible
                        // TODO : Deal with 0AD/BC problem
                        if ($this->startYear)
                        {
                            $yearvalue = $key + $this->startYear;
                        }
                        else
                        {
                            $yearvalue = $key;
                        }

                            $xml.="<value year=\"".$yearvalue."\" wjinc=\"".$value['wjinc']."\" wjdec=\"".$value['wjdec']."\" count=\"".$value['count']."\">".$value['reading']."</value>";
                    }
                    $xml.="</readings>";
                }
                
                // Include all refences to other vmeasurements recursing if requested 
                if ($this->referencesArray)
                { 
                    $xml.="<references>";
                    foreach($this->referencesArray as $value)
                    {
                        $myReference = new measurement();
                        $success = $myReference->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$myReference->asXML("all", $isRecursive);
                        }
                        else
                        {
                            $myMetaHeader->setErrorMessage($myReference->getLastErrorCode, $myReference->getLastErrorMessage);
                        }
                    }
                    $xml.="</references>";
                }
            }

            if(($mode=="all") || ($mode=="end"))
            {
                // End XML tag
                $xml.= "</measurement>\n";
            }
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblmeasurement")."'>";
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
        return $this->vmeasurementID;
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
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {

            // Check DB connection is OK before continueing
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
               
                // 
                // New record - readings data and no references means a new direct measurement
                //
                if(($this->vmeasurementID == NULL) && $this->readingsArray && !$this->referencesArray)
                {
                    $sql = "insert into tblmeasurement  (  ";
                        if($this->radiusID) $sql.= "radiusid, "; 
                        if($this->isReconciled) $sql.= "isreconciled, "; 
                        if($this->startYear) $sql.= "startyear, "; 
                        if($this->isLegacyCleaned) $sql.= "islegacycleaned, "; 
                        if($this->measuredByID) $sql.= "measuredbyid, "; 
                        if($this->datingErrorPositive) $sql.= "datingerrorpositive, "; 
                        if($this->datingErrorNegative) $sql.= "datingerrornegative, "; 
                        if($this->datingTypeID) $sql.= "datingtypeid, "; 
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if($this->radiusID) $sql.= "'".$this->radiusID."', ";
                        if($this->isReconciled) $sql.= "'".fromPHPtoPGBool($this->isReconciled)."', ";
                        if($this->startYear) $sql.= "'".$this->startYear."', ";
                        if($this->isLegacyCleaned) $sql.= "'".fromPHPtoPGBool($this->isLegacyCleaned)."', ";
                        if($this->measuredByID) $sql.= "'".$this->measuredByID."', ";
                        if($this->datingErrorPositive) $sql.= "'".$this->datingErrorPositive."', ";
                        if($this->datingErrorNegative) $sql.= "'".$this->datingErrorNegative."', ";
                        if($this->datingTypeID) $sql.= "'".$this->datingTypeID."' ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";

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
                        else
                        {
                            // Insert successful so retrieve automated field values
                            $sql2 = "select * from tblmeasurement where measurementid=currval('tblmeasurement_measurementid_seq')";
                            $result = pg_query($dbconn, $sql2);
                            while ($row = pg_fetch_array($result))
                            {
                                $this->vmeasurementID=$row['measurementid'];   
                                $this->createdTimeStamp=$row['createdtimestamp'];   
                                $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
                            }
                            
                            // Now create a matching vmeasurement record for this new entry
                            // TODO : replace hard coded vmeasurementopid and owneruserid fields in following SQL
                            $sql3 = "insert into tblvmeasurement (
                                                            measurementid,
                                                            vmeasurementopid,
                                                            name,
                                                            description,
                                                            ispublished,
                                                            owneruserid
                                                                )
                                                    values
                                                        (
                                                            '".$this->vmeasurementID."',
                                                            '5', 
                                                            '".$this->name."',
                                                            '".$this->description."',
                                                            '".fromPHPtoPGBool($this->ispublished)."',
                                                            '1'
                                                        )";
                            pg_send_query($dbconn, $sql3);
                            $result = pg_get_result($dbconn);
                            if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                            {
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql3");
                                return FALSE;
                            }
                        }
                    }

                }

                //
                // Adding a new vmeasurement based on other vmeasurements
                //

                elseif(($this->vmeasurementID == NULL) && $this->referencesArray)
                {
                    // TODO : replace hard coded owneruserid field in follwoing SQL
                    $sql = "insert into tblvmeasurement (
                                                    vmeasurementopid,
                                                    vmeasurementopparameter,
                                                    name,
                                                    description,
                                                    ispublished,
                                                    owneruserid
                                                        )
                                            values
                                                (
                                                    '".$this->vmeasurementOpID."',
                                                    '".$this->vmeasurementOpParameter."',
                                                    '".$this->name."',
                                                    '".$this->description."',
                                                    '".fromPHPtoPGBool($this->isPublished)."',
                                                    '1'
                                                )";
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
                        $sql2 = "select * from tblvmeasurement where vmeasurementid=currval('tblvmeasurement_vmeasurementid_seq')";
                        $result = pg_query($dbconn, $sql2);
                        while ($row = pg_fetch_array($result))
                        {
                            $this->vmeasurementID=$row['vmeasurementid'];   
                            $this->createdTimeStamp=$row['createdtimestamp'];   
                            $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
                        }

                        // Now add all references to other vmeasurements into tblvmeasurementgroup
                        foreach($this->referencesArray as $value)
                        {
                            $insertSQL = "insert into tblvmeasurementgroup (vmeasurementid, membervmeasurementid) values (".$this->vmeasurementID.", ".$value.")"; 
                            pg_send_query($dbconn, $insertSQL);
                            $result = pg_get_result($dbconn);
                            if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                            {
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $insertSQL");
                                return FALSE;
                            }
                        }
                    }
                }

                //
                // Editing an existing measurement
                //

                elseif($this->vmeasurementID !== NULL) 
                {
                    // First update the tblvmeasurement table
                    $sql = "update tblvmeasurement set ";
                    $sql.= "name = '".$this->name."', ";
                    $sql.= "description = '".$this->description."' ";
                    //$sql.= "ispublished='".fromPHPtoPGBool($this->isPublished)."' ,";
                    //$sql.= "owneruserid = '".$this->owneruserid."' ";
                    $sql.= "where vmeasurementid=".$this->vmeasurementID;
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        return FALSE;
                    }
                    
                    // Next update the tblmeasurement table
                    $sql = "update tblmeasurement set ";
                    $sql.= "radiusid = '".$this->radiusID."', ";
                    $sql.= "isreconciled='".fromPHPtoPGBool($this->isReconciled)."' ,";
                    $sql.= "startyear = '".$this->startYear."', ";
                    $sql.= "islegacycleaned='".fromPHPtoPGBool($this->isLegacyCleaned)."' ,";
                    $sql.= "measuredbyid = '".$this->measuredByID."', ";
                    $sql.= "datingtypeid = '".$this->datingTypeID."', ";
                    $sql.= "datingerrorpositive = '".$this->datingerrorpositive."', ";
                    $sql.= "datingerrornegative = '".$this->datingerrornegative."' ";
                    $sql.= "where measurementid=".$this->measurementID;
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        return FALSE;
                    }


                    // Then update references or readings
                    if ($this->vmeasurementOp!=="Direct")
                    {
                        // Update references to other vmeasurements
                        
                        // Delete all existing references and replace with new ones
                        $deleteSQL = "delete from tblvmeasurementgroup where measurementid=".$this->measurementid;
                        pg_send_query($dbconn, $deleteSQL);
                        $result = pg_get_result($dbconn);
                        if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                        {
                            $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $deleteSQL");
                            return FALSE;
                        }

                        // Insert new readings
                        $relyear = 0;
                        foreach($this->readingsArray as $key => $value)
                        {
                            $insertSQL = "insert into tblreading (measurementid, relyear, reading) values (".$this->measurementid.", ".$relyear.", ".$value.")";
                            $relyear++;
                            pg_send_query($dbconn, $deleteSQL);
                            $result = pg_get_result($dbconn);
                            if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                            {
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $deleteSQL");
                                return FALSE;
                            }
                        }

                    }
                    else
                    {
                        // Update readings

                        // Delete all existing readings and replace with new ones
                        $deleteSQL = "delete from tblreading where measurementid=".$this->measurementid;
                        pg_send_query($dbconn, $deleteSQL);
                        $result = pg_get_result($dbconn);
                        if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                        {
                            $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $deleteSQL");
                            return FALSE;
                        }

                        // Insert new readings
                        $relyear = 0;
                        foreach($this->readingsArray as $key => $value)
                        {
                            $insertSQL = "insert in tblreading (measurementid, relyear, reading) values (".$this->measurementid.", ".$relyear.", ".$value.")";
                            $relyear++;
                            pg_send_query($dbconn, $deleteSQL);
                            $result = pg_get_result($dbconn);
                            if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                            {
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $deleteSQL");
                                return FALSE;
                            }
                        }



                    }

                }

                // Shouldn't reach here
                else
                {
                    $this->seterrormessage("XXX", "Unknown error.  Not sure what to write to DB");
                    return false;
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

                $sql = "delete from tblmeasurement where measurementid=".$this->vmeasurementID;

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
