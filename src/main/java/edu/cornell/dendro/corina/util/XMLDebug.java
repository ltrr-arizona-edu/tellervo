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
package edu.cornell.dendro.corina.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLDebug {
	
	public static void dumpDocument(Document d) {
		XMLOutputter xmlo = new XMLOutputter();
		xmlo.setFormat(Format.getPrettyFormat());
		
		try {
			xmlo.output(d, System.out);
		} catch(Exception e) {}
	}

	public static void dumpElement(Element d) {
		XMLOutputter xmlo = new XMLOutputter();
		xmlo.setFormat(Format.getPrettyFormat());
		
		try {
			xmlo.output(d, System.out);
		} catch(Exception e) {}
	}
}
