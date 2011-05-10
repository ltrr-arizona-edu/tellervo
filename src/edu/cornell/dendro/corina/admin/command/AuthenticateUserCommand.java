package edu.cornell.dendro.corina.admin.command;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.AuthenticateEvent;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;

public class AuthenticateUserCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        		AuthenticateEvent event = (AuthenticateEvent) argEvent;
                LoginDialog dlg = event.model.getLoginDialog();
                
             // Make sure the user has admin credentials
            	try {
            		dlg.setGuiForConfirmation();
            		dlg.setUsername(App.currentUser.getUsername());
            		dlg.doLogin(null, false);            		
               	} catch (UserCancelledException uce) {
            		return;
            	}
        }
}