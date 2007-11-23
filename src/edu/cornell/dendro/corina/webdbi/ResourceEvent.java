/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.util.EventObject;

/**
 * @author Lucas Madar
 *
 */
public class ResourceEvent extends EventObject {
	public static final int RESOURCE_QUERY_COMPLETE = 1;

	private int eventType;
	public int getEventType() {
		return eventType;
	}
	/**
	 * @param source The object that created this event
	 * @param eventType The type of event as indicated in the ResourceEvent class
	 */
	public ResourceEvent(Object source, int eventType) {
		super(source);
		
		this.eventType = eventType;
	}

}
