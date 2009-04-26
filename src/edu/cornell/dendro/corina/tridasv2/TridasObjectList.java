/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.tridas.schema.TridasObject;

import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceCacher;
import edu.cornell.dendro.corina.wsi.corina.ResourceQueryType;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.TridasGenericFieldMap;

/**
 * @author Lucas Madar
 *
 */
public class TridasObjectList extends CorinaResource {

	/**
	 * @param resourceName
	 * @param queryType
	 */
	public TridasObjectList() {
		super("tridas.objects", ResourceQueryType.READ);
		
		// ensure our data is all set up
		data = new ListViews(0);
		
		// load my cache and unload on a successful remote load
		new CorinaResourceCacher(this, true).load();
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.corina.CorinaResource#populateRequest(edu.cornell.dendro.corina.schema.WSIRequest)
	 */
	@Override
	protected void populateRequest(WSIRequest request) {
		SearchParameters params = new SearchParameters("object");
		
		params.setIncludeChildren(true);
		params.addSearchConstraint("parentObjectID", "is", "NULL");
		
		request.setSearchParams(params);
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
				
		// get a list of only tridas objects
		List<TridasObjectEx> objects = ListUtil.subListOfType(
				object.getContent().getSqlOrObjectOrElement(), TridasObjectEx.class);
		
		ListViews newData = new ListViews(objects.size());
		
		// create our lists
		for(TridasObjectEx obj : objects) {
			recursiveAdd(obj, newData);
		}
		
		// move our data over
		synchronized(data) {
			data = newData;
		}
		
		return true;
	}

	/**
	 * Recursively add all objects to the list
	 * Deals with the object tree being n-deep
	 * @param obj
	 * @param view
	 */
	private void recursiveAdd(TridasObjectEx obj, ListViews view) {
		view.allObjects.add(obj);
		view.bySiteCode.put(obj.getLabCode(), obj);
		
		if(obj.hasChildren()) {
			for(TridasObjectEx child : ListUtil.subListOfType(obj.getObject(), TridasObjectEx.class))
				recursiveAdd(child, view);
		}
	}
	
	/**
	 * Find a tridas object by site code
	 * 
	 * @param siteCode
	 * @return
	 */
	public TridasObjectEx findObjectBySiteCode(String siteCode) {
		synchronized(data) {
			return data.bySiteCode.get(siteCode);
		}
	}
	
	/**
	 * Retrieve an unmodifiable list of all tridas objects
	 * @return
	 */
	public List<TridasObjectEx> getObjectList() {
		synchronized(data) {
			return Collections.unmodifiableList(data.allObjects);
		}
	}
	
	private ListViews data;
	private static class ListViews {
		public ListViews(int initSize) {	
			allObjects = new ArrayList<TridasObjectEx>();
			bySiteCode = new TreeMap<String, TridasObjectEx>();
		}
		
		public List<TridasObjectEx> allObjects;
		public Map<String, TridasObjectEx> bySiteCode;
	}
}
