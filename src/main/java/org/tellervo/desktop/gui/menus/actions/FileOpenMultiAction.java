package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;

public class FileOpenMultiAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	
	public FileOpenMultiAction() {
        //super(I18n.getText("menus.file.open"), Builder.getIcon("fileopen.png", 22));
        super("Open multiple", Builder.getIcon("folder_documents.png", 22));
		putValue(SHORT_DESCRIPTION, "Open multiple series");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.openmulti")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.openmulti"));

        if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
        {
        	this.setEnabled(false);
        }

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
		{
			// Open multi not supported in Tellervo-Lite
		    
		}
		else
		{
			FileOpenAction.opendb(true);
		}
	}
	
	

	
}