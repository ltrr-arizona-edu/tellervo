/**
 * Created at Sep 20, 2010, 2:41:44 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import java.util.ArrayList;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.CopySelectedRowsEvent;
import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSingleRowModel;

/**
 * @author Daniel
 *
 */
public class CopySelectedRowsCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		CopySelectedRowsEvent event = (CopySelectedRowsEvent) argEvent;
		
		ArrayList<IBulkImportSingleRowModel> selected = new ArrayList<IBulkImportSingleRowModel>();
		event.model.getTableModel().getSelected(selected);
		ArrayList<Object> cloned = new ArrayList<Object>(selected.size());
		
		for(IBulkImportSingleRowModel hm : selected){
			ICloneable newRow = event.model.createRowInstance();
			newRow.cloneFrom(hm);
			cloned.add(newRow);
		}
		
		event.model.getRows().addAll(cloned);
	}
}
