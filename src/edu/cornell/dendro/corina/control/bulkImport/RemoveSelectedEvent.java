/**
 * Created at Aug 10, 2010, 12:45:10 AM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;

/**
 * @author daniel
 *
 */
public class RemoveSelectedEvent extends MVCEvent implements ITrackable {
	private static final long serialVersionUID = 1L;

	public final IBulkImportSectionModel model;
	
	public RemoveSelectedEvent(IBulkImportSectionModel argModel) {
		super(BulkImportController.REMOVE_SELECTED);
		model = argModel;
	}
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Remove Selected";
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
