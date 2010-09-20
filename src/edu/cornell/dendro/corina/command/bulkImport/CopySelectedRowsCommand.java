/**
 * Created at Sep 20, 2010, 2:41:44 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import java.util.ArrayList;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.HashModel;

import edu.cornell.dendro.corina.control.bulkImport.CopySelectedRowsEvent;

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
		
		HashModel[] selected = event.model.getTableModel().getSelectedRows();
		ArrayList<Object> cloned = new ArrayList<Object>(selected.length);
		
		for(HashModel hm : selected){
			ICloneable newRow = event.model.createRowInstance();
			newRow.cloneFrom(hm);
			cloned.add(newRow);
		}
		
		event.model.getRows().addAll(cloned);
	}
}
