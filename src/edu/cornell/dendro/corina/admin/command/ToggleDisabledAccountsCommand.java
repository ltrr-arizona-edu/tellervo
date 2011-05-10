package edu.cornell.dendro.corina.admin.command;

import javax.swing.JDialog;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.ToggleDisabledAccountsEvent;
import edu.cornell.dendro.corina.admin.model.SecurityUserTableModel;

public class ToggleDisabledAccountsCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        	ToggleDisabledAccountsEvent event = (ToggleDisabledAccountsEvent) argEvent;
        	boolean show = event.show;
        	SecurityUserTableModel usersModel = event.model.getUsersModel();
        	TableRowSorter<SecurityUserTableModel> usersSorter = event.model.getUsersSorter(usersModel); 
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