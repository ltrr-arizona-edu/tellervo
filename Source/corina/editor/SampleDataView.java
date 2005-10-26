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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import corina.Range;
import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.Year;
import corina.core.App;
import corina.gui.Bug;
import corina.prefs.Prefs;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import corina.ui.Alert;
import corina.ui.Builder;
import corina.util.PopupListener;

// TODO: add slasher -- but it needs to either (1) override table
// sizing, or (2) override scrollpane painting (probably the latter)

/**
   A view of the raw data in a Sample.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/

    // left to add:
    // - setSelectedYear(Year) -- (why?)
    // - (the popup)

public class SampleDataView extends JPanel implements SampleListener,
		PrefsListener {

	private Sample mySample;

	public JTable myTable;

	private TableModel myModel;

	// pass this along to the table
	public void requestFocus() {
		myTable.requestFocus();
	}

	// (for Editor)
	public void stopEditing() {
		// strategy: if editing, fire an VK_ENTER keytype event at the table
		// (that also solves the "user typed the number and shouldn't lose that data" problem)
		if (myTable.isEditing())
			myTable.dispatchEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED,
					System.currentTimeMillis(), 0, KeyEvent.VK_ENTER));
	}

	public SampleDataView(Sample s) {
		// copy data reference, add self as observer
		mySample = s;
		mySample.addSampleListener(this);

		// create table
		myModel = new DecadalModel(mySample);
		/*
		 final Color DARK = new Color(0.7333f, 0.7765f, 0.8431f); // EXTRACT CONSTs!
		 final Color LIGHT = new Color(0.8196f, 0.8510f, 0.9216f);
		 final int THIN = 2;
		 final int THICK = 5;
		 */
		myTable = new JTable(myModel); /* {
		 public void paint(Graphics g) {
		 setOpaque(true);

		 int w = getWidth(), h = getHeight();

		 // fill light
		 g.setColor(LIGHT);
		 g.fillRect(0, 0, w, h);

		 // dark stripes
		 g.setColor(DARK);
		 ((Graphics2D) g).setStroke(new BasicStroke(THIN-1f));
		 for (int x=0; x<w+h; x+=THIN+THICK)
		 g.drawLine(x, 0, x-h, h);

		 super.paint(g);
		 }
		 }; */
		myTable.setGridColor(new Color(0, 0, 0, 0)); // completeley transparent

		// mouse listener for table
		myTable.addMouseListener(new PopupListener() {
			public void showPopup(MouseEvent e) {
				int row = myTable.rowAtPoint(e.getPoint());
				int col = myTable.columnAtPoint(e.getPoint());

				// clicked on a row header?  don't do anything.
				if (col == 0)
					return;

				// select the cell at e.getPoint()
				myTable.setRowSelectionInterval(row, row);
				myTable.setColumnSelectionInterval(col, col);
				// (does this work?  it does, but
				// the table doesn't get hilited
				// immediately..)

				// TODO: if it's not a valid data cell, don't show popup
				// TODO: if you can't ins/del a year here, dim those menuitems [done?]

				// show a popup here.
				JPopupMenu popup = new JPopupMenu();
				// PERF: build this popup lazily here, and hold on to it.

				// TODO: use buttongroup (what for? -- oh, the marks)

				/* DISABLED
				 JMenu marks = new JMenu("Mark with");
				 for (int i=0; i<Mark.defaults.length; i++)
				 marks.add(new JRadioButtonMenuItem(Mark.defaults[i].icon, false));
				 marks.addSeparator();
				 marks.add(new JRadioButtonMenuItem("None", true));
				 */

				JMenuItem insert = Builder.makeMenuItem("insert_year");
				insert.addActionListener(new AbstractAction() {
					public void actionPerformed(ActionEvent ae) {
						insertYear();
					}
				});

				JMenuItem insertMR = Builder.makeMenuItem("insert_mr");
				insertMR.addActionListener(new AbstractAction() {
					public void actionPerformed(ActionEvent ae) {
						insertMR();
					}
				});

				JMenuItem delete = Builder.makeMenuItem("delete_year");
				delete.addActionListener(new AbstractAction() {
					public void actionPerformed(ActionEvent ae) {
						deleteYear();
					}
				});

				popup.add(insert);
				popup.add(insertMR);
				popup.add(delete);
				// DISABLED until they're implemented.
				// popup.addSeparator();
				// popup.add(marks);
				// popup.addSeparator();
				// popup.add(new JMenuItem("Edit note..."));
				// TODO: hook up edit_note, with i18n

				// (dim insert/insertMR/delete, if it's not an editable sample.)
				if (!mySample.isEditable()) {
					insert.setEnabled(false);
					insertMR.setEnabled(false);
					delete.setEnabled(false);
				}

				popup.show(myTable, e.getX(), e.getY());
			}
		});

		// key listener for table
		myTable.addKeyListener(new DecadalKeyListener(myTable, mySample));

		// select the first year
		myTable.setRowSelectionAllowed(false);
		myTable.setRowSelectionInterval(0, 0);
		myTable.setColumnSelectionInterval(
				mySample.range.getStart().column() + 1, mySample.range
						.getStart().column() + 1);

		// don't let the columns be rearranged or resized
		myTable.getTableHeader().setReorderingAllowed(false);
		myTable.getTableHeader().setResizingAllowed(false);

		// make the last column a jprogressbar, % of max
		int max = 0;
		if (mySample.count != null)
			max = ((Integer) Collections.max(mySample.count)).intValue();
		// DISABLED: use column-header renderer for first column (pseudo-row-headers)
		// -- it doesn't look that great, since there are still gridlines between
		// rows; what i should really do is make a real table-row-header, which isn't too hard.
		// myTable.getColumnModel().getColumn(0).setCellRenderer(new
		//         javax.swing.table.JTableHeader().getDefaultRenderer());
		myTable.getColumnModel().getColumn(11).setCellRenderer(
				new CountRenderer(max));

		// make nulls elsewhere shaded, to indicate "can't use"
		// DISABLED, because it doesn't hit the area below the table yet (how?).
		// (but it looks really cool.)
		//SlashedIfNullRenderer slasher = new SlashedIfNullRenderer(mySample, myModel);
		//for (int i=1; i<=10; i++)
		//myTable.getColumnModel().getColumn(i).setCellRenderer(slasher);
		//myTable.setIntercellSpacing(new Dimension(0, 0));

		// set font, gridlines, colors ==> handled by refreshFromPreferences()

		// add to panel
		setLayout(new BorderLayout(0, 0)); // huh?
		JScrollPane sp = new JScrollPane(myTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		add(sp, BorderLayout.CENTER);
		add(new Modeline(myTable, mySample), BorderLayout.SOUTH);

		initPrefs();
		App.prefs.addPrefsListener(this);
	}

	/** Return the Year of the currently selected cell.
	 @return the selected Year */
	public Year getSelectedYear() {
		return ((DecadalModel) myModel).getYear(myTable.getSelectedRow(),
				myTable.getSelectedColumn());
	}

	// default is an empty string to type in.
	public void insertYear() {
		insertYear("");
	}

	// use Sample.MR for now.
	// -- let users change this?
	// -- add it as "MR", display as a different symbol, and save as "MR"?
	public void insertMR() {
		insertYear(new Integer(Sample.MR));
	}

	public void insertYear(Object val) {
		// note: ideally this would never be called for isSummed() or
		// isIndexed() samples, so these checks will be obselete.

		// make sure it's not indexed or summed
		if (!mySample.isEditable()) {
			Alert.error("Can't Modify Data",
					"You cannot modify indexed or summed data files.");
			return;
		}

		// get row, col
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();

		// get year => get data index
		Year y = ((DecadalModel) myModel).getYear(row, col);
		int i = y.diff(mySample.range.getStart());

		// make sure it's a valid place to insert a year
		if (!mySample.range.contains(y)
				&& !mySample.range.getEnd().add(+1).equals(y)) {
			// Alert.error("Can't insert here",
			//    "This isn't a valid place to insert a year.");
			return;
		}

		// insert 0
		mySample.data.add(i, val); // new Integer(0));
		mySample.range = new Range(mySample.range.getStart(), mySample.range
				.getEnd().add(+1));
		// REFACTOR: by LoD, should be range.extend()

		// fire event -- obsolete?
		((DecadalModel) myModel).fireTableDataChanged();

		// select this cell again?  edit it
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		// (HACK! -- but not if it's a MR.)
		if (!val.equals(new Integer(Sample.MR)))
			myTable.editCellAt(row, col);

		// set modified
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
		mySample.setModified();
	}

	// TODO: insert/delete shouldn't be enabled if the selection isn't a data year, either.

	public void deleteYear() {
		// make sure it's not indexed or summed
		if (!mySample.isEditable()) {
			Bug.bug(new IllegalArgumentException(
					"deleteYear() called on non-editable sample"));
			return;
		}

		// get row, col
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();

		// get year => get data index
		Year y = ((DecadalModel) myModel).getYear(row, col);
		int i = y.diff(mySample.range.getStart());

		// make sure there's data to delete
		if (!mySample.range.contains(y)) {
			// Alert.error("Can't delete here",
			//    "This isn't a value that can be deleted.");
			return;
		}

		// delete value
		mySample.data.remove(i);
		// mySample.range.end = mySample.range.end.add(-1);
		mySample.range = new Range(mySample.range.getStart(), mySample.range
				.getEnd().add(-1));

		// fire event
		((DecadalModel) myModel).fireTableDataChanged();

		// select this cell again
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		// note: if the last datum was just deleted, should back up
		// the cursor to the new-last-cell.  better yet: BACKSPACE
		// does this always, DELETE never does.

		// set modified
		mySample.setModified();		
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
	}

	public void sampleRedated(SampleEvent e) {
		// update data view
		((DecadalModel) myModel).fireTableDataChanged();
	}

	public void sampleDataChanged(SampleEvent e) {
		// update data view
		((DecadalModel) myModel).fireTableDataChanged();
		// FIXME: make myModel an AbstractTableModel, so i don't have to cast
	}

	public void sampleMetadataChanged(SampleEvent e) {
	}

	public void sampleElementsChanged(SampleEvent e) {
	}

	private void initPrefs() {
		// reset fonts
		Font font = Font.decode(App.prefs.getPref("corina.edit.font"));
		if (font != null)
			myTable.setFont(font);

		// from font size, set table row height
		myTable.setRowHeight((font == null ? 12 : font.getSize()) + 4);
		// BUG: this seems to not work sometimes (?) -- try zapfino

		// disable gridlines, if requested
		boolean gridlines = Boolean.valueOf(
				App.prefs.getPref(Prefs.EDIT_GRIDLINES)).booleanValue();
		myTable.setShowGrid(gridlines);

		// set colors
		myTable.setBackground(App.prefs.getColorPref(Prefs.EDIT_BACKGROUND,
				Color.white));
		myTable.setForeground(App.prefs.getColorPref(Prefs.EDIT_FOREGROUND,
				Color.black));
	}

	// should this be part of update()?  well, the constructor will
	// need it, too, so it might as well be a separate method, anyway.
	public void prefChanged(PrefsEvent e) {
		initPrefs();
	}

	public void measured(int x) {
		// figure out what year we're looking at now -- BREAKS IF EDITING=TRUE
		Year y = ((DecadalModel) myTable.getModel()).getYear(myTable
				.getSelectedRow(), myTable.getSelectedColumn());

		// beyond the end?  extend.
		// -- old way:
		// Year end = mySample.range.getEnd();
		// if (y.compareTo(end) > 0) {
		// mySample.range.setEnd(end.add(1));
		// mySample.data.add(new Integer(0));
		// }
		if (!mySample.range.contains(y)) {
			mySample.range = new Range(mySample.range.getStart(),
					mySample.range.getEnd().add(1));
			mySample.data.add(new Integer(0));
		}

		// set the value
		int i = y.diff(mySample.range.getStart());
		mySample.data.set(i, new Integer(x));

		// beep! (twice on column 0)
		Toolkit.getDefaultToolkit().beep();
		if (y.column() == 0)
			Toolkit.getDefaultToolkit().beep();

		// what's the next year?
		y = y.add(1);

		// where's it located?
		int row = y.row() - mySample.range.getStart().row();
		int col = y.column() + 1;

		// fire sample events first, so the table update below gets the good data
		mySample.fireSampleRedated();
		mySample.fireSampleDataChanged();
		mySample.setModified();

		// update table (overkill?)
		((AbstractTableModel) myTable.getModel()).fireTableDataChanged();

		// select it
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);
	}
}
