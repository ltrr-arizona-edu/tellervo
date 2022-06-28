package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;

public class EditPreferencesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public EditPreferencesAction() {
        super("Preferences", Builder.getIcon("advancedsettings.png", 22));
		putValue(SHORT_DESCRIPTION, "Show Tellervo preferences");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		App.showPreferencesDialog();
		
	}

}
