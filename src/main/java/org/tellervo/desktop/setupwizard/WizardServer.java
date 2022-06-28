package org.tellervo.desktop.setupwizard;
import java.awt.Color;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.panels.NetworkPrefsPanel;

import net.miginfocom.swing.MigLayout;

public class WizardServer extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;


	public WizardServer() {
		super("Configuring the Tellervo Server",
				"The Tellervo system is made up of two packages: the Tellervo desktop " +
				"client that you are using now; and the Tellervo Server.  If you are " +
				"working in a lab your systems administrator may have already set up " +
				"the Tellervo Server and given you the URL to connect to.  Alternatively, " +
				"you may have already installed the Tellervo server yourself.  If so the " +
				"installation program should have given you the URL.\n\n" +
				"If you don't have access to a " +
				"Tellervo Server yet, you should close this wizard, then go to the Tellervo " +
				"website and download it. Alternatively you can continue in Tellervo-lite mode with the webservice " +
				"disabled but this will mean the vast majority of the Tellervo functionality " +
				"is missing including mapping, data sharing, databasing etc.  In Tellervo-lite " +
				"mode, Tellervo works as a basic file-based dendro measuring tool.");
		
		setLayout(new MigLayout("", "[100px,grow]", "[][120px,grow]"));
		
		NetworkPrefsPanel networkPanel = new NetworkPrefsPanel(null);
		networkPanel.setBackgroundColor(Color.WHITE);
		networkPanel.setProxyPanelVisible(false);
		networkPanel.setWebservicePanelToSimple(true);
		this.add(networkPanel, "cell 0 1,alignx left,aligny top");

	}

}
