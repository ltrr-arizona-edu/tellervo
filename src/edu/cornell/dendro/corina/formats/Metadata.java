package edu.cornell.dendro.corina.formats;

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
	
	/* Timestamps */
	public final static String CREATED_TIMESTAMP = "createdTimestamp";
	public final static String MODIFIED_TIMESTAMP = "modifiedTimestamp";
	
	/** Derived series version */
	public static final String VERSION = "version";

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
	public final static String OBJECT_ARRAY = "::objectArray";
	public final static String OBJECT = "::object";
	public final static String ELEMENT = "::element";
	public final static String SAMPLE = "::sample";
	public final static String RADIUS = "::radius";
	
}
