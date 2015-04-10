package org.tellervo.desktop.gui.menus;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.openrecent.OpenRecent;

public class LiteEditorMenuBar extends EditorMenuBar{

	private static final long serialVersionUID = 1L;
		
	public LiteEditorMenuBar(LiteEditorActions actions, LiteEditor editor)
	{
		super(actions, editor);
		
		
		// FILE MENU
		JMenu mnFile = new JMenu("File");

		JMenuItem miNew = new JMenuItem(actions.fileNewAction);
		mnFile.add(miNew);

		JMenuItem miOpen = new JMenuItem(actions.fileOpenAction);
		mnFile.add(miOpen);

		
		JMenu openrecent = OpenRecent.makeOpenRecentMenu();
		mnFile.add(openrecent);

		// JMenuItem miExportMap = new JMenuItem(actions.fileExportMapAction);
		// mnFile.add(miExportMap);

		mnFile.addSeparator();

		JMenuItem miSave = new JMenuItem(actions.fileSaveAction);
		mnFile.add(miSave);
		
		JMenuItem miSaveAs = new JMenuItem(actions.fileSaveAsAction);
		mnFile.add(miSaveAs);


		mnFile.addSeparator();


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

		JMenu measureModeMenu = Builder.makeMenu("menus.edit.measuremode");
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
		
		// TOOLS MENU
		
		JMenu mnTools = new JMenu("Tools");
		add(mnTools);
		
		JMenuItem miToolsTruncate = new JMenuItem(actions.toolsTruncateAction);
		mnTools.add(miToolsTruncate);
		
		JMenuItem miToolsReverse = new JMenuItem(actions.toolsReverseAction);
		mnTools.add(miToolsReverse);
		
		JMenuItem miToolsCrossdate = new JMenuItem(actions.toolsCrossdateAction);
		mnTools.add(miToolsCrossdate);
		
		JMenuItem miToolsCrossdateWorkspace = new JMenuItem(actions.toolsCrossdateWorkspaceAction);
		mnTools.add(miToolsCrossdateWorkspace);


		
		// GRAPH MENU
		
		JMenu mnGraph = new JMenu("Graph");
		
		JMenuItem miCurrentSeries = new JMenuItem(actions.graphCurrentSeriesAction);
		mnGraph.add(miCurrentSeries);

	    JMenuItem miAllSeries = new JMenuItem(actions.graphAllSeriesAction);
	    mnGraph.add(miAllSeries);
	    	    
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

		
		mnHelp.addSeparator();
		
		JMenu miDebugInfo = new JMenu("Debug Information");
		
		JMenuItem miErrorLogViewer = new JMenuItem(actions.helpErrorLogViewerAction);
		miDebugInfo.add(miErrorLogViewer);
				

		
		JMenuItem miSystemsInformation = new JMenuItem(actions.helpSystemsInformationAction);
		miDebugInfo.add(miSystemsInformation);
		
		mnHelp.add(miDebugInfo);
		mnHelp.addSeparator();
		
		JMenuItem miAboutTellervo = new JMenuItem(actions.helpAboutTellervoAction);
		mnHelp.add(miAboutTellervo);
		
		
		add(mnHelp);

	}

}
