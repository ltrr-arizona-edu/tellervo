package edu.cornell.dendro.corina.setupwizard;
import java.awt.Color;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.prefs.panels.NetworkPrefsPanel;

public class WizardServer extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;


	public WizardServer() {
		super("Configuring the Corina Server",
				"The Corina system is made up of two packages: the Corina desktop " +
				"client that you are using now; and the Corina server.  If you are " +
				"working in a lab your systems administrator may have already set up " +
				"the Corina server and given you the URL to connect to.  Alternatively, " +
				"you may have already installed the Corina server yourself.  If so the " +
				"installation program should have given you the URL.\n\n" +
				"If you don't have access to a " +
				"Corina server yet, you should close this wizard, then go to the Corina " +
				"website and download it.");
		
		setLayout(new MigLayout("", "[100px,grow]", "[][120px,grow]"));
		
		NetworkPrefsPanel networkPanel = new NetworkPrefsPanel(null);
		networkPanel.setBackgroundColor(Color.WHITE);
		networkPanel.setProxyPanelVisible(false);
		networkPanel.setWebservicePanelToSimple(true);
		this.add(networkPanel, "cell 0 1,alignx left,aligny top");

	}

}
