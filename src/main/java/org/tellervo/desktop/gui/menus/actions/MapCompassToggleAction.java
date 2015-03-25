package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapCompassToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapCompassToggleAction(FullEditor editor) {
        super("Hide/Show compass", Builder.getIcon("compass.png", 22));
		putValue(SHORT_DESCRIPTION, "Show/show the map compass");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Layer compasslayer = editor.getMapPanel().getWwd().getModel().getLayers().getLayerByName("Compass");
		
		compasslayer.setEnabled(!compasslayer.isEnabled());
		
	}

}
