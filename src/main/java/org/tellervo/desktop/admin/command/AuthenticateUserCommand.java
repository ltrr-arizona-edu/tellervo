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
package org.tellervo.desktop.admin.command;

import org.tellervo.desktop.admin.control.DisplayUGAEvent;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.LoginDialog;
import org.tellervo.desktop.gui.UserCancelledException;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class AuthenticateUserCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
                LoginDialog dlg = new LoginDialog();
                
             // Make sure the user has admin credentials
            	try {
            		dlg.setGuiForConfirmation();
            		dlg.setUsername(App.currentUser.getUsername());
            		dlg.doLogin(null, false);      
            		new DisplayUGAEvent().dispatch();
               	} catch (UserCancelledException uce) {
            		return;
            	}
        }
}
