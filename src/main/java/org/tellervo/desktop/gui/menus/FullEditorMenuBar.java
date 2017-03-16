package org.tellervo.desktop.gui.menus;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.menus.actions.FileImportLegacyFile;
import org.tellervo.desktop.gui.seriesidentity.IdentifySeriesPanel;
import org.tellervo.desktop.io.view.ImportView;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;

import edu.emory.mathcs.backport.java.util.Collections;

public class FullEditorMenuBar extends EditorMenuBar{

	private static final long serialVersionUID = 1L;
	private JMenuItem miOpenMulti;
	private JMenuItem miLogoff;
	private JMenuItem miLogon;
	private JMenuItem miExportData;
	private JMenuItem miBulkDataEntry;
	private JMenuItem miDesignODKForm;
	private JMenuItem miDeleteODKDefinitions;
	private JMenuItem miDeleteODKInstances;
	private JMenuItem miICMSImport;
	private Window parent;

	
	private JMenu mnAdministration;
	private JMenuItem miUsersAndGroups;
	private JMenuItem miEditViewPermissions;
	private JMenuItem miChangePassword;
	private JMenuItem miForgetPassword;
	private JMenuItem miReports;
	private JMenu miLabels;
	private JMenuItem miBoxLabel;
	private JMenuItem miBasicBoxLabel;
	private JMenuItem miSampleBoxLabel;
	private JMenuItem miRecordCard;
	private JMenuItem miDatabaseStatistics;
	private JMenu miCurationMenu;
	private JMenuItem miCurationMenuFindSample;
	private JMenuItem miCurationMenuSampleStatus;
	private JMenuItem miCurationMenuBoxDetails;
	private JMenuItem miCurationMenuNewLoan;
	private JMenuItem miCurationMenuLoanDialog;
	private JMenuItem miMetaDB;
	private JMenuItem miSiteMap;
	private JMenuItem miReportBugOnLastTransaction;
	private JMenuItem miXMLCommunicationsViewer;
	private JMenu mnTools;
	JMenuItem miComponentSeries;
	private JMenuItem miToolsCrossdate;
	private JMenuItem miToolsCrossdateWorkspace;
	private JMenuItem miToolsTruncate;
	private JMenuItem miNew;
	private JMenuItem miOpen;
	private JMenu openrecent;
	private JMenuItem miSave;
	private JMenuItem miPrint;
	

	
	public FullEditorMenuBar(FullEditorActions actions, FullEditor editor)
	{
        super(actions, editor);
        
		// FILE MENU
		JMenu mnFile = new JMenu("File");

		miNew = new JMenuItem(actions.fileNewAction);
		mnFile.add(miNew);

		miOpen = new JMenuItem(actions.fileOpenAction);
		mnFile.add(miOpen);

		miOpenMulti = new JMenuItem(actions.fileOpenMultiAction);
		mnFile.add(miOpenMulti);
		
		openrecent = OpenRecent.makeOpenRecentMenu();
		mnFile.add(openrecent);
		
		mnFile.addSeparator();
		
		mnFile.add(getImportDataOnlyMenu());
		
		miICMSImport = new JMenuItem(actions.fileImportICMSAction);
		mnFile.add(miICMSImport);
		//mnFile.add(getImportDataAndMetadataMenu());

		miExportData = new JMenuItem(actions.fileExportDataAction);
		mnFile.add(miExportData);
		
		miBulkDataEntry = new JMenuItem(actions.fileBulkDataEntryAction);
		mnFile.add(miBulkDataEntry);

		JMenu mnODK = new JMenu("Field data collection");
		mnODK.setIcon(Builder.getIcon("odk-logo.png", 22));
		mnFile.add(mnODK);
		
		miDesignODKForm = new JMenuItem(actions.fileDesignODKFormAction);
		mnODK.add(miDesignODKForm);
		mnODK.addSeparator();
		miDeleteODKDefinitions = new JMenuItem(actions.fileDeleteODKDefinitionsAction);
		mnODK.add(miDeleteODKDefinitions);
		miDeleteODKInstances = new JMenuItem(actions.fileDeleteODKInstancesAction);
		mnODK.add(miDeleteODKInstances);
		
		mnFile.addSeparator();


		miSave = new JMenuItem(actions.fileSaveAction);
		mnFile.add(miSave);
		
		try{
			JMenuItem miSaveAll = new JMenuItem(actions.fileSaveAllAction);
			mnFile.add(miSaveAll);
		} catch (Exception e)
		{
			
		}

		mnFile.addSeparator();

		miPrint = new JMenuItem(actions.filePrintAction);
		mnFile.add(miPrint);

		mnFile.addSeparator();

		miLogoff = new JMenuItem(actions.fileLogoffAction);
		mnFile.add(miLogoff);

		miLogon = new JMenuItem(actions.fileLogonAction);
		mnFile.add(miLogon);

		mnFile.addSeparator();

		
		JMenuItem miExit = new JMenuItem(actions.fileExitAction);
		mnFile.add(miExit);

		add(mnFile);

		
		// EDIT MENU
		
		JMenu mnEdit = new JMenu("Edit");
		add(mnEdit);

		JMenuItem miCopy = new JMenuItem(actions.editCopyAction);
		mnEdit.add(miCopy);
		
		JMenuItem miPaste = new JMenuItem(actions.editPasteAction);
		mnEdit.add(miPaste);

		mnEdit.addSeparator();

		JMenuItem miSelectAll = new JMenuItem(actions.editSelectAllAction);
		mnEdit.add(miSelectAll);

		mnEdit.addSeparator();

		JMenuItem miInsertYearPushForwards = new JMenuItem(actions.editInsertYearPushForwardsAction);
		mnEdit.add(miInsertYearPushForwards);

		JMenuItem miInsertYearPushBackwards = new JMenuItem(actions.editInsertYearPushBackwardsAction);
		mnEdit.add(miInsertYearPushBackwards);

		JMenuItem miInsertMissingRingPushForwards = new JMenuItem(actions.editInsertMissingRingPushForwardsAction);
		mnEdit.add(miInsertMissingRingPushForwards);

		JMenuItem miInsertMissingRingPushBackwards = new JMenuItem(actions.editInsertMissingRingPushBackwardsAction);
		mnEdit.add(miInsertMissingRingPushBackwards);

		JMenuItem miInsertYears = new JMenuItem(actions.editInsertYearsAction);
		mnEdit.add(miInsertYears);

		
		JMenuItem miDeleteYear = new JMenuItem(actions.editDeleteAction);
		mnEdit.add(miDeleteYear);
		
		mnEdit.addSeparator();

		JMenuItem miInitializeDataGrid = new JMenuItem(actions.editInitGridAction);
		mnEdit.add(miInitializeDataGrid);

		JMenu measureModeMenu = new JMenu(actions.editMeasureModeAction);
		final JRadioButtonMenuItem btnRingWidth = new JRadioButtonMenuItem(actions.editMeasureRingWidthsModeAction);
		final JRadioButtonMenuItem btnEWLWWidth = new JRadioButtonMenuItem(actions.editMeasureEWLWWidthsModeAction);
		ButtonGroup group = new ButtonGroup();
		group.add(btnRingWidth);
		group.add(btnEWLWWidth);
		measureModeMenu.add(btnRingWidth);
		measureModeMenu.add(btnEWLWWidth);
		mnEdit.add(measureModeMenu);
		
		JMenuItem miStartMeasuring = new JMenuItem(actions.editMeasureAction);
		mnEdit.add(miStartMeasuring);

		mnEdit.addSeparator();

		JMenuItem miPreferences = new JMenuItem(actions.editPreferencesAction);
		mnEdit.add(miPreferences);

		add(mnEdit);
		
		
		// ADMIN MENU

		mnAdministration = new JMenu("Administration");
		
		miUsersAndGroups = new JMenuItem(actions.adminUserAndGroupsAction);
		mnAdministration.add(miUsersAndGroups);
		
		miEditViewPermissions = new JMenuItem(actions.adminEditViewPermissionsAction);
		mnAdministration.add(miEditViewPermissions);
		
		mnAdministration.addSeparator();
		
		miChangePassword = new JMenuItem(actions.adminChangePasswordAction);
		mnAdministration.add(miChangePassword);
		
		miForgetPassword = new JMenuItem(actions.adminForgetPasswordAction);
		mnAdministration.add(miForgetPassword);
		
		miReports = new JMenuItem(actions.adminReportsAction);
		mnAdministration.add(miReports);
		
		miLabels = new JMenu(actions.adminLabelAction);
		miBoxLabel = new JMenuItem(actions.adminBoxLabelAction);
		miBasicBoxLabel = new JMenuItem(actions.adminBasicBoxLabelAction);
		miSampleBoxLabel = new JMenuItem(actions.adminSampleLabelAction);
		miRecordCard = new JMenuItem(actions.adminRecordCardAction);
		miLabels.add(miBoxLabel);
		miLabels.add(miBasicBoxLabel);
		miLabels.add(miSampleBoxLabel);
		miLabels.add(miRecordCard);
		mnAdministration.add(miLabels);
		
		
		mnAdministration.addSeparator();
		
		miDatabaseStatistics = new JMenuItem(actions.adminDatabaseStatisticsAction);
		mnAdministration.add(miDatabaseStatistics);
		
		miCurationMenu = new JMenu("Curation...");
		miCurationMenu.setIcon(Builder.getIcon("curation.png", 22));
		miCurationMenuFindSample = new JMenuItem(actions.adminCurationMenuFindSampleAction);
		miCurationMenuSampleStatus = new JMenuItem(actions.adminCurationMenuSampleStatusAction);
		miCurationMenuBoxDetails = new JMenuItem(actions.adminCurationMenuBoxDetailsAction);
		miCurationMenuLoanDialog = new JMenuItem(actions.adminCurationMenuLoanDialogAction);
		miCurationMenuNewLoan = new JMenuItem(actions.adminCurationMenuNewLoanAction);
		miCurationMenu.add(miCurationMenuFindSample);
		miCurationMenu.add(miCurationMenuSampleStatus);
		miCurationMenu.add(miCurationMenuBoxDetails);
		miCurationMenu.add(miCurationMenuLoanDialog);
		miCurationMenu.add(miCurationMenuNewLoan);
		mnAdministration.add(miCurationMenu);
		
		mnAdministration.addSeparator();
		
		miMetaDB = new JMenuItem(actions.adminMetaDBAction);
		mnAdministration.add(miMetaDB);
		
		miSiteMap = new JMenuItem(actions.adminSiteMapAction);
		mnAdministration.add(miSiteMap);		
		
		add(mnAdministration);
	
		
		// MAP MENU
		
		JMenu mnMap = new JMenu("Map");
		
		JMenu mnControls = new JMenu(actions.mapControlsAction);
		
		JCheckBoxMenuItem miCompass = new JCheckBoxMenuItem(actions.mapCompassToggleAction);
		mnControls.add(miCompass);
		
		JCheckBoxMenuItem miWorldMapLayer = new JCheckBoxMenuItem(actions.mapWorldMapLayerToggleAction);
		mnControls.add(miWorldMapLayer);
		
		JCheckBoxMenuItem miControlLayer = new JCheckBoxMenuItem(actions.mapControlLayerToggleAction);
		mnControls.add(miControlLayer);
		
		JCheckBoxMenuItem miScaleBarLayer = new JCheckBoxMenuItem(actions.mapScaleBarLayerToggleAction);
		mnControls.add(miScaleBarLayer);
		
		mnMap.add(mnControls);
		
		JMenu mnAnnotations = new JMenu(actions.mapAnnotationsAction);
		
		JCheckBoxMenuItem miUTMGraticuleLayer = new JCheckBoxMenuItem(actions.mapUTMGraticuleLayerToggleAction);
		mnAnnotations.add(miUTMGraticuleLayer);
		
		JCheckBoxMenuItem miMGRSGraticuleLayer = new JCheckBoxMenuItem(actions.mapMGRSGraticuleLayerToggleAction);
		mnAnnotations.add(miMGRSGraticuleLayer);
		
		JCheckBoxMenuItem miNASAWFSPlaceName = new JCheckBoxMenuItem(actions.mapNASAWFSPlaceNameLayerToggleAction);
		mnAnnotations.add(miNASAWFSPlaceName);
		
		JCheckBoxMenuItem miCountryBoundaries = new JCheckBoxMenuItem(actions.mapCountryBoundariesLayerToggleAction);
		mnAnnotations.add(miCountryBoundaries);
		
		mnMap.add(mnAnnotations);
		
		
		
		mnMap.addSeparator();
				
		JMenuItem miStereo = new JMenuItem(actions.mapStereoModeAction);
		mnMap.add(miStereo);
		
		JMenuItem miSave = new JMenuItem(actions.mapSaveCurrentMapAsImagesAction);
		mnMap.add(miSave);
		mnMap.addSeparator();
		
		JMenu mnAddLayers = new JMenu(actions.mapAddLayersAction);
		
		JMenuItem miShapefileLayer = new JMenuItem(actions.mapShapefileLayerAction);
		mnAddLayers.add(miShapefileLayer);
		
		JMenuItem miKMLLayer = new JMenuItem(actions.mapKMLLayerAction);
		mnAddLayers.add(miKMLLayer);
		
		JMenuItem miGISImage = new JMenuItem(actions.mapGISImageAction);
		mnAddLayers.add(miGISImage);
		
		JMenuItem miWMSLayer = new JMenuItem(actions.mapWMSLayerAction);
		mnAddLayers.add(miWMSLayer);
		
		JMenuItem miDBLayer = new JMenuItem(actions.mapDatabaseLayerAction);
		mnAddLayers.add(miDBLayer);
		
		
		JMenuItem miSpatialSearch = new JMenuItem(actions.mapSpatialSearchAction);
		mnMap.add(miSpatialSearch);
		
		mnMap.add(mnAddLayers);
		add(mnMap);

		
		// TOOLS MENU
		
		mnTools = new JMenu("Tools");
		add(mnTools);
		
		miToolsTruncate = new JMenuItem(actions.toolsTruncateAction);
		mnTools.add(miToolsTruncate);
		
		JMenuItem miToolsReverse = new JMenuItem(actions.toolsReverseAction);
		mnTools.add(miToolsReverse);
				
		JMenuItem miToolsRedate = new JMenuItem(actions.toolsRedateAction);
		mnTools.add(miToolsRedate);
		
		mnTools.addSeparator();
		
		JMenuItem miToolsReconcile = new JMenuItem(actions.toolsReconcileAction);
		mnTools.add(miToolsReconcile);
		
		JMenuItem miToolsIndex = new JMenuItem(actions.toolsIndexAction);
		mnTools.add(miToolsIndex);
		
		JMenuItem miToolsSum = new JMenuItem(actions.toolsSumAction);
		mnTools.add(miToolsSum);
	
		mnTools.addSeparator();
		
		miToolsCrossdate = new JMenuItem(actions.toolsCrossdateAction);
		mnTools.add(miToolsCrossdate);
		
		miToolsCrossdateWorkspace = new JMenuItem(actions.toolsCrossdateWorkspaceAction);
		mnTools.add(miToolsCrossdateWorkspace);
		

		
		// GRAPH MENU
		
		JMenu mnGraph = new JMenu("Graph");
		
		JMenuItem miCurrentSeries = new JMenuItem(actions.graphCurrentSeriesAction);
		mnGraph.add(miCurrentSeries);
		
	    miComponentSeries = new JMenuItem(actions.graphComponentSeriesAction);
	    mnGraph.add(miComponentSeries);
	    
	    JMenuItem miAllSeries = new JMenuItem(actions.graphAllSeriesAction);
	    mnGraph.add(miAllSeries);
	    
	    JMenuItem miCreateFileHistoryPlot = new JMenuItem(actions.graphCreateFileHistoryPlotAction);
	    mnGraph.add(miCreateFileHistoryPlot);
	    
		add(mnGraph);


		
		
		// HELP MENU
		
		JMenu mnHelp = new JMenu("Help");
		
		JMenuItem miHelpContents = new JMenuItem(actions.helpHelpContentsAction);
		mnHelp.add(miHelpContents);
		
		JMenuItem miSetupWizard = new JMenuItem(actions.helpSetupWizardAction);
		mnHelp.add(miSetupWizard);
		
		JMenu miVideoTutorials = new JMenu("Video tutorials... ");
		miVideoTutorials.setIcon(Builder.getIcon("video.png", 16));
				
		JMenuItem miVideoIntro = new JMenuItem(actions.helpVideoIntroAction);
		miVideoTutorials.add(miVideoIntro);
		
		JMenuItem miVideoGettingStarted = new JMenuItem(actions.helpVideoGettingStartedAction);
		miVideoTutorials.add(miVideoGettingStarted);
		
		JMenuItem miVideoServerInstallation = new JMenuItem(actions.helpVideoServerInstallationAction);
		miVideoTutorials.add(miVideoServerInstallation);
		
		JMenuItem miEnteringMetadata = new JMenuItem(actions.helpVideoEnteringMetadataAction);
		miVideoTutorials.add(miEnteringMetadata);
		
		JMenuItem miMeasuringSamples = new JMenuItem(actions.helpVideoMeasuringSamplesAction);
		miVideoTutorials.add(miMeasuringSamples);
		
		JMenuItem miMapping = new JMenuItem(actions.helpVideoMappingAction);
		miVideoTutorials.add(miMapping);
		
		JMenuItem miAdministeringUsersAndGroups = new JMenuItem(actions.helpVideoAdminsteringUsersAndGroupsAction);
		miVideoTutorials.add(miAdministeringUsersAndGroups);
		
		JMenuItem miCuratingYourCollection = new JMenuItem(actions.helpVideoCuratingYourCollectionAction);
		miVideoTutorials.add(miCuratingYourCollection);
		
		JMenuItem miExportingData = new JMenuItem(actions.helpVideoExportingDataAction);
		miVideoTutorials.add(miExportingData);
		
		JMenuItem miImporting = new JMenuItem(actions.helpVideoImportingAction);
		miVideoTutorials.add(miImporting);
		
		JMenuItem miGraphing = new JMenuItem(actions.helpVideoGraphingAction);
		miVideoTutorials.add(miGraphing);
		
		JMenuItem miDataManipulation = new JMenuItem(actions.helpVideoDataManipulationAction);
		miVideoTutorials.add(miDataManipulation);
				
		mnHelp.add(miVideoTutorials);
		
		mnHelp.addSeparator();
		
		JMenuItem miCheckForUpdates = new JMenuItem(actions.helpCheckForUpdatesAction);
		mnHelp.add(miCheckForUpdates);
		
		JMenuItem miEmailDevelopers = new JMenuItem(actions.helpEmailDeveloperAction);
		mnHelp.add(miEmailDevelopers);
		
		miReportBugOnLastTransaction = new JMenuItem(actions.helpReportBugOnLastTransactionAction);
		mnHelp.add(miReportBugOnLastTransaction);
		
		mnHelp.addSeparator();
		
		JMenu miDebugInfo = new JMenu("Debug Information");
		
		JMenuItem miErrorLogViewer = new JMenuItem(actions.helpErrorLogViewerAction);
		miDebugInfo.add(miErrorLogViewer);
				
		miXMLCommunicationsViewer = new JMenuItem(actions.helpXMLCommunicationsViewerAction);
		miDebugInfo.add(miXMLCommunicationsViewer);
		
		JMenuItem miMVCMonitor = new JMenuItem(actions.helpMVCMonitorAction);
		miDebugInfo.add(miMVCMonitor);
		
		JMenuItem miSystemsInformation = new JMenuItem(actions.helpSystemsInformationAction);
		miDebugInfo.add(miSystemsInformation);
		
		mnHelp.add(miDebugInfo);
		mnHelp.addSeparator();
		
		JMenuItem miAboutTellervo = new JMenuItem(actions.helpAboutTellervoAction);
		mnHelp.add(miAboutTellervo);
		
		
		add(mnHelp);


		

	}
	
	
	/**
	 * Get a submenu containing menu items for importing data and metadata from legacy data file
	 * 
	 * @return
	 */
	private JMenu getImportDataAndMetadataMenu()
	{
		
		JMenu fileimport = Builder.makeMenu("menus.file.import", "fileimport.png");
				
		for (final String s : TridasIO.getSupportedReadingFormats()) 
		{
			
			JMenuItem importitem = new JMenuItem(s);

			importitem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Set up file chooser and filters
					AbstractDendroFileReader reader = TridasIO.getFileReader(s);
					DendroFileFilter filter = reader.getDendroFileFilter();
					File lastFolder = null;
					try{
						lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
					} catch (Exception e){}
					
					JFileChooser fc = new JFileChooser(lastFolder);
					fc.addChoosableFileFilter(filter);
					fc.setFileFilter(filter);
					
					int returnVal = fc.showOpenDialog(null);
						
					// Get details from user
				    if (returnVal == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        ImportView importDialog = new ImportView(file, s);
						importDialog.setVisible(true);
				        
						// Remember this folder for next time
						App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
					    
				    } else {
				    	return;
				    }

					
				}
				
			});
			
			fileimport.add(importitem);
			
			
		}
	
		return fileimport;
		
		
	}
	
	
	/**
	 * Get a submenu containing menu items for importing data only from legacy data file
	 * 
	 * @return
	 */
	private JMenu getImportDataOnlyMenu()
	{

		JMenu fileimportdataonly = Builder.makeMenu("menus.file.importdataonly", "fileimport.png");
		
		ArrayList<AbstractDendroFileReader> readers = TridasIO.getInstantiatedReaders();
		Collections.sort(readers);
		
		for(AbstractDendroFileReader r : readers)
		{
			final AbstractDendroFileReader reader = r;
			
			FileImportLegacyFile action = new FileImportLegacyFile(reader, editor);
			JMenuItem importitem = new JMenuItem(action);
			fileimportdataonly.add(importitem);
		}
		return fileimportdataonly;
	}
	

}
