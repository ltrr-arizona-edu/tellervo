/**
 * Created at Jul 24, 2010, 3:38:04 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.view.bulkImport.ColumnChooserView;

/**
 * @author daniel
 *
 */
public class BulkImportController extends FrontController {
	public static final String DISPLAY_COLUMN_CHOOSER = "BULK_IMPORT_DISPLAY_COLUMN_CHOOSER";
	public static final String HIDE_COLUMN_CHOOSER = "BULK_IMPORT_HIDE_COLUMN_CHOOSER";

	
	public BulkImportController(){
		registerCommand(DISPLAY_COLUMN_CHOOSER, "showColumnWindow");
	}
}
