/**
 * 
 */
package edu.cornell.dendro.corina.wsi;

import java.io.IOException;

/**
 * An exception thrown when loading or acquiring a resource
 * @author lucasm
 */
public class ResourceException extends IOException {
	private static final long serialVersionUID = 2440001920979509944L;

	/**
	 * @param message
	 * @param cause
	 */
	public ResourceException(String message, Throwable cause) {
		super(message);
		initCause(cause);
	}

	/**
	 * @param cause
	 */
	public ResourceException(Throwable cause) {
		super();
		initCause(cause);
	}

	/**
	 * 
	 */
	public ResourceException() {
	}

	/**
	 * @param message
	 */
	public ResourceException(String message) {
		super(message);
	}
}
