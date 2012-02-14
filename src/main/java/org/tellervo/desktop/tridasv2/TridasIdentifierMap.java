/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2;

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
