package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;

import javax.swing.ListCellRenderer;

/**
 * An interface that XXXComboBoxItemRenderers can implement
 * 
 * @author Lucas Madar
 */

public interface ComboBoxItemRenderer extends ListCellRenderer {
	/**
	 * Change the value of the component displayed
	 * 
	 * @param value The new value of the component (can be null)
	 */
	public void modifyComponent(Object value);
	
	public void setBackground(Color background);
	public void setForeground(Color foreground);
}
