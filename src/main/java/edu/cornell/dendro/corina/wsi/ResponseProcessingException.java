package edu.cornell.dendro.corina.wsi;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;

public class ResponseProcessingException extends IOException {
	private static final long serialVersionUID = 1L;
	
	private Document nonvalidatingDocument;
	private File invalidFile;

	public ResponseProcessingException(Throwable cause) {
		initCause(cause);
		nonvalidatingDocument = null;
		invalidFile = null;
	}

	public ResponseProcessingException(Throwable cause, Document doc) {
		initCause(cause);
		
		nonvalidatingDocument = doc;
		invalidFile = null;
	}
	
	public ResponseProcessingException(Throwable cause, File file) {
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
