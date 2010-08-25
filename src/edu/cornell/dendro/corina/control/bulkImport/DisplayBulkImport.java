/**
 * Created at Aug 6, 2010, 10:26:09 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.tracking.ITrackable;

/**
 * @author daniel
 *
 */
public class DisplayBulkImport extends MVCEvent implements ITrackable {
	private static final long serialVersionUID = 1L;

	public DisplayBulkImport() {
		super(BulkImportController.DISPLAY_BULK_IMPORT);
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Show";
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
