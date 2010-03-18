/**
 * 3:21:37 AM, Mar 17, 2010
 */
package edu.cornell.dendro.corina.mvc.control;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;


/**
 * Abstract controller.  Use {@link #registerEventKey(String, IEventListener)} and {@link #registerEventKey(String, String)}
 * to listen for events from the view objects.  The {@link #registerEventKey(String, String)} method is
 * easier to use, as it will call a method for you when an event with the given key is dispatched.
 * @author daniel
 */
public abstract class FrontController{

	private final HashMap<String, Vector<Method>> keyToMethods = new HashMap<String, Vector<Method>>();
	private final FrontControllerEventListener listener;
	
	// for getting the method
	private final Class<?> paramTypes[] = { CEvent.class };
	
	public FrontController(){
		listener = new FrontControllerEventListener(this);
	}
	/**
	 * Registers the listener to the given key. {@link CEvent}'s are dispatched globally, so 
	 * careful with the actual values of your keys and make sure they are unique.
	 * @param argKey
	 * @param argListener
	 */
	protected void registerEventKey(String argKey, IEventListener argListener){
		CorinaMVC.addEventListener( argKey, argListener);
	}
	
	/**
	 * Registers the given method with the event key.  The method must have only one argument
	 * of type {@link CEvent}.  It will be called with the event that was dispatched with this key.
	 * More than one method can be assigned to a key, and they will be called in the order of
	 * registration.  Duplicate registrations will be ignored.
	 * @param argKey
	 * @param argMethodName
	 */
	protected void registerEventKey(String argKey, String argMethodName){
		Vector<Method> methods = keyToMethods.get( argKey);
		
		if(methods == null){
			methods = new Vector<Method>();
			keyToMethods.put( argKey, methods);
		}
		
		Class<? extends FrontController> c = this.getClass();
		Method m;
		try {
			m = c.getMethod( argMethodName, paramTypes);
		} catch ( SecurityException e) {
			e.printStackTrace();
			return;
		} catch ( NoSuchMethodException e) {
			throw new RuntimeException("No such method '"+argMethodName+"'");
		}
		
		methods.add( m);
		
		CorinaMVC.addEventListener( argKey, listener);
	}
	
	private class FrontControllerEventListener implements IEventListener{
		private FrontController controller;
		public FrontControllerEventListener(FrontController argController) {
			controller = argController;
		}
		
		@Override
		public void eventReceived( CEvent argEvent) {
			Vector<Method> vec = keyToMethods.get(argEvent.key);
			for(Method m: vec){
				try {
					m.invoke( controller, argEvent);
				} catch ( IllegalArgumentException e) {
					System.err.println("Error invoking method '"+m+"'");
					e.printStackTrace();
				} catch ( IllegalAccessException e) {
					System.err.println("Error invoking method '"+m+"'");
					e.printStackTrace();
				} catch ( InvocationTargetException e) {
					System.err.println("Error invoking method '"+m+"'");
					e.printStackTrace();
				}
			}
		}
		
	}
}
