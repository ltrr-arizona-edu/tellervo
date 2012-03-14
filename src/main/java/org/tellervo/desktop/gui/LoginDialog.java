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
 ******************************************************************************/
package org.tellervo.desktop.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel.NetworkStatus;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.WSIServerDetails;
import org.tellervo.desktop.wsi.tellervo.WebInterfaceCode;
import org.tellervo.desktop.wsi.tellervo.WebInterfaceException;
import org.tellervo.desktop.wsi.tellervo.resources.AuthenticateResource;


public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField username;
	private JPasswordField password;
	private JTextField serverUrl;
	private JCheckBox rememberUsername;
	private JCheckBox rememberPassword;
	private JCheckBox autoLogin;
	private JToggleButton wsurlLock;
	private JLabel subtitle = new JLabel();	
	private JButton loginButton;
	private JButton cancelButton;
	private JLabel lblLoginTo;
	private JLabel lockIcon;
	private JLabel lblTitle;
	private boolean cancelled = true;
	private boolean ignoreSavedInfo = false;
	private LoginFocusTraversalPolicy focusPolicy;
	
	public LoginDialog(Frame frame) {
		super(frame, true);
		initialize();
	}

	public LoginDialog(Dialog dialog) {
		super(dialog, true);
		initialize();
	}
	
	public LoginDialog() {
		super((Frame)null, true);
		initialize();
	}
	
	/**
	 * Create the dialog
	 */
	private void initialize() {
		
		setTitle(I18n.getText("login.Authentication"));
		setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 502, 277);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{122, 77, 16, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 26, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			lblTitle = new JLabel(I18n.getText("login.requestLogin"));
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblNewLabel.gridwidth = 2;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.gridx = 2;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblTitle, gbc_lblNewLabel);
		}
		{
			lockIcon = new JLabel(Builder.getIcon("key.png", 128));
			GridBagConstraints gbc_lockIcon = new GridBagConstraints();
			gbc_lockIcon.insets = new Insets(0, 0, 0, 5);
			gbc_lockIcon.anchor = GridBagConstraints.NORTHWEST;
			gbc_lockIcon.gridheight = 6;
			gbc_lockIcon.gridx = 0;
			gbc_lockIcon.gridy = 1;
			contentPanel.add(lockIcon, gbc_lockIcon);
		}
		{
			JLabel lblUserName = new JLabel(I18n.getText("login.username")+":");
			GridBagConstraints gbc_lblUserName = new GridBagConstraints();
			gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
			gbc_lblUserName.anchor = GridBagConstraints.EAST;
			gbc_lblUserName.gridx = 1;
			gbc_lblUserName.gridy = 1;
			contentPanel.add(lblUserName, gbc_lblUserName);
		}
		{
			username = new JTextField();
			GridBagConstraints gbc_username = new GridBagConstraints();
			gbc_username.fill = GridBagConstraints.HORIZONTAL;
			gbc_username.gridwidth = 2;
			gbc_username.insets = new Insets(0, 0, 5, 0);
			gbc_username.gridx = 2;
			gbc_username.gridy = 1;
			contentPanel.add(username, gbc_username);
			username.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel(I18n.getText("login.password")+":");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_1.gridx = 1;
			gbc_lblNewLabel_1.gridy = 2;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			password = new JPasswordField();
			GridBagConstraints gbc_password = new GridBagConstraints();
			gbc_password.gridwidth = 2;
			gbc_password.fill = GridBagConstraints.HORIZONTAL;
			gbc_password.insets = new Insets(0, 0, 5, 0);
			gbc_password.gridx = 2;
			gbc_password.gridy = 2;
			contentPanel.add(password, gbc_password);
		}
		{
			lblLoginTo = new JLabel(I18n.getText("login.serverurl"));
			GridBagConstraints gbc_lblLoginTo = new GridBagConstraints();
			gbc_lblLoginTo.insets = new Insets(0, 0, 5, 5);
			gbc_lblLoginTo.anchor = GridBagConstraints.EAST;
			gbc_lblLoginTo.gridx = 1;
			gbc_lblLoginTo.gridy = 3;
			contentPanel.add(lblLoginTo, gbc_lblLoginTo);
		}
		{
			final JDialog glue = this;
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				loginButton = new JButton(I18n.getText("login"));
				loginButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						performAuthentication(0);
					}
				});
				buttonPane.add(loginButton);
				getRootPane().setDefaultButton(loginButton);
			}
			{
				cancelButton = new JButton(I18n.getText("general.cancel"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						glue.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		{
			serverUrl = new JTextField();
			GridBagConstraints gbc_serverUrl = new GridBagConstraints();
			gbc_serverUrl.fill = GridBagConstraints.HORIZONTAL;
			gbc_serverUrl.insets = new Insets(0, 0, 5, 5);
			gbc_serverUrl.gridx = 2;
			gbc_serverUrl.gridy = 3;
			contentPanel.add(serverUrl, gbc_serverUrl);
			serverUrl.setColumns(10);
			new TextComponentWrapper(serverUrl, "tellervo.webservice.url", null);
		}
		{
			wsurlLock = new JToggleButton("");
			wsurlLock.setSize(16,16);
			GridBagConstraints gbc_btnUnlockUrl = new GridBagConstraints();
			gbc_btnUnlockUrl.anchor = GridBagConstraints.WEST;
			gbc_btnUnlockUrl.insets = new Insets(0, 0, 5, 0);
			gbc_btnUnlockUrl.gridx = 3;
			gbc_btnUnlockUrl.gridy = 3;
			contentPanel.add(wsurlLock, gbc_btnUnlockUrl);
			
			wsurlLock.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent ev) {
					setURLEnabled(!wsurlLock.isSelected());	
				}
				
			});
			wsurlLock.setSelected(true);
			wsurlLock.setFocusable(false);
			wsurlLock.setContentAreaFilled(false);
			wsurlLock.setBorderPainted(false);
			wsurlLock.setContentAreaFilled(false);
			
			// Lock URL if its already filled
			if(serverUrl.getText().trim().length()>0)
			{
				setURLEnabled(false);
			}
			else
			{
				setURLEnabled(true);
			}
		}
		{
			rememberUsername = new JCheckBox(I18n.getText("login.rememberMyUsername"));
			GridBagConstraints gbc_rememberUsername = new GridBagConstraints();
			gbc_rememberUsername.gridwidth = 2;
			gbc_rememberUsername.anchor = GridBagConstraints.WEST;
			gbc_rememberUsername.insets = new Insets(0, 0, 5, 0);
			gbc_rememberUsername.gridx = 2;
			gbc_rememberUsername.gridy = 4;
			contentPanel.add(rememberUsername, gbc_rememberUsername);
		}
		{
			rememberPassword = new JCheckBox(I18n.getText("login.rememberMyPassword"));
			GridBagConstraints gbc_rememberPassword = new GridBagConstraints();
			gbc_rememberPassword.gridwidth = 2;
			gbc_rememberPassword.anchor = GridBagConstraints.WEST;
			gbc_rememberPassword.insets = new Insets(0, 0, 5, 0);
			gbc_rememberPassword.gridx = 2;
			gbc_rememberPassword.gridy = 5;
			contentPanel.add(rememberPassword, gbc_rememberPassword);
		}
		{
			autoLogin = new JCheckBox(I18n.getText("login.automatically"));
			GridBagConstraints gbc_autoLogin = new GridBagConstraints();
			gbc_autoLogin.gridwidth = 2;
			gbc_autoLogin.anchor = GridBagConstraints.WEST;
			gbc_autoLogin.gridx = 2;
			gbc_autoLogin.gridy = 6;
			contentPanel.add(autoLogin, gbc_autoLogin);
		}

		
		
		// deal with checkbox mnemonic
		ActionListener checkboxListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				checkCheckboxes();
			}
		};
		rememberUsername.addActionListener(checkboxListener);
		rememberPassword.addActionListener(checkboxListener);
		autoLogin.addActionListener(checkboxListener);
		
		// auto-select the entire box for password/username when we select it
		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent fe) {
				Component c = fe.getComponent();
				
				if(c instanceof JTextField || c instanceof JPasswordField)
					((JTextField)c).selectAll();
			}
		};
		
		username.addFocusListener(focusListener);
		password.addFocusListener(focusListener);
		
		ArrayList<Component> order = new ArrayList<Component>(7);
		
		order.add(username);
		order.add(password);
		order.add(loginButton);
		
		focusPolicy = new LoginFocusTraversalPolicy(order);
		setFocusTraversalPolicy(focusPolicy);		
		
		// finally, make it so when we press enter we choose to log in
		getRootPane().setDefaultButton(loginButton);
		setLocationRelativeTo(null);
		
		
		
	}
	
	/**
	 * Ensure our checkbox mnemonic makes sense.
	 */
	private void checkCheckboxes() {
		if(autoLogin.isSelected()) {
			rememberPassword.setSelected(true);
			rememberPassword.setEnabled(false);			
		}
		else {
			rememberUsername.setEnabled(true);
			rememberPassword.setEnabled(true);
		}
		
		if(rememberPassword.isSelected()) {
			rememberUsername.setSelected(true);
			rememberUsername.setEnabled(false);						
		} else {
			rememberUsername.setEnabled(true);
		}
	}
	
	/**
	 * Make the GUI minimalistic for confirming credentials
	 */
	public void setGuiForConfirmation()
	{
		this.autoLogin.setVisible(false);
		this.rememberPassword.setVisible(false);
		this.rememberUsername.setVisible(false);
		this.autoLogin.setSelected(true);
		this.serverUrl.setVisible(false);
		this.password.setText("");
		this.lblLoginTo.setVisible(false);
		this.loginButton.setText("Confirm");

		this.lblTitle.setText("Confirm credentials");
		this.ignoreSavedInfo = true;
		this.wsurlLock.setVisible(false);
		this.lblLoginTo.setVisible(false);
		pack();
	}
	
	/**
	 * Lock or unlock the WS URL field
	 * @param b
	 */
	private void setURLEnabled(Boolean b)
	{
		this.serverUrl.setEnabled(b);
		this.loginButton.setEnabled(!b);
		if(b)
		{
			this.wsurlLock.setIcon(Builder.getIcon("unlock.png", 16));
		}
		else
		{
			this.wsurlLock.setIcon(Builder.getIcon("lock.png", 16));
		}
		

	}
	
	public void setInstructionText(String instr)
	{
		this.lblTitle.setText(instr);
	}
	
	/**
	 * Programmayically set the username.  Useful for headless use.
	 * 
	 * @param usr
	 */
	public void setUsername(String usr)
	{
		username.setText(usr);
	}
	
	/**
	 * Programmatically set the password. Useful for headless use.
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd)
	{
		password.setText(pwd);
	}
	
	private void loadSettings() {
		
		// Do not help the user if ignoreSavedInfo is true as we are
		// forcing them to remember!
		if(ignoreSavedInfo) 
		{
			return;
		}
		
		
		// remember the username? load it.
		if(App.prefs.getBooleanPref(PrefKey.REMEMBER_USERNAME, true)) {
			rememberUsername.setSelected(true);
			username.setText(App.prefs.getPref(PrefKey.PERSONAL_DETAILS_USERNAME, ""));		
			focusPolicy.setDefaultComponent(password);
		} else {
			rememberUsername.setSelected(false);
		}
		
		// remember the password? load it.
		if(App.prefs.getBooleanPref(PrefKey.REMEMBER_PASSWORD, false)) {
			rememberPassword.setSelected(true);
			password.setText(decryptPassword(App.prefs.getPref(PrefKey.PERSONAL_DETAILS_PASSWORD, "").toCharArray()));
			focusPolicy.setDefaultComponent(loginButton);
		} else {
			rememberPassword.setSelected(false);
		}
		
		// auto login?
		if(App.prefs.getBooleanPref(PrefKey.AUTO_LOGIN, false)) {		
			autoLogin.setSelected(true);
		} else {
			autoLogin.setSelected(false);
		}
		
		checkCheckboxes();
	}
	
	public static String encryptPassword(char[] in) {
		StringBuffer out = new StringBuffer();

		for(int i = 0; i < in.length; i++) {
			String v = Integer.toHexString(in[i]);
			
			if(v.length() < 2)
				out.append("0");
			out.append(v);
		}

		return out.toString();
	}
	
	private String decryptPassword(char[] in) {
		// sanity check.
		if(in.length == 0 || in.length % 2 != 0)
			return new String("");

		StringBuffer out = new StringBuffer();
		for(int i = 0; i < in.length; i += 2) {
			char inv[] = { in[i], in[i+1] };
			try {
				int outv = Integer.parseInt(new String(inv), 16);
				
				out.append((char)outv);
			} catch (NumberFormatException nfe) {
				return new String("");
			}
		}
		return out.toString();
	}

	
	private void saveSettings() {
		
		// Don't make any changes if we're ignoring saved info
		if (this.ignoreSavedInfo) return;
		
		if(rememberUsername.isSelected()) {
			App.prefs.setBooleanPref(PrefKey.REMEMBER_USERNAME, true);
			App.prefs.setPref(PrefKey.PERSONAL_DETAILS_USERNAME, username.getText());
		}
		else {
			App.prefs.removePref(PrefKey.REMEMBER_USERNAME);
			App.prefs.removePref(PrefKey.PERSONAL_DETAILS_USERNAME);
		}
		
		if(rememberPassword.isSelected()) {
			App.prefs.setBooleanPref(PrefKey.REMEMBER_PASSWORD, true);
			App.prefs.setPref(PrefKey.PERSONAL_DETAILS_PASSWORD, encryptPassword(password.getPassword()));
		} else {
			App.prefs.removePref(PrefKey.REMEMBER_PASSWORD);
			App.prefs.removePref(PrefKey.PERSONAL_DETAILS_PASSWORD);
		}

		if(autoLogin.isSelected()) {
			App.prefs.setBooleanPref(PrefKey.AUTO_LOGIN, true);
		} else {
			App.prefs.removePref(PrefKey.AUTO_LOGIN);
		}
	}
	
	private void placeCursor() {
		if(username.getText().length() == 0) {
			username.requestFocus();
		} else if(password.getPassword().length == 0) {
			password.requestFocus();
		} else {
			password.requestFocus();
			password.selectAll();
		}
	}

	public String getUsername() {
		return username.getText();
	}

	public String getPassword() {
		return new String(password.getPassword());
	}

	/**
	 * Do the actual server authentication
	 * Danger! this runs in the event thread!
	 */
	private AuthenticateResource authenticator;
	private SyncTaskDialog authenticationNotifier;
	private void performAuthentication(final int recursionLevel) {
		final JDialog glue = this;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		
		// make sure that we're not recursing way too much
		if(recursionLevel > 5) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(glue.isVisible() ? glue : null,
					"Error: Too much recursion. Is there a problem with the server?",
				    "Could not authenticate",
				    JOptionPane.ERROR_MESSAGE);
			
			enableDialogButtons(true);
			
			if(authenticationNotifier != null) {
				authenticationNotifier.setSuccess(false);
				authenticationNotifier.stop();
			}
			
			// abort!
			return;
		}
		
		// Check that the server is valid
		WSIServerDetails serverDetails = new WSIServerDetails();
		
			
		if(!serverDetails.isServerValid())
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			Alert.error(this, "Server connection error", "Error connecting to server:\n" + serverDetails.getErrorMessage());
			return;
					
		}
				
		// first off, we're busy now.
		enableDialogButtons(false);
		
		if(serverNonce == null) {
			// ok, so we're trying to log in pre-emptively. First, we need a nonce from the server.
			authenticator = new AuthenticateResource();
			
			authenticator.addResourceEventListener(new ResourceEventListener() {
				public void resourceChanged(ResourceEvent re) {
					if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
						setCursor(Cursor.getDefaultCursor());
						// ok, so we have a nonce
						setNonce(authenticator.getServerNonce(), authenticator.getServerNonceSeq());
						
						// start this whole thing over again...
						performAuthentication(recursionLevel + 1);
					}
					else if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
						Exception e = re.getAttachedException();
						setCursor(Cursor.getDefaultCursor());
						
						// failure. what type?
						JOptionPane.showMessageDialog(glue.isVisible() ? glue : null,
								"Error authenticating: Query failed",
							    "Could not authenticate",
							    JOptionPane.ERROR_MESSAGE);
						enableDialogButtons(true);
						
						if(authenticationNotifier != null) {
							authenticationNotifier.setSuccess(false);
							authenticationNotifier.stop();
						}
					}
				}			
			});
			
			// start the query!
			authenticator.query();
		}
		else {
			// ok, so we have a nonce and we're trying to actually log in
			authenticator = new AuthenticateResource(getUsername(), getPassword(), serverNonce, serverNonceSeq);
			
			authenticator.addResourceEventListener(new ResourceEventListener() {
				public void resourceChanged(ResourceEvent re) {
					if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
						// yay! we've authenticated!
						setCursor(Cursor.getDefaultCursor());
						
						App.currentUser = authenticator.getAuthenticatedUser();
						
						if(authenticationNotifier != null) {
							authenticationNotifier.setSuccess(true);
							authenticationNotifier.stop();
						}
						
						cancelled = false;
						dispose();
					}
					else if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
						setCursor(Cursor.getDefaultCursor());
						Exception e = re.getAttachedException();
						
						// bad server nonce?
						if(e instanceof WebInterfaceException && 
								((WebInterfaceException)e).getMessageCode() == 
									WebInterfaceCode.BAD_SERVER_NONCE) {
							
							setNonce(null, null);
							performAuthentication(recursionLevel + 1);
							return;
						}
						// Standard authentication error
						else if(e instanceof WebInterfaceException && 
								((WebInterfaceException)e).getMessageCode() == 
									WebInterfaceCode.AUTHENTICATION_FAILED) {
							JOptionPane.showMessageDialog(glue.isVisible() ? glue : null,
									"Incorrect username or password.\n"
									+"Please check and try again.",
									"Invalid credentials",
									JOptionPane.ERROR_MESSAGE);
							enableDialogButtons(true);
						}
						else
						{
							// failure. what type?
							JOptionPane.showMessageDialog(glue.isVisible() ? glue : null,
									"Error authenticating",
								    "Could not authenticate",
								    JOptionPane.ERROR_MESSAGE);
							enableDialogButtons(true);
						}
						
						if(authenticationNotifier != null) {
							authenticationNotifier.setSuccess(false);
							authenticationNotifier.stop();
						}
					}
				}			
			});
			
			// start the query!
			authenticator.query();
		}
	}

	private void enableDialogButtons(boolean enable) {
		loginButton.setEnabled(enable);
		//cancelButton.setEnabled(enable);
	}

	/**
	 * Used if we're actually doing a login!
	 * @param nonce
	 */
	public void setNonce(String nonce, String seq) {
		serverNonce = nonce;
		serverNonceSeq = seq;
	}
	
	private String serverNonce;
	private String serverNonceSeq;

	
	public void doLogin(String subtitle, boolean forced) throws UserCancelledException {
		if(subtitle != null) {
			this.subtitle.setText(subtitle);
			pack();
		}
		
		// If it's forced, make our subtitle menacing and red!
		if(forced) {
			this.subtitle.setForeground(Color.RED);
			this.subtitle.setFont(this.subtitle.getFont().deriveFont(Font.BOLD));
		}
		
		loadSettings();
		placeCursor();
		
		// not enough information, or no auto login? show the dialog.
		if(forced || username.getText().length() == 0 || password.getPassword().length == 0 ||
				!autoLogin.isSelected()) {
			setVisible(true);
		} else {
			
			// this is more complicated than it seems
			// we essentially want to wait until we succeed or fail
			authenticationNotifier = new SyncTaskDialog(null);

			performAuthentication(0);
			
			authenticationNotifier.start();
			
			if(!authenticationNotifier.isSuccess()) {
				authenticationNotifier = null;
				setVisible(true);
			}
		}
		
		if(cancelled)
			throw new UserCancelledException();
		else
			App.appmodel.setNetworkStatus(NetworkStatus.ONLINE);
			
		saveSettings();		
	}
	
	// ugly hack: while we're waiting for stuff to finish, pump events to make UI responsive.
	private class SyncTaskDialog {
		protected JDialog dialog;
		private boolean success = false;
		
		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			
			if(success)
			{
				App.appmodel.setNetworkStatus(NetworkStatus.ONLINE);
			}
			else
			{
				App.appmodel.setNetworkStatus(NetworkStatus.OFFLINE);
			}

			
			this.success = success;
		}

		public SyncTaskDialog(JDialog parent) {
			if(parent == null)
				dialog = new JDialog((Frame) null, true);
			else
				dialog = new JDialog(parent, true);
			dialog.setUndecorated(true);
			dialog.setBounds(0, 0, 0, 0);
		}
		
		public void start() {
			dialog.setVisible(true);
		}
		
		public void stop() {
			dialog.setVisible(false);
		}
	}

}
