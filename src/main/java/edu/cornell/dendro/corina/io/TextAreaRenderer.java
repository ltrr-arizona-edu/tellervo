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
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class TextAreaRenderer extends JTextArea implements TableCellRenderer {
	
	  private Color grey = new Color(0.1f, 0.1f, 0.1f);
	  
	  public TextAreaRenderer() {
		    setLineWrap(true);
		    setWrapStyleWord(true);
		  }

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
		    setText((String) value);
		    
		    if(!isSelected)
		    {
			    if(row % 2 ==0)
				{
			    	
					this.setBackground(grey);
				}
			    else
			    {
					this.setBackground(Color.WHITE);
			    }
			}
		    else
		    {
		    	this.setBackground(table.getSelectionBackground());
		    }
	    
            int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
            int textLength = this.getText().length();
            int lines = 1;
            if(this.getColumns()!=0)
            {
            	lines = textLength / this.getColumns() +1;//+1, cause we need at least 1 row.
            }

            int height = fontHeight * lines;                        
            this.setPreferredSize(new Dimension(this.getWidth(), height));
            
            
			return this;
		}


}

