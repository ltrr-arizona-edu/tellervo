package edu.cornell.dendro.corina.util;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;

public class XMLParsingException extends IOException {
	
	private Document nonvalidatingDocument;
	private File invalidFile;

	public XMLParsingException(Throwable cause) {
		initCause(cause);
		nonvalidatingDocument = null;
		invalidFile = null;
	}

	public XMLParsingException(Throwable cause, Document doc) {
		initCause(cause);
		
		nonvalidatingDocument = doc;
		invalidFile = null;
	}
	
	public XMLParsingException(Throwable cause, File file) {
		initCause(cause);
		
		nonvalidatingDocument = null;
		invalidFile = file;
	}
	
	public Document getNonvalidatingDocument() {
		return nonvalidatingDocument;
	}
	
	public File getInvalidFile() {
		return invalidFile;
	}
}
