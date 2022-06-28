package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.testing.WSTester;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.versioning.UpdateChecker;
import org.tellervo.desktop.wsi.TransactionDebug;

public class HelpWebserviceTestsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpWebserviceTestsAction() {
        super("Run webservice tests", Builder.getIcon("bugreport.png", 22));
		putValue(SHORT_DESCRIPTION, "Run a series of tests against the server");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		WSTester.showDialog();
		}

}
