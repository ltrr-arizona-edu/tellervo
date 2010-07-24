/**
 * Created on Jul 24, 2010, 7:27:11 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;

/**
 * @author Daniel Murphy
 *
 */
public class HideColumnWindowCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		
		BulkImportModel model = BulkImportModel.getInstance();
		if(model.getCurrColumnChooser() == null){
			
		}
	}
	
}
