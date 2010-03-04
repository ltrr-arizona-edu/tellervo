package org.tridas.interfaces;

import org.tridas.schema.TridasIdentifier;

/**
 * An entity that supports the tridasidentifier
 * 
 * @author Lucas Madar
 */

public interface TridasIdentifiable {
    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link TridasIdentifier }
     *     
     */
    public TridasIdentifier getIdentifier();
}
