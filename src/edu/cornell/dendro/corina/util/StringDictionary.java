package edu.cornell.dendro.corina.util;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A static hash of entity names, so we don't reuse a lot of memory
 * 
 * @author lucasm
 */

public class StringDictionary {
	private static Map<String, String> entities = new WeakHashMap<String, String>();
	
	public static String getEntityName(String entityName) {
		String value = entities.get(entityName);
		
		if(value != null)
			return value;

		// make a new string, because we'll reference it weakly!
		value = new String(entityName);
		entities.put(entityName, value);
		
		return value;
	}
}
