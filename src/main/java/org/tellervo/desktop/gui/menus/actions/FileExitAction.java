package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.gui.TellervoMainWindow;
import org.tellervo.desktop.ui.Builder;

public class FileExitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public FileExitAction() {
        super("Exit", Builder.getIcon("exit.png", 22));
		putValue(SHORT_DESCRIPTION, "Exit Tellervo");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		TellervoMainWindow.quit();
		
	}

}
