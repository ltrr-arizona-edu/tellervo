package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;

import java.util.List;

import org.tellervo.desktop.gis.TridasMarker;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasObject;
import org.tridas.spatial.GMLPointSRSHandler;

public class TridasMarkerFactory {

	
	public static TridasMarker getMarkerForObject(TridasObject obj, BasicMarkerAttributes markerStyle)
	{
		if(obj==null) return null;
		
		if(obj.isSetLocation())
		{
			if(obj.getLocation().isSetLocationGeometry())
			{
				return getMarkerForTridasEntity(obj.getLocation().getLocationGeometry(), obj, markerStyle);
			}
		}
		
		return null;
	}
	
	
	public static TridasMarker getMarkerForTridasEntity(TridasLocationGeometry geom, ITridas entity, BasicMarkerAttributes markerStyle)
	{

		
		if(geom.isSetPoint())
		{
			
			GMLPointSRSHandler tph = new GMLPointSRSHandler(geom.getPoint());

			
			if(tph.getWGS84LatCoord()!=null && tph.getWGS84LongCoord()!=null)
			{
			
				return new TridasMarker(Position.fromDegrees(tph.getWGS84LatCoord(), tph.getWGS84LongCoord()), 
						markerStyle, entity);
				
				// TODO FIX ME!!!!
				//return new TridasMarker(Position.fromDegrees(tph.getWGS84LongCoord(), tph.getWGS84LatCoord()), 
				//		markerStyle, entity);
	
			}
			
		}
		return null;
	}
	
	

	/*
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
		
		
		return marker;
	}*/
}
