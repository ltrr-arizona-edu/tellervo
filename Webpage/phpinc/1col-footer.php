

</div>
</div>
</div>
<hr />

<div id="footer">
<!-- The footer-content div contains the Cornell University copyright -->
<div id="footer-content">
	&copy;2007 <a href="http://www.cornell.edu/">Cornell University</a>.  
<?php
if($_SERVER['SCRIPT_FILENAME'])
{
	$last_modified = filemtime($_SERVER['SCRIPT_FILENAME']);
	echo "Page last modified on ".date("l, jS F, Y", $last_modified).".";
}
?>
</div>
</div>

<?php include('googleanalytics.php'); ?>
</body>
</html>
