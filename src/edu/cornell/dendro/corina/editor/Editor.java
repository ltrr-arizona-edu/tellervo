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

package edu.cornell.dendro.corina.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.graph.BargraphFrame.BargraphPanel;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.io.LegacyCorinaMeasuringDevice;
import edu.cornell.dendro.corina.io.LegacySerialSampleIO;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.prefs.PrefsEvent;
import edu.cornell.dendro.corina.prefs.PrefsListener;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.MapLink;
import edu.cornell.dendro.corina.tridasv2.ui.ComponentViewer;
import edu.cornell.dendro.corina.tridasv2.ui.TridasMetadataPanel;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.DocumentListener2;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.Overwrite;

/*
 left to do:
 -- extract EditorMenuBar as its own class?
 -- javadoc!
 
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

  -- guts of save() look really familiar ...
     ... that goes into XFrame, or some other general utility class

  -- WJ panel stuff could go into a wjpanel, perhaps subclassing dataviewpanel

  -- makeMenus is lines 455-868 (n=414).  ouch.  maybe this could be (compiled?) scheme.

  -- the 3 view menuitems can certainly be combined into one Action

  -- since the hold-down-control crap is gone now, those menus don't need object-scope.
  (what did i mean by this?)

  -- refactor mapframe so it can be simply "new MapFrame(sample)"
*/

public class Editor extends XFrame implements SaveableDocument, PrefsListener,
		SampleListener, PrintableDocument {

	private static final long serialVersionUID = 1L;

	private static final CorinaLog log = new CorinaLog("Editor");

	// gui
	private JTable wjTable;

	private JPanel wjPanel;

	private EditorMeasurePanel measurePanel = null;

	private JComponent metaView;

	private ComponentViewer componentsPanel = null;
	
	private BargraphPanel bargraphPanel = null;
	
	// gui -- new
	private SampleDataView dataView; // (a jpanel)

	// mozilla panel for maps
	private EditorMozillaMapPanel mozillaMapPanel;
	
	private JTabbedPane rolodex;
	
	// for menus we have to notify...
	private EditorFileMenu editorFileMenu;
	private EditorEditMenu editorEditMenu;

	// undo
	private UndoManager undoManager = new UndoManager();

	private UndoableEditSupport undoSupport = new UndoableEditSupport();

	public void postEdit(UndoableEdit x) {
		undoSupport.postEdit(x);
	}

	private void refreshUndoRedo(JMenuItem undoMenu, JMenuItem redoMenu) {
		undoMenu.setText(undoManager.getUndoPresentationName());
		undoMenu.setEnabled(undoManager.canUndo());
		if (!undoManager.canUndo())
			undoMenu.setText(I18n.getText("undo"));
		redoMenu.setText(undoManager.getRedoPresentationName());
		redoMenu.setEnabled(undoManager.canRedo());
		if (!undoManager.canRedo())
			redoMenu.setText(I18n.getText("redo"));
	}

	private void initUndoRedo() {
		undoManager = new UndoManager();
		undoSupport.addUndoableEditListener(new UndoAdapter());

		// ??
		// DISABLED: refreshUndoRedo(/* FAKE: */ null, null);
	}

	private class UndoAdapter implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit());
			// DISABLED: refreshUndoRedo(/* FAKE: */ null, null);
		}
	}

	// data
	private Sample sample;

	// BUG: measureMenu gets enabled/disabled here, when it's status
	// should be the AND of what editor thinks and what measure
	// thinks. add pleaseDim()/canUndim(), or just override setEnabled()?

	// SampleListener
	public void sampleRedated(SampleEvent e) {
		updateTitle(); // title
	}

	public void sampleDataChanged(SampleEvent e) {
		// menubar gets "*" if change made -- BUG: no, then meta field modified?
		// gets set, so this ISN'T NEEDED.  right?
		updateTitle();

		// -> todo: menubar needs undo/redo, save updated
	}

	public void sampleMetadataChanged(SampleEvent e) {
		// title may have changed
		updateTitle();

		// get rid of wj, elements tabs
		if (e != null) { // only for real events, not menu setup -- HACK!
			if (!sample.hasWeiserjahre())
				rolodex.remove(wjPanel);
			else if (rolodex.indexOfComponent(wjPanel) == -1)
				rolodex.add(wjPanel, I18n.getText("tab_weiserjahre"));
		}
	}

	public void sampleElementsChanged(SampleEvent e) {
	}

	// SaveableDocument
	@Override
	public String toString() {
		return sample.toString();
	}

	public boolean isSaved() {
		return !sample.isModified();
	}

	public void setFilename(String fn) {
		sample.setMeta("filename", fn);
	}

	public String getFilename() {
		return (String) sample.getMeta("filename");
	}

	public String getDocumentTitle() {
		String fn = getFilename();
		if (fn != null) { // REFACTOR: why's this not return new File(fn).getName()?
			int lastSlash = fn.lastIndexOf(File.separatorChar);
			if (lastSlash != -1)
				fn = fn.substring(lastSlash + 1);
			return fn;
		} else {
			return sample.getDisplayTitle();
		}
	}
	
    // can we use save as?
    public boolean isNameChangeable() { 
    	SampleLoader loader = sample.getLoader();

    	// we can only save file elements!
    	if(loader == null || loader instanceof FileElement)
    		return true;
    	
    	return false;
    };

	public void save() {
		// make sure we're not measuring
		this.stopMeasuring();
		
		// make sure user isn't editing
		dataView.stopEditing(false);
		
		// make sure they're all numbers -- no nulls, strings, etc.
		// we don't have to do this anymore!
		
		// get filename from sample; fall back to user's choice
		if (sample.getLoader() == null) {

			// make sure metadata was entered
			if (!sample.wasMetadataChanged()) {
				// what i'd prefer:
				// Alert.ask("You didn't set the metadata!", { "Save Anyway", "Cancel" })
				// or even: Alert.ask("You didn't set the metadata! [Save Anyway] [Cancel]"); (!)
				/*
				 can i put something like this directly in a resource?
				 You didn't set the metadata! [Save Anyway] [Cancel]
				 that's crazy talk!
				 */
				int x = JOptionPane.showOptionDialog(this,
						"You didn't set the metadata!", "Metadata Untouched",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, // no icon
						new String[] { "Save Anyway", "Cancel" }, null); // default
				if (x == 1) {
					// show metadata tab, and abort.
					rolodex.setSelectedIndex(1);
					return; // user cancelled!
				}
			}

			String filename = (String) sample.getMeta("filename"); // BUG: why not containsKey()?
			// get target filename
			try {
				filename = FileDialog.showSingle("Save");

				// check for already-exists
				Overwrite.overwrite(filename);
			} catch (UserCancelledException uce) {
				return;
			}

			sample.setMeta("filename", filename);
			
			// attach a FileElement to it
			sample.setLoader(new FileElement(filename));
		}

		// complain if it's not complete yet 
		// but only if it's not derived!
		if(!sample.getSampleType().isDerived() && !sample.hasMeta(Metadata.RADIUS)) {
			JOptionPane.showMessageDialog(this,
					"Metadata not complete. You must choose a radius.",
					"Save error", JOptionPane.ERROR_MESSAGE);
			rolodex.setSelectedIndex(1);
			return;
		}
		
		// now, actually try and save the sample
		try {
			sample.getLoader().save(sample);
		} catch (IOException ioe) {
			Alert.error("I/O Error", "There was an error while saving the file: \n" + ioe.getMessage());
			return;
		} catch (Exception e) {
			new Bug(e);
		}

		// set the necessary bits...
		sample.clearModified();
		sample.fireSampleMetadataChanged(); // things may have changed...
		App.platform.setModified(this, false);
		updateTitle();
	}

	public Object getSavedDocument() {
		return sample;
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
		if (!sample.hasWeiserjahre())
			return;

		// create the table
		wjTable = new JTable(new WJTableModel(sample));

		// select the first year
		wjTable.setRowSelectionAllowed(false);
		if (sample.hasWeiserjahre()) {
			wjTable.setRowSelectionInterval(0, 0);
			wjTable.setColumnSelectionInterval(
					sample.getRange().getStart().column() + 1, sample.getRange()
							.getStart().column() + 1);
		}

		// make the "Nr" column renderer a progress bar -- this recomputes max(count)!!!
		int max = 0;
		if (sample.hasCount())
			max = (Collections.max(sample.getCount())).intValue();
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
		metaView = new TridasMetadataPanel(sample);
	}

	private void initComponentsPanel() {
		if(sample.getSampleType().isDerived())
			componentsPanel = new ComponentViewer(sample);
	}

		
	private void initMozillaMapPanel() {
		MapLink link = new MapLink(sample.getSeries());
	
		// no link? no panel!
		if(link == null)
			return;
		
		// See if we have access to mozilla libs
		try {
			// this loads the DLL...
			Class.forName("org.mozilla.browser");
		}
		catch (Exception e) {
			// driver not installed...
			System.out.println("No mozilla - no map");
			System.out.println(e.getMessage());
			return;
		}
		catch (Error e) {
			// native interface not installed...
			System.out.println("No mozilla - no map");
			System.out.println(e.getMessage());
			return;
		}
		
		try {
			mozillaMapPanel = new EditorMozillaMapPanel(link);
		} catch (Throwable e) {
			// no mapping? no problem. don't fail violently!
		}
	}

	private void addCards() {
		// start fresh
		rolodex.removeAll();

		// all samples get data, meta
		dataView = new SampleDataView(sample);
		rolodex.add(dataView, I18n.getText("tab_data"));
		rolodex.add(metaView, I18n.getText("tab_metadata"));

		// wj and elements, if it's summed
		if (sample.hasWeiserjahre())
			rolodex.add(wjPanel, I18n.getText("tab_weiserjahre"));
		
		if(componentsPanel != null) {
			rolodex.add(componentsPanel, I18n.getText("tab_components"));			
			
			// let the components panel know it's being set as visible...
			rolodex.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(rolodex.getSelectedComponent() == componentsPanel)
						componentsPanel.notifyPanelVisible();
				}
			});
		}
				
		if(mozillaMapPanel != null)
			rolodex.add(mozillaMapPanel, "Map");
	}

	private void initRolodex() {
		// try also: BOTTOM, but that's worse, by Fitt's Law, isn't it?
		// (excel, for example, does that.)
		rolodex = new JTabbedPane(SwingConstants.TOP);
		// rolodex.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0)); -- but the old frame is still there!  hmm...
		addCards();
		getContentPane().add(rolodex, BorderLayout.CENTER);
	}

	public Sample getSample() {
		return sample;
	}

	public void updateTitle() {
		SampleType st = sample.getSampleType();
		
		setTitle(sample.toString() + 
				((st != null) ? (" [" + st + "]") : "") +
				" - " +
				Build.VERSION + " " +
				Build.TIMESTAMP);
	}

	

	
	
	// ask the user for a title for this (new) sample.  it's guaranteed to have a number, now!
	/*
	 TODO:
	 -- esc doesn't work -- no, this is a problem with OKCancel in 1.4.1, not here
	 -- (initial value is name of this site?)
	 ---- (askTitle(Site)?)
	 -- (title for non-mac platforms?)
	 -- (i18n / extract text)
	 -- (import classes so i don't need fq here)
	 */
	/*
	 what's truly unique about this?
	 ... AskText extends JDialog ...
	 pass it:
	 -- initial text
	 -- dictionary for autocomplete
	 -- instructions text
	 -- help tag
	 then, simply:
	 ask.show();
	 title = ask.getResult();
	 */

	private static String askTitle() throws UserCancelledException {		
		JLabel line1 = new JLabel("Enter a name for the new sample.");
		JLabel line2 = new JLabel("Names are usually in the form of a single capital letter.");
		JLabel line3 = new JLabel(
				"(You can easily change this later)");
		// jmultilinelabel for last 2 lines?
		line2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		line3.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JTextField input = new JTextField(); //new AutoComplete("", 30);
		// defaults to site names -- TODO: pass current site in as initial text?

		final boolean isOk[] = new boolean[1];

		//JButton help = Builder.makeButton("help");
		//Help.addToButton(help, "identification");
		JButton cancel = Builder.makeButton("cancel");
		final JButton ok = Builder.makeButton("ok");
		ok.setEnabled(false);

		input.getDocument().addDocumentListener(new DocumentListener2() {
			@Override
			public void update(DocumentEvent e) {
				try {
					Document doc = e.getDocument();
					String text = doc.getText(0, doc.getLength()); // BETTER: why not just input.getText()?
					ok.setEnabled(containsLetter(text)); // FIXME: combine these!
				} catch (BadLocationException ble) {
					// can't happen
				}
			}
		});

		JPanel text = Layout.boxLayoutY(line1, line2, line3);
		JPanel buttons = Layout.buttonLayout(cancel, ok);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		JPanel content = Layout.borderLayout(text, null, input, null, null);
		JPanel fixed = Layout.borderLayout(content, null, null, null, buttons);

		fixed.setBorder(BorderFactory.createEmptyBorder(10, 14, 6, 14));

		final JDialog dialog = new JDialog(new Frame(), true);
		dialog.getContentPane().add(fixed);

		ActionListener okCancel = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isOk[0] = (e.getSource() == ok);
				dialog.dispose();
			}
		};
		ok.addActionListener(okCancel);
		cancel.addActionListener(okCancel);

		dialog.pack();
		OKCancel.addKeyboardDefaults(ok);
		input.selectAll(); // (if there's anything here, later, e.g., site name)
		input.requestFocus();
		Center.center(dialog);
		dialog.setVisible(true);

		// then, after it's hidden (by cancel, ok, or close-box)...

		if (!isOk[0])
			throw new UserCancelledException();

		return input.getText();
	}

	// FIXME: disable the "ok" button if there's no number in the title, but
	// put a notice in the dialog saying it must have a number!
	private String askTitleOld(String defaultText)
			throws UserCancelledException {
		String title = defaultText;
		for (;;) {
			title = (String) JOptionPane.showInputDialog(null, // parent component
					I18n.getText("new_sample_prompt"), // message
					I18n.getText("new_sample"), // title
					JOptionPane.QUESTION_MESSAGE, Builder
							.getIcon("Tree-64x64.png", 64), // null, // icon
					null, // values (options)
					title);

			// user cancelled?
			if (title == null)
				throw new UserCancelledException();

			// make sure there's a digit in there somewhere, and return.
			if (containsDigit(title) && containsLetter(title))
				return title;

			// no numbers!
			// (FIXME: this error shouldn't exist!)
			Alert
					.error("No number",
							"There's no number in that title.  I think you forgot the sample number.");

			// be sure to put in the user manual the trick for creating a sample
			// without a sample number, if they ever need that: put a digit on
			// the end, and remove it right away (heh heh).
		}
	}

	// if i need this anywhere else, move to util?
	private static boolean containsDigit(String s) {
		for (int i = 0; i < s.length(); i++)
			if (Character.isDigit(s.charAt(i)))
				return true;
		return false;
	}

	private static boolean containsLetter(String s) {
		for (int i = 0; i < s.length(); i++)
			if (Character.isLetter(s.charAt(i)))
				return true;
		return false;
	}

	// TODO: want single-instance editors.
	// so:
	// -- make this private
	// -- (should it just be Editor(filename)?)
	// -- keep a (sample,editor) hash
	// -- create a public getEditor(Sample)
	// -- if an editor is in the hash, just front() it
	// -- if it's not, add it
	// -- on dispose(), remove it from the hash
	public Editor(Sample sample) {
		// copy data ref
		this.sample = sample;

		// pass
		setup();
	}

	// setup common to both constructors
	private void setup() {
		
		// first, make sure we stop measuring if the window is closed.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				stopMeasuring();
			}
		});
		
		// view area
		initWJPanel();
		initMetaView();
		initComponentsPanel();
		//initElemPanel();
		initMozillaMapPanel();

		// i'll watch the data
		sample.addSampleListener(this);

		// title (must be before menubar)
		updateTitle();

		// put views into notecard-rolodex
		initRolodex();

		// set preferences
		setUIFromPrefs();
		
		// menubar
		// This must happen *after* initRolodex(), as dataview and elempanel come from it.
		JMenuBar menubar = new JMenuBar();

		// TODO: extend CorinaMenuBar
		menubar.add(new EditorFileMenu(this, sample));
		editorEditMenu = new EditorEditMenu(sample, dataView, this);
		menubar.add(editorEditMenu);
		menubar.add(new EditorToolsMenu(sample, this));
		menubar.add(new EditorGraphMenu(sample));
		//menubar.add(new EditorSiteMenu(sample));
		if (App.platform.isMac())
			menubar.add(new WindowMenu(this));
		menubar.add(new HelpMenu());
		setJMenuBar(menubar);

		if(mozillaMapPanel != null)
			makeMenusHeavyweight();
		
		// init undo/redo
		initUndoRedo();

		App.prefs.addPrefsListener(this);
		// pack, size, and show
		pack(); // is this needed?
		setSize(new Dimension(640, 480));
		// TODO: store window position, X-style ("WIDTHxHEIGHT+LEFT+TOP"), so it always re-appears in the same place.
		// Q: store the resolution, as well, so the relative position
		// is the same, or just make sure the absolute is within range?
		// i can store the position either in a ;WINDOW field, or beyond the ~author line.
		Center.center(this);
		setVisible(true);

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
	
	/**
	 * When using heavyweight components, we have to make sure menus extend over them!
	 */
	private void makeMenusHeavyweight() {
		JMenuBar menus = getJMenuBar();
		
		for(int i = 0; i < menus.getMenuCount(); i++)
			menus.getMenu(i).getPopupMenu().setLightWeightPopupEnabled(false);
	}

	// DESIGN: move all editor menus into corina.editor.menus.ManipulateMenu, etc.

	/*
	 TODO:
	 -- resumming used to call Platform.setModified(editor, true) for isMac.
	 that's bad.  instead, Editor should be a SampleListener which calls
	 setMod(this,meta.mod?) on metadataChanged() -- or anything, actually.
	 */

	private void setUIFromPrefs() {
		if (wjTable == null)
			return;
		Font font = App.prefs.getFontPref(Prefs.EDIT_FONT, null);
		if (font != null)
			wjTable.setFont(font);
		// BUG: this doesn't reset the row-heights!

		// from font size, set table row height
		wjTable.setRowHeight((font == null ? 12 : font.getSize()) + 4);

		// disable gridlines, if requested
		boolean gridlines = Boolean.valueOf(
				App.prefs.getPref(Prefs.EDIT_GRIDLINES, "true")).booleanValue();
		wjTable.setShowGrid(gridlines);

		// set colors
		wjTable.setBackground(App.prefs.getColorPref(Prefs.EDIT_BACKGROUND,
				Color.white));
		wjTable.setForeground(App.prefs.getColorPref(Prefs.EDIT_FOREGROUND,
				Color.black));
		wjTable.repaint();
	}

	// PrefsListener
	public void prefChanged(PrefsEvent e) {
		// strategy: refresh each view i contain

		// data view
		// TODO: remove this commented line now that HasPreferences usage has been replaced
		// with PrefsListener
		//dataView.refreshFromPreferences();

		// used to refreshFromPreferences() on elemPanel here, too.  but why?

		// add metadata update here, when it's written

		setUIFromPrefs();
	}

	//
	// for serial-line measure-mode
	//
	public Year measured(int x) {
		return dataView.measured(x);
	}

	// printing
	public String getPrintTitle() {
		return getTitle();
	}

	public Object getPrinter(PageFormat pf) {
		// what to do with |format| here?
		
		SampleBit bits = SampleBit.askBits(this);
		
		// user cancelled anyway...
		if(bits == null)
			return null;
		
		SamplePrintEditor spe = new SamplePrintEditor(sample, bits, this, (int) pf.getImageableWidth());
		
		return spe.getPrintable();
		
//		return null;

		// TODO: use askWhichPages() to figure out which sections to print
		// then pass to SamplePrinter(sample, bool[])
		// BUT: how to distinguish?  as an Editor, i'll represent 2 PrintableDocs!

		//return new SamplePrinter(sample);
	}

	// TODO: use me!
	private static boolean[] askWhichPages(Sample s, int def)
			throws UserCancelledException {
		// dialog
		final JDialog d = new JDialog(new Frame(), "", true); // TODO: modal?

		// components
		JLabel question = new JLabel("Print which sections?"); // TODO: i18n
		final JCheckBox s1 = new JCheckBox(I18n.getText("tab_data"), def == 0);
		final JCheckBox s2 = new JCheckBox(I18n.getText("tab_metadata"),
				def == 1);
		final JCheckBox s3 = new JCheckBox(I18n.getText("tab_weiserjahre"),
				def == 2);
		final JCheckBox s4 = new JCheckBox(I18n.getText("tab_components"),
				def == 3);
		// dim sections which aren't available
		s3.setEnabled(s.hasWeiserjahre());
		s4.setEnabled(s.getElements() != null); // FIXME: hasElements method!
		// FIXME: if s1-4 is an array, i can simply say s[def].setEnabled(true)
		// -- if def=0..3
		final JButton cancel = Builder.makeButton("cancel");
		final JButton ok = Builder.makeButton("print");
		Component indent = Box.createHorizontalStrut(14);

		// on ok/cancel, done
		final boolean okClicked[] = new boolean[1];
		ActionListener a = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okClicked[0] = (e.getSource() == ok);
				d.dispose();
			}
		};
		cancel.addActionListener(a);
		ok.addActionListener(a);

		// if you uncheck all, "print" gets dimmed
		ActionListener b = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok.setEnabled(s1.isSelected() || s2.isSelected()
						|| s3.isSelected() || s4.isSelected());
			}
		};
		s1.addActionListener(b);
		s2.addActionListener(b);
		s3.addActionListener(b);
		s4.addActionListener(b);

		// layout
		JPanel checks = Layout.boxLayoutY(s1, s2, s3, s4);
		JPanel buttons = Layout.buttonLayout(cancel, ok);
		JPanel content = Layout.borderLayout(question, indent, checks, null,
				buttons);
		question.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
		buttons.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// display
		d.setContentPane(content);
		OKCancel.addKeyboardDefaults(ok);
		d.pack();
		d.setResizable(false);
		d.setVisible(true);

		// -- (user selects something) --

		// maybe cancel
		if (!okClicked[0])
			throw new UserCancelledException();

		// else return it
		boolean result[] = new boolean[4];
		result[0] = s1.isSelected();
		result[1] = s2.isSelected();
		result[2] = s3.isSelected();
		result[3] = s4.isSelected();
		return result;
	}
	
	public void toggleMeasuring() {
		// are we already measuring?
		if(measurePanel != null) {
			stopMeasuring();
			return;
		}
		
		// ok, start measuring, if we can!
		LegacySerialSampleIO dataPort;
		try {
			dataPort = LegacyCorinaMeasuringDevice.initialize();
		}
		catch (IOException ioe) {
			Alert.error("Couldn't start measuring", 
					"There was an error while initializing the external communications device: " +
					ioe.toString());
			return;
		}
		
		editorEditMenu.setMeasuring(true);
		dataView.enableEditing(false);
		
		// add the measure panel...
		measurePanel = new EditorMeasurePanel(this, dataPort);
		add(measurePanel, BorderLayout.SOUTH);
		getContentPane().validate();
		getContentPane().repaint();
	}
	
	public void stopMeasuring() {
		if (measurePanel != null) {
			measurePanel.cleanup();
			remove(measurePanel);
			editorEditMenu.setMeasuring(false);
			dataView.enableEditing(true);
			getContentPane().validate();
			getContentPane().repaint();
			measurePanel = null;
		}
	}
	
	/**
	 * Get the sampleDataView
	 * @return The SampleDataVeiw I am holding
	 */
	public SampleDataView getSampleDataView() {
		return dataView;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		App.prefs.removePrefsListener(this);
	}

	public static void main(String args[]) throws Exception {
		try {
			Sample s = new FileElement(args[0]).load();

			boolean x[] = askWhichPages(s, 3);
			System.out.print("x[] = { ");
			for (int i = 0; i < x.length; i++) {
				System.out.print(x[i] ? "#t" : "#f");
				System.out.print(", ");
			}
			System.out.println("}");
		} catch (UserCancelledException uce) {
			System.out.println("user cancelled");
		}
	}
}
