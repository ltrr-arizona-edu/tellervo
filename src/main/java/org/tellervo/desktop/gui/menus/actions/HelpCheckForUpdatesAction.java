package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.versioning.UpdateChecker;

public class HelpCheckForUpdatesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpCheckForUpdatesAction() {
        super("Check for updates", Builder.getIcon("upgrade.png", 22));
		putValue(SHORT_DESCRIPTION, "Check for updates");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		UpdateChecker.doUpdateCheck(true);
		}

}
