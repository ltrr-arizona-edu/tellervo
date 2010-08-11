/**
 * Created at Aug 10, 2010, 12:38:33 AM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.RemoveSelectedEvent;

/**
 * @author daniel
 *
 */
public class RemoveSelectedCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		RemoveSelectedEvent event = (RemoveSelectedEvent) argEvent;
		// took the lazy path, had the model do it for us
		event.model.removeSelected();
	}
	
}
