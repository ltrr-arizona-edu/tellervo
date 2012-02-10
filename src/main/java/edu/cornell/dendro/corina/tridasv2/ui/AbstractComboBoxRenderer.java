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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.tridas.schema.ControlledVoc;

import edu.cornell.dendro.corina.tridasv2.ui.support.NotPresent;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.ColorUtils;

public abstract class AbstractComboBoxRenderer extends JPanel implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * @return The renderer to use to render cells
	 */
	abstract public ComboBoxItemRenderer getRenderer();
	
	/**
	 * @return the drop down box
	 */
	abstract public JComponent getDropdownBox();
	
	/**
	 * @return true if this is a required attribute
	 */
	abstract public boolean isRequired();
	
	/**
	 * Get the table cell renderer for this attribute
	 */
	public final Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// get the renderer
		ComboBoxItemRenderer renderer = getRenderer();
		JComponent dropdown = getDropdownBox();
		
		dropdown.setVisible(table.isCellEditable(row, column));

		renderer.modifyComponent(value);
		
        if (isSelected) {
            renderer.setForeground(table.getSelectionForeground());
            setForeground(table.getSelectionForeground());
            
            // highlight bad values
            if(value instanceof ControlledVoc && value != null)
            {
            	JLabel label = new JLabel();
            	ControlledVoc val = (ControlledVoc) value;
            	
            	if(val.isSetNormalStd())
            	{
            		
            		if(!val.getNormalStd().equals("Corina") &&  
            		   !val.getNormalStd().equals("Catalogue of Life Annual Checklist 2008"))
            		{
                    	Color blend = ColorUtils.blend(table.getSelectionBackground(), Color.red);
                    	renderer.setBackground(blend);
                    	super.setBackground(blend);          	
          
                    	label.setIcon(Builder.getIcon("missingicon.png", 16));
                    	
            		}
                	
            		if(val.isSetNormal())
                	{
                		label.setText(val.getNormal());
                	}
                	else
                	{
                		label.setText(val.getValue());
                	}
            	}
            	return label;
            }
            else if(isRequired() && (value == null || value instanceof NotPresent)) {
            	Color blend = ColorUtils.blend(table.getSelectionBackground(), Color.red);
            	renderer.setBackground(blend);
            	super.setBackground(blend);          	
            	JLabel label = new JLabel();
            	label.setIcon(Builder.getIcon("required.png", 16));
            	return label;
            }
            else {
            	renderer.setBackground(table.getSelectionBackground());
            	super.setBackground(table.getSelectionBackground());
            }
        } else {
            setForeground(table.getForeground());
            renderer.setForeground(table.getForeground());

            // highlight bad values
            if(value instanceof ControlledVoc && value != null)
            {
            	JLabel label = new JLabel();
            	ControlledVoc val = (ControlledVoc) value;
            	
            	if(val.isSetNormalStd())
            	{
            		
            		if(!val.getNormalStd().equals("Corina") && 
            		   !val.getNormalStd().equals("Catalogue of Life Annual Checklist 2008"))
            		{
                    	Color blend = ColorUtils.blend(table.getSelectionBackground(), Color.red);
                    	renderer.setBackground(blend);
                    	super.setBackground(blend);          	
          
                    	label.setIcon(Builder.getIcon("missingicon.png", 16));
                    	
            		}
                	
            		if(val.isSetNormal())
                	{
                		label.setText(val.getNormal());
                	}
                	else
                	{
                		label.setText(val.getValue());
                	}
            	}
            	return label;
            }
            else if(isRequired() && (value == null || value instanceof NotPresent)) {
            	renderer.setBackground(Color.red);
            	setBackground(Color.red);
            	JLabel label = new JLabel();
            	label.setIcon(Builder.getIcon("required.png", 16));
            	return label;
            }
            else {
            	setBackground(table.getBackground());
            	renderer.setBackground(table.getBackground());
            }
        }

        return this;
	}
}
