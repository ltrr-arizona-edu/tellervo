<?php

function setXMLErrors($myMetaHeader, $errors)
{
print_r($errors);

    foreach ($errors as $error)
    {
        $message=  "XML Error  - ".trim($error->message).". Error located on line ".$error->line.", column ".$error->column.".";
        $myMetaHeader->setMessage("905", $message);
    }
}

/**
 * Get the XML fragment from a DOMNodeList as either an XML string or a DomDocument
 *
 * @param DOMNodeList $tags
 * @param String $format one of string or dom
 * @return Mixed string or dom
 */
function getXMLFragment($tags, $format='string')
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

?>
