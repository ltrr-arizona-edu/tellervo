package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapMGRSGraticuleLayerToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapMGRSGraticuleLayerToggleAction(FullEditor editor) {
        super("MGRS graticule", Builder.getIcon("grid.png", 22));
		putValue(SHORT_DESCRIPTION, "Show/show the MGRS Graticule layer");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Layer mgrsGraticuleLayer = editor.getMapPanel().getWwd()
				.getModel().getLayers()
				.getLayerByName(Logging.getMessage("layers.Earth.MGRSGraticule.Name"));
		
		mgrsGraticuleLayer.setEnabled(!mgrsGraticuleLayer.isEnabled());
		
	}

}
