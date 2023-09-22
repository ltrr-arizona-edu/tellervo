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
 *     Dan Girshovich
 ******************************************************************************/
package org.tellervo.desktop.admin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.search.Searchable;
import org.jdesktop.swingx.search.TableSearchable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.control.AuthenticateEvent;
import org.tellervo.desktop.admin.control.CreateNewGroupEvent;
import org.tellervo.desktop.admin.control.CreateNewUserEvent;
import org.tellervo.desktop.admin.control.DeleteGroupEvent;
import org.tellervo.desktop.admin.control.DeleteUserEvent;
import org.tellervo.desktop.admin.control.EditGroupEvent;
import org.tellervo.desktop.admin.control.EditUserEvent;
import org.tellervo.desktop.admin.control.OkFinishEvent;
import org.tellervo.desktop.admin.control.ToggleDisabledUsersEvent;
import org.tellervo.desktop.admin.control.UpdateUserEvent;
import org.tellervo.desktop.admin.model.SecurityGroupTableModelA;
import org.tellervo.desktop.admin.model.SecurityMixTableModel;
import org.tellervo.desktop.admin.model.TransferableGroup;
import org.tellervo.desktop.admin.model.TransferableUser;
import org.tellervo.desktop.admin.model.UserGroupAdminModel;
import org.tellervo.desktop.admin.model.UserGroupNode;
import org.tellervo.desktop.admin.model.UserGroupTreeCellRenderer;
import org.tellervo.desktop.model.TellervoModelLocator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;

import net.miginfocom.swing.MigLayout;

/**
 * GUI class for administering users and groups.  Allows user with the correct
 * privileges to create and edit users and groups.  Also allows editing of which
 * groups a user is in.
 *
 * @author  peterbrewer
 * @author  dan
 */
public class UserGroupAdminView extends javax.swing.JDialog implements ActionListener
{
    
	private static final long serialVersionUID = -7039984838996355038L;
	private static UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
	private SecurityGroupTableModelA groupsModel;
	private ArrayList<WSISecurityGroup> groupList;
    protected javax.swing.JTabbedPane accountsTabPane;
    protected javax.swing.JButton btnDeleteGroup;
    protected javax.swing.JButton btnDeleteUser;
    protected javax.swing.JButton btnEditGroup;
    protected javax.swing.JButton btnEditUser;
    protected javax.swing.JButton btnNewGroup;
    protected javax.swing.JButton btnNewUser;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JCheckBox chkShowDisabledUsers;
    protected javax.swing.JPanel groupPanel;
    protected javax.swing.JScrollPane scrollGroups;
    protected javax.swing.JScrollPane scrollMembers;
    protected javax.swing.JScrollPane scrollUsers;
    protected javax.swing.JTable tblGroups;
    protected javax.swing.JTable tblMembers;
    protected JXTable tblUsers;
    protected javax.swing.JPanel userPanel;
    private JLabel lblMembers;
    private JPanel debugPanel;
    private JButton test1;
    private JButton test2;
    private JButton test3;
    private JButton test4;
    private JButton test5;
    private JPanel treeviewPanel;
    private JScrollPane scrollPane;
    private UserGroupTree tree;
    private JButton btnClearAllPlacements;
    private JSplitPane splitPane;
    private JPanel panel;
    private JPanel panel_1;
    private JPanel panelTitle;
    private JLabel lblTitle;
    private JPanel panelIcon;
    private JLabel lblIcon;
    private JTextArea txtDescription;
    private JButton btnRefresh;
    private JPanel panelButtons;
    private Searchable searchableUser;
	
	private final static Logger log = LoggerFactory.getLogger(UserGroupAdminView.class);
	private JButton btnFindUser;


    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               	TellervoModelLocator.getInstance();
               	new AuthenticateEvent(mainModel).dispatch();
            }
        });
    }
     
    /** Creates new form UserGroupAdmin */
    public UserGroupAdminView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        linkModel();
        setupGui();
        internationlizeComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

        accountsTabPane = new javax.swing.JTabbedPane();
        userPanel = new javax.swing.JPanel();
        
        scrollUsers = new javax.swing.JScrollPane();
        tblUsers = new JXTable();
        searchableUser = new TableSearchable(tblUsers);
        
        
        btnEditUser = new javax.swing.JButton();
        btnNewUser = new javax.swing.JButton();
        btnDeleteUser = new javax.swing.JButton();
        chkShowDisabledUsers = new javax.swing.JCheckBox();
        groupPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblUsers.setShowVerticalLines(false);
        scrollUsers.setViewportView(tblUsers);

        btnEditUser.setText("Edit");
        btnEditUser.setIcon(Builder.getIcon("edit.png", 16));

        btnEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserAction(evt);
            }
        });

        btnNewUser.setText("New");
        btnNewUser.setIcon(Builder.getIcon("edit_add.png", 16));
        btnDeleteUser.setText("Delete");
        btnDeleteUser.setIcon(Builder.getIcon("cancel.png", 16));

        chkShowDisabledUsers.setSelected(true);
        chkShowDisabledUsers.setText("Show disabled accounts");
        chkShowDisabledUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowDisabledUsersActionPerformed(evt);
            }
        });

        accountsTabPane.addTab("Users", userPanel);
        userPanel.setLayout(new MigLayout("hidemode 3", "[236.00px,grow][][][][]", "[25px][335px,grow,fill]"));
        
        btnFindUser = new JButton("Find");
        btnFindUser.setIcon(Builder.getIcon("find.png", 16));
        btnFindUser.setActionCommand("findUser");
        btnFindUser.addActionListener(this);
        
        
        userPanel.add(btnFindUser, "cell 1 0");
        userPanel.add(scrollUsers, "cell 0 1 5 1,grow");
        userPanel.add(chkShowDisabledUsers, "cell 0 0,alignx left,aligny center");
        userPanel.add(btnEditUser, "cell 2 0,alignx left,aligny top");
        userPanel.add(btnNewUser, "cell 3 0,alignx left,aligny top");
        userPanel.add(btnDeleteUser, "cell 4 0,alignx center,aligny top");

        accountsTabPane.addTab("Groups", groupPanel);
        groupPanel.setLayout(new BorderLayout(0, 0));
        
        splitPane = new JSplitPane();
        splitPane.setResizeWeight(1.0);
        splitPane.setOneTouchExpandable(true);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(1.0);
        groupPanel.add(splitPane);
        
        panel = new JPanel();
        splitPane.setLeftComponent(panel);
        panel.setLayout(new MigLayout("", "[79.00px][grow][][][]", "[15px][200px,grow]"));
        btnEditGroup = new javax.swing.JButton();
        btnEditGroup.setIcon(Builder.getIcon("edit.png", 16));
        panel.add(btnEditGroup, "cell 2 0");
        
                btnEditGroup.setText("Edit");
        btnNewGroup = new javax.swing.JButton();
        btnNewGroup.setIcon(Builder.getIcon("edit_add.png", 16));
        panel.add(btnNewGroup, "cell 3 0");
        //btnNewGroup.setEnabled(false);
        btnNewGroup.setText("New");
        scrollGroups = new javax.swing.JScrollPane();
        panel.add(scrollGroups, "cell 0 1 5 1,growx,aligny top");
        tblGroups = new javax.swing.JTable();
        
                scrollGroups.setViewportView(tblGroups);
                        btnDeleteGroup = new javax.swing.JButton();
                        btnDeleteGroup.setIcon(Builder.getIcon("cancel.png", 16));
                       
                        panel.add(btnDeleteGroup, "cell 4 0");
                        btnDeleteGroup.setText("Delete");
        
        panel_1 = new JPanel();
        splitPane.setRightComponent(panel_1);
        panel_1.setMinimumSize(new Dimension(0,0));
        panel_1.setLayout(new MigLayout("", "[453px,grow]", "[][403px,grow]"));
        
        lblMembers = new JLabel("List of members:");
        panel_1.add(lblMembers, "cell 0 0,alignx left,aligny center");
        scrollMembers = new javax.swing.JScrollPane();
        panel_1.add(scrollMembers, "cell 0 1,growx,aligny top");
        tblMembers = new javax.swing.JTable();
        
        scrollMembers.setViewportView(tblMembers);
        getContentPane().setLayout(new MigLayout("", "[550.00px,grow]", "[102.00][350px,grow][25px]"));
        
        panelTitle = new JPanel();
        panelTitle.setBackground(Color.WHITE);
        getContentPane().add(panelTitle, "cell 0 0,grow");
        panelTitle.setLayout(new MigLayout("", "[378.00,grow][]", "[20.00][34.00,grow]"));
        
        lblTitle = new JLabel("Users and Groups Admin");
        lblTitle.setFont(new Font("Dialog", Font.BOLD, 14));
        panelTitle.add(lblTitle, "cell 0 0");
        
        panelIcon = new JPanel();
        panelIcon.setBackground(Color.WHITE);
        panelTitle.add(panelIcon, "cell 1 0 1 2,grow");
        panelIcon.setLayout(new BorderLayout(0, 0));
        
        lblIcon = new JLabel();
        lblIcon.setIcon(Builder.getIcon("edit_group.png", 64));
        panelIcon.add(lblIcon);
        
        txtDescription = new JTextArea();
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(null);
        txtDescription.setText("Use the tables below to add, edit and delete users and groups.  You can use the 'tree view' tab to see a hierarchical representation of the groups and users currently in the database.");
        txtDescription.setLineWrap(true);
        txtDescription.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtDescription.setFocusable(false);
        txtDescription.setEditable(false);
        panelTitle.add(txtDescription, "cell 0 1,grow, wmin 10");
        getContentPane().add(accountsTabPane, "cell 0 1,grow");
        
        debugPanel = new JPanel();
        //accountsTabPane.addTab("Debug tab", null, debugPanel, null);
        debugPanel.setLayout(new MigLayout("", "[173px][192px][205px][117px]", "[29px][][][][]"));
        
        test1 = new JButton("Create User testABC ");
        test1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		WSISecurityUser user = new WSISecurityUser();
    			user.setUsername("testABC");
    			user.setFirstName("first");
    			user.setLastName("last");
    			user.setMemberOves(null);
    			user.setIsActive(true);
    			new CreateNewUserEvent(user, "pwdpwdpwd", new JDialog()).dispatch();
        	}
        });
        debugPanel.add(test1, "cell 0 0,alignx left,aligny top");
        
        
        test2 = new JButton("Create Group testGroup");
        test2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		WSISecurityGroup group = new WSISecurityGroup();
    			group.setName("testGroup");
    			group.setDescription("test group");
    			new CreateNewGroupEvent(group, new JDialog()).dispatch();
        	}
        });
        
        btnClearAllPlacements = new JButton("Clear all placements");
        btnClearAllPlacements.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		for(WSISecurityUser u:mainModel.getUserList()){
        			if(u.getMemberOves()!=null && !u.getMemberOves().isEmpty()){
	        			ArrayList<WSISecurityGroup> oldMemList = new ArrayList<WSISecurityGroup>();
	        			for(String memId : u.getMemberOves()){
	        				oldMemList.add(mainModel.getGroupById(memId));
	        			}
	        			u.setMemberOves(null);
	        			new UpdateUserEvent(u, oldMemList, new ArrayList<WSISecurityGroup>(), new JDialog()).dispatch();
	        			break;
        			}
        		}
        	}
        });
        debugPanel.add(btnClearAllPlacements, "cell 1 0");
        debugPanel.add(test2, "cell 0 1,alignx left,aligny top");
        
        test3 = new JButton("Add testABC to testGroup");
        test3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		WSISecurityUser user= null;
    			WSISecurityGroup group = null;
    			for(WSISecurityUser u : mainModel.getUserList()){
    				if(u.getUsername().equals("testABC")) user = u;
    			}
    			for(WSISecurityGroup g : mainModel.getGroupList()){
    				if(g.getName().equals("testGroup")) group = g ;
    			}
    			if(user!=null && group!=null){
    				user.getMemberOves().add(group.getId());
    				ArrayList<WSISecurityGroup> oldMemList = new ArrayList<WSISecurityGroup>();
    				ArrayList<WSISecurityGroup> newMemList = new ArrayList<WSISecurityGroup>();
    				newMemList.add(group);
    				new UpdateUserEvent(user, oldMemList, newMemList, new JDialog()).dispatch();
    			}
        	}
        });
        debugPanel.add(test3, "cell 0 2,alignx left,aligny top");
        
        test4 = new JButton("Delete testABC");
        test4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		WSISecurityUser user= null;
    			for(WSISecurityUser u : mainModel.getUserList()){
    				if(u.getUsername().equals("testABC")) user = u;
    			}
    			if(user!=null){
    					new DeleteUserEvent(user.getId()).dispatch();
    			}
        	}
        });
        debugPanel.add(test4, "cell 0 3,alignx left,aligny top");
        
        test5 = new JButton("Delete testGroup");
        test5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		WSISecurityGroup group= null;
    			for(WSISecurityGroup g : mainModel.getGroupList()){
    				if(g.getName().equals("testGroup")) group = g;
    			}
    			if(group != null){
    				new DeleteGroupEvent(group.getId()).dispatch();
    			}
    			else
    			{
    				Alert.error("Error", "testGroup not found");
    			}
        	}
        });
        debugPanel.add(test5, "cell 0 4");
        
        treeviewPanel = new JPanel();
        accountsTabPane.addTab("Tree view", treeviewPanel);
        treeviewPanel.setLayout(new MigLayout("", "[grow][][][][][][][][][grow]", "[grow][][][][][][grow]"));
        
        scrollPane = new JScrollPane();
        treeviewPanel.add(scrollPane, "cell 0 0 10 7,grow");
        
        setupTree();
        
        panelButtons = new JPanel();
        getContentPane().add(panelButtons, "cell 0 2,growx");
        panelButtons.setLayout(new MigLayout("", "[89px][54px,grow][]", "[25px]"));
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.setIcon(Builder.getIcon("reload.png", 16));
        btnRefresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				linkModel();
				groupsModel.fireTableDataChanged();
				tblGroups.repaint();
				tblUsers.repaint();
				tblMembers.repaint();
				
			}
        	
        });
        
        panelButtons.add(btnRefresh, "cell 0 0,alignx left,aligny top");
                btnOk = new javax.swing.JButton();
                panelButtons.add(btnOk, "cell 2 0,growx,aligny top");
                
                        btnOk.setText("Ok");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
	private void setupTree()
	{
        tree = buildTree();
        UserGroupTreeCellRenderer treeRenderer = new UserGroupTreeCellRenderer();
        tree.setCellRenderer(treeRenderer);
        scrollPane.setViewportView(tree);
	}
	
    @SuppressWarnings("unchecked")
	private void linkModel(){
    	
    	// Populate user list
        tblUsers.setModel(mainModel.getUsersModelA());
        tblUsers.setRowSorter(mainModel.getUsersSorterA());
        
        // Populate groups list
       	groupList = mainModel.getGroupList();
    	groupsModel = new SecurityGroupTableModelA();
        tblGroups.setModel(groupsModel);
        tblGroups.setRowSorter(new TableRowSorter<SecurityGroupTableModelA>(groupsModel));
        
        setupTree();
        
         
    }
    
	private void setupGui(){
    	// Set up basic dialog 
        setLocationRelativeTo(null);
        setIconImage(Builder.getApplicationIcon());

        tblUsers.addMouseListener(new UserTableListener());
        tblUsers.removeColumn(tblUsers.getColumn( I18n.getText("dbbrowser.hash")));
              
        tblGroups.addMouseListener(new GroupTableListener());
        
        btnOk.addActionListener(this);
        btnDeleteUser.addActionListener(this);
        btnNewUser.addActionListener(this);
        btnDeleteGroup.addActionListener(this);
        btnNewGroup.addActionListener(this);
        btnEditGroup.addActionListener(this);        
        
        tree = buildTree();
               
        this.chkShowDisabledUsers.setSelected(true);
        new ToggleDisabledUsersEvent(true, mainModel).dispatch();   
    }
	
	/**
	 * Build the tree representation of users and groups
	 * 
	 * @return
	 */
	public UserGroupTree buildTree() {
		WSISecurityGroup dummyRoot = new WSISecurityGroup();
		dummyRoot.setName("All");
    	UserGroupNode root = new UserGroupNode(new TransferableGroup(dummyRoot));
    	root.setRestrictedChildType(UserGroupNode.Type.USER); //this prevents user nodes from being dropped in the group root node "All"
    	for(WSISecurityGroup parent: mainModel.getParentGroups()){
    		UserGroupNode groupRoot = addMembersRecurse(new UserGroupNode(new TransferableGroup(parent)));
    		root.add(groupRoot);
    	}
    	return new UserGroupTree(root);
	}
    
	/** 
	 * Recursively add child nodes
	 * 
	 * @param parent a group node
	 * */
    private UserGroupNode addMembersRecurse(UserGroupNode parent){
    	//check it's a group node
    	if(!parent.getType().equals(UserGroupNode.Type.GROUP)) return null;
    	
    	try{
    	WSISecurityGroup group = ((TransferableGroup) parent.getUserObject()).getGroup();
    	for(String childId: group.getGroupMembers()){
    		WSISecurityGroup child = mainModel.getGroupById(childId);
    		UserGroupNode childNode = addMembersRecurse(new UserGroupNode(new TransferableGroup(child)));
    		parent.add(childNode);
    	}
    	
    	for(String childId: group.getUserMembers()){
    		WSISecurityUser child = mainModel.getUserById(childId);
    		if(!child.isIsActive()) continue;
    		UserGroupNode childNode = new UserGroupNode(new TransferableUser(child));
    		parent.add(childNode);
    	}
    	} catch (Exception e)
    	{
    		log.error(e.getLocalizedMessage());
    		e.printStackTrace();
    	}
    	return parent;
    }

    /**
     * Internationalize the components
     */
	private void internationlizeComponents()
    {
    	this.setTitle(I18n.getText("admin.usersAndGroups"));
    	chkShowDisabledUsers.setText(I18n.getText("admin.showDisabledAccounts"));
    	btnOk.setText(I18n.getText("general.ok"));
    	btnEditUser.setText(I18n.getText("menus.edit"));
    	btnNewUser.setText(I18n.getText("menus.file.new"));
    	btnDeleteUser.setText(I18n.getText("general.delete"));
    	btnEditGroup.setText(I18n.getText("menus.edit"));
    	btnNewGroup.setText(I18n.getText("menus.file.new"));
    	btnDeleteGroup.setText(I18n.getText("general.delete"));
    	accountsTabPane.setTitleAt(0, I18n.getText("admin.users"));
    	accountsTabPane.setTitleAt(1, I18n.getText("admin.groups"));
    	lblMembers.setText(I18n.getText("admin.members"));
    }
    
  
    private void editUserAction(java.awt.event.ActionEvent evt) {
    	editUser();
    }

    
    private void chkShowDisabledUsersActionPerformed(java.awt.event.ActionEvent evt) {
		new ToggleDisabledUsersEvent(chkShowDisabledUsers.isSelected(), mainModel).dispatch();
    }

    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnOk) {
			new OkFinishEvent(mainModel).dispatch();
		} else if (e.getSource() == this.btnDeleteUser) {
			Object[] options = { "OK", "Cancel" };
			WSISecurityUser selectedUser = mainModel.getUsersModelA().getUserAt(
					tblUsers.convertRowIndexToModel(tblUsers.getSelectedRow()));
			int ret = JOptionPane.showOptionDialog(
					getParent(),
					"Are you sure you want to delete the user '"
							+ selectedUser.getUsername()
							+ "'?", "Confirm delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);

			if (ret == JOptionPane.YES_OPTION) {
				new DeleteUserEvent(selectedUser.getId()).dispatch();
			}
		} else if (e.getSource() == this.btnNewUser) {
			new UserUIView(this, true).setVisible(true);
		} else if (e.getSource() == this.btnEditUser){
			editUser();
		} else if (e.getSource() == this.btnNewGroup){
			new GroupUIView(this, true).setVisible(true);
		} else if (e.getSource() == this.btnEditGroup){
			editGroup();
		} else if (e.getSource() == this.btnDeleteGroup){
			Object[] options = { "OK", "Cancel" };
			WSISecurityGroup selectedGroup = groupsModel.getGroupAt(
					tblGroups.convertRowIndexToModel(tblGroups
							.getSelectedRow()));
			int ret = JOptionPane.showOptionDialog(
					getParent(),
					"Are you sure you want to delete the group '"
							+ selectedGroup.getName()
							+ "'?", "Confirm delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);

			if (ret == JOptionPane.YES_OPTION) {
				new DeleteGroupEvent(selectedGroup.getId()).dispatch();
			}
		} else if (e.getActionCommand().equals("findUser"))
		{
			SearchFactory.getInstance().showFindInput(this.tblUsers, searchableUser);

		}
    }

	private void resetMembers(){
		WSISecurityGroup selectedGroup = groupsModel.getGroupAt(
				tblGroups.convertRowIndexToModel(tblGroups.getSelectedRow()));

		SecurityMixTableModel membersModel = new SecurityMixTableModel(selectedGroup);
		tblMembers.setModel(membersModel);
		tblMembers.setRowSorter(new TableRowSorter<SecurityMixTableModel>(membersModel));
	}
    
	private void editUser() {
		int selRow = tblUsers.getSelectedRow();
		if(selRow!=-1){
			int userIndex = tblUsers.convertRowIndexToModel(selRow);
			new EditUserEvent(userIndex, mainModel).dispatch();
		}
	}

	private void editGroup() {
		int selRow = tblGroups.getSelectedRow();
		if(selRow!=-1){
			int groupIndex = tblGroups.convertRowIndexToModel(selRow);
			new EditGroupEvent(groupIndex, mainModel).dispatch();
		}
	}

	private class UserTableListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent me) {
			if (me.getClickCount() > 1) {
				// Edit user when table is double clicked
				editUser();
			}
		}
	}

	private class GroupTableListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent me) {
			if (me.getClickCount() > 1) {
				// Edit group when table is double clicked
				editGroup();
			}
			else if(me.getClickCount() == 1) {
				super.mouseClicked(me);
				resetMembers();
			}
		}
	}
	
	
}
