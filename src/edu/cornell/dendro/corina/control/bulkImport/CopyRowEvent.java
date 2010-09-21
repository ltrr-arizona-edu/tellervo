package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;

public class CopyRowEvent extends ObjectEvent<Integer> implements ITrackable {

	private static final long serialVersionUID = 1L;
	
	public final IBulkImportSectionModel model;
	
	public CopyRowEvent(IBulkImportSectionModel argModel, Integer selRowIndex) {
		super(BulkImportController.COPY_ROW, selRowIndex);
		model = argModel;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Copy Row";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
	 */
	@Override
	public String getTrackingCategory() {
		return "Bulk Import";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
	 */
	@Override
	public String getTrackingLabel() {
		return null;
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		return null;
	}

}
