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

import javax.swing.ListCellRenderer;

/**
 * An interface that XXXComboBoxItemRenderers can implement
 * 
 * @author Lucas Madar
 */

public interface ComboBoxItemRenderer {
	/**
	 * Change the value of the component displayed
	 * 
	 * @param value The new value of the component (can be null)
	 */
	public void modifyComponent(Object value);
	
	public void setBackground(Color background);
	public void setForeground(Color foreground);
}
