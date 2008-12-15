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
&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;corina xmlns="http://dendro.cornell.edu/schema/corina/1.0" xmlns:tridas="http://www.tridas.org/1.1"&gt;
  &lt;request type="plainlogin"&gt;
     &lt;authenticate username="yourusername" password="yourpassword" /&gt;
  &lt;/request&gt;
&lt;/corina&gt;
</pre>


<h4>Get details of a site:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8" ?&gt;
&lt;corina xmlns="http://dendro.cornell.edu/schema/corina/1.0" xmlns:tridas="http://www.tridas.org/1.1"&gt;
  &lt;request type="read"&gt;
     &lt;entity type="sample" id="1" /&gt;
  &lt;/request&gt;
&lt;/corina&gt;
</pre>


<h4>Search for measurements of a site:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8" xmlns="http://dendro.cornell.edu/schema/corina/1.0" xmlns:tridas="http://www.tridas.org/1.1"?&gt;
&lt;corina&gt;
  &lt;request type="search"&gt;
     &lt;searchParams returnObject="measurement" limit="5"&gt;
        &lt;param name="siteid" operator="&gt;" value="571" /&gt;
     &lt;/searchParams&gt;
  &lt;/request&gt;
&lt;/corina&gt;
</pre>





</html>
