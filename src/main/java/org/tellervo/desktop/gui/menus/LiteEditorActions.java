package org.tellervo.desktop.gui.menus;

import javax.swing.Action;

import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.gui.menus.actions.FileSaveAsAction;
import org.tellervo.desktop.gui.menus.actions.RenameSeriesAction;
import org.tellervo.desktop.sample.SampleEvent;


public class LiteEditorActions extends AbstractEditorActions {

	public Action fileSaveAsAction;
	public Action renameSeriesAction;

	
	public LiteEditorActions(LiteEditor editor)
	{
		super(editor);
		initExtraActions();
	}

	private void initExtraActions() {
		
		fileSaveAsAction = new FileSaveAsAction(editor);
		renameSeriesAction = new RenameSeriesAction(editor);
		
		linkModel();
		
	}

	@Override
	protected void setMenusForNetworkStatus() {
		
	}

	
	
	
	
}
