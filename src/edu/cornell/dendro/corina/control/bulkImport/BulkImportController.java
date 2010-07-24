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
	public static final String SHOW_HIDE_COLUMNS_PRESSED = "BULK_IMPORT_SHOW_HIDE_COLUMNS_PRESSED";
	
	
	public BulkImportController(){
		registerCommand(SHOW_HIDE_COLUMNS_PRESSED, "showColumnWindow");
	}
}
