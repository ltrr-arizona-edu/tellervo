package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.versioning.UpdateChecker;

public class HelpSetupWizardAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpSetupWizardAction() {
        super("Setup Wizard", Builder.getIcon("wizard.png", 22));
		putValue(SHORT_DESCRIPTION, "Setup Wizard");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		SetupWizard.launchWizard();
		}

}
