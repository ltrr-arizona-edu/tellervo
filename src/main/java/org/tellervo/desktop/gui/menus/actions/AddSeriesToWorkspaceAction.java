package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class AddSeriesToWorkspaceAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public AddSeriesToWorkspaceAction(AbstractEditor editor) {
        super(I18n.getText("action.addseriestoworkspace"), Builder.getIcon("edit_add.png", 16));
		putValue(SHORT_DESCRIPTION, I18n.getText("action.addseriestoworkspace.description"));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		editor.addSample(new Sample());
		
	}

}
