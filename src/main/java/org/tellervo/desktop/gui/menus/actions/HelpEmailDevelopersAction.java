package org.tellervo.desktop.gui.menus.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.versioning.UpdateChecker;

public class HelpEmailDevelopersAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public HelpEmailDevelopersAction() {
        super("Email developers", Builder.getIcon("email.png", 22));
		putValue(SHORT_DESCRIPTION, "Email developers");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Desktop desktop = Desktop.getDesktop();
		
		
		
		try {
			URI uri = new URI("mailto:p.brewer@ltrr.arizona.edu");

			desktop.mail(uri);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
		}


