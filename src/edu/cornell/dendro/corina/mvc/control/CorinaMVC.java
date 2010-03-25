/**
 * Created at 2:19:39 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.mvc.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This stores all the listener information, and will dispatch events
 * to the corresponding listeners.
 * @author daniel
 */
public class CorinaMVC extends Thread{
	private static final CorinaMVC thread = new CorinaMVC();

	private final HashMap<String, Stack<IEventListener>> listeners = new HashMap<String, Stack<IEventListener>>();
	private final Queue<CEvent> eventQueue = new LinkedList<CEvent>();
	private volatile boolean running = false;
	
	private CorinaMVC() {
		super("CorinaMVC");
	}

	/**
	 * Adds a listener for the given event key.  If the listener is already listening
	 * to that key, then nothing is done.
	 * 
	 * @param argKey
	 * @param argReciever
	 */
	public synchronized static void addEventListener( String argKey, IEventListener argListener) {

		if (thread.listeners.containsKey(argKey)) {
			// return if we're already listening
			if( isEventListener( argKey, argListener)){
				return;
			}
			thread.listeners.get(argKey).add(argListener);
		}
		else {
			final Stack<IEventListener> stack = new Stack<IEventListener>();
			stack.push(argListener);
			thread.listeners.put(argKey, stack);
		}
	}
	
	/**
	 * Checks to see if the listener is listening to the given key.
	 * @param argKey
	 * @param argListener
	 * @return
	 */
	public synchronized static boolean isEventListener( String argKey, IEventListener argListener) {
		if(!thread.listeners.containsKey( argKey)){
			return false;
		}
		
		Stack<IEventListener> stack = thread.listeners.get( argKey);
		return stack.contains( argListener);
	}

	/**
	 * gets the listeners for the given event key.
	 * 
	 * @param argKey
	 * @return
	 */
	public synchronized static Stack<IEventListener> getListeners( String argKey) {
		if (thread.listeners.containsKey(argKey)) {
			return thread.listeners.get(argKey);
		}
		else {
			Stack<IEventListener> stack = new Stack<IEventListener>();
			thread.listeners.put(argKey, stack);
			return stack;
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
	public static synchronized boolean removeEventListener( String argKey, IEventListener argListener) {
		if (thread.listeners.containsKey(argKey)) {
			Stack<IEventListener> stack = thread.listeners.get(argKey);
			return stack.remove(argListener);
		}
		return false;
	}
	
	protected static void dispatchEvent( CEvent argEvent) {
		if (thread.listeners.containsKey(argEvent.key)) {
			thread.eventQueue.add( argEvent	);
			if(!thread.running){
				thread.start();
			}
		}
	}
	
	public synchronized static void stopDispatchThread(){
		System.out.println("Stopping CorinaMVC EventDispatch thread.");
		thread.running = false;
	}
	
	public synchronized static void restartDispatchThread(){
		if(thread.running){
			return;
		}
		thread.start();
	}
	
	@Override
	public void run(){
		running = true;
		System.out.println("CorinaMVC EventDispatch thread starting");
		while(running){
			if(eventQueue.isEmpty()){
				try {
					sleep(100);
				} catch ( InterruptedException e) {}
			}else {
				CEvent event = eventQueue.poll();
				internalDispatchEvent( event);
			}
		}
		System.out.println("CorinaMVC EventDispatch thread stopped");
	}
	
	private synchronized void internalDispatchEvent(CEvent argEvent){
		Stack<IEventListener> stack = listeners.get(argEvent.key);
		
		while(!stack.isEmpty() && argEvent.isPropagating()){
			stack.pop().eventReceived( argEvent);
		}
	}
}
