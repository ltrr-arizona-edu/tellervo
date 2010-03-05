/**
 * 
 */
package edu.cornell.dendro.corina.gui.dbbrowse;

import edu.cornell.dendro.corina.sample.ElementList;

/**
 * An interface for an object that gets notified about the state of
 * searches (currently, only DBBrowser cares)
 * 
 * @author Lucas Madar
 */
public interface SearchResultManager {
	/** 
	 * Called when a search is starting
	 * Can be invoked multiple times without a finish (invoking twice means the first search was cancelled)
	 */
	public void notifySearchStarting();
	
	/** 
	 * Called when a search finishes 
	 * 
	 * @param elements the search results, or null if the search failed/was cancelled
	 */
	public void notifySearchFinished(ElementList elements);
}
