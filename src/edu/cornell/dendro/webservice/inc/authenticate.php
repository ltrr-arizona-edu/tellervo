<?php
/**
 * *******************************************************************
 * PHP Corina Middleware
 * E-Mail: p.brewer@cornell.edu
 * Requirements : PHP >= 5.0
 * 
 * @author Peter Brewer
 * @license http://opensource.org/licenses/gpl-license.php GPL
 * @package CorinaWS
 * *******************************************************************
 */


require_once('dbhelper.php');

/**
 * This class handles authentication requests from the user
 *
 */
class authenticate 
{
    var $id = NULL;
    var $xmldata = NULL;
    var $lastErrorCode = NULL;
    var $lastErrorMessage = NULL;
    var $sqlcommand = NULL;

    /***************/
    /* CONSTRUCTOR */
    /***************/

	/**
	 * Constructor for this class
	 *
	 */
    function __construct()
    {
    }

    /***********/
    /* SETTERS */
    /***********/

    /**
     * Raise an error
     *
     * @param Integer $theCode
     * @param String $theMessage
     */
    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    /**
     * Validate the parameters passed depending on the 'CRUD' mode.
     * In this case $crudMode should be one of 'plainlogin', 'nonce'
     * or 'securelogin'.
     *
     * @param Parameters class $paramsObj
     * @param String $crudMode
     * @return Boolean
     */
    public function validateRequestParams($paramsObj)
    {
    	global $myRequest;  	
    	
        switch($myRequest->getCrudMode())
        {
            case "plainlogin":
                if($paramsObj->getUsername()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'username' field is required when doing a plain login");
                    return false;
                }
                if($paramsObj->getPassword()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'password' field is required when doing a plain login");
                    return false;
                }
                return true;
            
            case "securelogin":
                if($paramsObj->getUsername()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'username' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->hash==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'hash' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->getCNonce()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'cnonce' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->getSNonce()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'snonce' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->getSeq()==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'seq' field is required when doing a secure login");
                    return false;
                }
                return true;

            case "nonce":
                return true;
            
            default:
                $this->setErrorMessage("667", "Program bug - invalid crudMode specified when validating request");
                return false;
        }
    }

    /***********/
    /*ACCESSORS*/
    /***********/
    
    /**
     * Do a 'plain' authentication
     *
     * @param Parameters Class $paramsClass
     * @param Auth Class $auth
     * @return Boolean
     */
    public function doPlainAuthentication($paramsClass, $auth)
    {
        $myAuth = $auth;
        $myRequest = $paramsClass;

        $myAuth->login($myRequest->getUsername(), $myRequest->getPassword());
        if($myAuth->isLoggedIn())
        {
            return True;
        }
        else
        {
            // Log in failed
            $this->setErrorMessage($myAuth->getLastErrorCode(), $myAuth->getLastErrorMessage());
            return False;
        }
    }
    
    /**
     * Do a secure authentication 
     *
     * @param Params Class $paramsClass
     * @param Auth Class $auth
     * @return Boolean
     */
    public function doSecureAuthentication($paramsClass, $auth)
    {
        $myAuth = $auth;
        $myRequest = $paramsClass;
        
        $myAuth->loginWithNonce($myRequest->getUsername(), $myRequest->getHash(), $myRequest->getCNonce(), $myRequest->getSNonce(), $myRequest->getSeq());
        
        if($myAuth->isLoggedIn())
        {
            // Log in worked
            return true;
        }
        else
        {
            // Log in failed
            $this->setErrorMessage($myAuth->getLastErrorCode(), $myAuth->getLastErrorMessage());
            return false;
        }
    }

    /**
     * Set the nonce 
     *
     * @param Params Class $paramsClass
     * @param Auth Class $auth
     */
    public function setNonce($paramsClass, $auth)
    {
        $myAuth = $auth;
        $myRequest = $paramsClass;

        $seq = $myAuth->sequence();
        $this->xmldata = "<nonce seq=\"".$seq."\">".$myAuth->nonce($seq)."</nonce>";
        
    }

    /**
     * Get the XML representation of this class
     *
     * @return String
     */
    public function asXML($mode="all")
    {
        if(isset($this->xmldata))
        {
            return $this->xmldata;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get code for the most recent error
     *
     * @return Integer
     */
    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }
    
    /**
     * Get the message for the most recent error
     *
     * @return String
     */
    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
    }

    /***********/
    /*FUNCTIONS*/
    /***********/

    /**
     * Get an SQL representation of the array of parameters
     *
     * @param Array  $paramsArray
     * @param String $paramName
     * @return String
     */
    function paramsToFilterSQL($paramsArray, $paramName)
    {
        $filterSQL="";
        foreach($paramsArray as $param)
        {
            // Set operator
            switch ($param['operator'])
            {
            case ">":
                $operator = ">";
                $value = " '".$param['value']."'";
                break;
            case "<":
                $operator = "<";
                $value = " '".$param['value']."'";
                break;
            case "=":
                $operator = "=";
                $value = " '".$param['value']."'";
                break;
            case "!=":
                $operator = "!=";
                $value = " '".$param['value']."'";
                break;
            case "like":
                $operator = "ilike";
                $value = " '%".$param['value']."%'";
                break;
            default :
                $operator = "=";
                $value = " '".$param['value']."'";
            }
            $filterSQL .= $this->tableName($paramName).".".$param['name']." ".$operator.$value." and ";
        }

        return $filterSQL;
    }

    /**
     * Convert a variable name to it's database alias.
     *
     * @param String $objectName
     * @return String
     */
    function variableName($objectName)
    {
        switch($objectName)
        {
        case "site":
            return "site";
            break;
        case "subsite":
            return "subsite";
            break;
        case "element":
            return "element";
            break;
        case "sample":
            return "sample";
            break;
        case "radius":
            return "radius";
            break;
        case "series":
            return "vseries";
            break;
        default:
            return false;
        }

    }

    /**
     * Covert a variable name to it's database table alias
     *
     * @param String $objectName
     * @return String
     */
    function tableName($objectName)
    {
        switch($objectName)
        {
        case "site":
            return "vwtblsite";
            break;
        case "subsite":
            return "vwtblsubsite";
            break;
        case "element":
            return "vwtblelement";
            break;
        case "sample":
            return "vwtblsample";
            break;
        case "radius":
            return "vwtblradius";
            break;
        case "series":
            return "vwvseries";
            break;
        default:
            return false;
        }

    }

    /**
     * This function returns an interger representing the most junior level of relationship required in this query
     * tblsite     -- 6 -- most senior
     * tblsubsite  -- 5 --
     * tblelement  -- 4 --
     * tblsample   -- 3 --
     * tblradius   -- 2 --
     * tblseries   -- 1 -- most junior
     *
     * @param String $theRequest
     * @return Integer
     */
    function getLowestRelationshipLevel($theRequest)
    {
        $myRequest = $theRequest;
        
        if (($myRequest->seriesParamsArray) || ($myRequest->returnObject == 'series'))
        {
            return 1;
        }
        elseif (($myRequest->radiusParamsArray) || ($myRequest->returnObject == 'radius'))
        {
            return 2;
        }
        elseif (($myRequest->sampleParamsArray) || ($myRequest->returnObject == 'sample'))
        {
            return 3;
        }
        elseif (($myRequest->elementParamsArray) || ($myRequest->returnObject == 'element'))
        {
            return 4;
        }
        elseif (($myRequest->subsiteParamsArray) || ($myRequest->returnObject == 'subsite'))
        {
            return 5;
        }
        if (($myRequest->siteParamsArray) || ($myRequest->returnObject == 'site'))
        {
            return 6;
        }
        else
        {
            return false;
        }
    }

    /**
     * This function returns an interger representing the most senior level of relationship required in this query
     * tblsite     -- 6 -- most senior
     * tblsubsite  -- 5 --
     * tblelement  -- 4 --
     * tblsample   -- 3 --
     * tblradius   -- 2 --
     * tblseries   -- 1 -- most junior   
     *
     * @param String $theRequest
     * @return String
     */
    function getHighestRelationshipLevel($theRequest)
    {
        $myRequest = $theRequest;

        if (($myRequest->siteParamsArray) || ($myRequest->returnObject == 'site'))
        {
            return 6;
        }
        elseif (($myRequest->subsiteParamsArray) || ($myRequest->returnObject == 'subsite'))
        {
            return 5;
        }
        elseif (($myRequest->elementParamsArray) || ($myRequest->returnObject == 'element'))
        {
            return 4;
        }
        elseif (($myRequest->sampleParamsArray) || ($myRequest->returnObject == 'sample'))
        {
            return 3;
        }
        elseif (($myRequest->radiusParamsArray) || ($myRequest->returnObject == 'radius'))
        {
            return 2;
        }
        elseif (($myRequest->seriesParamsArray) || ($myRequest->returnObject == 'series'))
        {
            return 1;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the 'where' clause part of the query SQL for the table relationships 
     *
     * @param String $theRequest
     * @return String
     */
    function getRelationshipSQL($theRequest)
    {
        $myRequest = $theRequest;
        $lowestLevel  = $this->getLowestRelationshipLevel($myRequest);
        $highestLevel = $this->getHighestRelationshipLevel($myRequest);
        $sql ="";

        //echo "high = $highestLevel\n";
        //echo "low = $lowestLevel\n";

        if (($lowestLevel==1) && ($highestLevel>=1))
        {
            $sql .= "vwvseries.seriesid=tblseries.seriesid and ";
        }
        if (($lowestLevel<=1) && ($highestLevel>1))
        {
            $sql .= "vwvseries.radiusid=vwtblradius.radiusid and ";
        }
        if (($lowestLevel<=2) && ($highestLevel>2))
        {
            $sql .= "vwtblradius.sampleid=vwtblsample.sampleid and ";
        }
        if (($lowestLevel<=3) && ($highestLevel>3))
        {
            $sql .= "vwtblsample.elementid=vwtblelement.elementid and ";
        }
        if (($lowestLevel<=4) && ($highestLevel>4))
        {
            $sql .= "vwtblelement.subsiteid=vwtblsubsite.subsiteid and ";
        }
        if (($lowestLevel<=5) && ($highestLevel>5))
        {
            $sql .= "vwtblsubsite.siteid=vwtblsite.siteid and ";
        }

        return $sql;
    }



// End of Class
} 
?>
