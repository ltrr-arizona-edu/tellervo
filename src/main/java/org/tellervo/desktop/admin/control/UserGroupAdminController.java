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
package org.tellervo.desktop.admin.control;

//import com.dmurph.mvc.MVC;
import org.tellervo.desktop.admin.command.AuthenticateUserCommand;
import org.tellervo.desktop.admin.command.CreateNewGroupCommand;
import org.tellervo.desktop.admin.command.CreateNewUserCommand;
import org.tellervo.desktop.admin.command.DeleteGroupCommand;
import org.tellervo.desktop.admin.command.DeleteUserCommand;
import org.tellervo.desktop.admin.command.EditGroupCommand;
import org.tellervo.desktop.admin.command.EditUserCommand;
import org.tellervo.desktop.admin.command.ToggleDisabledUsersCommand;
import org.tellervo.desktop.admin.command.UpdateGroupCommand;
import org.tellervo.desktop.admin.command.UpdateUserCommand;
import org.tellervo.desktop.admin.model.UserGroupAdminModel;
import org.tellervo.desktop.admin.view.UserGroupAdminView;
import org.tellervo.desktop.core.App;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;
//import com.dmurph.tracking.AnalyticsConfigData;
//import com.dmurph.tracking.JGoogleAnalyticsTracker;
//import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;


public class UserGroupAdminController extends FrontController {
	
		public static final String DISPLAY_UGA = "UGA_DISPLAY_UGA";
		public static final String AUTHENTICATE_USER = "UGA_AUTHENICATE_USER";
		public static final String EDIT_USER = "UGA_EDIT_USER";
		public static final String EDIT_GROUP = "UGA_EDIT_GROUP";
		public static final String TOGGLE_DISABLED_USERS = "UGA_TOGGLE_DISABLED_ACCOUNTS";
		public static final String DELETE_USER = "UGA_DELETE_USER";
		public static final String DELETE_GROUP = "UGA_DELETE_GROUP";
		public static final String OK_FINISH = "UGA_OK_FINISH";
		public static final String CREATE_NEW_USER = "UGA_CREATE_NEW_USER";
		public static final String UPDATE_USER = "UGA_UPDATE_USER";
		public static final String CREATE_NEW_GROUP = "UGA_CREATE_GROUP";
		public static final String UPDATE_GROUP = "UGA_UPDATE_GROUP";
        
        public UserGroupAdminController(){
                registerCommand(DISPLAY_UGA, "display");
                registerCommand(AUTHENTICATE_USER, AuthenticateUserCommand.class);
                registerCommand(EDIT_USER, EditUserCommand.class);
                registerCommand(EDIT_GROUP, EditGroupCommand.class);
                registerCommand(TOGGLE_DISABLED_USERS, ToggleDisabledUsersCommand.class);
                registerCommand(DELETE_USER, DeleteUserCommand.class);
                registerCommand(DELETE_GROUP, DeleteGroupCommand.class);
                registerCommand(OK_FINISH, "finish");
                registerCommand(CREATE_NEW_USER, CreateNewUserCommand.class);
                registerCommand(UPDATE_USER, UpdateUserCommand.class);
                registerCommand(UPDATE_GROUP, UpdateGroupCommand.class);
                registerCommand(CREATE_NEW_GROUP, CreateNewGroupCommand.class);
        }
        
    	public void display(MVCEvent argEvent){
    		
            UserGroupAdminView dialog = new UserGroupAdminView(new javax.swing.JFrame(), false);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {

                }
            });
            
    		UserGroupAdminModel.getInstance().setMainView(dialog);
            dialog.setVisible(true);
    	}
    	
    	public void finish(MVCEvent argEvent){
    		UserGroupAdminView view = ((OkFinishEvent) argEvent).model.getMainView();
			App.dictionary.query();
			App.dictionary.debugDumpListeners();
			view.dispose();
    	}
}
