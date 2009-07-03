/**
 * 
 */
package edu.cornell.dendro.corina.sample;

import java.util.List;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.Certainty;
import org.tridas.schema.TridasWoodCompleteness;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.tridas.LabCode;

/**
 * @author Lucas Madar
 *
 */
public class BaseSampleMetadata implements CorinaMetadata {
	private BaseSample bs;
	protected ITridasSeries series;
	
	public BaseSampleMetadata(BaseSample bs) {
		this.bs = bs;
		
		this.series = bs.getSeries();
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getDatingCertainty()
	 */
	public Certainty getDatingCertainty() {
		return Certainty.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getName()
	 */
	public String getName() {
		return series.isSetTitle() ? series.getTitle() : 
			(series.isSetIdentifier() ? series.getIdentifier().getValue() : (series.getClass().getName() + "@" + series.hashCode()));
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getNumberOfSapwoodRings()
	 */
	public Integer getNumberOfSapwoodRings() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getSiteCode()
	 */
	public String getSiteCode() {
		LabCode labcode = bs.getMeta(Metadata.LABCODE, LabCode.class);
		List<String> siteCodes;
		
		if(labcode == null || (siteCodes = labcode.getSiteCodes()).size() == 0)
			return null;
		
		return siteCodes.get(siteCodes.size() - 1);
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#hasSiteCode()
	 */
	public boolean hasSiteCode() {
		return getSiteCode() != null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getWoodCompleteness()
	 */
	public TridasWoodCompleteness getWoodCompleteness() {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#hasSapwood()
	 */
	public boolean hasSapwood() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.sample.CorinaMetadata#getTaxon()
	 */
	public String getTaxon() {
		return bs.getMetaString(Metadata.SUMMARY_MUTUAL_TAXON);
	}
}
