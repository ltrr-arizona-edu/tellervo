/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import org.jdom.Document;

/**
 * @author Lucas Madar
 *
 */
public class MalformedDocumentException extends ResourceException {
	public MalformedDocumentException(Document doc, String s) {
		super(s);
		
		this.malformedDoc = doc;
	}
	
	private Document malformedDoc;
	
	/**
	 * Gets the document that failed, or null if it doesn't exist
	 * @return
	 */
	public Document getMalformedDocument() {
		return malformedDoc;
	}
}
