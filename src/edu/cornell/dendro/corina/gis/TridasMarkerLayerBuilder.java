package edu.cornell.dendro.corina.gis;

import edu.cornell.dendro.corina.core.App;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;
import java.util.List;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasObject;
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
	BasicMarkerAttributes defaultAttributes = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
	ArrayList<Marker> markers = new ArrayList<Marker>();
	
	public TridasMarkerLayerBuilder(){
			
	}

	
	public void addMarkerForTridasObject(TridasObject obj)
	{
		if(obj==null) return;
		
		if(obj.isSetLocation())
		{
			if(obj.getLocation().isSetLocationGeometry())
			{
				addMarkerForTridasEntity(obj.getLocation().getLocationGeometry(), obj);
			}
		}
	}
	
	public void addMarkerForTridasElement(TridasElement elem)
	{
		if(elem==null) return;
		 
		if(elem.isSetLocation())
		{
			if(elem.getLocation().isSetLocationGeometry())
			{
				addMarkerForTridasEntity(elem.getLocation().getLocationGeometry(), elem);
			}
		}
	}
	
	private void addMarkerForTridasEntity(TridasLocationGeometry geom, ITridas entity)
	{
		if(geom.isSetPoint())
		{
			List<Double> coords = geom.getPoint().getPos().getValues();
			if(coords.size()==2)
			{
				markers.add(new TridasMarker(Position.fromDegrees(coords.get(1), coords.get(0)), 
						getMarkerAttributesForEntity(entity.getClass()), entity));
			}
		}
		

		
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
	
	public MarkerLayer getMarkerLayer()
	{

		MarkerLayer layer = new MarkerLayer(markers);
		
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
	

	
	private BasicMarkerAttributes getMarkerAttributesForEntity(Class<? extends ITridas> clazz)
	{
		return getMarkerAttributesForEntity(clazz, false);
	}

	private BasicMarkerAttributes getMarkerAttributesForEntity(Class<? extends ITridas> clazz, Boolean selected)
	{
		double opacity = 0.6d;
		if(selected) opacity = 1.0d;
		
		if((clazz == TridasObject.class) || (clazz == TridasObjectEx.class))
		{
			return new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, opacity);
		}
		else if(clazz == TridasElement.class)
		{
			return new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.CONE, opacity);
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
		
        return builder.getMarkerLayer();
	}
	
}
