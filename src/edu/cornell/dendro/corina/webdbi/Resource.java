package edu.cornell.dendro.corina.webdbi;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.util.WeakEventListenerList;

import java.io.IOException;

/*
 * A resource is essentially a wrapper for an XML document acquired
 * from or to be sent to our web server. It is intended to be a base class from which
 * other resources are to be derived
 */

/**
 * @author Lucas Madar
 *
 */
public class Resource {
	
	private String resourceName; // the noun associated with this resource
	private boolean resourceLoaded;
	
	/**
	 * A resource that binds to the webservice for data
	 * 
	 * @param resourceName the noun this resource binds to (ie, 'dictionaries')
	 */
	public Resource(String resourceName) {
		this.resourceName = resourceName;
		this.resourceLoaded = false;
	}
	
	public String getResourceName() { return resourceName; }
	
	public WebXMLDocumentAccessor getDocumentAccessor(String verb) {
		return new WebXMLDocumentAccessor(resourceName, verb);
	}
	
	/**
	 * This function queries the webservice with the 'read' verb.
	 * The loadDocument function is called upon success; 
	 * the loadFailed function is called upon failure.
	 * 
	 * loadWait does not return until loading has succeeded or until an error
	 * occurs.
	 */
	public void loadWait() {
		try {
			Document doc = getDocumentAccessor("read").query();
			
			if(doc.getRootElement().getName().compareToIgnoreCase("corina") != 0)
				throw new IOException("Invalid XML document returned; Root element is not corina type.");
			
			if(loadDocument(doc))
				loadSucceeded(doc);
		} catch (IOException ioe) {
			loadFailed(ioe);
		}
	}
	
	/**
	 * In this function, parse the document and populate any internal variables.
	 * 
	 * WARNING: This function *must* be threadsafe. 
	 * This means it must not change any class variables without synchronizing against them!
	 * 
	 * @param doc The XML JDOM document obtained by the load function
	 * @return true on success, false on failure
	 */
	public boolean loadDocument(Document doc) {
		// this is meant to be overloaded.
		return true;
	}
	
	/**
	 * Called if loadDocument returns true
	 * Meant only to be overloaded by CachedResource
	 * @param doc
	 */
	protected void loadSucceeded(Document doc) {
		resourceLoaded = true;
		fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_LOADED));
	}
	
	/**
	 * In this function, handle any failure condition.
	 * This is only called if loadDocument() is not called.
	 */
	public void loadFailed(Exception e) {
		System.out.println("Failed to load resource " + resourceName);
		e.printStackTrace();
	}
	
	/**
	 * This procedure simply starts a new thread and calls loadWait
	 */
	public void load() {
		new Thread() {
			public void run() {
				loadWait();
			}
		}.start();
	}
	
	
	/*
	 * our event notification handlers are below. This is pretty standard java,
	 * so it's not so commented.
	 */
	protected WeakEventListenerList listenerList = new WeakEventListenerList();
	
	public void addResourceEventListener(ResourceEventListener rel) {
		listenerList.add(ResourceEventListener.class, rel);
	}
	
	public void removeResourceEventListener(ResourceEventListener rel) {
		listenerList.remove(ResourceEventListener.class, rel);
	}
	
	protected void fireResourceEvent(ResourceEvent re) {
		Object[] listeners = listenerList.getListenerList();
		
		// For some reason, this array is stored oddly. ookay, java...
		for(int i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == ResourceEventListener.class)
				((ResourceEventListener)listeners[i+1]).resourceChanged(re);
		}
	}
	
	public void debugDumpListeners() {
		Object[] listeners = listenerList.getListenerList();
		
		System.out.println("DUMPING LISTENERS " + listeners.length);
		
		// For some reason, this array is stored oddly. ookay, java...
		for(int i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == ResourceEventListener.class)
				System.out.println("Listening: " + listeners[i + 1]);
		}		
	}
}
