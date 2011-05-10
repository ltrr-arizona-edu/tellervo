package edu.cornell.dendro.corina.admin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;

import com.dmurph.mvc.MVCEvent;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.admin.control.AuthenticateEvent;
import edu.cornell.dendro.corina.admin.control.DisplayUGAEvent;
import edu.cornell.dendro.corina.admin.control.EditUserEvent;
import edu.cornell.dendro.corina.admin.control.ToggleDisabledAccountsEvent;
import edu.cornell.dendro.corina.admin.model.SecurityGroupTableModel;
import edu.cornell.dendro.corina.admin.model.SecurityUserTableModel;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.WSIEntityResource;

/**
 * GUI class for administering users and groups.  Allows user with the correct
 * privileges to create and edit users and groups.  Also allows editing of which
 * groups a user is in.
 *
 * @author  peterbrewer
 * @author dan
 */
public class UserGroupAdminView extends javax.swing.JDialog implements ActionListener, MouseListener
{
    
	private static final long serialVersionUID = -7039984838996355038L;
	private static UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
	private SecurityUserTableModel usersModel;
	private SecurityGroupTableModel groupsModel;
	private TableRowSorter<SecurityUserTableModel> usersSorter;
	private TableRowSorter<SecurityGroupTableModel> groupsSorter;

	
    /** Creates new form UserGroupAdmin */
    public UserGroupAdminView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        linkModel();
        setupGui();
        internationlizeComponents();
    }
    
    private void linkModel(){
    	
        // Populate user list
        usersModel = mainModel.getUsersModel();
        tblUsers.setModel(usersModel);
        usersSorter = mainModel.getUsersSorter(usersModel);
        
        // Populate groups list
        groupsModel = mainModel.getGroupsModel();
        tblGroups.setModel(groupsModel);
        groupsSorter = mainModel.getGroupsSorter(groupsModel);
        
    }
    
    @SuppressWarnings("unchecked")
	private void setupGui(){
    	// Set up basic dialog 
        setLocationRelativeTo(null);
        setIconImage(Builder.getApplicationIcon());
                
        tblUsers.setRowSorter(usersSorter);
        tblGroups.setRowSorter(groupsSorter);

        tblUsers.addMouseListener(this);
        tblUsers.removeColumn(tblUsers.getColumn( I18n.getText("dbbrowser.hash")));
              
        btnOk.addActionListener(this);
        btnDeleteUser.addActionListener(this);
        btnNewUser.addActionListener(this);
        
        setIconImage(Builder.getApplicationIcon());
        
        this.chkShowDisabledUsers.setSelected(false);
        showDisabledAccounts(false);
        
		/*tblUsers.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				// only clicks on tables
				if(!(e.getSource() instanceof JTable))
					return;
				
				JTable table = (JTable) e.getSource();
				ElementListTableModel model = (ElementListTableModel) table.getModel();
				
				// get the row and sanity check
				int row = table.rowAtPoint(e.getPoint());
				if(row < 0 || row >= model.getRowCount())
					return;
				
				// select it?
				table.setRowSelectionInterval(row, row);
				
				// get the element
				Element element = model.getElementAt(row);
				
				// create and show the menu
				//JPopupMenu popup = new ElementListPopupMenu(element, DBBrowser.this);
				//popup.show(table, e.getX(), e.getY());
			}
		});*/
        
        
    }
    
    private void internationlizeComponents()
    {
    	this.setTitle(I18n.getText("admin.usersAndGroups"));
    	chkShowDisabledUsers.setText(I18n.getText("admin.showDisabledAccounts"));
    	chkShowDisabledGroups.setText(I18n.getText("admin.showDisabledGroups"));
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
        chkShowDisabledGroups = new javax.swing.JCheckBox();
        btnEditGroup = new javax.swing.JButton();
        btnNewGroup = new javax.swing.JButton();
        btnDeleteGroup = new javax.swing.JButton();
        scrollMembers = new javax.swing.JScrollPane();
        tblMembers = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "peter", "Peter", "Brewer", "Admin, Staff", new Boolean(true)},
                {"2", "lucas", "Lucas", "Madar", "Admin, Staff", new Boolean(true)}
            },
            new String [] {
                "ID", "User", "First name", "Last name", "Groups", "Enabled"
            }
        ) {
            @SuppressWarnings("unchecked")
			Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            @SuppressWarnings("unchecked")
			public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
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

        tblGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Admin", new Boolean(true)},
                {"2", "Staff", new Boolean(true)},
                {"3", "Students", new Boolean(true)},
                {"4", "Guests", new Boolean(true)}
            },
            new String [] {
                "ID", "Group", "Enabled"
            }
        ) {
            @SuppressWarnings("unchecked")
			Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            @SuppressWarnings("unchecked")
			public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollGroups.setViewportView(tblGroups);

        chkShowDisabledGroups.setSelected(true);
        chkShowDisabledGroups.setText("Show disabled groups");
        chkShowDisabledGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowDisabledGroupsActionPerformed(evt);
            }
        });

        btnEditGroup.setText("Edit");

        btnNewGroup.setText("New");

        btnDeleteGroup.setText("Delete");

        tblMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "peter", "Peter", "Brewer", "Admin, Staff", new Boolean(true)},
                {"2", "lucas", "Lucas", "Madar", "Admin, Staff", new Boolean(true)}
            },
            new String [] {
                "ID", "User", "First name", "Last name", "Groups", "Enabled"
            }
        ) {
            @SuppressWarnings("unchecked")
			Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            @SuppressWarnings("unchecked")
			public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollMembers.setViewportView(tblMembers);

        accountsTabPane.addTab("Groups", groupPanel);
        groupPanel.setLayout(new MigLayout("", "[183px][140px,grow][61px][6px][65px][6px][81px]", "[25px][127px,grow][15px][181px,grow]"));
        lblGroupMembers = new javax.swing.JLabel();
        
                lblGroupMembers.setText("Group members:");
                groupPanel.add(lblGroupMembers, "cell 0 2 7 1,alignx left,aligny top");
        groupPanel.add(scrollMembers, "cell 0 3 7 1,grow");
        groupPanel.add(scrollGroups, "cell 0 1 7 1,grow");
        groupPanel.add(chkShowDisabledGroups, "cell 0 0,alignx left,aligny center");
        groupPanel.add(btnEditGroup, "cell 2 0,alignx left,aligny top");
        groupPanel.add(btnNewGroup, "cell 4 0,alignx left,aligny top");
        groupPanel.add(btnDeleteGroup, "cell 6 0,alignx left,aligny top");
        getContentPane().setLayout(new MigLayout("", "[571px,grow]", "[405px,grow][25px]"));

        btnOk.setText("Ok");
        getContentPane().add(btnOk, "cell 0 1,alignx right,aligny top");
        getContentPane().add(accountsTabPane, "cell 0 0,grow");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditUser444ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUser444ActionPerformed
    	editUser();
    }//GEN-LAST:event_btnEditUser444ActionPerformed

    
    private void chkShowDisabledUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowDisabledUsersActionPerformed
    	showDisabledAccounts(chkShowDisabledUsers.isSelected());
    }//GEN-LAST:event_chkShowDisabledUsersActionPerformed

    private void chkShowDisabledGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowDisabledGroupsActionPerformed  
    }//GEN-LAST:event_chkShowDisabledGroupsActionPerformed
    
    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               	CorinaModelLocator.getInstance();
               	MVCEvent authenticateUserEvent = new AuthenticateEvent(mainModel);
               	authenticateUserEvent.dispatch();
        		MVCEvent displayEvent = new DisplayUGAEvent();
        		displayEvent.dispatch();
            }
        });
    }
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTabbedPane accountsTabPane;
    protected javax.swing.JButton btnDeleteGroup;
    protected javax.swing.JButton btnDeleteUser;
    protected javax.swing.JButton btnEditGroup;
    protected javax.swing.JButton btnEditUser;
    protected javax.swing.JButton btnNewGroup;
    protected javax.swing.JButton btnNewUser;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JCheckBox chkShowDisabledGroups;
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
    // End of variables declaration//GEN-END:variables

 	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnOk) 
		{
			App.dictionary.query();
			App.dictionary.debugDumpListeners();
			this.dispose();
		}
		else if (e.getSource()==this.btnDeleteUser)
		{
			Object[] options = {"OK",
            "Cancel"};
			int ret = JOptionPane.showOptionDialog(getParent(), 
					"Are you sure you want to delete the user '"+ usersModel.getUserAt(tblUsers.convertRowIndexToModel(tblUsers.getSelectedRow())).getUsername() +"'?", 
					"Confirm delete", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			if(ret == JOptionPane.YES_OPTION)
			{
				deleteUser(usersModel.getUserAt(tblUsers.convertRowIndexToModel(tblUsers.getSelectedRow())).getId());
			}
		}
		else if (e.getSource()==this.btnNewUser)
		{
	        UserUIView userDialog = new UserUIView(this, true);
	        userDialog.setVisible(true);
		}
	}
	
	private Boolean deleteUser(String usrid)
	{
		WSIEntity entity = new WSIEntity();
		
		entity.setId(usrid);
		
		entity.setType(EntityType.SECURITY_USER);
    			
		// associate a resource
    	WSIEntityResource rsrc = new WSIEntityResource(CorinaRequestType.DELETE, entity);
    	
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		
		if(accdialog.isSuccessful())
		{
			rsrc.getAssociatedResult();
			JOptionPane.showMessageDialog(this, "User deleted", "Success", JOptionPane.NO_OPTION);
			return true;
		}
		
		JOptionPane.showMessageDialog(this, "Unable to delete user as their details are referenced by data in the database.\n" +
				"If the user is no longer active you can disable instead.", "Error", JOptionPane.ERROR_MESSAGE);
		
		return false;
	}
    
	/**
	 * Hide or show disabled user accounts
	 * 
	 * @param show
	 */
	public void showDisabledAccounts(Boolean show)
	{
		ToggleDisabledAccountsEvent event = new ToggleDisabledAccountsEvent(show, mainModel);
		event.dispatch();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()>1)
		{
			// Edit user when table is double clicked
			editUser();
		}
		
	}
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) {	}
	
	private void editUser()
	{
		int userIndex = tblUsers.convertRowIndexToModel(tblUsers.getSelectedRow());
		EditUserEvent event = new EditUserEvent(userIndex, mainModel);
		event.dispatch();
	}
}
