package edu.cornell.dendro.corina.admin;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.schema.SecurityGroup;
import edu.cornell.dendro.corina.schema.SecurityUser;


public class SecurityUserTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private List<SecurityUser> userList;
	    	
    private final String[] columnNames = {
            "#",
            "User",
            "First name",
            "Last name",
            "Groups",
            "Enabled",
        };
	
	public SecurityUserTableModel(List<SecurityUser> usrLst){
		userList = usrLst;
	}
    
    public void setUsers(List<SecurityUser> usrList){
    	userList = usrList;        	
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
	
	public SecurityUser getUserAt(int rowIndex) {
		return userList.get(rowIndex);						
	}		
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		SecurityUser usr = getUserAt(rowIndex);

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
	
	public String getGroupsAsString(SecurityUser usr)
	{
		List<SecurityGroup> grps = usr.getMemberOf().getSecurityGroups();
		String str = "";
		for(SecurityGroup grp : grps)
		{
			str+=grp.getName()+"; ";
		}
		
		// Remove last "; " from string
		if(str.length()>=2) str=str.substring(0, str.length()-2);
		
		return str;
	}


}