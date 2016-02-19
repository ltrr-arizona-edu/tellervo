package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.bulkdataentry.control.DeleteODKInstancesEvent;
import org.tellervo.desktop.ui.Builder;

public class FileDeleteODKInstancesAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	
	public FileDeleteODKInstancesAction() {
        super("Delete field data from server", Builder.getIcon("odk-delete.png", 22));
		putValue(SHORT_DESCRIPTION, "Delete field data from server");

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		DeleteODKInstancesEvent event = new DeleteODKInstancesEvent();
		event.dispatch();
	}
	
	
}