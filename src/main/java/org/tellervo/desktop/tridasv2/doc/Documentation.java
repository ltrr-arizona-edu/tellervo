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
package org.tellervo.desktop.tridasv2.doc;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * @author lucasm
 *
 */
public class Documentation {
	private Documentation() {
		// don't instantiate me!
	}
		
	/**
	 * Retrieves documentation for a hierarchy object, max 3 depth at the end
	 * For instance, if we want documentation for:
	 *    object.a.b.c.d.e.f
	 * We will look for documentation for:
	 *    d.e.f
	 *    e.f
	 *    f
	 * And then give up.
	 * 
	 * @param key
	 * @return The localized documentation, or null if nothing was found
	 */
	public static String getDocumentation(String key) {
		String[] keys = key.split("\\.");
		
		int maxdepth = Math.min(3, keys.length);
		for(int i = maxdepth - 1; i >= 0; i--) {
			//String thisKey = StringUtils.join(keys, '.', keys.length - (i+1), keys.length);
			String thisKey = StringUtils.join(keys);
			
			try {
				return msg.getString(thisKey);
			} catch (MissingResourceException mre) {
				// continue...
			}
		}
		
		return null;
	}
	
    // the resource bundle to use
    private final static ResourceBundle msg;
    
    static {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle("org.tellervo.desktop.tridasv2/doc/DocsBundle");
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
			bundle = new ResourceBundle() {
				
				protected Object handleGetObject(String key) {
					return key;
				}
				
				public Enumeration<String> getKeys() {
					return EMPTY_ENUMERATION;
				}
				
				private final Enumeration<String> EMPTY_ENUMERATION = new Enumeration<String>() {
					public boolean hasMoreElements() {
						return false;
					}
					
					public String nextElement() {
						throw new NoSuchElementException();
					}
				};
			};
		}
		msg = bundle;
	}
}
