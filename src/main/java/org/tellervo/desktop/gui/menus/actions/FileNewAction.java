package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class FileNewAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Window parent;
	
	public FileNewAction(Window parent) {
        super(I18n.getText("menus.file.new"), Builder.getIcon("filenew.png", 22));
		putValue(SHORT_DESCRIPTION, "Create a new series");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));
        this.parent = parent;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		EditorFactory.newSeries(parent);
		
	}

}
