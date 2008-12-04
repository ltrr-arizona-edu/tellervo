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
    protected $id = NULL;	
	protected $parentXMLTag = NULL; 
    protected $lastErrorMessage = NULL;
    protected $lastErrorCode = NULL;

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