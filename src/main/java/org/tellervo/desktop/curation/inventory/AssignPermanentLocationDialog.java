package org.tellervo.desktop.curation.inventory;


import org.tellervo.desktop.core.App;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JFrame;

import org.tellervo.desktop.gui.widgets.AbstractWizardDialog;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.ui.Builder;

public class AssignPermanentLocationDialog extends AbstractWizardDialog {

	private static final long serialVersionUID = 1L;
	private AssignPermanentLocationPanel page1;
	
	private boolean cancelled = false;
	
	public AssignPermanentLocationDialog(JFrame parent) {
		
		super("Assign Curation Locations", Builder.getImageAsIcon("sidebar.png"));
		App.init();

		this.parent = parent;
	
		// Setup pages
		pages = new ArrayList<AbstractWizardPanel>();
		page1 = new AssignPermanentLocationPanel();
		pages.add(page1);
				
		setupGui();
		this.setModal(true);
		this.setVisible(true);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("next")) {
			showNextPage();
		} else if (evt.getActionCommand().equals("previous")) {
			showPreviousPage();
		} else if (evt.getActionCommand().equals("close")) {
			this.cancelled = true;
			cleanup();
			
		}

	}
	
	public boolean wasCancelled()
	{
		return this.cancelled;
	}
	
}
