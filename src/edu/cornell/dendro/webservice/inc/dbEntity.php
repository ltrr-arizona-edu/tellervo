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
 * @package CorinaWS
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('dbsetup.php');
require_once('geometry.php');
require_once('taxon.php');
require_once('lookupEntity.php');
require_once('securityUser.php');

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
    protected $id = NULL;	
    
    /**
     * Class containing details of this entities parent entity
     *
     * @var Mixed
     */
	protected $parentEntityArray = NULL;
	
	/**
	 * Array of classes representing this entities children
	 *
	 * @var Mixed
	 */
	protected $childrenEntityArray = NULL;
	
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
     * @var String
     */
    protected $title = NULL;    
    
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
     * A cache for minimizing repetitive DB queries
     *
     * @var Array
     */
    private static $dbEntityCache = array();
    
    /**
     * Constructor for this entity
     *
     * @param String $groupXMLTag
     */
    public function __construct($groupXMLTag)
    {
    	$this->setgroupXMLTag($groupXMLTag);
    	$this->parentEntityArray = array();
    	$this->childrenEntityArray = array();
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
    final function setErrorMessage($theCode, $theMessage)
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
    final protected function setgroupXMLTag($theTag)
    {
		$this->groupXMLTag = $theTag;
		return true;
    }
    
    /**
     * Set the identifier and from what domain it came
     *
     * @param String $identifier
     * @param String $domain
     * @return Boolean
     */
    final protected function setID($identifier, $domain=NULL)
    {
    	$this->id = $identifier;
    	$this->identifierDomain = $domain;	
    	return true;
    }
    

    /**
     * Set the current entities code.
     *
     * @deprecated use setTitle()
     * @param String $theCode
     * @return Boolean
     */
    protected function setCode($theCode)
    {
        $this->code=$theCode;
        return true;
    }
    
    protected function setTitle($title)
    {
    	$this->title=$title;
    	return true;
    }
    
	/**
	 * Set the timestamp for when this entity was created
	 *
	 * @param Timestamp $timestamp
	 * @return Boolean
	 */
    final protected function setCreatedTimestamp($timestamp)
    {
    	$this->createdTimeStamp= dbHelper::pgDateTimeToCompliantISO($timestamp);
    	return true;
    }
    
    /**
     * Set the timestamp for when this entity was last modified
     *
     * @param Timestamp $timestamp
     * @return Boolean
     */
    final protected function setLastModifiedTimestamp($timestamp)
    {
    	$this->lastModifiedTimeStamp= dbHelper::pgDateTimeToCompliantISO($timestamp);
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
     * Get the code for this database entity
     *
     * @deprecated use getTitle()
     * @return String
     */
    protected function getCode()
    {
    	if(isset($this->title))
    	{
    		return $this->title;
    	}
    	else
    	{
    		return NULL;
    	}
    }
    
    /**
     * Get the title of this database entity
     *
     * @return String
     */
    function getTitle()
    {
       	if(isset($this->title))
    	{
    		return dbHelper::escapeXMLChars($this->title);
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
     * Returns the internal XML reference string for this entity
     *
     * @return String
     */
    function getXMLRefID()
    {
    	return "XREF-".$this->getID();
    }
    
    /**
     * Get the XML representation of the identifier
     *
     * @return unknown
     */
    function getIdentifierXML()
    {
    	global $domain;
        return "<tridas:title>".dbHelper::escapeXMLChars($this->getTitle())."</tridas:title>\n".
               "<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>\n".
        	   "<tridas:createdTimestamp>".dbHelper::pgDateTimeToCompliantISO($this->getCreatedTimestamp())."</tridas:createdTimestamp>\n".
		       "<tridas:lastModifiedTimestamp>".dbHelper::pgDateTimeToCompliantISO($this->getLastModifiedTimestamp())."</tridas:lastModifiedTimestamp>\n";
         	   //"<tridas:createdTimestamp>".$this->getCreatedTimestamp()."</tridas:createdTimestamp>\n".
		       //"<tridas:lastModifiedTimestamp>".$this->getLastModifiedTimestamp()."</tridas:lastModifiedTimestamp>\n";
        		
    }
    
    function getDBIDXML()
    {
    	return "<tridas:genericField name=\"corinaDBID\" type=\"integer\">".$this->getID()."</tridas:genericField>\n";
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
	 * @param String $format optional PHP date() format string
	 * @return Mixed
     */
    protected function getCreatedTimestamp($format=NULL)
    {
    	if($format==NULL)
    	{
    		return $this->createdTimeStamp;
    	}
    	else
    	{
    		return date($format, $this->createdTimeStamp);
    	}
    }
    
	/**
	 * Get the timestamp for when this entity was last modified
	 *
	 * @param String $format optional PHP date() format string
	 * @return Mixed
	 */
    protected function getLastModifiedTimestamp($format=NULL)
    {
        if($format==NULL)
    	{
    		return $this->lastModifiedTimeStamp;
    	}
    	else
    	{
    		return date($format, $this->lastModifiedTimeStamp);
    	}
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
    
   function getMapLink()
   {
   		global $domain;
   		
   		return "https://".$domain."mapservice.php?format=gmap&entity=".$this->getEntityType()."&id=".$this->getID();
   }
    
   static function getCachedEntity($entityType, $id, $format='standard') 
   {
      $key = $entityType.':'.$id.':'.$format;
      if(!array_key_exists($key, dbEntity::$dbEntityCache)) {
         return NULL;
      }
      return dbEntity::$dbEntityCache[$key];
   }

   protected function cacheSelf($format='standard')
   {
      $key = $this->getEntityType().':'.$this->getID().':'.$format;
      dbEntity::$dbEntityCache[$key] = $this;
   }

   protected function cacheEntity($entity, $entityType, $id, $format='standard')
   {
      $key = $entityType.':'.$id.':'.$format;
      dbEntity::$dbEntityCache[$key] = $entity;
   }
}


/**
 * Class for representing a TRiDaS object database entity.  An object is the subject of dendro-research - an item that is investigated 
 *
 */
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

	/**
	 * Code name for this object
	 *
	 * @var String
	 */
	protected $code = NULL;
	
	/**
	 * Number of vmeasurements associated with this object
	 *
	 * @var Integer
	 */
	protected $countOfChildVMeasurements= NULL;
	
    function __construct()
    {  
    	$this->location = new location();
        $groupXMLTag = "objects";
        parent::__construct($groupXMLTag);  	
	}

	/***********/
    /* SETTERS */
    /***********/   	
	
	function setCountOfChildVMeasurements($count)
	{
		$this->countOfChildVMeasurements = (int) $count;
	}
	
	/**
	 * Set the type of object
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setType($value)
	{
		
		$this->type = $value;
		return true;
	}
	
	/**
	 * Set this object's description
	 *
	 * @param String $value
	 */
	function setDescription($value)
	{
		$this->description = $value;
	}
	
	/**
	 * Set the title of this object
	 *
	 * @param String $value
	 */
	function setTitle($value)
	{
		$this->title = $value;
	}
	
	/**
	 * Set the name of the creator - name, place of the workshop/wharf etc
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setCreator($value)
	{
		$this->creator = $value;
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
		$this->owner = $value;
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
		$this->file = $value;
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
		$this->coverageTemporal = $period;
		$this->coverageTemporalFoundation = $foundation;	
	}
	
	/**
	 * Set this object's code name
	 *
	 * @param String $value
	 */
	function setCode($value)
	{
		$this->code = $value;
	}
	

	/***********/
    /* GETTERS */
    /***********/ 	

	function getCountOfChildVMeasurements()
	{
		return $this->countOfChildVMeasurements;
	}
	
	/**
	 * Get the type of object
	 *
	 * @param Boolean $asKey
	 * @return unknown
	 */
	function getType($asKey=false)
	{
		if($asKey)
		{
			return dbHelper::getKeyFromValue("objecttype", $this->type);
		}
		else
		{
			return $this->type;
		}
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
		return $this->coverageTemporal;
	}
	
	/**
	 * Get the foundation for the broad historical periods this object covers
	 *
	 * @return String
	 */
	function getTemporalCoverageFoundation()
	{
		return $this->coverageTemporalFoundation;
	}
	
	/**
	 * Get code name for this object
	 *
	 * @return String
	 */
	function getCode()
	{
		return $this->code;
	}
	
	/**
	 * Does this object have any geometry information?
	 *
	 * @return Geometry
	 */
    function hasGeometry()
    {
        return $this->location->getLocationGeometry()!=NULL;
    }
}

/**
 * Class for representing a TRiDaS element database entity.  An element is a piece of wood originating from one tree
 *
 */
class elementEntity extends dbEntity
{
	protected $objectID = NULL;

	/**
	 * Taxonomic information about this element
	 *
	 * @var taxon
	 */
	protected $taxon = NULL;
		
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
	 * Units in which the dimensions are recorded
	 *
	 * @var String
	 */
	protected $dimensionUnits = NULL;	
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
	 * @var Location
	 */
	protected $location = NULL;
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
	
	/**
	 * Altitude of tree
	 *
	 * @var Integer
	 */
	protected $altitude = NULL;
	/**
	 * Angle of slope
	 *
	 * @var Integer
	 */
	protected $slopeAngle = NULL;
	/**
	 * Azimuth of slope
	 *
	 * @var Angle
	 */
	protected $slopeAzimuth = NULL;
	/**
	 * Description of the soil type
	 *
	 * @var String
	 */
	protected $soilDescription = NULL;
	/**
	 * Depth of soil in centimetres
	 *
	 * @var unknown_type
	 */
	protected $soilDepth = NULL;
	/**
	 * Description of the underlying bedrock
	 *
	 * @var String
	 */
	protected $bedrockDescription = NULL;
	
	
    function __construct()
    {  
		parent::__construct("");
		$this->location = new location();	
		$this->taxon = new taxon(); 	
	}

	/***********/
    /* SETTERS */
    /***********/ 	
		
	function setAltitude($altitude)
	{
		$this->altitude = (int) $altitude;
	}
	
	function setSlope($angle, $azimuth)
	{
		$this->setSlopeAngle($angle);
		$this->setSlopeAzimuth($azimuth);
	}
	
	function setSlopeAngle($angle)
	{
		$this->slopeAngle = (int) $angle;
	}
	
	function setSlopeAzimuth($azimuth)
	{
		$this->slopeAzimuth = (int) $azimuth;
	}
	
	function setSoilDescription($description)
	{
		$this->soilDescription = $description;
	}
	
	function setSoilDepth($depth)
	{
		$this->soilDepth = (double) $depth;	
	}
	
	function setBedrockDescription($description)
	{
		$this->bedrockDescription = $description;
	}
	
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
		$this->shape = $value;
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
	 * Set the width of this element
	 *
	 * @param Double $width
	 * @return Boolean
	 */
	function setWidth($width)
	{
		$this->width = (double) $width;
		return true;
	}
	/**
	 * Set the depth of this element
	 *
	 * @param Double $depth
	 * @return Boolean
	 */
	function setDepth($depth)
	{
		$this->depth = (double) $depth;
		return true;
	}	
	
	/**
	 * Set the dimensions of this element
	 *
	 * @param String $units
	 * @param Double $height
	 * @param Double $width
	 * @param Double $depth
	 * @return Boolean
	 */
	function setDimensions($units, $height, $width, $depth)
	{
		$this->setDimensionUnits($units);
		$this->setHeight($height);
		$this->setWidth($width);
		$this->setDepth($depth);
		return true;
	}
	
	function setDimensionUnits($units)
	{
		$this->dimensionUnits = $units;
	}
	
	/**
	 * Set the type of this element
	 *
	 * @param String $type
	 * @return Boolean
	 */
	function setType($type)
	{
		$this->type = $type;
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
		$this->file = $file;
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
		$this->processing = $value; 
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
		$this->marks = $value;
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
	 * Set the description of this element
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setDescription($value)
	{
		$this->description = $value;
		return true;
	}
	
	/**
	 * Set the location field of this element using a GML string
	 *
	 * @param String $gml
	 */
	function setGeometryFromGML($gml)
	{
		$this->location->setGeometryFromGML($gml);
	}
	
	/**
	 * Set the location field of this element using lat/long values.  Only applicable to point geometries.
	 *
	 * @param Float $lat
	 * @param Float $long
	 */
	function setPointGeometryFromLatLong($lat, $long)
	{
		$this->location->setPointGeometryFromLatLong($lat, $long);
	}
	
	/**
	 * Set the object ID 
	 *
	 * @param Integer $value
	 * @return Boolean 
	 */
	function setObjectID($value)
	{
		$this->objectID = (int) $value;
		return TRUE;
	}

	/***********/
    /* GETTERS */
    /***********/ 	
	
	function getAltitude()
	{
		return $this->altitude;
	}
	
	function getSlopeAngle()
	{
		return $this->slopeAngle;
	}
	
	function getSlopeAzimuth()
	{
		return $this->slopeAzimuth;
	}

	function getSoilDescription()
	{
		return $this->soilDescription;
	}
	
	function getSoilDepth()
	{
		return $this->soilDepth;
	}
	
	function getBedrockDescription()
	{
		return $this->bedrockDescription;
	}
	
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
	 * @param Boolean $asKey
	 * @return unknown
	 */
	function getAuthenticity($asKey=false)
	{
		if($asKey)
		{
			return dbHelper::getKeyFromValue("elementauthenticity", $this->authenticity);
		}
		else
		{
			return $this->authenticity;
		}
	}

	/**
	 * Get the shape of this element
	 *
	 * @param Boolean $asKey
	 * @return Mixed
	 */
	function getShape($asKey=false)
	{
		if($asKey)
		{
			return dbHelper::getKeyFromValue("elementshape", $this->shape);
		}
		else
		{
			return $this->shape;
		}
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
	 * Get the units the dimensions are stored in
	 *
	 * @return Double
	 */
	function getDimensionUnits()
	{
		return $this->dimensionUnits;
	}
	
	/**
	 * Get the element type
	 *
	 * @return String
	 */
	function getType($asKey=false)
	{
		if($asKey)
		{
			return dbHelper::getKeyFromValue("elementtype", $this->type);
		}
		else
		{
			return $this->type;
		}
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
        return $this->location!=NULL;
    }
}


/**
 * Class for representing a TRiDaS sample database entity.  A sample is the physical sample of an element to be measured
 *
 */
class sampleEntity extends dbEntity
{
	protected $elementID = NULL;

	/**
	 * Type of sample 
	 *
	 * @var sampleType
	 */
	protected $type = NULL;
	/**
	 * Date the sample was taken
	 *
	 * @var ISODate
	 */
	protected $samplingDate = NULL;
	/**
	 * Associated file URL
	 *
	 * @var String
	 */
	protected $file = NULL;
	/**
	 * Position of the sample in the element
	 *
	 * @var String
	 */
	protected $position = NULL;
	/**
	 * State of the material (dry, wet, conserved etc)
	 *
	 * @var String
	 */
	protected $state = NULL;
	/**
	 * Presence of knots
	 *
	 * @var Boolean
	 */
	protected $knots = NULL;

	
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
	 * @param Integer $id
	 * @param String $value
	 * @return Boolean
	 */
	function setType($id, $value)
	{
		if(!isset($this->type))
		{
			$this->type = new sampleType();
		}
		
		return $this->type->setSampleType($id, $value);
	}
	
	/**
	 * Set the date that this sample was taken on
	 * 
	 * @param String $date
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
		$this->position = $position;
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
		$this->state = $state;
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
		$value = dbHelper::formatBool($value);
		if($value!='error')
		{
			$this->knots = $value;
			return TRUE;		
		}
		else
		{
			return FALSE;
		}
	}
	
	function setFile($file)
	{
		$this->file == $file;
	}
	
	/**
	 * Set the description of this sample
	 *
	 * @param String $value
	 */
	function setDescription($value)
	{
		$this->description = $value;
	}
	

	/**
	 * Set the element ID 
	 *
	 * @param Integer $value
	 * @return Boolean 
	 */
	function setElementID($value)
	{
		$this->elementID = (int) $value;
		return TRUE;
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
		return $this->type->getValue();	
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
		return dbHelper::formatBool($this->knots, $format);
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
	
	function getFile()
	{
		return $this->file;
	}
}

/**
 * Class for representing a TRiDaS radius database entity.  A radius is a line from pith to bark from which the measurements are made.
 *
 */
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
     * @var sapwood
     */
    protected $sapwood = NULL;
    
    /**
     * One of n/a; absent; complete; incomplete
     *
     * @var heartwood	
     */
    protected $heartwood = NULL;
    
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

    /**
     * The angle from true north 
     *
     * @var Double
     */
    protected $azimuth = NULL;
    
    function __construct()
    {
        $groupXMLTag = "radii";
        parent::__construct($groupXMLTag);  	
	}

	/***********/
    /* SETTERS */
    /***********/   
	
	/**
	 * Set the angle of this radius from true north
	 *
	 * @param Double $value
	 */
	function setAzimuth($value)
	{
		$this->azimuth = (double) $value;
	}
	
	/**
	 * Set whether the pith is present or not
	 *
	 * @param Boolean $value
	 * 
	 */
	function setPithPresent($value)
	{
		$value = dbHelper::formatBool($value);
		if($value!=='error')
		{
			$this->pithPresent = $value;
			return TRUE;		
		}
		else
		{
			return FALSE;
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
	 * Set whether the sapwood is n/a; absent; complete; incomplete
	 *
	 * @param String $value
	 * @param Integer $id
	 * @retun Boolean
	 */
	function setSapwood($id, $value)
	{
		$this->sapwood = new sapwood();
		return $this->sapwood->setSapwood($id, $value);
	}
	
	/**
	 * Set whether the bark is present
	 *
	 * @param Boolean $value
	 * @retun Boolean
	 */
	function setBarkPresent($value)
	{
		$bark = dbHelper::formatBool($value);
		if($bark!=='error')
		{
			$this->barkPresent = $bark;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Set whether the heartwood is n/a; absent; complete; incomplete
	 *
	 * @param String $value
	 * @param Integer $id
	 * @retun Boolean
	 */
	function setHeartwood($id, $value)
	{
		$this->heartwood = new heartwood();
		return $this->heartwood->setHeartwood($id, $value);
	}
	
	/**
	 * Set the number of sapwood rings
	 *
	 * @param Integer $value
	 * @return Boolean
	 */
	function setNumberOfSapwoodRings($value)
	{
		$this->numberOfSapwoodRings = (int) $value;
		return true;

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
		$this->missingHeartwoodRingsToPith = (int) $value;
		return true;

	}

	/**
	 * Set the foundation on which the number of missing heartwood rings was estimated
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setMissingHeartwoodRingsToPithFoundation($value)
	{
		$this->missingHeartwoodRingsToPithFoundation = $value;
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

		$this->missingSapwoodRingsToBark = (int) $value;
		return true;
	}
	
	/**
	 * Set the foundation on which the number of missing sapwood rings was estimated
	 *
	 * @param String $value
	 * @return Boolean
	 */	
	function setMissingSapwoodRingsToBarkFoundation($value)
	{
		$this->missingSapwoodRingsToBarkFoundation = $value;
		return true;
	}
	
     /**********/
    /* GETTERS */
    /***********/   	

	/**
	 * Get the angle of this radius from true norht
	 *
	 * @return Double
	 */
	function getAzimuth()
	{
		return $this->azimuth;
	}
	
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
	function getSapwood($asKey=false)
	{
		if($asKey)
		{
			return $this->sapwood->getID();
		}
		else
		{
			return $this->sapwood->getValue();
		}
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
	 * Get whether the heartwood is n/a, absent, complete or incomplete
	 *
	 * @return String
	 */
	function getHeartwood($asKey=false)
	{
		if($asKey)
		{
			return $this->heartwood->getID();
		}
		else
		{
			return $this->heartwood->getValue();
		}
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


/**
 * Class representing a taxon in the database
 *
 */
class taxonEntity extends dbEntity
{
    protected $parentID = NULL;
    protected $label = NULL;
    protected $colID = NULL;
    protected $colParentID = NULL;
    protected $taxonRank = NULL;
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

    function setLabel($theLabel)
    {
        // Set the current objects note.
        $this->label=dbHelper::escapeXMLChars($theLabel);
    }   
        
    function setHigherTaxonomy()
    {
        global $dbconn;
        echo "bii";
        $sql = "select * from cpgdb.qrytaxonomy(".$this->getID().")";
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
    

	/**
	 * Set the current taxon details using the Catalogue of Life dictionary.
	 *
	 * @param String $CoLID - CoL ID 
	 * @param String $CoLNormalName - CoL normalised name for belts and braces check
	 * @param String $webservice - either 'local' or 'remote'
	 * @return Boolean
	 */
    function setParamsFromCoL($CoLID, $CoLNormalName, $webservice="local")
    {
    	if($webservice=='local')
    	{
    		$this->setErrorMessage("702", "Setting taxon parameters using the local Cataloge of life webservice has not yet been implemented.");
    		
    	}
    	else
    	{
    		$this->setErrorMessage("702", "Setting taxon parameters using the remote Cataloge of life webservice has not yet been implemented.");
    	}
    	
    	/**
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
		**/
    }  
    
    function setCoLID($id)
    {
    	$this->colID = $id;
    }
    
    function setCoLParentID($id)
    {
    	$this->colParentID = $id;
    }
    
    function setTaxonRank($rank)
    {
    	$this->taxonRank = $rank;
    }
    
    function setParentID($id)
    {
    	$this->parentID = $id;
    }
    
    function setOriginalTaxon($taxon)
    {
    	$this->originalTaxon = $taxon;
    }
    /**********/
    /* GETTERS */
    /***********/     

   
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

    function getLabel()
    {
        return $this->label;
    }
    
    function getHigherTaxonXML($theRank)
    {
        $xml = "<tridas:genericField name=\"corina.$theRank\" >";
        switch($theRank)
        {
            case "kingdom":
                if($this->kingdom!=NULL) return $xml.$this->kingdom."</tridas:genericField>";
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


/**
 * A class representing TRiDaS measurementSeries and derivedSeries in the database.  This class also includes representations of TRiDaS values.
 *
 */
class measurementEntity extends dbEntity 
{	
	/**
	 * What method was used to measure 
	 *
	 * @var measuringMethod
	 */
	protected $measuringMethod = NULL;
	/**
	 * The variable that was measured (ring with, earlywood, latewood etc)
	 *
	 * @var variable
	 */
	protected $variable = NULL;
	/**
	 * Units of measurement (e.g. meters)
	 *
	 * @var unit
	 */
	protected $units = NULL;
	/**
	 * Resolution of measurement, used in combination with the unit field.  Recorded as an integer representing the exponent of the SI-unit in the unit field.  
	 *
	 * @var unknown_type
	 */
	protected $power = NULL;
	/**
	 * Name of the analyst that made the series
	 *
	 * @var securityUser
	 */
	protected $analyst = NULL;
	/**
	 * Name of the dendrochronologist that oversaw the analyst
	 *
	 * @var securityUser
	 */
	protected $dendrochronologist = NULL;
	/**
	 * More information about the measurement
	 *
	 * @var String
	 */
	protected $comments = NULL;
	/**
	 * How the measurement is used e.g. in which calendar
	 *
	 * @var String
	 */
	protected $usage = NULL;
	/**
	 * Comments by later users on the quality of the series
	 *
	 * @var unknown_type
	 */
	protected $usageComments = NULL;
	/**
	 * 	Year of the first measured ring
	 *
	 * @var Integer
	 */
	protected $firstYear = NULL;
	/**
	 * Estimated provenance derived from the matching calendar
	 *
	 * @var String
	 */
	protected $provenance = NULL;
	
	
	/**
	 * Type e.g. chronology, object curve
	 *
	 * @var String
	 */
	protected $type = NULL;
	
	/**
	 * Author of this virtual series
	 *
	 * @var securityUser
	 */
	protected $author = NULL;
	/**
	 * Reason this series was made
	 *
	 * @var String
	 */
	protected $objective = NULL;
	/**
	 * Version number for this series
	 *
	 * @var unknown_type
	 */
	protected $version = NULL;
	/**
	 * This series' operation type
	 *
	 * @var vmeasurementOp
	 */
    protected $vmeasurementOp = NULL;
    /**
     * Measurement ID
     *
     * @var Integer
     */
	protected $measurementID = NULL;
	/**
	 * VMeasuremennt Result ID
	 *
	 * @var Integer
	 */
    protected $vmeasurementResultID = NULL;
    /**
     * Whether this vm has been reconciled
     *
     * @var Boolean
     */
	protected $isReconciled = FALSE;
	/**
	 * Justification for choice of crossdate
	 *
	 * @var String
	 */
    protected $justification = NULL;
    /**
     * Confidence for this choice of crossdate
     *
     * @var Integer
     */
    protected $confidenceLevel = NULL;
    /**
     * The code for this vmeasurement
     *
     * @var String
     */
	protected $code = NULL;
    
	/**
	 * How this measurement has been dated
	 *
	 * @var dating
	 */
	protected $dating = NULL;
	
    /**
     * Array of readings/values
     *
     * @var unknown_type
     */
    var $readingsArray = array();
    
    //  Various summary fields
    var $referencesArray = array();
    var $readingCount = NULL;
    var $measurementCount = NULL;
    var $masterVMeasurementID = NULL;
   
	/**
	 * Array of Object codes and titles for this measurement ordered from most senior to most junior
	 *
	 * @var Array
	 */
    protected $summaryObjectArray = array();
    /**
     * Summerisation of the element title for this measurement
     *
     * @var Mixed
     */
    protected $summaryElementTitle = NULL;    
    /**
     * Summerisation of the sample title for this measurement
     *
     * @var Mixed
     */
    protected $summarySampleTitle = NULL;   
    /**
     * Summerisation of the radius title for this measurement
     *
     * @var Mixed
     */
    protected $summaryRadiusTitle = NULL;   
    /**
     * Number of objects this vmeasurements is associated with
     *
     * @var Mixed
     */  
    protected $summaryObjectCount = NULL;
    /**
     * Summary of the taxonomic information for this vmeasurement
     *
     * @var Mixed
     */
    protected $summaryTaxonName = NULL;
    /**
     * Number of taxa that are associated with this measurement
     *
     * @var Mixed
     */
    protected $summaryTaxonCount = NULL;    
    /**
     * Lab prefix code for this measurement
     *
     * @var Mixed
     */
    protected $labPrefix = NULL;
    /**
     * Full lab code for thie measurement
     *
     * @var Mixed
     */
    protected $fullLabCode = NULL;
   
    /**
     * The geographical extent covered by this series
     *
     * @var extent
     */
    protected $extent = NULL;
      
    
    
    function __construct()
    {  
        $groupXMLTag = "elements";
        parent::__construct($groupXMLTag); 
		$this->extent = new extent();	
		$this->taxon = new taxon(); 	
		$this->dendrochronologist = new securityUser();
		$this->analyst = new securityUser();
		$this->dating = new dating();
		$this->vmeasurementOp = new vmeasurementOp();
	}

	/***********/
    /* SETTERS */
    /***********/ 	
	
	function setExtent($extent)
	{
		$this->extent->setGeometry($extent);
	}
	
	function setExtentFromGML($gml)
	{
		$this->extent->setGeometryFromGML($gml);
	}
	
	function setExtentComment($comment)
	{
		$this->extent->setComment($comment);
	}
	
	function setVMeasurementOp($id, $value)
	{
		$this->vmeasurementOp->setVMeasurementOp($id, $value);
	}
	
	function setDatingType($id, $value)
	{
		$this->dating = new dating();
		$this->dating->setDatingType($id, $value);
	}
	
	/**
	function setSummaryInfo($objecttitle, $objectcount, $taxonname, $taxoncount, $labprefix)
	{
        $this->summaryObjectCode = $objectcode;
        $this->summaryObjectCount =  $objectcount;
        $this->summaryTaxonName = $taxonname;
        $this->summaryTaxonCount = $taxoncount;
        $this->labPrefix = $labprefix;
        $this->fullLabCode = $labprefix.$this->getCode();
	}**/
	
	
	function setSummaryObjectArray($juniorObjectID)
	{
		if($juniorObjectID==NULL) return false;

		if(($myObjectEntityArray = dbEntity::getCachedEntity("objectEntityArray", $juniorObjectID)) != NULL)
        	{
        	   $this->summaryObjectArray = $myObjectEntityArray;
        	   return TRUE;
        	}
		

		global $dbconn;
		
		$sql = "select vwq.* from cpgdb.findObjectAncestors($juniorObjectID, true) oa JOIN vwTblObject vwq ON oa.objectid=vwq.objectid";
		    
		$dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {	         
        	$result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
            	$myObject = new object();
            	$myObject->setParamsFromDBRow($row);
                array_push($this->summaryObjectArray, $myObject);
            }
		$this->summaryObjectArray = array_reverse($this->summaryObjectArray);
		$this->cacheEntity($this->summaryObjectArray, "objectEntityArray", $juniorObjectID);
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }
		
	}
		
	function setSummaryElementTitle($title)
	{
		$this->summaryElementTitle = $title;
	}

	function setSummarySampleTitle($title)
	{
		$this->summarySampleTitle = $title;
	}
	
	function setSummaryRadiusTitle($title)
	{
		$this->summaryRadiusTitle = $title;
	}
		
	function setMeasurementCount($count)
	{
		$this->measurementCount = (integer) $count;
	}
	
	function setReadingCount($count)
	{
		$this->readingCount = (integer) $count;
	}
	
	/**
	 * Set the method used to create this measurement.  You can supply id, value or both
	 *
	 * @param Integer $id
	 * @param String $value
	 */
	function setMeasurementMethod($id, $value)
	{
		
		$this->measuringMethod = new measuringMethod();
		$this->measuringMethod->setMeasuringMethod($id, $value);
		
	}
		
	
	/**
	 * Set the measurment units 
	 *
	 * @param Double $units
	 * @param Integer $power
	 */
	function setMeasuringUnits($id, $value, $power)
	{
		$this->units = new unit();
		$this->units->setUnit($id, $value);
		$this->power = $power;
	}
		
	/**
	 * Set the code for this measurement
	 *
	 * @param unknown_type $code
	 */
	function setCode($code)
	{
		$this->code = $code;
	}
	
	/**
	 * Set the person who measured this using their ID
	 *
	 * @param unknown_type $id
	 */
	function setAnalyst($id)
	{
		$this->analyst = new securityUser();
		$this->analyst->setParamsFromDB($id);
	}
	
	function setDendrochronologist($name)
	{
		$this->dendrochronologist = $name;
	}
	
	function setComments($comments)
	{
		$this->comments = $comments;
	}
	
	function setUsage($usage)
	{
		$this->usage = $usage;
	}
	
	function setUsageComments($comments)
	{
		$this->usageComments = $comments;
	}
	
	function setFirstYear($year)
	{
		$this->firstYear = (int) $year;
	}
	
	function setStatType($type)
	{
		$this->statType = $type;
	}
	
	function setStatValue($value)
	{
		$this->statValue = (double) $value;	
	}
	
	function setSignificanceLevel($level)
	{
		$this->significanceLevel = (double) $level;
	}
	
	function setProvenance($provenance)
	{
		$this->provenance = $provenance;
	}
	
	function setType($type)
	{
		$this->type = $type;
	}
	
	function setStandardizingMethod($id, $value)
	{
		// If the operation has not been set make it 'index' 
		// as this must be the case if setting the standardization
		// method.
		if(!isset($this->vmeasurementOp))
		{
			$this->vmeasurementOp = new vmeasurementOp();
			$this->vmeasurementOp->setVMeasurementOp(NULL, "Index");
		}
		
		
		$this->vmeasurementOp->setStandardizingMethod($id, $value);
		
	
	}
	
	function setAuthor($authorid)
	{
		$this->author = new securityUser();
		$this->author->setParamsFromDB($authorid);
	}
	
	function setObjective($objective)
	{
		$this->objective = $objective;
	}
	
	function setVersion($version)
	{
		$this->version = $version;
	}
		
    function setOwnerUserID($theOwnerUserID)
    {
        if($theOwnerUserID)
        {
            //Only run if valid parameter has been provided
            global $dbconn;

            $this->ownerUserID = $theOwnerUserID;
            
            $sql  = "select username from tblsecurityuser where securityuserid=".$this->ownerUserID;
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->owner = $row['username'];
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
        return FALSE;
    }

    function setVariable($id, $value)
    {
    	$this->variable = new variable();
    	$this->variable->setVariable($id, $value);
    }
    
    
    function setMasterVMeasurementID($id)
    {
    	$this->masterVMeasurementID = (integer) $id;
    }

    
    function setMeasurementID($id)
    {
    	if ($id==NULL)
        {
            //Only run if valid parameter has been provided
            global $dbconn;
            
            $sql  = "select measurementid from tblvmeasurement where vmeasurementid=".$this->getID();
          
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                while ($row = pg_fetch_array($result))
                {
                    $this->measurementID = $row['measurementid'];
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
        else
        {
        	$this->measurementID = $id;
        	return TRUE;
        }

    }
          
    
    function setUnits($id, $value)
    {
		$this->units = new unit();
		$this->units->setUnit($id, $value);
    }
    
    function setNewStartYear($year)
    {
        $this->newStartYear = $year;
    }
    
    function setConfidenceLevel($level)
    {
        $this->confidenceLevel = $level;
    }
    
    function setJustification($justification)
    {
        $this->justification = $justification;
    }
       
    function setReadingsArray($theReadingsArray)
    {
        $this->readingsArray = $theReadingsArray;
    }
    
    function setReferencesArray($theReferencesArray)
    {
        $this->referencesArray = array();
        $this->referencesArray = $theReferencesArray;
    }
    
    function setRadiusID($theRadiusID)
    {
        $this->radiusID = $theRadiusID;
    }

    function setIsReconciled($isReconciled)
    {
        $this->isReconciled = dbHelper::formatBool($isReconciled);
    }

    function setStartYear($theStartYear)
    {
        $this->startYear = $theStartYear;
    }    


    /**
     * Enter description here...
     *
     * @deprecated 
     * @param unknown_type $theName
     */
    function setName($theName)
    {
/*        $this->name = $theName;

        // assemble our full lab code, if we can
        if($this->labPrefix != NULL)
           $this->fullLabCode = $this->labPrefix . $this->name;
*/
    }
    
    function setDescription($theDescription)
    {
        $this->description = $theDescription;
    }

    function setIsPublished($isPublished)
    {
        $this->isPublished = dbHelper::formatBool($isPublished);
    }
	
	
	
	/***********
    /* GETTERS */
    /***********/

    
    function hasExtent()
    {
        return $this->extent->getGeometry()!=NULL;
    }
    
    function getExtentAsXML()
    {
    	return $this->extent->asXML();
    }
    
    function getTridasSeriesType()
    {

    	if($this->vmeasurementOp->getValue()=='Direct')
    	{
    		return "measurementSeries";
    	}
    	else
    	{
    		return "derivedSeries";
    	}
    }
    
	/**
	 * Get the year in which we think the tree sprouted
	 *
	 * @return Integer
	 */
    function getSproutYear()
    { 
    	if (isset($this->parantEntityArray[0]))
    	{
    		return $this->getFirstYear() - $this->parentEntityArray[0]->getMissingHeartwoodRingsToPith();
    	}
    	else
    	{
    		//trigger_error('666'.'Unable to obtain estimate of missing heartwood rings for this measurement', E_USER_NOTICE);
    		return false;
    	}
    }
    
    /**
     * Get the year in which this tree died
     *
     * @return Integer
     */
    function getDeathYear()
    {
    	if (isset($this->parantEntityArray[0]))
    	{
    		return $this->getFirstYear() + $this->getReadingCount() + $this->parentEntityArray[0]->getMissingSapwoodRingsToBark();
    	}
    	else
    	{    	
    		//trigger_error('666'.'Unable to obtain estimate of missing sapwood rings for this measurement', E_USER_NOTICE);
    		return false;    		
    	}
    }
    
    /**
     * Get the name and version of the software used to do the statistics
     *
     * @return unknown
     */
    function getUsedSoftware()
    {
    	global $wsversion;
    	return "Corina ".$wsversion;
    }
    
    function getCode()
    {
    	return $this->code;
    }
    
    function getSummaryObjectTitle($level=1)
    {
    	return $this->summaryObjectArray[$level]['title'];
    }
    
    function getSummaryElementTitle()
    {
    	return $this->summaryElementTitle;
    }

    function getSummarySampleTitle()
    {
    	return $this->summarySampleTitle;
    }    

    function getSummaryRadiusTitle()
    {
    	return $this->summaryRadiusTitle;
    }    
    
    function getSummaryObjectCount()
    {
    	return $this->summaryObjectCount;
    }
    
    function getSummaryObjectCode($level=1)
    {
    	return $this->summaryObjectArray[$level]['code'];
    }
    
    function getSummaryTaxonName()
    {
    	return $this->summaryTaxonName;
    }
    
    function getSummaryTaxonCount()
    {
    	return $this->summaryTaxonCount;
    }
    
    function getLabPrefix()
    {
    	return $this->labPrefix;
    }
    
    function getFullLabCode()
    {
    	return $this->getLabPrefix().$this->getCode;
    }
    
    
    function getConfidenceLevel()
    {
    	return $this->confidenceLevel;
    	
    }
        
    function getIsReconciled()
    {
    	return $this->isReconciled;
    }
    
    function getJustification()
    {
    	return $this->justification;
    }
    
    function getMasterVMeasurementID()
    {
    	return $this->masterVMeasurementID;
    }
    
    function getMeasurementCount()
    {
    	return $this->measurementCount;
    }
    
    function getMeasurementID()
    {
    	return $this->measurementID;
    }
        
    function getReadingCount()
    {
    	return $this->readingCount;
    }
    
    function getVMeasurementOp()
    {
    	if(isset($this->vmeasurementOp))
    	{
    		return $this->vmeasurementOp->getValue();
    	}
    	else
    	{
    		return false;
    	}
    }
    
    function getStandardizingMethod()
    {
    	if(isset($this->vmeasurementOp))
    	{
    		return $this->vmeasurementOp->getStandardizingMethod();
    	}
    	else
    	{
    		return false;
    	}
    }
    
    function getVMeasurementResultID()
    {
    	return $this->vmeasurementResultID;
    }
    
	function getMeasuringMethod()
	{
		return $this->measuringMethod;
	}
	
	/**
	 * Get the variable type
	 * @todo Work out how we will do early/late wood stuff
	 *
	 * @return String
	 */
	function getVariable()
	{
		return "Ring width";
		//return $this->variable->getValue();
	}
	
	function getUnits()
	{
		return $this->units->getValue();
	}
	
	function getComments()
	{
		return $this->comments;
	}
	
	function getUsage()
	{
		return $this->usage;
	}
	
	function getUsageComments()
	{
		return $this->usageComments;
	}
	
	function getFirstYear()
	{
		return $this->firstYear;
	}
	
	function getStatType()
	{
		return $this->statType;
	}
	
	function getStatValue()
	{
		return $this->statValue;
	}
	
	function getSignificanceLevel()
	{
		return $this->significanceLevel;
	}
		
	function getProvenance()
	{
		return $this->provenance;
	}
	
	function getType()
	{
		return $this->vmeasurementOp->getValue();
	}
		
	function getAuthor()
	{
		return $this->author->getFormattedName();
	}
	
	function getObjective()
	{
		return $this->objective;
	}
	
	function getVersion()
	{
		return $this->version;
	}
	
    function getEndYear()
    {
        $length = count($this->readingsArray);
        return $this->startYear + $length;
    }	
	
	
	/*************/
    /* FUNCTIONS */
    /*************/ 

    function getIndexNameFromParamID($paramid)
    {
        global $dbconn;

        $sql  = "select indexname from tlkpindextype where indexid='".$paramid."'";
        $dbconnstatus = pg_connection_status($dbconn);
        if ($dbconnstatus ===PGSQL_CONNECTION_OK)
        {
            $result = pg_query($dbconn, $sql);
            while ($row = pg_fetch_array($result))
            {
                return $row['indexname'];
            }
        }
        else
        {
            // Connection bad
            $this->setErrorMessage("001", "Error connecting to database");
            return FALSE;
        }
    }

    function unitsHandler($value, $inputUnits, $outputUnits)
    {   
        // This is a helper function to deal with the units of readings 
        // Internally Corina uses microns as this is (hopefully) the smallest 
        // unit required in dendro
        //
        // To use default units set $inputUnits or $outputUnits to "default"

        // Set units to default (microns) if requested
        if($inputUnits == 'db-default') $inputUnits = -6;
        if($outputUnits == 'db-default') $outputUnits = -6;
        if($inputUnits == 'ws-default') $inputUnits = -5;
        if($outputUnits == 'ws-default') $outputUnits = -5;

        // Calculate difference between input and output units
        $convFactor = $inputUnits - $outputUnits;

        switch($convFactor)
        {
            case 0:
                return $value;
            case -1:
                return round($value/10);
            case -2:
                return round($value/100);
            case -3:
                return round($value/1000);
            case -4:
                return round($value/10000);
            case -5:
                return round($value/100000);
            case 1:
                return $value*10;
            case 2:
                return $value*100;
            case 3:
                return $value*1000;
            case 4:
                return $value*10000;
            case 5:
                return $value*100000;
            default:
                // Not supported
                $this->setErrorMessage("905", "This level of units is not supported");
                return false;
        }
    }
    
        
    
}



?>
