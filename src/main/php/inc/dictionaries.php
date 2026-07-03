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
    var $cacheVersion = NULL;

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

        $cacheVersion = $this->getDictionaryCacheVersion();
        if($cacheVersion!==FALSE)
        {
            $cachedXML = $this->readDictionaryCache($cacheVersion);
            if($cachedXML!==FALSE)
            {
                $this->cacheVersion = $cacheVersion;
                $this->xmldata = $cachedXML;
                $firebug->log($cacheVersion, "Dictionary cache hit");
                return true;
            }
            $firebug->log($cacheVersion, "Dictionary cache miss");
        }
        
        $xmldata = "";
        
        //$dictItems = array('objectType', 'elementType', 'sampleType', 'coverageTemporal', 'coverageTemporalFoundation', 
        //				  'elementAuthenticity', 'datingType', 'taxon', 'configuration', 'sampleStatus');
        $dictItems = array('projectType', 'objectType', 'elementType', 'sampleType', 'coverageTemporal', 'coverageTemporalFoundation', 
        				  'elementAuthenticity', 'datingType', 'taxon', 'configuration', 'sampleStatus', 'userDefinedField', 'userDefinedTerm', 'projectCategory');
       
        
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
                elseif($item=="userDefinedField")
                {
                	
                }
                elseif ($item=="userDefinedTerm")
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
                else if($item=='userDefinedField')
                {
                	// Run SQL
                	$sql = "SELECT dictionarykey, fieldname, longfieldname, datatype, attachedto, description FROM tlkpuserdefinedfield";
                	$result = pg_query($dbconn, $sql);
                	while ($row = pg_fetch_array($result))
                	{
                		switch ($row['attachedto'])
                		{
                			case 1:
                				$attachedto = 'project';
                				break;
                			case 2:
                				$attachedto = 'object';
                				break;
               				case 3:
               					$attachedto = 'element';
               					break;                	
               				case 4:
               					$attachedto = 'sample';
               					break;               					
          					case 5:
           						$attachedto = 'radius';
           						break;
           					case 6:
           						$attachedto = 'series';
           						break;           						
                		}
                		$xmldata .= "<userDefinedField name=\"userDefinedField.".$row['fieldname']."\" longfieldname=\"".$row['longfieldname']."\" datatype=\"".$row['datatype']."\" attachedto=\"".$attachedto."\"  description=\"".$row['description']."\"  ";
                		if($row['dictionarykey']!=null)
                		{
                			$xmldata.= " dictionarykey=\"".$row['dictionarykey']."\"";
                		}
                		$xmldata .= " />\n";
                	}
                }
                else if ($item=='userDefinedTerm')
                {
                	$sql = "SELECT * FROM tlkpuserdefinedterm";
                	$result = pg_query($dbconn, $sql);
                	while ($row = pg_fetch_array($result))
                	{
                		
                		$xmldata .= "<userDefinedTerm term=\"".$row['term']."\" id=\"".$row['userdefinedtermid']."\" dictionarykey=\"".$row['dictionarykey']."\" />\n";
                	}
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
            $this->cacheVersion = $cacheVersion;
            if($cacheVersion!==FALSE)
            {
                $this->writeDictionaryCache($cacheVersion, $xmldata);
            }
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

    private function getDictionaryCacheTTL()
    {
        global $dictionaryCacheTTL;

        if(!isset($dictionaryCacheTTL))
        {
            return 300;
        }

        $ttl = (int) $dictionaryCacheTTL;
        if($ttl<0)
        {
            return 0;
        }
        return $ttl;
    }

    private function getDictionaryCacheFile()
    {
        global $dbName;
        global $domain;

        $cacheDir = sys_get_temp_dir().DIRECTORY_SEPARATOR."tellervo-dictionary-cache";
        if(!is_dir($cacheDir))
        {
            if(!@mkdir($cacheDir, 0770, true) && !is_dir($cacheDir))
            {
                return FALSE;
            }
        }

        $key = md5((isset($dbName) ? $dbName : "tellervo")."|".(isset($domain) ? $domain : ""));
        return $cacheDir.DIRECTORY_SEPARATOR.$key.".cache";
    }

    private function readDictionaryCache($version)
    {
        $ttl = $this->getDictionaryCacheTTL();
        if($ttl==0)
        {
            return FALSE;
        }

        $cacheFile = $this->getDictionaryCacheFile();
        if($cacheFile===FALSE || !is_readable($cacheFile))
        {
            return FALSE;
        }

        $cache = @unserialize(@file_get_contents($cacheFile));
        if(!is_array($cache) || !isset($cache['version']) || !isset($cache['created']) || !isset($cache['xmldata']))
        {
            return FALSE;
        }

        if($cache['version']!==$version)
        {
            return FALSE;
        }

        if((time() - (int) $cache['created']) > $ttl)
        {
            return FALSE;
        }

        return $cache['xmldata'];
    }

    private function writeDictionaryCache($version, $xmldata)
    {
        $ttl = $this->getDictionaryCacheTTL();
        if($ttl==0)
        {
            return FALSE;
        }

        $cacheFile = $this->getDictionaryCacheFile();
        if($cacheFile===FALSE)
        {
            return FALSE;
        }

        $cache = array(
            'version' => $version,
            'created' => time(),
            'xmldata' => $xmldata
        );

        $tmpFile = $cacheFile.".".getmypid().".tmp";
        if(@file_put_contents($tmpFile, serialize($cache), LOCK_EX)===FALSE)
        {
            return FALSE;
        }

        @chmod($tmpFile, 0660);
        return @rename($tmpFile, $cacheFile);
    }

    private function getDictionarySourceTables()
    {
        return array(
            'tlkpprojecttype',
            'tlkpobjecttype',
            'tlkpelementtype',
            'tlkpsampletype',
            'tlkpcoveragetemporal',
            'tlkpcoveragetemporalfoundation',
            'tlkpelementauthenticity',
            'tlkpdatingtype',
            'tlkptaxon',
            'tlkptaxonrank',
            'tlkpsamplestatus',
            'tlkpuserdefinedfield',
            'tlkpuserdefinedterm',
            'tlkpprojectcategory',
            'tblsecurityuser',
            'tblsecuritygroup',
            'tblsecurityusermembership',
            'tblsecuritygroupmembership',
            'tlkpreadingnote',
            'tblbox',
            'tblsample',
            'tlkpdomain',
            'tlkpwmsserver'
        );
    }

    private function tableExists($table)
    {
        global $dbconn;

        $result = pg_query_params($dbconn, "SELECT to_regclass($1) AS table_name", array('public.'.$table));
        if($result===FALSE)
        {
            return FALSE;
        }

        $row = pg_fetch_array($result);
        return ($row && $row['table_name']!=NULL);
    }

    private function tableHasColumn($table, $column)
    {
        global $dbconn;

        $result = pg_query_params($dbconn, "SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=$1 AND column_name=$2", array($table, $column));
        if($result===FALSE)
        {
            return FALSE;
        }

        return pg_num_rows($result)>0;
    }

    private function getTableCacheFingerprint($table)
    {
        global $dbconn;

        if(!$this->tableExists($table))
        {
            return $table.":missing";
        }

        $quotedTable = pg_escape_identifier($dbconn, $table);
        $fields = array(
            "count(*)::text AS rowcount",
            "coalesce(max(xmin::text), '') AS maxxmin"
        );

        if($this->tableHasColumn($table, 'lastmodifiedtimestamp'))
        {
            $fields[] = "coalesce(max(lastmodifiedtimestamp)::text, '') AS maxmodified";
        }
        if($this->tableHasColumn($table, 'createdtimestamp'))
        {
            $fields[] = "coalesce(max(createdtimestamp)::text, '') AS maxcreated";
        }

        $sql = "SELECT ".implode(", ", $fields)." FROM ".$quotedTable;
        $result = pg_query($dbconn, $sql);
        if($result===FALSE)
        {
            return $table.":error";
        }

        $row = pg_fetch_assoc($result);
        return $table.":".md5(json_encode($row));
    }

    private function getDictionaryCacheVersion()
    {
        global $dbconn;
        global $labname;
        global $labacronym;
        global $taxonomicAuthorityEdition;
        global $wsversion;

        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus !==PGSQL_CONNECTION_OK)
        {
            return FALSE;
        }

        $parts = array(
            "dictionary-cache-v1",
            "labname=".(isset($labname) ? $labname : ""),
            "labacronym=".(isset($labacronym) ? $labacronym : ""),
            "taxonomicAuthorityEdition=".(isset($taxonomicAuthorityEdition) ? $taxonomicAuthorityEdition : ""),
            "wsversion=".(isset($wsversion) ? $wsversion : "")
        );

        foreach($this->getDictionarySourceTables() as $table)
        {
            $parts[] = $this->getTableCacheFingerprint($table);
        }

        return md5(implode("|", $parts));
    }

  


// End of Class
} 
?>
