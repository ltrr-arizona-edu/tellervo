package org.tellervo.desktop.editor;

import javax.swing.Action;

import org.tellervo.desktop.gui.menus.actions.FileExportDataAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.gui.menus.actions.GraphCurrentSeriesAction;
import org.tellervo.desktop.gui.menus.actions.EditInitDataGridAction;
import org.tellervo.desktop.gui.menus.actions.EditMeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.AdminMetadatabaseBrowserAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.gui.menus.actions.RemarkToggleAction;
import org.tellervo.desktop.gui.menus.actions.FileSaveAction;
import org.tellervo.desktop.gui.menus.actions.ToolsTruncateAction;
import org.tellervo.desktop.io.control.IOController;

public class EditorActions {

	private AbstractEditor editor;
	
	// File menu actions
	public Action fileOpenAction ;
	public Action fileSaveAction;
	public Action fileExportAction;
	public Action filePrintAction;

	// Editor menu actions
	public Action editMeasureAction;
	public Action editInitGridAction;
	
	// Admin menu actions
	public Action adminMetaDBAction;

	// View menu actions
	
	// Tools menu action
	public Action toolsTruncateAction;

	// Graph menu action
	public Action graphSeriesAction;
	
	// Toolbar only actions
	public Action remarkAction;
	
	
	
	
	public EditorActions(AbstractEditor editor)
	{
		this.editor = editor;
		init();
	}
	
	
	private void init()
	{
		fileOpenAction = new FileOpenAction(editor);
		fileSaveAction = new FileSaveAction(editor);
		fileExportAction = new FileExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		filePrintAction = new FilePrintAction(editor);
		editMeasureAction = new EditMeasureToggleAction(editor);
		editInitGridAction = new EditInitDataGridAction(editor);
		remarkAction = new RemarkToggleAction(editor);
		adminMetaDBAction = new AdminMetadatabaseBrowserAction();
		toolsTruncateAction = new ToolsTruncateAction(editor);
		graphSeriesAction = new GraphCurrentSeriesAction(editor);
	}
}
