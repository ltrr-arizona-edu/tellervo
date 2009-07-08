package edu.cornell.dendro.corina.tridasv2;

import java.util.Collection;
import java.util.HashMap;

import org.tridas.interfaces.TridasIdentifiable;
import org.tridas.schema.TridasIdentifier;

/**
 * A quick map from TridasIdentifier to anything that implements ITridas
 * 
 * @author Lucas Madar
 *
 * @param <V> the type to map to
 */

public class TridasIdentifierMap<V extends TridasIdentifiable> extends HashMap<TridasIdentifier, V> {
	private static final long serialVersionUID = -8714556821286002816L;
	
	public TridasIdentifierMap() {
		super();
	}

	public TridasIdentifierMap(int initialCapacity) {
		// leave some extra room
		super(Math.max(16, (initialCapacity + 8)) & ~0x3);
	}

	/**
	 * Create a TridasIdentifierMap from the given collection of TridasIdentifiables
	 * @param c
	 */
	public TridasIdentifierMap(Collection<V> c) {
		this(c.size());
		
		for(V e : c)
			put(e);
	}	

	/**
	 * Add this element to the map
	 * 
	 * @param e
	 * @return the previous element with the same identifier
	 */
	public final V put(V e) {
		TridasIdentifier identifier = e.getIdentifier();
		
		if(identifier == null)
			throw new IllegalArgumentException(e.getClass().getName() + " is missing identifier");
		
		return put(identifier, e);
	}
}
