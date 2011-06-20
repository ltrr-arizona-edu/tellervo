package edu.cornell.dendro.corina.admin.command;

import javax.swing.JDialog;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import edu.cornell.dendro.corina.admin.control.EditUserEvent;
import edu.cornell.dendro.corina.admin.model.SecurityUserTableModel;
import edu.cornell.dendro.corina.admin.view.UserUIView;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class EditUserCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        	EditUserEvent event = (EditUserEvent) argEvent;
        	int userSelected = event.userIndex;
        	SecurityUserTableModel usersModel = event.model.getUsersModel();
        	JDialog view = event.model.getMainView();
        	
        	WSISecurityUser seluser = usersModel.getUserAt(userSelected);
            UserUIView userDialog = new UserUIView(view, true, seluser);
            userDialog.setVisible(true); 
        }
}