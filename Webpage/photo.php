<?php
$photourl = $_GET['photourl'];
$photoCaption = $_GET['photocaption'];
$photoCredit = $_GET['photocredit'];

if(!$photourl)
{
    echo "<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;URL=http://dendro.cornell.edu/error\">";
    die;
}

$title = "The Malcolm and Carolyn Wiener Laboratory for Aegean and Near Eastern Dendrochronology";
$hidetitle = True;
include('phpinc/1col-header.php');

?>

    <img id="photo" src="<?echo $photourl?>" alt="" />

<div id="photo-meta">
    <div id="webring">
        
    </div>

    <p class="caption">
        <?echo $photoCaption?>
    </p>

    <div id="photo-credit">
        <?echo $photoCredit?>
    </div>
</div>


<?php

include('phpinc/1col-footer.php');

?>
