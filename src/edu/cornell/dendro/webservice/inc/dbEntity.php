<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

/**
 * Base class for entities that are representations of database tables
 *
 */
class dbEntity 
{
	/**
	 * Unique identifier for this entity
	 *
	 * @var String
	 */
    protected $id = NULL;	
    
    /**
     * Domain from which this entities identifier was issued
     *
     * @var String
     */
    protected $identifierDomain = NULL;
    
    /**
     * More information about the entity
     *
     * @var String
     */
    protected $description = NULL;
    
    /**
     * Name of this entity
     *
     * @var unknown_type
     */
    protected $name = NULL;    
    
    /**
     * XML tag for this entities parent entity
     *
     * @var String
     */
	protected $parentXMLTag = NULL; 
	
	/**
	 * The last error associated with this entity
	 *
	 * @var String
	 */
    protected $lastErrorMessage = NULL;
    
    /**
     * The last error code associated with this entity
     *
     * @var Integer
     */
    protected $lastErrorCode = NULL;
    
    /**
     * When this entity was created
     *
     * @var Timestamp
     */
    protected $createdTimeStamp = NULL;
    
    /**
     * When this entity was last modified
     *
     * @var Timestamp
     */
    protected $lastModifiedTimeStamp = NULL;
    
    /**
     * Whether the permissions should also be included in the output
     * 
     * @var Boolean
     */
    protected $includePermissions = FALSE;
    
    /**
     * Whether the user can create children of this entity
     *
     * @var Boolean
     */
    protected $canCreate = NULL;
    
    /**
     * Whether the user can update this entity
     *
     * @var Boolean
     */
    protected $canUpdate = NULL;
    
    /**
     * Whether the user can delete this entity
     *
     * @var Boolean
     */
    protected $canDelete = NULL;
    
    
    
    
    /**
     * Constructor for this entity
     *
     * @param String $parentXMLTag
     */
    protected function __construct($parentXMLTag)
    {
    	$this->setParentXMLTag($parentXMLTag);
    }

    
    protected function __destruct()
    {
    }    
    
     /**********/
    /* SETTERS */
    /***********/   
    
	/**
     * Set the error message
     *
     * @param Integer $theCode
     * @param String $theMessage
     */
    protected function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    /**
     * Set the text used in the XML for the parent of the inheriting entity
     *
     * @param String $theTag
     */
    private function setParentXMLTag($theTag)
    {
		$this->parentXMLTag = $theTag;
    }
    
    /**
     * Set the identifier and from what domain it came
     *
     * @param String $identifier
     * @param String $domain
     */
    private function setID($identifier, $domain)
    {
    	$this->id = $identifier;
    	$this->identifierDomain = $domain;	
    }
    
	/**
	 * Set the timestamp for when this entity was created
	 *
	 * @param Timestamp $timestamp
	 */
    private function setCreatedTimestamp($timestamp)
    {
    	$this->createdTimeStamp=$timestamp;
    }
    
    /**
     * Set the timestamp for when this entity was last modified
     *
     * @param Timestamp $timestamp
     */
    private function setLastModifiedTimestamp($timestamp)
    {
    	$this->lastModifiedTimeStamp=$timestamp;
    }
    
    
    /***********/
    /* GETTERS */
    /***********/    
    
    /**
     * Get the open parent XML tag 
     *
     * @return String
     */
    protected function getParentTagBegin()
    {
        // Return a string containing the start XML tag for the current object's parent
        $xml = "<".$this->parentXMLTag." lastModified='".getLastUpdateDate("tblsample")."'>";
        return $xml;
    }

    /**
     * Get the end parent XML tag
     *
     * @return String
     */
    protected function getParentTagEnd()
    {
        // Return a string containing the end XML tag for the current object's parent
        $xml = "</".$this->parentXMLTag.">";
        return $xml;
    }

    /**
     * Get the ID number for this database entity
     *
     * @return Integer
     */
    function getID()
    {
        return $this->id;
    }
    
    /**
     * Get the most recent error code for this database entity
     *
     * @return Integer
     */
    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }

    /**
     * Get the most recent error message for this database entity
     *
     * @return String
     */
    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
    }    
    
    /**
     * Retrieve the relevant permissions for this class from the database 
     *
     * @param Integer $securityUserID
     * @return Boolean
     */
    function getPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, get_class($this), $this->id)";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            $row = pg_fetch_array($result);
            
            $this->canCreate = fromPGtoPHPBool($row['cancreate']);
            $this->canUpdate = fromPGtoPHPBool($row['canupdate']);
            $this->canDelete = fromPGtoPHPBool($row['candelete']);
            $this->includePermissions = TRUE;
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }
        return TRUE; 
    }
    
    /**
     * Stub for outputting this entity to XML.  This should be overloaded
     *
	 * @param String $format one of standard, comprehensive, summary, or minimal. Defaults to 'standard'
	 * @param String $parts one of all, beginning or end. Defaults to 'all'
	 * @return Boolean
     */
    function asXML($format='standard', $parts='all')
    {
    	$this->setErrorMessage("667", "The asXML() function should have been overloaded but hasn't been");
    	return false;
    }
    
    /**
     * Stub for writing this entity to the database.  This should be overloaded
     *
     * @return Boolean
     */
     function writeToDB()
    {
     	$this->setErrorMessage("667", "The writeToDB() function should have been overloaded but hasn't been");
    	return false;   	
    }
    
     /**
     * Stub for deleting this entity from the database.  This should be overloaded
     *
     * @return Boolean
     */
    function deleteFromDB()
    {
     	$this->setErrorMessage("667", "The deleteFromDB() function should have been overloaded but hasn't been");
    	return false;     	
    }
}
?>
