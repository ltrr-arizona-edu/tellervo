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
import java.util.Collections;
import java.util.List;

import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceCacher;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tridas.schema.TridasProject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Lucas Madar
 *
 */
public class TridasProjectList extends TellervoResource {

	/**
	 * @param resourceName
	 * @param queryType
	 */
	public TridasProjectList() {
		super("tridas.projects", TellervoRequestType.READ);
		
		// ensure our data is all set up
		data = new MVCArrayList<TridasProject>();
		
		// load my cache and unload on a successful remote load
		new TellervoResourceCacher(this, true).load();
	}
	

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.wsi.tellervo.CorinaResource#populateRequest(org.tellervo.desktop.schema.WSIRequest)
	 */
	@Override
	protected void populateRequest(WSIRequest request) {
		SearchParameters params = new SearchParameters(SearchReturnObject.PROJECT);
		
		params.setIncludeChildren(false);
		params.addSearchConstraint(SearchParameterName.PROJECTTITLE, SearchOperator.NOT_EQUALS, "NULL");
		request.setSearchParams(params);
		request.setType(TellervoRequestType.SEARCH);
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {

		// get a list of only tridas projects
		List<TridasProject> projects = ListUtil.subListOfType(
				object.getContent().getSqlsAndProjectsAndObjects(), TridasProject.class);
			
		Collections.sort(projects, new TridasComparator());
		
		// move our data over
		synchronized(data) {
			data.clear();
			data.addAll(projects);
		}
		
		return true;
	}
	
	/**
	 * Retrieve an unmodifiable list of all tridas projects
	 * @return a list
	 */
	public synchronized List<TridasProject> getProjectList() {
		synchronized(data) {
			return Collections.unmodifiableList(data);
		}
	}
	
	/**
	 * updates the tridas project with the same identifier currently in the list to
	 * this tridas project.
	 * @param argProject
	 * @return if an project was found and updated
	 */
	public boolean updateTridasProject(TridasProject argProject){
		synchronized (data) {
			for(TridasProject p : data){
				if(p.getIdentifier().equals(argProject.getIdentifier())){
					argProject.copyTo(p);
					return true;
				}
			}
		}
		System.err.println("Could not find project to update: "+argProject.getTitle());
		return false;
	}
	
	/**
	 * adds a project to the list, which will notify listeners
	 * @param argProject
	 */
	public void addTridasProject(TridasProject argProject){
		synchronized (data) {
			data.add(argProject);
			
			
		}
	}
	
	/**
	 * Adds a property change listener to the object list
	 * @param argListener
	 */
	public void addPropertyChangeListener(PropertyChangeListener argListener){
		data.addPropertyChangeListener(argListener);
	}
	
	/**
	 * removes a property change listener from the object list
	 * @param argListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener argListener){
		data.removePropertyChangeListener(argListener);
	}
	
	/**
	 * Retrieves the mutable object list used to populate all other requests.
	 * Modifying this list will change results for other method calls in this class.
	 * One should not add/remove objects from this list, instead us {@link #addTridasProject(TridasObjectEx)}
	 * @return
	 */
	public synchronized MVCArrayList<TridasProject> getMutableObjectList(){
		synchronized(data)
		{
			return data;
		}
	}
	

		
	private final MVCArrayList<TridasProject> data;

}
