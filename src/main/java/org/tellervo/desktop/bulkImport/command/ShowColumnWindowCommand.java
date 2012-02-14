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
package org.tellervo.desktop.bulkImport.command;

import org.tellervo.desktop.bulkImport.control.DisplayColumnChooserEvent;
import org.tellervo.desktop.bulkImport.model.BulkImportModel;
import org.tellervo.desktop.bulkImport.model.ColumnChooserModel;
import org.tellervo.desktop.bulkImport.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkImport.view.ColumnChooserView;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


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
		DisplayColumnChooserEvent event = (DisplayColumnChooserEvent) argEvent;
		
		BulkImportModel biModel = BulkImportModel.getInstance();
		
		if(biModel.getCurrColumnChooser() != null){
			
			if(biModel.getCurrColumnChooser().isVisible())
			{
				biModel.getCurrColumnChooser().setVisible(false);
				return;
			}
			else
			{
				biModel.setCurrColumnChooser(null);
			}
		}
	
		// give it the possible columns
		ColumnChooserModel model = event.model.getColumnModel();
		model.poplutePossibleColumns(event.model.getModelTableProperties());
		
		
		// remove any columns in the model that aren't possible columns
		for(String s : model){
			if(!model.getPossibleColumns().contains(s)){
				if(!s.equals(IBulkImportSingleRowModel.IMPORTED)){
					model.remove(s);	
				}
			}
		}
		
		ColumnChooserView view = new ColumnChooserView(model, biModel.getMainView(), event.locationComponent);
		biModel.setCurrColumnChooser(view);
		
		try {
			MVC.splitOff();
		}catch (IllegalThreadException e) { e.printStackTrace();}
		catch (IncorrectThreadException e) { e.printStackTrace();}
		
		view.pack();
		view.setVisible(true);
	}
	
}
