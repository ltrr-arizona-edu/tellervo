package org.tellervo.desktop.bulkdataentry.view.odkwizard;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.ui.Builder;

public class WizardFinish extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;

	
	
	public WizardFinish() {
		super("ODK Wizard Complete", 
		"That is all the information that is required for you to proceed.  Click 'finish' to return to the Bulk Data Entry screen."
		);
		
	}


}
