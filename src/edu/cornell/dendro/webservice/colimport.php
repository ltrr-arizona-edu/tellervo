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

$searchCount = 0;
$speciesFoundCount = 0;
$dbEntryCount = 0;
$synonymCount = 0;
$dubiousCount = 0;
$notFoundCount = 0;


$speciesstringsfile = "/tmp/search.txt";
$notfoundlog = "/tmp/colimportlogs/notfound.txt";
$dubiouslog = "/tmp/colimportlogs/dubiousmatches.txt";
$synonymlog = "/tmp/colimportlogs/synonym.txt";

$handle = @fopen($speciesstringsfile, "r");
$notfoundhandle = @fopen($notfoundlog, "w");
$dubioushandle = @fopen($dubiouslog, "w");
$synonymhandle = @fopen($synonymlog, "w");

if ($handle) 
{
    while (!feof($handle)) 
    {
            // Read search string from text file
            $species = trim(fgets($handle));
            
            //Only lookup if string has not got crud in it
            if(strlen($species)>3)
            {
                $searchCount++;

                // Get CoL WS XML
                $colURL="http://webservice.catalogueoflife.org/annual-checklist/2008/search.php?response=full&name=".urlencode($species);
                $colXML = simplexml_load_file($colURL);
                if($colXML['total_number_of_results']==0)
                {
                    $notFoundCount++;
                    echo "<br><b><font color=\"red\">No match for $species</font></b><br>";
                    fwrite($notfoundhandle, $species."\n");
                    ob_flush();
                    flush();


                }
                elseif($colXML['total_number_of_results']>1)
                {
                    $dubiousCount++;
                    echo "<br><b><font color=\"red\">Multiple matches for $species</font></b><br>";
                    fwrite($dubioushandle, $species."\n");
                    ob_flush();
                    flush();
                }
                else
                {
                    echo "<br><b>Match found for $species</b><br>";
                    $speciesFoundCount++;

                    if($colXML->result->name_status!='accepted name')
                    {

                        // Write Higher taxon records
                        $parentID = NULL;
                        foreach ($colXML->result->accepted_name->classification->taxon as $currentTaxon)
                        {
                            if(!taxonRecordExists($currentTaxon->id))
                            {
                                echo "Written '".$currentTaxon->name."' to DB<br>";
                                dbWriteTaxa($currentTaxon->id, $parentID, $currentTaxon->rank, $currentTaxon->name);
                                $dbEntryCount++;
                            }
                            ob_flush();
                            flush();
                            $parentID = $currentTaxon->id;
                        }
                        
                        // Add to synonym count and log
                        $synonymCount++;
                        fwrite($synonymhandle, $species." - synonym of ".$colXML->result->accepted_name->name." ".$colXML->result->accepted_name->author."\n");

                        // Write requested taxon details
                        if(!taxonRecordExists($colXML->result->accepted_name->id))
                        {
                            echo "Written '".$colXML->result->accepted_name->name."' to DB<br>";
                            if($colXML->result->accepted_name->rank=="Infraspecies")
                            {
                                $rank = $colXML->result->accepted_name->infraspecies_marker;
                            }
                            else
                            {
                                $rank = $colXML->result->accepted_name->rank;
                            }
                            dbWriteTaxa($colXML->result->accepted_name->id, $parentID, $rank, $colXML->result->accepted_name->name." ".$colXML->result->accepted_name->author);
                            $dbEntryCount++;
                            ob_flush();
                            flush();
                        }

                    }
                    else
                    {
                        //Accepted name

                        // Write Higher taxon records
                        $parentID = NULL;
                        foreach ($colXML->result->classification->taxon as $currentTaxon)
                        {
                            if(!taxonRecordExists($currentTaxon->id))
                            {
                                echo "Written '".$currentTaxon->name."' to DB<br>";
                                dbWriteTaxa($currentTaxon->id, $parentID, $currentTaxon->rank, $currentTaxon->name);
                                $dbEntryCount++;
                            }
                            ob_flush();
                            flush();
                            $parentID = $currentTaxon->id;
                        }

                        // Write requested taxon details
                        if(!taxonRecordExists($colXML->result->id))
                        {
                            if($colXML->result->rank=="Infraspecies")
                            {
                                // This needs to change
                                $rank = $colXML->result->infraspecies_marker;
                            }
                            else
                            {
                                $rank = $colXML->result->rank;
                            }
                            echo "Written '".$colXML->result->name."' to DB<br>";
                            dbWriteTaxa($colXML->result->id, $parentID, $rank, $colXML->result->name." ".$colXML->result->author);
                            $dbEntryCount++;
                            ob_flush();
                            flush();
                        }
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
    $sql="select count(*) from tlkptaxon where colid=$colID";
    
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
        $sql = "select taxonid from tlkptaxon where colid=$colParentID";
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
        $sql = "insert into tlkptaxon (colid, colparentid, taxonrankid, label, parenttaxonid) VALUES ('$colID', '$colParentID', '$taxonRankID', '$label', '$parentTaxonID')"; 
    }
    else
    {
        $sql = "insert into tlkptaxon (colid, taxonrankid, label) VALUES ('$colID', '$taxonRankID', '$label' )"; 
    }
            
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

echo "<hr><table>";
echo "<tr><td><b>Total number of searches</b></td> <td>$searchCount </td></tr>";
echo "<tr><td><b>Total species found</b></td> <td>$speciesFoundCount </td></tr>";
echo "<tr><td><b>Database entries made</b></td> <td>$dbEntryCount </td></tr>";
echo "<tr><td><b>Number of synonyms</b></td> <td>$synonymCount </td></tr>";
echo "<tr><td><b>Number of ambiguous searches</b></td> <td>$dubiousCount </td></tr>";
echo "<tr><td><b>Number of names not found</b></td> <td>$notFoundCount </td></tr>";
echo "</table><hr>";
?>
