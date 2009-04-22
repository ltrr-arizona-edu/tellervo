package edu.cornell.dendro.corina.formats;

import java.io.IOException;

/**
 * Something to throw when a loading error occurs that's not related directly to IO
 * (e.g., data is malformed)
 * 
 * @author lucasm
 *
 */

public class InvalidDataException extends IOException {

	public InvalidDataException() {
	}

	public InvalidDataException(String s) {
		super(s);
	}

	public InvalidDataException(Throwable arg0) {
		super(arg0);
	}

	public InvalidDataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
