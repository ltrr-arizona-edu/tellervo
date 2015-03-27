package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.ApplicationTemplate.AppFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis2.WWJPanel;
import org.tellervo.desktop.ui.Builder;

public class MapStereoModeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapStereoModeAction(FullEditor editor) {
        super("Stereo Mode", Builder.getIcon("map.png", 22));
		putValue(SHORT_DESCRIPTION, "Stereo mode");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 46.7045);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -121.6242);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e3);
        Configuration.setValue(AVKey.INITIAL_HEADING, 342);
        Configuration.setValue(AVKey.INITIAL_PITCH, 80);
        
        //ApplicationTemplate.start("World Wind Anaglyph Stereo", AppFrame.class);

		
	}

}
