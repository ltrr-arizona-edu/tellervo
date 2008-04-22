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
require_once('inc/vmeasurementNote.php');

class measurement 
{
    var $measurementID = NULL;
    var $vmeasurementID = NULL;
    var $vmeasurementResultID = NULL;
    var $vmeasurementOpID = 5;
    var $vmeasurementOpParam = NULL;
    var $vmeasurementOp = "Direct";
    var $vmeasurementUnits = NULL;
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
    var $readingsArray = array();
    var $referencesArray = array();
    var $vmeasurementNoteArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
    var $name = NULL;
    var $description = NULL;
    var $centroidLat = NULL;
    var $centroidLong = NULL;
    var $minLat = NULL;
    var $maxLat = NULL;
    var $minLong = NULL;
    var $maxLong = NULL;

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

    function setParamsFromDB($theID, $format="full")
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
                //$this->vmeasurementOpID = $row['vmeasurementopid'];
                //$this->vmeasurementOpParam = $row['vmeasurementparamid'];
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

                // Get more parameters directly from tblvmeasurement
                $sql2 = "select tblvmeasurement.* from tblvmeasurement where vmeasurementid=".$this->vmeasurementID;
                pg_send_query($dbconn, $sql2);
                $result2 = pg_get_result($dbconn);
                $row2 = pg_fetch_array($result2);
                $this->setMeasurementID();
                $this->setVMeasurementOp($row2['vmeasurementopid']);
                $this->setOwnerUserID($row2['owneruserid']);

                if ($format=="full")
                {
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
                // Get all reading values to array 
                $this->readingsArray[$row['relyear']] = array('reading' => $row['reading'], 
                                                              'wjinc' => $row['wjinc'], 
                                                              'wjdec' => $row['wjdec'], 
                                                              'count' => $row['count'],
                                                              'notesArray' => array()
                                                             );
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

    function setReferencesFromDB()
    {
        // Add any vmeasurements that the current measurement has been made from
        global $dbconn;
        
        $sql  = "select * from cpgdb.findvmparents('".$this->vmeasurementID."', 'false') where recursionlevel=0";
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

            $this->vmeasurementOpID = $theVMeasurementOp;
            
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
        elseif(is_string($theVMeasurementOp))
        {
            global $dbconn;

            $this->vmeasurementOp = $theVMeasurementOp;
            
            $sql  = "select vmeasurementopid from tlkpvmeasurementop where name ilike'".$theVMeasurementOp."'";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->vmeasurementOpID = $row['vmeasurementopid'];
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

    function getEndYear()
    {
        $length = count($this->readingsArray);
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

    function asXML($recurseLevel=1, $style="full")
    {
        //print_r($this->referencesArray);
        // Return a string containing the current object in XML format

        // $mode = all, begin or end to denote which section of XML you require
        //      Default = all
        // $recurseLevel = the number of levels of references tags you would like 
        //      in your XML output.  
        //      Default = 1 
        global $domain;
        $xml = "";

        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            $xml.= "<measurement ";
            $xml.= "id=\"".$this->vmeasurementID."\" ";
            $xml.= "url=\"http://$domain/measurement/".$this->vmeasurementID."\">";
            
            $xml.= "<metadata>\n";
            if(isset($this->name))                  $xml.= "<name>".$this->name."</name>\n";
            if(isset($this->isReconciled))          $xml.= "<isReconciled>".fromPHPtoStringBool($this->isReconciled)."</isReconciled>\n";
            if(isset($this->startYear))             $xml.= "<startYear>".$this->startYear."</startYear>\n";
            if(isset($this->isLegacyCleaned))       $xml.= "<isLegacyCleaned>".fromPHPtoStringBool($this->isLegacyCleaned)."</isLegacyCleaned>\n";
            if(isset($this->measuredByID))          $xml.= "<measuredBy id=\"".$this->measuredByID."\">".$this->measuredBy."</measuredBy>\n";
            if(isset($this->ownerUserID))           $xml.= "<owner id=\"".$this->ownerUserID."\">".$this->owner."</owner>\n";
            if(isset($this->datingTypeID))          $xml.= "<datingType id=\"".$this->datingTypeID."\">".$this->datingType."</datingType>\n";
            if(isset($this->datingErrorPositive))   $xml.= "<datingErrorPositive>".$this->datingErrorPositive."</datingErrorPositive>\n";
            if(isset($this->datingErrorNegative))   $xml.= "<datingErrorNegative>".$this->datingErrorNegative."</datingErrorNegative>\n";
            if(isset($this->description))           $xml.= "<description>".$this->description."</description>\n";
            if(isset($this->isPublished))           $xml.= "<isPublished>".fromPHPtoStringBool($this->isPublished)."</isPublished>\n";
            if(isset($this->createdTimeStamp))      $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
            if(isset($this->lastModifiedTimeStamp)) $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";

            // Brief Format so just give minimal XML for all references and nothing else
            if($style=="brief")
            {
                foreach($this->referencesArray as $value)
                {
                    $myReference = new measurement();
                    $success = $myReference->setParamsFromDB($value);

                    if($success)
                    {
                        $xml.=$myReference->asXML($recurseLevel, "brief");
                    }
                    else
                    {
                        $this->setErrorMessage($myReference->getLastErrorCode, $myReference->getLastErrorMessage);
                    }
                }
                $xml.="</metadata>\n";
                $xml.= "</measurement>\n";
                return $xml;
            }

            // Full format so give the whole lot
            elseif($style=="full")
            {
            
                // Include site notes if present
                if ($this->vmeasurementNoteArray)
                {
                    $xml.= "<siteNotes>\n";
                    foreach($this->vmeasurementNoteArray as $value)
                    {
                        $myVMeasurementNote = new vmeasurementNote();
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
                
                // Include all refences to other vmeasurements recursing if requested 
                if ($this->referencesArray)
                {
                    // Decrement recurseLevel if necessary
                    if (is_numeric($recurseLevel))  $recurseLevel=$recurseLevel-1;

                    $xml.="<references";
                    if(isset($this->vmeasurementOp)) $xml.= " operation=\"".$this->vmeasurementOp."\"";
                    $xml.=">";
                    foreach($this->referencesArray as $value)
                    {
                        $myReference = new measurement();
                        $success = $myReference->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$myReference->asXML($recurseLevel, "brief");
                        }
                        else
                        {
                            $this->setErrorMessage($myReference->getLastErrorCode, $myReference->getLastErrorMessage);
                        }
                    }
                    $xml.="</references>\n";
                }
                else
                {
                    $xml.="<references>\n";
                    $xml.= "<radius id=\"".$this->radiusID."\" url=\"http://$domain/radius/".$this->radiusID."\" />\n";
                    $xml.="</references>\n";
                }
                
                $xml.= "</metadata>\n";

                // Include all readings 
                if ($this->readingsArray)
                {
                    $xml.="<readings type=\"annual\" units=\"-6\">\n";
                    foreach($this->readingsArray as $key => $value)
                    {
                        // Calculate absolute year where possible
                        if ($this->startYear)
                        {
                            if($this->startYear+$key  >= 0)
                            {
                                // Add 1 to year to cope with BC/AD transition issue (no 0bc/ad)
                                $yearvalue = $key + $this->startYear;
                            }
                            else
                            {
                                // Date is BC so fudge not required
                                $yearvalue = $key + $this->startYear - 1;
                            }
                        }
                        else
                        {
                            // Years are relative
                            $yearvalue = $key;
                        }

                        $xml.="<reading year=\"".$yearvalue."\" ";
                        if ($value['wjinc'] || $value['wjdec'])
                        {
                            $xml.="weiserjahre=\"".$value['wjinc']."/".$value['wjdec']."\" ";
                        }
                        $xml .="count=\"".$value['count']."\" value=\"".$value['reading']."\">";

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

                        $xml.="</reading>\n";

                    }
                    $xml.="</readings>\n";
                }    
                $xml.= "</measurement>\n";
                return $xml;
            }
            else
            {
                //Brief XML output


            }
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
                        foreach($this->readingsArray as $key => $value)
                        {
                            // First loop through the readingsArray and create insert statement for tblreading table
                            $insertSQL = "insert into tblreading (measurementid, relyear, reading) values (".$this->measurementID.", ".$key.", ".$value['reading'].")";
                            
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
                        // Successful to retrieve the automated fields for this new vmeasurement
                        while ($row = pg_fetch_array($result))
                        {
                            $this->setParamsFromDB($row['createnewvmeasurement']);
                        }
                    }
                }
                else
                {
                    // Editing an exisiting record

                    // First update the tblvmeasurement table
                    $updateSQL = "update tblvmeasurement set ";
                    $insertSQL="";
                    if($this->name)               $updateSQL.= "name = '".$this->name."', ";
                    if($this->description)        $updateSQL.= "description = '".$this->description."' ";
                    if(isset($this->isPublished)) $updateSQL.= "ispublished='".fromPHPtoPGBool($this->isPublished)."' ,";
                    if($this->ownerUserID)        $updateSQL.= "owneruserid = '".$this->ownerUserID."', ";
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
                        $updateSQL.= "update tblmeasurement set ";
                        if(isset($this->radiusID))            $updateSQL.= "radiusid = '".$this->radiusID."', ";
                        if(isset($this->isReconciled))        $updateSQL.= "isreconciled='".fromPHPtoPGBool($this->isReconciled)."', ";
                        if(isset($this->startYear))           $updateSQL.= "startyear = '".$this->startYear."', ";
                        if(isset($this->isLegacyCleaned))     $updateSQL.= "islegacycleaned='".fromPHPtoPGBool($this->isLegacyCleaned)."', ";
                        if(isset($this->measuredByID))        $updateSQL.= "measuredbyid = '".$this->measuredByID."', ";
                        if(isset($this->datingTypeID))        $updateSQL.= "datingtypeid = '".$this->datingTypeID."', ";
                        if(isset($this->datingerrorpositive)) $updateSQL.= "datingerrorpositive = '".$this->datingerrorpositive."', ";
                        if(isset($this->datingerrornegative)) $updateSQL.= "datingerrornegative = '".$this->datingerrornegative."', ";
                        $updateSQL = substr($updateSQL, 0 , -2);
                        $updateSQL.= " where measurementid=".$this->measurementID."; ";

                        // Update readings
                        $deleteSQL = "delete from tblreading where measurementid=".$this->measurementID."; ";
                        $relyear = 0;
                        foreach($this->readingsArray as $key => $value)
                        {
                            $insertSQL .= "insert into tblreading (measurementid, relyear, reading) values (".$this->measurementID.", ".$relyear.", ".$value."); ";
                            $relyear++;
                        }
                    }
                    
                    // Perform query using transactions so that if anything goes wrong we can roll back
                    $transaction = "begin; $updateSQL $deleteSQL $insertSQL";
                    //echo $transaction;
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
                    $this->setErrorMessage("903", "There are existing measurements that rely upon this measurement.  You must delete all child measurements before deleting the parent.");
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
    }

// End of Class
} 

?>
