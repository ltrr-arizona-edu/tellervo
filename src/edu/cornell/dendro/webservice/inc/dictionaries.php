<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

function getDictionariesAsXML()
{
    global $dbconn;

    // Specimen Type
    echo "<specimenTypeDictionary>\n";
    $sql = "select * from tlkpSpecimenType";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<specimenType id=\"".$row['specimentypeid']."\">".$row['label']."</specimenType>\n";
    }
    echo "</specimenTypeDictionary>\n";
    
    // Terminal Ring
    echo "<terminalRingDictionary>\n";
    $sql = "select * from tlkpTerminalRing";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<terminalRing id=\"".$row['terminalringid']."\">".$row['label']."</terminalRing>\n";
    }
    echo "</terminalRingDictionary>\n";

    // specimen quality 
    echo "<specimenQualityDictionary>\n";
    $sql = "select * from tlkpspecimenquality";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<specimenQuality id=\"".$row['specimenqualityid']."\">".$row['label']."</specimenQuality>\n";
    }
    echo "</specimenQualityDictionary>\n";
    
    // Pith 
    echo "<pithDictionary>\n";
    $sql = "select * from tlkppith";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<pith id=\"".$row['pithid']."\">".$row['label']."</pith>\n";
    }
    echo "</pithDictionary>\n";
    
    // Taxon ranks 
    echo "<taxonRankDictionary>\n";
    $sql = "select * from tlkptaxonrank order by rankorder";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<taxonRank id=\"".$row['taxonrankid']."\" rankorder=\"".$row['rankorder']."\">".$row['taxonrank']."</taxonRank>\n";
    }
    echo "</taxonRankDictionary>\n";
    
    // Site note 
    echo "<siteNoteDictionary>\n";
    $sql = "select * from tlkpsitenote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<siteNote id=\"".$row['sitenoteid']."\">".$row['note']."</siteNote>\n";
    }
    echo "</siteNoteDictionary>\n";

    // Tree note 
    echo "<treeNoteDictionary>\n";
    $sql = "select * from tlkptreenote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<treeNote id=\"".$row['treenoteid']."\">".$row['note']."</treeNote>\n";
    }
    echo "</treeNoteDictionary>\n";

    // Measurement note 
    echo "<measurementNoteDictionary>\n";
    $sql = "select * from tlkpvmeasurementnote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<measurementNote id=\"".$row['measurementnoteid']."\">".$row['note']."</measurementNote>\n";
    }
    echo "</measurementNoteDictionary>\n";

    // Reading note 
    echo "<readingNoteDictionary>\n";
    $sql = "select * from tlkpreadingnote where isstandard='t'";
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result)) 
    {
        echo "<readingNote id=\"".$row['readingnoteid']."\">".$row['note']."</readingNote>\n";
    }
    echo "</readingNoteDictionary>\n";


}


 
?>
