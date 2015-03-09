package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.AboutBox;
import org.tellervo.desktop.gui.XMLDebugView;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.PropertiesWindow;
import org.tellervo.desktop.versioning.UpdateChecker;
import org.tellervo.desktop.wsi.TransactionDebug;

import com.dmurph.mvc.MVC;

public class HelpAboutTellervoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpAboutTellervoAction() {
        super("About Tellervo", Builder.getIcon("info.png", 22));
		putValue(SHORT_DESCRIPTION, "About Tellervo");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		AboutBox.getInstance().setVisible(true);
		}

}
