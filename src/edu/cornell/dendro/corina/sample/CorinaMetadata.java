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
}