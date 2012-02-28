package org.tellervo.desktop.admin.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.schema.WSIPermission;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.schema.WSIPermission.Entity;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.PermissionsResource;


public class GroupsWithPermissionsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(GroupsWithPermissionsTableModel.class);

	private ArrayList<WSIPermission> groupList;
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.group"),
            I18n.getText("admin.create"),
            I18n.getText("admin.read"),
            I18n.getText("admin.update"),
            I18n.getText("admin.delete"),
            I18n.getText("permission.denied"), 
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
    				permclone.getSecurityUsersAndSecurityGroups().add((Serializable) usrOrGroup);
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
    	if (c==7)
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
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		// Do not allow admin row to be edited
		if(getValueAt(row, 0).equals("1"))
		{
			return false;
		}
		
		
		if(col>=2 && col <=5)
		{
			if(getValueAt(row, 6).equals(true))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else if (col ==6)
		{
			return true;
		}
		
		return false;
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
			case 6: return permission.isPermissionDenied();
			case 7: return permission.getDecidedBy();
			default: return null;
		}
	}
	
	public void setRowToDefaultValues(int rowIndex)
	{
		WSIPermission permission = getWSIPermissionAt(rowIndex);
		permission.setPermissionToCreate(false);
	    permission.setPermissionToRead(false); 
	    permission.setPermissionToUpdate(false); 
	    permission.setPermissionToDelete(false); 
	    permission.setPermissionDenied(false);
	    permission.setDecidedBy(null);
		
	    PermissionsResource resource = new PermissionsResource(permission);
		
		// Query db 
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting permissions info");
			Alert.error("Error", "Error getting permissions info");
			return;
		}
		
		ArrayList<WSIPermission> res = resource.getAssociatedResult();
		
		groupList.set(rowIndex, res.get(0));
		
		fireTableDataChanged();
	    
	}
	
	@Override
	public void setValueAt(Object val, int rowIndex, int colIndex)
	{
		if(!isCellEditable(rowIndex, colIndex)) return;
		if(getColumnClass(colIndex)!=Boolean.class) return;
		
		WSIPermission permission = getWSIPermissionAt(rowIndex);
		Boolean oldval = (Boolean) getValueAt(rowIndex, colIndex);
		
		switch (colIndex) {
		case 2: permission.setPermissionToCreate(!oldval); break;
		case 3: permission.setPermissionToRead(!oldval); break;
		case 4: permission.setPermissionToUpdate(!oldval); break;
		case 5: permission.setPermissionToDelete(!oldval); break;
		case 6: 
			permission.setPermissionDenied(!oldval);
			permission.setPermissionToCreate(oldval);
			permission.setPermissionToRead(oldval);
			permission.setPermissionToUpdate(oldval);
			permission.setPermissionToDelete(oldval);
			break;
		}
		
		permission.setDecidedBy(null);
		
		PermissionsResource resource = new PermissionsResource(permission);
		
		// Query db 
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting permissions info");
			Alert.error("Error", "Error getting permissions info");
			return;
		}
		
		ArrayList<WSIPermission> res = resource.getAssociatedResult();
		
		groupList.set(rowIndex, res.get(0));
		
		fireTableDataChanged();
	}
	
	public WSISecurityGroup getSecurityGroupAtRow(int rowind)
	{
		WSIPermission perm = getWSIPermissionAt(rowind);
		
		return (WSISecurityGroup) perm.getSecurityUsersAndSecurityGroups().get(0);
	}
	

}
