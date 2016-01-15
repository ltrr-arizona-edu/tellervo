package org.tellervo.desktop.setupwizard;

import java.awt.Color;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.panels.NetworkPrefsPanel;

import net.miginfocom.swing.MigLayout;

public class WizardProxy extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;
	
	public WizardProxy(){
	super("Connecting to the internet", 
			"Some networks require you to access the internet via a proxy server.  " +
			"If this is the case where you are you should set the details below.  If this " +
			"is not the case, or you are unsure you should leave it set to the default settings.");
	
	setLayout(new MigLayout("", "[100px,grow]", "[][120px,grow]"));
			
	NetworkPrefsPanel networkPanel = new NetworkPrefsPanel(null);
	networkPanel.setBackgroundColor(Color.WHITE);
	networkPanel.setWebservicePanelVisible(false);
	
	this.add(networkPanel, "cell 0 1,alignx left,aligny top");
	}
}
