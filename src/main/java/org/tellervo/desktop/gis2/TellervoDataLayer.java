package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;

import java.util.ArrayList;

import org.tellervo.desktop.gis.TridasMarker;


public interface TellervoDataLayer {
	
	
	
	
	public abstract ArrayList<TridasMarker> getTridasMarkers();
	
	public abstract void setMarkerStyle(BasicMarkerAttributes marker);
	public abstract BasicMarkerAttributes getMarkerStyle();

	public abstract void setHighlightedMarkerStyle(BasicMarkerAttributes marker);
	public abstract BasicMarkerAttributes getHighlightedMarkerStyle();
	
}
