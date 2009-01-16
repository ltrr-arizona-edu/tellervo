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

class search 
{
    var $id = NULL;
    var $xmldata = NULL;
    var $lastErrorCode = NULL;
    var $lastErrorMessage = NULL;
    var $sqlcommand = NULL;
    var $deniedRecArray = array();

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

    function validateRequestParams($paramsObj)
    {
    	global $myRequest;
    	
        switch($myRequest->getCrudMode())
        {
            case "search":
                if($paramsObj->returnObject==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'returnObject' field is required when performing a search");
                    return false;
                }
                if(($paramsObj->allData===TRUE) && ($paramsObj->returnObject=='measurement'))
                {
                    $this->setErrorMessage("901","Invalid user parameters - you cannot request all measurements as it is computationally too expensive");
                    return false;
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
    function doSearch($paramsClass, $auth, $includePermissions, $format)
    {

        global $dbconn;
        $myAuth       = $auth;
        $myRequest    = $paramsClass;
        $orderBySQL   = NULL;
        $groupBySQL   = NULL;
        $filterSQL    = NULL;
        $skipSQL      = NULL;
        $limitSQL     = NULL;
        $xmldata      = NULL;

        // Overide return object to vmeasurement if measurement requested
        if($myRequest->returnObject=='measurement')
        {
            $myRequest->returnObject='vmeasurement';
        }

        // Build return object dependent SQL
        $returnObjectSQL = $this->tableName($myRequest->returnObject).".".$this->variableName($myRequest->returnObject)."id as id ";
        $orderBySQL      = "\n ORDER BY ".$this->tableName($myRequest->returnObject).".".$this->variableName($myRequest->returnObject)."id asc ";
        $groupBySQL      = "\n GROUP BY ".$this->tableName($myRequest->returnObject).".".$this->variableName($myRequest->returnObject)."id" ;
        if ($myRequest->limit) $limitSQL = "\n LIMIT ".$myRequest->limit;
        if ($myRequest->skip)  $skipSQL  = "\n OFFSET ".$myRequest->skip;

        if($myRequest->allData===FALSE)
        {
            // User doing a normal search (not all records) so build filter
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                // Build filter SQL
                if ($myRequest->siteParamsArray)         $filterSQL .= $this->paramsToFilterSQL($myRequest->siteParamsArray, "site");
                if ($myRequest->subSiteParamsArray)      $filterSQL .= $this->paramsToFilterSQL($myRequest->subSiteParamsArray, "subsite");
                if ($myRequest->treeParamsArray)         $filterSQL .= $this->paramsToFilterSQL($myRequest->treeParamsArray, "tree");
                if ($myRequest->specimenParamsArray)     $filterSQL .= $this->paramsToFilterSQL($myRequest->specimenParamsArray, "specimen");
                if ($myRequest->radiusParamsArray)       $filterSQL .= $this->paramsToFilterSQL($myRequest->radiusParamsArray, "radius");
                if ($myRequest->vmeasurementParamsArray) $filterSQL .= $this->paramsToFilterSQL($myRequest->vmeasurementParamsArray, "vmeasurement");
                if ($myRequest->measurementParamsArray)  $filterSQL .= $this->paramsToFilterSQL($myRequest->measurementParamsArray, "measurement");
                if ($myRequest->vmeasurementMetaCacheParamsArray) $filterSQL .= $this->paramsToFilterSQL($myRequest->vmeasurementMetaCacheParamsArray, "vmeasurementmetacache");
                // Trim off final ' and ' from filter SQL
                $filterSQL = substr($filterSQL, 0, -5);
            }
        }

        // Compile full SQL statement from parts
        if(isset($filterSQL))
        {
            $fullSQL = "SELECT ".$returnObjectSQL.$this->fromSQL($myRequest)." WHERE ".$filterSQL.$groupBySQL.$orderBySQL.$limitSQL.$skipSQL;
        }
        else
        {
            $fullSQL = "SELECT ".$returnObjectSQL.$this->fromSQL($myRequest).$groupBySQL.$orderBySQL.$limitSQL.$skipSQL;
        }

        // Add SQL to XML output
        $this->sqlcommand .= $fullSQL;

        // Do SQL Query
        pg_send_query($dbconn, $fullSQL);
        $result = pg_get_result($dbconn);
            /*echo "rows = ".pg_num_rows($result);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903","No records matched the criteria specified. SQL statement was: $fullSQL");
            }
            else
            {*/

            $result = @pg_query($dbconn, $fullSQL);
            while ($row = @pg_fetch_array($result))
            {
                // Check user has permission to read then create a new object
                if($myRequest->returnObject=="site") 
                {
                    $myReturnObject = new site();
                    $hasPermission = $myAuth->getPermission("read", "site", $row['id']);
                }
                elseif($myRequest->returnObject=="subsite")
                {
                    $myReturnObject = new subSite();
                    $hasPermission = $myAuth->getPermission("read", "subSite", $row['id']);
                }
                elseif($myRequest->returnObject=="tree") 
                {
                    $myReturnObject = new tree();
                    $hasPermission = $myAuth->getPermission("read", "tree", $row['id']);
                }
                elseif($myRequest->returnObject=="specimen")
                {
                    $myReturnObject = new specimen();
                    $hasPermission = $myAuth->getPermission("read", "specimen", $row['id']);
                }
                elseif($myRequest->returnObject=="radius") 
                {
                    $myReturnObject = new radius();
                    $hasPermission = $myAuth->getPermission("read", "radius", $row['id']);
                }
                elseif($myRequest->returnObject=="vmeasurement")
                {
                    $myReturnObject = new measurement();
                    $hasPermission = $myAuth->getPermission("read", "measurement", $row['id']);
                }
                else
                {
                    $this->setErrorMessage("901","Invalid return object ".$myRequest->returnObject." specified.  Must be one of site, subsite, tree, specimen, radius or measurement");
                }

                if($hasPermission===FALSE)
                {
                    array_push($this->deniedRecArray, $row['id']); 
                    continue;
                }

                // Set parameters on new object and return XML
                $success = $myReturnObject->setParamsFromDB($row['id'], $format);

                // Get permissions if requested
                if($includePermissions===TRUE) $myReturnObject->getPermissions($myAuth->getID());

                //$success = $myReturnObject->setParamsFromDB($row['id'], "brief");
                if($success)
                {
                    $xmldata.=$myReturnObject->asXML($format, "all");

                }
                else
                {
                    $this->setErrorMessage($myReturnObject->getLastErrorCode(), $myReturnObject->getLastErrorMessage());
                }
            }
        // }

            if(count($this->deniedRecArray)>0 )
            {
                $errMessage = "Permission denied on the following ".$myRequest->returnObject."id(s): ";
                foreach ($this->deniedRecArray as $id)
                {
                    $errMessage .= $id.", ";
                }
                $errMessage = substr($errMessage, 0, -2).".";
                $this->setErrorMessage("103", $errMessage);
            }

            // Put xmldata into class variable
            if($xmldata!=NULL)
            {
                $this->xmldata=$xmldata;
            }

            if($this->lastErrorCode==NULL)
            {
                return true;
            } 
            else
            {
                return false;
            }
    }

    function asXML($format='standard', $mode="all")
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
    }

    function getParentTagEnd()
    {
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
        $filterSQL = NULL;
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
        case "tree":
            return "tree";
            break;
        case "specimen":
            return "specimen";
            break;
        case "radius":
            return "radius";
            break;
        case "vmeasurement":
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
        case "tree":
            return "vwtbltree";
            break;
        case "specimen":
            return "vwtblspecimen";
            break;
        case "radius":
            return "vwtblradius";
            break;
        case "measurement":
            return "vwtblmeasurement";
            break;
        case "vmeasurement":
            return "vwtblvmeasurement";
            break;
        case "vmeasurementmetacache":
            return "vwtblvmeasurementmetacache";
            break;
//        case "vmeasurementresult":
//            return "vwtblvmeasurementresult";
//            break;
        case "vmeasurementderivedcache":
            return "tblvmeasurementderivedcache";
            break;
        default:
            return false;
        }

    }
    
    function fromSQL($myRequest)
    {
        $fromSQL = "\nFROM ";
        $withinJoin = FALSE;

        if( (($this->getLowestRelationshipLevel($myRequest)<=6) && ($this->getHighestRelationshipLevel($myRequest)>=6)) || ($myRequest->returnObject == 'site'))
        {
            $fromSQL .= $this->tableName("site")." \n";
            $withinJoin = TRUE;
        }
        
        if( (($this->getLowestRelationshipLevel($myRequest)<=5) && ($this->getHighestRelationshipLevel($myRequest)>=5)) || ($myRequest->returnObject == 'subsite'))  
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("subsite")." ON ".$this->tableName("site").".siteid = ".$this->tableName("subsite").".siteid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("subsite")." \n";
                $withinJoin = TRUE;
            }
        }
        
        if( (($this->getLowestRelationshipLevel($myRequest)<=4) && ($this->getHighestRelationshipLevel($myRequest)>=4)) || ($myRequest->returnObject == 'tree'))
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("tree")." ON ".$this->tableName("subsite").".subsiteid = ".$this->tableName("tree").".subsiteid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("tree")." \n";
                $withinJoin = TRUE;
            }
        }        
        
        if( (($this->getLowestRelationshipLevel($myRequest)<=3) && ($this->getHighestRelationshipLevel($myRequest)>=3)) || ($myRequest->returnObject == 'specimen'))
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("specimen")." ON ".$this->tableName("tree").".treeid = ".$this->tableName("specimen").".treeid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("specimen")." \n";
                $withinJoin = TRUE;
            }
        }
        
        if( (($this->getLowestRelationshipLevel($myRequest)<=2) && ($this->getHighestRelationshipLevel($myRequest)>=2)) || ($myRequest->returnObject == 'radius'))
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("radius")." ON ".$this->tableName("specimen").".specimenid = ".$this->tableName("radius").".specimenid \n";
            }
            else
            {
                $fromSQL .= $this->tableName("radius")." \n";
                $withinJoin = TRUE;
            }
        }
        
        if( (($this->getLowestRelationshipLevel($myRequest)<=1) && ($this->getHighestRelationshipLevel($myRequest)>=1)) || ($myRequest->returnObject == 'measurement'))  
        {
            if($withinJoin)
            {
                $fromSQL .= "INNER JOIN ".$this->tableName("measurement")." ON ".$this->tableName("radius").".radiusid = ".$this->tableName("measurement").".radiusid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementderivedcache")." ON ".$this->tableName("measurement").".measurementid = ".$this->tableName("vmeasurementderivedcache").".measurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurement")." ON ".$this->tableName("vmeasurementderivedcache").".vmeasurementid = ".$this->tableName("vmeasurement").".vmeasurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementmetacache")." ON ".$this->tableName("vmeasurement").".vmeasurementid = ".$this->tableName("vmeasurementmetacache").".vmeasurementid \n";
            }
            else
            {
                $fromSQL .= $this->tableName('measurement')." \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementderivedcache")." ON ".$this->tableName("measurement").".measurementid = ".$this->tableName("vmeasurementderivedcache").".measurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurement")." ON ".$this->tableName("vmeasurementderivedcache").".vmeasurementid = ".$this->tableName("vmeasurement").".vmeasurementid \n";
                $fromSQL .= "INNER JOIN ".$this->tableName("vmeasurementmetacache")." ON ".$this->tableName("vmeasurement").".vmeasurementid = ".$this->tableName("vmeasurementmetacache").".vmeasurementid \n";
            }
        }
               
        return $fromSQL;
    }

    function getLowestRelationshipLevel($theRequest)
    {
        // This function returns an interger representing the most junior level of relationship required in this query
        // tblsite         -- 6 -- most senior
        // tblsubsite      -- 5 --
        // tbltree         -- 4 --
        // tblspecimen     -- 3 --
        // tblradius       -- 2 --
        // tblmeasurement  -- 1 -- most junior
        
        $myRequest = $theRequest;
        
        if (($myRequest->measurementParamsArray) || ($myRequest->returnObject == 'vmeasurement'))
        {
            return 1;
        }
        elseif (($myRequest->radiusParamsArray) || ($myRequest->returnObject == 'radius'))
        {
            return 2;
        }
        elseif (($myRequest->specimenParamsArray) || ($myRequest->returnObject == 'specimen'))
        {
            return 3;
        }
        elseif (($myRequest->treeParamsArray) || ($myRequest->returnObject == 'tree'))
        {
            return 4;
        }
        elseif (($myRequest->subSiteParamsArray) || ($myRequest->returnObject == 'subsite'))
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
        // tbltree         -- 4 --
        // tblspecimen     -- 3 --
        // tblradius       -- 2 --
        // tblmeasurement  -- 1 -- most junior

        $myRequest = $theRequest;

        if (($myRequest->siteParamsArray) || ($myRequest->returnObject == 'site'))
        {
            return 6;
        }
        elseif (($myRequest->subSiteParamsArray) || ($myRequest->returnObject == 'subsite'))
        {
            return 5;
        }
        elseif (($myRequest->treeParamsArray) || ($myRequest->returnObject == 'tree'))
        {
            return 4;
        }
        elseif (($myRequest->specimenParamsArray) || ($myRequest->returnObject == 'specimen'))
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
        elseif (($myRequest->vmeasurementParamsArray) || ($myRequest->returnObject == 'measurement'))
        {
            return 1;
        }
        elseif (($myRequest->vmeasurementMetaCacheParamsArray) || ($myRequest->returnObject == 'measurement'))
        {
            return 1;
        }
        elseif (($myRequest->derivedCacheParamsArray) || ($myRequest->returnObject == 'measurement'))
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
            $sql .= "tblmeasurement.measurementid=tblvmeasurementderivedcache.measurementid and tblvmeasurementderivedcache.vmeasurementid=tblvmeasurement.vmeasurementid and tblvmeasurementmetacache.vmeasurementid=tblvmeasurement.vmeasurementid and ";
        }
        if (($lowestLevel<=1) && ($highestLevel>1))
        {
            $sql .= "tblmeasurement.radiusid=vwtblradius.radiusid and ";
        }
        if (($lowestLevel<=2) && ($highestLevel>2))
        {
            $sql .= "vwtblradius.specimenid=vwtblspecimen.specimenid and ";
        }
        if (($lowestLevel<=3) && ($highestLevel>3))
        {
            $sql .= "vwtblspecimen.treeid=vwtbltree.treeid and ";
        }
        if (($lowestLevel<=4) && ($highestLevel>4))
        {
            $sql .= "vwtbltree.subsiteid=vwtblsubsite.subsiteid and ";
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
