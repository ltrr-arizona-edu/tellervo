/**
 * Created on Jul 24, 2010, 7:27:11 PM
 */
package edu.cornell.dendro.corina.bulkImport.command;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;

/**
 * @author Daniel Murphy
 */
public class HideColumnWindowCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		
		BulkImportModel model = BulkImportModel.getInstance();
		if(model.getCurrColumnChooser() == null){
			return;
		}
		model.getCurrColumnChooser().setVisible(false);
		model.setCurrColumnChooser(null);
	}
	
}
