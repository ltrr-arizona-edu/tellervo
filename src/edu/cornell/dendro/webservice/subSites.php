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
require_once("inc/subSite.php");
require_once("inc/site.php");
require_once("inc/auth.php");

$myAuth = new auth();

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));
$theName = strtolower(addslashes($_GET['name']));
$theID = (int) $_GET['id'];
if(isset($_GET['label'])) $theName = addslashes($_GET['label']);

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
            if($theName == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
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
            if($theName == NULL) $myMetaHeader->setMessage("902", "Missing parameter - 'name' field is required.");
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
    // Create subSite object 
    $mySubSite = new subSite();
    $parentTagBegin = $mySubSite->getParentTagBegin();
    $parentTagEnd = $mySubSite->getParentTagEnd();

    // Set existing parameters if updating or deleting from database
    if($theMode=='update' || $theMode=='delete') 
    {
        $success = $mySubSite->setParamsFromDB($theID);
        if(!$success) 
        {
            $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
        }
    }

    // Update parameters in object if updating or creating an object 
    if($theMode=='update' || $theMode=='create')
    {
        if (isset($theName)) $mySubSite->setName($theName);
        if (isset($theCode)) $mySubSite->setCode($theCode);
        
        if( (($theMode=='update') && ($myAuth->subSitePermission($theID, "update")))  || 
            (($theMode=='create') && ($myAuth->subSitePermission($theID, "create")))    )
        {
            // Check user has permission to update / create subsite before writing object to database
            $success = $mySubSite->writeToDB();
            if($success)
            {
                $xmldata=$mySubSite->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on subsiteid $theID");
        }
    }

    // Delete record from db if requested
    if($theMode=='delete')
    {
        if($myAuth->subSitePermission($theID, "delete"))
        {
            // Check the user has permission to delete record before performing statement
            $success = $mySubSite->deleteFromDB();
            if($success)
            {
                $xmldata=$mySubSite->asXML();
            }
            else
            {
                $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
            }
        }
        else
        {
            $myMetaHeader->setMessage("103", "Permission denied on subsiteid $theID");
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
                $sql="select tblsubsite.subsiteid, tblsubsite.siteid from tblsubsite  where subsiteid=$theID order by tblsubsite.subsiteid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read subSite
                    if($myAuth->subSiteReadPermission($row['subsiteid'], "read"))
                    {
                        $mySubSite = new subSite();
                        $success = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success2 = $mySubSite->setChildParamsFromDB();
                        $mySite = new site();
                        $success3 = $mySite->setParamsFromDB($row['siteid']);

                        if($success && $success2 && $success3 )
                        {
                            $xmldata.= $mySite->asXML("begin");
                            $xmldata.= $mySubSite->asXML();
                            $xmldata.= $mySite->asXML("end");
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on subsiteid ".$row['subsiteid'], "Warning");
                    }
                }
            }
            else
            {
                $xmldata.= $parentTagBegin."\n";
                $sql="select * from tblsubsite order by subsiteid";
                // Run SQL
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    // Check user has permission to read subSite
                    if($myAuth->subSitePermission($row['subsiteid'], "read"))
                    {
                        $mySubSite = new subSite();
                        $success = $mySubSite->setParamsFromDB($row['subsiteid']);
                        $success2 = $mySubSite->setChildParamsFromDB();

                        if($success && $success2)
                        {
                            $xmldata.=$mySubSite->asXML();
                        }
                        else
                        {
                            $myMetaHeader->setMessage($mySubSite->getLastErrorCode(), $mySubSite->getLastErrorMessage());
                        }
                    }
                    else
                    {
                        $myMetaHeader->setMessage("103", "Permission denied on subsiteid ".$row['subsiteid'], "Warning");
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
