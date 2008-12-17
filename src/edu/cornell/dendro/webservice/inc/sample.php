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
require_once('inc/radius.php');
require_once('inc/sampleType.php');
require_once('inc/dbEntity.php');

class sample extends sampleEntity implements IDBAccessor
{
    var $radiusArray = array();
    
    /***************/
    /* CONSTRUCTOR */
    /***************/

    public function __construct()
    {
        $groupXMLTag = "samples";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/
    
    function setParamsFromDB($theID)
    {
        // Set the current objects parameters from the database

        global $dbconn;
        
        $this->id=$theID;
        $sql = "select * from tblsample where sampleid='$theID'";
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
                $this->id = $row['sampleid'];
                $this->samplingDate = $row['samplingdate'];
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
        // samplesampleNotes

        global $dbconn;

        $sql  = "select radiusid from tblradius where sampleid='".$this->id."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
			
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                array_push($this->radiusArray, $row['radiusid']);
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
     * Set the parameters of this class based upon the Parameters Class that has been passed
     *
     * @param Parameters Class $paramsClass
     * @return Boolean
     */
    private function setParamsFromParamsClass($paramsClass)
    {
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if(isset($paramsClass->id))                            $this->id                               = $paramsClass->id;                      
        if(isset($paramsClass->treeID))                        $this->treeID                           = $paramsClass->treeID;                      
        if(isset($paramsClass->name))                          $this->name                             = $paramsClass->name;                      
        if(isset($paramsClass->samplingDate))                  $this->samplingDate                     = $paramsClass->samplingDate;            
        if(isset($paramsClass->sampleType))                    $this->sampleType                       = $paramsClass->sampleType;              
        return true;
    }

    
    /*************/
    /* FUNCTIONS */
    /*************/    
    
    /**
     * Validate the parameters passed based on the CRUD mode
     *
     * @param Params Class $paramsObj
     * @param String $crudMode one of create, read, update or delete.
     * @return unknown
     */
    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a sample.");
                    return false;
                }
                if( (gettype($paramsObj->id)!="integer") && ($paramsObj->id!=NULL) ) 
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be an integer.  It is currently a ".gettype($paramsObj->id));
                    return false;
                }
                if(!($paramsObj->id>0) && !($paramsObj->id==NULL))
                {
                    $this->setErrorMessage("901","Invalid parameter - 'id' field must be a valid positive integer.");
                    return false;
                }
                return true;
         
            case "update":
                if($paramsObj->id == NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                if(($paramsObj->name==NULL) 
                    && ($paramsObj->samplingDate==NULL) 
                    && ($paramsObj->sampleType==NULL) 
                    && ($paramsObj->hasChild!=True))
                {
                    $this->setErrorMessage("902","Missing parameters - you haven't specified any parameters to update.");
                    return false;
                }
                return true;

            case "delete":
                if($paramsObj->id == NULL) 
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required.");
                    return false;
                }
                return true;

            case "create":
                if($paramsObj->hasChild===TRUE)
                {
                    if($paramsObj->id == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'sampleid' field is required when creating a radius.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a sample.");
                        return false;
                    }
                    if($paramsObj->treeID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'treeid' field is required when creating a sample.");
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

	/**
	 * Get the XML representation of this class
	 * 
	 * @param String $format one of standard, comprehensive, summary, or minimal. Defaults to 'standard'
	 * @param String $parts one of all, beginning or end. Defaults to 'all'
	 * @return String
	 */
    function asXML($format='standard', $parts='all')
    {
        switch($format)
        {
        case "comprehensive":
            require_once('site.php');
            require_once('subSite.php');
            require_once('tree.php');
            global $dbconn;
            $xml = NULL;

            $sql = "SELECT tblsubsite.siteid, tblsubsite.subsiteid, tbltree.treeid, tblsample.sampleid
                FROM tblsubsite 
                INNER JOIN tbltree ON tblsubsite.subsiteid=tbltree.subsiteid
                INNER JOIN tblsample ON tbltree.treeid=tblsample.treeid
                where tblsample.sampleid='".$this->id."'";

            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                pg_send_query($dbconn, $sql);
                $result = pg_get_result($dbconn); 

                if(pg_num_rows($result)==0)
                {
                    // No records match the id specified
                    $this->setErrorMessage("903", "No match for sample id=".$this->id);
                    return FALSE;
                }
                else
                {
                    $row = pg_fetch_array($result);

                    $mySite = new site();
                    $mySubSite = new subSite();
                    $myTree = new tree();

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

                    $xml = $mySite->asXML("summary", "beginning");
                    $xml.= $mySubSite->asXML("summary", "beginning");
                    $xml.= $myTree->asXML("summary", "beginning");
                    $xml.= $this->_asXML("standard", "all");
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

	/**
	 * Internal function for getting the XML representation of this class.
	 * You should almost certainly be used asXML() instead.
	 *
	 * @param String $format one of standard, comprehensive, summary, or minimal. Defaults to 'standard'
	 * @param String $parts one of all, beginning or end. Defaults to 'all'
	 * @return String
	 */
    private function _asXML($format, $parts)
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
    
            if( ($parts=="all") || ($parts=="beginning"))
            {
                $xml.= "<tridas:sample>\n";
                $xml.= "<tridas:identifier domain=\"$domain\">".$this->id."</tridas:identifier>";

                //$xml.= getResourceLinkTag("sample", $this->id)."\n ";
                
                // Include permissions details if requested
                if($this->includePermissions===TRUE) 
                {
                    $xml.= "<tridas:genericField name=\"canCreate\" type=\"boolean\">".fromPHPtoStringBool($this->canCreate)."</tridas:genericField ";
                    $xml.= "<tridas:genericField name=\"canUpdate\" type=\"boolean\">".fromPHPtoStringBool($this->canUpdate)."</tridas:genericField ";
                    $xml.= "<tridas:genericField name=\"canDelete\" type=\"boolean\">".fromPHPtoStringBool($this->canDelete)."</tridas:genericField ";
                } 
              
                if(isset($this->name))                        $xml.= "<tridas:genericField name=\"name\">".escapeXMLChars($this->name)."</tridas:genericField>\n";
                
                if($format!="minimal")
                {
                    if(isset($this->samplingDate))            $xml.= "<samplingDate\">".$this->samplingDate."</samplingDate>\n";
                    if(isset($this->sampleType))              $xml.= "<tridas:genericField name=\"sampleType\">".$this->sampleType."</tridas:genericField>\n";
                    if(isset($this->createdTimeStamp))        $xml.= "<tridas:genericField name=\"createdTimeStamp\">".$this->createdTimeStamp."</tridas:genericField>\n";
                    if(isset($this->lastModifiedTimeStamp))   $xml.= "<tridas:genericField name=\"lastModifiedTimeStamp\">".$this->lastModifiedTimeStamp."</tridas:genericField>\n";
                }
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</tridas:sample>\n";
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
     * Write this class representation to the database  
     *
     * @return Boolean
     */
    function writeToDB()
    {
        // Write the current object to the database

        global $dbconn;
        $sql = NULL;
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
                    // New record
                    $sql = "insert into tblsample ( ";
                        if(isset($this->name))                        $sql.="name, ";
                        if(isset($this->treeID))                      $sql.="treeid, ";
                        if(isset($this->samplingDate))                $sql.="samplingDate, ";
                        if(isset($this->sampleType))                  $sql.="sampletype, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if(isset($this->name))                          $sql.="'".$this->name                                           ."', ";
                        if(isset($this->treeID))                        $sql.="'".$this->treeID                                         ."', ";
                        if(isset($this->samplingDate))                 $sql.="'".$this->samplingDate                                  ."', ";
                        if(isset($this->sampleType))                  $sql.="'".$this->sampleType                                   ."', ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblsample where sampleid=currval('tblsample_sampleid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql.="update tblsample set ";
                        if(isset($this->name))                          $sql.="name='"                          .$this->name                                            ."', ";
                        if(isset($this->treeID))                        $sql.="treeID='"                        .$this->treeID                                          ."', ";
                        if(isset($this->samplingDate))                 $sql.="samplingDate='"                 .$this->samplingDate                                   ."', ";
                        if(isset($this->sampleType))                  $sql.="sampletype='"                  .$this->sampleType                                    ."', ";
                    $sql = substr($sql, 0, -2);
                    $sql.= " where sampleid='".$this->id."'";
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
                        case 23514:
                                // Foreign key violation
                                $this->setErrorMessage("909", "Check constraint violation.  Sapwood count must be a positive integer.");
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
                        $this->id=$row['sampleid'];   
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
     * Delete this class representation from the database
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

                $sql = "delete from tblsample where sampleid='".$this->id."'";

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated radii before deleting this sample.");
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
