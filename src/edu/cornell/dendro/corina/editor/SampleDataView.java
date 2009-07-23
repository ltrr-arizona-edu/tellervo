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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasRemark;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.prefs.PrefsEvent;
import edu.cornell.dendro.corina.prefs.PrefsListener;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.PopupListener;

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

	private static final long serialVersionUID = 1L;

	private Sample mySample;

	public JTable myTable;

	protected TableModel myModel;

	// pass this along to the table
	@Override
	public void requestFocus() {
		myTable.requestFocus();
	}

	// (for Editor)
	public void stopEditing(boolean disableFutureEdits) {
		
		/*
		 * This "old way" looks like quite a kludge. 
		 * How about we tell the table to stop editing, instead of jamming
		 * keystrokes into it's queue? - lucas
		 * 
		  // strategy: if editing, fire an VK_ENTER keytype event at the table
		  // (that also solves the "user typed the number and shouldn't lose that data" problem)
		  if (myTable.isEditing())
			  myTable.dispatchEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED,
				  	System.currentTimeMillis(), 0, KeyEvent.VK_ENTER));
		*/
		
		int row = myTable.getEditingRow();
		int col = myTable.getEditingColumn();
		if (row != -1 && col != -1) {
		  myTable.getCellEditor(row,col).stopCellEditing(); 

		  // disable editing
		  if(disableFutureEdits)
			  ((DecadalModel)myModel).enableEditing(false);
		  
		  // reselect whatever we just deselected
		  myTable.setRowSelectionInterval(row, row);
		  myTable.setColumnSelectionInterval(col, col);
		}
		
	}
	
	public void startEditing() {
		((DecadalModel)myModel).enableEditing(true);
	}


	public void enableEditing(boolean enable) {
		if(enable)
			startEditing();
		else
			stopEditing(true);
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
		myTable.setGridColor(new Color(240, 240, 240)); 

		// mouse listener for table
		myTable.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				int row = myTable.rowAtPoint(e.getPoint());
				int col = myTable.columnAtPoint(e.getPoint());

				JPopupMenu menu = createPopupMenu(row, col);
				
				if(menu != null)
					menu.show(myTable, e.getX(), e.getY());
			}
		});

		// key listener for table
		myTable.addKeyListener(new DecadalKeyListener(myTable, mySample));

		myTable.setCellSelectionEnabled(true);
		myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// select the first year
		myTable.setRowSelectionInterval(0, 0);
		myTable.setColumnSelectionInterval(
				mySample.getRange().getStart().column() + 1, mySample.getRange()
						.getStart().column() + 1);
		

		// don't let the columns be rearranged or resized
		myTable.getTableHeader().setReorderingAllowed(false);
		myTable.getTableHeader().setResizingAllowed(false);

		// make the last column a jprogressbar, % of max
		int max = 0;
		if (mySample.getCount() != null)
			max = (Collections.max(mySample.getCount())).intValue();
		// DISABLED: use column-header renderer for first column (pseudo-row-headers)
		// -- it doesn't look that great, since there are still gridlines between
		// rows; what i should really do is make a real table-row-header, which isn't too hard.
		// myTable.getColumnModel().getColumn(0).setCellRenderer(new
		//         javax.swing.table.JTableHeader().getDefaultRenderer());
		myTable.getColumnModel().getColumn(11).setCellRenderer(
				new CountRenderer(max));
		
		TableCellRenderer defaultCellRenderer = new IconBackgroundCellRenderer(mySample);
		for(int i = 1; i < 11; i++)
			myTable.getColumnModel().getColumn(i).setCellRenderer(defaultCellRenderer);

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
	
	private JPopupMenu createPopupMenu(int row, int col) {
		// clicked on a row header?  don't do anything.
		if (col == 0)
			return null;

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
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				insertYear();
			}
		});

		JMenuItem insertMR = Builder.makeMenuItem("insert_mr");
		insertMR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				insertMR();
			}
		});

		JMenuItem delete = Builder.makeMenuItem("delete_year");
		delete.addActionListener(new ActionListener() {
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
		
		popup.addSeparator();
		addNotesMenu(popup, row, col);
		
		return popup;
	}
	
	private final void addNotesMenu(JPopupMenu menu, int row, int col) {
		// get the year
		final Year y = ((DecadalModel) myModel).getYear(row, col);
		
		if(!mySample.getRange().contains(y))
			return;
		
		// get the already-set remarks for this year
		final List<TridasRemark> remarksForYear = mySample.getRemarksForYear(y);
		
		for(final NormalTridasRemark nt : NormalTridasRemark.values()) {
			JMenuItem item = new JMenuItem(nt.value());
			
			menu.add(item);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean adding = true;
					
					// adding a remark is simple!
					if(adding) {
						TridasRemark remark = new TridasRemark();
						remark.setNormalTridas(nt);
						
						remarksForYear.add(remark);
					}
					
					mySample.fireSampleDataChanged();
				}
			});
		}
		
	}

	/**
	 * @return a list of all possible TridasRemarks, in their form
	 */
	private List<TridasRemark> populateTridasRemarks(JMenu tridasMenu) {
		ArrayList<TridasRemark> remarks = new ArrayList<TridasRemark>();
		
		for(NormalTridasRemark nt : NormalTridasRemark.values()) {
			TridasRemark remark = new TridasRemark();
			
			remark.setNormalTridas(nt);
			
			remarks.add(remark);
		}
		
		return remarks;
	}

	/** Return the Year of the currently selected cell.
	 @return the selected Year */
	public Year getSelectedYear() {
		return ((DecadalModel) myModel).getYear(myTable.getSelectedRow(),
				myTable.getSelectedColumn());
	}

	// default is an empty string to type in.
	public void insertYear() {
		insertYear(0, true);
	}

	// use Sample.MR for now.
	// -- let users change this?
	// -- add it as "MR", display as a different symbol, and save as "MR"?
	public void insertMR() {
		insertYear(new Integer(Sample.MR), true);
	}

	public void insertYear(Integer val, boolean selectAndEdit) {
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
		int i = y.diff(mySample.getRange().getStart());

		// make sure it's a valid place to insert a year
		if (!mySample.getRange().contains(y)
				&& !mySample.getRange().getEnd().add(+1).equals(y)) {
			// Alert.error("Can't insert here",
			//    "This isn't a valid place to insert a year.");
			return;
		}

		// insert 0
		mySample.getData().add(i, val); // new Integer(0));
		mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
				.getEnd().add(+1)));
		// REFACTOR: by LoD, should be range.extend()

		// fire event -- obsolete?
		((DecadalModel) myModel).fireTableDataChanged();

		// select this cell again?  edit it
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		if (selectAndEdit)
			myTable.editCellAt(row, col);

		// set modified
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
		mySample.setModified();
		
		if(!selectAndEdit) {
			// what's the next year?
			y = y.add(1);

			// where's it located?
			row = y.row() - mySample.getRange().getStart().row();
			col = y.column() + 1;

			myTable.setRowSelectionInterval(row, row);
			myTable.setColumnSelectionInterval(col, col);
		}
	}

	public void insertYears(Integer val, int nYears) {
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
		int i = y.diff(mySample.getRange().getStart());

		// make sure it's a valid place to insert a year
		if (!mySample.getRange().contains(y)
				&& !mySample.getRange().getEnd().add(nYears).equals(y)) {
			// Alert.error("Can't insert here",
			//    "This isn't a valid place to insert a year.");
			return;
		}

		// insert 0, nyears times...
		for(int j = 0; j < nYears; j++)
			mySample.getData().add(i, val); // new Integer(0));
		mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
				.getEnd().add(nYears)));
		// REFACTOR: by LoD, should be range.extend()

		// fire event -- obsolete?
		((DecadalModel) myModel).fireTableDataChanged();

		// select this cell again?  edit it
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		// set modified
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
		mySample.setModified();
		
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);
	}
	
	// TODO: insert/delete shouldn't be enabled if the selection isn't a data year, either.

	public void deleteYear() {
		// make sure it's not indexed or summed
		if (!mySample.isEditable()) {
			new Bug(new IllegalArgumentException(
					"deleteYear() called on non-editable sample"));
			return;
		}

		// get row, col
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();

		// get year => get data index
		Year y = ((DecadalModel) myModel).getYear(row, col);
		int i = y.diff(mySample.getRange().getStart());

		// make sure there's data to delete
		if (!mySample.getRange().contains(y)) {
			// Alert.error("Can't delete here",
			//    "This isn't a value that can be deleted.");
			return;
		}

		// delete value
		mySample.getData().remove(i);
		// mySample.range.end = mySample.range.end.add(-1);
		mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
				.getEnd().add(-1)));

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
		Font font = App.prefs.getFontPref("corina.edit.font", null);
		if (font != null)
			myTable.setFont(font);

		// from font size, set table row height
		myTable.setRowHeight((font == null ? 12 : font.getSize()) + 4);
		// BUG: this seems to not work sometimes (?) -- try zapfino

		myTable.setRowHeight(16);
		
		// disable gridlines, if requested
		boolean gridlines = Boolean.valueOf(
				App.prefs.getPref(Prefs.EDIT_GRIDLINES, "true")).booleanValue();
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

	// returns the Year that was measured, for graphical display goodness.
	public Year measured(int x) {
		/*
		 * This code inserts the year instead of overwriting values... 
		 * insertYear(new Integer(x), false);
		 */
		
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
		if (!mySample.getRange().contains(y)) {
			mySample.setRange(new Range(mySample.getRange().getStart(),
					mySample.getRange().getEnd().add(1)));
			mySample.getData().add(new Integer(0));
		}

		// set the value
		int i = y.diff(mySample.getRange().getStart());
		mySample.getData().set(i, new Integer(x));
		
		// this is the year we return...
		Year retYear = y;

		// beep! (twice on column 0)
		/*
		 * meh.. beeping doesn't do anything on newer computers.
		 * 
		 * play a .wav file instead... to be implemented.
		 * 
		 *	Toolkit.getDefaultToolkit().beep();
		 *	if (y.column() == 0)
		 *		Toolkit.getDefaultToolkit().beep();
		 */

		// what's the next year?
		y = y.add(1);

		// where's it located?
		int row = y.row() - mySample.getRange().getStart().row();
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
		
		return retYear;
	}
}
