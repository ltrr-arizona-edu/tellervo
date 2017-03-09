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

class object extends objectEntity implements IDBAccessor
{
                   
    /***************/
    /* CONSTRUCTOR */
    /***************/
    

    public function __construct()
    {
        $groupXMLTag = "object";
    	parent::__construct($groupXMLTag);
    }

    public function __destruct()
    {

    }
    
    
    /***********/
    /* SETTERS */
    /***********/
    
    function setParamsFromDBRow($row, $format="standard")
    {  	
        global $debugFlag;
        global $myMetaHeader;
        if ($debugFlag===TRUE) $myMetaHeader->setTiming("Setting object parameters for objectid ".$row['objectid']);                 
               
        // Set parameters from db
        $this->setTitle($row['title']);
        $this->setID($row['objectid'], $row['domain']);
        $this->setCreatedTimestamp($row['createdtimestamp']);
        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
        $this->setComments($row['comments']);
        $this->setType($row['objecttypeid'], $row['objecttype']);
        $this->setDescription($row['description']);
        $this->setFilesFromStrArray($row['file']);
        $this->setCreator($row['creator']);
        $this->setOwner($row['owner']);
        $this->setVegetationType($row['vegetationtype']);
        $this->setCoverageTemporal($row['coveragetemporal'], $row['coveragetemporalfoundation']);
        $this->location->setGeometry($row['locationgeometry'], $row['locationtype'], $row['locationprecision']);
        $this->location->setComment($row['locationcomment']);
        $this->location->setAddressLine1($row['locationaddressline1']);
        $this->location->setAddressLine2($row['locationaddressline2']);
        $this->location->setCityOrTown($row['locationcityortown']);
        $this->location->setStateProvinceRegion($row['locationstateprovinceregion']);
        $this->location->setPostalCode($row['locationpostalcode']);
        $this->location->setCountry($row['locationcountry']);
        $this->setCountOfChildVMeasurements($row['countofchildvmeasurements']);
		$this->setCode($row['code']);

        return true;
    }    
    
	/**
	 * Set this object's parameters from the database
	 *
	 * @param Integer $theID
	 * @param String $idType
	 */
    function setParamsFromDB($theID, $idType='db')
	{
 		
       global $dbconn;
        
       switch(strtolower($idType))
       {
       	case 'db':
		    $sql = "SELECT * FROM vwtblobject WHERE objectid='".$theID."'";
		    break;
		    
       	case 'lab':
		    $sql = "SELECT * FROM vwtblobject WHERE code='".$theID."'";
       		break;

       	default:
       		trigger_error('667'.'Unknown database id type.', E_USER_ERROR);
       		die();
       }
          
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                trigger_error("903"."No records match the specified id. $sql", E_USER_ERROR);
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->setParamsFromDBRow($row);
            }

        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database", E_USER_ERROR);
            return FALSE;
        }

        $this->cacheSelf();
        return TRUE;		
	}

    /**
     * Add the id's of the current object's direct children from the database
     *
     * @return Boolean
     */
    function setChildParamsFromDB($cascade=false)	
    {
        global $dbconn;

        $sql  = "select objectid from vwtblobject where parentobjectid='".$this->getID()."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);

            if(pg_num_rows($result)>0)
            {
            	// Object has 'object' descendants
                while ($row = pg_fetch_array($result))
	            {
	            	$entity = new object();
	            	$entity->setParamsFromDB($row['objectid']);
	            	$entity->setChildParamsFromDB(true);
	            	//print_r($entity);
	                array_push($this->childrenEntityArray, $entity);
	            }
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
     * Set the current object's parameters from a paramsClass object
     *
     * @param objectParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {

	global $firebug;
	$firebug->log($paramsClass, "params class");

    			$this->setTitle($paramsClass->getTitle());
    		$this->setComments($paramsClass->getComments());
        		$this->setType($paramsClass->getType(true), $paramsClass->getType());
    		$this->setDescription($paramsClass->getDescription());
    			$this->setFiles($paramsClass->getFiles());      
        	$this->setCreator($paramsClass->getCreator());
        		$this->setOwner($paramsClass->getOwner());
        		$this->setVegetationType($paramsClass->getVegetationType());
        $this->setCoverageTemporal($paramsClass->getTemporalCoverage(), $paramsClass->getTemporalCoverageFoundation());
        $this->location->setGeometry($paramsClass->location->getGeometry());
        $this->location->setType($paramsClass->location->getType());
        $this->location->setPrecision($paramsClass->location->getPrecision());
        $this->location->setComment($paramsClass->location->getComment());
	$this->location->setAddressLine1($paramsClass->location->getAddressLine1());
	$this->location->setAddressLine2($paramsClass->location->getAddressLine2());
		$this->location->setCityOrTown($paramsClass->location->getCityOrTown());
		$this->location->setStateProvinceRegion($paramsClass->location->getStateProvinceRegion());
		$this->location->setPostalCode($paramsClass->location->getPostalCode());
			$this->location->setCountry($paramsClass->location->getCountry());
		
        
        
          		$this->setCode($paramsClass->getCode());
        
        if ($paramsClass->parentID!=NULL)
        {
        	$parentObj = new object();
        	$parentObj->setParamsFromDB($paramsClass->parentID);
        	array_push($this->parentEntityArray, $parentObj);
        }
        
        
        return true;
    }


    /***********/
    /* GETTERS */
    /***********/
    
    /**
     * Get an XML representation of this object
     *
     * @param String $format
     * @param String $parts
     * @return Boolean
     */
	function asXML($format='standard', $parts='all')
	{
		global $myRequest;
		
		// Override request for comprehensive if doing a delete
		if ($myRequest->getCrudMode()=='delete') $format = 'standard';
		
	    switch($format)
        {
        case "comprehensive":
            require_once('object.php');
            global $dbconn;
	        global $tellervoNS;
	        global $tridasNS;
	        global $xlinkNS;
	        global $gmlNS;

	        // Create a DOM Document to hold the XML as it's produced
            $xml = new DomDocument();
    		$xml->loadXML("<object xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\"></object>");
    		//$xml->formatOutput = true;

    	    $myParentObjectArray = Array();
	  		array_push($myParentObjectArray, $this);
    		
            $sql = "SELECT * from cpgdb.findobjectancestors('".$this->getID()."', false)";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
            	$result = pg_query($dbconn, $sql);

				while ($row = pg_fetch_array($result)) 
				{	
					$myParentObject = new object();
					$myParentObject->setParamsFromDB($row['objectid']);
					array_push($myParentObjectArray, $myParentObject);
				}

				$myParentObjectArray = array_reverse($myParentObjectArray);
				
				$i = 0;
				foreach($myParentObjectArray as $obj)
				{
					$dom = new DomDocument();
					$dom->loadXML("<root xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$obj->asXML()."</root>");
					$dom->formatOutput = true;
					
					$objnode = $dom->getElementsByTagName("object")->item(0);
					$objnode = $xml->importNode($objnode, true);
					$xml->getElementsByTagName("object")->item($i)->appendChild($objnode);
					$i++;
				
				}
				
				return $xml->saveXML($xml->getElementsByTagName("object")->item(1));
            }
            else
	        {
	            // Connection bad
	            $this->setErrorMessage("001", "Error connecting to database");
	            return FALSE;
	        }
 
        case "standard":
            return $this->_asXML($format, $parts);

        case "summary":
            return $this->_asXML($format, $parts);

        case "minimal":
            return $this->_asXML($format, $parts);

        
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary' or 'comprehensive'");
            return false;
        }
	}
	
	
	public function asKML()
	{
		$kml = "<Placemark><name>".$this->getCode()." - ".dbHelper::escapeXMLChars($this->getTitle())."</name>\n
		<description><![CDATA[<b>Type</b>: ".dbHelper::escapeXMLChars($this->getType())."\n<br>
		<b>Location type:</b> ".dbHelper::escapeXMLChars($this->location->getType())."\n<br>
		<b>Associated series:</b> ".$this->getCountOfChildVMeasurements()."<br>
		<b>Description</b>: ".dbHelper::escapeXMLChars($this->getDescription())."\n]]></description>\n";
		$kml .= "<styleUrl>#tellervoDefault</styleUrl>\n";
		$kml .= $this->location->asKML();
		$kml .= "</Placemark>\n";
		if($this->location->asKML(2, "POLYGON")!=NULL)
		{
			$kml .= "<Placemark>\n<name>Extent of ".$this->getTitle()."</name>\n";
			$kml .= "<styleUrl>#tellervoDefault</styleUrl>\n";
			$kml .= $this->location->asKML(2, "POLYGON");
			$kml .= "</Placemark>\n";
		}
		return $kml;
	}
	
	public function asKMLWithValue($value)
	{
		$kml = "<Placemark><name>".$this->getCode()." - ".dbHelper::escapeXMLChars($this->getTitle())."</name>\n
		<description><![CDATA[<b>Type</b>: ".dbHelper::escapeXMLChars($this->getType())."\n<br>
		<b>Location type:</b> ".dbHelper::escapeXMLChars($this->location->getType())."\n<br>
		<b>Associated series:</b> ".$this->getCountOfChildVMeasurements()."<br>
		<b>Description</b>: ".dbHelper::escapeXMLChars($this->getDescription())."\n]]></description>\n";
		
		$roundvalue = round($value);
		
		if($roundvalue>=10) $tagstyle = "#tscore10";
		else if($roundvalue>=9)  $tagstyle = "#tscore9";
		else if($roundvalue>=8) $tagstyle = "#tscore8";
		else if($roundvalue>=7) $tagstyle = "#tscore7";
		else if($roundvalue>=6) $tagstyle = "#tscore6";
		else if($roundvalue>=5) $tagstyle = "#tscore5";
		else if($roundvalue>=4) $tagstyle = "#tscore4";
		else  					$tagstyle = "#tscore3";

		$kml .= "<styleUrl>$tagstyle</styleUrl>\n";
		$kml .= $this->location->asKML();	
		$kml .= "</Placemark>\n";
		return $kml;
	}	
	
	
	private function _asXML($format='standard', $parts='all')
	{
	global $firebug;
        $xml = NULL;

        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            if(($parts=="all") || ($parts=="beginning"))
            { 
            	$xml.= "<tridas:object>\n";
                $xml.= $this->getIdentifierXML(); 
                if($this->getComments()!=NULL)		$xml.= "<tridas:comments>".dbhelper::escapeXMLChars($this->getComments())."</tridas:comments>\n";    
                									$xml.= "<tridas:type normal=\"".dbhelper::escapeXMLChars($this->getType())."\" normalId=\"".$this->getType(TRUE)."\" normalStd=\"Tellervo\" />\n";        	
            	if($this->getDescription()!==NULL)	$xml.= "<tridas:description>".dbHelper::escapeXMLChars($this->getDescription())."</tridas:description>";
            	$xml.= $this->getFileXML();
		$firebug->log($this->getFileXML(), "File XMLLLL");
               	if($this->getCreator()!=NULL)		$xml.= "<tridas:creator>".dbHelper::escapeXMLChars($this->getCreator())."</tridas:creator>";
            	if($this->getOwner()!=NULL)			$xml.= "<tridas:owner>".dbHelper::escapeXMLChars($this->getOwner())."</tridas:owner>";         	
            	
				
            	
            	if($this->getTemporalCoverage()!=NULL)
            	{
            		$xml .="<tridas:coverage>";
            		$xml .="<tridas:coverageTemporal>".dbhelper::escapeXMLChars($this->getTemporalCoverage())."</tridas:coverageTemporal>";
            		$xml .="<tridas:coverageTemporalFoundation>".dbhelper::escapeXMLChars($this->getTemporalCoverageFoundation())."</tridas:coverageTemporalFoundation>";
            		$xml .="</tridas:coverage>";
            	}
            	if($this->hasGeometry()) 			$xml.= $this->location->asXML();
            	if($this->hasGeometry())			$xml.="<tridas:genericField name=\"tellervo.mapLink\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getMapLink())."</tridas:genericField>\n";
            	if($this->getCode()!=NULL)			$xml.="<tridas:genericField name=\"tellervo.objectLabCode\" type=\"xs:string\">".$this->getCode()."</tridas:genericField>\n";
            	if($this->getCountOfChildVMeasurements()!=NULL) $xml.="<tridas:genericField name=\"tellervo.countOfChildSeries\" type=\"xs:int\">".$this->getCountOfChildVMeasurements()."</tridas:genericField>\n";
            	if($this->getVegetationType()!=NULL) 		$xml.="<tridas:genericField name=\"tellervo.vegetationType\" type=\"xs:string\">".$this->getVegetationType()."</tridas:genericField>\n";	
            
            }  
            
            //echo "count of children = ".count($this->childrenEntityArray)."\n";
            if(count($this->childrenEntityArray))
            {
            	foreach($this->childrenEntityArray as $childObject)
            	{           		
            		$xml.= $childObject->asXML()."\n";
            	}
            }
            
	    
        	if(($parts=="all") || ($parts=="end"))
            {
                // End XML tag
                $xml.= "</tridas:object>\n";
                //if ($this->hasGeometry())			$xml.="</gml:featureMember>\n";
            }

            return $xml;
        }
        else
        {
            return FALSE;
        }        
        
        
	}
	
    /*************/
    /* FUNCTIONS */
    /*************/	

	/**
	 * Write the current object to the database
	 *
	 * @return Boolean
	 */
	function writeToDB($crudMode="create")
	{
        // Write the current object to the database

        global $dbconn;
        global $domain;
        global $firebug;
        $sql = "";
        $sql2 = "";


        // Check for required parameters
        if($crudMode!="create" && $crudMode!="update")
        {
	    $this->setErrorMessage("667", "Invalid mode specified in writeToDB().  Only create and update are supported");
	    return FALSE;
        }
        
        if($this->getTitle() == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'title' field is required.");
            return FALSE;
        }
	    if($this->getCode() == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'code' field is required.");
            return FALSE;
        }
        
        if($crudMode=="update" && $this->getID()==NULL)
        {
	    $this->setErrorMessage("902", "Missing parameter - 'id' field is required when editing");
            return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($crudMode=="create")
                {
                    // New Record
                    if($this->getID()==NULL) $this->setID(uuid::getUUID(), $domain);
                  
                
                    // New Record
                                    	
                    $sql = "insert into tblobject ( ";
                    
                        $sql.= "title, ";
			$sql.="objectid, ";                        
			$sql.= "comments, ";
                       	$sql.= "objecttypeid, ";                        
                       	$sql.= "description, ";
                       	$sql.= "file, ";
                        $sql.= "creator, ";
                        $sql.= "owner, ";
                        $sql.= "coveragetemporal, ";
                        $sql.= "coveragetemporalfoundation, ";
                        $sql.= "locationgeometry, ";
                        $sql.= "locationcomment, ";
                        $sql.= "locationtypeid, ";
                        $sql.= "locationprecision, ";
                        $sql.= "locationaddressline1, ";
                        $sql.= "locationaddressline2, ";
                        $sql.= "locationcityortown, ";
                        $sql.= "locationstateprovinceregion, ";
                        $sql.= "locationpostalcode, ";
                        $sql.= "locationcountry, ";                            
                        $sql.= "code, ";
                        $sql.= "vegetationtype, ";
                        if(isset($this->parentEntityArray) && count($this->parentEntityArray)>0) $sql.= "parentobjectid, ";
			$sql = substr($sql, 0, -2);
                    $sql.=") values (";
                    
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getTitle()).", ";
	                $sql.= dbHelper::tellervo_pg_escape_string($this->getID()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getComments()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getType(true)).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getDescription()).", ";
                        $sql.= dbHelper::phpArrayToPGStrArray($this->getFiles()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getCreatedTimestamp()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getOwner()).", ";     
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getTemporalCoverage()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getTemporalCoverageFoundation()).", ";
                        $sql.= dbHelper::pg_value_wrapper($this->location->getGeometry()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getComment()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getTypeID()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getPrecision()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getAddressLine1()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getAddressLine2()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getCityOrTown()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getStateProvinceRegion()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getPostalCode()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getCountry()).", ";                        
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getCode()).", ";      
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getVegetationType()).", ";      
                        if(isset($this->parentEntityArray) && count($this->parentEntityArray)>0) $sql.= "'".pg_escape_string($this->parentEntityArray[0]->getID()).", ";
			$sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblobject where objectid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblobject set ";
                        $sql.= "title=".dbHelper::tellervo_pg_escape_string($this->getTitle()).", ";
                        $sql.= "comments=".dbHelper::tellervo_pg_escape_string($this->getComments()).", ";
                      	$sql.= "objecttypeid=".dbHelper::tellervo_pg_escape_string($this->getType(true)).", ";
                      	$sql.= "description=".dbHelper::tellervo_pg_escape_string($this->getDescription()).", ";
                      	$sql.= "file=".dbHelper::phpArrayToPGStrArray($this->getFiles()).", ";
                        $sql.= "creator=".dbHelper::tellervo_pg_escape_string($this->getCreator()).", ";
                        $sql.= "owner=".dbHelper::tellervo_pg_escape_string($this->getOwner()).", ";
                       	$sql.= "coveragetemporal=".dbHelper::tellervo_pg_escape_string($this->getTemporalCoverage()).", ";
                       	$sql.= "coveragetemporalfoundation=".dbHelper::tellervo_pg_escape_string($this->getTemporalCoverageFoundation()).", ";
                       	$sql.= "locationgeometry=".dbHelper::pg_value_wrapper($this->location->getGeometry()).", ";
                       	$sql.= "locationcomment=".dbHelper::tellervo_pg_escape_string($this->location->getComment()).", ";
                       	$sql.= "locationtypeid=".dbHelper::pg_value_wrapper($this->location->getTypeID()).", ";
                       	$sql.= "locationprecision=".dbHelper::tellervo_pg_escape_string($this->location->getPrecision()).", ";
                        $sql.= "locationaddressline1=".dbHelper::tellervo_pg_escape_string($this->location->getAddressLine1()).", ";
                        $sql.= "locationaddressline2=".dbHelper::tellervo_pg_escape_string($this->location->getAddressLine2()).", ";
                        $sql.= "locationcityortown=".dbHelper::tellervo_pg_escape_string($this->location->getCityOrTown()).", ";
                        $sql.= "locationstateprovinceregion=".dbHelper::tellervo_pg_escape_string($this->location->getStateProvinceRegion()).", ";
                        $sql.= "locationpostalcode=".dbHelper::tellervo_pg_escape_string($this->location->getPostalCode()).", ";
                        $sql.= "locationcountry=".dbHelper::tellervo_pg_escape_string($this->location->getCountry()).", ";
                        $sql.= "code=".dbHelper::tellervo_pg_escape_string($this->getCode()).", ";             
                        $sql.= "vegetationtype=".dbHelper::tellervo_pg_escape_string($this->getVegetationType()).", ";             
                        if(isset($this->parentEntityArrayi)) $sql.= "parentobject='".pg_escape_string($this->parentEntityArray[0]->getID()).", ";                    
			$sql = substr($sql, 0, -2);
                    $sql .= " where objectid='".$this->getID()."'";
                }
                //echo $sql;
		$firebug->log($sql, "write sql");

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
                        case "23505":
                        		if(strpos(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_PRIMARY), "object_code_index"))
                        		{
                        			$this->setErrorMessage("908", "An object with the code '".$this->getCode()."' already exists.");	
                        		}	
                        		else
                        		{
                        			$this->setErrorMessage("908", pg_result_error($result)."--- SQL was $sql");	
                        		}
                        default:
                                // Any other error
                                /*$firebug->log(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE), "PGSQL_DIAG_SQLSTATE");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_HINT), "PGSQL_DIAG_MESSAGE_HINT");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_PRIMARY), "PGSQL_DIAG_MESSAGE_PRIMARY");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_MESSAGE_DETAIL), "PGSQL_DIAG_MESSAGE_DETAIL");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SEVERITY), "PGSQL_DIAG_SEVERITY");
                                
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_STATEMENT_POSITION), "PGSQL_DIAG_STATEMENT_POSITION");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_INTERNAL_POSITION), "PGSQL_DIAG_INTERNAL_POSITION");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_INTERNAL_QUERY), "PGSQL_DIAG_INTERNAL_QUERY");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_CONTEXT), "PGSQL_DIAG_CONTEXT");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SOURCE_FILE), "PGSQL_DIAG_SOURCE_FILE");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SOURCE_LINE), "PGSQL_DIAG_SOURCE_LINE");
                                $firebug->log(pg_result_error_field($result, PGSQL_DIAG_SOURCE_FUNCTION), "PGSQL_DIAG_SOURCE_FUNCTION"); 
                             */
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
                        $this->setCreatedTimestamp($row['createdtimestamp']);   
                        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);   
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
	
	/**
	 * Delete this object from the database
	 *
	 * @return Boolean
	 */
	function deleteFromDB()
	{
      // Delete the record in the database matching the current object's ID

        global $dbconn;

        // Check for required parameters
        if($this->getID() == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'id' field is required.");
            return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {

                $sql = "DELETE FROM tblobject WHERE objectid='".$this->getID()."'";
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
                            $this->setErrorMessage("907", "Foreign key violation.  You must delete all entities associated with an object before deleting the object itself.");
                            break;
                    default:
                            // Any other error
                            $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                    }
                    return FALSE;
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
	
	/**
     * Check that the parameters within a defined parameters class are valid
	 *
	 * @param objectParameters $paramsClass
	 * @param String $crudMode
	 * @return Boolean
	 */
	function validateRequestParams($paramsClass, $crudMode)
	{    	
        switch($crudMode)
        {
            case "read":
                if($paramsClass->getID() == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a element.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsClass->getID() == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a element.");
                    return false;
                }
                if(    ($paramsClass->getCode()==NULL)
                    && ($paramsClass->getCreator()==NULL) 
                    && ($paramsClass->getDescription()===NULL)
                    && ($paramsClass->getType()==NULL)
                    && ($paramsClass->getFiles()==NULL)
                    && ($paramsClass->getOwner()==NULL)
                    && ($paramsClass->getTemporalCoverage()==NULL)
                    && ($paramsClass->getTemporalCoverageFoundation()==NULL)
                    && ($paramsClass->getTitle()==NULL) 
                  )
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsClass->getID() == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a element.");
                    return false;
                }
                return true;

            case "create":
                if($paramsClass->hasChild===TRUE)
                {
                    if($paramsClass->getID() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'objectid' field is required when creating an element.");
                        return false;
                    }
                }
                else
                {
                    if($paramsClass->getTitle() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'title' field is required when creating an object.");
                        return false;
                    }
                }
                return true;

            case "merge":
                if($paramsObj->getID() == NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required when merging.", E_USER_ERROR);
                    return false;
                }
                if($paramsObj->mergeWithID == NULL)
                {
                	trigger_error("902"."Missing parameter - 'mergeWithID' field is required when merging.", E_USER_ERROR);
                    return false;
                }
                return true;
                
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
		
	}
	
	function mergeRecords($mergeWithID)
	{
    	global $firebug;
		global $dbconn;
		
		$goodID = $mergeWithID;
		$badID  = $this->getID();
		        
		//Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
        	$sql = "select * from cpgdb.mergeradii('$goodID', '$badID')";
        	$firebug->log($sql, "SQL");
	       	$dbconnstatus = pg_connection_status($dbconn);
	        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
	        {
                // Run SQL 
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn);
                if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                {
                	$PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
                    switch($PHPErrorCode)
                    {
                        default:
                                // Any other error
                                $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
                    }
                    return FALSE;
                }
                else
                {
                	$firebug->log("Merge successful");
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
        
        $this->setParamsFromDB($goodID);
        return TRUE;
	}
	
	
}
?>
