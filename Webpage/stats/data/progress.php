<?php

include_once('/var/www/webservice/inc/dbsetup.php');

$data = array();
$dataCum = array();
$labels = array();
$startDate = mktime(0,0,0,8,1,2007);
$endDate   = mktime(0,0,0,date('m'),date('d'),date('Y'));

$currentDate = $startDate;
$lastDate = $startDate; 
while ($currentDate<$endDate) 
{
    $currentDate=mktime(0,0,0,date('m',$lastDate)+1, date('d', $lastDate), date('Y', $lastDate));

    $sql = "select count(*) from tblvmeasurement where createdtimestamp>='".date('Y-m-d',$lastDate)."' and createdtimestamp<'".date('Y-m-d',$currentDate)."'";
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            array_push($data, $row['count']);
        }
    }
    
    $sql = "select count(*) from tblvmeasurement where createdtimestamp>='".date('Y-m-d',$startDate)."' and createdtimestamp<'".date('Y-m-d',$currentDate)."'";
    $dbconnstatus = pg_connection_status($dbconn);
    if ($dbconnstatus ===PGSQL_CONNECTION_OK)
    {
        $result = pg_query($dbconn, $sql);
        while ($row = pg_fetch_array($result))
        {
            array_push($dataCum, $row['count']);
        }
    }

    array_push($labels, date('M y', $lastDate));

    $lastDate = $currentDate; 
}



   include_once( '/var/www/website/phpinc/ofc-library/open-flash-chart.php' );
   $g = new graph();

   $g->set_data( $data);
   $g->bar(50, '#AAAAAA', 'Monthly Data', 10 );
   $g->set_data( $dataCum);
   $g->line_hollow(2, 4, '#b31b1b', 'Total', 10);
   $g->attach_to_y_right_axis(1);

   $g->set_x_labels($labels);
   $g->set_x_legend('Date', 10, '#000000');
   $g->x_axis_colour('#000000', '#DDDDDD');
   $g->set_x_label_style('10', '#000000', 1, 0, '#DDDDDD');

   $g->set_y_right_max(round(max($data)*1.5, -2));
   $g->set_y_max(round(max($dataCum)*1.5, -2));
   $g->set_y_right_legend('Monthly Records', 10, '#000000');
   $g->set_y_legend('Cumulative Records', 10, '#000000');
   $g->set_y_label_style('10', '#000000', 2, 2, '#BBBBBB');
   $g->y_axis_colour('#000000', '#DDDDDD');
   $g->y_right_axis_colour('#000000', '#AAAAAA');
   
   $g->set_tool_tip( '#x_label# <br>#val# record(s)' );
   $g->bg_colour = '#f0eee4';

   $g->title( 'Measurements in the database', '{font-weight : bold; font-size:14px; color: #000000; padding : 10px;}' );
   echo $g->render();

?>

