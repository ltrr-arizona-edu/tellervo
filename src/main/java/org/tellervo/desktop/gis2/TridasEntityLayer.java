package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gis.TridasMarker;
import org.tellervo.desktop.sample.Sample;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;

public class TridasEntityLayer extends MarkerLayer implements TellervoPointDataLayer, TellervoDataLayer {
	
	private BasicMarkerAttributes markerAttrib = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
	private BasicMarkerAttributes highlightedMarkerAttrib = new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.CYLINDER, 0.6d);
	
	protected final static Logger log = LoggerFactory.getLogger(TridasEntityLayer.class);
	private HashMap<ITridas, TridasMarker> markermap = new HashMap<ITridas, TridasMarker>();

	public TridasEntityLayer(String name)
	{
		setName(name);
		
        setOverrideMarkerElevation(true);
        setElevation(0);
        setEnablePickSizeReturn(true);
        
        

	}
	
	public HashMap<ITridas, TridasMarker> getMarkerMap()
	{
		return markermap;
	}
	
	public Marker getMarkerForEntity(ITridas s)
	{
		addMarker(s);
		
		return markermap.get(s);
		
	}
	
	protected void highlightMarkerForSample(ITridas s)
	{
	    Iterator it = markermap.entrySet().iterator();
	   
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Marker value = (Marker) pair.getValue();
	        value.setAttributes(getMarkerStyle());
	    }
		
	    getMarkerForEntity(s).setAttributes(getHighlightedMarkerStyle());
		
		
	}		
	
    private void updateSuperMarkers()
    {
    	ArrayList<Marker> lst = new ArrayList<Marker>();
    	
 	    Iterator it = markermap.entrySet().iterator();
 	   
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Marker value = (Marker) pair.getValue();
	        lst.add(value);
	    }

    	setMarkers(lst);
    }
	

    
    
	public void addMarker(ITridas s)
	{
		if(markermap.containsKey(s))
		{
			return;
		}
			
			
		TridasMarker themarker = null;
		
		if(s instanceof TridasObject)
		{
			TridasObject obj = (TridasObject)s;		
			if(obj.isSetLocation() && (obj.getLocation().isSetLocationGeometry()))
			{
				try{				
					themarker = TridasMarkerFactory.getMarkerForTridasEntity(obj.getLocation().getLocationGeometry(), obj, this.getMarkerStyle());
				
				} catch (Exception e)
				{
					log.error("Failed to get TridasMarker");
				}
			}
			else
			{
				return;
			}
		}
		else if (s instanceof TridasElement)
		{
			TridasElement el = (TridasElement)s;		
			if(el.isSetLocation() && (el.getLocation().isSetLocationGeometry()))
			{
				try{				
					themarker = TridasMarkerFactory.getMarkerForTridasEntity(el.getLocation().getLocationGeometry(), el, this.getMarkerStyle());
				
				} catch (Exception e)
				{
					log.error("Failed to get TridasMarker");
				}
			}
			else
			{
				return;
			}
		}
		else{
			return;
		}
		

		if(themarker!=null){
			markermap.put(s, themarker);
			updateSuperMarkers();
		}
		else
		{
			markermap.put(s, null);
			updateSuperMarkers();
			return;
		}
		
	}

	@Override
	public ArrayList<TridasMarker> getTridasMarkers() {
	
		ArrayList<TridasMarker> markers = new ArrayList<TridasMarker>();
	    
 	    Iterator it = markermap.entrySet().iterator();

		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        TridasMarker s = (TridasMarker) pair.getValue();
	       
	       markers.add(s);
	        
	    }
		
		return markers;
	}
	
	@Override
	public void setMarkerStyle(BasicMarkerAttributes style) {
	
		this.markerAttrib = style;
		
		for(Marker marker : this.getMarkers())
		{			
			marker.setAttributes(markerAttrib);
		}
		
	}

	@Override
	public BasicMarkerAttributes getMarkerStyle() {
			return markerAttrib;
	}

	@Override
	public void setHighlightedMarkerStyle(BasicMarkerAttributes marker) {
		this.highlightedMarkerAttrib = marker;
		
	}

	@Override
	public BasicMarkerAttributes getHighlightedMarkerStyle() {
		return highlightedMarkerAttrib;
	}


	
	
	
}
