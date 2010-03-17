/**
 * Created at 2:19:39 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This stores the listener information, and will dispatch events
 * to the corresponding listeners.
 * @author daniel
 */
public class MVCEventCenter {
	private static final HashMap<String, Vector<IEventListener>> listeners = new HashMap<String, Vector<IEventListener>>();
	
	private MVCEventCenter() {}

	/**
	 * Adds a listener for the given event key.
	 * 
	 * @param argKey
	 * @param argReciever
	 */
	public static void addEventListener(final String argKey, final IEventListener argListener) {

		if (listeners.containsKey(argKey)) {
			listeners.get(argKey).add(argListener);
		}
		else {
			final Vector<IEventListener> vec = new Vector<IEventListener>();
			vec.add(argListener);
			listeners.put(argKey, vec);
		}
	}

	/**
	 * gets the listeners for the given event key.
	 * 
	 * @param argKey
	 * @return
	 */
	public static Vector<IEventListener> getListeners(final String argKey) {
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
	public static boolean removeEventListener(final String argKey, final IEventListener argListener) {
		if (listeners.containsKey(argKey)) {
			final Vector<IEventListener> vec = listeners.get(argKey);
			return vec.remove(argListener);
		}
		return false;
	}

	/*
	 * Dispatches a event to the listeners.
	 *
	public static void dispatchEvent(final CEvent argEvent) {
		if (listeners.containsKey(argEvent.key)) {
			final Iterator<IEventListener> it = listeners.get(argEvent.key).iterator();
			
			while (it.hasNext()) {
				it.next().eventReceived( argEvent);
			}
		}
	}*/
	
	protected static void dispatchEvent(final CEvent argEvent) {
		if (listeners.containsKey(argEvent.key)) {
			final Iterator<IEventListener> it = listeners.get(argEvent.key).iterator();
			
			while (it.hasNext()) {
				it.next().eventReceived( argEvent);
			}
		}
	}

	/*
	 * @see java.util.HashMap#hashCode()
	 *
	@Override
	public int hashCode() {
		return listeners.hashCode();
	}
	
	@Override
	public boolean equals(final Object argOther) {
		if (this == argOther) {
			return true;
		}
		final MVCEventCenter disp = (MVCEventCenter) argOther;
		return listeners.equals(disp.listeners);
	}*/
}
