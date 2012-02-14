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
package edu.cornell.dendro.corina.sample;

import org.tridas.schema.Certainty;
import org.tridas.schema.TridasWoodCompleteness;

public interface CorinaMetadata {

	public abstract TridasWoodCompleteness getWoodCompleteness();

	/**
	 * Does this series have sapwood?
	 * @return true if sapwood.presence is COMPLETE or INCOMPLETE, false otherwise
	 */
	public abstract boolean hasSapwood();

	/**
	 * 
	 * @return The number of sapwood rings, or 0 if there are none/not present
	 */
	public abstract Integer getNumberOfSapwoodRings();

	/**
	 * Get the dating certainty of this series
	 * 
	 * @return The certainty, or UNKNOWN if it wasn't set
	 */
	public abstract Certainty getDatingCertainty();

	/**
	 * Get the series name
	 * 
	 * @return The name of the series, or something representative
	 */
	public abstract String getName();

	/**
	 * Get the site code
	 * 
	 * @return the site code, or null if none found
	 */
	public abstract String getSiteCode();
	
	/**
	 * Is there a site code?
	 * 
	 * @return true if a site code exists
	 */
	public abstract boolean hasSiteCode();
	
	/**
	 * Get the taxonomy of this thing
	 * 
	 * @return the taxon, or null
	 */
	public abstract String getTaxon();

	/**
	 * Does the sample have an associated BoxID?
	 * 
	 * @return true if getBoxID() returns not null
	 */
	public boolean hasBoxID();
	
	/**
	 * Get the Box ID of the associated Sample
	 * 
	 * @return the box ID from sample's corina.boxID (a uuid String), or null
	 */
	public String getBoxID();
}
