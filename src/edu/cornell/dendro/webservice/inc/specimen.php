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
require_once('inc/radius.php');
require_once('inc/specimenType.php');
require_once('inc/terminalRing.php');
require_once('inc/specimenQuality.php');
require_once('inc/specimenContinuity.php');
require_once('inc/pith.php');

class specimen 
{
    var $id = NULL;
    var $name = NULL;
    var $treeID = NULL;
    var $dateCollected = NULL;
    var $specimenType = NULL;
    var $terminalRing = NULL;
    var $isTerminalRingVerified = NULL;
    var $sapwoodCount = NULL;
    var $sapwoodCountVerified = NULL;
    var $specimenQuality = NULL;
    var $specimenQualityVerified = NULL;
    var $specimenContinuity = NULL;
    var $specimenContinuityVerified = NULL;
    var $pith = NULL;
    var $pithVerified = NULL;
    var $isPithVerified = NULL;
    var $unmeasuredPre = NULL;
    var $isUnmeasuredPreVerified = NULL;
    var $unmeasuredPost = NULL;
    var $isUnmeasuredPostVerified = NULL;
    
    var $radiusArray = array();
    var $createdTimeStamp = NULL;
    var $lastModifiedTimeStamp = NULL;

    var $parentXMLTag = "specimens"; 
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
        // Set the current objects note.
        $this->name=$theName;
    }
    
    function setTreeID($theTreeID)
    {
        // Set the current objects note.
        $this->treeID=$theTreeID;
    }

    function setSpecimenType($specimenType)
    {
        // Set speciemn type id from name
        $mySpecimenType = new specimenType;
        $mySpecimenType->setParamsFromName($specimenType);
        $this->specimenType=$mySpecimenType->getName();
    }

    function setSpecimenTypeID($specimenTypeID)
    {
        // Set specimen type id from id
        $this->specimenTypeID=$specimenTypeID;
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

    function setSpecimenQuality($specimenQuality)
    {
        // Set specimen quality from name
        $this->specimenQuality=$specimenQuality;
    }

    function setSpecimenQualityID($specimenQualityID)
    {
        // Set specimen quality id from id
        $this->specimenQualityID=$specimenQualityID;
    }
    
    function setIsSpecimenQualityVerified($theBool)
    {
        $this->isSpecimenQualityVerified=$theBool;
    }

    function setSpecimenContinuity($specimenContinuity)
    {
        // Set specimen continuity from name
        $this->specimenContinuity=$specimenContinuity;
    }

    function setSpecimenContinuityID($specimenContinuityID)
    {
        // Set specimen continuity id from id
        $this->specimenContinuityID=$specimenContinuityID;
    }
    
    function setIsSpecimenContinuityVerified($theBool)
    {
        $this->isSpecimenContinuityVerified=$theBool;
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
    
    function setDateCollected($dateCollected)
    {
        $this->dateCollected = $dateCollected;
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
        $sql = "select * from tblspecimen where specimenid='$theID'";
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
                $this->id = $row['specimenid'];
                $this->dateCollected = $row['datecollected'];
                $this->specimenType = $row['specimentype'];
                $this->terminalRing = $row['terminalring'];
                $this->isTerminalRingVerified = fromPGtoPHPBool($row['isterminalringverified']);
                $this->sapwoodCount = $row['sapwoodcount'];
                $this->isSapwoodCountVerified = fromPGtoPHPBool($row['issapwoodcountverified']);
                $this->specimenQuality= $row['specimenquality'];
                $this->isSpecimenQualityVerified= fromPGtoPHPBool($row['isspecimenqualityverified']);
                $this->specimenContinuity = $row['specimencontinuity'];
                $this->isSpecimenContinuityVerified = fromPGtoPHPBool($row['isspecimencontinuityverified']);
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
        // SpecimenSpecimenNotes

        global $dbconn;

        $sql  = "select radiusid from tblradius where specimenid='".$this->id."'";
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
    
    function setParamsFromParamsClass($paramsClass)
    {
        // Alters the parameter values based upon values supplied by the user and passed as a parameters class
        if(isset($paramsClass->id))                            $this->id                               = $paramsClass->id;                      
        if(isset($paramsClass->treeID))                        $this->treeID                           = $paramsClass->treeID;                      
        if(isset($paramsClass->name))                          $this->name                             = $paramsClass->name;                      
        if(isset($paramsClass->dateCollected))                 $this->dateCollected                    = $paramsClass->dateCollected;            
        if(isset($paramsClass->specimenType))                  $this->specimenType                     = $paramsClass->specimenType;              
        if(isset($paramsClass->terminalRing))                  $this->terminalRing                     = $paramsClass->terminalRing;              
        if(isset($paramsClass->sapwoodCount))                  $this->sapwoodCount                     = $paramsClass->sapwoodCount;              
        if(isset($paramsClass->specimenQuality))               $this->specimenQuality                  = $paramsClass->specimenQuality;           
        if(isset($paramsClass->specimenContinuity))            $this->specimenContinuity               = $paramsClass->specimenContinuity;       
        if(isset($paramsClass->pith))                          $this->pith                             = $paramsClass->pith;          
        if(isset($paramsClass->unmeasuredPre))                 $this->unmeasuredPre                    = $paramsClass->unmeasuredPre;             
        if(isset($paramsClass->unmeasuredPost))                $this->unmeasuredPost                   = $paramsClass->unmeasuredPost;            
        if(isset($paramsClass->isTerminalRingVerified))        $this->isTerminalRingVerified           = $paramsClass->isTerminalRingVerified;   
        if(isset($paramsClass->isSapwoodCountVerified))        $this->isSapwoodCountVerified           = $paramsClass->isSapwoodCountVerified;    
        if(isset($paramsClass->isSpecimenQualityVerified))     $this->isSpecimenQualityVerified        = $paramsClass->isSpecimenQualityVerified; 
        if(isset($paramsClass->isSpecimenContinuityVerified))  $this->isSpecimenContinuityVerified     = $paramsClass->isSpecimenContinuityVerified;
        if(isset($paramsClass->isPithVerified))                $this->isPithVerified                   = $paramsClass->isPithVerified;            
        if(isset($paramsClass->isUnmeasuredPreVerified))       $this->isUnmeasuredPreVerified          = $paramsClass->isUnmeasuredPreVerified;  
        if(isset($paramsClass->isUnmeasuredPostVerified))      $this->isUnmeasuredPostVerified         = $paramsClass->isUnmeasuredPostVerified;  
        return true;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                if($paramsObj->id==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'id' field is required when reading a specimen.");
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
                    && ($paramsObj->dateCollected==NULL) 
                    && ($paramsObj->specimenType==NULL) 
                    && ($paramsObj->terminalRing==NULL) 
                    && ($paramsObj->sapwoodCount==NULL) 
                    && ($paramsObj->specimenQuality==NULL) 
                    && ($paramsObj->specimenContinuity==NULL) 
                    && ($paramsObj->pith==NULL) 
                    && ($paramsObj->unmeasuredPre==NULL) 
                    && ($paramsObj->unmeasuredPost==NULL) 
                    && ($paramsObj->isTerminalRingVerified==NULL) 
                    && ($paramsObj->isSapwoodCountVerified==NULL) 
                    && ($paramsObj->isSpecimenQualityVerified==NULL) 
                    && ($paramsObj->isSpecimenContinuityVerified==NULL) 
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
                        $this->setErrorMessage("902","Missing parameter - 'specimenid' field is required when creating a radius.");
                        return false;
                    }
                }
                else
                {
                    if($paramsObj->name == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'name' field is required when creating a specimen.");
                        return false;
                    }
                    if($paramsObj->treeID == NULL) 
                    {
                        $this->setErrorMessage("902","Missing parameter - 'treeid' field is required when creating a specimen.");
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

    function asXML($style="full")
    {
        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if (!isset($this->lastErrorCode))
        {
            // Only return XML when there are no errors.
    
            if($style=="full")
            {
                $xml.= "<specimen ";
                $xml.= "id=\"".$this->id."\" >";
                $xml.= getResourceLinkTag("specimen", $this->id)."\n ";
              
                if(isset($this->name))                         $xml.= "<name>".$this->name."</name>\n";
                if(isset($this->dateCollected))                 $xml.= "<dateCollected>".$this->dateCollected."</dateCollected>\n";
                if(isset($this->specimenType))                  $xml.= "<specimenType>".$this->specimenType."</specimenType>\n";
                if(isset($this->terminalRing))                  $xml.= "<terminalRing>".$this->terminalRing."</terminalRing>\n";
                if(isset($this->isTerminalRingVerified))        $xml.= "<isTerminalRingVerified>".fromPGtoStringBool($this->isTerminalRingVerified)."</isTerminalRingVerified>";
                if(isset($this->sapwoodCount))                  $xml.= "<sapwoodCount>".$this->sapwoodCount."</sapwoodCount>\n";
                if(isset($this->isSapwoodCountVerified))        $xml.= "<isSapwoodCountVerified>".fromPHPtoStringBool($this->isSapwoodCountVerified)."</isSapwoodCountVerified>";
                if(isset($this->specimenQuality))               $xml.= "<specimenQuality>".$this->specimenQuality."</specimenQuality>\n";
                if(isset($this->isSpecimenQualityVerified))     $xml.= "<isSpecimenQualityVerified>".fromPHPtoStringBool($this->isSpecimenQualityVerified)."</isSpecimenQualityVerified>\n";
                if(isset($this->specimenContinuity))            $xml.= "<specimenContinuity>".$this->specimenContinuity."</specimenContinuity>\n";
                if(isset($this->isSpecimenContinuityVerified))  $xml.= "<isSpecimenContinuityVerified>".fromPHPtoStringBool($this->isSpecimenContinuityVerified)."</isSpecimenContinuityVerified>\n";
                if(isset($this->pith))                          $xml.= "<pith>".$this->pith."</pith>\n";
                if(isset($this->isPithVerified))                $xml.= "<isPithVerified>".fromPHPtoStringBool($this->isPithVerified)."</isPithVerified>\n";
                if(isset($this->unmeasuredPre))                 $xml.= "<unmeasuredPre>".$this->unmeasuredPre."</unmeasuredPre>\n";
                if(isset($this->isUnmeasuredPreVerified))       $xml.= "<isUnmeasuredPreVerified>".fromPHPtoStringBool($this->isUnmeasuredPreVerified)."</isUnmeasuredPreVerified>\n";
                if(isset($this->unmeasuredPost))                $xml.= "<unmeasuredPost>".$this->unmeasuredPost."</unmeasuredPost>\n";
                if(isset($this->isUnmeasuredPostVerified))      $xml.= "<isUnmeasuredPostVerified>".fromPHPtoStringBool($this->isUnmeasuredPostVerified)."</isUnmeasuredPostVerified>\n";
                if(isset($this->createdTimeStamp))              $xml.= "<createdTimeStamp>".$this->createdTimeStamp."</createdTimeStamp>\n";
                if(isset($this->lastModifiedTimeStamp))         $xml.= "<lastModifiedTimeStamp>".$this->lastModifiedTimeStamp."</lastModifiedTimeStamp>\n";
                $xml.= "</specimen>\n";
            }
            elseif($style=="brief")
            {
                $xml.= "<specimen ";
                $xml.= "id=\"".$this->id."\" ";
                $xml.= "url=\"http://$domain/specimen/".$this->id."\" /> ";
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
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblspecimen")."'>";
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
                    $sql = "insert into tblspecimen ( ";
                        if(isset($this->name))                     $sql.="name, ";
                        if(isset($this->treeID))                    $sql.="treeid, ";
                        if(isset($this->dateCollected))             $sql.="datecollected, ";
                        if(isset($this->specimenType))              $sql.="specimentype, ";
                        if(isset($this->terminalRing))              $sql.="terminalring, ";
                        if(isset($this->isTerminalRingVerified))    $sql.="isterminalringverified, ";
                        if(isset($this->sapwoodCount))              $sql.="sapwoodcount, ";
                        if(isset($this->isSapwoodCountVerified))    $sql.="issapwoodcountverified, ";
                        if(isset($this->specimenQuality))           $sql.="specimenquality, ";
                        if(isset($this->isSpecimenQualityVerified)) $sql.="isspecimenqualityverified, ";
                        if(isset($this->specimenContinuity))        $sql.="specimencontinuity, ";
                        if(isset($this->pith))                      $sql.="pith, ";
                        if(isset($this->isPithVerified))            $sql.="ispithverified, ";
                        if(isset($this->unmeasuredPre))             $sql.="unmeaspre, ";
                        if(isset($this->isUnmeasuredPreVerified))   $sql.="isunmeaspreverified, ";
                        if(isset($this->unmeasuredPost))            $sql.="unmeaspost, ";
                        if(isset($this->isUnmeasuredPostVerified))  $sql.="isunmeaspostverified, ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=") values (";
                        if(isset($this->name))                     $sql.="'".$this->name                                          ."', ";
                        if(isset($this->treeID))                    $sql.="'".$this->treeID                                         ."', ";
                        if(isset($this->dateCollected))             $sql.="'".$this->dateCollected                                  ."', ";
                        if(isset($this->specimenType))              $sql.="'".$this->specimenType                                   ."', ";
                        if(isset($this->terminalRing))              $sql.="'".$this->terminalRing                                   ."', ";
                        if(isset($this->isTerminalRingVerified))    $sql.="'".fromPHPtoPGBool($this->isTerminalRingVerified)        ."', ";
                        if(isset($this->sapwoodCount))              $sql.="'".$this->sapwoodCount                                   ."', ";
                        if(isset($this->isSapwoodCountVerified))    $sql.="'".fromPHPtoPGBool($this->isSapwoodCountVerified)        ."', ";
                        if(isset($this->specimenQuality))           $sql.="'".$this->specimenQuality                                ."', ";
                        if(isset($this->isSpecimenQualityVerified)) $sql.="'".fromPHPtoPGBool($this->isSpecimenQualityVerified)     ."', ";
                        if(isset($this->specimenContinuity))        $sql.="'".$this->specimenContinuity                             ."', ";
                        if(isset($this->pith))                      $sql.="'".$this->pith                                           ."', ";
                        if(isset($this->isPithVerified))            $sql.="'".fromPHPtoPGBool($this->isPithVerified)                ."', ";
                        if(isset($this->unmeasuredPre))             $sql.="'".$this->unmeasuredPre                                  ."', ";
                        if(isset($this->isUnmeasuredPreVerified))   $sql.="'".fromPHPtoPGBool($this->isUnmeasuredPreVerified)       ."', ";
                        if(isset($this->unmeasuredPost))            $sql.="'".$this->unmeasuredPost                                 ."', ";
                        if(isset($this->isUnmeasuredPostVerified))  $sql.="'".fromPHPtoPGBool($this->isUnmeasuredPostVerified)      ."', ";
                    // Trim off trailing space and comma
                    $sql = substr($sql, 0, -2);
                    $sql.=")";
                    $sql2 = "select * from tblspecimen where specimenid=currval('tblspecimen_specimenid_seq')";
                }
                else
                {
                    // Updating DB
                    $sql.="update tblspecimen set ";
                        if(isset($this->name))                     $sql.="name='"                     .$this->name                                          ."', ";
                        if(isset($this->treeID))                    $sql.="treeID='"                    .$this->treeID                                         ."', ";
                        if(isset($this->dateCollected))             $sql.="datecollected='"             .$this->dateCollected                                  ."', ";
                        if(isset($this->specimenType))            $sql.="specimentype='"            .$this->specimenType                                 ."', ";
                        if(isset($this->terminalRing))            $sql.="terminalring='"            .$this->terminalRing                                 ."', ";
                        if(isset($this->isTerminalRingVerified))    $sql.="isterminalringverified='"    .fromPHPtoPGBool($this->isTerminalRingVerified)        ."', ";
                        if(isset($this->sapwoodCount))              $sql.="sapwoodcount='"              .$this->sapwoodCount                                   ."', ";
                        if(isset($this->isSapwoodCountVerified))    $sql.="issapwoodcountverified='"    .fromPHPtoPGBool($this->isSapwoodCountVerified)        ."', ";
                        if(isset($this->specimenQuality))         $sql.="specimenquality='"         .$this->specimenQuality                              ."', ";
                        if(isset($this->isSpecimenQualityVerified)) $sql.="isspecimenqualityverified='" .fromPHPtoPGBool($this->isSpecimenQualityVerified)     ."', ";
                        if(isset($this->specimenContinuity))      $sql.="specimencontinuity='"      .$this->specimenContinuity                           ."', ";
                        if(isset($this->pith))                    $sql.="pith='"                    .$this->pith                                         ."', ";
                        if(isset($this->isPithVerified))            $sql.="ispithverified='"            .fromPHPtoPGBool($this->isPithVerified)                ."', ";
                        if(isset($this->unmeasuredPre))             $sql.="unmeaspre='"                 .$this->unmeasuredPre                                  ."', ";
                        if(isset($this->isUnmeasuredPreVerified))   $sql.="isunmeaspreverified='"       .fromPHPtoPGBool($this->isUnmeasuredPreVerified)       ."', ";
                        if(isset($this->unmeasuredPost))            $sql.="unmeaspost='"                .$this->unmeasuredPost                                 ."', ";
                        if(isset($this->isUnmeasuredPostVerified))  $sql.="isunmeaspostverified='"      .fromPHPtoPGBool($this->isUnmeasuredPostVerified)      ."', ";
                    $sql = substr($sql, 0, -2);
                    $sql.= " where specimenid='".$this->id."'";
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
                        $this->id=$row['specimenid'];   
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

                $sql = "delete from tblspecimen where specimenid='".$this->id."'";

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
                                $this->setErrorMessage("907", "Foreign key violation.  You must delete all associated radii before deleting this specimen.");
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
