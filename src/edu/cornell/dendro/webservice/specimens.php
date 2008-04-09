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
require_once("inc/auth.php");
require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/output.php");

require_once("inc/specimen.php");
require_once("inc/tree.php");
require_once("inc/subSite.php");
require_once("inc/site.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
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
            if($myRequest->id == NULL) trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
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
            if($myRequest->name == NULL) trigger_error("902"."Missing parameter - 'name' field is required.", E_USER_ERROR);
            if($myRequest->treeid == NULL) trigger_error("902"."Missing parameter - 'treeid' field is required.", E_USER_ERROR);
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

$xmldata ="";
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
            trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage(), E_USER_ERROR);
        }
    }

    // Update parameters in object if updating or creating an object 
    if($myRequest->mode=='update' || $myRequest->mode=='create')
    {
        if (isset($myRequest->name))                            $mySpecimen->setName($myRequest->name);
        if (isset($myRequest->treeid))                          $mySpecimen->setTreeID($myRequest->treeid);
        if (isset($myRequest->datecollected))                   $mySpecimen->setDateCollected($myRequest->datecollected);
        if (isset($myRequest->specimentype))                    $mySpecimen->setSpecimenType($myRequest->specimentype);
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
        if (isset($myRequest->specimencontinuity))              $mySpecimen->setSpecimenContinuity($myRequest->specimencontinuity);
        if (isset($myRequest->pith))                            $mySpecimen->setPith($myRequest->pith);
        if (isset($myRequest->specimenquality))                 $mySpecimen->setSpecimenQuality($myRequest->specimenquality);
        if (isset($myRequest->terminalring))                    $mySpecimen->setTerminalRing($myRequest->terminalring);

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
                trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage(), E_USER_ERROR);
            }
        }  
        else
        {
            if(isset($myRequest->id))
            {
                trigger_error("103"."Permission denied on specimenid ".$myRequest->id, E_USER_ERROR);
            }
            else
            {
                trigger_error("103"."Permission denied on treeid ".$myRequest->treeid, E_USER_ERROR);
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
                trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage(), E_USER_ERROR);
            }
        }
        else
        {
            trigger_error("103"."Permission denied on specimenid ".$myRequest->id, E_USER_ERROR);
        }
    }

    if($myRequest->mode=='read')
    {
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            // DB connection ok
            // Build SQL depending on parameters
            if($myRequest->id!=NULL)
            {
                // Check user has permission to read tree
                if($myAuth->specimenPermission($myRequest->id, "read"))
                {
                    $mySpecimen = new specimen();
                    $success = $mySpecimen->setParamsFromDB($myRequest->id);
                    $success2 = $mySpecimen->setChildParamsFromDB();

                    if($success && $success2)
                    {
                        $xmldata.=$mySpecimen->asXML();
                    }
                    else
                    {
                        trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage(), E_USER_ERROR);
                    }
                }
                else
                {
                    trigger_error("103"."Permission denied on specimenid ".$myRequest->id, E_USER_WARNING);
                }
            }
            else
            {
                // Output all specimens
                $sql="select * from tblspecimen order by specimenid";
                $result = pg_query($dbconn, $sql);
                $xmldata.=$parentTagBegin;
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read specimen
                    if($myAuth->specimenPermission($row['specimenid'], "read"))
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
                            trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage(), E_USER_ERROR);
                        }
                    }
                    else
                    {
                        trigger_error("103"."Permission denied on specimenid ".$row['specimenid'], E_USER_WARNING);
                    }
                }
                $xmldata.=$parentTagEnd;
            }
        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database", E_USER_ERROR);
        }
    }
}

// ***********
// OUTPUT DATA
// ***********
writeOutput($myMetaHeader, $xmldata);
