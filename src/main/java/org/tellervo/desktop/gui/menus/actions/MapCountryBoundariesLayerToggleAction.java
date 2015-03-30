package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapCountryBoundariesLayerToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapCountryBoundariesLayerToggleAction(FullEditor editor) {
        super("Political boundaries", Builder.getIcon("compass.png", 22));
		putValue(SHORT_DESCRIPTION, "Show/show the Political boundaries");
		putValue(Action.SELECTED_KEY, false);
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Layer countryBoundaries = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName("Political Boundaries");
		
		countryBoundaries.setEnabled(!countryBoundaries.isEnabled());
		
	}

}
