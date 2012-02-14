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

class permission extends permissionEntity implements IDBAccessor
{
	protected $thisParamsClass = NULL;
	
    /***************/
    /* CONSTRUCTOR */
    /***************/

    function permission()
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
    	$this->thisParamsClass = $paramsClass;
	return TRUE;
    }
    
    function setParamsFromDB($id)
    {
        return TRUE;
    }

    function setChildParamsFromDB()
    {
	return TRUE;
    }

        
    function asXML()
    {
	global $dbconn;
	global $firebug;
    	
	$xml = "";
	
	$firebug->log($this->thisParamsClass, "Params class prior to outputting as XML");
	foreach($this->thisParamsClass->securityUserArray as $user)
	{
    	   foreach($this->thisParamsClass->entityArray as $entity)
    	   {
		if($entity['id']==null)
		{
		   $entity['id']=='aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa';
		}
		if($entity['type']=='default')
		{		
			$sql = "select * from tblsecuritydefault where securityuserid";
		}
		else
		{
			$sql = "select * from cpgdb.getuserpermissionset('$user', '".$entity['type']."', '".$entity['id']."'::uuid)";
		}
	                    
		pg_send_query($dbconn, $sql);
	        $result = pg_get_result($dbconn);
		    if(pg_num_rows($result)==0)
		    {
			// No records match
			trigger_error("903"."No records match the specified permissions request. $sql", E_USER_ERROR);
			return FALSE;
		    }
		    else
		    {
			// Set parameters from db
			$row = pg_fetch_array($result);
			$xml.= "<permission decidedBy=\"".$row['decidedby']."\">\n";
			$xml.= "<permissionToCreate>".dbhelper::formatBool($row['cancreate'], "english")."</permissionToCreate>\n";
			$xml.= "<permissionToRead>".dbhelper::formatBool($row['canread'], "english")."</permissionToRead>\n";
			$xml.= "<permissionToUpdate>".dbhelper::formatBool($row['canupdate'], "english")."</permissionToUpdate>\n";
			$xml.= "<permissionToDelete>".dbhelper::formatBool($row['candelete'], "english")."</permissionToDelete>\n";
			$xml.= "<permissionDenied>".dbhelper::formatBool($row['denied'], "english")."</permissionDenied>\n";
	    	        $xml.= "<entity type=\"".$entity['type']."\" id=\"".$entity['id']."\"/>\n";	
		   	$xml.= "<securityUser id=\"".$user."\"/>\n";
	    		$xml.= "</permission>\n";    	
		    }
  	   }
	}	

	foreach($this->thisParamsClass->securityGroupArray as $group)
	{
    	   foreach($this->thisParamsClass->entityArray as $entity)
    	   {

		if($entity['id']=='')
		{
		   $firebug->log("id is null so setting dummy uuid");
		   $sql = "select * from cpgdb.getgrouppermissionset('{".$group."}', '".$entity['type']."', 'aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa'::uuid)";

		}
		else
		{
		   
		   $sql = "select * from cpgdb.getgrouppermissionset('{".$group."}', '".$entity['type']."', '".$entity['id']."'::uuid)";
		}
	
	                    
		pg_send_query($dbconn, $sql);
	        $result = pg_get_result($dbconn);
		    if(pg_num_rows($result)==0)
		    {
			// No records match
			trigger_error("903"."No records match the specified permissions request. $sql", E_USER_ERROR);
			return FALSE;
		    }
		    else
		    {
			// Set parameters from db
			$row = pg_fetch_array($result);
			$xml.= "<permission decidedBy=\"".$row['decidedby']."\">\n";
			$xml.= "<permissionToCreate>".dbhelper::formatBool($row['cancreate'], "english")."</permissionToCreate>\n";
			$xml.= "<permissionToRead>".dbhelper::formatBool($row['canread'], "english")."</permissionToRead>\n";
			$xml.= "<permissionToUpdate>".dbhelper::formatBool($row['canupdate'], "english")."</permissionToUpdate>\n";
			$xml.= "<permissionToDelete>".dbhelper::formatBool($row['candelete'], "english")."</permissionToDelete>\n";
			$xml.= "<permissionDenied>".dbhelper::formatBool($row['denied'], "english")."</permissionDenied>\n";
	    	        $xml.= "<entity type=\"".$entity['type']."\" id=\"".$entity['id']."\"/>\n";	
		   	$xml.= "<securityGroup id=\"".$group."\"/>\n";
	    		$xml.= "</permission>\n";    	
		    }
  	   }
	}	

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
	$this->deleteFromDB();

    	global $dbconn;
        global $firebug;
	$firebug->log("Starting permissions write to db");
	$firebug->log($this->thisParamsClass, "Writing this params class to db");
                
    	foreach($this->thisParamsClass->entityArray as $entity)
    	{
		$firebug->log($entity, "Permissions being written for this entity");
    		// Entity has to be of a certain type otherwise continue;
    		if ($entity["type"]!='object' && $entity["type"]!='element' && $entity["type"]!='measurement' && $entity['type']!='default')
    		{
			$firebug->log("Unsupported entity for permissions. Ignoring");
    			continue;
    		}
    		
    		foreach($this->thisParamsClass->securityUserArray as $user)
    		{
    			// Directly adding permissions to users is not yet supported
			$firebug->log("Assigning permissions by user not yet supported");
				continue;
    		}
    		
    		foreach($this->thisParamsClass->securityGroupArray as $group)
    		{
		    $firebug->log($group, "Permissions being written for this group");
		    $canCreate = $this->thisParamsClass->canCreate();
		    $firebug->log($canCreate, "Doing 'create'");

    		    if($this->thisParamsClass->canCreate===TRUE)
    			{
				$firebug->log("Creating SQL for 'create' permission");
				
				if($entity['type']=='default')
				{
					$sql = "INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('$group','3'); ";
				}
				else
    				{
					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','3'); ";
				}

				$firebug->log($sql, "Permission write sql");
    			        // Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }
    			}
    		    if($this->thisParamsClass->canRead===TRUE )
    			{
				if($entity['type']=='default')
				{
					$sql = "INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('$group','2'); ";
				}
				else
				{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','2'); ";
				}

				$firebug->log($sql, "Permission write sql");
    			    	// Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }    					
    			}
    		    if($this->thisParamsClass->canUpdate===TRUE )
    			{
				if($entity['type']=='default')
				{
					$sql = "INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('$group','4'); ";
				}
				else
				{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','4'); ";
				}
				$firebug->log($sql, "Permission write sql");
    			    	// Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }    					
    			}
    		    if($this->thisParamsClass->canDelete===TRUE)
    			{
				if($entity['type']=='default')
				{
					$sql = "INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('$group','5'); ";
				}
				else
				{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','5'); ";
				}
				$firebug->log($sql, "Permission write sql");
    			    	// Run SQL 
	                    pg_send_query($dbconn, $sql);
	                    $result = pg_get_result($dbconn);
	                    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
	                    {
	                        $this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
	                        return FALSE;
	                    }    					
    			}    			
    		    if($this->thisParamsClass->isPermissionDenied()===TRUE)
    			{
				if($entity['type']=='default')
				{
					$sql = "INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('$group','6'); ";
				}
				else
				{
    					$sql = "INSERT INTO tblsecurity".$entity["type"]." (".$entity["type"]."id, securitygroupid, securitypermissionid) VALUES ";
    					$sql .=" ('".$entity["id"]."','$group','6'); ";
				}
				$firebug->log($sql, "Permission write sql");
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
	return TRUE;
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
                if($paramsObj->entityArray == NULL || count($paramsObj->entityArray)<1)
                {
                    $this->setErrorMessage("902","One or more entities is required when creating a permissions record");
                    return false;
                }
                if( (($paramsObj->securityUserArray == NULL) || count($paramsObj->securityUserArray)<1)   && 
                    (($paramsObj->securityGroupArray == NULL) || count($paramsObj->securityGroupArray)<1)
                  )
                  
                {
                    $this->setErrorMessage("902","One or more security users and groups is required when creating a permissions record");
                    return false;
                }
     			if ( !isset($paramsObj->canCreate) && !isset($paramsObj->canRead) && !isset($paramsObj->canUpdate) && !isset($paramsObj->canDelete))           
     			{
               		$this->setErrorMessage("902","One or more of the fields 'create', 'read', 'update' and/or 'delete' is required when creating a permissions record");
               		return false;	
     			}
     			return true;
     		case "read":
             	if ( $paramsObj->canCreate!='nullvalue' && $paramsObj->canRead!='nullvalue' && $paramsObj->canUpdate!='nullvalue' && $paramsObj->canDelete!='nullvalue')           
     			{
               		$this->setErrorMessage("902","The fields 'create', 'read', 'update' and 'delete' are invalid when reading permissions information.");
               		return false;	
     			}
     			return true;
            case "update":
                if($paramsObj->entityArray == NULL || count($paramsObj->entityArray)<1)
                {
                    $this->setErrorMessage("902","One or more entities is required when updating a permissions record");
                    return false;
                }
                if( (($paramsObj->securityUserArray == NULL) || count($paramsObj->securityUserArray)<1)   && 
                    (($paramsObj->securityGroupArray == NULL) || count($paramsObj->securityGroupArray)<1)
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

	global $dbconn;
	global $firebug;
	$firebug->log("deleteFromDB() called");

	foreach($this->thisParamsClass->securityGroupArray as $group)
	{
    	   foreach($this->thisParamsClass->entityArray as $entity)
    	   {
		if($entity['type']=='default')
		{
			$sql = "DELETE FROM tblsecuritydefault WHERE securitygroupid='$group'";
		}
		else
		{
			$sql = "DELETE FROM tblsecurity".$entity['type']." WHERE ".$entity['type']."id='".$entity['id']."' AND securitygroupid='$group'";
		}
		$firebug->log($sql, "Delete existing permissions records");
	    pg_send_query($dbconn, $sql);
	    $result = pg_get_result($dbconn);
		    if(pg_result_error_field($result, PGSQL_DIAG_SQLSTATE))
		    {
			$this->setErrorMessage("002", pg_result_error($result)."--- SQL was $sql");
			return FALSE;
		    }

		
	   }
        }

	


        return false;
    }

    function mergeRecords($id)
    {
        return false;
    }
    

// End of Class
} 
?>
