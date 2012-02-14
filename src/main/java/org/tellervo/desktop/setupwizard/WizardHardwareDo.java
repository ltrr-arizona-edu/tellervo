package org.tellervo.desktop.setupwizard;

import java.awt.Color;

import org.tellervo.desktop.prefs.panels.HardwarePrefsPanel;

import net.miginfocom.swing.MigLayout;

public class WizardHardwareDo extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;

	public WizardHardwareDo() {
		super("Measuring platform configuration", 
				"Use the form below to configure and test your measuring platform.  " +
				"Some measuring platforms have fixed settings in which case the " +
				"port settings will be set automatically, but others can be changed in the " +
				"hardware and must be set explicitly here. Use the 'Test Connection' button " +
				"to make sure that Corina can successfully communicate with your platform.");
		
		setLayout(new MigLayout("", "[100px,grow]", "[][120px,grow]"));
				
		HardwarePrefsPanel hardwarePanel = new HardwarePrefsPanel(null);
		hardwarePanel.setBackgroundColor(Color.WHITE);
		hardwarePanel.setBarcodePanelVisible(false);
		
		this.add(hardwarePanel, "cell 0 1,alignx left,aligny top");
		

	}

}
