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
require_once('inc/note.php');
require_once('inc/sample.php');
require_once('inc/taxon.php');

class element extends elementEntity implements IDBAccessor
{

    protected $sampleArray = array();
    protected $elementNoteArray = array();

                   
    /***************/
    /* CONSTRUCTOR */
    /***************/
    

    public function __construct()
    {
    	parent::__construct();
    }

    public function __destruct()
    {

    }
    /***********/
    /* SETTERS */
    /***********/

    function setParentsFromDB()
    {
        require_once('object.php');
        global $dbconn;
        global $tellervoNS;
        global $tridasNS;
        global $gmlNS;

    	if(($myObjectEntityArray = dbEntity::getCachedEntity("objectEntityArray", $this->objectID)) != NULL)
    	{
    	   $this->parentEntityArray = $myObjectEntityArray;
    	   return;
    	}

                
        // First find the immediate object entity parent
           $sql = "SELECT * from cpgdb.findelementobjectancestors('".pg_escape_string($this->getID())."')";
           $dbconnstatus = pg_connection_status($dbconn);
           if ($dbconnstatus ===PGSQL_CONNECTION_OK)
           {
               pg_send_query($dbconn, $sql);
               $result = pg_get_result($dbconn); 

               if(pg_num_rows($result)==0)
               {
                   // No records match the id specified
                   $this->setErrorMessage("903", "There are no objects associated with element id=".$this->getID());
                   return FALSE;
               }
               else
               {
				   // Empty array before populating it
               	   $this->parentEntityArray = array();
               	   
               	   // Loop through all the parents
                   while($row = pg_fetch_array($result))
                   {
                   		$myObject = new object();
            			$success = $myObject->setParamsFromDB($row['objectid']);
	                   	if($success===FALSE)
	                   	{
	                   	    trigger_error($myObject->getLastErrorCode().$myObject->getLastErrorMessage());
	                   	}  

	                   	// Add to the array of parents
	                   	array_push($this->parentEntityArray,$myObject);
                   }
                   
                   // Reverse array so that the immediate parent is first and is followed by
                   // successively more ancestral parents
                   $this->parentEntityArray = array_reverse($this->parentEntityArray);
                   $this->cacheEntity($this->parentEntityArray, "objectEntityArray", $this->objectID);
               }
           }	
    }


    function setParamsFromDBRow($row, $format="standard")
    {
        $this->setTitle($row['title']);    	
        $this->setID($row['elementid'], $row['domain']);
        $this->setCreatedTimestamp($row['createdtimestamp']);       
        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
        $this->setComments($row['comments']);        
        $this->setType($row['elementtypeid'], $row['elementtype']);
        $this->setDescription($row['description']);
        $this->setFilesFromStrArray($row['file']);
        $this->setShape($row['elementshapeid'], $row['elementshape']);        
        $this->setDimensions($row['height'], $row['width'], $row['depth']);       
        $this->setDimensionUnits($row['unitid'], $row['units']);
        $this->setDiameter($row['diameter']);        
        $this->setAuthenticity($row['authenticity']);
        $this->location->setGeometry($row['locationgeometry'], $row['locationtype'], $row['locationprecision']);
        $this->location->setComment($row['locationcomment']);
        $this->location->setAddressLine1($row['locationaddressline1']);
        $this->location->setAddressLine2($row['locationaddressline2']);
        $this->location->setCityOrTown($row['locationcityortown']);
        $this->location->setStateProvinceRegion($row['locationstateprovinceregion']);
        $this->location->setPostalCode($row['locationpostalcode']);
        $this->location->setCountry($row['locationcountry']);
        $this->setProcessing($row['processing']);
        $this->setMarks($row['marks']);     
        $this->setAltitude($row['altitude']);
        $this->setSlope($row['slopeangle'], $row['slopeazimuth']);
        $this->setSoilDepth($row['soildepth']);
        $this->setSoilDescription($row['soildescription']);
        $this->setBedrockDescription($row['bedrockdescription']);                      
        $this->setCode($row['code']);
        $this->setObjectID($row['objectid']);
        $this->setSummaryObjectCode($row['objectcode']);
        $this->taxon->setParamsFromDBRow($row);


        
        return true;
    }
   
    /**
     * Set the current element's parameters from the database
     *
     * @param UUID $theID
     * @param String $format (standard or summary. defaults to standard)
     * @return unknown
     */
    function setParamsFromDB($theID, $format="standard")
    {
        global $dbconn;
        
        $this->setID($theID);
        //$sql = "select tblelement.* tlkplocationtype.code as locationtype from tlkplocationtype, tblelement where elementid='".$this->getID()."' and tblelement.locationtypeid=tlkplocationtype.locationtypeid";
        $sql = "SELECT * from vwtblelement WHERE elementid='".$this->getID()."'";


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
                
                if($format=='summary')
                {
                    $sql = "select cpgdb.getlabel('element', '".pg_escape_string($this->getID())."')";
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    $row = pg_fetch_array($result);
                    //$this->summaryFullLabCode = $row['getlabel'];
                }   
            }

        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        $this->cacheSelf();
        return TRUE;
    }

    
    /**
     * Add the id's of the current elements direct children from the database
     * i.e. elementNotes and samples
     *
     * @return Boolean
     */
    function setChildParamsFromDB()
    {
        global $dbconn;
		global $firebug;
        
        $sql2 = "select sampleid from tblsample where elementid='".pg_escape_string($this->getID())."'";
        $firebug->log($sql2, "sql for getting children");
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql2);
            while ($row = pg_fetch_array($result))
            {
                // Get all sample id's for this element and store 
                array_push($this->sampleArray, $row['sampleid']);
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
     * Set the current elements parameters from a paramsClass object
     *
     * @param elementParameters $paramsClass
     * @return unknown
     */
    function setParamsFromParamsClass($paramsClass)
    {   
   	
        // Alter the parameter values based upon values supplied by the user and passed as a parameters class
        $this->setTitle($paramsClass->getTitle());
        $this->setComments($paramsClass->getComments());
        $this->setType($paramsClass->getType(true), $paramsClass->getType());
        $this->setDescription($paramsClass->getDescription());
        $this->setFiles($paramsClass->getFile());  
	$this->setShape(null, $paramsClass->getShape());
	$this->setDimensionUnits($paramsClass->getDimensionUnits(true), $paramsClass->getDimensionUnits(false));       
        $this->setDiameter($paramsClass->getDiameter());
        $this->setAuthenticity($paramsClass->getAuthenticity());
      	$this->setProcessing($paramsClass->getProcessing());
      	$this->setMarks($paramsClass->getMarks());
      	$this->setAltitude($paramsClass->getAltitude());
      	$this->setSlopeAngle($paramsClass->getSlopeAngle());
      	$this->setSlopeAzimuth($paramsClass->getSlopeAzimuth());
      	$this->setSoilDepth($paramsClass->getSoilDepth());
      	$this->setSoilDescription($paramsClass->getSoilDescription());
      	$this->setBedrockDescription($paramsClass->getBedrockDescription());
      	$this->setCode($paramsClass->getCode());																				 
	$this->setDimensions($paramsClass->getDimension('height'), 
        	             $paramsClass->getDimension('width'), 
        		     $paramsClass->getDimension('depth'));
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
	  
	if (isset($paramsClass->taxon)){
		$this->taxon->setOriginalTaxon($paramsClass->taxon->getOriginalTaxon());
		$this->taxon->setParamsFromCoL($paramsClass->taxon->getCoLID(), $paramsClass->taxon->getLabel());
	}
        if ($paramsClass->parentID!=NULL)
        {
        	$parentObj = new object();
        	$parentObj->setParamsFromDB($paramsClass->parentID);
        	array_push($this->parentEntityArray, $parentObj);
        }																				 
	return true;        																		 

    }

    /**
     * Check that the parameters within a defined parameters class are valid for
     * a specific crudMode
     *
     * @todo wouldn't it be better to have the permissions functions done here?
     * @param elementParameters $paramsObj
     * @param String $crudMode
     * @return Boolean
     */
    function validateRequestParams($paramsObj, $crudMode)
    {    	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID() == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a element.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->getID() == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a element.");
                    return false;
                }
                if(   ($paramsObj->getType()==NULL)
                   && ($paramsObj->getAuthenticity()==NULL)
                   && ($paramsObj->getDescription()==NULL)
                   && ($paramsObj->hasDimensions()===FALSE)
                   && ($paramsObj->getFile()==NULL)
                   && ($paramsObj->getMarks()==NULL)
                   && ($paramsObj->getProcessing()==NULL)
                   && ($paramsObj->getShape()==NULL)
                   && ($paramsObj->taxon->getCoLID()==NULL)
                   && ($paramsObj->taxon->getOriginalTaxon()==NULL)
                   && ($paramsObj->taxon->getLabel()==NULL)
                   && ($paramsObj->hasChild!=True))
                {
                    trigger_error("902"."Missing parameters - you haven't specified any parameters to update.", E_USER_ERROR);
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->getID() == NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required when deleting an element.", E_USER_ERROR);
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->getID() == NULL) 
                    {
                        trigger_error("902"."Missing parameter - 'elementid' field is required when creating a sample.", E_USER_ERROR);
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->getCode() == NULL) 
                    {
                        trigger_error("902"."Missing parameter - 'code' field is required when creating a element.", E_USER_ERROR);
                        return false;
                    }
                    if($paramsObj->getType()=="")
                    {
                    	trigger_error("902"."Missing parameter - 'elementType' field is required when creating an element", E_USER_ERROR);
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
                trigger_error("667"."Program bug - invalid crudMode specified when validating request", E_USER_ERROR);
                return false;
        }
    }

    
    /***********/
    /*ACCESSORS*/
    /***********/

    
    /**
     * Get the XML representation of this element
     *
     * @param String $format (one of standard, comprehensive, summary or minimal. Defaults to standard)
     * @param String $parts (one of all, beginning, or end. Defaults to all)
     * @return Boolean
     */
    function asXML($format='standard', $parts='all')
    {
        switch($format)
        {
        case "comprehensive":
            require_once('object.php');
            global $dbconn;
	        global $tellervoNS;
	        global $tridasNS;
	        global $gmlNS;
	        global $xlinkNS;
	        
	        // We need to return the comprehensive XML for this element i.e. including all it's ancestral 
	        // object entities.
	        
        	// Make sure the parent entities are set
	        if($this->setParentsFromDB()===FALSE)
	        {
				return FALSE;     
	        }        
	        
            // Grab the XML representation of the immediate parent using the 'comprehensive'
            // attribute so that we get all the object ancestors formatted correctly                   
            $xml = new DomDocument();   
    		$xml->loadXML("<root xmlns=\"$tellervoNS\" xmlns:xlink=\"$xlinkNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".end($this->parentEntityArray)->asXML('comprehensive')."</root>");                   

    		// We need to locate the leaf tridas:object (one with no child tridas:objects)
    		// because we need to insert our element xml here
	        $xpath = new DOMXPath($xml);
	       	$xpath->registerNamespace('cor', $tellervoNS);
	       	$xpath->registerNamespace('tridas', $tridasNS);		    		
    		$nodelist = $xpath->query("//tridas:object[* and not(descendant::tridas:object)]");
    		
    		// Create a temporary DOM document to store our element XML
    		$tempdom = new DomDocument();
			$tempdom->loadXML("<root xmlns=\"$tellervoNS\" xmlns:tridas=\"$tridasNS\" xmlns:xlink=\"$xlinkNS\" xmlns:gml=\"$gmlNS\">".$this->asXML()."</root>");
   		
			// Import and append the element XML node into the main XML DomDocument
			$elemnode = $tempdom->getElementsByTagName("element")->item(0);
			$elemnode = $xml->importNode($elemnode, true);
			$nodelist->item(0)->appendChild($elemnode);

            // Return an XML string representation of the entire shebang
            return $xml->saveXML($xml->getElementsByTagName("object")->item(0));
            
        case "standard":
            return $this->_asXML($format, $parts);
        case "summary":
            return $this->_asXML($format, $parts);
        case "minimal":
            return $this->_asXML($format, $parts);
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary', 'minimal' or 'comprehensive'");
            return false;
        }
    }

	public function asKML()
	{
		$kml = "<Placemark><name>".dbHelper::escapeXMLChars($this->getTitle())."</name><description><![CDATA[<br><b>Type</b>: ".dbHelper::escapeXMLChars($this->getType())."<br><b>Description</b>: ".dbHelper::escapeXMLChars($this->getDescription())."<br><br><font style=\"font-size: 8px; color: grey\">Created: ".$this->getCreatedTimeStamp('j M Y \a\t H:i')."<br>Last modified: ".$this->getLastModifiedTimestamp('j M Y \a\t H:i')."]]></description>";
		$kml .= "<styleUrl>#tellervoDefault</styleUrl>";
		$kml .= $this->location->asKML();
		$kml .= "</Placemark>";
		return $kml;
	}
    
	public function asKMLWithValue($value)
	{
		$kml = "<Placemark><name>".dbHelper::escapeXMLChars($this->getTitle())."</name><description><![CDATA[<br><b>Type</b>: ".dbHelper::escapeXMLChars($this->getType())."<br><b>Description</b>: ".dbHelper::escapeXMLChars($this->getDescription())."<br><br><font style=\"font-size: 8px; color: grey\">Created: ".$this->getCreatedTimeStamp('j M Y \a\t H:i')."<br>Last modified: ".$this->getLastModifiedTimestamp('j M Y \a\t H:i')."]]></description>";
				
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
	
	
   /**
    * Internal function for getting the XML representation of this element.  Use asXML instead.
    *
    * @param String $format
    * @param String $parts
    * @return Boolean
    */
    private function _asXML($format, $parts)
    {
        global $domain;
        global $firebug;
        $xml ="";

        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            if(($parts=="all") || ($parts=="beginning"))
            {

                // Only return XML when there are no errors.
                $xml = "<tridas:element>";
                $xml.= $this->getIdentifierXML();   
                if($this->getComments()!=NULL)					$xml.="<tridas:comments>".dbhelper::escapeXMLChars($this->getComments())."</tridas:comments>\n";
                $xml.="<tridas:type normal=\"".dbhelper::escapeXMLChars($this->getType())."\" normalId=\"".$this->getType(TRUE)."\" normalStd=\"Tellervo\"/>\n";
                if($this->getDescription()!=NULL) $xml.="<tridas:description>".dbhelper::escapeXMLChars($this->getDescription())."</tridas:description>\n";
                $xml.= $this->getFileXML();
                
                $xml.= $this->taxon->asXML();

                    if($this->getShape()!=NULL)             $xml.= "<tridas:shape normalTridas=\"".dbhelper::escapeXMLChars($this->getShape())."\" normalId=\"".$this->getShape(TRUE)."\" />\n";
                    if($this->hasDimensions())
                    {
                    	$xml.="<tridas:dimensions>";
                    	/* @todo Units needs completing properly */
                    	$xml.="<tridas:unit normalTridas=\"".$this->getDimensionUnits()."\" />";
                    	if($this->getDimension('width')!=NULL)
                    	{
                    		// Doing height, width, depth
                    		$xml.="<tridas:height>".dbhelper::escapeXMLChars($this->getDimension('height'))."</tridas:height>\n";
                    	    $xml.="<tridas:width>".dbhelper::escapeXMLChars($this->getDimension('width'))."</tridas:width>\n";
                    	    $xml.="<tridas:depth>".dbhelper::escapeXMLChars($this->getDimension('depth'))."</tridas:depth>\n";
                    		
                    	}
                    	else
                    	{
                    		// Doing height and diameter
                    		$xml.="<tridas:height>".dbhelper::escapeXMLChars($this->getDimension('height'))."</tridas:height>\n";
                    		$xml.="<tridas:diameter>".dbhelper::escapeXMLChars($this->getDimension('diameter'))."</tridas:diameter>\n";
                    		
                    	}
                    	
                    	$xml.="</tridas:dimensions>";                    	
                    }                                     
                    if($this->getAuthenticity()!=NULL)      $xml.= "<tridas:authenticity>".dbhelper::escapeXMLChars($this->getAuthenticity())."</tridas:authenticity>\n";
                     
                   $xml.=$this->location->asXML();
                    
                    if($this->getProcessing()!=NULL) $xml.="<tridas:processing>".dbhelper::escapeXMLChars($this->getProcessing())."</tridas:processing>\n";
                    if($this->getMarks()!=NULL) $xml.="<tridas:marks>".dbhelper::escapeXMLChars($this->getMarks())."</tridas:marks>\n";
                    if($this->getAltitude()!=NULL) $xml.="<tridas:altitude>".dbhelper::escapeXMLChars($this->getAltitude())."</tridas:altitude>\n";
                    if(($this->getSlopeAngle()!=NULL) || ($this->getSlopeAzimuth())!=NULL) 
                    {
                    	$xml.="<tridas:slope>\n"; 
                    	if($this->getSlopeAngle()!=NULL) $xml.="<tridas:angle>".dbhelper::escapeXMLChars($this->getSlopeAngle())."</tridas:angle>\n";
                    	if($this->getSlopeAzimuth()!=NULL) $xml.="<tridas:azimuth>".dbhelper::escapeXMLChars($this->getSlopeAzimuth())."</tridas:azimuth>\n";
                    	$xml.="</tridas:slope>\n";
                    }
                    if(($this->getSoilDepth()!=NULL) || (dbhelper::escapeXMLChars($this->getSoilDescription())!=NULL))
                    {
                    	$xml.="<tridas:soil>\n";
                    	if($this->getSoilDescription()!=NULL) $xml.="<tridas:description>".dbHelper::escapeXMLChars($this->getSoilDescription())."</tridas:description>\n";
                    	if($this->getSoilDepth()!=NULL) $xml.="<tridas:depth>".dbhelper::escapeXMLChars($this->getSoilDepth())."</tridas:depth>\n";
                    	$xml.="</tridas:soil>\n";
                    }
                    
                    if($this->getBedrockDescription()!=NULL)	$xml.= "<tridas:bedrock>\n<tridas:description>".dbhelper::escapeXMLChars($this->getBedrockDescription())."</tridas:description>\n</tridas:bedrock>\n";
                    
	                // Include permissions details if requested            
	                $xml .= $this->getPermissionsXML();    

	                
	                if($format!="minimal") $xml.= $this->taxon->getHigherTaxonomyXML();
        
            		$xml.="<tridas:genericField name=\"tellervo.objectLabCode\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getSummaryObjectCode())."</tridas:genericField>\n";           
                    
                    if($format=="summary")
                    {
                    	
                    	
                    	$this->setChildParamsFromDB();
                    	$firebug->log(__LINE__, "at line");
                    	$firebug->log($this->sampleArray, "samplearray");
                        // Include samples if present
                        if ($this->sampleArray)
                        {
                            foreach($this->sampleArray as $value)
                            {
                                $mysample = new sample();
                                $success = $mysample->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$mysample->asXML("minimal", "all");
                                }
                                else
                                {
                                    $myMetaHeader->setErrorMessage($mysample->getLastErrorCode, $mysample->getLastErrorMessage);
                                }
                            }
                        }
                    }
                }

            if(($parts=="all") || ($parts=="end"))
            {
                // End XML tag
                $xml.= "</tridas:element>\n";
            }

            return $xml;
        }
        else
        {
            return FALSE;
        }
    }


    /***********/
    /*FUNCTIONS*/
    /***********/

    /**
     * Write the current element to the database
     *
     * @return Boolean
     */
    function writeToDB($crudMode="create")
    {
        // Write the current object to the database

        global $dbconn;
        global $domain;
        
        $sql = "";
        $sql2 = "";


        // Check for required parameters
        if($crudMode!="create" && $crudMode!="update")
        {
	    $this->setErrorMessage("667", "Invalid mode specified in writeToDB().  Only create and update are supported");
	    return FALSE;
        }        
        
        // Check for required parameters
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
                	
                    $sql = "insert into tblelement ( ";
             		$sql.= "elementid, ";
                    	$sql.= "code, ";
                    	$sql.= "comments, ";                    	
                    	$sql.= "elementtypeid, ";
                        $sql.= "description, ";	
                        $sql.= "file, ";                    	
                        $sql.= "taxonid, ";
                        $sql.= "elementshapeid, ";
                        if ($this->hasDimensions())								
	                    {
	                    	$sql.= "units, ";
	                    	$sql.= "height, ";
	                    	$sql.= "width, ";
	                    	$sql.= "depth, ";
	                    	$sql.= "diameter, ";              	
	                    }
                       	$sql.= "authenticity, ";	                    
                       	$sql.= "locationtypeid, ";
                       	$sql.= "locationprecision, ";
                       	$sql.= "locationcomment, ";
                       	$sql.= "locationgeometry, ";
                       	$sql.= "locationaddressline1, ";
                       	$sql.= "locationaddressline2, ";
                       	$sql.= "locationcityortown, ";
                        $sql.= "locationstateprovinceregion, ";
                       	$sql.= "locationpostalcode, ";
                       	$sql.= "locationcountry, ";                                                                        
                       	$sql.= "processing, ";
                       	$sql.= "marks, ";
                       	$sql.= "altitude, ";                        
                       	$sql.= "slopeangle, "; 
                       	$sql.= "slopeazimuth, ";    
                       	$sql.= "soildepth, ";    
		       	$sql.= "soildescription, ";                                            
		       	$sql.= "bedrockdescription, ";      
                    	if (isset($this->parentEntityArray[0]))					$sql.= "objectid, ";                                                    
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getID()). ", ";										
                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getTitle()).  ", ";                    
                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getComments()). ", ";                    
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getType(true)).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getDescription()).", ";                     
                       	$sql.= dbHelper::phpArrayToPGStrArray($this->getFile()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->taxon->getID()). ", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getShape(true)).", ";
                        if ($this->hasDimensions())								
	                    {
	                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getDimensionUnits(true))."', ";
	                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getDimension('height'))."', ";
	                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getDimension('width'))."', ";
	                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getDimension('depth'))."', ";
	                    	$sql.= dbHelper::tellervo_pg_escape_string($this->getDimension('diameter'))."', ";;              	
	                    }                        
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getAuthenticity()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getTypeID()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getPrecision()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getComment()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getGeometry()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getAddressLine1()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->location->getAddressLine2()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->location->getCityOrTown()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->location->getStateProvinceRegion()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->location->getPostalCode()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->location->getCountry()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getProcessing()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getMarks()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getAltitude()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getSlopeAngle()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getSlopeAzimuth()).", ";
                       	$sql.= dbHelper::tellervo_pg_escape_string($this->getSoilDepth()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getSoilDescription()).", ";
                        $sql.= dbHelper::tellervo_pg_escape_string($this->getBedrockDescription()).", ";
                        if (isset($this->parentEntityArray[0]))	$sql.= dbHelper::tellervo_pg_escape_string($this->parentEntityArray[0]->getID()).", ";                    
                        
                     // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblelement where elementid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblelement set ";
                    	$sql.= "code=".dbHelper::tellervo_pg_escape_string($this->getTitle()).", ";
                    	$sql.= "comments=".dbHelper::tellervo_pg_escape_string($this->getComments()).", ";
                       	$sql.= "elementtypeid=".dbHelper::tellervo_pg_escape_string($this->getType(true)).", ";
                       	$sql.= "description=".dbHelper::tellervo_pg_escape_string($this->getDescription()).", ";	  
                       	$sql.= "file=".dbHelper::phpArrayToPGStrArray($this->getFile()).", ";
                        $sql.= "taxonid=".dbHelper::tellervo_pg_escape_string($this->taxon->getID()).", ";
                    	$sql.= "elementshapeid=".dbHelper::tellervo_pg_escape_string($this->getShape(true)).", ";
                        if ($this->hasDimensions())								
	                    {
	                    	$sql.= "units=".dbHelper::tellervo_pg_escape_string($this->getDimensionUnits(true)).", ";
	                    	$sql.= "height=".dbHelper::tellervo_pg_escape_string($this->getDimension('height')).", ";
	                    	$sql.= "width=".dbHelper::tellervo_pg_escape_string($this->getDimension('width')).", ";
	                    	$sql.= "depth=".dbHelper::tellervo_pg_escape_string($this->getDimension('depth')).", ";
	                    	$sql.= "diameter=".dbHelper::tellervo_pg_escape_string($this->getDimension('diameter')).", ";            	
	                    }                    	
                    	$sql.= "authenticity=".dbHelper::tellervo_pg_escape_string($this->getAuthenticity()).", ";
                    	$sql.= "locationtypeid=".dbHelper::tellervo_pg_escape_string($this->location->getTypeID()).", ";
                    	$sql.= "locationprecision=".dbHelper::tellervo_pg_escape_string($this->location->getPrecision()).", ";
                    	$sql.= "locationcomment=".dbHelper::tellervo_pg_escape_string($this->location->getComment()).", ";
                    	$sql.= "locationgeometry=".dbHelper::tellervo_pg_escape_string($this->location->getGeometry()).", ";
                        $sql.= "locationaddressline1=".dbHelper::tellervo_pg_escape_string($this->location->getAddressLine1()).", ";
                        $sql.= "locationaddressline2=".dbHelper::tellervo_pg_escape_string($this->location->getAddressLine2()).", ";
                        $sql.= "locationcityortown=".dbHelper::tellervo_pg_escape_string($this->location->getCityOrTown()).", ";
                        $sql.= "locationstateprovinceregion=".dbHelper::tellervo_pg_escape_string($this->location->getStateProvinceRegion()).", ";
                        $sql.= "locationpostalcode=".dbHelper::tellervo_pg_escape_string($this->location->getPostalCode()).", ";
                        $sql.= "locationcountry=".dbHelper::tellervo_pg_escape_string($this->location->getCountry()).", ";
                        $sql.= "processing=".dbHelper::tellervo_pg_escape_string($this->getProcessing()).", ";
                        $sql.= "marks=".dbHelper::tellervo_pg_escape_string($this->getMarks()).", ";
                        $sql.= "altitude=".dbHelper::tellervo_pg_escape_string($this->getAltitude()).", ";
                        $sql.= "slopeangle=".dbHelper::tellervo_pg_escape_string($this->getSlopeAngle()).", ";
                        $sql.= "slopeazimuth=".dbHelper::tellervo_pg_escape_string($this->getSlopeAzimuth()).", ";
                        $sql.= "soildepth=".dbHelper::tellervo_pg_escape_string($this->getSoilDepth()).", ";
                        $sql.= "soildescription=".dbHelper::tellervo_pg_escape_string($this->getSoilDescription()).", ";
                        $sql.= "bedrockdescription=".dbHelper::tellervo_pg_escape_string($this->getBedrockDescription()).", ";
                        if (isset($this->parentEntityArray[0]))	$sql.= "objectid=".dbHelper::tellervo_pg_escape_string($this->parentEntityArray[0]->getID()).", ";
                        
                     // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql .= " where elementid='".pg_escape_string($this->getID())."'";
	
                }
global $firebug;
                
                $firebug->log($sql, "SQL");
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
                        case 23514:
				if(strpos(pg_result_error($result), "enforce_valid_dimensions")!==FALSE)
				{
					$this->setErrorMessage("909", "Invalid dimensions.  If dimensions are included they should include height and diameter; or height, width and depth.");
	                                break;
				}
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
     * Delete the current element from the database
     *
     * @return Boolean
     */
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

                $sql = "delete from tblelement where elementid='".$this->getID()."'";

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all samples associated with a element before deleting the element itself.");
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
    
	function mergeRecords($mergeWithID)
	{
		global $firebug;
		global $dbconn;
		
		$goodID = $mergeWithID;
		$badID  = $this->getID();
		        
		//Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
        	$sql = "select * from cpgdb.mergeelements('$goodID', '$badID')";
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

// End of Class
} 
?>
