package org.tellervo.desktop.gui.menus;

import javax.swing.Action;

import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.gui.menus.actions.FileSaveAsAction;


public class LiteEditorActions extends EditorActions {

	public Action fileSaveAsAction;

	
	public LiteEditorActions(LiteEditor editor)
	{
		super(editor);
		initExtraActions();
	}

	private void initExtraActions() {
		
		fileSaveAsAction = new FileSaveAsAction(editor);

		
		linkModel();
		
	}

	@Override
	protected void setMenusForNetworkStatus() {
		
	}
	
	
	
}
