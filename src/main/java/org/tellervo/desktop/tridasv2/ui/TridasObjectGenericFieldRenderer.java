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

import java.util.ArrayList;

import org.tridas.schema.TridasGenericField;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * renderer for a tridas controlled vocabulary
 * 
 * Contains a dictionary that specifies specific behaviors
 * for different enums, based on qname
 * 
 * @author Lucas Madar
 */
public class TridasObjectGenericFieldRenderer extends DefaultCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertToString(Object value) {
		
		
		if(value instanceof ArrayList)
		{				
			for(Object v : (ArrayList<Object>) value)
			{
				if(v instanceof TridasGenericField)
				{
					if(((TridasGenericField)v).getName().equals("tellervo.objectLabCode"))
					{
						return ((TridasGenericField)v).getValue();
					}
				}
			}
		}
		
		
		return super.convertToString(value);
	}
}
