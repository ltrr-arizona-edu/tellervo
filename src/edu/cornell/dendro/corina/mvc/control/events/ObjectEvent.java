/**
 * 1:29:25 AM, Mar 25, 2010
 */
package edu.cornell.dendro.corina.mvc.control.events;

import edu.cornell.dendro.corina.mvc.control.CEvent;

/**
 *
 * @author daniel
 */
public class ObjectEvent<K> extends CEvent {

	private final K object;
	/**
	 * @param argKey
	 */
	public ObjectEvent( String argKey, K argObject) {
		super( argKey);
		object = argObject;
	}
	
	public K getObject(){
		return object;
	}
}
