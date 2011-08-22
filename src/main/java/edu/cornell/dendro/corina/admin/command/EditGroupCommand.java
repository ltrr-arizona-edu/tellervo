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

import java.util.ArrayList;

import javax.swing.JDialog;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.EditGroupEvent;
import edu.cornell.dendro.corina.admin.model.SecurityGroupTableModelA;
import edu.cornell.dendro.corina.admin.view.GroupUIView;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;

public class EditGroupCommand implements ICommand {

        @SuppressWarnings("unchecked")
		public void execute(MVCEvent argEvent) {
        	EditGroupEvent event = (EditGroupEvent) argEvent;
        	int groupSelected = event.groupIndex;
        	SecurityGroupTableModelA groupsModel = new SecurityGroupTableModelA();
        	JDialog view = event.model.getMainView();
        	WSISecurityGroup selGroup = groupsModel.getGroupAt(groupSelected);
            GroupUIView userDialog = new GroupUIView(view, true, selGroup);
            userDialog.setVisible(true); 
        }
}
