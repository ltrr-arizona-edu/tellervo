package edu.cornell.dendro.corina.wsi.corina;

import edu.cornell.dendro.corina.wsi.ResourceProperties;

public abstract class CorinaResourceProperties extends ResourceProperties {
	/**
	 * The request format we want to override.
	 * 
	 * Note that overriding with things like "minimal" and others
	 * can have unintended results (such as crashing). Be careful.
	 * 
	 * @see edu.cornell.dendro.corina.schema.CorinaRequestFormat
	 */
	public final static String ENTITY_REQUEST_FORMAT = "corina.requestFormat";
}
