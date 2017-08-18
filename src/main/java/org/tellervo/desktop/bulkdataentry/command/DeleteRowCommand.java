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
package org.tellervo.desktop.bulkdataentry.command;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.DeleteRowEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.util.JTableRowHeader;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;


public class DeleteRowCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(DeleteRowCommand.class);

	@Override
	public void execute(MVCEvent argEvent) {
		
		try {
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
		        // this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
		        // this means that this MVC thread is not the main thread, it was already splitOff() previously
		        e.printStackTrace();
		}
		
		DeleteRowEvent event = (DeleteRowEvent) argEvent;
		
		MVCArrayList rowsToDelete = new MVCArrayList();
		
		boolean dirtyflag = false;
		
		for(int i : event.getValue())
		{
			IBulkImportSingleRowModel item =  (IBulkImportSingleRowModel) event.model.getRows().get(i);
			
			if(item instanceof SingleObjectModel)
			{
				if(((SingleObjectModel) item).isDirty())
				{
					dirtyflag=true;
				}
			}
			
			else if(item instanceof SingleElementModel)
			{
				if(((SingleElementModel) item).isDirty())
				{
					dirtyflag=true;
				}
			}
			
			else if(item instanceof SingleSampleModel)
			{
				if(((SingleSampleModel) item).isDirty())
				{
					dirtyflag=true;
				}
			}
			
			rowsToDelete.add(event.model.getRows().get(i));
		}
		
		if(dirtyflag)
		{
			
			int response = JOptionPane.showConfirmDialog(BulkImportModel.getInstance().getMainView(), "Any unsaved changes will be lost.  Are you sure you want to continue?");
			
			if(response==JOptionPane.YES_OPTION)
			{
				event.model.getRows().removeAll(rowsToDelete);

			}
		}
		
	}
}
