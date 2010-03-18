/**
 * Created at 2:20:27 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.mvc.control;

/**
 * Interface for an event receiver.
 * @author daniel
 */
public interface IEventListener {
	
	/**
	 * this is what gets called when an event is dispatched that
	 * this object was listening for
	 * 
	 * @param argEvent
	 *            event dispatched
	 */
	public void eventReceived(final CEvent argEvent);
}
