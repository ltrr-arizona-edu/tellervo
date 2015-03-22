package org.tellervo.desktop.gui.menus;

import java.awt.Window;

import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

public abstract class EditorMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	protected EditorActions actions;
	protected Window editor;
	
	public EditorMenuBar(EditorActions actions, Window editor)
	{
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        this.actions = actions;
        this.editor = editor;
	}
	
}
