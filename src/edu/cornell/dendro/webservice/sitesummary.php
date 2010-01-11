<?php
//*******************************************************************
////// PHP Corina Middleware
////// License: GPL
////// Author: Peter Brewer
////// E-Mail: p.brewer@cornell.edu
//////
////// Requirements : PHP >= 5.0
//////*******************************************************************

require_once("config.php");
require_once("inc/dbsetup.php");
require_once("inc/dbhelper.php");
require_once("inc/meta.php");
require_once("inc/auth.php");
require_once("inc/request.php");
require_once("inc/parameters.php");
require_once("inc/search.php");
//require_once("inc/errors.php");
require_once("inc/output.php");
require_once("inc/olStyles.php");

require_once("inc/object.php");
require_once("inc/element.php");
require_once("inc/sample.php");
require_once("inc/radius.php");
require_once("inc/measurement.php");
require_once("inc/authenticate.php");

$xmldata = NULL;
$myAuth         = new auth();
$myMetaHeader   = new meta();

// Intercept login requests
if($_POST['requesturl'])
{
	global $domain;
	
	$myAuth->login($_POST['user'], $_POST['password']);
	
	$redirect = "Location: https://dendro.cornell.edu".$_POST['requesturl'];
	header( $redirect ) ;
}

// Get GET parameters
$site = $_GET['site'];

// Do basic error checking and set defaults
if($site==null) 
{
	echo "no site";
	die();
}

//$firebug->log($site, "Site requested");

$myMetaHeader->setRequestType('read');


// Check authentication and request login if necessary
if($myAuth->isLoggedIn())
{
    $myMetaHeader->setUser($myAuth->getUsername(), $myAuth->getFirstname(), $myAuth->getLastname(), $myAuth->getID());
}
else
{
	echo "<html>
		<link rel=\"stylesheet\" href=\"css/weblogin.css\" type=\"text/css\" />
	<link rel=\"stylesheet\" href=\"css/openLayersStyle.css\" type=\"text/css\" />
	<div id=\"weblogin\"><h1>Corina login:</h1>
	<br/>
	<form method=\"POST\">
	<table>
	<tr><th>Username: </td><td><input type=\"text\" size=\"25\" name=\"user\"></td></tr>
	<tr><th>Password: </td><td><input type=\"password\" size=\"25\" name=\"password\"></td></tr>
	<tr><td></td><td><input type=\"submit\" value=\"Login\"/></td></tr>
	</table>
	<input type=\"hidden\" name=\"requesturl\" value=\"".$_SERVER['REQUEST_URI']."\"/>
	</form>
	</div>";
	
    $seq = $myAuth->sequence();
    $myMetaHeader->requestLogin($myAuth->nonce($seq), $seq);
    die();
}

$xmldata = "";

/*$sampleSQL = "select 
objectcode||'-'||elementcode||'-'||samplecode as code,
elementcode::interval
from
vwcomprehensivevm
where
objectcode='$site'
group by objectcode||'-'||elementcode||'-'||samplecode, elementcode::interval
order by elementcode::interval ASC
";*/

$sampleSQL= "select
o.code as objectcode,
e.code as elementcode,
s.code as samplecode
from
tblobject o 
left join tblelement e on e.objectid = o.objectid
left join tblsample s on s.elementid = e.elementid
where
o.code='$site' 
order by objectcode, elementcode, samplecode";


?>

<html>
		<link rel="stylesheet" href="css/cnc.css" type="text/css" />

	
<table border="0"><thead><tr><th></th><th colspan="3">Radius A</th><th colspan="3">Radius B</th><th colspan="3">Radius C</th></tr>
<tr><th>Sample</th><th>1st Reading</th><th>2nd Reading</th><th>3rd Reading</th><th>1st Reading</th><th>2nd Reading</th><th>3rd Reading</th><th>1st Reading</th><th>2nd Reading</th><th>3rd Reading</th></tr>
</thead><tbody>
<?php


global $dbconn;


// DO SEARCH
//$firebug->log($sampleSQL, "Sample SQL");
   $result = pg_query($dbconn, $sampleSQL);

   $i = 0;
   $prevSample = null;
   $prevRadius = null;
   $prevSeries = null;
   $newSample = true;
   
   while ($row = pg_fetch_array($result))
   {
   	  $readingCount = 0;
   		$radiusArray = array("A", "B", "C");
   	   echo "<tr><th>".$row['objectcode']."-".$row['elementcode']."-".$row['samplecode']."</th>";
   	   	   
   	 
   	   foreach ($radiusArray as $radiusCode)
   	   { 	
   	   		
   	   		/*$seriesSQL = "select 
					objectcode,
					elementcode,
					samplecode,
					radiuscode,
					code as seriescode,
					measuredbyid,
					su.lastname as measuredbyname,
					createdtimestamp,
					readingcount
					from
					vwcomprehensivevm
					left join tblsecurityuser su on su.securityuserid=measuredbyid
					where
					objectcode||'-'||elementcode||'-'||samplecode='".$row['code']."'
					and radiuscode='$radiusCode'
					order by objectcode, elementcode, samplecode, radiuscode, code
					limit 3"; */
					
   	   		$seriesSQL = "select 
					objectcode,
					elementcode,
					samplecode,
					radiuscode,
					code as seriescode,
					measuredbyid,
					su.lastname as measuredbyname,
					createdtimestamp,
					readingcount
					from
					vwcomprehensivevm
					left join tblsecurityuser su on su.securityuserid=measuredbyid
					where
					objectcode='".$row['objectcode']."' 
   	   				and elementcode='".$row['elementcode']."' 
   	   				and samplecode='".$row['samplecode']."' 
					and radiuscode='$radiusCode'
					order by objectcode, elementcode, samplecode, radiuscode, code
					limit 3"; 					
   	   		
					
					//$firebug->log($seriesSQL, "Series SQL");
					$seriesresult = pg_query($dbconn, $seriesSQL);
			
					while ($seriesrow = pg_fetch_array($seriesresult))
					{
							echo "<td>";
							echo $seriesrow['measuredbyname']."<br>".$seriesrow['readingcount']." rings<br>";
							if($seriesrow['createdtimestamp']>'2000-01-01') echo date('d/m/Y', strtotime($seriesrow['createdtimestamp']));
							echo "</td>";
							$readingCount++;
						
					}			

					if($readingCount<3) 
		   	   		while($readingCount<3)
					{
						echo "<td></td>";
						$readingCount++;
					}
					
   	   				if($readingCount<6 && $readingCount>3) 
		   	   		while($readingCount<6)
					{
						echo "<td></td>";
						$readingCount++;
					}
   	      	   		
					if($readingCount<9 && $readingCount>6) 
		   	   		while($readingCount<9)
					{
						echo "<td></td>";
						$readingCount++;
					}					
					
   	   }
   	   
		while($readingCount<9)
		{
			echo "<td></td>";
			$readingCount++;
		}
		echo "\n";
   	   doflush();
    }

    ?>
    
    </tbody></table></html>
    
    <?php 
    
    
function doflush (){
    echo(str_repeat(' ',256));
    // check that buffer is actually set before flushing
    if (ob_get_length()){           
        @ob_flush();
        @flush();
        @ob_end_flush();
    }   
    @ob_start();
}
?>
        

