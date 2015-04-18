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

public class MapScaleBarToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapScaleBarToggleAction(FullEditor editor) {
        super("Hide/Show ScaleBar", Builder.getIcon("scale.png", 22));
		putValue(SHORT_DESCRIPTION, "Hide/Show the scale bar");
		putValue(Action.SELECTED_KEY, App.prefs.getBooleanPref(PrefKey.MAP_SCALEBAR_ENABLED, true));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.switchToMapTab();
		Layer scalebarlayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.Earth.ScalebarLayer.Name"));
		
		scalebarlayer.setEnabled(!scalebarlayer.isEnabled());
		
		App.prefs.setBooleanPref(PrefKey.MAP_SCALEBAR_ENABLED, scalebarlayer.isEnabled());

		
	}

}
