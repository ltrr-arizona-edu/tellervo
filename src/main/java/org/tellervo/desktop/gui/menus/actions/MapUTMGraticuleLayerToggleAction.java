package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapUTMGraticuleLayerToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapUTMGraticuleLayerToggleAction(FullEditor editor) {
        super("UTM graticule", Builder.getIcon("compass.png", 22));
		putValue(SHORT_DESCRIPTION, "Show/show the UTM Graticule map layer");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Layer utmGraticuleLayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.Earth.UTMGraticule.Name"));
		
		utmGraticuleLayer.setEnabled(!utmGraticuleLayer.isEnabled());
		
	}

}
