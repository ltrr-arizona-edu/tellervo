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
        $this->setID($row['objectid']);
        $this->setCreatedTimestamp($row['createdtimestamp']);
        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
        $this->setComments($row['comments']);
        $this->setType($row['objecttypeid'], $row['objecttype']);
        $this->setDescription($row['description']);
        $this->setFilesFromStrArray($row['file']);
        $this->setCreator($row['creator']);
        $this->setOwner($row['owner']);
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
    	if ($paramsClass->getTitle()!=NULL)					$this->setTitle($paramsClass->getTitle());
    	if ($paramsClass->getComments()!=NULL)				$this->setComments($paramsClass->getComments());
        if ($paramsClass->getType()!=NULL)					$this->setType($paramsClass->getType(true), $paramsClass->getType());
    	if ($paramsClass->getDescription()!=NULL)			$this->setDescription($paramsClass->getDescription());
    	if ($paramsClass->getFile()!=NULL)					$this->setFiles($paramsClass->getFile());      
        if ($paramsClass->getCreator()!=NULL)		  		$this->setCreator($paramsClass->getCreator());
        if ($paramsClass->getOwner()!=NULL)					$this->setOwner($paramsClass->getOwner());
        if ($paramsClass->getTemporalCoverage()!=NULL)		$this->setCoverageTemporal($paramsClass->getTemporalCoverage(), $paramsClass->getTemporalCoverageFoundation());
        if ($paramsClass->location->getGeometry()!=NULL)	$this->location->setGeometry($paramsClass->location->getGeometry());
        if ($paramsClass->location->getType()!=NULL)		$this->location->setType($paramsClass->location->getType());
        if ($paramsClass->location->getPrecision()!=NULL)	$this->location->setPrecision($paramsClass->location->getPrecision());
        if ($paramsClass->location->getComment()!=NULL)		$this->location->setComment($paramsClass->location->getComment());
		if ($paramsClass->location->getAddressLine1()!=NULL)	$this->location->setAddressLine1($paramsClass->location->getAddressLine1());
		if ($paramsClass->location->getAddressLine2()!=NULL)	$this->location->setAddressLine2($paramsClass->location->getAddressLine2());
		if ($paramsClass->location->getCityOrTown()!=NULL)		$this->location->setCityOrTown($paramsClass->location->getCityOrTown());
		if ($paramsClass->location->getStateProvinceRegion()!=NULL)	$this->location->setStateProvinceRegion($paramsClass->location->getStateProvinceRegion());
		if ($paramsClass->location->getPostalCode()!=NULL)		$this->location->setPostalCode($paramsClass->location->getPostalCode());
		if ($paramsClass->location->getCountry()!=NULL)			$this->location->setCountry($paramsClass->location->getCountry());
		
        
        
        if ($paramsClass->getCode()!=NULL)             		$this->setCode($paramsClass->getCode());
        
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
	        global $corinaNS;
	        global $tridasNS;
	        global $gmlNS;

	        // Create a DOM Document to hold the XML as it's produced
            $xml = new DomDocument();
    		$xml->loadXML("<object xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\"></object>");
    		$xml->formatOutput = true;

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
					$dom->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$obj->asXML()."</root>");
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
		$kml .= "<styleUrl>#corinaDefault</styleUrl>\n";
		$kml .= $this->location->asKML();
		$kml .= "</Placemark>\n";
		if($this->location->asKML(2, "POLYGON")!=NULL)
		{
			$kml .= "<Placemark>\n<name>Extent of ".$this->getTitle()."</name>\n";
			$kml .= "<styleUrl>#corinaDefault</styleUrl>\n";
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
        $xml = NULL;


        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            if(($parts=="all") || ($parts=="beginning"))
            { 
            	$xml.= "<tridas:object>\n";
                $xml.= $this->getIdentifierXML(); 
                if($this->getComments()!=NULL)		$xml.= "<tridas:comments>".dbhelper::escapeXMLChars($this->getComments())."</tridas:comments>\n";    
                									$xml.= "<tridas:type normal=\"".dbhelper::escapeXMLChars($this->getType())."\" normalId=\"".$this->getType(TRUE)."\" normalStd=\"Corina\" />\n";        	
            	if($this->getDescription()!=NULL)	$xml.= "<tridas:description>".dbHelper::escapeXMLChars($this->getDescription())."</tridas:description>";
               	if($this->getCreator()!=NULL)		$xml.= "<tridas:creator>".dbHelper::escapeXMLChars($this->getCreator())."</tridas:creator>";
            	if($this->getOwner()!=NULL)			$xml.= "<tridas:owner>".dbHelper::escapeXMLChars($this->getOwner())."</tridas:owner>";         	
            	
				$xml.= $this->getFileXML();
            	
            	if($this->getTemporalCoverage()!=NULL)
            	{
            		$xml .="<tridas:coverage>";
            		$xml .="<tridas:coverageTemporal>".dbhelper::escapeXMLChars($this->getTemporalCoverage())."</tridas:coverageTemporal>";
            		$xml .="<tridas:coverageTemporalFoundation>".dbhelper::escapeXMLChars($this->getTemporalCoverageFoundation())."</tridas:coverageTemporalFoundation>";
            		$xml .="</tridas:coverage>";
            	}
            	if($this->hasGeometry()) 			$xml.= $this->location->asXML();
            	if($this->hasGeometry())			$xml.="<tridas:genericField name=\"corina.mapLink\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getMapLink())."</tridas:genericField>\n";
            	if($this->getCode()!=NULL)			$xml.="<tridas:genericField name=\"corina.objectLabCode\" type=\"xs:string\">".$this->getCode()."</tridas:genericField>\n";
            	if($this->getCountOfChildVMeasurements()!=NULL) $xml.="<tridas:genericField name=\"corina.countOfChildSeries\" type=\"xs:int\">".$this->getCountOfChildVMeasurements()."</tridas:genericField>\n";
            	
            
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
	function writeToDB()
	{
        // Write the current object to the database

        global $dbconn;
        global $domain;
        global $firebug;
        $sql = "";
        $sql2 = "";


        // Check for required parameters
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

        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->getID() == NULL)
                {
                    // New Record
                    
                	// Generate a new UUID pkey
                	$this->setID(uuid::getUUID(), $domain);
                	
                    $sql = "insert into tblobject ( ";
                        if ($this->getTitle()!=NULL)                                    $sql.= "title, ";
																	                    $sql.= "objectid, ";
                        if ($this->getComments()!=NULL)									$sql.= "comments, ";
                        if ($this->getType()!=NULL)										$sql.= "objecttypeid, ";                        
                        if ($this->getDescription()!=NULL)								$sql.= "description, ";
                        if ($this->getFile()!=NULL)										$sql.= "file, ";
                        if ($this->getCreator()!=NULL)									$sql.= "creator, ";
                        if ($this->getOwner()!=NULL)									$sql.= "owner, ";
                        if ($this->getTemporalCoverage()!=NULL)							$sql.= "coveragetemporal, ";
                        if ($this->getTemporalCoverageFoundation()!=NULL)				$sql.= "coveragetemporalfoundation, ";
                        if ($this->location->getGeometry()!=NULL)						$sql.= "locationgeometry, ";
                        if ($this->location->getComment()!=NULL)						$sql.= "locationcomment, ";
                        if ($this->location->getType()!=NULL)							$sql.= "locationtype, ";
                        if ($this->location->getPrecision()!=NULL)						$sql.= "locationprecision, ";
                        if ($this->location->getAddressLine1()!=NULL)					$sql.= "locationaddressline1, ";
                        if ($this->location->getAddressLine2()!=NULL)					$sql.= "locationaddressline2, ";
                        if ($this->location->getCityOrTown()!=NULL)						$sql.= "locationcityortown, ";
                        if ($this->location->getStateProvinceRegion()!=NULL)			$sql.= "locationstateprovinceregion, ";
                        if ($this->location->getPostalCode()!=NULL)						$sql.= "locationpostalcode, ";
                        if ($this->location->getCountry()!=NULL)						$sql.= "locationcountry, ";                            
                        
                        if ($this->getCode()!=NULL)										$sql.= "code, ";
                        if (count($this->parentEntityArray)>0)							$sql.= "parentobjectid, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if ($this->getTitle()!=NULL)                                    $sql.= "'".pg_escape_string($this->getTitle())."', ";
	                    if ($this->getID()!=NULL)                                       $sql.= "'".pg_escape_string($this->getID())."', ";
                        if ($this->getComments()!=NULL)                                 $sql.= "'".pg_escape_string($this->getComments())."', ";
                        if ($this->getType()!=NULL)										$sql.= "'".pg_escape_string($this->getType(true))."', ";
                        if ($this->getDescription()!=NULL)								$sql.= "'".pg_escape_string($this->getDescription())."', ";
                        if ($this->getFile()!=NULL)										$sql.= "'".dbHelper::phpArrayToPGStrArray($this->getFile())."', ";
                        if ($this->getCreator()!=NULL)									$sql.= "'".pg_escape_string($this->getCreatedTimestamp())."', ";
                        if ($this->getOwner()!=NULL)									$sql.= "'".pg_escape_string($this->getOwner())."', ";     
                        if ($this->getTemporalCoverage()!=NULL)							$sql.= "'".pg_escape_string($this->getTemporalCoverage())."', ";
                        if ($this->getTemporalCoverageFoundation()!=NULL)				$sql.= "'".pg_escape_string($this->getTemporalCoverageFoundation())."', ";
                        if ($this->location->getGeometry()!=NULL)						$sql.= "'".$this->location->getGeometry()."', ";
                        if ($this->location->getComment()!=NULL)						$sql.= "'".pg_escape_string($this->location->getComment())."', ";
                        if ($this->location->getType()!=NULL)							$sql.= "'".pg_escape_string($this->location->getType())."', ";
                        if ($this->location->getPrecision()!=NULL)						$sql.= "'".pg_escape_string($this->location->getPrecision())."', ";
                        if ($this->location->getAddressLine1()!=NULL)					$sql.= "'".pg_escape_string($this->location->getAddressLine1())."', ";
                        if ($this->location->getAddressLine2()!=NULL)					$sql.= "'".pg_escape_string($this->location->getAddressLine2())."', ";
                        if ($this->location->getCityOrTown()!=NULL)						$sql.= "'".pg_escape_string($this->location->getCityOrTown())."', ";
                        if ($this->location->getStateProvinceRegion()!=NULL)			$sql.= "'".pg_escape_string($this->location->getStateProvinceRegion())."', ";
                        if ($this->location->getPostalCode()!=NULL)						$sql.= "'".pg_escape_string($this->location->getPostalCode())."', ";
                        if ($this->location->getCountry()!=NULL)						$sql.= "'".pg_escape_string($this->location->getCountry())."', ";                        
                        if ($this->getCode()!=NULL)										$sql.= "'".pg_escape_string($this->getCode())."', ";      
                        if (count($this->parentEntityArray)>0)							$sql.= "'".pg_escape_string($this->parentEntityArray[0]->getID())."', ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblobject where objectid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblobject set ";
                        if ($this->getTitle()!=NULL)                                    $sql.= "title='".pg_escape_string($this->getTitle())."', ";
                        if ($this->getComments()!=NULL)                                 $sql.= "comments='".pg_escape_string($this->getComments())."', ";
                        if ($this->getType()!=NULL)										$sql.= "objecttypeid='".pg_escape_string($this->getType(true))."', ";
                        if ($this->getDescription()!=NULL)								$sql.= "description='".pg_escape_string($this->getDescription())."', ";
                        if ($this->getFile()!=NULL)										$sql.= "file='".dbHelper::phpArrayToPGStrArray($this->getFile())."', ";
                        if ($this->getCreator()!=NULL)									$sql.= "creator='".pg_escape_string($this->getCreator())."', ";
                        if ($this->getOwner()!=NULL)									$sql.= "owner='".pg_escape_string($this->getOwner())."', ";
                        if ($this->getTemporalCoverage()!=NULL)							$sql.= "coveragetemporal='".pg_escape_string($this->getTemporalCoverage())."', ";
                        if ($this->getTemporalCoverageFoundation()!=NULL)				$sql.= "coveragetemporalfoundation='".pg_escape_string($this->getTemporalCoverageFoundation())."', ";
                        if ($this->location->getGeometry()!=NULL)						$sql.= "locationgeometry='".$this->location->getGeometry()."', ";
                        if ($this->location->getComment()!=NULL)						$sql.= "locationcomment='".pg_escape_string($this->location->getComment())."', ";
                        if ($this->location->getType()!=NULL)							$sql.= "locationtypeid='".$this->location->getTypeID()."', ";
                        if ($this->location->getPrecision()!=NULL)						$sql.= "locationprecision='".pg_escape_string($this->location->getPrecision())."', ";
                        if ($this->location->getAddressLine1()!=NULL)					$sql.= "locationaddressline1='".pg_escape_string($this->location->getAddressLine1())."', ";
                        if ($this->location->getAddressLine2()!=NULL)					$sql.= "locationaddressline2='".pg_escape_string($this->location->getAddressLine2())."', ";
                        if ($this->location->getCityOrTown()!=NULL)						$sql.= "locationcityortown='".pg_escape_string($this->location->getCityOrTown())."', ";
                        if ($this->location->getStateProvinceRegion()!=NULL)			$sql.= "locationstateprovinceregion='".pg_escape_string($this->location->getStateProvinceRegion())."', ";
                        if ($this->location->getPostalCode()!=NULL)						$sql.= "locationpostalcode='".pg_escape_string($this->location->getPostalCode())."', ";
                        if ($this->location->getCountry()!=NULL)						$sql.= "locationcountry='".pg_escape_string($this->location->getCountry())."', ";
                        if ($this->getCode()!=NULL)										$sql.= "code='".pg_escape_string($this->getCode())."', ";             
                        if (count($this->parentEntityArray)>0)							$sql.= "parentobject='".pg_escape_string($this->parentEntityArray[0]->getID())."', ";                    
                        // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql .= " where objectid='".$this->getID()."'";
                }
                //echo $sql;

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
                    && ($paramsClass->getDescription()==NULL)
                    && ($paramsClass->getType()==NULL)
                    && ($paramsClass->getFile()==NULL)
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
