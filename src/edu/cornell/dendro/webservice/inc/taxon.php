<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
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
        
        $this->setID($theID);
        $sql = "SELECT taxon.taxonid, taxon.label, taxon.parentTaxonID, taxon.colID, taxon.colParentID, rank.taxonrank
				FROM tlkpTaxon taxon
				INNER JOIN tlkpTaxonRank rank on rank.taxonrankid=taxon.taxonrankid where taxonid=".pg_escape_string($this->getID());
        //echo $sql;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified taxon ID");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->setLabel($row['label']);
                $this->setCoLID($row['colid']);
                $this->setCoLParentID($row['colparentid']);
                $this->setTaxonRank($row['taxonrank']);
                $this->setParentID($row['parenttaxonid']);
                //$this->setHigherTaxonomy();
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

    
	/**
	 * Set the current taxon details using the Catalogue of Life dictionary.
	 *
	 * @param String $CoLID - CoL ID 
	 * @param String $CoLNormalName - CoL normalised name for belts and braces check
	 * @param String $webservice - either 'local' or 'remote'
	 * @return Boolean
	 */
    function setParamsFromCoL($CoLID, $CoLNormalName, $webservice="local")
    {
    	if($webservice=='local')
    	{
	        // Set the current objects parameters from the database
	
	        global $dbconn;
	        
	        $sql = "SELECT taxonid, colid, label 
	        		FROM tlkptaxon 
	        		WHERE colid=".pg_escape_string($CoLID)." 
	        		AND label='".pg_escape_string($CoLNormalName)."'";
	        //echo $sql;
	        $dbconnstatus = pg_connection_status($dbconn);
	        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	        {
	            pg_send_query($dbconn, $sql);
	            $result = pg_get_result($dbconn);
	            if(pg_num_rows($result)==0)
	            {
	                // No records match the id specified
	                $this->setErrorMessage("903", "Either no record matches the specified catalogue of life ID or the normalised name you gave is not consistent with the Catalogue of Life");
	                return FALSE;
	            }
	            else
	            {
                	$row = pg_fetch_array($result);	            	
					
                	$this->setParamsFromDB($row['taxonid']);
					
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
    		$this->setErrorMessage("702", "Setting taxon parameters using the remote Cataloge of life webservice has not yet been implemented.");
    	}
    	

    }  
        
    
    
        
    function setParamsWithParents($colID, $colParentID, $taxonRank, $label)
    {
        global $dbconn;

        // Lookup taxon rank id
        $sql = "SELECT taxonrankid 
        		FROM tlkptaxonrank 
        		WHERE taxonrank ilike '".pg_escape_string($taxonRank)."'";
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
            $sql = "SELECT taxonid 
            		FROM tlkptaxon 
            		WHERE colid=".pg_escape_string($colParentID);
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

   function setHigherTaxonomy()
    {
        global $dbconn;
        
        $sql = "SELECT * FROM cpgdb.qrytaxonomy(".pg_escape_string($this->getID()).")";
        //echo $sql;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified id when looking up higher taxonomy");
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


    function asXML()
    {
    	global $taxonomicAuthorityEdition;
    	$xml = "<tridas:taxon normalStd=\"$taxonomicAuthorityEdition\" normalId=\"".$this->getCoLID()."\" normal=\"".$this->getLabel()."\">".$this->getOriginalTaxon()."</tridas:taxon>\n";    	
    	return $xml;
    }
    
    function getHigherTaxonomyXML()
    {
    	$xml = NULL;
 		$xml.= $this->getHigherTaxonXML('kingdom')."\n";   
        $xml.= $this->getHigherTaxonXML('phylum')."\n";   
        $xml.= $this->getHigherTaxonXML('class')."\n";   
        $xml.= $this->getHigherTaxonXML('order')."\n";   
        $xml.= $this->getHigherTaxonXML('family')."\n";   
        $xml.= $this->getHigherTaxonXML('genus')."\n";   
        $xml.= $this->getHigherTaxonXML('species')."\n";  
    	
    	return $xml;    	
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
        
        if ($this->getName()==NULL) $this->setErrorMessage("902", "Missing parameter - 'name' field is required."); return FALSE;
        

        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode()== NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set or colID has been set, then we assume that we are writing a new record to the DB.  Otherwise updating.
                if( ($this->getID() == NULL) || ($this->getCoLID()!=NULL) ) 
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
