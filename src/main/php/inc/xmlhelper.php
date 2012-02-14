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


/*
function setXMLErrors($myMetaHeader, $errors)
{


    foreach ($errors as $error)
    {
        $message=  "XML Error  - ".trim($error->message).". Error located on line ".$error->line.", column ".$error->column.".";
        $myMetaHeader->setMessage("905", $message);
    }
}
*/
class xmlHelper
{
	/**
	 * Get the XML fragment from a DOMNodeList as either an XML string or a DomDocument
	 *
	 * @param DOMNodeList $tags
	 * @param String $format one of string or dom
	 * @return Mixed string or dom
	 */
	static public function getXMLFragment($tags, $format='string')
	{
	   	$tmp_doc = new DOMDocument();
	   	   	
	   	foreach ($tags as $paramtag)
	   	{
	   		$paramtag = $tmp_doc->importNode($paramtag, true);
	   		$tmp_doc->appendChild($paramtag); 		
	   	}
	   	
	   	switch(strtolower($format))
	   	{
	   		case "string":
	   			return $tmp_doc->saveXML();
	   		case "dom":
	   			return $tmp_doc;
	   		default:
	   			trigger_error("666"."Unknown format type in getXMLFragment()", E_USER_ERROR);
	   			return false;
	   	}
	}
	
}


class dateHelper
{
	static public function getGregorianSuffixFromSignedYear($year)
	{
		if($year==NULL) return NULL;

		if($year>0) 
		{
			return "AD";
		}
		elseif($year<=0) 
		{
			return "BC";	
		}
	}
	
	static public function getGregorianYearNumberFromSignedYear($year)
	{
		if($year>0) return $year;
		if($year<=0) return 1 - $year;
		
		trigger_error("910"."Invalid year supplied to getGregorianYearNumberFromSignedYear()", E_USER_ERROR);
		return false;
	}
	
	static public function getGregorianYearFromBPYear($yearbp, $includesuffix=true)
	{
		if((strtolower(substr($yearbp, -2)))=='bp') $yearbp = substr($yearbp, 0, -2); 	
				
		$outyear = 1950 - $yearbp;
		
		if($includesuffix===TRUE)
		{
			return $outyear.dateHelper::getGregorianSuffixFromSignedYear($outyear);
		}
		else
		{
			return $outyear;
		}
	}
	
	static public function getBPYearFromGregorianYear($year, $includesuffix=true)
	{
		$inyear = substr($year, 0, -2);
		
		switch(strtolower(substr($yearbp, -2)))
		{
			case "bc": 
				$outyear = 1950 + $inyear;
				break;
			case "ad":
				if($inyear > 1950) trigger_error("910". $outyear."AD can not be translated into a BP date as BP date must be before 1950", E_USER_ERROR); return false;
				$outyear = 1950 - $inyear;
				break; 
			default : trigger_error("910"."Unknown year suffix in getBPYearFromGregorianYear()", E_USER_ERROR); return false;
		}
				
		if($includesuffix===TRUE)
		{
			return $outyear."BP";
		}
		else
		{
			return $outyear;
		}		
	}
	
	static public function getSignedYearFromYearWithSuffix($yearwithsuffix, $outputformat="gregorian")
	{
		$suffix = strtolower(substr($yearwithsuffix, -2));
		$year = (int) substr($yearwithsuffix, 0, -2);
		
		if($outputformat="gregorian")
		{
			switch($suffix)
			{
				case "bc": return 0 - $year;
				case "ad": return $year;
				case "bp": return dateHelper::getGregorianYearFromBPYear($year, false);
				default: trigger_error("910"."Year supplied to getSignedYearFromYearWithSuffix() contained an unknown suffix", E_USER_ERROR);
			}
		}
		elseif($outputformat="bp")
		{
			switch($suffix)
			{
				case "bc": return dateHelper::getBPYearFromGregorianYear($yearwithsuffix, false);
				case "ad": return dateHelper::getBPYearFromGregorianYear($yearwithsuffix, false);
				case "bp": return $year;
				default: trigger_error("910"."Year supplied to getSignedYearFromYearWithSuffix() contained an unknown suffix", E_USER_ERROR);
			}			
		}
		else
		{
			trigger_error("910". "Unknown year format in getSignedYearFromYearWithSuffix()", E_USER_ERROR);
			return false;
		}
		
	}
}
?>
