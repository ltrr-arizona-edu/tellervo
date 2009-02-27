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


class lookupEntity
{
	private $tablename;
	private $fieldname;
	private $idfieldname;
	
	private $value;
	private $id;
	
	function __construct($lookuptablename, $lookupnamefield, $lookupidfield=NULL)
	{
		$this->tablename = $lookuptablename;
		$this->fieldname = $lookupnamefield;
		if($lookupidfield!=NULL)
		{
			$this->idfieldname = $lookupidfield;
		}
		else
		{
			$this->idfieldname = $lookupnamefield."id";
		}
	}
	
	protected function setLookupEntity($id, $value)
	{
		global $dbconn;
		
		/*echo "\n\n****".$this->tablename."\n";
		echo "id = $id\n";
		echo "value = $value\n";
		echo "****\n";
		*/
		
		if( ($id!=NULL) && ($value==NULL) )
		{
			// ID but no value
			$sql = "SELECT ".$this->fieldname." AS thevalue FROM ".$this->tablename." WHERE ".$this->idfieldname." = '".$id."'";
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                if (pg_num_rows($result) == 0)
                {
					trigger_error("903"."The id '".$id."' was not found when doing a lookup in table ".$this->tablename, E_USER_ERROR);
					return false;
                }                
                while ($row = pg_fetch_array($result))
                {
                	$this->id = $id;
                    $this->value = $row['thevalue'];
                }
            }
            else
            {
                // Connection bad
                trigger_error("001"."Error connecting to database", E_USER_ERROR );
                return FALSE;
            }
            return TRUE;	
		}
		elseif ( ($id==NULL) && ($value!=NULL) )
		{	
			// Value given but no ID
			$sql = "SELECT ".$this->idfieldname." AS theid FROM ".$this->tablename." WHERE ".$this->fieldname." = '".$value."'";
		
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
                if (pg_num_rows($result) == 0)
                {
					trigger_error("903"."The value '".$value."' was not found when doing a lookup in table ".$this->tablename, E_USER_ERROR);
					return false;
                }
                while ($row = pg_fetch_array($result))
                {
                	$this->id = $row['theid'];
                    $this->value = $value;
                }
            }
            else
            {
                // Connection bad
                trigger_error("001"."Error connecting to database", E_USER_ERROR );
                return FALSE;
            }
            return TRUE;			
		}
		elseif ( ($id!=NULL) && ($value!=NULL) )
		{
			// Both ID and value given. Trusting that they are 
			// valid and we're saving them
			$this->value = addslashes($value);
			$this->id = (integer) $id;
		}
		else
		{
			return false;
		}
		
		
	}
	
	function getID()
	{
		return $this->id;
	}
	
	function getValue()
	{
		return $this->value;
	}
	
}


class measuringMethod extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpmeasuringmethod", "measuringmethod");
	}

	function setMeasuringMethod($id, $value)	
	{
		return $this->setLookupEntity($id, $value);
	}
}

class dating extends lookupEntity
{
	/**
	 * Positive error around date
	 *
	 * @var Integer
	 */
	protected $datingErrorPositive = NULL;
	/**
	 * Negative error around date
	 *
	 * @var Integer
	 */
	protected $datingErrorNegative = NULL;
	
	function __construct()
	{
		parent::__construct("tlkpdatingtype", "datingtype");
	}

	function setDatingType($id, $value)	
	{
		return $this->setLookupEntity($id, $value);
	}
	
	function setDatingErrors($positive, $negative)
	{
		$this->datingErrorNegative = (integer) $negative;
		$this->datingErrorPositive = (integer) $positive;	
	}
	
	function getDatingErrorPositive()
	{
		return $this->datingErrorPositive;
	}
	
	function getDatingErrorNegative()
	{
		return $this->datingErrorNegative;
	}
}


class unit extends lookupEntity
{
	/**
	 * Exponent of the unit
	 *
	 * @var Integer
	 */
	protected $power = NULL;
	
	function __construct()
	{
		parent::__construct("tlkpunit", "unit");
	}
	
	function setUnit($id, $value)
	{
		$this->setLookupEntity($id, $value);
	}
	
	function setPower($power)
	{
		return $this->power = (integer) $power;
	}
	
	function getPower()
	{
		return $this->power;
	}
}

class vmeasurementOp extends lookupEntity
{
	/**
	 * vmeasurement operation parameter object
	 *
	 * @var vmeasurementOpParam
	 */
	protected $param = NULL;
	
	function __construct()
	{
		parent::__construct("tlkpvmeasurementop", "name", "vmeasurementopid");
		$this->param = new standardizingMethod();
	}
	
	function setVMeasurementOp($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
	
	function setStandardizingMethod($id, $value)
	{
		return $this->param->setStandardizingMethod($id, $value);

	}
	
	function getStandardizingMethod()
	{
		return $this->param->getStandardizingMethod();
	}
	
	function getStandardizingMethodID()
	{
		return $this->param->getID();
	}
	
	function getParamID()
	{
		return $this->param->getID();
	}
	
	function getParamValue()
	{
		return $this->param->getValue();
	}
}

class standardizingMethod extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpindextype", "indexname", "indexid");
	}
	
	function setStandardizingMethod($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
	
	/**
	 * Get the standardizing method
	 *
	 * @return String
	 */
	function getStandardizingMethod()
	{
		return $this->getValue();	
	}
	
}


class variable extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpmeasurementvariable", "measurementvariable");
	}
	
	function setVariable($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
}

class sapwood extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpsapwood","sapwood", "sapwoodid");
	}
	
	function setSapwood($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
}

class heartwood extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpheartwood", "heartwood", "heartwoodid");
	}
	
	function setHeartwood($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
}

class sampleType extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpsampletype", "sampletype", "sampletypeid");
	}
	
	function setSampleType($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
}

class locationType extends lookupEntity
{

	function __construct()
	{
		parent::__construct("tlkplocationtype", "locationtype", "locationtypeid");
	}
	
	function setLocationType($id, $value)
	{
		return $this->setLookupEntity($id, $value);
	}
	
}

?>