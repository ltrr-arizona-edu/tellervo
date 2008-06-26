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
require_once('inc/specimen.php');
require_once('inc/taxon.php');

class tree 
{
    var $id = NULL;
    var $taxonID = NULL;
    var $originalTaxonName = NULL;
    var $subSiteID = NULL;
    var $name = NULL;
    var $latitude = NULL;
    var $longitude = NULL;
    var $precision = NULL;
    var $isLiveTree = NULL;
    var $specimenArray = array();
    var $treeNoteArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;

    var $includePermissions = FALSE;
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;

    var $parentXMLTag = "trees"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function tree()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/

    function setName($theName)
    {
        // Set the current objects name
        $this->name=$theName;
    }
    
    function setTaxonID($theTaxonID)
    {
        // Set the current objects taxonid
        $this->taxonID=$theTaxonID;
    }

    function setSubSiteID($theSubSiteID)
    {
        // Set the current objects subsite ID
        $this->subSiteID=$theSubSiteID;
    }
    

    function setLatitude($theLatitude)
    {
        // Set the current objects latitude 
        $this->latitude= (float) $theLatitude;
    }
    
    function setLongitude($theLongitude)
    {
        // Set the current objects longitude 
        $this->longitude= (float) $theLongitude;
    }
    
    function setPrecision($thePrecision)
    {
        // Set the current objects precision 
        $this->precision=$thePrecision;
    }

    function setLocality($theLat, $theLong, $thePrecision)
    {
        // Set the location of the current object
        $this->latitude = $theLat;
        $this->longitude = $theLong;
        $this->precision = $thePrecision;
    }

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $this->id=$theID;
        $sql = "select originaltaxonname, treeid, taxonid, subsiteid, name, X(location) as long, Y(location) as lat, precision, createdtimestamp, lastmodifiedtimestamp from tbltree where treeid=$theID";
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

    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database
        // Specimen

        global $dbconn;

        $sql  = "select treenoteid from tbltreetreenote where treeid=".$this->id;
        $sql2 = "select specimenid from tblspecimen where treeid=".$this->id;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->treeNoteArray, $row['treenoteid']);
            }

            $result = pg_query($dbconn, $sql2);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->specimenArray, $row['specimenid']);
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
        
        if (isset($paramsClass->treeNoteArray))
        {
            // Remove any existing site notes ready to be replaced with what user has specified
            unset($this->treeNoteArray);
            $this->treeNoteArray = array();

            if($paramsClass->treeNoteArray[0]!='empty')
            {
                foreach($paramsClass->treeNoteArray as $item)
                {
                    array_push($this->treeNoteArray, (int) $item[0]);
                }
            }
        }   

        return true;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a tree.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a tree.");
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
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a tree.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->id == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'treeid' field is required when creating a specimen.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a tree.");
                        return false;
                    }
                    if($paramsObj->taxonID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'validatedtaxonid' field is required when creating a tree.");
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

    
    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, 'tree', $this->id)";
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

    function asXML($format='standard', $parts='all')
    {
        switch($format)
        {
        case "comprehensive":
            require_once('site.php');
            require_once('subSite.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid, tbltree.treeid
                FROM tblsubsite 
                INNER JOIN tbltree ON tblsubsite.subsiteid=tbltree.subsiteid
                where tbltree.treeid='".$this->id."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for tree id=".$this->id);
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
                $xml = "<tree ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("tree", $this->id)."\n";
                $xml.= getResourceLinkTag("tree", $this->id, "map")."\n";
                
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
                    if(isset($this->isLiveTree))            $xml.= "<isLiveTree>".$this->isLiveTree."</isLiveTree>\n";
                    if(isset($this->createdTimeStamp))      $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                    if(isset($this->lastModifiedTimeStamp)) $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";

                    if($format!="summary")
                    {
                    
                        // Include tree notes if present
                        if ($this->treeNoteArray)
                        {
                            foreach($this->treeNoteArray as $value)
                            {
                                $myTreeNote = new treeNote();
                                $success = $myTreeNote->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$myTreeNote->asXML();
                                }
                                else
                                {
                                    $myMetaHeader->setErrorMessage($myTreeNote->getLastErrorCode, $myTreeNote->getLastErrorMessage);
                                }
                            }
                        }

                        // Include specimens if present
                        if (($this->specimenArray) && ($format=="standard"))
                        {
                            $xml.="<references>\n";
                            foreach($this->specimenArray as $value)
                            {
                                $mySpecimen = new specimen();
                                $success = $mySpecimen->setParamsFromDB($value);

                                if($success)
                                {
                                    $xml.=$mySpecimen->asXML("minimal", "all");
                                }
                                else
                                {
                                    $myMetaHeader->setErrorMessage($mySpecimen->getLastErrorCode, $mySpecimen->getLastErrorMessage);
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
                $xml.= "</tree>\n";
            }

            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tbltree")."'>";
        return $xml;
    }

    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
    }

    function getID()
    {
        return $this->id;
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
                    $sql = "insert into tbltree ( ";
                        if (isset($this->taxonID))                                  $sql.= "taxonid, ";
                        if (isset($this->subSiteID))                                $sql.= "subsiteid, ";
                        if (isset($this->name))                                     $sql.= "name, ";
                        if (isset($this->precision))                                $sql.= "precision, ";
                        if (isset($this->isLiveTree))                               $sql.= "islivetree, ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "location, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if (isset($this->taxonID))                                  $sql.= "'".$this->taxonID.   "', ";
                        if (isset($this->subSiteID))                                $sql.= "'".$this->subSiteID. "', ";
                        if (isset($this->name))                                     $sql.= "'".$this->name.     "', ";
                        if (isset($this->precision))                                $sql.= "'".$this->precision. "', ";
                        if (isset($this->isLiveTree))                               $sql.="'".fromPHPtoPGBool($this->isLiveTree)."', ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "setsrid(makepoint(".sprintf("%1.8f",$this->longitude).", ".sprintf("%1.8f",$this->latitude)."), 4326), ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tbltree where treeid=currval('tbltree_treeid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tbltree set ";
                        if (isset($this->taxonID))                                  $sql.= "taxonid='".    $this->taxonID    ."', ";
                        if (isset($this->subSiteID))                                $sql.= "subsiteid='".  $this->subSiteID  ."', ";
                        if (isset($this->name))                                     $sql.= "name='".      $this->name      ."', ";
                        if (isset($this->precision))                                $sql.= "precision='".  $this->precision  ."', ";
                        if((isset($this->latitude)) && (isset($this->longitude)))   $sql.= "location=setsrid(makepoint(".sprintf("%1.8f",$this->longitude).", ".sprintf("%1.8f",$this->latitude)."), 4326), ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql .= " where treeid=".$this->id;
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
                        $this->id=$row['treeid'];   
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

                $sql = "delete from tbltree where treeid=".$this->id;

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all specimens associated with a tree before deleting the tree itself.");
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
