package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
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


	public TellervoMenuBar(EditorActions actions)
	{

		// FILE MENU
		JMenu mnFile = new JMenu("File");

		JMenuItem miNew = new JMenuItem(actions.fileNewAction);
		mnFile.add(miNew);

		JMenuItem miOpen = new JMenuItem(actions.fileOpenAction);
		mnFile.add(miOpen);

		JMenuItem miOpenMulti = new JMenuItem(actions.fileOpenMultiAction);
		mnFile.add(miOpenMulti);
		
		JMenu openrecent = OpenRecent.makeOpenRecentMenu();
		mnFile.add(openrecent);
		
		mnFile.addSeparator();

		
		mnFile.add(getImportDataOnlyMenu());
		mnFile.add(getImportDataAndMetadataMenu());


		JMenuItem miExportData = new JMenuItem(actions.fileExportDataAction);
		mnFile.add(miExportData);

		// JMenuItem miExportMap = new JMenuItem(actions.fileExportMapAction);
		// mnFile.add(miExportMap);

		mnFile.addSeparator();

		
		JMenuItem miBulkDataEntry = new JMenuItem(actions.fileBulkDataEntryAction);
		mnFile.add(miBulkDataEntry);

		JMenuItem miDesignODKForm = new JMenuItem(actions.fileDesignODKFormAction);
		mnFile.add(miDesignODKForm);

		JMenuItem miSave = new JMenuItem(actions.fileSaveAction);
		mnFile.add(miSave);

		mnFile.addSeparator();

		JMenuItem miPrint = new JMenuItem(actions.filePrintAction);
		mnFile.add(miPrint);

		mnFile.addSeparator();

		JMenuItem miLogoff = new JMenuItem(actions.fileLogoffAction);
		mnFile.add(miLogoff);

		JMenuItem miLogon = new JMenuItem(actions.fileLogonAction);
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

		JMenu mnAdministration = new JMenu("Administration");
		add(mnAdministration);

		// VIEW MENU
		
		JMenu mnView = new JMenu("View");
		add(mnView);

		
		// TOOLS MENU
		
		JMenu mnTools = new JMenu("Tools");
		add(mnTools);

		
		// GRAPH MENU
		
		JMenu mnGraph = new JMenu("Graph");
		add(mnGraph);

		
		// HELP MENU
		
		JMenu mnHelp = new JMenu("Help");
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
				        ImportDataOnly importDialog = new ImportDataOnly(null, file, s);
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
	
}
