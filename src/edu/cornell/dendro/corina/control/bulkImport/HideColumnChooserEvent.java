/**
 * Created on Jul 24, 2010, 4:19:37 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.tracking.ITrackable;

/**
 * @author Daniel Murphy
 *
 */
public class HideColumnChooserEvent extends MVCEvent implements ITrackable{
	private static final long serialVersionUID = 1L;

	public HideColumnChooserEvent(){
		super(BulkImportController.HIDE_COLUMN_CHOOSER);
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Hide Column Chooser";
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
