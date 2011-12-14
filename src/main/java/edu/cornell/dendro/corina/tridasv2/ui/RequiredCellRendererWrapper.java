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
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.ColorUtils;

/**
 * A wrapper around TableCellRenderer that highlights
 * table cells with invalid values
 * 
 * @author Lucas Madar
 *
 */

public class RequiredCellRendererWrapper implements TableCellRenderer {
	private TableCellRenderer renderer;
	
	public RequiredCellRendererWrapper(TableCellRenderer renderer) {	
		this.renderer = renderer;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(value == null || value.toString() == "") {
			/*if(isSelected) {

				
				
				
				Color blended = ColorUtils.blend(Color.red, c.getBackground());
				c.setBackground(blended);
			}
			else {
				c.setBackground(Color.red);
			}*/
			
        	JLabel label = new JLabel();
        	label.setIcon(Builder.getIcon("required.png", 16));
        	return label;
		}
		
				
		return c;
	}

}
