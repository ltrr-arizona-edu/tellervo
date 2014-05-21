/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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


package org.tellervo.desktop.editor;


import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.remarks.Remark;
import org.tellervo.desktop.remarks.RemarkPanel;
import org.tellervo.desktop.remarks.Remarks;
import org.tellervo.desktop.sample.Sample;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasValue;

// BUG: NOT UNDOABLE!
// BUG: DOESN'T SET MODIFIED FLAG!

public class DecadalKeyListener extends KeyAdapter {
	private final static Logger log = LoggerFactory.getLogger(DecadalKeyListener.class);

	private JTable _table;

	private Sample _sample;

	public DecadalKeyListener(JTable table, Sample sample) {
		_table = table;
		_sample = sample;
	}

	// (not editing year!)
	private Year getSelectedYear() {
		int row, col;
		if (_table.isEditing()) {
			row = _table.getEditingRow();
			col = _table.getEditingColumn();
		} else {
			row = _table.getSelectedRow();
			col = _table.getSelectedColumn();
		}
		return ((DecadalModel) _table.getModel()).getYear(row, col);
	}
	
	private ArrayList<Year> getSelectedYears(){
		
		ArrayList<Year> list = new ArrayList<Year>();
		
		for(int col :_table.getSelectedColumns())
		{
			for(int row : _table.getSelectedRows())
			{
				list.add(((DecadalModel) _table.getModel()).getYear(row, col));
			}
		}
		
		return list;
		
	}
	

	private void selectYear(Year y) {
		// compute (row,col)
		int row = y.row() - _sample.getRange().getStart().row();
		int col = y.column() + 1;

		// move selection
		_table.setRowSelectionInterval(row, row);
		_table.setColumnSelectionInterval(col, col);
	}

	private Year cancelEditing()
	{
		// where am i?
		int row = _table.getEditingRow();
		int col = _table.getEditingColumn();

		// stop editing
		TableCellEditor ed = _table.getCellEditor(row, col);
		((TableCellEditor) ed).cancelCellEditing();

		// compute year
		return ((DecadalModel) _table.getModel()).getYear(row, col);
	}
	
	// stop editing, and return the year that was being edited
	private Year stopEditing() {
		// where am i?
		int row = _table.getEditingRow();
		int col = _table.getEditingColumn();

		// stop editing
		TableCellEditor ed = _table.getCellEditor(row, col);
		((TableCellEditor) ed).stopCellEditing();

		// compute year
		return ((DecadalModel) _table.getModel()).getYear(row, col);
	}

	// this fixes a bug where menu shortcuts get inserted into the table..
	// this an obnoxious java mnemonic: pressed events are consumed by the
	// menu accelerator, typed events are not.
	@Override
	public void keyTyped(KeyEvent e) {
		// just EAT any modifiers that aren't shift
		int notshiftdown = ~InputEvent.SHIFT_DOWN_MASK;
		if(!((e.getModifiers() & notshiftdown) == 0)) {
			e.consume();
			return;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Year y, target = null;
		Boolean eventHandled = false;
		

		
		// just ignore any modifiers that aren't shift		
		if(e.isControlDown() || e.isAltDown()) { e.consume(); return;}
				
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			log.debug("Cancel edits to table");
			y = cancelEditing();
			e.consume();
			eventHandled = true;
			break;
		case KeyEvent.VK_TAB: // but shift-tab goes LEFT!
			if (e.isShiftDown()) {
				y = (_table.isEditing() ? stopEditing() : getSelectedYear());
				target = y.add(-1);
				e.consume();
				eventHandled = true;
				break;
			}
			// fall-through to...
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_RIGHT:
			y = (_table.isEditing() ? stopEditing() : getSelectedYear()); // this series is common, functionize it?
			target = y.add(+1);
			e.consume();
			eventHandled = true;
			break;
		case KeyEvent.VK_LEFT:
			y = (_table.isEditing() ? stopEditing() : getSelectedYear());
			target = y.add(-1);
			e.consume();
			eventHandled = true;
			break;
		case KeyEvent.VK_UP:
			y = (_table.isEditing() ? stopEditing() : getSelectedYear());
			target = y.add(-10);
			e.consume();
			eventHandled = true;
			break;
		case KeyEvent.VK_DOWN:
			y = (_table.isEditing() ? stopEditing() : getSelectedYear());
			target = y.add(+10);
			e.consume();
			eventHandled = true;
			break;
		case KeyEvent.VK_HOME:
			target = _sample.getRange().getStart();
			e.consume();
			eventHandled = true;
			break;
		case KeyEvent.VK_END:
			target = _sample.getRange().getEnd();
			e.consume();
			eventHandled = true;
			break;
				
		case KeyEvent.VK_BACK_SPACE:
			log.debug("Backspace pressed");
			int rw = _table.getSelectedRow();
			int c = _table.getSelectedColumn();
			if (!_table.isCellEditable(rw, c))
				return;
			_table.editCellAt(rw, c);			
			return;
		case KeyEvent.VK_1:
		case KeyEvent.VK_2:
		case KeyEvent.VK_3:
		case KeyEvent.VK_4:
		case KeyEvent.VK_5:
		case KeyEvent.VK_6:
		case KeyEvent.VK_7:
		case KeyEvent.VK_8:
		case KeyEvent.VK_9:
		case KeyEvent.VK_0:
			log.debug("Digit pressed: "+e.getKeyChar());
			if (!_table.isCellEditable(_table.getSelectedRow(), _table.getSelectedColumn()))
				return;
			
			log.debug("Is Table editing? " + _table.isEditing());
			
			if(!_table.isEditing())
			{
			
			
				try{
					_table.getModel().setValueAt(e.getKeyChar(), _table.getSelectedRow(), _table.getSelectedColumn());
				} catch (Exception ex)
				{
					log.debug("Failed to set value of table model");
				}
				_table.editCellAt(_table.getEditingRow(), _table.getEditingColumn());

				//e.consume();
			}
			


			break;
			
			
		case KeyEvent.VK_U:		
		case KeyEvent.VK_D:
		case KeyEvent.VK_E:
		case KeyEvent.VK_L:
		case KeyEvent.VK_A:
		case KeyEvent.VK_M:
		case KeyEvent.VK_X:
			// FHX characters
			log.debug("FHX Remark key pressed");
			log.debug("Shift = "+e.isShiftDown());
			List<Remark> remarks = Remarks.getRemarks();
			
			for(Year yr : getSelectedYears())
			{
				TridasValue val;
				try{
					val = _sample.getRingWidthValueForYear(yr);				
				} catch (IndexOutOfBoundsException ex )
				{
					return;
				}
				
				for(Remark r : remarks)
				{
					if(
						((r.getDisplayName().equals("Fire scar in latewood") && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_A))            ||
					    ((r.getDisplayName().equals("Fire injury in latewood") && !e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_A))         ||
					    ((r.getDisplayName().equals("Fire scar in dormant position") && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_D))    ||
					    ((r.getDisplayName().equals("Fire injury in dormant position") && !e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_D)) || 
					    ((r.getDisplayName().equals("Fire scar in first third of earlywood") && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_E))    ||
					    ((r.getDisplayName().equals("Fire injury in first third of earlywood") && !e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_E)) || 
					    ((r.getDisplayName().equals("Fire scar in middle third of earlywood") && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_M))    ||
					    ((r.getDisplayName().equals("Fire injury in middle third of earlywood") && !e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_M)) || 
					    ((r.getDisplayName().equals("Fire scar in last third of earlywood") && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_L))    ||
					    ((r.getDisplayName().equals("Fire injury in last third of earlywood") && !e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_L)) || 
					    ((r.getDisplayName().equals("Fire scar - position undetermined") && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_U))    ||
					    ((r.getDisplayName().equals("Fire injury - position undetermined") && !e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_U)) ||
					    ((r.getDisplayName().equals("Not recording fires") && e.getKeyCode()==KeyEvent.VK_X)) 
					   ) 
					{
						Boolean alreadyMarked = false;
						for(TridasRemark currRem : val.getRemarks())
						{
							
							if(currRem.getNormal().equals(r.getDisplayName()))
							{
								alreadyMarked = true;
							}
						}
						
						if(alreadyMarked)
						{
							log.debug("FHX Remark removed");
							r.removeRemark(val);
						}
						else
						{
							log.debug("FHX Remark applied");
							r.applyRemark(val);
						}
						
						
					}
					
				}			
				int row;
				int col;
				if (_table.isEditing()) {
					row = _table.getEditingRow();
					col = _table.getEditingColumn();
				} else {
					row = _table.getSelectedRow();
					col = _table.getSelectedColumn();
				}
				((UnitAwareDecadalModel)_table.getModel()).fireTableCellUpdated(row, col);
			}
			
			
			e.consume();
			_table.repaint();
			
			if(getSelectedYears().size()>1)
			{
				return;
			}
			else if (getSelectedYears().size()==1)
			{
				y = (_table.isEditing() ? stopEditing() : getSelectedYear()); 
				target = y.add(+1);
				eventHandled = true;
			}
					
		
		}		
		
		// Fudge to cope with e.consume() not stopping alpha chars being entered into matrix
		if(eventHandled==false && (!Character.isDigit(e.getKeyChar())))
		{
			e.consume();
			return;
		}
		
		// move to target, if set
		if (target != null) {
			if (target.compareTo(_sample.getRange().getStart()) < 0)
				target = _sample.getRange().getStart();
			if (target.compareTo(_sample.getRange().getEnd().add(1)) > 0)
				target = _sample.getRange().getEnd().add(1);
			selectYear(target);

			// scroll to visible
			int row = target.row() - _sample.getRange().getStart().row();
			_table.scrollRectToVisible(_table.getCellRect(row, 0, true));
		}

		// [0-9] on keyboard/keypad -> start editing
		/*if (Character.isDigit(e.getKeyChar())) {
			int row = _table.getSelectedRow();
			int col = _table.getSelectedColumn();
			if (!_table.isCellEditable(row, col))
				return;
			
			//_table.setValueAt("", row, col);
			_table.editCellAt(row, col);
			
			return;
		}*/
		
		
		// unknown -- ignore
	}

}
