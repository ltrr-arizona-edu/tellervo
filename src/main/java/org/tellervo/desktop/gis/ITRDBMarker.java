package org.tellervo.desktop.gis;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

public class ITRDBMarker implements Marker {

    private Position position; // may be null
    private Angle heading; // may be null
    // To avoid the memory overhead of creating an attributes object for every new marker, attributes are
    // required to be specified at construction.
    private MarkerAttributes attributes;
    
    public ITRDBMarker(Position pos, MarkerAttributes attrb)
    {
    	position = pos;
    	attributes = attrb;
    }
	
	@Override
	public MarkerAttributes getAttributes() {
		
		return attributes;
	}

	@Override
	public Angle getHeading() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Angle getPitch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position getPosition() {

		return position;
	}

	@Override
	public Angle getRoll() {
		// TODO Auto-generated method stub
		return null;
	}
	
    public void render(DrawContext dc, Vec4 point, double radius, boolean isRelative)
    {
        this.attributes.getShape(dc).render(dc, this, point, radius, isRelative);
    }

    public void render(DrawContext dc, Vec4 point, double radius)
    {
        this.attributes.getShape(dc).render(dc, this, point, radius, false);
    }
	

	@Override
	public void setAttributes(MarkerAttributes arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeading(Angle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPitch(Angle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPosition(Position arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRoll(Angle arg0) {
		// TODO Auto-generated method stub

	}

}
