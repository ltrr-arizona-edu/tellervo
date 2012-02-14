/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.sample;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;

public class ElementFactory {
	/**
	 * @param filename 
	 * @return
	 */
	public static Element createElement(String filename, Class<? extends Element> clazz) {
		System.out.println("New element, filename: " + filename);
		
		// is this a URL? (e.g. xxxx://xxxxx?)
		if(filename.matches("^\\w+://\\S+$")) {
			URI uri;
			
			try {
				uri = new URI(filename);
			} catch (URISyntaxException use) {
				throw new UnsupportedOperationException(use);
			}
			
			// getScheme() has to return non-null, 
			// since we don't go here if no scheme was specified (see above regex)
			
			// a file?
			if(uri.getScheme().equals("file")) {
				File file = new File(uri);
				
				return spawnElement(clazz, new FileElement(file.getPath()));
			}
						
			throw new UnsupportedOperationException("No support for loading URL schema " + uri.toString());
			// a webpage?
			//if(uri.getScheme().equals("http")) {	
			//}
		}

		// No, just a file then.
		return spawnElement(clazz, new FileElement(filename));
	}
	
	public static Element createElement(String filename) {
		return createElement(filename, Element.class);
	}
	
	private static Element spawnElement(Class<? extends Element> clazz, SampleLoader loader) {
		try {
			Constructor<? extends Element> c = clazz.getConstructor(new Class[] { SampleLoader.class });
			return c.newInstance(new Object[] { loader });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
