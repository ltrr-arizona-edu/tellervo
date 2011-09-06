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

import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

@SuppressWarnings("serial")
/** a node in the tree - represents either a user or a group */

public class MyNode extends DefaultMutableTreeNode{
	
	public static enum Type {USER, GROUP};
	private Type type;
	
	public MyNode(TransferableUser user){
		super(user);
		type = Type.USER;
	}
	
	public MyNode(TransferableGroup group){
		super(group);
		type = Type.GROUP;
	}
	
	public Object getData(){
		return userObject;
	}
	
	public boolean getAllowsChildren(){
		return type.equals(Type.GROUP);
	}
	
	public Type getType(){
		return type;
	}
	
	@Override
	public String toString(){
		if(type.equals(Type.GROUP)){
			return ((TransferableGroup) userObject).getGroup().getName();
		}
		else if(type.equals(Type.USER)){
			WSISecurityUser u = ((TransferableUser) userObject).getUser();
			return u.getFirstName() +" " + u.getLastName();
		}
		else{
			return "Invalid MyNode type";
		}
	}
}
