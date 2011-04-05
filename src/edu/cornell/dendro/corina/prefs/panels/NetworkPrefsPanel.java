package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.prefs.wrappers.CheckBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.RadioButtonWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.SpinnerWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.TextComponentWrapper;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ArrayListModel;

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
	/**
	 * Create the panel.
	 */
	public NetworkPrefsPanel() {
		super(I18n.getText("preferences.network"), 
				"networksettings.png", 
				"Webservice network connection preferences");
		
		setLayout(new MigLayout("", "[grow]", "[115.00px][188.00px][grow]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Web service", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[100px:100px:100px][grow]", "[][15px][]"));
		
		chkDisableWS = new JCheckBox("Disable web service integration and work offline");
		panel.add(chkDisableWS, "cell 1 0");
		chkDisableWS.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				pingComponents();
			}
			
		});
		
		JLabel lblWebservice = new JLabel("URL:");
		panel.add(lblWebservice, "cell 0 1,alignx trailing,aligny center");
		
		txtWSURL = new JTextField();
		panel.add(txtWSURL, "cell 1 1,growx");
		txtWSURL.setColumns(10);
		
		btnForceDictionaryReload = new JButton("Force Dictionary Reload");
		panel.add(btnForceDictionaryReload, "cell 1 2,alignx right");
		
		JPanel panelNetworkConnection = new JPanel();
		panelNetworkConnection.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Network Connection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panelNetworkConnection, "cell 0 1,grow");
		panelNetworkConnection.setLayout(new MigLayout("", "[100px:100px:100px][grow][][]", "[][][][][]"));
		
		btnDefaultProxy = new JRadioButton("Use system default proxy settings");
		panelNetworkConnection.add(btnDefaultProxy, "cell 1 0 3 1");
		
		btnNoProxy = new JRadioButton("Direct connection");
		panelNetworkConnection.add(btnNoProxy, "cell 1 1 3 1");
		
		btnManualProxy = new JRadioButton("Use manual proxy settings");
		panelNetworkConnection.add(btnManualProxy, "cell 1 2 3 1");
		
		ButtonGroup group = new ButtonGroup();
		group.add(btnDefaultProxy);
		group.add(btnManualProxy);
		group.add(btnNoProxy);
				
		lblHttpProxy = new JLabel("HTTP Proxy:");
		panelNetworkConnection.add(lblHttpProxy, "cell 0 3,alignx trailing");
		
		txtHTTPProxyUrl = new JTextField();
		panelNetworkConnection.add(txtHTTPProxyUrl, "cell 1 3,growx");
		txtHTTPProxyUrl.setColumns(10);
		
		lblHTTPPort = new JLabel("Port:");
		panelNetworkConnection.add(lblHTTPPort, "cell 2 3");
		
		spnHTTPProxyPort = new JSpinner();
		spnHTTPProxyPort.setModel(new SpinnerNumberModel(80, 0, 65535, 1));
		panelNetworkConnection.add(spnHTTPProxyPort, "cell 3 3");
		
		lblHttpsProxy = new JLabel("HTTPS Proxy:");
		panelNetworkConnection.add(lblHttpsProxy, "cell 0 4,alignx trailing");
		
		txtHTTPSProxyUrl = new JTextField();
		panelNetworkConnection.add(txtHTTPSProxyUrl, "cell 1 4,growx");
		txtHTTPSProxyUrl.setColumns(10);
		
		lblHTTPSPort = new JLabel("Port:");
		panelNetworkConnection.add(lblHTTPSPort, "cell 2 4");
		
		spnHTTPSProxyPort = new JSpinner();
		spnHTTPSProxyPort.setModel(new SpinnerNumberModel(80, 0, 65535, 1));
		panelNetworkConnection.add(spnHTTPSProxyPort, "cell 3 4");

		linkToPrefs();
		pingComponents();
	}

	private void pingComponents()
	{
		txtWSURL.setEnabled(!chkDisableWS.isSelected());
		btnForceDictionaryReload.setEnabled(!chkDisableWS.isSelected());
	}
	
	private void linkToPrefs()
	{
		// networking - proxy
		btnDefaultProxy.setActionCommand("default");
		btnManualProxy.setActionCommand("manual");
		btnNoProxy.setActionCommand("direct");

		new TextComponentWrapper(txtHTTPProxyUrl, "corina.proxy.http", null);
		new SpinnerWrapper(spnHTTPProxyPort, "corina.proxy.http_port", 80);
		new TextComponentWrapper(txtHTTPSProxyUrl, "corina.proxy.https", null);
		new SpinnerWrapper(spnHTTPSProxyPort, "corina.proxy.https_port", 443);
		new RadioButtonWrapper(new JRadioButton[] { btnDefaultProxy, btnManualProxy, btnNoProxy }, 
				"corina.proxy.type", "default");
		new CheckBoxWrapper(chkDisableWS, "corina.webservice.disable", false );
	
		// manual proxy button behavior
		setEnableProxy(btnManualProxy.isSelected());
		btnManualProxy.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setEnableProxy(btnManualProxy.isSelected());
			}
		});
		
		// networking - server & smtp
		new TextComponentWrapper(txtWSURL, "corina.webservice.url", null);
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
	


	
}
