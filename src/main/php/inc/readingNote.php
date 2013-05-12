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
require_once('lookupEntity.php');

/**
 * Class for storing reading note remarks
 *
 */
class readingNote
{
	protected $id = NULL;
	protected $note = NULL;
	protected $standardisedid = NULL;
	
	/**
	 * Controlled vocabulary for this note
	 *
	 * @var controlledVoc
	 */
	protected $controlledVoc = NULL;
	protected $inheritedCount = NULL;

	/***************/
	/* CONSTRUCTOR */
	/***************/	
	function __construct()
	{
		$this->controlledVoc = new controlledVoc();
	}

	/***********/
	/* SETTERS */
	/***********/

	function setErrorMessage()
	{
		return false;
	}
	
	function setParamsFromDB($theID)
	{
        global $dbconn;
        
        $this->id=$theID;
        $sql = "select * from tlkpreadingnote where readingnoteid=$theID";
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
                $this->setID($row['readingnoteid']);
                $this->setStandardisedID($row['standardisedid']);
                $this->setNote($row['note']);
                $this->controlledVoc->setControlledVoc($row['vocabularyid'], null);
                

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
	
	function setID($id)
	{
		$this->id = $id;
	}
	
	function setNote($note)
	{
		$this->note = $note;
	}
	
    function setStandardisedID($id)
    {
            $this->standardisedid = $id;
    }
	
	
	function setControlledVoc($id, $value)
	{
		return $this->controlledVoc->setControlledVoc($id, $value);
	}
	
	function setInheritedCount($count)
	{
		$this->inheritedCount = $count;
	}
	
	/***********/
	/* GETTERS */
	/***********/		
	
	function getID()
	{
		if($this->id)
		{
			return $this->id;
		}



	}
	
	function getNote()
	{
		return $this->note;
	}
	
	function getControlledVocName()
	{
		return $this->controlledVoc->getValue();
	}
	
    function getStandardisedID()
    {
            return $this->standardisedid;
    }
	
	
	function getControlledVocID()
	{
		return $this->controlledVoc->getID();
	}
	
	function getInheritedCount()
	{
		return $this->inheritedCount;
	}
	
	function asXML()
	{
		if ($this->getNote()!=NULL)
		{
			$xml = "<tridas:remark ";
			
			if($this->getInheritedCount()>0)
			{
				$xml.= "inheritedCount=\"".$this->getInheritedCount()."\" ";
			}
			
			if($this->getControlledVocName()=='TRiDaS')
			{
				$xml.= "normalTridas=\"".$this->getNote()."\" />";
			}
			elseif(($this->getControlledVocID()==NULL) || ($this->getControlledVocName()=="[Custom]"))
			{
				$xml.=">".$this->getNote()."</tridas:remark>";
			}
			else
			{
                $xml.= "normalStd=\"".$this->getControlledVocName()."\" normal=\"".$this->getNote()."\" normalId=\"".$this->getID()."\"/>";
			}
			return $xml;
		}
		
		return false;
	}
	
}

?>
