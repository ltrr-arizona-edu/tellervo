<?php

include_once('/var/www/webservice/inc/dbsetup.php');

$data = array();
$labels = array();
$sql = "select count(*), regionid from tblsiteregion group by regionid order by regionid asc";
$dbconnstatus = pg_connection_status($dbconn);
if ($dbconnstatus ===PGSQL_CONNECTION_OK)
{
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result))
    {
        array_push($data, $row['count']);
    }
}
$sql = "select distinct(tblsiteregion.regionid), tblregion.regionname from tblsiteregion, tblregion where tblsiteregion.regionid=tblregion.regionid order by tblsiteregion.regionid asc";
$dbconnstatus = pg_connection_status($dbconn);
if ($dbconnstatus ===PGSQL_CONNECTION_OK)
{
    $result = pg_query($dbconn, $sql);
    while ($row = pg_fetch_array($result))
    {
        array_push($labels, $row['regionname']);
    }
}

   include_once( '/var/www/website/phpinc/ofc-library/open-flash-chart.php' );
   $g = new graph();

   //
   // PIE chart, 60% alpha
   //
   $g->pie(60,'#505050','{font-size : 10px;}');

   //
   // pass in two arrays, one of data, the other data labels
   //
   $g->pie_values( $data, $labels );
   //
   // Colours for each slice, in this case some of the colours
   // will be re-used (3 colurs for 5 slices means the last two
   // slices will have colours colour[0] and colour[1]):
   //
   $g->pie_slice_colours( array('#d01f3c','#356aa0','#C79810', '#53AE66', 'a367e3') );

   $g->set_tool_tip( '#x_label# <br>#val# record(s)' );
   $g->bg_colour = '#f0eee4';

   $g->title( 'Sites by Region', '{text-align : left; font-weight : bold; font-size:14px; color: #000000}' );
   echo $g->render();

?>

