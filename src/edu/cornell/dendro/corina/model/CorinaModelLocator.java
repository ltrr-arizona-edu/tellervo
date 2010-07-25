package edu.cornell.dendro.corina.model;

import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.control.bulkImport.ColumnChooserController;
import edu.cornell.dendro.corina.control.editor.EditorController;

/**
 *
 * @author daniel
 */
@SuppressWarnings("unused")
public class CorinaModelLocator {
	private static final CorinaModelLocator model = new CorinaModelLocator();
	
	private EditorController editorController = new EditorController();
	private ColumnChooserController columnController = new ColumnChooserController();
	private BulkImportController bulkImportController = new BulkImportController();
	
	private CorinaModelLocator(){
		
	}
	
	
	public static CorinaModelLocator getInstance(){
		return model;
	}
}
