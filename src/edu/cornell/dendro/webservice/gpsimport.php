#!/usr/bin/php
<?php 
if (!isset($argc))
{
	echo "This file should be called from the command line only";
	die();
	
}

// Help text
if ($argc != 2 || in_array($argv[1], array('--help', '-help', '-h', '-?'))) {
	?>
		
This is a command line PHP script for extracting site location data from a Corina v1.1 site database file.

  Usage:
  <?php echo $argv[0]; ?> <infile>

  <infile> if the Corina sitedb file that you want to process
  
  With the --help, -help, -h,
  or -? options, you can get this help.
		
		<?php
}





?>