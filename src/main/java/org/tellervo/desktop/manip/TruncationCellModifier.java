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
package org.tellervo.desktop.manip;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.editor.DecadalModel;
import org.tellervo.desktop.editor.support.AbstractTableCellModifier;


public class TruncationCellModifier extends AbstractTableCellModifier {
	private Range range;
	
	public TruncationCellModifier(Range range) {
		this.range = range;
	}
	
	public void updateRange(Range range) {
		this.range = range;
		
		repaintParent();
	}

	public void modifyComponent(Component c, JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		DecadalModel model = (DecadalModel)table.getModel();
		Year year = model.getYear(row, column);		
		
		if(!range.contains(year))
			c.setBackground(Color.black);
	}

}
