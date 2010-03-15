/**
 * Created at 2:19:51 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.event;

/**
 * Interface for an Event Dispatcher
 * @author daniel
 */
public interface IEventDispatcher {

	/**
	 * Adds a listener for the given event key.
	 * 
	 * @param argKey
	 * @param argReciever
	 */
	public void addEventListener(String argKey, IEventListener argListener);
	
	/**
	 * removes a listener from the given key.
	 * 
	 * @param argKey
	 * @param argReciever
	 * @return true if the listener was removed, and false if it wasn't there to
	 *         begin with
	 */
	public boolean removeEventListener(String argKey, IEventListener argListener);
}
