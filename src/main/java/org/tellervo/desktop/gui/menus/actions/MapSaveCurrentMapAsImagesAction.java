package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwindx.examples.ScreenShots;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapSaveCurrentMapAsImagesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	private WorldWindowGLCanvas wwd;
	
	public MapSaveCurrentMapAsImagesAction(FullEditor editor) {
        super("Save current map as images", Builder.getIcon("map.png", 22));
		putValue(SHORT_DESCRIPTION, "Save current map as images");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ScreenShots frame = new ScreenShots();

        frame.setJMenuBar(frame.createMenuBar()); // Create menu and associate with frame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
		
		       
		
	}

}
