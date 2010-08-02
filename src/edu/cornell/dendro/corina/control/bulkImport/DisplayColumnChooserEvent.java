/**
 * Created at Jul 24, 2010, 3:37:10 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.tracking.ITrackable;

/**
 * @author daniel
 *
 */
public class DisplayColumnChooserEvent extends MVCEvent implements ITrackable{
	
	public final HashModel model;
	public DisplayColumnChooserEvent(HashModel argModel) {
		super(BulkImportController.DISPLAY_COLUMN_CHOOSER);
		model = argModel;
	}
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Display Column Chooser";
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
