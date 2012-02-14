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

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tellervo.desktop.admin.control.UpdateGroupEvent;
import org.tellervo.desktop.admin.control.UpdateUserEvent;
import org.tellervo.desktop.admin.model.UserGroupAdminModel;
import org.tellervo.desktop.schema.CorinaRequestType;
import org.tellervo.desktop.schema.WSISecurityGroup;
import org.tellervo.desktop.schema.WSISecurityUser;
import org.tellervo.desktop.wsi.corina.CorinaResourceAccessDialog;
import org.tellervo.desktop.wsi.corina.resources.SecurityUserEntityResource;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


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
			
			try {
			        MVC.splitOff(); // so other mvc events can execute
			} catch (IllegalThreadException e) {
			        // this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
			        e.printStackTrace();
			} catch (IncorrectThreadException e) {
			        // this means that this MVC thread is not the main thread, it was already splitOff() previously
			        e.printStackTrace();
			}

			for(WSISecurityGroup group: changedGroups){

				new UpdateGroupEvent(group, parent).dispatch();
			
			}
			
		}
}
