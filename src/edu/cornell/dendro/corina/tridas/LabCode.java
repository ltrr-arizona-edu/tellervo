package edu.cornell.dendro.corina.tridas;

import java.util.ArrayList;
import java.util.List;

public class LabCode {
	private List<String> siteCodes;
	private List<String> siteTitles;
	private String elementCode;
	private String sampleCode;
	private String radiusCode;
	private String seriesCode;
	
	public LabCode() {
		siteCodes = new ArrayList<String>(1);
		siteTitles = new ArrayList<String>(1);
		elementCode = sampleCode = radiusCode = seriesCode = null;
	}
	
	/**
	 * @return true if this code is empty (not counting series code)
	 */
	public boolean isEmptyCode() {
		return siteCodes.isEmpty() && elementCode == null && sampleCode == null && radiusCode == null;
	}

	/**
	 * @return the siteCodes
	 */
	public List<String> getSiteCodes() {
		return siteCodes;
	}

	/**
	 * @return the siteTitles
	 */
	public List<String> getSiteTitles() {
		return siteTitles;
	}

	/** 
	 * Clear the list of site codes
	 */
	public void clearSites() {
		this.siteCodes.clear();
		this.siteTitles.clear();
	}

	/**
	 * @param siteCode a siteCode to append
	 */
	public void appendSiteCode(String siteCode) {
		this.siteCodes.add(siteCode);
	}

	/**
	 * @param siteTitle a siteTitle to append
	 */
	public void appendSiteTitle(String siteTitle) {
		this.siteTitles.add(siteTitle);
	}

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		if(elementCode.length() == 0)
			elementCode = null;
		
		this.elementCode = elementCode;
	}

	/**
	 * @return the sampleCode
	 */
	public String getSampleCode() {
		return sampleCode;
	}

	/**
	 * @param sampleCode the sampleCode to set
	 */
	public void setSampleCode(String sampleCode) {
		if(sampleCode.length() == 0)
			sampleCode = null;
		
		this.sampleCode = sampleCode;
	}

	/**
	 * @return the radiusCode
	 */
	public String getRadiusCode() {
		return radiusCode;
	}

	/**
	 * @param radiusCode the radiusCode to set
	 */
	public void setRadiusCode(String radiusCode) {
		if(radiusCode.length() == 0)
			radiusCode = null;
		
		this.radiusCode = radiusCode;
	}

	/**
	 * @return the seriesCode
	 */
	public String getSeriesCode() {
		return seriesCode;
	}

	/**
	 * @param seriesCode the seriesCode to set
	 */
	public void setSeriesCode(String seriesCode) {
		if(seriesCode.length() == 0)
			seriesCode = null;
		
		this.seriesCode = seriesCode;
	}
		
}
