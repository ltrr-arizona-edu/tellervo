package edu.cornell.dendro.corina.sample;

import org.tridas.schema.Certainty;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasSapwood;
import org.tridas.schema.TridasWoodCompleteness;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;

public class SampleMetadata extends BaseSampleMetadata implements CorinaMetadata {
	@SuppressWarnings("unused")
	private Sample s;
	
	private TridasObject object;
	private TridasElement element;
	private TridasSample sample;
	private TridasRadius radius;
	
	public SampleMetadata(Sample s) {
		super(s);
		
		this.s = s;
		
		object = s.getMeta(Metadata.OBJECT, TridasObject.class);
		element = s.getMeta(Metadata.ELEMENT, TridasElement.class);
		sample = s.getMeta(Metadata.SAMPLE, TridasSample.class);
		radius = s.getMeta(Metadata.RADIUS, TridasRadius.class);
	}

	/**
	 * Convenience method
	 * 
	 * @param value
	 * @return true if value is not null
	 */
	private final boolean have(Object value) {
		return value != null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getWoodCompleteness()
	 */
	@Override
	public final TridasWoodCompleteness getWoodCompleteness() {
		if(series instanceof TridasMeasurementSeries) {
			TridasMeasurementSeries mseries = (TridasMeasurementSeries) series;

			// check for woodcompleteness here, return it
			if(mseries.isSetWoodCompleteness())
				return mseries.getWoodCompleteness();
		}
		
		// get the woodCompleteness from the radius, if it exists
		return have(radius) ? radius.getWoodCompleteness() : null;
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#hasSapwood()
	 */
	@Override
	public final boolean hasSapwood() {
		TridasWoodCompleteness woodCompleteness = getWoodCompleteness();
		
		if(!have(woodCompleteness))
			return false;
		
		TridasSapwood sapwood = woodCompleteness.getSapwood();
		
		if(!have(sapwood))
			return false;
		
		switch(sapwood.getPresence()) {
		case COMPLETE:
		case INCOMPLETE:
			return true;

		default:
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getNumberOfSapwoodRings()
	 */
	@Override
	public final Integer getNumberOfSapwoodRings() {
		TridasWoodCompleteness woodCompleteness = getWoodCompleteness();
		
		if(!have(woodCompleteness))
			return 0;
		
		TridasSapwood sapwood = woodCompleteness.getSapwood();
		
		if(!have(sapwood))
			return 0;
		
		return sapwood.getNrOfSapwoodRings();
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getDatingCertainty()
	 */
	@Override
	public final Certainty getDatingCertainty() {
		if(!series.isSetInterpretation() || !series.getInterpretation().isSetFirstYear() ||
				!series.getInterpretation().getFirstYear().isSetCertainty())
			
			return Certainty.UNKNOWN;
		
		return series.getInterpretation().getFirstYear().getCertainty();
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getSiteCode()
	 */
	@Override
	public final String getSiteCode() {		
		return hasSiteCode() ? ((TridasObjectEx)object).getLabCode() : null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#hasSiteCode()
	 */
	@Override
	public final boolean hasSiteCode() {
		return have(object) && (object instanceof TridasObjectEx) && ((TridasObjectEx)object).hasLabCode();
	}

	@Override
	public String getTaxon() {
		ControlledVoc voc;
		if(have(element) && (voc = element.getTaxon()) != null) {
			if(voc.isSetNormal())
				return voc.getNormal();
			else if(voc.isSetValue() && voc.getValue().length() > 0)
				return voc.getValue();
		}
		
		return null;
	}
	
	@Override
	public String getBoxID() {
		if(have(sample)) {
			TridasGenericField field = GenericFieldUtils.findField(sample, "corina.boxID");
			if(field != null)
				return field.getValue();
		}
		return null;
	}

	@Override
	public boolean hasBoxID() {
		return getBoxID() != null;
	}
}
