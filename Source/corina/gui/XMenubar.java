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
import corina.files.ExportDialog;
import corina.cross.Sequence;
import corina.cross.CrossFrame;
import corina.cross.GridFrame;
import corina.cross.TableFrame;
import corina.cross.CrossdateKit;
import corina.manip.Sum;
import corina.graph.GraphFrame;
import corina.editor.Editor;
import corina.site.SiteDB;
import corina.map.MapFrame;
import corina.prefs.PrefsDialog;
import corina.util.Platform;
import corina.util.Overwrite;
import corina.browser.Browser;
import corina.ui.Builder;
import corina.ui.I18n;

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
    private static ResourceBundle oldmsg = ResourceBundle.getBundle("MenubarBundle");
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

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
            super("    " + oldmsg.getString("sample")); // REFACTOR: indent() method...  BETTER: my own sub-menu, 2 styles.
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("sample_key").charAt(0)));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(oldmsg.getString("sample_acc"))));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                new Editor();
            } catch (Throwable t) {
                Bug.bug(t);
            }
        }
    }

    public class NewSum extends AbstractAction {
        public NewSum() {
            super("    " + oldmsg.getString("sum") + "...");
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("sum_key").charAt(0)));
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
            super("    " + oldmsg.getString("plot") + "...");
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("plot_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get samples
                List ss = FileDialog.showMulti(oldmsg.getString("plot"));

                // make graph
                new GraphFrame(ss);
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class NewGrid extends AbstractAction {
        public NewGrid() {
            super("    " + oldmsg.getString("grid") + "...");
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("grid_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get samples
                List ss = FileDialog.showMulti(oldmsg.getString("grid"));

                // make grid
                new GridFrame(ss);
            } catch (UserCancelledException uce) {
                // do nothing
            } catch (Throwable t) {
                Bug.bug(t);
            }
        }
    }

    public class NewTable extends AbstractAction {
        public NewTable() {
            super(oldmsg.getString("table"));
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("table_key").charAt(0)));
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

    // ----------------------------------------------------------------------

    public static class Open extends AbstractAction {
        public Open() {
            super(oldmsg.getString("open"));
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("open_key").charAt(0)));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(macize(oldmsg.getString("open_acc"))));
        }
        public void actionPerformed(ActionEvent ae) {
            try {
                // get filename, and load
                CanOpener.open(FileDialog.showSingle(oldmsg.getString("open")));
            } catch (UserCancelledException uce) {
                return;
            } catch (IOException ioe) {
		// BUG: this should never happen.  loading is so fast, it'll get displayed
		// in the preview component, and if it can't be loaded, "ok" should be dimmed.
                JOptionPane.showMessageDialog(null,
                                              "Can't open file: " + ioe.getMessage(),
                                              "I/O Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class Rename extends AbstractAction {
        public Rename() {
            super(oldmsg.getString("rename_to"));
            putValue(Action.MNEMONIC_KEY, new Integer(oldmsg.getString("rename_to_key").charAt(0)));
        }
        public void actionPerformed(ActionEvent ae) {
            // make sure we're a saveable document
            if (!(myFrame instanceof SaveableDocument))
                return;
            SaveableDocument doc = (SaveableDocument) myFrame;

            // just show the filename.  ("de fault!  the two sweetest words in the english language".)
            String deFault = (new File(doc.getFilename())).getName();

            String filename = (String) JOptionPane.showInputDialog(myFrame,
                                                                   "Rename this file to:",
                                                                   "Rename File",
                                                                   JOptionPane.PLAIN_MESSAGE,
                                                                   null, null, // icon, values
                                                                   deFault);

            if (filename == null) {
		return; // user cancelled
            }
            
            // make new file
            String parent = (new File(doc.getFilename())).getParent();
            File newFile = new File(parent + File.separator + filename);

            // check for already-exists
            if (newFile.exists() && Overwrite.overwrite(filename)) // BUG: isn't this check backwards?
                return;

            // rename disk file
            File oldFile = new File(doc.getFilename());
            try {
                boolean success = oldFile.renameTo(newFile);
                if (!success) {
                    return; // error!  BUG: deal with me (when does this happen?  the api doesn't say...)
                }
            } catch (SecurityException se) {
                return; // error!  BUG: deal with me (when does this happen?  if it can't be written to)
            }

            // set filename
            doc.setFilename(newFile.getPath());
        }
    }

    private void tryToPrint(boolean askOptions) {
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
	    if (askOptions)
                if (!printJob.printDialog())
                    return;

	    // print (in background thread)
	    final PrinterJob glue = printJob;
	    (new Thread() {
                    public void run() {
                        try {
                            glue.print();
                        } catch (PrinterException pe) {
                            // make this friendlier/more useful!
                            // "an error in the print system caused printing to be aborted.  this could indicate
                            // a hardware or system problem, or a bug in Corina."  ( details ) (( ok ))
                            // (details->stacktrace) -- create static String Bug.getStackTrace(ex)
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

            // the Peter-catcher: if the 'fixed' file is relatively
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
                List m = FileDialog.showMulti(msg.getString("moving"));

                // go ahead if m = f?

                // new Cross
                new CrossFrame(new Sequence(f, m));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    }

    public class CrossSuperSpiffy extends AbstractAction {
        public CrossSuperSpiffy() {
            super("Crossdate Kit...");
        }
        public void actionPerformed(ActionEvent ae) {
            new CrossdateKit();
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
            new MapFrame();
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
    // public stuff!
    // ----------------------------------------------------------------------

    // new way: indent!
    public void addNewMenu(JMenu n) {
        XMenuItem header = new XMenuItem(oldmsg.getString("new"));
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
	JMenu n = Builder.makeMenu("new");
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

    private JMenu makeHelpMenu() {
        JMenu help = Builder.makeMenu("help");
	JMenuItem manual = Builder.makeMenuItem("help");
	manual.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    new HelpBrowser();
		}
	    });
        help.add(manual);
        if (!Platform.isMac) {
	    JMenuItem about = Builder.makeMenuItem("about");
	    about.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			new AboutBox();
		    }
		});
            help.add(about);
	}
        return help;
    }

    static {
        // on mac, register about menuitem -- MOVE THIS TO PLATFORM.JAVA
        if (Platform.isMac) {
            // add about-handler
            try {
                Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
//                Object aboutHandler = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                ClassLoader cl = Class.forName("corina.gui.XMenubar").getClassLoader();
                Object aboutHandler = Proxy.newProxyInstance(cl,
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
                Bug.bug(e);
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
//                Object quitHandler = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                Object quitHandler = Proxy.newProxyInstance(appUtils.getClassLoader(),
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
//                Object prefsHandler = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                ClassLoader cl = Class.forName("corina.gui.XMenubar").getClassLoader();
                Object prefsHandler = Proxy.newProxyInstance(cl, // appUtils.getClassLoader(),
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
	JMenu file = Builder.makeMenu("file");
        addNewMenu(file);
        file.add(new XMenuItem(new XMenubar.Open()));
	{
	    JMenuItem browse = Builder.makeMenuItem("browse...");
	    browse.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			new Browser();
		    }
		});
	    file.add(browse);
	}
        file.add(OpenRecent.makeOpenRecentMenu());
        
        // Mac OS X already has a "Quit Corina" menuitem, under the
        // "Corina" menu.  unfortunately, it doesn't do everything my
        // Quit does, but that's another problem.
        if (!Platform.isMac) {
            file.addSeparator();
	    JMenuItem quit = Builder.makeMenuItem("quit");
	    quit.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			XCorina.quit(); // this deals with unsaved documents, etc.
		    }
		});
            file.add(quit);
        }

        // menu: edit.  (Mac has preferences under the app menu, so there's no reason.)
	JMenu edit = null;
        if (!Platform.isMac) {
            edit = Builder.makeMenu("edit");

	    JMenuItem prefs = Builder.makeMenuItem("preferences");
	    prefs.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			PrefsDialog.showPreferences();
		    }
		});

            edit.add(prefs);
        }

        // menu: cross
        JMenu cross = Builder.makeMenu("crossdate");
        cross.add(new XMenuItem(new XMenubar.Cross1byN()));
        cross.add(new XMenuItem(new XMenubar.CrossNbyN()));
        cross.add(new XMenuItem(new XMenubar.Cross1by1()));
        cross.add(new XMenuItem(new XMenubar.CrossNby1()));
        cross.addSeparator();
        // cross.add(new XMenuItem(new XMenubar.CrossSpiffy())); // the next-gen crosser!
        cross.add(new XMenuItem(new XMenubar.CrossSuperSpiffy())); // the next-gen^2 crosser!

	JMenu script = Builder.makeMenu("script");
        // script.add(Shell.makeShellMenuItem());
        script.add(new XMenuItem(new XMenubar.ShellWindow()));
        script.addSeparator();
        JMenuItem scripts[] = Shell.loadScripts();
        if (scripts.length == 0) {
	    JMenuItem blank = Builder.makeMenuItem("no_scripts_found");
            blank.setEnabled(false);
            script.add(blank);
        }
        for (int i=0; i<scripts.length; i++)
            if (scripts[i] != null) // fixme: why would it be null?
                script.add(scripts[i]);

        // menu: help
        JMenu help = makeHelpMenu();

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
        boolean canPrint = (me instanceof PrintableDocument);

        // menu: file
	JMenu file = Builder.makeMenu("file");
        addNewMenu(file);
        file.add(new XMenuItem(new XMenubar.Open()));
	{
	    JMenuItem browse = Builder.makeMenuItem("browse...");
	    browse.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			new Browser();
		    }
		});
	    file.add(browse);
	}
        file.add(OpenRecent.makeOpenRecentMenu());
        file.addSeparator();
	{
	    JMenuItem close = Builder.makeMenuItem("close");
	    close.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			myFrame.close();
		    }
		});
	    file.add(close);
	}
        {
	    JMenuItem save = Builder.makeMenuItem("save");
	    save.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent ae) {
			if (myFrame instanceof SaveableDocument) {
			    SaveableDocument doc = (SaveableDocument) myFrame;

			    doc.save();
			    OpenRecent.fileOpened(doc.getFilename());
			}
		    }
		});
	    file.add(save);
	}
        {
	    JMenuItem saveAs = Builder.makeMenuItem("save_as");
	    saveAs.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent ae) {
			if (myFrame instanceof SaveableDocument) {
			    // get new filename
			    String filename;
			    try {
				filename = FileDialog.showSingle(msg.getString("save")); // TODO: need to call getText() on this?
			    } catch (UserCancelledException uce) {
				return;
			    }

			    // check for already-exists
			    if (new File(filename).exists() && Overwrite.overwrite(filename))
				return;

			    // set filename
			    ((SaveableDocument) myFrame).setFilename(filename);

			    // save
			    ((SaveableDocument) myFrame).save();
			}
		    }
		});
	    file.add(saveAs);
	}
        if (myFrame instanceof Editor) {
	    JMenuItem export = Builder.makeMenuItem("export...");
	    export.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			// get the sample
			Sample s = ((Editor) myFrame).getSample();

			// make an export dialog
			new ExportDialog(s, myFrame);
		    }
		});
	    file.add(export);
	}
        file.add(new XMenuItem(new XMenubar.Rename()));
        file.addSeparator();
        {
	    JMenuItem setup = Builder.makeMenuItem("page_setup");
	    setup.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent ae) {
			// make printer job, if none exists yet
			if (printJob == null)
			    printJob = PrinterJob.getPrinterJob();

			// get page format
			pageFormat = printJob.pageDialog(pageFormat);
		    }
		});
	    setup.setEnabled(canPrint);
	    file.add(setup);
	}
        {
	    JMenuItem printOne = Builder.makeMenuItem("print_one");
	    printOne.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			tryToPrint(/* ask options = */ false);
		    }
		});
	    printOne.setEnabled(canPrint);
	    file.add(printOne);
	}
        {
	    JMenuItem print = Builder.makeMenuItem("print");
	    print.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			tryToPrint(/* ask options = */ true);
		    }
		});
	    print.setEnabled(canPrint);
	    file.add(print);
	}
                
        // menu: edit -- see below

        // menu: help
        JMenu help = makeHelpMenu();

        // (i don't want to worry about menus being null, so just in case...)
        if (menus == null)
            menus = new JMenu[0];

        // put them all together:
        add(file);

        // if the first menu is called "Edit", add "Preferences..." to the bottom.
        if (!Platform.isMac) {
	    JMenuItem prefs = Builder.makeMenuItem("preferences");
	    prefs.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			PrefsDialog.showPreferences();
		    }
		});

            if (menus.length >= 1 && menus[0].getText().equals(I18n.getText("edit"))) { // ick!
                menus[0].addSeparator();
                menus[0].add(prefs);
            } else {
                // otherwise, make an edit menu, and give it "Preferences..."
		JMenu edit = Builder.makeMenu("edit");
                edit.add(prefs);
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
