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

package corina.cross;

import javax.swing.table.AbstractTableModel;

public class TableTableModel extends AbstractTableModel {

    private Table t; // data

    public int getColumnCount() {
	return 6; // count(name, t, tr, d, n, dist)
    }

    public int getRowCount() {
	if (t == null) // delete me later ... this should never happen
	    return 0;

	return t.table.size();
    }

    public String getColumnName(int col) {
	switch (col) {
	case 0: return "Title";
	case 1: return "T-Score";
	case 2: return "Trend";
	case 3: return "D-Score";
	case 4: return "Overlap";
	case 5: return "Distance";
	default: return null;
	}
    }

    public Object getValueAt(int row, int col) {
	if (t == null) // delete me later ... this should never happen
	    return null;

	Table.Row r = (Table.Row) t.table.get(row);

	switch (col) {
	case 0: return r.title;
	case 1: return t.f1.format(r.t);
	case 2: return t.f2.format(r.tr);
	case 3: return t.f3.format(r.d);
	case 4: return String.valueOf(r.overlap);
	case 5: return ((r.dist == null) ? null : (r.dist + " km"));
	default: return null;
	}
    }

    public TableTableModel(Table t) {
	this.t = t;
    }

}
