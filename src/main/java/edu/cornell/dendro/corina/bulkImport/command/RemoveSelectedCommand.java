/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.bulkImport.command;

import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.bulkImport.control.RemoveSelectedEvent;
import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;

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
