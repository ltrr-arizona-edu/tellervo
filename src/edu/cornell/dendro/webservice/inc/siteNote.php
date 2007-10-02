<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('dbhelper.php');

class siteNote 
{
    var $id = NULL;
    var $note = NULL;
    var $isStandard = NULL; 

    function siteNote()
    {

    }

    function setID($theID)
    {
        $this->id=$theID;
    }
    
    function setNote($theNote)
    {
        $this->id=$theNote;
    }
    
    function setIsStandard($theFlag)
    {
        $this->id=$theFlag;
    }

    function setParamsFromDB($theID)
    {
        global $dbconn;
        $this->id=$theID;

        $sql = "select * from tlkpsitenote where sitenoteid=$theID";
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            $this->note = $row['note'];
            $this->isStandard = fromPGtoPHPBool($row['isstandard']);
        }

    }

    function writeToDB()
    {
        
    }

    function asXML()
    {
        $xml.= "<siteNote id=\"".$this->id."\" isStandard=\"".fromPGtoStringBool($this->isStandard)."\">".$this->note."</siteNote>\n";
        return $xml;
    }
} 
?>
