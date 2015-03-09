package org.tellervo.desktop.editor;

import javax.swing.Action;

import org.tellervo.desktop.gui.menus.actions.EditCopyAction;
import org.tellervo.desktop.gui.menus.actions.EditDeleteAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertMissingRingPushBackwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertMissingRingPushForwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertYearPushBackwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertYearPushForwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertYearsAction;
import org.tellervo.desktop.gui.menus.actions.EditPreferencesAction;
import org.tellervo.desktop.gui.menus.actions.EditSelectAllAction;
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
	//public Action fileExportMapAction;
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
	public Action editCopyAction;
	public Action editSelectAllAction;
	public Action editInsertYearPushForwardsAction;
	public Action editInsertYearPushBackwardsAction;
	public Action editInsertMissingRingPushForwardsAction;
	public Action editInsertMissingRingPushBackwardsAction;
	public Action editDeleteAction;
	public Action editInsertYearsAction;
	public Action editPreferencesAction;
	
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
		//fileExportMapAction = new FileExportMapAction(editor);
		fileBulkDataEntryAction = new FileBulkDataEntryAction(IOController.OPEN_EXPORT_WINDOW);
		fileDesignODKFormAction = new FileDesignODKFormAction(editor);
		fileSaveAction = new FileSaveAction(editor);
		filePrintAction = new FilePrintAction(editor);
		fileLogoffAction = new FileLogoffAction();
		fileLogonAction = new FileLogonAction();
		fileExitAction = new FileExitAction();
		editCopyAction = new EditCopyAction(editor);
		editSelectAllAction = new EditSelectAllAction(editor);
		editInsertYearPushForwardsAction = new EditInsertYearPushForwardsAction(editor);
		editInsertYearPushBackwardsAction = new EditInsertYearPushBackwardsAction(editor);
		editInsertMissingRingPushForwardsAction = new EditInsertMissingRingPushForwardsAction(editor);
		editInsertMissingRingPushBackwardsAction = new EditInsertMissingRingPushBackwardsAction(editor);
		editDeleteAction = new EditDeleteAction(editor);
		editInsertYearsAction = new EditInsertYearsAction(editor);
		editInitGridAction = new EditInitDataGridAction(editor);
		editMeasureAction = new EditMeasureToggleAction(editor);
		editPreferencesAction = new EditPreferencesAction();
		remarkAction = new RemarkToggleAction(editor);
		adminMetaDBAction = new AdminMetadatabaseBrowserAction();
		
		toolsTruncateAction = new ToolsTruncateAction(editor);
		graphSeriesAction = new GraphCurrentSeriesAction(editor);
	}
	
	
}
