/**
 * Created at Aug 10, 2010, 12:38:33 AM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.RemoveSelectedEvent;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;

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
		
		int response;
		
		if(event.model.getTableModel().getSelectedCount()==0)
		{
			return;
		}
		else if (event.model.getTableModel().getSelectedCount()==1)
		{
			response = JOptionPane.showConfirmDialog(BulkImportModel.getInstance().getMainView(), "Are you sure you want to delete the selected row?");
		}
		else
		{
			response = JOptionPane.showConfirmDialog(BulkImportModel.getInstance().getMainView(), "Are you sure you want to delete the "
					+ event.model.getTableModel().getSelectedCount()
					+ " selected rows?");
		}
			
		if(response==JOptionPane.YES_OPTION)
		{
			// took the lazy path, had the model do it for us
			event.model.removeSelected();
		}
	}
	
}
