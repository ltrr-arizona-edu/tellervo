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

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.model.AbstractModel;

import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.WSIEntityResource;

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

	@SuppressWarnings("unchecked")
	public void addGroups(ArrayList<WSISecurityGroup> newGroups){
		groupList.addAll((ArrayList<WSISecurityGroup>) newGroups.clone());
	}

	@SuppressWarnings("unchecked")
	public void addUsers(ArrayList<WSISecurityUser> newUsers){
		userList.addAll((ArrayList<WSISecurityUser>) newUsers.clone());
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
	
	public void removeGroups(ArrayList<WSISecurityGroup> groups){
		for(WSISecurityGroup g:groupList){
			if(groups.contains(g)){
				groupList.remove(g);
			}
		}
	}
	
	public void removeUsers(ArrayList<WSISecurityUser> users){
		for(WSISecurityUser u:userList){
			if(users.contains(u)){
				userList.remove(u);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setGroupList(ArrayList<WSISecurityGroup> newList){
		groupList = (ArrayList<WSISecurityGroup>) newList.clone();
	}
	
	public void setMainView(UserGroupAdminView mainView) {
		this.mainView = mainView;
	}
	
	@SuppressWarnings("unchecked")
	public void setUserList(ArrayList<WSISecurityUser> newList){
		userList = (ArrayList<WSISecurityUser>) newList.clone();
	}

	public void addUser(WSISecurityUser user) {
		userList.add(user);
		usersModelA.fireTableDataChanged();
	}

	public void updateUser(WSISecurityUser user) {
		for(WSISecurityUser u:userList){
			if(u.getId()==user.getId()){
				u=(WSISecurityUser) user.clone();
				break;
			}
		}
		usersModelA.fireTableDataChanged();
	}


}
