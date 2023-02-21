package org.tellervo.desktop.bulkdataentry.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingUtilities;

import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

public class TridasObjectElementListCache {

	private HashMap<TridasObject, ArrayList<TridasElement>> possElementCache = new HashMap<TridasObject, ArrayList<TridasElement>>();

	
	public TridasObjectElementListCache() {
		
	}
	
	public ArrayList<TridasElement> getPossibleElementsForObject(TridasObject obj)
	{
		
		if(possElementCache.containsKey(obj))
		{
			return possElementCache.get(obj);
		}
		
		return populateForObject(obj);
		
	}
	
	private ArrayList<TridasElement> populateForObject(TridasObject o) 
	{
	
			SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
	    	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue().toString());

	    	EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
			resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
			
			TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(BulkImportModel.getInstance().getMainView(), resource);
			resource.query();	
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) 
			{ 
				possElementCache.put(o, null);
				return null;
			}
			else
			{
				ArrayList<TridasElement> elList = new ArrayList<TridasElement>(resource.getAssociatedResult());
				possElementCache.put(o, elList);
				return elList;
			}

	}

	
	
	
}
