package edu.cornell.dendro.corina.model;

import edu.cornell.dendro.corina.control.editor.EditorController;

/**
 *
 * @author daniel
 */
public class CorinaModelLocator {
	private static final CorinaModelLocator model = new CorinaModelLocator();
	
	private EditorController editorController = new EditorController();
	
	private CorinaModelLocator(){
		
	}
	
	
	public static CorinaModelLocator getInstance(){
		return model;
	}
}
