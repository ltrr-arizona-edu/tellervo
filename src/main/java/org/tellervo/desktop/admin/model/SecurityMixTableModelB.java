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
package org.tellervo.desktop.admin.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.schema.WSISecurityGroup;
import org.tellervo.desktop.schema.WSISecurityUser;
import org.tellervo.desktop.ui.I18n;


/**
 * 
 * @author peterbrewer
 * @author dan
 *
 */

public class SecurityMixTableModelB extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private WSISecurityGroup parentGroup;
	private ArrayList<WSISecurityGroup> groupMemList;
	private ArrayList<WSISecurityUser> userMemList;	
	private ArrayList<Object> membersList = new ArrayList<Object>();
	private HashMap<WSISecurityGroup, ArrayList<WSISecurityGroup>> parentsMap;
	private UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
	
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.user"),
            I18n.getText("admin.description"),
            I18n.getText("admin.parents"),
        };
	
	public SecurityMixTableModelB(WSISecurityGroup argParentGroup){
		parentGroup = argParentGroup;
		groupMemList = new ArrayList<WSISecurityGroup>();
		userMemList = new ArrayList<WSISecurityUser>();
		parentsMap = new HashMap<WSISecurityGroup, ArrayList<WSISecurityGroup>>();
		for(String groupId: parentGroup.getGroupMembers()){
			groupMemList.add(mainModel.getGroupById(groupId));
		}
		for(String userId: parentGroup.getUserMembers()){
			userMemList.add(mainModel.getUserById(userId));
		}
		buildParentsMap();
		membersList.addAll(groupMemList);
		membersList.addAll(userMemList);
	}
	
	
	private void buildParentsMap(){
		for(WSISecurityGroup group:mainModel.getGroupList()){
        	for(String childId: group.getGroupMembers()){
        		WSISecurityGroup child = mainModel.getGroupById(childId);
        		if(!parentsMap.containsKey(child)){
        			parentsMap.put(child, new ArrayList<WSISecurityGroup>());
        		}
        		
        		ArrayList<WSISecurityGroup> parentsList = parentsMap.get(child);
        		if(!parentsList.contains(group)){
        			parentsList.add(group);
    				parentsMap.put(child, parentsList);
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
		case 3: 
			String parentStr = "";
			ArrayList<WSISecurityGroup> parentsList = parentsMap.get(grp);
			if(parentsList!=null){
				for(WSISecurityGroup parent: parentsList){
					parentStr += mainModel.getGroupById(parent.getId()).getName()+", ";
				}
				return parentStr.substring(0, parentStr.lastIndexOf(","));
			}
			return ""; 
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
			for(String groupId: usr.getMemberOves()){
				parentStr += mainModel.getGroupById(groupId).getName()+", ";
			}
			return parentStr.equals("") ? "" : parentStr.substring(0, parentStr.lastIndexOf(","));
		default: return null;
		}
		
	}


}
