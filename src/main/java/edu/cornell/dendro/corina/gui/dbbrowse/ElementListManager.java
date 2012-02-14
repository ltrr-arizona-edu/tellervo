/**
 * 
 */
package edu.cornell.dendro.corina.gui.dbbrowse;

import edu.cornell.dendro.corina.sample.Element;

/**
 * @author Lucas Madar
 *
 */
public interface ElementListManager {
	/** The element has been deleted; remove it from any lists */
	public void deleteElement(Element e);
	
	/** Is the given element disabled? Used to render it in a greyed out state */
	public boolean isElementDisabled(Element e);
}
