package edu.cornell.dendro.corina.admin.command;

import javax.swing.JDialog;
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