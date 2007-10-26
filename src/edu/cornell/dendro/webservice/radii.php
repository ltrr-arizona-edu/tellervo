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
require_once("inc/radius.php");
require_once("inc/subSite.php");
require_once("inc/site.php");
require_once("inc/specimen.php");
require_once("inc/tree.php");
require_once("inc/auth.php");

$myAuth = new auth();
// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
if(isset($_GET['label'])) $theLabel = addslashes($_GET['label']);
$theID = (int) $_GET['id'];
$theSpecimenID = (int) $_GET['specimenid'];

// Create new meta object and check required input parameters and data types
switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        if($myAuth->isLoggedIn())
        {
            break;
        }
        else
        {
            $myMetaHeader->setMessage("102", "You must login to run this query.");
            break;
        }

    
    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isLoggedIn())
        {
            if($theID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            if(($theSpecimenID==NULL) && ($theLabel==NULL)) $myMetaHeader->setMessage("902", "Missing parameters - you haven't specified any parameters to update.");
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
            if($theSpecimenID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'specimenid' field is required.");
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
    // Create radius object 
    $myRadius = new radius();
    $parentTagBegin = $myRadius->getParentTagBegin();
    $parentTagEnd = $myRadius->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $myRadius->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (isset($theLabel)) $myRadius->setLabel($theLabel);
        if (!($theSpecimenID)==NULL) $myRadius->setSpecimenID($theSpecimenID);
        
        if( (($theMode=='update') && ($myAuth->radiusPermission($theID, "update")))  || 
            (($theMode=='create') && ($myAuth->radiusPermission($theID, "create")))    )
        {
            // Check user has permission to update / create radius before writing object to database
            $success = $myRadius->writeToDB();
            if($success)
            {
                $xmldata=$myRadius->asXML();
            }
            else
            {
               $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
            }
        }  
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on radiusid $theID");
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        if($myAuth->radiusPermission($theID, "delete"))
        {
            // Check user has permission to delete record before performing statement
            $success = $myRadius->deleteFromDB();
            if($success)
            {
                $xmldata=$myRadius->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on radiusid $theID");
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
                $sql="select tblradius.*, tblspecimen.specimenid, tbltree.treeid, tblsubsite.subsiteid, tblsubsite.siteid from tblradius, tblspecimen, tbltree, tblsubsite where radiusid=$theID 
                    and tblradius.specimenid=tblspecimen.specimenid and tblspecimen.treeid=tbltree.treeid and tbltree.subsiteid=tblsubsite.subsiteid order by tblradius.radiusid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read radius
                    if($myAuth->radiusPermission($row['radiusid'], "read"))
                    {
                        $myRadius = new radius();
                        $mySpecimen = new specimen();
                        $myTree = new tree();
                        $mySubSite = new subSite();
                        $mySite = new site();
                        $success = $myRadius->setParamsFromDB($row['radiusid']);
                        $success2 = $myRadius->setChildParamsFromDB();
                        $success3 = $mySpecimen->setParamsFromDB($row['specimenid']);
                        $success4 = $myTree->setParamsFromDB($row['treeid']);
                        $success5 = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success6 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 && $success4 && $success5 && $success6 )
                        {
                            $xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML("begin");
                            $xmldata.= $myTree->asXML("begin");
                            $xmldata.= $mySpecimen->asXML("begin");
                            $xmldata.= $myRadius->asXML();
                            $xmldata.= $mySpecimen->asXML("end");
                            $xmldata.= $myTree->asXML("end");
                            $xmldata.= $mySubSite->asXML("end");
                            $xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on radiusid ".$row['radiusid'], "Warning");
                    }
                }
            }
            else
            {
                $xmldata.= $parentTagBegin."\n";
                $sql="select * from tblradius order by radiusid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read radius
                    if($myAuth->radiusPermission($row['radiusid'], "read"))
                    {
                        $myRadius = new radius();
                        $success = $myRadius->setParamsFromDB($row['radiusid']);
                        $success2 = $myRadius->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.=$myRadius->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($myRadius->getLastErrorCode(), $myRadius->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on radiusid ".$row['radiusid'], "Warning");
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
