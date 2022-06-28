package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.hierarchy.AddRemoveWSITagDialog;
import org.tellervo.desktop.gui.hierarchy.WSITagNameDialog;
import org.tellervo.desktop.ui.Builder;

public class AddRemoveTagAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	private FullEditor editor;
	
	public AddRemoveTagAction(FullEditor editor) {
        super("Add/remove tag(s)", Builder.getIcon("tag.png", 16));
		putValue(SHORT_DESCRIPTION, "Add or remove tag(s) from this series");
		this.editor = editor;
    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		AddRemoveWSITagDialog.showDialog(App.mainWindow, this.editor.getSample().getSeries());
		
	}
	
	
}