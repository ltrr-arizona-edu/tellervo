package edu.cornell.dendro.corina.gis;

import java.util.ArrayList;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;

import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;

/**
 * Wrapper class to make it easy to create WWJ MarkerLayers 
 * from Tridas entities
 * 
 * @author peterbrewer
 *
 */
public class TridasMarkerLayer extends MarkerLayer {

	/** Marker attributes */
	BasicMarkerAttributes attrs;
	
	public TridasMarkerLayer(TridasObject obj){
	
		attrs = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
		
	}
	
	public TridasMarkerLayer(TridasElement elem){
		
		attrs = new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.CONE, 0.6d);
		
	}
	
	public TridasMarkerLayer(TridasDerivedSeries dseries)
	{
		attrs = new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 0.6d);
		
		
	}
	
	public TridasMarkerLayer(ArrayList<? extends ITridas> tridasList){
		
		if(tridasList==null) return;
		if(!(tridasList.size()>0)) return;
		Class clazz;
		
		for(ITridas entity : tridasList)
		{
			if(entity instanceof TridasObject)
			{
				attrs = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
				clazz = TridasObject.class;
			}
			else if(entity instanceof TridasElement)
			{
				attrs = new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.CONE, 0.6d);
				clazz = TridasElement.class;
			}
			else if(entity instanceof TridasDerivedSeries)
			{
				attrs = new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 0.6d);
			}
			else
			{
				return;
			}
		}
	}
	

	

	
}
