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

package corina.editor;

import corina.Sample;
import corina.SampleListener;
import corina.SampleEvent;
import corina.Element;
import corina.Weiserjahre;
import corina.files.TwoColumn;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteInfo;
import corina.site.SiteNotFoundException;
import corina.map.MapFrame;
import corina.graph.GraphFrame;
import corina.graph.BargraphFrame;
import corina.cross.CrossFrame;
import corina.cross.Sequence;
import corina.index.IndexDialog;
import corina.manip.TruncateDialog;
import corina.manip.RedateDialog;
import corina.manip.Reverse;
import corina.manip.Sum;
import corina.manip.Clean;
import corina.manip.Reconcile;
import corina.manip.ReconcileDialog;
import corina.gui.XFrame;
import corina.gui.WindowMenu;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.gui.ElementsPanel;
import corina.gui.HasPreferences;
import corina.gui.SaveableDocument;
import corina.gui.PrintableDocument;
import corina.gui.XMenubar;
import corina.gui.Bug;
import corina.prefs.Prefs;
import corina.files.WrongFiletypeException;
import corina.util.PureStringWriter;
import corina.util.TextClipboard;
import corina.util.Platform;
import corina.util.Overwrite;
import corina.print.Printer;
import corina.ui.Builder;

import java.io.File;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.text.MessageFormat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;
import java.awt.datatransfer.*;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

/*
  change around the menus slightly:

  File
    (usual)
  Edit
    Undo
    Redo
    ---
    Cut (dimmed)
    Copy
    Paste
    ---
    Insert Year
    Delete Year
    ---
    Start Measuring
    ---
    (Preferences...)
  Manipulate
    Redate...
    Index...
    Truncate...
    Reverse*
    ---
    Cross Against...
    Reconcile*
  Sum (Master?)
    Re-Sum
    Clean
    ---
    Add Element...
    Remove Element --- no, add cut/copy/paste/delete to Edit; Edit->Delete removes element?

  NB: the menu enabler/disabler code is duplicated -- in the init, and also in the event handler.  refactor.

  ----------------------------------------

  this class is the second-biggest (with 64 more lines it would be the
  biggest), and is in serious need of refactoring.  things that don't
  belong here:

  -- setEnabled() calls are duplicated: on init, and also in the event-handlers

  -- guts of save() look really familiar ... that goes into XFrame, or some other general utility class

  -- WJ panel stuff could go into a wjpanel, perhaps subclassing dataviewpanel

  -- printText() is a hack.  at least move it somewhere else.  then implement real printing.

  -- makeMenus is lines 455-868 (n=414).  ouch.  maybe this could be (compiled?) scheme.

  -- the 3 view menuitems can certainly be combined into one Action

 -- since the hold-down-control crap is gone now, those menus don't need object-scope.

 -- reconcile should be only in Reconcile and ReconcileDialog -- and it needs to be a real display, not just html

  -- refactor mapframe so it can be simply "new MapFrame(sample)"
*/

public class Editor extends XFrame
                 implements SaveableDocument, HasPreferences, SampleListener, PrintableDocument {

    // gui
    private JTable wjTable;
    private JPanel wjPanel;
    private ElementsPanel elemPanel;
    private JComponent metaView;

    // gui -- new
    private JPanel dataView;
    private JTabbedPane rolodex;

    // gui -- menus
    private JMenuItem resumMenu, cleanMenu, reverseMenu, reconcile;
    private JMenuItem indexMenu;
    private JMenuItem addMenu, remMenu;
    private JMenuItem plotAll, bargraphAll;
    private JMenuItem v1, v2, v3;

    // undo
    private UndoManager undoManager;
    private JMenuItem undoMenu, redoMenu;
    public void refreshUndoRedo() {
	undoMenu.setText(undoManager.getUndoPresentationName());
	undoMenu.setEnabled(undoManager.canUndo());
	if (!undoManager.canUndo())
	    undoMenu.setText(msg.getString("cant_undo"));
	redoMenu.setText(undoManager.getRedoPresentationName());
	redoMenu.setEnabled(undoManager.canRedo());
	if (!undoManager.canRedo())
	    redoMenu.setText(msg.getString("cant_redo"));
    }
    private void initUndoRedo() {
	undoManager = new UndoManager();
	sample.getUndoSupport().addUndoableEditListener(new UndoAdapter());
	refreshUndoRedo();
    }
    private class UndoAdapter implements UndoableEditListener {
	public void undoableEditHappened (UndoableEditEvent e) {
	    undoManager.addEdit(e.getEdit());
	    refreshUndoRedo();
	}
    }

    // data
    private Sample sample;

    // i18n
    private ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // BUG: measureMenu gets enabled/disabled here, when it's status
    // should be the AND of what editor thinks and what measure
    // thinks. add pleaseDim()/canUndim(), or just override setEnabled()?

    // SampleListener
    public void sampleRedated(SampleEvent e) {
	updateTitle(); // title
    }
    public void sampleDataChanged(SampleEvent e) {
	// menubar gets "*" if change made
	updateTitle();

	// -> todo: menubar needs undo/redo, save updated
    }
    public void sampleMetadataChanged(SampleEvent e) {
	// index menu: only if not indexed
	indexMenu.setEnabled(!sample.isIndexed());

	// clean menu: only if summed
	cleanMenu.setEnabled(sample.isSummed());

	// resum menu: only if elements present
	resumMenu.setEnabled(sample.elements != null);

        // measure menu: only if not summed
        if (measureMenu != null)
            measureMenu.setEnabled(!sample.isSummed());

	// title may have changed
	updateTitle();
    }
    public void sampleFormatChanged(SampleEvent e) {
	// resum/clean get disabled
	resumMenu.setEnabled(sample.elements != null);
	cleanMenu.setEnabled(sample.isSummed());
        if (measureMenu != null)
            measureMenu.setEnabled(!sample.isSummed());

	// view menus
	v1.setEnabled(sample.elements != null);
	v2.setEnabled(sample.elements != null);
	v3.setEnabled(sample.elements != null);

	// plot all, bargraph all
	plotAll.setEnabled(sample.elements!=null && sample.elements.size()>0);
	bargraphAll.setEnabled(sample.elements!=null && sample.elements.size()>0);

	// get rid of wj, elements tabs
	if (e != null) { // only for real events, not menu setup
	    if (!sample.hasWeiserjahre())
		rolodex.remove(wjPanel);
	    else if (rolodex.indexOfComponent(wjPanel) == -1)
		rolodex.add(wjPanel, msg.getString("tab_weiserjahre"));
	    if (sample.elements == null)
		rolodex.remove(elemPanel);
	    else if (rolodex.indexOfComponent(elemPanel) == -1)
		rolodex.add(elemPanel, msg.getString("tab_elements"));
	}
    }
    public void sampleElementsChanged(SampleEvent e) { }

    // SaveableDocument
    public String toString() {
	return sample.toString();
    }
    public boolean isSaved() {
	return !sample.isModified();
    }
    public void setFilename(String fn) {
	sample.meta.put("filename", fn);
    }
    public String getFilename() {
	return (String) sample.meta.get("filename");
    }
    public String getDocumentTitle() {
        String fn = getFilename();
        if (fn != null) {
            int lastSlash = fn.lastIndexOf(File.separatorChar);
            if (lastSlash != -1)
                fn = fn.substring(lastSlash+1);
            return fn;
        } else {
            return (String) sample.meta.get("title");
        }
    }
    public void save() {
        // make sure user isn't editing
        ((SampleDataView) dataView).stopEditing();

        // make sure they're all numbers -- no nulls, strings, etc.
        // abstract this out as "boolean verifyOnlyNumbers()" or something?
        for (int i=0; i<sample.data.size(); i++) {
            Object o = sample.data.get(i);
            if (o==null || !(o instanceof Integer)) { // integer?  or number?
                JOptionPane.showMessageDialog(this,
                                              "One or more years had bad (non-numeric) data, or no data:\n" +
                                              "- year " + sample.range.getStart().add(i) + " has " + (o==null ? "no value" : "value " + o),
                                              "Bad Data",
                                              JOptionPane.ERROR_MESSAGE);
                return;
                // BUG: return failure.  how?  UserCancelled
            }
        }

        // get filename from sample; fall back to user's choice
        String filename = (String) sample.meta.get("filename");
        if (filename == null) {

            // make sure metadata was entered
            if (!sample.wasMetadataChanged()) {
                int x = JOptionPane.showOptionDialog(this,
                                                      "You didn't set the metadata!",
                                                      "Metadata Untouched",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.QUESTION_MESSAGE,
                                                      null, // no icon
                                                      new String[] { "Save Anyway", "Cancel"},
                                                      null); // default
                if (x == 1) {
                    // show metadata tab, and abort.
                    rolodex.setSelectedIndex(1);
                    return; // user cancelled!
                }
            }

            // get target filename
            try {
                filename = corina.gui.FileDialog.showSingle("Save");
            } catch (UserCancelledException uce) {
                return;
            }

            // check for already-exists -- DUPLICATE CODE, REFACTOR
            if (new File(filename).exists() && !Overwrite.overwrite(filename))
                return; // BUG: should return FAILURE -- how?  UserCancelled
            sample.meta.put("filename", filename);
        }

        save(filename);
    }
    public void save(String filename) {
        // save sample
        try {
            sample.save((String) sample.meta.get("filename"));
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null,
                                          "There was an error while saving the file: \n" +
                                          ioe.getMessage(),
                                          "I/O Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            Bug.bug(e);
        }

        sample.clearModified();
        Platform.setModified(this, false); // mac os x
                                           // BIGGER ISSUE: need to separate/abstract window-title/window-modified setting
        // -- make window (jframe) a samplelistener for updating platform.modified?
        // -- (platform.modified sets either mac property, or resets *+title from getTitle()?)
        updateTitle();
    }

    // init methods
    private void initWJPanel() {
	// Mac OS X has animated progress bars, which is *not* wanted
	// for the histogram (they're not really animated here, but
	// they change phase when redrawn).  Cocoa has other special
	// bars to use for this sort of thing, but probably not
	// available to a normal Java program.  So lose the last
	// column, and add a hand-drawn histogram instead.

	// no wj?  die.  (Q: why didn't i have/need this before?  A: i
	// made it, but it never got displayed, so nobody checks to
	// see if it actually has any rows or columns)
	// if (!sample.hasWeiserjahre())
	// return;
	// -> i should go back to doing it this way.  don't have an
	// initialized wjtable/panel sitting around if it's not being used.
	// FIXME.

	// create the table
	wjTable = new JTable(new WJTableModel(sample));

	// select the first year
	wjTable.setRowSelectionAllowed(false);
	if (sample.hasWeiserjahre()) {
	    wjTable.setRowSelectionInterval(0, 0);
	    wjTable.setColumnSelectionInterval(sample.range.getStart().column() + 1,
					       sample.range.getStart().column() + 1);
	}

	// make the "Nr" column renderer a progress bar -- this recomputes max(count)!!!
	int max=0;
	if (sample.count != null)
	    max = ((Integer) Collections.max(sample.count)).intValue();
	wjTable.getColumnModel().getColumn(11).setCellRenderer(
			         new CountRenderer(max));

	// set font, gridlines, and colors -- these are all user preferences

	// put table and new modeline into a panel
	wjPanel = new JPanel(new BorderLayout(0, 0));
	wjPanel.add(new JScrollPane(wjTable,
				    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
		      BorderLayout.CENTER);
	wjPanel.add(new Modeline(wjTable, sample), BorderLayout.SOUTH);
    }

    private void initMetaView() {
        metaView = new SampleMetaView(sample, this); // SampleMeta2View is a work-in-progress
        sample.addSampleListener((SampleListener) metaView);
    }

    private void initElemPanel() {
        if (sample.elements != null) {
            elemPanel = new ElementsPanel(this);
            sample.addSampleListener(elemPanel);
        }
    }

    private void addCards() {
	// start fresh
	rolodex.removeAll();

	// all samples get data, meta
	rolodex.add(dataView = new SampleDataView(sample), msg.getString("tab_data"));
	rolodex.add(metaView, msg.getString("tab_metadata"));

	// wj and elements, if it's summed
	if (sample.hasWeiserjahre())
	    rolodex.add(wjPanel, msg.getString("tab_weiserjahre"));
	if (sample.elements != null)
	    rolodex.add(elemPanel, msg.getString("tab_elements"));
    }

    private void initRolodex() {
        // try also: BOTTOM, but that's worse, by Fitt's Law, isn't it?
        // (excel, for example, does that.)
        rolodex = new JTabbedPane(JTabbedPane.TOP);
        addCards();
        getContentPane().add(rolodex, BorderLayout.CENTER);
    }

    public Sample getSample() {
        return sample;
    }

    public void updateTitle() {
        setTitle(sample.toString());
    }

    // ask the user for a title for this (new) sample.  it's guaranteed to have a number, now!
    private String askTitle() throws UserCancelledException {
        return askTitle("");
    }
    private String askTitle(String defaultText) throws UserCancelledException {
        String title=defaultText;
        for (;;) {
            title = (String) JOptionPane.showInputDialog(null, // parent component
                                                         msg.getString("new_sample_prompt"), // message
                                                         msg.getString("new_sample"), // title
                                                         JOptionPane.QUESTION_MESSAGE,
                                                         null, // icon
                                                         null, // values (options)
                                                         title);

            // user cancelled?
            if (title == null)
                throw new UserCancelledException();

            // make sure there's a digit in there somewhere, and return.
            for (int i=0; i<title.length(); i++)
                if (Character.isDigit(title.charAt(i)))
                    return title;

            // no numbers!
            JOptionPane.showMessageDialog(null, "There's no number in that title.  I think you forgot the sample number.");

            // be sure to put in the user manual the trick for creating a sample
            // without a sample number, if they ever need that: put a digit on
            // the end, and remove it right away (heh heh).
        }
    }

    public Editor() {
        // ask user for title
        String title;
        try {
            title = askTitle();
        } catch (UserCancelledException uce) {
            dispose();
            return;
        }

        // make dataset ref, with our title
        sample = new Sample();
        sample.meta.put("title", title);

        // pass
        setup();
    }

    // new sample for site
    public Editor(Site s) {
        // ask user for title
        String title;
        try {
            title = askTitle(s.getName() + " ");
        } catch (UserCancelledException uce) {
            dispose();
            return;
        }

        // make dataset ref, with our title
        sample = new Sample();
        sample.meta.put("title", title);

        // pass
        setup();
    }

    public Editor(Sample sample) {
        // copy data ref
        this.sample = sample;

        // pass
        setup();
    }

    // setup common to both constructors
    private void setup() {
        // view area
        initWJPanel();
        initMetaView();
        initElemPanel();

        // i'll watch the data
        sample.addSampleListener(this);

        // title (must be before menubar)
        updateTitle();

        // menubar
        setJMenuBar(new XMenubar(this, makeMenus()));

        // put views into notecard-rolodex
        initRolodex();

        // set preferences
        refreshFromPreferences();

        // init undo/redo
        initUndoRedo();

        // pack, size, and show
        pack(); // is this needed?
        setSize(new Dimension(640, 480));
	show();

	/*
	// strategy: keep going down, until it would go off-screen, then start again.
	// -- unless there's somewhere else the user would like it (save bounds in file?)
	if (base == null) {
	    show();
	    doffset = getContentPane().getLocationOnScreen().y - getLocationOnScreen().y;
	    base = getLocationOnScreen();
	    // System.out.println("doffset = " + doffset);
	} else {
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(new java.awt.Point(base.x+offset, base.y+offset));
	    show(); // BUG!  don't show until i've made sure it's where i want it!
	    if (getBounds().x + getBounds().width > screen.width ||
		getBounds().y + getBounds().height > screen.height) {
		setLocation(new java.awt.Point(0, 0));
		offset = doffset;
	    } else {
		offset += doffset;
	    }
	    // show();
	}
	*/

        // datatable gets initial focus, and select year 1001
        dataView.requestFocus();
    }

    // bump windows down by this much
    /*
      private static int offset = 0; // _next_ window by this much
    private static java.awt.Point base = null;
    private static int doffset = 0; // height of the titlebar (i'll compute this at runtime)
    */

    // measure-mode menu
    private JMenuItem measureMenu=null;

    // menus for XMenubar constuctor
    private JMenu[] makeMenus() {
	// edit menu
	JMenu edit = Builder.makeMenu("edit");

	// undo
	undoMenu = new XMenubar.XMenuItem(msg.getString("cant_undo"));
        undoMenu.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize(msg.getString("undo_acc"))));
	undoMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    undoManager.undo();
		    refreshUndoRedo();
		}
	    });
	edit.add(undoMenu);
	redoMenu = new XMenubar.XMenuItem(msg.getString("cant_redo"));
	redoMenu.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize(msg.getString("redo_acc"))));
	redoMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    undoManager.redo();
		    refreshUndoRedo();
		}
	    });
	edit.add(redoMenu);

        // ---
        edit.addSeparator();

        final Sample glue7 = sample; // hack!

        // let's make the user feel at home
        JMenuItem cut = new XMenubar.XMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control X")));
        cut.setEnabled(false);
        edit.add(cut);

        // copy: put all data unto clipboard in 2-column format
        // JMenuItem copy = new XMenubar.XMenuItem("Copy"); // i18n!  use Builder!
        // copy.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control C"))); // accel C!
	JMenuItem copy = Builder.makeMenuItem("copy");
        copy.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
		    // REFACTOR: extract method -- but to where?

                    // save this sample, in 2-col format, to a string
                    PureStringWriter w = new PureStringWriter(10 * glue7.data.size());
                    new TwoColumn(w).save(glue7);

                    // copy that string to the clipboard
                    TextClipboard.copy(w.toString());
                } catch (IOException ioe) {
		    // can't happen -- it's i/o to memory, not a real i/o device
                }
            }
        });
        edit.add(copy);

        // paste: replace (insert?) data from clipboard (any format) into this sample
        JMenuItem paste = new XMenubar.XMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control V")));
        paste.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // get the stuff that's on the clipboard
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = c.getContents(null);
                if (t == null)
                    return; // clipboard contains no data

                try {
                    // copy text from the clipboard to ~/.corina/clipboard.
                    // (why?  because Sample(filename) takes arbitrary formats, load(Reader) does not.  (fixable?))
                    DataFlavor f = DataFlavor.selectBestTextFlavor(t.getTransferDataFlavors());
                    BufferedReader r = new BufferedReader(f.getReaderForText(t));
                    BufferedWriter w = new BufferedWriter(new FileWriter(Prefs.USER_PROPERTIES_DIR +
                                                                         File.separator + "clipboard"));
                    for (;;) {
                        String l = r.readLine();
                        if (l == null)
                            break;
                        w.write(l + '\n');
                    }

                    w.close();
                    r.close();

                    // now try to load it
                    Sample tmp = new Sample(Prefs.USER_PROPERTIES_DIR +
                                            File.separator + "clipboard");

                    // copy it here, except for the filename (".../clipboard")
                    Sample.copy(tmp, sample);
                    sample.meta.remove("filename");
                } catch (WrongFiletypeException wfte) {
                    // unreadable format.  tell user.
                    JOptionPane.showMessageDialog(null,
                                                  "The clipboard doesn't appear to have a dendro dataset.",
                                                  "Problem While Pasting",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IOException ioe) {
                    // shouldn't ever happen.  this means there was a problem reading or
                    // writing a "clipboard" file in USER_PROPERTIES_DIR.  probably a bug, then.
                    Bug.bug(ioe);
                    return;
                } catch (UnsupportedFlavorException ufe) {
                    // clipboard doesn't have text on it.  tell user.
                    JOptionPane.showMessageDialog(null,
                                                  "The clipboard doesn't appear to have text on it.",
                                                  "Problem While Pasting",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // fire all sorts of alarms
                sample.fireSampleRedated();
                sample.fireSampleDataChanged();
                sample.fireSampleMetadataChanged();
                sample.fireSampleFormatChanged();
                sample.fireSampleElementsChanged();

                // delete the file now, either way.  fixme: this should be in the finally
                // of the first try-catch, except it's needed in the second one.  hmm...
                new File(Prefs.USER_PROPERTIES_DIR + File.separator + "clipboard").delete();
                
                /* oh crap.  it iterates over the LOADERS[], passing them filenames.
                    i'll need to abstract out the iteration, and put loading from a
                    filename in sample(filename) only.  i guess that cleans up some
                    low-level things, but it means more work now.

                    err, no.  what do you want it to do, iterate over readers?
                    can you guarantee that a reader is reset()able to its start?
                    you might have to save the entire thing to a buffer ... er, that's
                    where it is.  you might have to get a reader passed, clone it,
                    try, clone, try, but readers probably don't work that way.  there's
                    got to be a way to do this.

                    (it's good this came up, though.  opening a file 5 times because
                     it's not the most popular format is killing i/o performance.)

                    quick hack just to make it work: save to ~/.corina/clipboard, ...
                    */
            }
        });
        edit.add(paste);
        
        // ---
        edit.addSeparator();

        // insert
	JMenuItem insert = new XMenubar.XMenuItem(msg.getString("insert_year"), 'I');
	insert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, Event.CTRL_MASK));
	insert.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    ((SampleDataView) dataView).insertYear();
		}
	    });
	edit.add(insert);

	// delete
	JMenuItem delete = new XMenubar.XMenuItem(msg.getString("delete_year"), 'D');
	delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, Event.CTRL_MASK));
	delete.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    ((SampleDataView) dataView).deleteYear();
		}
	    });
	edit.add(delete);

        // if comm present, start/stop measure
        if (false) { // Measure.hasSerialAPI()) {
            edit.addSeparator();
            measureMenu = new Measure(this).makeMenuItem();
            edit.add(measureMenu);
        }

	// view menu
	JMenu v = Builder.makeMenu("view");

	/*
	  this should be:
	  View
	  ----
	  Data        ^1
	  Metadata    ^2
	  Weiserjahre ^3
	  Elements    ^4
	  ---
	  Elements by Filenames
	  Elements by Summary Fields
	  Elements by All Fields
	  ---
	* Show Count as Histogram
	  Show Count as Numbers
	  ---
         Font >
         Size >
         (Style >)
         Text Color>
         Background Color>
         Show/Hide Gridlines

	  (wj,elements dimmed if not present)
	  (text/*, gridlines, and which meta view are global, and get saved!)

         (global means i'll need more references like open-recent.  abstract that out somehow?  GlobalMenu?  GlobalMenuItem?)
	 */

	ButtonGroup bg = new ButtonGroup();

	v1 = new XMenubar.XRadioButtonMenuItem(msg.getString("view_filenames"));
	v1.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    elemPanel.setView(ElementsPanel.VIEW_FILENAMES);
		}
	    });
	v.add(v1);
	bg.add(v1);

	v2 = new XMenubar.XRadioButtonMenuItem(msg.getString("view_standard"));
	v2.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    elemPanel.setView(ElementsPanel.VIEW_STANDARD);
		}
	    });
	v.add(v2);
	bg.add(v2);

	v3 = new XMenubar.XRadioButtonMenuItem(msg.getString("view_all"));
	v3.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    elemPanel.setView(ElementsPanel.VIEW_ALL);
		}
	    });
	v.add(v3);
	bg.add(v3);

	v1.setSelected(true);

	ButtonGroup bg2 = new ButtonGroup();

	/* TODO:
	   -- implement a new renderer for numbers-only (medium)
	   -- add hooks so these menuitems change renderers (easy)
	   -- (remember what renderer you used, as a hidden pref?) (easy)
	   -- these items are undimmed iff the sample has a nonnull .count field (easy)
	 */

	JMenuItem vc1 = new XMenubar.XRadioButtonMenuItem(msg.getString("view_histogram"));
	vc1.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // WRITE ME
		    System.out.println("view histogram");
		}
	    });

	JMenuItem vc2 = new XMenubar.XRadioButtonMenuItem(msg.getString("view_numbers"));
	vc2.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // WRITE ME
		    System.out.println("view numbers");
		}
	    });

	bg2.add(vc1);
	bg2.add(vc2);
	v.addSeparator();
	v.add(vc1);
	v.add(vc2);
	vc1.setSelected(true);

	// manipulate menu
	final JMenu s = Builder.makeMenu("manip");

	// manip, until now, was simply a catch-all for random stuff people wanted to do to samples.
	// make it an interface for operating on samples, so redate/truncate/index know that they're
	// manips, and can create their own menuitems, given a sample.

	/*
	  (menu "Manipulate" "M"
	    (menuitem "Redate" "R" "RedateDialog")
	    (separator))
	 */

	/*
	  idea #/n+1/:

	  { "Redate", 'R', 1 }
	  null // ---

	  ...

	  ActionListener a = new Actor();

	  private class XMenuItemWithID extends XMenubar.XMenuItem {
	    private int id;
	    // constructors
	  }

	  for (int i=0; i<menus.length; i++) {
	    JMenuItem m = new XMenuItemWithID(name, mnemonic, i);
	    s.add(m);
	    m.addActionListener(a);
	  }

	  final Editor me = this;

	  private class Actor extends AbstractAction {
	    public void actionPerformed(ActionEvent e) {
	    int id = ((XMenuItemWithID) e.getSource()).id;
	    switch (id) {
	    case 1: new RedateDialog(sample, me);	break;
	    case 2: new IndexDialog(sample);		break;
	    case 3: new TruncateDialog(sample);		break;
	    default: // blah
	  }
	*/

	// redate
	final Editor glue = this;
	JMenuItem redate = new XMenubar.XMenuItem(msg.getString("redate..."),
						  msg.getString("redate_key").charAt(0));
	// redate.setAccelerator(KeyStroke.getKeyStroke(msg.getString("redate_acc")));
	// control-R, etc., are grabbed by the table -- agH!
	redate.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    new RedateDialog(sample, glue);
		}
	    });
	s.add(redate);

	// index
	indexMenu = new XMenubar.XMenuItem(msg.getString("index..."),
					   msg.getString("index_key").charAt(0));
	// indexMenu.setAccelerator(KeyStroke.getKeyStroke(msg.getString("index_acc")));
	indexMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    new IndexDialog(sample, glue);
		}
	    });
	indexMenu.setEnabled(!sample.isIndexed());
	s.add(indexMenu);

    // truncate
    JMenuItem truncate = new XMenubar.XMenuItem(msg.getString("truncate..."),
                                                msg.getString("truncate_key").charAt(0));
    // truncate.setAccelerator(KeyStroke.getKeyStroke(msg.getString("truncate_acc")));
    truncate.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent ae) {
            new TruncateDialog(sample, glue);
        }
    });
    s.add(truncate);

    // reverse
    reverseMenu = new XMenubar.XMenuItem(msg.getString("reverse"),
                                         msg.getString("reverse_key").charAt(0));
    reverseMenu.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent ae) {
            // reverse, and add to the undo-stack
            sample.postEdit(Reverse.reverse(sample));
        }
    });
    s.add(reverseMenu);

    // ---
    s.addSeparator();

    // cross
    JMenuItem crossAgainst = new XMenubar.XMenuItem(msg.getString("cross_against..."),
                                                    msg.getString("cross_against_key").charAt(0));
    // crossAgainst.setAccelerator(KeyStroke.getKeyStroke(msg.getString("cross_against_acc")));
    crossAgainst.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent ae) {
            try {
                // select moving files
                List ss = FileDialog.showMulti("Crossdate \"" + sample + "\" against:");

                // (note also the Peter-catcher: see XMenubar.java)

                // fix for bug 228: filename may be null, and sequence uses it to hash,
                // so let's make up a fake filename that can't be a real filename.
                String filename = getFilename();
                if (filename == null)
                    filename = "\u011e"; // this can't begin a word!
                
                new CrossFrame(new Sequence(Collections.singletonList(filename), ss));
            } catch (UserCancelledException uce) {
                // do nothing
            }
        }
    });
    s.add(crossAgainst);

    // reconcile
    reconcile = new XMenubar.XMenuItem(msg.getString("reconcile"),
                                       msg.getString("reconcile_key").charAt(0));
    reconcile.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            // check for filename
            String filename = (String) sample.meta.get("filename");

            // if null, need to ask user.
            String target=null;
            if (filename==null) {
                // ask user here
                try {
                    target = FileDialog.showSingle(msg.getString("other_reading"));
                } catch (UserCancelledException uce) {
                    return;
                }
            }

            // try to guess
            try {
                target = Reconcile.guessOtherReading(filename);
            } catch (FileNotFoundException fnfe) {
                // ask user here -- REDUNDANT!
                try {
                    target = FileDialog.showSingle(msg.getString("other_reading"));
                } catch (UserCancelledException uce) {
                    return;
                }
            }

            try {
                // reconcile this and target
		new ReconcileDialog(sample, new Sample(target)); // IN TESTING!
		/*                Reconcile r = new Reconcile(sample, new Sample(target));
                r.run();

                // report it
                JEditorPane text = new JEditorPane("text/html", r.getReport());
                text.setEditable(false);
                JFrame myFrame = new JFrame(r.toString());
                myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                myFrame.getContentPane().add(new JScrollPane(text), BorderLayout.CENTER);
                myFrame.setSize(new Dimension(500, 400));
                myFrame.show();
*/
            } catch (IOException ioe) {
                return; // ack!
            } catch (Exception ex) {
                Bug.bug(ex);
            }
/*
 here's how the reconcile UI should work:
 -- auto-tile
 +------A Reading------+ +------C Reading------+
 |                                |   |                                |
 |                                |   |                                |
 |                                |   |                                |
 |                                |   |                                |
 |                                |   |                                |
 |                                |   |                                |
 |                                |   |                                |
 +-------------------------+  +-------------------------+
 +-----------------Reconcile Dialog-----------------+
 |                                                                     |
 |                                                                     |
 +--------------------------------------------------------+
 -- selecting an error row in the reconcile dialog selects that year in A and C
 */
        }
    });
    s.add(reconcile);

	// master menu
	JMenu m = Builder.makeMenu("sum");

	// re-sum
	resumMenu = new XMenubar.XMenuItem(msg.getString("resum"),
					   msg.getString("resum_key").charAt(0));
	resumMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // resum it
		    try {
			sample = Sum.sum(sample);
			
		    } catch (IOException ioe) {
			JOptionPane.showMessageDialog(null,
						      "There was an error while summing these files:\n" +
						         ioe.getMessage(),
						      "Error Summing",
						      JOptionPane.ERROR_MESSAGE);
			return;
		    } catch (Exception e) {
			Bug.bug(e);

			// BUG: if the user deletes all elements from
			// this sample, sum throws something to here...
		    }
		}
	    });
	m.add(resumMenu);

	// clean
	cleanMenu = new XMenubar.XMenuItem(msg.getString("clean"),
					   msg.getString("clean_key").charAt(0));
	cleanMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // clean, and add to undo-stack
		    sample.postEdit(Clean.clean(sample));
		}
	    });
	m.add(cleanMenu);

	// ---
	m.addSeparator();

    // add item -- ENABLED IFF THERE EXIST OTHER ELEMENTS OR DATA IS-EMPTY
    addMenu = new XMenubar.XMenuItem("Add...", 'A');
    addMenu.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            // open file dialog
            String filename;
            try {
                filename = FileDialog.showSingle("Add");
            } catch (UserCancelledException uce) {
                return;
            }

            // new elements, if needed
            if (sample.elements == null)
                sample.elements = new ArrayList();

            // ADD CHECK: make sure indexed+indexed, or raw+raw

            // remember how many elements there used to be
            int numOld = sample.elements.size();

            // add -- if summed, add each one
            Sample testSample=null;
            try {
                testSample = new Sample(filename);
            } catch (IOException ioe) {
                int x = JOptionPane.showConfirmDialog(null,
                                                      "The file \"" + filename + "\" could not be loaded.  Add anyway?",
                                                      "Unloadable file.",
                                                      JOptionPane.YES_NO_OPTION);
                if (x == JOptionPane.NO_OPTION)
                    return;
            }

            // for a summed sample, don't add it, but add its elements
            if (testSample.elements != null) {
                for (int i=0; i<testSample.elements.size(); i++)
                    sample.elements.add(testSample.elements.get(i)); // copy ref only
            } else {
                sample.elements.add(new Element(filename));
            }

            // modified, and update
            sample.setModified();
            sample.fireSampleElementsChanged();
            if (!Platform.isMac)
                sample.fireSampleMetadataChanged(); // so title gets updated (modified-flag)
            else
                Platform.setModified(glue, true);
        }
    });
    m.add(addMenu);

	// remove item
	remMenu = new XMenubar.XMenuItem("Remove", 'R');
	remMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    elemPanel.removeSelectedRows(); // fixme: make this undoable
		}
	    });
	m.add(remMenu);

	// graph menu
	JMenu d = Builder.makeMenu("graph");

    // plot
    JMenuItem plotMenu = new XMenubar.XMenuItem(msg.getString("graph"),
                                                msg.getString("graph_key").charAt(0));
    plotMenu.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize(msg.getString("graph_acc"))));
    plotMenu.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent ae) {
            new GraphFrame(sample);
        }
    });
    d.add(plotMenu);

	// plot all
	plotAll = new XMenubar.XMenuItem(msg.getString("graph_elements"),
					 msg.getString("graph_elements_key").charAt(0));
    plotAll.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize(msg.getString("graph_elements_acc"))));
    plotAll.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    new GraphFrame(sample.elements);
		}
	    });
	d.add(plotAll);

	// bargraph all
	bargraphAll = new XMenubar.XMenuItem(msg.getString("bargraph"),
					     msg.getString("bargraph_key").charAt(0));
	bargraphAll.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // FIXME: the bargraph should have this
		    // sample's (sum's) title.  but i think i have
		    // other infrastructure problems to settle
		    // before i can fix that.  darn.
		    new BargraphFrame(sample.elements);
		}
	    });
	d.add(bargraphAll);

    // site
    JMenu ss = Builder.makeMenu("site");

    // map
    JMenuItem map = new XMenubar.XMenuItem(msg.getString("map"),
                                           msg.getString("map_key").charAt(0));
    map.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent ae) {
            try {
                // get site
                Site mySite = SiteDB.getSiteDB().getSite(sample);

                // draw map there
                new MapFrame(mySite, mySite); // no MapFrame(Site) yet, but this is almost as good

            } catch (SiteNotFoundException snfe) {
                JOptionPane.showMessageDialog(null,
                                              "Couldn't find a site for this sample.",
                                              "No Site Found",
                                              JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                Bug.bug(e);
            }
        }
    });
    ss.add(map);

    // site properties
    JMenuItem props = new XMenubar.XMenuItem("Properties", 'P');
    props.addActionListener(new AbstractAction() {
        public void actionPerformed(ActionEvent ae) {
            try {
                // get my site -- FIXME: LoD says this should be sample.getSite();
                Site mySite = SiteDB.getSiteDB().getSite(sample);

                // show props
                SiteInfo.showInfo(mySite);
            } catch (SiteNotFoundException snfe) {
                JOptionPane.showMessageDialog(null,
                                              "Couldn't find a site for this sample.",
                                              "No Site Found",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    ss.add(props);

    // do fake-events to trick the menus into their correct states
    sampleFormatChanged(null);

    // store and return
    if (Platform.isMac) {
        return new JMenu[] { edit, v, s, m, d, ss, new WindowMenu(this) };
    } else {
        return new JMenu[] { edit, v, s, m, d, ss };
    }
            }

    // HasPreferences
    public void refreshFromPreferences() {
	// strategy: refresh each view i contain

	// data view
	((SampleDataView) dataView).refreshFromPreferences();

	// used to refreshFromPreferences() on elemPanel here, too.  but why?

	// add metadata update here, when it's written

	// --- old-style stuff below here: wj ---

	// reset fonts
	Font font = Font.getFont("corina.edit.font");
	if (font != null && wjTable != null)
		wjTable.setFont(font);

	// from font size, set table row height
	if (wjTable != null)
	    wjTable.setRowHeight((font == null ? 12 : font.getSize()) + 4);

	// disable gridlines, if requested
	boolean gridlines = Boolean.getBoolean("corina.edit.gridlines");
	if (wjTable != null)
	    wjTable.setShowGrid(gridlines);

	// set colors
	Color fore = Color.getColor("corina.edit.foreground");
	Color back = Color.getColor("corina.edit.background");
	if (back != null && wjTable != null)
	    wjTable.setBackground(back);
	if (fore != null && wjTable != null)
	    wjTable.setForeground(fore);
    }

    //
    // for serial-line measure-mode
    //
    public void measured(int x) {
	((SampleDataView) dataView).measured(x);
    }

    // printing
    public String getPrintTitle() {
	return getTitle();
    }
    public Pageable makePageable(PageFormat format) {
	return null;
    }
    public Printable makePrintable(PageFormat format) { // what to do with |format| here?
	//    return new SamplePrinter(sample);
	SamplePrinter printer = new SamplePrinter(sample);
	return Printer.print(printer);
    }
    public int getPrintingMethod() {
	return PRINTABLE;
    }
}
