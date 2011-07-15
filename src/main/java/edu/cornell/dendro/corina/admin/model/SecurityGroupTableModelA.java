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
 */
public class SecurityGroupTableModelA extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private ArrayList<WSISecurityGroup> groupList;
	
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.groups"),
            I18n.getText("admin.description"),
        };
	
	public SecurityGroupTableModelA(ArrayList<WSISecurityGroup> grpLst){
		groupList = grpLst;
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
    	return String.class;
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public ArrayList<WSISecurityGroup> getGroups() {
		return groupList;
	}
	
	public WSISecurityGroup getGroupAt(int rowIndex) {
		return groupList.get(rowIndex);						
	}		
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSISecurityGroup grp = getGroupAt(rowIndex);

		return getColumnValueForGroup(grp, columnIndex);
	}

	public Object getColumnValueForGroup(WSISecurityGroup grp, int columnIndex) {
		
		switch (columnIndex) {
		case 0: return grp.getId();
		case 1: return grp.getName();
		case 2: return grp.getDescription();
		default: return null;
		}
		
	}


}
