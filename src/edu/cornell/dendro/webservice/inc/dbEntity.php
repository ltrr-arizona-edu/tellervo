<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('dbsetup.php');
require_once('geometry.php');

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
	protected $groupXMLTag = NULL; 
	
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
     * @param String $groupXMLTag
     */
    protected function __construct($groupXMLTag)
    {
    	$this->setgroupXMLTag($groupXMLTag);
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
    private function setgroupXMLTag($theTag)
    {
		$this->groupXMLTag = addslashes($theTag);
    }
    
    /**
     * Set the identifier and from what domain it came
     *
     * @param String $identifier
     * @param String $domain
     */
    private function setID($identifier, $domain)
    {
    	$this->id = addslashes($identifier);
    	$this->identifierDomain = addslashes($domain);	
    }
    

    /**
     * Set the current entities name.
     *
     * @param String $theName
     */
    function setName($theName)
    {
        $this->name=addslashes($theName);
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
        $xml = "<".$this->groupXMLTag." lastModified='".getLastUpdateDate("tblsample")."'>";
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
        $xml = "</".$this->groupXMLTag.">";
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

    function getLatitudeFromGeometry($geometry)
    {
    	global $dbconn;
    	
    	
    }
}

class objectEntity extends dbEntity
{
	/**
	 * Functional description: building (church, house etc) water well, painting,
	 * musical instrucment, ship, type of forest
	 *
	 * @var String
	 */
	protected $type = NULL;
	
	/**
	 * More elaborate description of the object itself
	 *
	 * @var String
	 */
	protected $description = NULL;
	
	/**
	 * Name, place of the workshop/wharf
	 *
	 * @var String
	 */
	protected $creator = NULL;
	
	/**
	 * Owner of the object
	 *
	 * @var String
	 */
	protected $owner = NULL;
	
	/**
	 * URL's of associated files
	 *
	 * @var String
	 */
	protected $file = NULL;
	
	/**
	 * Geometry object representing the location 
	 *
	 * @var Geometry
	 */
	protected $location = NULL;
	
    function __construct()
    {  
    	$this->location = new geometry();
        $groupXMLTag = "objects";
        parent::__construct($groupXMLTag);  	
	}

	/***********/
    /* SETTERS */
    /***********/   	
	
	/**
	 * Set the type of object
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setType($value)
	{
		
		$this->type = addslashes($value);
		return true;
	}
	
	/**
	 * Set the description of the object
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setDescription($value)
	{
		$this->description = addslashes($value);
		return true;
	}
	
	/**
	 * Set the name of the creator - name, place of the workshop/wharf etc
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setCreator($value)
	{
		$this->creator = addslashes($value);
		return true;
	}
	
	/**
	 * Set the name of the owner of this object
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setOwner($value)
	{	
		$this->owner = addslashes($value);
		return true;
	}
	
	/**
	 * Set the url of an associated file
	 *
	 * @param String $value
	 * @return true;
	 */
	function setFile($value)
	{
		$this->file = addslashes($value);
		return true;
	}

	function setLocationFromGeometry($geometry)
	{
		if(!(isset($this->location)))
		{
			$this->location = new geometry;	
		}
		
		$this->location->setGeometry($geometry);

	}



	/***********/
    /* GETTERS */
    /***********/ 	

	function getType()
	{
		return $this->type;
	}
	
	function getDescription()
	{
		return $this->description;
	}
	
	function getCreator()
	{
		return $this->creator;
	}
	
	function getOwner()
	{	
		return $this->owner;
	}
	
	function getFile()
	{
		return $this->file;
	}


	
}


class elementEntity extends dbEntity
{
	protected $taxonID = NULL;
	protected $authenticity = NULL;
	protected $shape = NULL;
	protected $diameter = NULL;
	protected $height = NULL;
	protected $width = NULL;
	protected $depth = NULL;
	protected $type = NULL;
	protected $file = NULL;
	/**
	 * One of growth; utilised (static); utilised (mobile); current, manufacture
	 *
	 * @var String
	 */
	protected $locationType = NULL;
	
	/**
	 * Precision of the location information in metres
	 *
	 * @var Integer
	 */
	protected $locationPrecision = NULL;
	
	/**
	 * Additional information and the location 
	 *
	 * @var String
	 */
	protected $locationComment = NULL;
	protected $processing = NULL;
	protected $marks = NULL;
	protected $description = NULL;
	
	
    function __construct()
    {  
        $groupXMLTag = "elements";
        parent::__construct($groupXMLTag);  	
	}

	/***********/
    /* SETTERS */
    /***********/ 	
	
	function setTaxonID($value)
	{
		$this->taxonID = (int) $value;
	}
	
	function setAuthenticity($value)
	{
		$this->authenticity = addslashes($value);
	}

	function setShape($value)
	{
		$this->shape = addslashes($value);
	}
	
	function setDiameter($value)
	{
		$this->diameter = (double) $diameter;
	}
	
	function setDimensions($height, $width, $depth)
	{
		$this->height = (double) $height;
		$this->width = (double) $width;
		$this->depth = (double) $depth;
	}
	
	function setType($value)
	{
		$this->type = addslashes($value);
	}
	
	function setFile($value)
	{
		$this->file = addslashes($file);
	}
		
	function setLocationType($value)
	{
		$this->locationType = addslashes($value);
	}
	
	function setLocationPrecision($value)
	{	
		$this->locationPrecision = (int) $value;
	}
	
	function setLocationComment($value)
	{
		$this->locationComment = addslashes($value);
	}
	
	function setProcessing($value)
	{
		$this->processing = addslashes($value); 
	}
	
	function setMarks($value)
	{
		$this->marks = addslashes($value);
	}
	
	function setDescription($value)
	{
		$this->description = addslashes($value);
	}

	/***********/
    /* GETTERS */
    /***********/ 	

	/**
	 * Get the Taxon
	 *
	 * @return unknown
	 */
	function getTaxonID()
	{
		return $this->taxonID;
	}
	
	function getAuthenticity()
	{
		return $this->authenticity;
	}

	function getShape()
	{
		return $this->shape;
	}
	
	function getDiameter()
	{
		return $this->diameter;
	}
	
	/**
	 * Get the height, width, depth or diameter of the element
	 *
	 * @param String $dimension
	 * @return Double
	 */
	function getDimension($dimension)
	{
		switch($dimension)
		{
			case "height":
				return $this->height;
			case "width":
				return $this->width;
			case "depth":
				return $this->depth;
			case "diameter":
				return $this->getDiameter();
			default:
				return false;
		}
	}
	
	function getType()
	{
		return $this->type;
	}
	
	function getFile()
	{
		return $this->file;
	}
		
	function getLocationType()
	{
		return $this->locationType;
	}
	
	function getLocationPrecision()
	{	
		return $this->locationPrecision;
	}
	
	function getLocationComment()
	{
		return $this->locationComment;
	}
	
	function getProcessing()
	{
		return $this->processing; 
	}
	
	function getMarks()
	{
		return $this->marks;
	}
	
	function getDescription()
	{
		return $this->description = addslashes();
	}	
	
}



class sampleEntity extends dbEntity
{
    function __construct()
    {  
        $groupXMLTag = "samples";
        parent::__construct($groupXMLTag);  	
	}	
	
}

class radiusEntity extends dbEntity
{
    protected $sampleID = NULL;   
    protected $measurementArray = array();
    /**
     * Whether pith is present or absent 
     *
     * @var Boolean
     */
    protected $pithPresent = NULL;
    
    /**
     * One of n/a; absent; complete; incomplete
     *
     * @var String
     */
    protected $sapwood = NULL;
    
    /**
     * Bark present or absent
     *
     * @var Boolean
     */
    protected $barkPresent = NULL;
    
    /**
     * Number of observed sapwood rings
     *
     * @var Integer
     */
    protected $numberOfSapwoodRings = NULL;
    
    /**
     * Information about the last rings under the bark
     *
     * @var String
     */
    protected $lastRingUnderBark = NULL;
    
    /**
     * Estimated number of missing heartwood rings
     *
     * @var Integer
     */
    protected $missingHeartwoodRingsToPith = NULL;
    
    /**
     * Foundation for missing heartwood rings estimate
     *
     * @var String
     */
    protected $missingHeartwoodRingsToPithFoundation = NULL;
    
    /**
     * Estimated number of missing sapwood rings
     *
     * @var Integer
     */
    protected $missingSapwoodRingsToBark = NULL;
    
    /**
     * Foundation for missing sapwood rings estimate
     *
     * @var unknown_type
     */
    protected $missingSapwoodRingsToBarkFoundation = NULL;

    function __construct()
    {
        $groupXMLTag = "radii";
        parent::__construct($groupXMLTag);  	
	}

	/***********/
    /* SETTERS */
    /***********/   
	
	/**
	 * Set whether the pith is present or not
	 *
	 * @param Boolean $value
	 * 
	 */
	function setPithPresent($value)
	{
		if(formatBool($value)=='error')
		{
			$this->setErrorMessage(901, 'Pith field data type not recognised');	
			return FALSE;
		}
		else
		{
			$this->pithPresent = $value;
			return TRUE;		
		}
	}
	
	/**
	 * Set the sample ID 
	 *
	 * @param Integer $value
	 * @return Boolean 
	 */
	function setSampleID($value)
	{
		$this->sampleID = (int) $value;
		return TRUE;
	}
	
	/**
	 * Set whether the sapwood is absent, complete, incomplete or n/a
	 *
	 * @param unknown_type $value
	 */
	function setSapwood($value)
	{
		if( (strtolower($value)!='n/a') || (strtolower($value)!='absent') || (strtolower($value)!='complete') ||(strtolower($value)!='incomplete') )
		{
			$this->sapwood = strtolower($value);
			return TRUE;
		}
		else
		{
			$this->setErrorMessage(901, 'Sapwood field must be one of n/a; absent; complete; incomplete');
			return FALSE;
		}
	}
	
	function setBarkPresent($value)
	{
		$this->barkPresent = $value;
	}
	
	function setNumberOfSapwoodRings($value)
	{
		$this->numberOfSapwoodRings = $value;
	}
	
	function setLastRingUnderBark($value)
	{
		$this->lastRingUnderBark = $value;
	}
	
	function setMissingHeartwoodRingsToPith($value)
	{
		$this->missingHeartwoodRingsToPith = $value;
	}

	function setMissingHeartwoodRingsToPithFoundation($value)
	{
		$this->missingHeartwoodRingsToPithFoundation = $value;
	}
	
	function setMissingSapwoodRingsToPith($value)
	{
		$this->missingSapwoodRingsToPith = $value;
	}
	
	function setMissingSapwoodRingsToPithFoundation($value)
	{
		$this->missingSapwoodRingsToPithFoundation = $value;
	}
	
     /**********/
    /* GETTERS */
    /***********/   	

	function getPithPresent()
	{
		return $this->pithPresent;		
	}
	
	function getSampleID()
	{
		return $this->sampleID;
	}
	
	function getSapwood()
	{
		return $this->sapwood;
	}
	
	function getBarkPresent()
	{
		return $this->barkPresent;
	}
	
	function getNumberOfSapwoodRings()
	{
		return $this->numberOfSapwoodRings;
	}
	
	function getLastRingUnderBark()
	{
		return $this->lastRingUnderBark;
	}
	
	function getMissingHeartwoodRingsToPith()
	{
		return $this->missingHeartwoodRingsToPith;
	}

	function getMissingHeartwoodRingsToPithFoundation()
	{
		return $this->missingHeartwoodRingsToPithFoundation;
	}
	
	function getMissingSapwoodRingsToPith()
	{
		return $this->missingSapwoodRingsToPith;
	}
	
	function getMissingSapwoodRingsToPithFoundation()
	{
		return $this->missingSapwoodRingsToPithFoundation;
	}	
	
}





?>