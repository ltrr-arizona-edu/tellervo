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
package org.tellervo.desktop.wsi.corina;

import org.tridas.schema.TridasIdentifier;

public class NewTridasIdentifier extends TridasIdentifier {
	private static final long serialVersionUID = 1L;
	
	private static final String newSeriesIdentifier = "newSeries";

	private NewTridasIdentifier(String domain) {
		this.domain = domain;
		this.value = newSeriesIdentifier;
	}
	
	/**
	 * @return a new tridas identifier with the given domain
	 */
	public static final TridasIdentifier getInstance(String domain) {
		return new NewTridasIdentifier(domain);
	}
	
	/**
	 * @return a new tridas identifier with the domain of the source identifier
	 */
	public static final TridasIdentifier getInstance(TridasIdentifier sourceIdentifier) {
		return getInstance(sourceIdentifier.getDomain());
	}

	/**
	 * Checks if this series is new
	 * 
	 * @param identifier
	 * @return
	 */
	public static final boolean isNew(final TridasIdentifier identifier) {
		return newSeriesIdentifier.equals(identifier.getValue());
	}
}
