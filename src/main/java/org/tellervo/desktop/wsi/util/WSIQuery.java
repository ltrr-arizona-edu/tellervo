package org.tellervo.desktop.wsi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.curation.BoxCuration;
import org.tellervo.desktop.tridasv2.TridasComparator;
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
import org.tridas.schema.TridasSample;

public class WSIQuery {
	private final static Logger log = LoggerFactory.getLogger(WSIQuery.class);

	
	public static List<TridasElement> getElementsOfObject(TridasObject obj)
	{
		if(obj==null) return null;
		
		if(!obj.isSetIdentifier()) return null;
		
		// Find all elements for an object 
    	SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
    	param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue().toString());

    	// we want an object return here, so we get a list of object->elements->samples when we use comprehensive
		EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting elements");
			return null;
		}
		
		List<TridasElement> elList = resource.getAssociatedResult();
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(elList, numSorter);
		
		return elList;
	}
	
	public static List<TridasSample> getSamplesOfElement(TridasElement el)
	{
		if(el==null) return null;
		
		if(!el.isSetIdentifier()) return null;
		
		// Find all samples for an element 
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, el.getIdentifier().getValue().toString());

    	// we want an element return here, so we get a list of elements->samples when we use comprehensive
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting samples");
			return null;
		}
		
		List<TridasSample> sampleList = resource.getAssociatedResult();
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampleList, numSorter);
		
		return sampleList;
	}
	
	public static List<TridasObject> getSamplesOfObject(TridasObject o)
	{
		if(o==null) return null;
		
		if(!o.isSetIdentifier()) return null;
		
		// Find all samples for an object 
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue().toString());

		EntitySearchResource<TridasObject> resource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting samples");
			return null;
		}
		
		List<TridasObject> obList = resource.getAssociatedResult();
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(obList, numSorter);
		
		return obList;
	}
	
}
