package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;

public class EditMeasureModeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public EditMeasureModeAction() {
        super("Measure mode");
		putValue(SHORT_DESCRIPTION, "Choose between whole ring width and early/latewood measuring mode");

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
	}

}
