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
package edu.cornell.dendro.corina.dictionary;

import org.jdom.Element;

@SuppressWarnings("unchecked")
public class Taxon extends DictionaryElement implements Comparable {
	public Taxon(Element e) {
		super(DictionaryElement.Type.Standardized, e);
	}

	public int compareTo(Object o) {
		if(o instanceof Taxon)
			return this.getValue().compareToIgnoreCase(((Taxon)o).getValue());
			
		return 0;
	}

}
