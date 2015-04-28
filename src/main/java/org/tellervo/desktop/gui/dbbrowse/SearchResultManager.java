/**
 * 
 */
package org.tellervo.desktop.gui.dbbrowse;

import java.util.Collection;

import org.tellervo.desktop.sample.ElementList;
import org.tridas.interfaces.ITridas;

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
	 * Called when a search for series finishes 
	 * 
	 * @param elements the search results, or null if the search failed/was cancelled
	 */
	public void notifySeriesSearchFinished(ElementList elements);
	
	/** 
	 * Called when a search for entities finishes 
	 * 
	 * @param elements the search results, or null if the search failed/was cancelled
	 */
	public void notifyEntitySearchFinished(Collection<ITridas> entities);
}
