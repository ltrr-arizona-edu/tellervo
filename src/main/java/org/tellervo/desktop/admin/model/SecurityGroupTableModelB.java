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
package org.tellervo.desktop.admin.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.desktop.ui.I18n;


/**
 * Simple table model for a list of securityGroups
 * 
 * @author peterbrewer
 *
 */
public class SecurityGroupTableModelB extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
	private ArrayList<WSISecurityGroup> groupList;
	private ArrayList<WSISecurityGroup> oldMemberOfList;
	private ArrayList<WSISecurityGroup> newMemberOfList;	
	private HashMap<WSISecurityGroup, ArrayList<WSISecurityGroup>> parentsMap;
	private WSISecurityUser targetUser;
	
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.group"),
            I18n.getText("admin.description"),
            I18n.getText("admin.parents"),
            I18n.getText("admin.ismember"), 
        };
	
	@SuppressWarnings("unchecked")
	public SecurityGroupTableModelB(WSISecurityUser trgetUser){
		groupList = mainModel.getGroupList();
		targetUser = trgetUser;
		oldMemberOfList = new ArrayList<WSISecurityGroup>(); 
		newMemberOfList = new ArrayList<WSISecurityGroup>();
		parentsMap = new HashMap<WSISecurityGroup, ArrayList<WSISecurityGroup>>();
		if(targetUser != null){
			for(String groupId: targetUser.getMemberOves()){
				newMemberOfList.add(mainModel.getGroupById(groupId));
			}
			oldMemberOfList = (ArrayList<WSISecurityGroup>) newMemberOfList.clone();
		}
		buildParentsMap();
	}
	
	private void buildParentsMap(){
		for(WSISecurityGroup group:groupList){
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
		if(groupList==null)
		{
			return 0;
		}
		return groupList.size();
	};		
	
    public Class<?> getColumnClass(int c) {
    	if (c==4){
    		return Boolean.class;
    	} else {
    		return String.class;
    	}
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public ArrayList<WSISecurityGroup> getGroups() {
		return groupList;
	}
	
	public ArrayList<WSISecurityGroup> getNewGroupMembership() {
		return newMemberOfList;
	}
	
	public ArrayList<WSISecurityGroup> getOrigGroupMembership() {
		return oldMemberOfList;
	}
	
	public HashMap<WSISecurityGroup, ArrayList<WSISecurityGroup>> getParentsModel() {
		return parentsMap;
	}
	
	public WSISecurityGroup getGroupAt(int rowIndex) {
		return groupList.get(rowIndex);						
	}		
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSISecurityGroup grp = getGroupAt(rowIndex);

		return getColumnValueForGroup(grp, columnIndex);
	}
		
	public void setMembershipAt(int rowIndex, Boolean member)
	{
		WSISecurityGroup grp = getGroupAt(rowIndex);

		if (member)
		{
			// Make sure we are a member of the specifed group
			if(!this.newMemberOfList.contains(grp))
			{
				newMemberOfList.add(grp);
			}
		}
		else
		{
			// Make sure we are NOT a member of the specified group
			if(this.newMemberOfList.contains(grp))
			{
				newMemberOfList.remove(grp);
			}
		}
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
		case 4: 
			return newMemberOfList.contains(grp);
		default: return null;
		}
		
	}


}
