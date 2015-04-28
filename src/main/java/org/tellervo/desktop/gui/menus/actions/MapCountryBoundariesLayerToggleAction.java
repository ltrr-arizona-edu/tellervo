package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;

public class MapCountryBoundariesLayerToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapCountryBoundariesLayerToggleAction(FullEditor editor) {
        super("Political boundaries", Builder.getIcon("politicalboundary.png", 22));
		putValue(SHORT_DESCRIPTION, "Hide/Show the Political boundaries");
		putValue(Action.SELECTED_KEY, App.prefs.getBooleanPref(PrefKey.MAP_COUNTRYBOUNDARY_ENABLED, true));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.switchToMapTab();
		Layer countryBoundaries = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName("Political Boundaries");
		
		countryBoundaries.setEnabled(!countryBoundaries.isEnabled());
		App.prefs.setBooleanPref(PrefKey.MAP_COUNTRYBOUNDARY_ENABLED, countryBoundaries.isEnabled());

		
		
	}

}
