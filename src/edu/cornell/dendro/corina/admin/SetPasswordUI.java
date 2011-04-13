package edu.cornell.dendro.corina.admin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;
import net.miginfocom.swing.MigLayout;

/**
 * Dialog for setting users own password, or for user with admin
 * privileges to set other users passwords
 *
 * @author  peterbrewer
 */
public class SetPasswordUI extends javax.swing.JDialog implements KeyListener{
    
	private static final long serialVersionUID = 6612655712307306825L;
	WSISecurityUser thisUser;
	Frame parent;
	
    /**
     * Create a new set password dialog 
     * @param parent
     * @wbp.parser.constructor
     */
    public SetPasswordUI(Frame parent) {
        super(parent, true);
        this.parent = parent;
        thisUser = App.currentUser;
        initComponents();
        setupGui(false);   
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
     * @param otherUser
     */
    public SetPasswordUI(java.awt.Frame parent, WSISecurityUser otherUser) {
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
   	    setLocationRelativeTo(null);
   	    setIconImage(Builder.getApplicationIcon());
   	    
    	if(asAdmin)
    	{
    		lblUser.setVisible(true);
        	lblUserText.setVisible(true);
        	this.lblOld.setText("Admin password");
        	this.lblUserText.setText(this.thisUser.getUsername());
    	}
    	else
    	{
    		lblUser.setVisible(false);
        	lblUserText.setVisible(false);
    	}

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
    			
    			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    			
    			if(isPasswordCorrect(pwdOld.getPassword()))
    			{
    				
    				Boolean success = setPassword(pwdNew.getPassword());
    				
    				if(success) 
    				{
    					setCursor(Cursor.getDefaultCursor());
    					closeDialog();
    				}
    				else
    				{
    					setCursor(Cursor.getDefaultCursor());
    					Alert.message("error", "Failed to set password");
    				}
    			}
    			else
    			{
					setCursor(Cursor.getDefaultCursor());
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
       
    private void closeDialog()
    {
    	this.dispose();
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
    	LoginDialog dlg = new LoginDialog(this);
    	dlg.setGuiForConfirmation();
    	dlg.setInstructionText("Confirm original credentials");
    	dlg.setUsername(thisUser.getUsername());
    	dlg.setPassword(new String(pwd));
    	dlg.setVisible(false);
    	try {
			dlg.doLogin(null, false);
		} catch (UserCancelledException e) {
			return false;
		}
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

    	// Set password to hash
    	MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			String pwd1 = new String(this.pwdNew.getPassword());
			digest.update(pwd1.getBytes());
	    	thisUser.setHashOfPassword(StringUtils.bytesToHex(digest.digest()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// associate a resource
    	SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, thisUser);
    	
    	
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		
		if(accdialog.isSuccessful())
		{
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
    public void initComponents() {

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

        GroupLayout gl_panelFields = new GroupLayout(panelFields);
        panelFields.setLayout(gl_panelFields);
        gl_panelFields.setHorizontalGroup(
            gl_panelFields.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_panelFields.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_panelFields.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblOld)
                    .addComponent(lblNew)
                    .addComponent(lblVerify)
                    .addComponent(lblUser))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_panelFields.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblUserText, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(pwdNew, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(pwdOld, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(pwdVerify, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        gl_panelFields.setVerticalGroup(
            gl_panelFields.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_panelFields.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_panelFields.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUser)
                    .addComponent(lblUserText))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_panelFields.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOld)
                    .addComponent(pwdOld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_panelFields.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNew)
                    .addComponent(pwdNew, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gl_panelFields.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVerify)
                    .addComponent(pwdVerify, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(GroupLayout.Alignment.TRAILING, gl_panelFields.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        lblStrengthText.setText("Password meets systems requirements");

        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(null);

        btnOk.setText("Ok");
        btnOk.setMaximumSize(new java.awt.Dimension(86, 29));
        btnOk.setMinimumSize(new java.awt.Dimension(86, 29));
        btnOk.setPreferredSize(null);

        GroupLayout gl_panelButtons = new GroupLayout(panelButtons);
        panelButtons.setLayout(gl_panelButtons);
        gl_panelButtons.setHorizontalGroup(
            gl_panelButtons.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, gl_panelButtons.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStrengthText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        gl_panelButtons.setVerticalGroup(
            gl_panelButtons.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_panelButtons.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gl_panelButtons.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStrengthText)))
        );

        sep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sep.setMaximumSize(new java.awt.Dimension(9999, 2));
        sep.setMinimumSize(new java.awt.Dimension(30, 2));
        sep.setPreferredSize(new java.awt.Dimension(50, 2));
        getContentPane().setLayout(new MigLayout("", "[473px]", "[136px][2px][37px]"));
        getContentPane().add(panelButtons, "cell 0 2,growx,aligny top");
        getContentPane().add(panelFields, "cell 0 0,alignx left,aligny top");
        getContentPane().add(sep, "cell 0 1,growx,aligny top");

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
    
}
