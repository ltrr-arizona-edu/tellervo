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
import corina.Sample;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;

// BUG: NOT UNDOABLE!
// BUG: DOESN'T SET MODIFIED FLAG!

public class DecadalKeyListener extends KeyAdapter {

    private JTable _table;
    private Sample _sample;

    public DecadalKeyListener(JTable table, Sample sample) {
	_table = table;
	_sample = sample;
    }

    // not editing!
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
    private void selectYear(Year y) {
	// compute (row,col)
	int row = y.row() - _sample.range.getStart().row();
	int col = y.column()+1;

	// move selection
	_table.setRowSelectionInterval(row, row);
	_table.setColumnSelectionInterval(col, col);
    }

    // stop editing, and return the year that was being edited
    private Year stopEditing() {
	// where am i?
	int row = _table.getEditingRow();
	int col = _table.getEditingColumn();

	// stop editing
	TableCellEditor ed = _table.getCellEditor(row, col);
	((DefaultCellEditor) ed).stopCellEditing();

	// compute year
	return ((DecadalModel) _table.getModel()).getYear(row, col);
    }

    public void keyPressed(KeyEvent e) {
	Year y, target = null;

	// ignore control-keys ?
	// if (e.getModifiers() != 0) {
	// System.out.println("ignoring " + e);
	// return; // ???
	// }

	switch (e.getKeyCode()) {
	case KeyEvent.VK_TAB: // but shift-tab goes LEFT!
	    if (e.isShiftDown()) {
		y = (_table.isEditing() ? stopEditing() : getSelectedYear());
		target = y.add(-1);
		e.consume();
		break;
	    }
	    // fall-through to...
	case KeyEvent.VK_ENTER:
	case KeyEvent.VK_RIGHT:
	    y = (_table.isEditing() ? stopEditing() : getSelectedYear()); // this series is common, functionize it?
	    target = y.add(+1);
	    e.consume();
	    break;
	case KeyEvent.VK_LEFT:
	    y = (_table.isEditing() ? stopEditing() : getSelectedYear());
	    target = y.add(-1);
	    e.consume();
	    break;
	case KeyEvent.VK_UP:
	    y = (_table.isEditing() ? stopEditing() : getSelectedYear());
	    target = y.add(-10);
	    e.consume();
	    break;
	case KeyEvent.VK_DOWN:
	    y = (_table.isEditing() ? stopEditing() : getSelectedYear());
	    target = y.add(+10);
	    e.consume();
	    break;
	case KeyEvent.VK_HOME:
	    target = _sample.range.getStart();
	    e.consume();
	    break;
	case KeyEvent.VK_END:
	    target = _sample.range.getEnd();
	    e.consume();
	    break;
	}

	// move to target, if set
	if (target != null) {
	    if (target.compareTo(_sample.range.getStart()) < 0)
		target = _sample.range.getStart();
	    if (target.compareTo(_sample.range.getEnd().add(1)) > 0)
		target = _sample.range.getEnd().add(1);
	    selectYear(target);

	    // scroll to visible
	    int row = target.row() - _sample.range.getStart().row();
	    _table.scrollRectToVisible(_table.getCellRect(row, 0, true));
	}

	// [0-9] on keyboard/keypad -> start editing
	if (Character.isDigit(e.getKeyChar())) {
	    int row = _table.getSelectedRow();
	    int col = _table.getSelectedColumn();
	    if (!_table.isCellEditable(row, col))
		return;
	    _table.setValueAt("", row, col);
	    _table.editCellAt(row, col);
	    return;
	}

	// unknown -- ignore
    }

}
