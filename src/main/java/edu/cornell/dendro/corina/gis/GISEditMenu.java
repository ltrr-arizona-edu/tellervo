package edu.cornell.dendro.corina.gis;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;

import edu.cornell.dendro.corina.gui.menus.EditMenu;

public class GISEditMenu extends EditMenu {

	public GISEditMenu(JFrame frame) {
		super(frame);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	}

	
	
}
