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
require_once('inc/treeNote.php');
require_once('inc/specimen.php');
require_once('inc/taxon.php');

class tree 
{
    var $id = NULL;
    var $taxonID = NULL;
    var $subSiteID = NULL;
    var $label = NULL;
    var $latitude = NULL;
    var $longitude = NULL;
    var $precision = NULL;
    var $specimenArray = array();
    var $treeNoteArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;


    var $parentXMLTag = "trees"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function tree()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/

    function setLabel($theLabel)
    {
        // Set the current objects label
        $this->label=$theLabel;
    }
    
    function setTaxonID($theTaxonID)
    {
        // Set the current objects taxonid
        $this->taxonID=$theTaxonID;
    }

    function setSubSiteID($theSubSiteID)
    {
        // Set the current objects subsite ID
        $this->subSiteID=$theSubSiteID;
    }

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function setLocality($theLat, $theLong, $thePrecision)
    {
        // Set the location of the current object
        $this->latitude = $theLat;
        $this->longitude = $theLong;
        $this->precision = $thePrecision;
    }

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $this->id=$theID;
        $sql = "select treeid, taxonid, subsiteid, label, X(location) as long, Y(location) as lat, precision, createdtimestamp, lastmodifiedtimestamp from tbltree where treeid=$theID";
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
                $this->taxonID = $row['taxonid'];
                $this->subSiteID = $row['subsiteid'];
                $this->label = $row['label'];
                $this->latitude = $row['lat'];
                $this->longitude = $row['long'];
                $this->precision = $row['precision'];
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
        // Specimen

        global $dbconn;

        $sql  = "select treenoteid from tbltreetreenote where treeid=".$this->id;
        $sql2 = "select specimenid from tblspecimen where treeid=".$this->id;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->treeNoteArray, $row['treenoteid']);
            }

            $result = pg_query($dbconn, $sql2);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->specimenArray, $row['specimenid']);
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

    function asXML()
    {
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            $myTaxon = new taxon;
            $myTaxon->setParamsFromDB($this->taxonID);

            // Only return XML when there are no errors.
            $xml.= "<tree ";
            $xml.= "id=\"".$this->id."\" ";
            $xml.= "label=\"".$this->label."\" ";
            $xml.= "taxon=\"".$myTaxon->getLabel()."\" ";
            $xml.= "latitude=\"".$this->latitude."\" ";
            $xml.= "longitude=\"".$this->longitude."\" ";
            $xml.= "precision=\"".$this->precision."\" ";
            $xml.= "createdTimeStamp=\"".$this->createdTimeStamp."\" ";
            $xml.= "lastModifiedTimeStamp=\"".$this->lastModifiedTimeStamp."\" ";
            $xml.= ">";
            
            // Include tree notes if present
            if ($this->treeNoteArray)
            {
                foreach($this->treeNoteArray as $value)
                {
                    $myTreeNote = new treeNote();
                    $success = $myTreeNote->setParamsFromDB($value);

                    if($success)
                    {
                        $xml.=$myTreeNote->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setErrorMessage($myTreeNote->getLastErrorCode, $myTreeNote->getLastErrorMessage);
                    }
                }
            }

            // Include specimens if present
            if ($this->specimenArray)
            {
                foreach($this->specimenArray as $value)
                {
                    $mySpecimen = new specimen();
                    $success = $mySpecimen->setParamsFromDB($value);

                    if($success)
                    {
                        $xml.=$mySpecimen->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setErrorMessage($mySpecimen->getLastErrorCode, $mySpecimen->getLastErrorMessage);
                    }
                }
            }

            // End XML tag
            $xml.= "</tree>\n";
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
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tbltree")."'>";
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
                    $sql = "insert into tbltree (name, code) values ('".$this->name."', '".$this->code."')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tbltree set name='".$this->name."', code='".$this->code."' where treeid=".$this->id;
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

                $sql = "delete from tbltree where treeid=".$this->id;

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
