package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapNASAWFSPlaceNameLayerToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapNASAWFSPlaceNameLayerToggleAction(FullEditor editor) {
        super("Place names", Builder.getIcon("compass.png", 22));
		putValue(SHORT_DESCRIPTION, "Show/show the place names");
		putValue(Action.SELECTED_KEY, true);
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Layer NASAWFSPlaceNameLayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.Earth.PlaceName.Name"));
		
		NASAWFSPlaceNameLayer.setEnabled(!NASAWFSPlaceNameLayer.isEnabled());
		
	}

}
