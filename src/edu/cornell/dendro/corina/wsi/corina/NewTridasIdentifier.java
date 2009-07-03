package edu.cornell.dendro.corina.wsi.corina;

import org.tridas.schema.TridasIdentifier;

public class NewTridasIdentifier extends TridasIdentifier {
	private static final long serialVersionUID = 1L;

	private NewTridasIdentifier() {
		this.domain = "local";
		this.value = "new";
	}
	
	private static final NewTridasIdentifier instance = new NewTridasIdentifier();

	/**
	 * @return the 
	 */
	public static final TridasIdentifier getInstance() {
		return instance;
	}
	
	public static final boolean isNew(final TridasIdentifier identifier) {
		return instance.equals(identifier);
	}
}
