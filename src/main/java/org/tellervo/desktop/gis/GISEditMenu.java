package org.tellervo.desktop.gis;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;

import org.tellervo.desktop.gui.menus.EditMenu;


public class GISEditMenu extends EditMenu {

	public GISEditMenu(JFrame frame) {
		super(frame);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	}

	
	
}
