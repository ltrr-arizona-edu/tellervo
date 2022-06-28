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
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.tellervo.desktop.ui.Builder;


/**
 * @author Lucas Madar
 *
 */
public class BooleanCellRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	private boolean useImages = false;
	private boolean blankForFalse = false;
	private Icon trueicon = Builder.getIcon("apply.png", 16);
	private Icon falseicon = Builder.getIcon("cancel.png", 16);
	
	public BooleanCellRenderer(boolean useImages)
	{
		this.useImages = useImages;
		if(useImages) blankForFalse = true;
	}
	
	public BooleanCellRenderer()
	{
		
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
			Component item = super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column);

			/*if (showOddAndEvenRows && !isSelected) {
			  if (row % 2 == 0) {
			    setBackground(oddBackgroundColor);
			  } else {
			    setBackground(evenBackgroundColor);
			  }
			}*/
			
			if(useImages)
			{
				if(value instanceof Boolean)
				{
				}
				else
				{
					return item; 
				}
				
				Boolean b = (Boolean) value;
				
				JLabel label = new JLabel();
				label.setHorizontalAlignment( JLabel.CENTER );
				if(b==null)
				{
					if(blankForFalse)
					{
						
					}
					else
					{
						label.setIcon(falseicon);
					}
				}
				else if(b== true)
				{
					
					label.setIcon(trueicon);
					
				}
				else
				{
					if(blankForFalse)
					{
						
					}
					else
					{
						label.setIcon(falseicon);
					}
					 
				}
				
				label.setBackground(item.getBackground());
				label.setForeground(item.getForeground());
				return label;
			}
			
	   
	    
			return this;
		}
	
	@Override
	public String toString()
	{
		return null;
	}

	public boolean isBlankForFalse() {
		return blankForFalse;
	}

	public void setBlankForFalse(boolean blankForFalse) {
		this.blankForFalse = blankForFalse;
	}
	
}
