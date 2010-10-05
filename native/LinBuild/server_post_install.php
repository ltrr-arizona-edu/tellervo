#!/usr/bin/php
<?php
if (!isset($argc))
{
	echo "This file should be called from the command line only";
	die();
}

echo "\nCONFIGURING CORINA SERVER...\n\n"



echo "Type your name...";
$name = fgets(STDIN); 

echo "Your name is $name";


      
?>