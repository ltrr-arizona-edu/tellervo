package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.curation.BoxCuration;
import org.tellervo.desktop.ui.Builder;

public class FileNewBoxAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public FileNewBoxAction(Window parent) {
        super("New box", Builder.getIcon("box.png", 22));
		putValue(SHORT_DESCRIPTION, "Create a new box");


    }

	@Override
	public void actionPerformed(ActionEvent e) {
		BoxCuration.showDialog(true);
		}	

}
