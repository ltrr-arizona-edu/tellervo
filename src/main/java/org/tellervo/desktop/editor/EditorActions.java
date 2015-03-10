package org.tellervo.desktop.editor;

import java.awt.Window;

import javax.swing.Action;

import org.tellervo.desktop.gui.menus.actions.AdminBasicBoxLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminBoxLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminChangePasswordAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuBoxDetailsAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuFindSampleAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuLoanDialogAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuNewLoanAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuSampleStatusAction;
import org.tellervo.desktop.gui.menus.actions.AdminDatabaseStatisticsAction;
import org.tellervo.desktop.gui.menus.actions.AdminEditViewPermissionsAction;
import org.tellervo.desktop.gui.menus.actions.AdminForgetPasswordAction;
import org.tellervo.desktop.gui.menus.actions.AdminLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminReportsAction;
import org.tellervo.desktop.gui.menus.actions.AdminSampleLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminSiteMapAction;
import org.tellervo.desktop.gui.menus.actions.AdminUsersAndGroupsAction;
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
import org.tellervo.desktop.gui.menus.actions.FileSaveAsAction;
import org.tellervo.desktop.gui.menus.actions.GraphCurrentSeriesAction;
import org.tellervo.desktop.gui.menus.actions.EditInitDataGridAction;
import org.tellervo.desktop.gui.menus.actions.EditMeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.AdminMetadatabaseBrowserAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.gui.menus.actions.HelpCheckForUpdatesAction;
import org.tellervo.desktop.gui.menus.actions.HelpHelpContentsAction;
import org.tellervo.desktop.gui.menus.actions.HelpVideoTutorialsAction;
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
	//private String videoname;
	
	// File menu actions
	public Action fileOpenAction ;
	public Action fileSaveAction;
	public Action fileSaveAsAction;
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
	public Action adminUserAndGroupsAction;
	public Action adminEditViewPermissionsAction;
	public Action adminChangePasswordAction;
	public Action adminForgetPasswordAction;
	public Action adminReportsAction;
	public Action adminLabelAction;
	public Action adminBoxLabelAction;
	public Action adminBasicBoxLabelAction;
	public Action adminSampleLabelAction;
	public Action adminDatabaseStatisticsAction;
	public Action adminCurationMenuAction;
	public Action adminCurationMenuBoxDetailsAction;
	public Action adminCurationMenuFindSampleAction;
	public Action adminCurationMenuLoanDialogAction;
	public Action adminCurationMenuNewLoanAction;
	public Action adminCurationMenuSampleStatusAction;
	public Action adminSiteMapAction;
	
	// View menu actions
	
	// Tools menu action
	public Action toolsTruncateAction;

	// Graph menu action
	public Action graphSeriesAction;
	
	// Toolbar only actions
	public Action remarkAction;
	
	//Help Menu actions
	public Action helpHelpContentsAction;
	public Action helpVideoTutorialsAction;
	public Action helpCheckForUpdatesAction;
	
	
	/**
	 * Constructor for main window
	 */
	public EditorActions(Window parent)
	{
		fileNewAction = new FileNewAction(parent);
		fileOpenAction = new FileOpenAction(parent);
		fileOpenMultiAction = new FileOpenMultiAction();
		//fileExportDataAction = new FileExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		//fileExportMapAction = new FileExportMapAction(editor);
		fileBulkDataEntryAction = new FileBulkDataEntryAction(IOController.OPEN_EXPORT_WINDOW);
		fileDesignODKFormAction = new FileDesignODKFormAction(editor);
		//fileSaveAction = new FileSaveAction(editor);
		//filePrintAction = new FilePrintAction(editor);
		//fileLogoffAction = new FileLogoffAction();
		//fileLogonAction = new FileLogonAction();*/
		fileExitAction = new FileExitAction();
		/**editCopyAction = new EditCopyAction(editor);
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
		
		adminUserAndGroupsAction = new AdminUsersAndGroupsAction();
		adminEditViewPermissionsAction = new AdminEditViewPermissionsAction();
		adminChangePasswordAction = new AdminChangePasswordAction();
		adminForgetPasswordAction = new AdminForgetPasswordAction();
		adminReportsAction = new AdminReportsAction();
		adminLabelAction = new AdminLabelAction();
		adminBoxLabelAction = new AdminBoxLabelAction();
		adminBasicBoxLabelAction = new AdminBasicBoxLabelAction();
		adminSampleLabelAction = new AdminSampleLabelAction();
		adminDatabaseStatisticsAction = new AdminDatabaseStatisticsAction();
		adminCurationMenuAction = new AdminCurationMenuAction();
		adminCurationMenuBoxDetailsAction = new AdminCurationMenuBoxDetailsAction();
		adminCurationMenuFindSampleAction = new AdminCurationMenuFindSampleAction();
		adminCurationMenuLoanDialogAction = new AdminCurationMenuLoanDialogAction(editor);
		adminCurationMenuNewLoanAction = new AdminCurationMenuNewLoanAction(editor);
		adminCurationMenuSampleStatusAction = new AdminCurationMenuSampleStatusAction();		
		adminMetaDBAction = new AdminMetadatabaseBrowserAction();
		adminSiteMapAction = new AdminSiteMapAction();
		
		toolsTruncateAction = new ToolsTruncateAction(editor);
		graphSeriesAction = new GraphCurrentSeriesAction(editor);*/
		
		helpHelpContentsAction = new HelpHelpContentsAction();
		//helpVideoTutorialsAction = new HelpVideoTutorialsAction(videoname);
		helpCheckForUpdatesAction = new HelpCheckForUpdatesAction();
	}
	
	
	
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
		fileSaveAsAction = new FileSaveAsAction(editor);
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
		
		adminUserAndGroupsAction = new AdminUsersAndGroupsAction();
		adminEditViewPermissionsAction = new AdminEditViewPermissionsAction();
		adminChangePasswordAction = new AdminChangePasswordAction();
		adminForgetPasswordAction = new AdminForgetPasswordAction();
		adminReportsAction = new AdminReportsAction();
		adminLabelAction = new AdminLabelAction();
		adminBoxLabelAction = new AdminBoxLabelAction();
		adminBasicBoxLabelAction = new AdminBasicBoxLabelAction();
		adminSampleLabelAction = new AdminSampleLabelAction();
		adminDatabaseStatisticsAction = new AdminDatabaseStatisticsAction();
		adminCurationMenuAction = new AdminCurationMenuAction();
		adminCurationMenuBoxDetailsAction = new AdminCurationMenuBoxDetailsAction();
		adminCurationMenuFindSampleAction = new AdminCurationMenuFindSampleAction();
		adminCurationMenuLoanDialogAction = new AdminCurationMenuLoanDialogAction(editor);
		adminCurationMenuNewLoanAction = new AdminCurationMenuNewLoanAction(editor);
		adminCurationMenuSampleStatusAction = new AdminCurationMenuSampleStatusAction();		
		adminMetaDBAction = new AdminMetadatabaseBrowserAction();
		adminSiteMapAction = new AdminSiteMapAction();
		
		toolsTruncateAction = new ToolsTruncateAction(editor);
		graphSeriesAction = new GraphCurrentSeriesAction(editor);
		
		helpHelpContentsAction = new HelpHelpContentsAction();
		//helpVideoTutorialsAction = new HelpVideoTutorialsAction(videoname);
		helpCheckForUpdatesAction = new HelpCheckForUpdatesAction();
		
		
	}
	
	
}
