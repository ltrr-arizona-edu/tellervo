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
package org.tellervo.desktop.io;

/**
 * This file contains two important things:
 * 
 * 1) The list of known fields for a Sample's metadata, in String format
 * 2) A mapping from field name to representing field class, via getDataType
 * 3) A mapping from tridas name to metadata name
 */

public class Metadata {
	/** The display title */
	public final static String TITLE = "title";
	/** The filename (mostly obselete, irrelevant for "web" files, etc */
	public final static String FILENAME = "filename";
	
	/** If 'Tellervo-lite' is being used then this stores the format the file is saved as */
	public final static String LEGACY_FORMAT = "format";
	public final static String LEGACY_SAVED_BY_TELLERVO = "savedByTellervo";
	public final static String KEYCODE = "keycode";
	public final static String AUTHOR = "author";
	public final static String SPECIES = "species";
	
	/* Timestamps */
	public final static String CREATED_TIMESTAMP = "createdTimestamp";
	public final static String MODIFIED_TIMESTAMP = "modifiedTimestamp";
	
	/** Derived series version */
	public static final String VERSION = "version";
	
	/** Number of child measurements */
	public static final String CHILD_COUNT = "childCount";

	/** The server says this is a "legacy cleaned" sample
	 * FIXME: What is that, exactly?
	 */
	public final static String LEGACY_CLEANED = "legacyCleaned";
	
	/** The TridasIdentifier associated with this Object */
	public final static String TRIDAS_IDENTIFIER = "::tridasID";
	
	/** A LabCode object */
	public final static String LABCODE = "::labcode";
	
	/** The units */
	public final static String UNITS = "::units";

	/** boolean: is reconciled? */
	public final static String RECONCILED = "::reconciled";
	
	/** Summary stuff */
	public final static String SUMMARY_SUM_CONSTITUENT_COUNT = "::summary:seriesCount";
	public final static String SUMMARY_MUTUAL_TAXON = "::summary:mututalTaxon";
	public final static String SUMMARY_MUTUAL_TAXON_COUNT = "::summary:mututalTaxonCount";
	
	/** The associated other object types */
	public final static String PROJECT = "::project";
	public final static String OBJECT_ARRAY = "::objectArray";
	public final static String OBJECT = "::object";
	public final static String ELEMENT = "::element";
	public final static String SAMPLE = "::sample";
	public final static String RADIUS = "::radius";

	public final static String BOX = "::box";
}
