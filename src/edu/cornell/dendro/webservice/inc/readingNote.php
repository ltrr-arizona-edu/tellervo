<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.2
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
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

	function setID($id)
	{
		$this->id = $id;
	}
	
	function setNote($note)
	{
		$this->note = $note;
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
			elseif($this->getControlledVocID()==NULL)
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