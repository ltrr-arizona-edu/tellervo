package edu.cornell.dendro.corina.gui.menus;

import java.awt.Frame;
import java.awt.event.ActionEvent;
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

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.CanOpener;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.datawizard.CreateOrImportWizard;
import edu.cornell.dendro.corina.gui.datawizard.SiteEditorPanel;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.gui.newui.HierarchyPanel;
import edu.cornell.dendro.corina.gui.newui.CreateSampleDialog;
import edu.cornell.dendro.corina.manip.Sum;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.LegacySampleExtractor;
import edu.cornell.dendro.corina.util.Overwrite;
import edu.cornell.dendro.corina.io.ExportDialog;

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

	protected JFrame f;

	public FileMenu(JFrame f) {
		super(I18n.getText("file"));
		// FIXME: i18n: simply getText() doesn't do _F_ile on win32
		// TODO: set font here, maybe
		// QUESTION: does JMenuBar.setFont() affect all menus and menuitems?
		// -- if so, that makes some things much easier...

		this.f = f;

		addNewOpenMenus();
		addSeparator();
		addIOMenus();
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

	// *****
	// for indenting menuitems
	private static String INDENT = "    ";

	private static void indent(JMenuItem m) {
		m.setText(INDENT + m.getText());
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
		
		add(Builder.makeMenuItem("dbimport...", "edu.cornell.dendro.corina.gui.menus.FileMenu.importdb()", "fileimport.png"));		
	}
	
	public void addNewOpenMenus() {
		
		/*
		// new sample
		JMenuItem sample = Builder.makeMenuItem("sample...",
				"new edu.cornell.dendro.corina.editor.Editor()");
		indent(sample); // (re-indent!)

		// new sum
		JMenuItem sum = Builder.makeMenuItem("sum...",
			"edu.cornell.dendro.corina.gui.menus.FileMenu.sum()"); // why not "new Sum()"?
		indent(sum);

		// new plot
		JMenuItem plot = Builder.makeMenuItem("plot...",
				"new edu.cornell.dendro.corina.graph.GraphWindow()");
		indent(plot); // (re-indent!)

		// new grid
		// (REMOVE ME)
		// JMenuItem grid = Builder.makeMenuItem("grid...",
		// "new corina.cross.GridFrame()");
		// indent(grid); // (re-indent!)
		JMenuItem crossdate = Builder.makeMenuItem("crossdate...",
				"new edu.cornell.dendro.corina.cross.CrossdateKit()");
		indent(crossdate);

		// new atlas
		JMenuItem map = Builder.makeMenuItem("atlas...",
				"new edu.cornell.dendro.corina.map.MapFrame()");
		indent(map);

		// new site (site editor)
		JMenuItem sites = Builder.makeMenuItem("site...",
				"new edu.cornell.dendro.corina.site.SiteEditor()");
		indent(sites);

		// add menuitems to menu
		add(Builder.makeMenuItem("new", false));
		add(sample);
		add(sum);
		add(plot);
		// add(grid);
		add(crossdate);
		add(map);
		add(sites);
*/
		// db stuff
	//	addSeparator();
		
		
		add(Builder.makeMenuItem("dbnew...", "edu.cornell.dendro.corina.gui.menus.FileMenu.newdb()", "filenew.png"));
		add(Builder.makeMenuItem("dbopen...", "edu.cornell.dendro.corina.gui.menus.FileMenu.opendb()", "fileopen.png"));		
		add(Builder.makeMenuItem("dbmultiopen...", "edu.cornell.dendro.corina.gui.menus.FileMenu.opendbmulti()"));	
		add(OpenRecent.makeOpenRecentMenu());
		//addSeparator();

		
		
		// open, browse
		//add(Builder.makeMenuItem("open...", "edu.cornell.dendro.corina.gui.menus.FileMenu.open()"));
		//add(Builder.makeMenuItem("open_multi...",
		//		"edu.cornell.dendro.corina.gui.menus.FileMenu.openMulti()"));
		//add(Builder.makeMenuItem("bulkexport...",
			//	"edu.cornell.dendro.corina.gui.menus.FileMenu.bulkexport()", "fileexport.png"));
		
		//add(Builder.makeMenuItem("browse...", "new edu.cornell.dendro.corina.browser.Browser();"));
	}

	// ask the user for a file to open, and open it
	public static void open() {
		String filename = "";
		
		try {
			filename = FileDialog.showSingle(I18n.getText("open"));
			// get filename, and load
			CanOpener.open(filename);
		} catch (UserCancelledException uce) {
			// do nothing
		} catch (WrongFiletypeException wfte) {
			Alert.error("Can't open file", "Can't open the file " + 
					new File(filename).getName() + ".\n" +
					"Corina does not recognize that file type.");
		} catch (IOException ioe) {
			// BUG: this should never happen.  loading is so fast, it'll get displayed
			// in the preview component, and if it can't be loaded, "ok" should be dimmed.
			// (is that possible with jfilechooser?)
			Alert.error("I/O Error", "Can't open file " + filename + ":\n" + ioe.getMessage());
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

			ElementList elements = FileDialog.showMulti(I18n.getText("open"));

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);

				if (!elements.isActive(e)) // skip inactive
					continue;

				try {
					CanOpener.open(e.getName());
				} catch (WrongFiletypeException wfte) {
					Alert.error("Can't open file", "Can't open the file " + e.getShortName() + ":\n"
							+ "Unsupported file type.");
				} catch (IOException ioe) {
					Alert.error("I/O Error", "Can't open file " + e.getShortName() + ":\n"
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
					Alert.error("Error Loading Sample",
							"Can't open this file: " + ioe.getMessage());
					continue;
				}

				OpenRecent.sampleOpened(s.getLoader());
				
				// open it
				new Editor(s);
			}
		}
	}

	public static void importdb() {
		String filename = "";
		
		try {
			filename = FileDialog.showSingle("Choose a sample to import");
			// get filename, and load
			Element e = ElementFactory.createElement(filename);
		    Sample s = e.load();
		    
		    CreateOrImportWizard wiz = new CreateOrImportWizard(null, false);

		    // extract info from the sample
		    wiz.setLegacySample(new LegacySampleExtractor(s));

		    wiz.setVisible(true);
		} catch (UserCancelledException uce) {
			// do nothing
		} catch (WrongFiletypeException wfte) {
			Alert.error("Can't open file", "Can't open the file " + 
					new File(filename).getName() + ".\n" +
					"Corina does not recognize that file type.");
		} catch (IOException ioe) {
			// BUG: this should never happen.  loading is so fast, it'll get displayed
			// in the preview component, and if it can't be loaded, "ok" should be dimmed.
			// (is that possible with jfilechooser?)
			Alert.error("I/O Error", "Can't open file " + filename + ":\n" + ioe.getMessage());
			// (so why not use Bug.bug()?)
		}
	}
	
	public static void newdb() {
		CreateOrImportWizard wizardDialog = new CreateOrImportWizard((JFrame)null, false);
		
		wizardDialog.setVisible(true);
	}

	
	
	// ask the user for a list of files to open
	public static void bulkexport() {
		try {
			// get a list of elements
			ElementList elements = FileDialog.showMulti(I18n.getText("bulkexport..."));

			// convert them to samples
			boolean problem = false;
			List<Sample> samples = new ArrayList<Sample>(elements.size());
			String errorsamples = "";

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);

				if (!elements.isActive(e)) // skip inactive
					continue;

				try {
					Sample s = e.load();
					samples.add(s);
				} catch (IOException ioe) {
					problem = true;
					if (errorsamples.length() != 0)
						errorsamples += ", ";
					errorsamples += e.getShortName();
				}
			}

			// problem?
			if (problem) {
				Alert.error("Error loading sample(s):", errorsamples);
				return;
			}

			// no samples => don't bother doing anything
			if (samples.isEmpty()) {
				return;
			}

			// ok, now we have a list of valid samples in 'samples'...

			// actions:
			// 0: normal; export individualy
			// 1: export to a packed format
			// 2: cancel
			int action = 0;

			if (samples.size() > 1) {
				String labels[] = { "Individually", "Packed", "Cancel" };

				action = JOptionPane
						.showOptionDialog(
								null,
								"You are exporting more than one sample.\n"
										+ "Would you like to export to a single file ('packed' format),\n"
										+ "or convert each file individually?",
								I18n.getText("bulkexport..."),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, labels,
								labels[0]);
			} else if (((Sample) samples.get(0)).isSummed()) {
				String labels[] = { "Sum", "Elements", "Combined" };

				action = JOptionPane
						.showOptionDialog(
								null,
								"You are exporting a sum.\n"
										+ "Would you like to export the summed values,\n"
										+ "export the sum's elements in a packed file,\n"
										+ "or export them combined in a packed file?",
								I18n.getText("bulkexport..."),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, labels,
								labels[0]);

				Sample base = (Sample) samples.get(0);

				switch (action) {
				case 0:
					break; // this case is normal. whew.

				case 1: // export elements. we need to reload the samples array.
					samples.clear();
					// fall through...

				case 2: // export everything.
					elements = base.getElements();

					errorsamples = "";

					for (int i = 0; i < elements.size(); i++) {
						Element e = elements.get(i);

						if (!elements.isActive(e)) // skip inactive
							continue;

						try {
							Sample s = e.load();
							samples.add(s);
						} catch (IOException ioe) {
							problem = true;
							if (errorsamples.length() != 0)
								errorsamples += ", ";
							errorsamples += e.getName();
						}
					}

					// problem?
					if (problem) {
						Alert.error("Error loading sample(s):", errorsamples);
						return;
					}

					// no samples => don't bother doing anything
					if (samples.isEmpty()) {
						return;
					}

					// now, kludge action for below, so it uses a packed format...
					action = 1;
				}
			}

			switch (action) {
			case 0:
				new ExportDialog(samples, null, false);
				break;
			case 1:
				new ExportDialog(samples, null, true);
				break;
			case JOptionPane.CLOSED_OPTION:
			case 2:
				// user cancelled
				return;
			}

		} catch (UserCancelledException uce) {
			// do nothing
		}
	}

	// ask user for some files to sum, and make the sum
	// REFACTOR: why isn't this in manip.Sum?
	public static void sum() {
		ElementList tmp;
		try {
			// get files for sum
			tmp = FileDialog.showMulti(I18n.getText("sum"));
		} catch (UserCancelledException uce) {
			return; /// !!!
		}

		// DESIGN: what if the user picks exactly 1 sample?  is a sum of 1 sample a sum?

		Collections.sort(tmp);
		
		// get sampleset, and copy to new, loading elements as needed
		ElementList s = new ElementList();
		for (int i = 0; i < tmp.size(); i++) {
			Element e = tmp.get(i);
			
			Sample testSample;
			try {
				testSample = e.load();
			} catch (IOException ioe) {
				Alert.error("Can't load file", "The file \"" + e
						+ "\" could not be loaded.");
				return;
				// FIXME: this error-handling is really piss-poor.
				// BETTER:
				// -- if one couldn't be loaded, ask what to do
				// -- if >1 couldn't be loaded, present list
				// choices are "remove", "replace...", ??
				// ALSO: use Finder.java here (and RENAME it, because i can't ever think of its name)
			} catch (Exception ex) {
				new Bug(ex); // REMOVE ME: this never happens
				return;
			}
			if (testSample.getElements() != null) { // if summed (has elements),
				// s.addAll(testSample.getElements()); // add all elements
				for(Element el : testSample.getElements()) {
					s.add(el);
					s.setActive(el, testSample.getElements().isActive(el));
				}
			}
			else {
				s.add(e);
				s.setActive(e, tmp.isActive(e));
			}
		}

		// sort, by filename (is that really what i want?)
		Collections.sort(s);

		// PERF: doesn't this end up loading every sample twice?
		// (once, above, to see if it has elements; again, below, when summing)

		// make sum
		try {
			Sample m = Sum.sum(s); // FIXME: catch Sum.SpecificException here!
			new Editor(m);
		} catch (IOException e) {
			Alert.error("Error summing", e.getMessage());
		}
	}

	public void addCloseSaveMenus() {
		addCloseMenu();
		addSaveMenu();
		//addSaveAsMenu();
	}

	public void addSaveMenu() {
		JMenuItem save = Builder.makeMenuItem("save");

		if (f instanceof SaveableDocument) {
			// add menuitems, hooked up to |f|
			save.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// get doc
					SaveableDocument doc = (SaveableDocument) f;

					// save
					doc.save();

					// add to the recently opened files list if the user actually saved
					// also, the user can try to save a document they didn't do anything to. argh.
					if (doc.isSaved() && doc.getFilename() != null) {
						if(doc.getSaverClass() != null && doc.getSaverClass() instanceof SampleLoader)
							OpenRecent.sampleOpened((SampleLoader)doc.getSaverClass());
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
		JMenuItem saveAs = Builder.makeMenuItem("save_as...");

		if (f instanceof SaveableDocument
				&& ((SaveableDocument) f).isNameChangeable()) {
			// add menuitems, hooked up to |f|
			saveAs.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						// get doc
						SaveableDocument doc = (SaveableDocument) f;

						// DESIGN: start out in the same folder as the old filename,
						// if there is one?

						// get new filename
						String filename = FileDialog.showSingle(I18n
								.getText("save"));
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
		JMenuItem close = Builder.makeMenuItem("close");
		close.addActionListener(new AbstractAction() {
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
		JMenuItem setup = Builder.makeMenuItem("page_setup...");

		if (f instanceof PrintableDocument) {
			// page setup
			setup.addActionListener(new AbstractAction() {
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
		JMenuItem print = Builder.makeMenuItem("print...");

		if (f instanceof PrintableDocument) {
			// print
			print.addActionListener(new AbstractAction() {
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
			Bug.bug(new IllegalArgumentException(
					"not an object which can be printed!"));

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
					Alert.error("Error printing", "Printer error: "
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
			add(Builder.makeMenuItem("quit", "edu.cornell.dendro.corina.gui.XCorina.quit()", "exit.png"));
		}
	}
}