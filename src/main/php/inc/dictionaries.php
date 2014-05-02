<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */
require_once('dbhelper.php');
require_once('inc/dbEntity.php');

require_once("inc/readingNote.php");
require_once("inc/taxon.php");
require_once("inc/region.php");
require_once("inc/securityUser.php");
require_once("inc/securityGroup.php");
require_once("inc/box.php");
require_once("inc/domain.php");

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
        global $firebug;
        
        $xmldata = "";
        
        $dictItems = array('objectType', 'elementType', 'sampleType', 'coverageTemporal', 'coverageTemporalFoundation', 
        				  'elementAuthenticity', 'datingType', 'taxon', 'configuration', 'sampleStatus');
        //$dictItems = array('elementType',  
        //				  'elementAuthenticity', 'datingType', 'configuration');
       
        
		// Standard dictionary items
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
                elseif($item=="taxon")
                {
                	// Unique case
                	$sql = "SELECT taxon.taxonid, taxon.label, taxon.parentTaxonID, taxon.colID, taxon.colParentID, rank.taxonrank
                			FROM tlkpTaxon taxon
							INNER JOIN tlkpTaxonRank rank on rank.taxonrankid=taxon.taxonrankid";
                }
                elseif($item=='configuration')
                {
                }
                else
                {
                	// Looking up in tlkp style table
                    $sql = "select ".strtolower($item)."id as id, ".strtolower($item)." as value from tlkp".strtolower($item)." where ".strtolower($item)."id>0 order by ".strtolower($item)."id"; 
                }
                                
                $xmldata.= "<".$item."Dictionary>\n";
                
                if ($item=='configuration')
                {
                	global $labname;
                	global $labacronym;
                	$xmldata.="<configuration key=\"lab.name\" value=\"$labname\" />\n";
                	$xmldata.="<configuration key=\"lab.acronym\" value=\"$labacronym\" />\n";
                }
                else
                {
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
	                    	$xmldata.= "<".$item." normalStd=\"Tellervo\" normalId=\"".$row['id']."\" normal=\"".dbHelper::escapeXMLChars($row['value'])."\"/>\n";
	                	}
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
        $dictItemsWithClasses = array('securityUser', 'securityGroup', 'readingNote', 'box', 'domain');
        //$dictItemsWithClasses = array('securityGroup', 'box');
        
        
        
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            foreach($dictItemsWithClasses as $item)
            {
            	$firebug->log($item, "Dictionary");
            	 
                switch($item)
                {
            		case "securityUser": 
            			$sql="select securityuserid as id from tblsecurityuser";
            			$myObj = new securityUser();
            			break;     
            		case "securityGroup":            			
            			$sql="select securitygroupid as id from tblsecuritygroup";
            			$myObj = new securityGroup();
            			break;               			
            		case "readingNote": 
            			$sql="select readingnoteid as id from tlkpreadingnote where vocabularyid>0";
            			$myObj = new readingNote();
            			break;              			     			
            		case "taxon":
            			 
                       	$sql="select taxonid as id, label as value from tlkptaxon order by taxonid";
                       	$myObj = new taxon();
                       	break;
            		case "box":
            			$sql="select boxid as id, title as value from tblbox order by createdtimestamp"; 	
                      	$myObj = new box();
                      	break;
                    case "domain":
                    	 
                      	$sql="select domainid as id, domain as value, prefix from tlkpdomain";
                      	$myObj = new domain();
                      	break;
                }
            		
		
                            
                $xmldata.= "<".$item."Dictionary>\n";
                
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                	
                	$success = $myObj->setParamsFromDB($row['id']);
                	
                	if ($item=="securityUser"){
                		$myObj->setChildParamsFromDB();
                		$xmldata.=$myObj->asXML("comprehensive");
                	}
  					else if ($item=='securityGroup')
  					{
  						$myObj->setChildParamsFromDB();
  						$xmldata.=$myObj->asXML("comprehensive");
  					}
                	else
                	{ 	   
						//$firebug->log($myObj->asXML(), "XML for ".$item." with id ".$row['id']);
                		
	                	if($success)
	                	{
	                		$xmldata.=$myObj->asXML("minimal");
	                	}
	                	else
	                	{
	                		//$firebug->log($myObj->getLastErrorMessage(), "Error getting XML");
	                	}
                	}
                }
                

                $xmldata.= "</".$item."Dictionary>\n";
                unset($myObj);
            }
            
            // add wmsServer entries
            $xmldata.= "<wmsServerDictionary>\n";
            $sql="select name, url from tlkpwmsserver order by name"; 
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {	
             	$xmldata.= "<wmsServer name=\"".$row['name']."\" url=\"".$row['url']."\"/>\n";
            }
            $xmldata.= "</wmsServerDictionary>\n";
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
