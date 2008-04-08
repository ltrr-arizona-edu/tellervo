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
    var $originalTaxonLabel = NULL;
    var $subSiteID = NULL;
    var $label = NULL;
    var $latitude = NULL;
    var $longitude = NULL;
    var $precision = NULL;
    var $isLiveTree = NULL;
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
    

    function setLatitude($theLatitude)
    {
        // Set the current objects latitude 
        $this->latitude=$theLatitude;
    }
    
    function setLongitude($theLongitude)
    {
        // Set the current objects longitude 
        $this->longitude=$theLongitude;
    }
    
    function setPrecision($thePrecision)
    {
        // Set the current objects precision 
        $this->precision=$thePrecision;
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
        $sql = "select originaltaxonlabel, treeid, taxonid, subsiteid, label, X(location) as long, Y(location) as lat, precision, createdtimestamp, lastmodifiedtimestamp from tbltree where treeid=$theID";
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
                $this->originalTaxonLabel = $row['originaltaxonlabel'];
                $this->subSiteID = $row['subsiteid'];
                $this->label = $row['label'];
                $this->latitude = $row['lat'];
                $this->longitude = $row['long'];
                $this->precision = $row['precision'];
                $this->createdTimeStamp = dateFudge($row['createdtimestamp']);
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
    
    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }


    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML($mode="all")
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($mode=="all") || ($mode=="begin"))
            {
                $myTaxon = new taxon;
                $myTaxon->setParamsFromDB($this->taxonID);
                $hasHigherTaxonomy = $myTaxon->setHigherTaxonomy();

                // Only return XML when there are no errors.
                $xml = "<tree ";
                $xml.= "id=\"".$this->id."\" ";
                $xml.= "url=\"http://$domain/tree/".$this->id."\">\n ";
                
                if(isset($this->name))                  $xml.= "<name>".$this->label."</name>\n";
                if(isset($this->taxonID))               $xml.= "<validatedTaxonLabel>".$myTaxon->getLabel()."</validatedTaxonLabel>\n";
                if(isset($this->originalTaxonLabel))    $xml.= "<originalTaxonLabel>".$this->originalTaxonLabel."</originalTaxonLabel>\n";

                if($hasHigherTaxonomy)
                {
                    $xml.=$myTaxon->getHigherTaxonXML('kingdom');   
                    $xml.=$myTaxon->getHigherTaxonXML('phylum');   
                    $xml.=$myTaxon->getHigherTaxonXML('class');   
                    $xml.=$myTaxon->getHigherTaxonXML('order');   
                    $xml.=$myTaxon->getHigherTaxonXML('family');   
                    $xml.=$myTaxon->getHigherTaxonXML('genus');   
                    $xml.=$myTaxon->getHigherTaxonXML('species');   
                }

                if(isset($this->latitude))              $xml.= "<latitude>".$this->latitude."</latitude>\n";
                if(isset($this->longitude))             $xml.= "<longitude>".$this->longitude."</longitude>\n";
                if(isset($this->precision))             $xml.= "<precision>".$this->precision."</precision>\n";
                if(isset($this->isLiveTree))            $xml.= "<isLiveTree>".$this->isLiveTree."</isLiveTree>\n";
                if(isset($this->createdTimeStamp))      $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                if(isset($this->lastModifiedTimeStamp)) $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";
                
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
                    $xml.="<references>\n";
                    foreach($this->specimenArray as $value)
                    {
                        $mySpecimen = new specimen();
                        $success = $mySpecimen->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$mySpecimen->asXML("brief");
                        }
                        else
                        {
                            $myMetaHeader->setErrorMessage($mySpecimen->getLastErrorCode, $mySpecimen->getLastErrorMessage);
                        }
                    }
                    $xml.="</references>\n";
                }
            }

            if(($mode=="all") || ($mode=="end"))
            {
                // End XML tag
                $xml.= "</tree>\n";
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
        if($this->label == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'label' field is required.");
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
                    // New Record
                    $sql = "insert into tbltree ( ";
                        if (isset($this->taxonID))                                  $sql.= "taxonid, ";
                        if (isset($this->subSiteID))                                $sql.= "subsiteid, ";
                        if (isset($this->label))                                    $sql.= "label, ";
                        if (isset($this->precision))                                $sql.= "precision, ";
                        if (isset($this->isLiveTree))                               $sql.= "islivetree, ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "location, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if (isset($this->taxonID))                                  $sql.= "'".$this->taxonID.   "', ";
                        if (isset($this->subSiteID))                                $sql.= "'".$this->subSiteID. "', ";
                        if (isset($this->label))                                    $sql.= "'".$this->label.     "', ";
                        if (isset($this->precision))                                $sql.= "'".$this->precision. "', ";
                        if (isset($this->isLiveTree))                               $sql.="'".fromPHPtoPGBool($this->isLiveTree)."', ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "setsrid(makepoint(".$this->longitude.", ".$this->latitude."), 4326), ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tbltree where treeid=currval('tbltree_treeid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tbltree set ";
                        if (isset($this->taxonID))                                  $sql.= "taxonid='".    $this->taxonID    ."', ";
                        if (isset($this->subSiteID))                                $sql.= "subsiteid='".  $this->subSiteID  ."', ";
                        if (isset($this->label))                                    $sql.= "label='".      $this->label      ."', ";
                        if (isset($this->precision))                                $sql.= "precision='".  $this->precision  ."', ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "location=setsrid(makepoint(".$this->longitude.", ".$this->latitude."), 4326), ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql .= " where treeid=".$this->id;
                }

                // Run SQL command
                if ($sql)
                {
                    // Run SQL 
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
                        switch($PHPErrorCode)
                        {
                        case 23514:
                                // Foreign key violation
                                $this->setErrorMessage("909", "Check constraint violation.  The location precision specified must be a postive integer.");
                                break;
                        default:
                                // Any other error
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        }
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
                        $this->id=$row['treeid'];   
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

                $sql = "delete from tbltree where treeid=".$this->id;

                // Run SQL command
                if ($sql)
                {
                    // Run SQL 
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
                        switch($PHPErrorCode)
                        {
                        case 23503:
                                // Foreign key violation
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all specimens associated with a tree before deleting the tree itself.");
                                break;
                        default:
                                // Any other error
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                        }
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
