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

class permission extends permissionEntity
{
	protected $thisParamsClass = NULL;
	
    /***************/
    /* CONSTRUCTOR */
    /***************/

    function permission()
    {
    }
    
    function __construct()
    {
    	
    }

    /***********/
    /* SETTERS */
    /***********/

    function setParamsFromDBRow($row)
    {
		return true;
    }
    
    function setParamsFromParamsClass($paramsClass)
    {
    	$thisParamsClass = $paramsClass;
    }
    
    function setParamsFromDB($id)
    {
        return TRUE;
    }

        
    function asXML()
    {
    	$xml = "<permission>\n";
    	$xml.= "</permission>\n";    	
    	return $xml;
    }
    
    /***********/
    /*FUNCTIONS*/
    /***********/

    /**
     * Write these permissions to the database
     *
     * @return unknown
     */
    function writeToDB()
    {
    	global $dbconn;
                
    	foreach($thisParamsClass->entityArray as $entity)
    	{
    		// Entity has to be of a certain type otherwise continue;
    		if ($entity["type"]!='object' && $entity["type"]!='element' && $entity["type"]!='measurement')
    		{
    			continue;
    		}
    		
    		foreach($thisParamsClass->securityUserArray as $user)
    		{
    			// Directly adding permissions to users is not yet supported
				continue;
    		}
    		
    		foreach($thisParamsClass->securityGroupArray as $group)
    		{
    		    if($thisParamsClass->canCreate!=NULL)
    			{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','3'); ";
    			        // Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }
    			}
    		    if($thisParamsClass->canRead!=NULL)
    			{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','2'); ";
    			    	// Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }    					
    			}
    		    if($thisParamsClass->canUpdate!=NULL)
    			{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','4'); ";
    			    	// Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }    					
    			}
    		    if($thisParamsClass->canDelete!=NULL)
    			{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','5'); ";
    			    	// Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }    					
    			}    			
    		}
    	}
    }

            
    /**
     * Check that the parameters within a defined parameters class are valid for
     * a specific crudMode
     *
     * @todo wouldn't it be better to have the permissions functions done here?
     * @param elementParameters $paramsObj
     * @param String $crudMode
     * @return Boolean
     */
    function validateRequestParams($paramsObj, $crudMode)
    {        	
        switch($crudMode)
        {
            case "create":
                if($paramsObj->entityArray == NULL || count($paramsObj->entityArray<1))
                {
                    $this->setErrorMessage("902","One or more entities is required when creating a permissions record");
                    return false;
                }
                if( ($paramsObj->securityUserArray() == NULL) || count($paramsObj->securityUserArray()<1) && 
                    ($paramsObj->securityGroupArray() == NULL) || count($paramsObj->securityGroupArray()<1))
                  
                {
                    $this->setErrorMessage("902","One or more security users and groups is required when creating a permissions record");
                    return false;
                }
     			if ( $paramsObj->canCreate==NULL && $paramsObj->canRead==NULL && $paramsObj->canUpdate==NULL && $paramsObj->canDelete==NULL)           
     			{
               		$this->setErrorMessage("902","One or more of the fields 'create', 'read', 'update' and/or 'delete' is required when creating a permissions record");
               		return false;	
     			}
     			return true;
     		case "read":
             	if ( $paramsObj->canCreate!=NULL && $paramsObj->canRead!=NULL && $paramsObj->canUpdate!=NULL && $paramsObj->canDelete!=NULL)           
     			{
               		$this->setErrorMessage("902","The fields 'create', 'read', 'update' and 'delete' are invalid when reading permissions information.");
               		return false;	
     			}
     			return true;
            case "update":
                if($paramsObj->entityArray == NULL || count($paramsObj->entityArray<1))
                {
                    $this->setErrorMessage("902","One or more entities is required when updating a permissions record");
                    return false;
                }
                if( ($paramsObj->securityUserArray() == NULL) || count($paramsObj->securityUserArray()<1) && 
                    ($paramsObj->securityGroupArray() == NULL)|| count($paramsObj->securityGroupArray()<1)
                  )
                {
                    $this->setErrorMessage("902","One or more security users and groups are required when updating permissions records");
                    return false;
                }
     			if ( $paramsObj->canCreate==NULL && $paramsObj->canRead==NULL && $paramsObj->canUpdate==NULL && $paramsObj->canDelete==NULL)           
     			{
               		$this->setErrorMessage("902","One or more of the fields 'create', 'read', 'update' and/or 'delete' are required when updating permissions records");
               		return false;	
     			}
     			return true;        			
     			
       		case "delete":
             	if ( $paramsObj->canCreate!=NULL && $paramsObj->canRead!=NULL && $paramsObj->canUpdate!=NULL && $paramsObj->canDelete!=NULL)           
     			{
               		$this->setErrorMessage("902","The fields 'create', 'read', 'update' and 'delete' are invalid when deleting permissions information.");
               		return false;	
     			}
     			return true;
  			
        }
    }
    
    function deleteFromDB()
    {
        return false;
    }
    

// End of Class
} 
?>
