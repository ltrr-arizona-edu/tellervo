/**
 * 
 */
package edu.cornell.dendro.corina.wsi.util;

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
