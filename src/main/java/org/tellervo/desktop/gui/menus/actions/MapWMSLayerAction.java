package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapWMSLayerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapWMSLayerAction(FullEditor editor) {
        super("WMS Layer", Builder.getIcon("map.png", 22));
		putValue(SHORT_DESCRIPTION, "WMS Layer");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//ApplicationTemplate.start("World Wind WMS Layers", AppFrame.class);
		
	}

}
