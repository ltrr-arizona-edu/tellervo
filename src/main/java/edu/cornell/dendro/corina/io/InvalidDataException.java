package edu.cornell.dendro.corina.io;

import java.io.IOException;

/**
 * Something to throw when a loading error occurs that's not related directly to IO
 * (e.g., data is malformed)
 * 
 * @author lucasm
 *
 */

@SuppressWarnings("serial")
public class InvalidDataException extends IOException {

	public InvalidDataException() {
	}

	public InvalidDataException(String s) {
		super(s);
	}

	public InvalidDataException(Throwable cause) {
		super();
		initCause(cause);
	}

	public InvalidDataException(String s, Throwable cause) {
		super(s);
		initCause(cause);
	}
}
