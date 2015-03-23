package org.tellervo.desktop.gui.menus;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.menus.actions.AddSeriesToWorkspaceAction;
import org.tellervo.desktop.gui.menus.actions.AdminBasicBoxLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminBoxLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminChangePasswordAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuBoxDetailsAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuFindSampleAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuLoanDialogAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuNewLoanAction;
import org.tellervo.desktop.gui.menus.actions.AdminCurationMenuSampleStatusAction;
import org.tellervo.desktop.gui.menus.actions.AdminDatabaseStatisticsAction;
import org.tellervo.desktop.gui.menus.actions.AdminEditViewPermissionsAction;
import org.tellervo.desktop.gui.menus.actions.AdminForgetPasswordAction;
import org.tellervo.desktop.gui.menus.actions.AdminLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminMetadatabaseBrowserAction;
import org.tellervo.desktop.gui.menus.actions.AdminReportsAction;
import org.tellervo.desktop.gui.menus.actions.AdminSampleLabelAction;
import org.tellervo.desktop.gui.menus.actions.AdminSiteMapAction;
import org.tellervo.desktop.gui.menus.actions.AdminUsersAndGroupsAction;
import org.tellervo.desktop.gui.menus.actions.EditCopyAction;
import org.tellervo.desktop.gui.menus.actions.EditDeleteAction;
import org.tellervo.desktop.gui.menus.actions.EditInitDataGridAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertMissingRingPushBackwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertMissingRingPushForwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertYearPushBackwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertYearPushForwardsAction;
import org.tellervo.desktop.gui.menus.actions.EditInsertYearsAction;
import org.tellervo.desktop.gui.menus.actions.EditMeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.EditPreferencesAction;
import org.tellervo.desktop.gui.menus.actions.EditSelectAllAction;
import org.tellervo.desktop.gui.menus.actions.FileBulkDataEntryAction;
import org.tellervo.desktop.gui.menus.actions.FileDesignODKFormAction;
import org.tellervo.desktop.gui.menus.actions.FileExitAction;
import org.tellervo.desktop.gui.menus.actions.FileExportDataAction;
import org.tellervo.desktop.gui.menus.actions.FileLogoffAction;
import org.tellervo.desktop.gui.menus.actions.FileLogonAction;
import org.tellervo.desktop.gui.menus.actions.FileNewAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenMultiAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.gui.menus.actions.FileSaveAction;
import org.tellervo.desktop.gui.menus.actions.FileSaveAsAction;
import org.tellervo.desktop.gui.menus.actions.GraphAllSeriesAction;
import org.tellervo.desktop.gui.menus.actions.GraphComponentSeriesAction;
import org.tellervo.desktop.gui.menus.actions.GraphCreateFileHistoryPlotAction;
import org.tellervo.desktop.gui.menus.actions.GraphCurrentSeriesAction;
import org.tellervo.desktop.gui.menus.actions.HelpAboutTellervoAction;
import org.tellervo.desktop.gui.menus.actions.HelpCheckForUpdatesAction;
import org.tellervo.desktop.gui.menus.actions.HelpEmailDevelopersAction;
import org.tellervo.desktop.gui.menus.actions.HelpErrorLogViewerAction;
import org.tellervo.desktop.gui.menus.actions.HelpHelpContentsAction;
import org.tellervo.desktop.gui.menus.actions.HelpMVCMonitorAction;
import org.tellervo.desktop.gui.menus.actions.HelpReportBugOnLastTransactionAction;
import org.tellervo.desktop.gui.menus.actions.HelpSetupWizardAction;
import org.tellervo.desktop.gui.menus.actions.HelpSystemsInformationAction;
import org.tellervo.desktop.gui.menus.actions.HelpVideoTutorialsAction;
import org.tellervo.desktop.gui.menus.actions.HelpXMLCommunicationsViewerAction;
import org.tellervo.desktop.gui.menus.actions.RemarkToggleAction;
import org.tellervo.desktop.gui.menus.actions.RemoveSeriesFromWorkspaceAction;
import org.tellervo.desktop.gui.menus.actions.ToolsCrossdateAction;
import org.tellervo.desktop.gui.menus.actions.ToolsTruncateAction;
import org.tellervo.desktop.io.control.IOController;
import org.tellervo.desktop.sample.Sample;

/**
 * A collection of actions organised by their position in a standard menu
 * 
 * @author pbrewer
 *
 */
public abstract class EditorActions {

	protected AbstractEditor editor;
	//private String videoname;

	// File menu actions
	public Action fileOpenAction ;
	public Action fileSaveAction;


	public Action fileExportDataAction;
	//public Action fileExportMapAction;
	public Action fileNewAction;
	public Action fileOpenMultiAction;
	public Action fileBulkDataEntryAction;
	public Action fileDesignODKFormAction;
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
	public Action adminCurationMenuBoxDetailsAction;
	public Action adminCurationMenuFindSampleAction;
	public Action adminCurationMenuLoanDialogAction;
	public Action adminCurationMenuNewLoanAction;
	public Action adminCurationMenuSampleStatusAction;
	public Action adminSiteMapAction;

	// View menu actions

	// Tools menu action
	public Action toolsTruncateAction;
	public Action toolsCrossdateAction;

	// Graph menu action
	public Action graphCurrentSeriesAction;
	public Action graphComponentSeriesAction;
	public Action graphAllSeriesAction;

	// Toolbar only actions
	public Action remarkAction;
	public Action addSeriesAction;
	public Action removeSeriesAction;

	//Help Menu actions
	public Action helpHelpContentsAction;
	public Action helpVideoIntroAction;
	public Action helpVideoGettingStartedAction;
	public Action helpVideoServerInstallationAction;
	public Action helpVideoEnteringMetadataAction;
	public Action helpVideoMeasuringSamplesAction;
	public Action helpVideoMappingAction;
	public Action helpVideoAdminsteringUsersAndGroupsAction;
	public Action helpVideoCuratingYourCollectionAction;
	public Action helpVideoExportingDataAction;
	public Action helpVideoImportingAction;
	public Action helpVideoGraphingAction;
	public Action helpVideoDataManipulationAction;
	public Action helpCheckForUpdatesAction;
	public Action helpEmailDeveloperAction;
	public Action helpSetupWizardAction;
	public Action helpErrorLogViewerAction;
	public Action helpReportBugOnLastTransactionAction;
	public Action helpXMLCommunicationsViewerAction;
	public Action helpMVCMonitorAction;
	public Action helpSystemsInformationAction;
	public Action helpAboutTellervoAction;

	private Sample currentSample;



	public EditorActions(AbstractEditor editor)
	{
		this.editor = editor;


		fileNewAction = new FileNewAction(editor);
		fileOpenAction = new FileOpenAction(editor);
		fileOpenMultiAction = new FileOpenMultiAction(editor);
		fileExportDataAction = new FileExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		//fileExportMapAction = new FileExportMapAction(editor);
		fileBulkDataEntryAction = new FileBulkDataEntryAction(IOController.OPEN_EXPORT_WINDOW);
		fileDesignODKFormAction = new FileDesignODKFormAction(editor);
		fileSaveAction = new FileSaveAction(editor);


		fileExitAction = new FileExitAction(editor);
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
		adminCurationMenuBoxDetailsAction = new AdminCurationMenuBoxDetailsAction();
		adminCurationMenuFindSampleAction = new AdminCurationMenuFindSampleAction();
		adminCurationMenuLoanDialogAction = new AdminCurationMenuLoanDialogAction(editor);
		adminCurationMenuNewLoanAction = new AdminCurationMenuNewLoanAction(editor);
		adminCurationMenuSampleStatusAction = new AdminCurationMenuSampleStatusAction();		
		adminMetaDBAction = new AdminMetadatabaseBrowserAction();
		adminSiteMapAction = new AdminSiteMapAction();

		toolsTruncateAction = new ToolsTruncateAction(editor);
		toolsCrossdateAction = new ToolsCrossdateAction(editor);

		graphCurrentSeriesAction = new GraphCurrentSeriesAction(editor);
		graphComponentSeriesAction = new GraphComponentSeriesAction(editor);
		graphAllSeriesAction = new GraphAllSeriesAction(editor);

		helpHelpContentsAction = new HelpHelpContentsAction();
		helpVideoIntroAction = new HelpVideoTutorialsAction("Introduction");
		helpVideoGettingStartedAction = new HelpVideoTutorialsAction("Getting started");
		helpVideoServerInstallationAction = new HelpVideoTutorialsAction("Server installation");
		helpVideoEnteringMetadataAction = new HelpVideoTutorialsAction("Entering metadata");
		helpVideoMeasuringSamplesAction = new HelpVideoTutorialsAction("Measuring samples");
		helpVideoMappingAction = new HelpVideoTutorialsAction("mapping");
		helpVideoAdminsteringUsersAndGroupsAction = new HelpVideoTutorialsAction("Administering users and groups");
		helpVideoCuratingYourCollectionAction = new HelpVideoTutorialsAction("Curating your collection");
		helpVideoExportingDataAction = new HelpVideoTutorialsAction("Exporting data");
		helpVideoImportingAction = new HelpVideoTutorialsAction("Importing");
		helpVideoGraphingAction = new HelpVideoTutorialsAction("Graphing");
		helpVideoDataManipulationAction = new HelpVideoTutorialsAction("Data manipulation");
		helpCheckForUpdatesAction = new HelpCheckForUpdatesAction();
		helpEmailDeveloperAction = new HelpEmailDevelopersAction();
		helpSetupWizardAction = new HelpSetupWizardAction();
		helpErrorLogViewerAction = new HelpErrorLogViewerAction();
		helpReportBugOnLastTransactionAction = new HelpReportBugOnLastTransactionAction(); 
		helpXMLCommunicationsViewerAction = new HelpXMLCommunicationsViewerAction();
		helpMVCMonitorAction = new HelpMVCMonitorAction();
		helpSystemsInformationAction = new HelpSystemsInformationAction();
		helpAboutTellervoAction = new HelpAboutTellervoAction();

		addSeriesAction = new AddSeriesToWorkspaceAction(editor);
		removeSeriesAction = new RemoveSeriesFromWorkspaceAction(editor);



	}

	/**
	 * Enable/Disable actions based on network status
	 */
	private void setSharedMenusForNetworkStatus() {


		this.fileNewAction.setEnabled(App.isLoggedIn());
		// TODO Add others

		setMenusForNetworkStatus();
	}
	protected abstract void setMenusForNetworkStatus();
	
	
	private void setMenusForSampleStatus()
	{
		this.editMeasureAction.setEnabled(this.currentSample!=null);
	}
	

	protected void linkModel() {
		App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if (argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)) {
					setSharedMenusForNetworkStatus();
				}
			}
		});
		setSharedMenusForNetworkStatus();

		
		editor.getLstSamples().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				currentSample = editor.getSample();
				setMenusForSampleStatus();

			}

		});
		setMenusForSampleStatus();
		
	}
}
