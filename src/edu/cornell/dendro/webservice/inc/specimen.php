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
//require_once('inc/radius.php');
require_once('inc/specimenType.php');
require_once('inc/terminalRing.php');
require_once('inc/specimenQuality.php');
require_once('inc/specimenContinuity.php');
require_once('inc/pith.php');

class specimen 
{
    var $id = NULL;
    var $label = NULL;
    var $treeID = NULL;
    var $dateCollected = NULL;
    var $specimenTypeID = NULL;
    var $terminalRingID = NULL;
    var $isTerminalRingVerified = NULL;
    var $sapwoodCount = NULL;
    var $sapwoodCountVerified = NULL;
    var $specimenQualityID = NULL;
    var $specimenQualityVerified = NULL;
    var $specimenContinuityID = NULL;
    var $specimenContinuityVerified = NULL;
    var $pithID = NULL;
    var $pithVerified = NULL;
    var $isPithVerified = NULL;
    var $unmeasuredPre = NULL;
    var $isUnmeasuredPreVerified = NULL;
    var $unmeasuredPost = NULL;
    var $isUnmeasuredPostVerified = NULL;
    
    var $radiusArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;

    var $parentXMLTag = "specimens"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function specimen()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/

    function setLabel($theLabel)
    {
        // Set the current objects note.
        $this->label=$theLabel;
    }
    
    function setTreeID($theTreeID)
    {
        // Set the current objects note.
        $this->treeID=$theTreeID;
    }

    function setSpecimenType($specimenType)
    {
        // Set speciemn type id from label
        $mySpecimenType = new specimenType;
        $mySpecimenType->setParamsFromLabel($specimenType);
        $this->specimenTypeID=$mySpecimenType->getID();
    }

    function setSpecimenTypeID($specimenTypeID)
    {
        // Set specimen type id from id
        $this->specimenTypeID=$specimenTypeID;
    }

    function setTerminalRing($terminalRing)
    {
        // Set terminal ring id from label
        $myTerminalRing = new terminalRing;
        $myTerminalRing->setParamsFromLabel($terminalRing);
        $this->terminalRingID=$myTerminalRing->getID();
    }

    function setTerminalRingID($terminalRingID)
    {
        // Set terminal ring id from id
        $this->terminalRingID=$terminalRingID;
    }

    function setIsTerminalRingVerified($theBool)
    {
        $this->isTerminalRingVerified=$theBool;
    }

    function setSpecimenQuality($specimenQuality)
    {
        // Set specimen quality id from label
        $mySpecimenQuality = new specimenQuality;
        $mySpecimenQuality->setParamsFromLabel($specimenQuality);
        $this->specimenQualityID=$mySpecimenQuality->getID();
    }

    function setSpecimenQualityID($specimenQualityID)
    {
        // Set specimen quality id from id
        $this->specimenQualityID=$specimenQualityID;
    }
    
    function setIsSpecimenQualityVerified($theBool)
    {
        $this->isSpecimenQualityVerified=$theBool;
    }

    function setSpecimenContinuity($specimenContinuity)
    {
        // Set specimen continuity id from label
        $mySpecimenContinuity = new specimenContinuity;
        $mySpecimenContinuity->setParamsFromLabel($specimenContinuity);
        $this->specimenContinuityID=$mySpecimenContinuity->getID();
    }

    function setSpecimenContinuityID($specimenContinuityID)
    {
        // Set specimen continuity id from id
        $this->specimenContinuityID=$specimenContinuityID;
    }
    
    function setIsSpecimenContinuityVerified($theBool)
    {
        $this->isSpecimenContinuityVerified=$theBool;
    }

    function setPith($pith)
    {
        // Set pith id from label
        $myPith = new specimenContinuity;
        $myPith->setParamsFromLabel($pith);
        $this->pithID=$myPith->getID();
    }

    function setPithID($pithID)
    {
        // Set pith id from id
        $this->pithID=$pithID;
    }
    
    function setIsPithVerified($theBool)
    {
        // Set pith id from id
        $this->isPithVerified=$theBool;
    }
    
    function setDateCollected($dateCollected)
    {
        $this->dateCollected = $dateCollected;
    }
    
    function setSapwoodCount($sapwoodCount)
    {
        $this->sapwoodCount = $sapwoodCount;
    }
    
    function setIsSapwoodCountVerified($theBool)
    {
        $this->isSapwoodCountVerified = $theBool;
    }
    
    function setUnmeasPre($theValue)
    {
        $this->unmeasuredPre = $theValue;
    }
    
    function setIsUnmeasPreVerified($theBool)
    {
        $this->isUnmeasuredPreVerified = $theBool;
    }
    
    function setUnmeasPost($theValue)
    {
        $this->unmeasuredPost = $theValue;
    }
    
    function setIsUnmeasPostVerified($theBool)
    {
        $this->isUnmeasuredPostVerified = $theBool;
    }

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
        
        $this->id=$theID;
        $sql = "select * from tblspecimen where specimenid=$theID";
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
                $this->label = $row['label'];
                $this->id = $row['specimenid'];
                $this->dateCollected = $row['datecollected'];
                $this->specimenTypeID = $row['specimentypeid'];
                $this->terminalRingID = $row['terminalringid'];
                $this->isTerminalRingVerified = $row['isterminalringverified'];
                $this->sapwoodCount = $row['sapwoodcount'];
                $this->isSapwoodCountVerified = $row['issapwoodcountverified'];
                $this->specimenQualityID= $row['specimenqualityid'];
                $this->isSpecimenQualityVerified= $row['isspecimenqualityverified'];
                $this->specimenContinuityID = $row['specimencontinuityid'];
                $this->isSpecimenContinuityVerified = $row['isspecimencontinuityverified'];
                $this->pithID = $row['pithid'];
                $this->isPithVerified = $row['ispithverified'];
                $this->unmeasuredPre = $row['unmeaspre'];
                $this->isUnmeasuredPreVerified = $row['isunmeaspreverified'];
                $this->unmeasuredPost = $row['unmeaspost'];
                $this->isUnmeasuredPostVerified = $row['isunmeaspostverified'];
                $this->createdTimeStamp = $row['createdtimestamp'];
                $this->lastModifiedTimeStamp = $row['lastmodifiedtimestamp'];
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

    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database
        // SpecimenSpecimenNotes

        global $dbconn;

        $sql  = "select radiusid from tblradius where specimenid=".$this->id;
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


    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML($mode="all")
    {
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($mode=="all") || ($mode=="begin"))
            {
                $mySpecimenType = new specimenType;
                $mySpecimenType->setParamsFromDB($this->specimenTypeID);
                $myTerminalRing = new terminalRing;
                $myTerminalRing->setParamsFromDB($this->terminalRingID);
                $mySpecimenQuality = new specimenQuality;
                $mySpecimenQuality->setParamsFromDB($this->specimenQualityID);
                $mySpecimenContinuity = new specimenContinuity;
                $mySpecimenContinuity->setParamsFromDB($this->specimenContinuityID);
                $myPith = new pith;
                $myPith->setParamsFromDB($this->pithID);

                // Only return XML when there are no errors.
                $xml.= "<specimen ";
                $xml.= "id=\"".$this->id."\" ";
                $xml.= "label=\"".$this->label."\" ";
                $xml.= "dateCollected=\"".$this->dateCollected."\" ";
                $xml.= "specimenTypeID=\"".$this->specimenTypeID."\" ";
                $xml.= "specimenType=\"".$mySpecimenType->getLabel()."\" ";
                $xml.= "terminalRingID=\"".$this->terminalRingID."\" ";
                $xml.= "terminalRing=\"".$myTerminalRing->getLabel()."\" ";
                $xml.= "isTerminalRingVerified=\"".fromPGtoStringBool($this->isTerminalRingVerified)."\" ";
                $xml.= "sapwoodCount=\"".$this->sapwoodCount."\" ";
                $xml.= "isSapwoodCountVerified=\"".fromPHPtoStringBool($this->isSapwoodCountVerified)."\" ";
                $xml.= "specimenQualityID=\"".$this->specimenQualityID."\" ";
                $xml.= "specimenQuality=\"".$mySpecimenQuality->getLabel()."\" ";
                $xml.= "isSpecimenQualityVerified=\"".fromPHPtoStringBool($this->isSpecimenQualityVerified)."\" ";
                $xml.= "specimenContinuityID=\"".$this->specimenContinuityID."\" ";
                $xml.= "specimenContinuity=\"".$mySpecimenContinuity->getLabel()."\" ";
                $xml.= "isSpecimenContinuityVerified=\"".fromPHPtoStringBool($this->isSpecimenContinuityVerified)."\" ";
                $xml.= "pithID=\"".$this->pithID."\" ";
                $xml.= "pith=\"".$myPith->getLabel()."\" ";
                $xml.= "isPithVerified=\"".fromPHPtoStringBool($this->isPithVerified)."\" ";
                $xml.= "unmeasuredPre=\"".$this->unmeasuredPre."\" ";
                $xml.= "isUnmeasuredPreVerified=\"".fromPHPtoStringBool($this->isUnmeasuredPreVerified)."\" ";
                $xml.= "unmeasuredPost=\"".$this->unmeasuredPost."\" ";
                $xml.= "isUnmeasuredPostVerified=\"".fromPHPtoStringBool($this->isUnmeasuredPostVerified)."\" ";

                $xml.= "createdTimeStamp=\"".$this->createdTimeStamp."\" ";
                $xml.= "lastModifiedTimeStamp=\"".$this->lastModifiedTimeStamp."\" ";
                $xml.= ">";

                /*
                // Include radii if present
                if ($this->radiusArray)
                {
                    foreach($this->radiusArray as $value)
                    {
                        $myRadius = new radius();
                        $success = $myRadius->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$myRadius->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setErrorMessage($myRadius->getLastErrorCode, $myRadius->getLastErrorMessage);
                        }
                    }
                }
                */
            }

            if(($mode=="all") || ($mode=="end"))
            {
                // End XML tag
                $xml.= "</specimen>\n";
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
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblspecimen")."'>";
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
        return $this->id;
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
        //if($this->code == NULL) 
        //{
        //    $this->setErrorMessage("902", "Missing parameter - 'code' field is required.");
        //    return FALSE;
       // }

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->id == NULL)
                {
                    // New record
                    $sql = "insert into tblspecimen ( ";
                        if(isset($this->label))                     $sql.="label, ";
                        if(isset($this->treeID))                    $sql.="treeid, ";
                        if(isset($this->dateCollected))             $sql.="datecollected, ";
                        if(isset($this->specimenTypeID))            $sql.="specimentypeid, ";
                        if(isset($this->terminalRingID))            $sql.="terminalringid, ";
                        if(isset($this->isTerminalRingVerified))    $sql.="isterminalringverified, ";
                        if(isset($this->sapwoodCount))              $sql.="sapwoodcount, ";
                        if(isset($this->isSapwoodCountVerified))    $sql.="issapwoodcountverified, ";
                        if(isset($this->specimenQualityID))         $sql.="specimenqualityid, ";
                        if(isset($this->isSpecimenQualityVerified)) $sql.="isspecimenqualityverified, ";
                        if(isset($this->specimenContinuityID))      $sql.="specimencontinuityid, ";
                        if(isset($this->pithID))                    $sql.="pithid, ";
                        if(isset($this->isPithVerified))            $sql.="ispithverified, ";
                        if(isset($this->unmeasuredPre))             $sql.="unmeaspre, ";
                        if(isset($this->isUnmeasuredPreVerified))   $sql.="isunmeaspreverified, ";
                        if(isset($this->unmeasuredPost))            $sql.="unmeaspost, ";
                        if(isset($this->isUnmeasuredPostVerified))  $sql.="isunmeaspostverified, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if(isset($this->label))                     $sql.="'".$this->label                                          ."', ";
                        if(isset($this->treeID))                    $sql.="'".$this->treeID                                         ."', ";
                        if(isset($this->dateCollected))             $sql.="'".$this->dateCollected                                  ."', ";
                        if(isset($this->specimenTypeID))            $sql.="'".$this->specimenTypeID                                 ."', ";
                        if(isset($this->terminalRingID))            $sql.="'".$this->terminalRingID                                 ."', ";
                        if(isset($this->isTerminalRingVerified))    $sql.="'".fromPHPtoPGBool($this->isTerminalRingVerified)        ."', ";
                        if(isset($this->sapwoodCount))              $sql.="'".$this->sapwoodCount                                   ."', ";
                        if(isset($this->isSapwoodCountVerified))    $sql.="'".fromPHPtoPGBool($this->isSapwoodCountVerified)        ."', ";
                        if(isset($this->specimenQualityID))         $sql.="'".$this->specimenQualityID                              ."', ";
                        if(isset($this->isSpecimenQualityVerified)) $sql.="'".fromPHPtoPGBool($this->isSpecimenQualityVerified)     ."', ";
                        if(isset($this->specimenContinuityID))      $sql.="'".$this->specimenContinuityID                           ."', ";
                        if(isset($this->pithID))                    $sql.="'".$this->pithID                                         ."', ";
                        if(isset($this->isPithVerified))            $sql.="'".fromPHPtoPGBool($this->isPithVerified)                ."', ";
                        if(isset($this->unmeasuredPre))             $sql.="'".$this->unmeasuredPre                                  ."', ";
                        if(isset($this->isUnmeasuredPreVerified))   $sql.="'".fromPHPtoPGBool($this->isUnmeasuredPreVerified)       ."', ";
                        if(isset($this->unmeasuredPost))            $sql.="'".$this->unmeasuredPost                                 ."', ";
                        if(isset($this->isUnmeasuredPostVerified))  $sql.="'".fromPHPtoPGBool($this->isUnmeasuredPostVerified)      ."', ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblspecimen where specimenid=currval('tblspecimen_specimenid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql.="update tblspecimen set ";
                        if(isset($this->label))                     $sql.="label='"                     .$this->label                                          ."', ";
                        if(isset($this->treeID))                    $sql.="treeid='"                    .$this->treeID                                         ."', ";
                        if(isset($this->dateCollected))             $sql.="datecollected='"             .$this->dateCollected                                  ."', ";
                        if(isset($this->specimenTypeID))            $sql.="specimentypeid='"            .$this->specimenTypeID                                 ."', ";
                        if(isset($this->terminalRingID))            $sql.="terminalringid='"            .$this->terminalRingID                                 ."', ";
                        if(isset($this->isTerminalRingVerified))    $sql.="isterminalringverified='"    .fromPHPtoPGBool($this->isTerminalRingVerified)        ."', ";
                        if(isset($this->sapwoodCount))              $sql.="sapwoodcount='"              .$this->sapwoodCount                                   ."', ";
                        if(isset($this->isSapwoodCountVerified))    $sql.="issapwoodcountverified='"    .fromPHPtoPGBool($this->isSapwoodCountVerified)        ."', ";
                        if(isset($this->specimenQualityID))         $sql.="specimenqualityid='"         .$this->specimenQualityID                              ."', ";
                        if(isset($this->isSpecimenQualityVerified)) $sql.="isspecimenqualityverified='" .fromPHPtoPGBool($this->isSpecimenQualityVerified)     ."', ";
                        if(isset($this->specimenContinuityID))      $sql.="specimencontinuityid='"      .$this->specimenContinuityID                           ."', ";
                        if(isset($this->pithID))                    $sql.="pithid='"                    .$this->pithID                                         ."', ";
                        if(isset($this->isPithVerified))            $sql.="ispithverified='"            .fromPHPtoPGBool($this->isPithVerified)                ."', ";
                        if(isset($this->unmeasuredPre))             $sql.="unmeaspre='"                 .$this->unmeasuredPre                                  ."', ";
                        if(isset($this->isUnmeasuredPreVerified))   $sql.="isunmeaspreverified='"       .fromPHPtoPGBool($this->isUnmeasuredPreVerified)       ."', ";
                        if(isset($this->unmeasuredPost))            $sql.="unmeaspost='"                .$this->unmeasuredPost                                 ."', ";
                        if(isset($this->isUnmeasuredPostVerified))  $sql.="isunmeaspostverified='"      .fromPHPtoPGBool($this->isUnmeasuredPostVerified)      ."', ";
                    $sql = substr($sql, 0, -2);
                    $sql.= " where specimenid='".$this->id."'";
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
                        $this->id=$row['specimenid'];   
                        $this->createdTimeStamp=$row['createdtimestamp'];   
                        $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
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
        if($this->id == NULL) 
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

                $sql = "delete from tblspecimen where specimenid=".$this->id;

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
