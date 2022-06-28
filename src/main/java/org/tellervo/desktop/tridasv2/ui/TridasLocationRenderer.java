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

import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasLocationGeometry;

/**
 * @author Lucas Madar
 *
 */
public class TridasLocationRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object value) {
		try{
			TridasLocation loc = (TridasLocation) value;
			
			if(loc.isSetLocationGeometry())
			{
				TridasLocationGeometry geo = loc.getLocationGeometry();
				if(geo != null && geo.isSetPoint()) {
					return "Coordinate location";
				}
				else if (geo != null && geo.isSetPolygon())
				{
					return "Polygon location";
				}
			}
			
		} catch (Exception e)
		{
			
		}
		
	
		
		return "";
	}

}
