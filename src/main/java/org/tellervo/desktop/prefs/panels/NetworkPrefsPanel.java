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
package org.tellervo.desktop.prefs.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.RadioButtonWrapper;
import org.tellervo.desktop.prefs.wrappers.SpinnerWrapper;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.WSIServerDetails;
import org.tellervo.desktop.wsi.WSIServerDetails.WSIServerStatus;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;

import net.miginfocom.swing.MigLayout;

public class NetworkPrefsPanel extends AbstractPreferencesPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtWSURL;
	private JTextField txtHTTPProxyUrl;
	private JTextField txtHTTPSProxyUrl;
	private JRadioButton btnDefaultProxy;
	private JRadioButton btnNoProxy;
	private JRadioButton btnManualProxy;
	private JSpinner spnHTTPProxyPort;
	private JSpinner spnHTTPSProxyPort;
	private JLabel lblHTTPSPort;
	private JLabel lblHttpsProxy;
	private JLabel lblHTTPPort;
	private JLabel lblHttpProxy;
	private JCheckBox chkDisableWS;
	private JButton btnForceDictionaryReload;
	private String originalURL;
	private JPanel wsPanel;
	private JPanel proxyPanel;
	private MigLayout layout;
	private JButton btnTest;
	
	/**
	 * Create the panel.
	 */
	public NetworkPrefsPanel(final JDialog parent) {
		super(I18n.getText("preferences.network"), 
				"networksettings.png", 
				"Webservice network connection preferences",
				parent);
		
		layout = new MigLayout("", "[grow,fill]", "[][][grow]");
		
		setLayout(layout);
		
		wsPanel = new JPanel();
		wsPanel.setBorder(new TitledBorder(null, "Web service", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(wsPanel, "cell 0 0,grow, hidemode 2");
		wsPanel.setLayout(new MigLayout("", "[100px:100px:100px][grow]", "[][15px][][]"));
		
		chkDisableWS = new JCheckBox("Disable web service integration and work offline");
		wsPanel.add(chkDisableWS, "cell 1 0");
		chkDisableWS.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				pingComponents();
			}
			
		});
		
		JLabel lblWebservice = new JLabel("URL:");
		wsPanel.add(lblWebservice, "cell 0 1,alignx trailing,aligny center");
		
		txtWSURL = new JTextField();
		wsPanel.add(txtWSURL, "flowx,cell 1 1,growx");
		txtWSURL.setColumns(10);
		
		btnForceDictionaryReload = new JButton("Force Dictionary Reload");

		btnForceDictionaryReload.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				App.dictionary = new Dictionary();
				TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(parent, App.dictionary);
				App.dictionary.query();
				dlg.setVisible(true);
				
				if(!dlg.isSuccessful()) {
					// Dictionary reload failed
					new Bug(dlg.getFailException());
				}
			}
			
		});
		wsPanel.add(btnForceDictionaryReload, "cell 1 2,alignx right");
		
		btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				testWSConnection(true);
				
			}
			
		});
		
		wsPanel.add(btnTest, "cell 1 1");
		
		proxyPanel = new JPanel();
		proxyPanel.setBorder(new TitledBorder(null, "Network Connection", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(proxyPanel, "cell 0 1,grow, hidemode 2");
		proxyPanel.setLayout(new MigLayout("", "[100px:100px:100px][grow][][]", "[][][][][]"));
		
		btnDefaultProxy = new JRadioButton("Use system default proxy settings");
		proxyPanel.add(btnDefaultProxy, "cell 1 0 3 1");
		
		btnNoProxy = new JRadioButton("Direct connection");
		proxyPanel.add(btnNoProxy, "cell 1 1 3 1");
		
		btnManualProxy = new JRadioButton("Use manual proxy settings");
		proxyPanel.add(btnManualProxy, "cell 1 2 3 1");
		
		ButtonGroup group = new ButtonGroup();
		group.add(btnDefaultProxy);
		group.add(btnManualProxy);
		group.add(btnNoProxy);
				
		lblHttpProxy = new JLabel("HTTP Proxy:");
		proxyPanel.add(lblHttpProxy, "cell 0 3,alignx trailing");
		
		txtHTTPProxyUrl = new JTextField();
		proxyPanel.add(txtHTTPProxyUrl, "cell 1 3,growx");
		txtHTTPProxyUrl.setColumns(10);
		
		lblHTTPPort = new JLabel("Port:");
		proxyPanel.add(lblHTTPPort, "cell 2 3");
		
		spnHTTPProxyPort = new JSpinner();
		spnHTTPProxyPort.setModel(new SpinnerNumberModel(80, 0, 65535, 1));
		proxyPanel.add(spnHTTPProxyPort, "cell 3 3");
		
		lblHttpsProxy = new JLabel("HTTPS Proxy:");
		proxyPanel.add(lblHttpsProxy, "cell 0 4,alignx trailing");
		
		txtHTTPSProxyUrl = new JTextField();
		proxyPanel.add(txtHTTPSProxyUrl, "cell 1 4,growx");
		txtHTTPSProxyUrl.setColumns(10);
		
		lblHTTPSPort = new JLabel("Port:");
		proxyPanel.add(lblHTTPSPort, "cell 2 4");
		
		spnHTTPSProxyPort = new JSpinner();
		spnHTTPSProxyPort.setModel(new SpinnerNumberModel(80, 0, 65535, 1));
		proxyPanel.add(spnHTTPSProxyPort, "cell 3 4");

		linkToPrefs();
		pingComponents();
	}

	public void setBackgroundColor(Color col)
	{
		this.setBackground(col);
		this.wsPanel.setBackground(col);
		this.proxyPanel.setBackground(col);
	}
	
	public void setWebservicePanelVisible(Boolean b)
	{
		this.wsPanel.setVisible(b);
		
	}
	
	public void setProxyPanelVisible(Boolean b)
	{
		this.proxyPanel.setVisible(b);
	}
	
	public void setWebservicePanelToSimple(Boolean b)
	{
		btnForceDictionaryReload.setVisible(!b);
	}
	
	private void pingComponents()
	{
		txtWSURL.setEnabled(!chkDisableWS.isSelected());
		btnForceDictionaryReload.setEnabled(!chkDisableWS.isSelected());
	}
	
	public Boolean testWSConnection(Boolean feedbackOnSuccess)
	{
		WSIServerDetails serverDetails = new WSIServerDetails();
		
		if(serverDetails.getWSIServerStatus()==WSIServerStatus.NO_CONNECTION)
		{
			Alert.error("No network connection", 
			"You don't appear to have a network connection.  Please check your\n " +
			"network settings and try again.");
			return false;	
		}
		else if(!serverDetails.isServerValid())
		{
			Alert.error("Invalid Webservice URL", 
			"The URL you have entered is not a valid Tellervo Webservice");
			return false;
		}
		
		if(feedbackOnSuccess)
		{
			Alert.message(parent, "Success", "Webservice connection OK.");
		}
		
		return true;
	}
	
	private void linkToPrefs()
	{
		// networking - proxy
		btnDefaultProxy.setActionCommand("default");
		btnManualProxy.setActionCommand("manual");
		btnNoProxy.setActionCommand("direct");

		new TextComponentWrapper(txtHTTPProxyUrl, "tellervo.proxy.http", null);
		new SpinnerWrapper(spnHTTPProxyPort, "tellervo.proxy.http_port", 80);
		new TextComponentWrapper(txtHTTPSProxyUrl, "tellervo.proxy.https", null);
		new SpinnerWrapper(spnHTTPSProxyPort, "tellervo.proxy.https_port", 443);
		new RadioButtonWrapper(new JRadioButton[] { btnDefaultProxy, btnManualProxy, btnNoProxy }, 
				"tellervo.proxy.type", "default");
		new CheckBoxWrapper(chkDisableWS, "tellervo.webservice.disable", false );
	
		// manual proxy button behavior
		setEnableProxy(btnManualProxy.isSelected());
		btnManualProxy.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setEnableProxy(btnManualProxy.isSelected());
			}
		});
		
		// networking - server & smtp
		new TextComponentWrapper(txtWSURL, "tellervo.webservice.url", null);
		originalURL = txtWSURL.getText();
	}
	
	private void setEnableProxy(boolean isEnabled) {
		txtHTTPProxyUrl.setEnabled(isEnabled);
		txtHTTPSProxyUrl.setEnabled(isEnabled);
		spnHTTPProxyPort.setEnabled(isEnabled);
		spnHTTPSProxyPort.setEnabled(isEnabled);
		lblHttpsProxy.setEnabled(isEnabled);
		lblHttpProxy.setEnabled(isEnabled);
		lblHTTPPort.setEnabled(isEnabled);
		lblHTTPSPort.setEnabled(isEnabled);
	}
	
	public Boolean hasWSURLChanged()
	{
		if(!originalURL.equalsIgnoreCase(txtWSURL.getText()))
		{
			originalURL = txtWSURL.getText();
			return true;
		}
		
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	


	
}
