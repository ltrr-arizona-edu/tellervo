package edu.cornell.dendro.corina.admin.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSIPermission;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.I18n;

public class UsersWithPermissionsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<WSIPermission> userList;
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.user"),
            I18n.getText("admin.firstName"),
            I18n.getText("admin.lastName"),
            I18n.getText("admin.create"),
            I18n.getText("admin.read"),
            I18n.getText("admin.update"),
            I18n.getText("admin.delete"), 
            I18n.getText("permission.decidedby"),
        };
	
    
    public UsersWithPermissionsTableModel(ArrayList<WSIPermission> list)
    {
    	userList = new ArrayList<WSIPermission>();
    	
    	// Remove any groups from the list as we're not interested in these
    	for(WSIPermission perm : list)
    	{
    		WSIPermission permclone = (WSIPermission) perm.clone();
    		permclone.getSecurityUsersAndSecurityGroups().clear();
    		
    		for(Object usrOrGroup : perm.getSecurityUsersAndSecurityGroups())
    		{
    			if(usrOrGroup instanceof WSISecurityUser)
    			{
    				permclone.getSecurityUsersAndSecurityGroups().add(usrOrGroup);
    			}
    		}
    		
    		if(permclone.getSecurityUsersAndSecurityGroups().size()>0)
    		{
    			userList.add(permclone);
    		}
    	}
    	
    }
    
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(userList==null)
		{
			return 0;
		}
		return userList.size();
	};	

	@Override
    public Class<?> getColumnClass(int c) {
    	if (c==8)
    	{
    		return String.class;
    	}
    	else if(c>3)
    	{
    		return Boolean.class;
    	} 
    	else 
    	{
    		return String.class;
    	}
    }
	
	@Override
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public WSIPermission getWSIPermissionAt(int rowIndex)
	{
		return userList.get(rowIndex);		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSIPermission permission = getWSIPermissionAt(rowIndex);
    	ArrayList<WSISecurityUser> userDictionary = (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary");  

    	WSISecurityUser usr = null;
		for(WSISecurityUser u: userDictionary){
			Object userorgroup = permission.getSecurityUsersAndSecurityGroups().get(0);
			
			if(userorgroup instanceof WSISecurityUser)
			{	
				if(((WSISecurityUser)userorgroup).getId().equals(u.getId())) usr = u;
			}
		}
	
		if(usr==null) return null;
    	
		switch (columnIndex) {
			case 0: return usr.getId();
			case 1: return usr.getUsername();
			case 2: return usr.getFirstName();
			case 3: return usr.getLastName();
			case 4: return permission.isPermissionToCreate();
			case 5: return permission.isPermissionToRead();
			case 6: return permission.isPermissionToUpdate();
			case 7: return permission.isPermissionToDelete();
			case 8: return permission.getDecidedBy();
			default: return null;
		}
	}
	
	public WSISecurityUser getSecurityUserAtRow(int rowind)
	{
		WSIPermission perm = getWSIPermissionAt(rowind);
		
		return (WSISecurityUser) perm.getSecurityUsersAndSecurityGroups().get(0);
	}
	
}
