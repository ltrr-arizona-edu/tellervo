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
	private boolean cancelled = true;
	
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
		
		setResizable(false);

		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Corina Login");
		
		GridBagConstraints ogbc = new GridBagConstraints();

		username = new JTextField();
		username.setColumns(12);
		
		password = new JPasswordField();
		password.setColumns(12);
		
		JLabel lockIcon = new JLabel(Builder.getIcon("lock.png", 128));
		lockIcon.setBorder(BorderFactory.createEtchedBorder());
		
		
		ogbc.gridx = 0;
		ogbc.gridy = 0;
		ogbc.insets = new Insets(0, 0, 0, 20);
		ogbc.anchor = GridBagConstraints.NORTHWEST;		
		getContentPane().add(lockIcon, ogbc);
		ogbc.gridx++;
		
		// create an 'inside panel'
		JPanel insidePanel = new JPanel(new GridBagLayout());
		JLabel tmp;
		GridBagConstraints igbc = new GridBagConstraints();

		igbc.gridx = 0;
		igbc.gridy = 0;
		
		// title for login
		tmp = new JLabel("You must log in for access.");
		igbc.anchor = GridBagConstraints.CENTER;
		igbc.insets = new Insets(15, 0, 0, 0);
		igbc.gridwidth = 2;
		
		insidePanel.add(tmp, igbc);
		
		//igbc.gridy++;
		//igbc.insets = new Insets(5, 0, 10, 0);
		//subtitle = new JLabel("Please provide your credentials.");
		//insidePanel.add(subtitle, igbc);
		
		igbc.anchor = GridBagConstraints.WEST;
		igbc.gridwidth = 1;

		// username label and field
		igbc.gridx = 0;
		igbc.gridy++;
		igbc.insets = new Insets(8, 10, 0, 0);
		tmp = new JLabel(I18n.getText("login.username"));
		tmp.setLabelFor(username);
		
		igbc.insets = new Insets(8, 10, 0, 0);
		insidePanel.add(tmp, igbc);
		
		igbc.ipady = 0;
		igbc.gridx++;
		insidePanel.add(username, igbc);

		// password label and field
		igbc.gridx = 0;
		igbc.gridy++;
		
		tmp = new JLabel(I18n.getText("login.password"));
		tmp.setLabelFor(password);
		
		igbc.ipady = 0;
		igbc.ipadx = 0;
		insidePanel.add(tmp, igbc);
		
		igbc.ipady = 0;
		igbc.gridx++;
		insidePanel.add(password, igbc);
		
		
		// checkboxes
		igbc.gridwidth = 2;
		igbc.insets = new Insets(10, 50, 0, 0);
		igbc.gridx = 0;
		
		igbc.gridy++;
		rememberUsername = new JCheckBox("Remember my username");
		insidePanel.add(rememberUsername, igbc);
		
		igbc.gridy++;
		igbc.insets = new Insets(0, 50, 0, 0);
		rememberPassword = new JCheckBox("Remember my password");
		insidePanel.add(rememberPassword, igbc);
		
		igbc.gridy++;		
		autoLogin = new JCheckBox("Log in automatically");
		insidePanel.add(autoLogin, igbc);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		final JDialog glue = this;
		loginButton = new JButton("Log in");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				performAuthentication(0);
			}
		});
		buttonPanel.add(loginButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				glue.dispose();
			}
		});
		buttonPanel.add(cancelButton);
		
		// TODO: Implement offline mode
		JButton button;
		button = new JButton("Work offline");
		button.setEnabled(false);
		buttonPanel.add(button);

		igbc.gridx = 0;
		igbc.gridy++;
		igbc.gridwidth = 3;
		igbc.anchor = GridBagConstraints.WEST;
		igbc.insets = new Insets(10, 0, 0, 0);
		
		insidePanel.add(buttonPanel, igbc);

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
