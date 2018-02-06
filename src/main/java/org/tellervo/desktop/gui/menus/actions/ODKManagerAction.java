package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.hierarchy.AddRemoveWSITagDialog;
import org.tellervo.desktop.gui.hierarchy.WSITagNameDialog;
import org.tellervo.desktop.odk.ODKManagerPanel;
import org.tellervo.desktop.ui.Builder;

public class ODKManagerAction extends AbstractAction{

	private static final long serialVersionUID = 1L;


	
	public ODKManagerAction() {
        super("ODK form manager", Builder.getIcon("odk.png", 16));
		putValue(SHORT_DESCRIPTION, "Open ODK form manager");
		
    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		ODKManagerPanel.showDialog();
		
	}
	
	
}