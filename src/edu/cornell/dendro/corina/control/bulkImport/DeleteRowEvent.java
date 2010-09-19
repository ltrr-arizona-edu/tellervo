package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;

public class DeleteRowEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;

	public final IBulkImportSectionModel model;
	private Integer selectedRowIndex;
	
	public DeleteRowEvent(IBulkImportSectionModel argModel, Integer rowIndex) {
		super(BulkImportController.DELETE_ROW);
		model = argModel;
		selectedRowIndex = rowIndex;
	}

	public Integer getSelectedRowIndex()
	{
		return selectedRowIndex;
	}
	
}
