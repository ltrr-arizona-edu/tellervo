package edu.cornell.dendro.corina.wsi.corina;

import org.tridas.schema.TridasIdentifier;

public class NewTridasIdentifier extends TridasIdentifier {
	private static final long serialVersionUID = 1L;
	
	private static final String newSeriesIdentifier = "newSeries";

	private NewTridasIdentifier(String domain) {
		this.domain = domain;
		this.value = newSeriesIdentifier;
	}
	
	/**
	 * @return a new tridas identifier with the given domain
	 */
	public static final TridasIdentifier getInstance(String domain) {
		return new NewTridasIdentifier(domain);
	}
	
	/**
	 * @return a new tridas identifier with the domain of the source identifier
	 */
	public static final TridasIdentifier getInstance(TridasIdentifier sourceIdentifier) {
		return getInstance(sourceIdentifier.getDomain());
	}

	/**
	 * Checks if this series is new
	 * 
	 * @param identifier
	 * @return
	 */
	public static final boolean isNew(final TridasIdentifier identifier) {
		return newSeriesIdentifier.equals(identifier.getValue());
	}
}
