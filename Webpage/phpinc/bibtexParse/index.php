<?php

include('PARSEENTRIES.php');

$parse = NEW PARSEENTRIES();
$parse->openBib("bib.bib");
$parse->extractEntries();
$parse->closeBib();

print_r($parse);

?>
