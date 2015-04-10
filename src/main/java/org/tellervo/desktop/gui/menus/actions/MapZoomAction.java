package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis2.OpenGLTestCapabilities;
import org.tellervo.desktop.ui.Builder;

public class MapZoomAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public MapZoomAction(AbstractEditor editor) {
        super("View on map", Builder.getIcon("zoomtoextent.png", 22));
		putValue(SHORT_DESCRIPTION, "Zoom map");
		putValue(Action.SELECTED_KEY, true);
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(editor instanceof FullEditor)
		{
			
			
			if(OpenGLTestCapabilities.isOpenGLCapable())
			{
				((FullEditor)editor).switchToMapTab();
				((FullEditor)editor).wwMapPanel.zoomToSample(editor.getSample());
			}
		}
		
		
	}

}
