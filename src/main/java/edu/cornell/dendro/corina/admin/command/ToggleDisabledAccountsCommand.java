/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.admin.command;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.ToggleDisabledAccountsEvent;
import edu.cornell.dendro.corina.admin.model.SecurityUserTableModel;

public class ToggleDisabledAccountsCommand implements ICommand {

        @SuppressWarnings("unchecked")
		public void execute(MVCEvent argEvent) {
        	ToggleDisabledAccountsEvent event = (ToggleDisabledAccountsEvent) argEvent;
        	boolean show = event.show;
        	//JTable table = event.table;
        	SecurityUserTableModel usersModel = event.model.getUsersModel();
        	TableRowSorter<SecurityUserTableModel> usersSorter = event.model.getUsersSorter(usersModel, null); 
        	if(show){
        		usersSorter.setRowFilter(null);
        	}
        	else	
        	{
    	        RowFilter<SecurityUserTableModel, Object> rf = null;
    	        //If current expression doesn't parse, don't update.
    	        try {
    	            rf = RowFilter.regexFilter("t", 5);
    	        } catch (java.util.regex.PatternSyntaxException e) {
    	            return;
    	        }
    	        usersSorter.setRowFilter(rf);
        	}
        }
}
