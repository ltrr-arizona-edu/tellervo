package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.io.Metadata;

public class RenameSeriesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public RenameSeriesAction(AbstractEditor editor) {
        super("Rename");
		putValue(SHORT_DESCRIPTION, "Rename series");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

		this.editor = editor;
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		String s = JOptionPane.showInputDialog(editor.getLstSamples(), "New title for series", " ", JOptionPane.QUESTION_MESSAGE);
		
		if(s!=null) {
			editor.getSample().setMeta(Metadata.KEYCODE, s);
			editor.getSample().setMeta(Metadata.TITLE, s);
			editor.getSample().fireSampleMetadataChanged();
		}
		
	}

	
}