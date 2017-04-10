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
 * Class representation of a user defined dictionary term
 * 
 * @author pbrewer
 */
class userDefinedTerm
{
	private $id;
	private $term;
	private $dictionarykey;
	
		
	function construct()
	{

	}

		
	/**
	 * Set the class variable values based on a row
	 * 
	 * @param unknown $row
	 */
	function setFromDBRow($row)
	{
		$this->id = $row['userdefinedtermid'];
		$this->term = $row['term'];
		$this->dictionarykey = $row['dictionarykey'];
	}
		
	
}