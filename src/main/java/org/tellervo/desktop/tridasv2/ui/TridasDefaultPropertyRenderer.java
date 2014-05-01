/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;


public class TridasDefaultPropertyRenderer extends DefaultCellRendererEx  {
	private static final long serialVersionUID = 1L;

	private Font font;
	
	@Override
	protected String convertToString(Object value) {
		if(value == null)
			return "";
		else
			return "Click to remove...";		
	}

	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		// blank out the value if we're null...
		if (table instanceof TellervoPropertySheetTable && 
				!((TellervoPropertySheetTable)table).isCellEditable(row, column))
			value = null;
		
		// if we're not selected, be inconspicuous
		if(!isSelected)
			setForeground(Color.GRAY.brighter());
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// make the font italic
		if(font == null)
			font = c.getFont().deriveFont(Font.ITALIC);		
		c.setFont(font);
		
		return c;
	}
}
