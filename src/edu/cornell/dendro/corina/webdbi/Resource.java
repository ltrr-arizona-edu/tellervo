package edu.cornell.dendro.corina.webdbi;

import org.jdom.Document;
import org.jdom.Element;
import java.io.IOException;

/*
 * A resource is essentially a wrapper for an XML document acquired
 * from or to be sent to our web server. It is intended to be a base class from which
 * other resources are to be derived
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
			
			loadDocument(doc);
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
	 */
	public void loadDocument(Document doc) {
		// this is meant to be overloaded.		
	}
	
	/**
	 * In this function, handle any failure condition
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
}
