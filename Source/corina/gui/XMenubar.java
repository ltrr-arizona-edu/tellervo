//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.gui;

import org.python.util.PythonInterpreter;

import corina.Sample;
import corina.Element;
import corina.files.Filetype;
import corina.cross.Sequence;
import corina.cross.CrossFrame;
import corina.cross.GridFrame;
import corina.cross.TableFrame;
import corina.manip.Sum;
import corina.graph.GraphFrame;
import corina.editor.Editor;
import corina.site.SiteDB;
import corina.map.MapFrame;
import corina.prefs.PrefsDialog;
import corina.util.Platform;

import java.io.File;
import java.io.IOException;

// for MRJ
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.awt.Font;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;

public class XMenubar extends JMenuBar {

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("MenubarBundle");

    // mac-ize keystrokes.  s/control/meta/ (meta==command)
    public static String macize(String k) {
	if (!Platform.isMac)
	    return k;
	int i = k.indexOf("control");
	if (i == -1)
	    return k;
	return k.substring(0, i) + "meta" + k.substring(i + 7);
    }

    // ----------------------------------------
    // - set font to corina.menubar.font
    // - fix ACCELERATOR_KEY bug
    public static class XMenuItem extends JMenuItem {
	public XMenuItem(Action a) {
	    super(a);
            if (Platform.isMac)
                setMnemonic(0); // no removeMnemonic()?
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	    setAccelerator((KeyStroke) a.getValue(Action.ACCELERATOR_KEY));
	}
	public XMenuItem(String n, char mnemonic) {
            super(n);
            if (!Platform.isMac)
                setMnemonic(mnemonic);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	}
	public XMenuItem(String n) {
	    super(n);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	}
    }
    // ----------------------------------------

    // ----------------------------------------
    // - set font to corina.menubar.font
    // - fix ACCELERATOR_KEY bug
    public static class XRadioButtonMenuItem extends JRadioButtonMenuItem {
	public XRadioButtonMenuItem(Action a) {
	    super(a);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	    setAccelerator((KeyStroke) a.getValue(Action.ACCELERATOR_KEY));
	}
        public XRadioButtonMenuItem(String n, char mnemonic) {
            super(n);
            if (!Platform.isMac)
                setMnemonic(mnemonic);
            if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
                setFont(Font.getFont("corina.menubar.font"));
	}
	public XRadioButtonMenuItem(String n) {
	    super(n);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	}
    }
    // ----------------------------------------

    // ----------------------------------------
    // - set font to corina.menubar.font
    // - fix ACCELERATOR_KEY bug
    public static class XCheckBoxMenuItem extends JCheckBoxMenuItem {
	public XCheckBoxMenuItem(Action a) {
	    super(a);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	    setAccelerator((KeyStroke) a.getValue(Action.ACCELERATOR_KEY));
	}
	public XCheckBoxMenuItem(String n, char mnemonic) {
	    super(n);
            if (!Platform.isMac)
                setMnemonic(mnemonic);
            if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
                setFont(Font.getFont("corina.menubar.font"));
	}
	public XCheckBoxMenuItem(String n) {
	    super(n);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	}
    }
    // ----------------------------------------

    // ----------------------------------------
    // - set font to corina.menubar.font
    public static class XMenu extends JMenu {
	public XMenu(String s) {
	    super(s);
	    if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
		setFont(Font.getFont("corina.menubar.font"));
	}
	public XMenu(String s, char c) {
	    super(s);
            if (!Platform.isMac)
                setMnemonic(c);
            if (System.getProperty("corina.menubar.font") != null && !Platform.isMac)
                setFont(Font.getFont("corina.menubar.font"));
	}
    }
    // ----------------------------------------

    // for an XFrame, this references the frame
    private XFrame myFrame=null;

    // printing!
    private PrinterJob printJob=null;
    private PageFormat pageFormat=new PageFormat();

    public static class NewSample extends AbstractAction {
	public NewSample() {
            super("    " + msg.getString("sample")); // REFACTOR: indent() method...  BETTER: my own sub-menu, 2 styles.
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("sample_key").charAt(0)));
	    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("sample_acc"))));
	}
	public void actionPerformed(ActionEvent ae) {
	    new Editor();
	}
    }

    public class NewSum extends AbstractAction {
        public NewSum() {
            super("    " + msg.getString("sum") + "...");
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("sum_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            List tmp;
            try {
                // get files for sum
                tmp = FileDialog.showMulti("Sum");
            } catch (UserCancelledException uce) {
                return;
            }

            // get sampleset, and copy to new, loading elements as needed
            List s = new ArrayList();
            for (int i=0; i<tmp.size(); i++) {
                String filename = ((Element) tmp.get(i)).filename;
                Sample testSample;
                try {
                    testSample = new Sample(filename);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null,
                                                  "The file \"" + filename + "\" could not be loaded.",
                                                  "Can't load file",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                    // this error-handling is really piss-poor.
                } catch (Exception e) {
                    Bug.bug(e);
                    return;
                }
                if (testSample.elements != null) // if summed (has elements),
                    s.addAll(testSample.elements); // add all elements
                else
                    s.add(new Element(filename));
            }

            // sort
            Collections.sort(s);

            // make sum
            try {
                Sample m = Sum.sum(s);
                new Editor(m);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                                              e.getMessage(),
                                              "Error summing",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class NewPlot extends AbstractAction {
        public NewPlot() {
            super("    " + msg.getString("plot") + "...");
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("plot_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get samples
                List ss = FileDialog.showMulti(msg.getString("plot"));

                // make graph
                new GraphFrame(ss);
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class NewGrid extends AbstractAction {
        public NewGrid() {
            super("    " + msg.getString("grid") + "...");
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("grid_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get samples
                List ss = FileDialog.showMulti(msg.getString("grid"));

                // make grid
                new GridFrame(ss);
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class NewTable extends AbstractAction {
        public NewTable() {
            super(msg.getString("table"));
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("table_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get single
                String s = FileDialog.showSingle("First");

                // get many
                List ss = FileDialog.showMulti("Samples");

                // make graph
                new TableFrame(s, ss);
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class Preferences extends AbstractAction {
	public Preferences() {
	    super(msg.getString("preferences"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("preferences_key").charAt(0)));
	}
	public void actionPerformed(ActionEvent ae) {
	    PrefsDialog.showPreferences();
	}
    }

    // ----------------------------------------------------------------------

    public static class Open extends AbstractAction {
        public Open() {
            super(msg.getString("open"));
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("open_key").charAt(0)));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("open_acc"))));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get filename, and load
                CanOpener.open(FileDialog.showSingle(msg.getString("open")));
            } catch (UserCancelledException uce) {
                return;
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null,
                                              "Can't open file: " + ioe.getMessage(),
                                              "I/O Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public final class Save extends AbstractAction {
	public Save() {
	    super(msg.getString("save"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("save_key").charAt(0)));
	    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("save_acc"))));
	}
	public void actionPerformed(ActionEvent ae) {
	    if (myFrame instanceof SaveableDocument) {
		SaveableDocument doc = (SaveableDocument) myFrame;

		doc.save();
		OpenRecent.fileOpened(doc.getFilename());
	    }
	}
    }

    public class SaveAs extends AbstractAction {
        public SaveAs() {
            super(msg.getString("save_as"));
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("save_as_key").charAt(0)));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("save_as_acc"))));
        }
        public void actionPerformed(ActionEvent ae) {
            if (myFrame instanceof SaveableDocument) {
                // get new filename
                String filename;
                try {
                    filename = FileDialog.showSingle(msg.getString("save"));
                } catch (UserCancelledException uce) {
                    return;
                }

                // check for already-exists -- THIS IS SO FRIGGIN' DUPLICATE CODE IT'S NOT FUNNY
                {
                    File f = new File(filename);
                    if (f.exists()) {
                        int x = JOptionPane.showConfirmDialog(null,
                                                              "File \"" + filename + "\"\n" +
                                                              "already exists; overwrite?",
                                                              "Already exists",
                                                              JOptionPane.YES_NO_OPTION);
                        if (x == JOptionPane.NO_OPTION)
                            return;
                    }
                }

                // set filename
                ((SaveableDocument) myFrame).setFilename(filename);

                // save
                ((SaveableDocument) myFrame).save();
            }
        }
    }

    public class Rename extends AbstractAction {
	public Rename() {
	    super(msg.getString("rename_to"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("rename_to_key").charAt(0)));
	}
	public void actionPerformed(ActionEvent ae) {
	    // make sure we're a saveable document
	    if (!(myFrame instanceof SaveableDocument))
		return;
	    SaveableDocument doc = (SaveableDocument) myFrame;

	    // new way: just show the filename.  ("de-fault!  the two
	    // sweetest words in the English language".)
	    String deFault = (new File(doc.getFilename())).getName();

	    String filename = (String) JOptionPane.showInputDialog(myFrame,
								   "Rename this file to:",
								   "Rename File",
								   JOptionPane.PLAIN_MESSAGE,
								   null, // icon
								   null, // values
								   deFault);

/* -- old way: show a file dialog
	    // get new name
	    String filename = FileDialog.showSingle("Rename to"); -- be sure to catch UCE now
*/

	    if (filename == null) {
		// user cancelled
		return;
	    }

	    // make new file
	    String parent = (new File(doc.getFilename())).getParent();
	    File newFile = new File(parent + File.separator + filename);

	    // check for already-exists
	    if (newFile.exists()) {
		int x = JOptionPane.showConfirmDialog(null,
						      "File \"" + filename + "\"\n" +
						          "already exists; overwrite?",
						      "Already exists",
						      JOptionPane.YES_NO_OPTION);
		if (x == JOptionPane.NO_OPTION)
		    return;
	    }

	    // rename disk file
	    File oldFile = new File(doc.getFilename());
	    try {
		boolean success = oldFile.renameTo(newFile);
		if (!success) {
		    // error!
		    return;
		}
	    } catch (SecurityException se) {
		// error!
		return;
	    }

	    // set filename
	    doc.setFilename(newFile.getPath());
	}
    }

    public final class Close extends AbstractAction {
	public Close() {
	    super(msg.getString("close"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("close_key").charAt(0)));
	    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("close_acc"))));
	}
	public void actionPerformed(ActionEvent ae) {
	    myFrame.close();
	}
    }

    public final class PageSetup extends AbstractAction {
	public PageSetup(boolean canPrint) {
	    super(msg.getString("page_setup"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("page_setup_key").charAt(0)));
	    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("page_setup_acc"))));
	    super.setEnabled(canPrint);
	}
	public void actionPerformed(ActionEvent ae) {
	    // make printer job, if none exists yet
	    if (printJob == null)
		printJob = PrinterJob.getPrinterJob();

	    // get page format
	    pageFormat = printJob.pageDialog(pageFormat);
	}
    }

    public final class Print extends AbstractAction {
	public Print(boolean canPrint) {
	    super(msg.getString("print"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("print_key").charAt(0)));
	    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("print_acc"))));
	    super.setEnabled(canPrint);
	}
	public void actionPerformed(ActionEvent ae) {
	    // SPECIAL CASE: text print of Editor
	    if (myFrame instanceof Editor) {
		((Editor) myFrame).printText();
		return;
	    }

            if (myFrame instanceof PrintableDocument) {
                // page setup wasn't run: force it
                if (printJob == null)
                    printJob = PrinterJob.getPrinterJob();
                if (pageFormat == null)
                    pageFormat = printJob.pageDialog(pageFormat);
                if (pageFormat == null)
                    return; // abort

                int method = ((PrintableDocument) myFrame).getPrintingMethod();
                if (method == PrintableDocument.PAGEABLE) {
                    // get document to print
                    Pageable pageable = ((PrintableDocument) myFrame).makePageable(pageFormat);

                    // prepare to print
                    printJob.setPageable(pageable);
                } else {
                    // get document to print
                    Printable printable = ((PrintableDocument) myFrame).makePrintable(pageFormat);

                    // prepare to print
                    printJob.setPrintable(printable);
                }

                // job title
                printJob.setJobName(((PrintableDocument) myFrame).getPrintTitle());

                // ask user options
		if (!printJob.printDialog())
		    return;

		// print (in background thread)
		final PrinterJob glue = printJob;
		(new Thread() {
			public void run() {
			    try {
				glue.print();
			    } catch (PrinterException pe) {
				JOptionPane.showMessageDialog(null,
							      "Printer error: " + pe.getMessage(),
							      "Error printing",
							      JOptionPane.ERROR_MESSAGE);
				return;
			    }
			}
		    }).start();
	    }
	}
    }

    public class Quit extends AbstractAction {
	public Quit() {
	    super(msg.getString("quit"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("quit_key").charAt(0)));
	    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("quit_acc"))));
	}
	public void actionPerformed(ActionEvent ae) {
	    // this deals with unsaved documents, etc.
	    XCorina.quit();
	}
    }

    // ----------------------------------------------------------------------

    // !!!
    // ugly, ugly, ugly
    // - at the worst, these should be created from the cross package.
    // (ideally, they're not really needed at all...)

    public class Cross1byN extends AbstractAction {
        public Cross1byN() {
            super(msg.getString("1_by_n"));
        }
        public void actionPerformed(ActionEvent ae) {
            // select fixed file
            try {
                String fixed = FileDialog.showSingle(msg.getString("fixed"));

                // select moving files
                List ss = FileDialog.showMulti(msg.getString("moving"));

            // the Peter-catcher: if the "fixed" file is relatively
            // dated, he really wanted an Nx1.  d'oh!  DWIM.
            /*
             if (!fixedSample.isAbsolute())
             new CrossFrame(new Sequence(f, ss));
             else
             new CrossFrame(new Sequence(ss, f));
             */

                // new Cross
                new CrossFrame(new Sequence(Collections.singletonList(fixed), ss));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class CrossNbyN extends AbstractAction {
        public CrossNbyN() {
            super(msg.getString("n_by_n"));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // select files
                List ss = FileDialog.showMulti(msg.getString("crossdate"));

                // watch out: need at least 2 samples
                if (ss.size() < 2) {
                    JOptionPane.showMessageDialog(null,
                                                  "For N-by-N crossdating, you must\n" +
                                                  "select at least 2 samples",
                                                  "Not enough samples",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // new Cross
                new CrossFrame(new Sequence(ss, ss));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class Cross1by1 extends AbstractAction {
        public Cross1by1() {
            super(msg.getString("1_by_1"));
        }
        public void actionPerformed(ActionEvent ae) {
            // select fixed file
            try {
                String fixed = FileDialog.showSingle(msg.getString("fixed"));

                // select moving file
                String moving = FileDialog.showSingle(msg.getString("moving"));
                if (moving == null)
                    return;

                // new Cross
                new CrossFrame(new Sequence(Collections.singletonList(fixed),
                                            Collections.singletonList(moving)));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class CrossNby1 extends AbstractAction {
        public CrossNby1() {
            super(msg.getString("n_by_1"));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // select fixed files
                List ss = FileDialog.showMulti(msg.getString("fixed"));

                // select moving file
                String moving = FileDialog.showSingle(msg.getString("moving"));

                // new Cross
                new CrossFrame(new Sequence(ss, Collections.singletonList(moving)));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class CrossSpiffy extends AbstractAction {
        public CrossSpiffy() {
            super("Crossdate...");
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // select fixed files
                List f = FileDialog.showMulti(msg.getString("fixed"));

                // select moving files
                List m = FileDialog.showMulti(msg.getString("fixed"));

                // go ahead if m = f?

                // new Cross
                new CrossFrame(new Sequence(f, m));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    // temporary mapper!
    // better: "draw a map of (1) all sites in the database, (2) only some sites, ..."
    public class Map extends AbstractAction {
	public Map() {
	    super("    " + "Map");
	    putValue(Action.MNEMONIC_KEY, new Integer('M'));
	}
	public void actionPerformed(ActionEvent e) {
	    try {
		new MapFrame(SiteDB.getSiteDB());
	    } catch (IOException ioe) {
		System.out.println("error -- " + ioe);
	    }
	}
    }

    // ----------------------------------------------------------------------
    // move me to Shell.java?

    public class ShellWindow extends AbstractAction {
	public ShellWindow() {
	    super("Shell");
	    putValue(Action.MNEMONIC_KEY, new Integer('S'));
	}
	public void actionPerformed(ActionEvent ae) {
	    // make shell
	    new Shell();
	}
    }

    // ----------------------------------------------------------------------

    public final class Manual extends AbstractAction {
        public Manual() {
            super(msg.getString("help"));
            putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("help_key").charAt(0)));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(msg.getString("help_acc"))));
        }
	public void actionPerformed(ActionEvent ae) {
	    /*
	    // get browser
	    String browser = System.getProperty("corina.browser");

	    // autodetect browser, if none specified
	    if (browser == null) {
		// WRITE ME
		return;
	    }

	    // note: this should use a format string, like for
	    // printing, because users may want to perform a
	    // command-line that has the URL in the middle, e.g.,
	    // "netscape -remote
	    // 'openURL(http://xcorina.org/manual/)'".

	    // exec it
	    Runtime r = Runtime.getRuntime();
	    Process p;
	    try {
		// point to xcorina.org
		p = r.exec(new String[] { browser, "http://www.xcorina.org/manual/" });
	    } catch (IOException ioe) {
		JOptionPane.showMessageDialog(null,
					      "Error starting browser:\n" +
					      ioe.getMessage(),
					      "Can't start browser",
					      JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    */
	    new HelpBrowser();
	}
    }

    public final class About extends AbstractAction {
	public About() {
	    super(msg.getString("about"));
	    putValue(Action.MNEMONIC_KEY, new Integer(msg.getString("about_key").charAt(0)));
	}
	public void actionPerformed(ActionEvent ae) {
	    // the new about-box
	    new AboutBox();
	}
    }

    // ----------------------------------------------------------------------

    // formats to allow exporting
    public final static String EXPORTS[] = { "corina.files.Tucson",
					     "corina.files.TwoColumn",
					     "corina.files.Corina",
					     "corina.files.Heidelberg",
					     "corina.files.Hohenheim",
					     "corina.files.TSAPMatrix",
					     // "corina.files.HTML",
    };

    public XMenu makeExportMenu() {
	XMenu exportMenu = new XMenu(msg.getString("export"));

	for (int i=0; i<EXPORTS.length; i++) {
	    Filetype exporter;
	    try {
		exporter = Filetype.makeFiletype(EXPORTS[i]);
	    } catch (Exception e) {
		System.err.println("error when trying to create exporter for class " + EXPORTS[i]);
		continue;
	    }

	    XMenuItem tmp = new XMenuItem(exporter + "...", exporter.getMnemonic());
	    final int glue = i;
	    tmp.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent ae) {
			// get sample for export
			if (!(myFrame instanceof Editor)) {
			    JOptionPane.showMessageDialog(null,
							  "This window is not a sample, and therefore\n" +
							  "cannot be exported.",
							  "Not a sample",
							  JOptionPane.ERROR_MESSAGE);
			    return;
			}
			Sample sample = ((Editor) myFrame).getSample();

                        // get filename for export
                        String filename;
                        try {
                            filename = FileDialog.showSingle(msg.getString("export"));
                        } catch (UserCancelledException uce) {
                            return;
                        }

			// check for already-exists
			{
			    File f = new File(filename);
			    if (f.exists()) {
				int x = JOptionPane.showConfirmDialog(null,
								      "File \"" + filename + "\"\n" +
								      "already exists; overwrite?",
								      "Already exists",
								      JOptionPane.YES_NO_OPTION);
				if (x == JOptionPane.NO_OPTION)
				    return;
			    }
			}

			// try to save
			try {
			    Filetype myExporter = Filetype.makeFiletype(EXPORTS[glue]);
			    myExporter.save(filename, sample);
			} catch (IOException ioe) {
			    JOptionPane.showMessageDialog(null,
							  "Error: " + ioe.getMessage(),
							  "Error Exporting",
							  JOptionPane.ERROR_MESSAGE);
			    return;
			} catch (Exception e) { // instantiation, classnotfound, ...
			    JOptionPane.showMessageDialog(null,
							  "Error: " + e.getMessage(),
							  "Error with exporter",
							  JOptionPane.ERROR_MESSAGE);
			    return;
			}
		    }
		});
	    exportMenu.add(tmp);
	}

	return exportMenu;
    }

    // ----------------------------------------------------------------------
    // public stuff!
    // ----------------------------------------------------------------------

    // new way: indent!
    public void addNewMenu(JMenu n) {
        XMenuItem header = new XMenuItem(msg.getString("new"));
        header.setEnabled(false);
        n.add(header);
        n.add(new XMenuItem(new NewSample()));
        n.add(new XMenuItem(new NewSum()));
        n.add(new XMenuItem(new NewPlot()));
        n.add(new XMenuItem(new NewGrid()));

        n.add(new XMenuItem(new Map()));
    }
    
    // old way: a nested menu.  UNUSED NOW.
    public void addNewMenuOLD(JMenu m) {
        // a nested menu
        XMenu n = new XMenu(msg.getString("new"));
        if (!Platform.isMac)
            n.setMnemonic(msg.getString("new_key").charAt(0));
        n.add(new XMenuItem(new NewSample()));
        n.add(new XMenuItem(new NewSum()));
        n.add(new XMenuItem(new NewPlot()));
        n.add(new XMenuItem(new NewGrid()));

        n.add(new XMenuItem(new Map()));
        // n.add(new XMenuItem(new XMenubar.NewTable()));
        // new cross?

        // add it
        m.add(n);
    }

    private XMenu makeHelpMenu() {
        XMenu help = new XMenu("Help");
        if (!Platform.isMac)
            help.setMnemonic('H');
        help.add(new XMenuItem(new XMenubar.Manual()));
        if (!Platform.isMac)
            help.add(new XMenuItem(new XMenubar.About()));
        return help;
    }

    static {
        // on mac, register about menuitem -- MOVE THIS TO PLATFORM
        if (Platform.isMac) {
            // add about-handler
            try {
                Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
                Object aboutHandler = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                                             new Class[] { Class.forName("com.apple.mrj.MRJAboutHandler") },
                                                             new InvocationHandler() {
                                                                 public Object invoke(Object proxy, Method method, Object[] args) {
                                                                     if (method.getName().equals("handleAbout"))
                                                                         new AboutBox();
                                                                     return null; // gotta return something, and the docs don't say what "void" should return.
                                                                 }
                                                             });
                Method register = appUtils.getMethod("registerAboutHandler", new Class[] { Class.forName("com.apple.mrj.MRJAboutHandler") });
                register.invoke(appUtils.newInstance(), new Object[] { aboutHandler });
            } catch (Exception e) {
                // can't happen <=> bug!
            }

            /* that's a pure-reflection way to say this, which won't even compile on non-mac platforms:
            com.apple.mrj.MRJApplicationUtils.registerAboutHandler(new com.apple.mrj.MRJAboutHandler() {
                public void handleAbout() {
                    new AboutBox();
                }
            });
            */

            // while we're here, register quit, too.  (this gets called twice.  dunno why, but it doesn't seem to hurt anybody.)
            /*
            com.apple.mrj.MRJApplicationUtils.registerQuitHandler(new com.apple.mrj.MRJQuitHandler() {
                public void handleQuit() {
                    (new Thread() { // needs to run in its own thread, for reasons i don't entirely understand.
                        public void run() {
                            try {
                                XCorina.quit();
                                System.exit(0);
                            } catch (IllegalStateException ise) {
                                // don't quit -- i guess this doesn't need to be rethrown, though again, no idea why.
                            }
                        }
                    }).start();
                }
            });
*/
            try {
                Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
                Object quitHandler = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                                             new Class[] { Class.forName("com.apple.mrj.MRJQuitHandler") },
                                                             new InvocationHandler() {
                                                                 public Object invoke(Object proxy, Method method, Object[] args) {
                                                                     if (method.getName().equals("handleQuit")) {
                                                                         (new Thread() { // needs to run in its own thread, for reasons i don't entirely understand.
                                                                             public void run() {
                                                                                 try {
                                                                                     XCorina.quit();
                                                                                     System.exit(0);
                                                                                 } catch (IllegalStateException ise) {
                                                                                     // don't quit -- i guess this doesn't need to be rethrown, though again, no idea why.
                                                                                 }
                                                                             }
                                                                         }).start();
                                                                     }
                                                                     return null; // gotta return something, and the docs don't say what "void" should return.
                                                                 }
                                                             });
                Method register = appUtils.getMethod("registerQuitHandler", new Class[] { Class.forName("com.apple.mrj.MRJQuitHandler") });
                register.invoke(appUtils.newInstance(), new Object[] { quitHandler });
            } catch (Exception e) {
                // can't happen <=> bug!
            }
            
            // and preferences.
/*
             com.apple.mrj.MRJApplicationUtils.registerPrefsHandler(new com.apple.mrj.MRJPrefsHandler() {
                public void handlePrefs() {
                    PrefsDialog.showPreferences();
                }
            });
             */
            try {
                Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
                Object prefsHandler = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                                             new Class[] { Class.forName("com.apple.mrj.MRJPrefsHandler") },
                                                             new InvocationHandler() {
                                                                 public Object invoke(Object proxy, Method method, Object[] args) {
                                                                     if (method.getName().equals("handlePrefs"))
                                                                         PrefsDialog.showPreferences();
                                                                     return null; // gotta return something, and the docs don't say what "void" should return.
                                                                 }
                                                             });
                Method register = appUtils.getMethod("registerPrefsHandler", new Class[] { Class.forName("com.apple.mrj.MRJPrefsHandler") });
                register.invoke(appUtils.newInstance(), new Object[] { prefsHandler });
            } catch (Exception e) {
                // can't happen <=> bug!
            }
            
        }
    }

    // for XCorina (main window)
    public XMenubar() {
        // menu: file
        XMenu file = new XMenu(msg.getString("file"));
        if (!Platform.isMac)
            file.setMnemonic(msg.getString("file_key").charAt(0));
        addNewMenu(file);
        file.add(new XMenuItem(new XMenubar.Open()));
        file.add(OpenRecent.makeOpenRecentMenu());

        // Mac OS X already has a "Quit Corina" menuitem, under the
        // "Corina" menu.  unfortunately, it doesn't do everything my
        // Quit does, but that's another problem.
        if (!Platform.isMac) {
            file.addSeparator();
            file.add(new XMenuItem(new Quit()));
        }

        // menu: edit.  (Mac has preferences under the app menu, so there's no reason.)
        XMenu edit = null;
        if (!Platform.isMac) {
            edit = new XMenu(msg.getString("edit"));
            edit.setMnemonic(msg.getString("edit_key").charAt(0));
            edit.add(new XMenuItem(new XMenubar.Preferences()));
        }

        // menu: cross
        XMenu cross = new XMenu(msg.getString("crossdate"));
        if (!Platform.isMac)
            cross.setMnemonic(msg.getString("crossdate_key").charAt(0));
        cross.add(new XMenuItem(new XMenubar.Cross1byN()));
        cross.add(new XMenuItem(new XMenubar.CrossNbyN()));
        cross.add(new XMenuItem(new XMenubar.Cross1by1()));
        cross.add(new XMenuItem(new XMenubar.CrossNby1()));
        cross.add(new XMenuItem(new XMenubar.CrossSpiffy())); // the next-gen crosser!

        XMenu script = new XMenu("Script");
        if (!Platform.isMac)
            script.setMnemonic('S');
        // script.add(Shell.makeShellMenuItem());
        script.add(new XMenuItem(new XMenubar.ShellWindow()));
        script.addSeparator();
        JMenuItem scripts[] = Shell.loadScripts();
        if (scripts.length == 0) {
            JMenuItem blank = new XMenuItem("no scripts found");
            blank.setEnabled(false);
            script.add(blank);
        }
        for (int i=0; i<scripts.length; i++)
            if (scripts[i] != null) // fixme: why would it be null?
                script.add(scripts[i]);

        // menu: help
        XMenu help = makeHelpMenu();

        // put them all together
        add(file);
        if (!Platform.isMac)
            add(edit);
        add(cross);
        add(script);
        add(help);
    }

    // for XFrames; arg menus is inserted between file and manip, if non-null
    // MOVE THIS TO XFRAME!
    public XMenubar(XFrame me, JMenu menus[]) {
        // store frame reference
        myFrame = me;

        // can print this document?
        boolean canPrint = (me instanceof PrintableDocument) || (me instanceof Editor);

        // menu: file
        XMenu file = new XMenu(msg.getString("file"));
        if (!Platform.isMac)
            file.setMnemonic(msg.getString("file_key").charAt(0));
        addNewMenu(file);
        file.add(new XMenuItem(new XMenubar.Open()));
        file.add(OpenRecent.makeOpenRecentMenu());
        file.addSeparator();
        file.add(new XMenuItem(new XMenubar.Close()));
        file.add(new XMenuItem(new XMenubar.Save()));
        file.add(new XMenuItem(new XMenubar.SaveAs()));
        file.add(new XMenuItem(new XMenubar.Rename()));
        if (myFrame instanceof Editor) // this belongs in Editor *only*
            file.add(makeExportMenu());
        file.addSeparator();
        file.add(new XMenuItem(new XMenubar.PageSetup(canPrint)));
        file.add(new XMenuItem(new XMenubar.Print(canPrint)));

        // menu: edit -- see below

        // menu: help
        XMenu help = makeHelpMenu();

        // (i don't want to worry about menus being null, so just in case...)
        if (menus == null)
            menus = new JMenu[0];

        // put them all together:
        add(file);

        // if the first menu is called "Edit", add "Preferences..." to the bottom.
        if (!Platform.isMac) {
            if (menus.length >= 1 && menus[0].getText().equals(msg.getString("edit"))) { // ick!
                menus[0].addSeparator();
                menus[0].add(new XMenuItem(new XMenubar.Preferences()));
            } else {
                // otherwise, make an edit menu, and give it "Preferences..."
                XMenu edit = new XMenu(msg.getString("edit"));
                if (!Platform.isMac)
                    edit.setMnemonic(msg.getString("edit_key").charAt(0));
                edit.add(new XMenuItem(new XMenubar.Preferences()));
                add(edit);
            }
        }
        // of course, the Mac, as usual, has a more elegant way to do it.

        // XFrame-specific menus
        for (int i=0; i<menus.length; i++)
            add(menus[i]);

        add(help);
    }
}
