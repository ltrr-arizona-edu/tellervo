package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.ui.Builder;

public class ToolsReverseAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private final AbstractEditor editor;
	
	public ToolsReverseAction(AbstractEditor editor) {
        super("Reverse", Builder.getIcon("reverse.png", 22));
        putValue(SHORT_DESCRIPTION, "Reverse the current series");
        this.editor = editor;
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		int selectedOption = JOptionPane.showConfirmDialog(editor, 
                "Are you sure you want to reverse your series?", 
                "Choose", 
                JOptionPane.YES_NO_CANCEL_OPTION); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			Reverse.reverse(editor.getSample());
		}
		
		
		
	}
	
}