/**
 * Created on Jul 24, 2010, 4:19:37 PM
 */
package edu.cornell.dendro.corina.control.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.ObjectEvent;

/**
 * @author Daniel Murphy
 *
 */
public class HideColumnChooserEvent extends MVCEvent {
	
	public HideColumnChooserEvent(){
		super(BulkImportController.HIDE_COLUMN_CHOOSER);
	}
}
