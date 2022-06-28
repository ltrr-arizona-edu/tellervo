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
 *     Dan Girshovich
 ******************************************************************************/

package org.tellervo.desktop.admin.command;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tellervo.desktop.admin.control.UpdateGroupEvent;
import org.tellervo.desktop.admin.model.UserGroupAdminModel;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.SecurityGroupEntityResource;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class UpdateGroupCommand implements ICommand {

		//Updates the group locally and on the server.	
	
		UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
		UpdateGroupEvent event;
		WSISecurityGroup group;
		JDialog parent;
		
        public void execute(MVCEvent argEvent) {

        	event = (UpdateGroupEvent) argEvent;
        	group = event.group;
        	parent = event.parent;
        	
			// associate a resource
	    	SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(TellervoRequestType.UPDATE, group);
	    	
			TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(parent, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				mainModel.updateGroup(rsrc.getAssociatedResult());
				parent.dispose();
			}
			else{
				JOptionPane.showMessageDialog(parent, "Error updating group: " + accdialog.getFailException().
						getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
}
