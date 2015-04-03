package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class MapStereoModeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapStereoModeAction(FullEditor editor) {
        super("Stereo Mode", Builder.getIcon("3d.png", 22));
		putValue(SHORT_DESCRIPTION, "View map in Stereo mode");
		putValue(Action.SELECTED_KEY, isSelected());

        this.editor = editor;

    }

	private boolean isSelected()
	{
		if(System.getProperty("gov.nasa.worldwind.stereo.mode")!=null && System.getProperty("gov.nasa.worldwind.stereo.mode").equals("redblue"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		editor.switchToMapTab();
		if(System.getProperty("gov.nasa.worldwind.stereo.mode")!=null && System.getProperty("gov.nasa.worldwind.stereo.mode").equals("redblue"))
		{
			System.setProperty("gov.nasa.worldwind.stereo.mode", "");
		}
		else
		{
			System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");

		}

		editor.reinitMapPanel();
	}

}
