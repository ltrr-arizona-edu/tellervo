package edu.cornell.dendro.corina.setupwizard;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.netbeans.spi.wizard.WizardPage;

public class SetupIntroduction extends WizardPage {

	/**
	 * Create the panel.
	 */
	public SetupIntroduction() {
		this.setSize(new Dimension(300,300));
		JLabel txtIntro = new JLabel("Welcome to the wizard");
		this.add(txtIntro);
		
			
		
	}
	
	public static String getDescription()
	{
		return "Introduction";
	}
}
