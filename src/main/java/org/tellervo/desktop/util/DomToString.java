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

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Utility class takes a DOM element or document and turns it into a string
 * 
 * @author Lucas Madar
 *
 */

public class DomToString {
	private final Element domElement;
	private final boolean showXmlDeclaration;
	private final boolean pretty;

	public DomToString(Element domElement, boolean pretty) {
		this.domElement = domElement;
		this.showXmlDeclaration = false;
		this.pretty = pretty;
	}

	public DomToString(Document domDocument, boolean pretty) {
		this.domElement = domDocument.getDocumentElement();
		this.showXmlDeclaration = true;
		this.pretty = pretty;
	}
	
	/**
	 * Output the XML document to a string
	 */
	public String toString() {
		try {
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(domElement);
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			
			xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, showXmlDeclaration ? "no" : "yes");
			xformer.setOutputProperty(OutputKeys.INDENT, pretty ? "yes" : "no");
			
			xformer.transform(source, result);
			
			return sw.toString();
		} catch (Exception e) {
			return "<error: " + e.getMessage() + " >";
		}
	}
}
