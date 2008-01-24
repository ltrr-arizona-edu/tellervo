<?php
$xmldata.="<paragraph>The Authenticate webservice only works using POST requests to ensure that passwords are not stored in webservers logs etc. There are two methods for logging in, a plain text method and a secure method. We strongly recommend the secure method but there may be times when you can only manage the plain method. The parameters described below should be sent via a standard POST request such as a HTML form with the action set to POST.</paragraph>";


$xmldata.="<helpsectiontitle>Parameters</helpsectiontitle>";
$xmldata.="<paragraph>
    <listitem>mode - plainlogin, securelogin, nonce or logout</listitem>
    <listitem>username - the username of the user trying to login</listitem>
    <listitem>password - the plaintext password of the user trying to login using the plainlogin mode</listitem>
    <listitem></listitem>
    </paragraph>";


?>
