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
package edu.cornell.dendro.corina.admin.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * Simple table model for a list of securityUsers
 * 
 * @author peterbrewer
 *
 */
public class SecurityUserTableModelA extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
	private ArrayList<WSISecurityUser> userList;
	private ArrayList<WSISecurityUser> completeUserList;
	private Boolean hideDisabled = true;    	
	
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.user"),
            I18n.getText("admin.firstName"),
            I18n.getText("admin.lastName"),
            I18n.getText("admin.groups"),
            I18n.getText("general.enabled"),
        };
	
	public SecurityUserTableModelA(){
		userList = mainModel.getUserList();
	}

	public int getColumnCount() {
		return columnNames.length;
	};
	
	public int getRowCount() {
		if(userList==null)
		{
			return 0;
		}
		return userList.size();
	};		
	
    public Class<?> getColumnClass(int c) {
    	if (c==5){
    		return Boolean.class;
    	} else {
    		return String.class;
    	}
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public ArrayList<WSISecurityUser> getUsers() {
		return userList;
	}
	
	public WSISecurityUser getUserAt(int rowIndex) {
		return userList.get(rowIndex);						
	}		
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSISecurityUser usr = getUserAt(rowIndex);

		switch (columnIndex) {
			case 0: return usr.getId();
			case 1: return usr.getUsername();
			case 2: return usr.getFirstName();
			case 3: return usr.getLastName();
			case 4: 
				String str = "";
				for(String groupId:usr.getMemberOves()){
					str+=mainModel.getGroupById(groupId).getName() + ", ";
				}
				if(str.equals("")){
					return str;
				}else{
					return str.substring(0,str.lastIndexOf(","));
				}
			case 5: return usr.isIsActive();
			default: return null;
		}
	}

}
