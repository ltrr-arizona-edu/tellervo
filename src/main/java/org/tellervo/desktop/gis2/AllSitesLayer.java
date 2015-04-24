package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.TridasMarker;
import org.tridas.util.TridasObjectEx;

public class AllSitesLayer extends MarkerLayer implements TellervoPointDataLayer, TellervoDataLayer{
	
	private BasicMarkerAttributes markerAttrib = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);
	private BasicMarkerAttributes highlightedMarkerAttrib = new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.CYLINDER, 0.6d);

	public AllSitesLayer()
	{
		setName("All objects in database");
		
        setOverrideMarkerElevation(true);
        setElevation(0);
        setEnablePickSizeReturn(true);
        
        
        ArrayList<Marker> markers = new ArrayList<Marker>();
        
		for(TridasObjectEx obj : App.tridasObjects.getTopLevelObjectList())
		{
			TridasMarker m = TridasMarkerFactory.getMarkerForObject(obj, getMarkerStyle());
			if(m!=null) markers.add(m);
		}
		
		this.setMarkers(markers);
        
	}
	
	
	@Override
	public ArrayList<TridasMarker> getTridasMarkers() {
	
		ArrayList<TridasMarker> markers = new ArrayList<TridasMarker>();
	    
		for(Marker m : this.getMarkers())
		{
			markers.add((TridasMarker) m);
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
