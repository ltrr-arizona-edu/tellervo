package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.bulkdataentry.control.DeleteODKDefinitionsEvent;
import org.tellervo.desktop.ui.Builder;

public class FileDeleteODKDefinitionsAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	
	public FileDeleteODKDefinitionsAction() {
        super("Delete all form designs from server", Builder.getIcon("odk-delete.png", 22));
		putValue(SHORT_DESCRIPTION, "Delete form designs from server");

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		DeleteODKDefinitionsEvent event = new DeleteODKDefinitionsEvent();
		event.dispatch();
	}
	
	
}