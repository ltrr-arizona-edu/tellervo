package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;

public class CopyRowEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	
	private Integer selectedRowIndex;
	public final IBulkImportSectionModel model;
	
	public CopyRowEvent(IBulkImportSectionModel argModel, Integer selRowIndex) {
		super(BulkImportController.COPY_ROW);
		model = argModel;
		selectedRowIndex = selRowIndex;
	}
	
	public Integer getSelectedRowIndex()
	{
		return selectedRowIndex;
	}

}
