/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.io.IOException;

/**
 * @author Lucas Madar
 *
 */
public class MalformedDocumentException extends IOException {
	public MalformedDocumentException() {
		super();
	}
	
	public MalformedDocumentException(String s) {
		super(s);
	}
}
