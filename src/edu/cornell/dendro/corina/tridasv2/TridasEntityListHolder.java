package edu.cornell.dendro.corina.tridasv2;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.tridas.schema.TridasEntity;
import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;

public class TridasEntityListHolder {
	private HashMap<String, List<TridasEntity>> listMap;
	private HashMap<String, ListQueryHolder> queryMap;
	private Window parentWindow;
	
	public TridasEntityListHolder() {
		listMap = new HashMap<String, List<TridasEntity>>();
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
	public synchronized void prepareChildList(TridasEntity parentObject) {
		// no identifier, can't get a list
		if(!parentObject.isSetIdentifier())
			return;
		
		String key = getKey(parentObject);
		
		if(listMap.containsKey(key) || queryMap.containsKey(key)) 
			return;

		doPrepareChildList(parentObject, key);
	}
	
	private ListQueryHolder doPrepareChildList(TridasEntity parentObject, String key) {
		ListQueryHolder qh = new ListQueryHolder();

		// make the resource (easy)
		qh.resource = new EntitySearchResource<TridasEntity>(parentObject);
		
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
	public List<TridasEntity> getChildList(TridasEntity parentObject, boolean goRemote) throws Exception {
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
			List<TridasEntity> entities = new ArrayList<TridasEntity>(qh.resource.getAssociatedResult());
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
	private String getKey(TridasEntity parentObject) {
		XmlRootElement root = parentObject.getClass().getAnnotation(XmlRootElement.class);
		TridasIdentifier identifier = parentObject.getIdentifier();
			
		if(root != null)
			return root.name() + ":" + identifier.toString();
		else
			return parentObject.getClass().getName() + ":" + identifier.toString();
	}

	private static class ListQueryHolder {
		public EntitySearchResource<TridasEntity> resource;
		public CorinaResourceAccessDialog dialog;
	}

}
