<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAs0rCgSUzwBX9znK1mNUjuxQnohD9EHDH5vHbaGAe1t4gNC_fpBTcA5ndeWcl_Gi8QmJzNK964A-eDQ"
      type="text/javascript"></script>
<script type="text/javascript">

	//<![CDATA[
	
	function load() 
	{
		if (GBrowserIsCompatible()) 
		{
			map = new GMap2(document.getElementById("map"));
			map.setCenter(new GLatLng(40, 30), 5);
			map.addControl(new GSmallMapControl());
			map.addControl(new GMapTypeControl());
		}
	
		// Creates a marker at the given point with the given number label
		function createMarker(point, code, placename, id, species, datafilename, datatype) 
		{
			if(datatype=='1')
			{
			var marker = new GMarker(point, iconred);
			}
			else if(datatype=='2')
			{
			var marker = new GMarker(point, iconblue);
			}
			
			else if(datatype=='3')
			{
			var marker = new GMarker(point, iconyellow);
			}
			else
			{
			var marker = new GMarker(point, iconblack);
			}
			
			GEvent.addListener(marker, "click", function() {
			marker.openInfoWindowHtml("<b>" + code + "<br> "+ placename +"</b><br><br><i>"+species+"</i><br><br><font size=1>"+datafilename+"</font>");
			});
			return marker;
		}
	}

</script>
