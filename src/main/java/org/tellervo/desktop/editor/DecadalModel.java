/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
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

package org.tellervo.desktop.editor;

import java.awt.KeyboardFocusManager;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.manip.RedateDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Years;


/**
   Table model for a decadal dataset.

   <p>Left to do: it might be simpler to make the data table actually
   3 tables (year, data, count) that scroll together, so tabbing
   always stays in the same table.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class DecadalModel extends AbstractTableModel {

	private final static Logger log = LoggerFactory.getLogger(DecadalModel.class);
	private static final long serialVersionUID = 1L;
	
	/** I'll have a lot of these.  better to use only one (flyweight).*/
	protected final static Integer ZERO = Integer.valueOf(0);
	
	/** Unit multiplier*/
	private Double unitMultiplier = 0.0;
	
	/** The sample whose data is being displayed. */
	protected Sample s;

	/** The first row of the dataset.  Default is 0.
	 @see #row_max
	 @see Year#row() */
	protected int row_min = 0;

	/** The last row of the dataset.  Default is -1, so if row_min and
	 row_max are never set, no rows are displayed.
	 @see #row_min
	 @see Year#row() */
	protected int row_max = -1;

	/** Whether editing has been disabled **/
	private boolean editingOff = false;

	/** oldval from /previous/ edit -- because typing into a cell
	counts as 2 edits, oldvalue->"", ""->newvalue*/
	private Number lastOldVal = null;
	
	/** Default constructor.  Not used, but required for subclassing. */
	public DecadalModel() {
		// to keep subclasses happy
	}

	/** Constructor, given a Sample.
	 @param s the Sample whose data is being viewed */
	public DecadalModel(Sample s) {
		this.s = s;
		countRows();
	}

	/**
	 * Get the sample for this model
	 * 
	 * @return
	 */
	public Sample getSample() {
		return s;
	}

	/** Return the column name.  The first column is "Year", the last
	 column is "Nr" (number), and the 10 columns in between are 0
	 through 9.
	 @param col the JTable column number to query
	 @return the column's name */
	@Override
	public String getColumnName(int col) {
		if (col == 0)
			return I18n.getText("editor.year");
		else if (col == 11)
			return I18n.getText("general.hash");
		else
			return Integer.toString(col - 1);
	}

	/** query the Sample to figure out row_min, row_max */
	protected void countRows() {
		row_min = s.getRange().getStart().row();
		row_max = s.getRange().getEnd().row();
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

		// always give a blank space for more data entry; if the last year
		// is the last year of this decade, we'll need another decade
		if (s.getRange().getEnd().column() == 9)
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

	 <b>This method creates a new object, and is called far, far too often.</b>
	 For opening one raw sample, it creates 526 Year objects, and about as
	 many each time the window is uncovered (and there are only about 200 years
	 of data visible!).  This can't be good for performance.
	 Make sure each call to this is really needed, or make them not require
	 object creation each time.

	 (Candidates for this: 2 calls in getValueAt().)

	 @param row the row in question
	 @param col the column in question
	 @return the Year that the (row,col) cell should display */
	public Year getYear(int row, int col) {
		return Years.forRowAndColumn(row + row_min, col - 1);
	}

	protected final Integer getMean(int row) {
		// if no count, just return zero
		if (!s.hasCount())
			return ZERO;

		// compute left end of range
		Year y1 = getYear(row, 1);
		if (row == 0)
			y1 = s.getRange().getStart();
		else if (row + row_min == 0)
			y1 = new Year(1);

		// compute right end of range: min(10th column, end)
		Year y2 = getYear(row, 10);
		y2 = Year.min(y2, s.getRange().getEnd());

		// compute span (assumed nonzero), sum, mean
		int sum = 0, span = y2.diff(y1) + 1;
		int left = y1.diff(s.getRange().getStart());
		int right = y2.diff(s.getRange().getStart());
		for (int i = left; i <= right; i++)
			sum += (s.getCount().get(i)).intValue();

		// in a very rare case it can be zero!  (when?  the n+1 year of a sum that ends in -9.)
		// (actually, though, sums shouldn't have the n+1 cell, so this special case belongs elsewhere!)
		if (span == 0)
			return ZERO;

		// return the mean
		int mean = Math.round((float) sum / span);
		return new Integer(mean);
	}

	/**
	 * Get the value of a particular cell
	 */
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			if (row == 0)
				return s.getRange().getStart();
			else if (row + row_min == 0) // special case
				return "1";
			else {
				// System.out.println("getYear() called (a=" + __a++ + ")");
				return getYear(row, 1); // called 66 times
				// (was (row,col+1), but col=0 here)
			}
		} else if (col == 11) { // average
			return getMean(row);
		} else if (col == 1 && row + row_min == 0) {
			return null; // year "zero"
		} else {
			// System.out.println("getYear() called (b=" + __b++ + ")");
			Year y = getYear(row, col); // 479 -- THIS GETS CALLED TOO MANY TIMES!
			if (!s.getRange().contains(y)) return null;
			
			MeasurementVariable var = MeasurementVariable.getPreferredVariable(s);

			if(var.equals(MeasurementVariable.RING_WIDTH))
			{
				return s.getRingWidthData().get(y.diff(s.getRange().getStart()));
			}
			else if (var.equals(MeasurementVariable.EARLYWOOD_WIDTH))
			{
				return s.getEarlywoodWidthData().get(y.diff(s.getRange().getStart()));
			}
			else if (var.equals(MeasurementVariable.LATEWOOD_WIDTH))
			{
				return s.getLatewoodWidthData().get(y.diff(s.getRange().getStart()));
			}
			else if (var.equals(MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH))
			{
				return new EWLWValue(s.getEarlywoodWidthData().get(y.diff(s.getRange().getStart())),
										s.getLatewoodWidthData().get(y.diff(s.getRange().getStart()))); 
			}
				
			return null;
			
			
		}
	}

	/** Return the Class of the specified column.  This is
	 <code>Integer.class</code> for the displayed data, else
	 <code>String.class</code>
	 @param col the column to query
	 @return the column's class */
	@Override
	public Class<?> getColumnClass(int col) {
		
		// If we're doing 
		if(App.prefs.getPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.RING_WIDTH.toString()).equals(MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH))
		{
			return String.class;
		}
		
		return ((col >= 1 && col <= 10) ? Integer.class : String.class);
	}

	/**
	 * Turn editing on/off
	 * 
	 * @param enable
	 */
	public void enableEditing(boolean enable) {
		// if it's true, turn off editingOff
		editingOff = !enable;
	}

	/**
	 * Determine if a particular cell is editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		// we're currently in a mode that doesn't allow editing!
		if(editingOff)
			return false;

		// feature: (0,0) is editable (it's the start year, and editing it redates the sample)
		if (row == 0 && col == 0)
			return true;

		// if bad column, is indexed, or is summed, NOT editable
		if ((col < 1) || (col > 10) || (!s.isEditable()))
			return false;

		// proposed year to edit
		Year y = getYear(row, col);

		// If the sample contains early/late wood then do not allow editing of whole ring
		if(s.containsSubAnnualData() && MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.RING_WIDTH))
			return false;
		
		// okay if in range, or one beyond end
		return (s.getRange().contains(y) || s.getRange().getEnd().add(1).equals(y));
	}

	/**
	 * Set the value of a particular cell
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		
		// Redate series if the cell 0,0 (i.e. cell 1001) is altered 
		if (row == 0 && col == 0) {
			// redate
			try {
				// compute and set
				Year newYear = new Year((String) value);

				// if we don't change it at all, don't mark it as modified...
				if(newYear.equals(s.getRange().getStart()))
					return;
				
				Range newRange = s.getRange().redateStartTo(newYear);
				
				// get the current top level window
				Window windowFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
				if(windowFocusOwner instanceof JFrame)
					new RedateDialog(s, (JFrame) windowFocusOwner, newRange).setVisible(true);
				
			} catch (NumberFormatException nfe) {
				// tell the user this is bad?
			} catch (Exception e) {
				new Bug(e);
			}

			// stop, either way
			return;
		}

		// Convert the cell value into one or two Integers depending on type
		Number value1 = null;
		Number value2 = null;
		if ((value instanceof String) && ((String) value).length() > 0) 
		{
			// A string could be;
			// 1) 44/55 style for EW/LW 
			// 2) Plain integer
			// 3) Plain double
			// 4) Dodgy string
			
			String strvalue = ((String)value).trim();
			if (strvalue.matches("^[0-9]*/[0-9]*"))
			{
				// Option 1
				String[] widthparts = strvalue.split("/");
				if(widthparts.length!=2) return;
				
				try {
					// Option 2
					value1 = Integer.parseInt(widthparts[0]);
					value2 = Integer.parseInt(widthparts[1]);
				} catch (NumberFormatException nfe) {	
					try{
						// Option 3
						value1 = Double.parseDouble(widthparts[0]);
						value2 = Double.parseDouble(widthparts[1]);
					} catch (NumberFormatException nfe2) {
						// Option 4
						return;
					}
				} catch (Exception e) {
					new Bug(e);
					return;
				}
				
			}
			else
			{
				try {
					// Option 2
					value1 = Integer.parseInt(strvalue);
				} catch (NumberFormatException nfe) {
					try{
						// Option 3
						value1 = Double.parseDouble(strvalue);
					} catch (NumberFormatException nfe2) {
						// Option 4
						return;
					}
				} catch (Exception e) {
					new Bug(e);
					return;
				}
			}
		}
		else if(value instanceof Integer) 
		{
			value1 = (Integer) value;
		}
		else if(value instanceof Double) 
		{
			value1 = (Double) value;
		}
		else if(value == null)
		{
			value1 = null;
		}
		else if(value instanceof EWLWValue)
		{
			value1 = ((EWLWValue) value).getEarlywoodValue().intValue();
			value2 = ((EWLWValue) value).getLatewoodValue().intValue();
			
			if(value1==null || value2==null)
			{
				log.debug("Ignoring data input value because one or both values of EW/LW is null");
				return;
			}
		}
		else
		{
			return;
		}


		// If the user typed into end+1, we'll need to increase the size of the data model
		final boolean bigger = s.getRange().getEnd().add(+1).equals(
				getYear(row, col));
		final Year y = (bigger ? s.getRange().getEnd().add(+1) : getYear(row, col));
		Number tmp = (bigger ? null : s.getRingWidthData().get(y.diff(s.getRange().getStart())));
		if (tmp != null && tmp.toString().length() == 0) {
			tmp = lastOldVal;
		} else {
			lastOldVal = tmp;
		}
		final Number oldVal = tmp;

		
		if (bigger) {	
			// Adding extra value to end of <values> 
			
			if(value1 == null) return;
			
			if(MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.RING_WIDTH))
			{
				s.getRingWidthData().add(value1);
			}
			else if (MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.EARLYWOOD_WIDTH))
			{
				s.getEarlywoodWidthData().add(value1);
				s.getLatewoodWidthData().add(0);
				s.recalculateRingWidths();
			}
			else if (MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.LATEWOOD_WIDTH))
			{
				s.getEarlywoodWidthData().add(0);
				s.getLatewoodWidthData().add(value1);
				s.recalculateRingWidths();
			}
			else if (MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH))
			{
				s.getEarlywoodWidthData().add(value1);
				s.getLatewoodWidthData().add(value2);
				s.recalculateRingWidths();
			}
			else
			{
				log.error("Unknown measurement variable returned by MeasurementVariable.getPreferredVariable()");
			}
			
			// Reset the range
			s.setRange(new Range(s.getRange().getStart(), s.getRange().getEnd().add(+1)));
		} 
		else 
		{
			// Editing existing <value>
			if(value.equals(oldVal)) return;
			
			if(MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.RING_WIDTH))
			{
				s.getRingWidthData().set(y.diff(s.getRange().getStart()), value1);
			}
			else if(MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.EARLYWOOD_WIDTH))
			{
				s.getEarlywoodWidthData().set(y.diff(s.getRange().getStart()), value1);
				s.recalculateRingWidths();
			}
			else if(MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.LATEWOOD_WIDTH))
			{
				s.getLatewoodWidthData().set(y.diff(s.getRange().getStart()), value1);
				s.recalculateRingWidths();
			}
			else if(MeasurementVariable.getPreferredVariable(s).equals(MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH))
			{
				s.getEarlywoodWidthData().set(y.diff(s.getRange().getStart()), value1);
				s.getLatewoodWidthData().set(y.diff(s.getRange().getStart()), value2);
				s.recalculateRingWidths();
			}	
			
		}
		
		// Tell listeners that table has changed
		fireTableCellUpdated(row, col);

		// Mark the series as modified
		s.setModified();		
		
		// Tell listeners that sample data has changed
		s.fireSampleDataChanged();
		
		// If the series has had an extra year added warn listeners
		if (bigger) s.fireSampleRedated();

		// Add undoable
		if (value == null || value.toString().length() == 0) return; // better to use collapsing undo-edits -- solves both problems
		
		final Number glue = value1;
		s.postEdit(new AbstractUndoableEdit() {
			private static final long serialVersionUID = 1L;
			
			private Number newVal = glue;
			private boolean grew = bigger; // BIGGER IS ALWAYS FALSE HERE -- LASTVAL PROBLEM!

			@Override
			public void undo() throws CannotUndoException {
				// debugging
				System.out.println("undo, grew=" + grew);

				if (grew) {
					s.getRingWidthData().remove(s.getRingWidthData().size() - 1);
					s.setRange(new Range(s.getRange().getStart(), s.getRange().getEnd()
							.add(-1)));
				} else {
					s.getRingWidthData().set(y.diff(s.getRange().getStart()), oldVal);
					// selectYear(y);
				}

				s.fireSampleDataChanged();
				if (grew)
					s.fireSampleRedated();
			}

			@Override
			public void redo() throws CannotRedoException {
				// debugging
				System.out.println("redo, grew=" + grew);

				if (grew) {
					s.getRingWidthData().add(newVal);
					s.setRange(new Range(s.getRange().getStart(), s.getRange().getEnd()
							.add(+1)));
				} else {
					s.getRingWidthData().set(y.diff(s.getRange().getStart()), newVal);
				}

				s.fireSampleDataChanged();
				if (grew)
					s.fireSampleRedated();
			}

			@Override
			public boolean canRedo() {
				return true;
			}

			@Override
			public String getPresentationName() {
				return I18n.getText("edit");
				// that's silly, that's the edit menu title -- well, it works...
			}
		});
	}

	public void setUnitMultiplier(Double unitMultiplier) {
		this.unitMultiplier = unitMultiplier;
	}

	public Double getUnitMultiplier() {
		return unitMultiplier;
	}
}
