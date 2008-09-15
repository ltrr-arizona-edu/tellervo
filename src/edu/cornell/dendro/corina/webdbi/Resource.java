package edu.cornell.dendro.corina.webdbi;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.util.WeakEventListenerList;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.event.EventListenerList;

/*
 * A resource is essentially a wrapper for an XML document acquired
 * from or to be sent to our web server. It is intended to be a base class from which
 * other resources are to be derived
 */

/**
 * @author Lucas Madar
 *
 */
public abstract class Resource {
	
	private String resourceName; // the noun associated with this resource
	protected ResourceQueryType queryType;
	
	/**
	 * A resource that binds to the webservice for data
	 * 
	 * @param resourceName the noun this resource binds to (ie, 'dictionaries')
	 */
	public Resource(String resourceName) {
		this(resourceName, ResourceQueryType.READ);
	}
	
	/**
	 * A resource that binds to the webservice for data
	 * 
	 * @param resourceName the noun this resource binds to (ie, 'dictionaries')
	 * @param queryType the query type (CRUD)
	 */
	public Resource(String resourceName, ResourceQueryType queryType) {
		this.resourceName = resourceName;
		this.queryType = queryType;
	}
	
	public String getResourceName() { 
		return resourceName; 
	}
	
	/**
	 * @return the queryType
	 */
	public ResourceQueryType getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(ResourceQueryType queryType) {
		this.queryType = queryType;
	}

	public static final int PROMPT_FOR_LOGIN = 1;
	public static final int JUST_FAIL = 2;
	
	private int resourceQueryExceptionBehavior = PROMPT_FOR_LOGIN;

	/**
	 * Sets the behavior to perform on a permissions exception:
	 * PROMPT_FOR_LOGIN (default) or JUST_FAIL
	 * 
	 * @param behavior
	 */
	public void setResourceQueryExceptionBehavior(int behavior) {
		resourceQueryExceptionBehavior = behavior;
	}
	
	/**
	 * Handle the query exception.
	 * 
	 * This function is responsible for calling queryFailed. It must not pop up
	 * any dialogs except for a login dialog.
	 * 
	 * @param w the exception
	 * @return true if we should try accessing the resource again, false if not.
	 */
	protected boolean handleQueryException(WebPermissionsException w) {
		System.out.println(w);
		int code = w.getMessageCode();
		
		// if we're set to prompt for login on failure, do so!
		if (resourceQueryExceptionBehavior == PROMPT_FOR_LOGIN &&
				(code == WebInterfaceException.ERROR_LOGIN_REQUIRED ||
				 code == WebInterfaceException.ERROR_AUTHENTICATION_FAILED)) {
			
			if(w.getNonce() == null) {
				// the server requires a login, but no nonce? er.. ?
				doQueryFailed(w);
				return false;
			}
			
			LoginDialog dialog = new LoginDialog();
			
			// dialog needs to know the nonce!
			dialog.setNonce(w.getNonce());

			try {
				
				if(code == WebInterfaceException.ERROR_AUTHENTICATION_FAILED)
					dialog.doLogin("Invalid username or password", true);
				else
					dialog.doLogin("(for access to " + resourceName + ")", false);
				
				// dialog succeeded?
				return true;
			} catch (UserCancelledException uce) {
				doQueryFailed(uce);
				return false;
			}
		}
		
		doQueryFailed(w);
		return false;
	}
		
	/**
	 * This function queries the webservice with the 'read' verb.
	 * The processQueryResult function is called upon success; 
	 * the queryFailed function is called upon failure.
	 * 
	 * queryWait does not return until query has succeeded or until an error
	 * occurs.
	 */
	public boolean queryWait() {
		try {
			WebXMLDocumentAccessor wx = new WebXMLDocumentAccessor(resourceName);
			Document doc;
			
			// we need to prepare the query
			prepareQuery(queryType, wx.createRequest(queryType));

			// notify debug listeners!
			fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_DEBUG_OUT, wx.getRequestDocument()));

			/*
			 * Keep trying to query the document until we get
			 * a non-recoverable error.
			 */
			while(true) {
				try {
					// if we get no exceptions, break out of the loop.
					doc = wx.query(); 
					
					// notify debug listeners!
					fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_DEBUG_IN, doc));

					break;
				} catch (WebPermissionsException wpe) {
					if(resourceQueryExceptionBehavior == PROMPT_FOR_LOGIN && !handleQueryException(wpe))
						return false;
					else if(resourceQueryExceptionBehavior == JUST_FAIL) {
						doQueryFailed(wpe);
						return false;
					}
					else 
						throw new IllegalStateException("Illegal queryExceptionBehavior!");
				}
			}
			
			if(processQueryResult(doc)) {
				querySucceeded(doc);
				return true;
			}
			else 
				// we had a parsing error
				fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_FAILED, 
						new ResourceException("Parsing failed")));
		} catch (IOException ioe) {
			doQueryFailed(ioe);
		}
		
		return false;
	}
	
	private void doQueryFailed(Exception e) {
		// Call our (potentially overridden) queryFailed method
		queryFailed(e);
		
		// Notify listeners that our query failed
		fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_FAILED, e));
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
	 * @param queryType enum ResourceQueryType
	 * @param requestElement an XML root element
	 * @throws ResourceException if error preparing query
	 */
	protected abstract Element prepareQuery(ResourceQueryType queryType, Element requestElement) 
	throws ResourceException;
	
	/* EX:
	{
		// this is meant to be overloaded, but defaults to above
		return requestElement;
	}
	*/
	
	/**
	 * In this function, parse the document and populate any internal variables.
	 * 
	 * WARNING: This function *must* be threadsafe. 
	 * This means it must not change any class variables without synchronizing against them!
	 * 
	 * If this function returns false, queryFailed is not called
	 * If it throws an exception, queryFailed IS called
	 * 
	 * @param doc The XML JDOM document obtained by the query function
	 * @return true on success, false on failure
	 * @throws ResourceException if error parsing
	 */
	protected abstract boolean processQueryResult(Document doc)
	throws ResourceException; 
	
	/* EX:
	{
		// this is meant to be overloaded.
		return true;
	}
	*/
	
	/**
	 * Called if processQueryResult returns true
	 * Meant only to be overloaded by CachedResource
	 * @param doc
	 */
	protected void querySucceeded(Document doc) {
		fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_COMPLETE));
	}
	
	/**
	 * In this function, handle any failure condition.
	 * This is only called if processQueryResult() is not called.
	 */
	protected void queryFailed(Exception e) {
		System.err.println("Failed to query resource " + resourceName);
		e.printStackTrace();
		
		if(e instanceof ResourceException && e.getCause() != null) {
			System.err.println("Caused by:");
			e.getCause().printStackTrace();
		}
	}
	
	/**
	 * This procedure simply starts a new thread and calls queryWait
	 */
	public void query() {
		new Thread() {
			@Override
			public void run() {
				queryWait();
			}
		}.start();
	}
	
	
	/*
	 * our event notification handlers are below. This is pretty standard java,
	 * so it's not so commented.
	 */
	protected EventListenerList listenerList = new EventListenerList();
	
	public void addResourceEventListener(ResourceEventListener rel) {
		listenerList.add(ResourceEventListener.class, rel);
	}
	
	public void removeResourceEventListener(ResourceEventListener rel) {
		listenerList.remove(ResourceEventListener.class, rel);
	}
	
	protected void fireResourceEvent(final ResourceEvent re) {
		// listeners is a copy; its contents are threadsafe
		final Object[] listeners = listenerList.getListenerList();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// For some reason, this array is stored oddly. ookay, java...
				for(int i = 0; i < listeners.length; i += 2) {
					if(listeners[i] == ResourceEventListener.class) {
						((ResourceEventListener)listeners[i+1]).resourceChanged(re);
					}
				}

			}			
		});
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
