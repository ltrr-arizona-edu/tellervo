<?php 
// The absolute path of the base of the website
$basepath="/var/www/website/";
$basewebpath="http://dendro.cornell.edu/";
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<!--
	This document provides the basis of a semantically structured web page 
	authored in XHTML 1.0 Transitional using established Cornell University
	naming conventions.
-->

<head>
	<title><?php echo $title;?></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Content-Language" content="en-us" />
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
	
<!--
	All layout and formatting should be controlled through Cascading Stylesheets (CSS).
	The following link tag should appear in the head of every page in the website. see
	styles/screen.css.
-->
<link rel="stylesheet" type="text/css" media="screen" href="<?echo $basewebpath."/styles/screen.css";?>" />
<?php

if($includeTimeline)
{
    ?>
    <script src="http://simile.mit.edu/timeline/api/timeline-api.js" type="text/javascript"></script>
    <script>
    var tl;
    function onLoad() {
        var eventSource = new Timeline.DefaultEventSource();        
        var bandInfos = [
            Timeline.createBandInfo({
                eventSource:    eventSource,
                date:           "1000",
                width:          "50%", 
                intervalUnit:   Timeline.DateTime.CENTURY, 
                durationColor: "#FF0000",
                intervalPixels: 200
            }),
        
            Timeline.createBandInfo({
                eventSource:    eventSource,
                date:           "1000",
                width:          "30%", 
                intervalUnit:   Timeline.DateTime.CENTURY, 
                intervalPixels: 50
            }),

            Timeline.createBandInfo({
                eventSource:    eventSource,
                date:           "1000",
                width:          "20%", 
                intervalUnit:   Timeline.DateTime.MILLENNIUM, 
                intervalPixels: 50,
                showEventText : false,
                trackGap:       0.2,
                trackHeight:    0.5
            })
        ];
        
        bandInfos[1].syncWith = 0;
        bandInfos[2].syncWith = 1;
        bandInfos[1].highlight = true;
        bandInfos[2].highlight = true;




        tl = Timeline.create(document.getElementById("my-timeline"), bandInfos);
        Timeline.loadXML("http://dendro.cornell.edu/data.xml", function(xml, url) {eventSource.loadXML(xml, url); });
    }

    var resizeTimerID = null;
    function onResize() {
        if (resizeTimerID == null) {
            resizeTimerID = window.setTimeout(function() {
                resizeTimerID = null;
                tl.layout();
            }, 500);
        }
    }



    </script>
    <body onload="onLoad();" onresize="onResize();">

<?php
}
elseif($includeDojo)
{
?>
    <script type="text/javascript" src="../js/dojo/dojo.js" djConfig="parseOnLoad:true"></script>
    <script type="text/javascript">
            dojo.require("dojo.parser");
            dojo.require("dijit.Toolbar");
            dojo.require("dijit.layout.LayoutContainer");
            dojo.require("dijit.layout.SplitContainer");
            dojo.require("dijit.layout.AccordionContainer");
            dojo.require("dijit.layout.TabContainer");
            dojo.require("dijit.layout.ContentPane");
    </script>
    <style type="text/css">
       @import "../js/dojo/resources/dojo.css";
       @import "../js/dijit/themes/tundra/tundra.css";
    </style>
    </head>
    <body class="tundra">
<?php
}
else
{
?>
    </head>
    <body class="onecolumn">
<?php
}
?>


<!--
	The following link provides a way for people using text-based browsers and
	screen readers to skip over repetitive navigation elements so that they can 
	get directly to the content. It is hidden from general users through CSS.
-->
<div id="skipnav">
	<a href="#content">Skip to main content</a>
</div>

<hr />

<!-- The following div contains the Cornell University logo and search link -->
<div id="cu-identity">
	<div id="cu-logo">
		<a href="http://www.cornell.edu/"><img src="/images/logos/cu_logo_unstyled.gif" alt="Cornell University" width="180" height="45" border="0" /></a>
	</div>
	<div id="cu-search">
		<a href="http://www.cornell.edu/search/">Search Cornell</a>
	</div>
</div>

<hr />

<?php
include('horiz-navbar.php');
?>

<hr />

<div id="wrap">

<!-- The content div contains the main content of the page -->
<div id="content">


	<div id="main">
<?php
if(!$hidetitle){ echo "<h2>$title</h2>";}
?>
		

	
