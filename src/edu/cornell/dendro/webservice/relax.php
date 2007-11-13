<?php

include('config.php');

// Extract parameters from XML post
$xmlstring = stripslashes($_POST['xmlrequest']);
$doc = new DomDocument;
$doc->loadXML($xmlstring);
if($doc->relaxNGValidate('/home/aps03pwb/test.rng'))
{
    echo "valid";
}
else
{
    echo "invalid";
}


?>

