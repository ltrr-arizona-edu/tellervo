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

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.model.AbstractModel;

import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class UserGroupAdminModel extends AbstractModel {

	private static final long serialVersionUID = -1731874198092507070L;
	private static UserGroupAdminModel model = null;
	private UserGroupAdminView mainView;
	private SecurityGroupTableModelA groupsModel;
	private	TableRowSorter<SecurityGroupTableModelA> groupsSorter;
	private SecurityUserTableModelA usersModel;
	private TableRowSorter<SecurityUserTableModelA> usersSorter;

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

}
