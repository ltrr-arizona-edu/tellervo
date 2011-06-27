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

import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.ToggleDisabledGroupsEvent;
import edu.cornell.dendro.corina.admin.model.SecurityGroupTableModel;

public class ToggleDisabledGroupsCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        	ToggleDisabledGroupsEvent event = (ToggleDisabledGroupsEvent) argEvent;
        	boolean show = event.show;
        	SecurityGroupTableModel groupsModel = event.model.getGroupsModel();
        	TableRowSorter<SecurityGroupTableModel> groupsSorter = event.model.getGroupsSorter(groupsModel); 
        	if(show){
        		groupsSorter.setRowFilter(null);
        	}
        	else	
        	{
    	        RowFilter<SecurityGroupTableModel, Object> rf = null;
    	        //If current expression doesn't parse, don't update.
    	        try {
    	            rf = RowFilter.regexFilter("t", 5);
    	        } catch (java.util.regex.PatternSyntaxException e) {
    	            return;
    	        }
    	        groupsSorter.setRowFilter(rf);
        	}
        }
}
