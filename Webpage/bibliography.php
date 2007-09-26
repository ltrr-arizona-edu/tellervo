<?php

$title = "Bibliography";
$news = "";
$vertnavbarfilename = "phpinc/vert-navbar.php";

include('/var/www/dendrowebsite/phpinc/hub-header.php');
?>
<p>
Below is a bibliographical listing of publications by members of the Malcolm and
Carolyn Wiener Laboratory for Aegean and Near Eastern Dendrochronology.
URLs to publications or links to a PDF version are given where available. In
such cases, the following publications appear as they were published, or
as close to that state as is practical. No dendrochronological information
in these published (and thus historic) papers has been brought up-to-date
subsequent to publication. Thus, since dendro-datings (and especially
approximate best dates or hypotheses) do occasionally change with the
accumulation of new data, items published some time ago may not convey our
latest, most up-to-date thoughts on a given site.
</p>

<?php
echo "<p>";
include("phpinc/bibinc.php");
echo "</p>";




include('phpinc/hub-footer.php');

?>
