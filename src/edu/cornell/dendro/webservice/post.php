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
&lt;corina xmlns="http://dendro.cornell.edu/schema/corina/1.0" xmlns:tridas="http://www.tridas.org/1.2"&gt;
  &lt;request type="plainlogin"&gt;
     &lt;authenticate username="yourusername" password="yourpassword" /&gt;
  &lt;/request&gt;
&lt;/corina&gt;
</pre>


<h4>Get details of a site object:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8" ?&gt;
&lt;corina xmlns="http://dendro.cornell.edu/schema/corina/1.0" xmlns:tridas="http://www.tridas.org/1.2"&gt;
  &lt;request type="read"&gt;
     &lt;entity type="object" id="136a70a6-566b-546b-a3ae-c48cb046e4cd" /&gt;
  &lt;/request&gt;
&lt;/corina&gt;
</pre>


<h4>Search for measurements of a site object:</h4>

<pre>
&lt;?xml version="1.0" encoding="UTF-8" ?&gt;
&lt;corina xmlns="http://dendro.cornell.edu/schema/corina/1.0" xmlns:tridas="http://www.tridas.org/1.2"&gt;
  &lt;request type="search"&gt;
     &lt;searchParams returnObject="measurementSeries" limit="5"&gt;
        &lt;param name="objectid" operator="&gt;" value="136a70a6-566b-546b-a3ae-c48cb046e4cd" /&gt;
     &lt;/searchParams&gt;
  &lt;/request&gt;
&lt;/corina&gt;
</pre>

<h4>Example UUIDs</h4>
Object - 136a70a6-566b-546b-a3ae-c48cb046e4cd<br>
Element - 00faf80a-7ca2-57ba-b8f8-268cd28429df<br>
Sample - 0172719e-5903-5cf8-adf8-ae755e7350b0<br>
Radius - 03933250-894c-5d9d-ac92-ea6fc7f2770d<br>
MeasurementSeries - 02189be5-b19c-5dbd-9035-73ae8827dc7a<br>

</html>
