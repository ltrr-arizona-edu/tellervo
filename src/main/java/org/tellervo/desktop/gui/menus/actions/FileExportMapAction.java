package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwindx.examples.util.ScreenShotAction;

import javax.swing.Action;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class FileExportMapAction extends ScreenShotAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileExportMapAction(AbstractEditor editor) {
			
		super(((FullEditor)editor).getMapPanel().getWwd());
		
	        putValue(Action.SMALL_ICON, Builder.getIcon("captureimage.png", 22));
	        putValue(Action.LARGE_ICON_KEY, Builder.getIcon("captureimage.png", 22));
	        //putValue(Action.NAME, I18n.getText("menus.file.import"));
	        putValue(SHORT_DESCRIPTION, "Export map as image");
	        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.import")); 
	        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.import"));

  
    }
	
}