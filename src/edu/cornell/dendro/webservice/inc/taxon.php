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

class taxon extends taxonEntity implements IDBAccessor
{

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
    /*FUNCTIONS*/
    /***********/

    /**
     * Write this taxon to the database
     *
     * @return unknown
     */
    function writeToDB()
    {
        
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

    function validateRequestParams($paramsClass, $crudMode)
    {

        return false;
    }
    function deleteFromDB()
    {
        return false;
    }
// End of Class
} 
?>
