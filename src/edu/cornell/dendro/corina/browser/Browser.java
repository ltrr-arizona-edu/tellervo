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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.browser;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.cross.RangeRenderer;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.Scripts;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.manip.Sum;
import edu.cornell.dendro.corina.metadata.*;
import edu.cornell.dendro.corina.prefs.Geometry;
import edu.cornell.dendro.corina.print.ByLine;
import edu.cornell.dendro.corina.print.EmptyLine;
import edu.cornell.dendro.corina.print.Line;
import edu.cornell.dendro.corina.print.Printer;
import edu.cornell.dendro.corina.print.TabbedLineFactory;
import edu.cornell.dendro.corina.print.TextLine;
import edu.cornell.dendro.corina.print.ThinLine;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.SiteDB;
import edu.cornell.dendro.corina.site.SiteNotFoundException;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.NaturalSort;
import edu.cornell.dendro.corina.util.TextClipboard;
import edu.cornell.dendro.corina.util.GreedyProgressMonitor;
import edu.cornell.dendro.corina.gui.XFrame;

/**
   Corina's browser.

   <p>-- WRITEME</p>
 
   <h2>Left to do</h2>
   <ul>
     <li>Refactor!  It's huge!  Some suggestions:
     <li>Does right-click on the header really need to show the popup?  Why?  If I get rid of that,
         several things get a lot simpler.
     <li>Extract each menu into its own class -- BrowserViewMenu should also have FieldCheckBoxMenuItem (private)
     <li>Extract the menubar itself into its own class
     <li>Extract printing into its own class
     <li>Need good public interface for parts of this that extracted/other classes need; see BrowserPrinter for an example
     <li>Extract the header operations (sorting, visibility, persistence, etc.) to their own class?
     <li>Extract the table itself (think browse, Summary, SampleRef) into its own class
     <li>Rename this to "BrowserWindow"
     <li>I18n
     <li>Javadoc
     <li>ODD_ROW_COLOR doesn't belong here; extract to corina.util?
     <li>... and gobs more -- it's more of a proof-of-concept than a usable program, at this point.
     <li>Support for arbitrary DataSources (future! very future!)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Browser extends XFrame {
	/*
	 -- refactor!
	 
	 -- add ITRDB support, now that i can efficiently load URLs.  (need helper classes for ftp dir listing?)
	 -- probably should abstract out FileSystem->(LocalFileSystem,RemoteFileSystem)  (later!)

	 -- RET on a non-sample file opens it (canopener!)
	 -- full drag-n-drop
	 -- cmd-up/backspace to go up a folder?
	 -- ugh, when folderpopup has focus, up/down cause all hell to break loose!  (well, sort of)  possible to fix?
	 -- type at table to jump to file?

	 -- i18n. (futured.)  (nearly done, too.)

	 -- possible to select multiple files for summing/plotting/grids/bargraphs/etc.?  (what's my interface?  ps7-style popup?)
	 -- MENUS! :
	 -- menu: File -> { open, plot, index (!), crossdate (?), grid }
	 -- menu: Edit -> { Undo, Redo, Cut/Copy/Paste, Select All, Select None }
	 -- menu: Help -> Corina Help

	 -- ps7 context menu: [ open, select all, deselect all, ---, rename, batch rename..., delete, ---, (rotate), ---, reveal location in finder, new folder, ---, (rankings) ]

	 -- add "little trash icon" like ps7 has.  (ready, just needs dnd)

	 -- whenever "fields" changes, save it as a preference ("corina.browser.fields = name size modified format range length")
	 -- set initial value of fields from preference
	 -- if no preference, pick something sensible
	 -- store in order, including column-reordering mangling
	 -- load on open.  when to save?  -- when selecting a menuitem; -- when reordering the columns.

	 -- search string "pam am" should match "pam, 7am", but not "pam" alone (!)

	 -- highlight search term matches in the table -- this sounds hard

	 -- need something along the lines of:
	 ---- open in new tab
	 ---- clone this view (in another tab, window)
	 ---- i dunno, but some way to manage multiple views.

	 -- feature: show range graphically, as a bar

	 // TODO: add "show all fields" / "show only filename" features?
	 */

	/** Return the folder the browser is currently viewing.
	 @return the folder currently being browsed */
	public String getFolder() {
		return folder;
	}

	void setFolder(String newFolder) {
		folder = newFolder;
		if (label1 != null) // !!?!
			updateSummary(); // PERF: does this get called twice, now?
		App.prefs.setPref("corina.browser.folder", folder);
	}

	private JLabel label1, label2;

	/*
	 TODO:
	 [x] -- list the number of selected files, as well
	 [x] ---- (and update this when the selection size changes)
	 [ ] -- say "samples", unless you're really looking at all files
	 [x] -- if this is a site, show the site name in big text
	 [x] ---- (and update this when the folder changes)
	 [ ] ---- with a trigger (button?) to view/edit it
	 [ ] ---- if it's not a site, add a trigger (button?) to make it one
	 */
	private void updateSummary() {
		// label 1: "<code>: <site name>"
		try {
			Site site = SiteDB.getSiteDB().getSite(new File(folder));
			String code = site.getCode();
			String name = site.getName();
			label1.setText(code + ": " + name);
		} catch (SiteNotFoundException snfe) {
			label1.setText("(not a site)");
		}
		// TODO: do this in background!  loading the sitedb won't be super-fast,
		// so do it in parallel; when it's done loading, then set the label here.
		// (for future updates, it won't need its own thread)

		// FIXME: if no site here, put "(no site here)" or similar text,
		// but (1) smaller, and (2) dimmer?

		// label 2: "Focused on x of y files/folders" / "x files/folders"
		String text;
		if (searchField.isEmpty()) {
			if (files.size() == 1)
				text = "1 sample";
			else
				text = files.size() + " samples";
		} else {
			text = "Focused on " + visibleFiles.size() + " of " + files.size()
					+ " samples";
		}

		// ", %d selected"
		int n = table.getSelectedRows().length;
		if (n != 0)
			text += ", " + n + " selected";

		// FIXME: too many temp string allocated!  would stringbufs help?

		label2.setText(text);
	}

	private SearchField searchField;

	/*private*/JTable table;

	private AbstractTableModel browserModel;

	// REFACTOR: this constructor runs over 150 lines (!!!)
	public Browser() {
		super(); // get the tree icon goodness!
		setTitle("Corina Browser");

		String dir = App.prefs.getPref("corina.browser.folder"); // BUG: what if this folder doesn't exist?
		if (dir == null)
			dir = App.prefs.getPref("corina.dir.data"); // BUG: what if this is null, too?
		// BUG: what if this folder doesn't exist?

		// if no folder, use |user.dir|
		if (dir == null || dir.equals(""))
			dir = System.getProperty("user.dir");

		// if there's something wrong with that, default to this folder.
		// (the other option is user.home, the user's home, but i think
		// this is probably more likely what the user wants.  plus, there's
		// no guarantee that user.home exists, so i'd need even more error
		// handling for that.)
		{
			File folder = new File(dir);
			if (!folder.exists() || !folder.isDirectory()) {
				// use full path
				folder = new File(System.getProperty("user.dir"));
				dir = folder.getPath();
			}
		}

		// watch size/location -- EXTRACT this?
		String geom = App.prefs.getPref("corina.browser.geometry");
		if (geom == null)
			geom = "600x400+100+20";
		Geometry.decode(this, geom); // FIXME: bad interface!  (really?)
		final JFrame glue = this;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				App.prefs.setPref("corina.browser.geometry", Geometry
						.encode(glue));
			}

			@Override
			public void componentResized(ComponentEvent e) {
				App.prefs.setPref("corina.browser.geometry", Geometry
						.encode(glue));
			}
			// FIXME[PERF]: should i set this only on dispose()?  it saves far too often, right now.
		});
		// ---] (end maybe-extract block)

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		// "folder-tree | browser" split
		tree = new FolderTree(this);
		JSplitPane p2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				tree, p);
		p2.setContinuousLayout(true); // -- when it's faster (my renderers suck)
		setContentPane(p2);

		// set folder
		setFolder(dir); // -- REMOVE: this will get done by FolderTree() automatically
		// TODO: select row of tree which corresponds to |dir| (which automatically calls setFolder())

		// labels
		label1 = new JLabel();
		label1.setFont(label1.getFont().deriveFont(
				label1.getFont().getSize() * 1.4f)); // (!)
		label1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // WHY?
		label2 = new JLabel("", SwingConstants.CENTER); // WHY?
		label2.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8)); // WHY?

		{
			// north is another panel
			JPanel north = new JPanel();
			north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));

			// stack labels
			JPanel stack = Layout.boxLayoutY(label1, label2);
			north.add(stack);

			// |<-- space -->|
			north.add(Box.createHorizontalGlue());

			// load default fields from prefs
			loadFields();

			// center is the table (but searchfield needs this, so put it here)
			browserModel = new BrowserTableModel();
			table = new JTable(browserModel) {
				@Override
				public boolean isManagingFocus() {
					return false; // don't let the table see tab/ctrl-tab -- DOESN'T WORK ON WIN32?
				}
				// (POSSIBLE to do without extending jtable?

				// DEPRECATED in 1.4 -- see:
				// http://java.sun.com/j2se/1.4.1/docs/api/javax/swing/JComponent.html#isManagingFocus()
			};

			// watch for selection changes
			table.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							updateSummary();
						}
					});

			// custom header renderer: show which column is used for the sort
			shr = new SortedHeaderRenderer(table, nameOfField(sortField));
			table.getTableHeader().setDefaultRenderer(shr);

			// popup
			new BrowserContextMenu(this);

			// grid style: vertical lines, in light-gray, only
			table.setShowGrid(false);
			table.setShowVerticalLines(true);
			table.setGridColor(Color.lightGray);

			addIconsForFirstColumn();

			// make every-other-line white/blue
			// REFACTOR: EXTRACT CLASS/METHOD?
			table.setDefaultRenderer(Object.class,
					new DefaultTableCellRenderer() {
						@Override
						public Component getTableCellRendererComponent(
								JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column) {
							// get existing label
							JLabel c = (JLabel) super
									.getTableCellRendererComponent(table,
											value, isSelected, hasFocus, row,
											column);

							// if this isn't called, "last mod" column is pure white -- WHY?
							// PERF: i think this might slow down drawing a little, but i'm not positive.
							c.setOpaque(true);

							// every-other-line colors -- REFACTOR: OAOO me, but how?
							if (!isSelected)
								c.setBackground(row % 2 == 0 ? ODD_ROW_COLOR
										: Color.white);

							// return it
							return c;
						}
					});

			// use RangeRenderer for Ranges
			table.setDefaultRenderer(Range.class, new RangeRenderer());

			// "search for"
			JLabel searchLabel = new JLabel("Search for:");
			if (App.platform.isMac())
				searchLabel.setDisplayedMnemonic('S');
			searchField = new SearchField(this, table);
			searchLabel.setLabelFor(searchField);
			north.add(searchLabel);
			north.add(Box.createHorizontalStrut(10));
			north.add(searchField);
			north.add(Box.createHorizontalStrut(14));

			// table: return opens a file/folder
			table.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER)
						e.consume(); // don't go to next row of table
				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER)
						e.consume(); // don't go to next row of table
				}

				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						e.consume(); // don't go to next row of table
						openCurrentFile();
					}
				}
			});

			// table: double-click opens, too.
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && !e.isPopupTrigger()) {
						openCurrentFile();
					}
				}
			});

			// table: click-to-sort -- EXTRACT standard table behavior?
			// (TODO: also, double-click boundary to size column to fit?)
			table.getTableHeader().addMouseListener(new MouseAdapter() {
				private boolean wasPopup = false;

				@Override
				public void mousePressed(MouseEvent e) { // mac
					maybeShowPopup(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) { // win32
					maybeShowPopup(e);
				}

				private void maybeShowPopup(MouseEvent e) {
					if (e.isPopupTrigger()) {
						JPopupMenu p = new JPopupMenu();
						addViewMenus(p);
						p.show(e.getComponent(), e.getX(), e.getY());
						wasPopup = true;

						// TODO: update View menu items when something is selected here

						// BUG: right-mouse-drag brings up the menu properly,
						// but also still drags the table column (mac only)
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() != 1)
						// should double-click count as 2 clicks?  users might find that natural.
						return;

					// right-click shouldn't sort.
					if (wasPopup) {
						wasPopup = false;
						return;
					}

					// sort by that field
					sortBy(table.getColumnModel().getColumnIndexAtX(e.getX()));

					wasPopup = false;
				}
			});

			// TODO: make option "show/hide non-dendro files" -- don't show *.grf,.. files at all

			JScrollPane sp = new JScrollPane(table,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			JPanel main = Layout.borderLayout(north, null, sp, null, null);
			p.add(main);
		}

		makeMenus();

		// try putting keyboard-sorting on a bunch of components
		// -- BUG: still doesn't seem to work on win32, but why?
		addKeyListener(new KeyboardSorter());
		// DISABLED: folderPopup.addKeyListener(new KeyboardSorter());
		searchField.addKeyListener(new KeyboardSorter());

		setVisible(true);

		// UGLY: if it's after the show() call, it starts out the other way, and
		// then jumps to the correct widths; if it's before, it has no effect.
		// what gives?
		restoreColumnWidths();

		// if user re-orders columns, save new prefs.
		table.getColumnModel().addColumnModelListener(
				new TableColumnModelListener() {
					public void columnAdded(TableColumnModelEvent e) {
					}

					public void columnMarginChanged(ChangeEvent e) {
						// "column margin changed" really means "column resized", though
						// that's completely absent from any of the documentation.
						// we record this, too.

						saveColumnWidths();
					}

					public void columnMoved(TableColumnModelEvent e) {
						// a columnMoved event is reported whenever a column moves, even
						// if the order of the columns doesn't change.  no problem: i'll
						// just ignore all events where from=to.
						if (e.getFromIndex() == e.getToIndex())
							return;

						saveFields();
						saveColumnWidths(); // need to save these here, too
					}

					public void columnRemoved(TableColumnModelEvent e) {
					}

					public void columnSelectionChanged(ListSelectionEvent e) {
					}
				});

		searchField.requestFocus();
		
		doList();
	}

	private SortedHeaderRenderer shr;

	// (JComponent is the most specific common class of JPopupMenu and JMenu!)
	private void addViewMenus(JComponent p) {
		// -- file metadata
		p.add(new FieldCheckBoxMenuItem("name", I18n.getText("browser_name"),
				false));
		p.add(new FieldCheckBoxMenuItem("kind", I18n.getText("browser_kind")));
		p.add(new FieldCheckBoxMenuItem("size", I18n.getText("browser_size")));
		p.add(new FieldCheckBoxMenuItem("modified", I18n
				.getText("browser_modified")));

		// this one isn't even normally used, but it's there.
		p.add(new FieldCheckBoxMenuItem("filetype", "Filetype"));

		// this seems downright ridiculous.  (it's the same method name!)
		if (p instanceof JMenu)
			((JMenu) p).addSeparator();
		else if (p instanceof JPopupMenu)
			((JPopupMenu) p).addSeparator();
		else
			throw new IllegalArgumentException("need a JMenu or JPopupMenu");

		// -- range metadata
		p
				.add(new FieldCheckBoxMenuItem("range", I18n
						.getText("browser_range")));
		p.add(new FieldCheckBoxMenuItem("start", Builder.INDENT
				+ I18n.getText("browser_start")));
		p.add(new FieldCheckBoxMenuItem("end", Builder.INDENT
				+ I18n.getText("browser_end")));
		p.add(new FieldCheckBoxMenuItem("length", Builder.INDENT
				+ I18n.getText("browser_length")));

		// (...doubly so, now)
		if (p instanceof JMenu)
			((JMenu) p).addSeparator();
		else if (p instanceof JPopupMenu)
			((JPopupMenu) p).addSeparator();
		else
			throw new IllegalArgumentException("need a JMenu or JPopupMenu");

		// -- sample metadata
		Iterator i = MetadataTemplate.getFields();
		while (i.hasNext()) {
			MetadataField f = (MetadataField) i.next();
			p
					.add(new FieldCheckBoxMenuItem(f.getVariable(), f
							.getFieldDescription()));
		}
	}

	// return all of the selected files, as a List of filenames
	// DEPRECATE?  who uses this?
	/*private*/
	/*private*/
	
	public List<String> getSelectedFilenames() {
		int rows[] = table.getSelectedRows();
		int n = rows.length;
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < n; i++)
			list.add(((Row) visibleFiles.get(rows[i])).getPath());
		return list;
	}
	
	public ElementList getSelectedElements() {
		int rows[] = table.getSelectedRows();
		int n = rows.length;
		ElementList list = new ElementList();
		
		for (int i = 0; i < n; i++)
			list.add(ElementFactory.createElement((visibleFiles.get(rows[i])).getPath()));
		
		return list;
	}

	/**
	 Return the one selected row.  If more than one row is selected,
	 usually this is the last row the user clicked on, and has a white
	 outline or something like that around it - but you shouldn't
	 expect just one row when there's actually more than one.  If
	 nothing is selected, an exception is thrown.

	 @return the selected row
	 @exception NoSuchElementException if nothing is selected
	 */
	public Row getSelectedRow() throws NoSuchElementException {
		int selection = table.getSelectedRow();
		if (selection != -1)
			return (Row) visibleFiles.get(selection);
		else
			throw new NoSuchElementException();
	}

	/**
	 Return an Iterator that emits all of the selected Rows.
	 @return an Iterator that goes throw all of the selected Rows
	 */
	public Iterator getSelectedRows() {
		return new Iterator() {
			int rows[] = table.getSelectedRows();

			int i = 0;

			public boolean hasNext() {
				return (i < rows.length);
			}

			public Object next() throws NoSuchElementException {
				if (i < rows.length)
					return visibleFiles.get(i++);
				else
					throw new NoSuchElementException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	// makes the first column into "icon/text", instead of just "text".
	private void addIconsForFirstColumn() {
		table.getColumnModel().getColumn(0).setCellRenderer(
				new DefaultTableCellRenderer() {
					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						// get existing label
						JLabel c = (JLabel) super
								.getTableCellRendererComponent(table, value,
										isSelected, hasFocus, row, column);

						// every-other-line colors
						c.setOpaque(true);
						if (!isSelected)
							c.setBackground(row % 2 == 0 ? ODD_ROW_COLOR
									: Color.white);

						// REFACTOR: extend the renderer used for everything else.  the icon/text here is the only diff.

						Row r = (Row) visibleFiles.get(row); // use row's icon
						c.setIcon(r.getIcon());

						// done
						return c;
					}
				});
	}

	// watch for cmd-/digit/
	private class KeyboardSorter extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			// FIXME: isn't there a way to get the default accel on this platform?
			// (OAOO in corina.ui, at least)
			boolean cmdPressed = ((App.platform.isMac() && e.isMetaDown()) || (App.platform
					.isWindows() && e.isControlDown()));

			if (cmdPressed && Character.isDigit(e.getKeyChar())) {
				// 0 means 10, because it's the 10th key on the board; otherwise 1=0, 2=1, ...
				int column;
				if (e.getKeyChar() == '0')
					column = 9;
				else
					column = Character.getNumericValue(e.getKeyChar()) - 1;

				// make sure there's enough columns, and call sortBy()
				if (column < fields.size())
					sortBy(column);
			}
		}
	}

	/** Copy the current browser view to the clipboard, as text.  Columns
	 are separated by tabs, and rows by newlines. */
	public void copy() {
		// (should i only copy selected rows, if any are selected?)
		StringBuffer buf = new StringBuffer();

		// header line
		for (int col = 0; col < fields.size(); col++) {
			int realCol = table.convertColumnIndexToModel(col); // user's column order
			buf.append(browserModel.getColumnName(realCol));
			if (col < fields.size() - 1)
				buf.append("\t");
		}
		buf.append("\n");

		// data lines
		for (int row = 0; row < visibleFiles.size(); row++) {
			// REFACTOR: can i use a Row.toString() here?  (will it know the fields to use?)
			for (int col = 0; col < fields.size(); col++) {
				Row r = (Row) visibleFiles.get(row);
				int realCol = table.convertColumnIndexToModel(col); // user's column order
				Object cell = r.getField((String) fields.get(realCol));
				buf.append(cell == null ? "" : cell.toString());
				if (col < fields.size() - 1)
					buf.append("\t");
			}
			buf.append("\n");
		}

		TextClipboard.copy(buf.toString());
	}

	// REFACTOR: this method is huge!  (down to 250 lines!)
	private void makeMenus() {
		JMenuBar mb = new JMenuBar();

		JMenu file = new BrowserFileMenu(this);

		// EDIT -- TODO: extend EditMenu
		JMenu edit = Builder.makeMenu("edit");
		edit.add(Builder.makeMenuItem("undo", false)); // WRITEME: (tough one)
		edit.add(Builder.makeMenuItem("redo", false)); // WRITEME: (also tough)
		edit.addSeparator();
		edit.add(Builder.makeMenuItem("cut", false)); // always dimmed
		{
			JMenuItem copy = Builder.makeMenuItem("copy"); // copy text as list
			copy.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					copy();
				}
			});
			edit.add(copy);
		}
		edit.add(Builder.makeMenuItem("paste", false)); // always dimmed

		// ---
		edit.addSeparator();

		// select all
		JMenuItem selectAll = Builder.makeMenuItem("select_all");
		selectAll.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				table.selectAll();
			}
		});
		edit.add(selectAll);

		// select none
		JMenuItem selectNone = Builder.makeMenuItem("select_none");
		selectNone.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				table.getSelectionModel().clearSelection();
			}
		});
		edit.add(selectNone);

		// inverse
		JMenuItem inverse = Builder.makeMenuItem("select_inverse");
		inverse.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int n = table.getRowCount(); // REFACTOR: extract me?: selectInverse(); (also, all, none?)
				for (int i = 0; i < n; i++) {
					if (table.isRowSelected(i))
						table.removeRowSelectionInterval(i, i);
					else
						table.addRowSelectionInterval(i, i);
				}
			}
		});
		edit.add(inverse);

		// select same ...
		JMenu same = new JMenu("Select Same");
		// all EXCEPT name, kind, size, modified
		// FIXME: "filetype", too
		same.add(new JMenuItem(I18n.getText("browser_range")));
		// FIXME: these need to be added to the loop below...
		same.add(new JMenuItem(Builder.INDENT + I18n.getText("browser_start")));
		same.add(new JMenuItem(Builder.INDENT + I18n.getText("browser_end")));
		same
				.add(new JMenuItem(Builder.INDENT
						+ I18n.getText("browser_length")));
		same.addSeparator();
		Iterator i = MetadataTemplate.getFields();
		while (i.hasNext()) {
			MetadataField f = (MetadataField) i.next();
			final String glue = f.getVariable();

			JMenuItem sameAs = new JMenuItem(f.getFieldDescription());
			sameAs.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// select-same:
					// -- figure out what this {F = pith, format, ...} is, V
					// -- unselect all (extract method!)
					// -- foreach row in visibleFiles, if field[F].equals(V),
					// add row i

					// what's selected?
					Row r = (Row) visibleFiles.get(table.getSelectedRow());

					// what's its field?
					String value = (String) r.getField(glue);

					// (DESIGN: deselect all rows first?)

					// now, just loop over all visibleFiles and those rows to the selection
					for (int ii = 0; ii < visibleFiles.size(); ii++) {
						Object test = ((Row) visibleFiles.get(ii))
								.getField(glue);
						if (test == null)
							continue;
						if (test instanceof String
								&& ((String) test).equalsIgnoreCase(value))
							table.addRowSelectionInterval(ii, ii);
						else if (test.equals(value))
							table.addRowSelectionInterval(ii, ii);
					}
				}
			});
			same.add(sameAs);
		}
		edit.add(same);

		// VIEW
		JMenu view = Builder.makeMenu("view");
		addViewMenus(view);

		// SEARCH
		JMenu search = new JMenu("Search");

		// show all
		JMenuItem showAll = new JMenuItem("Show All");
		showAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		// strangely, the "ESC" accelerator doesn't seem to work -- but searchfield already watches
		// for it, so it does serve to tell the user "esc does this", which is the Right Thing to do.
		showAll.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				searchField.reset();
			}
		});
		search.add(showAll);
		search.addSeparator();

		// add sample searches here
		search.add(new SearchMenuItem("Oak", "quercus", "species",
				browserModel));
		search.add(new SearchMenuItem("Tucson Format", "tucson", "filetype",
				browserModel));
		search.add(new SearchMenuItem("Absolutely Dated", "absolut", "dating",
				browserModel));
		search.add(new SearchMenuItem("Indexed with a Cubic Spline",
				"cubic spline", "index_type", browserModel));
		search.add(new SearchMenuItem("Modified Today", "today", "modified",
				browserModel));
		search.add(new SearchMenuItem("Modified Yesterday Afternoon",
				"yesterday pm", "modified", browserModel));
		// HMM: the search strings need i18n, too ("heute")

		// or type your own
		JMenuItem yourOwn = new JMenuItem("Or Type Your Own!");
		yourOwn.setEnabled(false);
		search.add(yourOwn);

		// misc stuff (formerly "scripts", formerly "misc")
		// TODO: integrate with browser better!
		JMenu misc = Builder.makeMenu("manip");
		JMenuItem index = new JMenuItem("Index Selected Files (broken)");
		index.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// TODO: enabled only if multiple files are selected?
				// TODO: indexManyFiles() method should live in corina.index

				// get selection
				ElementList selection = getSelectedElements();
				if (selection.size() == 0)
					return; // FIXME: don't allow this!

				// convert to Elements
				/* BROKEN!!
				for (int i = 0; i < selection.size(); i++)
					selection.set(i, new ObsFileElement((String) selection.get(i)));
					*/

				// pass selected files to index-many method
				Scripts.indexManyFiles(selection);

				// TODO: let indexMany throw UCE, and don't doList() if that happens

				// kick browser to update itself
				doList();

				// TODO: select newly-created files now?

				// TODO: this should be undoable
			}
		});
		JMenuItem split = new JMenuItem("Split into Early/Late (broken)");
		split.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// TODO: only enabled when exactly 1 file selected (closure?)

				// get selection
				int selected = table.getSelectedRow();
				if (selected == -1)
					return; // FIXME: don't allow this!
				String filename = ((Row) visibleFiles.get(selected)).getPath();
				// REFACTOR: why don't i have a getSelectedFile() method?

				// pass selected file to splitting method
				Scripts.splitIntoEarlyLate(filename);

				// kick browser to update itself
				doList();

				// TODO: select newly-created files now?

				// TODO: (offer to kill original file?)

				// TODO: this should be undoable
			}
		});
		JMenuItem join = new JMenuItem("Join Early/Late (broken)");
		join.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// TODO: only enabled when there are exactly 2 files selected (closure?)

				// DESIGN: takes 1 file, users (including me!) might expect 2

				// get selection
				int selected = table.getSelectedRow();
				if (selected == -1)
					return; // FIXME: don't allow this!
				String filename = ((Row) visibleFiles.get(selected)).getPath();
				// REFACTOR: why don't i have a getSelectedFile() method?

				//Scripts.joinEarlyLate(filename);

				// kick browser to update itself
				doList();

				// TODO: select newly-created file now?  (what if it's not visible?)
			}
		});
		misc.add(index);
		misc.addSeparator();
		misc.add(split);
		misc.add(join);
		// TODO: add pack/unpack tucson files here, too?  or file submenu?

		// HELP
		JMenu help = new HelpMenu();

		// put together the menubar
		mb.add(file);
		mb.add(edit);
		mb.add(view);
		mb.add(search);
		mb.add(misc);
		mb.add(help);
		setJMenuBar(mb); // use xmenubar for a real file menu, etc.?
	}

	/*
	 BETTER:
	 - table -> get selection -> list of filenames -> load -> list of elements
	 - map [has-elements? -> replace this element with its elements] over list of elements
	 - list of elements -> make sum -> sum -> make editor

	 - in other words, about 4 lines of lisp.  how can i make this better?
	 -- graph uses table->get selection, as well -- that's a REFACTORing waiting to happen
	 */

	// does just what it says!
	// FIXME: use getSelectedRows() iterator, not this -- it's too low-level.
	public void makeSumFromSelection() {
		// make list of elements
		int selected[] = table.getSelectedRows();
		int n = selected.length;
		ElementList elements = new ElementList();

		try {
			// loop, adding elements to list
			for (int i = 0; i < n; i++) {
				Element e = new CachedElement(visibleFiles.get(selected[i]).getElement());
				Sample test = e.load();

				// if this sample has elements, use its elements, else just add this element.
				if (test.getElements() != null)
					elements.addAll(test.getElements());
				else
					elements.add(e);
			}

			// make sum (don't sort here: it's already sorted somehow)
			new Editor(Sum.sum(elements));

			// BUG: provide intelligent error messages for common summing errors here
			// -- raw+indexed
			// -- no overlap

			// RFE: pick an intelligent name
			// -- if the files are "xyz1.ext", "xyz2.ext", and "xyz3.ext", call it "xyz 1-3".  (duh.)
			// -- first, ignore extension.  result extension will be ".sum" (for now)
			// -- next, sort them (natural ordering, ignore case)
			// -- look for common prefix; if complete (i.e., "zkb111"+"zkb222", but not "zkb111"+"zyb111")
			// -- for each block of common prefixes, then look at the numbers
			// -- for each block of consecutive numbers, replace with "x-y"
			// -- smash all remaining number blocks together with ","
			// -- smash all remaining prefix blocks together with ","
			// -- result: something like "ZKB 1-3,4, ZYB 5,7,9-13" [.SUM]
		} catch (IOException ioe) {
			Alert.error("Error summing", ioe.getMessage());
		} catch (Exception ex) {
			new Bug(ex); // FIXME: can this ever happen?
		}
	}

	private class SearchMenuItem extends JMenuItem {
		private AbstractTableModel tableModel;

		SearchMenuItem(String name, String terms, String field,
				AbstractTableModel model) {
			super(name);
			tableModel = model;
			final String _terms = terms;
			final String _field = field;
			addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					ensureVisible(_field);
					searchField.setText(_terms);
				}
			});
		}

		private void ensureVisible(String field) {
			// already visible?  done.
			if (fields.contains(field))
				return;
			// if (!fields.contains(field))
			//  fields.add(field);  -- then update view?

			// no?  just add it to the end.
			fields.add(field);

			// BUG: these need to update the MENUITEMS, too.
			// which means i'll need to keep a (field, menuitem) hash for the view menuitems

			// do a bunch of other stuff -- STOLEN FROM FIELDCHECKBOXMENUITEM!
			// TODO: move these to a fieldsChanged() method.

			// either way, the table changed
			tableModel.fireTableStructureChanged();

			// that kills the icons in col0, so reset those
			addIconsForFirstColumn();

			// re-search, since the last search may very well no longer be valid
			doSearch();

			// finally, save these fields in the prefs
			saveFields();
		}
	}

	private class FieldCheckBoxMenuItem extends JCheckBoxMenuItem {
		// e.g., ("name", "Name")
		public FieldCheckBoxMenuItem(String variable, String label) {
			this(variable, label, true);
		}

		public FieldCheckBoxMenuItem(String variable, String label, boolean enabled) {
			super(label);
			
			this.setEnabled(enabled);
			
			final String field = variable;
			final FieldCheckBoxMenuItem glue = this;

			setSelected(fields.indexOf(field) != -1);

			addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					if (glue.isSelected()) {
						// selected => add to end of list
						fields.add(field);
					} else {
						// unselected => remove from list
						fields.remove(fields.indexOf(field));

						// if field.equals(sortField), pick something else to sort by?  (why?)
						// (because i'll be showing it in a header)
					}

					// either way, the table changed
					browserModel.fireTableStructureChanged();

					// that kills the icons in col0, so reset those
					addIconsForFirstColumn();

					// re-search, since the last search may very well no longer be valid
					doSearch();

					// finally, save these fields in the prefs
					saveFields();
				}
			});
		}
	}

	// (actually a method of the table, but java won't let me obey LoD)
	private void selectFirstRow() {
		if (table.getRowCount() >= 1)
			table.setRowSelectionInterval(0, 0);
	}

	private void openCurrentFile() {
		int i = table.getSelectedRow(); // BUG?: only gets 1 selected row
		if (i == -1)
			return; // no file selected
		Row r = (Row) visibleFiles.get(i); // XXX
		// if fn is a folder, enter that folder
		if (r.isDirectory()) {
			// set folder and re-list table
			//            folder = r.getPath();
			setFolder(r.getPath());
			doList();

			// add an element to the folderPopup
			// DISABLED: folderPopup.descendInto(new File(folder));

			// focus back on the search field?
			searchField.requestFocus();

			// stop.  (not pretty, i know.)
			return;
		}

		// nope, it's a file, so try to open that.  BETTER: use the canopener so you can load grids, too.
		try {
			Sample s = r.getElement().load();
			new Editor(s);
			// FIXME: this editor is just a JFrame, so add a close listener to update this row when it closes,
			// iff the modified date changes.
			// (or even: as long as it's open, add a thread to watch it by statting the file every (10?) sec.)
		} catch (IOException ioe) {
			System.out.println("ugh, i/o error: " + ioe);
		}
	}

	private String folder = App.prefs.getPref("corina.dir.data"); // redundant?

	private static final Map fileMetadata = new Hashtable();
	static {
		// you know, this seems downright silly...
		fileMetadata.put("name", "browser_name");
		fileMetadata.put("size", "browser_size");
		fileMetadata.put("kind", "browser_kind");
		fileMetadata.put("modified", "browser_modified");

		fileMetadata.put("range", "browser_range");
		fileMetadata.put("start", "browser_start");
		fileMetadata.put("end", "browser_end");
		fileMetadata.put("length", "browser_length");
	}

	/*
	 ideas:
	 -- move select all/none/inverse to edit menu
	 -- get rid of rest of "select" menu ("same"), since nobody uses it
	 -- add "site" menu:
	 ---- create site here
	 ---- delete site here
	 ---- edit site here (are these 3 the same?)
	 ---- ---
	 ---- map this site
	 ---- ---
	 ---- site list
	 */

	private class BrowserTableModel extends AbstractTableModel {
		public int getRowCount() {
			return visibleFiles.size();
		}

		public int getColumnCount() {
			return fields.size();
		}

		// getColumnName() gets called only once per column, typically, so it
		// doesn't matter if it's not the fastest method on the block.
		@Override
		public String getColumnName(int column) {
			String field = (String) fields.get(column);
			return nameOfField(field);
		}

		@Override
		public Class getColumnClass(int column) {
			// DISABLED: when the coloring problem is solved, re-enable this:
			// if (fields.get(column).equals("range"))
			// return Range.class;

			// ALSO, use decimalrenderer for ints: start(%4), end(%4), length(%3), sapwood(%2)
			// -- start/end = year; i can use a decimalrenderer("0000") for years
			// -- sapwood/length = int; i can't use different decimalrenderers for each
			// based only on their class, then.  solution: use a decimalrenderer("000")
			// for both; it'll look fine, i think.

			// default for the rest
			return Object.class;
		}

		public Object getValueAt(int row, int column) {
			// this is an awful bug,
			// but it breaks corina pretty badly.
			// TODO: Actually fix me!
			if(row >= getRowCount()) {
				return "BROKEN!!";
			}
			
			Row r = (Row) visibleFiles.get(row);
			return r.getField((String) fields.get(column));
		}
	}

	// "name" => "Name", for example.
	private String nameOfField(String field) {
		// file metadata
		if (fileMetadata.containsKey(field))
			return I18n.getText((String) fileMetadata.get(field));

		// standard metadata label
		Iterator i = MetadataTemplate.getFields();
		while (i.hasNext()) {
			MetadataField f = (MetadataField) i.next();
			if (f.getVariable().equals(field))
				return f.getFieldDescription();
		}

		// "filetype", which is (sort of) a hack.  (well, it sure is now!)
		if (field.equals("filetype"))
			return "Filetype";

		// unknown -- should never happen
		return "???";
	}

	// fields to view
	private List fields = new ArrayList();

	public List getVisibleFields() {
		return fields;
	}

	// files to view
	List<Row> files = new ArrayList<Row>(); // of Element

	List<Row> visibleFiles = new ArrayList<Row>(); // of Element

	Summary summary = null;
	FolderTree tree = null;

	// strategy: instead of updating the table in-place (which is slow,
	// and annoying), we'll load it as fast as possible, then just
	// show the whole thing at once.
	
	public void doList() {
		// reset search field (good idea?)
		searchField.reset();

		// clear old list -- (why don't i just make a new one?)
		while (files.size() > 0)
			files.remove(0);
		
		final Browser _parent = this;
		table.setVisible(false);
		tree.setEnabled(false);
		
		Thread th = new Thread(new Runnable() {
			public void run() {
				// make a progress monitor, in case this takes a while (it
				// might)
				GreedyProgressMonitor monitor = new GreedyProgressMonitor(_parent,
						"Summarizing folder...", "Listing files", 0, files
								.size() + 10);
				monitor.setMillisToDecideToPopup(10); // default: 500 (1/2
														// sec)
				monitor.setMillisToPopup(20); // default: 2000 (2 sec) (was:
												// 1000)

				long t1 = System.currentTimeMillis();		

				monitor.setNote("Checking summary...");

				// ask Summary for folder contents
				try {
					summary = new Summary(folder, monitor, _parent);

					// FIXME: summary c'tor needs to have a ref to this monitor
				} catch (Throwable thr) {
					new Bug(thr);
				}
					/*
				} catch (IOException ioe) {
					System.out.println("BUG: ioe!");
					ioe.printStackTrace();
				}
				*/
				
				int idx = 0;
				Iterator filenames = summary.getFilenames();
				while (filenames.hasNext()) {
					String fn = (String) filenames.next();
					SampleHandle sh = summary.getSampleHandle(fn);
					if (sh != null)
					{
						monitor.setProgressGreedy(++idx);
						files.add(new Row(sh, _parent));
					}
				}

				monitor.setProgressGreedy(files.size() + 10);

				/*
				 * for (int i=0; i<files.size(); i++) { Row r = (Row)
				 * files.get(i);
				 * 
				 * monitor.setNote("Loading " + r.getName());
				 *  // load row if (!r.isDirectory()) r.load();
				 * 
				 * monitor.setProgress(i + 1); }
				 * monitor.setProgress(files.size() + 1);
				 */

				doSearch();
				// saveSelection(); { // PERF: doSort() has a save/restore
				// block, too!
				doSort();
				
				long t2 = System.currentTimeMillis();
				System.out.println("doList() took " + (t2 - t1)
						+ " ms to list all metadata for this folder");		
				
				table.setVisible(true);
				tree.setEnabled(true);
				browserModel.fireTableDataChanged();
			}
		});
		th.start();
		
		// } restoreSelection();
	}

	// the name of the field to sort by
	private String sortField = App.prefs.getPref("corina.browser.sort", "name");

	private boolean reverse = Boolean.valueOf(
			App.prefs.getPref("corina.browser.reverse")).booleanValue(); // BUG: need to set SHR

	// assumes it's given a valid (view) column
	public void sortBy(int viewColumn) {
		int modelColumn = table.convertColumnIndexToModel(viewColumn);
		String newSort = (String) fields.get(modelColumn);
		if (sortField.equals(newSort)) {
			reverse = !reverse;
			shr.setReversed(reverse);
			App.prefs.setPref("corina.browser.reverse", String.valueOf(reverse));
			// wait ... in this case, i don't need to doSort() again; reverse() is good enough.
			// plus, it acts as the user expects.
			saveSelection();
			{
				Collections.reverse(visibleFiles);
				browserModel.fireTableDataChanged();
			}
			restoreSelection();
		} else {
			sortField = newSort;
			shr.setSortColumn(nameOfField(sortField));
			App.prefs.setPref("corina.browser.sort", sortField);
			reverse = false;
			shr.setReversed(reverse);
			App.prefs.setPref("corina.browser.reverse", String.valueOf(reverse));
			doSort();
		}

		// force the header to redraw itself 
		table.getTableHeader().repaint();
	}

	private Set selectedSet;

	private void saveSelection() {
		selectedSet = new HashSet();
		int selected[] = table.getSelectedRows();
		int n = selected.length;
		for (int i = 0; i < n; i++)
			selectedSet.add(visibleFiles.get(selected[i]));
	}

	private void restoreSelection() {
		if (table.getRowCount() > 1)
			table.removeRowSelectionInterval(0, table.getRowCount() - 1);
		for (int i = 0; i < visibleFiles.size(); i++)
			if (selectedSet.contains(visibleFiles.get(i)))
				table.addRowSelectionInterval(i, i);
	}

	// run the sort based on |sortField| and |reverse|, updating the table when done
	private void doSort() {
		// preamble: save stored rows
		saveSelection();

		Collections.sort(files, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Object f1 = ((Row) o1).getField(sortField);
				Object f2 = ((Row) o2).getField(sortField);

				// swap f1/f2 based on |reverse|.
				if (reverse) {
					Object tmp = f1;
					f1 = f2;
					f2 = tmp;
				}

				// if one is null, dump it at the end(?)
				if (f1 == null && f2 == null)
					return 0;
				if (f1 == null)
					return +1;
				if (f2 == null)
					return -1;

				// if they're strings, ignore case when sorting, because users will.
				if (f1 instanceof String && f2 instanceof String) {
					// use natural sort (thanks martin)
					return NaturalSort.compareIgnoreCase((String) f1,
							(String) f2);
				}

				// sort numbers backwards, high-to-low.  (this is why FileLength extends Number)
				if (f1 instanceof Number && f2 instanceof Number) {
					return ((Comparable) f2).compareTo(f1);
				}

				// dates should go backwards (newest-to-oldest) -- this is why RelativeDate should extend Date
				if (f1 instanceof RelativeDate && f2 instanceof RelativeDate) {
					return ((Comparable) f2).compareTo(f1);
				}

				// ranges should go backwards, because that's fallback-order
				if (f1 instanceof Range && f2 instanceof Range) {
					return ((Comparable) f2).compareTo(f1);
				}

				// whatever's left, just compare them
				return ((Comparable) f1).compareTo(f2);
			}
		});
		doSearch();
		// Q: would it be better to run the sort on just the visible elements?
		// maybe: sort(visible), <update>, sort(all)
		// it might look more responsive that way.
		// the downside: if the user types something, you'll need all sorted, anyway.
		// what you're saving here is having to run doSearch() when sorting, which could be significant.
		browserModel.fireTableDataChanged();

		// postamble: restore selected rows
		restoreSelection();
	}

	// searching: update |visibleFiles| from |files|, using |searchField|
	public void doSearch() {
		// clear old list -- Q: do i really need to maintain the old reference?
		while (visibleFiles.size() > 0)
			visibleFiles.remove(0);

		// create search target: fake case insensitivity by doing everything in lower-case
		String words[] = searchField.getTextAsWords();

		// loop through files, adding to visibleFiles if it should be visible
		// (setq visibleFiles (loop for r in files when (row-matches r words) collect r))
		for (int i = 0; i < files.size(); i++) {
			Row r = (Row) files.get(i);
			if (r.matches(words))
				visibleFiles.add(r);
		}

		// update the table and summary line, and select the first one
		browserModel.fireTableDataChanged();
		updateSummary();
		//        selectFirstRow(); -- XXX do we want this?
	}

	// searching: update |visibleFiles| from |files|, using |searchField|.
	// but only search things which are already visible.
	public void doSearchRestrict() {
		// create search target: fake case insensitivity by doing everything in lower-case
		String words[] = searchField.getTextAsWords();

		// loop, removing visibleFiles if it shouldn't be visible.
		// (count backwards because i'm removing elements.)
		for (int i = visibleFiles.size() - 1; i >= 0; i--) {
			Row r = (Row) visibleFiles.get(i);
			if (!r.matches(words))
				visibleFiles.remove(i);
		}

		// update the table and summary line, and select the first one
		browserModel.fireTableDataChanged();
		updateSummary();
		//        selectFirstRow(); -- XXX do we want this?
	}

	// returns true iff |test| matches (case-insensitiviley) all of |terms[]|;
	// assumes all of |terms[]| are lower-case.
	static boolean matchesAny(String test, String terms[]) {
		// no terms to match?  then it can't not-match.
		if (terms.length == 0)
			return true;

		test = test.toLowerCase();
		boolean match = true;
		for (int i = 0; i < terms.length; i++) {
			if (test.indexOf(terms[i]) == -1) {
				match = false;
				break;
			}
		}
		return match;
	}

	// odd-row color. -- REFACTOR: move me somewhere more public?
	// (this is about what iTunes uses)
	public final static Color ODD_ROW_COLOR = new Color(236, 243, 254);

	// save fields as a pref
	private synchronized void saveFields() {
		// generate the string
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < fields.size(); i++) {
			int j = table.convertColumnIndexToModel(i); // view -> model mapping
			buf.append((String) fields.get(j));
			if (i < fields.size() - 1)
				buf.append(" ");
		}

		// save to prefs
		App.prefs.setPref("corina.browser.fields", buf.toString());
	}

	// load fields from prefs (or use a reasonable default).
	private void loadFields() {
		String pref = App.prefs.getPref("corina.browser.fields", DEFAULT_FIELDS);
		
		// always add name, if for some reason it's not there (e.g., fields="")
		if (pref.indexOf("name") == -1)
			pref = "name," + pref;

		StringTokenizer tok = new StringTokenizer(pref, ", ");
		int n = tok.countTokens();
		fields = new ArrayList(n);
		for (int i = 0; i < n; i++)
			fields.add(tok.nextToken());
	}

	private static final String DEFAULT_FIELDS = "name size modified range format";

	public void print(PrinterJob printJob, PageFormat pageFormat) {
		// page setup wasn't run: force it
		if (printJob == null)
			printJob = PrinterJob.getPrinterJob();
		if (pageFormat == null)
			pageFormat = printJob.pageDialog(pageFormat);
		if (pageFormat == null)
			return; // abort

		// build document
		// REFACTOR: this is very similar to "copy" code, above
		Printable p = new BrowserPrinter();

		// prepare to print
		printJob.setPrintable(p);

		// job title
		printJob.setJobName("Corina: folder " + folder);

		// ask user options
		if (!printJob.printDialog())
			return;

		// print (in background thread) --
		// EXTRACT! -- Printer.printInBackground(PrinterJob).
		final PrinterJob glue = printJob;
		(new Thread() {
			@Override
			public void run() {
				try {
					glue.print();
				} catch (PrinterAbortException pae) {
					// do nothing, the user already knows
				} catch (PrinterException pe) {
					// make this friendlier/more useful!
					// "an error in the print system caused printing to be aborted.  this could indicate
					// a hardware or system problem, or a bug in Corina."  ( details ) (( ok ))
					// (details->stacktrace) -- create static String Bug.getStackTrace(ex)
					Alert.error("Error printing", "Printer error: "
							+ pe.getMessage());
					return;
				}
			}
		}).start();
	}

	// --------------------------------------------------
	// USES:
	// + folder (text)
	// - searchField (getTextAsWords)
	// - table (width, header widths, col ordering)
	// - fields (size, keys)
	// - browserModel (col names)
	// - label2 (for text, but should be changed)
	// - visibleFiles (size, rows)
	// (ouch!)
	// i'll need to make each of thees public (or come up with a better interface), and just pass it a Browser ref.
	private class BrowserPrinter extends Printer {
		BrowserPrinter() {
			// folder sometimes has 2 file.sep's -- normalize it
			// BUG: does this not cause other problems?  why not?
			folder = new File(folder).getPath();

			lines.add(new TextLine("Folder " + folder + ":", Line.TITLE_SIZE));
			lines.add(new EmptyLine());

			// write search terms as:
			// -- ... containing the text "a"
			// -- ... containing the text "a" and "b"
			// -- ... containing the text "a", "b", "c", "d", and "e"
			String search[] = searchField.getTextAsWords();
			// EXTRACT METHOD!
			if (search.length > 0) {
				String restriction = "Restricted to samples containing the text ";
				if (search.length == 1) {
					restriction += "\"" + search[0] + "\"";
				} else if (search.length == 2) {
					restriction += "\"" + search[0] + "\" and \"" + search[1]
							+ "\"";
				} else if (search.length >= 3) {
					for (int i = 0; i < search.length - 1; i++)
						restriction += "\"" + search[i] + "\", ";
					restriction += "and \"" + search[search.length - 1] + "\"";
				}
				lines.add(new TextLine(restriction));
				lines.add(new EmptyLine());
			}

			// generate |spec| for table, based on columns
			String spec = "";
			// use the column widths the user set
			// -- width of a column is table.getColumnModel().getColumn(col).getWidth()
			// -- width of the table is table.getWidth()
			int tableWidth = table.getWidth();
			for (int col = 0; col < fields.size(); col++) {
				int colWidth = table.getColumnModel().getColumn(col).getWidth();
				float pct = 100f * colWidth / tableWidth;
				spec += ">" + pct + "%";
			}
			TabbedLineFactory f = new TabbedLineFactory(spec);

			// header row here
			String header = "";
			for (int col = 0; col < fields.size(); col++) {
				int realCol = table.convertColumnIndexToModel(col); // user's column order
				header += browserModel.getColumnName(realCol);
				if (col < fields.size() - 1)
					header += "\t";
			}
			lines.add(f.makeLine(header));

			// add each row to |lines| using |f|, based on rows
			for (int row = 0; row < visibleFiles.size(); row++) {
				String line = "";
				for (int col = 0; col < fields.size(); col++) {
					Row r = (Row) visibleFiles.get(row);
					int realCol = table.convertColumnIndexToModel(col); // user's column order
					Object cell = r.getField((String) fields.get(realCol));
					line = line + (cell == null ? "" : cell.toString());
					if (col < fields.size() - 1)
						line = line + "\t";
				}
				lines.add(f.makeLine(line));
			}

			// number of files/folders
			lines.add(new EmptyLine());
			String count = label2.getText(); // just steal the text from the jlabel
			// FIXME: printouts don't have a selection, so part of the label is nonsense for printouts
			lines.add(new TextLine(count));

			// by-lines
			lines.add(new EmptyLine());
			lines.add(new ThinLine(0.0f, 0.3f));
			lines.add(new ByLine());
		}
	}

	// --------------------------------------------------

	// NOTE: everything below here uses only the JTable, and the pref name; extract class?
	// save column widths
	private void saveColumnWidths() {
		StringBuffer buf = new StringBuffer();

		// REFACTOR: this is eerily like print(), above
		int totalWidth = table.getWidth();
		DecimalFormat fmt = new DecimalFormat("0.00%");
		for (int col = 0; col < table.getModel().getColumnCount(); col++) {
			// save as percent-of-table-width, so if the browser gets shrunk
			// because the resolution is different than last time, we don't get
			// in trouble with a bunch of tiny columns
			int colWidth = table.getColumnModel().getColumn(col).getWidth();
			float pct = colWidth / (float) totalWidth;
			buf.append(fmt.format(pct) + " ");
		}

		App.prefs.setPref("corina.browser.columnwidths", buf.toString());
	}

	// restore column widths
	private void restoreColumnWidths() {
		String spec = App.prefs.getPref("corina.browser.columnwidths");
		if (spec == null) // Q: is this the proper way to read "none exist"?
			return; // none there?  stick with defaults.

		int width = table.getWidth();
		DecimalFormat fmt = new DecimalFormat("0.00%"); // extract const?

		StringTokenizer tok = new StringTokenizer(spec, ", ");
		int n = tok.countTokens();
		
		// if we've removed columns, just ignore the extra defs...
		if(n > table.getModel().getColumnCount())
			n = table.getModel().getColumnCount();
		
		for (int i = 0; i < n; i++) {
			try {
				float pct = fmt.parse(tok.nextToken()).floatValue();
				int x = (int) (pct * width);
				table.getColumnModel().getColumn(i).setPreferredWidth(x);
			} catch (ParseException e) {
				// ignore?
			}
		}
	}
}
