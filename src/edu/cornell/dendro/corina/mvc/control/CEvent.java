/**
 * Created at 2:22:05 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.mvc.control;

/**
 * Simple event, stands for Corina Event (Event is already in Java, so we don't
 * want any confusion).
 * @author daniel
 */
public class CEvent {
	public final String key;
	
	public CEvent(final String argKey) {
		key = argKey;
	}

	@Override
	public String toString() {
		return super.toString() + "-" + key;
	}
	
	/**
	 * Dispatches the event.  Events are dispatched globally, so make
	 * sure you're key is unique!
	 */
	public void dispatch(){
		CorinaMVC.dispatchEvent( this);
	}
}
