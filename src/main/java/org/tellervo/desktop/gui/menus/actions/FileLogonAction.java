package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.gui.LoginDialog;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.ui.Builder;

public class FileLogonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public FileLogonAction() {
        super("Log on", Builder.getIcon("logon.png", 22));
		putValue(SHORT_DESCRIPTION, "Log on to the Tellervo");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		LoginDialog dlg = new LoginDialog();
    	try {
    		dlg.doLogin(null, false);            		
       	} catch (UserCancelledException uce) {
    		return;
    	}
		
	}

}
