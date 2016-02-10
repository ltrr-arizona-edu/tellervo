<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */
require_once('dbhelper.php');
require_once('inc/note.php');
require_once('inc/region.php');

/**
 * Class for directing search requests.  Compiles SQL commands from search parameters and compiles XML results.
 *
 */
class search Implements IDBAccessor
{
    var $id = NULL;
    var $xmldata = NULL;
    var $lastErrorCode = NULL;
    var $lastErrorMessage = NULL;
    var $sqlcommand = NULL;
    var $deniedRecArray = array();
    var $returnObject= NULL;
    var $format = NULL;
    var $recordHits = 0;
    var $includeLoan = false;
    var $includeCuration = false;

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
    	global $firebug;
    	
    	$firebug->log($paramsObj->returnObject, "Return object");
    	$firebug->log($paramsObj->allData, "All data?");

    	
        switch($crudMode)
        {
            case "search":
                if($paramsObj->returnObject==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'returnObject' field is required when performing a search");
                    return false;
                }
                if(($paramsObj->allData===TRUE) && ($paramsObj->returnObject=='measurementSeries'))
                {
                    $this->setErrorMessage("901","Invalid user parameters - you cannot request all measurements as it is computationally too expensive");
                    return false;
                }
                
               /* if($paramsObj->returnObject=='tag')
                {
		  if($paramsObj->allData===TRUE)
		  {
		    $firebug->log("Search for all tags");
		  }
		  else
		  {
                
		    $this->setErrorMessage("901","Invalid user parameters - you can only search for 'all' tags, search parameters are not currently supported");
		    return false;
		  }
                }*/
                
                return true;
            
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }

    /***********/
    /*ACCESSORS*/
    /***********/
    
    /**
     * Actually do the search
     *
     * @param searchParameters $paramsClass
     * @param Auth $auth
     * @param Boolean $includePermissions
     * @param String $format
     * @return Boolean
     */
    function doSearch($paramsClass, $auth, $includePermissions, $format)
    {

        global $dbconn;
        global $debugFlag;
        global $myMetaHeader;
        global $firebug;
        
        $myAuth       = $auth;
        $myRequest    = $paramsClass;
		
        $this->returnObject = $paramsClass->returnObject;
        $this->format = $format;
        
        
        $orderBySQL   = NULL;
        $groupBySQL   = NULL;
        $filterSQL    = NULL;
        $skipSQL      = NULL;
        $limitSQL     = NULL;
        $xmldata      = NULL;

        // Override return object to vmeasurement if measurement requested
        if(($myRequest->returnObject=='measurementSeries') || ($myRequest->returnObject=='derivedSeries'))
        {
            $myRequest->returnObject='vmeasurement';
            $this->returnObject = 'vmeasurement';
        }

        // Build return object dependent SQL
        $returnObjectSQL = "DISTINCT ON (".$this->tableName($this->returnObject).".".$this->variableName($this->returnObject)."id)". $this->tableName($this->returnObject).".* ";
        //$returnObjectSQL = $this->tableName($this->returnObject).".".$this->variableName($this->returnObject)."id as id ";
        $orderBySQL      = "\n ORDER BY ".$this->tableName($this->returnObject).".".$this->variableName($this->returnObject)."id asc ";
        //$groupBySQL      = "\n GROUP BY ".$this->tableName($this->returnObject).".".$this->variableName($this->returnObject)."id" ;
        $groupBySQL = NULL;
        if ($myRequest->limit) $limitSQL = "\n LIMIT ".pg_escape_string($myRequest->limit);
        if ($myRequest->skip)  $skipSQL  = "\n OFFSET ".pg_escape_string($myRequest->skip);

        if( $myRequest->allData===FALSE || $this->includeTag($this->tablesFromParams($paramsArray)) )
        {
            // User doing a normal search (not all records) OR
            // request include tbltag which always needs filter
            // so build filter  
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // Build filter SQL
                $filterSQL .= $this->paramsToFilterSQL($myRequest->paramsArray);
            }
        }

        // Compile full SQL statement from parts
        if($filterSQL!=NULL)
        {
            $fullSQL = "SELECT ".$returnObjectSQL.$this->fromSQL($myRequest->paramsArray)." WHERE ".$filterSQL.$groupBySQL.$orderBySQL.$limitSQL.$skipSQL;
        }
        else
        {
            $fullSQL = "SELECT ".$returnObjectSQL.$this->fromSQL($myRequest->paramsArray).$groupBySQL.$orderBySQL.$limitSQL.$skipSQL;
        }

        // Add SQL to XML output
        $firebug->log($fullSQL, "Search SQL");
        $this->sqlcommand .= $fullSQL;
        
        $firebug->log("Compilation of search SQL complete");

        // Do SQL Query
        pg_send_query($dbconn, $fullSQL);
        $result = pg_get_result($dbconn);
        $this->recordHits = pg_num_rows($result);

        $result = pg_query($dbconn, $fullSQL);


        $firebug->log("Begin permissions check");
        while ($row = pg_fetch_array($result))
        {                   	
            // Check user has permission to read requested entity
            if($this->returnObject=="object") 
            {
            	$firebug->log("Checking object permissions");
                $myReturnObject = new object();
                $hasPermission = $myAuth->getPermission("read", "object", $row['objectid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['objectid']);
                	$firebug->log($row['objectid'], "Permission denied on objectid");	 
                	continue;
                }              
            }
            elseif($this->returnObject=="element")
            {
                $myReturnObject = new element();
                $hasPermission = $myAuth->getPermission("read", "element", $row['elementid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['elementid']); 
                	$firebug->log($row['elementid'], "Permission denied on elementid");	 
                	continue;
                }
                
            }
            elseif($this->returnObject=="sample") 
            {
                $myReturnObject = new sample();
                $hasPermission = $myAuth->getPermission("read", "sample", $row['sampleid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['sampleid']); 
                	$firebug->log($row['sampleid'], "Permission denied on sampleid");	 
                	continue;
                }              
            }
            elseif($this->returnObject=="loan")
            {
            	$myReturnObject = new loan();
            	$hasPermission = $myAuth->getPermission("read", "loan", $row['loanid']);
            	if($hasPermission===FALSE) {
            		array_push($this->deniedRecArray, $row['loanid']);
            		$firebug->log($row['loanid'], "Permission denied on loanid");
            		continue;
            	}
            }
            elseif($this->returnObject=="box")
            {
            	$myReturnObject = new box();
            	$hasPermission = $myAuth->getPermission("read", "box", $row['boxid']);
            	if($hasPermission===FALSE) {
            		array_push($this->deniedRecArray, $row['boxid']);
            		$firebug->log($row['boxid'], "Permission denied on boxid");
            		continue;
            	}
            }
            elseif($this->returnObject=="curation")
            {
            	$myReturnObject = new curation();
            	$hasPermission = $myAuth->getPermission("read", "curation", $row['curationid']);
            	if($hasPermission===FALSE) {
            		array_push($this->deniedRecArray, $row['curationid']);
            		$firebug->log($row['curationid'], "Permission denied on curationid");
            		continue;
            	}
            }
            elseif($this->returnObject=="radius") 
            {
                $myReturnObject = new radius();
                $hasPermission = $myAuth->getPermission("read", "radius", $row['radiusid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['radiusid']); 
                	$firebug->log($row['radiusid'], "Permission denied on radiusid");	 
                	continue;
                }            
            }
            elseif($this->returnObject=="tag") 
            {
                $myReturnObject = new tag();
                /*$hasPermission = $myAuth->getPermission("read", "tag", $row['tagid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['tagid']); 
                	$firebug->log($row['radiusid'], "Permission denied on tagid");	 
                	continue;
                } */           
            }
            elseif($this->returnObject=="odkFormDefinition") 
            {
                $myReturnObject = new odkFormDefinition();
                /*$hasPermission = $myAuth->getPermission("read", "tag", $row['tagid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['tagid']); 
                	$firebug->log($row['radiusid'], "Permission denied on tagid");	 
                	continue;
                } */           
            }
            elseif($this->returnObject=="vmeasurement")
            {
		        /**
		         * SORT THIS OUT!
		         * 
		         * We have serious performance issues so for now disable check on
		         * whether the user can read the series or not.  
		         */            	
            	
                $myReturnObject = new measurement();
                /*$hasPermission = $myAuth->getPermission("read", "measurement", $row['vmeasurementid']);
                if($hasPermission===FALSE) {
                	array_push($this->deniedRecArray, $row['vmeasurementid']); 
                	$firebug->log($row['vmeasurementid'], "Permission denied on vmeasurementid");	                 	
                	continue;            
                }*/
            }
            else
            {
                $this->setErrorMessage("901","Invalid return object ".$this->returnObject." specified.  Must be one of object, element, sample radius or measurement");
            }

            $firebug->log("Permissions check complete");

            // Set parameters on new object and return XML
            $firebug->log("Get current entities details from database");
            $success = $myReturnObject->setParamsFromDBRow($row, $format);
            //$success = $myReturnObject->setParamsFromDB($row['id']);
            $firebug->log("Got details from database");

            // Do children if requested
            if($paramsClass->includeChildren===TRUE)
            {
            	$myReturnObject->setChildParamsFromDB(true);
            }
            
            $firebug->log($xmldata, "XML data");
            
          
            // Get permissions if requested
            if($includePermissions===TRUE) $myReturnObject->getPermissions($myAuth->getID());

            if ($debugFlag===TRUE) $myMetaHeader->setTiming("Get current entities XML");
            //$success = $myReturnObject->setParamsFromDB($row['id'], "brief");
            if($success)
            {
                $xmldata.=$myReturnObject->asXML($format);

            }
            else
            {
                $this->setErrorMessage($myReturnObject->getLastErrorCode(), $myReturnObject->getLastErrorMessage());
            }
        }
        // }


        
        
        if(count($this->deniedRecArray)>0 )
        {
            $errMessage = "Permission denied on the following ".$this->returnObject." id(s): ";
            foreach ($this->deniedRecArray as $id)
            {
                $errMessage .= $id.", ";
            }
            $errMessage = substr($errMessage, 0, -2).".";
            $this->setErrorMessage("103", $errMessage);
        }

        if ($debugFlag===TRUE) $myMetaHeader->setTiming("Permissions checks complete"); 
        
        // Put xmldata into class variable
        if($xmldata!=NULL)
        {
            $this->xmldata=$xmldata;
        }

        if($this->lastErrorCode==NULL)
        {
            return true;
        } 
        else
        {
            return false;
        }
    }

    function xmlDebugOutput()
    {
    	//$xmldata = "<sql records=\"".$this->recordHits."\">".htmlSpecialChars($this->sqlcommand)."</sql>";
    	//return $xmldata;
    	global $firebug;
    	
    	$firebug->log($this->recordHits, "Number of hits");
    	$firebug->log($this->sqlcommand, "SQL used");
    }
    
    function asXML($format='standard', $mode="all")
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

    private function paramsToFilterSQL($paramsArray)
    {
    	global $firebug;
    	global $myAuth;
    	
        $filterSQL = NULL;

        foreach($paramsArray as $param)
        {
        	// Intercept special cases first
        	if($param['field']=='anyparentobjectid')
        	{
        		switch ($param['operator'])
        		{
        			case "=":
        				$operator = "IN";
        				$value = "'".$param['value']."'";
        				break;
        			case "!=":
        				$operator = "NOT IN";
        				$value = "'".$param['value']."'";
        				break;
        			default:
    					// No other operators allowed
    					$this->setErrorMessage("901", "Invalid search operator used. The '".$param['field']."' field can use only = or != equals operators");
        				return null;     				
        		}
        		$filterSQL.= $param['table'].".objectid ";
        		$filterSQL.= $operator." (SELECT objectid FROM cpgdb.findobjectdescendants(".$value.", true))\n AND ";
        	}
        	elseif($param['field']=='anyparentobjectcode')
        	{
        		switch ($param['operator'])
        		{
        			case "=":
        				$operator = "IN";
        				$value = "'".$param['value']."'";
        				break;
        			case "!=":
        				$operator = "NOT IN";
        				$value = "'".$param['value']."'";
        				break;
        			default:
    					// No other operators allowed
    					$this->setErrorMessage("901", "Invalid search operator used. The '".$param['field']."' field can use only = or != equals operators");
        				return null;     				
        		}
        		$filterSQL.= $param['table'].".code ";
        		$filterSQL.= $operator." (SELECT code FROM tblobject WHERE objectid IN (SELECT objectid FROM cpgdb.findobjectdescendantsfromcode(".$value.", true)))\n AND ";
        	}
        	elseif($param['field']=='dependentseriesid')
        	{
        		$firebug->log("building filter SQL clause for seriesdependencyid");
        		switch ($param['operator'])
        		{
        			case "=":
        				$operator = "IN";
        				$value = "'".$param['value']."'";
        				break;
        			case "!=":
        				$operator = "NOT IN";
        				$value = "'".$param['value']."'";
        				break;
        			default:
        				// No other operators allowed
        				$this->setErrorMessage("901", "Invalid search operator used. The '".$param['field']."' field can use only = or != equals operators");
        				return null;
        		}
        		$filterSQL.= $param['table'].".vmeasurementid ";
        		$filterSQL.= $operator." (SELECT vmeasurementid FROM cpgdb.findvmchildren(".$value.", false))\n AND ";
        		
        		$firebug->log($filterSQL, "seriesdependencyid filter sql");
        		
        	}
        	// All other cases can be handled generically
        	else
        	{ 	
	            // Set operator
	            switch ($param['operator'])
	            {
	            case ">":
	                $operator = ">";
	                $value = " '".$param['value']."'";
	                break;
	            case "<":
	                $operator = "<";
	                $value = " '".$param['value']."'";
	                break;
	            case "!=":
	                $operator = "!=";
	                $value = " '".$param['value']."'";
	                break;
	            case "like":
	                $operator = "ilike";
	                $value = " '%".$param['value']."%'";
	                break;
	            case "is":
	            	$operator = "is";
	            	$value = " ".$param['value']." ";
	            	break;
	            default :
	                $operator = "=";
	                $value = " '".$param['value']."'";
	            }
	            $filterSQL .= $param['table'].".".$param['field']." ".$operator.$value."\n AND ";
        	}
        }
        
        // Force clause to hide personal tags
        if($this->includeTag($this->tablesFromParams($paramsArray)))
        {
	    $filterSQL .= " (tbltag.ownerid IS NULL OR tbltag.ownerid='".$myAuth->getID()."') AND ";
        }

        // Trim off last 'and'
        $filterSQL = substr($filterSQL, 0, -5);
        
        return $filterSQL;
    }

    private function variableName($objectName)
    {

        switch($objectName)
        {
        case "object":
        case "element":
        case "sample":
        case "radius":
        case "loan":   
        case "box":
        case "curation":
        case "tag":
            return $objectName;
            break;
        case "vmeasurement":
            return "vmeasurement";
            break;     			
	case "odkFormDefinition":
	    return "odkdefinition";
	    break;
	case "odkFormInstance":
	    return "odkinstance";
	    break;
        default:
            return false;
        }

    }

    private function tableName($objectName)
    {

        switch($objectName)
        {
        case "object":
            return "vwtblobject";
            break;
        case "element":
            return "vwtblelement";
            break;
        case "sample":
            return "vwtblsample";
            break;
        case "radius":
            return "vwtblradius";
            break;
        case "measurement":
            return "vwcomprehensivevm";
            break;
        case "vmeasurement":
            return "vwcomprehensivevm";
            break;
        case "vmeasurementmetacache":
            return "vwcomprehensivevm";
            break;
        case "vmeasurement":
        	return "vwcomprehensivevm";
        	break;
        case "vmeasurementderivedcache":
            return "vwcomprehensivevm";
            break;
        case "box":
        	return "vwtblbox";
        	break;
        case "loan":
        	return "vwtblloan";
        	break;
        case "curation":
        	return "vwtblcuration";
        	break;
        case "tag":
	        return "tbltag";
		break;
        case "odkFormDefinition":
	        return "tblodkdefinition";
		break;
        case "odkFormInstance":
	        return "tblodkinstance";
		break;
        default:
        	echo "unable to determine table name.  Fatal error.";
            die();
        }

    }
    
    private function tablesFromParams($paramsArray)
    {
    	// Create an array of tables that are being used
    	$uniqTables = array();
    	foreach ($paramsArray as $param)
    	{
    		array_push($uniqTables, $param['table']);
    	}
    	$uniqTables = array_unique($uniqTables);
    	return $uniqTables;
    }
    
    
    private function fromSQL($paramsArray)
    {    
    	global $firebug;
    	
    	$tables = $this->tablesFromParams($paramsArray);	
        $fromSQL = "\nFROM ";
        $withinJoin = FALSE;

        /*if( (($this->getLowestRelationshipLevel($myRequest)<=6) && ($this->getHighestRelationshipLevel($myRequest)>=6)) || ($this->returnObject == 'project'))
        {
            $fromSQL .= $this->tableName("project")." \n";
            $withinJoin = TRUE;
        }*/
              
        $firebug->log($this->getLowestRelationshipLevel($tables), "Lowest Relationship Level");
        $firebug->log($this->getHighestRelationshipLevel($tables), "Highest Relationship Level");

	if($this->includeODKFormDefinition($tables))
	{
		return $fromSQL .= "tblodkdefinition";
	}
        
        if( (($this->getLowestRelationshipLevel($tables)<=5) && ($this->getHighestRelationshipLevel($tables)>=5)) || ($this->returnObject == 'object'))  
        {
            if($withinJoin)
            {
                $fromSQL .= $this->tableName("object")." \n";
            }
            else
            {
                $fromSQL .= $this->tableName("object")." \n";
                $withinJoin = TRUE;
            }
        }
        
        if( (($this->getLowestRelationshipLevel($tables)<=4) && ($this->getHighestRelationshipLevel($tables)>=4)) || ($this->returnObject == 'element'))
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("element")." ON ".$this->tableName("object").".objectid = ".$this->tableName("element").".objectid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("element")." \n";
                $withinJoin = TRUE;
            }
        }        

        if( (($this->getLowestRelationshipLevel($tables)<=3) && ($this->getHighestRelationshipLevel($tables)>=3)) || ($this->returnObject == 'sample'))
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("sample")." ON ".$this->tableName("element").".elementid = ".$this->tableName("sample").".elementid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("sample")." \n";
                $withinJoin = TRUE;
            }
            
            if($this->includeLoan($tables) || $this->includeCuration($tables))
            {
            	$fromSQL .= "INNER JOIN ".$this->tableName("curation")." ON ".$this->tableName("sample").".sampleid = ".$this->tableName("curation").".sampleid \n";
            }
            
            if($this->includeLoan($tables))
            {
            	$fromSQL .= "INNER JOIN ".$this->tableName("loan")." ON ".$this->tableName("curation").".loanid = ".$this->tableName("loan").".loanid \n";
            }
                        
        }
        else 
        {
        	if($this->includeCuration($tables))
        	{
        		if($withinJoin)
        		{
        			$fromSQL .= "INNER JOIN ".$this->tableName("curation")." ON ".$this->tableName("sample").".sampleid = ".$this->tableName("curation").".sampleid \n";
        		}
        		else
        		{
        			$fromSQL .= $this->tableName("curation")." \n";
        			$withinJoin = TRUE;
        		}
        	}
        	
        	if($this->includeLoan($tables))
        	{
        		if($withinJoin)
        		{
        			$fromSQL .= "INNER JOIN ".$this->tableName("loan")." ON ".$this->tableName("curation").".loanid = ".$this->tableName("loan").".loanid \n";
        		}
        		else
        		{
        			$fromSQL .= $this->tableName("loan")." \n";
        			$withinJoin = TRUE;
        		}
        	}
        }
        
        if ($this->includeBox($tables))
        {
        	if($withinJoin)
        	{
        		$fromSQL .= "INNER JOIN ".$this->tableName("box")." ON ".$this->tableName("sample").".boxid = ".$this->tableName("box").".boxid \n";
        	}
        	else
        	{
        		$fromSQL .= $this->tableName("box")." \n";
        		$withinJoin = TRUE;
        	}
        }
        
        
        if( (($this->getLowestRelationshipLevel($tables)<=2) && ($this->getHighestRelationshipLevel($tables)>=2)) || ($this->returnObject == 'radius'))
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("radius")." ON ".$this->tableName("sample").".sampleid = ".$this->tableName("radius").".sampleid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("radius")." \n";
                $withinJoin = TRUE;
            }
        }
        
        
        if( (($this->getLowestRelationshipLevel($tables)<=1) && ($this->getHighestRelationshipLevel($tables)>=1)) || ($this->returnObject == 'vmeasurement') || ($this->returnObject =='vmeasurement'))  
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN tblmeasurement ON tblmeasurement.radiusid = ".$this->tableName("radius").".radiusid \n";
                $fromSQL .= "INNER JOIN tblvmeasurementderivedcache dc ON dc.measurementid = tblmeasurement.measurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurement")." ON dc.vmeasurementid = ".$this->tableName("vmeasurement").".vmeasurementid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("vmeasurement")." \n";
                $withinJoin = TRUE;
            }
            
            if ($this->includeTag($tables))
	    {
		 $fromSQL .=" LEFT JOIN tblvmeasurementtotag ON ".$this->tableName("vmeasurement").".vmeasurementid = tblvmeasurementtotag.vmeasurementid \n";
		 $fromSQL .=" LEFT JOIN ".$this->tableName("tag")." ON tblvmeasurementtotag.tagid = ".$this->tableName("tag").".tagid \n";
	    }
            

        }
        else
        {
            if ($this->includeTag($tables))
	    {
        	if($withinJoin)
        	{
			$fromSQL .=" LEFT JOIN tblvmeasurementtotag ON ".$this->tableName("vmeasurement").".vmeasurementid = tblvmeasurementtotag.vmeasurementid \n";
			$fromSQL .=" LEFT JOIN ".$this->tableName("tag")." ON tblvmeasurementtotag.tagid = ".$this->tableName("tag").".tagid \n";        	}
        	else
        	{
        		$fromSQL .= $this->tableName("tag")." \n";
        		$withinJoin = TRUE;
        	}
	    }
        
        }
                
        /*
        if( (($this->getLowestRelationshipLevel($tables)<=1) && ($this->getHighestRelationshipLevel($tables)>=1)) || ($this->returnObject == 'vmeasurement'))  
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("measurement")." ON ".$this->tableName("radius").".radiusid = ".$this->tableName("measurement").".radiusid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementderivedcache")." ON ".$this->tableName("measurement").".measurementid = ".$this->tableName("vmeasurementderivedcache").".measurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurement")." ON ".$this->tableName("vmeasurementderivedcache").".vmeasurementid = ".$this->tableName("vmeasurement").".vmeasurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementmetacache")." ON ".$this->tableName("vmeasurement").".vmeasurementid = ".$this->tableName("vmeasurementmetacache").".vmeasurementid \n";
            }
            else
            {
                $fromSQL .= $this->tableName('measurement')." \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementderivedcache")." ON ".$this->tableName("measurement").".measurementid = ".$this->tableName("vmeasurementderivedcache").".measurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurement")." ON ".$this->tableName("vmeasurementderivedcache").".vmeasurementid = ".$this->tableName("vmeasurement").".vmeasurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementmetacache")." ON ".$this->tableName("vmeasurement").".vmeasurementid = ".$this->tableName("vmeasurementmetacache").".vmeasurementid \n";
            }
        }*/
               
        return $fromSQL;
    }

    
    /**
     * Whether or not the Loan table should be included
     */
    private function includeLoan($tables)
    {
    	if ((in_array('vwtblloan', $tables)) || ($this->returnObject == 'loan'))
    	{
    		return true;
    	}
    }
    
     /**
     * Whether or not the tag table should be included
     */
    private function includeTag($tables)
    {
    	if ((in_array('tbltag', $tables)) || ($this->returnObject == 'tag'))
    	{
    		return true;
    	}
    }
   
    private function includeODKFormDefinition($tables)
    {
    	if ((in_array('tblodkdefinition', $tables)) || ($this->returnObject == 'odkFormDefinition'))
    	{
    		return true;
    	}

    } 
    
    /**
     * Whether or not the Box table should be included
     */
    private function includeBox($tables)
    {
    	if ((in_array('vwtblbox', $tables)) || ($this->returnObject == 'box'))
    	{
    		return true;
    	}
    }
    
    /**
     * Whether or not the Curation table should be included
     */
    private function includeCuration($tables)
    {
    	if ((in_array('vwtblcuration', $tables)) || ($this->returnObject == 'curation'))
    	{
    		return true;
    	}
    }
    
    private function getLowestRelationshipLevel($tables)
    {
        // This function returns an interger representing the most junior level of relationship required in this query
        // tblproject      -- 6 -- most senior
        // tblobject       -- 5 --
        // tblelement      -- 4 --
        // tblsample       -- 3 --  
        // tblradius       -- 2 --
        // tblmeasurement  -- 1 -- most junior
    	
        if ((in_array('vwcomprehensivevm', $tables))  			||
		         ($this->returnObject == 'vmeasurement') 
                     )
        {
            return 1;
        }
        elseif ((in_array('vwtblradius', $tables)) || ($this->returnObject == 'radius'))
        {
            return 2;
        }
        elseif ((in_array('vwtblsample', $tables)) || ($this->returnObject == 'sample'))
        {
            return 3;
        }
        elseif ((in_array('vwtblelement', $tables)) || ($this->returnObject == 'element'))
        {
            return 4;
        }
        elseif ((in_array('vwtblobject', $tables)) || ($this->returnObject == 'object'))
        {
            return 5;
        }
        //elseif ((in_array('vwtblproject')) || ($this->returnObject == 'project'))
        //{
        //    return 6;
        //}
        else
        {
            return false;
        }
    }

    private function getHighestRelationshipLevel($tables)
    {
        // This function returns an interger representing the most senior level of relationship required in this query
        // tblproject      -- 6 -- most senior
        // tblobject       -- 5 --
        // tblelement      -- 4 --
        // tblsample       -- 3 --
        // tblradius       -- 2 --
        // tblmeasurement  -- 1 -- most junior

        //if (($myRequest->projectParamsArray) || ($this->returnObject == 'project'))
        //{
        //    return 6;
        //}
        if ((in_array('vwtblobject', $tables)) || ($this->returnObject == 'object'))
        {
            return 5;
        }
        elseif ((in_array('vwtblelement', $tables)) || ($this->returnObject == 'element'))
        {
            return 4;
        }
        elseif ((in_array('vwtblsample', $tables)) || ($this->returnObject == 'sample'))
        {
            return 3;
        }
        elseif ((in_array('vwtblradius', $tables)) || ($this->returnObject == 'radius'))
        {
            return 2;
        }
        elseif ( (in_array('vwcomprehensivevm', $tables)) || 
		         ($this->returnObject == 'vmeasurement') 
		       )
        {
            return 1;
        }
        else
        {
            return false;
        }
    }

    private function getRelationshipSQL($theRequest)
    {
        // Returns the 'where' clause part of the query SQL for the table relationships 

        $myRequest = $theRequest;
        $lowestLevel  = $this->getLowestRelationshipLevel($myRequest);
        $highestLevel = $this->getHighestRelationshipLevel($myRequest);
        $sql ="";


        if (($lowestLevel==1) && ($highestLevel>=1))
        {
            $sql .= "tblmeasurement.measurementid=tblvmeasurementderivedcache.measurementid and tblvmeasurementderivedcache.vmeasurementid=tblvmeasurement.vmeasurementid and tblvmeasurementmetacache.vmeasurementid=tblvmeasurement.vmeasurementid and ";
        }
        if (($lowestLevel<=1) && ($highestLevel>1))
        {
            $sql .= "tblmeasurement.radiusid=vwtblradius.radiusid and ";
        }
        if (($lowestLevel<=2) && ($highestLevel>2))
        {
            $sql .= "vwtblradius.sampleid=vwtblample.sampleid and ";
        }
        if (($lowestLevel<=3) && ($highestLevel>3))
        {
            $sql .= "vwtblsample.elementid=vwtblelement.elementid and ";
        }
        if (($lowestLevel<=4) && ($highestLevel>4))
        {
            $sql .= "vwtblelement.objectid=vwtblobject.objectid and ";
        }
        //if (($lowestLevel<=5) && ($highestLevel>5))
        //{
        //    $sql .= "vwtblsubsite.siteid=vwtblsite.siteid and ";
        //}

        return $sql;
    }

    
    function writeToDB($crudMode="create")
    {
    	trigger_error("667"."search class should not be asked to write to db", E_USER_ERROR);
    	return false;
    }
    
    function deleteFromDB()
    {
    	trigger_error("667"."search class should not be asked to delete from the db", E_USER_ERROR);
    	return false;
    }
    
    function mergeRecords($newParentID)
    {
    	trigger_error("667"."search class should not be asked to merge", E_USER_ERROR);
    	return false;
    }


// End of Class
} 
?>
