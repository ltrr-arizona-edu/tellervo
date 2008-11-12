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

package edu.cornell.dendro.corina.gui;

import edu.cornell.dendro.corina.metadata.*;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

	private ElementList elements; // list of Elements
	private List<MetadataField> fields; // list of preview fields
	private Map<Element, BaseSample> elementMap;
	private int view; // ElementsPanel.currentView...

	// constructor -- fields to use are given; fields cannot be null:
	// pass an empty List if you don't want anything else.
	public ElementsTableModel(ElementList elements, Map<Element, BaseSample> elementMap,
			List<MetadataField> fields, int view) {
		this.elements = elements;
		this.elementMap = elementMap;
		this.fields = fields;
		this.view = view;
	}
	
	public boolean elementIsActive(Element e) {
		return elements.isActive(e);
	}

	public boolean elementIsBad(Element e) {
		return !elementMap.containsKey(e);
	}

	// this is a really bad place for this...
	// HEY: why can't i use a jcheckbox here?
	public static class FilenameRenderer extends JPanel implements
			TableCellRenderer {
		private JLabel label = new JLabel();
		private JCheckBox check = new JCheckBox();
		private JTable table;

		public FilenameRenderer(JTable table) {
			this.table = table;

			setLayout(new BorderLayout());

			label.setFont(table.getFont());

			label.setOpaque(true);
			check.setOpaque(true);

			add(check, BorderLayout.WEST);
			add(label, BorderLayout.CENTER);			
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Element e = (Element) value;

			check.setSelected(((ElementsTableModel) table.getModel()).elementIsActive(e));
			label.setText(e.getShortName());

			// REFACTOR: new UserFriendlyFile avoids this problem

			Color fore = (isSelected ? table.getSelectionForeground() : table
					.getForeground());
			Color back = (isSelected ? table.getSelectionBackground() : table
					.getBackground());

			if(((ElementsTableModel) table.getModel()).elementIsBad(e))
				fore = Color.red;
			/*
			 * // light-blue-ish if (row % 2 == 0) back = new
			 * Color(back.getRed() - 16, back.getGreen() - 16, back.getBlue());
			 */

			check.setForeground(fore);
			label.setForeground(fore);
			check.setBackground(back);
			label.setBackground(back);

			return this;
		}
	}

	// column name
	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Filename";
		case 1:
			return "Folder";
		case 2:
			return "Range";
		default:
			return ((MetadataField) fields.get(col - 3))
					.getFieldDescription();
		}
	}

	// row count
	public int getRowCount() {
		return (elements == null ? 0 : elements.size());
	}

	// column count
	public int getColumnCount() {
		// just filename and meta...?
		if(view == ElementsPanel.VIEW_FILENAMES_MINIMAL)
			return 1 + fields.size();
		// filename, folder, range + fields
		return 3 + fields.size();
	}

	// value of cell (row,col)
	public Object getValueAt(int row, int col) {
		// element to look at
		Element e = elements.get(row);

		switch (col) {
		case 0: 
			return e;

		case 1:
			if(e.getLoader() instanceof FileElement)
				return ((FileElement) e.getLoader()).getFolder();
			
			return null;

		case 2: {
			BaseSample bs = this.elementMap.get(e);
			return (bs != null) ? bs.getRange() : null;
		}

		default: {
			BaseSample bs = this.elementMap.get(e);

			// refresh failed -- is this a redundant test?
			if (bs == null)
				return null;

			String key = ((MetadataField) fields.get(col - 3)).getVariable();
			return bs.getMeta(key);
		}
		}
	}

	// is editable?
	@Override
	public boolean isCellEditable(int row, int col) {
		// get element
		Element e = elements.get(row);
		BaseSample bs = elementMap.get(e);
		
		// File Elements only!
		if(!(e.getLoader() instanceof FileElement))
			return false;
		
		// only after refresh?  no, assume refresh is always "done".  (threadme)

		return (col != 1 && col != 2); // everything except folder, and range
	}

	// column class
	@Override
	public Class getColumnClass(int col) {
		switch (col) {
		case 0:
			return Element.class; // meaning, the value is here; we render it in FilenameRenderer
		case 1:
		case 2:
			return String.class; // well, it's a range or a folder...
		default:
			return String.class; //  hrm.  well, assume it's a String...
		}
	}

	// set cell (row,col)
	@Override
	public void setValueAt(Object value, int row, int col) {
		// get element
		Element e = elements.get(row);
		BaseSample bs = elementMap.get(e);

		switch (col) {

		case 0: // filename + active-flag
			elements.setActive(e, ((Boolean) value).booleanValue());
			break;

		// case 1: ignore, range isn't editable

		default: // update a user-chosen metadata field
			// this is the key to update
			String key = ((MetadataField) fields.get(col - 2))
					.getVariable();

			// err...?
			if(bs == null)
				return;
		
			// null?  remove it.  (q: are there any cases where it's assumed key exists, like title?)
			if (value == null
					|| (value instanceof String && ((String) value).length() == 0)) {
				bs.removeMeta(key);
				value = null;
			} else {
				// try to squeeze the string into an integer
				try {
					value = new Integer(value.toString());
				} catch (NumberFormatException nfe) {
					// ... but don't care if you can't
				}

				// store it in the Element
				bs.setMeta(key, value);
			}

			// update the disk file -- do this in a background thread, or delayed?  see jwz.
			Sample s;
			try {
				s = e.load();
			} catch (IOException ioe) {
				Alert.error("I/O Error", "Error loading file: "
						+ ioe.getMessage());
				return;
			}

			if(value == null)
				s.removeMeta(key);
			else
				s.setMeta(key, value);

			try {
				s.getLoader().save(s);
			} catch (IOException ioe) {
				Alert.error("I/O Error", "Error saving file: "
						+ ioe.getMessage());
			}
		}

		fireTableCellUpdated(row, col);

		// this (maybe?) changes the sample, if this [list of
		// elements] is some sample's .elements -- so [this list of
		// elements] should also have a "Sample myMaster" member, so I
		// can say here "myMaster.setModified()"  (right?)
	}
}
