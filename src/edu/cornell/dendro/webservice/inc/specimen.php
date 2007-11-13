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
    var $terminalRingVerified = NULL;
    var $sapwoodCount = NULL;
    var $sapwoodCountVerified = NULL;
    var $specimenQualityID = NULL;
    var $specimenQualityVerified = NULL;
    var $specimenContinuityID = NULL;
    var $specimenContinuityVerified = NULL;
    var $pithID = NULL;
    var $pithVerified = NULL;
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
        $this->name=$theLabel;
    }
    
    function setTreeID($theTreeID)
    {
        // Set the current objects note.
        $this->treeID=$theTreeID;
    }

    function setSpecimenTypeID($specimenType)
    {
        $mySpecimenType = new specimenType;
        $mySpecimenType->setParamsFromLabel($specimenType);
        $this->specimenTypeID=$mySpecimenType->getID();
    }

    function setTerminalRingID($terminalRing)
    {
        $myTerminalRing = new terminalRing;
        $myTerminalRing->setParamsFromLabel($terminalRing);
        $this->terminalRingID=$myTerminalRing->getID();
    }

    function setSpecimenQualityID($specimenQuality)
    {
        $mySpecimenQuality = new specimenQuality;
        $mySpecimenQuality->setParamsFromLabel($specimenQuality);
        $this->specimenQualityID=$mySpecimenQuality->getID();
    }

    function setSpecimenContinuityID($specimenContinuity)
    {
        $mySpecimenContinuity = new specimenContinuity;
        $mySpecimenContinuity->setParamsFromLabel($specimenContinuity);
        $this->$specimenContinuityID=$mySpecimenContinuity->getID();
    }

    function setPithID($pith)
    {
        $myPith = new specimenContinuity;
        $myPith->setParamsFromLabel($pith);
        $this->$pithID=$myPith->getID();
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
                $xml.= "specimenType=\"".$mySpecimenType->getLabel()."\" ";
                $xml.= "terminalRing=\"".$myTerminalRing->getLabel()."\" ";
                $xml.= "isTerminalRingVerified=\"".fromPGtoStringBool($this->isTerminalRingVerified)."\" ";
                $xml.= "sapwoodCount=\"".$this->sapwoodCount."\" ";
                $xml.= "isSapwoodCountVerified=\"".fromPHPtoStringBool($this->isSapwoodCountVerified)."\" ";
                $xml.= "specimenQuality=\"".$mySpecimenQuality->getLabel()."\" ";
                $xml.= "isSpecimenQualityVerified=\"".fromPHPtoStringBool($this->isSpecimenQualityVerified)."\" ";
                $xml.= "specimenContinuity=\"".$mySpecimenContinuity->getLabel()."\" ";
                $xml.= "isSpecimenContinuityVerified=\"".fromPHPtoStringBool($this->isSpecimenContinuityVerified)."\" ";
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
        if($this->name == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            return FALSE;
        }
        if($this->code == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'code' field is required.");
            return FALSE;
        }

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
                    $sql = "insert into tblspecimen (label, treeid, datecollected, specimentypeid, terminalringid, isterminalringverified, sapwoodcount, issapwoodcountverified, specimenqualityid, isspecimenqualityverified, specimencontinuityid, pithid, ispithverified, unmeaspre, isunmeaspreverified, unmeaspost, isunmeaspostverified)";
                    $sql.= " values (";
                    $sql.= "'".$this->label."', ";
                    $sql.= $this->treeid." ,";
                    $sql.= "'".$this->dateCollected."', ";
                    $sql.= $this->specimentypeid." ,";
                    $sql.= $this->terminalringid." ,";
                    $sql.= "'".fromPHPtoPGBool($this->isterminalringverified)."' ,";
                    $sql.= $this->sapwoodcount." ,";
                    $sql.= "'".fromPHPtoPGBool($this->issapwoodcountverified)."' ,";
                    $sql.= $this->specimenqualityid." ,";
                    $sql.= "'".fromPHPtoPGBool($this->isspecimenqualityverified)."' ,";
                    $sql.= $this->specimencontinuityid." ,";
                    $sql.= $this->pithid." ,";
                    $sql.= "'".fromPHPtoPGBool($this->ispithverified)."' ,";
                    $sql.= $this->unmeaspre." ,";
                    $sql.= "'".fromPHPtoPGBool($this->isunmeaspreverified)."' ,";
                    $sql.= $this->unmeaspost." ,";
                    $sql.= "'".fromPHPtoPGBool($this->isunmeaspostverified)."' ,";
                    $sql.=")";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblspecimen set ";
                    $sql.= "label='".$this->label."', ";
                    $sql.= "treeid=".$this->treeid." ,";
                    $sql.= "dateCollected='".$this->dateCollected."', ";
                    $sql.= "specimentypeid=".$this->specimentypeid." ,";
                    $sql.= "terminalringid=".$this->terminalringid." ,";
                    $sql.= "isterminalringverified='".fromPHPtoPGBool($this->isterminalringverified)."' ,";
                    $sql.= "sapwoodcount=".$this->sapwoodcount." ,";
                    $sql.= "issapwoodcountverified='".fromPHPtoPGBool($this->issapwoodcountverified)."' ,";
                    $sql.= "specimenqualityid=".$this->specimenqualityid." ,";
                    $sql.= "isspecimenqualityverified='".fromPHPtoPGBool($this->isspecimenqualityverified)."' ,";
                    $sql.= "specimencontinuityid=".$this->specimencontinuityid." ,";
                    $sql.= "pithid=".$this->pithid." ,";
                    $sql.= "ispithverified='".fromPHPtoPGBool($this->ispithverified)."' ,";
                    $sql.= "unmeaspre=".$this->unmeaspre." ,";
                    $sql.= "usunmeaspreverified='".fromPHPtoPGBool($this->isunmeaspreverified)."' ,";
                    $sql.= "unmeaspost=".$this->unmeaspost." ,";
                    $sql.= "isunmeaspostverified='".fromPHPtoPGBool($this->isunmeaspostverified)."' ,";
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
