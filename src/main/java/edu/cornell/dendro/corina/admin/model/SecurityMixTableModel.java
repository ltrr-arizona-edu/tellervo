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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * Simple table model for a list of securityGroups
 * 
 * @author peterbrewer
 * @author dan
 *
 */
public class SecurityMixTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private WSISecurityGroup parentGroup;
	private ArrayList<WSISecurityGroup> groupList;
	private ArrayList<WSISecurityUser> userList;	
	private ArrayList<Object> membersList = new ArrayList<Object>();
	private HashMap<WSISecurityGroup, ArrayList<WSISecurityGroup>> parentsMap;
	
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.member"),
            I18n.getText("admin.description"),
            I18n.getText("admin.parents"),
        };
	
	public SecurityMixTableModel(WSISecurityGroup argParentGroup){
		parentGroup = argParentGroup;
		groupList = new ArrayList<WSISecurityGroup>();
		userList = new ArrayList<WSISecurityUser>();
		if(parentGroup.isSetMembers()){
			if(parentGroup.getMembers().isSetSecurityGroups())
				groupList = (ArrayList<WSISecurityGroup>) parentGroup.getMembers().getSecurityGroups();	
			if(parentGroup.getMembers().isSetSecurityUsers())
				userList = (ArrayList<WSISecurityUser>) parentGroup.getMembers().getSecurityUsers();
		}
		buildParentsMap();
		membersList.addAll(groupList);
		membersList.addAll(userList);
	}
	
	
	private void buildParentsMap(){
		for(WSISecurityGroup group:groupList){
        	for(WSISecurityGroup checkParent:groupList){
    			if(checkParent.getMembers().getSecurityGroups().contains(group)){
    				if(!parentsMap.containsKey(group)){
    					parentsMap.put(group, new ArrayList<WSISecurityGroup>());
    				}
    				parentsMap.get(group).add(checkParent);
    			}
        	}   				    			
    	}
	}
  
	public int getColumnCount() {
		return columnNames.length;
	};
	
	public int getRowCount() {
		return membersList.size();
	};		
	
    public Class<?> getColumnClass(int c) {
    		return String.class;
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}	
			
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object member = membersList.get(rowIndex);
		
		if(member instanceof WSISecurityGroup)
			return getColumnValueForGroup((WSISecurityGroup) member, columnIndex);
		else if(member instanceof WSISecurityUser)
			return getColumnValueForUser((WSISecurityUser) member, columnIndex);
		else
			return null;
	}
		
	public Object getColumnValueForGroup(WSISecurityGroup grp, int columnIndex) {
		
		switch (columnIndex) {
		case 0: return grp.getId();
		case 1: return grp.getName();
		case 2: return grp.getDescription();
		case 3: return parentsMap.get(grp);
		default: return null;
		}
		
	}
	
public Object getColumnValueForUser(WSISecurityUser usr, int columnIndex) {
		
		switch (columnIndex) {
		case 0: return usr.getId();
		case 1: return usr.getFirstName()+" "+usr.getLastName();
		case 2: return "";
		case 3: 
			String parentStr = "";
			for(WSISecurityGroup g:usr.getMemberOf().getSecurityGroups()){
				parentStr += g.getName()+", ";
			}
			return parentStr.substring(0, parentStr.lastIndexOf(","));
		default: return null;
		}
		
	}


}
