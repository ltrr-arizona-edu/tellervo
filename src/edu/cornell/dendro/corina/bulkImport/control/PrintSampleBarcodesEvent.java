/**
 * Created at Aug 1, 2010, 3:10:27 AM
 */
package edu.cornell.dendro.corina.bulkImport.control;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.bulkImport.model.IBulkImportSectionModel;

/**
 * @author daniel
 *
 */
public class PrintSampleBarcodesEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;

	public final IBulkImportSectionModel model;
	
	public PrintSampleBarcodesEvent(IBulkImportSectionModel argModel){
		super(BulkImportController.PRINT_SAMPLE_BARCODES);
		model = argModel;
	}
}
