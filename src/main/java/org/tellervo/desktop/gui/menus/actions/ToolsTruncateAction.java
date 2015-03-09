package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.ui.Builder;

public class ToolsTruncateAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private final AbstractEditor editor;
	
	public ToolsTruncateAction(AbstractEditor editor) {
        super("Truncate", Builder.getIcon("truncate.png", 22));
        putValue(SHORT_DESCRIPTION, "Truncate the current series");
        this.editor = editor;
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		new TruncateDialog(editor.getSample(), editor);
		
	}
	
}