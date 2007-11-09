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
require_once("inc/specimen.php");
require_once("inc/tree.php");
require_once("inc/subSite.php");
require_once("inc/site.php");
//require_once("inc/radii.php");
require_once("inc/auth.php");

$myAuth = new auth();

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theID = (int) $_GET['id'];
if(isset($_GET['label'])) $theName = addslashes($_GET['label']);
$theTreeID = (int) $_GET['treeid'];
$theDay = (int) $_GET['collectedday'];
$theMonth = (int) $_GET['collectedmonth'];
$theYear = (int) $_GET['collectedyear'];
$theSpecimenTypeID = (int) $_GET['specimentypeid'];
$theTerminalRingID = (int) $_GET['terminalringid'];
$theIsTerminalRingVerified= fromStringToPHPBool($_GET['isterminalringverified']);
$theSapwoodCount = (int) $_GET['sapwoodcount'];
$theIsSapwoodCountVerified= fromStringToPHPBool($_GET['issapwoodcountverified']);
$theSpecimenQualityID = (int) $_GET['specimenqualityid'];
$theIsSpecimenQualityVerified= fromStringToPHPBool($_GET['isspecimenqualityverified']);
$theSpecimenContinuityID = (int) $_GET['specimencontinuityid'];
$thePithID = (int) $_GET['pithid'];
$theIsPithVerified= fromStringToPHPBool($_GET['ispithverified']);
$theUnmeasPre = (int) $_GET['unmeaspre'];
$theIsUnmeasPreVerified= fromStringToPHPBool($_GET['isunmeaspreverified']);
$theUnmeasPost = (int) $_GET['unmeaspost'];
$theIsUnmeasPostVerified= fromStringToPHPBool($_GET['isunmeaspostverified']);

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
            if($theTreeID == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'treeid' field is required.");
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
    // Create specimen object 
    $mySpecimen = new specimen();
    $parentTagBegin = $mySpecimen->getParentTagBegin();
    $parentTagEnd = $mySpecimen->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $mySpecimen->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (isset($theLabel)) $mySpecimen->setLabel($theLabel);
        if (isset($theTreeID)) $mySpecimen->setTreeID($theTreeID);
        if ((isset($theDay)) && (isset($theMonth)) && (isset($theYear))) $mySpecimen->setCollectionDate($theDay, $theMonth, $theYear);
        if (isset($theSpecimenTypeID)) $mySpecimen->setSpecimenTypeID($theSpecimenTypeID);
        if (isset($theTerminalRingID)) $mySpecimen->setTerminalRingID($theTerminalRingID);
        if (isset($theIsTerminalRingVerified)) $mySpecimen->setIsTerminalRingVerified($theIsTerminalRingVerified);
        if (isset($theSapwoodCount)) $mySpecimen->setSapwoodCount($theSapwoodCount);
        if (isset($theIsSapwoodCountVerified)) $mySpecimen->setIsSapwoodCountVerified($theIsSapwoodCountVerified);
        if (isset($theSpecimenQualityID)) $mySpecimen->setSpecimenQualityID($theSpecimenQualityID);
        if (isset($theIsSpecimenQualityVerified)) $mySpecimen->setIsSpecimenQualityVerified($theIsSpecimenQualityVerified);
        if (isset($theSpecimenContinuityID)) $mySpecimen->setSpecimenContinuityID($theSpecimenContinuityID);
        if (isset($thePithID)) $mySpecimen->setPithID($thePithID);
        if (isset($theIsPithVerified)) $mySpecimen->setIsPithVerified($theIsPithVerified);
        if (isset($theUnmeasPre)) $mySpecimen->setUnmeasPre($theUnmeasPre);
        if (isset($theIsUnmeasPreVerified)) $mySpecimen->setIsUnmeasPreVerified($theIsUnmeasPreVerified);
        if (isset($theUnmeasPost)) $mySpecimen->setUnmeasPost($theUnmeasPost);
        if (isset($theIsUnmeasPostVerified)) $mySpecimen->setIsUnmeasPostVerified($theIsUnmeasPostVerified);

        if( (($theMode=='update') && ($myAuth->specimenPermission($theID, "update")))  || 
            (($theMode=='create') && ($myAuth->specimenPermission($theID, "create")))    )
        {
            // Write to object to database
            $success = $mySpecimen->writeToDB();
            if($success)
            {
                $xmldata=$mySpecimen->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
            }
        }  
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on specimenid $theID");
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        if($myAuth->specimenPermission($theID, "delete"))
        {
            // Write to Database
            $success = $mySpecimen->deleteFromDB();
            if($success)
            {
                $xmldata=$mySpecimen->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on specimenid $theID");
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
                // Output multiple specimens as XML

                $sql = "select tblsite.siteid, tblsubsite.subsiteid, tbltree.treeid, tblspecimen.specimenid ";
                $sql.= "from tblsite, tblsubsite, tbltree, tblspecimen ";
                $sql.= "where tblspecimen.specimenid=$theID and tblspecimen.treeid=tbltree.treeid and tbltree.subsiteid=tblsubsite.subsiteid and tblsubsite.siteid=tblsite.siteid";
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read tree
                    if($myAuth->specimenPermission($row['specimenid'], "read"))
                    {
                        $mySpecimen = new specimen();
                        $myTree = new tree();
                        $mySubSite = new subSite();
                        $mySite = new site();
                        $success = $mySpecimen->setParamsFromDB($row['specimenid']);
                        $success2 = $mySpecimen->setChildParamsFromDB();
                        $success3 = $myTree->setParamsFromDB($row['treeid']);
                        $success4 = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success5 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 && $success4 && $success5)
                        {
                            $xmldata.=$mySite->asXML("begin");
                            $xmldata.=$mySubSite->asXML("begin");
                            $xmldata.=$myTree->asXML("begin");
                            $xmldata.=$mySpecimen->asXML();
                            $xmldata.=$myTree->asXML("end");
                            $xmldata.=$mySubSite->asXML("end");
                            $xmldata.=$mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on specimenid ".$row['specimenid'], "Warning");
                    }
                }
            }
            else
            {
                $xmldata.=$parentTagBegin."\n";
                // Output one specimen with its parents
                $sql="select * from tblspecimen order by specimenid";
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read tree
                    if($myAuth->treePermission($row['treeid'], "read"))
                    {
                        $mySpecimen = new specimen();
                        $success = $mySpecimen->setParamsFromDB($row['specimenid']);
                        $success2 = $mySpecimen->setChildParamsFromDB();
                        if($success && $success2)
                        {
                            $xmldata.=$mySpecimen->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on specimenid ".$row['specimenid'], "Warning");
                    }
                }
                $xmldata.=$parentTagEnd."\n";
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
echo "<content>\n";
echo $xmldata;
echo "</content>\n";
echo "</corina>";
