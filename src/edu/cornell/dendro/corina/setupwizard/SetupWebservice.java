package edu.cornell.dendro.corina.setupwizard;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import org.netbeans.spi.wizard.WizardPage;

public class SetupWebservice extends WizardPage {

	private JTextField txtWebservice;
	
	private static String STEP_ID = "webservice";
	private static String DESCRIPTION = "Database server";
	
	public SetupWebservice()
	{
		super(STEP_ID, DESCRIPTION, true);
		
		txtWebservice = new JTextField();
		txtWebservice.setName("webserviceURL");
		this.setLayout(new BorderLayout());
		this.add(txtWebservice, BorderLayout.CENTER);
	}
	
	public static String getDescription()
	{
		return DESCRIPTION;
	}
	
	public static String getStep()
	{
		return STEP_ID;
	}
	
}
