package edu.cornell.dendro.corina.tridas;

/**
 * A quick and dirty java 'Properties' class that converts values to Class types
 */

import java.util.Properties;

public class PropertiesClass extends Properties {

	public Class<?> getClassFor(String key, Class<?> defaultClass) {
		Class<?> clazz = (Class<?>) super.get(key);
		
		return (clazz == null) ? defaultClass : clazz;
	}

	public Class<?> getClassFor(String key) {
		return (Class<?>) super.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public synchronized Object put(Object key, Object value) {
		String className = value.toString();
		Class<?> clazz;
		try {
			clazz = Class.forName(className);			
			return super.put(key, clazz);
		} catch (ClassNotFoundException e) {
			System.err.println("ClassProperties: Can't load " + className);
			return null;
		}
	}

}
