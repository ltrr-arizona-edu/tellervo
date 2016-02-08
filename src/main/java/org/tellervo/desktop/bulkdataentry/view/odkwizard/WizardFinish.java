package org.tellervo.desktop.bulkdataentry.view.odkwizard;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;

public class WizardFinish extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;

	
	
	public WizardFinish() {
		super("ODK Wizard Complete", 
		"That is all the information that is required for you to proceed.  Click 'finish' to "
		+ "copy your ODK field data into the Bulk Data Entry screen.  You can then validate and edit "
		+ "the information before importing into the Tellervo database."
		);
		
	}


}
