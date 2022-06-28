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

import java.util.ArrayList;

import org.tellervo.desktop.bulkdataentry.control.CopySelectedRowsEvent;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


/**
 * @author Daniel
 *
 */
public class CopySelectedRowsCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@SuppressWarnings("unchecked")
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
		
		CopySelectedRowsEvent event = (CopySelectedRowsEvent) argEvent;
		
		ArrayList<IBulkImportSingleRowModel> selected = new ArrayList<IBulkImportSingleRowModel>();
		event.model.getTableModel().getSelected(selected);
		ArrayList<Object> cloned = new ArrayList<Object>(selected.size());
		
		for(IBulkImportSingleRowModel hm : selected){
			IBulkImportSingleRowModel newRow = event.model.createRowInstance();
			newRow.cloneFrom(hm);
			
			AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
			//otm.setSelected(newRow, false);
			
			cloned.add(newRow);
		}
		
		event.model.getRows().addAll(cloned);
	}
}
