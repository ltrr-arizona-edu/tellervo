package org.tellervo.desktop.editor;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.io.view.ImportView;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;

public class TellervoMenuBar extends JMenuBar{

	private static final long serialVersionUID = 1L;
	private JMenuItem miOpenMulti;
	private JMenuItem miLogoff;
	private JMenuItem miLogon;
	private JMenuItem miExportData;
	private JMenuItem miBulkDataEntry;
	private JMenuItem miDesignODKForm;

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
	private JMenu mnView;
	private JMenu mnTools;
	JMenuItem miComponentSeries;

	public TellervoMenuBar()
	{
		parent = null;
	}
	
	public TellervoMenuBar(EditorActions actions, Window editor)
	{
		this.parent = editor;

		// FILE MENU
		JMenu mnFile = new JMenu("File");

		JMenuItem miNew = new JMenuItem(actions.fileNewAction);
		mnFile.add(miNew);

		JMenuItem miOpen = new JMenuItem(actions.fileOpenAction);
		mnFile.add(miOpen);

		miOpenMulti = new JMenuItem(actions.fileOpenMultiAction);
		mnFile.add(miOpenMulti);
		
		JMenu openrecent = OpenRecent.makeOpenRecentMenu();
		mnFile.add(openrecent);
		
		mnFile.addSeparator();

		
		mnFile.add(getImportDataOnlyMenu());
		mnFile.add(getImportDataAndMetadataMenu());


		miExportData = new JMenuItem(actions.fileExportDataAction);
		mnFile.add(miExportData);

		// JMenuItem miExportMap = new JMenuItem(actions.fileExportMapAction);
		// mnFile.add(miExportMap);

		mnFile.addSeparator();

		
		miBulkDataEntry = new JMenuItem(actions.fileBulkDataEntryAction);
		mnFile.add(miBulkDataEntry);

		miDesignODKForm = new JMenuItem(actions.fileDesignODKFormAction);
		mnFile.add(miDesignODKForm);

		JMenuItem miSave = new JMenuItem(actions.fileSaveAction);
		mnFile.add(miSave);
		
		JMenuItem miSaveAs = new JMenuItem(actions.fileSaveAsAction);
		mnFile.add(miSaveAs);

		mnFile.addSeparator();

		JMenuItem miPrint = new JMenuItem(actions.filePrintAction);
		mnFile.add(miPrint);

		mnFile.addSeparator();

		miLogoff = new JMenuItem(actions.fileLogoffAction);
		mnFile.add(miLogoff);

		miLogon = new JMenuItem(actions.fileLogonAction);
		mnFile.add(miLogon);

		JMenuItem miExit = new JMenuItem(actions.fileExitAction);
		mnFile.add(miExit);

		add(mnFile);

		
		// EDIT MENU
		
		JMenu mnEdit = new JMenu("Edit");
		add(mnEdit);

		JMenuItem miCopy = new JMenuItem(actions.editCopyAction);
		mnEdit.add(miCopy);

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

		JMenuItem miDeleteYear = new JMenuItem(actions.editDeleteAction);
		mnEdit.add(miDeleteYear);

		JMenuItem miInsertYears = new JMenuItem(actions.editInsertYearsAction);
		mnEdit.add(miInsertYears);

		mnEdit.addSeparator();

		JMenuItem miInitializeDataGrid = new JMenuItem(actions.editInitGridAction);
		mnEdit.add(miInitializeDataGrid);

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
		miLabels.add(miBoxLabel);
		miLabels.add(miBasicBoxLabel);
		miLabels.add(miSampleBoxLabel);
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

		// VIEW MENU
		
		mnView = new JMenu("View");
		add(mnView);

		
		// TOOLS MENU
		
		mnTools = new JMenu("Tools");
		add(mnTools);

		
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
		
		
		linkModel();
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
		
		for (final String s : TridasIO.getSupportedReadingFormats()) {
			
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
				        
				        ImportDataOnly importDialog = new ImportDataOnly(parent, file, s);
				        
				        importDialog.openEditors();

				        
						// Remember this folder for next time
						App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
					    
				    } else {
				    	return;
				    }

					
				}
				
			});
			
			fileimportdataonly.add(importitem);
		}
		return fileimportdataonly;
	}
	


	
	private void setGUILoggedIn(Boolean loggedin)
	{
		this.miLogon.setVisible(loggedin);
		this.miLogoff.setVisible(!loggedin);
	}
	
	private void setTellervoFullMode(Boolean fullMode)
	{
		this.miOpenMulti.setVisible(fullMode);
		this.miExportData.setVisible(fullMode);
		this.miBulkDataEntry.setVisible(fullMode);
		this.miDesignODKForm.setVisible(fullMode);
		this.mnAdministration.setVisible(fullMode);
		
		this.miUsersAndGroups.setVisible(fullMode);
		this.miEditViewPermissions.setVisible(fullMode);
		this.miChangePassword.setVisible(fullMode);
		this.miForgetPassword.setVisible(fullMode);
		this.miReports.setVisible(fullMode);
		this.miLabels.setVisible(fullMode);
		this.miCurationMenu.setVisible(fullMode);
		
		this.miMetaDB.setVisible(fullMode);
		this.miSiteMap.setVisible(fullMode); 
		
		this.mnView.setVisible(fullMode);
		this.mnTools.setVisible(fullMode);
		
		this.miReportBugOnLastTransaction.setVisible(fullMode);
		this.miXMLCommunicationsViewer.setVisible(fullMode);
		this.miComponentSeries.setVisible(fullMode);
		
	}
	

	
	protected void linkModel() {
		App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if (argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)) {
					setGUILoggedIn(App.isLoggedIn());
				}
			}
		});

		setGUILoggedIn(App.isLoggedIn());

		if (App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false)) {
			setTellervoFullMode(false);
		} else {
			setTellervoFullMode(true);
		}
	}
}
