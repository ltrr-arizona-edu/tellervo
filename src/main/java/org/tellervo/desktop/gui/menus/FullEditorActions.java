package org.tellervo.desktop.gui.menus;

import javax.swing.Action;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.menus.actions.FileLogoffAction;
import org.tellervo.desktop.gui.menus.actions.FileLogonAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.gui.menus.actions.FileSaveAllAction;
import org.tellervo.desktop.gui.menus.actions.GraphCreateFileHistoryPlotAction;
import org.tellervo.desktop.gui.menus.actions.ViewToExtentAction;

public class FullEditorActions extends EditorActions {

	public Action fileSaveAllAction;
	public Action filePrintAction;
	public Action fileLogoffAction;
	public Action fileLogonAction;
	public Action graphCreateFileHistoryPlotAction;
	public Action viewZoomToExtent;

	public FullEditorActions(FullEditor editor)
	{
		super(editor);
		initExtraActions();
	}

	private void initExtraActions() {
		
		fileSaveAllAction = new FileSaveAllAction((FullEditor) editor);
		filePrintAction = new FilePrintAction(editor);
		fileLogoffAction = new FileLogoffAction();
		fileLogonAction = new FileLogonAction();
		
		graphCreateFileHistoryPlotAction = new GraphCreateFileHistoryPlotAction(editor);
		
		viewZoomToExtent = new ViewToExtentAction((FullEditor) editor);
		
		linkModel();

	}

	@Override
	protected void setMenusForNetworkStatus() {
		
		this.fileLogonAction.setEnabled(!App.isLoggedIn());
		this.fileLogoffAction.setEnabled(App.isLoggedIn());
		
		
	}
	
	
	
}
