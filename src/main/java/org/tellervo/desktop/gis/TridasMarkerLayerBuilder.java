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

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasObject;
import org.tridas.spatial.GMLPointSRSHandler;
import org.tridas.util.TridasObjectEx;

/**
 * Wrapper class to make it easy to create WWJ MarkerLayers 
 * from Tridas entities
 * 
 * @author peterbrewer
 *
 */
public class TridasMarkerLayerBuilder {

	/** Marker attributes */
	private static BasicMarkerAttributes defaultAttributes = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private String layerName = "Marker layer";
	
	
	public TridasMarkerLayerBuilder(){
			
	}
	
	public boolean addMarkerForTridasObject(TridasObject obj)
	{
		if(obj==null) return false;
		
		if(obj.isSetLocation())
		{
			if(obj.getLocation().isSetLocationGeometry())
			{
				return addMarkerForTridasEntity(obj.getLocation().getLocationGeometry(), obj);
			}
		}
		
		return false;
	}
	
	public boolean addMarkerForTridasElement(TridasElement elem)
	{
				
		if(elem==null) return false;
		 
		if(elem.isSetLocation())
		{
			if(elem.getLocation().isSetLocationGeometry())
			{
				return addMarkerForTridasEntity(elem.getLocation().getLocationGeometry(), elem);
			}
		}
		
		return false;
	}
	
	private boolean addMarkerForTridasEntity(TridasLocationGeometry geom, ITridas entity)
	{
		if(geom.isSetPoint())
		{
			
			GMLPointSRSHandler tph = new GMLPointSRSHandler(geom.getPoint());
			
			if(tph.hasPointData())
			{
				markers.add(new TridasMarker(Position.fromDegrees(tph.getWGS84LatCoord(), tph.getWGS84LongCoord()), 
						getMarkerAttributesForEntity(entity.getClass()), entity));
				return true;
			}
			
		}
		return false;
	}
	
	public void addLatLongMarker(Double lat, Double lon)
	{
		markers.add(new TridasMarker(Position.fromDegrees(lat, lon), getMarkerAttributesForEntity(TridasObject.class), new TridasObject()));
	}
	
	public Boolean containsMarkers()
	{
		if(markers.isEmpty())
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
	
	public void setName(String name)
	{
		this.layerName = name;
	}
	
	public String getName()
	{
		return this.layerName;
	}
	
	public MarkerLayer getMarkerLayer()
	{

		MarkerLayer layer = new MarkerLayer(markers);
		layer.setName(this.getName());
		
        layer.setOverrideMarkerElevation(true);
        layer.setElevation(0);
        layer.setEnablePickSizeReturn(true);
        
        return layer;
	}
	
	private  void loadAllSiteObjectMarkers()
	{
		for(TridasObjectEx obj : App.tridasObjects.getTopLevelObjectList())
		{
			addMarkerForTridasObject(obj);
		}
	}
	


	
	private static BasicMarkerAttributes getMarkerAttributesForEntity(Class<? extends ITridas> clazz)
	{
		return getMarkerAttributesForEntity(clazz, false);
	}

	private static BasicMarkerAttributes getMarkerAttributesForEntity(Class<? extends ITridas> clazz, Boolean selected)
	{
		double opacity = 0.6d;
		if(selected) opacity = 1.0d;
		
		if((clazz == TridasObject.class) || (clazz == TridasObjectEx.class))
		{
			return new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, opacity);
		}
		else if(clazz == TridasElement.class)
		{
			return new BasicMarkerAttributes(Material.ORANGE, BasicMarkerShape.CONE, opacity);
		}
		else if (clazz == TridasDerivedSeries.class)
		{
			return new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, opacity);
		}
		
		
		return defaultAttributes;
	}
	
	public static MarkerLayer getMarkerLayerForAllSites()
	{
		TridasMarkerLayerBuilder builder = new TridasMarkerLayerBuilder();
		builder.loadAllSiteObjectMarkers();
		builder.setName("All objects in database");
		
        return builder.getMarkerLayer();
	}
	
	
	
	public static MarkerLayer getMarkerLayerForLatLong(Double lat, Double lon)
	{
		TridasMarkerLayerBuilder builder = new TridasMarkerLayerBuilder();
		builder.addLatLongMarker(lat, lon);
		builder.setName("Coordinate marker");
		
        return builder.getMarkerLayer();
	}
}
