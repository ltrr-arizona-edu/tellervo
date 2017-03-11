<?php
/**
 * *******************************************************************
 * PHP Tellervo Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package TellervoWS
 * *******************************************************************
 */
require_once('dbhelper.php');

/**
 * Class representation of a user defined field and associated data value.  
 * 
 * @author pbrewer
 */
class userDefinedFieldAndValue
{
	private $name;
	private $longfieldname;
	private $datatype;
	private $attachedto;
	private $description;
	
	private $userdefinedfieldid;
	private $value;
	private $entityid;
	
	private $userdefinedfieldvalueid;
		
	function construct($fieldname, $entityid, $attachedto, $value=null)
	{
		$this->name = $fieldname;
		$this->entityid = $entityid;
		$this->attachedto = $attachedto;
		$this->value = $value;
		$this->setDefinitionFields();
	}

		
	/**
	 * Set the class variable values based on a row returned from vwtbluserdefinedfieldandvalue 
	 * 
	 * @param unknown $row
	 */
	function setFromDBRow($row)
	{
		$this->userdefinedfieldvalueid = $row['userdefinedfieldvalueid'];
		$this->userdefinedfieldid = $row['userdefinedfieldid'];
		$this->name = strtolower($row['fieldname']);
		$this->longfieldname = $row['longfieldname'];
		$this->datatype = $row['datatype'];
		$this->attachedto = $row['attachedto'];
		$this->description = $row['description'];
		$this->value = $row['value'];
		$this->entityid = $row['entityid'];
	}
	
	/**
	 * Query database to populate the userdefinedfieldid and datatype based on the fieldname and attached to parameters
	 * 
	 * @throws Exception
	 */
	function setDefinitionFields()
	{
		global $dbconn;
		
		$result = pg_query_params($dbconn, "SELECT * FROM tlkpuserdefinedfield WHERE fieldname=$1 AND attachedto=$2", array(strtolower($this->name), $this->attachedto));

		if ($result===FALSE) {
			throw new Exception("Field '".$this->name."' has not been defined");
			
		}
		
		while ($row = pg_fetch_array($result)) {
			$this->userdefinedfieldid = $row['userdefinedfieldid'];
			$this->datatype = $row['datatype'];
		}	
		
	}
	
	/**
	 * Get the TRiDaS XML representation of this user defined field
	 * 
	 * @return string
	 */
	function getAsTridasXML()
	{
		$xml ="<tridas:genericField name=\"userDefinedField.".strtolower($this->name)."\" type=\"$this->datatype\">".$this->value."</tridas:genericField>\n";
		return $xml;
	}
	
	/**
	 * Write this user defined field value to the database.  
	 * 
	 * @param unknown $entityid
	 * @throws Exception
	 * @return boolean
	 */
	function writeToDB($entityid=null)
	{
		global $dbconn;
		global $firebug;
		
		if($entityid==null && $this->entityid==null)
		{
			throw new Exception("Missing entity id");
		}
		
		if($entityid!=null)
		{
			$this->entityid = $entityid;
		}
		
		if($this->userdefinedfieldid==null)
		{
			$sql = "SELECT userdefinedfieldid AS id, fieldname AS nme FROM tlkpuserdefinedfield WHERE fieldname='".strtolower($this->name)."' AND attachedto=".$this->attachedto;
			$firebug->log($sql, "SQL");
			$result = pg_query($dbconn, $sql);
			if(pg_num_rows($result)==0)
			{
				throw new Exception("Field '".strtolower($this->name)."' has not been defined");
			}
			else
			{
				while($row = pg_fetch_row($result))
				{
					$this->userdefinedfieldid = $row[0];
				}
			}
		}
				
		if($this->userdefinedfieldvalueid==null)
		{
			$sql = "SELECT userdefinedfieldvalueid AS id FROM tbluserdefinedfieldvalue WHERE userdefinedfieldid='".$this->userdefinedfieldid."' AND entityid='".$this->entityid."'";
			$firebug->log($sql, "SQL");
			$result = pg_query($dbconn, $sql);
			if(pg_num_rows($result)==0)
			{
				
			}
			else
			{
				while($row = pg_fetch_row($result))
				{
					$this->userdefinedfieldvalueid = $row[0];
				}
			}
		}
		
		if($this->userdefinedfieldvalueid!=null)
		{
			// Updating
			$result = pg_query_params("UPDATE tbluserdefinedfieldvalue SET value=$1 WHERE userdefinedfieldvalueid=$2", array($this->value, $this->userdefinedfieldvalueid));
			
			if($result===FALSE)
			{
				return false;
			}
			else 
			{
				return true;
			}
		}
		else
		{
			// Creating new record 
			
			$result = pg_query_params("INSERT INTO tbluserdefinedfieldvalue ( userdefinedfieldid, entityid, value) VALUES ($1, $2, $3) RETURNING userdefinedfieldvalueid", array($this->userdefinedfieldid, $this->entityid, $this->value));
			if($result===FALSE)
			{
				$firebug->log("Failed to write user defined field value to the database");
				return false;
			}
			else
			{
				$firebug->log("User defined field '".strtolower($this->name)."' written to database with value '".$this->value."'");
				return true;
			}
			
		}
		
		
	}
}