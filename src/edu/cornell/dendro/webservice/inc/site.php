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
require_once('inc/subSite.php');
require_once('inc/region.php');

class site 
{
    var $id = NULL;
    var $name = NULL;
    var $code = NULL;
    var $centroidLat = NULL;
    var $centroidLong = NULL;
    var $siteNoteArray = array();
    var $subSiteArray = array();
    var $regionArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;
    var $minLat = NULL;
    var $maxLat = NULL;
    var $minLong = NULL;
    var $maxLong = NULL;

    var $parentXMLTag = "sites"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

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

    function setName($theName)
    {
        $this->name=$theName;
    }
    
    function setCode($theCode)
    {
        $this->code=$theCode;
    }
    
    function setInitialCentroid($thelat, $thelong)
    {
        $this->centroidLat=$thelat;
        $this->centroidLong=$thelong;
    }

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $this->id=$theID;
        $sql = "select tblsite.*, x(centroid(expand(tblsite.siteextent, 0.1))) as centroidx, y(centroid(expand(tblsite.siteextent, 0.1))) as centroidy, xmin(tblsite.siteextent) as xmin, xmax(tblsite.siteextent) as xmax, ymin(tblsite.siteextent) as ymin, ymax(tblsite.siteextent) as ymax from tblsite where siteid=$theID";
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
                $this->name = $row['name'];
                $this->code = $row['code'];
                $this->createdTimeStamp = $row['createdtimestamp'];
                $this->lastModifiedTimeStamp = $row['lastmodifiedtimestamp'];
                $this->minLat = $row['ymin'];
                $this->maxLat = $row['ymax'];
                $this->minLong = $row['xmin'];
                $this->maxLong = $row['xmax'];
                $this->centroidLong = $row['centroidx'];
                $this->centroidLat  = $row['centroidy'];
                
            }

            $sql  = "select sitenoteid from tblsitesitenote where siteid=".$this->id;
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->siteNoteArray, $row['sitenoteid']);
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        $this->setChildParamsFromDB();

        return TRUE;
    }

    function setChildParamsFromDB()
    {
        // Add the id's of the current objects direct children from the database
        // SiteSiteNotes

        global $dbconn;

        $sql1 = "select subsiteid from tblsubsite where siteid=".$this->id;
        $sql2 = "select regionid from tblsiteregion where siteid=".$this->id;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql1);
            while ($row = pg_fetch_array($result))
            {
                // Get all subsites for this site and store 
                array_push($this->subSiteArray, $row['subsiteid']);
            }
            $this->subSiteArray = array_unique($this->subSiteArray);

            $result = pg_query($dbconn, $sql2);
            while ($row = pg_fetch_array($result))
            {
                // Get all regions for this site and store 
                array_push($this->regionArray, $row['regionid']);
            }
            $this->regionArray = array_unique($this->regionArray);
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
        if (isset($paramsClass->id))   $this->id   = $paramsClass->id;
        if (isset($paramsClass->name)) $this->name = $paramsClass->name;
        if (isset($paramsClass->code)) $this->code = $paramsClass->code;

        if (isset($paramsClass->siteNoteArray))
        {
            // Remove any existing site notes ready to be replaced with what user has specified
            unset($this->siteNoteArray);
            $this->siteNoteArray = array();

            if($paramsClass->siteNoteArray[0]!='empty')
            {
                foreach($paramsClass->siteNoteArray as $item)
                {
                    array_push($this->siteNoteArray, (int) $item[0]);
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
                if( (gettype($paramsObj->id)!="integer") && ($paramsObj->id!=NULL) ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer when reading sites.  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer when reading sites.");
                    return false;
                }
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a site.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when updating a site.");
                    return false;
                }
                if(($paramsObj->name == NULL) && ($paramsObj->code==NULL) && ($paramsObj->hasChild!=True)) 
                {
                    $this->setErrorMessage("902","Missing parameter - either 'name' or 'code' fields (or both) must be specified when updating a site.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when deleting a site.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->id == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'siteid' field is required when creating a subSite.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a site.");
                        return false;
                    }
                    if($paramsObj->code == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'code' field is required when creating a site.");
                        return false;
                    }
                }
                return true;

            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }
    
    function validateRequest($paramsObj, $crudMode, $auth)
    {
        $paramsOK = validateRequestParams($paramObj, $crudMode);
            
        if($paramsOK===FALSE)
        {
            // Problem with parameters
            return false;
        }
        
        // Parameters are ok, so now lets check the user has permission to do this
        if(isset($paramsObj->xmlrequest->subSite)) $parentID = $paramsObj->xmlrequest->subSite;
        
        switch($crudMode)
        {
            case "read":
         
            case "update":

            case "delete":

            case "create":

        }




    }

    /***********/
    /*ACCESSORS*/
    /***********/

    function asXML($mode="all")
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            if(($mode=="all") || ($mode=="begin"))
            {
                // Only return XML when there are no errors.
                $xml = "<site ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("site", $this->id)."\n ";
                $xml.= getResourceLinkTag("site", $this->id, "map")."\n ";
                $xml.= "<name>".escapeXMLChars($this->name)."</name>\n";
                $xml.= "<code>".escapeXMLChars($this->code)."</code>\n";
                $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";

                if( (isset($this->minLat)) && (isset($this->minLong)) && (isset($this->maxLat)) && (isset($this->maxLong)))
                {
                    $xml.= "<extent minLat=\"".$this->minLat."\" maxLat=\"".$this->maxLat."\" minLong=\"".$this->minLong."\" maxLong=\"".$this->maxLong."\" centroidLat=\"".$this->centroidLat."\" centroidLong=\"".$this->centroidLong."\" />";
                }
                
                // Include regions if present
                if ($this->regionArray)
                {
                    foreach($this->regionArray as $value)
                    {
                        $myRegion = new region();
                        $success = $myRegion->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$myRegion->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setErrorMessage($myRegion->getLastErrorCode, $myRegion->getLastErrorMessage);
                        }
                    }
                }

                // Include site notes if present
                if ($this->siteNoteArray)
                {
                    foreach($this->siteNoteArray as $value)
                    {
                        $mySiteNote = new siteNote();
                        $success = $mySiteNote->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$mySiteNote->asXML();
                        }
                        else
                        {
                            $this->setErrorMessage($mySiteNote->lastErrorCode, $mySiteNote->lastErrorMessage);
                        }
                    }
                }
                
                // Include subsites if present
                if ($this->subSiteArray)
                {
                    $xml.="<references>\n";
                    foreach($this->subSiteArray as $value)
                    {
                        $mySubSite = new subSite();
                        $success = $mySubSite->setParamsFromDB($value);

                        if($success)
                        {
                            $xml.=$mySubSite->asXML();
                        }
                        else
                        {
                            $this->setErrorMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
                        }
                    }
                    $xml.="</references>\n";
                }
            }

            if(($mode=="all") || ($mode=="end"))
            {
                // End XML tag
                $xml.= "</site>\n";
            }
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function asKML($mode="all")
    {
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            global $dbconn;

            $sql  = "select asKML(centroid(tblsite.siteextent)) as kml from tblsite where siteid=".$this->id." and tblsite.siteextent is not null";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $xml = "<Placemark>\n";
                    $xml.= "<name>".$this->name."</name>\n";
                    $xml.= "<visibility>1</visibility>\n";
                    $xml.= "<styleURL>#redLineRedPoly</styleURL>\n";
                    $xml.= $row['kml']."\n";
                    $xml.= "</Placemark>\n";
                    return $xml;
                }
            }
            else
            {
                // Connection bad
                $this->setErrorMessage("001", "Error connecting to database");
                return FALSE;
            }
        }
        else
        {
            return FALSE;
        }
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblsite")."'>";
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

        $sql  = NULL;
        $sql2 = NULL;

        //Only attempt to run SQL if there are no errors so far
        if($this->lastErrorCode == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->id == NULL)
                {
                    // New recordi

                    if (($this->centroidLat)&& ($this->centroidLong))
                    {
                        // Create WKT string for polygon if lat long known
                        $polygonwkt = "POLYGON((";
                        $polygonwkt.= ($this->centroidLong-0.1)." ".($this->centroidLat-0.1).", ";
                        $polygonwkt.= ($this->centroidLong+0.1)." ".($this->centroidLat-0.1).", ";
                        $polygonwkt.= ($this->centroidLong+0.1)." ".($this->centroidLat+0.1).", ";
                        $polygonwkt.= ($this->centroidLong-0.1)." ".($this->centroidLat+0.1).", ";
                        $polygonwkt.= ($this->centroidLong-0.1)." ".($this->centroidLat-0.1);
                        $polygonwkt.= "))";
                    }
                        // Build SQL insert statement
                        $sql = "insert into tblsite (";
                            if(isset($this->name))      $sql .= "name, ";
                            if(isset($this->code))      $sql .= "code, ";
                            if((isset($this->centroidLat)) && (isset($this->centroidLong)))    $sql .= "siteextent, ";
                        // Trim off trailing space and comma
                        $sql = substr($sql, 0, -2);
                        $sql.=") values (";
                            if(isset($this->name))      $sql .= "'".$this->name."', ";
                            if(isset($this->code))      $sql .= "'".$this->code."', ";
                            if((isset($this->centroidLat)) && (isset($this->centroidLong)))    $sql .= "polygonfromtext('$polygonwkt',4326))";
                        // Trim off trailing space and comma
                        $sql = substr($sql, 0, -2);
                        $sql.=")";
                        $sql2 = "select * from tblsite  where siteid=currval('tblsite_siteid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql .= "update tblsite set ";
                        if(isset($this->name))      $sql.="name='"          .$this->name."', ";
                        if(isset($this->code))      $sql.="code='"          .$this->code."', ";
                    $sql = substr($sql, 0, -2);
                    $sql.=" where siteid='".$this->id."'";
                }

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
                        case 23505:
                                // Foreign key violation
                                $this->setErrorMessage("908", "Unique constraint violation.  A site with this code already exists.");
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
                        $this->setParamsFromDB($row['siteid']);
                        //$this->id=$row['siteid'];   
                        //$this->createdTimeStamp=$row['createdtimestamp'];   
                        //$this->lastModifiedTimeStamp=$row['lastmodifiedtimestamp'];   
                    }
                }

                // Set or unset siteNotes for this site
                if(isset($this->siteNoteArray))
                {
                    $sql = "delete from tblsitesitenote where siteid=".$this->id;
                    $result = pg_query($dbconn, $sql);

                    foreach($this->siteNoteArray as $item)
                    {
                        $obj = new siteNote();
                        $obj->setParamsFromDB($item);
                        $obj->assignToParent($this->id);
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

                $sql = "delete from tblsite where siteid=".$this->id;

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated sub sites before deleting this site.");
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
