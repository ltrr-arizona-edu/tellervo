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
package org.tellervo.desktop.tridasv2.ui.support;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.tellervo.desktop.wsi.corina.CorinaResourceAccessDialog;
import org.tellervo.desktop.wsi.corina.resources.EntitySearchResource;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasIdentifier;


public class TridasEntityListHolder {
	private HashMap<String, List<ITridas>> listMap;
	private HashMap<String, ListQueryHolder> queryMap;
	private Window parentWindow;
	
	public TridasEntityListHolder() {
		listMap = new HashMap<String, List<ITridas>>();
		queryMap = new HashMap<String, ListQueryHolder>();
	}
	
	public void setParentWindow(Window window) {
		this.parentWindow = window;
	}
	
	/**
	 * Initiate a query for this child's objects
	 * 
	 * @param parentObject
	 */
	public synchronized void prepareChildList(ITridas parentObject) {
		// no identifier, can't get a list
		if(!parentObject.isSetIdentifier())
			return;
		
		String key = getKey(parentObject);
		
		if(listMap.containsKey(key) || queryMap.containsKey(key)) 
			return;

		doPrepareChildList(parentObject, key);
	}
	
	/**
	 * Append the given object to parentObject's combo list
	 * 
	 * @param parentObject
	 * @param object
	 */
	public synchronized void appendChildToList(ITridas parentObject, ITridas object) {
		String key = getKey(parentObject);
		List<ITridas> list = listMap.get(key);
		
		// lazily create if it doesn't exist
		if(list == null) {
			list = new ArrayList<ITridas>(1);
			listMap.put(key, list);
		}
		
		// add the new object to list
		list.add(object);
	}
	
	private ListQueryHolder doPrepareChildList(ITridas parentObject, String key) {
		ListQueryHolder qh = new ListQueryHolder();

		// make the resource (easy)
		qh.resource = new EntitySearchResource<ITridas>(parentObject, true);
		
		// make the dialog
		if(parentWindow == null || parentWindow instanceof Frame)
			qh.dialog = new CorinaResourceAccessDialog((Frame) parentWindow, qh.resource);
		else if(parentWindow instanceof Dialog)
			qh.dialog = new CorinaResourceAccessDialog((Dialog) parentWindow, qh.resource);
		else 
			throw new IllegalArgumentException("Not frame or dialog??");
		
		// store it in our map
		queryMap.put(key, qh);
		
		// start querying the resource
		qh.resource.query();
		
		return qh;
	}
	
	/**
	 * Get a list of child objects (either from cache or for the server)
	 * May pop up a dialog for waiting
	 * 
	 * @param parentObject
	 * @param goRemote should I try loading from the remote server if I haven't already?
	 * @return
	 * @throws Exception
	 */
	public synchronized List<ITridas> getChildList(ITridas parentObject, boolean goRemote) throws Exception {
		// no identifier -> no children!
		if(!parentObject.isSetIdentifier()) {
			return Collections.emptyList();
		}
		
		String key = getKey(parentObject);

		// already have the list? nice!
		if(listMap.containsKey(key))
			return listMap.get(key);
		
		// are we currently processing this query?
		ListQueryHolder qh;
		if(queryMap.containsKey(key))
			qh = queryMap.get(key);
		else {
			if(!goRemote)
				return Collections.emptyList();
			
			// no, start the query up
			qh = doPrepareChildList(parentObject, key);
		}
			
		// start the blocking dialog (this may return immediately if this query is completed)
		qh.dialog.setVisible(true);

		// we're done with this query: remove it
		queryMap.remove(key);

		if(qh.dialog.isSuccessful()) {
			// store the query results
			List<ITridas> entities = new ArrayList<ITridas>(qh.resource.getAssociatedResult());
			listMap.put(key, entities);
			
			return entities;
		}
		else
			throw qh.dialog.getFailException();
	}
	
	/**
	 * Gets a key for this parent object
	 * @param parentObject
	 * @return
	 */
	private String getKey(ITridas parentObject) {
		XmlRootElement root = parentObject.getClass().getAnnotation(XmlRootElement.class);
		TridasIdentifier identifier = parentObject.getIdentifier();
			
		if(root != null)
			return root.name() + ":" + identifier.toString();
		else
			return parentObject.getClass().getName() + ":" + identifier.toString();
	}

	private static class ListQueryHolder {
		public EntitySearchResource<ITridas> resource;
		public CorinaResourceAccessDialog dialog;
	}

}
