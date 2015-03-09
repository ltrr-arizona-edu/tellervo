package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.XMLDebugView;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.versioning.UpdateChecker;
import org.tellervo.desktop.wsi.TransactionDebug;

public class HelpXMLCommunicationsViewerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpXMLCommunicationsViewerAction() {
        super("XML Communications viewer", Builder.getIcon("networksettings.png", 22));
		putValue(SHORT_DESCRIPTION, "XML Communications viewer");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		XMLDebugView.showDialog();
		}

}
