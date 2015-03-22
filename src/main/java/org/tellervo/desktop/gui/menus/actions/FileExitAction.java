package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.ui.Builder;

public class FileExitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private AbstractEditor editor;
	
	public FileExitAction(AbstractEditor editor) {
        super("Exit", Builder.getIcon("exit.png", 22));
        this.editor = editor;
		putValue(SHORT_DESCRIPTION, "Exit Tellervo");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(editor instanceof FullEditor)
		{
			((FullEditor) editor).cleanupAndDispose();
		}
		else if (editor instanceof LiteEditor)
		{
			LiteEditor.closeAllEditors();
		}
		
		
	}

}
