package edu.cornell.dendro.corina.admin.model;

import java.util.ArrayList;

import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.model.AbstractModel;

import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class UserGroupAdminModel extends AbstractModel {

	private static UserGroupAdminModel model = null;
	private UserGroupAdminView mainView;
	private SecurityUserTableModel usersModel;
	private TableRowSorter<SecurityUserTableModel> usersSorter;
	private SecurityGroupTableModel groupsModel;
	private TableRowSorter<SecurityGroupTableModel> groupsSorter;

	public static UserGroupAdminModel getInstance() {
		if (model == null) {
			model = new UserGroupAdminModel();
		}
		return model;
	}

	public void setMainView(UserGroupAdminView mainView) {
		this.mainView = mainView;
	}
	
	public UserGroupAdminView getMainView() {
		return mainView;
	}

	public SecurityGroupTableModel getGroupsModel() {
		if (groupsModel == null) {
			ArrayList<WSISecurityGroup> lstofGroups = (ArrayList<WSISecurityGroup>) Dictionary
					.getDictionaryAsArrayList("securityGroupDictionary");
			groupsModel = new SecurityGroupTableModel(lstofGroups, null);
		}
		return groupsModel;
	}

	public TableRowSorter<SecurityGroupTableModel> getGroupsSorter(
			SecurityGroupTableModel argGroupsModel) {
		if(groupsSorter == null){
			groupsSorter = new TableRowSorter<SecurityGroupTableModel>(argGroupsModel);
		}
		return groupsSorter;
	}

	public SecurityUserTableModel getUsersModel() {
		if (usersModel == null) {
			ArrayList<WSISecurityUser> lstofUsers = (ArrayList<WSISecurityUser>) Dictionary
					.getDictionaryAsArrayList("securityUserDictionary");
			usersModel = new SecurityUserTableModel(lstofUsers);
		}
		return usersModel;
	}

	public TableRowSorter<SecurityUserTableModel> getUsersSorter(
			SecurityUserTableModel argUsersModel) {
		if(usersSorter==null){
			usersSorter = new TableRowSorter<SecurityUserTableModel>(argUsersModel);
		}
		return usersSorter;
	}

}