/**
 * Created at Jul 24, 2010, 2:40:15 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.StringEvent;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;

/**
 * @author daniel
 *
 */
public class ColumnsModifiedEvent extends StringEvent implements ITrackable{
	private static final long serialVersionUID = 2L;
	
	public final ColumnChooserModel model;
	
	public ColumnsModifiedEvent(String argKey, String argValue, ColumnChooserModel argModel) {
		super(argKey, argValue);
		model = argModel;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		if(key.equals(ColumnChooserController.COLUMN_ADDED)){
			return "Column Added";
		}else if(key.equals(ColumnChooserController.COLUMN_REMOVED)){
			return "Column Removed";
		}
		return "Unknown Key";
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
		return getValue();
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		return null;
	}
}
