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

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.gui.HasPreferences;

import java.util.Collections;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;

/**
   A view of the raw data in a Sample.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

    // left to add:
    // - setSelectedYear(Year) -- (why?)
    // - (the popup)

public class SampleDataView extends JPanel implements SampleListener, HasPreferences {

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
	myTable = new JTable(myModel);

	// key listener for table
	myTable.addKeyListener(new DecadalKeyListener(myTable, mySample));

	// select the first year
	myTable.setRowSelectionAllowed(false);
	myTable.setRowSelectionInterval(0, 0);
	myTable.setColumnSelectionInterval(mySample.range.getStart().column() + 1,
					   mySample.range.getStart().column() + 1);

	// don't let the columns be rearranged or resized
	myTable.getTableHeader().setReorderingAllowed(false);
	myTable.getTableHeader().setResizingAllowed(false);

	// make the last column a jprogressbar, % of max
	int max = 0;
	if (mySample.count != null)
	    max = ((Integer) Collections.max(mySample.count)).intValue();
	myTable.getColumnModel().getColumn(11).setCellRenderer(new CountRenderer(max));

	// set font, gridlines, colors ==> handled by refreshFromPreferences()

	// add to panel
	setLayout(new BorderLayout(0, 0));
	add(new JScrollPane(myTable,
			    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
	    BorderLayout.CENTER);
	add(new Modeline(myTable, mySample), BorderLayout.SOUTH);
    }

    /** Return the Year of the currently selected cell.
	@return the selected Year */
    public Year getSelectedYear() {
	return ((DecadalModel) myModel).getYear(myTable.getSelectedRow(),
						myTable.getSelectedColumn());
    }

    public void insertYear() {
	// note: ideally this would never be called for isSummed() or
	// isIndexed() samples, so these checks will be obselete.

	// make sure it's not indexed
	if (mySample.isSummed()) {
	    JOptionPane.showMessageDialog(null,
					  "You cannot modify summed data files.\n" +
					     "Try cleaning it first.",
					  "Can't Modify Data",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}

	// make sure it's not summed
	if (mySample.isIndexed()) {
	    JOptionPane.showMessageDialog(null,
					  "You cannot modify indexed data files.\n" +
					     "Try loading the raw data file, and editing that.",
					  "Can't Modify Data",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}

	// get row, col
	int row = myTable.getSelectedRow();
	int col = myTable.getSelectedColumn();

	// get year => get data index
	Year y = ((DecadalModel) myModel).getYear(row, col);
	int i = y.diff(mySample.range.getStart());

	// make sure it's a valid place to insert a year
	if (!mySample.range.contains(y) && !mySample.range.getEnd().add(+1).equals(y)) {
	    // JOptionPane.showMessageDialog(null,
	    //   "This isn't a valid place to insert a year.",
	    //   "Can't insert here",
	    //   JOptionPane.ERROR_MESSAGE);
	    return;
	}

	// insert 0
	mySample.data.add(i, ""); // new Integer(0));
	// mySample.range.end = mySample.range.getEnd().add(+1);
	mySample.range = new Range(mySample.range.getStart(),
				   mySample.range.getEnd().add(+1));
	
	// fire event -- obsolete?
	((DecadalModel) myModel).fireTableDataChanged();

	// select this cell again?  edit it
	myTable.setRowSelectionInterval(row, row);
	myTable.setColumnSelectionInterval(col, col);
	myTable.editCellAt(row, col);

	// set modified
	mySample.fireSampleDataChanged();
	mySample.fireSampleRedated();
	mySample.setModified();
    }

    public void deleteYear() {
	// make sure it's not indexed or summed
	if (mySample.isIndexed() || mySample.isSummed()) {
	    JOptionPane.showMessageDialog(null,
					  "You cannot modify indexed or summed data files.\n",
					  "Can't Modify Data",
					  JOptionPane.ERROR_MESSAGE);
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
	    // JOptionPane.showMessageDialog(null,
	    //   "This isn't a value that can be deleted.",
	    //   "Can't delete here",
	    //   JOptionPane.ERROR_MESSAGE);
	    return;
	}

	// delete value
	mySample.data.remove(i);
	// mySample.range.end = mySample.range.end.add(-1);
	mySample.range = new Range(mySample.range.getStart(),
				   mySample.range.getEnd().add(-1));

	// fire event
	((DecadalModel) myModel).fireTableDataChanged();

	// select this cell again
	myTable.setRowSelectionInterval(row, row);
	myTable.setColumnSelectionInterval(col, col);

	// note: if the last datum was just deleted, should back up
	// the cursor to the new-last-cell.  better yet: BACKSPACE
	// does this always, DELETE never does.

	// set modified
	mySample.fireSampleDataChanged();
	mySample.fireSampleRedated();
	mySample.setModified();
    }

    public void sampleRedated(SampleEvent e) {
	// update data view
	((DecadalModel) myModel).fireTableDataChanged();
    }
    public void sampleDataChanged(SampleEvent e) {
	// update data view
	((DecadalModel) myModel).fireTableDataChanged();
    }
    public void sampleMetadataChanged(SampleEvent e) { }
    public void sampleFormatChanged(SampleEvent e) {
	// update data view
	((DecadalModel) myModel).fireTableDataChanged();
    }
    public void sampleElementsChanged(SampleEvent e) { }

    // should this be part of update()?  well, the constructor will
    // need it, too, so it might as well be a separate method, anyway.
    public void refreshFromPreferences() {
	// reset fonts
	Font font = Font.getFont("corina.edit.font");
	if (font != null)
	    myTable.setFont(font);

	// from font size, set table row height
	myTable.setRowHeight((font == null ? 12 : font.getSize()) + 4);

	// disable gridlines, if requested
	boolean gridlines = Boolean.getBoolean("corina.edit.gridlines");
	myTable.setShowGrid(gridlines);

	// set colors
	Color fore = Color.getColor("corina.edit.foreground");
	Color back = Color.getColor("corina.edit.background");
	if (back != null)
	    myTable.setBackground(back);
	if (fore != null)
	    myTable.setForeground(fore);
    }

    public void measured(int x) {
	// figure out what year we're looking at now -- BREAKS IF EDITING=TRUE
	Year y = ((DecadalModel) myTable.getModel()).getYear(myTable.getSelectedRow(),
							     myTable.getSelectedColumn());

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
