<?php

$title = "Developers";
$news = "";
$vertnavbarfilename = "phpinc/vert-navbar-corina.php";
$sideimagesarray["code.jpg"] = "";

include('../phpinc/2col-header.php');



?>
<!--<h4>Feature requests and bug tracker</h4>

<p>
If you would like to request a new feature in Corina or you would like to report a bug with the current version, please either the <a href="mailto:corina@dendro.cornell.edu">tracking system</a> being sure to include a descriptive subject heading, or <a href="http://tracker.dendro.cornell.edu">log in</a> to the tracker system.
</p>-->

<h4>Accessing the source code</h4>
<p>	
If you would like to view the Corina source code you can either <a href="http://dendro.cornell.edu/svn/corina/">browse the SVN repository</a> online, or you can do an anonymous SVN checkout using:
</p>

<ul>
<li>svn co http://dendro.cornell.edu/svn/corina/trunk</li>
</ul>

<p>
If you are interested in contributing to Corina please <a href="mailto:general@dendro.cornell.edu">email us</a> to request a developers account and then checkout the code using:
</p>

<ul>
<li>svn+ssh://username@dendro.cornell.edu/svn/corina/trunk</li>
</ul>

<p>Discussion about the development of Corina is hosted on our <a href="http://dendro.cornell.edu/corina-manual/DevelopersPages">Developers Wiki</a>.

<?php

include('../phpinc/2col-footer.php');

?>
