/**
 * Created at Jul 24, 2010, 3:37:10 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import javax.swing.JFrame;

import com.dmurph.mvc.IModel;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.HashModel;

import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;

/**
 * @author daniel
 *
 */
public class DisplayColumnChooserEvent extends MVCEvent{
	
	public final HashModel model;
	public DisplayColumnChooserEvent(HashModel argModel) {
		super(BulkImportController.DISPLAY_COLUMN_CHOOSER);
		model = argModel;
	}
}
