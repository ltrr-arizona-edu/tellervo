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
package org.tellervo.desktop.tridasv2;

import java.util.List;

import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasValues;

/**
 * Lazy way to clone an object hierarchy that's JAXB-defined
 * 
 * @author Lucas Madar
 */

public class TridasCloner {
	@SuppressWarnings("unchecked")
	public static <T extends CopyTo> T clone(T o, Class<? extends T> argClass) {
		Object copy;
		try {
			copy = argClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//Object copy = o.createCopy();
		o.copyTo(copy);
		
		return (T) copy;
	}
	
	/**
	 * Copy a series, but make the values just a reference
	 * @param series
	 * @return A copy of the series with values as reference
	 */
	public static ITridasSeries cloneSeriesRefValues(ITridasSeries series, Class<? extends ITridasSeries> seriesClass) {
		// safety check
		if(series == null)
			return null;
		
		List<TridasValues> values = series.isSetValues() ? series.getValues() : null;
		
		// remove values from the series temporarily
		series.unsetValues();
	
		// copy the series
		ITridasSeries copy;
		try {
			copy = seriesClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//ITridasSeries copy = (ITridasSeries)(((Copyable) series).createCopy());
		
		((CopyTo)series).copyTo(copy);
		
		// re-set the values (and reference them, too)
		series.setValues(values);
		copy.setValues(values);
		
		return copy;
	}
		
	/*
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		if(o == null)
			return null;
		
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(buf);
		
			out.writeObject(o);
			
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
			
			return (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	*/
}
