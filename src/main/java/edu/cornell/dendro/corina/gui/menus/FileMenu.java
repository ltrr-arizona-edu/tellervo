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
package edu.cornell.dendro.corina.gui.menus;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.SafeIntYear;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasProject;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.bulkImport.control.BulkImportController;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.core.AppModel;
import edu.cornell.dendro.corina.core.AppModel.NetworkStatus;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.editor.ScanBarcodeUI;
import edu.cornell.dendro.corina.editor.EditorFactory.BarcodeDialogResult;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.CanOpener;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.ImportFrame;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.io.AbstractDendroReaderFileFilter;
import edu.cornell.dendro.corina.io.DendroReaderFileFilter;
import edu.cornell.dendro.corina.io.ExportDialog;
import edu.cornell.dendro.corina.io.WrongFiletypeException;
import edu.cornell.dendro.corina.io.control.IOController;
import edu.cornell.dendro.corina.io.view.ImportView;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.Overwrite;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import edu.cornell.dendro.corina.wsi.util.WSCookieStoreHandler;


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
	protected JMenuItem bulkentry;
	protected JMenuItem save;
	
	public FileMenu(JFrame f) {
		super(I18n.getText("menus.file"));

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
					JFileChooser fc = new JFileChooser();
					AbstractDendroFileReader reader = TridasIO.getFileReader(s);
					DendroFileFilter filter = reader.getDendroFileFilter();

					fc.addChoosableFileFilter(filter);
					fc.setFileFilter(filter);
					
					int returnVal = fc.showOpenDialog(null);
						
					// Get details from user
				    if (returnVal == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        
				        
				        ImportView importDialog = new ImportView(file, s);

						importDialog.setVisible(true);
				        
				 
					    
				    } else {
				    	return;
				    }

					
				}
				
			});
			
			fileimport.add(importitem);
		}
		
		//fileimport = Builder.makeMenuItem("menus.file.import", "edu.cornell.dendro.corina.gui.menus.FileMenu.importdbwithtricycle()", "fileimport.png");
		add(fileimport);
		
		addExportMenus();

		
		bulkentry = Builder.makeMVCMenuItem("menus.file.bulkimport", BulkImportController.DISPLAY_BULK_IMPORT, "bulkDataEntry.png");
		add(bulkentry);
	}
	
	public void addExportMenus(){
		fileexport = Builder.makeMVCMenuItem("menus.file.export", IOController.OPEN_EXPORT_WINDOW, "fileexport.png");
		add(fileexport);
	}

	public void addNewOpenMenus() {
				
		filenew = Builder.makeMenuItem("menus.file.new", true, "filenew.png");
		filenew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edu.cornell.dendro.corina.editor.EditorFactory.newSeries(f);
			}
		});
		add(filenew);
		
		fileopen = Builder.makeMenuItem("menus.file.open", "edu.cornell.dendro.corina.gui.menus.FileMenu.opendb()", "fileopen.png");
		add(fileopen);
		
		fileopenmulti = Builder.makeMenuItem("menus.file.openmulti", "edu.cornell.dendro.corina.gui.menus.FileMenu.opendbmulti()", "folder_documents.png");
		add(fileopenmulti);
		
		openrecent = OpenRecent.makeOpenRecentMenu();
		add(openrecent);

	}
	

	public void addSaveMenu() {
		save = Builder.makeMenuItem("menus.file.save", true, "filesave.png");
		
		// add menuitems, hooked up to |f|
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get doc
				SaveableDocument doc = (SaveableDocument) f;

				// save
				doc.save();

				// add to the recently opened files list if the user actually saved
				// also, the user can try to save a document they didn't do anything to. argh.
				if (doc.isSaved() && doc.getFilename() != null) {
					if(doc.getSavedDocument() instanceof Sample)
						OpenRecent.sampleOpened(new SeriesDescriptor((Sample) doc.getSavedDocument()));
					else
						OpenRecent.fileOpened(doc.getFilename());
				}
			}
		});
		
		add(save);
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
		JMenuItem close = Builder.makeMenuItem("menus.file.close", false, "fileclose.png");
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
			add(Builder.makeMenuItem("menus.file.quit", "edu.cornell.dendro.corina.gui.XCorina.quit()", "exit.png"));
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
	
	public static void importdbwithtricycle()
	{
		// Set up file chooser and filters
		JFileChooser fc = new JFileChooser();
		for (String readername : TridasIO.getSupportedReadingFormats())
		{
			AbstractDendroReaderFileFilter filter = new DendroReaderFileFilter(readername);
			fc.addChoosableFileFilter(filter);
		}
		int returnVal = fc.showOpenDialog(null);
			
		// Get details from user
		String type = null;
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        type = fc.getFileFilter().getDescription();
	        
	        ImportView importDialog = new ImportView(file, type);

			importDialog.setVisible(true);
	        
	 
		    
	    } else {
	    	return;
	    }


	    
	}
	
	public static void importdbwithbarcode(){
			
		// Set up file chooser and filters
		JFileChooser fc = new JFileChooser();
		for (String readername : TridasIO.getSupportedReadingFormats())
		{
			AbstractDendroReaderFileFilter filter = new DendroReaderFileFilter(readername);
			fc.addChoosableFileFilter(filter);
		}
		int returnVal = fc.showOpenDialog(null);
			
		// Get details from user
		String fullFilename = null;

		String type = null;
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        type = fc.getFileFilter().getDescription();
	        
	        fullFilename = file.getPath();

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
		
		// Load file
		try {
			reader.loadFile(fullFilename);
		} catch (IOException e1) {
			System.out.println(e1.toString());
			e1.printStackTrace();
			Alert.error("Error", e1.getLocalizedMessage());
			return;
		} catch (InvalidDendroFileException e) {
			System.out.println(e.toString());
			e.printStackTrace();
			Alert.error("Error", e.getLocalizedMessage());
			return;
		}
		
		// Extract project
		TridasProject project = reader.getProjects()[0];	
		ArrayList<TridasMeasurementSeries> seriesList = TridasUtils.getMeasurementSeriesFromTridasProject(project);
		
		if(seriesList.size()==0) 
		{
			Alert.error("Error", "No series in file");
			return;
		}
		
		TridasMeasurementSeries series = seriesList.get(0);
		
		// make dataset ref, based on our series
		Sample sample = new Sample(series);
		
		// Set range from series
		SafeIntYear startYear = new SafeIntYear(1001);
		if(series.isSetInterpretation())
		{
			if (series.getInterpretation().isSetFirstYear())
			{
				 startYear = new SafeIntYear(series.getInterpretation().getFirstYear());
			}
		}
		SafeIntYear endYear = startYear.add(series.getValues().get(0).getValues().size());
		Range rng = new Range(new Year(startYear.toString()), new Year(endYear.toString()));
		sample.setRange(rng);
		
		// Set filename
		sample.setMeta("filename", fullFilename);
		
		// setup our loader and series identifier
		CorinaWsiTridasElement.attachNewSample(sample);
		

		final JDialog dialog = new JDialog();
		final ScanBarcodeUI barcodeUI = new ScanBarcodeUI(dialog);
		
		dialog.setContentPane(barcodeUI);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setModal(true);
		Center.center(dialog);
		dialog.setVisible(true);
		BarcodeDialogResult result = barcodeUI.getResult();

		if(!result.barcodeScanSuccessful())
		{
			// start the import dialog with no barcode info   
		    ImportFrame importdialog = new ImportFrame(sample);
		    importdialog.setVisible(true);			
		}
		else{
			// start the import dialog with barcode info 
		    ImportFrame importdialog = new ImportFrame(sample, result);
		    importdialog.setVisible(true);
		}
		
		
		
	}
	
	public static void importdbwithbarcodeold(){
		String filename = "";
		
		try {
			filename = FileDialog.showSingle(I18n.getText("menus.file.import"), I18n.getText("menus.file.import"));
			// get filename, and load
			Element e = ElementFactory.createElement(filename);
		    Sample s = e.load();
		    
			final JDialog dialog = new JDialog();
			final ScanBarcodeUI barcodeUI = new ScanBarcodeUI(dialog);
			
			dialog.setContentPane(barcodeUI);
			dialog.setResizable(false);
			dialog.pack();
			dialog.setModal(true);
			Center.center(dialog);
			dialog.setVisible(true);
			BarcodeDialogResult result = barcodeUI.getResult();

			if(!result.barcodeScanSuccessful())
			{
				// start the import dialog with no barcode info   
			    ImportFrame importdialog = new ImportFrame(s);
			    importdialog.setVisible(true);
			}
			else{
				// start the import dialog with barcode info 
			    ImportFrame importdialog = new ImportFrame(s, result);
			    importdialog.setVisible(true);
			}
			
			
		    
		} catch (UserCancelledException uce) {
			// do nothing
		} catch (WrongFiletypeException wfte) {
			Alert.error(I18n.getText("error.cantOpenFile"), I18n.getText("error.cantOpenFile") + ":\n"
					+ I18n.getText("error.fileTypeNotRecognized"));
		} catch (IOException ioe) {
			// BUG: this should never happen.  loading is so fast, it'll get displayed
			// in the preview component, and if it can't be loaded, "ok" should be dimmed.
			// (is that possible with jfilechooser?)
			Alert.error(I18n.getText("error.cantOpenFile"), I18n.getText("error.cantOpenFile") + ":\n"
					+ ioe.getMessage());
			// (so why not use Bug.bug()?)
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
		JMenuItem print = Builder.makeMenuItem("menus.file.print", true, "printer.png");

		if (f instanceof PrintableDocument) {
			// print
			print.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print();
				}
			});
		} else {
			// dim menuitem
			print.setEnabled(false);
		}

		add(print);
	}

	public void print() {
		// page setup wasn't run?  do it now.
		if (printJob == null)
			printJob = PrinterJob.getPrinterJob();
		if (pageFormat == null)
			pageFormat = printJob.pageDialog(pageFormat);
		if (pageFormat == null)
			return; // null=cancel

		PrintableDocument doc = (PrintableDocument) f;

		Object p = doc.getPrinter(pageFormat);

		// let the printable object cancel.
		if (p == null)
			return;

		if (p instanceof Printable)
			printJob.setPrintable((Printable) p);
		else if (p instanceof Pageable)
			printJob.setPageable((Pageable) p);
		else
			new Bug(new IllegalArgumentException(
					I18n.getText("error.notPrintable")));

		// job title
		printJob.setJobName(doc.getPrintTitle());

		// ask user options
		if (!printJob.printDialog())
			return; // false=cancel

		// print (in background thread)
		(new Thread() {
			@Override
			public void run() {
				try {
					printJob.print();
				} catch (PrinterAbortException pae) {
					// do nothing, the user already knows
				} catch (PrinterException pe) {
					// ***
					Alert.error(I18n.getText("error.printing"), I18n.getText("error.printing")
							+ pe.getMessage());
				}
			}
		}).start();
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
		  fileopenmulti.setEnabled(App.isLoggedIn());
		  openrecent.setEnabled(App.isLoggedIn());
		  fileimport.setEnabled(App.isLoggedIn());
		  fileexport.setEnabled(App.isLoggedIn());
		  bulkentry.setEnabled(App.isLoggedIn());
		  save.setEnabled(App.isLoggedIn() && f instanceof SaveableDocument);

	  }
}
