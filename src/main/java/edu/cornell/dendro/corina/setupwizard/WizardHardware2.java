package edu.cornell.dendro.corina.setupwizard;

import java.awt.Color;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.prefs.panels.HardwarePrefsPanel;

public class WizardHardware2 extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;

	public WizardHardware2() {
		super("Measuring platform configuration", 
				"Use the form below to configure and test your measuring platform.  " +
				"You can also specify whether you'd like to enable support for a barcode " +
				"scanner.  You can change any of these options at a later date by opening " +
				"the preferences dialog.");
		
		setLayout(new MigLayout("", "[100px,grow]", "[][120px,grow]"));
				
		HardwarePrefsPanel hardwarePanel = new HardwarePrefsPanel(null);
		hardwarePanel.setBackgroundColor(Color.WHITE);
		hardwarePanel.setBarcodePanelVisible(false);
		
		this.add(hardwarePanel, "cell 0 1,alignx left,aligny top");
	}

}
