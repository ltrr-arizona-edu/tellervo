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

require_once("inc/taxon.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new taxonRequest($myMetaHeader, $myAuth);

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
        $myMetaHeader->setRequestType("read");
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
        $myMetaHeader->setRequestType("update");
        if($myAuth->isLoggedIn())
        {
            trigger_error("104"."The taxon webservice does not support the updating of taxa.", E_USER_ERROR);
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "delete":
        $myMetaHeader->setRequestType("delete");
        if($myAuth->isLoggedIn())
        {
            trigger_error("104"."The taxon webservice does not support the deleting of taxa.", E_USER_ERROR);
            break;
        }
        else
        {
            $myMetaHeader->requestLogin($myAuth->nonce());
            break;
        }

    case "create":
        $myMetaHeader->setRequestType("create");
        if($myAuth->isLoggedIn())
        {
            if($myRequest->colid == NULL) 
            {
                if(!(isset($myRequest->parentid)) )   trigger_error("902"."Missing parameter - 'parentid' field is required.", E_USER_ERROR);
                if(!(isset($myRequest->label)) )      trigger_error("902"."Missing parameter - 'label' field is required.", E_USER_ERROR);
                if(!(isset($myRequest->taxonrank)) )  trigger_error("902"."Missing parameter - 'taxonrank' field is required.", E_USER_ERROR);
            }
            else
            {
                trigger_error("902"."Missing parameter - 'colid' field is required.", E_USER_ERROR);
            }
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
    // Create tree object 
    $myTaxon = new taxon();
    $parentTagBegin = $myTaxon->getParentTagBegin();
    $parentTagEnd = $myTaxon->getParentTagEnd();

    if($myRequest->mode=='update' || $myRequest->mode=='delete') 
    {
        $success = $myTaxon->setParamsFromDB($myRequest->id);
        if(!$success) 
        {
            trigger_error($mySpecimen->getLastErrorCode().$mySpecimen->getLastErrorMessage(), E_USER_ERROR);
        }
    }
    
    if($myRequest->mode=='update' )
    {
        if (isset($myRequest->parentid)) $myTaxon->setParentID($myRequest->parentid);
        if (isset($myRequest->label)) $myTaxon->setLabel($myRequest->label);
        $success = $myTaxon->writeToDB();
        if($success)
        {
            $xmldata=$myTaxon->asXML();
        }
        else
        {
           trigger_error($myTaxon->getLastErrorCode().$myTaxon->getLastErrorMessage(), E_USER_ERROR);
        }
    } 

    if($myRequest->mode=='create') 
    {
        if ($myAuth->isAdmin()) 
        {
            if (isset($myRequest->colid))
            {
                // Creating new taxon using Catalogue of Life
                $success = $myTaxon->setParamsFromCoL($myRequest->colid);
                if($success)
                {
                    $xmldata=$myTaxon->asXML();
                }
                else
                {
                   trigger_error($myTaxon->getLastErrorCode().$myTaxon->getLastErrorMessage(), E_USER_ERROR);
                }
            }
            else
            {
                // Creating new taxon from scratch
                if (isset($myRequest->parentid)) $myTaxon->setParentID($myRequest->parentid);
                if (isset($myRequest->label)) $myTaxon->setLabel($myRequest->label);
                $success = $myTaxon->writeToDB();
                if($success)
                {
                    $xmldata=$myTaxon->asXML();
                }
                else
                {
                   trigger_error($myTaxon->getLastErrorCode().$myTaxon->getLastErrorMessage(), E_USER_ERROR);
                }
            }
        }
        else
        {
            trigger_error("103"."Permission denied on taxonid $myRequest->id", E_USER_ERROR);
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
                    $myTaxon = new taxon();
                    $success = $myTaxon->setParamsFromDB($myRequest->id);

                    if($success)
                    {
                        $xmldata.= $myTaxon->asXML();
                    }
                    else
                    {
                        trigger_error($myTaxon->getLastErrorCode().$myTaxon->getLastErrorMessage(), E_USER_ERROR);
                    }
            }
            else
            {
                $xmldata.= $parentTagBegin."\n";
                $sql="select * from tlkptaxon order by taxonrankid, label";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $myTaxon = new taxon();
                    $success = $myTaxon->setParamsFromDB($row['taxonid']);

                    if($success)
                    {
                        $xmldata.=$myTaxon->asXML();
                    }
                    else
                    {
                        trigger_error($myTaxon->getLastErrorCode().$myTaxon->getLastErrorMessage(), E_USER_WARNING);
                    }
                }
                $xmldata.= $parentTagEnd."\n";
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
