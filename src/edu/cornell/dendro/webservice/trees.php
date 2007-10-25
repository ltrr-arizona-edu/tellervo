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

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/meta.php");
require_once("inc/tree.php");
require_once("inc/subSite.php");
require_once("inc/site.php");
require_once("inc/specimen.php");
require_once("inc/auth.php");

$myAuth = new auth();
// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
if(isset($_GET['label'])) $theLabel = addslashes($_GET['label']);
$theID = (int) $_GET['id'];
$theTaxonID = (int) $_GET['taxonid'];
$theSubSiteID = (int) $_GET['subsiteid'];
$theLatitude = (double) $_GET['lat'];
$theLongitude = (double) $_GET['long'];
$thePrecision = (int) $_GET['precision'];


// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        break;
    
    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isLoggedIn())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(($theTaxonID==NULL) && ($theSubSiteID==NULL) && ($theLatitude==NULL) && ($theLongitude==NULL) && ($thePrecision=NULL)) $myMetaHeader->setMessage("902", "Missing parameters - you haven't specified any parameters to update.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }

    case "delete":
        $myMetaHeader = new meta("delete");
        if($myAuth->isLoggedIn())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }


    case "create":
        $myMetaHeader = new meta("create");
        if($myAuth->isLoggedIn())
        {
            if($theLabel == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'label' field is required.");
            if($theTaxonID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'taxonid' field is required.");
            if($theSubSiteID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'subsiteid' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }

    default:
        $myMetaHeader = new meta("help");
        $myMetaHeader->setUser("Guest", "", "");
        // Output the resulting XML
        echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        echo "<corina>\n";
        echo $myMetaHeader->asXML();
        echo "<help> Details of how to use this web service will be added here later! </help>";
        echo "</corina>\n";
        die;
}

// Set user details
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname());
}
else
{
    $myMetaHeader->setUser("Guest", "Guest", "Guest");
}

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create tree object 
    $myTree = new tree();
    $parentTagBegin = $myTree->getParentTagBegin();
    $parentTagEnd = $myTree->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $myTree->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (isset($theLabel)) $myTree->setLabel($theLabel);
        if (!($theLatitude)==NULL) $myTree->setLatitude($theLatitude);
        if (!($theLongitude)==NULL) $myTree->setLongitude($theLongitude);
        if (!($thePrecision)==NULL) $myTree->setPrecision($thePrecision);
        if (!($theTaxonID)==NULL) $myTree->setTaxonID($theTaxonID);
        if (!($theSubSiteID)==NULL) $myTree->setSubSiteID($theSubSiteID);
        
        if( (($theMode=='update') && ($myAuth->treeUpdatePermission($theID)))  || 
            (($theMode=='create') && ($myAuth->treeCreatePermission($theID)))    )
        {
            // Check user has permission to update / create tree before writing object to database
            $success = $myTree->writeToDB();
            if($success)
            {
                $xmldata=$myTree->asXML();
            }
            else
            {
               $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
            }
        }  
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on treeid $theID");
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        if($myAuth->treeDeletePermission($theID))
        {
            // Check user has permission to delete record before performing statement
            $success = $myTree->deleteFromDB();
            if($success)
            {
                $xmldata=$myTree->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
            }
        }
    }

    if($theMode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if(!$theID==NULL)
            {
                $sql="select tbltree.*, tblsubsite.subsiteid, tblsubsite.siteid from tbltree, tblsubsite  where treeid=$theID and tbltree.subsiteid=tblsubsite.subsiteid order by tbltree.treeid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read tree
                    if($myAuth->treeReadPermission($row['treeid']))
                    {
                        $myTree = new tree();
                        $success = $myTree->setParamsFromDB($row['treeid']);
                        $success2 = $myTree->setChildParamsFromDB();
                        $mySubSite = new subSite();
                        $success3 = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $mySite = new site();
                        $success4 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 && success4 )
                        {
                            $xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML("begin");
                            $xmldata.= $myTree->asXML();
                            $xmldata.= $mySubSite->asXML("end");
                            $xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on treeid ".$row['treeid'], "Warning");
                    }
                }
            }
            else
            {
                $xmldata.= $parentTagBegin."\n";
                $sql="select * from tbltree order by treeid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read tree
                    if($myAuth->treeReadPermission($row['treeid']))
                    {
                        $myTree = new tree();
                        $success = $myTree->setParamsFromDB($row['treeid']);
                        $success2 = $myTree->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.=$myTree->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myTree->getLastErrorCode(), $myTree->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on treeid ".$row['treeid'], "Warning");
                    }
                }
                $xmldata.= $parentTagEnd."\n";
            }
        }
        else
        {
            // Connection bad
            $myMetaHeader->setMessage("001", "Error connecting to database");
        }
    }
}

// Output the resulting XML
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<data>\n";
echo $xmldata;
echo "</data>\n";
echo "</corina>";
