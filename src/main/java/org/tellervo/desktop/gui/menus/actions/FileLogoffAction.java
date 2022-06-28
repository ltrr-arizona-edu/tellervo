package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel.NetworkStatus;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.util.WSCookieStoreHandler;

public class FileLogoffAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public FileLogoffAction() {
        super("Log off", Builder.getIcon("logoff.png", 22));
		putValue(SHORT_DESCRIPTION, "Log out from Tellervo server");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		WSCookieStoreHandler.getCookieStore().emptyCookieJar();
		App.appmodel.setNetworkStatus(NetworkStatus.OFFLINE);
		
	}

}
