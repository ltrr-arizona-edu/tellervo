/**
 * Created at Sep 20, 2010, 2:42:55 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;

/**
 * @author Daniel
 *
 */
public class CopySelectedRowsEvent extends MVCEvent implements ITrackable {
	
	public final IBulkImportSectionModel model;
	/**
	 * @param argKey
	 */
	public CopySelectedRowsEvent(IBulkImportSectionModel argModel) {
		super(BulkImportController.COPY_SELECTED_ROWS);
		model = argModel;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
	 */
	@Override
	public String getTrackingCategory() {
		return "Bulk Import";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Copy All Selected Rows";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
	 */
	@Override
	public String getTrackingLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
