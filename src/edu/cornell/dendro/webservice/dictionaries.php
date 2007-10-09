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
require_once("inc/dictionaries.php");
require_once("inc/siteNote.php");

// Extract parameters from request and ensure no SQL has been injected
$theMode = strtolower(addslashes($_GET['mode']));

switch($theMode)
{
    case "read":
        $myMetaHeader = new meta("read");
        break;
    
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

$myMetaHeader->setUser("Guest", "", "");

$myXML="";

//Only attempt to run SQL if there are no errors so far
if(!($myMetaHeader->status == "Error"))
{

    global $dbconn;

    // Specimen Type
    $myXML.= "<specimenTypeDictionary>\n";
    $sql = "select * from tlkpSpecimenType";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<specimenType id=\"".$row['specimentypeid']."\">".$row['label']."</specimenType>\n";
    }
    $myXML.="</specimenTypeDictionary>\n";
    
    // Terminal Ring
    $myXML.= "<terminalRingDictionary>\n";
    $sql = "select * from tlkpTerminalRing";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<terminalRing id=\"".$row['terminalringid']."\">".$row['label']."</terminalRing>\n";
    }
    $myXML.= "</terminalRingDictionary>\n";

    // specimen quality 
    $myXML.= "<specimenQualityDictionary>\n";
    $sql = "select * from tlkpspecimenquality";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<specimenQuality id=\"".$row['specimenqualityid']."\">".$row['label']."</specimenQuality>\n";
    }
    $myXML.= "</specimenQualityDictionary>\n";
    
    // Pith 
    $myXML.= "<pithDictionary>\n";
    $sql = "select * from tlkppith";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<pith id=\"".$row['pithid']."\">".$row['label']."</pith>\n";
    }
    $myXML.= "</pithDictionary>\n";
    
    // Taxon ranks 
    $myXML.= "<taxonRankDictionary>\n";
    $sql = "select * from tlkptaxonrank order by rankorder";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<taxonRank id=\"".$row['taxonrankid']."\" rankorder=\"".$row['rankorder']."\">".$row['taxonrank']."</taxonRank>\n";
    }
    $myXML.= "</taxonRankDictionary>\n";
    
    
    // Site note 
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        // DB connection ok
        $sql="select * from tlkpsitenote order by sitenoteid";

        if($sql)
        {
            $myDummySiteNote = new siteNote();
            $myXML.=$myDummySiteNote->getParentTagBegin();
            // Run SQL
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                $mySiteNote = new siteNote();
                $success = $mySiteNote->setParamsFromDB($row['sitenoteid']);

                if($success)
                {
                    $myXML.=$mySiteNote->asXML();
                }
                else
                {
                    $myMetaHeader->setMessage($mySiteNote->getLastErrorCode, $mySiteNote->getLastErrorMessage);
                }
            }
            $myXML.=$myDummySiteNote->getParentTagEnd();
        }
    }
    else
    {
        // Connection bad
        $myMetaHeader->setMessage("001", "Error connecting to database");
    }
    

    // Tree note 
    $myXML.= "<treeNoteDictionary>\n";
    $sql = "select * from tlkptreenote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<treeNote id=\"".$row['treenoteid']."\">".$row['note']."</treeNote>\n";
    }
    $myXML.= "</treeNoteDictionary>\n";

    // Measurement note 
    $myXML.= "<measurementNoteDictionary>\n";
    $sql = "select * from tlkpvmeasurementnote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<measurementNote id=\"".$row['measurementnoteid']."\">".$row['note']."</measurementNote>\n";
    }
    $myXML.= "</measurementNoteDictionary>\n";

    // Reading note 
    $myXML.= "<readingNoteDictionary>\n";
    $sql = "select * from tlkpreadingnote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        $myXML.= "<readingNote id=\"".$row['readingnoteid']."\">".$row['note']."</readingNote>\n";
    }
    $myXML.= "</readingNoteDictionary>\n";


}

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
echo "<corina>\n";
echo $myMetaHeader->asXML();
echo "<data>\n";
echo $myXML;
echo "</data>\n";
echo "</corina>\n";
?>
