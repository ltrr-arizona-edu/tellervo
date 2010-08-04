package edu.cornell.dendro.corina.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.schema.WSISecurityUser.MemberOf;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

/*
 * UserUI.java
 *
 * Created on January 8, 2010, 11:03 AM
 */



/**
 * GUI Class for viewing/editing a users details.
 * 
 * @author  peterbrewer
 */
public class UserUI extends javax.swing.JDialog implements ActionListener, MouseListener{
    
	WSISecurityUser user = new WSISecurityUser();
	Boolean isNewUser = true;
	private SecurityGroupTableModel groupsModel;
	private TableRowSorter<SecurityGroupTableModel> groupsSorter;
	
    /** Creates new form UserUI */
    public UserUI(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    	isNewUser = true;
        setupGUI();

    }
    
    public UserUI(JDialog parent, boolean modal, WSISecurityUser user) {
        super(parent, modal);
        this.user = user;
        initComponents();
    	isNewUser = false;
    	setupGUI();

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblUser = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        txtFirstname = new javax.swing.JTextField();
        txtLastname = new javax.swing.JTextField();
        chkEnabled = new javax.swing.JCheckBox();
        scrollPane = new javax.swing.JScrollPane();
        tblGroups = new javax.swing.JTable();
        btnDoIt = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnSetPwd = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        lblPassword2 = new javax.swing.JLabel();
        txtPassword2 = new javax.swing.JPasswordField();

        jLabel1.setText("jLabel1");

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblId.setText("ID:");

        txtId.setEditable(false);

        lblUser.setText("User name:");

        lblName.setText("Real name:");

        chkEnabled.setText("Enabled");

        tblGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Group", "Description", "Member"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane.setViewportView(tblGroups);

        btnDoIt.setText("Apply");

        btnClose.setText("OK");

        btnSetPwd.setText("Reset Password");

        lblPassword.setText("Password:");

        lblPassword2.setText("Confirm:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnSetPwd)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 93, Short.MAX_VALUE)
                        .add(btnDoIt)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnClose))
                    .add(layout.createSequentialGroup()
                        .add(lblUser)
                        .add(12, 12, 12)
                        .add(txtUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblPassword)
                            .add(lblPassword2))
                        .add(19, 19, 19)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtPassword2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .add(txtPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblName)
                            .add(lblId))
                        .add(14, 14, 14)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtId, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .add(chkEnabled)
                            .add(layout.createSequentialGroup()
                                .add(txtFirstname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtLastname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblId)
                    .add(txtId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkEnabled)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblName)
                    .add(txtLastname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtFirstname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUser)
                    .add(txtUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPassword))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblPassword2)
                    .add(txtPassword2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnClose)
                    .add(btnDoIt)
                    .add(btnSetPwd))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnClose;
    protected javax.swing.JButton btnDoIt;
    protected javax.swing.JButton btnSetPwd;
    protected javax.swing.JCheckBox chkEnabled;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel lblId;
    protected javax.swing.JLabel lblName;
    protected javax.swing.JLabel lblPassword;
    protected javax.swing.JLabel lblPassword2;
    protected javax.swing.JLabel lblUser;
    protected javax.swing.JScrollPane scrollPane;
    protected javax.swing.JTable tblGroups;
    protected javax.swing.JTextField txtFirstname;
    protected javax.swing.JTextField txtId;
    protected javax.swing.JTextField txtLastname;
    protected javax.swing.JPasswordField txtPassword;
    protected javax.swing.JPasswordField txtPassword2;
    protected javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
 
    @SuppressWarnings("unchecked")
	private void setupGUI()
    {
    	this.setLocationRelativeTo(getRootPane());
    	this.lblId.setVisible(false);
    	this.txtId.setVisible(false);
    	    	
    	this.tblGroups.addMouseListener(this);

    	ArrayList<WSISecurityGroup> memberGroups = new ArrayList<WSISecurityGroup>();
    	if(isNewUser)
    	{
        	this.setTitle("Create user");
        	btnDoIt.setText("Create");
        	btnClose.setText("Close");
        	btnSetPwd.setVisible(false);   	
        	chkEnabled.setSelected(true);
    	}
    	else
    	{
        	this.setTitle("Edit user");
        	btnDoIt.setText("Apply");
        	btnClose.setText("Close");
        	lblPassword.setVisible(false);
        	txtPassword.setVisible(false);
        	lblPassword2.setVisible(false);
        	txtPassword2.setVisible(false);
	    	if(user.isSetFirstName()) txtFirstname.setText(user.getFirstName());
	    	if(user.isSetLastName())  txtLastname.setText(user.getLastName());
	    	if(user.isSetId()) 		  txtId.setText(user.getId());
	    	if(user.isSetUsername())  txtUsername.setText(user.getUsername());
	    	if(user.isSetIsActive())  chkEnabled.setSelected(user.isIsActive());
	    	memberGroups = ((ArrayList)this.user.getMemberOf().getSecurityGroups());
    	}
    	
        // Populate groups list
        ArrayList<WSISecurityGroup> lstofGroups = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
        groupsModel = new SecurityGroupTableModel(lstofGroups, memberGroups);
        tblGroups.setModel(groupsModel);
        groupsSorter = new TableRowSorter<SecurityGroupTableModel>(groupsModel);
        tblGroups.setRowSorter(groupsSorter);
        tblGroups.setEditingColumn(3);
    	
    	this.btnDoIt.addActionListener(this);
    	this.btnClose.addActionListener(this);
    	this.btnSetPwd.addActionListener(this);
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnClose) 
		{
			this.dispose();
		}	
		else if (e.getSource()==this.btnDoIt)
		{
			saveChangesToUser();
		}
		else if (e.getSource()==this.btnSetPwd)
		{
			SetPasswordUI setPwdDialog = new SetPasswordUI(null, this.user);
			setPwdDialog.setVisible(true);
			setPwdDialog.setModal(true);
			setPwdDialog.setLocationRelativeTo(null);
			
		}
	}
	
	private void saveChangesToUser()
	{
		user.setFirstName(txtFirstname.getText());
		user.setLastName(txtLastname.getText());
		user.setUsername(txtUsername.getText());
		user.setIsActive(this.chkEnabled.isSelected());
		
		// Get groups
		ArrayList<WSISecurityGroup> membershipList = this.groupsModel.getGroupMembership();
		MemberOf memberOf = new MemberOf();
		memberOf.setSecurityGroups(membershipList);
		user.setMemberOf(memberOf);
		
		if(isNewUser)
		{
			// Creating new user
	    	
			// Check passwords match
			String p1 = new String(this.txtPassword.getPassword());
			String p2 = new String(this.txtPassword2.getPassword());
			if(!p1.equals(p2))
			{
				JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			p1 = null;
			p2 = null;
			
	    	// Set password to hash
	    	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
				String pwd1 = new String(this.txtPassword.getPassword());
				digest.update(pwd1.getBytes());
		    	user.setHashOfPassword(StringUtils.bytesToHex(digest.digest()));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
						
			// associate a resource
	    	SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.CREATE, user);
	    	
	    	
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				rsrc.getAssociatedResult();
				dispose();
			}
			
			JOptionPane.showMessageDialog(this, "Error creating user.  Make sure the username is unique." + accdialog.getFailException().
					getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{
			// Editing existing user
			
			// associate a resource
	    	SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, user);
	    	
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				rsrc.getAssociatedResult();
			}
			
			JOptionPane.showMessageDialog(this, "Error updating user: " + accdialog.getFailException().
					getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()>1)
		{
			// Double clicked on groups table - change users membership accordingly
			Boolean isMember = (Boolean) this.groupsModel.getValueAt(this.tblGroups.getSelectedRow(), 3);
			
			if(isMember)
			{
				groupsModel.setMembershipAt(this.tblGroups.getSelectedRow(), false);
			}
			else
			{
				groupsModel.setMembershipAt(this.tblGroups.getSelectedRow(), true);

			}
			this.tblGroups.repaint();
		}
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
