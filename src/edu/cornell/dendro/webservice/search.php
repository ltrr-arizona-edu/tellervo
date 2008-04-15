<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
header('Content-Type: application/xhtml+xml; charset=utf-8');

require_once("inc/dbsetup.php");
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/errors.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/site.php");
require_once("inc/subSite.php");
require_once("inc/tree.php");
require_once("inc/specimen.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new searchRequest($myMetaHeader, $myAuth);

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}

// **************
// GET PARAMETERS
// **************
if(isset($_POST['xmlrequest']))
{
    // Extract parameters from XML request POST
    $myRequest->getXMLParams();
}
else
{
    // Extract parameters from get request and ensure no SQL has been injected
    $myRequest->getGetParams();
}

// ****************
// CHECK PARAMETERS 
// ****************
switch($myRequest->mode)
{
    case "search":
        $myMetaHeader = new meta("search");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->returnObject== NULL) trigger_error("902"."Missing parameter - 'returnObject' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }
    
    case "getcapabilities":
        $myMetaHeader = new meta("getcapabilities");
        if($myAuth->isLoggedIn())
        {
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "failed":
        $myMetaHeader->setRequestType("help");

    default:
        $myMetaHeader->setRequestType("help");
        // Output the resulting XML
        writeHelpOutput($myMetaHeader);
        die;
}

// *************
// PERFORM QUERY
// *************

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    if($myRequest->mode=='search')
    {
        $orderBySQL="";
        $groupBySQL="";
        $filterSQL="";
        $skipSQL="";
        $limitSQL="";
        $xmldata="";
        
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
        
            // Build return object dependent SQL
            $returnObjectSQL = tableName($myRequest->returnObject).".".variableName($myRequest->returnObject)."id as id ";
            $orderBySQL      = " order by ".tableName($myRequest->returnObject).".".variableName($myRequest->returnObject)."id asc ";
            $groupBySQL      = " group by ".tableName($myRequest->returnObject).".".variableName($myRequest->returnObject)."id" ;
            if ($myRequest->limit) $limitSQL = " limit ".$myRequest->limit;
            if ($myRequest->skip)  $skipSQL  = " offset ".$myRequest->skip;
            
            // Build "from..." section of SQL
            if( ((getLowestRelationshipLevel()<=1) && (getHighestRelationshipLevel()>=1))    || ($myRequest->returnObject == 'measurement'))  $fromTableSQL = tableName("measurement").", tblmeasurement, ";
            if( ((getLowestRelationshipLevel()<=2) && (getHighestRelationshipLevel()>=2))    || ($myRequest->returnObject == 'radius'))       $fromTableSQL = tableName("radius").", ";
            if( ((getLowestRelationshipLevel()<=3) && (getHighestRelationshipLevel()>=3))    || ($myRequest->returnObject == 'specimen'))     $fromTableSQL = tableName("specimen").", ";
            if( ((getLowestRelationshipLevel()<=4) && (getHighestRelationshipLevel()>=4))    || ($myRequest->returnObject == 'tree'))         $fromTableSQL = tableName("tree").", ";
            if( ((getLowestRelationshipLevel()<=5) && (getHighestRelationshipLevel()>=5))    || ($myRequest->returnObject == 'subsite'))      $fromTableSQL = tableName("subsite").", ";
            if( ((getLowestRelationshipLevel()<=6) && (getHighestRelationshipLevel()>=6))    || ($myRequest->returnObject == 'site'))         $fromTableSQL = tableName("site").", ";
                   
            // Trim off last ', ' from the 'form' clause SQL
            $fromTableSQL = substr($fromTableSQL, 0, -2);

            // Build "where..." section of SQL
            if ($myRequest->siteParamsArray)        $filterSQL .= paramsToFilterSQL($myRequest->siteParamsArray, "site");
            if ($myRequest->subsiteParamsArray)     $filterSQL .= paramsToFilterSQL($myRequest->subsiteParamsArray, "subsite");
            if ($myRequest->treeParamsArray)        $filterSQL .= paramsToFilterSQL($myRequest->treeParamsArray, "tree");
            if ($myRequest->specimenParamsArray)    $filterSQL .= paramsToFilterSQL($myRequest->specimenParamsArray, "specimen");
            if ($myRequest->radiusParamsArray)      $filterSQL .= paramsToFilterSQL($myRequest->radiusParamsArray, "radius");
            if ($myRequest->measurementParamsArray) $filterSQL .= paramsToFilterSQL($myRequest->measurementParamsArray, "measurement");
            $filterSQL .= getRelationshipSQL();

            // Trim off final ' and ' from filter SQL
            $filterSQL = substr($filterSQL, 0, -5);

            // Compile full SQL statement from parts
            $fullSQL = "select ".$returnObjectSQL." from ".$fromTableSQL." where ".$filterSQL.$groupBySQL.$orderBySQL.$limitSQL.$skipSQL;
            //echo $fullSQL;

            // Add SQL to XML output
            $xmldata .= "<sql>".$fullSQL."</sql>";

            // Do SQL Query
            $result = @pg_query($dbconn, $fullSQL);
            while ($row = @pg_fetch_array($result))
            {
                // Check user has permission to read then create a new object
                if( ($myRequest->returnObject=="site") && ($myAuth->sitePermission($row['id'], "read")) )
                {
                    $myReturnObject = new site();
                }
                elseif( ($myRequest->returnObject=="subsite") && ($myAuth->subSitePermission($row['id'], "read")) )
                {
                    $myReturnObject = new subSite();
                }
                elseif( ($myRequest->returnObject=="tree") && ($myAuth->treePermission($row['id'], "read")) )
                {
                    $myReturnObject = new tree();
                }
                elseif( ($myRequest->returnObject=="specimen") && ($myAuth->specimenPermission($row['id'], "read")) )
                {
                    $myReturnObject = new specimen();
                }
                elseif( ($myRequest->returnObject=="radius") && ($myAuth->radiusPermission($row['id'], "read")) )
                {
                    $myReturnObject = new radius();
                }
                elseif( ($myRequest->returnObject=="measurement") && ($myAuth->vmeasurementPermission($row['id'], "read")) )
                {
                    $myReturnObject = new measurement();
                }
                elseif( ($myRequest->returnObject) )
                {
                    $myMetaHeader->setMessage("XXX", "Invalid returnObject.  One of site, subsite, tree, specimen, radius or measurement must be specified", "Error");
                }
                else
                {
                    $myMetaHeader->setMessage("103", "Permission denied on ".$myRequest->returnObject."id ".$row['id'], "Warning");
                }

                if($myMetaHeader->status == "OK")
                {
                    // Set parameters on new object and return XML
                    $success = $myReturnObject->setParamsFromDB($row['id'], "brief");
                    if($success)
                    {
                        $xmldata.=$myReturnObject->asXML();
                    }
                    else
                    {
                        $myMetaHeader->setMessage($myReturnObject->getLastErrorCode(), $myReturnObject->getLastErrorMessage());
                    }
                }
            }
        }
    }
    elseif($myRequest->mode=='getcapabilities')
    {
            $myMetaHeader->setMessage("001", "Not yet implemented.  The search capability XML file we be returned here soon!");
        
    }
}


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
        $filterSQL .= tableName($paramName).".".$param['name']." ".$operator.$value." and ";
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
        return "vwvmeasurement";
        break;
    default:
        return false;
    }

}

function getLowestRelationshipLevel()
{
    // This function returns an interger representing the most junior level of relationship required in this query
    // tblsite         -- 6 -- most senior
    // tblsubsite      -- 5 --
    // tbltree         -- 4 --
    // tblspecimen     -- 3 --
    // tblradius       -- 2 --
    // tblmeasurement  -- 1 -- most junior
    
    global $myRequest;
    
    if (($myRequest->measurementParamsArray) || ($myRequest->returnObject == 'measurement'))
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

function getHighestRelationshipLevel()
{
    // This function returns an interger representing the most senior level of relationship required in this query
    // tblsite         -- 6 -- most senior
    // tblsubsite      -- 5 --
    // tbltree         -- 4 --
    // tblspecimen     -- 3 --
    // tblradius       -- 2 --
    // tblmeasurement  -- 1 -- most junior

    global $myRequest;

    if (($myRequest->siteParamsArray) || ($myRequest->returnObject == 'site'))
    {
        return 6;
    }
    elseif (($myRequest->subsiteParamsArray) || ($myRequest->returnObject == 'subsite'))
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
    else
    {
        return false;
    }
}

function getRelationshipSQL()
{
    // Returns the 'where' clause part of the query SQL for the table relationships 

    $lowestLevel  = getLowestRelationshipLevel();
    $highestLevel = getHighestRelationshipLevel();
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





// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata);
