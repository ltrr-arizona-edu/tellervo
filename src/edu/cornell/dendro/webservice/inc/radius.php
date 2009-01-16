<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('inc/dbEntity.php');

class radius extends radiusEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {

    }

    /***********/
    /* SETTERS */
    /***********/

    /**
     * Set the current objects parameters from the database
     *
     * @param Integer $theID
     * @return Boolean
     */
    function setParamsFromDB($theID)
    {
        global $dbconn;
        
        $this->setID($theID);
        $sql = "select * from tblradius where radiusid=".$this->getID();
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
                $this->setName($row['name']);
                $this->setSampleID($row['sampleid']);
                $this->setCreatedTimestamp($row['createdtimestamp']);
                $this->setLastModifiedTimestamp($row['lastmodifiedtimestamp']);
                $this->setPithPresent(fromPGtoPHPBool($row['pithpresent']));
                $this->setSapwood($row['sapwood']);
                $this->setBarkPresent(fromPGtoPHPBool($row['barkpresent']));
                $this->setNumberOfSapwoodRings($row['numberofsapwoodrings']);
                $this->setLastRingUnderBark($row['lastringunderbark']);
                $this->setMissingHeartwoodRingsToPith($row['missingheartwoodringstopith']);
                $this->setMissingHeartwoodRingsToPithFoundation($row['missingheartwoodringstopithfoundation']);
                $this->setMissingSapwoodRingsToBark($row['missingsapwoodringstobark']);
                $this->setMissingSapwoodRingsToBarkFoundation($row['missingsapwoodringstobarkfoundation']);  
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
        // RadiusRadiusNotes

        global $dbconn;

        $sql  = "select measurementid from tblmeasurement where radiusid=".$this->getID();
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {

            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->measurementArray, $row['measurementid']);
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
    	//$paramsClass = new RadiusParameters;
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if ($paramsClass->getName()!=NULL)       $this->setName($paramsClass->getName());
        if ($paramsClass->getSampleID()!=NULL)   $this->setSampleID($paramsClass->getSampleID());
        

        return true;
    }

    function validateRequestParams($paramsObj)
    {
        // Check parameters based on crudMode 
    	global $myRequest;
    	
        switch($myRequest->getCrudMode())
        {
            case "read":
                if($paramsObj->getID()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a radius.");
                    return false;
                }
                if(!($paramsObj->getID()>0) && !($paramsObj->getID()==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->getID()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                if(($paramsObj->getSampleID()==NULL) 
                    && ($paramsObj->getName()==NULL)
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->getID() == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->getID() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'radiusid' field is required when creating a measurement.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->getName() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a radius.");
                        return false;
                    }
                    if($paramsObj->getSampleID() == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'sampleid' field is required when creating a radius.");
                        return false;
                    }
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

    
    function asXML($format='standard', $parts='all')
    {
        switch($format)
        {
        case "comprehensive":
            require_once('site.php');
            require_once('subSite.php');
            require_once('tree.php');
            require_once('sample.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid, tbltree.treeid, tblsample.sampleid, tblradius.radiusid
                FROM tblsubsite 
                INNER JOIN tbltree ON tblsubsite.subsiteid=tbltree.subsiteid
                INNER JOIN tblsample ON tbltree.treeid=tblsample.treeid
                INNER JOIN tblradius ON tblsample.sampleid=tblradius.sampleid
                where tblradius.radiusid='".$this->getID()."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for radius id=".$this->getID());
                    return FALSE;
                }
                else
                {
                    $row = pg_fetch_array($result);

                    $mySite = new site();
                    $mySubSite = new subSite();
                    $myTree = new tree();
                    $mysample = new sample();

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
                    
                    $success = $myTree->setParamsFromDB($row['treeid']);
                    if($success===FALSE)
                    {
                        trigger_error($myTree->getLastErrorCode().$myTree->getLastErrorMessage());
                    }
                    
                    $success = $mysample->setParamsFromDB($row['sampleid']);
                    if($success===FALSE)
                    {
                        trigger_error($mysample->getLastErrorCode().$mysample->getLastErrorMessage());
                    }

                    $xml = $mySite->asXML("summary", "beginning");
                    $xml.= $mySubSite->asXML("summary", "beginning");
                    $xml.= $myTree->asXML("summary", "beginning");
                    $xml.= $mysample->asXML("summary", "beginning");
                    $xml.= $this->_asXML("standard", "all");
                    $xml.= $mysample->asXML("summary", "end");
                    $xml.= $myTree->asXML("summary", "end");
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
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary' or 'comprehensive'");
            return false;
        }
    }


    private function _asXML($format, $parts)
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()!=NULL)
        {
            if( ($parts=="all") || ($parts=="beginning") )
            {
                // Only return XML when there are no errors.
                $xml.= "<radius ";
                $xml.= "id=\"".$this->getID()."\" >";
                $xml.= getResourceLinkTag("radius", $this->getID())."\n ";
                
                // Include permissions details if requested
                if($this->getIncludePermissions()===TRUE) 
                {
                    $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->getPermission("Create"))."\" ";
                    $xml.= "canUpdate=\"".fromPHPtoStringBool($this->getPermission("Update"))."\" ";
                    $xml.= "canDelete=\"".fromPHPtoStringBool($this->getPermission("Delete"))."\" />\n";
                } 
                
                $xml.= "<name>".escapeXMLChars($this->getName())."</name>\n";

                if($format!="minimal")
                {
                    $xml.= "<createdTimeStamp>".$this->getCreatedTimestamp()."</createdTimeStamp>\n";
                    $xml.= "<lastModifiedTimeStamp>".$this->getLastModifiedTimestamp()."</lastModifiedTimeStamp>\n";
                }
            }

            if( ($parts=="all") || ($parts=="end"))
            {
                $xml.= "</radius>\n";
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

    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        $sql = NULL;
        $sql2 = NULL;
        
        //Only attempt to run SQL if there are no errors so far
        if($this->getLastErrorCode() == NULL)
        {
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // If ID has not been set then we assume that we are writing a new record to the DB.  Otherwise updating.
                if($this->getID() == NULL)
                {
                    // New record
                    $sql = "insert into tblradius (name, sampleid) values ('".$this->getName()."', '".$this->getSampleID()."')";
                    $sql2 = "select * from tblradius where radiusid=currval('tblradius_radiusid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql = "update tblradius set name='".$this->getName()."', sampleid='".$this->getSampleID()."' where radiusid=".$this->getID();
                    $sql = "update tblradius set ";
                    if ($this->getName()!=NULL) $sql.="name='".$this->getName()."', ";
                    if ($this->getSampleID()!=NULL) $sql.="sampleid='".$this->getSampleID().", ";
                    $sql = substr($sql, 0, -2);
                    $sql.= " where radiusid=".$this->getID();
                }

                // Run SQL command
                if ($sql)
                {
                    // Run SQL 
                    pg_send_query($dbconn, $sql);
                    $result = pg_get_result($dbconn);
                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
                    {
                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
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
                        $this->setID($row['radiusid']);   
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

                $sql = "delete from tblradius where radiusid=".$this->getID();

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated measurements before deleting this radius.");
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
