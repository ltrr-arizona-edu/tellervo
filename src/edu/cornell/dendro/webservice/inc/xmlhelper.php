<?php

function setXMLErrors($myMetaHeader, $errors)
{
print_r($errors);

    foreach ($errors as $error)
    {
        $message=  "XML Error  - ".trim($error->message).". Error located on line ".$error->line.", column ".$error->column.".";
        $myMetaHeader->setMessage("905", $message);
    }
}
?>
