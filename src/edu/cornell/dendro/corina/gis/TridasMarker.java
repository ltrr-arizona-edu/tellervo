package edu.cornell.dendro.corina.gis;

import org.tridas.interfaces.ITridas;

import edu.cornell.dendro.corina.ui.I18n;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import gov.nasa.worldwind.util.Logging;

public class TridasMarker implements Marker {

	ITridas entity;
	
    private Position position; // may be null
    private Angle heading; // may be null

    // To avoid the memory overhead of creating an attibutes object for every new marker, attributes are
    // required to be specified at construction.
    private MarkerAttributes attributes;

    public TridasMarker(Position position, MarkerAttributes attrs)
    {
        throw new IllegalArgumentException("Needs a Tridas Entity"); 
    }
    
    public TridasMarker(Position position, MarkerAttributes attrs, Angle heading)
    {
    	throw new IllegalArgumentException("Needs a Tridas Entity"); 
    }
    
    public TridasMarker(Position position, MarkerAttributes attrs, ITridas entity)
    {
        if (attrs == null)
        {
            String message = Logging.getMessage("nullValue.AttributesIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        
        if(entity == null)
        {
            String message = "Needs a Tridas Entity";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        
        this.entity = entity;
        this.position = position;
        this.attributes = attrs;
    }

    public TridasMarker(Position position, MarkerAttributes attrs, Angle heading, ITridas entity)
    {
        if (attrs == null)
        {
            String message = Logging.getMessage("nullValue.AttributesIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if(entity == null)
        {
            String message = "Needs a Tridas Entity";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        
        this.entity = entity;        
        this.position = position;
        this.heading = heading;
        this.attributes = attrs;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Angle getHeading()
    {
        return this.heading;
    }

    public void setHeading(Angle heading)
    {
        this.heading = heading;
    }

    public MarkerAttributes getAttributes()
    {
        return attributes;
    }

    public void setAttributes(MarkerAttributes attributes)
    {
        this.attributes = attributes;
    }

    public void render(DrawContext dc, Vec4 point, double radius, boolean isRelative)
    {
        this.attributes.getShape(dc).render(dc, this, point, radius, isRelative);
    }

    public void render(DrawContext dc, Vec4 point, double radius)
    {
        this.attributes.getShape(dc).render(dc, this, point, radius, false);
    }
	
	
	
	public ITridas getEntity()
	{
		return entity;
	}
	
	public void setEntity(ITridas entity)
	{
		this.entity = entity;
	}

}
