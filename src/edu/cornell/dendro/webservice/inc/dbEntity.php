<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This file contains the interface and classes that store data 
 * representing the various data entities in the data model.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('dbsetup.php');
require_once('geometry.php');
require_once('taxon.php');

/**
 * Interface for classes that inherit dbEntity and read/write to/from the database
 *
 */
interface IDBAccessor
{
	function asXML();
	function writeToDB();
	function deleteFromDB();
	function validateRequestParams($paramsClass, $crudMode);
}


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
    private $id = NULL;	
    
    /**
     * Domain from which this entities identifier was issued
     *
     * @var String
     */
    private $identifierDomain = NULL;
    
    /**
     * More information about the entity
     *
     * @var String
     */
    private $description = NULL;
    
    /**
     * Name of this entity
     *
     * @var unknown_type
     */
    private $name = NULL;    
    
    /**
     * XML tag for this entities parent entity
     *
     * @var String
     */
	private $groupXMLTag = NULL; 
	
	/**
	 * The last error associated with this entity
	 *
	 * @var String
	 */
    private $lastErrorMessage = NULL;
    
    /**
     * The last error code associated with this entity
     *
     * @var Integer
     */
    private $lastErrorCode = NULL;
    
    /**
     * When this entity was created
     *
     * @var Timestamp
     */
    private $createdTimeStamp = NULL;
    
    /**
     * When this entity was last modified
     *
     * @var Timestamp
     */
    private $lastModifiedTimeStamp = NULL;
    
    /**
     * Whether the permissions should also be included in the output
     * 
     * @var Boolean
     */
    private $includePermissions = FALSE;
    
    /**
     * Whether the user can create children of this entity
     *
     * @var Boolean
     */
    private $canCreate = NULL;
    
    /**
     * Whether the user can update this entity
     *
     * @var Boolean
     */
    private $canUpdate = NULL;
    
    /**
     * Whether the user can delete this entity
     *
     * @var Boolean
     */
    private $canDelete = NULL;

    
    /**
     * Constructor for this entity
     *
     * @param String $groupXMLTag
     */
    public function __construct($groupXMLTag)
    {
    	$this->setgroupXMLTag($groupXMLTag);
    }

    
    public function __destruct()
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
     * @return Boolean 
     */
    protected function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
        
        // Trigger the error
        trigger_error($theCode.$theMessage, E_USER_ERROR);
        
        return true;
    }

    /**
     * Set the text used in the XML for the parent of the inheriting entity
     *
     * @param String $theTag
     * @return Boolean
     */
    protected function setgroupXMLTag($theTag)
    {
		$this->groupXMLTag = addslashes($theTag);
		return true;
    }

    
    /**
     * Set the identifier and from what domain it came
     *
     * @param String $identifier
     * @param String $domain
     * @return Boolean
     */
    protected function setID($identifier, $domain=NULL)
    {
    	$this->id = $identifier;
    	$this->identifierDomain = addslashes($domain);	
    	return true;
    }
    

    /**
     * Set the current entities name.
     *
     * @param String $theName
     * @return Boolean
     */
    protected function setName($theName)
    {
        $this->name=addslashes($theName);
        return true;
    }
    
	/**
	 * Set the timestamp for when this entity was created
	 *
	 * @param Timestamp $timestamp
	 * @return Boolean
	 */
    protected function setCreatedTimestamp($timestamp)
    {
    	$this->createdTimeStamp=$timestamp;
    	return true;
    }
    
    /**
     * Set the timestamp for when this entity was last modified
     *
     * @param Timestamp $timestamp
     * @return Boolean
     */
    protected function setLastModifiedTimestamp($timestamp)
    {
    	$this->lastModifiedTimeStamp=$timestamp;
    	return true;
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
     * Get the name for this database entity
     *
     * @return String
     */
    protected function getName()
    {
    	if(isset($this->name))
    	{
    		return $this->name;
    	}
    	else
    	{
    		return NULL;
    	}
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
     * Get the XML representation of the identifier
     *
     * @return unknown
     */
    function getIdentifierXML()
    {
    	global $domain;
        return "<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier >";  
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
     * Should permissions be included?
     *
     * @return Boolean
     */
    function getIncludePermissions()
    {   	
    	return $this->includePermissions;
    }
    
    /**
     * Get the XML representation for the permissions if requested
     *
     * @return String
     */
    function getPermissionsXML()
    {

    	$xml = NULL;
     	// Include permissions details if requested
        if($this->getIncludePermissions()===TRUE) 
        {
            $xml.= "<tridas:genericField name=\"canCreate\" type=\"boolean\">".fromPHPtoStringBool($this->getPermission("Create"))."</tridas:genericField ";
            $xml.= "<tridas:genericField name=\"canUpdate\" type=\"boolean\">".fromPHPtoStringBool($this->getPermission("Update"))."</tridas:genericField ";
            $xml.= "<tridas:genericField name=\"canDelete\" type=\"boolean\">".fromPHPtoStringBool($this->getPermission("Delete"))."</tridas:genericField ";
        } 

        return $xml;
    	
    }
    
    /**
     * Retrieve the relevant permissions for this class from the database 
     *
     * @param Integer $securityUserID
     * @return Boolean
     */
    function lookupPermissions($securityUserID)
    {
        global $dbconn;

        $sql = "select * from cpgdb.getuserpermissionset($securityUserID, $this->getEntityType(), $this->getID())";
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
    
    function hasPermission($securityUserID, $crudMode)
    {
    	
    }
    
    /**
     * Get the type of class that this entity is
     *
     * @return String
     */
    function getEntityType()
    {
        return get_class($this);
    }
    
    /**
     * Get the timestamp for when this entity was created
     *
     * @return ISODate
     */
    protected function getCreatedTimestamp()
    {
    	return $this->createdTimeStamp;
    }
    
    /**
     * Get the timestamp for when this entity was last modified
     *
     * @return ISODate
     */
    protected function getLastModifiedTimestamp()
    {
    	return $this->lastModifiedTimeStamp;
    }
    
    /**
     * Find out whether the user has permission to create, delete or update this entity
     *
     * @param String $type one of create, delete or update
     * @return Boolean
     */
    protected function getPermission($type)
    {
    	switch(strtolower($type))
    	{
    		case "create":
    			return $this->canCreate;
    		case "delete":
    			return $this->canDelete;
    		case "update":
    			return $this->canUpdate;
    		default:
    			return false;		
    	}
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
	
	/**
	 * Description of this object
	 *
	 * @var String
	 */
	protected $description = NULL;
	
	/**
	 * Title of this object
	 *
	 * @var String
	 */
	protected $title = NULL;
	
	/**
	 * Broad historical period this object covers
	 *
	 * @var String
	 */
	protected $coverageTemporal = NULL;
	
	/**
	 * Foundation for the broad historical date of this object
	 *
	 * @var unknown_type
	 */
	protected $coverageTemporalFoundation = NULL;
		
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
	 * Set this object's description
	 *
	 * @param String $value
	 */
	function setDescription($value)
	{
		$this->description = addslashes($value);
	}
	
	/**
	 * Set the title of this object
	 *
	 * @param String $value
	 */
	function setTitle($value)
	{
		$this->title = addslashes($value);
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
	
	/**
	 * Set the broad historical period of the object and the foundation for thinking that
	 *
	 * @param String $period
	 * @param String $foundation
	 */
	function setCoverageTemporal($period, $foundation)
	{
		$this->coverageTemporal = addslashes($period);
		$this->coverageTemporalFoundation = addslashes($foundation);	
	}
	
	
	

	/***********/
    /* GETTERS */
    /***********/ 	

	/**
	 * Get the type of object 
	 *
	 * @return String
	 */
	function getType()
	{
		return $this->type;
	}
	
	/**
	 * Get the description of this object
	 *
	 * @return String
	 */
	function getDescription()
	{
		return $this->description;
	}
	
	/**
	 * Get this object's title
	 *
	 * @return unknown
	 */
	function getTitle()
	{
		return $this->title;
	}
	
	/**
	 * Get the name of the creator of this object
	 *
	 * @return String
	 */
	function getCreator()
	{
		return $this->creator;
	}
	
	/**
	 * Get the name of the owner of this object
	 *
	 * @return String
	 */
	function getOwner()
	{	
		return $this->owner;
	}
	
	/**
	 * Get the URL of the associated file of this object
	 *
	 * @return unknown
	 */
	function getFile()
	{
		return $this->file;
	}

	/**
	 * Get the broad historical period this object covers
	 *
	 * @return String
	 */
	function getTemporalCoverage()
	{
		return $this->temporalCoverage;
	}
	
	/**
	 * Get the foundation for the broad historical periods this object covers
	 *
	 * @return String
	 */
	function getTemporalCoverageFoundation()
	{
		return $this->temporalCoverageFoundation;
	}
	
}


class elementEntity extends dbEntity
{
	/**
	 * Taxonomic information about this element
	 *
	 * @var Integer
	 */
	protected $taxon = NULL;
	
	/**
	 * The name that this element was originally identified as 
	 *
	 * @var String
	 */
	protected $originalTaxon = NULL;
	
	/**
	 * Whether this element is original, a repair, or later addition
	 *
	 * @var String
	 */
	protected $authenticity = NULL;
	/**
	 * Shape of this element
	 *
	 * @var String
	 */
	protected $shape = NULL;
	/**
	 * Diameter of this element
	 *
	 * @var Double
	 */
	protected $diameter = NULL;
	/**
	 * Height of this element
	 *
	 * @var Double
	 */
	protected $height = NULL;
	/**
	 * Width of this element
	 *
	 * @var Double
	 */
	protected $width = NULL;
	/**
	 * Depth of this element here...
	 *
	 * @var Double
	 */
	protected $depth = NULL;
	/**
	 * Type of element
	 *
	 * @var String
	 */
	protected $type = NULL;
	/**
	 * Associate file URL
	 *
	 * @var String
	 */
	protected $file = NULL;
	/**
	 * Geometry object representing the location 
	 *
	 * @var Geometry
	 */
	protected $geometry = NULL;
	/**
	 * Processing (carved, sawn etc) rafting marks
	 *
	 * @var String
	 */
	protected $processing = NULL;
	/**
	 * Carpenter marks
	 *
	 * @var String
	 */
	protected $marks = NULL;
	
	/**
	 * Description of element
	 *
	 * @var String
	 */
	protected $description = NULL;
	
    function __construct()
    {  
        $groupXMLTag = "elements";
        parent::__construct($groupXMLTag); 
		$this->geometry = new geometry;	
		$this->taxon = new taxon; 	
	}

	/***********/
    /* SETTERS */
    /***********/ 	
	
	/**
	 * Set authenticity of element to original; repair; later addition
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setAuthenticity($value)
	{
		if( (strtolower($value)=='original') || (strtolower($value)=='repair') || (strtolower($value)=='later addition'))
		{
			$this->authenticity = strtolower($value);
			return true;
		}
                elseif($value==NULL)
                {
                        return false;
                }
		else
		{
                        trigger_error("901"."Authenticity must be one of 'original, 'repair' or 'later addtion'. You specified '".strtolower($value)."'", E_USER_ERROR);
			return false;
		}
	}

	/**
	 * Set the shape of this element
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setShape($value)
	{
		$this->shape = addslashes($value);
		return true;
	}
	
	/**
	 * Set the diameter of this element
	 *
	 * @param Double $value
	 * @return Boolean
	 */
	function setDiameter($diameter)
	{
		$this->diameter = (double) $diameter;
		return true;
	}
	
	/**
	 * Set the height of this element
	 *
	 * @param Double $height
	 * @return Boolean
	 */
	function setHeight($height)
	{
		$this->height = (double) $height;
		return true;
	}
	
	/**
	 * Set the dimensions of this element
	 *
	 * @param Double $height
	 * @param Double $width
	 * @param Double $depth
	 * @return Boolean
	 */
	function setDimensions($height, $width, $depth)
	{
		$this->height = (double) $height;
		$this->width = (double) $width;
		$this->depth = (double) $depth;
		return true;
	}
	
	/**
	 * Set the type of this element
	 *
	 * @param String $type
	 * @return Boolean
	 */
	function setType($type)
	{
		$this->type = addslashes($type);
		return true;
	}
	
	/**
	 * Set the associated file
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setFile($value)
	{
		$this->file = addslashes($file);
		return true;
	}

	/**
	 * Set the processing (carved, sawn etc) rafting marks
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setProcessing($value)
	{
		$this->processing = addslashes($value); 
		return true;
	}
	
	/**
	 * Set the carpenter marks and inscriptions
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setMarks($value)
	{
		$this->marks = addslashes($value);
		return true;
	}
	
	/**
	 * Set taxon object
	 *
	 * @todo Implement
	 * @param String $name
	 * @param String $normalisedName (optional) Accepted varified and correct name
	 * @param String $normalisedID (optional) ID code from standardised vocab
	 * @param String $dictionary (optional) Name of standardised vocab from which the AVC name comes
	 */
	function setTaxon($name, $normalisedName=NULL, $normalisedID=NULL, $dictionary=NULL)
	{
		// If one of the optional attributes is supplied, then all must be supplied
		if( (isset($normalisedName)) || (isset($normalisedID)) || (isset($dictionary)) )
		{
			if( (!isset($normalisedName)) && (!isset($normalisedID)) && (!isset($dictionary)) )
			{
				trigger_error("need more attribs", 901);		
			}
		}
		
		$this->setOriginalTaxon($name);
				
	}

	/**
	 * Set the original name by which this element was identified
	 *
	 * @param unknown_type $value
	 */
	function setOriginalTaxon($value)
	{
		$this->taxon->setOriginalTaxon(addslashes($value));
	}
	
	/**
	 * Set the taxon using the Corina db ID code
	 *
	 * @param Integer $id
	 */
	function setTaxonByID($id)
	{
		$this->taxon = new Taxon;
		$this->taxon->setParamsFromDB($id);
		
	}
	
	/**
	 * Attempt to set the taxon using a name string
	 *
	 * @todo Implement
	 * @param String $name
	 */
	function setTaxonByString($name)
	{
		
	}
	
	/**
	 * Set the taxon using the ITRDB taxon code
	 * 
	 * @todo Needs implementing
	 * @param String $code
	 */
	function setTaxonByITRDBCode($code)
	{
		
	}
	
	/**
	 * Set the description of this element
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
	 * Set the geometry field of this element using a GML string
	 *
	 * @param String $gml
	 */
	function setGeometryFromGML($gml)
	{
		$this->geometry->setGeometryFromGML($gml);
	}
	
	/**
	 * Set the geometry field of this element using lat/long values.  Only applicable to point geometries.
	 *
	 * @param Float $lat
	 * @param Float $long
	 */
	function setPointGeometryFromLatLong($lat, $long)
	{
		$this->geometry->setPointGeometryFromLatLong($lat, $long);
	}
	
	/***********/
    /* GETTERS */
    /***********/ 	
	
	/**
	 * Does this element have any dimensions associated with it?
	 *
	 * @return Boolean
	 */
	function hasDimensions()
	{
		if( ($this->height!=NULL) || ($this->width!=NULL) || ($this->depth!=NULL) || ($this->diameter!=NULL) )
		{
			return True;
		}
		else
		{
			return False;
		}
		
	}
	
	/**
	 * Get the authenticity of this element
	 *
	 * @return String
	 */
	function getAuthenticity()
	{
		return $this->authenticity;
	}

	/**
	 * Get the shape of this element
	 *
	 * @return String
	 */
	function getShape()
	{
		return $this->shape;
	}
	
	/**
	 * Get the diameter of this element
	 *
	 * @return Double
	 */
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
	
	/**
	 * Get the element type
	 *
	 * @return String
	 */
	function getType()
	{
		return $this->type;
	}
	
	/**
	 * Get the URL of the associated file
	 *
	 * @return String
	 */
	function getFile()
	{
		return $this->file;
	}
	
	/**
	 * Get the processing marks
	 *
	 * @return String
	 */
	function getProcessing()
	{
		return $this->processing; 
	}
	
	/**
	 * Get the carpenter marks
	 *
	 * @return String
	 */
	function getMarks()
	{
		return $this->marks;
	}
	
	/**
	 * Get this elements description
	 *
	 * @return String
	 */
	function getDescription()
	{
		return $this->description;
	}
	
	/**
	 * Get the original taxon name which was given to this element
	 *
	 * @return String
	 */
	function getOriginalTaxon()
	{
            return $this->taxon->getOriginalTaxon();
	}

        function hasGeometry()
        {
            return $this->geometry!=NULL;
        }
}



class sampleEntity extends dbEntity
{
	/**
	 * Type of sample 
	 *
	 * @var String
	 */
	private $type = NULL;
	/**
	 * Date the sample was taken
	 *
	 * @var ISODate
	 */
	private $samplingDate = NULL;
	/**
	 * Associated file URL
	 *
	 * @var String
	 */
	private $file = NULL;
	/**
	 * Position of the sample in the element
	 *
	 * @var String
	 */
	private $position = NULL;
	/**
	 * State of the material (dry, wet, conserved etc)
	 *
	 * @var String
	 */
	private $state = NULL;
	/**
	 * Presence of knots
	 *
	 * @var Boolean
	 */
	private $knots = NULL;
	/**
	 * Description of this sample
	 *
	 * @var String
	 */
	private $description = NULL;
	
    function __construct()
    {  
        $groupXMLTag = "samples";
        parent::__construct($groupXMLTag);  	
	}	
	
	/***********/
    /* SETTERS */
    /***********/ 

	/**
	 * Set the sample type
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
	 * Set the date that this sample was taken on
	 * @todo check that the passed date is valid ISO8601
	 * 
	 * @param ISODate $date
	 * @return Boolean
	 */
	function setSamplingDate($date)
	{
		$this->samplingDate=$date;
		return true;
	}
	
	/**
	 * Set the position that the sample was in the element
	 *
	 * @param String $position
	 * @return Boolean
	 */
	function setPosition($position)
	{
		$this->position = addslashes($position);
		return true;
	}
	
	/**
	 * Set the state of the sample (dry, wet, conserved etc)
	 *
	 * @param String $state
	 * @return Boolean
	 */
	function setState($state)
	{
		$this->state = addslashes($state);
		return true;	
	}
	
	/**
	 * Set whether there are knots present
	 *
	 * @param Boolean $value
	 * @return Boolean
	 */
	function setKnots($value)
	{
		if(formatBool($value)=='error')
		{
			$this->setErrorMessage(901, 'Knots field data type not recognised');	
			return FALSE;
		}
		else
		{
			$this->knots = $value;
			return TRUE;		
		}
	}
	
	/**
	 * Set the description of this sample
	 *
	 * @param String $value
	 */
	function setDescription($value)
	{
		$this->description = addslashes($value);
	}
	
	
	/***********/
    /* GETTERS */
    /***********/  	
	
	/**
	 * Get the type of sample
	 *
	 * @return String
	 */
	function getType()
	{
		return $this->type;	
	}
	
	/**
	 * Get the date the sample was taken
	 *
	 * @return ISODate
	 */
	function getSamplingDate()
	{
		return $this->samplingDate;	
	}
	
	/**
	 * Get the position of the sample in the element
	 *
	 * @return String
	 */
	function getPosition()
	{
		return $this->position;
	}
	
	/**
	 * Get the state the sample is in (wet, dry etc)
	 *
	 * @return String
	 */
	function getState()
	{
		return $this->state;
	}
	
	/**
	 * Does the sample have knots?
	 *
	 * @param String $format (php, pg, english)
	 * @return Boolean or String
	 */
	function getKnots($format="php")
	{
		return formatBool($this->knots, $format);
	}
	
	/**
	 * Get this sample's description
	 *
	 * @return String
	 */
	function getDescription()
	{
		return $this->description;
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
	
	/**
	 * Set whether the bark is present
	 *
	 * @param Boolean $value
	 * @retun Boolean
	 */
	function setBarkPresent($value)
	{
		$bark = formatBool($value);
		if($bark=='error')
		{
			return false;
		}
		else
		{
			$this->barkPresent = $bark;
			return true;
		}
	}
	
	/**
	 * Set the number of sapwood rings
	 *
	 * @param Integer $value
	 * @return Boolean
	 */
	function setNumberOfSapwoodRings($value)
	{
		if ( (gettype($value)=='integer') || (gettype($value)=='double') )
		{		
			$this->numberOfSapwoodRings = (int) $value;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Set the last ring under the bark information
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setLastRingUnderBark($value)
	{
		$this->lastRingUnderBark = $value;
		return true;
	}
	
	/**
	 * Set the number of missing heartwood rings to the pith
	 *
	 * @param Integer $value
	 * @return Boolean
	 */
	function setMissingHeartwoodRingsToPith($value)
	{
		if ( (gettype($value)=='integer') || (gettype($value)=='double') )
		{
			$this->missingHeartwoodRingsToPith = (int) $value;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Set the foundation on which the number of missing heartwood rings was estimated
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setMissingHeartwoodRingsToPithFoundation($value)
	{
		$this->missingHeartwoodRingsToPithFoundation = addslashes($value);
		return true;
	}
	
	/**
	 * Set the number of missing sapwood rings to the bark
	 *
	 * @param Integer $value
	 * @return Boolean
	 */
	function setMissingSapwoodRingsToBark($value)
	{
		if ( (gettype($value)=='integer') || (gettype($value)=='double') )
		{
			$this->missingSapwoodRingsToBark = (int) $value;
			return true;
		}
		else
		{
			return false;
		}	
	}
	
	/**
	 * Set the foundation on which the number of missing sapwood rings was estimated
	 *
	 * @param String $value
	 * @return Boolean
	 */	
	function setMissingSapwoodRingsToBarkFoundation($value)
	{
		$this->missingSapwoodRingsToBarkFoundation = addslashes($value);
		return true;
	}
	
     /**********/
    /* GETTERS */
    /***********/   	

	/**
	 * Get whether the pith is present
	 *
	 * @return Boolean
	 */
	function getPithPresent()
	{
		return $this->pithPresent;		
	}
	
	function getSampleID()
	{
		return $this->sampleID;
	}
	
	/**
	 * Get whether the sapwood is n/a, absent, complete or incomplete
	 *
	 * @return String
	 */
	function getSapwood()
	{
		return $this->sapwood;
	}
	
	/**
	 * Is the bark present?
	 *
	 * @return Boolean
	 */
	function getBarkPresent()
	{
		return $this->barkPresent;
	}
	
	/**
	 * Get the number of sapwood rings
	 *
	 * @return Integer
	 */
	function getNumberOfSapwoodRings()
	{
		return $this->numberOfSapwoodRings;
	}
	
	/**
	 * Get information about the last ring under the bark
	 *
	 * @return String
	 */
	function getLastRingUnderBark()
	{
		return $this->lastRingUnderBark;
	}
	
	/**
	 * The number of missing heartwood rings to the pith
	 *
	 * @return Integer
	 */
	function getMissingHeartwoodRingsToPith()
	{
		return $this->missingHeartwoodRingsToPith;
	}

	/**
	 * The foundation by which the number of missing heartwood rings was estimated
	 *
	 * @return String
	 */
	function getMissingHeartwoodRingsToPithFoundation()
	{
		return $this->missingHeartwoodRingsToPithFoundation;
	}
	
	/**
	 * The number of missing sapwood rings
	 *
	 * @return Integer
	 */
	function getMissingSapwoodRingsToBark()
	{
		return $this->missingSapwoodRingsToBark;
	}
	
	/**
	 * The foundation by which the number of missing sapwood ring was estimated
	 *
	 * @return unknown
	 */
	function getMissingSapwoodRingsToBarkFoundation()
	{
		return $this->missingSapwoodRingsToBarkFoundation;
	}	
	
}

class taxonEntity extends dbEntity
{
    protected $parentID = NULL;
    protected $label = NULL;
    protected $colID = NULL;
    protected $colParentID = NULL;
    protected $taxonRank = NULL;
    protected $parentXMLTag = "taxonDictionary"; 
    protected $lastErrorMessage = NULL;
    protected $lastErrorCode = NULL;
    protected $kingdom = NULL;
    protected $phylum = NULL;
    protected $class = NULL;
    protected $order = NULL;
    protected $family = NULL;
    protected $genus = NULL;
    protected $species = NULL;
    protected $originalTaxon = NULL;
	
    
    function __construct()
    {
    	
    }
    
    /**********/
    /* SETTERS */
    /***********/

    function setOriginalTaxon($value)
    {
    	$this->originalTaxon=$value;
    }
    
    function setLabel($theLabel)
    {
        // Set the current objects note.
        $this->label=$theLabel;
    }   
    function taxonRecordExists($colID)
    {
        global $dbconn;
        $sql="select count(*) from tlkptaxon where colid=$colID";
        
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);    
            while ($row = pg_fetch_array($result))
            {
                if($row['count']>0)
                {
                    return True;
                }
                else
                {   
                    return False;
                }
            }
        }
    }
    
    function setHigherTaxonomy()
    {
        global $dbconn;
        
        $sql = "select * from cpgdb.qrytaxonomy(".$this->id.")";
        //echo $sql;
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            pg_send_query($dbconn, $sql);
            $result = pg_get_result($dbconn);
            if(pg_num_rows($result)==0)
            {
                // No records match the id specified
                $this->setErrorMessage("903", "No records match the specified id");
                return FALSE;
            }
            else
            {
                // Set parameters from db
                $row = pg_fetch_array($result);
                $this->kingdom  = $row['kingdom'];
                $this->phylum   = $row['phylum'];
                $this->class    = $row['class'];
                $this->order    = $row['txorder'];
                $this->family   = $row['family'];
                $this->genus    = $row['genus'];
                $this->species  = $row['species'];
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }

        return TRUE;

    }
    


    function setParamsFromCoL($CoLID)
    {
        $doneStuff = false;
        $colURL="http://webservice.catalogueoflife.org/annual-checklist/2008/search.php?response=full&id=$CoLID";
        $colXML = simplexml_load_file($colURL);
        if($colXML['total_number_of_results']==1)
        {
            if($colXML->result->name_status=='accepted name')
            {
                // Write Higher taxon records
                $parentID = NULL;
                foreach ($colXML->result->classification->taxon as $currentTaxon)
                {
                    if(!($this->taxonRecordExists($currentTaxon->id)))
                    {
                        $this->setParamsWithParents($currentTaxon->id, $parentID, $currentTaxon->rank, $currentTaxon->name);
                        $this->writeToDB();
                        $doneStuff = true;
                    }
                    $parentID = $currentTaxon->id;
                }

                // Write requested taxon details
                if(!($this->taxonRecordExists($colXML->result->id)))
                {
                    if($colXML->result->rank=="Infraspecies")
                    {
                        // This needs to change
                        $rank = $colXML->result->infraspecies_marker;
                    }
                    else
                    {
                        $rank = $colXML->result->rank;
                    }
                    $this->setParamsWithParents($colXML->result->id, $parentID, $colXML->result->rank, $colXML->result->name." ".$colXML->result->author);
                    $this->writeToDB();
                    $doneStuff = true;
                }
            }
            else
            {
                $this->setErrorMessage("901", "The requested taxon is a synonym.  Only accepted taxa can be added.");
                return false;
            }
    
        }
        else
        {   
            // More than one taxon returned
            $this->setErrorMessage("901", "The requested taxon is not unique.");
            return false;
        }

        if ($doneStuff)
        {
            return true;
        }
        else
        {
            $this->setErrorMessage("906", "Record already exists.");
            return false;
        }

    }  
    /**********/
    /* GETTERS */
    /***********/     
    
    /**
     * Get the name by which this taxon was original identified
     *
     * @return String
     */
    function getOriginalTaxon()
    {
    	return $this->originalTaxon;
    }
    
    function getCoLID()
    {
    	return $this->colID;
    }

    function asXML($format='standard', $parts='all')
    {
    	global $taxonomicAuthorityEdition;
    	
        // Return a string containing the current object in XML format
        if ($this->getLastErrorCode()==NULL)
        {
            // Only return XML when there are no errors.
            //
            // TO DO - Sort out XML special characters in XML.  
            $xml= "<tridas:taxon normalStd=\"$taxonomicAuthorityEdition\" normalId=\"".$this->getCoLID()."\" normal=\"".$this->getLabel()."\">".$this->getOriginalTaxon()."</tridas:taxon>\n";
            return $xml;
        }
        else
        {
            return FALSE;
        }
    }

    function getLabel()
    {
        return $this->label;
    }
    
    function getHigherTaxonXML($theRank)
    {
        $xml = "<tridas:genericField name=\"$theRank\" >";
        switch($theRank)
        {
            case "kingdom":
                if(isset($this->kingdom)) return $xml.$this->kingdom."</tridas:genericField>";
            case "phylum":
                if(isset($this->phylum))  return $xml.$this->phylum."</tridas:genericField>";
            case "class":
                if(isset($this->class))   return $xml.$this->class."</tridas:genericField>";
            case "order":
                if(isset($this->order))   return $xml.$this->order."</tridas:genericField>";
            case "family":
                if(isset($this->family))  return $xml.$this->family."</tridas:genericField>";
            case "genus":
                if(isset($this->genus))   return $xml.$this->genus."</tridas:genericField>";
            default:
                return false;
        }
    }

    function getHigherTaxon($theRank)
    {
        switch($theRank)
        {
            case "kingdom":
                return $this->kingdom;
            case "phylum":
                return $this->phylum;
            case "class":
                return $this->class;
            case "order":
                return $this->order;
            case "family":
                return $this->family;
            case "genus":
                return $this->genus;
            default:
                return false;
        }
    }
    
}



?>
