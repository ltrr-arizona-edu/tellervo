package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.manip.RedateDialog;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.ui.Builder;

public class ToolsRedateAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private final AbstractEditor editor;
	
	public ToolsRedateAction(AbstractEditor editor) {
        super("Redate", Builder.getIcon("redate.png", 22));
        putValue(SHORT_DESCRIPTION, "Redate");
        this.editor = editor;
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		new RedateDialog(editor.getSample(), editor).setVisible(true);
		
		
	}
	
}