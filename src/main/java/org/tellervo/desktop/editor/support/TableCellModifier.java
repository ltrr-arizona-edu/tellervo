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
package org.tellervo.desktop.editor.support;

import java.awt.Component;

import javax.swing.JTable;

/**
 * An interface that allows changing an already existing component.
 * 
 * Used to do things like change background colors.
 * 
 * @author Lucas Madar
 */

public interface TableCellModifier {
	/** Modify the component */
	public void modifyComponent(Component c, JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column);
	
	/** Set a listener that wants to know when we've changed (notify to repaint) */
	public void setListener(TableCellModifierListener listener);
}
