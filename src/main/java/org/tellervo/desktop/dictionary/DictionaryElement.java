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
package org.tellervo.desktop.dictionary;

import org.jdom.Element;

public abstract class DictionaryElement {

	// what kind of dictionary element is this?
	public enum Type {
		Standardized,
		SECURITYUSER
	}
	
	/** The id, given by the server */
	protected String id;
	/** The value of this dictionary element */
	protected String value;
	
	public DictionaryElement(Type type, Element e) {
		if(type == Type.Standardized) {
			id = e.getAttributeValue("normalId");
			value = e.getAttributeValue("normal");
		}
		else if(type == Type.SECURITYUSER) {
			id = e.getAttributeValue("id");
			value = e.getAttributeValue("username");
		}
	}
	
	/**
	 * Do not use this constructor except in special cases (eg, when we can't find a user in the dictionary)
	 * @param id
	 * @param value
	 */
	public DictionaryElement(String id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
