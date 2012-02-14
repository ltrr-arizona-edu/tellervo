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
 *     Dan Girshovich
 ******************************************************************************/
package edu.cornell.dendro.corina.admin.command;

import java.util.ArrayList;

import javax.swing.JDialog;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import edu.cornell.dendro.corina.admin.control.EditUserEvent;
import edu.cornell.dendro.corina.admin.model.SecurityUserTableModelA;
import edu.cornell.dendro.corina.admin.view.UserUIView;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class EditUserCommand implements ICommand {

        @SuppressWarnings("unchecked")
		public void execute(MVCEvent argEvent) {
        	EditUserEvent event = (EditUserEvent) argEvent;
        	int userSelected = event.userIndex;
        	SecurityUserTableModelA usersModel = event.model.getUsersModelA();
        	JDialog view = event.model.getMainView();
        	WSISecurityUser seluser = usersModel.getUserAt(userSelected);
        	//TODO: change userDialog back to modal (view, true, selUser). Right now if it is
        	// the events stemming from the dialog are blocked.
        	// Look into MVC.splitOff()
            UserUIView userDialog = new UserUIView(view, false, seluser);
            userDialog.setVisible(true); 
        }
}
