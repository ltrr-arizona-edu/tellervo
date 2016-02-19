/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceCacher;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Lucas Madar
 *
 */
public class TridasObjectList extends TellervoResource {

	/**
	 * @param resourceName
	 * @param queryType
	 */
	public TridasObjectList() {
		super("tridas.objects", TellervoRequestType.READ);
		
		// ensure our data is all set up
		data = new ListViews();
		
		// load my cache and unload on a successful remote load
		new TellervoResourceCacher(this, true).load();
	}
	

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.wsi.tellervo.CorinaResource#populateRequest(org.tellervo.desktop.schema.WSIRequest)
	 */
	@Override
	protected void populateRequest(WSIRequest request) {
		SearchParameters params = new SearchParameters(SearchReturnObject.OBJECT);
		
		params.setIncludeChildren(true);
		params.addSearchConstraint(SearchParameterName.PARENTOBJECTID, SearchOperator.IS, "NULL");
		
		request.setSearchParams(params);
		request.setType(TellervoRequestType.SEARCH);
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {

		// get a list of only tridas objects
		List<TridasObjectEx> objects = ListUtil.subListOfType(
				object.getContent().getSqlsAndObjectsAndElements(), TridasObjectEx.class);
		
		ListViews newData = new ListViews();
		
		// create our lists
		for(TridasObjectEx obj : objects) {
			recursiveAdd(obj, newData);
		}
	
		Collections.sort(newData.allObjects, new SiteComparator(SiteComparator.Mode.ALPHABETICAL));
		
		// move our data over
		synchronized(data) {
			data.allObjects.clear();
			data.allObjects.addAll(newData.allObjects);
			data.bySiteCode.clear();
			data.bySiteCode.putAll(newData.bySiteCode);
		}
		
		return true;
	}

	/**
	 * Sort objects by # of children, lab code presence, and then lab code or title
	 */
	public static class SiteComparator implements Comparator<TridasObjectEx> {
		public static enum Mode {
			POPULATED_FIRST,
			ALPHABETICAL,
		}
		
		private final Mode mode;
		private Boolean incParentCode = true;
		
		public SiteComparator(Mode mode) {
			this.mode = mode;
		}
		
		public SiteComparator(Mode mode, Boolean incParentCode){
			this.mode = mode;
			this.incParentCode = incParentCode;
		}
		
		public SiteComparator() {
			this(Mode.ALPHABETICAL);
		}
		
		public void setIncParentCode(Boolean incParentCode){
			this.incParentCode = incParentCode;
		}
		
		public int compare(TridasObjectEx o1, TridasObjectEx o2) {
			
			// ones with children first
			Integer s1 = o1.getSeriesCount();
			Integer s2 = o2.getSeriesCount();
			boolean c1 = (s1 != null && s1 > 0);
			boolean c2 = (s2 != null && s2 > 0);
			if(mode == Mode.POPULATED_FIRST) {
				if(c1 && !c2)
					return -1;
				else if(!c1 && c2)
					return 1;
			}
					
			// ones with lab codes first
			c1 = o1.hasLabCode();
			c2 = o2.hasLabCode();
			if(c1 && !c2)
				return -1;
			else if(!c1 && c2)
				return 1;
		
			// get lab codes (including parents if applicable)
			TridasObjectEx p1;
			String code1;
			TridasObjectEx p2;
			String code2;
			if((p1 = o1.getParent()) != null && this.incParentCode) 
				code1 = p1.getLabCode()+o1.getLabCode();
			else
				code1 = o1.getLabCode();
			if((p2 = o2.getParent()) !=null && this.incParentCode)
				code2 = p2.getLabCode()+o2.getLabCode();
			else
				code2 = o2.getLabCode();
						
			if(c1 && c2)
				return code1.compareToIgnoreCase(code2);
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
	 * @return a tridas object
	 */
	public TridasObjectEx findObjectBySiteCode(String siteCode) {
		synchronized(data) {
			return data.bySiteCode.get(siteCode);
		}
	}
	
	/**
	 * Retrieve an unmodifiable list of all tridas objects
	 * @return a list
	 */
	public synchronized List<TridasObjectEx> getObjectList() {
		synchronized(data) {
			return Collections.unmodifiableList(data.allObjects);
		}
	}
	
	/**
	 * updates the tridas object with the same lab code currently in the list to
	 * this tridas object.
	 * @param argObject
	 * @return if an object was found and updated
	 */
	public boolean updateTridasObject(TridasObjectEx argObject){
		synchronized (data) {
			for(TridasObjectEx o : data.allObjects){
				if(o.getLabCode().equals(argObject.getLabCode())){
					argObject.copyTo(o);
					return true;
				}
			}
		}
		System.err.println("Could not find object to update: "+argObject.getLabCode());
		return false;
	}
	
	/**
	 * adds an object to the list, which will notify listeners
	 * @param argObject
	 */
	public void addTridasObject(TridasObjectEx argObject){
		synchronized (data) {
			data.allObjects.add(argObject);
			
			for(TridasObjectEx  o: data.allObjects)
			{
				if(o.getLabCode().equals(argObject.getLabCode()))
				{
					System.out.println("Found it!");
				}
			}
			
			data.bySiteCode.put(argObject.getLabCode(), argObject);
		}
	}
	
	/**
	 * Adds a property change listener to the object list
	 * @param argListener
	 */
	public void addPropertyChangeListener(PropertyChangeListener argListener){
		data.allObjects.addPropertyChangeListener(argListener);
	}
	
	/**
	 * removes a property change listener from the object list
	 * @param argListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener argListener){
		data.allObjects.removePropertyChangeListener(argListener);
	}
	
	/**
	 * Retrieves the mutable object list used to populate all other requests.
	 * Modifying this list will change results for other method calls in this class.
	 * One should not add/remove objects from this list, instead us {@link #addTridasObject(TridasObjectEx)}
	 * @return
	 */
	public synchronized MVCArrayList<TridasObjectEx> getMutableObjectList(){
		synchronized(data)
		{
			return data.allObjects;
		}
	}
	
	/**
	 * Retrieve a list of all tridas objects, populated first
	 * 
	 * @return a list
	 */
	public List<TridasObjectEx> getPopulatedFirstObjectList() {
		synchronized(data) {
			List<TridasObjectEx> populatedFirstList = new ArrayList<TridasObjectEx>(data.allObjects);
			
			// sort it how we like it...
			Collections.sort(populatedFirstList, new SiteComparator(SiteComparator.Mode.POPULATED_FIRST));
			
			// don't care if the user modifies this list, because we generated it
			return populatedFirstList;
		}
	}
	
	public List<TridasObjectEx> getTopLevelObjectList(){
		synchronized(data) {
			ArrayList<TridasObjectEx> topLevelList = new ArrayList<TridasObjectEx>(data.allObjects.size());
			
			for(TridasObjectEx obj : data.allObjects) {
				
				if(obj.isTopLevelObject())
				{
					topLevelList.add(obj);
				}

			}
			
			// don't care if the user modifies this list, because we generated it
			return topLevelList;
		}
	}
	
	/**
	 * Retrieve a list of all populated Tridas objects
	 * @return a list
	 */
	public List<TridasObjectEx> getPopulatedObjectList() {
		synchronized(data) {
			List<TridasObjectEx> populatedList = new ArrayList<TridasObjectEx>(data.allObjects.size());
			
			for(TridasObjectEx obj : data.allObjects) {
				Integer childSeriesCount = obj.getChildSeriesCount();
				
				if(childSeriesCount != null && childSeriesCount > 0)
					populatedList.add(obj);
			}
			
			// don't care if the user modifies this list, because we generated it
			return populatedList;
		}
	}
	
	private final ListViews data;
	private static class ListViews {
		public ListViews() {	
			allObjects = new MVCArrayList<TridasObjectEx>();
			bySiteCode = new TreeMap<String, TridasObjectEx>();
		}
		
		public final MVCArrayList<TridasObjectEx> allObjects;
		public final Map<String, TridasObjectEx> bySiteCode;
	}
}
