package edu.cornell.dendro.corina.setupwizard;


public class WizardWelcome extends AbstractWizardPanel {


	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public WizardWelcome() {
		super("Welcome to Corina", 
				"This wizard will take you through the steps that are " +
				"necessary to configure Corina.  You may close this wizard at " +
				"any time and configure Corina manually in the preferences windows, " +
				"but until the Corina server details are supplied the majority of " +
				"functions will be unavailable to you.");
		
		


	}

}
