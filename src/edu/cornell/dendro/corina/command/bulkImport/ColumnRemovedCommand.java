/**
 * Created at Jul 24, 2010, 3:09:07 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.ColumnsModifiedEvent;

/**
 * @author daniel
 *
 */
public class ColumnRemovedCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		ColumnsModifiedEvent event = (ColumnsModifiedEvent) argEvent;
		event.model.remove(event.getValue());
	}
}
