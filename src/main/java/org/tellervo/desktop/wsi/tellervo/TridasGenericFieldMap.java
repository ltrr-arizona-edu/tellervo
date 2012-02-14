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
package org.tellervo.desktop.wsi.tellervo;

import java.util.HashMap;
import java.util.List;

import org.tridas.schema.TridasGenericField;

/**
 * Class to simplify lookup of generic fields by key
 * Values are String, Integer, or Boolean.
 */

public class TridasGenericFieldMap extends HashMap<String, Object>{
	private static final long serialVersionUID = 5965496844604631100L;

	public TridasGenericFieldMap(List<TridasGenericField> genericFields) {
		super(genericFields.size());
		
		for(TridasGenericField g : genericFields) {
			if("integer".equals(g.getType())) {
				try {
					put(g.getName(), Integer.valueOf(g.getValue()));
				} catch (NumberFormatException nfe) {
					// just ignore an invalid number!
				}
			}
			else if("boolean".equals(g.getType())) {
				put(g.getName(), Boolean.valueOf(g.getValue()));
			}
			else 
				put(g.getName(), g.getValue());
		}
	}

	public Integer getInteger(String key, Integer defaultValue) {
		Integer ret = getInteger(key);
		
		return (ret != null) ? ret : defaultValue;
	}
	
	public Integer getInteger(String key) {
		Object o = get(key);

		if(o == null)
			return null;

		if(Integer.class.isInstance(o))
			return (Integer) o;
		else try {
			return Integer.parseInt(o.toString());
		} catch (NumberFormatException nfe) {
			return null;
		}
	}
	
	public Boolean getBoolean(String key) {
		Object o = get(key);

		if(o == null)
			return null;
		
		if(Boolean.class.isInstance(o))
			return (Boolean) o;
		else 
			return Boolean.valueOf(o.toString());
	}
	
	public String getString(String key) {
		Object o = get(key);
		
		if(o == null)
			return null;

		return o.toString();
	}

}
