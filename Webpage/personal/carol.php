<?php

$title = "Carol Griggs";
$news = "";
$vertnavbarfilename = "phpinc/vert-navbar-contactus.php";
$sideimagesarray["carolalbany.jpg"] = "Carol Griggs (Research Associate)";
$sideimagesarray["carol.jpg"] = "";

include('../phpinc/2col-header.php');

?>

<h4>Education</h4>

<ul>
<li>Ph.D. - Geology - Cornell University 2006.</li>
<li>M.S. - Quaternary Studies - University of Maine at Orono, 1983.</li>
<li>A.B. - Anthropology and Archaeology - Cornell University, 1977</li>
</ul>

<h4>Previous Positions</h4>

<ul>
<li>Teaching Assistantships, Earth and Atmospheric Sciences, Cornell University, 2004-2005.</li>
<li>Research Support Specialist, Cornell University, 1998-2003.</li>
<li>Fellow of the Institute for Aegean Prehistory, Cornell University, 1990-1992</li>
<li>Research Support Specialist, Cornell University, 1986-1992.</li>
<li>Research Aide, Cornell University, 1977-1979.</li>
</ul>

<h4>Research Interests</h4>

<ul>
<li>Spatial and temporal change in the paleoclimate patterns from Late Glacial through the Holocene, particularly the interconnections between variations in solar activity and thermohaline circulation, and their effects on the terrestrial environment during the Late Glacial chronozone</li>

<li>Using modern tree-ring data to test dendroclimatological methods and the accuracy of interpreting paleoclimate proxy data; how to correctly interpret regional tree-ring chronologies as evidence or non-evidence of long-term climate change plus the influence of abrupt events</li>

<li>Dating historic buildings and other wooden structures, artifacts, and events over time.</li>

</ul>

<p>
All these have relevance to the effect of climate change on human history, and will help assess the future. 
</p>

<h4>Selected Publications</h4>

<?php
$authorfilter = "Griggs";
$sortChronologically = TRUE;
include('../phpinc/bibinc.php');


include('../phpinc/2col-footer.php');

?>
