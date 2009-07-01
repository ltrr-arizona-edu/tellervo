/**
 * 
 */
package edu.cornell.dendro.corina_indexing;

import java.util.List;

/**
 * An 'indexable' is something that can be passed to an Index for indexing.
 * 
 * @author Lucas Madar
 *
 */
public interface Indexable {
	/**
	 * Get the data to index.
	 * 
	 * @return A list of data for indexing. The calling function must not modify the list.
	 */
	public List<? extends Number> getData();
}
