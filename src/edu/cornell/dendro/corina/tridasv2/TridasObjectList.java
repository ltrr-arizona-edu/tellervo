/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceCacher;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;

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
		super("tridas.objects", CorinaRequestType.READ);
		
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
		SearchParameters params = new SearchParameters(SearchReturnObject.OBJECT);
		
		params.setIncludeChildren(true);
		params.addSearchConstraint(SearchParameterName.PARENTOBJECTID, SearchOperator.IS, "NULL");
		
		request.setSearchParams(params);
		request.setType(CorinaRequestType.SEARCH);
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {

		// get a list of only tridas objects
		List<TridasObjectEx> objects = ListUtil.subListOfType(
				object.getContent().getSqlsAndObjectsAndElements(), TridasObjectEx.class);
		
		ListViews newData = new ListViews(objects.size());
		
		// create our lists
		for(TridasObjectEx obj : objects) {
			recursiveAdd(obj, newData);
		}
	
		Collections.sort(newData.allObjects, new SiteComparator());
		
		// move our data over
		synchronized(data) {
			data = newData;
		}
		
		return true;
	}

	/**
	 * Sort objects by # of children, lab code presence, and then lab code or title
	 */
	private static class SiteComparator implements Comparator<TridasObjectEx> {
		public int compare(TridasObjectEx o1, TridasObjectEx o2) {
			Integer s1 = o1.getSeriesCount();
			Integer s2 = o2.getSeriesCount();
			boolean c1 = (s1 != null && s1 > 0);
			boolean c2 = (s2 != null && s2 > 0);
			
			// ones with children first
			if(c1 && !c2)
				return -1;
			else if(!c1 && c2)
				return 1;
			
			c1 = o1.hasLabCode();
			c2 = o2.hasLabCode();

			// ones with lab codes first
			if(c1 && !c2)
				return -1;
			else if(!c1 && c2)
				return 1;
		
			if(c1 && c2)
				return o1.getLabCode().compareToIgnoreCase(o2.getLabCode());
			else
				return o1.getTitle().compareToIgnoreCase(o2.getTitle());
		}
	
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
			for(TridasObjectEx child : ListUtil.subListOfType(obj.getObjects(), TridasObjectEx.class))
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
