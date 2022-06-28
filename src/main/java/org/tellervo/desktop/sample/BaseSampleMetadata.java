/**
 * 
 */
package org.tellervo.desktop.sample;

import java.util.List;

import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.Certainty;
import org.tridas.schema.TridasWoodCompleteness;


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
	 * @see org.tellervo.desktop.sample.CorinaMetadata#getDatingCertainty()
	 */
	public Certainty getDatingCertainty() {
		return Certainty.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#getName()
	 */
	public String getName() {
		return series.isSetTitle() ? series.getTitle() : 
			(series.isSetIdentifier() ? series.getIdentifier().getValue() : (series.getClass().getName() + "@" + series.hashCode()));
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#getNumberOfSapwoodRings()
	 */
	public Integer getNumberOfSapwoodRings() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#getSiteCode()
	 */
	public String getSiteCode() {
		LabCode labcode = bs.getMeta(Metadata.LABCODE, LabCode.class);
		List<String> siteCodes;
		
		if(labcode == null || (siteCodes = labcode.getSiteCodes()).size() == 0)
			return null;
		
		return siteCodes.get(siteCodes.size() - 1);
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#hasSiteCode()
	 */
	public boolean hasSiteCode() {
		return getSiteCode() != null;
	}
	
	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#getWoodCompleteness()
	 */
	public TridasWoodCompleteness getWoodCompleteness() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#hasSapwood()
	 */
	public boolean hasSapwood() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.tellervo.desktop.sample.CorinaMetadata#getTaxon()
	 */
	public String getTaxon() {
		return bs.getMetaString(Metadata.SUMMARY_MUTUAL_TAXON);
	}

	public String getBoxID() {
		return null;
	}

	public boolean hasBoxID() {
		return false;
	}
}
