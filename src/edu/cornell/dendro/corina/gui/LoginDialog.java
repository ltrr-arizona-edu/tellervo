package edu.cornell.dendro.corina.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.cornell.dendro.corina.prefs.wrappers.TextComponentWrapper;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.wsi.corina.resources.AuthenticateResource;
import edu.cornell.dendro.corina.wsi.ResourceEvent;
import edu.cornell.dendro.corina.wsi.ResourceEventListener;
import edu.cornell.dendro.corina.wsi.corina.WebInterfaceCode;
import edu.cornell.dendro.corina.wsi.corina.WebInterfaceException;
import edu.cornell.dendro.corina.wsi.corina.WebPermissionsException;
import edu.cornell.dendro.corina.core.App;


public class LoginDialog extends JDialog {

	private JTextField username;
	private JPasswordField password;
	private JTextField serverUrl;
	private JCheckBox rememberUsername;
	private JCheckBox rememberPassword;
	private JCheckBox autoLogin;
	private JLabel subtitle;	
	private JButton loginButton;
	private JButton cancelButton;
	private JButton btnWorkOffline;
	private JLabel lblLoginTo = new JLabel(I18n.getText("login.serverurl"));
	private JLabel lockIcon;
	private JLabel lblTitle;
	private boolean cancelled = true;
	private boolean ignoreSavedInfo = false;
	
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
	 * Make the GUI minimalistic for confirming credentials
	 */
	public void setGuiForConfirmation()
	{
		this.autoLogin.setVisible(false);
		this.rememberPassword.setVisible(false);
		this.rememberUsername.setVisible(false);
		this.serverUrl.setVisible(false);
		this.password.setText("");
		this.lblLoginTo.setVisible(false);
		this.loginButton.setText("Confirm");
		this.btnWorkOffline.setVisible(false);
		this.lblTitle.setText("Confirm credentials");
		this.ignoreSavedInfo = true;
		pack();
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
	
	/**
	 * Create the dialog
	 */
	private void initialize() {
		
		setResizable(false);

		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(I18n.getText("login.Authentication"));
        setIconImage(Builder.getApplicationIcon());

		GridBagConstraints ogbc = new GridBagConstraints();

		username = new JTextField();
		username.setColumns(20);
		
		password = new JPasswordField();
		password.setColumns(20);
		
		serverUrl = new JTextField();
		serverUrl.setColumns(20);
		serverUrl.setEditable(true);
		new TextComponentWrapper(serverUrl, "corina.webservice.url", null);
		serverUrl.setEnabled(false);
		
		
		lockIcon = new JLabel(Builder.getIcon("lock.png", 128));
		//lockIcon.setBorder(BorderFactory.createEtchedBorder());
	
		
		ogbc.gridx = 0;
		ogbc.gridy = 0;
		ogbc.insets = new Insets(20, 0, 0, 0);
		ogbc.anchor = GridBagConstraints.NORTHWEST;		
		getContentPane().add(lockIcon, ogbc);
		ogbc.gridx++;
		
		
		
		// create an 'inside panel'
		JPanel insidePanel = new JPanel(new GridBagLayout());
		JLabel tmp;
		GridBagConstraints igbc = new GridBagConstraints();

		igbc.gridx = 1;
		igbc.gridy = 0;
		
		// title for login
		lblTitle = new JLabel(I18n.getText("login.requestLogin"));
		igbc.anchor = GridBagConstraints.WEST;
		igbc.insets = new Insets(0, 10, 0, 80);
		igbc.gridwidth = 2;
		
		insidePanel.add(lblTitle, igbc);
		
		//igbc.gridy++;
		//igbc.insets = new Insets(5, 0, 10, 0);
		//subtitle = new JLabel("Please provide your credentials.");
		//insidePanel.add(subtitle, igbc);
		subtitle = new JLabel();
		
		igbc.anchor = GridBagConstraints.WEST;
		igbc.gridwidth = 1;

		// username label and field
		igbc.gridx = 0;
		igbc.gridy++;
		igbc.insets = new Insets(12, 10, 0, 0);
		tmp = new JLabel(I18n.getText("login.username")+":");
		tmp.setLabelFor(username);
		
		igbc.insets = new Insets(12, 10, 0, 20);
		insidePanel.add(tmp, igbc);
		
		igbc.ipady = 0;
		igbc.gridx++;
		insidePanel.add(username, igbc);

		// password label and field
		igbc.gridx = 0;
		igbc.gridy++;
		
		tmp = new JLabel(I18n.getText("login.password")+":");
		tmp.setLabelFor(password);
		
		igbc.ipady = 0;
		igbc.ipadx = 0;
		insidePanel.add(tmp, igbc);
		
		igbc.ipady = 0;
		igbc.gridx++;
		insidePanel.add(password, igbc);
		
		// server label and field
		igbc.gridx = 0;
		igbc.gridy++;	
		
		
		lblLoginTo.setLabelFor(serverUrl);
		
		igbc.ipady = 0;
		igbc.ipadx = 0;
		insidePanel.add(lblLoginTo, igbc);		
		
		igbc.ipady = 0;
		igbc.gridx++;
		insidePanel.add(serverUrl, igbc);		
		
		// checkboxes
		igbc.gridwidth = 2;
		igbc.anchor = GridBagConstraints.WEST;
		igbc.insets = new Insets(10, 8, 0, 0);
		igbc.gridx = 1;
		
		igbc.gridy++;
		rememberUsername = new JCheckBox(I18n.getText("login.rememberMyUsername"));
		insidePanel.add(rememberUsername, igbc);
		
		igbc.gridy++;
		igbc.insets = new Insets(0, 8, 0, 0);
		rememberPassword = new JCheckBox(I18n.getText("login.rememberMyPassword"));
		insidePanel.add(rememberPassword, igbc);
		
		igbc.gridy++;		
		autoLogin = new JCheckBox(I18n.getText("login.automatically"));
		insidePanel.add(autoLogin, igbc);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		final JDialog glue = this;
		loginButton = new JButton(I18n.getText("login"));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				performAuthentication(0);
			}
		});
		buttonPanel.add(loginButton);
		
		cancelButton = new JButton(I18n.getText("general.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				glue.dispose();
			}
		});
		buttonPanel.add(cancelButton);
		
		// TODO: Implement offline mode
		
		btnWorkOffline = new JButton(I18n.getText("login.workOffline"));
		btnWorkOffline.setEnabled(false);
		buttonPanel.add(btnWorkOffline);

		igbc.gridx = 0;
		igbc.gridy++;
		igbc.gridwidth = 2;
		igbc.anchor = GridBagConstraints.EAST;
		igbc.insets = new Insets(10, 0, 0, 0);
		
		insidePanel.add(buttonPanel, igbc);

		//insidePanel.setBorder(BorderFactory.createEtchedBorder());

		
		// finish up the UI part
		getContentPane().add(insidePanel, ogbc);
		
			
		pack();
		Center.center(this);
		
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
		
		// finally, make it so when we press enter we choose to log in
		getRootPane().setDefaultButton(loginButton);
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
	
	private void loadSettings() {
		
		// Do not help the user if ignoreSavedInfo is true as we are
		// forcing them to remember!
		if(ignoreSavedInfo) 
		{
			password.setText("");
			return;
		}
		
		
		// remember the username? load it.
		if(App.prefs.getBooleanPref("corina.login.remember_username", true)) {
			rememberUsername.setSelected(true);
			username.setText(App.prefs.getPref("corina.login.username", ""));			
		} else {
			rememberUsername.setSelected(false);
		}
		
		// remember the password? load it.
		if(App.prefs.getBooleanPref("corina.login.remember_password", false)) {
			rememberPassword.setSelected(true);
			password.setText(decryptPassword(App.prefs.getPref("corina.login.password", "").toCharArray()));			
		} else {
			rememberPassword.setSelected(false);
		}
		
		// auto login?
		if(App.prefs.getBooleanPref("corina.login.auto_login", false)) {		
			autoLogin.setSelected(true);
		} else {
			autoLogin.setSelected(false);
		}
		
		checkCheckboxes();
	}
	
	private String encryptPassword(char[] in) {
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
		if(rememberUsername.isSelected()) {
			App.prefs.setPref("corina.login.remember_username", "true");
			App.prefs.setPref("corina.login.username", username.getText());
		}
		else {
			App.prefs.removePref("corina.login.remember_username");
			App.prefs.removePref("corina.login.username");
		}
		
		if(rememberPassword.isSelected()) {
			App.prefs.setPref("corina.login.remember_password", "true");
			App.prefs.setPref("corina.login.password", encryptPassword(password.getPassword()));
		} else {
			App.prefs.removePref("corina.login.remember_password");
			App.prefs.removePref("corina.login.password");
		}

		if(autoLogin.isSelected()) {
			App.prefs.setPref("corina.login.auto_login", "true");
		} else {
			App.prefs.removePref("corina.login.auto_login");
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
		
		// make sure that we're not recursing way too much
		if(recursionLevel > 5) {
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
		
		// first off, we're busy now.
		enableDialogButtons(false);
		
		if(serverNonce == null) {
			// ok, so we're trying to log in pre-emptively. First, we need a nonce from the server.
			authenticator = new AuthenticateResource();
			
			authenticator.addResourceEventListener(new ResourceEventListener() {
				public void resourceChanged(ResourceEvent re) {
					if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
						// ok, so we have a nonce
						setNonce(authenticator.getServerNonce(), authenticator.getServerNonceSeq());
						
						// start this whole thing over again...
						performAuthentication(recursionLevel + 1);
					}
					else if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
						Exception e = re.getAttachedException();
												
						// failure. what type?
						JOptionPane.showMessageDialog(glue.isVisible() ? glue : null,
								"Error: " + e.toString(),
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
						if(authenticationNotifier != null) {
							authenticationNotifier.setSuccess(true);
							authenticationNotifier.stop();
						}
						
						cancelled = false;
						dispose();
					}
					else if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
						Exception e = re.getAttachedException();
						
						// bad server nonce?
						if(e instanceof WebInterfaceException && 
								((WebInterfaceException)e).getMessageCode() == 
									WebInterfaceCode.BAD_SERVER_NONCE) {
							
							setNonce(null, null);
							performAuthentication(recursionLevel + 1);
							return;
						}
						
						// failure. what type?
						JOptionPane.showMessageDialog(glue.isVisible() ? glue : null,
								"Error: " + e.toString(),
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
	}
	
	private void enableDialogButtons(boolean enable) {
		loginButton.setEnabled(enable);
		cancelButton.setEnabled(enable);
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
