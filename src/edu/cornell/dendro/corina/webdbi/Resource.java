package edu.cornell.dendro.corina.webdbi;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.util.WeakEventListenerList;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.ui.Alert;


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
	private boolean resourceQueried; /** true if we have received a response from the server */
	protected ResourceQueryType queryType;
	
	/**
	 * A resource that binds to the webservice for data
	 * 
	 * @param resourceName the noun this resource binds to (ie, 'dictionaries')
	 */
	public Resource(String resourceName) {
		this(resourceName, new ResourceQueryType(ResourceQueryType.READ));
	}
	
	/**
	 * A resource that binds to the webservice for data
	 * 
	 * @param resourceName the noun this resource binds to (ie, 'dictionaries')
	 * @param queryType the query type (CRUD)
	 */
	public Resource(String resourceName, ResourceQueryType queryType) {
		this.resourceName = resourceName;
		this.resourceQueried = false;
		this.queryType = queryType;
	}
	
	public String getResourceName() { return resourceName; }
	
	/**
	 * Handle the query exception.
	 * 
	 * This function is responsible for calling queryFailed. It must not pop up
	 * any dialogs except for a login dialog.
	 * 
	 * @param w the exception
	 * @return true if we should try accessing the resource again, false if not.
	 */
	private boolean handleQueryException(WebPermissionsException w) {
		System.out.println(w);
		int code = w.getMessageCode();
		
		if (code == WebInterfaceException.ERROR_LOGIN_REQUIRED ||
			code == WebInterfaceException.ERROR_AUTHENTICATION_FAILED) {
			
			if(w.getNonce() == null) {
				// the server requires a login, but no nonce? er.. ?
				queryFailed(w);
				return false;
			}
			
			LoginDialog dialog = new LoginDialog();

			try {
				
				if(code == WebInterfaceException.ERROR_AUTHENTICATION_FAILED)
					dialog.doLogin("Invalid username or password", true);
				else
					dialog.doLogin("(for access to " + resourceName + ")", false);
				
				new Authenticate(dialog.getUsername(), dialog.getPassword(), w.getNonce()).queryWait();

				return true;
			} catch (UserCancelledException uce) {
				queryFailed(uce);
				return false;
			}
		}
		
		queryFailed(w);
		return false;
	}
		
	/**
	 * This function queries the webservice with the 'read' verb.
	 * The queryDocument function is called upon success; 
	 * the queryFailed function is called upon failure.
	 * 
	 * queryWait does not return until query has succeeded or until an error
	 * occurs.
	 */
	public void queryWait() {
		try {
			WebXMLDocumentAccessor wx = new WebXMLDocumentAccessor(resourceName);
			Document doc;
			
			// we need to prepare the query
			prepareQuery(wx.createRequest(queryType));
			
			/*
			 * Keep trying to query the document until we get
			 * a non-recoverable error.
			 */
			while(true) {
				try {
					// if we get no exceptions, break out of the loop.
					doc = wx.query(); 
					break;
				} catch (WebPermissionsException wpe) {
					if(!handleQueryException(wpe))
						return;
				}
			}
			
			/*
			 * we do this already in the documentaccessor
			if(doc.getRootElement().getName().compareToIgnoreCase("corina") != 0)
				throw new IOException("Invalid XML document returned; Root element is not corina type.");
				*/
			
			if(processQueryResult(doc))
				querySucceeded(doc);
		} catch (IOException ioe) {
			queryFailed(ioe);
		}
	}

	/**
	 * Prepare the query document, which is in the format below.
	 * The element passed is "request." 
	 * This is returned so the method can be chained.
	 * 
	 * <corina>
	 *    <request type="action">
	 *    </request>
	 * </corina>
	 * 
	 * @param requestElement 
	 */
	protected Element prepareQuery(Element requestElement) {
		// this is meant to be overloaded, but defaults to reading everything...
		return requestElement;
	}
	
	/**
	 * In this function, parse the document and populate any internal variables.
	 * 
	 * WARNING: This function *must* be threadsafe. 
	 * This means it must not change any class variables without synchronizing against them!
	 * 
	 * @param doc The XML JDOM document obtained by the query function
	 * @return true on success, false on failure
	 */
	protected boolean processQueryResult(Document doc) {
		// this is meant to be overloaded.
		return true;
	}
	
	/**
	 * Called if queryDocument returns true
	 * Meant only to be overloaded by CachedResource
	 * @param doc
	 */
	protected void querySucceeded(Document doc) {
		resourceQueried = true;
		fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_COMPLETE));
	}
	
	/**
	 * In this function, handle any failure condition.
	 * This is only called if queryDocument() is not called.
	 */
	protected void queryFailed(Exception e) {
		System.out.println("Failed to query resource " + resourceName);
		e.printStackTrace();
	}
	
	/**
	 * This procedure simply starts a new thread and calls queryWait
	 */
	public void query() {
		new Thread() {
			public void run() {
				queryWait();
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
