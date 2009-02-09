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
			$sql = "SELECT ".$this->idfieldname." AS theid FROM ".$this->tablename." WHERE ".$this->fieldname." = '".$this->value."'";
		
            $dbconnstatus = pg_connection_status($dbconn);
            if ($dbconnstatus ===PGSQL_CONNECTION_OK)
            {
                $result = pg_query($dbconn, $sql);
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
		$this->setLookupEntity($id, $value);
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
		$this->setLookupEntity($id, $value);
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
		$this->power = (integer) $power;
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
		parent::__construct("tlkpvmeasurementop", "name");
		$this->param = new vmeasurementOpParam();
	}
	
	function setVMeasurementOp($id, $value)
	{
		$this->setLookupEntity($id, $value);
	}
	
	function setParam($id, $value)
	{
		$this->param->setVmeasurementOpParam($id, $value);
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

class vmeasurementOpParam extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpindextype", "indexname", "indexid");
	}
	
	function setVmeasurementOpParam($id, $value)
	{
		$this->setLookupEntity($id, $value);
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
		$this->setLookupEntity($id, $value);
	}
}





?>