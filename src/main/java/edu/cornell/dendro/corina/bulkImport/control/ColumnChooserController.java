/**
 * Created at Jul 24, 2010, 4:29:32 AM
 */
package edu.cornell.dendro.corina.bulkImport.control;

import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.bulkImport.command.ColumnAddedCommand;
import edu.cornell.dendro.corina.bulkImport.command.ColumnRemovedCommand;

/**
 * @author daniel
 *
 */
public class ColumnChooserController extends FrontController {
	public static final String COLUMN_ADDED = "COLUMN_CHOOSER_COLUMN_ADDED";
	public static final String COLUMN_REMOVED = "COLUMN_CHOOSER_COLUMN_REMOVED";
	
	public ColumnChooserController(){
		registerCommand(COLUMN_ADDED, ColumnAddedCommand.class);
		registerCommand(COLUMN_REMOVED, ColumnRemovedCommand.class);
	}
}
