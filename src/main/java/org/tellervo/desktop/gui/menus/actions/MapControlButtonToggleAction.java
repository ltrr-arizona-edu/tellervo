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

public class MapControlButtonToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapControlButtonToggleAction(FullEditor editor) {
        super("Hide/Show Controls", Builder.getIcon("controls.png", 22));
		putValue(SHORT_DESCRIPTION, "Hide/Show the control buttons");
		putValue(Action.SELECTED_KEY, App.prefs.getBooleanPref(PrefKey.MAP_CONTROLS_ENABLED, true));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.switchToMapTab();
		Layer controllayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.ViewControlsLayer.Name"));
		
		controllayer.setEnabled(!controllayer.isEnabled());
		
		App.prefs.setBooleanPref(PrefKey.MAP_CONTROLS_ENABLED, controllayer.isEnabled());
		
	}

}
