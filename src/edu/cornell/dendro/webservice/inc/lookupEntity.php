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
	
	private $value;
	private $id;
	
	function __construct($tablename, $fieldname)
	{
		$this->tablename = $tablename;
		$this->fieldname = $fieldname;
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
			$sql = "SELECT ".$this->fieldname." AS thevalue FROM ".$this->tablename." WHERE ".$this->fieldname."id = '".$id."'";
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
			$sql = "SELECT ".$this->fieldname."id AS theid FROM ".$this->tablename." WHERE ".$this->fieldname." = '".$this->value."'";
		
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

class datingType extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpdatingtype", "datingtype");
	}

	function setDatingType($id, $value)	
	{
		$this->setLookupEntity($id, $value);
	}
}


class unit extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpunit", "unit");
	}
	
	function setUnit($id, $value)
	{
		$this->setLookupEntity($id, $value);
	}
}

class vmeasurementOp extends lookupEntity
{
	function __construct()
	{
		parent::__construct("tlkpvmeasurementop", "name");
	}
	
	function setVMeasurementOp($id, $value)
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