/**
 * Created at Jul 24, 2010, 3:37:10 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import javax.swing.JFrame;

import com.dmurph.mvc.IModel;
import com.dmurph.mvc.MVCEvent;

/**
 * @author daniel
 *
 */
public class ShowHideColumnsPressed extends MVCEvent{
	
	public final IModel model;
	public final JFrame frame;
	public ShowHideColumnsPressed(IModel argModel, JFrame argFrame) {
		super(BulkImportController.SHOW_HIDE_COLUMNS_PRESSED);
		model = argModel;
		frame = argFrame;
	}
}
