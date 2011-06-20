/**
 * 
 */
package edu.cornell.dendro.corina.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * @author lucasm
 *
 */
@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

	private JTextField username;
	private JPasswordField password;
	private JCheckBox rememberUsername;
	private JCheckBox rememberPassword;
	private JCheckBox autoLogin;
	private JLabel subtitle;	
	private JButton loginButton;

	
	public LoginPanel() {
		super();
	}

	/**
	 * Create the dialog
	 */
	public void createPanel() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints ogbc = new GridBagConstraints();

		username = new JTextField();
		username.setColumns(16);
		
		password = new JPasswordField();
		password.setColumns(16);
		
		JLabel lockIcon = new JLabel(Builder.getIcon("lock.png", 128));
		
		ogbc.gridx = 0;
		ogbc.gridy = 0;
		ogbc.insets = new Insets(0, 0, 0, 50);
		ogbc.anchor = GridBagConstraints.NORTHWEST;		
		add(lockIcon, ogbc);
		ogbc.gridx++;
		
		// create an 'inside panel'
		JPanel insidePanel = new JPanel(new GridBagLayout());
		JLabel tmp;
		GridBagConstraints igbc = new GridBagConstraints();
		
		insidePanel.setOpaque(false);

		igbc.gridx = 0;
		igbc.gridy = 0;
		
		// title for login
		igbc.anchor = GridBagConstraints.CENTER;
		igbc.gridwidth = 2;
		igbc.insets = new Insets(5, 0, 10, 0);
		subtitle = new JLabel(I18n.getText("login.provideCredentials"));
		insidePanel.add(subtitle, igbc);
		
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
		igbc.insets = new Insets(0, 0, 0, 0);
		igbc.gridx = 0;
		igbc.anchor = GridBagConstraints.CENTER;
		igbc.gridy++;
		
		rememberUsername = new JCheckBox(I18n.getText("login.rememberInfo"));
		rememberUsername.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
		insidePanel.add(rememberUsername, igbc);
		
		rememberPassword = new JCheckBox("password");
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setOpaque(false);
		
		JButton button;
		loginButton = new JButton("Log in");
		/*
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cancelled = false;
				glue.dispose();
			}
		});
		*/
		buttonPanel.add(loginButton);
		
		button = new JButton(I18n.getText("generic.cancel"));
		/*
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				glue.dispose();
			}
		});
		buttonPanel.add(button);
		*/
		
		// TODO: Implement offline mode
		button = new JButton(I18n.getText("login.continueOffline"));
		button.setEnabled(false);
		buttonPanel.add(button);

		igbc.gridx = 0;
		igbc.gridy++;
		igbc.gridwidth = 2;
		igbc.anchor = GridBagConstraints.EAST;
		igbc.insets = new Insets(0, 0, 0, 0);
		
		insidePanel.add(buttonPanel, igbc);

		// finish up the UI part
		add(insidePanel, ogbc);
					
		// deal with checkbox mnemonic
		ActionListener checkboxListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				checkCheckboxes();
			}
		};
		rememberUsername.addActionListener(checkboxListener);
		rememberPassword.addActionListener(checkboxListener);
		//autoLogin.addActionListener(checkboxListener);
		
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
		//getRootPane().setDefaultButton(loginButton);
	}

	/**
	 * Ensure our checkbox mnemonic makes sense.
	 */
	private void checkCheckboxes() {
		{
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
	
	@SuppressWarnings("unused")
	private void loadSettings() {
		String tmp;
		
		// remember the username? load it.
		if((tmp = App.prefs.getPref("corina.login.remember_username")) != null &&
				Boolean.valueOf(tmp) == true) {
			
			rememberUsername.setSelected(true);
			username.setText(App.prefs.getPref("corina.login.username", ""));			
		} else {
			rememberUsername.setSelected(false);
		}
		
		// remember the password? load it.
		if((tmp = App.prefs.getPref("corina.login.remember_password")) != null &&
				Boolean.valueOf(tmp) == true) {
			
			rememberPassword.setSelected(true);
			password.setText(decryptPassword(App.prefs.getPref("corina.login.password", "").toCharArray()));			
		} else {
			rememberPassword.setSelected(false);
		}
		
		// auto login?
		if((tmp = App.prefs.getPref("corina.login.auto_login")) != null &&
				Boolean.valueOf(tmp) == true) {	
			
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
	
	@SuppressWarnings("unused")
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
	
	@SuppressWarnings("unused")
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
	
	/*
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
			cancelled = false;
			dispose();
		}
		
		if(cancelled)
			throw new UserCancelledException();
		
		saveSettings();		
	}
	*/
}
