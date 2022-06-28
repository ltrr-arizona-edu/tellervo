/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gis;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import gov.nasa.worldwind.util.Logging;

import org.tridas.interfaces.ITridas;

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

	@Override
	public Angle getPitch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Angle getRoll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPitch(Angle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoll(Angle arg0) {
		// TODO Auto-generated method stub
		
	}

}
