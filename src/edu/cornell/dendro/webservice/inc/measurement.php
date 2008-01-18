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
    var $measurementID = NULL;
    var $vmeasurementID = NULL;
    var $vmeasurementResultID = NULL;
    var $vmeasurementOpID = 5;
    var $vmeasurementOp = "Direct";
    var $radiusID = NULL;
    var $isReconciled = FALSE;
    var $startYear = NULL;
    var $isLegacyCleaned = NULL;
    var $datingTypeID = 3;
    var $datingType = "Relative";
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
            
            $sql  = "select measurementid from tblvmeasurement where vmeasurementid=".$this->vmeasurementID;
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
    
    function setReferencesArray($theReferencesArray)
    {
        $this->referencesArray = $theReferencesArray;
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

    function asXML($mode="all", $recurseLevel=1)
    {
        // Return a string containing the current object in XML format

        // $mode = all, begin or end to denote which section of XML you require
        //      Default = all
        // $recurseLevel = the number of levels of references tags you would like 
        //      in your XML output.  
        //      Default = 1 


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
                if ($this->referencesArray && $recurseLevel>0)
                { 
                    // Decrement recurseLevel if necessary
                    if (is_numeric($recurseLevel))  $recurseLevel= $recurseLevel-1;

                    $xml.="<references>";
                    foreach($this->referencesArray as $value)
                    {
                        $myReference = new measurement();
                        $success = $myReference->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$myReference->asXML("all", $recurseLevel);
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
                // New record - readings data and no references means a new direct measurement
                //
                if(($this->vmeasurementID == NULL) && $this->vmeasurementOp=='Direct' && $this->readingsArray && !$this->referencesArray)
                {
                    $sql = "insert into tblmeasurement  (  ";
                        if($this->radiusID)            $sql.= "radiusid, "; 
                        if($this->isReconciled)        $sql.= "isreconciled, "; 
                        if($this->startYear)           $sql.= "startyear, "; 
                        if($this->isLegacyCleaned)     $sql.= "islegacycleaned, "; 
                        if($this->measuredByID)        $sql.= "measuredbyid, "; 
                        if($this->datingErrorPositive) $sql.= "datingerrorpositive, "; 
                        if($this->datingErrorNegative) $sql.= "datingerrornegative, "; 
                        if($this->datingTypeID)        $sql.= "datingtypeid, "; 
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if($this->radiusID)            $sql.= "'".$this->radiusID."', ";
                        if($this->isReconciled)        $sql.= "'".fromPHPtoPGBool($this->isReconciled)."', ";
                        if($this->startYear)           $sql.=     $this->startYear.", ";
                        if($this->isLegacyCleaned)     $sql.= "'".fromPHPtoPGBool($this->isLegacyCleaned)."', ";
                        if($this->measuredByID)        $sql.= "'".$this->measuredByID."', ";
                        if($this->datingErrorPositive) $sql.= "'".$this->datingErrorPositive."', ";
                        if($this->datingErrorNegative) $sql.= "'".$this->datingErrorNegative."', ";
                        if($this->datingTypeID)        $sql.= "'".$this->datingTypeID."', ";
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
                            $this->createdTimeStamp=$row['createdtimestamp'];   
                            $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
                        }
                        
                        // Now create a matching vmeasurement record for this new entry
                        // TODO : replace hard coded vmeasurementopid and owneruserid fields in following SQL
                        $sql3 = "insert into tblvmeasurement ( ";
                                                     $sql3.= "vmeasurementopid, ";
                                                     $sql3.= "owneruserid, ";
                            if($this->measurementID) $sql3.= "measurementid, ";
                            if($this->name)          $sql3.= "name, ";
                            if($this->description)   $sql3.= "description, ";
                            if($this->ispublished)   $sql3.= "ispublished, ";
                            // Trim off trailing space and comma
                            $sql3 = substr($sql3, 0, -2);
                            $sql3.= ") values (";
                                                     $sql3.= "'5', "; 
                                                     $sql3.= "'1', ";
                            if($this->measurementID) $sql3.= "'".$this->measurementID."', ";
                            if($this->name)          $sql3.= "'".$this->name."', ";
                            if($this->description)   $sql3.= "'".$this->description."', ";
                            if($this->ispublished)   $sql3.= "'".fromPHPtoPGBool($this->ispublished)."', ";
                            // Trim off trailing space and comma
                            $sql3 = substr($sql3, 0, -2);
                                                     $sql3.= ")";
                        pg_send_query($dbconn, $sql3);
                        $result = pg_get_result($dbconn);
                        if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                        {
                            $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql3");
                            return FALSE;
                        }
                        else
                        {
                            // Insert successful so retrieve automated field values
                            $sql4 = "select * from tblvmeasurement where vmeasurementid=currval('tblvmeasurement_vmeasurementid_seq')";
                            $result = pg_query($dbconn, $sql4);
                            while ($row = pg_fetch_array($result))
                            {
                                $this->vmeasurementID=$row['vmeasurementid'];   
                                $this->createdTimeStamp=$row['createdtimestamp'];   
                                $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
                            }
                            
                            // Insert new readings
                            foreach($this->readingsArray as $key => $value)
                            {
                                $insertSQL = "insert into tblreading (measurementid, relyear, reading) values (".$this->measurementID.", ".$key.", ".$value['reading'].")";
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
                }

                //
                // Adding a new vmeasurement based on other vmeasurements
                //
                    
                elseif(($this->vmeasurementID == NULL) && $this->vmeasurementOp!='Direct' && $this->referencesArray)
                {
                    // TODO : replace hard coded owneruserid field in following SQL
                    $sql = "insert into tblvmeasurement ( ";
                        if($this->vmeasurementOpID) $sql.= "vmeasurementopid, ";
                                                    $sql.= "owneruserid, ";
                        if($this->measurementID)    $sql.= "measurementid, ";
                        if($this->name)             $sql.= "name, ";
                        if($this->description)      $sql.= "description, ";
                        if($this->ispublished)      $sql.= "ispublished, ";
                        // Trim off trailing space and comma
                        $sql = substr($sql, 0, -2);
                        $sql.= ") values (";
                        if($this->vmeasurementOpID) $sql.= "'".$this->vmeasurementOpID."', ";
                                                    $sql.= "'1', ";
                        if($this->measurementID)    $sql.= "'".$this->measurementID."', ";
                        if($this->name)             $sql.= "'".$this->name."', ";
                        if($this->description)      $sql.= "'".$this->description."', ";
                        if($this->ispublished)      $sql.= "'".fromPHPtoPGBool($this->ispublished)."', ";
                        // Trim off trailing space and comma
                        $sql = substr($sql, 0, -2);
                                                 $sql.= ")";
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
                // Editing an existing  measurement
                //

                elseif($this->vmeasurementID !== NULL) 
                {
                    // First update the tblvmeasurement table
                    $updateSQL = "update tblvmeasurement set ";
                    $updateSQL.= "name = '".$this->name."', ";
                    $updateSQL.= "description = '".$this->description."' ";
                    //$updateSQL.= "ispublished='".fromPHPtoPGBool($this->isPublished)."' ,";
                    //$updateSQL.= "owneruserid = '".$this->owneruserid."' ";
                    $updateSQL.= "where vmeasurementid=".$this->vmeasurementID;
                   

                    // Then update references or readings depending on whether the measurement is direct or not
                    if ($this->vmeasurementOp!=="Direct")
                    {
                        // Update references to other vmeasurements
                        $deleteSQL = "delete from tblvmeasurementgroup where measurementid=".$this->measurementid;
                        $relyear = 0;
                        foreach($this->referencesArray as $key => $value)
                        {
                            $insertSQL = "insert into tblvmeasurementgroup (vmeasurementid, membervmeasurementid) values (".$this->vmeasurementid.", ".$value.")";
                            $relyear++;
                        }
                    }
                    elseif($this->vmeasurementOp=="Direct")
                    {
                        // Update the tblmeasurement table
                        $updateSQL = "update tblmeasurement set ";
                        $updateSQL.= "radiusid = '".$this->radiusID."', ";
                        $updateSQL.= "isreconciled='".fromPHPtoPGBool($this->isReconciled)."' ,";
                        $updateSQL.= "startyear = '".$this->startYear."', ";
                        $updateSQL.= "islegacycleaned='".fromPHPtoPGBool($this->isLegacyCleaned)."' ,";
                        $updateSQL.= "measuredbyid = '".$this->measuredByID."', ";
                        $updateSQL.= "datingtypeid = '".$this->datingTypeID."', ";
                        $updateSQL.= "datingerrorpositive = '".$this->datingerrorpositive."', ";
                        $updateSQL.= "datingerrornegative = '".$this->datingerrornegative."' ";
                        $updateSQL.= "where measurementid=".$this->measurementID;

                        // Update readings
                        $deleteSQL = "delete from tblreading where measurementid=".$this->measurementid;
                        $relyear = 0;
                        foreach($this->readingsArray as $key => $value)
                        {
                            $insertSQL = "insert in tblreading (measurementid, relyear, reading) values (".$this->measurementid.", ".$relyear.", ".$value.")";
                            $relyear++;
                        }
                    }
                    
                    // Perform query using transactions so that if anything goes wrong we can roll back
                    $transaction = "begin; $updateSQL; $deleteSQL; $insertSQL;";
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
                        // All gone well so commit transaction to db
                        pg_send_query($dbconn, "commit;");
                    }

                }

                // Shouldn't reach here
                else
                {
                    $this->seterrormessage("XXX", "Unable to determine if the DB write request was for a new direct, new derived, exisiting direct or exisiting derived measurement.");
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
                $sql = "select * from cpgdb.findvmchildren('".$this->vmeasurementID."')";
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);

                // Check whether there are any vmeasurements that rely upon this one
                if(pg_num_rows($result)!=0)
                {
                    $this->setErrorMessage("903", "There are exisiting measurements that rely upon this measurement.  You must delete all child measurements before deleting the parent.");
                    return FALSE;
                }
                else
                {
                    // Retrieve data for record about to be deleted
                    $this->setParamsFromDB($this->vmeasurementID);

                    if($this->vmeasurementOp=="Direct")
                    {
                        // Need to delete readings first
                        $deleteSQL = "delete from tblreading where measurementid=".$this->measurementID.";"; 
                    }
                    else
                    {
                        // Need to delete references first
                        $deleteSQL = "delete from tblmeasurementgroup where vmeasurementid=".$this->vmeasurementID.";"; 
                    }

                    // Next delete vmeasurement
                    $deleteSQL .= "delete from tblvmeasurement where vmeasurementid=".$this->vmeasurementID.";";

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
                        // All gone well so commit transaction to db
                        pg_send_query($dbconn, "commit;");
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
