<?php

?>


<html>
<form name="myform" method="post" action="webservice.php">
<br/>
XML:  <textarea name="xmlrequest" rows="20" cols="80">
<corina>
<request type="read">
</request>
</corina>

</textarea><br/>

<input type="submit" value="submit" >

</form>
<h4>Login:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;corina>
  &lt;request type="plainlogin">
     &lt;authenticate username="yourusername" password="yourpassword" />
  &lt;/request>
&lt;/corina>
</pre>


<h4>Get details of a site:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;corina>
  &lt;request type="read">
     &lt;site id="1" />
  &lt;/request>
&lt;/corina>
</pre>


<h4>Search for measurements of a site:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;corina>
  &lt;request type="search">
     &lt;searchParams returnObject="measurement" limit="5">
        &lt;param name="siteid" operator="=" value="571" />
     &lt;/searchParams>
  &lt;/request>
&lt;/corina>
</pre>





</html>
