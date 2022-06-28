package org.tellervo.desktop.wsi.tellervo;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tridas.schema.TridasElement;

public class TridasElementTemporaryCacher {

	private static final Logger log = LoggerFactory.getLogger(TridasElementTemporaryCacher.class);

	HashMap<String, TridasElement> elementCache = new HashMap<String, TridasElement>();
	
	public TridasElement getTridasElement(String objectcode, String elementcode)
	{
		if(elementCache.containsKey(getKeyFromCodes(objectcode, elementcode))) 
			return elementCache.get(getKeyFromCodes(objectcode, elementcode));
		
		// Set return type to element and set search param
    	SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
    	param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, objectcode);
    	param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, elementcode);
    	EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			log.debug("Search for obj: "+objectcode+", elm: "+elementcode+" failed,. Error code "+dialog.getErrorCode());
			return null;
		}
		
		List<TridasElement> result = resource.getAssociatedResult();
		if(result.size()>1) {
			// Error should never happen
			log.debug("Search for obj: "+objectcode+", elm: "+elementcode+" failed. More than 1 result was found");

		}
		if(result.size()==1) {
			
			elementCache.put(getKeyFromCodes(objectcode, elementcode), result.get(0));
			return result.get(0);
		}
		
		log.debug("Search for obj: "+objectcode+", elm: "+elementcode+" failed. "+result.size()+ " results found.");
		return null;
	
	}
	
	private String getKeyFromCodes(String objectcode, String elementcode)
	{
		return objectcode+"----"+elementcode;
	}
	
}
