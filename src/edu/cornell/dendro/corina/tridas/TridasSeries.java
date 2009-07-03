/**
 * 
 */
package edu.cornell.dendro.corina.tridas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.formats.InvalidDataException;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.webdbi.CorinaXML;

/**
 * @author lucasm
 *
 */
public class TridasSeries extends TridasEntityBase {
	private final static String RING_WIDTH_DATA = "tridas/Ring width";
	private final static String WEISERJAHRE_DATA = "Corina/weiserjahre";
	
	// wj constants
	private final static String WJINC = "inc";
	private final static String WJDEC = "dec";
	
	// ring width constants
	private final static String WIDTHS = "ringWidth";
	private final static String COUNTS = "count";
	
	// everything else
	private final static String RAW = "RAW";
	
	/**
	 * A list of values associated with this series
	 */
	private List<TridasValues> values = new ArrayList<TridasValues>(2);
	
	/**
	 * @param identifier
	 * @param title
	 */
	public TridasSeries(TridasIdentifier identifier, String title) {
		super(identifier, title);		
	}

	/**
	 * @param entityType
	 * @param identifier
	 * @param title
	 */
	public TridasSeries(String entityType, Element identifier, String title) {
		super(entityType, identifier, title);
	}

	/**
	 * @param rootElement
	 */
	public TridasSeries(Element rootElement) throws InvalidDataException {
		super(rootElement);
		
		loadValues(rootElement);
	}
	
	/**
	 * Are there any associated measurements with this sample?
	 * (really, just checks if we have RING_WIDTH_DATA)
	 * @return
	 */
	public boolean hasMeasurements() {
		return findTridasValuesByName(RING_WIDTH_DATA) != null;
	}

	/**
	 * Copy the associated measurements into the Corina Sample
	 * 
	 * @param s
	 */
	@SuppressWarnings("unchecked")
	public void copyMeasurementsOntoSample(Sample s) {
		TridasValues v;
		
		// copy ring width data
		v = findTridasValuesByName(RING_WIDTH_DATA);
		for(TridasValuesList vlist : v.valuesList) {
			/*
			if(vlist.name.equals(WIDTHS))
				s.setData(vlist.values);
				
			else if(vlist.name.equals(COUNTS))
				s.setCount((List<Integer>) (List) vlist.values);  // thanks, java generics! :P (type erasure!)
				*/
		}
		
		// copy wj data
		if((v = findTridasValuesByName(WEISERJAHRE_DATA)) != null) {
			for(TridasValuesList vlist : v.valuesList) {
				if(vlist.name.equals(WJINC))
					s.setWJIncr((List<Integer>)(List) vlist.values);
				else if(vlist.name.equals(WJDEC))
					s.setWJDecr((List<Integer>)(List) vlist.values);
			}
		}
	}
	
	/**
	 * Map data from this onto a corina base sample
	 */
	@Override
	public void mapOntoSample(BaseSample s) {
		String value;
		
		// I share the same identifier...
		s.setMeta(Metadata.TRIDAS_IDENTIFIER, identifier);
		
		if(getTridasEntityName().equals("measurementSeries"))
			s.setSampleType(SampleType.DIRECT);
		else
			s.setSampleType(SampleType.fromString(getMetadataString("series.type")));
		
		/**
		 * Try labcode from "summary" data first
		 */
		LabCode labCode = new LabCode();
		
		for(int i = 1;;i++) {
			if((value = getMetadataString("series.generic.corina.objectCode." + i)) != null)	
				labCode.appendSiteCode(value);
			else
				break;
		}
		
		if((value = getMetadataString("series.generic.corina.elementTitle")) != null)
			labCode.setElementCode(value);
		if((value = getMetadataString("series.generic.corina.sampleTitle")) != null)
			labCode.setSampleCode(value);
		if((value = getMetadataString("series.generic.corina.radiusTitle")) != null)
			labCode.setRadiusCode(value);
		if((value = getMetadataString("series.title")) != null)
			labCode.setSeriesCode(value);

		// add the labcode to the sample
		s.setMeta(Metadata.LABCODE, labCode);

		/**
		 * Acquire startyear and convert dating to corina-internal
		 */
		
		Integer startYear = (Integer) getMetadata("series.interpretation.firstYear");
		
		// This shouldn't ever be null. But if it is, let's be unreasonable so it shows up.
		if(startYear == null)
			startYear = -9999; // 10,000 BC! 

		// handle AD/BC
		String yearType = getMetadataString("series.interpretation.firstYear.@suffix");
		if(yearType != null) {
			if(yearType.equalsIgnoreCase("BC")) {
				startYear = 1 - startYear;
			}
			else if(yearType.equalsIgnoreCase("AD")) {
				// do nothing
			}
			else {
				throw new IllegalArgumentException("Unknown year suffix type: " + yearType);
			}
		}

		/**
		 * Handle range
		 */

		TridasValues ringWidthData = findTridasValuesByName(RING_WIDTH_DATA);
		if(ringWidthData != null) {
			// anything in ring width data should suffice for a count
			Integer count = ringWidthData.valuesList.get(0).values.size();

			s.setRange(new Range(new Year(startYear), count));
		}
		else {
			Integer count = (Integer) getMetadata("series.generic.corina.readingCount");
			if(count != null)
				s.setRange(new Range(new Year(startYear), count));
		}

		/**
		 * Now, handle "final" things like labcode
		 */
		
		s.setMeta("title", LabCodeFormatter.getDefaultFormatter().format(labCode));
	}

	/**
	 * Internal class for representing the list of lists of values
	 * @author lucasm
	 *
	 */
	private static class TridasValues {
		private String name;
		private TridasUnits units;
		private List<TridasValuesList> valuesList;
	}

	/**
	 * Internal class for representing a list of values
	 * @author lucasm
	 *
	 */
	private static class TridasValuesList {
		private String name;
		private List<Object> values;
		
		public TridasValuesList(String name, int sz) {
			this.name = name;
			this.values = new ArrayList<Object>(sz);
		}
	}
	
	private void loadValues(List<Element> valuesIn, TridasValues tvalues) throws InvalidDataException {
		if(tvalues.name.equalsIgnoreCase(RING_WIDTH_DATA)) {
			TridasValuesList ringValuesList = new TridasValuesList(WIDTHS, valuesIn.size());
			TridasValuesList countList = new TridasValuesList(COUNTS, valuesIn.size());
			
			// make these a list:
			tvalues.valuesList = Arrays.asList(new TridasValuesList[] { ringValuesList, countList });
			
			for(Element v : valuesIn) {
				String strValue = v.getAttributeValue("value");
				String strCount = v.getAttributeValue("count");	
				
				try {
					Integer value = Integer.valueOf(strValue);			
					Integer count = (strCount != null) ? Integer.valueOf(strCount) : 1;
					
					ringValuesList.values.add(value);
					countList.values.add(count);
				} catch (NumberFormatException e) {
					throw new InvalidDataException("Bad ring width: " + v, e);
				}
				
			}
		}
		else if(tvalues.name.equalsIgnoreCase(WEISERJAHRE_DATA)) {
			TridasValuesList incList = new TridasValuesList(WJINC, valuesIn.size());
			TridasValuesList decList = new TridasValuesList(WJDEC, valuesIn.size());
			
			tvalues.valuesList = Arrays.asList(new TridasValuesList[] { incList, decList });
			
			for(Element v : valuesIn) {
				try {
					String strValue = v.getAttributeValue("value");
					int slashPos;
					
					if(strValue == null || (slashPos = strValue.indexOf('/')) < 1)
						throw new InvalidDataException("Invalid Weiserjahre Data Format!");
					
					Integer inc = Integer.valueOf(strValue.substring(0, slashPos));
					Integer dec = Integer.valueOf(strValue.substring(slashPos + 1));
					
					incList.values.add(inc);
					decList.values.add(dec);
				} catch (NumberFormatException nfe) {
					throw new InvalidDataException("Invalid weiserjahre data (not integers!)");
				} 
			}
			
		}
		else {
			TridasValuesList unknown = new TridasValuesList(RAW, valuesIn.size());
			tvalues.valuesList = Collections.singletonList(unknown);
			
			for(Element v : valuesIn) 
				unknown.values.add(v.getAttributeValue("value"));
		}
	}
	
	private void loadValues(Element root) throws InvalidDataException {
		List<Element> valuesElements = (List<Element>) root.getChildren("values", CorinaXML.TRIDAS_NS);
		
		for(Element valuesElement : valuesElements) {
			TridasValues v = new TridasValues();
			
			// get a list of actual values
			List<Element> values = (List<Element>) valuesElement.getChildren("value", CorinaXML.TRIDAS_NS);
			// the 'variable' tag
			Element variable = valuesElement.getChild("variable", CorinaXML.TRIDAS_NS);
			// the 'unit' tag
			Element units = valuesElement.getChild("unit", CorinaXML.TRIDAS_NS);
			String strval;
			
			// set name
			if(variable != null && (strval = variable.getAttributeValue("normalTridas")) != null) {
				// this is a tridas standard
				v.name = "tridas/" + strval;
			}
			else if(variable != null && (strval = variable.getAttributeValue("normalStd")) != null) {
				// this is some other kind of standard
				v.name = strval + "/";
				
				// be safe
				if((strval = variable.getAttributeValue("normal")) == null)
					continue;
				
				v.name += strval;
			}
			else {
				// shouldn't happen, so ignore this type?
				continue;
			}

			if(units != null)
				v.units = TridasUnits.getUnitsForOfficialRepresentation(units.getAttributeValue("normalTridas"));
			
			loadValues(values, v);
			
			// add it to our list of values
			this.values.add(v);
		}
	}
	
	/**
	 * Search this List for a values structure
	 * 
	 * @param name
	 * @return
	 */
	private TridasValues findTridasValuesByName(String name) {
		for(TridasValues v : this.values)
			if(v.name.equalsIgnoreCase(name))
				return v;
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.tridas.TridasEntityBase#toXML()
	 */
	@Override
	public Element toXML() {
		return null;
	}

}
