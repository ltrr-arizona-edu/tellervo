<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAs0rCgSUzwBX9znK1mNUjuxQnohD9EHDH5vHbaGAe1t4gNC_fpBTcA5ndeWcl_Gi8QmJzNK964A-eDQ"
      type="text/javascript"></script>
<script type="text/javascript">

	//<![CDATA[
	
	function load() 
        {

            var iconred = new GIcon();
            iconred.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
            iconred.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
            iconred.iconSize = new GSize(12, 20);
            iconred.shadowSize = new GSize(22, 20);
            iconred.iconAnchor = new GPoint(6, 20);
            iconred.infoWindowAnchor = new GPoint(5, 1);

            // Create our "tiny" marker icon
             var iconblue = new GIcon();
             iconblue.image = "http://labs.google.com/ridefinder/images/mm_20_blue.png";
             iconblue.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
             iconblue.iconSize = new GSize(12, 20);
             iconblue.shadowSize = new GSize(22, 20);
             iconblue.iconAnchor = new GPoint(6, 20);
             iconblue.infoWindowAnchor = new GPoint(5, 1);
            
             // Create our "tiny" marker icon
             var iconyellow = new GIcon();
             iconyellow.image = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";
             iconyellow.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
             iconyellow.iconSize = new GSize(12, 20);
             iconyellow.shadowSize = new GSize(22, 20);
             iconyellow.iconAnchor = new GPoint(6, 20);
             iconyellow.infoWindowAnchor = new GPoint(5, 1);
            
             // Create our "tiny" marker icon
             var iconblack = new GIcon();
             iconblack.image = "http://labs.google.com/ridefinder/images/mm_20_black.png";
             iconblack.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
             iconblack.iconSize = new GSize(12, 20);
             iconblack.shadowSize = new GSize(22, 20);
             iconblack.iconAnchor = new GPoint(6, 20);
             iconblack.infoWindowAnchor = new GPoint(5, 1);
             


		if (GBrowserIsCompatible()) 
		{
			map = new GMap2(document.getElementById("map"));
                        map.setCenter(new GLatLng(<?php echo "$centerviewlat,$centerviewlong";?>), 9);
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
	

