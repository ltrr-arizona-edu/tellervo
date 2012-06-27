/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui.menus;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.bulkImport.control.BulkImportController;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.core.AppModel.NetworkStatus;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.editor.ScanBarcodeUI;
import org.tellervo.desktop.editor.EditorFactory.BarcodeDialogResult;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.CanOpener;
import org.tellervo.desktop.gui.FileDialog;
import org.tellervo.desktop.gui.ImportFrame;
import org.tellervo.desktop.gui.LoginDialog;
import org.tellervo.desktop.gui.PrintableDocument;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.gui.XFrame;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.gui.menus.actions.ExportDataAction;
import org.tellervo.desktop.gui.menus.actions.PrintAction;
import org.tellervo.desktop.gui.menus.actions.SaveAction;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.io.AbstractDendroReaderFileFilter;
import org.tellervo.desktop.io.DendroReaderFileFilter;
import org.tellervo.desktop.io.ExportDialog;
import org.tellervo.desktop.io.WrongFiletypeException;
import org.tellervo.desktop.io.control.IOController;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.io.view.ImportView;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.CorinaWsiTridasElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementFactory;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.Overwrite;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tellervo.desktop.wsi.util.WSCookieStoreHandler;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.SafeIntYear;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;



public class FileMenu extends JMenu {

	private static final long serialVersionUID = 1L;
	protected JFrame f;
	private PrinterJob printJob = null;
	private PageFormat pageFormat = new PageFormat();
	
	protected JMenuItem logoff;
	protected JMenuItem logon;
	protected JMenuItem filenew;
	protected JMenuItem fileopen;
	protected JMenuItem fileopenmulti;
	protected JMenuItem openrecent;
	protected JMenuItem fileexport;
	protected JMenuItem fileimport;
	protected JMenuItem fileimportdataonly;
	protected JMenuItem bulkentry;
	protected JMenuItem save;
	
	public FileMenu(JFrame f) {
		super(I18n.getText("menus.file"));
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		this.f = f;

		addNewOpenMenus();
			addSeparator();
		addIOMenus();
			addSeparator();
		addCloseMenu();
		addSaveMenu();
			addSeparator();
		addPrintingMenus();
		addExitMenu();
		
		// Link to the app model so we can enable/disable depending on network status
		linkModel();
	}

	
	public void addIOMenus(){
		
		fileimport = Builder.makeMenu("menus.file.import", "fileimport.png");
		
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
		
		
		
		fileimportdataonly = Builder.makeMenu("menus.file.importdataonly", "fileimport.png");
		
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
				        ImportDataOnly importDialog = new ImportDataOnly(f, file, s);
				        
						// Remember this folder for next time
						App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
					    
				    } else {
				    	return;
				    }

					
				}
				
			});
			
			fileimportdataonly.add(importitem);
		}
		add(fileimportdataonly);
		add(fileimport);
		
		
		addExportMenus();

		
		bulkentry = Builder.makeMVCMenuItem("menus.file.bulkimport", BulkImportController.DISPLAY_BULK_IMPORT, "bulkDataEntry.png");
		add(bulkentry);
	}
	
	public void addExportMenus(){
		Action exportAction = new ExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		fileexport = new JMenuItem(exportAction);
		add(fileexport);
	}

	public void addNewOpenMenus() {
		
		filenew = Builder.makeMenuItem("menus.file.new", true, "filenew.png");
		filenew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				org.tellervo.desktop.editor.EditorFactory.newSeries(f);
			}
		});
		add(filenew);
		
		fileopen = Builder.makeMenuItem("menus.file.open", "org.tellervo.desktop.gui.menus.FileMenu.opendb()", "fileopen.png");
		add(fileopen);
		
		fileopenmulti = Builder.makeMenuItem("menus.file.openmulti", "org.tellervo.desktop.gui.menus.FileMenu.opendbmulti()", "folder_documents.png");
		add(fileopenmulti);
		
		openrecent = OpenRecent.makeOpenRecentMenu();
		add(openrecent);

	}
	



	public void addSaveAsMenu() {
		JMenuItem saveAs = Builder.makeMenuItem("menus.file.saveas");

		if (f instanceof SaveableDocument
				&& ((SaveableDocument) f).isNameChangeable()) {
			// add menuitems, hooked up to |f|
			saveAs.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						// get doc
						SaveableDocument doc = (SaveableDocument) f;

						// DESIGN: start out in the same folder as the old filename,
						// if there is one?

						// get new filename
						String filename = FileDialog.showSingle(I18n
								.getText("menus.file.save"));
						Overwrite.overwrite(filename); // (may abort)
						doc.setFilename(filename);

						// save
						doc.save();
						OpenRecent.fileOpened(doc.getFilename());
					} catch (UserCancelledException uce) {
						// do nothing
					}
				}
			});
		} else {
			// dim menuitems
			saveAs.setEnabled(false);
		}

		add(saveAs);
	}

	public void addCloseMenu() {
		JMenuItem close = Builder.makeMenuItem("menus.file.close", true, "fileclose.png");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// call close() on XFrame (asks for confirmation), else dispose().
				// -- yeah, it should be able to call dispose() everywhere,
				// but see XFrame for why i haven't done that yet.
				if (f instanceof XFrame)
					((XFrame) f).close();
				else
					f.dispose();
			}
		});
		add(close);
	}

	// add:
	// - page setup...
	// - print...
	// (but only enabled if |f| is a printabledocument)
	public void addPrintingMenus() {
		addPageSetupMenu();
		addPrintMenu();
	}

	public void addPageSetupMenu() {
		// menuitem
		JMenuItem setup = Builder.makeMenuItem("menus.file.pagesetup", true, "pagesetup.png");

		if (f instanceof PrintableDocument) {
			// page setup
			setup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					pageSetup();
				}
			});
		} else {
			// dim menuitem
			setup.setEnabled(false);
		}

		add(setup);
	}
	public void addExitMenu() {
		
		addSeparator();

		logoff = Builder.makeMenuItem("menus.file.logoff", true, "logoff.png");
		add(logoff);
		logoff.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WSCookieStoreHandler.getCookieStore().emptyCookieJar();
				App.appmodel.setNetworkStatus(NetworkStatus.OFFLINE);
			}
		});
		
		
		logon = Builder.makeMenuItem("menus.file.logon", true, "logon.png");
		add(logon);
		logon.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
            	LoginDialog dlg = new LoginDialog(f);
            	try {
            		dlg.doLogin(null, false);            		
               	} catch (UserCancelledException uce) {
            		return;
            	}
			}
		});
		
		// Add exit button if not on Mac
		if (!Platform.isMac()) {
			add(Builder.makeMenuItem("menus.file.quit", "org.tellervo.desktop.gui.TellervoMainWindow.quit()", "exit.png"));
		}
		
	}
	

	// COMMANDS FOR DOING THINGS!

	public static void open() {
		String filename = "";
		
		try {
			filename = FileDialog.showSingle(I18n.getText("menus.file.open"));
			// get filename, and load
			CanOpener.open(filename);
		} catch (UserCancelledException uce) {
			// do nothing
		} catch (WrongFiletypeException wfte) {
			Alert.error(I18n.getText("error.cantOpenFile"), I18n.getText("error.cantOpenFile") + " " +
					new File(filename).getName() + ".\n" +
					I18n.getText("error.fileTypeNotRecognized"));
		} catch (IOException ioe) {
			// BUG: this should never happen.  loading is so fast, it'll get displayed
			// in the preview component, and if it can't be loaded, "ok" should be dimmed.
			// (is that possible with jfilechooser?)
			Alert.error(I18n.getText("error.ioerror"), I18n.getText("error.cantOpenFile") + filename + ":\n" + ioe.getMessage());
			// (so why not use Bug.bug()?)
		}
	}

	// ask the user for a list of files to open
	public static void openMulti() {
		try {

			/*
			 * This is a bit kludgy, but work around our existing framework:
			 * get a list of elements; open each.
			 */

			ElementList elements = FileDialog.showMulti(I18n.getText("menus.file.open"));

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);

				if (!elements.isActive(e)) // skip inactive
					continue;

				try {
					CanOpener.open(e.getName());
				} catch (WrongFiletypeException wfte) {
					Alert.error(I18n.getText("error.cantOpenFile"), I18n.getText("error.cantOpenFile") + " " + e.getShortName() + ":\n"
							+ I18n.getText("error.fileTypeNotRecognized"));
				} catch (IOException ioe) {
					Alert.error(I18n.getText("error.cantOpenFile"), I18n.getText("error.cantOpenFile") + " " + e.getShortName() + ":\n"
							+ ioe.getMessage());
				}
			}
		} catch (UserCancelledException uce) {
			// do nothing
		}
	}

	public static void opendbmulti() {
		opendb(true);
	}

	public static void opendb() {
		opendb(false);
	}
	
	public static void opendb(boolean multi) {
		DBBrowser browser = new DBBrowser((Frame) null, true, multi);
		
		browser.setVisible(true);
		
		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
			ElementList toOpen = browser.getSelectedElements();
			
			for(Element e : toOpen) {
				// load it
				Sample s;
				try {
					s = e.load();
				} catch (IOException ioe) {
					Alert.error(I18n.getText("error.loadingSample"),
							I18n.getText("error.cantOpenFile") +":" + ioe.getMessage());
					continue;
				}

				OpenRecent.sampleOpened(new SeriesDescriptor(s));
				
				// open it
				new Editor(s);
			}
		}
	}

	public static void exportMultiDB(){
		DBBrowser browser = new DBBrowser((Frame) null, true, true);
		browser.setVisible(true);

		
		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
			ElementList toOpen = browser.getSelectedElements();
		
			/*
			List<Sample> samples = new ArrayList<Sample>();

			for(Element e : toOpen) {
				// load it
				Sample s;
				try {
					s = e.load();
					
				} catch (IOException ioe) {
					Alert.error("Error Loading Sample",
							"Can't open this file: " + ioe.getMessage());
					continue;
				}
				samples.add(s);

				OpenRecent.sampleOpened(new SeriesDescriptor(s));
			}
			
			
			
			// no samples => don't bother doing anything
			if (samples.isEmpty()) {
				return;
			}

			// ok, now we have a list of valid samples in 'samples'...
			// so open export dialog box
			*/
			
			new ExportDialog(toOpen);

		}
	}
	
	
	public static void importdbwithbarcode(){
			
		// Set up file chooser and filters
		
		String format = App.prefs.getPref(PrefKey.IMPORT_FORMAT, null);
	
		JFileChooser fc = new JFileChooser();
		for (String readername : TridasIO.getSupportedReadingFormats())
		{
			AbstractDendroReaderFileFilter filter = new DendroReaderFileFilter(readername);
			fc.addChoosableFileFilter(filter);
			if(filter.getDescription().equals(format))
			{
				fc.setFileFilter(filter);
			}
		}
		
		
		// Open at last used directory
		if (App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null) != null) {
			fc.setCurrentDirectory(new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null)));
		}
		
		int returnVal = fc.showOpenDialog(null);
			


		
		// Get details from user
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        DendroReaderFileFilter filter = (DendroReaderFileFilter) fc.getFileFilter();
	        

	        
	        try {
				ImportDataOnly importDialog = new ImportDataOnly(null, file, filter);
				// Remember this folder for next time
				App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
				
			} catch (Exception e) {
				Alert.error("Error", e.getMessage());
			}
	        

		    
	    } else {
	    	return;
	    }
		
		
		
		
		
		
		
		
		// Get details from user
		String fullFilename = null;

		String type = null;
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        type = fc.getFileFilter().getDescription();
	        
	        fullFilename = file.getPath();
			
	        // Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
			
			// Remember this format for next time
			App.prefs.setPref(PrefKey.IMPORT_FORMAT, type);

	    } else {
	    	return;
	    }

		// Set up reader
	    AbstractDendroFileReader reader;
		reader = TridasIO.getFileReader(type);
		if(reader==null)
		{
			reader = TridasIO.getFileReaderFromExtension(fullFilename
					.substring(fullFilename.lastIndexOf(".") + 1));
		}
		
		
	}
	
	public void pageSetup() {
		// make printer job, if none exists yet
		if (printJob == null)
			printJob = PrinterJob.getPrinterJob();

		// get page format
		pageFormat = printJob.pageDialog(pageFormat);
	}

	public void addPrintMenu() {
		// menuitem
		PrintAction printAction = new PrintAction(new Sample());
		JMenuItem print = new JMenuItem(printAction);
		print.setEnabled(false);
		add(print);
	}



	  protected void linkModel()
	  {
		  App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent argEvt) {
					if(argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)){
						setMenusForNetworkStatus();
					}	
				}
			});
		  
		  setMenusForNetworkStatus();
	  }
	  
	  protected void setMenusForNetworkStatus()
	  {

		  logoff.setVisible(App.isLoggedIn());  
		  logon.setVisible(!App.isLoggedIn());  
		  fileopen.setEnabled(App.isLoggedIn());
		  filenew.setEnabled(App.isLoggedIn());
		  fileopenmulti.setEnabled(App.isLoggedIn());
		  openrecent.setEnabled(App.isLoggedIn());
		  fileimport.setEnabled(App.isLoggedIn());
		  fileimportdataonly.setEnabled(App.isLoggedIn());
		  fileexport.setEnabled(App.isLoggedIn());
		  bulkentry.setEnabled(App.isLoggedIn());
		  save.setEnabled(App.isLoggedIn() && f instanceof SaveableDocument);

	  }
	  
		public void addSaveMenu() {
			
			Action saveAction = new SaveAction(f);
			save = new JMenuItem(saveAction);
			add(save);
		}
}



