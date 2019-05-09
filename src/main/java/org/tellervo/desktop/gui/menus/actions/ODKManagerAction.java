package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
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

	private Window parent;
		
	public ODKManagerAction( Window parent ){
        super("ODK form manager", Builder.getIcon("odk-logo.png", 22));
		putValue(SHORT_DESCRIPTION, "Open ODK form manager");
        this.parent = parent;

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		ODKManagerPanel.showDialog(parent);
		
	}
	
	
}