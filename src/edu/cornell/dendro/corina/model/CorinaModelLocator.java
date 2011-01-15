package edu.cornell.dendro.corina.model;

import edu.cornell.dendro.corina.bulkImport.control.BulkImportController;
import edu.cornell.dendro.corina.bulkImport.control.ColumnChooserController;
import edu.cornell.dendro.corina.editor.control.EditorController;
import edu.cornell.dendro.corina.io.control.IOController;
import edu.cornell.dendro.corina.io.model.ImportModel;

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
	private IOController corinaImportController = new IOController();
	
	private final ImportModel corinaImportModel = new ImportModel();
	
	
	private CorinaModelLocator(){
		
	}
	
	public ImportModel getImportModel()
	{
		return corinaImportModel;
	}
	
	public static CorinaModelLocator getInstance(){
		return model;
	}
}
