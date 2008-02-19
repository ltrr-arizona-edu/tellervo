<?php

$title = "Peter Brewer";
$news = "";
$vertnavbarfilename = "phpinc/vert-navbar-contactus.php";
$sideimagesarray["peterb.jpg"] = "Peter Brewer (Research Associate)";


include('../phpinc/2col-header.php');

?>
<p>
Tel. (Lab): 60-255-8650<br/>
Email: <a href="mailto:p.brewer@cornell.edu">p.brewer@cornell.edu</a>
</p>

<h4>Education</h4>

<ul>
<li>Ph.D. - Bioinformatics - University of Reading, 2003.</li>
<li>M.Sc. - Geoarchaeology - University of Reading, 2000.</li>
<li>B.Sc. (Hons.) - Botany - University of Reading, 1999.</li>
</ul>

<h4>Previous Positions</h4>
<ul>
<li><a href="http://www.sp2000.org">Species 2000</a> and <a href="http://www.catalogueoflife.org">Catalogue of Life</a> Systems Manager, 
2005-2007.</li>
<li><a href="http://www.bdworld.org">BiodiversityWorld (BDWorld)</a> - UK eScience Research Project, 2003-2005.</li>
</ul>

<h4>Research Interests</h4>

<ul>
<li>
The application of informatics in the fields of biology and archaeology, specifically the use of Geographical Information Systems (GIS) and databases.
</li>

<li>The adoption of data standards and interoperability within the biological and archaeological communities. </li>

<li>
Modeling the effect of palaeo and future climate change on the distribution of species.</li>

</ul>

<h4>Selected Publications</h4>
<?php
$authorfilter = "Brewer, Peter W";
$sortChronologically = TRUE;
include('../phpinc/bibinc.php');


include('../phpinc/2col-footer.php');

?>
