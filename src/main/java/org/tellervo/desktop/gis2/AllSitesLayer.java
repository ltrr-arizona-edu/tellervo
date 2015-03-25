package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.TridasMarker;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

public class AllSitesLayer extends MarkerLayer implements TellervoDataLayer{
	
	public static BasicMarkerAttributes defaultAttributes = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
	public static BasicMarkerAttributes highlightAttributes = new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.CYLINDER, 0.6d);

	public AllSitesLayer()
	{
		setName("All objects in database");
		
        setOverrideMarkerElevation(true);
        setElevation(0);
        setEnablePickSizeReturn(true);
        
        
        ArrayList<Marker> markers = new ArrayList<Marker>();
        
		for(TridasObjectEx obj : App.tridasObjects.getTopLevelObjectList())
		{
			Marker m = getMarkerForObject(obj);
			if(m!=null) markers.add(m);
		}
		
		this.setMarkers(markers);
        
	}
	
	
	public static TridasMarker getMarkerForObject(TridasObject obj)
	{
		if(obj==null) return null;
		
		if(obj.isSetLocation())
		{
			if(obj.getLocation().isSetLocationGeometry())
			{
				return getMarkerForTridasEntity(obj.getLocation().getLocationGeometry(), obj);
			}
		}
		
		return null;
	}
	
	
	public static TridasMarker getMarkerForTridasEntity(TridasLocationGeometry geom, ITridas entity)
	{

		
		if(geom.isSetPoint())
		{
			List<Double> coords = geom.getPoint().getPos().getValues();
			if(coords.size()==2)
			{
				return new TridasMarker(Position.fromDegrees(coords.get(1), coords.get(0)), 
						getMarkerAttributesForEntity(entity.getClass()), entity);

			}
		}
		return null;
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
	
}
