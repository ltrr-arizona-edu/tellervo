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

class taxon 
{
    var $id = NULL;
    var $parentID = NULL;
    var $label = NULL;
    var $colID = NULL;
    var $colParentID = NULL;
    var $taxonRank = NULL;
    var $parentXMLTag = "taxonDictionary"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;
    var $kingdom = NULL;
    var $phylum = NULL;
    var $class = NULL;
    var $order = NULL;
    var $family = NULL;
    var $genus = NULL;
    var $species = NULL;
   

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function taxon()
    {
        // Constructor for this class.
        $this->isStandard = FALSE;
    }

    /***********/
    /* SETTERS */
    /***********/

    function setLabel($theLabel)
    {
        // Set the current objects note.
        $this->label=$theLabel;
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
        $sql = "select tlkptaxon.taxonid, tlkptaxon.parenttaxonid, tlkptaxon.colID, tlkptaxon.colParentID, tlkptaxonrank.taxonrank, tlkptaxon.label as label from tlkptaxon, tlkptaxonrank  where tlkptaxon.taxonid=$theID and tlkptaxonrank.taxonrankid=tlkptaxon.taxonrankid";
        //echo $sql;
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
                $this->colID = $row['colid'];
                $this->colParentID = $row['colparentid'];
                $this->taxonRank = $row['taxonrank'];
                $this->taxonRank = $row['taxonrank'];
                $this->parentID = $row['parenttaxonid'];
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
    
    function taxonRecordExists($colID)
    {
        global $dbconn;
        $sql="select count(*) from tlkptaxon where colid=$colID";
        
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);    
            while ($row = pg_fetch_array($result))
            {
                if($row['count']>0)
                {
                    return True;
                }
                else
                {   
                    return False;
                }
            }
        }
    }
    
    function setHigherTaxonomy()
    {
        global $dbconn;
        
        $sql = "select * from cpgdb.qrytaxonomy(".$this->id.")";
        //echo $sql;
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
                $this->kingdom  = $row['kingdom'];
                $this->phylum   = $row['phylum'];
                $this->class    = $row['class'];
                $this->order    = $row['txorder'];
                $this->family   = $row['family'];
                $this->genus    = $row['genus'];
                $this->species  = $row['species'];
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
    


    function setParamsFromCoL($CoLID)
    {
        $doneStuff = false;
        $colURL="http://webservice.catalogueoflife.org/annual-checklist/2008/search.php?response=full&id=$CoLID";
        $colXML = simplexml_load_file($colURL);
        if($colXML['total_number_of_results']==1)
        {
            if($colXML->result->name_status=='accepted name')
            {
                // Write Higher taxon records
                $parentID = NULL;
                foreach ($colXML->result->classification->taxon as $currentTaxon)
                {
                    if(!($this->taxonRecordExists($currentTaxon->id)))
                    {
                        $this->setParamsWithParents($currentTaxon->id, $parentID, $currentTaxon->rank, $currentTaxon->name);
                        $this->writeToDB();
                        $doneStuff = true;
                    }
                    $parentID = $currentTaxon->id;
                }

                // Write requested taxon details
                if(!($this->taxonRecordExists($colXML->result->id)))
                {
                    if($colXML->result->rank=="Infraspecies")
                    {
                        // This needs to change
                        $rank = $colXML->result->infraspecies_marker;
                    }
                    else
                    {
                        $rank = $colXML->result->rank;
                    }
                    $this->setParamsWithParents($colXML->result->id, $parentID, $colXML->result->rank, $colXML->result->name." ".$colXML->result->author);
                    $this->writeToDB();
                    $doneStuff = true;
                }
            }
            else
            {
                $this->setErrorMessage("901", "The requested taxon is a synonym.  Only accepted taxa can be added.");
                return false;
            }
    
        }
        else
        {   
            // More than one taxon returned
            $this->setErrorMessage("901", "The requested taxon is not unique.");
            return false;
        }

        if ($doneStuff)
        {
            return true;
        }
        else
        {
            $this->setErrorMessage("906", "Record already exists.");
            return false;
        }

    }
        
    function setParamsWithParents($colID, $colParentID, $taxonRank, $label)
    {
        global $dbconn;

        // Lookup taxon rank id
        $sql = "select taxonrankid from tlkptaxonrank where taxonrank ilike '$taxonRank'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                $taxonRankID = $row['taxonrankid'];
            }
        }
        else
        {
            $this->setErrorMessage("701", "Error determining rank. ".pg_result_error($result)."--- SQL was $sql");
        }

        // Lookup parent taxon id
        if ($colParentID)
        {
            $sql = "select taxonid from tlkptaxon where colid=$colParentID";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Get all tree note id's for this tree and store 
                    $parentTaxonID = $row['taxonid'];
                }
            }
            else
            {
                $this->setErrorMessage("701", "Error determining parent id. ".pg_result_error($result)."--- SQL was $sql");
            }
        }

        if ($colParentID)
        {
            $this->parentID = $parentTaxonID;
            $this->label = $label;
            $this->colID = $colID;
            $this->colParentID = $colParentID;
            $this->taxonRankID = $taxonRankID;
        }
        else
        {
            $this->label = $label;
            $this->colID = $colID;
            $this->taxonRank = $taxonRankID;
        }
    }


    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML()
    {
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
            //
            // TO DO - Sort out XML special characters in XML.  
            $xml= "<taxon id=\"".$this->id."\" parentID=\"".$this->parentID."\" colID=\"".$this->colID."\" colParentID=\"".$this->colParentID."\" taxonRank=\"".$this->taxonRank."\">".escapeXMLChars($this->label)."</taxon>\n";
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getLabel()
    {
        return $this->label;
    }
    
    function getHigherTaxonXML($theRank)
    {
        $xml = "<higherTaxon rank=\"$theRank\" >";
        switch($theRank)
        {
            case "kingdom":
                return $xml.$this->kingdom."</higherTaxon>";
            case "phylum":
                return $xml.$this->phylum."</higherTaxon>";
            case "class":
                return $xml.$this->class."</higherTaxon>";
            case "order":
                return $xml.$this->order."</higherTaxon>";
            case "family":
                return $xml.$this->family."</higherTaxon>";
            case "genus":
                return $xml.$this->genus."</higherTaxon>";
            default:
                return false;
        }
    }

    function getHigherTaxon($theRank)
    {
        switch($theRank)
        {
            case "kingdom":
                return $this->kingdom;
            case "phylum":
                return $this->phylum;
            case "class":
                return $this->class;
            case "order":
                return $this->order;
            case "family":
                return $this->family;
            case "genus":
                return $this->genus;
            default:
                return false;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag.">";
        return $xml;
    }

    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
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

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set or colID has been set, then we assume that we are writing a new record to the DB.  Otherwise updating.
                if( ($this->id == NULL) || (isset($this->colID)) ) 
                {
                    // New record
                    $sql = "insert into tlkptaxon (";
                        if(isset($this->colID)) $sql.="colid, ";
                        if(isset($this->colParentID)) $sql.="colparentid, ";
                        if(isset($this->taxonRankID)) $sql.="taxonrankid, ";
                        if(isset($this->parentID)) $sql.="parenttaxonid, ";
                        if(isset($this->label)) $sql.="label";
                    $sql.= ") VALUES (";
                        if(isset($this->colID)) $sql.="'".$this->colID."', ";
                        if(isset($this->colParentID)) $sql.="'".$this->colParentID."', "; 
                        if(isset($this->taxonRankID)) $sql.="'".$this->taxonRankID."', ";
                        if(isset($this->parentID)) $sql.="'".$this->parentID."', ";
                        if(isset($this->label)) $sql.="'".$this->label."')";
                    $sql2 = "select * from tlkptaxon where taxonid=currval('tlkptaxon_taxonid_seq')";
                        
                }
                else
                {
                    // Updating DB
                    $this->setErrorMessage("104", "Updating taxa not currently supported.");
                    return false;  
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
                    else
                    {
                        // Retrieve automated field values when a new record has been inserted
                        if ($sql2)
                        {
                            // Run SQL
                            $result = pg_query($dbconn, $sql2);
                            while ($row = pg_fetch_array($result))
                            {
                                $this->id=$row['taxonid'];   
                            }
                        }
                        return true;
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
    }
/*
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

                $sql = "delete from tlkpspecimentype where specimentypeid=".$this->id;

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
 */
// End of Class
} 
?>
