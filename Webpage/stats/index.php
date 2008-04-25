<?php


$title = "Database Statistics";
$news = "";
$vertnavbarfilename = "../phpinc/vert-navbar.php";

include('../phpinc/hub-header.php');
?>

<p>Below are some preliminary statistics about the evolving Corina database.  Until the database is complete these statistics will not be representative of the labs data holdings</p>

<table width="100%">
<tr>
<td>
<?

include_once '../phpinc/ofc-library/open_flash_chart_object.php';
open_flash_chart_object( 350, 200, 'http://'. $_SERVER['SERVER_NAME'] .'/stats/data/bytaxon.php', false, '../flash/' );
?>
</td>
<td>
<?
open_flash_chart_object( 350, 200, 'http://'. $_SERVER['SERVER_NAME'] .'/stats/data/byregion.php', false, '../flash/' );
?>
</td>
</tr>
</table>
<?
open_flash_chart_object( 740, 300, 'http://'. $_SERVER['SERVER_NAME'] .'/stats/data/progress.php', false, '../flash/' );
?>
<?
include('../phpinc/hub-footer.php');

?>
