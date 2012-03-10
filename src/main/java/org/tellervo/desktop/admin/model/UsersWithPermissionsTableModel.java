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
import org.tellervo.desktop.ui.I18n;


public class UsersWithPermissionsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<WSIPermission> userList;
	private final static Logger log = LoggerFactory.getLogger(UsersWithPermissionsTableModel.class);

    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.user"),
            I18n.getText("admin.create"),
            I18n.getText("admin.read"),
            I18n.getText("admin.update"),
            I18n.getText("admin.delete"),
            I18n.getText("permission.denied"), 
            I18n.getText("permission.decidedby"),
        };
	
    
    public UsersWithPermissionsTableModel(ArrayList<WSIPermission> list)
    {
    	setup(list, true);
    }
    
    public UsersWithPermissionsTableModel(ArrayList<WSIPermission> list, Boolean hideNoAccessUsers)
    {
    	setup(list, hideNoAccessUsers);
    }
    
    private void setup(ArrayList<WSIPermission> list, Boolean hideNoAccessUsers)
    {
    	userList = new ArrayList<WSIPermission>();
    	
    	// Remove any groups from the list as we're not interested in these
    	for(WSIPermission perm : list)
    	{
    		
    		log.debug("Permissions: o "+perm.isPermissionToCreate()+", "
    				+perm.isPermissionToRead()+", "
    				+perm.isPermissionToUpdate()+", "
    				+perm.isPermissionToDelete()+", ");
    		
    		// If requested, skip over users with no access 
    		if(perm.isPermissionToCreate()==false && 
    		   perm.isPermissionToRead()  ==false &&
    		   perm.isPermissionToUpdate()==false && 
    		   perm.isPermissionToUpdate()==false &&
    		   hideNoAccessUsers)
    		{
    			continue;
    		}
    		else if (perm.isPermissionDenied())
    		{
    			continue;
    		}

    		
    		WSIPermission permclone = (WSIPermission) perm.clone();
    		permclone.getSecurityUsers().clear();
    		
    		for(WSISecurityUser usrOrGroup : perm.getSecurityUsers())
    		{
    			if(usrOrGroup instanceof WSISecurityUser)
    			{
    				permclone.getSecurityUsers().add(usrOrGroup);
    			}
    		}
    		
    		if(permclone.getSecurityUsers().size()>0)
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
		return userList.get(rowIndex);		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSIPermission permission = getWSIPermissionAt(rowIndex);
    	ArrayList<WSISecurityUser> userDictionary = (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary");  

    	WSISecurityUser usr = null;
		for(WSISecurityUser u: userDictionary){
			WSISecurityUser userorgroup = permission.getSecurityUsers().get(0);
			

			if((userorgroup).getId().equals(u.getId())) usr = u;
			
		}
	
		if(usr==null) return null;
    	
		switch (columnIndex) {
			case 0: return usr.getId();
			case 1: return usr.getLastName()+", " + usr.getFirstName();
			case 2: return permission.isPermissionToCreate();
			case 3: return permission.isPermissionToRead();
			case 4: return permission.isPermissionToUpdate();
			case 5: return permission.isPermissionToDelete();
			case 6: return permission.isPermissionDenied();
			case 7: return permission.getDecidedBy();
			default: return null;
		}
	}
	
	public WSISecurityUser getSecurityUserAtRow(int rowind)
	{
		WSIPermission perm = getWSIPermissionAt(rowind);
		
		return (WSISecurityUser) perm.getSecurityUsers().get(0);
	}
	

}
