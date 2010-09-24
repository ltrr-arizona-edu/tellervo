/**
 * Created at Aug 1, 2010, 3:10:04 AM
 */
package edu.cornell.dendro.corina.bulkImport.command;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.bulkImport.control.AddRowEvent;

/**
 * @author daniel
 *
 */
@SuppressWarnings("unchecked")
public class AddRowCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		AddRowEvent event = (AddRowEvent) argEvent;
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		rows.add(event.model.createRowInstance());
	}
	
}
