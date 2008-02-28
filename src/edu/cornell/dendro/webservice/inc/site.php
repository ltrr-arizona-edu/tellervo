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
require_once('inc/siteNote.php');
require_once('inc/subSite.php');

class site 
{
    var $id = NULL;
    var $name = NULL;
    var $code = NULL;
    var $latitude = NULL;
    var $longitude = NULL;
    var $siteNoteArray = array();
    var $subSiteArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;


    var $parentXMLTag = "sites"; 
    var $lastErrorMessage = NULL;
    var $lastErrorCode = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function site()
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
    
    function setInitialExtent($thelat, $thelong)
    {
        $this->latitude=$thelat;
        $this->longitude=$thelong;
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
        $sql = "select * from tblsite where siteid=$theID";
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
        // SiteSiteNotes

        global $dbconn;

        $sql  = "select sitenoteid from tblsitesitenote where siteid=".$this->id;
        $sql2 = "select subsiteid from tblsubsite where siteid=".$this->id;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->siteNoteArray, $row['sitenoteid']);
            }

            $result = pg_query($dbconn, $sql2);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->subSiteArray, $row['subsiteid']);
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
                $xml.= "id=\"".$this->id."\" ";
                $xml.= "url=\"http://$domain/site/".$this->id."\" >\n ";
                $xml.= "<name>".$this->name."</name>\n";
                $xml.= "<code>".$this->code."</code>\n";
                $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";

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
                            $myMetaHeader->setErrorMessage($mySiteNote->getLastErrorCode, $mySiteNote->getLastErrorMessage);
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
                            $myMetaHeader->setErrorMessage($mySubSite->getLastErrorCode, $mySubSite->getLastErrorMessage);
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
        
        // Check for required parameters
        if($this->name == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'name' field is required.");
            return FALSE;
        }
        if($this->code == NULL) 
        {
            $this->setErrorMessage("902", "Missing parameter - 'code' field is required.");
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
                    // New record
                    if (($this->latitude)&& ($this->longitude))
                    {
                        $polygonwkt = "POLYGON((";
                        $polygonwkt.= ($this->longitude-0.1)." ".($this->latitude-0.1).", ";
                        $polygonwkt.= ($this->longitude+0.1)." ".($this->latitude-0.1).", ";
                        $polygonwkt.= ($this->longitude+0.1)." ".($this->latitude+0.1).", ";
                        $polygonwkt.= ($this->longitude-0.1)." ".($this->latitude+0.1).", ";
                        $polygonwkt.= ($this->longitude-0.1)." ".($this->latitude-0.1);
                        $polygonwkt.= "))";
                        echo $polygonwkt;
                        $sql = "insert into tblsite (name, code, siteextent) values ('".$this->name."', '".$this->code."', polygonfromtext('$polygonwkt',4326))";
                    }
                    else
                    {
                        $sql = "insert into tblsite (name, code) values ('".$this->name."', '".$this->code."')";
                    }
                    $sql2 = "select * from tblsite  where siteid=currval('tblsite_siteid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblsite set name='".$this->name."', code='".$this->code."' where siteid=".$this->id;
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
                        $this->id=$row['siteid'];   
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
