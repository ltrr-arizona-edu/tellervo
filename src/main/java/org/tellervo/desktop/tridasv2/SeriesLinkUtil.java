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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.SeriesLinks;
import org.tridas.schema.TridasIdentifier;

/**
 * @author Lucas Madar
 *
 * Small holder class to make an easier SeriesLink
 * Also, could contain utility methods
 */
public final class SeriesLinkUtil {
	private static final long serialVersionUID = 1L;
	
	/** Don't instantiate me! */
	private SeriesLinkUtil() {
	}
	
	/**
	 * Create a new SeriesLink for this identifier
	 * @param identifier
	 * @return
	 */
	public static SeriesLink forIdentifier(TridasIdentifier identifier) {
		SeriesLink link = new SeriesLink();
		
		link.setIdentifier(identifier);
		
		return link;
	}
	
	/**
	 * Get a list of TridasIdentifiers, given a list of seriesLinks
	 * @param input
	 * @return a populated list of TridasIdentifiers
	 */
	public static List<TridasIdentifier> getIdentifiers(SeriesLinks input) {
		// shortcut for empty...
		if(input == null || !input.isSetSeries())
			return Collections.emptyList();
		
		ArrayList<TridasIdentifier> output = new ArrayList<TridasIdentifier>();
		
		for(SeriesLink link : input.getSeries()) {
			if(link.isSetIdentifier())
				output.add(link.getIdentifier());
		}
		
		return output;
	}	
	
	/**
	 * Adds the given SeriesLink to the series
	 * Creates a LinkSeries if necessary
	 * @param series
	 * @param link
	 */
	public static void addToSeries(ITridasDerivedSeries series, SeriesLink link) {
		SeriesLinks links = series.getLinkSeries();
		
		// doesn't exist? create it
		if(links == null) {
			links = new SeriesLinks();
			series.setLinkSeries(links);
		}
		
		// add the link
		links.getSeries().add(link);
	}
	
	/**
	 * Add the given identifier as a linked series on the given series
	 * 
	 * @param series
	 * @param identifier
	 */
	public static void addToSeries(ITridasDerivedSeries series, TridasIdentifier identifier) {
		addToSeries(series, forIdentifier(identifier));
	}
}
