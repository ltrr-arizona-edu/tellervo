package org.tellervo.desktop.setupwizard;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.hardware.PlatformTestPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class WizardHardwareTest extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;
	PlatformTestPanel panel;
	
	
	public WizardHardwareTest() {
		super("Test your measuring platform", 
				"Use the form below to test your measuring platform. Check that "+
				"Tellervo can receive data and that it is interpreting the values "+
				"correctly by measuring a ruler and cross-checking the value.");
		setBackground(Color.WHITE);
		
		setLayout(new MigLayout("", "[grow,center]", "[][grow,top]"));
				
		panel = new PlatformTestPanel(Color.WHITE);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		
				
		this.add(panel, "cell 0 1,growx,aligny top, wmin 10");
		

	}
	
	@Override
	public void initialViewTasks()
	{
		panel.setupDevice();
		panel.setBackground(Color.WHITE);
	}

	@Override
	public void finish()
	{
		panel.finish();
	}
	
}
