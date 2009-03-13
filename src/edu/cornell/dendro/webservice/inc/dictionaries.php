<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
 * *******************************************************************
 */
require_once('dbhelper.php');
require_once('inc/dbEntity.php');

require_once("inc/note.php");
require_once("inc/taxon.php");
require_once("inc/region.php");
require_once("inc/securityUser.php");

class dictionaries
{
    var $parentXMLTag = "dictionaries"; 
    var $xmldata = NULL;
    var $lastErrorCode = NULL;
    var $lastErrorMessage = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        // Constructor for this class.
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

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                return true;
            
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request for dictionaries");
                return false;
        }
    }

    function setParamsFromDB()
    {
        global $dbconn;
        
        $xmldata = "";
        
        $dictItems = array('objectType', 'elementType', 'sampleType', 'coverageTemporal', 'coverageTemporalFoundation', 
        				  'locationType', 'elementAuthenticity', 'elementShape', 'sapwood', 'heartwood', 'measurementVariable',
        				  'datingType', 'taxon', 'region', 'readingNote');
       
        
		// Standard dictionar items
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            foreach($dictItems as $item)
            {
                if($item=="region")
                {
                	// Unique case
                    $sql="select distinct(tblobjectregion.regionid) as id, tblregion.regionname as value from tblobjectregion, tblregion where tblobjectregion.regionid=tblregion.regionid";
                }
                elseif($item=="readingNote")
                {
                	// Unique case
                	$sql="select readingnoteid as id, note as value from tlkpreadingnote where isstandard='t'";
                }
                elseif($item=="securityUser")
                {
                	// Unique case
                    $sql = "select securityuserid as id, as value from tbl".strtolower($item)." where ".strtolower($item)."id>0 order by ".strtolower($item)."id"; 
                }
                elseif($item=="taxon")
                {
                	// Unique case
                	$sql = "SELECT taxon.taxonid, taxon.label, taxon.parentTaxonID, taxon.colID, taxon.colParentID, rank.taxonrank
                			FROM tlkpTaxon taxon
							INNER JOIN tlkpTaxonRank rank on rank.taxonrankid=taxon.taxonrankid";
                }
                else
                {
                	// Looking up in tlkp style table
                    $sql = "select ".strtolower($item)."id as id, ".strtolower($item)." as value from tlkp".strtolower($item)." where ".strtolower($item)."id>0 order by ".strtolower($item)."id"; 
                }
                                
                $xmldata.= "<".$item."Dictionary>\n";
                
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                	if($item=='taxon')
                	{
				    	global $taxonomicAuthorityEdition;
				    	$xmldata .= "<tridas:taxon normalStd=\"$taxonomicAuthorityEdition\" normalId=\"".$row['colid']."\" normal=\"".dbHelper::escapeXMLChars($row['label'])."\"/>\n";    	
                	}
                	else
                	{
                    	$xmldata.= "<".$item." normalStd=\"Corina\" normalId=\"".$row['id']."\" normal=\"".dbHelper::escapeXMLChars($row['value'])."\"/>\n";
                	}
                }
                $xmldata.= "</".$item."Dictionary>\n";
            }
        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database");
        }

        
        // More complex dictionary items
        $dictItemsWithClasses = array('securityUser');
        
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            foreach($dictItemsWithClasses as $item)
            {
                switch($item)
                {
            		case "securityUser": 
            			$sql="select securityuserid as id from tblsecurityuser";
            			$myObj = new securityUser();
            			break;
            		case "taxon":
                       	$sql="select taxonid as id, label as value from tlkptaxon order by taxonid";
                       	$myObj = new taxon();
                       	break;
                }
            		
		
                            
                $xmldata.= "<".$item."Dictionary>\n";
                
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                	$success = $myObj->setParamsFromDB($row['id']);
                	
                	if($success)
                	{
                		$xmldata.=$myObj->asXML();
                	}
                	else
                	{
                		
                	}
                }
                $xmldata.= "</".$item."Dictionary>\n";
                unset($myObj);
            }
        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database");
        }       
       
        // Put xmldata into class variable
        if($xmldata!=NULL)
        {
            $this->xmldata=$xmldata;
            return true;
        }
        else
        {
            return false;
        }
            
        
    }
    
    function setChildParamsFromDB()
    {
        return true;
    }

    /***********/
    /*ACCESSORS*/
    /***********/
    
    function asXML($mode="all")
    {
        if(isset($this->xmldata))
        {
            return $this->xmldata;
        }
        else
        {
            return false;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblelement")."'>";
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

  


// End of Class
} 
?>
