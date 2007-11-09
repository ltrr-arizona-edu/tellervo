<?php



$doc = new DomDocument;
$doc->load($_POST['file']);

$valid = $doc->relaxNGValidate('schema/corina.rng');

echo "Document validates against RelaxNG schema";






?>

