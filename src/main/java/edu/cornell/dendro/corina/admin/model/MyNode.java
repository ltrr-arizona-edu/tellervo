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

package edu.cornell.dendro.corina.admin.model;
import javax.swing.tree.DefaultMutableTreeNode;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

@SuppressWarnings("serial")
	// a node in the tree - represents either a user or a group
public class MyNode extends DefaultMutableTreeNode{
	
	private UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();

	public MyNode(Object userObject){
		super(userObject);
	}
	
	//retrieves the actual user or group object from the id.
	public Object getFullEntity(){
		if(userObject instanceof UserNode){
			return mainModel.getUserById(((UserNode)userObject).getId());
		}
		else if(userObject instanceof GroupNode){
			return mainModel.getGroupById(((GroupNode)userObject).getId());
		}
		//this should never happen
		else{
			return null;
		}
		
	}
	
	public boolean getAllowsChildren(){
		return userObject instanceof GroupNode;
	}
	
	@Override
	public String toString(){
		if(this.userObject instanceof GroupNode){
			String gId = ((GroupNode) userObject).getId();
			return mainModel.getGroupById(gId).getName();
		}
		if(this.userObject instanceof UserNode){
			String uId = ((UserNode) userObject).getId();
			WSISecurityUser u = mainModel.getUserById(uId);
			return u.getFirstName() +" " + u.getLastName();
		}
		return "Invalid MyNode type";
	}

}
