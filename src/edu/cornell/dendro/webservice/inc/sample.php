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
require_once('inc/radius.php');
require_once('inc/sampleType.php');
require_once('inc/terminalRing.php');
require_once('inc/sampleQuality.php');
require_once('inc/sampleContinuity.php');
require_once('inc/pith.php');
require_once('inc/dbEntity.php');

class sample extends dbEntity
{
	/**
	 * Method that was used to take a sample from the element
	 *
	 * @var unknown_type
	 */
    var $sampleType = NULL; 
    
    /**
     * Year of dendrochronological sampling
     *
     * @var Date
     */
    var $sampingDate = NULL;
    	
    /**
     * Array of filenames associated with this sample
     *
     * @var Array
     */
    var $fileArray = array();
	
    /**
     * Position of sample in element
     *
     * @var String
     */
    var $position = NULL;
	
    
    /**
     * State this material is in (dry, wet, burned etc)
     *
     * @var String
     */
    var $state = NULL;
    
    /**
     * Presence of knots
     *
     * @var Boolean
     */
    var $knots = NULL;
    
    /**
     * More information about the sample
     *
     * @var String
     */
    var $description = NULL;
    
    var $name = NULL;
    var $treeID = NULL;
    var $terminalRing = NULL;
    var $isTerminalRingVerified = NULL;
    var $sapwoodCount = NULL;
    var $sapwoodCountVerified = NULL;
    var $sampleQuality = NULL;
    var $sampleQualityVerified = NULL;
    var $sampleContinuity = NULL;
    var $sampleContinuityVerified = NULL;
    var $pith = NULL;
    var $pithVerified = NULL;
    var $isPithVerified = NULL;
    var $unmeasuredPre = NULL;
    var $isUnmeasuredPreVerified = NULL;
    var $unmeasuredPost = NULL;
    var $isUnmeasuredPostVerified = NULL;
    
    
    var $includePermissions = FALSE;
    var $canCreate = NULL;
    var $canUpdate = NULL;
    var $canDelete = NULL;
    
    var $radiusArray = array();




    /***************/
    /* CONSTRUCTOR */
    /***************/

    public function __construct($parentXMLTag)
    {
    	parent::__construct($parentXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setName($theName)
    {
        // Set the current objects note.
        $this->name=$theName;
    }
    
    function setTreeID($theTreeID)
    {
        // Set the current objects note.
        $this->treeID=$theTreeID;
    }

    function setsampleType($sampleType)
    {
        // Set speciemn type id from name
        $mysampleType = new sampleType;
        $mysampleType->setParamsFromName($sampleType);
        $this->sampleType=$mysampleType->getName();
    }

    function setsampleTypeID($sampleTypeID)
    {
        // Set sample type id from id
        $this->sampleTypeID=$sampleTypeID;
    }

    function setTerminalRing($terminalRing)
    {
        // Set terminal ring  from name
        $this->terminalRing=$terminalRing;
    }

    function setTerminalRingID($terminalRingID)
    {
        // Set terminal ring id from id
        $this->terminalRingID=$terminalRingID;
    }

    function setIsTerminalRingVerified($theBool)
    {
        $this->isTerminalRingVerified=$theBool;
    }

    function setsampleQuality($sampleQuality)
    {
        // Set sample quality from name
        $this->sampleQuality=$sampleQuality;
    }

    function setsampleQualityID($sampleQualityID)
    {
        // Set sample quality id from id
        $this->sampleQualityID=$sampleQualityID;
    }
    
    function setIssampleQualityVerified($theBool)
    {
        $this->issampleQualityVerified=$theBool;
    }

    function setsampleContinuity($sampleContinuity)
    {
        // Set sample continuity from name
        $this->sampleContinuity=$sampleContinuity;
    }

    function setsampleContinuityID($sampleContinuityID)
    {
        // Set sample continuity id from id
        $this->sampleContinuityID=$sampleContinuityID;
    }
    
    function setIssampleContinuityVerified($theBool)
    {
        $this->issampleContinuityVerified=$theBool;
    }

    function setPith($pith)
    {
        // Set pith id from name
        $this->pith=$pith;
    }

    function setPithID($pithID)
    {
        // Set pith id from id
        $this->pithID=$pithID;
    }
    
    function setIsPithVerified($theBool)
    {
        // Set pith id from id
        $this->isPithVerified=$theBool;
    }
    
    function setSamplingDate($samplingDate)
    {
        $this->samplingDate = $samplingDate;
    }
    
    function setSapwoodCount($sapwoodCount)
    {
        $this->sapwoodCount = $sapwoodCount;
    }
    
    function setIsSapwoodCountVerified($theBool)
    {
        $this->isSapwoodCountVerified = $theBool;
    }
    
    function setUnmeasPre($theValue)
    {
        $this->unmeasuredPre = $theValue;
    }
    
    function setIsUnmeasPreVerified($theBool)
    {
        $this->isUnmeasuredPreVerified = $theBool;
    }
    
    function setUnmeasPost($theValue)
    {
        $this->unmeasuredPost = $theValue;
    }
    
    function setIsUnmeasPostVerified($theBool)
    {
        $this->isUnmeasuredPostVerified = $theBool;
    }
    
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
                $this->samplingDate = $row['datecollected'];
                $this->sampleType = $row['sampletype'];
                $this->terminalRing = $row['terminalring'];
                $this->isTerminalRingVerified = fromPGtoPHPBool($row['isterminalringverified']);
                $this->sapwoodCount = $row['sapwoodcount'];
                $this->isSapwoodCountVerified = fromPGtoPHPBool($row['issapwoodcountverified']);
                $this->sampleQuality= $row['samplequality'];
                $this->issampleQualityVerified= fromPGtoPHPBool($row['issamplequalityverified']);
                $this->sampleContinuity = $row['samplecontinuity'];
                $this->issampleContinuityVerified = fromPGtoPHPBool($row['issamplecontinuityverified']);
                $this->pith = $row['pith'];
                $this->isPithVerified = fromPGtoPHPBool($row['ispithverified']);
                $this->unmeasuredPre = $row['unmeaspre'];
                $this->isUnmeasuredPreVerified = fromPGtoPHPBool($row['isunmeaspreverified']);
                $this->unmeasuredPost = $row['unmeaspost'];
                $this->isUnmeasuredPostVerified = fromPGtoPHPBool($row['isunmeaspostverified']);
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
        if(isset($paramsClass->samplingDate))                 $this->samplingDate                    = $paramsClass->samplingDate;            
        if(isset($paramsClass->sampleType))                  $this->sampleType                     = $paramsClass->sampleType;              
        if(isset($paramsClass->terminalRing))                  $this->terminalRing                     = $paramsClass->terminalRing;              
        if(isset($paramsClass->sapwoodCount))                  $this->sapwoodCount                     = $paramsClass->sapwoodCount;              
        if(isset($paramsClass->sampleQuality))               $this->sampleQuality                  = $paramsClass->sampleQuality;           
        if(isset($paramsClass->sampleContinuity))            $this->sampleContinuity               = $paramsClass->sampleContinuity;       
        if(isset($paramsClass->pith))                          $this->pith                             = $paramsClass->pith;          
        if(isset($paramsClass->unmeasuredPre))                 $this->unmeasuredPre                    = $paramsClass->unmeasuredPre;             
        if(isset($paramsClass->unmeasuredPost))                $this->unmeasuredPost                   = $paramsClass->unmeasuredPost;            
        if(isset($paramsClass->isTerminalRingVerified))        $this->isTerminalRingVerified           = $paramsClass->isTerminalRingVerified;   
        if(isset($paramsClass->isSapwoodCountVerified))        $this->isSapwoodCountVerified           = $paramsClass->isSapwoodCountVerified;    
        if(isset($paramsClass->issampleQualityVerified))     $this->issampleQualityVerified        = $paramsClass->issampleQualityVerified; 
        if(isset($paramsClass->issampleContinuityVerified))  $this->issampleContinuityVerified     = $paramsClass->issampleContinuityVerified;
        if(isset($paramsClass->isPithVerified))                $this->isPithVerified                   = $paramsClass->isPithVerified;            
        if(isset($paramsClass->isUnmeasuredPreVerified))       $this->isUnmeasuredPreVerified          = $paramsClass->isUnmeasuredPreVerified;  
        if(isset($paramsClass->isUnmeasuredPostVerified))      $this->isUnmeasuredPostVerified         = $paramsClass->isUnmeasuredPostVerified;  
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
    protected function validateRequestParams($paramsObj, $crudMode)
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
                    && ($paramsObj->terminalRing==NULL) 
                    && ($paramsObj->sapwoodCount==NULL) 
                    && ($paramsObj->sampleQuality==NULL) 
                    && ($paramsObj->sampleContinuity==NULL) 
                    && ($paramsObj->pith==NULL) 
                    && ($paramsObj->unmeasuredPre==NULL) 
                    && ($paramsObj->unmeasuredPost==NULL) 
                    && ($paramsObj->isTerminalRingVerified==NULL) 
                    && ($paramsObj->isSapwoodCountVerified==NULL) 
                    && ($paramsObj->issampleQualityVerified==NULL) 
                    && ($paramsObj->issampleContinuityVerified==NULL) 
                    && ($paramsObj->isPithVerified==NULL) 
                    && ($paramsObj->isUnmeasuredPreVerified==NULL) 
                    && ($paramsObj->isUnmeasuredPostVerified==NULL)
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

    /**
     * Retrieve the relevant permissions for this class from the database 
     *
     * @param Integer $securityUserID
     * @return Boolean
     */
    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, 'sample', $this->id)";
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
	 * Get the XML representation of this class
	 * 
	 * @param String $format one of standard, comprehensive, summary, or minimal. Defaults to 'standard'
	 * @param String $parts one of all, beginning or end. Defaults to 'all'
	 * @return String
	 */
    protected function asXML($format='standard', $parts='all')
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
                $xml.= "<sample ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("sample", $this->id)."\n ";
                
                // Include permissions details if requested
                if($this->includePermissions===TRUE) 
                {
                    $xml.= "<permissions canCreate=\"".fromPHPtoStringBool($this->canCreate)."\" ";
                    $xml.= "canUpdate=\"".fromPHPtoStringBool($this->canUpdate)."\" ";
                    $xml.= "canDelete=\"".fromPHPtoStringBool($this->canDelete)."\" />\n";
                } 
              
                if(isset($this->name))                          $xml.= "<name>".escapeXMLChars($this->name)."</name>\n";
                
                if($format!="minimal")
                {
                    if(isset($this->samplingDate))                 $xml.= "<samplingDate>".$this->samplingDate."</samplingDate>\n";
                    if(isset($this->sampleType))                  $xml.= "<sampleType>".$this->sampleType."</sampleType>\n";
                    if(isset($this->terminalRing))                  $xml.= "<terminalRing>".$this->terminalRing."</terminalRing>\n";
                    if(isset($this->isTerminalRingVerified))        $xml.= "<isTerminalRingVerified>".fromPGtoStringBool($this->isTerminalRingVerified)."</isTerminalRingVerified>";
                    if(isset($this->sapwoodCount))                  $xml.= "<sapwoodCount>".$this->sapwoodCount."</sapwoodCount>\n";
                    if(isset($this->isSapwoodCountVerified))        $xml.= "<isSapwoodCountVerified>".fromPHPtoStringBool($this->isSapwoodCountVerified)."</isSapwoodCountVerified>";
                    if(isset($this->sampleQuality))               $xml.= "<sampleQuality>".$this->sampleQuality."</sampleQuality>\n";
                    if(isset($this->issampleQualityVerified))     $xml.= "<issampleQualityVerified>".fromPHPtoStringBool($this->issampleQualityVerified)."</issampleQualityVerified>\n";
                    if(isset($this->sampleContinuity))            $xml.= "<sampleContinuity>".$this->sampleContinuity."</sampleContinuity>\n";
                    if(isset($this->issampleContinuityVerified))  $xml.= "<issampleContinuityVerified>".fromPHPtoStringBool($this->issampleContinuityVerified)."</issampleContinuityVerified>\n";
                    if(isset($this->pith))                          $xml.= "<pith>".$this->pith."</pith>\n";
                    if(isset($this->isPithVerified))                $xml.= "<isPithVerified>".fromPHPtoStringBool($this->isPithVerified)."</isPithVerified>\n";
                    if(isset($this->unmeasuredPre))                 $xml.= "<unmeasuredPre>".$this->unmeasuredPre."</unmeasuredPre>\n";
                    if(isset($this->isUnmeasuredPreVerified))       $xml.= "<isUnmeasuredPreVerified>".fromPHPtoStringBool($this->isUnmeasuredPreVerified)."</isUnmeasuredPreVerified>\n";
                    if(isset($this->unmeasuredPost))                $xml.= "<unmeasuredPost>".$this->unmeasuredPost."</unmeasuredPost>\n";
                    if(isset($this->isUnmeasuredPostVerified))      $xml.= "<isUnmeasuredPostVerified>".fromPHPtoStringBool($this->isUnmeasuredPostVerified)."</isUnmeasuredPostVerified>\n";
                    if(isset($this->createdTimeStamp))              $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                    if(isset($this->lastModifiedTimeStamp))         $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";
                }
            }

            if (($parts=="all") || ($parts=="end"))
            {
                $xml.= "</sample>\n";
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
    protected function writeToDB()
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
                        if(isset($this->name))                          $sql.="name, ";
                        if(isset($this->treeID))                        $sql.="treeid, ";
                        if(isset($this->samplingDate))                 $sql.="samplingDate, ";
                        if(isset($this->sampleType))                  $sql.="sampletype, ";
                        if(isset($this->terminalRing))                  $sql.="terminalring, ";
                        if(isset($this->isTerminalRingVerified))        $sql.="isterminalringverified, ";
                        if(isset($this->sapwoodCount))                  $sql.="sapwoodcount, ";
                        if(isset($this->isSapwoodCountVerified))        $sql.="issapwoodcountverified, ";
                        if(isset($this->sampleQuality))               $sql.="samplequality, ";
                        if(isset($this->issampleQualityVerified))     $sql.="issamplequalityverified, ";
                        if(isset($this->sampleContinuity))            $sql.="samplecontinuity, ";
                        if(isset($this->issampleContinuityVerified))  $sql.="issamplecontinuityverified, ";
                        if(isset($this->pith))                          $sql.="pith, ";
                        if(isset($this->isPithVerified))                $sql.="ispithverified, ";
                        if(isset($this->unmeasuredPre))                 $sql.="unmeaspre, ";
                        if(isset($this->isUnmeasuredPreVerified))       $sql.="isunmeaspreverified, ";
                        if(isset($this->unmeasuredPost))                $sql.="unmeaspost, ";
                        if(isset($this->isUnmeasuredPostVerified))      $sql.="isunmeaspostverified, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if(isset($this->name))                          $sql.="'".$this->name                                           ."', ";
                        if(isset($this->treeID))                        $sql.="'".$this->treeID                                         ."', ";
                        if(isset($this->samplingDate))                 $sql.="'".$this->samplingDate                                  ."', ";
                        if(isset($this->sampleType))                  $sql.="'".$this->sampleType                                   ."', ";
                        if(isset($this->terminalRing))                  $sql.="'".$this->terminalRing                                   ."', ";
                        if(isset($this->isTerminalRingVerified))        $sql.="'".fromPHPtoPGBool($this->isTerminalRingVerified)        ."', ";
                        if(isset($this->sapwoodCount))                  $sql.="'".$this->sapwoodCount                                   ."', ";
                        if(isset($this->isSapwoodCountVerified))        $sql.="'".fromPHPtoPGBool($this->isSapwoodCountVerified)        ."', ";
                        if(isset($this->sampleQuality))               $sql.="'".$this->sampleQuality                                ."', ";
                        if(isset($this->issampleQualityVerified))     $sql.="'".fromPHPtoPGBool($this->issampleQualityVerified)     ."', ";
                        if(isset($this->sampleContinuity))            $sql.="'".$this->sampleContinuity                             ."', ";
                        if(isset($this->issampleContinuityVerified))  $sql.="'".fromPHPtoPGBool($this->issampleContinuityVerified)  ."', ";
                        if(isset($this->pith))                          $sql.="'".$this->pith                                           ."', ";
                        if(isset($this->isPithVerified))                $sql.="'".fromPHPtoPGBool($this->isPithVerified)                ."', ";
                        if(isset($this->unmeasuredPre))                 $sql.="'".$this->unmeasuredPre                                  ."', ";
                        if(isset($this->isUnmeasuredPreVerified))       $sql.="'".fromPHPtoPGBool($this->isUnmeasuredPreVerified)       ."', ";
                        if(isset($this->unmeasuredPost))                $sql.="'".$this->unmeasuredPost                                 ."', ";
                        if(isset($this->isUnmeasuredPostVerified))      $sql.="'".fromPHPtoPGBool($this->isUnmeasuredPostVerified)      ."', ";
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
                        if(isset($this->terminalRing))                  $sql.="terminalring='"                  .$this->terminalRing                                    ."', ";
                        if(isset($this->isTerminalRingVerified))        $sql.="isterminalringverified='"        .fromPHPtoPGBool($this->isTerminalRingVerified)         ."', ";
                        if(isset($this->sapwoodCount))                  $sql.="sapwoodcount='"                  .$this->sapwoodCount                                    ."', ";
                        if(isset($this->isSapwoodCountVerified))        $sql.="issapwoodcountverified='"        .fromPHPtoPGBool($this->isSapwoodCountVerified)         ."', ";
                        if(isset($this->sampleQuality))               $sql.="samplequality='"               .$this->sampleQuality                                 ."', ";
                        if(isset($this->issampleQualityVerified))     $sql.="issamplequalityverified='"     .fromPHPtoPGBool($this->issampleQualityVerified)      ."', ";
                        if(isset($this->sampleContinuity))            $sql.="samplecontinuity='"            .$this->sampleContinuity                              ."', ";
                        if(isset($this->issampleContinuityVerified))  $sql.="issamplecontinuityverified='"  .fromPHPtoPGBool($this->issampleContinuityVerified)   ."', ";
                        if(isset($this->pith))                          $sql.="pith='"                          .$this->pith                                            ."', ";
                        if(isset($this->isPithVerified))                $sql.="ispithverified='"                .fromPHPtoPGBool($this->isPithVerified)                 ."', ";
                        if(isset($this->unmeasuredPre))                 $sql.="unmeaspre='"                     .$this->unmeasuredPre                                   ."', ";
                        if(isset($this->isUnmeasuredPreVerified))       $sql.="isunmeaspreverified='"           .fromPHPtoPGBool($this->isUnmeasuredPreVerified)        ."', ";
                        if(isset($this->unmeasuredPost))                $sql.="unmeaspost='"                    .$this->unmeasuredPost                                  ."', ";
                        if(isset($this->isUnmeasuredPostVerified))      $sql.="isunmeaspostverified='"          .fromPHPtoPGBool($this->isUnmeasuredPostVerified)       ."', ";
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
