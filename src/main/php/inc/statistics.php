<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */
require_once('dbhelper.php');
require_once('inc/dbEntity.php');

require_once("inc/readingNote.php");
require_once("inc/taxon.php");
require_once("inc/region.php");
require_once("inc/securityUser.php");
require_once("inc/securityGroup.php");
require_once("inc/box.php");
require_once("inc/domain.php");

class statistics 
{
    var $parentXMLTag = "statistics"; 
    var $xmldata = NULL;
    var $lastErrorCode = NULL;
    var $lastErrorMessage = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "read":
                return true;
            
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request for statistics");
                return false;
        }
    }

    function setParamsFromDB()
    {
        global $dbconn;
        global $firebug;
        
        $xmldata = "";
        
        
		// Standard dictionary items
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
		$sql = "SELECT COUNT(securityuserid) AS val FROM tblsecurityuser";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"users.total\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(securityuserid) AS val FROM tblsecurityuser WHERE isactive='t'";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"users.active\" value=\"".$row['val']."\"/>\n";
		}
		
		
		$sql = "SELECT COUNT(objectid) AS val FROM tblobject";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"entities.objects\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(objectid) AS val FROM tblobject WHERE parentobjectid IS NULL";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"entities.topLevelObjects\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(elementid) AS val FROM tblelement";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"entities.elements\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(sampleid) AS val FROM tblsample";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"entities.samples\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(radiusid) AS val FROM tblradius";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"entities.radii\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(vmeasurementid) AS val FROM tblvmeasurement";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"entities.vmeasurements\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT ss.samplestatus, COUNT(s.samplestatusid) AS val FROM tblsample AS s, tlkpsamplestatus AS ss WHERE s.samplestatusid=ss.samplestatusid GROUP BY ss.samplestatus";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"samplestatus.".$row['samplestatus']."\" value=\"".$row['val']."\"/>\n";
		}
		
		$sql = "SELECT COUNT(readingid) AS val FROM tblreading";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"rings.totalmeasured\" value=\"".$row['val']."\"/>\n";
		}

		$sql = "SELECT sum(reading)/1000000000 AS val FROM tblreading";
	        $result = pg_query($dbconn, $sql);
	        while ($row = pg_fetch_array($result))
		{
			$xmldata.="<statistic key=\"rings.widthsInKM\" value=\"".$row['val']."\"/>\n";
		}


        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database");
        }
        
        // Put xmldata into class variable
        if($xmldata!=NULL)
        {
            $this->xmldata=$xmldata;
            return true;
        }
        else
        {
            return false;
        }
            
        
    }
    
    function setChildParamsFromDB()
    {
        return true;
    }

    /***********/
    /*ACCESSORS*/
    /***********/
    
    function asXML($mode="all")
    {
        if(isset($this->xmldata))
        {
		
            return "<statisticalResults>".$this->xmldata."</statisticalResults>\n";
        }
        else
        {
            return false;
        }
    }

    function getParentTagBegin()
    {
    }

    function getParentTagEnd()
    {
    }

    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }

    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
    }

  


// End of Class
} 
?>
