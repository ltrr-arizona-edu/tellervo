
	</div>
	<hr />
	
	<!--
		The contents of the secondary div are displayed in the left column sidebar
		below the secondary navigation. Each group of secondary content should be 
		organized in a secondary-section div, which will pad the content from the 
		edges of the sidebar and separate it from other content.
	-->
	<div id="secondary">
		
		<?php
		if($newsflash)
		{
			?>
			<div class="secondary-section">
				<h2>News</h2>
				<p>
				<? echo $newsflash; ?>
				</p>
			</div>
			<?php
		}
		?>
		<?php
		if($sideimagesarray)
		{
			?>
				<!--
					A secondary-photo div contains an image that is as wide as the sidebar 
					(200px) and a caption paragraph. Note that the secondary-photo should
					not be within a secondary-section div.
				-->
			<?php
			function printSideImages($item, $key)
			{
				echo "<img src=\"/images/sideimages/".$key."\" alt=\"".$item."\" /> \n";
				echo "<p class=\"caption\">".$item."</p><br/><br/>";
			}
			
			echo "<div class=\"secondary-photo\">";
			array_walk($sideimagesarray, 'printSideImages');
			echo "</div>";
		}
		?>
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
