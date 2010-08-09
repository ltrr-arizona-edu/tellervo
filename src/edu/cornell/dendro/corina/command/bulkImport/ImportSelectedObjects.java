/**
 * Created at Aug 8, 2010, 10:21:28 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import java.util.ArrayList;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectTableModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleObjectModel;

/**
 * @author daniel
 *
 */
public class ImportSelectedObjects implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		BulkImportModel model = BulkImportModel.getInstance();
		
		ObjectTableModel tmodel = model.getObjectModel().getTableModel();
		ArrayList<SingleObjectModel> selected = new ArrayList<SingleObjectModel>();
		tmodel.getSelected(selected);
		
		// verify they contain required info
		
	}
}
