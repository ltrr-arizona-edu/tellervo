/**
 * 
 */
package edu.cornell.dendro.corina.wsi;

import java.util.EventObject;

import org.jdom.Document;

/**
 * @author Lucas Madar
 *
 */
public class ResourceEvent extends EventObject {
	public static final int RESOURCE_QUERY_COMPLETE = 1; 	// attached object
	public static final int RESOURCE_QUERY_FAILED = 2; 		// may have optional attachedException!
	public static final int RESOURCE_DEBUG_IN = 3;			// no params
	public static final int RESOURCE_DEBUG_OUT = 4;			// no params

	private int eventType;
	private Exception attachedException = null;
	private Object attachedObject = null;
	
	public int getEventType() {
		return eventType;
	}
	
	public Exception getAttachedException() {
		return attachedException;
	}

	public Object getAttachedObject() {
		return attachedObject;
	}

	/**
	 * @param source The object that created this event
	 * @param eventType The type of event as indicated in the ResourceEvent class
	 */
	public ResourceEvent(Object source, int eventType) {
		super(source);
		
		this.eventType = eventType;
	}
	
	/**
	 * @param source The object that created this event
	 * @param eventType The type of event as indicated in the ResourceEvent class
	 * @param ex An exception tied to this message
	 */
	public ResourceEvent(Object source, int eventType, Exception ex) {
		super(source);
		
		this.eventType = eventType;
		this.attachedException = ex;
	}

	/**
	 * @param source The object that created this event
	 * @param eventType The type of event as indicated in the ResourceEvent class
	 * @param obj an attached object
	 */
	public ResourceEvent(Object source, int eventType, Object obj) {
		super(source);
		
		this.eventType = eventType;
		this.attachedObject = obj;
	}
}
