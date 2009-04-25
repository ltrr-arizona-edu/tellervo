/**
 * 
 */
package edu.cornell.dendro.corina.wsi;

import edu.cornell.dendro.corina.webdbi.ResourceException;

/**
 * Thrown when a resource is somehow invalid
 * 
 * Not for schema errors, but simply malformed documents
 */
public class MalformedResourceException extends ResourceException {
	private static final long serialVersionUID = -6237577480748267108L;

	public MalformedResourceException(String s) {
		super(s);
	}
}
