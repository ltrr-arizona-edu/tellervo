<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */
require_once('dbhelper.php');


class object extends objectEntity implements IDBAccessor
{
                   
    /***************/
    /* CONSTRUCTOR */
    /***************/
    

    public function __construct()
    {
        $groupXMLTag = "object";
    	parent::__construct($groupXMLTag);
    }

    public function __destruct()
    {

    }
    
    
    /***********/
    /* SETTERS */
    /***********/
    
    
	/**
	 * Set this object's parameters from the database
	 *
	 * @param Integer $theID
	 */
    function setParamsFromDB($theID)
	{
       global $dbconn;
        
        $this->setID($theID);
        $sql = "select * from tblobject where objectid='".$this->getID()."'";

        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                trigger_error("903"."No records match the specified id. $sql", E_USER_ERROR);
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->setDescription($row['description']);
                $this->setTitle($row['title']);
                $this->setCreator($row['creator']);
                $this->setOwner($row['owner']);
                $this->setFile($row['file']);
                $this->setCoverageTemporal($row['coverageTemporal'], $row['coverageTemporalFoundation']);
                $this->location->setGeometry($row['locationgeometry'], $row['locationtype'], $row['locationprecision'], $row['locationcomment']);
               
            }

        }
        else
        {
            // Connection bad
            trigger_error("001"."Error connecting to database", E_USER_ERROR);
            return FALSE;
        }

        return TRUE;		
	}

    /**
     * Add the id's of the current object's direct children from the database
     *
     * @return Boolean
     */
    function setChildParamsFromDB()	
    {
    	
    }
	
    /**
     * Set the current object's parameters from a paramsClass object
     *
     * @param Parameter Class $paramsClass
     * @return Boolean
     */
    function setParamsFromParamsClass($paramsClass)
    {
    	
    }


    /***********/
    /* SETTERS */
    /***********/
    
    /**
     * Get an XML representation of this object
     *
     * @param String $format
     * @param String $parts
     * @return Boolean
     */
	function asXML($format='standard', $parts='all')
	{
		return $this->_asXML();
	}
	
	private function _asXML($format='standard', $parts='all')
	{
        $xml = NULL;

        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()!=NULL)
        {
            if(($parts=="all") || ($parts=="beginning"))
            { 
            	$xml.= "<tridas:object>";
                $xml.= $this->getIdentifierXML();     
                if($this->getType()!=NULL)     		$xml.= "<tridas:type>".$this->getType()."</tridas:type>";        	
            	if($this->getDescription()!=NULL)	$xml.= "<tridas:description>".$this->getDescription()."</tridas:description>";
            	if($this->getTitle()!=NULL)			$xml.= "<tridas:title>".$this->getTitle()."</tridas:title>";
            	if($this->getCreator()!=NULL)		$xml.= "<tridas:creator>".$this->getCreator()."</tridas:creator>";
            	if($this->getOwner()!=NULL)			$xml.= "<tridas:owner>".$this->getOwner()."</tridas:owner>";
            	if($this->getFile()!=NULL)			$xml.= "<tridas:file xlink:href=\"".$this->getFile()."\" />";
            	if($this->getTemporalCoverage()!=NULL)
            	{
            		$xml .="<tridas:coverage>";
            		$xml .="<tridas:coverageTemporal>".$this->getTemporalCoverage()."</tridas:coverageTemporal>";
            		$xml .="<tridas:coverageTemporalFoundation>".$this->getTemporalCoverageFoundation()."</tridas:coverageTemporalFoundation>";
            		$xml .="</tridas:coverage>";
            	}
            	if($this->location->asGML()!=NULL) $xml.= $this->location->asGML();
            }  

	    
        	if(($parts=="all") || ($parts=="end"))
            {
                // End XML tag
                $xml.= "</tridas:object>\n";
            }

            return $xml;
        }
        else
        {
            return FALSE;
        }        
        
        
	}
	
    /*************/
    /* FUNCTIONS */
    /*************/	
	
	
	function writeToDB()
	{
		
	}
	
	function deleteFromDB()
	{
		
	}
	
	function validateRequestParams($paramsClass, $crudMode)
	{
		
	}
	
	
}
?>