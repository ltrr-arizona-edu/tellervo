package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.menus.EditorLiteFileMenu;
import org.tellervo.desktop.gui.menus.FileMenu;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class FileOpenAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	private Window parent;
	
	public FileOpenAction(Window parent) {
        super(I18n.getText("menus.file.open"), Builder.getIcon("fileopen.png", 22));
        putValue(SHORT_DESCRIPTION, "Open a series");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.open")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.open"));
        this.parent = parent;

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
		{
			EditorLiteFileMenu.openLegacyFile(parent);
		}
		else
		{
			FileMenu.opendb();
		}
	}
	
}