package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapCompassToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapCompassToggleAction(FullEditor editor) {
        super("Hide/Show compass", Builder.getIcon("compass.png", 22));
		putValue(SHORT_DESCRIPTION, "Hide/Show the map compass");
		putValue(Action.SELECTED_KEY, true);
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {

		editor.switchToMapTab();

		
		Layer compasslayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.CompassLayer.Name"));
		
		compasslayer.setEnabled(!compasslayer.isEnabled());
		
	}

}
