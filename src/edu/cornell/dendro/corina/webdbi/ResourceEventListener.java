/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.util.EventListener;

/**
 * @author Lucas Madar
 *
 */
public interface ResourceEventListener extends EventListener {
	public void resourceChanged(ResourceEvent re);
}
