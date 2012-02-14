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
package org.tellervo.desktop.tridasv2;

import java.net.URI;
import java.net.URISyntaxException;

import org.tridas.interfaces.ITridasGeneric;
import org.tridas.schema.TridasGenericField;

/**
 * Stupid wrapper class for Map Link
 * 
 * Why do we need this? Might be more convenient if we ever have different types of maps?
 * 
 * @author Lucas Madar
 */
public class MapLink {
	private URI mapURI;
	
	public MapLink(ITridasGeneric generic) {
		TridasGenericField field = GenericFieldUtils.findField(generic, "tellervo.mapLink");

		// no URI? no link!
		if(field == null) {
			mapURI = null;
			return;
		}

		// get the URI
		try {
			mapURI = new URI(field.getValue());
		} catch (URISyntaxException e) {
			System.err.println("Invalid map URI: " + field.getValue());
			mapURI = null;
		}
	}
	
	public URI getURI() {
		return mapURI;
	}
	
	public boolean hasLink() {
		return mapURI != null;
	}
}
