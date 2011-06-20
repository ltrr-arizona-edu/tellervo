package edu.cornell.dendro.corina.admin.command;

import javax.swing.JDialog;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.EditGroupEvent;
import edu.cornell.dendro.corina.admin.model.SecurityGroupTableModel;
import edu.cornell.dendro.corina.admin.view.GroupUIView;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;

public class EditGroupCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        	EditGroupEvent event = (EditGroupEvent) argEvent;
        	int groupSelected = event.groupIndex;
        	SecurityGroupTableModel groupsModel = event.model.getGroupsModel();
        	JDialog view = event.model.getMainView();
        	
        	WSISecurityGroup selGroup = groupsModel.getGroupAt(groupSelected);
            GroupUIView userDialog = new GroupUIView(view, true, selGroup);
            userDialog.setVisible(true); 
        }
}