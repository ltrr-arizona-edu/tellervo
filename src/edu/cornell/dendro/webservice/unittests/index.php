<?php
require_once('curl.php');
require_once('/home/aps03pwb/wsauth.php');

$snippetDir = '/var/www/webservice/unittests/crudTestRequests/';
$wsURL = "https://dendro.cornell.edu/webservice.php";
$xmlrequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
    <corina>
      <request type=\"read\">
          <site id=\"1\">
                          </site>
                            </request>
                            </corina>";
$siteID = NULL;
$subSiteID = NULL;
$treeID = NULL;
$specimenID = NULL;
$radiusID = NULL;
$measurementID = NULL;
$fileArray = array();
$passedCount = 0;
$failedCount = 0;

// Setup Curl
$curl = new Curl;
$curl->options['CURLOPT_SSL_VERIFYPEER'] = FALSE;
$curl->options['CURLOPT_POST'] = TRUE;

// Authenticate
$curl->options['CURLOPT_POSTFIELDS'] = array('xmlrequest' => $authrequest);
$auth = $curl->post($wsURL);

if ($handle = opendir($snippetDir))
{
    while( false !== ($file = readdir($handle)))
    {
        $pathinfo = pathinfo($file);
        $extension = $pathinfo['extension'];
        if ($file!="." && $file !=".." && $extension=='xml')
        {
            array_push($fileArray, $snippetDir.$file);
        }
    }
}

sort($fileArray);

echo "<table width=100% border=1 ><tr><td width=\"150\"><b>Test Name</b></td><td width=\"90\"><b>Request Type</b></td><td width=\"70\"><b>Result</b></td><td width=\"70\"><b>Time</b></td><td><b>Error code</b></td></tr>";
foreach($fileArray as $file)
{
    $xml = file_get_contents($file);
    $xml = str_replace('===SITEID===', $siteID, $xml);
    $xml = str_replace('===SUBSITEID===', $subSiteID, $xml);
    $xml = str_replace('===TREEID===', $treeID, $xml);
    $xml = str_replace('===SPECIMENID===', $specimenID, $xml);
    $xml = str_replace('===RADIUSID===', $radiusID, $xml);

    $curl->options['CURLOPT_POSTFIELDS'] = array('xmlrequest' => $xml);
    $response = $curl->post($wsURL);
    $object = basename($snippetDir.$file, ".xml");
    $xml = simplexml_load_string($response);

    if (isset($xml->content->site['id'])) $siteID = $xml->content->site['id'];
    if (isset($xml->content->subSite['id'])) $subSiteID = $xml->content->subSite['id'];
    if (isset($xml->content->tree['id'])) $treeID = $xml->content->tree['id'];
    if (isset($xml->content->specimen['id'])) $specimenID = $xml->content->specimen['id'];
    if (isset($xml->content->radius['id'])) $radiusID = $xml->content->radius['id'];

    printResultFromXML($xml, $object);
    if($xml->header->status=="OK")
    {
        $passedCount++;
    }
    else
    {
        $failedCount++;
    }
}
echo "</table>";
echo "<hr>";
echo "Tests passed = $passedCount <br>";
echo "Tests failed = $failedCount <br>";
echo "<hr>";


function printResultFromXML($xml, $object)
{
    if ($xml->header->status=="OK")
    {
        printResult($xml->header->requesttype, substr($object, 4), True, $xml->header->queryTime);
        return TRUE;
    }
    else
    {
        printResult($xml->header->requesttype, substr($object, 4), FALSE, $xml->header->queryTime, $xml->header->message);
        return FALSE;
    }
}

function printResult($requesttype, $name, $result, $time, $error=NULL)
{
    echo "<tr><td>$name</td>";
    echo "<td>$requesttype</td>";
    if ($result)
    {
        echo "<td bgcolor=\"green\">Pass</td>";
    }
    else
    {
        echo "<td bgcolor=\"red\">Fail</td>";
    }
    echo "<td>$time</td>";
    echo "<td>$error</td>";
    ob_flush();
    flush();
}

?>
