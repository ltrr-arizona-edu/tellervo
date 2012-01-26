package edu.cornell.dendro.corina.admin.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSIPermission;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.I18n;

public class GroupsWithPermissionsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<WSIPermission> groupList;
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.group"),
            I18n.getText("admin.create"),
            I18n.getText("admin.read"),
            I18n.getText("admin.update"),
            I18n.getText("admin.delete"), 
            I18n.getText("permission.decidedby"),
        };
	
    
    public GroupsWithPermissionsTableModel(ArrayList<WSIPermission> list)
    {
    	groupList = new ArrayList<WSIPermission>();
    	
    	// Remove any users from the list as we're not interested in these
    	for(WSIPermission perm : list)
    	{
    		WSIPermission permclone = (WSIPermission) perm.clone();
    		permclone.getSecurityUsersAndSecurityGroups().clear();
    		
    		for(Object usrOrGroup : perm.getSecurityUsersAndSecurityGroups())
    		{
    			if(usrOrGroup instanceof WSISecurityGroup)
    			{
    				permclone.getSecurityUsersAndSecurityGroups().add(usrOrGroup);
    			}
    		}
    		if(permclone.getSecurityUsersAndSecurityGroups().size()>0)
    		{
    			groupList.add(permclone);
    		}
    	}
    	
    }
    
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(groupList==null)
		{
			return 0;
		}
		return groupList.size();
	};	

	@Override
    public Class<?> getColumnClass(int c) {
    	if (c==6)
    	{
    		return String.class;
    	}
    	else if(c>1)
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
		return groupList.get(rowIndex);		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSIPermission permission = getWSIPermissionAt(rowIndex);
    	ArrayList<WSISecurityGroup> groupDictionary = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  

    	WSISecurityGroup grp = null;
		for(WSISecurityGroup u: groupDictionary){
			Object userorgroup = permission.getSecurityUsersAndSecurityGroups().get(0);
			
			if(userorgroup instanceof WSISecurityGroup)
			{	
				if(((WSISecurityGroup)userorgroup).getId().equals(u.getId())) grp = u;
			}
		}
	
		if(grp==null) return null;
    	
		switch (columnIndex) {
			case 0: return grp.getId();
			case 1: return grp.getName();
			case 2: return permission.isPermissionToCreate();
			case 3: return permission.isPermissionToRead();
			case 4: return permission.isPermissionToUpdate();
			case 5: return permission.isPermissionToDelete();
			case 6: return permission.getDecidedBy();
			default: return null;
		}
	}
}
