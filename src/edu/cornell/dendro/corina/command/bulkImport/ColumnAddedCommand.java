/**
 * Created at Jul 24, 2010, 3:03:59 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.ColumnsModifiedEvent;
import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;

/**
 * @author daniel
 *
 */
public class ColumnAddedCommand implements ICommand{

	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		ColumnsModifiedEvent event = (ColumnsModifiedEvent) argEvent;
		ColumnChooserModel model = event.model;
		model.add(event.getValue());
	}
	
}
