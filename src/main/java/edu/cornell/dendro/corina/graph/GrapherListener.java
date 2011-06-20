package edu.cornell.dendro.corina.graph;

import java.util.EventListener;

/**
 * A preliminary listener for graph events - maybe more to come
 * 
 * @author Lucas Madar
 */

public interface GrapherListener extends EventListener {
	public void graphChanged(GrapherEvent evt);
}
