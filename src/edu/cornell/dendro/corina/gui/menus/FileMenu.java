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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasProject;
import org.tridas.io.util.SafeIntYear;
import org.tridas.io.util.TridasUtils;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.bulkImport.view.BulkImportWindow;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.editor.ScanBarcodeUI;
import edu.cornell.dendro.corina.editor.EditorFactory.BarcodeDialogResult;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.CanOpener;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.ImportFrame;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.gui.dbbrowse.MetadataBrowser;
import edu.cornell.dendro.corina.io.AbstractDendroReaderFileFilter;
import edu.cornell.dendro.corina.io.DendroReaderFileFilter;
import edu.cornell.dendro.corina.io.ExportDialog;
import edu.cornell.dendro.corina.io.WrongFiletypeException;
import edu.cornell.dendro.corina.manip.Sum;
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

// TODO:
// -- refactor so Editor can use it (export)
// -- use it in Editor
// -- use it in Map, etc.
// -- remove all file-menu classes/methods from XMenubar
// -- (is the font stuff set properly here?)
// BUG: when you open something from the browser, it doesn't get added to the recent-open list
// -- and probably also other places.  move that to Editor's c'tor?

// a file menu.  default is:
// - new
// -   sample
// -   sum
// -   plot
// -   grid
// -   map
// - open...
// - browse...
// - open recent...
// ---
// - close
// - save * -- only if the frame is a SaveableDocument
// - save as...
// ---
// - page setup... * -- only if the frame is a PrintableDocument
// - print...
// ---
// - exit * - except on mac os (which has its own)

// (also, add "find..." menuitem?  or should that be part of the browser only?)
// use: file.add(Builder.makeMenuItem("find...", "new corina.search.SearchDialog()"));

// special file menus:
// - editor adds "export..."
// - map adds "export png...", "export svg..." (combine?)

public class FileMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	protected JFrame f;

	public FileMenu(JFrame f) {
		super(I18n.getText("menus.file"));
		// FIXME: i18n: simply getText() doesn't do _F_ile on win32
		// TODO: set font here, maybe
		// QUESTION: does JMenuBar.setFont() affect all menus and menuitems?
		// -- if so, that makes some things much easier...

		this.f = f;

		addNewOpenMenus();
		addSeparator();
		addIOMenus();
		addExportMenus();
		addSeparator();
		addDataEntryMenus();
		addSeparator();
		addCloseSaveMenus();
		addSeparator();
		addPrintingMenus();
		// TODO: each one of these should either be:
		// (1) addOneSingleMenu(), or
		// (2) addThisGroupOfMenus(), which adds a clump at a time.
		// that way, subclasses can override any single menu or any group, as desired.
		// oh, i should put all these add...() methods in an init() method.
		addExitMenu();
	}

	/*
	 BETTER: spec needs to only be something like

	 <menuitem text="new" enabled="false"/>
	 <menuitem text="sample..." indent="true" action="new corina.editor.Editor()"/>
	 <menuitem text="plot..." indent="true" action="new corina.graph.GraphFrame()"/>
	 */

	// sample, sum, plot, grid, map
	// (future: sample, sum, plot, crossdate, map?)
	
	public void addIOMenus(){
		
		add(Builder.makeMenuItem("menus.file.import", "edu.cornell.dendro.corina.gui.menus.FileMenu.importdbwithbarcode()", "fileimport.png"));		
	}
	
	public void addNewOpenMenus() {
		
		//add(Builder.makeMenuItem("dbnew...", "edu.cornell.dendro.corina.gui.menus.FileMenu.newdb()", "filenew.png"));
		add(Builder.makeMenuItem("menus.file.new", "edu.cornell.dendro.corina.editor.EditorFactory.newSeries()", "filenew.png"));
		add(Builder.makeMenuItem("menus.file.open", "edu.cornell.dendro.corina.gui.menus.FileMenu.opendb()", "fileopen.png"));		
		add(Builder.makeMenuItem("menus.file.openmulti", "edu.cornell.dendro.corina.gui.menus.FileMenu.opendbmulti()", "folder_documents.png"));	
		add(OpenRecent.makeOpenRecentMenu());

	}
	
	public void addExportMenus(){
		add(Builder.makeMenuItem("menus.file.export", "edu.cornell.dendro.corina.gui.menus.FileMenu.exportMultiDB()", "fileexport.png"));		
	}
	
	public void addDataEntryMenus(){
		add(Builder.makeMenuItem("menus.file.bulkimport", "edu.cornell.dendro.corina.bulkImport.view.BulkImportWindow.main()", "bulkDataEntry.png"));

	}

	// ask the user for a file to open, and open it
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
		JFileChooser fc = new JFileChooser();
		for (String readername : TridasIO.getSupportedReadingFormats())
		{
			AbstractDendroReaderFileFilter filter = new DendroReaderFileFilter(readername);
			fc.addChoosableFileFilter(filter);
		}
		int returnVal = fc.showOpenDialog(null);
			
		// Get details from user
		String fullFilename = null;
		String filename = null;
		String type = null;
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        type = fc.getFileFilter().getDescription();
	        
	        fullFilename = file.getPath();
	        filename = file.getName();
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
		TridasProject project = reader.getProject();	
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
	

	public void addCloseSaveMenus() {
		addCloseMenu();
		addSaveMenu();
		//addSaveAsMenu();
	}

	public void addSaveMenu() {
		JMenuItem save = Builder.makeMenuItem("menus.file.save", true, "filesave.png");

		if (f instanceof SaveableDocument) {
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
		} else {
			// dim menuitems
			save.setEnabled(false);
		}

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
		// close menu
		// -- DESIGN: don't i need an XFrame for this? ouch, that's a harsh restriction.
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

	// ***: make this friendlier/more useful!
	// "an error in the print system caused printing to be aborted.  this could indicate
	// a hardware or system problem, or a bug in Corina."  ( details ) (( ok ))
	// (details->stacktrace) -- create static String Bug.getStackTrace(ex)

	private PrinterJob printJob = null;

	private PageFormat pageFormat = new PageFormat();

	// add:
	// ---
	// - quit
	// (unless this is a mac)
	public void addExitMenu() {
		if (!App.platform.isMac()) {
			addSeparator();
			add(Builder.makeMenuItem("menus.file.quit", "edu.cornell.dendro.corina.gui.XCorina.quit()", "exit.png"));
		}
	}
	
}
