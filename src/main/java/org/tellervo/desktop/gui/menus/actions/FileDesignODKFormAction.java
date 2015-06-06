package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.odk.ODKFormDesignPanel;
import org.tellervo.desktop.ui.Builder;

public class FileDesignODKFormAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	private Window parent;
	
	public FileDesignODKFormAction(Window parent) {
        super("Design ODK form", Builder.getIcon("odk.png", 22));
		putValue(SHORT_DESCRIPTION, "Design ODK form");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.open")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.open"));
        this.parent = parent;

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		ODKFormDesignPanel.showDialog(parent);
	}
	
	
}