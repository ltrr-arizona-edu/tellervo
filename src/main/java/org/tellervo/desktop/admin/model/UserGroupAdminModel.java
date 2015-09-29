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
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.TableRowSorter;

import org.tellervo.desktop.admin.view.UserGroupAdminView;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;

import com.dmurph.mvc.model.AbstractModel;


public class UserGroupAdminModel extends AbstractModel {

	private static final long serialVersionUID = -1731874198092507070L;
	private static UserGroupAdminModel model = null;
	public static UserGroupAdminModel getInstance() {
		if (model == null) {
			model = new UserGroupAdminModel();
		}
		return model;
	}
	private UserGroupAdminView mainView;
	private SecurityUserTableModelA usersModelA;
	private TableRowSorter<SecurityUserTableModelA> usersSorter;
	private ArrayList<WSISecurityGroup> groupList;
	private ArrayList<WSISecurityUser> userList;

	public void addGroup(WSISecurityGroup group) {
		getGroupList().add(group);
	}

	@SuppressWarnings("unchecked")
	public void addGroups(ArrayList<WSISecurityGroup> newGroups){
		groupList.addAll((ArrayList<WSISecurityGroup>) newGroups.clone());
	}

	public void addUser(WSISecurityUser user) {
		userList.add(user);
		usersModelA.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addUsers(ArrayList<WSISecurityUser> newUsers){
		userList.addAll((ArrayList<WSISecurityUser>) newUsers.clone());
	}

	public WSISecurityGroup getGroupById(String id){
		for(WSISecurityGroup g: getGroupList()){
			if(id.equals(g.getId())) return g;
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<WSISecurityGroup> getGroupList() {
        if(groupList==null){
        	groupList = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
        }
        
        return groupList;
	}
	
	public UserGroupAdminView getMainView() {
		return mainView;
	}
	
	public ArrayList<WSISecurityGroup> getParentGroups() {
		ArrayList<WSISecurityGroup> parentGroups = new ArrayList<WSISecurityGroup>();
		Set<String> childIds = new HashSet<String>();
		
		
		for(WSISecurityGroup g:getGroupList()){
			for(String childId: g.getGroupMembers()){
				childIds.add(childId);
			}
		}
		for(WSISecurityGroup g:getGroupList()){
			if(!childIds.contains(g.getId()))
				parentGroups.add(g);
		}
		
		return parentGroups;
	}
	
	public WSISecurityUser getUserById(String id){
		for(WSISecurityUser u: getUserList()){
			if(id.equals(u.getId())) return u;
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<WSISecurityUser> getUserList() {
        if(userList==null){
        	userList = (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary");  
        }
        
        return userList;
	}
	
	public SecurityUserTableModelA getUsersModelA() {
		if(usersModelA==null){
			usersModelA = new SecurityUserTableModelA();
		}
		
		return usersModelA;
	}

	public TableRowSorter<SecurityUserTableModelA> getUsersSorterA() {
		if(usersSorter==null){
			usersSorter = new TableRowSorter<SecurityUserTableModelA>(getUsersModelA());
		}
		return usersSorter;
	}
	
	public void removeGroupById(String groupid) {
		for(WSISecurityGroup g:getGroupList()){
			if(g.getId() == groupid){
				getGroupList().remove(g);
			}
		}
	}

	public void removeUserById(String userId){
		for(WSISecurityUser u:getUserList()){
			if(u.getId() == userId){
				userList.remove(u);
			}
		}
	}

	public void setMainView(UserGroupAdminView mainView) {
		this.mainView = mainView;
	}

	public void updateGroup(WSISecurityGroup group) {
		for(WSISecurityGroup g:getGroupList()){
			if(g.getId()==group.getId()){
				g=(WSISecurityGroup) group.clone();
				break;
			}
		}

		//possibly fire change from here later
	}

	public void updateUser(WSISecurityUser user) {
		for(WSISecurityUser u:getUserList()){
			if(u.getId()==user.getId()){
				u=(WSISecurityUser) user.clone();
				break;
			}
		}
		usersModelA.fireTableDataChanged();
	}
	
}
