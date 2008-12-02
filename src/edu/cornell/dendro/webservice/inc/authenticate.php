<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************
require_once('dbhelper.php');

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

    function __construct()
    {
        // Constructor for this class.
    }

    /***********/
    /* SETTERS */
    /***********/

    function setErrorMessage($theCode, $theMessage)
    {
        // Set the error latest error message and code for this object.
        $this->lastErrorCode = $theCode;
        $this->lastErrorMessage = $theMessage;
    }

    function validateRequestParams($paramsObj, $crudMode)
    {
        // Check parameters based on crudMode 
        switch($crudMode)
        {
            case "plainlogin":
                if($paramsObj->username==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'username' field is required when doing a plain login");
                    return false;
                }
                if($paramsObj->password==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'password' field is required when doing a plain login");
                    return false;
                }
                return true;
            
            case "securelogin":
                if($paramsObj->username==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'username' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->hash==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'hash' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->cnonce==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'cnonce' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->snonce==NULL)
                {
                    $this->setErrorMessage("902","Missing parameter - 'snonce' field is required when doing a secure login");
                    return false;
                }
                if($paramsObj->seq==NULL)
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
    function doPlainAuthentication($paramsClass, $auth)
    {
        $myAuth = $auth;
        $myRequest = $paramsClass;

        $myAuth->login($myRequest->username, $myRequest->password);
        if($myAuth->isLoggedIn())
        {
            return True;
        }
        else
        {
            // Log in failed
            $this->setErrorMessage($myAuth->getLastErrorCode(), $myAuth->getLastErrorMessage());
        }
    }
    
    function doSecureAuthentication($paramsClass, $auth)
    {
        $myAuth = $auth;
        $myRequest = $paramsClass;
        
        $myAuth->loginWithNonce($myRequest->username, $myRequest->hash, $myRequest->cnonce, $myRequest->snonce, $myRequest->seq);
        
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

    function setNonce($paramsClass, $auth)
    {
        $myAuth = $auth;
        $myRequest = $paramsClass;

        $seq = $myAuth->sequence();
        $this->xmldata = "<nonce seq=\"".$seq."\">".$myAuth->nonce($seq)."</nonce>";
        
    }

    function asXML($mode="all")
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

    function asKML($mode="all")
    {
    }

    function getParentTagBegin()
    {
    }

    function getParentTagEnd()
    {
    }

    function getLastErrorCode()
    {
        // Return an integer containing the last error code recorded for this object
        $error = $this->lastErrorCode; 
        return $error;  
    }

    function getLastErrorMessage()
    {
        // Return a string containing the last error message recorded for this object
        $error = $this->lastErrorMessage;
        return $error;
    }

    /***********/
    /*FUNCTIONS*/
    /***********/

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

    function getLowestRelationshipLevel($theRequest)
    {
        // This function returns an interger representing the most junior level of relationship required in this query
        // tblsite         -- 6 -- most senior
        // tblsubsite      -- 5 --
        // tblelement         -- 4 --
        // tblsample     -- 3 --
        // tblradius       -- 2 --
        // tblseries  -- 1 -- most junior
        
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

    function getHighestRelationshipLevel($theRequest)
    {
        // This function returns an interger representing the most senior level of relationship required in this query
        // tblsite         -- 6 -- most senior
        // tblsubsite      -- 5 --
        // tblelement         -- 4 --
        // tblsample     -- 3 --
        // tblradius       -- 2 --
        // tblseries  -- 1 -- most junior

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

    function getRelationshipSQL($theRequest)
    {
        // Returns the 'where' clause part of the query SQL for the table relationships 

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
