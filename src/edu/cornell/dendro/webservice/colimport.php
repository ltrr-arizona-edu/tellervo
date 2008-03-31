<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
//header('Content-Type: application/xhtml+xml; charset=utf-8');

require_once("inc/dbsetup.php");
require_once("config.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
//require_once("inc/errors.php");
require_once("inc/request.php");
require_once("inc/output.php");

// Create Authentication, Request and Header objects
$myAuth         = new auth();
$myRequest      = new importColRequest($myMetaHeader, $myAuth);

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
    case "import":
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

    default:
        trigger_error("999"."No mode specified");
        break;
}

echo "<html>";


$speciesstringsfile = "/tmp/search.txt";
$notfoundlog = "/tmp/notfound.txt";

$handle = @fopen($speciesstringsfile, "r");
$notfoundhandle = @fopen($notfoundlog, "a");

if ($handle) 
{
    while (!feof($handle)) 
    {
            // Read search string from text file
            $species = trim(fgets($handle));
            
            //Only lookup if string has not got crud in it
            if(strlen($species)>3)
            {
                // Get CoL WS XML
                $colURL="http://webservice.catalogueoflife.org/annual-checklist/2008/search.php?response=full&name=".urlencode($species);
                $colXML = simplexml_load_file($colURL);
                if($colXML['total_number_of_results']==0)
                {
                    echo "No match for $species<br>";
                    fwrite($notfoundhandle, $species."\n");
                    ob_flush();
                    flush();


                }
                else
                {
                    echo "Match found for $species<br>";

                    // Write Higher taxon records
                    $parentID = NULL;
                    foreach ($colXML->result->classification->taxon as $currentTaxon)
                    {
                        if(!taxonRecordExists($currentTaxon->id))
                        {
                            echo "Written to DB - ";
                            dbWriteTaxa($currentTaxon->id, $parentID, $currentTaxon->rank, $currentTaxon->name);
                        }
                        else
                        {
                            echo "Already in DB<br>";
                        }
                        ob_flush();
                        flush();
                        $parentID = $currentTaxon->id;
                    }

                    // Write requested taxon details
                    if(!taxonRecordExists($colXML->result->id))
                    {
                        echo "Written to DB - ";
                        dbWriteTaxa($colXML->result->id, $parentID, $colXML->result->rank, $colXML->result->name);
                        ob_flush();
                        flush();
                    }

                }
            }

    }
    fclose($handle);
}
fclose($notfoundhandle);

echo "</html>";

function taxonRecordExists($colID)
{
    global $dbconn;
    $sql="select count(*) from tlkptaxontest where colid=$colID";
    
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);    
        while ($row = pg_fetch_array($result))
        {
            // Get all tree note id's for this tree and store 
            if($row['count']>0)
            {
                return True;
            }
            else
            {   
                return False;
            }
        }
    
    }


}



function dbWriteTaxa($colID, $colParentID, $taxonRank, $label)
{
    global $dbconn;

    // Lookup taxon rank id
    $sql = "select taxonrankid from tlkptaxonrank where taxonrank ilike '$taxonRank'";
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            // Get all tree note id's for this tree and store 
            $taxonRankID = $row['taxonrankid'];
        }
    }
    else
    {
        echo "rank sql error";
    }

    // Lookup parent taxon id
    if ($colParentID)
    {
        $sql = "select taxonid from tlkptaxontest where colid=$colParentID";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                // Get all tree note id's for this tree and store 
                $parentTaxonID = $row['taxonid'];
            }
        }
        else
        {
            echo "parent taxon id sql error";
        }
    }


    if ($colParentID)
    {
        $sql = "insert into tlkptaxontest (colid, colparentid, taxonrankid, label, parenttaxonid) VALUES ('$colID', '$colParentID', '$taxonRankID', '$label', '$parentTaxonID')"; 
    }
    else
    {
        $sql = "insert into tlkptaxontest (colid, taxonrankid, label) VALUES ('$colID', '$taxonRankID', '$label' )"; 
    }
            
    echo $sql."<br>";
    // Run SQL command
    if ($sql)
    {
        // Run SQL 
        $writeResult = pg_query($dbconn, $sql);
        if(!$writeResult)
        {
            "db write error<br>";
        }
    }
 

}
?>
