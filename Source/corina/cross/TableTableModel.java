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

import java.util.ResourceBundle;

public class TableTableModel extends AbstractTableModel {

    private Table t; // data

    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    public int getColumnCount() {
        return 6; // count(name, t, tr, d, n, dist)
                  // LATER: use any number of crosses (but table.java needs support first)
    }

    public int getRowCount() {
        return t.table.size();
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0: return msg.getString("title");
            case 1: return msg.getString("tscore");
            case 2: return msg.getString("trend");
            case 3: return msg.getString("dscore");
            case 4: return msg.getString("overlap");
            case 5: return msg.getString("distance");
            default: throw new IllegalArgumentException(); // never happens
        }
    }

    public Object getValueAt(int row, int col) {
        Table.Row r = (Table.Row) t.table.get(row);

        switch (col) {
            case 0: return r.title;
            case 1: return t.f1.format(r.cross.t);
            case 2: return t.f2.format(r.cross.tr);
            case 3: return t.f3.format(r.cross.d);
            case 4: return String.valueOf(r.cross.n);
            case 5: return r.cross.distanceAsString();
            default: throw new IllegalArgumentException(); // never happens
        }
    }

    public TableTableModel(Table t) {
        this.t = t;
    }
}
