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

package corina.gui;

import corina.MetadataTemplate;
import corina.Element;
import corina.Sample;
import corina.ui.Alert;

import java.io.IOException;
import java.io.File;

import java.util.List;
import java.util.ArrayList;

import java.awt.Component;
import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.AbstractTableModel;

public class ElementsTableModel extends AbstractTableModel {

    private List elements; // list of Elements

    private List fields; // list of preview fields

    // constructor -- fields to use are given; fields cannot be null:
    // pass an empty List if you don't want anything else.
    public ElementsTableModel(List elements, List fields) {
	this.elements = elements;
	this.fields = fields;
    }

    // this is a really bad place for this...
    // HEY: why can't i use a jcheckbox here?
    public static class FilenameRenderer extends JPanel implements TableCellRenderer {
	private JLabel label = new JLabel();
	private JCheckBox check = new JCheckBox();
	public FilenameRenderer(JTable table) {
	    setLayout(new BorderLayout());

	    label.setFont(table.getFont());

	    label.setOpaque(true);
	    check.setOpaque(true);

	    add(check, BorderLayout.WEST);
	    add(label, BorderLayout.CENTER);
	}
	public Component getTableCellRendererComponent(JTable table, Object value,
						       boolean isSelected, boolean hasFocus,
						       int row, int column) {
	    Element e = (Element) value;
	    check.setSelected(e.isActive());
	    label.setText(new File(e.getFilename()).getName()); // filename only (not fq)
                                                         // REFACTOR: new UserFriendlyFile avoids this problem

	    Color fore = (isSelected ? table.getSelectionForeground() : table.getForeground());
	    Color back = (isSelected ? table.getSelectionBackground() : table.getBackground());

	    /*
	    // light-blue-ish
	    if (row % 2 == 0)
		back = new Color(back.getRed() - 16,
				 back.getGreen() - 16,
				 back.getBlue());
	    */

	    check.setForeground(fore);
	    label.setForeground(fore);
	    check.setBackground(back);
	    label.setBackground(back);

	    return this;
	}
    }

    // column name
    public String getColumnName(int col) {
	switch (col) {
	case 0: return "Filename";
	case 1: return "Range";
	default:
	    return ((MetadataTemplate.Field) fields.get(col-2)).getDescription();
	}
    }

    // row count
    public int getRowCount() {
	return (elements == null ? 0 : elements.size());
    }

    // column count
    public int getColumnCount() {
	// for now: range data iff other data being shown
	return 1 + (fields.size()==0 ? 0 : 1) + fields.size();
    }

    // value of cell (row,col)
    public Object getValueAt(int row, int col) {
	// element to look at
	Element e = (Element) elements.get(row);

	switch (col) {
	case 0:
	    return e;
	case 1:
	    return e.getRange(); // (lazy-loads)
	default:
	    // might need more metadata
	    if (e.details == null)
		try {
		    e.loadMeta();
		} catch (IOException ioe) {
		    // System.out.println("ERROR: " + e + " won't refresh");
		}

	    // refresh failed -- is this a redundant test?
	    if (e.details == null)
		return null;

	    String key = ((MetadataTemplate.Field) fields.get(col-2)).getVariable();
	    return e.details.get(key);
	}
    }

    // is editable?
    public boolean isCellEditable(int row, int col) {
	// only after refresh?  no, assume refresh is always "done".  (threadme)

	return (col != 1); // everything except range
    }

    // column class
    public Class getColumnClass(int col) {
        switch (col) {
	case 0: return Element.class; // ???
	case 1: return String.class; // well, it's a range...
	default: return String.class; //  hrm.  well, assume it's a String...
        }
    }

    // set cell (row,col)
    public void setValueAt(Object value, int row, int col) {
	// get element
	Element e = (Element) elements.get(row);

	switch (col) {

	case 0: // filename + active-flag
	    e.active = ((Boolean) value).booleanValue();
	    break;

	// case 1: ignore, range isn't editable

	default: // update a user-chosen metadata field
	    // this is the key to update
	    String key = ((MetadataTemplate.Field) fields.get(col-2)).getVariable();

	    // null?  remove it.  (q: are there any cases where it's assumed key exists, like title?)
	    if (value == null || value == "") {
		e.details.remove(key);
	    } else {
		// try to squeeze the string into an integer
		try {
		    value = new Integer(value.toString());
		} catch (NumberFormatException nfe) {
		    // ... but don't care if you can't
		}

		// store it in the Element
		e.details.put(key, value);
	    }

	    // update the disk file -- do this in a background thread, or delayed?  see jwz.
	    Sample s;
	    try {
		s = e.load();
	    } catch (IOException ioe) {
		Alert.error("I/O Error", "Error loading file: " + ioe.getMessage());
		return;
	    }

	    s.meta.put(key, value);

	    try {
		s.save();
	    } catch (IOException ioe) {
		Alert.error("I/O Error", "Error saving file: " + ioe.getMessage());
	    }
	}

	fireTableCellUpdated(row, col);

	// this (maybe?) changes the sample, if this [list of
	// elements] is some sample's .elements -- so [this list of
	// elements] should also have a "Sample myMaster" member, so I
	// can say here "myMaster.setModified()"  (right?)
    }
}
