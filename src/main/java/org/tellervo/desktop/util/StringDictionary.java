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
package org.tellervo.desktop.util;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A static hash of entity names, so we don't reuse a lot of memory
 * 
 * @author lucasm
 */

public class StringDictionary {
	private static Map<String, String> entities = new WeakHashMap<String, String>();
	
	public static String getEntityName(String entityName) {
		String value = entities.get(entityName);
		
		if(value != null)
			return value;

		// make a new string, because we'll reference it weakly!
		value = new String(entityName);
		entities.put(entityName, value);
		
		return value;
	}
}
