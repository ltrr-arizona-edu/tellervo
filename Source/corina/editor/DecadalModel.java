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
import corina.manip.Redate;
import corina.gui.Bug;

import java.util.ResourceBundle;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/*
  <p>Table model for a dendro dataset.</p>

  <p>Left to do: it might be simpler to make the data table actually 3
  tables (year, data, count) that scroll together, so tabbing always
  stays in the same table.</p>

  @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
  @version $Id$ */

// bug! -- if the length changes (e.g., truncate), there's a problem
// with sums.  ouch.  (<-- what did i mean by this?)

public class DecadalModel extends AbstractTableModel {

    /** The sample whose data is being displayed. */
    protected Sample s;

    /** The first row of the dataset.  Default is 0.
	@see #row_max
	@see Year#row() */
    protected int row_min=0;

    /** The last row of the dataset.  Default is -1, so if row_min and
	row_max are never set, no rows are displayed.
	@see #row_min
	@see Year#row() */
    protected int row_max=-1;

    /** Default constructor.  Not used, but required for
        subclassing. */
    public DecadalModel() {
	// to keep subclasses happy
    }

    /** Constructor, given a Sample.
	@param s the Sample whose data is being viewed */
    public DecadalModel(Sample s) {
	this.s = s;
	countRows();
    }

    public Sample getSample() {
	return s;
    }

    /** Return the column name.  The first column is "Year", the last
        column is "Nr" (number), and the 10 columns in between are 0
        through 9.
	@param col the JTable column number to query
	@return the column's name */
    public String getColumnName(int col) {
	if (col == 0)
	    return "Year";
	else if (col == 11)
	    return "No.";
	else
	    return Integer.toString(col-1);
    }

    // query the Sample to figure out row_min, row_max
    protected void countRows() {
	row_min = s.range.getStart().row();
	row_max = s.range.getEnd().row();
    }

    /** Return the row count.  For the Data tab of a Sample (i.e., not
	Weiserjahre), it adds an extra row if the last datum is a -9
	year, thus ensuring that there's always one blank after the
	end for user editing.
	@return the number of displayable rows */
    public int getRowCount() {
	// these need to be updated when the user inserts/deletes
	countRows();

	// compute num rows
	int n = row_max - row_min + 1;

	// always give a blank space for more data entry
	if (s.range.getEnd().column() == 9)
	    n++;

	return n;
    }

    /** Return the column count.  This is always 12: the decade, 10
	columns of data, and the histogram cell
	@return the number of columns, 12 */
    public int getColumnCount() {
	return 12;
    }

    /** Return the Year that a (row,col) cell should display.
	@param row the row in question
	@param col the column in question
	@return the Year that the (row,col) cell should display */
    public /* protected */ Year getYear(int row, int col) {
	return new Year(row + row_min, // offset row by row_min (top row is 0)
			col - 1); // offset col by 1 (left col is year label)
    }

    // i'll have a lot of these.  better to use only one (flyweight).
    protected final static Integer ZERO = new Integer(0);

    // returns an Integer; by returning an Object, i can avoid an
    // extra (unnecessary) cast when returning from means.get().
    protected final Object getMean(int row) {
	// if no count, just return zero
	if (s.count == null)
	    return ZERO;

	// compute left end of range
	Year y1 = getYear(row, 1);
	if (row == 0)
	    y1 = s.range.getStart();
	else if (row + row_min == 0)
	    y1 = new Year(1);

	// compute right end of range: min(10th column, end)
	Year y2 = getYear(row, 10);
	if (y2.compareTo(s.range.getEnd()) > 0)
	    y2 = s.range.getEnd();

	// compute span (assumed nonzero), sum, mean
	int sum=0, span=y2.diff(y1)+1;
	for (int i=y1.diff(s.range.getStart()); i<=y2.diff(s.range.getStart()); i++) {
	    try {
		sum += ((Integer) s.count.get(i)).intValue(); // getting IndexOutOfBoundsException here -- so y1 isn't in s.range?
	    } catch (IndexOutOfBoundsException ioobe) {
		System.out.println("fuck ... ioobe i=" + i + ",y1=" + y1 + ",y2=" + y2 + ",span=" + span);
	    }
	}

	// whoa, very rare case it can be zero!  (when?  the n+1 year of a sum that ends in -9.)
	if (span == 0) return ZERO;

	// return it
	return new Integer(sum/span);
    }

    public Object getValueAt(int row, int col) {
	if (col == 0) {
	    if (row == 0)
		return s.range.getStart();
	    else if (row + row_min == 0) // special case
		return "1";
	    else
		return getYear(row, col+1);
	} else if (col == 11) { // average
	    return getMean(row);
	} else if (col==1 && row+row_min==0) {
	    return null; // year "zero"
	} else {
	    Year y = getYear(row, col);
	    if (!s.range.contains(y))
		return null;
	    else
		return s.data.get(y.diff(s.range.getStart()));
	}
    }

    /** Return the Class of the specified column.  This is
	<code>Integer.class</code> for the displayed data, else
	<code>String.class</code>
	@param col the column to query
	@return the column's class */
    public Class getColumnClass(int col) {
	if (col >= 1 && col <= 10)
	    return Integer.class;
	else
	    return String.class;
    }

    public boolean isCellEditable(int row, int col) {
	// new feature: (0,0) _is_ editable
	if (row==0 && col==0)
	    return true;

	// if bad column, is indexed, or is summed, NOT editable
	if ((col<1) || (col>10) || s.isIndexed() || s.isSummed())
	    return false;

	// proposed year to edit
	Year y = getYear(row, col);

	// okay if in range, or one beyond end
	return (s.range.contains(y) || s.range.getEnd().add(1).equals(y));
    }

    // oldval from /previous/ edit -- because typing into a cell
    // counts as 2 edits, oldvalue->"", ""->newvalue
    private Object lastOldVal=null;

    public void setValueAt(Object value, int row, int col) {
	// new feature: (0,0) redates
	if (row==0 && col==0) {
	    // redate
	    try {
		// compute and set
		Range newRange = s.range.redateStartTo(new Year((String) value));

		// redate, and post undo
		s.postEdit(Redate.redate(s, newRange));
		return;
	    } catch (NumberFormatException nfe) {
		// tell the user this is bad?
	    } catch (Exception e) {
		Bug.bug(e);
	    }

	    // stop, either way
	    return;
	}

	// if user just typed into end+1, we'll need to increase the size!
	final boolean bigger =  s.range.getEnd().add(+1).equals(getYear(row, col));

	// if we get a String, make it into an Integer
	if ((value instanceof String) && ((String) value).length() > 0)
	    try {
		value = Integer.decode((String) value);
	    } catch (NumberFormatException nfe) {
		value = new Integer(0); // can't parse, give 'em 0
	    } catch (Exception e) {
		Bug.bug(e);
	    }

	final Year y = (bigger ? s.range.getEnd().add(+1) : getYear(row,col));
	Object tmp = (bigger ? null : s.data.get(y.diff(s.range.getStart())));
	if (tmp != null && tmp.toString().length() == 0) {
	    tmp = lastOldVal;
	} else {
	    lastOldVal = tmp;
	}
	final Object oldVal = tmp;

	if (bigger) {
	    s.data.add(value);
	    // s.range.end = s.range.getEnd().add(+1);
	    s.range = new Range(s.range.getStart(),
				s.range.getEnd().add(+1));
	} else {
	    s.data.set(y.diff(s.range.getStart()), value);
	}
	fireTableCellUpdated(row, col);

	// set modified
	s.fireSampleDataChanged();
	if (bigger)
	    s.fireSampleRedated();

	// add undo-able
	if (value==null || value.toString().length() == 0) return; // better to use collapsing undo-edits -- solves both problems
	final Object glue = value;
	s.postEdit(new AbstractUndoableEdit() {
		private Object newVal = glue;
		private boolean grew = bigger; // BIGGER IS ALWAYS FALSE HERE -- LASTVAL PROBLEM!
		public void undo() throws CannotUndoException {
		    System.out.println("undo, grew=" + grew);
		    if (grew) {
			s.data.remove(s.data.size()-1);
			// s.range.end = s.range.end.add(-1);
			s.range = new Range(s.range.getStart(),
					    s.range.getEnd().add(-1));
		    } else {
			s.data.set(y.diff(s.range.getStart()), oldVal);
			// selectYear(y);
		    }

		    s.fireSampleDataChanged();
		    if (grew)
			s.fireSampleRedated();
		}
		public void redo() throws CannotRedoException {
		    System.out.println("redo, grew=" + grew);
		    if (grew) {
			s.data.add(newVal);
			// s.range.end = s.range.end.add(+1);
			s.range = new Range(s.range.getStart(),
					    s.range.getEnd().add(+1));
		    } else {
			s.data.set(y.diff(s.range.getStart()), newVal);
		    }

		    s.fireSampleDataChanged();
		    if (grew)
			s.fireSampleRedated();
		}
		public boolean canRedo() {
		    return true;
		}
		public String getPresentationName() {
		    return "Edit";
		}
	    });
    }
}
