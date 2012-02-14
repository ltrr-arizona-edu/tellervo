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
package edu.cornell.dendro.corina.io;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class RowShadingRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
	
	private static final long serialVersionUID = -5231781208719739960L;
	private Color grey = new Color(0.9f, 0.9f, 0.9f);
	  
	  public RowShadingRenderer() {

		  }

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
		      
			Component cell = super.getTableCellRendererComponent(
                      table, value, isSelected, hasFocus, row, column);
		    
		    if(!isSelected)
		    {
			    if(row % 2 ==0)
				{
					cell.setBackground(Color.WHITE);
				}
			    else
			    {
					cell.setBackground(grey);
			    }
			}
		    else
		    {
		    	cell.setBackground(table.getSelectionBackground());
		    }
	 
			return cell;
		}


}

