package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gis.TridasMarker;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.schema.TridasObject;

public class TridasEntityLayer extends MarkerLayer implements TellervoDataLayer {
	
	
	protected final static Logger log = LoggerFactory.getLogger(TridasEntityLayer.class);
	private HashMap<Sample, TridasMarker> markermap = new HashMap<Sample, TridasMarker>();

	public TridasEntityLayer(String name)
	{
		setName(name);
		
        setOverrideMarkerElevation(true);
        setElevation(0);
        setEnablePickSizeReturn(true);
        
        

	}
	
	public HashMap<Sample, TridasMarker> getMarkerMap()
	{
		return markermap;
	}
	
	public Marker getMarkerForSample(Sample s)
	{
		addMarker(s);
		
		return markermap.get(s);
		
	}
	
	protected void highlightMarkerForSample(Sample s)
	{
	    Iterator it = markermap.entrySet().iterator();
	   
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Marker value = (Marker) pair.getValue();
	        value.setAttributes(AllSitesLayer.defaultAttributes);
	    }
		
		getMarkerForSample(s).setAttributes(AllSitesLayer.highlightAttributes);
		
		
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
	
    /**
     * Removes any samples that we have in the layer that aren't in the provided array list.
     * 
     * @param samples
     */
    public void removeAbsentMarkers(ArrayList<Sample> samples)
    {
    	
    	    	
 	    Iterator it = markermap.entrySet().iterator();
 	   
 	    HashMap<Sample, TridasMarker>  markermap2 = (HashMap<Sample, TridasMarker>) markermap.clone();
 	    
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Sample s = (Sample) pair.getKey();
	       
	        if(!samples.contains(s))
	        {
	        	markermap2.remove(s);
	        }
	        
	    }

	    markermap = markermap2;
	    updateSuperMarkers();
    	
    }
    
    
	public void addMarker(Sample s)
	{
		if(markermap.containsKey(s))
		{
			return;
		}
		
		String seriesid = null;
		
		try{
			seriesid = s.getSeries().getIdentifier().getValue();
		} catch (Exception e)
		{
			markermap.put(s, null);
			updateSuperMarkers();
			return;
		}
		
		SearchParameters search = null;
		
		if(seriesid=="newSeries")
		{
			// Quit if this is a fresh series.
			return;
		}
		
		search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
		search.addSearchConstraint(SearchParameterName.SERIESDBID, SearchOperator.EQUALS, seriesid);
		
		// Do the search 
		EntitySearchResource<TridasObject> searchResource = new EntitySearchResource<TridasObject>(search, TridasObject.class);
		
		
		searchResource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(searchResource);
		searchResource.query();
		dlg.setVisible(true);
		
		
		if(!dlg.isSuccessful()) 
		{
			// Search failed
			markermap.put(s, null);
			updateSuperMarkers();
			return;
		} 
		else 
		{
			// Search successful
			List<TridasObject> foundEntities = searchResource.getAssociatedResult();
			
			TridasMarker marker = null;
			
			for(TridasObject on : foundEntities)
			{
				try{
					on.getElements().get(0).getLocation().getLocationGeometry();
					
					marker = AllSitesLayer.getMarkerForTridasEntity(on.getElements().get(0).getLocation().getLocationGeometry(), on.getElements().get(0));
				
				} catch (Exception e)
				{
					marker = AllSitesLayer.getMarkerForObject(on);
				}
			}


			if(marker!=null){
				markermap.put(s, marker);
				updateSuperMarkers();
			}
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


	
	
	
}
