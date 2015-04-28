package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.versioning.UpdateChecker;
import org.tellervo.desktop.wsi.TransactionDebug;

public class HelpReportBugOnLastTransactionAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpReportBugOnLastTransactionAction() {
        super("Report bug on last transaction", Builder.getIcon("bugreport.png", 22));
		putValue(SHORT_DESCRIPTION, "Report bug on last transaction");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDebug.forceGenerateWSBug();
		}

}
