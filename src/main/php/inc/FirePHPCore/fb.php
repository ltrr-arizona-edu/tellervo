<?php


//require_once dirname(__FILE__).'/FirePHP.class.php';

/**
 * Sends the given data to the FirePHP Firefox Extension.
 * The data can be displayed in the Firebug Console or in the
 * "Server" request tab.
 * 
 * @see http://www.firephp.org/Wiki/Reference/Fb
 * @param mixed $Object
 * @return true
 * @throws Exception
 */
function fb()
{

}


class FB
{
  /**
   * Enable and disable logging to Firebug
   * 
   * @see FirePHP->setEnabled()
   * @param boolean $Enabled TRUE to enable, FALSE to disable
   * @return void
   */
  public static function setEnabled($Enabled) {
    
  }
  
  /**
   * Check if logging is enabled
   * 
   * @see FirePHP->getEnabled()
   * @return boolean TRUE if enabled
   */
  public static function getEnabled() {
    return false;
  }  
  
  /**
   * Specify a filter to be used when encoding an object
   * 
   * Filters are used to exclude object members.
   * 
   * @see FirePHP->setObjectFilter()
   * @param string $Class The class name of the object
   * @param array $Filter An array or members to exclude
   * @return void
   */
  public static function setObjectFilter($Class, $Filter) {
   
  }
  
  /**
   * Set some options for the library
   * 
   * @see FirePHP->setOptions()
   * @param array $Options The options to be set
   * @return void
   */
  public static function setOptions($Options) {
  
  }

  /**
   * Get options for the library
   * 
   * @see FirePHP->getOptions()
   * @return array The options
   */
  public static function getOptions() {
   
  }

  /**
   * Log object to firebug
   * 
   * @see http://www.firephp.org/Wiki/Reference/Fb
   * @param mixed $Object
   * @return true
   * @throws Exception
   */
  public static function send()
  {
   
  }

  /**
   * Start a group for following messages
   * 
   * Options:
   *   Collapsed: [true|false]
   *   Color:     [#RRGGBB|ColorName]
   *
   * @param string $Name
   * @param array $Options OPTIONAL Instructions on how to log the group
   * @return true
   */
  public static function group($Name, $Options=null) {
  
  }

  /**
   * Ends a group you have started before
   *
   * @return true
   * @throws Exception
   */
  public static function groupEnd() {
  }

  /**
   * Log object with label to firebug console
   *
   * @see FirePHP::LOG
   * @param mixes $Object
   * @param string $Label
   * @return true
   * @throws Exception
   */
  public static function log($Object, $Label=null) {
    
  } 

  /**
   * Log object with label to firebug console
   *
   * @see FirePHP::INFO
   * @param mixes $Object
   * @param string $Label
   * @return true
   * @throws Exception
   */
  public static function info($Object, $Label=null) {
  } 

  /**
   * Log object with label to firebug console
   *
   * @see FirePHP::WARN
   * @param mixes $Object
   * @param string $Label
   * @return true
   * @throws Exception
   */
  public static function warn($Object, $Label=null) {
  } 

  /**
   * Log object with label to firebug console
   *
   * @see FirePHP::ERROR
   * @param mixes $Object
   * @param string $Label
   * @return true
   * @throws Exception
   */
  public static function error($Object, $Label=null) {
  } 

  /**
   * Dumps key and variable to firebug server panel
   *
   * @see FirePHP::DUMP
   * @param string $Key
   * @param mixed $Variable
   * @return true
   * @throws Exception
   */
  public static function dump($Key, $Variable) {
  } 

  /**
   * Log a trace in the firebug console
   *
   * @see FirePHP::TRACE
   * @param string $Label
   * @return true
   * @throws Exception
   */
  public static function trace($Label) {
  } 

  /**
   * Log a table in the firebug console
   *
   * @see FirePHP::TABLE
   * @param string $Label
   * @param string $Table
   * @return true
   * @throws Exception
   */
  public static function table($Label, $Table) {
  } 

}

