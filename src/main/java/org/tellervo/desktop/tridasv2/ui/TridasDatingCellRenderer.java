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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jogamp.opengl.awt.Java2D;

import org.tridas.schema.Date;
import org.tridas.schema.TridasDating;

public class TridasDatingCellRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	public TridasDatingCellRenderer()
	{
		//super.setOddBackgroundColor(Color.WHITE);
		
	}
	
	@Override
	protected String convertToString(Object value) {
		
		
		if(value instanceof TridasDating) {
			TridasDating dating = (TridasDating) value;
			
			return dating.isSetType() ? dating.getType().value() : "Not present";
		}
		else if(value instanceof Date) {
			Date date = (Date) value;
			
			Calendar calendar = date.getValue().toGregorianCalendar();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			formatter.setTimeZone(calendar.getTimeZone());
			String dateString = formatter.format(calendar.getTime());
			
			return dateString;
		}
		else if(value instanceof java.util.Date) {
			java.util.Date date = (java.util.Date) value;
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format(date);
		
		}
		
		
		return super.convertToString(value);
	}
}
