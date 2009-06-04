package edu.cornell.dendro.corina.formats;

import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;

import edu.cornell.dendro.corina.tridas.TridasIdentifier;

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
	/** The name of the sample. Usually something like 'A', 'C', etc */
	public final static String NAME = "name";
	
	/* Timestamps */
	public final static String CREATED_TIMESTAMP = "createdTimestamp";
	public final static String MODIFIED_TIMESTAMP = "modifiedTimestamp";
	
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
	
	/** The associated other object types */
	public final static String OBJECT_ARRAY = "::objectArray";
	public final static String OBJECT = "::object";
	public final static String ELEMENT = "::element";
	public final static String SAMPLE = "::sample";
	public final static String RADIUS = "::radius";
	public final static String SERIES = "::series";
	
	/** Measuring method */
	public final static String MEASURING_METHOD = "::measuringMethod";
	
	/**
	 * Get the class type of the corresponding metadata
	 * Defaults to String.class!
	 * @param tag
	 * @return
	 */
	public static Class<?> getDataType(String tag) {
		Class<?> clazz = dataTypeMap.get(tag);
		
		return (clazz == null) ? String.class : clazz;
	}
	
	/**
	 * Returns a tridas tag, or null if we don't have a mapping
	 * 
	 * @param tridas
	 * @return
	 */
	public static String getMetaTagFromTridas(String tridas) {
		return tridasToMetaMap.get(tridas);
	}
		
	private static Map<String, Class<?>> dataTypeMap = new HashMap<String, Class<?>>();
	private static Map<String, String> tridasToMetaMap = new HashMap<String, String>();
	private static Map<String, String> metaToTridasMap = new HashMap<String, String>();
	static {
		// The data type map. Defaults to String.class, so be careful!
		addType(TRIDAS_IDENTIFIER, TridasIdentifier.class);	
		addType(LEGACY_CLEANED, Boolean.class);

		// now, add mappings.
		addMapping(Tridas.TITLE, Metadata.NAME);
	}
	
	/**
	 * Add a mapping type
	 * 
	 * @param meta
	 * @param clazz
	 */
	private static void addType(String meta, Class<?> clazz) {
		dataTypeMap.put(meta, clazz);
	}
	
	/**
	 * Add a mapping from tridas -> meta and vice versa
	 * @param tridas
	 * @param meta
	 */
	private static void addMapping(String tridas, String meta) {
		tridasToMetaMap.put(tridas, meta);
		metaToTridasMap.put(meta, tridas);
	}
	
	/**
	 * Maps a tridas element to some sort of metadata
	 * Returns null if no mapping exists for the tag
	 * note: tridasTag is a hierarchical tag name, eg interpretation.firstYear
	 * 
	 * @param e
	 * @param tridasTag
	 * @return
	 */
	public static Mapping mapTridas(Element e, String tridasTag) {
		String tag = tridasToMetaMap.get(tridasTag);
		
		if(tag == null)
			return null;
		
		Mapping mapping = new Mapping();
		
		mapping.metaTag = tag;
		mapping.classType = getDataType(tag);
		
		String strVal = e.getText();
		
		if(mapping.classType == String.class) {
			mapping.value = strVal;
		}
		else if(mapping.classType == Boolean.class) {
			mapping.value = new Boolean(strVal);
		}
		else {
			throw new IllegalArgumentException("Type not supported!");
		}
		
		return mapping;
	}
	
	public static class Mapping {
		public String metaTag;
		public Class<?> classType;
		public Object value;
	}
}
