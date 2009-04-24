package edu.cornell.dendro.corina.wsi;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;

public class UnmarshallingException extends IOException {
	
	private Document nonvalidatingDocument;
	private File invalidFile;

	public UnmarshallingException(Throwable cause) {
		initCause(cause);
		nonvalidatingDocument = null;
		invalidFile = null;
	}

	public UnmarshallingException(Throwable cause, Document doc) {
		initCause(cause);
		
		nonvalidatingDocument = doc;
		invalidFile = null;
	}
	
	public UnmarshallingException(Throwable cause, File file) {
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
