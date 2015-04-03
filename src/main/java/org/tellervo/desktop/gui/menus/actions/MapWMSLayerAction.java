package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis.WMSManager;
import org.tellervo.desktop.ui.Builder;

public class MapWMSLayerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;

	public MapWMSLayerAction(FullEditor editor) {
        super("WMS Layer", Builder.getIcon("wms.png", 22));
		putValue(SHORT_DESCRIPTION, "Add a WMS Layer to map");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.switchToMapTab();
		WMSManager wmsManager = new WMSManager(editor.getMapPanel().getWwd());
		wmsManager.setEnabled(true);
	
	}

}
