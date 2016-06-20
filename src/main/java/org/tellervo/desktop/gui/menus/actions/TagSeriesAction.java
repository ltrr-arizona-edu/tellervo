package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.hierarchy.WSITagNameDialog;
import org.tellervo.desktop.ui.Builder;

public class TagSeriesAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	private FullEditor editor;
	
	public TagSeriesAction(FullEditor editor) {
        super("Tag this series", Builder.getIcon("tag.png", 16));
		putValue(SHORT_DESCRIPTION, "Add tag to series");
		this.editor = editor;
    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		WSITagNameDialog.addTagToSeries(App.mainWindow, this.editor.getSample().getSeries());
		
	}
	
	
}