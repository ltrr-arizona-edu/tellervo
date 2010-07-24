/**
 * Created at Jul 24, 2010, 3:48:12 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.ShowHideColumnsPressed;

/**
 * @author daniel
 *
 */
public class ShowColumnWindowCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		ShowHideColumnsPressed event = (ShowHideColumnsPressed) argEvent;
		
		
	}
	
}
