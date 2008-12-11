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
    
}
?>