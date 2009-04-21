package edu.cornell.dendro.corina.tridas;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Maintains a weak mapping from tridas identifiers to any loaded objects
 * Provides a way of 'caching'
 * 
 * @author lucasm
 *
 */

public class TridasEntityMap {
	private static Map<TridasIdentifier, TridasEntityBase> tridasMap = new WeakHashMap<TridasIdentifier, TridasEntityBase>();
	
	/**
	 * Look for an object related to this tridas identifier
	 * @param identifier
	 * @return
	 */
	public static TridasEntityBase find(TridasIdentifier identifier) {
		return tridasMap.get(identifier);
	}
	
	/**
	 * Add this object by tridas identifier
	 * @param obj
	 */
	public static void put(TridasEntityBase obj) {
		tridasMap.put(obj.getIdentifier(), obj);
	}
}
