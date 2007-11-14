<?php

$title = "The Malcolm and Carolyn Wiener Laboratory for Aegean and Near Eastern Dendrochronology";
$hidetitle = True;
include('phpinc/hub-header.php');

?>

<!-- 
	The hub-description div is the left column of the hub page. 
	It should contain text that describes or introduces the contents 
	of the site section for which the hub page is the highest level.
-->
<div id="hub-description">
<h3>	
Welcome to the website of the Cornell Tree-Ring Laboratory, home of:
</h3>

<ul>
<li><a href="/projects/aegean.php">The Malcolm and Carolyn Wiener Laboratory for Aegean and Near Eastern Dendrochronology </a></li>
<li> <a href="/projects/usa.php">The New York State and NE North American Dendrochronology Project</a></li>
</ul>

<!--<p>
Please use the links opposite to learn more about dendrochronology and how it is being used in the Cornell Tree-Ring Laboratory.
</p>
-->


<h3>Teaching and Research</h3>
<ul>
<li>Each year we teach an  <a href="course.php">Introduction to Dendrochronology</a> course for Cornell graduate and undergraduate students.</li>
<li>We also have opportunities for <a href="gradstudents.php">graduate research.</a></li>
</ul>


<h3>We need your samples!</h3>
<ul>
<li>
If you have samples that you would like us to examine, please see our <a href="samples.php">'we need your samples'</a> page which includes sampling guidelines and instructions on how to send us your samples.</li>
</ul>

</div>


<!--
	The hub-sections div is the right column of the hub page.
	It provides a thumbnail, link, and description for each sub-section.
-->
<div id="hub-sections">
	<!--
		Each hub-section div contains a link, thumbnail, and description
		for a sub-section. The first hub-section div should have its ID
		attribute set to "hub-section-first"
	-->
	<div class="hub-section" id="hub-section-first">
		<h3><a href="whatisdendro.php">What is dendrochronology?</a></h3>
		<a href="whatisdendro.php"><img class="thumbnail" src="images/thumbnails/junipertree.jpg" alt="thumbnail alt text" /></a>
		<p>
		Learn more about dendrochronology and our laboratory procedures.
		</p>
	</div>
	<div class="hub-section">
		<h3><a href="ourprojects">About our projects</a></h3>
		<a href="ourprojects"><img class="thumbnail" src="images/thumbnails/sturtinforest.jpg" alt="thumbnail alt text" /></a>
		<p>
		Read more about the Aegean and Near Eastern Dendrochronology, and the New York and NE North American Dendrochronology projects.
		</p>
	</div>
	<div class="hub-section">
		<h3><a href="corina/index.php">Corina software</a></h3>
		<a href="corina/index.php"><img class="thumbnail" src="images/thumbnails/britawithcomputer.jpg" alt="thumbnail alt text" /></a>
		<p>
		Learn more about our open source dendrochronology software.
		</p>
	</div>
</div>


<div id="hub-more">
<!--
	The hub-more div is the white box at the bottom of the hub page.
	It contains sets of links to information related to the contents of 
	the site section. The links are organized into unordered lists, which
	are stacked in a horizontal row.
-->
	<h3>Index</h3>
	
	<ul>    
		<li><a href="reports.php">Annual reports</a></li>
		<li><a href="bibliography.php">Bibliography</a></li>
		<li><a href="contactus.php">Contact us</a></li>
		<li><a href="corina/index.php">Corina software</a></li>
        </ul>

        <ul>
		<li><a href="projects/dendrochem.php">Dendrochemistry</a></li>
		<li><a href="course.php">Dendrochronology course</a></li>
		<li><a href="gradstudents.php">Graduate opportunities</a></li>
		<li><a href="supporters.php">History &amp; Supporters</a></li>
        </ul>

        <ul>
		<li><a href="procedures.php">Laboratory procedures</a></li>
		<li><a href="links.php">Links</a></li>
                <li><a href="ourprojects.php">Our projects</a></li>
		<li><a href="samples.php">We need your samples!</a></li>
	</ul>
</div>	


<?php

include('phpinc/hub-footer.php');

?>
