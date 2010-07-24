/**
 * Created at Jul 24, 2010, 2:40:15 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.StringEvent;

import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;

/**
 * @author daniel
 *
 */
public class ColumnsModifiedEvent extends StringEvent {
	private static final long serialVersionUID = 2L;
	
	public final ColumnChooserModel model;
	public ColumnsModifiedEvent(String argKey, String argValue, ColumnChooserModel argModel) {
		super(argKey, argValue);
		model = argModel;
	}
}
