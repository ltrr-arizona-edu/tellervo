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
package org.tellervo.desktop.wsi;

import java.util.EventObject;

/**
 * @author Lucas Madar
 *
 */
public class ResourceEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public static final int RESOURCE_QUERY_COMPLETE = 1; 	// attached object
	public static final int RESOURCE_QUERY_FAILED = 2; 		// may have optional attachedException!
	public static final int RESOURCE_DEBUG_IN = 3;			// no params
	public static final int RESOURCE_DEBUG_OUT = 4;			// no params

	private int eventType;
	private Exception attachedException;
	private Object attachedObject;
	
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
