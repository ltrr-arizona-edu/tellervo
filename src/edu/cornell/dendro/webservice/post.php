<?php

?>


<html>
<form name="myform" method="post" action="sites.php">

Type: <select id="action">
        <option>sites.php</option>
        <option>subsites.php</option>
        <option>trees.php</option>
        <option>specimens.php</option>
        </select>
<br/>
XML:  <textarea name="xmlrequest" rows="20" cols="80">
<corina>
<request type="read">
</request>
</corina>

</textarea><br/>

<input type="button" value="submit" onclick="document.myform.action=document.getElementById('action').value;document.myform.submit();">

</form>

</html>
