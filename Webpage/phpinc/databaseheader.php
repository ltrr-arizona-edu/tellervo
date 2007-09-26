<?php 
// The absolute path of the base of the website
$basepath="/var/www/dendrowebsite/";
$includeGoogleMap=False;

?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<?php include_once('navigationhandler.php');?>

<!--
	This document provides the basis of a semantically structured web page 
	authored in XHTML 1.0 Transitional using established Cornell University
	naming conventions.
-->

<head>
	<title><?php echo $title;?></title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-9" />
	<meta http-equiv="Content-Language" content="en-us" />
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	
<!--
	All layout and formatting should be controlled through Cascading Stylesheets (CSS).
	The following link tag should appear in the head of every page in the website. see
	styles/screen.css.
-->
	<link rel="stylesheet" type="text/css" media="screen" href="/styles/screen.css" />
        <script type="text/javascript" src="../js/dojo/dojo.js" djConfig="parseOnLoad:true"></script>
        <script type="text/javascript">
        dojo.require("dojo.parser");
        dojo.require("dijit.form.TextBox");
                dojo.require("dijit.form.CheckBox");



        </script>
        <style type="/text/css">
        @import "../js/dojo/resources/dojo.css";
        @import "../js/dijit/themes/tundra/tundra.css";
        </style>
<?php
  if ($includeGoogleMap) { include('../phpinc/googlemap.php'); }
?>

</head>

<body class="tundra" onload="load()" onunload="GUnload()">


<!--
	The following link provides a way for people using text-based browsers and
	screen readers to skip over repetitive navigation elements so that they can 
	get directly to the content. It is hidden from general users through CSS.
-->
<div id="skipnav">
	<a href="#content">Skip to main content</a>
	<form id="myForm" name="myForm" method="post" action="<? echo $_SERVER["SELF"]; ?>">
</div>

<hr />

<!-- The following div contains the Cornell University logo and search link -->
<div id="cu-identity">
	<div id="cu-logo">
		<a href="http://www.cornell.edu/"><img src="/images/cu_logo_unstyled.gif" alt="Cornell University" width="180" height="45" border="0" /></a>
	</div>
	<div id="cu-search">
		<a href="http://www.cornell.edu/search/">Search Cornell</a>
	</div>
</div>

<hr />

<!-- The header div contains the main identity and main navigation for the site -->
<div id="header">
	<div id="identity">
		<h1>CorinaWeb - Dendrochronology Database</h1>
	</div>
	<div id="navigation">

		<ul>
			<li><?php include('navigationbar.php'); ?></li>
		</ul>
	</div>
</div>

<hr />

<div id="wrap">

<!-- The content div contains the main content of the page -->
<div id="content">

	<!--
		The section-navigation div contains the second level of site navigation.
		These links appear at the top of the left sidebar of the two-column page.
	-->
	<?php
	include($basepath."/phpinc/vert-navbar-database.php");
	?>
	
	<hr />

	<div id="main">
		<h2><?php echo $title;?></h2>
		

	
