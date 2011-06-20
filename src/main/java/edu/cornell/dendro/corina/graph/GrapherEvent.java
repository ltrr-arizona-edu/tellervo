/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import java.util.EventObject;

/**
 * @author Lucas Madar
 *
 */
public class GrapherEvent extends EventObject {
	private static final long serialVersionUID = -1200925825114491334L;

	public static enum Type {
		/** The X offset of this graph changed (and probably the range) */
		XOFFSET_CHANGED,
		/** The Y offset of this graph changed */
		YOFFSET_CHANGED,
		/** The scale of this graph changed */
		SCALE_CHANGED
	}
	
	private final Graph graph;
	private final Type eventType;

	/**
	 * Create a new graph event about this graph
	 * 
	 * @param source
	 * @param graph
	 * @param eventType
	 */
	public GrapherEvent(GrapherPanel source, Graph graph, Type eventType) {
		super(source);
		
		this.graph = graph;
		this.eventType = eventType;
	}
	
	/**
	 * Get the associated graph
	 * @return a Graph structure
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Get the event type
	 * @return a GrapherEvent.Type
	 */
	public Type getEventType() {
		return eventType;
	}
}
