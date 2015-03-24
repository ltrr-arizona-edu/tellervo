package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.markers.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	private HashMap<Sample, Marker> markers = new HashMap<Sample, Marker>();

	public TridasEntityLayer(String name)
	{
		setName(name);
		
        setOverrideMarkerElevation(true);
        setElevation(0);
        setEnablePickSizeReturn(true);

	}
	
	public Marker getMarkerForSample(Sample s)
	{
		addMarker(s);
		
		return markers.get(s);
		
	}
	
	public void highlightMarkerForSample(Sample s)
	{
	    Iterator it = markers.entrySet().iterator();
	   
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Marker value = (Marker) pair.getValue();
	        value.setAttributes(AllSitesLayer.defaultAttributes);
	    }
		
		getMarkerForSample(s).setAttributes(AllSitesLayer.highlightAttributes);
		
		
	}
	
    public Iterable<Marker> getMarkers()
    {
        return new ArrayList<Marker>(markers.values());
    }
	
    
    public void setMarkers(Iterable<Marker> markers)
    {
    	// not supported
    }
    
	public void addMarker(Sample s)
	{
		if(markers.containsKey(s))
		{
			return;
		}
		
		String seriesid = null;
		
		try{
			seriesid = s.getSeries().getIdentifier().getValue();
		} catch (Exception e)
		{
			markers.put(s, null);
			return;
		}
		
		SearchParameters search = null;
		
		
		
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
			markers.put(s, null);
			return;
		} 
		else 
		{
			// Search successful
			List<TridasObject> foundEntities = searchResource.getAssociatedResult();
			
			Marker marker = null;
			
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


			markers.put(s, marker);
				
			
			
		}

		
		
	}


	
	
	
}
