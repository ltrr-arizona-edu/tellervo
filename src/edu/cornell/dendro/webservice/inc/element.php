<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
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
        $groupXMLTag = "element";
    	parent::__construct($groupXMLTag);
    }

    public function __destruct()
    {

    }
    /***********/
    /* SETTERS */
    /***********/

   
    /**
     * Set the current element's parameters from the database
     *
     * @param Integer $theID
     * @param String $format (standard or summary. defaults to standard)
     * @return unknown
     */
    function setParamsFromDB($theID, $format="standard")
    {
        global $dbconn;
        
        $this->setID($theID);
        //$sql = "select tblelement.* tlkplocationtype.name as locationtype from tlkplocationtype, tblelement where elementid='".$this->getID()."' and tblelement.locationtypeid=tlkplocationtype.locationtypeid";
        $sql = "select * from tblelement left outer join (select locationtypeid, name as locationtype from tlkplocationtype) as loctype on (tblelement.locationtypeid = loctype.locationtypeid) where elementid='".$this->getID()."'";


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
                //$this->subSiteID = $row['subsiteid'];
                $this->setTaxonByID($row['taxonid']);
                $this->setOriginalTaxon($row['originaltaxonname']);
                $this->setName($row['name']);
                $this->setCreatedTimestamp($row['createdtimestamp']);
                $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
                $this->setAuthenticity($row['authenticity']);
                $this->setShape($row['shape']);
                $this->setDimensions($row['units'],$row['height'], $row['width'], $row['depth']);
                $this->setDiameter($row['diameter']);
                $this->setType($row['type']);
                $this->geometry->setGeometry($row['location'], $row['locationtype'], $row['locationprecision'], $row['locationcomment']);
                $this->setProcessing($row['processing']);
                $this->setMarks($row['marks']);
                $this->setDescription($row['description']);
                
                
                if($format=='summary')
                {
                    $sql = "select cpgdb.getlabel('element', '".$this->getID()."')";
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

        $sql  = "select elementnoteid from tblelementelementnote where elementid=".$this->getID();
        $sql2 = "select sampleid from tblsample where elementid=".$this->getID();
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all element note id's for this element and store 
                array_push($this->elementNoteArray, $row['elementnoteid']);
            }

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
     * @param Parameter Class $paramsClass
     * @return unknown
     */
    function setParamsFromParamsClass($paramsClass)
    {    	
    	// First validate that the parameters class is valid
    	if(!$this->validateRequestParams($paramsClass))
    	{
    		return false;
    	}
    	    	
        // Alter the parameter values based upon values supplied by the user and passed as a parameters class
        if ($paramsClass->getName()!=NULL)             $this->setName($paramsClass->getName());
        //if ($paramsClass->originalTaxonName))     $this->originalTaxonName    = $paramsClass->originalTaxonName;
        //if ($paramsClass->taxon->getTaxonID()!=NULL)   $this->setTaxonByID($paramsClass->taxon->getTaxonID());
        //if ($paramsClass->latitude))              $this->latitude             = $paramsClass->latitude;
        //if ($paramsClass->longitude))             $this->longitude            = $paramsClass->longitude;
        //if ($paramsClass->geometry->getPrecision()!=NULL)   $this->geometry->setPrecision($paramsClass->getPrecision());
        //if ($paramsClass->subSiteID))             $this->subSiteID            = $paramsClass->subSiteID;
        
        /*if ($paramsClass->elementNoteArray)
        {
            // Remove any existing site notes ready to be replaced with what user has specified
            unset($this->elementNoteArray);
            $this->elementNoteArray = array();

            if($paramsClass->elementNoteArray[0]!='empty')
            {
                foreach($paramsClass->elementNoteArray as $item)
                {
                    array_push($this->elementNoteArray, (int) $item[0]);
                }
            }
        }*/   

        return true;
    }

    /**
     * Check that the parameters within a defined parameters class are valid for
     * a specific crudMode
     *
     * @todo wouldn't it be better to have the permissions functions done here?
     * @param elementParameters $paramsObj
     * @return Boolean
     */
    function validateRequestParams($paramsObj)
    {
    	global $myRequest;
    	
        switch($myRequest->getCrudMode())
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
                if(($paramsObj->taxonID==NULL) 
                    && ($paramsObj->name==NULL) 
                    //&& ($paramsObj->subSiteID==NULL) 
                    //&& ($paramsObj->latitude==NULL) 
                    //&& ($paramsObj->longitude==NULL) 
                    && ($paramsObj->getPrecision()==NULL)
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->getID() == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a element.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->getID() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'elementid' field is required when creating a sample.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->getName() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a element.");
                        return false;
                    }
                    /*if($paramsObj->taxonID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'validatedtaxonid' field is required when creating a element.");
                        return false;
                    }
                    if($paramsObj->subSiteID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'subsiteid' field is required.");
                        return false;
                    }*/
                }
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
            require_once('site.php');
            require_once('subSite.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid, tblelement.elementid
                FROM tblsubsite 
                INNER JOIN tblelement ON tblsubsite.subsiteid=tblelement.subsiteid
                where tblelement.elementid='".$this->getID()."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for element id=".$this->id);
                    return FALSE;
                }
                else
                {
                    $row = pg_fetch_array($result);

                    $mySite = new site();
                    $mySubSite = new subSite();

                    $success = $mySite->setParamsFromDB($row['siteid']);
                    if($success===FALSE)
                    {
                        trigger_error($mySite->getLastErrorCode().$mySite->getLastErrorMessage());
                    }
                    
                    $success = $mySubSite->setParamsFromDB($row['subsiteid']);
                    if($success===FALSE)
                    {
                        trigger_error($mySubSite->getLastErrorCode().$mySubSite->getLastErrorMessage());
                    }

                    $xml = $mySite->asXML("summary", "beginning");
                    $xml.= $mySubSite->asXML("summary", "beginning");
                    $xml.= $this->_asXML("standard", "all");
                    $xml.= $mySubSite->asXML("summary", "end");
                    $xml.= $mySite->asXML("summary", "end");

                }
            }
            return $xml;
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
        $xml ="";

        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            if(($parts=="all") || ($parts=="beginning"))
            {

                // Only return XML when there are no errors.
                $xml = "<tridas:element>";
                $xml.= $this->getIdentifierXML();   
                
                // Include permissions details if requested            
                $xml .= $this->getPermissionsXML();
                
                if($this->getName()!=NULL)                        $xml.= "<tridas:genericField name=\"name\">".escapeXMLChars($this->getName())."</tridas:genericField>\n";
                

                if($format!="minimal")
                {
                    if($this->getAuthenticity()!=NULL)      $xml.= "<tridas:authenticity>".$this->getAuthenticity()."</tridas:authenticity>\n";
                    if($this->getShape()!=NULL)             $xml.= "<tridas:shape>".$this->getShape()."</tridas:shape>\n";
                    if($this->hasDimensions())
                    {
                    	$xml.="<tridas:dimensions>";
                    	/* @todo Units needs completing properly */
                    	$xml.="<tridas:unit>meter</tridas:unit>";
                    	if($this->getDimension('height')!=NULL)   $xml.="<tridas:height>".$this->getDimension('height')."</tridas:height>";
                    	if($this->getDimension('width')!=NULL)    $xml.="<tridas:width>".$this->getDimension('width')."</tridas:width>";
                    	if($this->getDimension('depth')!=NULL)    $xml.="<tridas:depth>".$this->getDimension('depth')."</tridas:depth>";
                    	if($this->getDimension('diameter')!=NULL) $xml.="<tridas:diameter>".$this->getDimension('diameter')."</tridas:diameter>";
                    	$xml.="</tridas:dimensions>";                    	
                    }
                    if($this->getType()!=NULL) $xml.="<tridas:type>".$this->getType()."</tridas:type>";
                    if($this->getFile()!=NULL) $xml.="<tridas:file xlink:href=\"".$this->getFile()."\" />";
                    if($this->hasGeometry()) 
                    {
                        $xml.=$this->geometry->asXML();
                    }
                    if($this->getProcessing()!=NULL) $xml.="<tridas:processing>".$this->getProcessing()."</tridas:processing>";
                    if($this->getMarks()!=NULL) $xml.="<tridas:marks>".$this->getMarks()."</tridas:marks>";  
                    if($this->getDescription()!=NULL) $xml.="<tridas:description>".$this->getDescription()."</tridas:description>";                                      
                    $xml.= $this->taxon->asXML();                  

                    //if(isset($this->taxonID))               $xml.= "<validatedTaxon id=\"".$this->taxonID."\">".escapeXMLChars($myTaxon->getLabel())."</validatedTaxon>\n";
                    //if(isset($this->originalTaxonName))    $xml.= "<originalTaxonName>".escapeXMLChars($this->originalTaxonName)."</originalTaxonName>\n";

					$xml.= $this->taxon->getHigherTaxonXML('kingdom');   
                    $xml.= $this->taxon->getHigherTaxonXML('phylum');   
                    $xml.= $this->taxon->getHigherTaxonXML('class');   
                    $xml.= $this->taxon->getHigherTaxonXML('order');   
                    $xml.= $this->taxon->getHigherTaxonXML('family');   
                    $xml.= $this->taxon->getHigherTaxonXML('genus');   
                    $xml.= $this->taxon->getHigherTaxonXML('species');   
               

                    //if(isset($this->latitude))              $xml.= "<latitude>".$this->latitude."</latitude>\n";
                    //if(isset($this->longitude))             $xml.= "<longitude>".$this->longitude."</longitude>\n";
                    //if(isset($this->precision))             $xml.= "<precision>".$this->precision."</precision>\n";
                    //if(isset($this->isLiveelement))            $xml.= "<isLiveelement>".$this->isLiveelement."</isLiveelement>\n";
                    if($this->getCreatedTimeStamp()!=NULL)      $xml.= "<tridas:genericField name=\"createdTimeStamp\">".$this->getCreatedTimeStamp()."</tridas:genericField>\n";
                    if($this->getLastModifiedTimeStamp()!=NULL) $xml.= "<tridas:genericField name=\"lastModifiedTimeStamp\">".$this->getLastModifiedTimeStamp()."</tridas:genericField>\n";

                    if($format=='summary') $xml.="<tridas:genericField name=\"fullLabCode\">".$this->summaryFullLabCode."</tridas:genericField>\n";

                    if($format!="summary")
                    {
                    
                        // Include element notes if present
                        if ($this->elementNoteArray)
                        {
                            foreach($this->elementNoteArray as $value)
                            {
                                $myelementNote = new elementNote();
                                $success = $myelementNote->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$myelementNote->asXML();
                                }
                                else
                                {
                                    $myMetaHeader->setErrorMessage($myelementNote->getLastErrorCode, $myelementNote->getLastErrorMessage);
                                }
                            }
                        }

                        // Include samples if present
                        if (($this->sampleArray) && ($format=="standard"))
                        {
                            $xml.="<references>\n";
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
                            $xml.="</references>\n";
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
        $sql = "";
        $sql2 = "";


        // Check for required parameters
        if($this->getName() == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
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
                    $sql = "insert into tblelement ( ";
                        if ($this->taxon->getTaxonID()!=NULL)                       $sql.= "taxonid, ";
                        if (isset($this->getP))                                $sql.= "subsiteid, ";
                        if (isset($this->name))                                     $sql.= "name, ";
                        if (isset($this->precision))                                $sql.= "precision, ";
                        if (isset($this->isLiveelement))                               $sql.= "isliveelement, ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "location, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if (isset($this->taxonID))                                  $sql.= "'".$this->taxonID.   "', ";
                        if (isset($this->subSiteID))                                $sql.= "'".$this->subSiteID. "', ";
                        if (isset($this->name))                                     $sql.= "'".$this->name.     "', ";
                        if (isset($this->precision))                                $sql.= "'".$this->precision. "', ";
                        if (isset($this->isLiveelement))                               $sql.="'".fromPHPtoPGBool($this->isLiveelement)."', ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "setsrid(makepoint(".sprintf("%1.8f",$this->longitude).", ".sprintf("%1.8f",$this->latitude)."), 4326), ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblelement where elementid=currval('tblelement_elementid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblelement set ";
                        if (isset($this->taxonID))                                  $sql.= "taxonid='".    $this->taxonID    ."', ";
                        if (isset($this->subSiteID))                                $sql.= "subsiteid='".  $this->subSiteID  ."', ";
                        if (isset($this->name))                                     $sql.= "name='".      $this->name      ."', ";
                        if (isset($this->precision))                                $sql.= "precision='".  $this->precision  ."', ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "location=setsrid(makepoint(".sprintf("%1.8f",$this->longitude).", ".sprintf("%1.8f",$this->latitude)."), 4326), ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql .= " where elementid=".$this->id;
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
                        $this->id=$row['elementid'];   
                        $this->createdTimeStamp=$row['createdtimestamp'];   
                        $this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
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

                $sql = "delete from tblelement where elementid=".$this->id;

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
