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

package edu.cornell.dendro.corina.admin.command;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import edu.cornell.dendro.corina.admin.control.UpdateUserEvent;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityGroupEntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

public class UpdateUserCommand implements ICommand {

		//Updates the user locally and on the server and does the same for any groups whose "userMembers" property changed.	
	
		UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
		UpdateUserEvent event;
		WSISecurityUser user;
		ArrayList<WSISecurityGroup> oldMemList;
		ArrayList<WSISecurityGroup> newMemList;
		JDialog parent;
		
        public void execute(MVCEvent argEvent) {

        	event = (UpdateUserEvent) argEvent;
        	user = event.user;
        	oldMemList = event.oldMembershipList;
        	newMemList = event.newMembershipList;
        	parent = event.parent;
        	
        	updateUser();
        	updateGroups();			
        }

		private void updateGroups() {
  	
			ArrayList<WSISecurityGroup> changedGroups = new ArrayList<WSISecurityGroup>();

			for(WSISecurityGroup group: mainModel.getGroupList()){
				if(newMemList.contains(group) && !oldMemList.contains(group)){
					group.getUserMembers().add(user.getId());
					changedGroups.add(group);
				}
				else if(!newMemList.contains(group) && oldMemList.contains(group)){
					group.getUserMembers().remove(user.getId());
					changedGroups.add(group);
				}
			}	
			
			for(WSISecurityGroup group: changedGroups){
				// associate a resource
		    	SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(CorinaRequestType.UPDATE, group);
		    	
				CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(parent, rsrc);
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

		private void updateUser() {
			// associate a resource
	    	SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, user);
	    	
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(parent, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				//this sets the id assigned by the server.
				//must happen before groups are updated!
				mainModel.updateUser(rsrc.getAssociatedResult());
				
				parent.dispose();
			}
			else{
				JOptionPane.showMessageDialog(parent, "Error updating user: " + accdialog.getFailException().
						getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
}
