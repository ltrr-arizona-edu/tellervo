function drawCircle(center, radius, nodes, liColor, liWidth, liOpa, fillColor, fillOpa)
{
    // Calculate km/degree
    var latConv = center.distanceFrom(new GLatLng(center.lat()+0.1, center.lng()))/100;
    var lngConv = center.distanceFrom(new GLatLng(center.lat(), center.lng()+0.1))/100;
    
    //Loop 
    var points = [];
    var step = parseInt(360/nodes)||10;
    for(var i=0; i<=360; i+=step)
    {
        var pint = new GLatLng(center.lat() + (radius/latConv * Math.cos(i * Math.PI/180)), center.lng() + 
            (radius/lngConv * Math.sin(i * Math.PI/180)));
        points.push(pint);
        bounds.extend(pint); //this is for fit function
    }
    fillColor = fillColor||liColor||"#0055ff";
    liWidth = liWidth||2;
    var poly = new GPolygon(points,liColor,liWidth,liOpa,fillColor,fillOpa);
    map.addOverlay(poly);
}
