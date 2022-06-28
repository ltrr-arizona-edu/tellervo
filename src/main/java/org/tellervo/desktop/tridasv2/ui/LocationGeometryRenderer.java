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

import java.text.NumberFormat;
import java.util.List;

import org.tridas.schema.TridasLocationGeometry;
import org.tridas.spatial.GMLPointSRSHandler;

/**
 * @author Lucas Madar
 *
 */
public class LocationGeometryRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object value) {
		TridasLocationGeometry geo = (TridasLocationGeometry) value;
		
		// only handle points, for now... 
		if(geo != null && geo.isSetPoint()) {
			// is the point useless? bail here
			if(!geo.getPoint().isSetPos() || !geo.getPoint().getPos().isSetValues())
				return null;
			
			GMLPointSRSHandler tph = new GMLPointSRSHandler(geo.getPoint());
			
			if(tph.getWGS84LatCoord()==null || tph.getWGS84LongCoord()==null) return null;
			
			NumberFormat format = NumberFormat.getInstance();
			// same as in LocationGeometryUI!
			format.setMinimumFractionDigits(3);
			format.setMaximumFractionDigits(5);

			// lat long
			return "Lat: "+format.format(tph.getWGS84LatCoord()) + " Lon: " + format.format(tph.getWGS84LongCoord());
		}
		
		return super.convertToString(value);
	}

}
