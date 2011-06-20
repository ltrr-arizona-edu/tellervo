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
public class AddRowEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;

	public final IBulkImportSectionModel model;
	
	public AddRowEvent(IBulkImportSectionModel argModel){
		super(BulkImportController.ADD_ROW);
		model = argModel;
	}
}
