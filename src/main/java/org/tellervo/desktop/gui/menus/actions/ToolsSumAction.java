package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.manip.SumCreationDialog;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.ui.Builder;

public class ToolsSumAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private final AbstractEditor editor;
	
	public ToolsSumAction(AbstractEditor editor) {
        super("Sum", Builder.getIcon("sum.png", 22));
        putValue(SHORT_DESCRIPTION, "Sum");
        this.editor = editor;
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!editor.getSample().isSummed())
		{
		new SumCreationDialog(editor, ElementList.singletonList(new CachedElement(editor.getSample())));
		}
		
		else
		{
			new SumCreationDialog(editor, editor.getSample());
		}
		
	}
	
}