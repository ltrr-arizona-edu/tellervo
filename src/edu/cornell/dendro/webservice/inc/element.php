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
        global $corinaNS;
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
        $this->setID($row['elementid']);
        $this->setCreatedTimestamp($row['createdtimestamp']);       
        $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
        $this->setComments($row['comments']);        
        $this->setType($row['elementtypeid'], $row['elementtype']);
        $this->setDescription($row['description']);
        $this->setFiles($row['file']);    
        $this->taxon->setParamsFromDB($row['taxonid']);
        $this->taxon->setOriginalTaxon($row['originaltaxonname']);
        $this->taxon->setHigherTaxonomy();
        $this->setShape($row['elementshapeid'], $row['elementshape']);        
        $this->setDimensions($row['units'],$row['height'], $row['width'], $row['depth']);       
        $this->setDiameter($row['diameter']);        
        $this->setAuthenticity($row['elementauthenticity']);
        $this->location->setGeometry($row['locationgeometry'], $row['locationtype'], $row['locationprecision'], $row['locationcomment']);
        $this->setProcessing($row['processing']);
        $this->setMarks($row['marks']);     
        $this->setAltitude($row['altitude']);
        $this->setSlope($row['slopeangle'], $row['slopeazimuth']);
        $this->setSoilDepth($row['soildepth']);
        $this->setSoilDescription($row['soildescription']);
        $this->setBedrockDescription($row['bedrockdescription']);                      
        $this->setCode($row['code']);
        $this->setObjectID($row['objectid']);
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
        if ($paramsClass->getTitle()!=NULL)             	$this->setTitle($paramsClass->getTitle());
        if ($paramsClass->getComments()!=NULL)             	$this->setComments($paramsClass->getComments());
        if ($paramsClass->getType()!=NULL)					$this->setType($paramsClass->getType(true), $paramsClass->getType());
        if ($paramsClass->getDescription()!=NULL)			$this->setDescription($paramsClass->getDescription());
        if ($paramsClass->getFile()!=NULL)					$this->setFiles($paramsClass->getFile());  
		if ($paramsClass->taxon->getOriginalTaxon()!=NULL)	$this->taxon->setOriginalTaxon($paramsClass->taxon->getOriginalTaxon());
		if ($paramsClass->taxon->getCoLID()!=NULL)			$this->taxon->setParamsFromCoL($paramsClass->taxon->getCoLID(), $paramsClass->taxon->getLabel());
		if ($paramsClass->getShape()!=NULL)					$this->setShape($paramsClass->getShape());
        if ($paramsClass->hasDimensions())   				$this->setDimensions($paramsClass->getDimensionUnits(), 
        																		 $paramsClass->getDimension('height'), 
        																		 $paramsClass->getDimension('width'), 
        																		 $paramsClass->getDimension('depth'));
		if ($paramsClass->getDimensionUnits()!=NULL)		$this->setDimensionUnits($paramsClass->getDimensionUnits());       
        if ($paramsClass->getDiameter()!=NULL)				$this->setDiameter($paramsClass->getDiameter());
        if ($paramsClass->getAuthenticity()!=NULL)			$this->setAuthenticity($paramsClass->getAuthenticity());
		if ($paramsClass->hasGeometry())					$this->location->setGeometry($paramsClass->location->getLocationGeometry(),
																						 $paramsClass->location->getType(),
																						 $paramsClass->location->getPrecision(),
																						 $paramsClass->location->getComment());        
        if ($paramsClass->getProcessing()!=NULL)			$this->setProcessing($paramsClass->getProcessing());
		if ($paramsClass->getMarks()!=NULL)					$this->setMarks($paramsClass->getMarks());
        if ($paramsClass->getAltitude()!=NULL)				$this->setAltitude($paramsClass->getAltitude());
        if ($paramsClass->getSlopeAngle()!=NULL)			$this->setSlopeAngle($paramsClass->getSlopeAngle());
        if ($paramsClass->getSlopeAzimuth()!=NULL)			$this->setSlopeAzimuth($paramsClass->getSlopeAzimuth());
        if ($paramsClass->getSoilDepth()!=NULL)				$this->setSoilDepth($paramsClass->getSoilDepth());
        if ($paramsClass->getSoilDescription()!=NULL)		$this->setSoilDescription($paramsClass->getSoilDescription());
        if ($paramsClass->getBedrockDescription()!=NULL)	$this->setBedrockDescription($paramsClass->getBedrockDescription());
        if ($paramsClass->getCode()!=NULL)             		$this->setCode($paramsClass->getCode());																				 
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
                    trigger_error("902"."Missing parameter - 'id' field is required when deleting a element.", E_USER_ERROR);
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
	        global $corinaNS;
	        global $tridasNS;
	        global $gmlNS;
	        
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
    		$xml->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->parentEntityArray[0]->asXML('comprehensive')."</root>");                   

    		// We need to locate the leaf tridas:object (one with no child tridas:objects)
    		// because we need to insert our element xml here
	        $xpath = new DOMXPath($xml);
	       	$xpath->registerNamespace('cor', $corinaNS);
	       	$xpath->registerNamespace('tridas', $tridasNS);		    		
    		$nodelist = $xpath->query("//tridas:object[* and not(descendant::tridas:object)]");
    		
    		// Create a temporary DOM document to store our element XML
    		$tempdom = new DomDocument();
			$tempdom->loadXML("<root xmlns=\"$corinaNS\" xmlns:tridas=\"$tridasNS\" xmlns:gml=\"$gmlNS\">".$this->asXML()."</root>");
   		
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
		$kml = "<Placemark><name>".$this->getTitle()."</name><description><![CDATA[<br><b>Type</b>: ".$this->getType()."<br><b>Description</b>: ".dbHelper::escapeXMLChars($this->getDescription())."<br><br><font style=\"font-size: 8px; color: grey\">Created: ".$this->getCreatedTimeStamp('j M Y \a\t H:i')."<br>Last modified: ".$this->getLastModifiedTimestamp('j M Y \a\t H:i')."]]></description>";
		$kml .= "<styleUrl>#corinaDefault</styleUrl>";
		$kml .= $this->location->asKML();
		$kml .= "</Placemark>";
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
                if($this->getComments()!=NULL)					$xml.="<tridas:comments>".$this->getComments()."</tridas:comments>\n";
                $xml.="<tridas:type normal=\"".$this->getType()."\" normalId=\"".$this->getType(TRUE)."\" normalStd=\"Corina\"/>\n";
                if($this->getDescription()!=NULL) $xml.="<tridas:description>".$this->getDescription()."</tridas:description>\n";
                if($format!="minimal")
                {
                	$xml.= $this->getFileXML();
                    $xml.= $this->taxon->asXML();  
                    if($this->getShape()!=NULL)             $xml.= "<tridas:shape normalTridas=\"".$this->getShape()."\" normalId=\"".$this->getShape(TRUE)."\" />\n";
                    if($this->hasDimensions())
                    {
                    	$xml.="<tridas:dimensions>";
                    	/* @todo Units needs completing properly */
                    	$xml.="<tridas:unit>meter</tridas:unit>";
                    	if($this->getDimension('height')!=NULL)   $xml.="<tridas:height>".$this->getDimension('height')."</tridas:height>\n";
                    	if($this->getDimension('width')!=NULL)    $xml.="<tridas:width>".$this->getDimension('width')."</tridas:width>\n";
                    	if($this->getDimension('depth')!=NULL)    $xml.="<tridas:depth>".$this->getDimension('depth')."</tridas:depth>\n";
                    	if($this->getDimension('diameter')!=NULL) $xml.="<tridas:diameter>".$this->getDimension('diameter')."</tridas:diameter>\n";
                    	$xml.="</tridas:dimensions>";                    	
                    }                                     
                    if($this->getAuthenticity()!=NULL)      $xml.= "<tridas:authenticity>".$this->getAuthenticity()."</tridas:authenticity>\n";
                    if($this->hasGeometry()) 
                    {
                        $xml.=$this->location->asXML();
                    }                 
                    
                    if($this->getProcessing()!=NULL) $xml.="<tridas:processing>".$this->getProcessing()."</tridas:processing>\n";
                    if($this->getMarks()!=NULL) $xml.="<tridas:marks>".$this->getMarks()."</tridas:marks>\n";
                    if($this->getAltitude()!=NULL) $xml.="<tridas:altitude>".$this->getAltitude()."</tridas:altitude>\n";
                    if(($this->getSlopeAngle()!=NULL) || ($this->getSlopeAzimuth()!=NULL)) 
                    {
                    	$xml.="<tridas:slope>\n"; 
                    	if($this->getSlopeAngle()!=NULL) $xml.="<tridas:angle>".$this->getSlopeAngle()."</tridas:angle>\n";
                    	if($this->getSlopeAzimuth()!=NULL) $xml.="<tridas:azimuth>".$this->getSlopeAzimuth()."</tridas:azimuth>\n";
                    	$xml.="</tridas:slope>\n";
                    }
                    if(($this->getSoilDepth()!=NULL) || ($this->getSoilDescription()!=NULL))
                    {
                    	$xml.="<tridas:soil>\n";
                    	if($this->getSoilDescription()!=NULL) $xml.="<tridas:description>".dbHelper::escapeXMLChars($this->getSoilDescription())."</tridas:description>\n";
                    	if($this->getSoilDepth()!=NULL) $xml.="<tridas:depth>".$this->getSoilDepth()."</tridas:depth>\n";
                    	$xml.="<tridas:soil>\n";
                    }
                    
                    if($this->getBedrockDescription()!=NULL)	$xml.= "<tridas:bedrock>\n<tridas:description>".$this->getBedrockDescription()."</tridas:description>\n</tridas:bedrock>\n";
                    
	                // Include permissions details if requested            
	                $xml .= $this->getPermissionsXML();                      
                    $xml.= $this->taxon->getHigherTaxonomyXML();
        
                    if($this->hasGeometry())			$xml.="<tridas:genericField name=\"corina.mapLink\" type=\"xs:string\">".dbHelper::escapeXMLChars($this->getMapLink())."</tridas:genericField>\n";

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
    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        global $domain;
        
        $sql = "";
        $sql2 = "";


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
                    	if ($this->getTitle()!=NULL)							$sql.= "code, ";
                    	if ($this->getComments()!=NULL)							$sql.= "comments, ";                    	
                    	if ($this->getType()!=NULL)								$sql.= "elementtypeid, ";
                        if ($this->getDescription()!=NULL)						$sql.= "description, ";	
                        if ($this->getFile()!=NULL)								$sql.= "file, ";                    	
                        if ($this->taxon->getID()!=NULL)                        $sql.= "taxonid, ";
                        if ($this->getShape()!=NULL)							$sql.= "elementshapeid, ";
                        if ($this->hasDimensions())								
	                    {
	                    	if($this->getDimensionUnits()!=NULL)				$sql.= "units, ";
	                    	if($this->getDimension('height')!=NULL)   			$sql.= "height, ";
	                    	if($this->getDimension('width')!=NULL)    			$sql.= "width, ";
	                    	if($this->getDimension('depth')!=NULL)    			$sql.= "depth, ";
	                    	if($this->getDimension('diameter')!=NULL) 			$sql.= "diameter, ";              	
	                    }
                        if ($this->getAuthenticity()!=NULL)						$sql.= "elementauthenticityid, ";	                    
                        if ($this->location->getType()!=NULL)					$sql.= "locationtypeid, ";
                        if ($this->location->getPrecision()!=NULL)				$sql.= "locationprecision, ";
                        if ($this->location->getComment()!=NULL)				$sql.= "locationcomment, ";
                        if ($this->location->getGeometry()!=NULL)				$sql.= "locationgeometry, ";
                        if ($this->getProcessing()!=NULL)						$sql.= "processing, ";
                        if ($this->getMarks()!=NULL)							$sql.= "marks, ";
                        if ($this->getAltitude()!=NULL)							$sql.= "altitude, ";                        
                        if ($this->getSlopeAngle()!=NULL)						$sql.= "slopeangle, "; 
                        if ($this->getSlopeAzimuth()!=NULL)						$sql.= "slopeazimuth, ";    
                        if ($this->getSoilDepth()!=NULL)						$sql.= "soildepth, ";    
						if ($this->getSoilDescription()!=NULL)					$sql.= "soildescription, ";                                            
						if ($this->getBedrockDescription()!=NULL)				$sql.= "bedrockdescription, ";      
                    	if (isset($this->parentEntityArray[0]))					$sql.= "objectid, ";                                                    
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                    	if ($this->getID()!=NULL)								$sql.= "'".pg_escape_string($this->getID()). "', ";										
                    	if ($this->getTitle()!=NULL)							$sql.= "'".pg_escape_string($this->getTitle()).  "', ";                    
                    	if ($this->getComments()!=NULL)							$sql.= "'".pg_escape_string($this->getComments()).  "', ";                    
                        if ($this->getType()!=NULL)								$sql.= "'".pg_escape_string($this->getType(true))."', ";
                        if ($this->getDescription()!=NULL)						$sql.= "'".pg_escape_string($this->getDescription())."', ";                     
                        if ($this->getFile()!=NULL)								$sql.= "'".dbHelper::phpArrayToPGStrArray($this->getFile())."', ";
                        if ($this->taxon->getID()!=NULL)                        $sql.= "'".pg_escape_string($this->taxon->getID()).   "', ";
                        if ($this->getShape()!=NULL)							$sql.= "'".pg_escape_string($this->getShape(true))."', ";
                        if ($this->hasDimensions())								
	                    {
	                    	if($this->getDimensionUnits()!=NULL)				$sql.= "'".pg_escape_string($this->getDimensionUnits())."', ";
	                    	if($this->getDimension('height')!=NULL)   			$sql.= "'".pg_escape_string($this->getDimension('height'))."', ";
	                    	if($this->getDimension('width')!=NULL)    			$sql.= "'".pg_escape_string($this->getDimension('width'))."', ";
	                    	if($this->getDimension('depth')!=NULL)    			$sql.= "'".pg_escape_string($this->getDimension('depth'))."', ";
	                    	if($this->getDimension('diameter')!=NULL) 			$sql.= "'".pg_escape_string($this->getDimension('diameter'))."', ";;              	
	                    }                        
                        if ($this->getAuthenticity()!=NULL)						$sql.= "'".pg_escape_string($this->getAuthenticity(true))."', ";
                        if ($this->location->getType()!=NULL)					$sql.= "'".pg_escape_string($this->location->getTypeID())."', ";
                        if ($this->location->getPrecision()!=NULL)				$sql.= "'".pg_escape_string($this->location->getPrecision())."', ";
                        if ($this->location->getComment()!=NULL)				$sql.= "'".pg_escape_string($this->location->getComment())."', ";
                        if ($this->location->getGeometry()!=NULL)				$sql.= "'".pg_escape_string($this->location->getGeometry())."', ";
                        if ($this->getProcessing()!=NULL)						$sql.= "'".pg_escape_string($this->getProcessing())."', ";
                        if ($this->getMarks()!=NULL)							$sql.= "'".pg_escape_string($this->getMarks())."', ";
                        if ($this->getAltitude()!=NULL)							$sql.= "'".pg_escape_string($this->getAltitude())."', ";
                        if ($this->getSlopeAngle()!=NULL)						$sql.= "'".pg_escape_string($this->getSlopeAngle())."', ";
                        if ($this->getSlopeAzimuth()!=NULL)						$sql.= "'".pg_escape_string($this->getSlopeAzimuth())."', ";
                        if ($this->getSoilDepth()!=NULL)						$sql.= "'".pg_escape_string($this->getSoilDepth())."', ";
                        if ($this->getSoilDescription()!=NULL)					$sql.= "'".pg_escape_string($this->getSoilDescription())."', ";
                        if ($this->getBedrockDescription()!=NULL)				$sql.= "'".pg_escape_string($this->getBedrockDescription())."', ";
                        if (isset($this->parentEntityArray[0]))					$sql.= "'".pg_escape_string($this->parentEntityArray[0]->getID())."', ";                    
                        
                     // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblelement where elementid='".$this->getID()."'";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblelement set ";
                    	if ($this->getTitle()!=NULL)							$sql.= "code='".pg_escape_string($this->getTitle())."', ";
                    	if ($this->getComments()!=NULL)							$sql.= "comments='".pg_escape_string($this->getComments())."', ";
                        if ($this->getType()!=NULL)								$sql.= "elementtypeid='".pg_escape_string($this->getType(true))."', ";
                        if ($this->getDescription()!=NULL)						$sql.= "description='".pg_escape_string($this->getDescription())."', ";	  
                        if ($this->getFile()!=NULL)								$sql.= "file='".dbHelper::phpArrayToPGStrArray($this->getFile())."', ";
                    	if ($this->taxon->getID()!=NULL)                        $sql.= "taxonid='".pg_escape_string($this->taxon->getID())."', ";
                        if ($this->getShape()!=NULL)							$sql.= "elementshapeid='".pg_escape_string($this->getShape(true))."', ";
                        if ($this->hasDimensions())								
	                    {
	                    	if($this->getDimensionUnits()!=NULL)				$sql.= "units='".pg_escape_string($this->getDimensionUnits())."', ";
	                    	if($this->getDimension('height')!=NULL)   			$sql.= "height='".pg_escape_string($this->getDimension('height'))."', ";
	                    	if($this->getDimension('width')!=NULL)    			$sql.= "width='".pg_escape_string($this->getDimension('width'))."', ";
	                    	if($this->getDimension('depth')!=NULL)    			$sql.= "depth='".pg_escape_string($this->getDimension('depth'))."', ";
	                    	if($this->getDimension('diameter')!=NULL) 			$sql.= "diameter='".pg_escape_string($this->getDimension('diameter'))."', ";            	
	                    }                    	
                    	if ($this->getAuthenticity()!=NULL)						$sql.= "elementauthenticityid='".pg_escape_string($this->getAuthenticity(true))."', ";
                        if ($this->location->getType()!=NULL)					$sql.= "locationtypeid='".pg_escape_string($this->location->getTypeID())."', ";
                        if ($this->location->getPrecision()!=NULL)				$sql.= "locationprecision='".pg_escape_string($this->location->getPrecision())."', ";
                        if ($this->location->getComment()!=NULL)				$sql.= "locationcomment='".pg_escape_string($this->location->getComment())."', ";
                        if ($this->location->getGeometry()!=NULL)				$sql.= "locationgeometry='".pg_escape_string($this->location->getGeometry())."', ";
                        if ($this->getProcessing()!=NULL)						$sql.= "processing='".pg_escape_string($this->getProcessing())."', ";
                        if ($this->getMarks()!=NULL)							$sql.= "marks='".pg_escape_string($this->getMarks())."', ";
                        if ($this->getAltitude()!=NULL)							$sql.= "altitude='".pg_escape_string($this->getAltitude())."', ";
                        if ($this->getSlopeAngle()!=NULL)						$sql.= "slopeangle='".pg_escape_string($this->getSlopeAngle())."', ";
                        if ($this->getSlopeAzimuth()!=NULL)						$sql.= "slopeazimuth='".pg_escape_string($this->getSlopeAzimuth())."', ";
                        if ($this->getSoilDepth()!=NULL)						$sql.= "soildepth='".pg_escape_string($this->getSoilDepth())."', ";
                        if ($this->getSoilDescription()!=NULL)					$sql.= "soildescription='".pg_escape_string($this->getSoilDescription())."', ";
                        if ($this->getBedrockDescription()!=NULL)				$sql.= "bedrockdescription='".pg_escape_string($this->getBedrockDescription())."', ";
                        if (isset($this->parentEntityArray[0]))					$sql.= "objectid='".pg_escape_string($this->parentEntityArray[0]->getID())."', ";
                        
                     // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql .= " where elementid='".pg_escape_string($this->getID())."'";
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
                        case 23514:
                                // Foreign key violation
                                $this->setErrorMessage("909", "Check constraint violation.  The location precision specified must be a postive integer.");
                                break;
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

                $sql = "delete from tblelement where elementid=".$this->getID();

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

// End of Class
} 
?>
