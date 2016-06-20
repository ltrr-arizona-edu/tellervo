package org.tellervo.desktop.gui.menus;

import javax.swing.Action;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.menus.actions.AddRemoveTagAction;
import org.tellervo.desktop.gui.menus.actions.FileDeleteODKDefinitionsAction;
import org.tellervo.desktop.gui.menus.actions.FileDeleteODKInstancesAction;
import org.tellervo.desktop.gui.menus.actions.FileDesignODKFormAction;
import org.tellervo.desktop.gui.menus.actions.FileExportMapAction;
import org.tellervo.desktop.gui.menus.actions.FileLogoffAction;
import org.tellervo.desktop.gui.menus.actions.FileLogonAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.gui.menus.actions.FileSaveAllAction;
import org.tellervo.desktop.gui.menus.actions.GraphCreateFileHistoryPlotAction;
import org.tellervo.desktop.gui.menus.actions.MapAddLayersAction;
import org.tellervo.desktop.gui.menus.actions.MapAnnotationsAction;
import org.tellervo.desktop.gui.menus.actions.MapCompassToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapControlButtonToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapControlsAction;
import org.tellervo.desktop.gui.menus.actions.MapCountryBoundariesLayerToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapDatabaseLayerAction;
import org.tellervo.desktop.gui.menus.actions.MapGISImageAction;
import org.tellervo.desktop.gui.menus.actions.MapKMLLayerAction;
import org.tellervo.desktop.gui.menus.actions.MapMGRSGraticuleLayerToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapNASAWFSPlaceNameLayerToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapSaveCurrentMapAsImagesAction;
import org.tellervo.desktop.gui.menus.actions.MapScaleBarToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapShapefileLayerAction;
import org.tellervo.desktop.gui.menus.actions.MapSpatialSearchAction;
import org.tellervo.desktop.gui.menus.actions.MapStereoModeAction;
import org.tellervo.desktop.gui.menus.actions.MapUTMGraticuleLayerToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapWMSLayerAction;
import org.tellervo.desktop.gui.menus.actions.MapWorldMapLayerToggleAction;
import org.tellervo.desktop.gui.menus.actions.MapZoomToSampleAction;
import org.tellervo.desktop.gui.menus.actions.TagSeriesAction;
import org.tellervo.desktop.gui.menus.actions.ToolsCrossdateAction;
import org.tellervo.desktop.gui.menus.actions.ToolsCrossdateWorkspaceAction;
import org.tellervo.desktop.gui.menus.actions.ToolsIndexAction;
import org.tellervo.desktop.gui.menus.actions.ToolsReconcileAction;
import org.tellervo.desktop.gui.menus.actions.ToolsSumAction;
import org.tellervo.desktop.gui.menus.actions.ViewToExtentAction;
import org.tellervo.desktop.util.openrecent.OpenRecent;

public class FullEditorActions extends AbstractEditorActions {

	public Action fileSaveAllAction;
	public Action filePrintAction;
	public Action fileLogoffAction;
	public Action fileLogonAction;
	public Action fileDesignODKFormAction;
	public Action fileDeleteODKDefinitionsAction;
	public Action fileDeleteODKInstancesAction;
	
	
	public Action graphCreateFileHistoryPlotAction;
	
	public Action viewZoomToExtent;
	
	public Action toolsReconcileAction;
	public Action toolsIndexAction;
	public Action toolsSumAction;
	public Action toolsCrossdateAction;
	public Action toolsCrossdateWorkspaceAction;
	
	public Action mapAddLayersAction;
	public Action mapControlsAction;
	public Action mapAnnotationsAction;
	public Action mapCompassToggleAction;
	public Action mapWorldMapLayerToggleAction;
	public Action mapUTMGraticuleLayerToggleAction;
	public Action mapMGRSGraticuleLayerToggleAction;
	public Action mapNASAWFSPlaceNameLayerToggleAction;
	public Action mapCountryBoundariesLayerToggleAction;
	public Action mapControlLayerToggleAction;
	public Action mapScaleBarLayerToggleAction;
	public Action mapStereoModeAction;
	public Action mapSaveCurrentMapAsImagesAction;
	public Action mapGISImageAction;
	public Action mapShapefileLayerAction;
	public Action mapWMSLayerAction;
	public Action mapDatabaseLayerAction;
	public Action mapKMLLayerAction;
	public Action mapZoomAction;
	public Action mapSpatialSearchAction;
	public Action tagSeriesAction;
	public Action addRemoveTagAction;
	
	
	
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
		fileDesignODKFormAction = new FileDesignODKFormAction(editor);
		fileDeleteODKDefinitionsAction = new FileDeleteODKDefinitionsAction();
		fileDeleteODKInstancesAction = new FileDeleteODKInstancesAction();
		

		tagSeriesAction = new TagSeriesAction((FullEditor) editor);
		addRemoveTagAction = new AddRemoveTagAction((FullEditor) editor);
		
		graphCreateFileHistoryPlotAction = new GraphCreateFileHistoryPlotAction(editor);
		
		viewZoomToExtent = new ViewToExtentAction((FullEditor) editor);
		
		toolsReconcileAction = new ToolsReconcileAction(editor);
		toolsIndexAction = new ToolsIndexAction(editor);
		toolsSumAction = new ToolsSumAction(editor);
		toolsCrossdateAction = new ToolsCrossdateAction(editor);
		toolsCrossdateWorkspaceAction = new ToolsCrossdateWorkspaceAction(editor);
		
		mapAddLayersAction = new MapAddLayersAction((FullEditor) editor);
		mapControlsAction = new MapControlsAction((FullEditor) editor);
		mapAnnotationsAction = new MapAnnotationsAction((FullEditor) editor);
		mapCompassToggleAction = new MapCompassToggleAction((FullEditor) editor);
		mapWorldMapLayerToggleAction = new MapWorldMapLayerToggleAction((FullEditor) editor);
		mapUTMGraticuleLayerToggleAction = new MapUTMGraticuleLayerToggleAction((FullEditor) editor);
		mapMGRSGraticuleLayerToggleAction = new MapMGRSGraticuleLayerToggleAction((FullEditor) editor);
		mapNASAWFSPlaceNameLayerToggleAction = new MapNASAWFSPlaceNameLayerToggleAction((FullEditor) editor);
		mapCountryBoundariesLayerToggleAction = new MapCountryBoundariesLayerToggleAction((FullEditor) editor);
		mapControlLayerToggleAction = new MapControlButtonToggleAction((FullEditor) editor);
		mapScaleBarLayerToggleAction = new MapScaleBarToggleAction((FullEditor) editor);
		mapStereoModeAction = new MapStereoModeAction((FullEditor) editor);
		mapSaveCurrentMapAsImagesAction = new MapSaveCurrentMapAsImagesAction((FullEditor)editor);
		mapShapefileLayerAction = new MapShapefileLayerAction((FullEditor) editor);
		mapKMLLayerAction = new MapKMLLayerAction((FullEditor) editor);
		mapGISImageAction = new MapGISImageAction((FullEditor) editor);
		mapWMSLayerAction = new MapWMSLayerAction((FullEditor) editor);
		mapDatabaseLayerAction = new MapDatabaseLayerAction((FullEditor) editor);
		mapZoomAction = new MapZoomToSampleAction((FullEditor) editor);
		mapSpatialSearchAction = new MapSpatialSearchAction((FullEditor) editor);
				
		linkModel();

	}

	@Override
	protected void setMenusForNetworkStatus() {
		
		fileLogonAction.setEnabled(!App.isLoggedIn());
		fileLogoffAction.setEnabled(App.isLoggedIn());
		fileSaveAllAction.setEnabled(App.isLoggedIn());
		filePrintAction.setEnabled(App.isLoggedIn());
		fileNewAction.setEnabled(App.isLoggedIn());
		fileOpenAction.setEnabled(App.isLoggedIn());
		fileOpenMultiAction.setEnabled(App.isLoggedIn());
		OpenRecent.makeOpenRecentMenu().setEnabled(App.isLoggedIn());
		fileBulkDataEntryAction.setEnabled(App.isLoggedIn());
		fileExportDataAction.setEnabled(App.isLoggedIn());
		fileNewAction.setEnabled(App.isLoggedIn());
		fileSaveAction.setEnabled(App.isLoggedIn());
		
		editCopyAction.setEnabled(App.isLoggedIn());
		editDeleteAction.setEnabled(App.isLoggedIn());
		editInsertMissingRingPushBackwardsAction.setEnabled(App.isLoggedIn());
		editInsertMissingRingPushForwardsAction.setEnabled(App.isLoggedIn());
		editInsertYearPushBackwardsAction.setEnabled(App.isLoggedIn());
		editInsertYearPushForwardsAction.setEnabled(App.isLoggedIn());
		editInsertYearsAction.setEnabled(App.isLoggedIn());
		editMeasureEWLWWidthsModeAction.setEnabled(App.isLoggedIn());
		editMeasureModeAction.setEnabled(App.isLoggedIn());
		editMeasureRingWidthsModeAction.setEnabled(App.isLoggedIn());
		editSelectAllAction.setEnabled(App.isLoggedIn());
		
		adminBasicBoxLabelAction.setEnabled(App.isLoggedIn());
		adminBoxLabelAction.setEnabled(App.isLoggedIn());
		adminChangePasswordAction.setEnabled(App.isLoggedIn());
		adminCurationMenuBoxDetailsAction.setEnabled(App.isLoggedIn());
		adminCurationMenuFindSampleAction.setEnabled(App.isLoggedIn());
		adminCurationMenuLoanDialogAction.setEnabled(App.isLoggedIn());
		adminCurationMenuNewLoanAction.setEnabled(App.isLoggedIn());
		adminCurationMenuSampleStatusAction.setEnabled(App.isLoggedIn());
		adminDatabaseStatisticsAction.setEnabled(App.isLoggedIn());
		adminEditViewPermissionsAction.setEnabled(App.isLoggedIn());
		adminForgetPasswordAction.setEnabled(App.isLoggedIn());
		adminLabelAction.setEnabled(App.isLoggedIn());
		adminMetaDBAction.setEnabled(App.isLoggedIn());
		adminReportsAction.setEnabled(App.isLoggedIn());
		adminSampleLabelAction.setEnabled(App.isLoggedIn());
		adminSiteMapAction.setEnabled(App.isLoggedIn());
		adminUserAndGroupsAction.setEnabled(App.isLoggedIn());
		
		graphAllSeriesAction.setEnabled(App.isLoggedIn());
		graphCurrentSeriesAction.setEnabled(App.isLoggedIn());
		graphCreateFileHistoryPlotAction.setEnabled(App.isLoggedIn());
		
		toolsTruncateAction.setEnabled(App.isLoggedIn());
		toolsReverseAction.setEnabled(App.isLoggedIn());
		toolsReconcileAction.setEnabled(App.isLoggedIn());
		toolsIndexAction.setEnabled(App.isLoggedIn());
		toolsCrossdateAction.setEnabled(App.isLoggedIn());
		toolsCrossdateWorkspaceAction.setEnabled(App.isLoggedIn());
		toolsSumAction.setEnabled(App.isLoggedIn());
		toolsRedateAction.setEnabled(App.isLoggedIn());		
		
	}

	@Override
	protected void setMenusForSample() {
		fileSaveAllAction.setEnabled(currentSample!=null);
		filePrintAction.setEnabled(currentSample!=null);

		toolsCrossdateWorkspaceAction.setEnabled(currentSample!=null);
		toolsCrossdateAction.setEnabled(currentSample!=null);
		toolsIndexAction.setEnabled(currentSample!=null);
		toolsReconcileAction.setEnabled(currentSample!=null);
		toolsSumAction.setEnabled(currentSample!=null);	
		
		graphCreateFileHistoryPlotAction.setEnabled(currentSample!=null);
	}

	
	
	
	
}
