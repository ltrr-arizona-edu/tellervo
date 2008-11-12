<?php

include('post.php');

$xmlrequest="<corina><request type=\"create\"><site name=\"".$code."\" code=\"".$code."\" latitude=\"".$lat."\" longitude=\"".$long."\" /></request></corina>";
echo postData($xmlrequest, 'showpost.php');

?>
