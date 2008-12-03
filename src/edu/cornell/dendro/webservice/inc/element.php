<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('dbhelper.php');
require_once('inc/note.php');
require_once('inc/sample.php');
require_once('inc/taxon.php');

class element 
{
    var $id = NULL;
    var $taxonID = NULL;
    var $originalTaxonName = NULL;
    var $subSiteID = NULL;
    var $name = NULL;
    var $latitude = NULL;
    var $longitude = NULL;
    var $precision = NULL;
    var $isLiveelement = NULL;
    var $sampleArray = array();
    var $elementNoteArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
                    
    var $summaryFullLabCode = NULL;

    var $includePermissions = FALSE;
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;

    var $parentXMLTag = "elements"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/
    

	/**
     * Constructor for this class.
     *
     * @return element
     */
    function element()
    { 
    }

    /***********/
    /* SETTERS */
    /***********/

    
    /**
     * // Set the current elements name
     *
     * @param String $theName
     */
    function setName($theName)
    {  
        $this->name=$theName;
    }
    
    /**
     * Set the current elements taxonid
     *
     * @param Integer $theTaxonID
     */
    function setTaxonID($theTaxonID)
    {
        $this->taxonID=$theTaxonID;
    }

    /**
     * Set the current elements subsite ID
     *
     * @param Integer $theSubSiteID
     */
    function setSubSiteID($theSubSiteID)
    {
        $this->subSiteID=$theSubSiteID;
    }
    
    /**
     * Set the current elements latitude.  
     * Deprecated - use setPrecision instead
     *
     * @param Float $theLatitude
     */
    function setLatitude($theLatitude)
    { 
        $this->latitude= (float) $theLatitude;
    }

     /**
     * Set the current objects longitude
     * Deprecated - use setPrecision instead
     *
     * @param Float $theLongitude 
     */
    function setLongitude($theLongitude)
    {
        $this->longitude= (float) $theLongitude;
    }
    
    /**
     * Set the current objects precision in meters
     *
     * @param Float $thePrecision
     */
    function setPrecision($thePrecision)
    {
        $this->precision=$thePrecision;
    }

     /**
      * Set the location of the current elements location
      *
      * @param Float $theLat
      * @param Float $theLong
      * @param Float $thePrecision
      */
    function setLocality($theLat, $theLong, $thePrecision)
    {
        $this->latitude =  $theLat;
        $this->longitude = $theLong;
        $this->precision = $thePrecision;
    }

    /**
     * Set the current elements parameters from the database
     *
     * @param Integer $theID
     * @param String $format (standard or summary. defaults to standard)
     * @return unknown
     */
    function setParamsFromDB($theID, $format="standard")
    {
        global $dbconn;
        
        $this->id=$theID;
        $sql = "select originaltaxonname, elementid, taxonid, subsiteid, name, X(location) as long, Y(location) as lat, precision, createdtimestamp, lastmodifiedtimestamp from tblelement where elementid=$theID";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified id");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->taxonID = $row['taxonid'];
                $this->originalTaxonName = $row['originaltaxonname'];
                $this->subSiteID = $row['subsiteid'];
                $this->name = $row['name'];
                $this->latitude = $row['lat'];
                $this->longitude = $row['long'];
                $this->precision = $row['precision'];
                $this->createdTimeStamp = dateFudge($row['createdtimestamp']);
                $this->lastModifiedTimeStamp = $row['lastmodifiedtimestamp'];
                
                if($format=='summary')
                {
                    $sql = "select cpgdb.getlabel('element', '".$this->id."')";
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    $row = pg_fetch_array($result);
                    $this->summaryFullLabCode = $row['getlabel'];
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
     * @return unknown
     */
    function setChildParamsFromDB()
    {
        global $dbconn;

        $sql  = "select elementnoteid from tblelementelementnote where elementid=".$this->id;
        $sql2 = "select sampleid from tblsample where elementid=".$this->id;
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
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if (isset($paramsClass->name))                  $this->name                 = $paramsClass->name;
        if (isset($paramsClass->originalTaxonName))     $this->originalTaxonName    = $paramsClass->originalTaxonName;
        if (isset($paramsClass->taxonID))               $this->taxonID              = $paramsClass->taxonID;
        if (isset($paramsClass->latitude))              $this->latitude             = $paramsClass->latitude;
        if (isset($paramsClass->longitude))             $this->longitude            = $paramsClass->longitude;
        if (isset($paramsClass->precision))             $this->precision            = $paramsClass->precision;
        if (isset($paramsClass->subSiteID))             $this->subSiteID            = $paramsClass->subSiteID;
        
        if (isset($paramsClass->elementNoteArray))
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
        }   

        return true;
    }

    /**
     * Check that the parameters within a defined parameters class are valid for
     * a specific crudMode
     *
     * @param Parameters Class $paramsObj
     * @param String $crudMode (one of create, read, update or delete)
     * @return unknown
     */
    function validateRequestParams($paramsObj, $crudMode)
    {
        switch($crudMode)
        {
            case "read":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a element.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a element.");
                    return false;
                }
                if(($paramsObj->taxonID==NULL) 
                    && ($paramsObj->name==NULL) 
                    && ($paramsObj->subSiteID==NULL) 
                    && ($paramsObj->latitude==NULL) 
                    && ($paramsObj->longitude==NULL) 
                    && ($paramsObj->precision==NULL)
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a element.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->id == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'elementid' field is required when creating a sample.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a element.");
                        return false;
                    }
                    if($paramsObj->taxonID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'validatedtaxonid' field is required when creating a element.");
                        return false;
                    }
                    if($paramsObj->subSiteID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'subsiteid' field is required.");
                        return false;
                    }
                }
                return true;

            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }

    /**
     * Set an error message for this element
     *
     * @param Integer $theCode
     * @param String $theMessage
     */
    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, 'element', $this->id)";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            $row = pg_fetch_array($result);
            
            $this->canCreate = fromPGtoPHPBool($row['cancreate']);
            $this->canUpdate = fromPGtoPHPBool($row['canupdate']);
            $this->canDelete = fromPGtoPHPBool($row['candelete']);
            $this->includePermissions = TRUE;
    
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        return TRUE;
        
    }


    /***********/
    /*ACCESSORS*/
    /***********/

    /**
     * Get the XML representation of this element
     *
     * @param String $format (one of standard, comprehensive, summary or minimal. Defaults to standard)
     * @param String $parts (one of all, beginning, or end. Defaults to all)
     * @return unknown
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
                where tblelement.elementid='".$this->id."'";

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
    * @param unknown_type $format
    * @param unknown_type $parts
    * @return unknown
    */
    private function _asXML($format, $parts)
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($parts=="all") || ($parts=="beginning"))
            {
                $myTaxon = new taxon;
                $myTaxon->setParamsFromDB($this->taxonID);
                $hasHigherTaxonomy = $myTaxon->setHigherTaxonomy();

                // Only return XML when there are no errors.
                $xml = "<element ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("element", $this->id)."\n";
                
                // Include map reference tag if appropriate
                if( (isset($this->latitude)) && (isset($this->longitude)) )
                {
                    $xml.= getResourceLinkTag("element", $this->id, "map");
                }
                
                // Include permissions details if requested
                if($this->includePermissions===TRUE) 
                {
                    $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->canCreate)."\" ";
                    $xml.= "canUpdate=\"".fromPHPtoStringBool($this->canUpdate)."\" ";
                    $xml.= "canDelete=\"".fromPHPtoStringBool($this->canDelete)."\" />\n";
                } 
                
                if(isset($this->name))                  $xml.= "<name>".escapeXMLChars($this->name)."</name>\n";

                if($format!="minimal")
                {

                    if(isset($this->taxonID))               $xml.= "<validatedTaxon id=\"".$this->taxonID."\">".escapeXMLChars($myTaxon->getLabel())."</validatedTaxon>\n";
                    if(isset($this->originalTaxonName))    $xml.= "<originalTaxonName>".escapeXMLChars($this->originalTaxonName)."</originalTaxonName>\n";

                    if($hasHigherTaxonomy)
                    {
                        $xml.=$myTaxon->getHigherTaxonXML('kingdom');   
                        $xml.=$myTaxon->getHigherTaxonXML('phylum');   
                        $xml.=$myTaxon->getHigherTaxonXML('class');   
                        $xml.=$myTaxon->getHigherTaxonXML('order');   
                        $xml.=$myTaxon->getHigherTaxonXML('family');   
                        $xml.=$myTaxon->getHigherTaxonXML('genus');   
                        $xml.=$myTaxon->getHigherTaxonXML('species');   
                    }

                    if(isset($this->latitude))              $xml.= "<latitude>".$this->latitude."</latitude>\n";
                    if(isset($this->longitude))             $xml.= "<longitude>".$this->longitude."</longitude>\n";
                    if(isset($this->precision))             $xml.= "<precision>".$this->precision."</precision>\n";
                    if(isset($this->isLiveelement))            $xml.= "<isLiveelement>".$this->isLiveelement."</isLiveelement>\n";
                    if(isset($this->createdTimeStamp))      $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                    if(isset($this->lastModifiedTimeStamp)) $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";

                    if($format=='summary')
                    {
                        $xml.="<parentSummary>";
                        $xml.="<fullLabCode>".$this->summaryFullLabCode."</fullLabCode>\n";
                        $xml.="</parentSummary>";
                    }

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
                $xml.= "</element>\n";
            }

            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    /**
     * Get the opening parent XML tag
     *
     * @return unknown
     */
    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblelement")."'>";
        return $xml;
    }

    /**
     * Get the end parent XML tag
     *
     * @return unknown
     */
    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
    }

    /**
    * Get the ID of this element
    *
    * @return unknown
    */
    function getID()
    {
        return $this->id;
    }
    
    /**
    * Get the last error code for this class
    *
    * @return unknown
    */   
    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }

    /**
     * Get the last error message for this class
     *
     * @return unknown
     */
    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
    }

    /***********/
    /*FUNCTIONS*/
    /***********/

    /**
     * Write the current element to the database
     *
     * @return unknown
     */
    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        $sql = "";
        $sql2 = "";


        // Check for required parameters
        if($this->name == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            return FALSE;
        }

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->id == NULL)
                {
                    // New Record
                    $sql = "insert into tblelement ( ";
                        if (isset($this->taxonID))                                  $sql.= "taxonid, ";
                        if (isset($this->subSiteID))                                $sql.= "subsiteid, ";
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
     * @return unknown
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
