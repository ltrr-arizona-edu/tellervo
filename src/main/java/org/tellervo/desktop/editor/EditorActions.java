package org.tellervo.desktop.editor;

import javax.swing.Action;

import org.tellervo.desktop.gui.menus.actions.FileBulkDataEntryAction;
import org.tellervo.desktop.gui.menus.actions.FileDesignODKFormAction;
import org.tellervo.desktop.gui.menus.actions.FileExitAction;
import org.tellervo.desktop.gui.menus.actions.FileExportDataAction;
import org.tellervo.desktop.gui.menus.actions.FileExportMapAction;
import org.tellervo.desktop.gui.menus.actions.FileLogoffAction;
import org.tellervo.desktop.gui.menus.actions.FileLogonAction;
import org.tellervo.desktop.gui.menus.actions.FileNewAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenMultiAction;
import org.tellervo.desktop.gui.menus.actions.GraphCurrentSeriesAction;
import org.tellervo.desktop.gui.menus.actions.EditInitDataGridAction;
import org.tellervo.desktop.gui.menus.actions.EditMeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.AdminMetadatabaseBrowserAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.gui.menus.actions.RemarkToggleAction;
import org.tellervo.desktop.gui.menus.actions.FileSaveAction;
import org.tellervo.desktop.gui.menus.actions.ToolsTruncateAction;
import org.tellervo.desktop.io.control.IOController;

/**
 * A collection of actions organised by their position in a standard menu
 * 
 * @author pbrewer
 *
 */
public class EditorActions {

	private AbstractEditor editor;
	
	// File menu actions
	public Action fileOpenAction ;
	public Action fileSaveAction;
	public Action fileExportDataAction;
	public Action fileExportMapAction;
	public Action filePrintAction;
	public Action fileNewAction;
	public Action fileOpenMultiAction;
	public Action fileBulkDataEntryAction;
	public Action fileDesignODKFormAction;
	public Action fileLogoffAction;
	public Action fileLogonAction;
	public Action fileExitAction;

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
		fileNewAction = new FileNewAction(editor);
		fileOpenAction = new FileOpenAction(editor);
		fileOpenMultiAction = new FileOpenMultiAction();
		fileExportDataAction = new FileExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		fileExportMapAction = new FileExportMapAction(editor);
		fileBulkDataEntryAction = new FileBulkDataEntryAction(IOController.OPEN_EXPORT_WINDOW);
		fileDesignODKFormAction = new FileDesignODKFormAction(editor);
		fileSaveAction = new FileSaveAction(editor);
		filePrintAction = new FilePrintAction(editor);
		fileLogoffAction = new FileLogoffAction();
		fileLogonAction = new FileLogonAction();
		fileExitAction = new FileExitAction();
		editMeasureAction = new EditMeasureToggleAction(editor);
		editInitGridAction = new EditInitDataGridAction(editor);
		remarkAction = new RemarkToggleAction(editor);
		adminMetaDBAction = new AdminMetadatabaseBrowserAction();
		toolsTruncateAction = new ToolsTruncateAction(editor);
		graphSeriesAction = new GraphCurrentSeriesAction(editor);
	}
	
	
}
