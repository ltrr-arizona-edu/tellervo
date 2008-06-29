package edu.cornell.dendro.corina.sample;

/**
 * A simple class to hold summary information about a sample
 * Comes from the summary format of searching for measurements in webdb
 * @author Lucas Madar
 *
 */
public class SampleSummary {
	public SampleSummary() {	
	}
	
	private String labCode;
	private int taxonCount;
	private String commonTaxon;
	private int siteCount;
	private String siteCode;
	private int measurementCount;
	
	// all getters
	public String getLabCode() {
		return labCode;
	}
	public int getTaxonCount() {
		return taxonCount;
	}
	public String getCommonTaxon() {
		return commonTaxon;
	}
	public int getSiteCount() {
		return siteCount;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public int getMeasurementCount() {
		return measurementCount;
	}
	
	
	public SampleSummary setLabCode(String labCode) {
		this.labCode = labCode;
		return this;
	}
	public SampleSummary setTaxonCount(int taxonCount) {
		this.taxonCount = taxonCount;
		return this;
	}
	public SampleSummary setCommonTaxon(String commonTaxon) {
		this.commonTaxon = commonTaxon;
		return this;
	}
	public SampleSummary setSiteCount(int siteCount) {
		this.siteCount = siteCount;
		return this;
	}
	public SampleSummary setSiteCode(String siteCode) {
		this.siteCode = siteCode;
		return this;
	}	
	public SampleSummary setMeasurementCount(int measurementCount) {
		this.measurementCount = measurementCount;
		return this;
	}	

	public String siteDescription() {
		if(siteCount > 1) 
			return "[" + siteCount + " sites]";

		return siteCode;
	}
	
	public String taxonDescription() {
		if(taxonCount > 1)
			return "[" + taxonCount + " taxa, common " + commonTaxon + "]";
		return commonTaxon;
	}
	
	/**
	 * Parses a summary element
	 * 
	 * @param summary
	 * @return
	 */
	public static SampleSummary fromXML(org.jdom.Element summary) {
		SampleSummary ss = new SampleSummary();
		org.jdom.Element e;

		ss.setLabCode(summary.getChildTextNormalize("fullLabCode"));

		if((e = summary.getChild("taxon")) != null) {
			ss.setTaxonCount(Integer.valueOf(e.getAttributeValue("count")))
			  .setCommonTaxon(e.getAttributeValue("commonAncestor"));
		}

		if((e = summary.getChild("site")) != null) {
			ss.setSiteCount(Integer.valueOf(e.getAttributeValue("count")))
			  .setSiteCode(e.getAttributeValue("siteCode"));
		}
		
		if((e = summary.getChild("measurement")) != null) {
			ss.setMeasurementCount(Integer.valueOf(e.getAttributeValue("count")));
		}

		return ss;
	}
}
