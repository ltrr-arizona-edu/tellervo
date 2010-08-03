package edu.cornell.dendro.corina.admin;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * Simple table model for a list of securityUsers
 * 
 * @author peterbrewer
 *
 */
public class SecurityUserTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
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
	
	public SecurityUserTableModel(ArrayList<WSISecurityUser> usrLst){
		userList = usrLst;
		completeUserList = userList;
	}
    
    public void setUsers(ArrayList<WSISecurityUser> usrList){
    	userList = usrList;  
		completeUserList = userList;

    }
    
	public int getColumnCount() {
		return columnNames.length;
	};
	
	public int getRowCount() {
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
			case 4: return getGroupsAsString(usr);
			case 5: return usr.isIsActive();
			default: return null;
		}
	}
	
	public String getGroupsAsString(WSISecurityUser usr)
	{
		ArrayList<WSISecurityGroup> grps = (ArrayList<WSISecurityGroup>) usr.getMemberOf().getSecurityGroups();
		String str = "";
		for(WSISecurityGroup grp : grps)
		{
			str+=grp.getName()+"; ";
		}
		
		// Remove last "; " from string
		if(str.length()>=2) str=str.substring(0, str.length()-2);
		
		return str;
	}
	
	public Object getColumnValueForUser(WSISecurityUser usr, int columnIndex) {
		
		switch(columnIndex) {
		case 0: 
			return usr.getId();
		case 1:
			return usr.getUsername();
		case 2:
			return usr.getFirstName();
		case 3:
			return usr.getLastName();
		case 4:
			return getGroupsAsString(usr);
		case 5:
			return usr.isIsActive();
		}
		
		return null;
	}


}