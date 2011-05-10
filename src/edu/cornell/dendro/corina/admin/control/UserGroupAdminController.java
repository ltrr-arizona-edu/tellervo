package edu.cornell.dendro.corina.admin.control;

//import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;
//import com.dmurph.tracking.AnalyticsConfigData;
//import com.dmurph.tracking.JGoogleAnalyticsTracker;
//import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

import edu.cornell.dendro.corina.admin.command.AuthenticateUserCommand;
import edu.cornell.dendro.corina.admin.command.EditUserCommand;
import edu.cornell.dendro.corina.admin.command.ToggleDisabledAccountsCommand;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;

public class UserGroupAdminController extends FrontController {
	
		public static final String DISPLAY_UGA = "UGA_DISPLAY_UGA";
		public static final String AUTHENTICATE_USER = "UGA_AUTHENICATE_USER";
		public static final String EDIT_USER = "UGA_EDIT_USER";
		public static final String TOGGLE_DISABLED_ACCOUNTS = "UGA_TOGGLE_DISABLED_ACCOUNTS";
        
        public UserGroupAdminController(){
                registerCommand(DISPLAY_UGA, "display");
                registerCommand(AUTHENTICATE_USER, AuthenticateUserCommand.class);
                registerCommand(EDIT_USER, EditUserCommand.class);
                registerCommand(TOGGLE_DISABLED_ACCOUNTS, ToggleDisabledAccountsCommand.class);
        }
        
    	public void display(MVCEvent argEvent){
    		
            UserGroupAdminView dialog = new UserGroupAdminView(new javax.swing.JFrame(), false);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {

                }
            });
            
    		UserGroupAdminModel.getInstance().setMainView(dialog);
            dialog.setVisible(true);
            
            //from bulk import display
            
//    		if(MVC.getTracker() == null){
//    			JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(new AnalyticsConfigData("UA-17109202-7"), GoogleAnalyticsVersion.V_4_7_2);
//    			MVC.setTracker(tracker);
//    		}else{
//    			MVC.getTracker().resetSession();
//    		}
    		//MVC.showEventMonitor();
    		
//    		if(UserGroupAdminModel.getInstance().getMainView() != null){
//    			UserGroupAdminView window = UserGroupAdminModel.getInstance().getMainView();
//    			window.setVisible(true);
//    			window.toFront();
//    			return;
//    		}
    		//MVC.showEventMonitor();
    	}
}