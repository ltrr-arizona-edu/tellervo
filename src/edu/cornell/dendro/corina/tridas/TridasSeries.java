/**
 * 
 */
package edu.cornell.dendro.corina.tridas;

import org.jdom.Element;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;

/**
 * @author lucasm
 *
 */
public class TridasSeries extends TridasEntityBase {
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
	public TridasSeries(Element rootElement) {
		super(rootElement);
	}
	
	@Override
	public void mapOntoSample(BaseSample s) {
		String value;
		
		// I share the same identifier...
		s.setMeta(Metadata.TRIDAS_IDENTIFIER, identifier);
		
		if(getTridasEntityName().equals("measurementSeries"))
			s.setSampleType(SampleType.DIRECT);
		
		/**
		 * Try labcode from "summary" data first
		 */
		LabCode labCode = new LabCode();
		
		for(int i = 1;;i++) {
			if((value = (String) getMetadata("series.generic.corina.objectCode." + i)) != null)	
				labCode.appendSiteCode(value);
			else
				break;
		}
		
		if((value = (String) getMetadata("series.generic.corina.elementTitle")) != null)
			labCode.setElementCode(value);
		if((value = (String) getMetadata("series.generic.corina.sampleTitle")) != null)
			labCode.setSampleCode(value);
		if((value = (String) getMetadata("series.generic.corina.radiusTitle")) != null)
			labCode.setRadiusCode(value);
		if((value = (String) getMetadata("series.title")) != null)
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
		String yearType = (String) getMetadata("series.interpretation.firstYear.@suffix");
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
		
		if(false) {
			// handle readings-derived stuff here
			Integer count = 0; // FIXME: Count number of ring-stuffs!
			
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

		
		if(s instanceof Sample) {
			// copy over readings!
		}
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.tridas.TridasEntityBase#toXML()
	 */
	@Override
	public Element toXML() {
		return null;
	}

}
