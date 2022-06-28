package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.ui.Builder;

public class EditInsertYearPushBackwardsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public EditInsertYearPushBackwardsAction(AbstractEditor editor) {
        super("Insert year (push backwards)", Builder.getIcon("insertyear.png", 22));
		putValue(SHORT_DESCRIPTION, "Insert a year pushing data backwards");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

		this.editor = editor;
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.getSeriesDataMatrix().insertYear(0, true, null, false);
		
	}
	
}
