<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('inc/dbEntity.php');

/**
 * Class for interacting with a radiusEntity.  This contains the logic of how to read and write data from the database as well as error checking etc.
 *
 */
class tag extends tagEntity implements IDBAccessor
{	  

    /***************/
    /* CONSTRUCTOR */
    /***************/

    function __construct()
    {
        $groupXMLTag = "tag";
    	parent::__construct($groupXMLTag);
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row, $format="standard")
    {
    	$this->setID($row['tagid']);
     	$this->setOwnerID($row['ownerid']);
     	$this->setTagText($row['tag']);
     	
     	
        return true;
    }

    /**
     * Set the current objects parameters from the database
     *
     * @param Integer $theID
     * @return Boolean
     */
    function setParamsFromDB($theID)
    {
	global $firebug;
        global $dbconn;
        
        $this->setID($theID);
        $sql = "SELECT * FROM tbltag WHERE tagid='".pg_escape_string($this->getID())."'";
		$firebug->log($sql, "tag sql");
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified tag id");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->setParamsFromDBRow($row);
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        $this->cacheSelf();
        return TRUE;
    }

    function setParentsFromDB()
    {


    }      
    
    function setChildParamsFromDB()
    {
	global $firebug;
        global $dbconn;
        
        $sql = "SELECT * FROM tblvmeasurementtotag WHERE tagid='".pg_escape_string($this->getID())."'";
		$firebug->log($sql, "tag link sql");
	$firebug->log($sql, "ChildParamsFromDB SQL in tag");
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);

	     $this->entityIdArray = array();
            
            if(pg_num_rows($result)>0)
            {
                while ($row = pg_fetch_array($result))
		{	    
		    array_push($this->entityIdArray, $row['vmeasurementid']);
		}
            }            
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }


        $this->cacheSelf();
        return TRUE;
    }
    
    /**
     * Set parameters based on those in a parameters class
     *
     * @param tagParameters $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {    	
    	global $firebug;
    	
    	
    	$this->setID($paramsClass->getID());
     	$this->setOwnerID($paramsClass->getOwnerID());
     	$this->setTagText($paramsClass->getTagText());
     	
     	if($paramsClass->getEntityIdArray()!=NULL) $this->entityIdArray = $paramsClass->getEntityIdArray();
    	
    	$firebug->log($paramsClass, "params class");
  	return true;  
    }

    /**
     * Validate parameters from a parameters class
     *
     * @param radiusParameters $paramsObj
     * @param String $crudMode
     * @return unknown
     */
    function validateRequestParams($paramsObj, $crudMode)
    {
	global $firebug;
	
	$firebug->log("validateRequestParams called in tag.php");
    
        // Check parameters based on crudMode    	
        switch($crudMode)
        {
            case "read":
                if($paramsObj->getID()==NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
                
                return true;
         
            case "update":
                if($paramsObj->getID()==NULL)
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
                
                return true;

            case "delete":
                if($paramsObj->getID() ==NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
                return true;

            case "create":

                
                return true;

            case "assign":
                if($paramsObj->getID() ==NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
		if($paramsObj->getEntityIdArray()==NULL)
		{
			trigger_error("902"."Missing parameter - 'assignedTo' records are required.", E_USER_ERROR);
			return false;
		}
            
		return true;
		
	    case "unassign":
		if($paramsObj->getID() ==NULL) 
                {
                    trigger_error("902"."Missing parameter - 'id' field is required.", E_USER_ERROR);
                    return false;
                }
	    	if($paramsObj->getEntityIdArray()==NULL)
		{
			trigger_error("902"."Missing parameter - 'assignedTo' records are required.", E_USER_ERROR);
			return false;
	
		}
	    
		return true;
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }

    /***********/
    /*ACCESSORS*/
    /***********/

    
    function asXML($format='standard', $parts='all')
    {
            switch($format)
        {
        case "comprehensive":    
        case "standard":
        case "summary":
        case "minimal":
            return $this->_asXML($format, $parts);
        default:
            $this->setErrorMessage("901", "Unknown format. Must be one of 'standard', 'summary', 'minimal' or 'comprehensive'");
            return false;
        }
    }


    private function _asXML($format, $parts)
    {

        global $domain;
        $xml ="";
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()===NULL)
        {
            // Only return XML when there are no errors.
            if( ($parts=="all") || ($parts=="beginning"))
            {
                $xml.="<tag ";
				$xml.="id=\"".$this->getID()."\" ";
				if($this->getOwnerID()!=NULL) $xml.="ownerid=\"".$this->getOwnerID()."\" ";
				$xml.="value=\"".$this->getTagText()."\" >\n";
				
		if($this->entityIdArray!=NULL)
		{
		  $xml.="<assignedTo>\n";
		  
		  foreach($this->entityIdArray as $entity)
		  {
		    $xml.="<measurementSeries id=\"".$entity."\"/>\n";
		  }
		  
		  $xml.="</assignedTo>\n";
		
		}
				
                $xml.="</tag>\n";
                             
                
            }

            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    /***********/
    /*FUNCTIONS*/
    /***********/

    function writeToDB($crudMode="create")
    {
    	global $dbconn;
    	global $firebug;
    	global $domain;
	global $myMetaHeader;
	
	if($crudMode=="update" && $this->getID()==NULL)
        {
	    $this->setErrorMessage("902", "Missing parameter - 'id' field is required when editing");
            return FALSE;
        }
	
	if($crudMode=="create")
	{
	 
	   if($this->getID()==NULL) $this->setID(uuid::getUUID(), $domain);  
	
	   $sql = "INSERT INTO tbltag (tagid, ownerid, tag) values (";
	   $sql.= dbHelper::tellervo_pg_escape_string($this->getID()). ", ";	
           $sql.= dbHelper::tellervo_pg_escape_string($this->getOwnerID()).  ", ";     
	   $sql.= dbHelper::tellervo_pg_escape_string($this->getTagText()).  ", "; 
	   $sql = substr($sql, 0, -2);
           $sql.=")";
           
           $firebug->log($sql, "SQL");
           
           
	    // Run SQL command
	    if ($sql)
	    {
		// Run SQL 
		pg_send_query($dbconn, $sql);
		$result = pg_get_result($dbconn);
		if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
		{
		    $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
		    switch($PHPErrorCode)
		    {
		    case 23505:
			    // Unique constraint error
			    $firebug->log("This tag already exists.  Grabbing existing ID and continuing");
			    $readsql = "SELECT tagid FROM tbltag WHERE tag=".dbHelper::tellervo_pg_escape_string($this->getTagText());
			    if($this->getOwnerID()==NULL)
			    {
				$readsql.=" AND ownerid IS NULL";
			    }
			    else
			    {
				$readsql.=" AND ownerid=".dbHelper::tellervo_pg_escape_string($this->getOwnerID());
			    }
			    
			    
			    $firebug->log($readsql, "Read SQL");
			    $dbconnstatus = pg_connection_status($dbconn);
			    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
			    {
				$result = pg_query($dbconn, $readsql);

				if(pg_num_rows($result)>0)
				{
				    while ($row = pg_fetch_array($result))
				    {
					$this->setID($row['tagid']);
				    }
				}            
			    }
			    else
			    {
				// Connection bad
				$this->setErrorMessage("001", "Error connecting to database");
				return FALSE;
			    }
			    
			    
			    break;
		    default:
			    // Any other error
			    $this->setErrorMessage("002", "Error code: ".$PHPErrorCode."  -  ".pg_result_error($result)."--- SQL was $sql");
			    return FALSE;
		    }
		   
		}
	    }
	    
	    $this->assignToSeries();
           
	}
	else if ($crudMode=="update")
	{
	
	
	}
	else if ($crudMode=="assign")
	{
	    $this->assignToSeries();
	}
	else if ($crudMode=="unassign")
	{
	   $this->unassignFromSeries();
	 
	}
	else
	{
	    $this->setErrorMessage("667", "Invalid mode specified in writeToDB().  Only create, update, assign and unassign are supported");
	    return FALSE;
	
	}
			

	
	return TRUE;
    }
    
    function assignToSeries()
    {
        global $dbconn;
    	global $firebug;
    	
    	$firebug->log("assignToSeries called");
    	
	foreach($this->entityIdArray as $idtoassign)
	{
	      $sql = "INSERT INTO tblvmeasurementtotag (tagid, vmeasurementid) VALUES ('".$this->getID()."', '".$idtoassign."')";
	      	
	      	$firebug->log($sql, "Tag assign SQL");
	      	
	      	// Run SQL 
		pg_send_query($dbconn, $sql);
		$result = pg_get_result($dbconn);
		if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
		{
		    $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
		    switch($PHPErrorCode)
		    {
		     case 23505:
			    // Unique constraint error
			    $firebug->log("This tag is already associated with this series.");   
			    
			    break;
		    
		    default:
			    // Any other error
			    $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
		    }
		    return FALSE;
		}
	  
	}
	
	$this->setChildParamsFromDB();
    
    }
    
    function unassignFromSeries()
    {
    
        global $dbconn;
    	global $firebug;
    	
    	$firebug->log("unassignFromSeries called");
    
     foreach($this->entityIdArray as $idtoassign)
	  {
		$sql = "DELETE FROM tblvmeasurementtotag WHERE tagid ='".$this->getID()."' AND vmeasurementid='".$idtoassign."')";
		  
		  $firebug->log($sql, "Tag unassign SQL");
		  
		  // Run SQL 
		  pg_send_query($dbconn, $sql);
		  $result = pg_get_result($dbconn);
		  if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
		  {
		      $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
		      switch($PHPErrorCode)
		      {
		      
		      default:
			      // Any other error
			      $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
		      }
		      return FALSE;
		  }
	    
	  }
    
      $this->setChildParamsFromDB();
      
      
      
    }
    

    function deleteFromDB()
    {
    
        global $dbconn;
    	global $firebug;
	$sql = "DELETE FROM tbltag WHERE tagid='".$this->getID()."'";
	      	
	      	$firebug->log($sql, "Tag delete SQL");
	      	
	      	// Run SQL 
		pg_send_query($dbconn, $sql);
		$result = pg_get_result($dbconn);
		if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
		{
		    $PHPErrorCode = pg_result_error_field($result, PGSQL_DIAG_SQLSTATE);
		    switch($PHPErrorCode)
		    {
		     		    
		    default:
			    // Any other error
			    $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
		    }
		    return FALSE;
		}
		
	return TRUE;
    }

    function mergeRecords($mergeWithID)
    {

    }
    
// End of Class
} 
?>
