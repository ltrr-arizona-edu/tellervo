/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.io.IOException;

/**
 * @author lucasm
 *
 */
public class ResourceException extends IOException {

	public ResourceException() {
	}

	/**
	 * @param message
	 */
	public ResourceException(String message) {
		super(message);
	}
}
