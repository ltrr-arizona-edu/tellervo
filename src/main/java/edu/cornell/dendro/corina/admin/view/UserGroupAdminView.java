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
 *     Dan Girshovich
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.admin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;
import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.admin.control.AuthenticateEvent;
import edu.cornell.dendro.corina.admin.control.DeleteGroupEvent;
import edu.cornell.dendro.corina.admin.control.DeleteUserEvent;
import edu.cornell.dendro.corina.admin.control.DisplayUGAEvent;
import edu.cornell.dendro.corina.admin.control.EditGroupEvent;
import edu.cornell.dendro.corina.admin.control.EditUserEvent;
import edu.cornell.dendro.corina.admin.control.OkFinishEvent;
import edu.cornell.dendro.corina.admin.control.ToggleDisabledUsersEvent;
import edu.cornell.dendro.corina.admin.model.SecurityGroupTableModelA;
import edu.cornell.dendro.corina.admin.model.SecurityMixTableModel;
import edu.cornell.dendro.corina.admin.model.SecurityUserTableModelA;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.admin.testing.SubmissionTest;
import edu.cornell.dendro.corina.admin.testing.UserGroupSyncer;
import edu.cornell.dendro.corina.admin.testing.UserMemberOfWiper;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import javax.swing.JButton;

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

    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               	CorinaModelLocator.getInstance();
               	new AuthenticateEvent(mainModel).dispatch();
        		new DisplayUGAEvent().dispatch();
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
    @SuppressWarnings("serial")
	private void initComponents() {

        accountsTabPane = new javax.swing.JTabbedPane();
        userPanel = new javax.swing.JPanel();
        scrollUsers = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        btnEditUser = new javax.swing.JButton();
        btnNewUser = new javax.swing.JButton();
        btnDeleteUser = new javax.swing.JButton();
        chkShowDisabledUsers = new javax.swing.JCheckBox();
        groupPanel = new javax.swing.JPanel();
        scrollGroups = new javax.swing.JScrollPane();
        tblGroups = new javax.swing.JTable();
        btnEditGroup = new javax.swing.JButton();
        btnNewGroup = new javax.swing.JButton();
        btnDeleteGroup = new javax.swing.JButton();
        scrollMembers = new javax.swing.JScrollPane();
        tblMembers = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblUsers.setShowVerticalLines(false);
        scrollUsers.setViewportView(tblUsers);

        btnEditUser.setText("Edit");
        btnEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUser444ActionPerformed(evt);
            }
        });

        btnNewUser.setText("New");

        btnDeleteUser.setText("Delete");

        chkShowDisabledUsers.setSelected(true);
        chkShowDisabledUsers.setText("Show disabled accounts");
        chkShowDisabledUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowDisabledUsersActionPerformed(evt);
            }
        });

        accountsTabPane.addTab("Users", userPanel);
        userPanel.setLayout(new MigLayout("", "[197px][126px,grow][61px][6px][65px][6px][81px]", "[25px][335px,grow,fill]"));
        userPanel.add(scrollUsers, "cell 0 1 7 1,grow");
        userPanel.add(chkShowDisabledUsers, "cell 0 0,alignx left,aligny center");
        userPanel.add(btnEditUser, "cell 2 0,alignx left,aligny top");
        userPanel.add(btnNewUser, "cell 4 0,alignx left,aligny top");
        userPanel.add(btnDeleteUser, "cell 6 0,alignx left,aligny top");

        scrollGroups.setViewportView(tblGroups);

        btnEditGroup.setText("Edit");
        btnNewGroup.setText("New");
        btnDeleteGroup.setText("Delete");
        
        scrollMembers.setViewportView(tblMembers);

        accountsTabPane.addTab("Groups", groupPanel);
        groupPanel.setLayout(new MigLayout("", "[183px][140px,grow][61px][6px][65px][6px][81px]", "[25px][127px,grow][15px][181px,grow]"));
        lblGroupMembers = new javax.swing.JLabel();
        
                lblGroupMembers.setText("Group members:");
                groupPanel.add(lblGroupMembers, "cell 0 2 7 1,alignx left,aligny top");
        groupPanel.add(scrollMembers, "cell 0 3 7 1,grow");
        groupPanel.add(scrollGroups, "cell 0 1 7 1,grow");
        groupPanel.add(btnEditGroup, "cell 2 0,alignx left,aligny top");
        groupPanel.add(btnNewGroup, "cell 4 0,alignx left,aligny top");
        groupPanel.add(btnDeleteGroup, "cell 6 0,alignx left,aligny top");
        getContentPane().setLayout(new MigLayout("", "[571px,grow]", "[405px,grow][25px]"));
        
        btnSync = new JButton("Sync");
        btnSync.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		new UserGroupSyncer();;
        	}
        });
        
        btnTestSubmit = new JButton("Test Submit");
        btnTestSubmit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		new SubmissionTest();
        	}
        });
        getContentPane().add(btnTestSubmit, "flowx,cell 0 1,alignx right");
        getContentPane().add(btnSync, "cell 0 1,alignx right");

        btnOk.setText("Ok");
        getContentPane().add(btnOk, "cell 0 1,alignx right,aligny top");
        getContentPane().add(accountsTabPane, "cell 0 0,grow");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    @SuppressWarnings("unchecked")
	private void linkModel(){
    	
        // Populate user list
        tblUsers.setModel(mainModel.getUsersModelA());
        tblUsers.setRowSorter(mainModel.getUsersSorterA());
        
        // Populate groups list
       	groupList = (ArrayList<WSISecurityGroup>) Dictionary
			.getDictionaryAsArrayList("securityGroupDictionary");
    	groupsModel = new SecurityGroupTableModelA(groupList);
        tblGroups.setModel(groupsModel);
        tblGroups.setRowSorter(new TableRowSorter<SecurityGroupTableModelA>(groupsModel));
         
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
            
        this.chkShowDisabledUsers.setSelected(true);
        new ToggleDisabledUsersEvent(true, mainModel).dispatch();   
    }
    
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
    }
    
  
    private void btnEditUser444ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUser444ActionPerformed
    	editUser();
    }//GEN-LAST:event_btnEditUser444ActionPerformed

    
    private void chkShowDisabledUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowDisabledUsersActionPerformed
		new ToggleDisabledUsersEvent(chkShowDisabledUsers.isSelected(), mainModel).dispatch();
    }//GEN-LAST:event_chkShowDisabledUsersActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
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
    protected javax.swing.JLabel lblGroupMembers;
    protected javax.swing.JScrollPane scrollGroups;
    protected javax.swing.JScrollPane scrollMembers;
    protected javax.swing.JScrollPane scrollUsers;
    protected javax.swing.JTable tblGroups;
    protected javax.swing.JTable tblMembers;
    protected javax.swing.JTable tblUsers;
    protected javax.swing.JPanel userPanel;
    private JButton btnSync;
    private JButton btnTestSubmit;
    // End of variables declaration//GEN-END:variables

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
				deleteUser(selectedUser.getId());
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
				deleteGroup(selectedGroup.getId());
			}
		}
	}
    
	private void deleteUser(String usrid) {
		new DeleteUserEvent(usrid, mainModel).dispatch();
	}
	
	private void deleteGroup(String groupid) {
		new DeleteGroupEvent(groupid, mainModel).dispatch();
	}
	
	private void resetMembers(){
		WSISecurityGroup selectedGroup = groupsModel.getGroupAt(
				tblGroups.convertRowIndexToModel(tblGroups.getSelectedRow()));

		SecurityMixTableModel membersModel = new SecurityMixTableModel(selectedGroup);
		tblMembers.setModel(membersModel);
		tblMembers.setRowSorter(new TableRowSorter<SecurityMixTableModel>(membersModel));
	}
    
	private void editUser() {
		int userIndex = tblUsers.convertRowIndexToModel(tblUsers
				.getSelectedRow());
		new EditUserEvent(userIndex, mainModel).dispatch();
	}

	private void editGroup() {
		int groupIndex = tblGroups.convertRowIndexToModel(tblGroups
				.getSelectedRow());
		new EditGroupEvent(groupIndex, mainModel).dispatch();
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
