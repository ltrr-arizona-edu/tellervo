package org.tellervo.desktop.setupwizard;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.hardware.PlatformTestPanel;
import java.awt.Color;

public class WizardHardwareTest extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;
	PlatformTestPanel panel;
	
	
	public WizardHardwareTest() {
		super("Test your measuring platform", 
				"Use the form below to test your measuring platform. Check that "+
				"Tellervo can receive data and that it is interpreting the values "+
				"correctly by measuring a ruler and cross-checking the value.");
		setBackground(Color.WHITE);
		
		setLayout(new MigLayout("", "[100px,grow]", "[][120px,grow]"));
				
		panel = new PlatformTestPanel();
				
		this.add(panel, "cell 0 1,alignx left,aligny top");
		

	}
	
	@Override
	public void initialViewTasks()
	{
		panel.setupDevice();
	}

}
