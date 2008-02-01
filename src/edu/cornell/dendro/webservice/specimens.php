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
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/specimen.php");
require_once("inc/tree.php");
require_once("inc/subSite.php");
require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myMetaHeader   = new meta();
$myRequest      = new specimenRequest($myMetaHeader, $myAuth);

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
    case "read":
        $myMetaHeader = new meta("read");
        if($myAuth->isLoggedIn())
        {
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "update":
        $myMetaHeader = new meta("update");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->id == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "delete":
        $myMetaHeader = new meta("delete");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->id == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'id' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }


    case "create":
        $myMetaHeader = new meta("create");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->label == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'label' field is required.");
            if($myRequest->treeid == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'treeid' field is required.");
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "failed":
        $myMetaHeader->setRequestType("help");
        break;

    default:
        $myMetaHeader->setRequestType("help");
        // Output the resulting XML
        $xmldata ="Details of how to use this web service will be added here later!";
        writeHelpOutput($myMetaHeader,$xmldata);
        die;
}

// *************
// PERFORM QUERY
// *************

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{
    // Create specimen object 
    $mySpecimen = new specimen();
    $parentTagBegin = $mySpecimen->getParentTagBegin();
    $parentTagEnd = $mySpecimen->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $mySpecimen->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySpecimen->getLastErrorCode(), $mySpecimen->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->label))                           $mySpecimen->setLabel($myRequest->label);
        if (isset($myRequest->treeid))                          $mySpecimen->setTreeID($myRequest->treeid);
        if (isset($myRequest->datecollected))                   $mySpecimen->setDateCollected($myRequest->datecollected);
        if (isset($myRequest->specimentypeid))                  $mySpecimen->setSpecimenTypeID($myRequest->specimentypeid);
        if (isset($myRequest->isterminalringverified))          $mySpecimen->setIsTerminalRingVerified($myRequest->isterminalringverified);
        if (isset($myRequest->sapwoodcount))                    $mySpecimen->setSapwoodCount($myRequest->sapwoodcount);
        if (isset($myRequest->issapwoodcountverified))          $mySpecimen->setIsSapwoodCountVerified($myRequest->issapwoodcountverified);
        if (isset($myRequest->isspecimenqualityverified))       $mySpecimen->setIsSpecimenQualityVerified($myRequest->isspecimenqualityverified);
        if (isset($myRequest->isspecimencontinuityverified))    $mySpecimen->setIsSpecimenContinuityVerified($myRequest->isspecimencontinuityverified);
        if (isset($myRequest->ispithverified))                  $mySpecimen->setIsPithVerified($myRequest->ispithverified);
        if (isset($myRequest->unmeasuredpre))                   $mySpecimen->setUnmeasPre($myRequest->unmeasuredpre);
        if (isset($myRequest->isunmeasuredpreverified))         $mySpecimen->setIsUnmeasPreVerified($myRequest->isunmeasuredpreverified);
        if (isset($myRequest->unmeasuredpost))                  $mySpecimen->setUnmeasPost($myRequest->unmeasuredpost);
        if (isset($myRequest->isunmeasuredpostverified))        $mySpecimen->setIsUnmeasPostVerified($myRequest->isunmeasuredpostverified);
        if (isset($myRequest->specimencontinuityid))            $mySpecimen->setSpecimenContinuityID($myRequest->specimencontinuityid);
        if (isset($myRequest->pithid))                          $mySpecimen->setPithID($myRequest->pithid);
        if (isset($myRequest->specimenqualityid))               $mySpecimen->setSpecimenQualityID($myRequest->specimenqualityid);
        if (isset($myRequest->terminalringid))                  $mySpecimen->setTerminalRingID($myRequest->terminalringid);

        if( (($myRequest->mode=='update') && ($myAuth->specimenPermission($myRequest->id, "update")))  || 
            (($myRequest->mode=='create') && ($myAuth->treePermission($myRequest->treeid, "create")))    )
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
            if(isset($myRequest->id))
            {
                $myMetaHeader->setMessage("103", "Permission denied on specimenid ".$myRequest->id);
            }
            else
            {
                $myMetaHeader->setMessage("103", "Permission denied on treeid ".$myRequest->treeid);
            }
        }
    }

    // Delete record from db if requested
    if($myRequest->mode=='delete')
    {
        if($myAuth->specimenPermission($myRequest->id, "delete"))
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
            $myMetaHeader->setMessage("103", "Permission denied on specimenid ".$myRequest->id);
        }
    }

    if($myRequest->mode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if(!$myRequest->id==NULL)
            {
                // Output multiple specimens as XML

                $sql = "select tblsite.siteid, tblsubsite.subsiteid, tbltree.treeid, tblspecimen.specimenid ";
                $sql.= "from tblsite, tblsubsite, tbltree, tblspecimen ";
                $sql.= "where tblspecimen.specimenid=$myRequest->id and tblspecimen.treeid=tbltree.treeid and tbltree.subsiteid=tblsubsite.subsiteid and tblsubsite.siteid=tblsite.siteid";
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
                // Output one specimen with its parents
                $sql="select * from tblspecimen order by specimenid";
                $result = pg_query($dbconn, $sql);
                $xmldata.=$parentTagBegin;
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
                $xmldata.=$parentTagEnd;
            }
        }
        else
        {
            // Connection bad
            $myMetaHeader->setMessage("001", "Error connecting to database");
        }
    }
}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata);
