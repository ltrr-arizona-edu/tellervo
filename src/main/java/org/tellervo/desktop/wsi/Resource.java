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
package org.tellervo.desktop.wsi;

import java.awt.EventQueue;
import java.awt.Window;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.gui.UserCancelledException;


/*
 * A resource is essentially a wrapper for an XML document acquired
 * from or to be sent to our web server. It is intended to be a base class from which
 * other resources are to be derived
 */

/**
 * @author Lucas Madar
 *
 */
public abstract class Resource<INTYPE, OUTTYPE> {

	private final static Logger log = LoggerFactory.getLogger(Resource.class);

	/** The noun of our resource (measurementSeries, dictionary, etc) */
	private String resourceName; // the noun associated with this resource

	/**
	 * A map of properties, used for setting flags to whatever
	 * actually accesses the resource
	 * 
	 * @see ResourceProperties
	 */
	private Map<String, Object> properties;
	
	/**
	 * A resource that binds to the webservice for data
	 * 
	 * @param resourceName the noun this resource binds to (ie, 'dictionaries')
	 * @param queryType the query type (CRUD)
	 */
	public Resource(String resourceName) {
		this.resourceName = resourceName;
	}
	
	/**
	 * 
	 * @return the noun associated with this resource
	 */
	public String getResourceName() { 
		return resourceName; 
	}
	
	/**
	 * A return type for preprocessing a query
	 * */
	public static enum PreprocessResult {
		/** Try and access the resources again */
		TRY_AGAIN,
		/** Failure: Don't try again (calls queryFailed) */
		FAILURE,
		/** Failure because of user cancellation */
		FAILURE_CANCELLED,
		/** Success: Passes these results on to the next stage of processing */
		SUCCESS
	}

	/**
	 * Acquires a DataAccessor used to access this resource
	 * 
	 * @return The DataAccessor
	 */
	public abstract DataAccessor<INTYPE, OUTTYPE> getDataAccessor();
	
	/**
	 * This function queries the webservice with the 'read' verb.
	 * The processQueryResult function is called upon success; 
	 * the queryFailed function is called upon failure.
	 * 
	 * queryWait does not return until query has succeeded or until an error
	 * occurs.
	 */
	public boolean queryWait() {
		/** have we notified anyone of completion? */
		boolean notifiedOfCompletion = false;
		
		try {
			DataAccessor<INTYPE, OUTTYPE> wx = getDataAccessor();
			INTYPE inObject;

			// prepare the query with our request object
			wx.setRequestObject(getQueryObject());

			// notify debug listeners!
			fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_DEBUG_OUT));

			/*
			 * Keep trying to query the document until we get
			 * a non-recoverable error or success!
			 */
			while(true) {
				// if we get no exceptions, break out of the loop.
				inObject = wx.query(); 
					
				// notify debug listeners!
				fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_DEBUG_IN));
				
				try {
					switch(preprocessQuery(inObject)) {
					
					// query the resource again
					case TRY_AGAIN:
						continue;
					
					// whoo hoo!	
					case SUCCESS:
						if(processQueryResult(inObject)) {
							notifiedOfCompletion = true;
							doQuerySucceeded(inObject);
							return true;
						}
						else {
							// we had a loading/parsing error
							fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_FAILED, 
									new ResourceException("Parsing failed")));
							notifiedOfCompletion = true;
							return false;
						}
						
					case FAILURE:
						throw new ResourceException("Preprocessor returned generic failure");
												
					case FAILURE_CANCELLED:
						throw new UserCancelledException();
						
					default:
						throw new IllegalStateException("Preprocessor returned an illegal state!");
					}
				} catch (ResourceException res) {
					notifiedOfCompletion = true;
					doQueryFailed(res);
					return false;
				}
			}
		} catch (IOException ioe) {
			notifiedOfCompletion = true;
			doQueryFailed(ioe);
			return false;
		} catch (UserCancelledException uce) {
			notifiedOfCompletion = true;
			doQueryFailed(uce);
			return false;
		} finally {
			if(!notifiedOfCompletion) {
				// make SURE we notify completion. If we got here, something went wrong. Badly.
				
				ResourceException dummyException = new ResourceException(
						"Internal error accessing resource. Check application logs.");
				fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_FAILED, 
						dummyException));

				// prompt that it's a bug, too
				new BugDialog(dummyException);
			}
		}
		
		// should be unreachable
	}

	/**
	 * Preprocess the query response
	 * 
	 * Use this to check for things such as validity, login errors, etc, 
	 * and handle them appropriately. The default implementation always
	 * returns success.
	 * 
	 * If a failure occurs, throw a descriptive ResourceException. 
	 * Returning FAILURE just throws a generic ResourceException.
	 * 
	 * @param object
	 * @return a PreprocessResult
	 * @throws ResourceException 
	 * @throws UserCancelledException 
	 * @see PreprocessResult for stuff
	 */
	protected PreprocessResult preprocessQuery(INTYPE object) throws ResourceException, UserCancelledException {
		return PreprocessResult.SUCCESS;
	}
	
	/**
	 * Internal method: called when the query succeeds
	 * 
	 * @param object
	 */
	protected void doQuerySucceeded(INTYPE object) {
		try {
			// Call our (potentially overridden) querySucceeded method
			querySucceeded(object);
		} finally {
			// Notify listeners that everything went well
			// no matter what!
			fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_COMPLETE, object));
		}
	}

	/**
	 * Internal method: called when the query fails
	 * 
	 * @param e the exception of failure!
	 */
	protected void doQueryFailed(Exception e) {
		try {
			// Call our (potentially overridden) queryFailed method
			queryFailed(e);
		} finally {	
			// Notify listeners that our query failed
			// No matter what!
			fireResourceEvent(new ResourceEvent(this, ResourceEvent.RESOURCE_QUERY_FAILED, e));
		}
	}

	/**
	 * Get the query object (Thing we send OUT)
	 * 
	 * @return An object of the right type for the query
	 * @throws ResourceException if error preparing query
	 */
	protected abstract OUTTYPE getQueryObject() 
	throws ResourceException;
	
	/**
	 * In this function, parse the document and populate any internal variables.
	 * 
	 * WARNING: This function *must* be threadsafe. 
	 * This means it must not change any class variables without synchronizing against them!
	 * 
	 * If this function returns false, queryFailed is not called
	 * If it throws an exception, queryFailed IS called
	 * 
	 * @param object The object obtained by the DataAccessor's query function
	 * @return true on success, false on failure
	 * @throws ResourceException if error parsing
	 */
	protected abstract boolean processQueryResult(INTYPE object)
	throws ResourceException; 
	
	/**
	 * Called if processQueryResult returns true
	 * 
	 * @param object The successful in object
	 */
	protected void querySucceeded(INTYPE object) {
	}
	
	/**
	 * In this function, handle any failure condition.
	 * This is only called if processQueryResult() is not called.
	 */
	protected void queryFailed(Exception e) {
		log.error("Failed to query resource " + resourceName);
		e.printStackTrace();
		
		if(e instanceof ResourceException && e.getCause() != null) {
			log.error("Caused by:"+ e.getCause().getLocalizedMessage());
		}
	}
	
	private Thread queryThread;

	/**
	 * This procedure simply starts a new thread and calls queryWait
	 */
	public void query() {
		queryThread = new Thread() {
			@Override
			public void run() {
				queryWait();
				
				queryThread = null;
			}
		};
		
		queryThread.start();
	}
	
	
	public void abortQuery() {
		if(queryThread != null)
			queryThread.interrupt();
	}	
	
	/** The window that requested this resource */
	private Window ownerWindow;
	
	/**
	 * Associate an owner window with this resource (can be null)
	 * @param window
	 */
	public void setOwnerWindow(Window window) {
		this.ownerWindow = window;
	}

	/**
	 * Get the associated owner window (can be null)
	 * @return
	 */
	public Window getOwnerWindow() {
		return this.ownerWindow;
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
		
		log.debug("DUMPING LISTENERS " + listeners.length);
		
		// For some reason, this array is stored oddly. ookay, java...
		for(int i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == ResourceEventListener.class)
				log.debug("Listening: " + listeners[i + 1]);
		}		
	}
	
	/**
	 * Set a property to the given value
	 * @param propertyName
	 * @param value
	 */
	public void setProperty(String propertyName, Object value) {
		// lazy-create properties
		if(properties == null) 
			properties = new HashMap<String, Object>();
		
		properties.put(propertyName, value);
	}

	/**
	 * Add all the properties in the given map
	 * @param newProperties
	 */
	public void setProperties(Map<String, ? extends Object> newProperties) {
		// lazy-create properties
		if(properties == null) 
			properties = new HashMap<String, Object>();
		
		properties.putAll(newProperties);
	}
	
	/**
	 * Check to see if this property is set
	 * @param propertyName
	 * @return true if this property exists, false otherwise
	 */
	public boolean hasProperty(String propertyName) {
		if(properties == null)
			return false;
		
		return properties.containsKey(propertyName);
	}
	
	/**
	 * Get a property
	 * @param <T> the property type
	 * @param propertyName
	 * @param propertyClass
	 * @return The value of the property, casted, or null if it doesn't exist
	 */
	public <T> T getProperty(String propertyName, Class<T> propertyClass) {
		if(properties == null)
			return null;

		Object value = properties.get(propertyName);
		
		return (value != null) ? propertyClass.cast(value) : null;
	}
}
