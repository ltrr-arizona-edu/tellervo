package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;

public class DeleteRowEvent extends ObjectEvent<Integer> implements ITrackable {
	private static final long serialVersionUID = 1L;

	public final IBulkImportSectionModel model;
	
	public DeleteRowEvent(IBulkImportSectionModel argModel, Integer rowIndex) {
		super(BulkImportController.DELETE_ROW, rowIndex);
		model = argModel;
	}

	@Override
	public String getTrackingAction() {
		return "Delete Row";
	}

	@Override
	public String getTrackingCategory() {
		return "Bulk Import";
	}

	@Override
	public String getTrackingLabel() {
		return null;
	}

	@Override
	public Integer getTrackingValue() {
		return null;
	}

}
