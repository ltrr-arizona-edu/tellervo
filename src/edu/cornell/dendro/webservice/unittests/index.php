<?php
require_once('curl.php');
require_once('/home/aps03pwb/wsauth.php');
require_once('../inc/dbsetup.php');
require_once('../config.php');


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
$measurementID = array();
$siteNoteID = NULL;
$treeNoteID = NULL;
$vmeasurementNoteID = NULL;
$readingNoteID = NULL;
$fileArray = array();
$passedCount = 0;
$failedCount = 0;
$totalProcessingTime = 0;
global $dbconn;

// Start out by cleaning all unit test records left over by previous runs 
//$sql = "delete from tblvmeasurement where measurementid=(select measurementid from tblmeasurement where radiusid=(select radiusid from tblradius where name='UNITTEST'));
$sql = "delete from tblmeasurement where radiusid=(select radiusid from tblradius where name='UNITTEST');
delete from tblradius where name='UNITTEST';
delete from tblspecimen where name='UNITTEST';
delete from tbltree where name='UNITTEST';
delete from tblsubsite where name='UNITTEST';
delete from tblsubsite where name='UNITTEST2';
delete from tblsite where name='UNITTEST';
delete from tlkpsitenote where note='UNITTEST';
delete from tlkptreenote where note='UNITTEST';
delete from tlkpvmeasurementnote where note='UNITTEST';
delete from tlkpreadingnote where note='UNITTEST';";
$dbconnstatus = pg_connection_status($dbconn);
if ($dbconnstatus ===PGSQL_CONNECTION_OK)
{
    pg_send_query($dbconn, $sql);
}


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
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Corina Unit Tests</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="Content-Language" content="en-us" />
        <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" media="screen" href="http://dendro.cornell.edu//styles/screen.css" />
    </head>
    <body class="onecolumn">
<div id="cu-identity">
    <div id="cu-logo">
        <a href="http://www.cornell.edu/"><img src="/images/logos/cu_logo_unstyled.gif" alt="Cornell University" width="180" height="45" border="0" /></a>
    </div>
    <div id="cu-search">
        <a href="http://www.cornell.edu/search/">Search Cornell</a>
    </div>
</div>

<div id="header">
<div id="identity">
<h1>Cornell Tree-Ring Laboratory</h1>
<h2>The Malcolm and Carolyn Wiener Laboratory for Aegean and Near Eastern Dendrochronology</h2>
</div>
<div id="navigation">
</div>
</div>

<div id="wrap">
<div id="content">
<div id="main">
<div id="hub-description">

<h2>Corina Webservice Unit Tests</h2>
<table width=600 border=1>
<tr>
<td width="70"><b>No.</b></td>
<td width="150"><b>Object</b></td>
<td width="90"><b>Type</b></td>
<td width="70"><b>Result</b></td>
<td width="70"><b>Time</b></td>
<td width="150"><b>Error code</b></td>
</tr>


<?php
foreach($fileArray as $file)
{
    $xml = file_get_contents($file);
    $xml = str_replace('===SITEID===', $siteID, $xml);
    $xml = str_replace('===SUBSITEID===', $subSiteID, $xml);
    $xml = str_replace('===TREEID===', $treeID, $xml);
    $xml = str_replace('===SPECIMENID===', $specimenID, $xml);
    $xml = str_replace('===RADIUSID===', $radiusID, $xml);
    $xml = str_replace('===SITENOTEID===', $siteNoteID, $xml);
    $xml = str_replace('===TREENOTEID===', $treeNoteID, $xml);
    $xml = str_replace('===VMEASUREMENTNOTEID===', $vmeasurementNoteID, $xml);
    $xml = str_replace('===READINGNOTEID===', $readingNoteID, $xml);
    if(isset($measurementID[0])) $xml = str_replace('===MEASUREMENTID===', $measurementID[0], $xml);
    if(isset($measurementID[1])) $xml = str_replace('===MEASUREMENTID2===', $measurementID[1], $xml);
    if(isset($measurementID[2])) $xml = str_replace('===MEASUREMENTID3===', $measurementID[2], $xml);
    if(isset($measurementID[3])) $xml = str_replace('===MEASUREMENTID4===', $measurementID[3], $xml);
    if(isset($measurementID[4])) $xml = str_replace('===MEASUREMENTID5===', $measurementID[4], $xml);
    if(isset($measurementID[5])) $xml = str_replace('===MEASUREMENTID6===', $measurementID[5], $xml);

    $curl->options['CURLOPT_POSTFIELDS'] = array('xmlrequest' => $xml);
    $response = $curl->post($wsURL);
    $object = basename($snippetDir.$file, ".xml");
    $responsexml = simplexml_load_string($response);

    if (isset($responsexml->content->site['id'])) $siteID = $responsexml->content->site['id'];
    if (isset($responsexml->content->subSite['id'])) $subSiteID = $responsexml->content->subSite['id'];
    if (isset($responsexml->content->tree['id'])) $treeID = $responsexml->content->tree['id'];
    if (isset($responsexml->content->specimen['id'])) $specimenID = $responsexml->content->specimen['id'];
    if (isset($responsexml->content->radius['id']))
    {
        $radiusID = $responsexml->content->radius['id'];
    }
    if (isset($responsexml->content->siteNote['id'])) $siteNoteID = $responsexml->content->siteNote['id'];
    if (isset($responsexml->content->treeNote['id'])) $treeNoteID = $responsexml->content->treeNote['id'];
    if (isset($responsexml->content->measurementNote['id'])) $vmeasurementNoteID = $responsexml->content->measurementNote['id'];
    if (isset($responsexml->content->readingNote['id'])) $readingNoteID = $responsexml->content->readingNote['id'];
    if (isset($responsexml->content->measurement['id'])) array_push($measurementID, (int) $responsexml->content->measurement['id']);
    $measurementID = array_unique($measurementID);
    sort($measurementID);
    

    

    printResultFromXML($responsexml, $object, $xml);
    if($responsexml->header->status=="OK")
    {
        $passedCount++;
        $totalProcessingTime = $totalProcessingTime + (double) $responsexml->header->queryTime;
    }
    else
    {
        $failedCount++;
        $totalProcessingTime = $totalProcessingTime + (double) $responsexml->header->queryTime;
    }
}
echo "</table>";
echo "<hr>";
echo "<h3>Results</h3><ul>";
echo "<li>Tests passed = $passedCount </li>";
echo "<li>Tests failed = $failedCount </li>";
echo "<li>Processing time  = $totalProcessingTime seconds</li>";
echo "</ul><hr>";
echo "</div></div></div></div></body></html>";


function printResultFromXML($responsexml, $object, $requestxml)
{
    if ($responsexml->header->status=="OK")
    {
        printResult(substr($object, 0, 3), $responsexml->header->requesttype, substr($object, 4), True, $responsexml->header->queryTime, $requestxml, $responsexml->asXML());
        return TRUE;
    }
    else
    {
        printResult(substr($object, 0, 3), $responsexml->header->requesttype, substr($object, 4), FALSE, $responsexml->header->queryTime, $requestxml, $responsexml->asXML(), $responsexml->header->message);
        return FALSE;
    }
}

function printResult($testnumber, $requesttype, $name, $result, $time, $requestxml, $responsexml, $error=NULL)
{
    echo "<tr><td>$testnumber</td>";
    echo "<td>".ucfirst($name)."</td>";
    echo "<td>".ucfirst($requesttype)."</td>";
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
    echo "</tr>\n<!-- REQUEST = $requestxml-->\n<!-- RESPONSE = $responsexml-->\n";
    ob_flush();
    flush();
}

?>
