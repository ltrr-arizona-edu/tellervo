package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.index.IndexDialog;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.ui.Builder;

public class ToolsIndexAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private final AbstractEditor editor;
	
	public ToolsIndexAction(AbstractEditor editor) {
        super("Index", Builder.getIcon("index.png", 22));
        putValue(SHORT_DESCRIPTION, "Index");
        this.editor = editor;
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// PERF: for big samples, it can take a couple
		// seconds for the dialog to appear.  not enough
		// for a progressbar, but enough that i should use
		// the "wait" cursor on the editor window.
		new IndexDialog(editor.getSample(), editor);
		
		
	}
	
}