/**
 * Created at 2:19:39 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.mvc.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This stores all the listener information, and will dispatch events
 * to the corresponding listeners.
 * @author daniel
 */
public class CorinaMVC {
	private static final HashMap<String, Vector<IEventListener>> listeners = new HashMap<String, Vector<IEventListener>>();
	
	private CorinaMVC() {}

	/**
	 * Adds a listener for the given event key.  If the listener is already listening
	 * to that key, then nothing is done.
	 * 
	 * @param argKey
	 * @param argReciever
	 */
	public static void addEventListener( String argKey, IEventListener argListener) {

		if (listeners.containsKey(argKey)) {
			// return if we're already listening
			if( isEventListener( argKey, argListener)){
				return;
			}
			listeners.get(argKey).add(argListener);
		}
		else {
			final Vector<IEventListener> vec = new Vector<IEventListener>();
			vec.add(argListener);
			listeners.put(argKey, vec);
		}
	}
	
	/**
	 * Checks to see if the listener is listening to the given key.
	 * @param argKey
	 * @param argListener
	 * @return
	 */
	public static boolean isEventListener( String argKey, IEventListener argListener) {
		if(!listeners.containsKey( argKey)){
			return false;
		}
		
		Vector<IEventListener> vec = listeners.get( argKey);
		return vec.contains( argListener);
	}

	/**
	 * gets the listeners for the given event key.
	 * 
	 * @param argKey
	 * @return
	 */
	public static Vector<IEventListener> getListeners( String argKey) {
		if (listeners.containsKey(argKey)) {
			return listeners.get(argKey);
		}
		else {
			final Vector<IEventListener> vec = new Vector<IEventListener>();
			listeners.put(argKey, vec);
			return vec;
		}
	}

	/**
	 * removes a listener from the given key.
	 * 
	 * @param argKey
	 * @param argReciever
	 * @return true if the listener was removed, and false if it wasn't there to
	 *         begin with
	 */
	public static boolean removeEventListener( String argKey, IEventListener argListener) {
		if (listeners.containsKey(argKey)) {
			final Vector<IEventListener> vec = listeners.get(argKey);
			return vec.remove(argListener);
		}
		return false;
	}
	
	protected static void dispatchEvent( CEvent argEvent) {
		if (listeners.containsKey(argEvent.key)) {
			final Iterator<IEventListener> it = listeners.get(argEvent.key).iterator();
			
			while (it.hasNext()) {
				it.next().eventReceived( argEvent);
			}
		}
	}
}
