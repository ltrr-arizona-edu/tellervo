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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

@SuppressWarnings("serial")
	// a node in the tree - represents either a user or a group
public class MyNode extends DefaultMutableTreeNode implements Transferable{
	
	//use types instead of flavors for simplicity
	public DataFlavor FLAVOR = null;
	private static DataFlavor flavors[] = {null};
	
	public static enum Type {USER, GROUP};
	private Type type;
	
	public MyNode(WSISecurityUser user){
		super(user);
		type = Type.USER;
	}
	
	public MyNode(WSISecurityGroup group){
		super(group);
		type = Type.GROUP;
	}
	
	public MyNode(Object data){
		super(data);
		if(data instanceof WSISecurityUser)
			type = Type.USER;
		else if(data instanceof WSISecurityGroup)
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
			return ((WSISecurityGroup) userObject).getName();
		}
		else if(type.equals(Type.USER)){
			WSISecurityUser u = ((WSISecurityUser) userObject);
			return u.getFirstName() +" " + u.getLastName();
		}
		return "Invalid MyNode type";
	}

	/** simplified version of getTranferData method which ignores flavor */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return this;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return true;
	}
}
