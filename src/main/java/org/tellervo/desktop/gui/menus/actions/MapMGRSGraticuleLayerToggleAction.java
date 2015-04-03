package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapMGRSGraticuleLayerToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapMGRSGraticuleLayerToggleAction(FullEditor editor) {
        super("MGRS graticule", Builder.getIcon("MGRS.png", 22));
		putValue(SHORT_DESCRIPTION, "Hide/Show the MGRS Graticule layer");
		putValue(Action.SELECTED_KEY, false);
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.switchToMapTab();
		Layer mgrsGraticuleLayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.Earth.MGRSGraticule.Name"));
		
		mgrsGraticuleLayer.setEnabled(!mgrsGraticuleLayer.isEnabled());
		
	}

}
