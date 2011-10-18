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
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * @author Lucas Madar
 *
 */
public class EnumComboBoxItemRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public EnumComboBoxItemRenderer() {
		setHorizontalAlignment(DefaultListCellRenderer.LEFT);
		setVerticalAlignment(DefaultListCellRenderer.CENTER);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		modifyComponent(value);
		
		return this;
	}
	
	public void modifyComponent(Object value) {
		if(value == null) {
			setText("");
		} 
		else {			
			// try to invoke the class' value() to get a String value
			try {
				Method method = value.getClass().getMethod("value", (Class<?>[]) null);
				setText(method.invoke(value, new Object[] {}).toString());
			} catch (Exception e) {
				setText(value.toString());
			}
		}		
	}

}
