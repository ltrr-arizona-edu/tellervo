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

import java.text.SimpleDateFormat;

import org.tridas.schema.Date;
import org.tridas.schema.DateTime;
import org.tridas.schema.Year;

/**
 * A renderer that shows Tridas Dates and DateTimes in a viewer friendly manner
 * 
 * @author Lucas Madar
 */

public class TridasYearDateTimeCellRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;
	private boolean showPrecision = false;
			
	public TridasYearDateTimeCellRenderer(boolean showPrecision)
	{
		setShowPrecision(showPrecision);
	}
	
	@Override
	protected String convertToString(Object value) {
		if(value instanceof Date) {
			Date dv = (Date) value;
			
			java.util.Date date = dv.getValue().toGregorianCalendar().getTime();
			String val = SimpleDateFormat.getDateInstance().format(date);
			if(dv.getCertainty() != null && showPrecision)
				val += " [" + dv.getCertainty().value() + "]";
			
			return val;
		}
		else if(value instanceof DateTime) {
			DateTime dv = (DateTime) value;
			
			java.util.Date date = dv.getValue().toGregorianCalendar().getTime();
			String val = SimpleDateFormat.getDateTimeInstance().format(date);
			if(dv.getCertainty() != null && showPrecision)
				val += " [" + dv.getCertainty().value() + "]";
			
			return val;			
		}
		else if(value instanceof Year) {
			Year year = (Year) value;
			
			String val = year.getValue().toString() + ' ' + year.getSuffix().value();
			if(year.getCertainty() != null && showPrecision)
				val += " [" + year.getCertainty().value() + "]";
			
			return val;
		}
		
		return null;
	}
	
	/**
	 * Set whether the Tridas precision should be displayed as well
	 * 
	 * @param b
	 */
	public void setShowPrecision(boolean b)
	{
		showPrecision = b;
	}
	
	public boolean isShowPrecisionEnabled()
	{
		return showPrecision;
	}
}
