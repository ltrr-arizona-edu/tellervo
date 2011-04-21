package edu.cornell.dendro.corina.admin;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * Simple table model for a list of securityGroups
 * 
 * @author peterbrewer
 *
 */
public class SecurityGroupTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -8612040164917147271L;
	private ArrayList<WSISecurityGroup> groupList;
	private ArrayList<WSISecurityGroup> memberOfList;	
  	
	
    private final String[] columnNames = {
            I18n.getText("dbbrowser.hash"),
            I18n.getText("admin.groups"),
            I18n.getText("admin.description"),
            I18n.getText("admin.ismember"),
        };
	
	public SecurityGroupTableModel(ArrayList<WSISecurityGroup> grpLst, ArrayList<WSISecurityGroup> memberOf){
		groupList = grpLst;
		memberOfList = memberOf;
	}
    
    public void setGroups(ArrayList<WSISecurityGroup> grpList){
    	groupList = grpList;  
    }
    
    public void setMemberOfList(ArrayList<WSISecurityGroup> memberOfList)
    {
    	this.memberOfList = memberOfList;
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
    	if (c==3){
    		return Boolean.class;
    	} else {
    		return String.class;
    	}
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public ArrayList<WSISecurityGroup> getGroups() {
		return groupList;
	}
	
	public ArrayList<WSISecurityGroup> getGroupMembership() {
		return memberOfList;
	}
	
	public WSISecurityGroup getGroupAt(int rowIndex) {
		return groupList.get(rowIndex);						
	}		
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSISecurityGroup grp = getGroupAt(rowIndex);

		return getColumnValueForGroup(grp, columnIndex);
	}
		
	public void setMembershipAt(int rowIndex, Boolean member)
	{
		WSISecurityGroup grp = getGroupAt(rowIndex);

		if (member)
		{
			// Make sure we are a member of the specifed group
			if(!this.memberOfList.contains(grp))
			{
				memberOfList.add(grp);
			}
		}
		else
		{
			// Make sure we are NOT a member of the specified group
			if(this.memberOfList.contains(grp))
			{
				memberOfList.remove(grp);
			}
		}
	}
	
	public Object getColumnValueForGroup(WSISecurityGroup grp, int columnIndex) {
		
		switch (columnIndex) {
		case 0: return grp.getId();
		case 1: return grp.getName();
		case 2: return grp.getDescription();
		case 3: 
			if(memberOfList==null) return false;
			for(WSISecurityGroup memberof : memberOfList)
			{
				if (grp.equals(memberof))
				{
					return true;
				}
			}
			return false;
		default: return null;
		}
		
	}


}