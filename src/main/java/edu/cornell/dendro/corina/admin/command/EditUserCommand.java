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
