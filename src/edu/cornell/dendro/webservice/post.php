<?php

?>


<html>
<form name="myform" method="post" action="sites.php">

Type: <select id="action">
        <option>sites.php</option>
        <option>subSites.php</option>
        <option>trees.php</option>
        <option>specimens.php</option>
        <option>radii.php</option>
        <option>siteNotes.php</option>
        <option>treeNotes.php</option>
        <option>readingNotes.php</option>
        <option>dictionaries.php</option>
        <option>authenticate.php</option>
        <option>measurements.php</option>
        <option>search.php</option>
        <option>showpost.php</option>
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
