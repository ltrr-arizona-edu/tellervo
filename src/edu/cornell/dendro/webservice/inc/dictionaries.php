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

require_once("inc/note.php");
require_once("inc/sampleType.php");
require_once("inc/terminalRing.php");
require_once("inc/sampleQuality.php");
require_once("inc/sampleContinuity.php");
require_once("inc/datingType.php");
require_once("inc/pith.php");
require_once("inc/taxon.php");
require_once("inc/region.php");
require_once("inc/securityUser.php");

class dictionaries 
{
    var $parentXMLTag = "dictionaries"; 
    var $xmldata = NULL;
    var $lastErrorCode = NULL;
    var $lastErrorMessage = NULL;

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

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                return true;
            
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request for dictionaries");
                return false;
        }
    }

    function setParamsFromDB()
    {
        global $dbconn;
        
        $xmldata = "";

        $dictItems = array('siteNote', 'pith', 'sampleQuality', 'sampleType', 'terminalRing', 'region', 'sampleContinuity', 'elementNote', 'vmeasurementNote', 'readingNote', 'taxon', 'securityUser', 'datingType');
            
        // sample Type 
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            foreach($dictItems as $item)
            {
                if($item=="region")
                {
                    $sql="select distinct(tblsiteregion.regionid) as id, tblregion.regionname from tblsiteregion, tblregion where tblsiteregion.regionid=tblregion.regionid";
                }
                elseif($item=="securityUser")
                {
                    $sql = "select ".strtolower($item)."id as id from tbl".strtolower($item)." order by ".strtolower($item)."id"; 
                }
                else
                {
                    $sql = "select ".strtolower($item)."id as id from tlkp".strtolower($item)." order by ".strtolower($item)."id"; 
                }
                
                switch ($item)
                {
                    case "siteNote":
                        $myObj = new siteNote();
                        break;
                    case "pith":
                        $myObj = new pith();
                        break;
                    case "sampleQuality":
                        $myObj = new sampleQuality();
                        break;
                    case "sampleType":
                        $myObj = new sampleType();
                        break;
                    case "terminalRing":
                        $myObj = new terminalRing();
                        break;
                    case "region":
                        $myObj = new region();
                        break;
                    case "sampleContinuity":
                        $myObj = new sampleContinuity();
                        break;
                    case "elementNote":
                        $myObj = new elementNote();
                        break;
                    case "vmeasurementNote":
                        $myObj = new vmeasurementNote();
                        break;
                    case "readingNote":
                        $myObj = new readingNote();
                        break;
                    case "taxon":
                        $myObj = new taxon();
                        break;
                    case "securityUser":
                        $myObj = new securityUser();
                        break;
                    case "datingType":
                        $myObj = new datingType();
                        break;
                    default:
                        echo "not supported yet";
                        die();
                }
                
                $xmldata.=$myObj->getParentTagBegin();
                
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $success = $myObj->setParamsFromDB($row['id']);

                    if($success)
                    {
                        $xmldata.=$myObj->asXML();
                    }
                    else
                    {
                    //   trigger_error($mysampleType->getLastErrorCode().$mysampleType->getLastErrorMessage());
                    }
                }
                $xmldata.=$myObj->getParentTagEnd();
                unset($myDummyObj, $myObj);
            }
        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database");
        }

        // Put xmldata into class variable
        if($xmldata!=NULL)
        {
            $this->xmldata=$xmldata;
            return true;
        }
        else
        {
            return false;
        }
            
        
    }
    
    function setChildParamsFromDB()
    {
        return true;
    }

    /***********/
    /*ACCESSORS*/
    /***********/
    
    function asXML($mode="all")
    {
        if(isset($this->xmldata))
        {
            return $this->xmldata;
        }
        else
        {
            return false;
        }
    }

    function asKML($mode="all")
    {
    }

    function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblelement")."'>";
        return $xml;
    }

    function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
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

    function paramsToFilterSQL($paramsArray, $paramName)
    {
        $filterSQL="";
        foreach($paramsArray as $param)
        {
            // Set operator
            switch ($param['operator'])
            {
            case ">":
                $operator = ">";
                $value = " '".$param['value']."'";
                break;
            case "<":
                $operator = "<";
                $value = " '".$param['value']."'";
                break;
            case "=":
                $operator = "=";
                $value = " '".$param['value']."'";
                break;
            case "!=":
                $operator = "!=";
                $value = " '".$param['value']."'";
                break;
            case "like":
                $operator = "ilike";
                $value = " '%".$param['value']."%'";
                break;
            default :
                $operator = "=";
                $value = " '".$param['value']."'";
            }
            $filterSQL .= $this->tableName($paramName).".".$param['name']." ".$operator.$value." and ";
        }

        return $filterSQL;
    }

    function variableName($objectName)
    {

        switch($objectName)
        {
        case "site":
            return "site";
            break;
        case "subsite":
            return "subsite";
            break;
        case "element":
            return "element";
            break;
        case "sample":
            return "sample";
            break;
        case "radius":
            return "radius";
            break;
        case "measurement":
            return "vmeasurement";
            break;
        default:
            return false;
        }

    }

    function tableName($objectName)
    {

        switch($objectName)
        {
        case "site":
            return "vwtblsite";
            break;
        case "subsite":
            return "vwtblsubsite";
            break;
        case "element":
            return "vwtblelement";
            break;
        case "sample":
            return "vwtblsample";
            break;
        case "radius":
            return "vwtblradius";
            break;
        case "measurement":
            return "vwvmeasurement";
            break;
        default:
            return false;
        }

    }

    function getLowestRelationshipLevel($theRequest)
    {
        // This function returns an interger representing the most junior level of relationship required in this query
        // tblsite         -- 6 -- most senior
        // tblsubsite      -- 5 --
        // tblelement         -- 4 --
        // tblsample     -- 3 --
        // tblradius       -- 2 --
        // tblmeasurement  -- 1 -- most junior
        
        $myRequest = $theRequest;
        
        if (($myRequest->measurementParamsArray) || ($myRequest->returnObject == 'measurement'))
        {
            return 1;
        }
        elseif (($myRequest->radiusParamsArray) || ($myRequest->returnObject == 'radius'))
        {
            return 2;
        }
        elseif (($myRequest->sampleParamsArray) || ($myRequest->returnObject == 'sample'))
        {
            return 3;
        }
        elseif (($myRequest->elementParamsArray) || ($myRequest->returnObject == 'element'))
        {
            return 4;
        }
        elseif (($myRequest->subsiteParamsArray) || ($myRequest->returnObject == 'subsite'))
        {
            return 5;
        }
        if (($myRequest->siteParamsArray) || ($myRequest->returnObject == 'site'))
        {
            return 6;
        }
        else
        {
            return false;
        }
    }

    function getHighestRelationshipLevel($theRequest)
    {
        // This function returns an interger representing the most senior level of relationship required in this query
        // tblsite         -- 6 -- most senior
        // tblsubsite      -- 5 --
        // tblelement         -- 4 --
        // tblsample     -- 3 --
        // tblradius       -- 2 --
        // tblmeasurement  -- 1 -- most junior

        $myRequest = $theRequest;

        if (($myRequest->siteParamsArray) || ($myRequest->returnObject == 'site'))
        {
            return 6;
        }
        elseif (($myRequest->subsiteParamsArray) || ($myRequest->returnObject == 'subsite'))
        {
            return 5;
        }
        elseif (($myRequest->elementParamsArray) || ($myRequest->returnObject == 'element'))
        {
            return 4;
        }
        elseif (($myRequest->sampleParamsArray) || ($myRequest->returnObject == 'sample'))
        {
            return 3;
        }
        elseif (($myRequest->radiusParamsArray) || ($myRequest->returnObject == 'radius'))
        {
            return 2;
        }
        elseif (($myRequest->measurementParamsArray) || ($myRequest->returnObject == 'measurement'))
        {
            return 1;
        }
        else
        {
            return false;
        }
    }

    function getRelationshipSQL($theRequest)
    {
        // Returns the 'where' clause part of the query SQL for the table relationships 

        $myRequest = $theRequest;
        $lowestLevel  = $this->getLowestRelationshipLevel($myRequest);
        $highestLevel = $this->getHighestRelationshipLevel($myRequest);
        $sql ="";

        //echo "high = $highestLevel\n";
        //echo "low = $lowestLevel\n";

        if (($lowestLevel==1) && ($highestLevel>=1))
        {
            $sql .= "vwvmeasurement.measurementid=tblmeasurement.measurementid and ";
        }
        if (($lowestLevel<=1) && ($highestLevel>1))
        {
            $sql .= "vwvmeasurement.radiusid=vwtblradius.radiusid and ";
        }
        if (($lowestLevel<=2) && ($highestLevel>2))
        {
            $sql .= "vwtblradius.sampleid=vwtblsample.sampleid and ";
        }
        if (($lowestLevel<=3) && ($highestLevel>3))
        {
            $sql .= "vwtblsample.elementid=vwtblelement.elementid and ";
        }
        if (($lowestLevel<=4) && ($highestLevel>4))
        {
            $sql .= "vwtblelement.subsiteid=vwtblsubsite.subsiteid and ";
        }
        if (($lowestLevel<=5) && ($highestLevel>5))
        {
            $sql .= "vwtblsubsite.siteid=vwtblsite.siteid and ";
        }

        return $sql;
    }



// End of Class
} 
?>
