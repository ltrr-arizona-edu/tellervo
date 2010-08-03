package edu.cornell.dendro.corina.admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

/**
 * Dialog for setting users own password, or for user with admin
 * privileges to set other users passwords
 *
 * @author  peterbrewer
 */
public class SetPasswordUI extends javax.swing.JDialog implements KeyListener, ActionListener{
    
	WSISecurityUser thisUser;
	
    /**
     * Create a new set password dialog 
     * @param parent
     */
    public SetPasswordUI(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setupGui(App.isAdmin);   
        internationalizeComponents();
        
    }
    

    private void internationalizeComponents()
    {
    	this.setTitle(I18n.getText("admin.setPassword"));
    	lblOld.setText(I18n.getText("admin.oldPassword")+":");
    	lblNew.setText(I18n.getText("admin.newPassword")+":");
    	lblVerify.setText(I18n.getText("admin.verify")+":");
    	btnCancel.setText(I18n.getText("general.cancel"));
    	btnOk.setText(I18n.getText("general.ok"));
    }
    
    /**
     * Create a new admin 'set password' dialog for resetting
     * another users password
     * 
     * @param parent
     * @param adminAuth
     * @param otherUser
     */
    public SetPasswordUI(java.awt.Frame parent, boolean adminAuth, WSISecurityUser otherUser) {
        super(parent, true);
        thisUser = otherUser;  
     
        initComponents();
        setupGui(true);      
    }
        
    /**
     * Setup gui, either as a normal user or
     * with admin privileges 
     * 
     * @param asAdmin
     */
    private void setupGui(Boolean asAdmin)
    {
   	    
    	// Hide user fields
    	lblUser.setVisible(false);
    	lblUserText.setVisible(false);
    	
    	// Add listeners
    	pwdOld.addKeyListener(this);
    	pwdNew.addKeyListener(this);
    	pwdVerify.addKeyListener(this); 
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
        		public void actionPerformed(java.awt.event.ActionEvent evt) {
        			dispose();
        		}
        });
        btnOk.addActionListener(new java.awt.event.ActionListener() {
    		public void actionPerformed(java.awt.event.ActionEvent evt) {
    			if(isPasswordCorrect(pwdNew.getPassword()))
    			{
    				
    				Boolean success = setPassword(pwdNew.getPassword());
    				
    				if(success) dispose();
    			}
    			else
    			{
    				Alert.message(I18n.getText("error"), I18n.getText("error.incorrectPassword"));
    			}
    		}
        });
	
        // Set icon        
        lblIcon.setIcon(Builder.getIcon("password.png", 64));
        
    	// Set up default text on components
    	lblStrengthText.setText("");
    	btnOk.setEnabled(false);
    	
        setIconImage(Builder.getApplicationIcon());

    	
    }
       
    /**
     * Setup GUI for normal user changing their
     * own password
     * 
     */
    private void setupGui()
    {
    	setupGui(false);
    }
    
    
    /**
     * Check the old or admin password specified is correct
     * 
     * @param pwd
     * @return
     * @todo implement
     */
    private Boolean isPasswordCorrect(char[] pwd)
    {
    	return true;
		
    }
    
    /**
     * Set password of specified user.  Only users with
     * admin rights are able to do this.
     * 
     * @param thisUser
     * @param pwd
     * @return
     */
    private Boolean setPassword(WSISecurityUser thisUser, char[] pwd)
    {
    	WSISecurityUser usr = App.currentUser;
    	
    	// Set password to hash
    	MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			String pwd1 = new String(this.pwdNew.getPassword());
			digest.update(pwd1.getBytes());
	    	usr.setHashOfPassword(StringUtils.bytesToHex(digest.digest()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// associate a resource
    	SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, usr);
    	
    	
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		
		if(accdialog.isSuccessful())
		{
			rsrc.getAssociatedResult();
			JOptionPane.showMessageDialog(this, "Password changed successfully", "Success", JOptionPane.NO_OPTION);
			return true;
		}
		
		JOptionPane.showMessageDialog(this, "Error updating: " + accdialog.getFailException().
				getLocalizedMessage(), "blah blah", JOptionPane.ERROR_MESSAGE);
		
		return false;
		
		
    	
    }
    
    /**
     * Set password of current user
     * 
     * @param pwd
     * @return
     * @todo implement
     */
    private Boolean setPassword(char[] pwd)
    {
    	WSISecurityUser thisUser = App.currentUser;
    	    	   	
    	return setPassword(thisUser, pwd);
    	
    }
    
    /**
     * Set the text describing to the user if their new password
     * is good enough.  
     * 
     * @todo add strength calculation to this 
     */
    private void setStrengthDescription()
    {
    	    	
    	if (!(pwdNew.getPassword().length>0 && pwdVerify.getPassword().length>0)) 
    	{
    		btnOk.setEnabled(false); 
    		return;        	
    	}
    	
    	if((Arrays.equals(pwdNew.getPassword(), pwdVerify.getPassword())==false))
    	{
    		// Passwords entered don't match
    		lblStrengthText.setText(I18n.getText("admin.passwordsDontMatch"));
    		lblStrengthText.setForeground(Color.RED);
    		return;
    	}
    	
    	if(pwdNew.getPassword().length<7)
    	{
    		// Passwords too short
    		lblStrengthText.setText(I18n.getText("admin.passwordTooShort"));
    		lblStrengthText.setForeground(Color.RED);
    		return;
    	}
    	else
    	{
    		// Passwords match and are long enough
	    	lblStrengthText.setText(I18n.getText("admin.passwordMeetsRequirements"));
			lblStrengthText.setForeground(Color.GREEN);
			
			// Enable OK button
			if(pwdOld.getPassword().length>1)
			{
				btnOk.setEnabled(true);
				return;
			}
	  	
    	}
    
    	btnOk.setEnabled(false); 
    	return;
    	
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFields = new javax.swing.JPanel();
        pwdVerify = new javax.swing.JPasswordField();
        lblVerify = new javax.swing.JLabel();
        lblOld = new javax.swing.JLabel();
        pwdOld = new javax.swing.JPasswordField();
        lblIcon = new javax.swing.JLabel();
        lblNew = new javax.swing.JLabel();
        pwdNew = new javax.swing.JPasswordField();
        lblUserText = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        panelButtons = new javax.swing.JPanel();
        lblStrengthText = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        sep = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Set Password");
        setResizable(false);

        lblVerify.setText("Verify:");

        lblOld.setText("Old password:");

        lblIcon.setFocusable(false);
        lblIcon.setPreferredSize(new java.awt.Dimension(64, 64));

        lblNew.setText("New password:");

        lblUserText.setText("aps03pwb");
        lblUserText.setFocusable(false);

        lblUser.setText("User:");

        org.jdesktop.layout.GroupLayout panelFieldsLayout = new org.jdesktop.layout.GroupLayout(panelFields);
        panelFields.setLayout(panelFieldsLayout);
        panelFieldsLayout.setHorizontalGroup(
            panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelFieldsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblOld)
                    .add(lblNew)
                    .add(lblVerify)
                    .add(lblUser))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblUserText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pwdNew, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .add(pwdOld, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pwdVerify, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .add(17, 17, 17)
                .add(lblIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelFieldsLayout.setVerticalGroup(
            panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelFieldsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUser)
                    .add(lblUserText))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblOld)
                    .add(pwdOld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNew)
                    .add(pwdNew, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFieldsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblVerify)
                    .add(pwdVerify, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelFieldsLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(9, 9, 9))
        );

        lblStrengthText.setText("Password meets systems requirements");

        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(null);

        btnOk.setText("Ok");
        btnOk.setMaximumSize(new java.awt.Dimension(86, 29));
        btnOk.setMinimumSize(new java.awt.Dimension(86, 29));
        btnOk.setPreferredSize(null);

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblStrengthText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblStrengthText)))
        );

        sep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sep.setMaximumSize(new java.awt.Dimension(9999, 2));
        sep.setMinimumSize(new java.awt.Dimension(30, 2));
        sep.setPreferredSize(new java.awt.Dimension(50, 2));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(7, 7, 7)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panelFields, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(sep, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(9, 9, 9)
                .add(panelFields, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sep, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Instantiate and display dialog
     */
    public static void loadDialog() {
        SetPasswordUI dialog = new SetPasswordUI(new javax.swing.JFrame());
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

	public void keyReleased(KeyEvent e) {
		Object src = e.getSource();
		if( src==pwdNew || src==pwdVerify || src==pwdOld) setStrengthDescription();
	} 
	public void keyPressed(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JLabel lblIcon;
    protected javax.swing.JLabel lblNew;
    protected javax.swing.JLabel lblOld;
    protected javax.swing.JLabel lblStrengthText;
    protected javax.swing.JLabel lblUser;
    protected javax.swing.JLabel lblUserText;
    protected javax.swing.JLabel lblVerify;
    protected javax.swing.JPanel panelButtons;
    protected javax.swing.JPanel panelFields;
    protected javax.swing.JPasswordField pwdNew;
    protected javax.swing.JPasswordField pwdOld;
    protected javax.swing.JPasswordField pwdVerify;
    protected javax.swing.JSeparator sep;
    // End of variables declaration//GEN-END:variables

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("hello");
	}


    
}
