package org.tellervo.desktop.admin.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.model.GroupsWithPermissionsTableModel;
import org.tellervo.desktop.admin.model.UsersWithPermissionsTableModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.PermissionsEntityType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIPermission;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.PermissionsResource;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import javax.swing.JLabel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PermissionByEntityUI extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	private UsersWithPermissionsTableModel userTableModel;
	private GroupsWithPermissionsTableModel groupTableModel;
	private final static Logger log = LoggerFactory.getLogger(PermissionByEntityUI.class);
	private JTable tblUserPerms;
	private ArrayList<WSIPermission> permsList;
	private JTable tblGroupPerms;
	private JTabbedPane tabbedPane;
	private JPanel panelUsers;
	private JPanel panelGroups;
	private JButton btnEditGroup;
	private JButton btnRevertToDefault;
	private JButton btnEditUser;
	private JPanel panelTitle;
	private JLabel lblGroupPermissions;
	private JPanel panelIcon;
	private JLabel lblIcon;
	private JTextArea txtDescription;
	private JLabel lblPermissionsInfoFor;
	private JTextField txtLabCode;
	private JButton btnGroupRefresh;
	private JButton btnUserRefresh;
	private ITridas theentity;

	/**
	 * Create the panel.
	 */
	public PermissionByEntityUI(ITridas entity) {

		setEntity(entity);
	}
	

	
	private void lookupUsersAndGroups()
	{
		PermissionsEntityType pEntityType;

		if(theentity instanceof TridasObject)
		{
			pEntityType = PermissionsEntityType.OBJECT;
		}
		else if(theentity instanceof TridasElement)
		{
			pEntityType = PermissionsEntityType.ELEMENT;
		}
		else if(theentity instanceof TridasMeasurementSeries)
		{
			pEntityType = PermissionsEntityType.MEASUREMENT_SERIES;
		}		
		else if(theentity instanceof TridasDerivedSeries)
		{
			pEntityType = PermissionsEntityType.DERIVED_SERIES;
		}	
		else
		{
			Alert.error("Error", "Error searching for permissions info.  Entity provided is not valid.");
			log.error("Error searching for permissions info.  Entity provided is not valid.");
			return;
		}
		
		PermissionsResource resource = new PermissionsResource();
		
		for (WSISecurityUser user : (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary"))
		{
			if(!user.isIsActive()) continue;
			
			resource.addPermission(pEntityType, theentity.getIdentifier().getValue(), user);
		}
		
		for (WSISecurityGroup grp : (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary"))
		{
			resource.addPermission(pEntityType, theentity.getIdentifier().getValue(), grp);
		}
		
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
		
		permsList = resource.getAssociatedResult();
		
		if(tblGroupPerms!=null) tblGroupPerms.repaint();
		if(tblUserPerms!=null) tblUserPerms.repaint();
		
		if(permsList.size()==0)
		{
			Alert.error("Error", "No records found");
			return;
		}
	}
	
	
	
	public void setEntity(ITridas entity)
	{
		theentity =entity;
		lookupUsersAndGroups();
		
		userTableModel = new UsersWithPermissionsTableModel(permsList);
		groupTableModel = new GroupsWithPermissionsTableModel(permsList);
		setLayout(new MigLayout("", "[][grow]", "[86px][28.00][328px,grow]"));
		
		lblPermissionsInfoFor = new JLabel("Permissions info for:");
		add(lblPermissionsInfoFor, "cell 0 1,alignx trailing");
		
		// TODO : make this code pretty...
		txtLabCode = new JTextField();	
		txtLabCode.setText(entity.getTitle());
		txtLabCode.setFocusable(false);
		
		txtLabCode.setEditable(false);
		add(txtLabCode, "cell 1 1,growx");
		txtLabCode.setColumns(10);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "cell 0 2 2 1,grow");
		
		panelGroups = new JPanel();
		tabbedPane.addTab("Group permissions", Builder.getIcon("edit_group.png", 22), panelGroups, null);
		panelGroups.setLayout(new MigLayout("", "[648px,grow]", "[381px,grow][]"));
		
		JScrollPane scrollGroup = new JScrollPane();
		panelGroups.add(scrollGroup, "cell 0 0,grow");
		
		tblGroupPerms = new JTable(groupTableModel);
		scrollGroup.setViewportView(tblGroupPerms);
		
		btnEditGroup = new JButton("View / Edit Group");
		btnEditGroup.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editSelectedGroup();
				
			}
		});
		panelGroups.add(btnEditGroup, "flowx,cell 0 1");
		
		btnRevertToDefault = new JButton("Revert to database defaults");
		btnRevertToDefault.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				resetPermissionsForCurrentGroup();
			}
			
		});
		panelGroups.add(btnRevertToDefault, "cell 0 1");
		
		btnGroupRefresh = new JButton("Refresh");
		btnGroupRefresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				App.dictionary.query();
				lookupUsersAndGroups();
				groupTableModel.fireTableDataChanged();
			}
			
		});
		panelGroups.add(btnGroupRefresh, "cell 0 1");
		
		tblGroupPerms.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblGroupPerms.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblGroupPerms.getColumnModel().getColumn(2).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(3).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(4).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(5).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(6).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(7).setPreferredWidth(300);
		
		
		panelUsers = new JPanel();
		tabbedPane.addTab("Users with access", Builder.getIcon("edit_user.png", 22), panelUsers, null);
		panelUsers.setLayout(new MigLayout("", "[648px,grow]", "[381px,grow][]"));
		
		JScrollPane scrollUser = new JScrollPane();
		panelUsers.add(scrollUser, "cell 0 0,grow");
		
		tblUserPerms = new JTable(userTableModel);
		scrollUser.setViewportView(tblUserPerms);
		
		btnEditUser = new JButton("View / Edit User");
		btnEditUser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editSelectedUser();
				
			}
		});
		panelUsers.add(btnEditUser, "flowx,cell 0 1");
		
		btnUserRefresh = new JButton("Refresh");
		btnUserRefresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				App.dictionary.query();
				userTableModel.fireTableDataChanged();	
				groupTableModel.fireTableDataChanged();
			}
			
		});
		panelUsers.add(btnUserRefresh, "cell 0 1");
		
		tblUserPerms.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblUserPerms.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblUserPerms.getColumnModel().getColumn(2).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(3).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(4).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(5).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(6).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(7).setPreferredWidth(300);
	
		tblGroupPerms.addMouseListener(this);
		tblUserPerms.addMouseListener(this);
		
		panelTitle = new JPanel();
		add(panelTitle, "cell 0 0 2 1,growx,aligny top");
		panelTitle.setBackground(Color.WHITE);
		panelTitle.setLayout(new MigLayout("", "[411.00,grow][]", "[][grow]"));
		
		lblGroupPermissions = new JLabel("Access Permissions");
		lblGroupPermissions.setFont(new Font("Dialog", Font.BOLD, 14));
		panelTitle.add(lblGroupPermissions, "flowy,cell 0 0");
		
		panelIcon = new JPanel();
		panelIcon.setBackground(Color.WHITE);
		panelTitle.add(panelIcon, "cell 1 0 1 2,grow");
		
		lblIcon = new JLabel();
		lblIcon.setIcon(Builder.getIcon("trafficlight.png", 64));
		panelIcon.add(lblIcon);
		
		txtDescription = new JTextArea();
		txtDescription.setBorder(null);
		txtDescription.setEditable(false);
		txtDescription.setFocusable(false);
		txtDescription.setFont(new Font("Dialog", Font.PLAIN, 10));
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);
		txtDescription.setText("Permissions are set on groups, not users.  You can set: create; read; " +
				"update; and delete permissions separately.  Permissions are inherited from the default " +
				"database permissions given to the group (editable in the main group dialog). If you " +
				"change permissions here you will override the access for the current entity and any " +
				"child entities in the database. " +
				"The users tab shows the current list of users who have permission to access this " +
				"entity in some way.");
		panelTitle.add(txtDescription, "cell 0 1,grow,wmin 10");
				
	}
	
	public ArrayList<WSIPermission> getUserPermissionsList()
	{
		return permsList;
	}


	private void editSelectedGroup()
	{
				
		WSISecurityGroup group = groupTableModel.getSecurityGroupAtRow(tblGroupPerms.getSelectedRow());
		
		if(group==null) return;
		
		for (WSISecurityGroup grp : (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary"))
		{
			if(grp.getId().equals(group.getId()))
			{
				group = grp;
				break;
			}
		}

		GroupUIView groupDialog = new GroupUIView(null, true, group, groupTableModel.getWSIPermissionAt(tblGroupPerms.getSelectedRow()));
		groupDialog.setVisible(true);
	}

	private void editSelectedUser()
	{
		WSISecurityUser user = userTableModel.getSecurityUserAtRow(tblUserPerms.getSelectedRow());
		
		for (WSISecurityUser usr : (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary"))
		{
			if(usr.getId().equals(user.getId()))
			{
				user = usr;
				break;
			}
		}

		UserUIView userDialog = new UserUIView(null, true, user);
		userDialog.setVisible(true);
	}
	
	private void resetPermissionsForCurrentGroup()
	{
		groupTableModel.setRowToDefaultValues(tblGroupPerms.getSelectedRow());
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		/*
		 * Disable double clicking to edit for now as it is confusing for the user
		 * 
		 * 
		 
		// Only worry about double clicks
		if(e.getClickCount()<2) return;
		 
		Component comp = e.getComponent();
		
		if(comp instanceof JTable)
		{
			if(((JTable) comp).getModel() instanceof GroupsWithPermissionsTableModel)
			{
				editSelectedGroup();
				
			}
			else if (((JTable) comp).getModel() instanceof UsersWithPermissionsTableModel)
			{
				editSelectedUser();
			}
		}*/
		
		
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
