<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * This file contains the interface and classes that store data 
 * representing the various data entities in the data model.
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */

require_once('dbhelper.php');
require_once('dbsetup.php');
require_once('geometry.php');
require_once('taxon.php');
require_once('lookupEntity.php');
require_once('securityUser.php');
require_once('box.php');

/**
 * Interface for classes that inherit dbEntity and read/write to/from the database
 *
 */
interface IDBAccessor
{
	function asXML();
	function writeToDB();
	function deleteFromDB();
	function mergeRecords($mergeWithID);
	function validateRequestParams($paramsClass, $crudMode);
}


/**
 * Base class for entities that are representations of database tables
 *
 */
class dbEntity 
{

	/**
	 * URL's of associated files
	 *
	 * @var Array
	 */
	protected $files = NULL;
	
	/**
	 * Unique identifier for this entity
	 *
	 * @var UUID
	 */
    protected $id = NULL;	
    
    /**
     * Class containing details of this entities parent entity
     *
     * @var Mixed
     */
	var $parentEntityArray = NULL;
	
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
     * Comments about this entity
     *
     * @var unknown_type
     */
    protected $comments = NULL;
    
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
	$this->files = array();

    }

    
    public function __destruct()
    {
    }    
    
     /**********/
    /* SETTERS */
    /***********/   
    

	/**
	 * Set an array of associated file urls
	 *
	 * @param Array $array
	 * @return true;
	 */
	function setFiles($array)
	{
		$this->files = $array;

		return true;
	}	
	
	/**
	 * Set an array of associated file urls using a string representation of a PG array delimited with ><
	 *
	 * @param String $pgstrarray
	 * @return true;
	 */
	function setFilesFromStrArray($pgstrarray)
	{
		$this->files = dbHelper::pgStrArrayToPHPArray($pgstrarray);		
		return true;
	}	    
    
	/**
	 * Add an associated file url 
	 *
	 * @param String $value
	 * @return true;
	 */
	function addFile($value)
	{
		global $firebug;
		$firebug->log($value, "Adding file to array");
		array_push($this->files, $value);
		$firebug->log($this->files, "Array now contains");
		return true;
	}
	    
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
     * @param UUID $identifier
     * @param String $domain
     * @return Boolean
     */
    protected function setID($identifier, $domain=NULL)
    {
    	global $firebug;
    	
    	$firebug->log($identifier, "setting entity id to");
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
    
    /**
     * Set the title of this entity
     *
     * @param String $title
     * @return Boolean
     */
    function setTitle($title)
    {
    	$this->title=$title;
    	return true;
    }
    
    /**
     * Set the comments for this entity
     *
     * @param String $comments
     * @return Boolean
     */
    protected function setComments($comments)
    {
    	$this->comments = $comments;
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
	 * Get the array of associated files of this entity
	 *
	 * @return array
	 */
	function getFiles()
	{
		return $this->files;
	}    
    
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
     * Get the comments for this entity
     *
     * @return String
     */
    function getComments()
    {
    	return $this->comments;
    }
    
    /**
     * Get the ID number for this database entity
     *
     * @return UUID
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
    function getIdentifierXML($format="standard")
    {   	
    	global $domain;

    	$xml= "<tridas:title>".dbHelper::escapeXMLChars($this->getTitle())."</tridas:title>\n".
               "<tridas:identifier domain=\"$domain\">".$this->getID()."</tridas:identifier>\n";
        if($format!="minimal")
        {
       		$xml .= "<tridas:createdTimestamp>".dbHelper::pgDateTimeToCompliantISO($this->getCreatedTimestamp())."</tridas:createdTimestamp>\n".
		    	   "<tridas:lastModifiedTimestamp>".dbHelper::pgDateTimeToCompliantISO($this->getLastModifiedTimestamp())."</tridas:lastModifiedTimestamp>\n";
        }
        return $xml;
    }
    
    function getDBIDXML()
    {
    	return "<tridas:genericField name=\"tellervoDBID\" type=\"integer\">".$this->getID()."</tridas:genericField>\n";
    }
    
    function getFileXML()
    {
    	$xml = NULL;
       	if(($this->getFiles()!=NULL) && (count($this->getFiles())>0))
       	{
        	foreach($this->getFiles() as $myFile)	
        	{
        		$xml.= "<tridas:file xlink:href=\"".$myFile."\" />\n";
        	}
       	}
       	return $xml;
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
            $xml.= "<tridas:genericField name=\"canCreate\" type=\"xs:boolean\">".fromPHPtoStringBool($this->getPermission("Create"))."</tridas:genericField ";
            $xml.= "<tridas:genericField name=\"canUpdate\" type=\"xs:boolean\">".fromPHPtoStringBool($this->getPermission("Update"))."</tridas:genericField ";
            $xml.= "<tridas:genericField name=\"canDelete\" type=\"xs:boolean\">".fromPHPtoStringBool($this->getPermission("Delete"))."</tridas:genericField ";
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
   		$entityType = NULL;
   		switch($this->getEntityType())
   		{
   			case "measurement": $entityType = 'series'; break;
   			default : $entityType = $this->getEntityType(); break;
   		}
   		return "https://".$domain."mapservice.php?format=gmap&entity=".$entityType."&id=".$this->getID();
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
	 * @var objectType
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
	 * Geometry object representing the location 
	 *
	 * @var Location
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

	protected $vegetationType=NULL;
	
    function __construct()
    {  
    	$this->location = new location();
    	$this->object = new objectType();
	$this->type = new objectType();
    	$this->files = array();
        $groupXMLTag = "objects";
        parent::__construct($groupXMLTag);  	
	}

	/***********/
    /* SETTERS */
    /***********/   	

	function setVegetationType($type)
	{
		$this->vegetationType = $type;
	}	

	function setCountOfChildVMeasurements($count)
	{
		$this->countOfChildVMeasurements = (int) $count;
	}
        
        /**
	 * Set the object type
	 *
	 * @param Integer $id
	 * @param String $value
	 * @return Boolean
	 */
	function setType($id, $value)
	{
		if(!isset($this->type))
		{
			$this->type = new objectType();
		}
		
		return $this->type->setObjectType($id, $value);
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
	 * Empty the file array
	 * 
	 * @return Boolean
	 */
	function clearFiles()
	{
		$this->files = array();
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

	function getVegetationType()
	{
		return $this->vegetationType;
	}

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
			return $this->type->getID();
		}
		else
		{
			return $this->type->getValue();
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
	var $taxon = NULL;
		
	/**
	 * Whether this element is original, a repair, or later addition
	 *
	 * @var String
	 */
	protected $authenticity = NULL;
	/**
	 * Shape of this element
	 *
	 * @var elementShape
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
	 * @var ElementType
	 */
	protected $type = NULL;
	/**
	 * Associate file URL
	 *
	 * @var String
	 */
	protected $files = NULL;
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
	
	protected $summaryObjectCode = NULL;
	
	
    function __construct()
    {  
		parent::__construct("");
		$this->location = new location();	
		$this->taxon = new taxon(); 	
		$this->type = new elementType(); 	
		$this->shape = new elementShape();
	}

	/***********/
    /* SETTERS */
    /***********/ 	
		
	function setSummaryObjectCode($code)
	{
		$this->summaryObjectCode=$code;
	}
	
	function setAltitude($altitude)
	{
		$this->altitude = $altitude;
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
		$this->authenticity = $value;
	}

	/**
	 * Set the shape of this element
	 *
	 * @param Integer $id
	 * @param String $value
	 * @return Boolean
	 */
	function setShape($id, $value)
	{
		if(!isset($this->shape))
		{
			$this->shape = new elementShape();
		}
		
		return $this->shape->setElementShape($id, $value);
	}
	
	/**
	 * Set the diameter of this element
	 *
	 * @param Double $value
	 * @return Boolean
	 */
	function setDiameter($diameter)
	{
		if($diameter===NULL)
		{
			$this->diameter = NULL;
		}
		else
		{
			$this->diameter = (double) $diameter;
		}
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
		if($height===NULL)
		{
			$this->height = NULL;
		}
		else
		{
			$this->height = (double) $height;
		}
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
		if($width===NULL)
		{
			$this->width = NULL;
		}
		else
		{
			$this->width = (double) $width;
		}
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
		if($depth===NULL)
		{
			$this->depth = NULL;
		}
		else
		{
			$this->depth = (double) $depth;
		}
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
	function setDimensions($height, $width, $depth)
	{
		$this->setHeight($height);
		$this->setWidth($width);
		$this->setDepth($depth);
		return true;
	}
	
	function setDimensionUnits($id, $value)
	{
		if(!isset($this->dimensionUnits))
		{
			$this->dimensionUnits = new unit();
		}
		
		return $this->dimensionUnits->setUnit($id, $value);
	}
	
	/**
	 * Set the element type
	 *
	 * @param Integer $id
	 * @param String $value
	 * @return Boolean
	 */
	function setType($id, $value)
	{
		if(!isset($this->type))
		{
			$this->type = new elementType();
		}
		
		return $this->type->setElementType($id, $value);
	}
	
	/**
	 * Set the associated file
	 *
	 * @param String $value
	 * @return Boolean
	 */
	function setFile($value)
	{
		$this->files = $file;
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
		$this->objectID = $value;
		return TRUE;
	}

	/***********/
    /* GETTERS */
    /***********/ 	
	
	function getSummaryObjectCode()
	{
		return $this->summaryObjectCode;
	}
	
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
	function getAuthenticity()
	{
		return $this->authenticity;
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
			return  $this->shape->getID();
		}
		else
		{
			return $this->shape->getValue();
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
	function getDimensionUnits($asKey=false)
	{
		
	    if(!isset($this->dimensionUnits)) return null;

	    if($asKey===FALSE)
            {
		return $this->dimensionUnits->getValue();	
            }
            else
            {
		return $this->dimensionUnits->getID();	
            }

	}
	
	/**
	 * Get the element type
	 *
	 * @return String
	 */
	function getType($asKey=false)
	{
            if($asKey===FALSE)
            {
		return $this->type->getValue();	
            }
            else
            {
		return $this->type->getID();	
            }
	}
	
	/**
	 * Get the URL of the associated file
	 *
	 * @return String
	 */
	function getFile()
	{
		return $this->files;
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
        return $this->location->getGeometry()!=null;
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
     * Box this sample is stored in
     *
     * @var Box
     */
    protected $box = NULL;
    
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
	
	protected $externalID = NULL;
	
	protected $summaryObjectCode = NULL;
	protected $summaryElementCode = NULL;

	
    function __construct()
    {  
        $groupXMLTag = "samples";
        parent::__construct($groupXMLTag);
        $this->type = new sampleType();  	
        $this->box = new box();
	}	
	
	/***********/
    /* SETTERS */
    /***********/ 
    
	function setExternalID($id)
	{
		$this->externalID = $id;
	}
	
	function setSummaryObjectCode($code)
	{
		$this->summaryObjectCode=$code;
	}
	
	function setSummaryElementCode($code)
	{
		$this->summaryElementCode=$code;
	}
	
	function setBoxID($id)
    {
    	$this->box->setParamsFromDB($id);
    }
    
    function setBoxFromName($name)
    {
    	return $this->box->setParamsFromDBFromName($name);
    }
    
    function setBoxComments($comments)
    {
    	$this->box->setComments($comments);
    	return true;
    }
    
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
		$this->knots = $value;
	}
	
	function setFile($file)
	{
		$this->files == $file;
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
	 * @param UUID $value
	 * @return Boolean 
	 */
	function setElementID($value)
	{
		$this->elementID = $value;
		return TRUE;
	}
	
	/***********/
    /* GETTERS */
    /***********/  	

	function getExternalID()
	{
		return $this->externalID;
	}
    
	function getSummaryObjectCode()
	{
		return $this->summaryObjectCode;
	}
	
	function getSummaryElementCode()
	{
		return $this->summaryElementCode;
	}
	
	
	function getBoxID()
    {
    	return $this->box->getID();
    }
    
	/**
	 * Get the type of sample
	 *
	 * @return String
	 */
	function getType($askey=false)
        {
            if($askey)
            {
                return $this->type->getID();
            }
            else
            {
				return $this->type->getValue();	
            }
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
	function getKnots()
	{
		return $this->knots;
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
		return $this->files;
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
     * Whether pith is present - complexPresenceAbsence 
     *
     * @var complexPresenceAbsence
     */
    protected $pith = NULL;
    
    /**
     * Is sapwood present?
     *
     * @var complexPresenceAbsence
     */
    protected $sapwood = NULL;
    
    /**
     * Is heartwood present?
     *
     * @var complexPresenceAbsence	
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
     * Is the last ring under the bark present?
     *
     * @var Boolean
     */
    protected $lastRingUnderBarkPresence = NULL;
    
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
    
    
    /**
    * Field for recording whether there are any rings at the inner (i.e. towards pith)
    * edge of the sample that have not been measured.  Typically used to note when 
    * rings are too damaged to measure.
    * 
    * @var Integer
    */
    protected $nrOfUnmeasuredInnerRings = NULL;
    
    /**
     * Field for recording whether there are any rings at the outer (i.e. towards bark) 
     * edge of the sample that have not been measured.  Typically used to note when 
     * rings are too damaged to measure.
     * 
     * @var Integer
     */
    protected $nrOfUnmeasuredOuterRings = NULL;
    
    function __construct()
    {
        $groupXMLTag = "radii";
        parent::__construct($groupXMLTag);  
        $this->pith = new complexPresenceAbsence();
        $this->heartwood = new complexPresenceAbsence();
        $this->sapwood = new complexPresenceAbsence();
	}

	/***********/
    /* SETTERS */
    /***********/   
	
	/**
	 * Set the number of unmeasured rings towards the pith
	 * 
	 * @param $value
	 */
	function setNrOfUnmeasuredInnerRings($value)
	{
		$this->nrOfUnmeasuredInnerRings = $value;
	}
	
	/**
	 * Set the number of unmeasured rings towards the bark
	 * 
	 * @param $value
	 */
	function setNrOfUnmeasuredOuterRings($value)
	{
		$this->nrOfUnmeasuredOuterRings = $value;
	}	
	
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
	 * Set whether the pith is present
	 *
	 * @param String $value
	 * @param Integer $id
	 * @return Boolean
	 */
	function setPith($id, $value)
	{
		$this->pith = new complexPresenceAbsence();
		return $this->pith->setComplexPresenceAbsence($id, $value);
	}
	
	/**
	 * Set the sample ID 
	 *
	 * @param Integer $value
	 * @return Boolean 
	 */
	function setSampleID($value)
	{
		$this->sampleID = $value;
		return TRUE;
	}
	
	/**
	 * Set whether the sapwood is n/a; absent; complete; incomplete
	 *
	 * @param String $value
	 * @param Integer $id
	 * @return Boolean
	 */
	function setSapwood($id, $value)
	{
		$this->sapwood = new complexPresenceAbsence();
		return $this->sapwood->setComplexPresenceAbsence($id, $value);
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
		$this->heartwood = new complexPresenceAbsence();
		return $this->heartwood->setComplexPresenceAbsence($id, $value);
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
	function setLastRingUnderBark($value, $presence=true)
	{
		$present = dbHelper::formatBool($presence);
		if($present!=='error')
		{
			$this->lastRingUnderBark = $value;
			$this->lastRingUnderBarkPresence = $present;
		}
		else
		{
			return false;
		}
		

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
	 * Get the number of unmeasured rings towards the pith
	 * 
	 * @return Integer
	 */
	function getNrOfUnmeasuredInnerRings()
	{
		return $this->nrOfUnmeasuredInnerRings;
	}
	
	
	/**
	 * Get the number of unmeasured rings towards the bark
	 * 
	 * @return Integer
	 */
	function getNrOfUnmeasuredOuterRings()
	{
		return $this->nrOfUnmeasuredOuterRings;
	}
	
	
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
	 * @return String
	 */
	function getPith($asKey=false)
	{
		if($asKey)
		{
			return $this->pith->getID();
		}
		else
		{
			return $this->pith->getValue();
		}
	}
	
	function getSampleID()
	{
		return $this->sampleID;
	}
	
	/**
	 * Get whether the sapwood present
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
	 * Get whether the heartwood is present
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
	
	function getLastRingUnderBarkPresence()
	{
		return $this->lastRingUnderBarkPresence;
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

class boxEntity extends dbEntity
{
	protected $trackingLocation = NULL;
	protected $curationLocation = NULL;
	protected $sampleCount = NULL;
	protected $sampleArray = array();

    function __construct()
    {

    		
    }
    
    /***********/
    /* SETTERS */
    /***********/

    
    function setComments($comments)
    {
    	$this->comments = $comments;
    }
    /**
     * Number of samples in this box
     *
     * @param unknown_type $count
     */
    function setSampleCount($count)
    {
    	$this->sampleCount = $count;
    }
    
    /**
     * Set the current location of this box
     *
     * @param String $trackingLocation
     */
    function setTrackingLocation($trackingLocation)
    {
    	$this->trackingLocation = $trackingLocation;
    }
    
    /**
     * Set the proper home for this box
     *
     * @param String $curationlocation
     */
    function setCurationLocation($curationlocation)
    {
    	$this->curationLocation = $curationlocation;
    }
    
    /***********/
    /* GETTERS */
    /***********/
    
    function getSampleCount()
    {
    	return $this->sampleCount;
    }
    
    /**
     * Get where this box currently is
     *
     * @return String
     */
    function getTrackingLocation()
    {
    	return $this->trackingLocation;
    }
    
    /**
     * Get where this box should permanently live
     *
     * @return String
     */
    function getCurationLocation()
    {
    	return $this->curationLocation;
    }
        
    
}

class securityGroupEntity extends dbEntity
{
	protected $id = NULL;
    protected $name = NULL;
    protected $description = NULL;
    protected $isActive = TRUE;
    public $userMembers = array();
    public $groupMembers = array();
	
    
    function __construct()
    {

    		
    }
    
    
    function setName($theName)
    {
        // Set the current objects precision 
        $this->name=$theName;
    }
    
    function setDescription($theDescription)
    {
        // Set the current objects precision 
        $this->description=$theDescription;
    }
    
    function setIsActive($theIsActive)
    {
        // Set the current objects precision 
        $this->isActive=$theIsActive;
        
    }
    
    function setUserMembers($arr)
    {
    	$this->userMembers= $arr;
    }
    
    function setGroupMembers($arr)
    {
    	$this->groupMembers= $arr;
    }
    
    function getName()
    {
    	return $this->name;
    }
    
    function getDescription()
    {
    	return $this->description;
    }
    
    function getIsActive()
    {
    	return $this->isActive;
    }
    
  
}


class securityUserEntity extends dbEntity
{

    protected $username = NULL;
    protected $firstName = NULL;
    protected $lastName = NULL;
    protected $password = NULL;
    protected $isActive = NULL;
    public    $groupArray = array();
    
    function __construct()
    {

    		
    }
    
    /***********/
    /* SETTERS */
    /***********/

    function setUsername($theUsername)
    {
        $this->username=$theUsername;
    }
    
    function setFirstname($theFirstname)
    {
        $this->firstName=$theFirstname;
    }
    
    function setLastname($theLastname)
    {
        $this->lastName=$theLastname;
    }
    
    /**
     * Set the identifier and from what domain it came
     *
     * @param UUID $identifier
     * @param String $domain
     * @return Boolean
     */
    function setID($identifier, $domain=NULL)
    {
    	$this->id = $identifier;
    	$this->identifierDomain = $domain;	
    	return true;	
    }
    
    function setPassword($thePassword, $format="plain")
    {
        switch($format)
        {
        case "plain":
            // password supplied is plain text so hash first
            $this->password=hash('md5', $thePassword);
            break;
        case "hash":
            // password is already hashed so just store
            $this->password=$thePassword;
            break;
        default:
            return false;
            break;
        }

        return true;
    }
    
    function setIsActive($theIsActive)
    {
        // Set the current objects precision 
        $this->isActive=$theIsActive;
    }
  
    
    /***********/
    /* GETTERS */
    /***********/

    function getUsername()
    {
        return $this->username;
    }
    
    function getFirstname()
    {
        return $this->firstName;
    }
    
    function getLastname()
    {
        return $this->lastName;
    }
    
    function getHashedPassword()
    {
        return $this->password;
    }
    

    function getFormattedName()
    {
    	if (($this->firstName!=NULL) && ($this->lastName!=NULL))
    	{
    		return $this->firstName." ".$this->lastName;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    function getIsActive()
    {
    	return $this->isActive;
    }
    
}

class permissionEntity extends dbEntity 
{
	protected $canCreate = "nullvalue";
	protected $canRead = "nullvalue";
	protected $canUpdate = "nullvalue";
	protected $canDelete = "nullvalue";	
	protected $permDenied = "nullvalue";	
	public $entityArray = array();
	public $securityUserArray = array();
	public $securityGroupArray = array();
	
    function __construct()
    {
		$this->entityArray = array();
    	$this->securityUserArray = array();
    	$this->securityGroupArray = array();
    }
	
	function setCanCreate($bool)
	{
		global $firebug;
		$bool = dbhelper::formatBool($bool);

		if($bool===TRUE)
		{
			$this->canCreate = TRUE;
			return TRUE;
		}
		else if ($bool===FALSE)
		{
			$this->canCreate = FALSE;
			return FALSE;
		}
		
		return NULL;
	}
	
	function setCanRead($bool)
	{
		$bool = dbhelper::formatBool($bool);
		if($bool===TRUE)
		{
			$this->canRead = TRUE;
			return TRUE;
		}
		else if ($bool===FALSE)
		{
			$this->canRead = FALSE;
			return FALSE;
		}
		return NULL;
	}
	
	function setCanUpdate($bool)
	{
		$bool = dbhelper::formatBool($bool);
		if($bool===TRUE)
		{
			$this->canUpdate = TRUE;
			return TRUE;
		}
		else if ($bool===FALSE)
		{
			$this->canUpdate = FALSE;
			return FALSE;
		}
		return NULL;
	}
	
	function setCanDelete($bool)
	{
		$bool = dbhelper::formatBool($bool);
		if($bool===TRUE)
		{
			$this->canDelete = TRUE;
			return TRUE;
		}
		else if ($bool===FALSE)
		{
			$this->canDelete = FALSE;
			return FALSE;
		}
		return NULL;
	}
	
	function setPermDenied($bool)
	{
		$bool = dbhelper::formatBool($bool);
		if($bool===TRUE)
		{
			$this->permDenied = TRUE;
			return TRUE;
		}
		else if ($bool===FALSE)
		{
			$this->permDenied = FALSE;
			return FALSE;
		}
		return NULL;
	}
	
	function canCreate()
	{
		return $this->canCreate;
	}
	
	function canRead()
	{
		return $this->canRead;
	}
	
	function canUpdate()
	{
		return $this->canUpdate;
	}
	
	function canDelete()
	{
		return $this->canDelete;
	}
	
	function isPermissionDenied()
	{
		return $this->permDenied;
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
        $xml = "<tridas:genericField name=\"tellervo.$theRank\" >";
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


class loanEntity extends dbEntity
{
	protected $firstname = NULL;
	protected $lastname = NULL;
	protected $organisation = NULL;
	protected $notes = NULL;
	protected $duedate = NULL;
	protected $returndate = NULL;
	
	public $entityIdArray = NULL;
	
	function __construct()
	{
		$this->entityIdArray = array();
	}
	
	function setFirstName($name)
	{
		$this->firstname = $name;
	}
	
	function setLastName($name)
	{
		$this->lastname = $name;
	}
	
	function setOrganisation($org)
	{
		$this->organisation = $org;
	}
	
	function setNotes($notes)
	{
		$this->notes = $notes;
	}
	
	function setDueDate($date)
	{
		$this->duedate= dbHelper::pgDateTimeToCompliantISO($date);
	}
	
	function setReturnDate($date)
	{
		$this->returndate =  dbHelper::pgDateTimeToCompliantISO($date);	
	}
	
	function getFirstName()
	{
		return $this->firstname;
	}
	
	function getLastName()
	{
		return $this->lastname;
	}
	
	function getOrganisation()
	{
		return $this->organisation;
	}
	
	function getNotes()
	{
		return $this->notes;
	}
	
	function getDueDate($format=NULL)
	{
		if($this->duedate==null)
		{
			return "";
		}
		elseif($format == NULL)
		{
			return $this->duedate;
		}
		else
		{
			return date($format, $this->duedate);
		}
	}
	
	function getReturnDate($format=NULL)
	{
		if($this->returndate==null)
		{
			return "";
		}
		elseif($format==NULL)
		{
			return $this->returndate;
		}
		else
		{
			return date($format, $this->returndate);
		}
	}
}

class curationEventEntity extends dbEntity
{
	protected $curationstatus = NULL;
	protected $notes = NULL;
	protected $curator = NULL;
	
	
	
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
	 * New start year when redating/crossdating
	 *
	 * @var Integer 
	 */
	protected $newStartYear = NULL;
	
	/**
	 * New end year when redating/crossdating
	 *
	 * @var Integer 
	 */
	protected $newEndYear = NULL;	
	
	
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
    public $summaryObjectArray = array();
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
     * @var Location
     */
    protected $location = NULL;

    /**
     * Date the measurementSeries was measured or the derivedSeries was created
     * maps to measurementSeries.measuringDate or derivedSeries.derivationDate
     *
     * @var Date
     */
    protected $birthDate = NULL;
    
    /**
     * Count of the number of immediate child series (not including descendents of descendants)
     *
     * @var Integer
     */
	protected $directChildCount = NULL;
    
    function __construct()
    {  
        $groupXMLTag = "elements";
        parent::__construct($groupXMLTag); 
		$this->location = new location();	
		$this->taxon = new taxon(); 	
		$this->dendrochronologist = new securityUser();
		$this->author = new securityUser();
		$this->analyst = new securityUser();
		$this->dating = new dating();
		$this->vmeasurementOp = new vmeasurementOp();
		$this->measuringMethod = new measuringMethod();
		$this->units = new unit();
	}

	/***********/
    /* SETTERS */
    /***********/ 	
		
	function setDirectChildCount($count)
	{
		$this->directChildCount = (int) $count;
	}
	
	function setVMeasurementOp($id, $value)
	{
		$this->vmeasurementOp->setVMeasurementOp($id, $value);
	}
	
	function setDatingType($id, $value)
	{
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
		
		$sql = "select vwq.* from cpgdb.findObjectAncestors('$juniorObjectID', true) oa JOIN vwTblObject vwq ON oa.objectid=vwq.objectid order by parentobjectid";
		    
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
	
	function setSummaryTaxonName($taxon)
	{
		$this->summaryTaxonName = $taxon;
	}
	
	function setSummaryTaxonCount($count)
	{
		$this->summaryTaxonCount = $count;
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
	function setMeasuringMethod($id, $value)
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
	
	function setDendrochronologist($id)
	{
		$this->dendrochronologist = new securityUser();
		$this->dendrochronologist->setParamsFromDB($id);
	}
	
	function setComments($comments)
	{
		$this->comments = $comments;
	}
	
	/**
	 * @deprecated 
	 *
	 * @param unknown_type $usage
	 */
	function setUsage($usage)
	{
	
	}
	
	/**
	 * @deprecated 
	 *
	 * @param unknown_type $comments
	 */
	function setUsageComments($comments)
	{
		
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
    	$this->masterVMeasurementID = $id;
    }

    
    function setMeasurementID($id)
    {
    	if ($id==NULL)
        {
            //Only run if valid parameter has been provided
            global $dbconn;
            
            $sql  = "select measurementid from tblvmeasurement where vmeasurementid='".$this->getID()."'";
          
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
    
    function setNewEndYear($year)
    {
    	$this->newEndYear = $year;
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
	
	function setBirthDate($date)
	{
		$this->birthDate = $date;
	}
    
   	function setMeasuringDate($date)
   	{
   		$this->setBirthDate($date);
   	}
   	
   	function setDerivationDate($date)
   	{
   		$this->setBirthDate($date);
   	}
	
	/***********
    /* GETTERS */
    /***********/

    /**
     * Get the number of immediate child series associated with this series.  Does not include descendants of descendants
     *
     * @return Integer
     */
   	function getDirectChildCount()
   	{
   		return $this->directChildCount;
   	}
   	
    function hasGeometry()
    {
        return $this->location->getGeometry()!=NULL;
    }
    
    function getLocationAsXML()
    {
    	return $this->location->asXML();
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
    	if(isset($this->parentEntityArray[0])) 
    	{
    		$radiusObj = $this->parentEntityArray[0];
		
	    	if ($radiusObj==NULL)
	    	{
	    		return $this->getFirstYear() - $radiusObj->getMissingHeartwoodRingsToPith();
	    	}
	    	else
	    	{    	
				return $this->getFirstYear();  		
	    	}   
    	}
    	return false;	
    }
    
    /**
     * Get the certainty of the sprout year provided by getSproutYear()
     *
     * @return String
     */
    function getSproutYearCertainty()
    {
    	if(isset($this->parentEntityArray[0])) 
    	{
			$radiusObj = $this->parentEntityArray[0];	
	
			if ($radiusObj==NULL) return "unknown";
			
	        if( ($radiusObj->getPith()=="complete") || ($radiusObj->getPith()=="incomplete")  )
	    	{
	    		return "exact";
	    	}
    	}
    	
    	return "before";
    }
    
    /**
     * Get the year in which this tree died
     *
     * @return Integer
     */
    function getDeathYear()
    {
    	if(isset($this->parentEntityArray[0])) 
    	{
			$radiusObj = $this->parentEntityArray[0];	
	    	if ($radiusObj==NULL)
	    	{
	    		return $this->getFirstYear() + $this->getReadingCount() + $radiusObj->getMissingSapwoodRingsToBark();
	    	}
	    	else
	    	{    	
				return $this->getFirstYear() + $this->getReadingCount();   		
	    	}
    	}
    	return false;
    }
    
    /**
     * Get the certainty of the death year provided by getDeathYear()
     *
     * @return String
     */
    function getDeathYearCertainty()
    {
    	$radiusObj = $this->parentEntityArray[0];
    	
    	if ($radiusObj==NULL) return "unknown";
    	if($radiusObj->getBarkPresent() || $radiusObj->getSapwood()=='complete')
    	{
    		return "exact";  		
    	}
    	else
    	{
    		return "after";
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
    	return "Tellervo ".$wsversion;
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
    
    function getSummaryObjectCode($level=0)
    {
    	if($this->summaryObjectArray[$level])
    	{
    		return $this->summaryObjectArray[$level]->getCode();
    	}
    	else
    	{
    		return null;
    	}
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
    
	function getMeasuringMethod($asKey=false)
	{
		if($asKey)
		{
			return $this->measuringMethod->getID();
		}
		else
		{
			return $this->measuringMethod->getValue();
		}
	}
	
	/**
	 * Get the variable type
	 *
	 * @return String
	 */
	function getVariable()
	{
		return $this->variable->getValue();
	}
	
	function getUnits()
	{
		if(isset($this->units))
		{
			return $this->units->getValue();
		}
		else
		{
			return null;
		}
	}
	
	function getComments()
	{
		return $this->comments;
	}
	
	/**
	 * @deprecated 
	 * @return unknown
	 */
	function getUsage()
	{
		return null;
	}
	
	/**
	 * @deprecated 
	 * @return unknown
	 */	
	function getUsageComments()
	{
		return $null;
	}
	
	function getFirstYear()
	{
		return $this->firstYear;
	}
	
	function getNewStartYear()
	{
		return $this->newStartYear;
	}
	
	function getNewEndYear()
	{
		return $this->newEndYear;
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
		
	function getAuthor($asKey=false)
	{
		if($asKey)
		{
			return $this->author->getID();
		}
		else
		{
			return $this->author->getFormattedName();
		}
	}
	
	function getAnalyst($asKey=false)
	{
		if($asKey)
		{
			return $this->analyst->getID();
		}
		else
		{
			return $this->analyst->getFormattedName();	
		}
	}
	
	function getDendrochronologist($asKey=false)
	{
		if($asKey)
		{
			return $this->dendrochronologist->getID();
		}
		else
		{
			return $this->dendrochronologist->getFormattedName();
		}
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
    
    function getBirthDate()
    {
    	return $this->birthDate;
    }
    
    function getMeasuringDate()
    {
    	return $this->getBirthDate();
    }
    
    function getDerivationDate()
    {
    	return $this->getBirthDate();
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

 
    
}



?>
